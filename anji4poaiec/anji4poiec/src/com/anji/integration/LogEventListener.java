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
 * created by Philip Tucker
 */
package com.anji.integration;

import com.anji.util.Configurable;
import com.anji.util.Properties;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.EvaluationFunction;
import org.jgap.Genotype;
import org.jgap.event.GeneticEvent;
import org.jgap.event.GeneticEventListener;

/**
 * Writes log events to log4j framework.
 *
 * @author Philip Tucker
 */
public class LogEventListener implements GeneticEventListener, Configurable {

    private static final Logger logger = Logger.getLogger(LogEventListener.class.getName());
    private Configuration config = null;

    /**
     * @param newConfig JGAP configuration.
     */
    public LogEventListener(Configuration newConfig) {
        config = newConfig;
    }

    /**
     * no initialization parameters
     *
     * @param p
     */
    @Override
    public void init(Properties p) {
        // noop
    }

    /**
     * @param event
     * <code>GeneticEvent.GENOTYPE_EVOLVED_EVENT</code> is the only event
     * handled; writes species count and stats of all fittest chromosomes.
     */
    @Override
    public void geneticEventFired(GeneticEvent event) {
        if (GeneticEvent.GENOTYPE_EVOLVED_EVENT.equals(event.getEventName())) {
            Genotype genotype = (Genotype) event.getSource();
            Chromosome fittest = genotype.getFittestChromosome();
            HashMap<Long, Double> maxFitnessValues = new HashMap<Long, Double>();
            for (EvaluationFunction func : config.getBulkFitnessFunctions().values())
            	maxFitnessValues.put(func.getEvaluationFunctionUUID(), (double) func.getMaxFitnessValue());
              
            HashMap<Long, Integer> fitness = fittest.getFitnessValues();
            logger.log(Level.INFO, "species count: {0}", genotype.getSpecies().size());
            int maxFitnessCount = 0;
            for (Chromosome c : genotype.getChromosomes()) {
            	for (Entry<Long, Integer> entry : c.getFitnessValues().entrySet()) {
            		Integer val = entry.getValue();
	                if ((val == null ? null : val.doubleValue()) == maxFitnessValues.get(entry.getKey())) {
	                    logger.log(Level.INFO, "max: id={0} score={1} nodes={2} conns={3}", new Object[]{c.getId(), fitness, c.getNodes().size(), c.getConnections().size()});
	                    ++maxFitnessCount;
	                    break;
	                }
            	}
            }
            logger.log(Level.INFO, "# chromosomes with max fitness: {0}", maxFitnessCount);
            logger.log(Level.INFO, "champ: id={0} score={1} nodes={2} conns={3}", new Object[]{fittest.getId(), fitness, fittest.getNodes().size(), fittest.getConnections().size()});
        }
    }
}
