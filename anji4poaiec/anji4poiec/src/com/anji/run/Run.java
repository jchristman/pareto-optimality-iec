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
package com.anji.run;

import com.anji.integration.Generation;
import com.anji.util.Configurable;
import com.anji.util.Properties;

import java.util.*;

import org.jgap.BehaviorVector;
import org.jgap.Chromosome;
import org.jgap.Genotype;
import org.jgap.event.GeneticEvent;
import org.jgap.event.GeneticEventListener;

/**
 * Hibernate-able run object.
 *
 * @author Philip Tucker
 */
public class Run implements GeneticEventListener, Configurable {

    private static final String RUN_KEY = "run.name";
    private int currentGenerationNumber = 1;
    /**
     * for hibernate
     */
    private Long id;
    private String name;
    private List<Generation> generations = new ArrayList<Generation>();
    private Properties props;
    private Calendar startTime = Calendar.getInstance();
    private HashMap<Long, Map<Chromosome, BehaviorVector>> allPointsVisited = 
    		new HashMap<Long,Map<Chromosome, BehaviorVector>>();
    private int evaluations = 0;

// TODO population
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
        if (o instanceof Run) {
            Run other = (Run) o;
            return id.equals(other.id);
        } else {
            return false;
        }
    }

    /**
     * should call
     * <code>init()</code> after this ctor, unless it's called from hibernate
     */
    public Run() {
        // no-op
    }

    /**
     * @param aName
     */
    public Run(String aName) {
        name = aName;
    }

    /**
     * Add new generation to run.
     *
     * @param genotype
     */
    public void addGeneration(Genotype genotype) {
        evaluations = genotype.getSeriesEvaluationCount();
        generations.add(new Generation(genotype, currentGenerationNumber++));
        allPointsVisited = genotype.getAllPointsVisited();
    }

    /**
     * @return @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * @param event
     * @see
     * org.jgap.event.GeneticEventListener#geneticEventFired(org.jgap.event.GeneticEvent)
     */
    @Override
    public void geneticEventFired(GeneticEvent event) {
        Genotype genotype = (Genotype) event.getSource();
        if (GeneticEvent.GENOTYPE_EVALUATED_EVENT.equals(event.getEventName())) {
            addGeneration(genotype);
        }
//        if (GeneticEvent.RUN_COMPLETED_EVENT.equals(event.getEventName())) {
//            allPointsVisited.addAll(genotype.getAllPointsVisited());
//        }
    }

    /**
     * @return unique run ID
     */
    public String getName() {
        return name;
    }

    /**
     * @return generations orderd by generation number
     */
    public List<Generation> getGenerations() {
        return generations;
    }
    
    /**
     * 
     * @return the number of evaluations in this run
     */
    public int getEvaluationCount() {
        return evaluations;
    }

    /**
     * for hibernate
     *
     * @param aGenerations
     */
    @SuppressWarnings("unused")
	private void setGenerations(List<Generation> aGenerations) {
        generations = aGenerations;
    }

    /**
     * for hibernate
     *
     * @param aName
     */
    @SuppressWarnings("unused")
	private void setName(String aName) {
        name = aName;
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

    /**
     * @param aProps 
     * @see com.anji.util.Configurable#init(com.anji.util.Properties)
     */
    @Override
    public void init(Properties aProps) throws Exception {
        props = aProps;
        name = props.getProperty(RUN_KEY);
    }

    /**
     * @return properties
     */
    public Properties getProps() {
        return props;
    }

    /**
     * @return time when this object was created
     */
    public Calendar getStartTime() {
        return startTime;
    }

    public HashMap<Long, Map<Chromosome, BehaviorVector>> getAllPointsVisited() {
        return allPointsVisited;
    }
}
