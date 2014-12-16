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

import com.anji.neat.WeightMutationOperator;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jgap.event.GeneticEvent;
import org.jgap.impl.FitnessReproductionOperator;
import org.jgap.impl.FitnessSelector;
import org.jgap.impl.NoveltyReproductionOperator;
import org.jgap.impl.NoveltySelector;

/**
 * Genotypes are fixed-length populations of chromosomes. As an instance of sa
 * <code>Genotype</code> is evolved, all of its <code>Chromosome</code> objects
 * are also evolved. A <code>Genotype</code> may be constructed normally,
 * whereby an array of <code>Chromosome</code> objects must be provided, or the
 * static <code>randomInitialGenotype()</code> method can be used to generate a
 * <code>Genotype</code> with a randomized <code>Chromosome</code> population.
 * Changes made by Tucker and James for <a
 * href="http://anji.sourceforge.net/">ANJI </a>:
 * <ul>
 * <li>added species</li>
 * <li>modified order of operations in <code>evolveForFitness()</code></li>
 * <li>added <code>addChromosome*()</code> methods</li>
 * </ul>
 */
@SuppressWarnings("serial")
public class Genotype extends ProgressSource implements Serializable {

	private static final Logger logger = Logger.getLogger(Genotype.class
			.getName());
	/**
	 * The current active Configuration instance.
	 */
	protected Configuration m_activeConfiguration;
	/**
	 * Species that makeup this Genotype's population.
	 */
	protected List<Specie> m_species = new ArrayList<Specie>();
	/**
	 * Chromosomes that makeup this Genotype's population.
	 */
	private List<Chromosome> m_chromosomes = new ArrayList<Chromosome>();
	private List<Chromosome> lastFitnessPopulation = new ArrayList<Chromosome>();
	private float iecMutationRate = 0.1f;
	private float iecMutationPower = 0.1f;

	public synchronized void setIECmutationRate(float aMutationRate) {
		// iecMutationRate = Math.min(Math.max(aMutationRate, 0.0f), 1.0f);
	}

	public synchronized void setIECmutationPower(float aMutationPower) {
		// iecMutationPower = Math.max(aMutationPower, 0.0f);
	}

	private float fitnessMutationRate = 0.1f;
	private float fitnessMutationPower = 0.1f;
	private ChromosomeDominanceComparator dominanceComparator;

	public synchronized void setFitnessMutationRate(float aMutationRate) {
		// fitnessMutationRate = Math.min(Math.max(aMutationRate, 0.0f), 1.0f);
	}

	public synchronized void setFitnessMutationPower(float aMutationPower) {
		// fitnessMutationPower = Math.max(aMutationPower, 0.0f);
	}

	private ReadWriteLock genotypeLock = new ReentrantReadWriteLock();
	private Lock readLock = genotypeLock.readLock();
	private Lock writeLock = genotypeLock.writeLock();

	/**
	 * This constructor is used for random initial Genotypes. Note that the
	 * Configuration object must be in a valid state when this method is
	 * invoked, or a InvalidconfigurationException will be thrown.
	 *
	 * @param a_activeConfiguration
	 *            The current active Configuration object.
	 * @param a_initialChromosomes
	 *            <code>List</code> contains Chromosome objects: The Chromosome
	 *            population to be managed by this Genotype instance.
	 * @throws IllegalArgumentException
	 *             if either the given Configuration object or the array of
	 *             Chromosomes is null, or if any of the Genes in the array of
	 *             Chromosomes is null.
	 * @throws InvalidConfigurationException
	 *             if the given Configuration object is in an invalid state.
	 */
	public Genotype(Configuration a_activeConfiguration,
			List<ChromosomeMaterial> a_initialChromosomes)
			throws InvalidConfigurationException {
		// Sanity checks: Make sure neither the Configuration, the array
		// of Chromosomes, nor any of the Genes inside the array are null.
		// ---------------------------------------------------------------
		if (a_activeConfiguration == null) {
			throw new IllegalArgumentException(
					"The Configuration instance may not be null.");
		}

		if (a_initialChromosomes == null) {
			throw new IllegalArgumentException(
					"The array of Chromosomes may not be null.");
		}

		for (int i = 0; i < a_initialChromosomes.size(); i++) {
			if (a_initialChromosomes.get(i) == null) {
				throw new IllegalArgumentException(
						"The Chromosome instance at index "
								+ i
								+ " of the array of "
								+ "Chromosomes is null. No instance in this array may be null.");
			}
		}

		// Lock the settings of the Configuration object so that the cannot
		// be altered.
		// ----------------------------------------------------------------
		a_activeConfiguration.lockSettings();
		m_activeConfiguration = a_activeConfiguration;
		
		dominanceComparator = new ChromosomeDominanceComparator(m_activeConfiguration);

		writeLock.lock();
		try {
			addChromosomesFromMaterial(a_initialChromosomes);
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

	private synchronized boolean userRequestedStop() {
		return userRequestedStop;
	}

	private synchronized void resetUserRequestedStop() {
		userRequestedStop = false;
	}

	/**
	 * adjust chromosome list to fit population size; first, clone population
	 * (starting at beginning of list) until we reach or exceed pop. size or
	 * trim excess (from end of list)
	 *
	 * @param chroms
	 *            <code>List</code> contains <code>Chromosome</code> objects
	 * @param targetSize
	 */
	private void adjustChromosomeList(List<ChromosomeMaterial> chroms,
			int targetSize) throws InvalidConfigurationException {
		List<ChromosomeMaterial> extras = new ArrayList<ChromosomeMaterial>();

		if (chroms.isEmpty()) {
			for (int i = 0; i < targetSize; i++) {
				chroms.add(ChromosomeMaterial
						.randomInitialChromosomeMaterial(m_activeConfiguration));
			}
		} else {
			for (ChromosomeMaterial cMat : chroms) {
				extras.add(cMat.clone(cMat.getPrimaryParentId()));
			}
		}

		while (chroms.size() < targetSize) {
			for (ChromosomeMaterial cMat : extras) {
				chroms.add(cMat.clone(cMat.getPrimaryParentId()));
			}
		}

		Collections.shuffle(chroms);
		while (chroms.size() > targetSize) {
			chroms.remove(0);
		}
	}

	/**
	 * @param chromosomes
	 *            <code>Collection</code> contains Chromosome objects
	 * @see Genotype#addChromosome(Chromosome)
	 */
	private void addChromosomes(Collection<Chromosome> chromosomes) {
		for (Chromosome chrom : chromosomes) {
			addChromosome(chrom);
		}
	}

	/**
	 * @param chromosomeMaterial
	 *            <code>Collection</code> contains ChromosomeMaterial objects
	 * @see Genotype#addChromosomeFromMaterial(ChromosomeMaterial)
	 */
	private void addChromosomesFromMaterial(
			Collection<ChromosomeMaterial> chromosomeMaterial) {
		for (ChromosomeMaterial cMat : chromosomeMaterial) {
			addChromosomeFromMaterial(cMat);
		}
	}

	/**
	 * @param cMat
	 *            chromosome material from which to construct new chromosome
	 *            object
	 * @see Genotype#addChromosome(Chromosome)
	 */
	private void addChromosomeFromMaterial(ChromosomeMaterial cMat) {
		Chromosome chrom = new Chromosome(cMat,
				m_activeConfiguration.nextChromosomeId());
		addChromosome(chrom);
	}

	/**
	 * add chromosome to population and to appropriate specie
	 *
	 * @param chrom
	 */
	private void addChromosome(Chromosome chrom) {
		m_chromosomes.add(chrom);
		if (!chromosomeIdArchive.contains(chrom.getId())) {
			chromosomeIdArchive.add(chrom.getId());
		}

		// specie collection
		boolean added = false;
		for (Specie specie : m_species) {
			if (specie.match(chrom)) {
				specie.add(chrom);
				added = true;
				break;
			}
		}
		if (!added) {
			m_species.add(new Specie(
					m_activeConfiguration.getSpeciationParms(), chrom));
		}
	}

	/**
	 * @return List contains Chromosome objects, the population of Chromosomes.
	 */
	public synchronized List<Chromosome> getChromosomes() {
		readLock.lock();
		try {
			return m_chromosomes;
			// return new ArrayList<Chromosome>(m_chromosomes);
		} finally {
			readLock.unlock();
		}
	}

	public void setChromosomes(List<Chromosome> aNewChromosomeSet) {
		writeLock.lock();
		try {
			setPopulation(aNewChromosomeSet);
		} finally {
			writeLock.unlock();
		}
	}

	private void appendToPopulation(List<Chromosome> newChromosomes) {
		updateSpecies();
		for (Chromosome newChrom : newChromosomes) {
			if (!m_chromosomes.contains(newChrom)) {
				addChromosome(newChrom);
			}
		}
	}

	private void setPopulation(List<Chromosome> aNewChromosomeSet) {
		m_chromosomes.clear();
		updateSpecies();
		addChromosomes(aNewChromosomeSet);
		updateSpecies();
	}

	/**
	 * @return the number of chromosomes in this genotype
	 */
	public int getGenotypeSize() {
		readLock.lock();
		try {
			return m_chromosomes.size();
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
			return m_species;
		} finally {
			readLock.unlock();
		}
	}

	/**
	 * Retrieves the Chromosome in the population with the highest fitness
	 * value.
	 *
	 * @return The Chromosome with the highest fitness value, or null if there
	 *         are no chromosomes in this Genotype.
	 */
	public synchronized Chromosome getFittestChromosome() {
		readLock.lock();
		try {
			return fittestChromosome();

		} finally {
			readLock.unlock();
		}
	}

	private Chromosome fittestChromosome() {
		setMinMaxValues();
		
		Chromosome champ = null;
		for (Chromosome next : m_chromosomes) {
			if (next == null)
				continue;
			if (champ == null)
				champ = next;

			if (dominanceComparator.compare(next, champ) > 0) {
				champ = next;
			}
		}

		return champ;
	}
	
	private Chromosome fittestChromosome(Chromosome seed, List<EvaluationFunction> funcs) {
		setMinMaxValues();
		
		Chromosome champ = seed;
		for (Chromosome next : m_chromosomes) {
			if (next == null)
				continue;
			if (champ == null)
				champ = next;
			
			if (dominanceComparator.dominanceCompare(next, champ, funcs) > 0) {
				champ = next;
			}
		}
		return champ;
	}
	
	private void setMinMaxValues() {
		for (Chromosome chrom : m_chromosomes) {
			for (Entry<Long, Integer> entry : chrom.getFitnessValues().entrySet()) {
				m_activeConfiguration.setMinValue(entry.getKey(), entry.getValue());
				m_activeConfiguration.setMaxValue(entry.getKey(), entry.getValue());
			}
		}
	}
	
	private void protectAllChromosomes() {
		for (Chromosome chrom : m_chromosomes)
			chrom.protect();
	}

	/**
	 * Retrieves the Chromosome in the population with the highest fitness
	 * value.
	 *
	 * @return The Chromosome with the highest fitness value, or null if there
	 *         are no chromosomes in this Genotype.
	 */
	public synchronized Chromosome getMostNovelChromosome() {
		readLock.lock();
		try {
			return nostNovelChromosome();

		} finally {
			readLock.unlock();
		}
	}

	private Chromosome nostNovelChromosome() {
		Chromosome champ = null;
		for (Chromosome next : m_chromosomes) {
			if (champ == null) {
				champ = next;
			}

			if (next.getNoveltyValue() > champ.getNoveltyValue()) {
				champ = next;
			}
		}

		return champ;
	}

	private void replaceLeastFitIndividual() {
		if (m_chromosomes.size() < m_activeConfiguration
				.getFitnessSearchPopulationSize()) {
			// Do Nothing
		} else {
			removeLeastFitIndividual();
		}
		reproduceForFitness(1);
		evaluateForFitness();
	}

	private void replaceLeastNovelIndividual() {
		if (m_chromosomes.size() < m_activeConfiguration
				.getNoveltySearchPopulationSize()) {
			// do nothing
		} else {
			removeLeastNovelIndividual();
		}
		reproduceForNovelty(1);
		evaluateForNovelty();
	}

	/**
	 * Performs one generation cycle, evaluating fitness, selecting survivors,
	 * repopulting with offspring, and mutating new population. This is a
	 * modified version of original JGAP method which changes order of
	 * operations and splits <code>GeneticOperator</code> into
	 * <code>ReproductionOperator</code> and <code>MutationOperator</code>. New
	 * order of operations:
	 * <ol>
	 * <li>assign <b>fitness </b> to all members of population with
	 * <code>EvaluationFunction</code> or <code>FitnessFunction</code></li>
	 * <li><b>select </b> survivors and remove casualties from population</li>
	 * <li>re-fill population with offspring via <b>reproduction </b> operators</li>
	 * <li><b>mutate </b> offspring (note, survivors are passed on un-mutated)</li>
	 * </ol>
	 * Genetic event <code>GeneticEvent.GENOTYPE_EVALUATED_EVENT</code> is fired
	 * between steps 2 and 3. Genetic event
	 * <code>GeneticEvent.GENOTYPE_EVOLVED_EVENT</code> is fired after step 4.
	 */
	public synchronized void evolveForFitness() {
		writeLock.lock();
		try {
			m_activeConfiguration.lockSettings();

			// If a bulk fitness function has been provided, then convert the
			// working pool to an array and pass it to the bulk fitness
			// function so that it can evaluateAllChromosomes and assign fitness
			// values to
			// each of the Chromosomes.
			// --------------------------------------------------------------
			evaluateForFitness();

			if (m_activeConfiguration.isSteadyState()) {
				for (int i = 0; i < m_activeConfiguration
						.getFitnessSearchPopulationSize(); i++) {
					replaceLeastFitIndividual();
					notifyPopulationListeners(m_chromosomes);
				}
			} else {

				// Select chromosomes to survive.
				// ------------------------------------------------------------
				FitnessSelector selector = m_activeConfiguration
						.getFitnessSelector();
				selector.add(m_activeConfiguration, m_chromosomes);
				m_chromosomes = selector.select(m_activeConfiguration);
				selector.empty();

				// Repopulate the population of species and chromosomes with
				// those selected
				// by the natural selector, and cull species down to contain
				// only remaining
				// chromosomes.
				updateSpecies();

				// Execute Reproduction Operators.
				// -------------------------------------
				Iterator<FitnessReproductionOperator> iterator = m_activeConfiguration
						.getFitnessReproductionOperators().iterator();
				List<ChromosomeMaterial> offspring = new ArrayList<ChromosomeMaterial>();
				while (iterator.hasNext()) {
					FitnessReproductionOperator operator = iterator.next();
					operator.reproduce(m_activeConfiguration, m_species,
							offspring);
				}

				// Execute Mutation Operators.
				// -------------------------------------
				Iterator<MutationOperator> mutOpIter = m_activeConfiguration
						.getMutationOperators().iterator();
				while (mutOpIter.hasNext()) {
					MutationOperator operator = mutOpIter.next();
					operator.mutate(m_activeConfiguration, offspring);
				}

				// in case we're off due to rounding errors
				Collections.shuffle(offspring,
						m_activeConfiguration.getRandomGenerator());
				adjustChromosomeList(offspring,
						m_activeConfiguration.getFitnessSearchPopulationSize()
								- m_chromosomes.size());

				// add offspring
				// ------------------------------
				addChromosomesFromMaterial(offspring);

			}

			// Fire an event to indicate we've evaluated all chromosomes.
			// -------------------------------------------------------
			m_activeConfiguration.getEventManager().fireGeneticEvent(
					new GeneticEvent(GeneticEvent.GENOTYPE_EVALUATED_EVENT,
							this));

			// Fire an event to indicate we're starting genetic operators. Among
			// other things this allows for RAM conservation.
			// -------------------------------------------------------
			m_activeConfiguration
					.getEventManager()
					.fireGeneticEvent(
							new GeneticEvent(
									GeneticEvent.GENOTYPE_START_GENETIC_OPERATORS_EVENT,
									this));

			// Fire an event to indicate we're starting genetic operators. Among
			// other things this allows for RAM conservation.
			// -------------------------------------------------------
			m_activeConfiguration
					.getEventManager()
					.fireGeneticEvent(
							new GeneticEvent(
									GeneticEvent.GENOTYPE_FINISH_GENETIC_OPERATORS_EVENT,
									this));

			// Fire an event to indicate we've performed an evolution.
			// -------------------------------------------------------
			m_activeConfiguration.getEventManager()
					.fireGeneticEvent(
							new GeneticEvent(
									GeneticEvent.GENOTYPE_EVOLVED_EVENT, this));

		} catch (InvalidConfigurationException ex) {
			Logger.getLogger(Genotype.class.getName()).log(Level.SEVERE, null,
					ex);
		} finally {
			writeLock.unlock();
		}
	}

	/**
	 * Performs one generation cycle, evaluating fitness, selecting survivors,
	 * repopulting with offspring, and mutating new population. This is a
	 * modified version of original JGAP method which changes order of
	 * operations and splits <code>GeneticOperator</code> into
	 * <code>ReproductionOperator</code> and <code>MutationOperator</code>. New
	 * order of operations:
	 * <ol>
	 * <li>assign <b>fitness </b> to all members of population with
	 * <code>EvaluationFunction</code> or <code>FitnessFunction</code></li>
	 * <li><b>select </b> survivors and remove casualties from population</li>
	 * <li>re-fill population with offspring via <b>reproduction </b> operators</li>
	 * <li><b>mutate </b> offspring (note, survivors are passed on un-mutated)</li>
	 * </ol>
	 * Genetic event <code>GeneticEvent.GENOTYPE_EVALUATED_EVENT</code> is fired
	 * between steps 2 and 3. Genetic event
	 * <code>GeneticEvent.GENOTYPE_EVOLVED_EVENT</code> is fired after step 4.
	 */
	public synchronized void evolveForNovelty() {
		writeLock.lock();
		try {
			m_activeConfiguration.lockSettings();

			// If a bulk fitness function has been provided, then convert the
			// working pool to an array and pass it to the bulk fitness
			// function so that it can evaluateAllChromosomes and assign fitness
			// values to
			// each of the Chromosomes.
			// --------------------------------------------------------------
			evaluateForNovelty();

			if (m_activeConfiguration.isSteadyState()) {
				for (int i = 0; i < m_activeConfiguration
						.getNoveltySearchPopulationSize(); i++) {
					replaceLeastNovelIndividual();
					notifyPopulationListeners(m_chromosomes);
				}
			} else {

				// Select chromosomes to survive.
				// ------------------------------------------------------------
				NoveltySelector selector = m_activeConfiguration
						.getNoveltySelector();
				selector.add(m_activeConfiguration, m_chromosomes);
				m_chromosomes = selector.select(m_activeConfiguration);
				selector.empty();

				// Repopulate the population of species and chromosomes with
				// those selected
				// by the natural selector, and cull species down to contain
				// only remaining
				// chromosomes.
				updateSpecies();

				// Execute Reproduction Operators.
				// -------------------------------------
				Iterator<NoveltyReproductionOperator> iterator = m_activeConfiguration
						.getNoveltyReproductionOperators().iterator();
				List<ChromosomeMaterial> offspring = new ArrayList<ChromosomeMaterial>();
				while (iterator.hasNext()) {
					NoveltyReproductionOperator operator = iterator.next();
					operator.reproduce(m_activeConfiguration, m_species,
							offspring);
				}

				// Execute Mutation Operators.
				// -------------------------------------
				Iterator<MutationOperator> mutOpIter = m_activeConfiguration
						.getMutationOperators().iterator();
				while (mutOpIter.hasNext()) {
					MutationOperator operator = mutOpIter.next();
					operator.mutate(m_activeConfiguration, offspring);
				}

				// in case we're off due to rounding errors
				Collections.shuffle(offspring,
						m_activeConfiguration.getRandomGenerator());
				adjustChromosomeList(offspring,
						m_activeConfiguration.getNoveltySearchPopulationSize()
								- m_chromosomes.size());

				// add offspring
				// ------------------------------
				addChromosomesFromMaterial(offspring);

			}

			// Fire an event to indicate we've evaluated all chromosomes.
			// -------------------------------------------------------
			m_activeConfiguration.getEventManager().fireGeneticEvent(
					new GeneticEvent(GeneticEvent.GENOTYPE_EVALUATED_EVENT,
							this));

			// Fire an event to indicate we're starting genetic operators. Among
			// other things this allows for RAM conservation.
			// -------------------------------------------------------
			m_activeConfiguration
					.getEventManager()
					.fireGeneticEvent(
							new GeneticEvent(
									GeneticEvent.GENOTYPE_START_GENETIC_OPERATORS_EVENT,
									this));

			// Fire an event to indicate we're starting genetic operators. Among
			// other things this allows for RAM conservation.
			// -------------------------------------------------------
			m_activeConfiguration
					.getEventManager()
					.fireGeneticEvent(
							new GeneticEvent(
									GeneticEvent.GENOTYPE_FINISH_GENETIC_OPERATORS_EVENT,
									this));

			// Fire an event to indicate we've performed an evolution.
			// -------------------------------------------------------
			m_activeConfiguration.getEventManager()
					.fireGeneticEvent(
							new GeneticEvent(
									GeneticEvent.GENOTYPE_EVOLVED_EVENT, this));

		} catch (InvalidConfigurationException ex) {
			Logger.getLogger(Genotype.class.getName()).log(Level.SEVERE, null,
					ex);
		} finally {
			writeLock.unlock();
		}
	}

	/**
	 * @return <code>String</code> representation of this <code>Genotype</code>
	 *         instance.
	 */
	@Override
	public synchronized String toString() {
		readLock.lock();
		try {
			StringBuilder buffer = new StringBuilder();

			Iterator<Chromosome> iter = m_chromosomes.iterator();
			while (iter.hasNext()) {
				Chromosome chrom = iter.next();
				buffer.append(chrom.toString());
				buffer.append(" [");
				for (Entry<Long, Integer> entry : chrom.getFitnessValues()
						.entrySet())
					buffer.append("{" + entry.getKey() + ":" + entry.getValue()
							+ "},");
				buffer.deleteCharAt(buffer.length() - 1); // Remove the trailing
															// comma
				buffer.append(']');
				buffer.append('\n');
			}

			return buffer.toString();
		} finally {
			readLock.unlock();
		}
	}

	public synchronized static Genotype novelInitialGenotype(
			Configuration a_activeConfiguration)
			throws InvalidConfigurationException {

		if (a_activeConfiguration == null) {
			throw new IllegalArgumentException(
					"The Configuration instance may not be null.");
		}

		a_activeConfiguration.lockSettings();
		List<ChromosomeMaterial> chroms = new ArrayList<ChromosomeMaterial>();
		for (int i = 0; i < a_activeConfiguration
				.getNoveltySearchPopulationSize(); i++) {
			chroms.add(ChromosomeMaterial
					.randomInitialChromosomeMaterial(a_activeConfiguration));
		}

		Genotype genotype = new Genotype(a_activeConfiguration, chroms);

		// noveltyTask();
		genotype.evaluateForFitness();
		genotype.evaluateForNovelty();

		// Collections.sort(m_chromosomes, new ChromosomeNoveltyComparator(true,
		// false));
		// while (m_chromosomes.size() >
		// a_activeConfiguration.getIECpopulationSize()) {
		// m_chromosomes.remove(0);
		// }
		// Collections.sort(m_chromosomes);

		List<Chromosome> novelChromosomes = new ArrayList<Chromosome>();
		for (Chromosome chrom : genotype.m_chromosomes) {
			if (chrom.getNoveltyValue() >= genotype.getIECnoveltyThreshold()) {
				novelChromosomes.add(chrom);
			}
		}
		genotype.m_chromosomes = novelChromosomes;
		Collections.sort(genotype.m_chromosomes,
				new ChromosomeNoveltyComparator(false, false));

		return genotype;
	}

	/**
	 * Convenience method that returns a newly constructed Genotype instance
	 * configured according to the given Configuration instance. The population
	 * of Chromosomes will created according to the setup of the sample
	 * Chromosome in the Configuration object, but the gene values (alleles)
	 * will be set to random legal values.
	 * <p>
	 * Note that the given Configuration instance must be in a valid state at
	 * the time this method is invoked, or an InvalidConfigurationException will
	 * be thrown.
	 *
	 * @param a_activeConfiguration
	 * @return A newly constructed Genotype instance.
	 * @throws InvalidConfigurationException
	 *             if the given Configuration instance not in a valid state.
	 */
	public synchronized static Genotype randomInitialGenotype(
			Configuration a_activeConfiguration)
			throws InvalidConfigurationException {
		if (a_activeConfiguration == null) {
			throw new IllegalArgumentException(
					"The Configuration instance may not be null.");
		}

		a_activeConfiguration.lockSettings();
		List<ChromosomeMaterial> chroms = new ArrayList<ChromosomeMaterial>();
		for (int i = 0; i < a_activeConfiguration
				.getNoveltySearchPopulationSize(); i++) {
			chroms.add(ChromosomeMaterial
					.randomInitialChromosomeMaterial(a_activeConfiguration));
		}

		return new Genotype(a_activeConfiguration, chroms);
	}

	public synchronized static Genotype emptyInitialGenotype(
			Configuration a_activeConfiguration)
			throws InvalidConfigurationException {
		if (a_activeConfiguration == null) {
			throw new IllegalArgumentException(
					"The Configuration instance may not be null.");
		}
		a_activeConfiguration.lockSettings();
		return new Genotype(a_activeConfiguration,
				new ArrayList<ChromosomeMaterial>());
	}

	/**
	 * Compares this Genotype against the specified object. The result is true
	 * if the argument is an instance of the Genotype class, has exactly the
	 * same number of chromosomes as the given Genotype, and, for each
	 * Chromosome in this Genotype, there is an equal chromosome in the given
	 * Genotype. The chromosomes do not need to appear in the same order within
	 * the populations.
	 *
	 * @param other
	 *            The object to compare against.
	 * @return true if the objects are the same, false otherwise.
	 */
	@Override
	public synchronized boolean equals(Object other) {
		readLock.lock();
		try {
			// First, if the other Genotype is null, then they're not equal.
			// -------------------------------------------------------------
			if (other == null) {
				return false;
			}

			Genotype otherGenotype = (Genotype) other;

			// First, make sure the other Genotype has the same number of
			// chromosomes as this one.
			// ----------------------------------------------------------
			if (m_chromosomes.size() != otherGenotype.m_chromosomes.size()) {
				return false;
			}

			// Next, prepare to compare the chromosomes of the other Genotype
			// against the chromosomes of this Genotype. To make this a lot
			// simpler, we first sort the chromosomes in both this Genotype
			// and the one we're comparing against. This won't affect the
			// genetic algorithm (it doesn't care about the order), but makes
			// it much easier to perform the comparison here.
			// --------------------------------------------------------------
			Collections.sort(m_chromosomes);
			Collections.sort(otherGenotype.m_chromosomes);

			Iterator<Chromosome> iter = m_chromosomes.iterator();
			Iterator<Chromosome> otherIter = otherGenotype.m_chromosomes
					.iterator();
			while (iter.hasNext() && otherIter.hasNext()) {
				Chromosome chrom = iter.next();
				Chromosome otherChrom = otherIter.next();
				if (!(chrom.equals(otherChrom))) {
					return false;
				}
			}

			return true;
		} catch (ClassCastException e) {
			return false;
		} finally {
			readLock.unlock();
		}
	}

	private void evaluateForNovelty() {
		// Evaluate the new population of of chromosomes.
		// -------------------------------------
		evaluateForFitness();
		for (EvaluationFunction func : m_activeConfiguration
				.getBulkFitnessFunctions().values()) {
			func.evaluateNovelty(m_chromosomes);
			break; // Only need to evaluate for novelty once
		}
	}

	private void evaluateForFitness() {
		// Evaluate the new population of of chromosomes.
		// -------------------------------------
		for (EvaluationFunction func : m_activeConfiguration
				.getBulkFitnessFunctions().values())
			func.evaluateFitness(m_chromosomes);
	}

	private void removeUnselectedChromosomes() {
		List<Chromosome> unselectedChromosomes = new ArrayList<Chromosome>();

		// Collect/remove unselected chromosomes from the genotype.
		// -------------------------------------
		for (Chromosome chrom : m_chromosomes) {
			if (chrom.isSelectedForNextGeneration()) {
				// Set the remaining chromosomes as unselected.
				chrom.setIsSelectedForNextGeneration(false);
			} else {
				unselectedChromosomes.add(chrom);
			}
		}
		m_chromosomes.removeAll(unselectedChromosomes);

		updateSpecies();
	}

	private void updateSpecies() {
		List<Specie> emptySpecies = new ArrayList<Specie>();
		// Culls old chromosomes from each species and removes empty species.
		// -------------------------------------
		for (Specie s : m_species) {
			s.cull(m_chromosomes);
			if (s.isEmpty()) {
				emptySpecies.add(s);
			}
		}
		m_species.removeAll(emptySpecies);
	}

	private void expandPopulation(int targetPopulationSize) {
		List<ChromosomeMaterial> offspring = new ArrayList<ChromosomeMaterial>();
		try {
			// Clone the current populationExpand the number of offspring to the
			// novelty search populations size, (e.g. 250).
			// -------------------------------------
			for (Chromosome chrom : m_chromosomes) {
				offspring.add(chrom.cloneMaterial());
			}

			// Execute Mutation Operators.
			// -------------------------------------
			adjustChromosomeList(offspring, targetPopulationSize
					- m_chromosomes.size());
			mutateChromosomeMaterial(offspring);

		} catch (InvalidConfigurationException ex) {
			Logger.getLogger(Genotype.class.getName()).log(Level.SEVERE, null,
					ex);
		}

		// Add offspring to the current population.
		// -------------------------------------
		addChromosomesFromMaterial(offspring);
	}

	private void removeLeastNovelIndividual() {

		Collections.sort(m_chromosomes, new ChromosomeNoveltyComparator(true,
				true));
		for (int i = 0; i < m_chromosomes.size(); i++) {
			if (m_chromosomes.get(i).isProtected()) {
				// Do nothing
			} else {
				m_chromosomes.remove(i);
				break;
			}
		}
		updateSpecies();
	}

	/**
	 * Removes the chromosome with the lowest speciated fitness score from the
	 * current population.
	 */
	private void removeLeastFitIndividual() {
		Collections.sort(m_chromosomes, dominanceComparator);
		for (int i = 0; i < m_chromosomes.size(); i++) {
			if (m_chromosomes.get(i).isProtected()) {
				// Do nothing
			} else {
				m_chromosomes.remove(i);
				break;
			}
		}
		updateSpecies();
	}

	private void mutateChromosomeMaterial(List<ChromosomeMaterial> offspring)
			throws InvalidConfigurationException {
		// Execute Mutation Operators.
		// -------------------------------------
		for (MutationOperator operator : m_activeConfiguration
				.getMutationOperators()) {
			operator.mutate(m_activeConfiguration, offspring);
		}
	}

	private void reproduceForNovelty(int offspringCount) {
		try {

			// Execute Reproduction Operators.
			// -------------------------------------
			List<ChromosomeMaterial> offspring = new ArrayList<ChromosomeMaterial>();
			for (NoveltyReproductionOperator operator : m_activeConfiguration
					.getNoveltyReproductionOperators()) {
				operator.reproduce(m_activeConfiguration, m_species, offspring);
			}

			adjustChromosomeList(offspring, offspringCount);
			mutateChromosomeMaterial(offspring);

			// add offspring
			// -------------------------------------
			addChromosomesFromMaterial(offspring);

		} catch (InvalidConfigurationException ex) {
			logger.log(Level.SEVERE, null, ex);
		}
	}

	private void reproduceForFitness(int offspringCount) {
		try {

			// Execute Reproduction Operators.
			// -------------------------------------
			List<ChromosomeMaterial> offspring = new ArrayList<ChromosomeMaterial>();
			for (FitnessReproductionOperator operator : m_activeConfiguration
					.getFitnessReproductionOperators()) {
				operator.reproduce(m_activeConfiguration, m_species, offspring);
			}

			adjustChromosomeList(offspring, offspringCount);
			mutateChromosomeMaterial(offspring);

			Collections.shuffle(offspring);
			while (offspring.size() > offspringCount) {
				offspring.remove(0);
			}

			// add offspring
			// ------------------------------
			for (ChromosomeMaterial chrom : offspring) {
				addChromosomeFromMaterial(chrom);
			}

		} catch (InvalidConfigurationException ex) {
			logger.log(Level.SEVERE, null, ex);
		}
	}

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

//	public synchronized List<Chromosome> getLineage(Chromosome chromosome) {
//		readLock.lock();
//		try {
//			LinkedList<Chromosome> lineage = new LinkedList<Chromosome>();
//			lineage.push(chromosome);
//			while (chromosome.getPrimaryParentId() != null) {
//				chromosome = chromosomeIdArchive.get(chromosome.getPrimaryParentId());
//				lineage.push(chromosome);
//			}
//			return lineage;
//		} finally {
//			readLock.unlock();
//		}
//	}
	
	public synchronized int getCurrentNoveltyArchiveSize() {
		readLock.lock();
		int val = 0;
		try {
			for (Entry<Long, EvaluationFunction> entry : m_activeConfiguration
					.getBulkFitnessFunctions().entrySet())
				val = entry.getValue().getNoveltyArchiveSize();
		} finally {
			readLock.unlock();
		}
		return val;
	}

	public synchronized double getCurrentNoveltyThreshold() {
		readLock.lock();
		try {
			for (Entry<Long, EvaluationFunction> entry : m_activeConfiguration
					.getBulkFitnessFunctions().entrySet())
				return entry.getValue().getNoveltyThreshold();
		} finally {
			readLock.unlock();
		}
		return -1;
	}

	public synchronized HashMap<Long, Map<Chromosome, BehaviorVector>> getAllPointsVisited() {
		readLock.lock();
		try {
			HashMap<Long, Map<Chromosome, BehaviorVector>> ret = new HashMap<Long, Map<Chromosome, BehaviorVector>>();
			for (EvaluationFunction func : m_activeConfiguration
					.getBulkFitnessFunctions().values())
				ret.put(func.getEvaluationFunctionUUID(),
						func.getAllPointsVisited());
			return ret;
		} finally {
			readLock.unlock();
		}
	}

	private void fireGeneticEvents() {
		// Fire an event to indicate we've evaluated all chromosomes.
		// -------------------------------------------------------
		m_activeConfiguration.getEventManager().fireGeneticEvent(
				new GeneticEvent(GeneticEvent.GENOTYPE_EVALUATED_EVENT, this));

		// Fire an event to indicate we're starting genetic operators. Among
		// other things this allows for RAM conservation.
		// -------------------------------------------------------
		m_activeConfiguration.getEventManager().fireGeneticEvent(
				new GeneticEvent(
						GeneticEvent.GENOTYPE_START_GENETIC_OPERATORS_EVENT,
						this));

		// Fire an event to indicate we're starting genetic operators. Among
		// other things this allows for RAM conservation.
		// -------------------------------------------------------
		m_activeConfiguration.getEventManager().fireGeneticEvent(
				new GeneticEvent(
						GeneticEvent.GENOTYPE_FINISH_GENETIC_OPERATORS_EVENT,
						this));

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
	
	public synchronized void doShortTermParetoOptimization() {
		if (isStopped()) {
			task = new GenotypeParetoTask();
			task.start();
		}
	}

	public synchronized void doShortTermOptimization(List<EvaluationFunction> funcs) {
		if (isStopped()) {
			task = new GenotypeFitnessTask(funcs);
			task.start();
		}
	}

	private double iecNoveltyThresholdMultiplyer = 2;

	private double getIECnoveltyThreshold() {
		return iecNoveltyThresholdMultiplyer * getCurrentNoveltyThreshold();
	}

	public synchronized void setIECnoveltyThresholdMultiplyer(
			double newNoveltyThresholdMultiplyer) {
		writeLock.lock();
		try {
			iecNoveltyThresholdMultiplyer = newNoveltyThresholdMultiplyer;
		} finally {
			writeLock.unlock();
		}
	}

	private int iecProgress(int evaluations) {
		double progress = 100.0 * evaluations
				/ m_activeConfiguration.getIECpopulationSize();
		progress = Math.max(Math.min(progress, 99), 0);
		return (int) Math.floor(progress);
	}

	private int noveltyProgress(int found, int evaluations) {
		double progressA = (double) (found / m_activeConfiguration
				.getIECpopulationSize());
		progressA = Math.max(Math.min(progressA, 1.0), 0.0);

		double progressB = (double) (evaluations / m_activeConfiguration
				.getShortTermNoveltySearchLimit());
		progressB = Math.max(Math.min(progressB, 1.0), 0.0);

		double progress = 100 * (progressA * progressB);
		progress = Math.max(Math.min(progress, 99), 0);
		return (int) Math.floor(progress);
	}

	private int noveltyProgress(int evaluations) {
		double progress = 100.0 * (double) evaluations
				/ m_activeConfiguration.getShortTermNoveltySearchLimit();
		progress = Math.max(Math.min(progress, 99), 1);
		return (int) Math.floor(progress);
	}

	private int fitnessProgress(int evaluations) {
		double progress = 100.0 * evaluations
				/ m_activeConfiguration.getShortTermFitnessSearchLimit();
		progress = Math.max(Math.min(progress, 99), 0);
		return (int) Math.floor(progress);
	}

	private void iecTask() {
		removeUnselectedChromosomes();

		// Mark the user slected chromosomes as Protected
		// -------------------------------------
		for (Chromosome chrom : m_chromosomes) {
			chrom.protect();
		}

		// Change the active mutation parameters
		// -------------------------------------
		WeightMutationOperator iecWeightMutationOperator = new WeightMutationOperator(
				iecMutationRate, iecMutationPower);
		WeightMutationOperator oldWeightMutationOperator = null;
		for (MutationOperator operator : m_activeConfiguration
				.getMutationOperators()) {
			if (operator instanceof WeightMutationOperator) {
				oldWeightMutationOperator = (WeightMutationOperator) operator;
				break;
			}
		}
		m_activeConfiguration.getMutationOperators().remove(
				oldWeightMutationOperator);
		m_activeConfiguration.getMutationOperators().add(
				iecWeightMutationOperator);

		expandPopulation(m_activeConfiguration.getIECpopulationSize());

		int i = 0;
		for (Chromosome chrom : m_chromosomes) {
			updateProgressListeners(iecProgress(i), chromosomeIdArchive.size());
			for (EvaluationFunction func : m_activeConfiguration
					.getBulkFitnessFunctions().values())
				func.evaluate(chrom);
			i++;

			if (userRequestedStop()) {
				break;
			}
		}
		evaluateForFitness();
		evaluateForNovelty();

		// Restore the active mutation parameters
		// -------------------------------------
		m_activeConfiguration.getMutationOperators().remove(
				iecWeightMutationOperator);
		m_activeConfiguration.getMutationOperators().add(
				oldWeightMutationOperator);

		// For convienence, re-select the chromosomes previously selected by the
		// user
		// -------------------------------------
		for (Chromosome chrom : m_chromosomes) {
			if (chrom.isProtected()) {
				chrom.setIsSelectedForNextGeneration(true);
				chrom.unprotect();
			}
		}

		finishProgressListeners();
		fireGeneticEvents();
	}

	private void noveltyTask() {

		List<Chromosome> selectedChromosomes = new ArrayList<Chromosome>();
		List<Chromosome> novelChromosomes = new ArrayList<Chromosome>();

		removeUnselectedChromosomes();
		selectedChromosomes.addAll(m_chromosomes);

		// Mark the user slected chromosomes as Protected
		// -------------------------------------
		for (Chromosome chrom : m_chromosomes) {
			chrom.protect();
		}

		expandPopulation(m_activeConfiguration.getNoveltySearchPopulationSize());

		int i = 1;
		for (Chromosome chrom : m_chromosomes) {
			for (Entry<Long, EvaluationFunction> entry : m_activeConfiguration
					.getBulkFitnessFunctions().entrySet()) {
				entry.getValue().evaluate(chrom);
				updateProgressListeners(noveltyProgress(i),
						chromosomeIdArchive.size());
				i++;

				if (userRequestedStop()) {
					break;
				}
			}
		}

		evaluateForNovelty();

		// Collect individuals above the novelty threshold.
		// -------------------------------------
		int novelChromosomeTargetSize = m_activeConfiguration
				.getIECpopulationSize() - selectedChromosomes.size();
		for (int j = i; j < m_activeConfiguration
				.getShortTermNoveltySearchLimit(); j++) {
			// Evaluate the new population of of chromosomes.
			// -------------------------------------
			for (Chromosome chrom : m_chromosomes) {
				if (!novelChromosomes.contains(chrom)) {
					if (chrom.getNoveltyValue() >= getIECnoveltyThreshold()) {
						novelChromosomes.add(chrom);
					}
				}
			}

			if (novelChromosomes.size() >= novelChromosomeTargetSize) {
				break;
			}

			if (userRequestedStop()) {
				break;
			}

			updateProgressListeners(
					noveltyProgress(novelChromosomes.size(), j),
					chromosomeIdArchive.size());
			replaceLeastNovelIndividual();
		}

		// Since an insufficient number of individuals were discovered within
		// the run
		// limit, the most novel individuals will be added to meet the IEC
		// quota.
		// -------------------------------------
		Collections.sort(m_chromosomes, new ChromosomeNoveltyComparator(false,
				false));
		for (Chromosome candidate : m_chromosomes) {
			if (novelChromosomes.size() < novelChromosomeTargetSize) {
				if (!novelChromosomes.contains(candidate)
						&& !selectedChromosomes.contains(candidate)) {
					novelChromosomes.add(candidate);
				}
			} else {
				break;
			}
		}

		/*
		 * Contract the population of novel discoveries to the evaluation
		 * population size. // -------------------------------------
		 * Collections.sort(novelChromosomes, new
		 * ChromosomeNoveltyComparator(true, false)); int i = 0; while
		 * (novelChromosomes.size() >
		 * m_activeConfiguration.getIECpopulationSize()) { if
		 * (novelChromosomes.get(i).isProtected()) { i++; } else if
		 * (novelChromosomes.get(i).isSolution()) { i++; } else {
		 * novelChromosomes.remove(i); } } This block was removed to allow the
		 * user to see the set of all candidates that were above the specified
		 * IEC Novelty Threshold
		 */

		// For convienence, re-select the chromosomes previously selected by the
		// user
		// -------------------------------------
		for (Chromosome chrom : selectedChromosomes) {
			chrom.setIsSelectedForNextGeneration(true);
		}

		setChromosomes(selectedChromosomes);

		Collections.sort(novelChromosomes, new ChromosomeNoveltyComparator(
				false, false));
		appendToPopulation(novelChromosomes);

		finishProgressListeners();

		fireGeneticEvents();
	}

	private void optimizationTask(List<EvaluationFunction> funcs) {
		// Record the current population so that it can be reconstructed later
		// -------------------------------------
		List<Chromosome> population = new ArrayList<Chromosome>(m_chromosomes);

		removeUnselectedChromosomes();

		// Protect the leftover chromosomes
		// -------------------------------------
		protectAllChromosomes();
		Chromosome seed = fittestChromosome(null, funcs);
		Chromosome champ = null;

		// Change the active mutation parameters
		// -------------------------------------
		WeightMutationOperator iecWeightMutationOperator = new WeightMutationOperator(
				fitnessMutationRate, fitnessMutationPower);
		WeightMutationOperator oldWeightMutationOperator = null;
		for (MutationOperator operator : m_activeConfiguration
				.getMutationOperators()) {
			if (operator instanceof WeightMutationOperator) {
				oldWeightMutationOperator = (WeightMutationOperator) operator;
				break;
			}
		}
		m_activeConfiguration.getMutationOperators().remove(
				oldWeightMutationOperator);
		m_activeConfiguration.getMutationOperators().add(
				iecWeightMutationOperator);

		int i = 0;
		expandPopulation(m_activeConfiguration.getFitnessSearchPopulationSize());
		for (Chromosome chrom : m_chromosomes) {
			updateProgressListeners(fitnessProgress(i),
					chromosomeIdArchive.size());
			for (EvaluationFunction func : 
				m_activeConfiguration.getBulkFitnessFunctions().values()) // Evaluate fitness functions
				func.evaluateFitness(chrom);
			i++;

			if (userRequestedStop()) {
				break;
			}
		}

		// Steady-state optimization until a new champ is discovered
		// In a pareto setting, this is a new "dominant" solution, which is
		// better in every objective function.
		// -------------------------------------
		for (; i < m_activeConfiguration.getShortTermFitnessSearchLimit(); i++) {
			updateProgressListeners(fitnessProgress(i),
					chromosomeIdArchive.size());

			// This will update the min/max values in fittestChromosome
			champ = fittestChromosome(seed, funcs);
			if (seed.getId() != champ.getId()) {
				break;			
			}

			if (userRequestedStop()) {
				break;
			}

			replaceLeastFitIndividual();
		}

		// Restore the active mutation parameters
		// -------------------------------------
		m_activeConfiguration.getMutationOperators().remove(
				iecWeightMutationOperator);
		m_activeConfiguration.getMutationOperators().add(
				oldWeightMutationOperator);

		champ.setIsSelectedForNextGeneration(true);
		int indexOfSeed = population.indexOf(seed);
		if (indexOfSeed != -1) {
			population.add(indexOfSeed, champ);
			population.remove(seed);
		}

		setParetoDominance(m_chromosomes);
		evaluateForNovelty();

		lastFitnessPopulation.clear();
		System.out.println("recording last fitness population");
		lastFitnessPopulation.addAll(m_chromosomes);

		setPopulation(population);
		finishProgressListeners();

		fireGeneticEvents();
	}
	
	private void paretoOptimizationTask() {
		List<Chromosome> selectedChromosomes = new LinkedList<Chromosome>();
		List<Chromosome> paretoChromosomes = new LinkedList<Chromosome>();

		removeUnselectedChromosomes();
		selectedChromosomes.addAll(m_chromosomes);
		
		// Mark the user slected chromosomes as Protected
		// -------------------------------------
		for (Chromosome chrom : m_chromosomes) {
			chrom.protect();
		}
		
		expandPopulation(m_activeConfiguration.getFitnessSearchPopulationSize());

		Chromosome seed = fittestChromosome(); // This will force calculation of max and min values

		// If we have a selected chromosome, then we are going to adjust the POPV on the fly.
		// The configuration object will determine whether or not to actually change the POPV
		// based on the configuration file.
		if (selectedChromosomes.size() > 0) {
			m_activeConfiguration.adjustPOPV(selectedChromosomes.get(0).getFitnessValues());
		}
		
		int i = 1;
		for (Chromosome chrom : m_chromosomes) {
			for (Entry<Long, EvaluationFunction> entry : m_activeConfiguration
					.getBulkFitnessFunctions().entrySet()) {
				entry.getValue().evaluate(chrom);
				updateProgressListeners(noveltyProgress(i),
						chromosomeIdArchive.size());
				i++;

				if (userRequestedStop()) {
					break;
				}
			}
		}
		
		setParetoDominance(m_chromosomes);
		
		int paretoChromosomeTargetSize = m_activeConfiguration
				.getIECpopulationSize() - selectedChromosomes.size();
		
		// Steady state optimization until we reach a new pareto dominant solution
		for (; i < m_activeConfiguration.getShortTermFitnessSearchLimit(); i++) {
			updateProgressListeners(fitnessProgress(i),
					chromosomeIdArchive.size());
			
			Chromosome curFittest = fittestChromosome();
			// This will update the min/max values in fittestChromosome and compare only pareto Dominance
			// for determining if we break out here
			if (dominanceComparator.dominanceCompare(seed, curFittest) > 0) {
				//System.out.println("Found a new pareto dominant solution");
				//System.out.println(seed.getId() + " < " + curFittest.getId());
				break;
			}
			
			if (userRequestedStop()) {
				break;
			}

			replaceLeastFitIndividual();
			setParetoDominance(m_chromosomes);
		}

		for (Chromosome candidate : m_chromosomes) {
			if (paretoChromosomes.size() < paretoChromosomeTargetSize) {
				if (!paretoChromosomes.contains(candidate)
						&& !selectedChromosomes.contains(candidate)) {
					paretoChromosomes.add(candidate);
				}
			} else {
				break;
			}
		}

		// For convienence, re-select the chromosomes previously selected by the
		// user
		// -------------------------------------
		for (Chromosome chrom : selectedChromosomes) {
			chrom.setIsSelectedForNextGeneration(true);
		}

		setChromosomes(selectedChromosomes);

		//System.out.println("Sorting chromosomes before insertion");
		try { Collections.sort(paretoChromosomes, dominanceComparator); }
		catch (IllegalArgumentException e) {}
		Collections.reverse(paretoChromosomes);
		//System.out.println("Printing sorted list");
		//for (Chromosome chrom : paretoChromosomes)
		//	System.out.println(chrom.getId());
		List<Chromosome> dominantSolutions = new LinkedList<Chromosome>();
		for (Chromosome chrom : paretoChromosomes)
			if (chrom.dominatedBy.size() == 0)
				dominantSolutions.add(chrom);
		appendToPopulation(dominantSolutions);

		evaluateForNovelty();
		
		finishProgressListeners();

		fireGeneticEvents();
		
		HashMap<Long,Double> POPV = m_activeConfiguration.getPOPV();
		System.out.println("Printing optimal values (according to POPV)");
		for (Entry<Long,Double> entry : POPV.entrySet()) {
			// Find the coefficients as a percentage of values based on the POPV
			System.out.println(entry.getKey() + " : " + entry.getValue() + " : " + 
					(m_activeConfiguration.getMaxValue(entry.getKey()) + 
					m_activeConfiguration.getMinValue(entry.getKey())) * POPV.get(entry.getKey()));
		}
	}

	private void setParetoDominance(List<Chromosome> chromosomes) {
		for (Chromosome chrom1 : chromosomes) {
			for (Chromosome chrom2 : chromosomes) {
				setParetoDominance(chrom1, chrom2);
			}
		}
	}
	
	
	private void setParetoDominance(Chromosome chrom1, Chromosome chrom2) {
        // If we are dominated by the other chromosome, no since in testing pareto dominance
        if (chrom1.getId() != chrom2.getId() && !chrom1.isDominatedBy(chrom2) && !chrom1.isDominant(chrom2)) {
            int dominance = dominanceComparator.dominanceCompare(chrom1, chrom2);
            if (dominance == 1) {
                chrom1.setDominant(chrom2);
                chrom2.setDominatedBy(chrom1);
            } else if (dominance == -1) {
                chrom2.setDominant(chrom1);
                chrom1.setDominatedBy(chrom2);
            }
        }
	}

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
				iecTask();
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
				noveltyTask();
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
		private List<EvaluationFunction> _funcs;
		GenotypeFitnessTask(List<EvaluationFunction> funcs) {
			_funcs = funcs;
		}

		@Override
		public void run() {
			writeLock.lock();
			try {
				optimizationTask(_funcs);
			} finally {
				writeLock.unlock();
			}
		}
	}
	
	/**
	 * 
	 * @author jchristman
	 */
	private class GenotypeParetoTask extends GenotypeTask {
		
		@Override
		public void run() {
			writeLock.lock();
			try {
				paretoOptimizationTask();
			} finally {
				writeLock.unlock();
			}
		}
	}
}
