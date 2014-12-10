/*
 * Copyright (C) 2004 Derek James and Philip Tucker
 * 
 * This file is part of ANJI (Another NEAT Java Implementation).
 * 
 * ANJI is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 * 
 * Created on Apr 4, 2004 by Philip Tucker
 */
package com.anji.integration;

import java.util.HashMap;
import java.util.Map.Entry;

import org.jgap.Chromosome;
import org.jgap.Genotype;
import org.jgap.Specie;

import com.anji.util.XmlPersistable;

/**
 * Converts generation data between
 * <code>Genotype</code> and XML.
 *
 * @author Philip Tucker
 */
public class Generation implements XmlPersistable {

    /**
     * XML base tag
     */
    public final static String GENERATION_TAG = "generation";
    private Genotype genotype;
    private Long id;
    private String cachedXml;

    /**
     * @return @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * @param o
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Generation) {
            Generation other = (Generation) o;
            return id.equals(other.id);
        } else {
            return false;
        }
    }

    /**
     * for hibernate
     */
    @SuppressWarnings("unused")
	private Generation() {
        super();
    }

    /**
     * @param aGenotype chromosomes from this object make up generation.
     * @param anId of generation
     */
    public Generation(Genotype aGenotype, long anId) {
        genotype = aGenotype;
        id = new Long(anId);
        cacheXml();
    }

    /**
     * @see com.anji.util.XmlPersistable#toXml()
     */
    @Override
    public String toXml() {
        return cachedXml;
    }

    private void cacheXml() {
        StringBuilder result = new StringBuilder();

        Chromosome champ = genotype.getFittestChromosome();
        long speciesSize = genotype.getSpecies().size();
        long archiveSize = genotype.getCurrentNoveltyArchiveSize();

        result.append("<").append(GENERATION_TAG).append(" id=\"").append(id).append("\" >\n");
        result.append("  <champ id=\"").append(champ.getId()).append("\" >\n");
        result.append("    <fitness>\n");
        for (Entry<Long, Integer> entry : champ.getFitnessValues().entrySet()) {
        	result.append("      <objective obj_id=\"" + entry.getKey() + "\">");
        	result.append(entry.getValue()).append("</objective>\n");
        }
        result.append("    </fitness>\n");
        result.append("    <novelty>").append(champ.getNoveltyValue()).append("</novelty>\n");
        result.append("    <nodes>").append(champ.getNodes().size()).append("</nodes>\n");
        result.append("    <connections>").append(champ.getConnections().size()).append("</connections>\n");
        result.append("  </champ>\n");
        result.append("  <species count=\"").append(speciesSize).append("\" />\n");
        result.append("  <archive count=\"").append(archiveSize).append("\" />\n");
        result.append("</").append(GENERATION_TAG).append(">\n");

        cachedXml = result.toString();
    }

    @SuppressWarnings("unused")
	private void cacheXml_orig() {
        HashMap<Long, Integer> maxFitness = new HashMap<Long, Integer>();
        HashMap<Long, Integer> minFitness = new HashMap<Long, Integer>();
        int maxComplexity = Integer.MIN_VALUE;
        int minComplexity = Integer.MAX_VALUE;

        StringBuilder result = new StringBuilder();
        result.append("<").append(GENERATION_TAG).append(" id=\"").append(id).append(
                "\" >\n");

        HashMap<Long, Long> runningFitnessTotals = new HashMap<Long, Long>();
        int popSize = 0;
        long runningComplexityTotal = 0;

        for (Chromosome chrom : genotype.getChromosomes()) {
            HashMap<Long, Integer> thisChromFitness = chrom.getFitnessValues();
            for (Entry<Long, Integer> entry : thisChromFitness.entrySet()) {
            	runningFitnessTotals.put(entry.getKey(), runningFitnessTotals.get(entry.getKey() + entry.getValue()));
            	
            	// Test each objective functions fitness for max fitness in that dimension
            	Integer fitness = maxFitness.get(entry.getKey());
            	if ((fitness == null ? Integer.MIN_VALUE : fitness) < entry.getValue()) {
            		maxFitness.put(entry.getKey(), entry.getValue());
            	}
                
            	if ((fitness == null ? Integer.MAX_VALUE : fitness) > entry.getValue()) {
                    minFitness.put(entry.getKey(), entry.getValue());
                }
            }
            
            int thisChromComplexity = chrom.size();
            runningComplexityTotal += thisChromComplexity;
            popSize++;
            
            if (thisChromComplexity > maxComplexity) {
                maxComplexity = thisChromComplexity;
            }
            if (thisChromComplexity < minComplexity) {
                minComplexity = thisChromComplexity;
            }
        }
        result.append("<fitness>\n");
        result.append("<max>\n");
        for (Entry<Long, Integer> entry : maxFitness.entrySet())
        	result.append("    <objective obj_id=\"" + entry.getKey() + "\">").append(entry.getValue()).append("</objective>\n");
        result.append("</max>\n");
        result.append("<min>\n");
        for (Entry<Long, Integer> entry : minFitness.entrySet())
        	result.append("    <objective obj_id=\"" + entry.getKey() + "\">").append(entry.getValue()).append("</objective>");
        result.append("</min>\n");
        result.append("<avg>");
        for (Entry<Long, Long> entry : runningFitnessTotals.entrySet())
        	result.append("    <objective obj_id=\"" + entry.getKey() + "\">").append(entry.getValue() / popSize).append("</objective>");
        result.append("</avg>\n");
        result.append("</fitness>\n");

        result.append("<complexity>\n");
        result.append("<champ>").append(genotype.getFittestChromosome().size());
        result.append("</champ>\n");
        result.append("<max>").append(maxComplexity);
        result.append("</max>\n");
        result.append("<min>").append(minComplexity);
        result.append("</min>\n");
        result.append("<avg>");
        result.append((double) runningComplexityTotal / popSize);
        result.append("</avg>\n");
        result.append("</complexity>\n");

        for (Specie specie : genotype.getSpecies()) {
            result.append(specie.toXml());
        }
        result.append("</").append(GENERATION_TAG).append(">\n");

        cachedXml = result.toString();
    }

    /**
     * @see com.anji.util.XmlPersistable#getXmlRootTag()
     */
    @Override
    public String getXmlRootTag() {
        return GENERATION_TAG;
    }

    /**
     * @see com.anji.util.XmlPersistable#getXmld()
     */
    @Override
    public String getXmld() {
        return null;
    }

    /**
     * for hibernate
     *
     * @return unique id
     */
    @SuppressWarnings("unused")
	private Long getId() {
        return id;
    }

    /**
     * for hibernate
     *
     * @param aId
     */
    @SuppressWarnings("unused")
	private void setId(Long aId) {
        id = aId;
    }
}
