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

import com.anji.neat.*;
import com.anji.nn.ActivationFunctionType;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

import org.w3c.dom.Node;
import org.w3c.dom.ranges.RangeException;

/**
 * Chromosomes represent potential solutions and consist of a fixed-length collection of genes.
 * Each gene represents a discrete part of the solution. Each gene in the Chromosome may be
 * backed by a different concrete implementation of the Gene interface, but all genes with the
 * same innovation ID must share the same concrete implementation across Chromosomes within a
 * single population (genotype).
 */
public class Chromosome implements Comparable<Chromosome>, Serializable {

    /**
     * default ID
     */
    public final static Long DEFAULT_ID = new Long(-1);
    private Long m_id = DEFAULT_ID;
    private String m_idString;
    /**
     * Genetic material contained in this chromosome.
     */
    private ChromosomeMaterial m_material = null;
    private SortedSet<Allele> m_alleles = null;
    /**
     * Keeps track of whether or not this Chromosome has been selected by the natural selector to
     * move on to the next generation.
     */
    protected boolean m_isSelectedForNextGeneration = false;
    
    protected List<Chromosome> dominatedBy = new LinkedList<Chromosome>();
    protected List<Chromosome> dominates = new LinkedList<Chromosome>();
    
//    /**
//     * Stores the fitness value of this Chromosome as determined by the active fitness function. A
//     * value of -1 indicates that this field has not yet been set with this Chromosome's fitness
//     * values (valid fitness values are always positive).
//     */
//    protected int m_fitnessValue = -1;
    protected HashMap<Long, Integer> m_fitnessValues = new HashMap<Long,Integer>();
    protected int m_noveltyValue = -1;
    private Specie m_specie = null;
    private static final long serialVersionUID = 3570976570964579587L;

    /**
     * ctor for hibernate
     */
    @SuppressWarnings("unused")
	private Chromosome() {
        m_material = new ChromosomeMaterial();
    }

    public Chromosome(Node xmlChromosome) {
        fromXml(xmlChromosome);
    }

    /**
     * this should only be called when a chromosome is being created from persistence; otherwise,
     * the ID should be generated by <code>a_activeConfiguration</code>.
     * 
     * @param a_material Genetic material to be contained within this Chromosome instance.
     * @param an_id unique ID of new chromosome
     */
    public Chromosome(ChromosomeMaterial a_material, Long an_id) {
        // Sanity checks: make sure the parameters are all valid.
        if (a_material == null) {
            throw new IllegalArgumentException("Chromosome material can't be null.");
        }

        setId(an_id);
        m_material = a_material;
        m_alleles = Collections.unmodifiableSortedSet(m_material.getAlleles());
        associateAllelesWithChromosome();
    }

    private void associateAllelesWithChromosome() {
        for (Allele allele : m_alleles) {
            allele.setChromosome(this);
        }
    }

    /**
     * Calculates compatibility distance between this and <code>target</code> according to <a
     * href="http://nn.cs.utexas.edu/downloads/papers/stanley.ec02.pdf">NEAT </a> speciation
     * methodology. It is generic enough that the alleles do not have to be nodes and connections.
     * 
     * @param target
     * @param parms
     * @return distance between this object and <code>target</code>
     * @see ChromosomeMaterial#distance(ChromosomeMaterial, SpeciationParms)
     */
    public double distance(Chromosome target, SpeciationParms parms) {
        return m_material.distance(target.m_material, parms);
    }

    /**
     * @return Long unique identifier for chromosome; useful for <code>hashCode()</code> and
     * persistence
     */
    public Long getId() {
        return m_id;
    }

    /**
     * for hibernate
     * @param id
     */
    private void setId(Long id) {
        m_id = id;
        m_idString = "Chromosome " + m_id;
    }

    /**
     * Returns the size of this Chromosome (the number of alleles it contains). A Chromosome's size
     * is constant and will never change.
     * 
     * @return The number of alleles contained within this Chromosome instance.
     */
    public int size() {
        return m_alleles.size();
    }

    public int countNodes() {
        return getNodes().size();
    }

    public List<NeuronAllele> getNodes() {
        List<NeuronAllele> nodes = new ArrayList<NeuronAllele>();
        for (Allele allele : m_alleles) {
            if (allele instanceof NeuronAllele) {
                nodes.add((NeuronAllele) allele);
            }
        }
        return nodes;
    }

    public int countLinks() {
        return getConnections().size();
    }

    public List<ConnectionAllele> getConnections() {
        List<ConnectionAllele> connections = new ArrayList<ConnectionAllele>();
        for (Allele allele : m_alleles) {
            if (allele instanceof ConnectionAllele) {
                connections.add((ConnectionAllele) allele);
            }
        }
        return connections;
    }

    /**
     * @return clone with primary parent ID of this chromosome and the same genetic material.
     */
    public ChromosomeMaterial cloneMaterial() {
        return m_material.clone(getId());
    }

    /**
     * @return SortedSet alleles, sorted by innovation ID
     */
    public SortedSet<Allele> getAlleles() {
        return m_alleles;
    }

    /**
     * @param alleleToMatch
     * @return Gene gene with same innovation ID as
     * <code>geneToMatch</code, or <code>null</code> if none match
     */
    public Allele findMatchingGene(Allele alleleToMatch) {
        for (Allele allele : m_alleles) {
            if (allele.equals(alleleToMatch)) {
                return allele;
            }
        }
        return null;
    }

    /**
     * Retrieves the fitness value of this Chromosome, as determined by the active fitness function.
     * @return a positive integer value representing the fitness of this Chromosome, or -1 if
     * fitness function has not yet assigned a fitness value to this Chromosome.
     * 
     * Editor: Joshua Christman - Adjusted to return the MOEA ArrayList
     */
    public HashMap<Long, Integer> getFitnessValues() {
        return m_fitnessValues;
    }

    /**
     * @return int fitness value adjusted for fitness sharing according to <a
     * href="http://nn.cs.utexas.edu/downloads/papers/stanley.ec02.pdf>NEAT </a> paradigm.
     * 
     * Editor: Joshua Christman - Adjusted to return the MOEA ArrayList
     */
    public HashMap<Long,Double> getNormalizedFitnessValues() {
    	HashMap<Long,Double> normalizedFitnessValues;
        if (m_specie == null) {
        	normalizedFitnessValues = new HashMap<Long,Double>();
        	for (Entry<Long, Integer> entry : getFitnessValues().entrySet())
        		normalizedFitnessValues.put(entry.getKey(),entry.getValue().doubleValue());
        	return normalizedFitnessValues;
        }
        normalizedFitnessValues = m_specie.normalizeChromosomeFitness(this);
        return normalizedFitnessValues;
    }

    /**
     * Sets the fitness value of this Chromosome. This method is for use by bulk fitness functions
     * and should not be invoked from anything else. This is the raw fitness value, before species
     * fitness sharing.
     * 
     * @param a_newFitnessValue a positive integer representing the fitness of this Chromosome. if
     * 0, fitness is set as 1.
     * 
     * Editor: Joshua Christman - Adjusted to use the MOEA ArrayList
     */
	public void setFitnessValue(int a_newFitnessValue, EvaluationFunction func) {
        if (a_newFitnessValue > 0)
        	m_fitnessValues.put(func.getEvaluationFunctionUUID(), a_newFitnessValue);
        else
        	throw new RangeException((short) -1, "Negative fitness values are invalid");
    }
	
    public int getNoveltyValue() {
        return m_noveltyValue;
    }

    public int getSpeciatedNoveltyValue() {
        if (m_specie == null) {
            return getNoveltyValue();
        }
        int result = (int) (m_specie.getChromosomeNoveltyValue(this) + 0.5);
        return (result == 0) ? 1 : result;
    }

    public void setNoveltyValue(int a_newNoveltyValue) {
        if (a_newNoveltyValue > 0) {
            m_noveltyValue = a_newNoveltyValue;
        } else {
            m_noveltyValue = 1;
        }
    }

    /**
     * Returns a string representation of this Chromosome, useful for some display purposes.
     * 
     * @return A string representation of this Chromosome.
     */
    @Override
    public String toString() {
        return m_idString;
    }

    /**
     * Compares this Chromosome against the specified object. The result is true if and the argument
     * is an instance of the Chromosome class and has a set of genes equal to this one.
     * 
     * @param o 
     * @return true if the objects are the same, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Chromosome) {
            return compareTo((Chromosome) o) == 0;
        } else {
            return false;
        }
    }

    /**
     * Retrieve a hash code for this Chromosome.
     * 
     * @return the hash code of this Chromosome.
     */
    @Override
    public int hashCode() {
        return m_id.hashCode();
    }

    /**
     * Compares the given Chromosome to this Chromosome. This chromosome is considered to be "less
     * than" the given chromosome if it has a fewer number of genes or if any of its gene values
     * (alleles) are less than their corresponding gene values in the other chromosome.
     * 
     * @param other 
     * @return a negative number if this chromosome is "less than" the given chromosome, zero if
     * they are equal to each other, and a positive number if this chromosome is "greater than" the
     * given chromosome.
     */
    @Override
    public int compareTo(Chromosome other) {
        return m_id.compareTo(other.m_id);
    }
    
    public void setDominatedBy(Chromosome other) {
    	if (!dominatedBy.contains(other)) dominatedBy.add(other);
    }
    
    public boolean isDominatedBy(Chromosome other) {
    	return dominatedBy.contains(other);
    }
    
    public int numDominatedBy() {
    	return dominatedBy.size();
    }
    
    public void setDominant(Chromosome other) {
    	if (!dominates.contains(other)) dominates.add(other);
    }
    
    public boolean isDominant(Chromosome other) {
    	return dominates.contains(other);
    }
    
    public int numDominates() {
    	return dominates.size();
    }

    /**
     * Sets whether this Chromosome has been selected by the natural selector to continue to the
     * next generation.
     * 
     * @param a_isSelected true if this Chromosome has been selected, false otherwise.
     */
    public void setIsSelectedForNextGeneration(boolean a_isSelected) {
        m_isSelectedForNextGeneration = a_isSelected;
    }

    /**
     * Retrieves whether this Chromosome has been selected by the natural selector to continue to
     * the next generation.
     * 
     * @return true if this Chromosome has been selected, false otherwise.
     */
    public boolean isSelectedForNextGeneration() {
        return m_isSelectedForNextGeneration;
    }

    /**
     * should only be called from Specie; assigns this chromosome to <code>aSpecie</code>; throws
     * exception if chromosome is added to a specie twice
     * 
     * @param aSpecie
     */
    void setSpecie(Specie aSpecie) {
//        if (m_specie != null) {
//            throw new IllegalStateException("chromosome can't be added to " + aSpecie
//                    + ", already a member of specie " + m_specie);
//        }
        m_specie = aSpecie;
    }

    /**
     * for hibernate
     * @param id
     */
    @SuppressWarnings("unused")
	private void setPrimaryParentId(Long id) {
        m_material.setPrimaryParentId(id);
    }

    /**
     * for hibernate
     * @param id
     */
    @SuppressWarnings("unused")
	private void setSecondaryParentId(Long id) {
        m_material.setSecondaryParentId(id);
    }

    /**
     * @return this chromosome's specie
     */
    public Specie getSpecie() {
        return m_specie;
    }

    /**
     * @return primary parent ID; this is the dominant parent for chromosomes spawned by crossover,
     * and the only parent for chromosomes spawned by cloning
     */
    public Long getPrimaryParentId() {
        return m_material.getPrimaryParentId();
    }

    /**
     * @return secondary parent ID; this is the recessive parent for chromosomes spawned by
     * crossover, and null for chromosomes spawned by cloning
     */
    public Long getSecondaryParentId() {
        return m_material.getSecondaryParentId();
    }

    /**
     * for hibernate
     * @param aAlleles
     */
    @SuppressWarnings("unused")
	private void setAlleles(SortedSet<Allele> aAlleles) {
        m_material.setAlleles(aAlleles);
        m_alleles = Collections.unmodifiableSortedSet(aAlleles);
        associateAllelesWithChromosome();
    }
    private boolean m_isSolution = false;

    public void setAsSolution(boolean solution) {
        m_isSolution = solution;
    }

    public boolean isSolution() {
        return m_isSolution;
    }
    private boolean m_isProtected = false;

    public void protect() {
        m_isProtected = true;
    }

    public void unprotect() {
        m_isProtected = false;
    }

    public boolean isProtected() {
        return m_isProtected;
    }

    private void fromXml(Node xmlChromosome) {
        if (xmlChromosome.getAttributes().getNamedItem(CHROMOSOME_ID_TAG).getNodeValue().matches("[0-9]+")) {
            m_id = Long.parseLong(xmlChromosome.getAttributes().getNamedItem(CHROMOSOME_ID_TAG).getNodeValue());
        }

        Node node = xmlChromosome.getFirstChild();
        while (node != null) {
            if (node.getNodeName().equalsIgnoreCase(SELECTION_TAG)) {
                m_isSelectedForNextGeneration = Boolean.parseBoolean(node.getTextContent());
            }

            if (node.getNodeName().equalsIgnoreCase(SOLUTION_TAG)) {
                m_isSolution = Boolean.parseBoolean(node.getTextContent());
            }

            // Editor: Joshua Christman - modified to parse an XML structure in an MOEA sense. The tags should now be arranged as such:
            // <fitness>
            //     <objective>5</objective>
            //     <objective>2</objective>
            // </fitness>
            if (node.getNodeName().equalsIgnoreCase(FITNESS_TAG)) {
            	Node objectives = node.getFirstChild();
            	while (objectives != null) {
            		if (objectives.getNodeName().equalsIgnoreCase(OBJECTIVE_TAG)) {
            			if (objectives.getTextContent().matches("[0-9]+")) {
            				m_fitnessValues.put(Long.parseLong(node.getNodeValue()),Integer.parseInt(node.getTextContent()));
            			}
            		}
            		
            		objectives.getNextSibling();
            	}
            }

            if (node.getNodeName().equalsIgnoreCase(NOVELTY_TAG)) {
                if (node.getTextContent().matches("[0-9]+")) {
                    m_noveltyValue = Integer.parseInt(node.getTextContent());
                }
            }

            if (node.getNodeName().equalsIgnoreCase(NODE_TAG)) {
                if (node.getTextContent().matches("[0-9]+")) {
                    if (m_alleles == null) {
                        m_alleles = new TreeSet<Allele>();
                    }
                    for (int i = 0; i < Integer.parseInt(node.getTextContent()); i++) {
                        NeuronGene neuronGene = new NeuronGene(NeuronType.OUTPUT, System.currentTimeMillis()+m_alleles.size(), ActivationFunctionType.TANH);
                        m_alleles.add(new NeuronAllele(neuronGene));
                    }
                }
            }

            if (node.getNodeName().equalsIgnoreCase(CONNECTION_TAG)) {
                if (node.getTextContent().matches("[0-9]+")) {
                    if (m_alleles == null) {
                        m_alleles = new TreeSet<Allele>();
                    }
                    for (int i = 0; i < Integer.parseInt(node.getTextContent()); i++) {
                        m_alleles.add(new ConnectionAllele(new ConnectionGene(System.currentTimeMillis()+m_alleles.size(), DEFAULT_ID, DEFAULT_ID)));
                    }
                }
            }

            node = node.getNextSibling();
        }
    }

    /*
     *   <chromosome id="120" >
     *      <selection>true</selection>
     *      <solution>false</solution>
     *      <fitness>91</fitness>
     *      <novelty>49</novelty>
     *      <nodes>13</nodes>
     *      <connections>22</connections>
     *  </chromosome>
     */
    public String toXml() {
        StringBuilder result = new StringBuilder();
        
        result.append(indent(3)).append(open(CHROMOSOME_TAG, CHROMOSOME_ID_TAG + "=\"" + getId() + "\""));
        result.append(indent(4)).append(textContentElement(SELECTION_TAG, isSelectedForNextGeneration()));
        result.append(indent(4)).append(textContentElement(SOLUTION_TAG, isSolution()));
        
        // Editor: Joshua Christman - Modified to correctly write the MOEA objective values to XML
        StringBuilder fitness = new StringBuilder("\n");
        for (Entry<Long,Integer> entry : getFitnessValues().entrySet()) {
        	fitness.append(indent(5)).append(openNoNewline(OBJECTIVE_TAG, OBJECTIVE_ID_TAG + "=\"" + entry.getKey() + "\""));
        	fitness.append(entry.getValue());
        	fitness.append(close(OBJECTIVE_TAG));
        }
        
        result.append(indent(4)).append(textContentElement(FITNESS_TAG, fitness.append(indent(4))));
        
        result.append(indent(4)).append(textContentElement(NOVELTY_TAG, getNoveltyValue()));
        result.append(indent(4)).append(textContentElement(NODE_TAG, getNodes().size()));
        result.append(indent(4)).append(textContentElement(CONNECTION_TAG, getConnections().size()));
        result.append(indent(3)).append(close(CHROMOSOME_TAG));
        
        return result.toString();
    }

    @SuppressWarnings("unused")
	private String open(String label) {
        return new StringBuilder().append("<").append(label).append(">\n").toString();
    }

    private String openNoNewline(String label, String attributes) {
        return new StringBuilder().append("<").append(label).append(" ").append(attributes).append(">").toString();
    }
    
    private String open(String label, String attributes) {
        return new StringBuilder().append("<").append(label).append(" ").append(attributes).append(">\n").toString();
    }

    private String close(String label) {
        return new StringBuilder().append("</").append(label).append(">\n").toString();
    }

    private String textContentElement(String label, Object value) {
        return new StringBuilder().append("<").append(label).append(">").append(value).append("</").append(label).append(">\n").toString();
    }

    private String indent(int x) {
        StringBuilder indention = new StringBuilder();
        for (int i = 0; i < x; i++) {
            indention.append("    ");
        }
        return indention.toString();
    }
    
    public final static String CHROMOSOME_TAG = "chromosome";
    public final static String CHROMOSOME_ID_TAG = "id";
    public final static String SELECTION_TAG = "selection";
    public final static String UNSELECTED_TAG = "unselected";
    public final static String SOLUTION_TAG = "solution";
    public final static String FITNESS_TAG = "fitness";
    public final static String OBJECTIVE_TAG = "objective";
    public final static String OBJECTIVE_ID_TAG = "obj_id";
    public final static String NOVELTY_TAG = "novelty";
    public final static String NODE_TAG = "nodes";
    public final static String CONNECTION_TAG = "connections";
}
