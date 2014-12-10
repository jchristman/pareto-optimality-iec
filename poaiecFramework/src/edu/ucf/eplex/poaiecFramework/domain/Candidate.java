/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.poaiecFramework.domain;

import java.util.HashMap;

import com.anji.integration.Activator;
import com.anji.neat.NeatConfiguration;

import org.jgap.Chromosome;
import org.jgap.EvaluationFunction;

/**
 *
 * @author Brian Woolley (brian.woolley at ieee.org)
 */
public class Candidate implements Comparable<Candidate> {

    private final Chromosome m_chromosome;
    private final NeatConfiguration m_config;

    /**
     * 
     * @param aChromosome
     * @param config 
     */
    public Candidate(Chromosome aChromosome, NeatConfiguration config) {
        m_chromosome = aChromosome;
        m_config = config;
    }

	public double[] next(double[] input) {
		return getANN().next(input);
	}

	public Activator getANN() {
        return m_config.createANN(m_chromosome);
    }
    
    /**
     * @return Long unique identifier for chromosome; useful for <code>hashCode()</code> and
     * persistence
     */
    public Long getId() {
        return m_chromosome.getId();
    }
    
    public Chromosome getChromosome() {
        return m_chromosome;
    }

    public int nodeCount() {
        return m_chromosome.countNodes();
    }

    public int connectionCount() {
        return m_chromosome.countLinks();
    }

    /**
     * Retrieves the fitness value of this Chromosome, as determined by the active fitness function.
     * @return a positive integer value representing the fitness of this Chromosome, or -1 if
     * fitness function has not yet assigned a fitness value to this Chromosome.
     */
    public HashMap<Long, Integer> getFitnessValues() {
        return m_chromosome.getFitnessValues();
    }

    /**
     * @return int fitness value adjusted for fitness sharing according to <a
     * href="http://nn.cs.utexas.edu/downloads/papers/stanley.ec02.pdf>NEAT </a> paradigm.
     */
    public HashMap<Long, Double> getNormalizedFitnessValues() {
        return m_chromosome.getNormalizedFitnessValues();
    }

    /**
     * Sets the fitness value of this Chromosome. This method is for use by bulk fitness functions
     * and should not be invoked from anything else. This is the raw fitness value, before species
     * fitness sharing.
     * 
     * @param a_newFitnessValue a positive integer representing the fitness of this Chromosome. if
     * 0, fitness is set as 1.
     */
    public void setFitnessValue(int a_newFitnessValue, EvaluationFunction func) {
        m_chromosome.setFitnessValue(a_newFitnessValue, func);
    }

    public int getNoveltyValue() {
        return m_chromosome.getNoveltyValue();
    }

    public int getSpeciatedNoveltyValue() {
        return m_chromosome.getSpeciatedNoveltyValue();
    }

    public void setNoveltyValue(int a_newNoveltyValue) {
        m_chromosome.setNoveltyValue(a_newNoveltyValue);
    }

    /**
     * Returns a string representation of this Chromosome, useful for some display purposes.
     * 
     * @return A string representation of this Chromosome.
     */
    @Override
    public String toString() {
        return m_chromosome.toString();
    }

    /**
     * @return primary parent ID; this is the dominant parent for chromosomes spawned by crossover,
     * and the only parent for chromosomes spawned by cloning
     */
    public Long getPrimaryParentId() {
        return m_chromosome.getPrimaryParentId();
    }

    /**
     * @return secondary parent ID; this is the recessive parent for chromosomes spawned by
     * crossover, and null for chromosomes spawned by cloning
     */
    public Long getSecondaryParentId() {
        return m_chromosome.getSecondaryParentId();
    }

    /**
     * Retrieves whether this Chromosome has been selected by the natural selector to continue to
     * the next generation.
     * 
     * @return true if this Chromosome has been selected, false otherwise.
     */
    public boolean isSelectedByTheUser() {
        return m_chromosome.isSelectedForNextGeneration();
    }

    public boolean toggelSelectedByTheUser() {
        if (isSelectedByTheUser()) {
            m_chromosome.unprotect();
            m_chromosome.setIsSelectedForNextGeneration(false);
        } else {
            m_chromosome.protect();
            m_chromosome.setIsSelectedForNextGeneration(true);
        }
        return isSelectedByTheUser();
    }

    public boolean isSolution() {
        return m_chromosome.isSolution();
    }

    public int getANNconnectionCount() {
        return getANN().getConnectionCount();
    }

    public void setAsSolution(boolean solution) {
        m_chromosome.setAsSolution(solution);
    }
    
    public int getDominatedBy() {
    	return m_chromosome.numDominatedBy();
    }
    
    public int getDominates() {
    	return m_chromosome.numDominates();
    }
    
    /**
     * 
     * @param o 
     * @return 
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Candidate) {
            Candidate other = (Candidate) o;
            return m_chromosome.equals(other.m_chromosome);
        } else {
            return false;
        }
    }

    /**
     * Compares the given Candidate to this Candidate. This candidate is considered to be "less
     * than" the other candidate based on the value of their chromosome id's
     * 
     * @param other 
     * @return a negative number if this candidates id "less than" the other candidate, zero if
     * they are equal to each other, and a positive number if this candidate id is "greater than" the
     * other candidate id.
     */
    public int compareTo(Candidate other) {       
        return m_chromosome.compareTo(other.m_chromosome);
    }

    @Override
    public int hashCode() {
        return m_chromosome.hashCode();
    }
    
    public HashMap<Long,EvaluationFunction> getFuncNames() {
    	return m_config.getBulkFitnessFunctions();
    }
}
