/*
 * Copyright (C) 2004 Derek James and Philip Tucker
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
 * Created on Feb 3, 2003 by Philip Tucker
 */
package org.jgap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * Species are reproductively isolated segments of a population. They are used to ensure
 * diversity in the population. This can protect innovation, and also serve to maintain a
 * broader search space, avoiding being trapped in local optima.
 * 
 * @author Philip Tucker
 */
public class Specie {

    /**
     * XML base tag
     */
    public final static String SPECIE_TAG = "specie";
    /**
     * XML ID tag
     */
    public final static String ID_TAG = "id";
    /**
     * XML count tag
     */
    public final static String COUNT_TAG = "count";
    /**
     * XML chromosome tag
     */
    public final static String CHROMOSOME_TAG = "chromosome";
    /**
     * XML fitness tag
     */
    public final static String FITNESS_TAG = "fitness";
    /**
     * chromosomes active in current population; these logically should be a <code>Set</code>,
     * but we use a <code>List</code> to make random selection easier, specifically in
     * <code>ReproductionOperator</code>
     */
    private List<Chromosome> chromosomes = new ArrayList<Chromosome>();
    private Chromosome representative = null;
    private SpeciationParms speciationParms = null;
    //private Chromosome fittest = null;
    private ArrayList<Chromosome> paretoOptimalSet = new ArrayList<Chromosome>();
    private Chromosome mostNovel = null;
    
    private ArrayList<Double> paretoOptimalityPointingVector = new ArrayList<Double>();
    
    /**
     * Added to track the min of every objective to express the best possible solution
     */
    private ArrayList<Integer> idealPoint = new ArrayList<Integer>();
    
    /**
     * for hibernate
     */
    private Long id;

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return representative.hashCode();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        Specie other = (Specie) o;
        return representative.equals(other.representative);
    }

    /**
     * @return unique ID; this is chromosome ID of representative
     */
    public Long getRepresentativeId() {
        return representative.getId();
    }

    /**
     * Create new specie from representative. Representative is first member of specie, and all
     * other members of specie are determined by compatibility with representative. Even if
     * representative dies from population, a reference is kept here to determine specie membership.
     * @param aSpeciationParms
     * @param aRepresentative
     */
    public Specie(SpeciationParms aSpeciationParms, Chromosome aRepresentative) {
        representative = aRepresentative;
        aRepresentative.setSpecie(this);
        chromosomes.add(aRepresentative);
        speciationParms = aSpeciationParms;
    }

    /**
     * @return representative chromosome
     */
    protected Chromosome getRepresentative() {
        return representative;
    }

    /**
     * @param aChromosome
     * @return true if chromosome is added, false if chromosome already is a member of this specie
     */
    public boolean add(Chromosome aChromosome) {
        if (!match(aChromosome)) {
            throw new IllegalArgumentException("chromosome does not match specie: " + aChromosome);
        }
        if (chromosomes.contains(aChromosome)) {
            return false;
        }
        aChromosome.setSpecie(this);
        paretoOptimalSet = new ArrayList<Chromosome>();
        mostNovel = null;
        return chromosomes.add(aChromosome);
    }

    /**
     * @return all chromosomes in specie
     */
    public List<Chromosome> getChromosomes() {
        return Collections.unmodifiableList(chromosomes);
    }

    /**
     * remove all chromosomes from this specie except <code>keepers</code>
     * 
     * @param keepers <code>Collection</code> contains chromosome objects
     */
    public void cull(Collection<Chromosome> keepers) {
        paretoOptimalSet = new ArrayList<Chromosome>();
        mostNovel = null;
        chromosomes.retainAll(keepers);
        paretoOptimalSet.retainAll(keepers); // Also keep the keepers in the Pareto Set
    }

    /**
     * @return true iff specie contains no active chromosomes in population
     */
    public boolean isEmpty() {
        return chromosomes.isEmpty();
    }

    /**
     * @param aChromosome
     * @return double adjusted fitness for aChromosome relative to this specie
     * @throws IllegalArgumentException if chromosome is not a member if this specie
     * 
     * Editor: Joshua Christman - calculates the ideal point and scales every objective independently per the paper
     * "An evolutionary many-objective optimization algorithm using reference-point based non-dominated sorting approach, 
     *  part I: Solving problems with box constraints"
     */
    public HashMap<Long, Double> normalizeChromosomeFitness(Chromosome aChromosome) {
        if (aChromosome.getFitnessValues().size() == 0) {
            throw new IllegalArgumentException("chromosome's fitness has not been set: "
                    + aChromosome.toString());
        }
        if (chromosomes.contains(aChromosome) == false) {
            throw new IllegalArgumentException("chromosome not a member of this specie: "
                    + aChromosome.toString());
        }

        if (idealPoint.size() == 0) {
        	calculateIdealPoint(aChromosome.getFitnessValues().size());
        }
        
        HashMap<Long,Double> scaledFitness = new HashMap<Long,Double>();
        int index = 0;
        for (Entry<Long,Integer> entry : aChromosome.getFitnessValues().entrySet()) {
        	scaledFitness.put(entry.getKey(), entry.getValue().doubleValue() / 
        			idealPoint.get(index).doubleValue()); // Scale every objective by the ideal point
        	index++;
        }
        
        return scaledFitness;
    }
    
    /**
     * Used to calculate the Ideal Point of the specie based on multiple objectives
     * 
     * @param objDimensions
     */
    private void calculateIdealPoint(int objDimensions) {
    	for (int i = 0; i < objDimensions; i++) {
    		int min = Integer.MAX_VALUE;
	    	for (Chromosome chrom : chromosomes) {
	    		int objValue = chrom.getFitnessValues().get(i);
	    		if (objValue < min)
	    			min = objValue;
	    	}
	    	idealPoint.add(min);
    	}
    }

    /**
     * @return average raw fitness (i.e., not adjusted for specie size) of all chromosomes in specie
     */
//    public double getFitnessValue() {
//        ArrayList<Long> totalRawFitness = new ArrayList<Long>();
//        Iterator iter = chromosomes.iterator();
//        while (iter.hasNext()) {
//            Chromosome aChromosome = (Chromosome) iter.next();
//            if (aChromosome.getFitnessValues().size() < 0) {
//                throw new IllegalStateException("chromosome's fitness has not been set: "
//                        + aChromosome.toString());
//            } else if (aChromosome.getFitnessValues().size() > totalRawFitness.size()) {
//            	
//            }
//            for (int objVal : aChromosome.getFitnessValues()) {
//            	
//            }
//        }
//
//        return (double) totalRawFitness / chromosomes.size();
//    }

    /**
     * @return Chromosome pareto optimal set
     */
    public synchronized ArrayList<Chromosome> getFittest() {
    	for (Chromosome chromosome : chromosomes) {
    		if (checkDominance(chromosome)) { // Update the Pareto Optimal Set
    			paretoOptimalSet.add(chromosome);
    		}
    	}

        return paretoOptimalSet;
    }
    
    /**
     * 
     * @param aChromosome
     * @return boolean based on if the chromosome is dominant against anything in the pareto set
     */
    private boolean checkDominance(Chromosome aChromosome) {
    	for (Chromosome chromosome : paretoOptimalSet) { // Check against everyone in the pareto optimal set for dominance
    		if (chromosome != aChromosome) {
    			int dominance = aChromosome.compareTo(chromosome);
    			if (dominance == 1) {
    				paretoOptimalSet.remove(chromosome);
    				return true;
    			} else if (dominance == -1) {
    				return false;
    			}
    		}
    	}
    	
		return true;
    }
    
    public boolean inParetoOptimalSet(Chromosome aChromosome) {
    	return paretoOptimalSet.contains(aChromosome);
    }

    /**
     * @param aChromosome
     * @return double adjusted fitness for aChromosome relative to this specie
     * @throws IllegalArgumentException if chromosome is not a member if this specie
     */
    public double getChromosomeNoveltyValue(Chromosome aChromosome) {
        if (aChromosome.getNoveltyValue() < 0) {
            throw new IllegalArgumentException("chromosome's novelty has not been set: "
                    + aChromosome.toString());
        }
        if (chromosomes.contains(aChromosome) == false) {
            throw new IllegalArgumentException("chromosome not a member of this specie: "
                    + aChromosome.toString());
        }

        return ((double) aChromosome.getNoveltyValue()) / chromosomes.size();
    }

    /**
     * @return Chromosome fittest in this specie
     */
    public synchronized Chromosome getMostNovel() {
        for (Chromosome next : chromosomes) {
            if (mostNovel == null) {
                mostNovel = next;
            } else if (next.getNoveltyValue() > mostNovel.getNoveltyValue()) {
                mostNovel = next;
            } else {
                // Do nothing...
            }
        }
        return mostNovel;
    }

    /**
     * @return average raw fitness (i.e., not adjusted for specie size) of all chromosomes in specie
     */
    public double getNoveltyValue() {
        long totalRawNovelty = 0;
        for (Chromosome aChromosome : chromosomes) {
            if (aChromosome.getNoveltyValue() < 0) {
                throw new IllegalStateException("chromosome's novelty has not been set: "
                        + aChromosome.toString());
            }
            totalRawNovelty += aChromosome.getNoveltyValue();
        }

        return (double) totalRawNovelty / chromosomes.size();
    }

    /**
     * @param aChromosome
     * @return boolean true iff compatibility difference between
     * <code>aChromosome</code? and representative
     * is less than speciation threshold
     */
    public boolean match(Chromosome aChromosome) {
        return (representative.distance(aChromosome, speciationParms) < speciationParms.getSpeciationThreshold());
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer result = new StringBuffer("Specie ");
        result.append(getRepresentativeId());
        return result.toString();
    }

    /**
     * @return String XML representation of object according to <a
     * href="http://nevt.sourceforge.net/">NEVT </a>.
     */
    public String toXml() {
        StringBuffer result = new StringBuffer();
        result.append("<").append(SPECIE_TAG).append(" ").append(ID_TAG).append("=\"");
        result.append(getRepresentativeId()).append("\" ").append(COUNT_TAG).append("=\"");
        result.append(getChromosomes().size()).append("\">\n");
        Iterator<Chromosome> chromIter = getChromosomes().iterator();
        while (chromIter.hasNext()) {
            Chromosome chromToStore = (Chromosome) chromIter.next();
            result.append("    <").append(CHROMOSOME_TAG).append(" ").append(ID_TAG).append("=\"");
            result.append(chromToStore.getId()).append(FITNESS_TAG).append("=\"(");
             
            for (Entry<Long,Integer> entry : chromToStore.getFitnessValues().entrySet()) {
	            result.append("{").append(entry.getKey()).append(",").append(entry.getValue()).append("},");
            }
            result.deleteCharAt(result.length() - 1);
            result.append(")\" />\n");
        }
        result.append("</").append(SPECIE_TAG).append(">\n");
        return result.toString();
    }

    /**
     * for hibernate
     * @return unique id
     */
    @SuppressWarnings("unused")
	private Long getId() {
        return id;
    }

    /**
     * for hibernate
     * @param aId
     */
    @SuppressWarnings("unused")
	private void setId(Long aId) {
        id = aId;
    }

	/**
	 * @return the paretoOptimalityPointingVector
	 */
	public ArrayList<Double> getPOPV() {
		return paretoOptimalityPointingVector;
	}

	/**
	 * @param paretoOptimalityPointingVector the paretoOptimalityPointingVector to set
	 */
	public void setPOPV(ArrayList<Double> paretoOptimalityPointingVector) {
		this.paretoOptimalityPointingVector = paretoOptimalityPointingVector;
	}
}
