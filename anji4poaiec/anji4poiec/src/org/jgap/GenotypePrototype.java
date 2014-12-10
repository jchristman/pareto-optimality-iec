/*
 * Copyright 2001-2003 Neil Rotstan Copyright (C) 2004 Derek James and Philip Tucker
 * 
 * This file is part of JGAP.
 * 
 * JGAP is free software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser Public License as published by the Free Software Foundation; either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * JGAP is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser Public License along with JGAP; if not,
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 * 
 * Modified on Feb 3, 2003 by Philip Tucker
 */
package org.jgap;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

import org.jgap.event.GeneticEvent;

/**
 * Genotypes are fixed-length populations of chromosomes. As an instance of a
 * <code>Genotype</code> is evolved, all of its
 * <code>Chromosome</code> objects are also evolved. A
 * <code>Genotype</code> may be constructed normally, whereby an array of
 * <code>Chromosome</code> objects must be provided, or the static
 * <code>randomInitialGenotype()</code> method can be used to generate a
 * <code>Genotype</code> with a randomized
 * <code>Chromosome</code> population. Changes made by Tucker and James for <a
 * href="http://anji.sourceforge.net/">ANJI </a>: <ul> <li>added species</li>
 * <li>modified order of operations in
 * <code>evolveForFitness()</code></li> <li>added
 * <code>addChromosome*()</code> methods</li> </ul>
 */
@SuppressWarnings("serial")
public class GenotypePrototype extends ProgressSource implements Serializable {

    private Population iecPopulation = null;
    @SuppressWarnings("unused")
	private Population fitnessPopulation = null;
    @SuppressWarnings("unused")
	private Population noveltyPopulation = null;
    
    @SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(GenotypePrototype.class.getName());
    /**
     * The current active Configuration instance.
     */
    protected Configuration m_activeConfiguration;
    /**
     * Chromosomes that makeup this Genotype's population.
     */
    @SuppressWarnings("unused")
	private float iecMutationRate = 0.1f;
    @SuppressWarnings("unused")
	private float iecMutationPower = 0.1f;

    public synchronized void setIECmutationRate(float aMutationRate) {
        iecMutationRate = Math.min(Math.max(aMutationRate, 0.0f), 1.0f);
    }

    public synchronized void setIECmutationPower(float aMutationPower) {
        iecMutationPower = Math.max(aMutationPower, 0.0f);
    }
    @SuppressWarnings("unused")
	private float fitnessMutationRate = 0.1f;
    @SuppressWarnings("unused")
	private float fitnessMutationPower = 0.1f;

    public synchronized void setFitnessMutationRate(float aMutationRate) {
        fitnessMutationRate = Math.min(Math.max(aMutationRate, 0.0f), 1.0f);
    }

    public synchronized void setFitnessMutationPower(float aMutationPower) {
        fitnessMutationPower = Math.max(aMutationPower, 0.0f);
    }
    private ReadWriteLock genotypeLock = new ReentrantReadWriteLock();
    private Lock readLock = genotypeLock.readLock();
    private Lock writeLock = genotypeLock.writeLock();

    /**
     * This constructor is used for random initial Genotypes. Note that the
     * Configuration object must be in a valid state when this method is
     * invoked, or a InvalidconfigurationException will be thrown.
     *
     * @param a_activeConfiguration The current active Configuration object.
     * @param a_initialChromosomes
     * <code>List</code> contains Chromosome objects: The Chromosome population
     * to be managed by this Genotype instance.
     * @throws IllegalArgumentException if either the given Configuration object
     * or the array of Chromosomes is null, or if any of the Genes in the array
     * of Chromosomes is null.
     * @throws InvalidConfigurationException if the given Configuration object
     * is in an invalid state.
     */
    public GenotypePrototype(Configuration a_activeConfiguration, List<ChromosomeMaterial> a_initialChromosomes)
            throws InvalidConfigurationException {
        // Sanity checks: Make sure neither the Configuration, the array
        // of Chromosomes, nor any of the Genes inside the array are null.
        // ---------------------------------------------------------------
        if (a_activeConfiguration == null) {
            throw new IllegalArgumentException("The Configuration instance may not be null.");
        }

        if (a_initialChromosomes == null) {
            throw new IllegalArgumentException("The array of Chromosomes may not be null.");
        }

        for (int i = 0; i < a_initialChromosomes.size(); i++) {
            if (a_initialChromosomes.get(i) == null) {
                throw new IllegalArgumentException("The Chromosome instance at index " + i
                        + " of the array of " + "Chromosomes is null. No instance in this array may be null.");
            }
        }

        // Lock the settings of the Configuration object so that the cannot
        // be altered.
        // ----------------------------------------------------------------
        a_activeConfiguration.lockSettings();
        m_activeConfiguration = a_activeConfiguration;

        writeLock.lock();
        try {
            iecPopulation = new Population(m_activeConfiguration);
            noveltyPopulation = new Population(m_activeConfiguration);
            fitnessPopulation = new Population(m_activeConfiguration);
            
            iecPopulation.addChromosomesFromMaterial(a_initialChromosomes);
        } finally {
            writeLock.unlock();
        }
    }

    private boolean isStopped() {
        if (task == null) {
            return true;
        } else {
            return !task.isAlive();
        }
    }
    private volatile boolean userRequestedStop = false;

    public synchronized void cancel() {
        userRequestedStop = true;
    }

    @SuppressWarnings("unused")
	private synchronized boolean userRequestedStop() {
        return userRequestedStop;
    }

    private synchronized void resetUserRequestedStop() {
        userRequestedStop = false;
    }

    /**
     * @return List contains Chromosome objects, the population of Chromosomes.
     */
    public synchronized List<Chromosome> getChromosomes() {
        readLock.lock();
        try {
            return iecPopulation.getChromosomes();
        } finally {
            readLock.unlock();
        }
    }

    public void setChromosomes(List<Chromosome> aNewChromosomeSet) {
        writeLock.lock();
        try {
            iecPopulation.setChromosomes(aNewChromosomeSet);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * @return the number of chromosomes in this genotype
     */
    public int getGenotypeSize() {
        readLock.lock();
        try {
            return iecPopulation.getPopulationSize();
        } finally {
            readLock.unlock();
        }
    }

    /**
     * @return List contains Specie objects
     */
    public synchronized List<Specie> getSpecies() {
        readLock.lock();
        try {
            return iecPopulation.getSpecies();
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Retrieves the Chromosome in the population with the highest fitness
     * value.
     *
     * @return The Chromosome with the highest fitness value, or null if there
     * are no chromosomes in this Genotype.
     */
    public synchronized Chromosome getFittestChromosome() {
        readLock.lock();
        try {
            return iecPopulation.getFittestChromosome();

        } finally {
            readLock.unlock();
        }
    }

    /**
     * Retrieves the Chromosome in the population with the highest fitness
     * value.
     *
     * @return The Chromosome with the highest fitness value, or null if there
     * are no chromosomes in this Genotype.
     */
    public synchronized Chromosome getMostNovelChromosome() {
        readLock.lock();
        try {
            return iecPopulation.getMostNovelChromosome();

        } finally {
            readLock.unlock();
        }
    }

    /**
     * Performs one generation cycle, evaluating fitness, selecting survivors,
     * repopulting with offspring, and mutating new population. This is a
     * modified version of original JGAP method which changes order of
     * operations and splits
     * <code>GeneticOperator</code> into
     * <code>ReproductionOperator</code> and
     * <code>MutationOperator</code>. New order of operations: <ol> <li>assign
     * <b>fitness </b> to all members of population with
     * <code>EvaluationFunction</code> or
     * <code>FitnessFunction</code></li> <li><b>select </b> survivors and remove
     * casualties from population</li> <li>re-fill population with offspring via
     * <b>reproduction </b> operators</li> <li><b>mutate </b> offspring (note,
     * survivors are passed on un-mutated)</li> </ol> Genetic event
     * <code>GeneticEvent.GENOTYPE_EVALUATED_EVENT</code> is fired between steps
     * 2 and 3. Genetic event
     * <code>GeneticEvent.GENOTYPE_EVOLVED_EVENT</code> is fired after step 4.
     */
//    public synchronized void evolveForFitness() {
//        writeLock.lock();
//        try {
//            m_activeConfiguration.lockSettings();
//
//            // If a bulk fitness function has been provided, then convert the
//            // working pool to an array and pass it to the bulk fitness
//            // function so that it can evaluateAllChromosomes and assign fitness values to
//            // each of the Chromosomes.
//            // --------------------------------------------------------------
//            evaluateForFitness();
//
//            if (m_activeConfiguration.isSteadyState()) {
//                for (int i = 0; i < m_activeConfiguration.getFitnessSearchPopulationSize(); i++) {
//                    replaceLeastFitIndividual();
//                    notifyPopulationListeners(iecPopulation);
//                }
//            } else {
//
//                // Select chromosomes to survive.
//                // ------------------------------------------------------------
//                FitnessSelector selector = m_activeConfiguration.getFitnessSelector();
//                selector.add(m_activeConfiguration, iecPopulation);
//                iecPopulation = selector.select(m_activeConfiguration);
//                selector.empty();
//
//                // Repopulate the population of species and chromosomes with those selected
//                // by the natural selector, and cull species down to contain only remaining
//                // chromosomes.
//                updateSpecies();
//
//                // Execute Reproduction Operators.
//                // -------------------------------------
//                Iterator<FitnessReproductionOperator> iterator = m_activeConfiguration.getFitnessReproductionOperators().iterator();
//                List<ChromosomeMaterial> offspring = new ArrayList<ChromosomeMaterial>();
//                while (iterator.hasNext()) {
//                    FitnessReproductionOperator operator = iterator.next();
//                    operator.reproduce(m_activeConfiguration, m_species, offspring);
//                }
//
//                // Execute Mutation Operators.
//                // -------------------------------------
//                Iterator<MutationOperator> mutOpIter = m_activeConfiguration.getMutationOperators().iterator();
//                while (mutOpIter.hasNext()) {
//                    MutationOperator operator = mutOpIter.next();
//                    operator.mutate(m_activeConfiguration, offspring);
//                }
//
//                // in case we're off due to rounding errors
//                Collections.shuffle(offspring, m_activeConfiguration.getRandomGenerator());
//                adjustChromosomeList(offspring, m_activeConfiguration.getFitnessSearchPopulationSize() - iecPopulation.size());
//
//                // add offspring
//                // ------------------------------
//                addChromosomesFromMaterial(offspring);
//
//            }
//
//            // Fire an event to indicate we've evaluated all chromosomes.
//            // -------------------------------------------------------
//            m_activeConfiguration.getEventManager().fireGeneticEvent(
//                    new GeneticEvent(GeneticEvent.GENOTYPE_EVALUATED_EVENT, this));
//
//            // Fire an event to indicate we're starting genetic operators. Among
//            // other things this allows for RAM conservation.
//            // -------------------------------------------------------
//            m_activeConfiguration.getEventManager().fireGeneticEvent(
//                    new GeneticEvent(GeneticEvent.GENOTYPE_START_GENETIC_OPERATORS_EVENT, this));
//
//            // Fire an event to indicate we're starting genetic operators. Among
//            // other things this allows for RAM conservation.
//            // -------------------------------------------------------
//            m_activeConfiguration.getEventManager().fireGeneticEvent(
//                    new GeneticEvent(GeneticEvent.GENOTYPE_FINISH_GENETIC_OPERATORS_EVENT, this));
//
//            // Fire an event to indicate we've performed an evolution.
//            // -------------------------------------------------------
//            m_activeConfiguration.getEventManager().fireGeneticEvent(
//                    new GeneticEvent(GeneticEvent.GENOTYPE_EVOLVED_EVENT, this));
//
//        } catch (InvalidConfigurationException ex) {
//            Logger.getLogger(GenotypePrototype.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            writeLock.unlock();
//        }
//    }

    /**
     * Performs one generation cycle, evaluating fitness, selecting survivors,
     * repopulting with offspring, and mutating new population. This is a
     * modified version of original JGAP method which changes order of
     * operations and splits
     * <code>GeneticOperator</code> into
     * <code>ReproductionOperator</code> and
     * <code>MutationOperator</code>. New order of operations: <ol> <li>assign
     * <b>fitness </b> to all members of population with
     * <code>EvaluationFunction</code> or
     * <code>FitnessFunction</code></li> <li><b>select </b> survivors and remove
     * casualties from population</li> <li>re-fill population with offspring via
     * <b>reproduction </b> operators</li> <li><b>mutate </b> offspring (note,
     * survivors are passed on un-mutated)</li> </ol> Genetic event
     * <code>GeneticEvent.GENOTYPE_EVALUATED_EVENT</code> is fired between steps
     * 2 and 3. Genetic event
     * <code>GeneticEvent.GENOTYPE_EVOLVED_EVENT</code> is fired after step 4.
     */
//    public synchronized void evolveForNovelty() {
//        writeLock.lock();
//        try {
//            m_activeConfiguration.lockSettings();
//
//            // If a bulk fitness function has been provided, then convert the
//            // working pool to an array and pass it to the bulk fitness
//            // function so that it can evaluateAllChromosomes and assign fitness values to
//            // each of the Chromosomes.
//            // --------------------------------------------------------------
//            evaluateForNovelty();
//
//            if (m_activeConfiguration.isSteadyState()) {
//                for (int i = 0; i < m_activeConfiguration.getNoveltySearchPopulationSize(); i++) {
//                    replaceLeastNovelIndividual();
//                    notifyPopulationListeners(iecPopulation);
//                }
//            } else {
//
//                // Select chromosomes to survive.
//                // ------------------------------------------------------------
//                NoveltySelector selector = m_activeConfiguration.getNoveltySelector();
//                selector.add(m_activeConfiguration, iecPopulation);
//                iecPopulation = selector.select(m_activeConfiguration);
//                selector.empty();
//
//                // Repopulate the population of species and chromosomes with those selected
//                // by the natural selector, and cull species down to contain only remaining
//                // chromosomes.
//                updateSpecies();
//
//                // Execute Reproduction Operators.
//                // -------------------------------------
//                Iterator<NoveltyReproductionOperator> iterator = m_activeConfiguration.getNoveltyReproductionOperators().iterator();
//                List<ChromosomeMaterial> offspring = new ArrayList<ChromosomeMaterial>();
//                while (iterator.hasNext()) {
//                    NoveltyReproductionOperator operator = iterator.next();
//                    operator.reproduce(m_activeConfiguration, m_species, offspring);
//                }
//
//                // Execute Mutation Operators.
//                // -------------------------------------
//                Iterator<MutationOperator> mutOpIter = m_activeConfiguration.getMutationOperators().iterator();
//                while (mutOpIter.hasNext()) {
//                    MutationOperator operator = mutOpIter.next();
//                    operator.mutate(m_activeConfiguration, offspring);
//                }
//
//                // in case we're off due to rounding errors
//                Collections.shuffle(offspring, m_activeConfiguration.getRandomGenerator());
//                adjustChromosomeList(offspring, m_activeConfiguration.getNoveltySearchPopulationSize()
//                        - iecPopulation.size());
//
//                // add offspring
//                // ------------------------------
//                addChromosomesFromMaterial(offspring);
//
//            }
//
//            // Fire an event to indicate we've evaluated all chromosomes.
//            // -------------------------------------------------------
//            m_activeConfiguration.getEventManager().fireGeneticEvent(
//                    new GeneticEvent(GeneticEvent.GENOTYPE_EVALUATED_EVENT, this));
//
//            // Fire an event to indicate we're starting genetic operators. Among
//            // other things this allows for RAM conservation.
//            // -------------------------------------------------------
//            m_activeConfiguration.getEventManager().fireGeneticEvent(
//                    new GeneticEvent(GeneticEvent.GENOTYPE_START_GENETIC_OPERATORS_EVENT, this));
//
//            // Fire an event to indicate we're starting genetic operators. Among
//            // other things this allows for RAM conservation.
//            // -------------------------------------------------------
//            m_activeConfiguration.getEventManager().fireGeneticEvent(
//                    new GeneticEvent(GeneticEvent.GENOTYPE_FINISH_GENETIC_OPERATORS_EVENT, this));
//
//            // Fire an event to indicate we've performed an evolution.
//            // -------------------------------------------------------
//            m_activeConfiguration.getEventManager().fireGeneticEvent(
//                    new GeneticEvent(GeneticEvent.GENOTYPE_EVOLVED_EVENT, this));
//
//        } catch (InvalidConfigurationException ex) {
//            Logger.getLogger(GenotypePrototype.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            writeLock.unlock();
//        }
//    }

//    public synchronized static Population novelInitialGenotype(Configuration a_activeConfiguration) throws InvalidConfigurationException {
//
//        if (a_activeConfiguration == null) {
//            throw new IllegalArgumentException("The Configuration instance may not be null.");
//        }
//
//        a_activeConfiguration.lockSettings();
//        List<ChromosomeMaterial> chroms = new ArrayList<ChromosomeMaterial>();
//        for (int i = 0; i < a_activeConfiguration.getNoveltySearchPopulationSize(); i++) {
//            chroms.add(ChromosomeMaterial.randomInitialChromosomeMaterial(a_activeConfiguration));
//        }
//
//        Population genotype = new Population(a_activeConfiguration);
//        genotype.addChromosomesFromMaterial(chroms);
//
////            noveltyTask();
//        genotype.evaluateForFitness();
//        genotype.evaluateForNovelty();
//
////            Collections.sort(m_chromosomes, new ChromosomeNoveltyComparator(true, false));
////            while (m_chromosomes.size() > a_activeConfiguration.getIECpopulationSize()) {
////                m_chromosomes.remove(0);
////            }
////            Collections.sort(m_chromosomes);
//
//        List<Chromosome> novelChromosomes = new ArrayList<Chromosome>();
//        for (Chromosome chrom : genotype.getChromosomes()) {
//            if (chrom.getNoveltyValue() >= this.getIECnoveltyThreshold()) {
//                novelChromosomes.add(chrom);
//            }
//        }
//        genotype.setChromosomes(novelChromosomes);
//        Collections.sort(genotype.getChromosomes(), new ChromosomeNoveltyComparator(false, false));
//
//        return genotype;
//    }

    /**
     * Convenience method that returns a newly constructed Genotype instance
     * configured according to the given Configuration instance. The population
     * of Chromosomes will created according to the setup of the sample
     * Chromosome in the Configuration object, but the gene values (alleles)
     * will be set to random legal values. <p> Note that the given Configuration
     * instance must be in a valid state at the time this method is invoked, or
     * an InvalidConfigurationException will be thrown.
     *
     * @param a_activeConfiguration
     * @return A newly constructed Genotype instance.
     * @throws InvalidConfigurationException if the given Configuration instance
     * not in a valid state.
     */
    public synchronized static Population randomInitialGenotype(Configuration a_activeConfiguration)
            throws InvalidConfigurationException {
        if (a_activeConfiguration == null) {
            throw new IllegalArgumentException("The Configuration instance may not be null.");
        }

        a_activeConfiguration.lockSettings();
        List<ChromosomeMaterial> chroms = new ArrayList<ChromosomeMaterial>();
        for (int i = 0; i < a_activeConfiguration.getNoveltySearchPopulationSize(); i++) {
            chroms.add(ChromosomeMaterial.randomInitialChromosomeMaterial(a_activeConfiguration));
        }

        Population population = new Population(a_activeConfiguration);
        population.addChromosomesFromMaterial(chroms);
        return population;
    }

    public synchronized static GenotypePrototype emptyInitialGenotype(Configuration a_activeConfiguration)
            throws InvalidConfigurationException {
        if (a_activeConfiguration == null) {
            throw new IllegalArgumentException("The Configuration instance may not be null.");
        }
        a_activeConfiguration.lockSettings();
        return new GenotypePrototype(a_activeConfiguration, new ArrayList<ChromosomeMaterial>());
    }

    /**
     * Compares this Genotype against the specified object. The result is true
     * if the argument is an instance of the Genotype class, has exactly the
     * same number of chromosomes as the given Genotype, and, for each
     * Chromosome in this Genotype, there is an equal chromosome in the given
     * Genotype. The chromosomes do not need to appear in the same order within
     * the populations.
     *
     * @param other The object to compare against.
     * @return true if the objects are the same, false otherwise.
     */
//    @Override
//    public synchronized boolean equals(Object other) {
//        readLock.lock();
//        try {
//            // First, if the other Genotype is null, then they're not equal.
//            // -------------------------------------------------------------
//            if (other == null) {
//                return false;
//            }
//
//            GenotypePrototype otherGenotype = (GenotypePrototype) other;
//
//            // First, make sure the other Genotype has the same number of
//            // chromosomes as this one.
//            // ----------------------------------------------------------
//            if (iecPopulation.size() != otherGenotype.iecPopulation.size()) {
//                return false;
//            }
//
//            // Next, prepare to compare the chromosomes of the other Genotype
//            // against the chromosomes of this Genotype. To make this a lot
//            // simpler, we first sort the chromosomes in both this Genotype
//            // and the one we're comparing against. This won't affect the
//            // genetic algorithm (it doesn't care about the order), but makes
//            // it much easier to perform the comparison here.
//            // --------------------------------------------------------------
//            Collections.sort(iecPopulation);
//            Collections.sort(otherGenotype.iecPopulation);
//
//            Iterator<Chromosome> iter = iecPopulation.iterator();
//            Iterator<Chromosome> otherIter = otherGenotype.iecPopulation.iterator();
//            while (iter.hasNext() && otherIter.hasNext()) {
//                Chromosome chrom = iter.next();
//                Chromosome otherChrom = otherIter.next();
//                if (!(chrom.equals(otherChrom))) {
//                    return false;
//                }
//            }
//
//            return true;
//        } catch (ClassCastException e) {
//            return false;
//        } finally {
//            readLock.unlock();
//        }
//    }

//    private void removeUnselectedChromosomes() {
//        List<Chromosome> unselectedChromosomes = new ArrayList<Chromosome>();
//
//        // Collect/remove unselected chromosomes from the genotype.
//        // -------------------------------------
//        for (Chromosome chrom : iecPopulation) {
//            if (chrom.isSelectedForNextGeneration()) {
//                // Set the remaining chromosomes as unselected.
//                chrom.setIsSelectedForNextGeneration(false);
//            } else {
//                unselectedChromosomes.add(chrom);
//            }
//        }
//        iecPopulation.removeAll(unselectedChromosomes);
//
//        updateSpecies();
//    }
    
//    @Deprecated
//    private void expandPopulation(int targetPopulationSize) {
//        // Add offspring to the current population.
//        // -------------------------------------
//        addChromosomesFromMaterial(getExtraChromosomes(targetPopulationSize));
//    }

//    @Deprecated
//    private List<ChromosomeMaterial> getExtraChromosomes(int targetChomosomeCount) {
//        return getExtraChromosomes(targetChomosomeCount, iecPopulation);
//    }
    
//    private List<ChromosomeMaterial> getExtraChromosomes(int targetChomosomeCount, Chromosome seed) {
//        Collection<Chromosome> seeds = new ArrayList<Chromosome>();
//        seeds.add(seed);
//        return getExtraChromosomes(targetChomosomeCount, seeds);
//    }
    
//    private List<ChromosomeMaterial> getExtraChromosomes(int targetChomosomeCount, Collection<Chromosome> seeds) {
//        List<ChromosomeMaterial> offspring = new ArrayList<ChromosomeMaterial>();
//        try {
//            // Clone the current populationExpand the number of offspring to the novelty search populations size, (e.g. 250).
//            // -------------------------------------
//            for (Chromosome chrom : iecPopulation) {
//                offspring.add(chrom.cloneMaterial());
//            }
//
//            // Execute Mutation Operators.
//            // -------------------------------------
//            adjustChromosomeList(offspring, targetChomosomeCount - iecPopulation.size());
//            mutateChromosomeMaterial(offspring);
//
//        } catch (InvalidConfigurationException ex) {
//            Logger.getLogger(GenotypePrototype.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return offspring;
//    }

    private Integer atLastStep = 0;
    private Integer iecStepCount = 0;

    public synchronized int getStepEvaluationCount() {
        readLock.lock();
        try {
            iecStepCount = chromosomeIdArchive.size() - atLastStep;
            return iecStepCount;
        } finally {
            readLock.unlock();
        }
    }

    public synchronized int getSeriesEvaluationCount() {
        readLock.lock();
        try {
            return chromosomeIdArchive.size();
        } finally {
            readLock.unlock();
        }
    }

    public synchronized void clearStepCounter() {
        writeLock.lock();
        try {
            atLastStep = chromosomeIdArchive.size();
        } finally {
            writeLock.unlock();
        }
    }
    private Collection<Long> chromosomeIdArchive = new HashSet<Long>();

//    public synchronized List<Chromosome> getLineage(Chromosome chromosome) {
//        readLock.lock();
//        try {
//            LinkedList<Chromosome> lineage = new LinkedList<Chromosome>();
//            lineage.push(chromosome);
//            while (chromosome.getPrimaryParentId() != null) {
//                chromosome = chromosomeArchive.get(chromosome.getPrimaryParentId());
//                lineage.push(chromosome);
//            }
//            return lineage;
//        } finally {
//            readLock.unlock();
//        }
//    }
    public synchronized int getCurrentNoveltyArchiveSize() {
        readLock.lock();
        try {
            return m_activeConfiguration.getBulkFitnessFunctions().get(0).getNoveltyArchiveSize();
        } finally {
            readLock.unlock();
        }
    }

    public synchronized double getCurrentNoveltyThreshold() {
        readLock.lock();
        try {
            return m_activeConfiguration.getBulkFitnessFunctions().get(0).getNoveltyThreshold();
        } finally {
            readLock.unlock();
        }
    }

    public synchronized ArrayList<Map<Chromosome, BehaviorVector>> getAllPointsVisited() {
        readLock.lock();
        try {
        	ArrayList<Map<Chromosome, BehaviorVector>> ret = 
        			new ArrayList<Map<Chromosome, BehaviorVector>>(m_activeConfiguration.getBulkFitnessFunctions().size());
        	for (EvaluationFunction func : m_activeConfiguration.getBulkFitnessFunctions().values())
        		ret.add(func.getAllPointsVisited());
        	return ret;
        } finally {
            readLock.unlock();
        }
    }

    @SuppressWarnings("unused")
	private void fireGeneticEvents() {
        // Fire an event to indicate we've evaluated all chromosomes.
        // -------------------------------------------------------
        m_activeConfiguration.getEventManager().fireGeneticEvent(
                new GeneticEvent(GeneticEvent.GENOTYPE_EVALUATED_EVENT, this));

        // Fire an event to indicate we're starting genetic operators. Among
        // other things this allows for RAM conservation.
        // -------------------------------------------------------
        m_activeConfiguration.getEventManager().fireGeneticEvent(
                new GeneticEvent(GeneticEvent.GENOTYPE_START_GENETIC_OPERATORS_EVENT, this));

        // Fire an event to indicate we're starting genetic operators. Among
        // other things this allows for RAM conservation.
        // -------------------------------------------------------
        m_activeConfiguration.getEventManager().fireGeneticEvent(
                new GeneticEvent(GeneticEvent.GENOTYPE_FINISH_GENETIC_OPERATORS_EVENT, this));

        // Fire an event to indicate we've performed an evolution.
        // -------------------------------------------------------
        m_activeConfiguration.getEventManager().fireGeneticEvent(
                new GeneticEvent(GeneticEvent.GENOTYPE_EVOLVED_EVENT, this));
    }
    private GenotypeTask task;

    public synchronized void doShortTermNoveltySearch() {
        if (isStopped()) {
            task = new GenotypeNoveltyTask();
            task.start();
        }
    }

    public synchronized void doIECstep() {
        if (isStopped()) {
            task = new GenotypeIECtask();
            task.start();
        }
    }

    public synchronized void doShortTermOptimization() {
        if (isStopped()) {
            task = new GenotypeFitnessTask();
            task.start();
        }
    }
    private double iecNoveltyThresholdMultiplyer = 2;

    @SuppressWarnings("unused")
	private double getIECnoveltyThreshold() {
        return iecNoveltyThresholdMultiplyer * getCurrentNoveltyThreshold();
    }

    public synchronized void setIECnoveltyThresholdMultiplyer(double newNoveltyThresholdMultiplyer) {
        writeLock.lock();
        try {
            iecNoveltyThresholdMultiplyer = newNoveltyThresholdMultiplyer;
        } finally {
            writeLock.unlock();
        }
    }

    @SuppressWarnings("unused")
	private int iecProgress(int evaluations) {
        double progress = 100.0 * evaluations / m_activeConfiguration.getIECpopulationSize();
        progress = Math.max(Math.min(progress, 99), 0);
        return (int) Math.floor(progress);
    }

    @SuppressWarnings("unused")
	private int noveltyProgress(int found, int evaluations) {
        double progressA = (double) (found / m_activeConfiguration.getIECpopulationSize());
        progressA = Math.max(Math.min(progressA, 1.0), 0.0);

        double progressB = (double) (evaluations / m_activeConfiguration.getShortTermNoveltySearchLimit());
        progressB = Math.max(Math.min(progressB, 1.0), 0.0);

        double progress = 100 * (progressA * progressB);
        progress = Math.max(Math.min(progress, 99), 0);
        return (int) Math.floor(progress);
    }

    @SuppressWarnings("unused")
	private int noveltyProgress(int evaluations) {
        double progress = 100.0 * (double) evaluations / m_activeConfiguration.getShortTermNoveltySearchLimit();
        progress = Math.max(Math.min(progress, 99), 1);
        return (int) Math.floor(progress);
    }

    @SuppressWarnings("unused")
	private int fitnessProgress(int evaluations) {
        double progress = 100.0 * evaluations / m_activeConfiguration.getShortTermFitnessSearchLimit();
        progress = Math.max(Math.min(progress, 99), 0);
        return (int) Math.floor(progress);
    }

//    private void iecTask() {
//        removeUnselectedChromosomes();
//
//        // Mark the user slected chromosomes as Protected
//        // -------------------------------------
//        for (Chromosome chrom : iecPopulation) {
//            chrom.protect();
//        }
//
//        // Change the active mutation parameters
//        // -------------------------------------
//        WeightMutationOperator iecWeightMutationOperator = new WeightMutationOperator(iecMutationRate, iecMutationPower);
//        WeightMutationOperator oldWeightMutationOperator = null;
//        for (MutationOperator operator : m_activeConfiguration.getMutationOperators()) {
//            if (operator instanceof WeightMutationOperator) {
//                oldWeightMutationOperator = (WeightMutationOperator) operator;
//                break;
//            }
//        }
//        m_activeConfiguration.getMutationOperators().remove(oldWeightMutationOperator);
//        m_activeConfiguration.getMutationOperators().add(iecWeightMutationOperator);
//
//        expandPopulation(m_activeConfiguration.getIECpopulationSize());
//
//        int i = 0;
//        for (Chromosome chrom : iecPopulation) {
//            updateProgressListeners(iecProgress(i), chromosomeIdArchive.size());
//            m_activeConfiguration.getBulkFitnessFunction().evaluate(chrom);
//            i++;
//
//            if (userRequestedStop()) {
//                break;
//            }
//        }
//        evaluateForFitness();
//        evaluateForNovelty();
//
//        // Restore the active mutation parameters
//        // -------------------------------------
//        m_activeConfiguration.getMutationOperators().remove(iecWeightMutationOperator);
//        m_activeConfiguration.getMutationOperators().add(oldWeightMutationOperator);
//
//        // For convienence, re-select the chromosomes previously selected by the user
//        // -------------------------------------
//        for (Chromosome chrom : iecPopulation) {
//            if (chrom.isProtected()) {
//                chrom.setIsSelectedForNextGeneration(true);
//                chrom.unprotect();
//            }
//        }
//
//        finishProgressListeners();
//        fireGeneticEvents();
//    }
//
//    private void noveltyTask() {
//
//        List<Chromosome> selectedChromosomes = new ArrayList<Chromosome>();
//        LinkedList<ChromosomeMaterial> extraChromosomeMaterial = new LinkedList<ChromosomeMaterial>();
//        List<Chromosome> novelChromosomes = new ArrayList<Chromosome>();
//
//        removeUnselectedChromosomes();
//        selectedChromosomes.addAll(iecPopulation);
//
//        // Mark the user slected chromosomes as Protected
//        // -------------------------------------
//        for (Chromosome chrom : iecPopulation) {
//            chrom.protect();
//        }
//
//        if (selectedChromosomes.isEmpty()) {
//            extraChromosomeMaterial.addAll(getExtraChromosomes(m_activeConfiguration.getNoveltySearchPopulationSize()));
////            expandPopulation(m_activeConfiguration.getNoveltySearchPopulationSize());
//        }
//
//        // Collect individuals above the novelty threshold.
//        // -------------------------------------
//        int novelChromosomeTargetSize = m_activeConfiguration.getIECpopulationSize() - selectedChromosomes.size();
//        for (int i = 0; i < m_activeConfiguration.getShortTermNoveltySearchLimit(); i++) {
//            updateProgressListeners(noveltyProgress(i), chromosomeIdArchive.size());
//
//            if (extraChromosomeMaterial.isEmpty()) {
//                replaceLeastNovelIndividual();
//                for (Chromosome chrom : iecPopulation) {
//                    if (!novelChromosomes.contains(chrom)) {
//                        if (chrom.getNoveltyValue() >= getIECnoveltyThreshold()) {
//                            novelChromosomes.add(chrom);
//                        }
//                    }
//                }
//            } else {
//                addChromosomeFromMaterial(extraChromosomeMaterial.removeFirst());
//                evaluateForFitness();
//                evaluateForNovelty();
//            }
//
//            if (userRequestedStop()) {
//                break;
//            }
//
//            if (i >= m_activeConfiguration.getNoveltySearchPopulationSize()) {
//                if (novelChromosomes.size() >= novelChromosomeTargetSize) {
//                    break;
//                }
//            }
//        }
//
//        // Since an insufficient number of individuals were discovered within the run
//        // limit, the most novel individuals will be added to meet the IEC quota.
//        // -------------------------------------
//        Collections.sort(iecPopulation, new ChromosomeNoveltyComparator(false, false));
//        for (Chromosome candidate : iecPopulation) {
//            if (novelChromosomes.size() < novelChromosomeTargetSize) {
//                if (!novelChromosomes.contains(candidate)
//                        && !selectedChromosomes.contains(candidate)) {
//                    novelChromosomes.add(candidate);
//                }
//            } else {
//                break;
//            }
//        }
//
//        /*
//         * Contract the population of novel discoveries to the evaluation
//         * population size. // -------------------------------------
//         * Collections.sort(novelChromosomes, new
//         * ChromosomeNoveltyComparator(true, false)); int i = 0; while
//         * (novelChromosomes.size() >
//         * m_activeConfiguration.getIECpopulationSize()) { if
//         * (novelChromosomes.get(i).isProtected()) { i++; } else if
//         * (novelChromosomes.get(i).isSolution()) { i++; } else {
//         * novelChromosomes.remove(i); } } This block was removed to allow the
//         * user to see the set of all candidates that were above the specified
//         * IEC Novelty Threshold
//         */
//
//        // For convienence, re-select the chromosomes previously selected by the user
//        // -------------------------------------
//        for (Chromosome chrom : selectedChromosomes) {
//            chrom.setIsSelectedForNextGeneration(true);
//        }
//
//        setChromosomes(selectedChromosomes);
//
//        Collections.sort(novelChromosomes, new ChromosomeNoveltyComparator(false, false));
//        appendToPopulation(novelChromosomes);
//
//        finishProgressListeners();
//
//        fireGeneticEvents();
//    }

//    private void optimizationTask() {
//        
//        // Protect the user selected IEC chromosomes
//        // -------------------------------------
//        Chromosome seed = null;
//        for (Chromosome chrom : iecPopulation) {
//            if (chrom.isSelectedForNextGeneration()) {
//                chrom.setIsSelectedForNextGeneration(false);
//                chrom.protect();
//                seed = chrom;
//            }
//        }
//
//        // Change the active mutation parameters
//        // -------------------------------------
//        WeightMutationOperator iecWeightMutationOperator = new WeightMutationOperator(fitnessMutationRate, fitnessMutationPower);
//        WeightMutationOperator oldWeightMutationOperator = null;
//        for (MutationOperator operator : m_activeConfiguration.getMutationOperators()) {
//            if (operator instanceof WeightMutationOperator) {
//                oldWeightMutationOperator = (WeightMutationOperator) operator;
//                break;
//            }
//        }
//        m_activeConfiguration.getMutationOperators().remove(oldWeightMutationOperator);
//        m_activeConfiguration.getMutationOperators().add(iecWeightMutationOperator);
//
//        LinkedList<ChromosomeMaterial> extraChromosomeMaterial = new LinkedList<ChromosomeMaterial>();
//        extraChromosomeMaterial.addAll(getExtraChromosomes(seed, m_activeConfiguration.getFitnessSearchPopulationSize()));
//
////        int i = 0;
////        for (Chromosome chrom : m_chromosomes) {
////            updateProgressListeners(fitnessProgress(i), chromosomeIdArchive.size());
////            m_activeConfiguration.getBulkFitnessFunction().evaluateFitness(chrom);
////            i++;
////
////            if (userRequestedStop()) {
////                break;
////            }
////        }
//
//        // Steady-state optimization until a new champ it discovered 
//        // -------------------------------------
//        for (int i = 0; i < m_activeConfiguration.getShortTermFitnessSearchLimit(); i++) {
//            updateProgressListeners(fitnessProgress(i), chromosomeIdArchive.size());
//
//            if (extraChromosomeMaterial.isEmpty()) {
//                if (seed.getFitnessValue() < fittestChromosome().getFitnessValue()) {
//                    break;
//                }
//            } else {
//                addChromosomeFromMaterial(extraChromosomeMaterial.removeFirst());
//                evaluateForFitness();
//            }
//
//            if (userRequestedStop()) {
//                break;
//            }
//
//            replaceLeastFitIndividual();
//        }
//
//        // Restore the active mutation parameters
//        // -------------------------------------
//        m_activeConfiguration.getMutationOperators().remove(iecWeightMutationOperator);
//        m_activeConfiguration.getMutationOperators().add(oldWeightMutationOperator);
//
//        // Replace the user selected seed with the new champ
//        // -------------------------------------
//        Chromosome champ = fittestChromosome();
//
//        champ.setIsSelectedForNextGeneration(true);
//        population.add(population.indexOf(seed), champ);
//        population.remove(seed);
//
//        setPopulation(population);
//        finishProgressListeners();
//
//        fireGeneticEvents();
//    }

    public synchronized boolean isRunning() {
        return !isStopped();
    }

    /**
     *
     * @author bwoolley
     */
    private abstract class GenotypeTask extends Thread {

        private GenotypeTask() {
            resetUserRequestedStop();
        }
    }

    /**
     *
     * @author bwoolley
     */
    private class GenotypeIECtask extends GenotypeTask {

        @Override
        public void run() {
            writeLock.lock();
            try {
//                iecTask();
            } finally {
                writeLock.unlock();
            }
        }
    }

    /**
     *
     * @author bwoolley
     */
    private class GenotypeNoveltyTask extends GenotypeTask {

        @Override
        public void run() {
            writeLock.lock();
            try {
//                noveltyTask();
            } finally {
                writeLock.unlock();
            }
        }
    }

    /**
     *
     * @author bwoolley
     */
    private class GenotypeFitnessTask extends GenotypeTask {

        @Override
        public void run() {
            writeLock.lock();
            try {
//                optimizationTask();
            } finally {
                writeLock.unlock();
            }
        }
    }
}
