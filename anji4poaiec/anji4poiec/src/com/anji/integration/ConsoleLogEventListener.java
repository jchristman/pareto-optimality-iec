/*
 * Copyright (C) 2004  Derek James and Philip Tucker
 *
 * This file is part of ANJI (Another NEAT Java Implementation).
 *
 * ANJI is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * created by Philip Tucker
 */
package com.anji.integration;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map.Entry;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Genotype;
import org.jgap.event.GeneticEvent;
import org.jgap.event.GeneticEventListener;

import com.anji.util.Configurable;
import com.anji.util.Properties;

/**
 * Writes log events to stdout.
 * @author Philip Tucker
 */
public class ConsoleLogEventListener implements GeneticEventListener, Configurable {

private PrintStream out = null;
@SuppressWarnings("unused")
private Configuration config = null;

/**
 * @param newConfig
 * @see ConsoleLogEventListener#ConsoleLogEventListener(Configuration, PrintStream)
 */
public ConsoleLogEventListener(Configuration newConfig) {
	this(newConfig, System.out);
}

/**
 * @param newConfig
 * @param newOut
 */
public ConsoleLogEventListener(Configuration newConfig, PrintStream newOut) {
	config = newConfig;
	out = newOut;
}

/**
 * @param p configuration parameters
 */
public void init(Properties p) {
	// noop
}

/**
 * @param event <code>GeneticEvent.GENOTYPE_EVOLVED_EVENT</code> is the
 * only event handled; writes species count and stats of fittest chromosome.
 */
public void geneticEventFired(GeneticEvent event) {
	if ( GeneticEvent.GENOTYPE_EVOLVED_EVENT.equals( event.getEventName() ) ) {
		Genotype genotype = (Genotype) event.getSource();
		Chromosome fittest = genotype.getFittestChromosome();
		
		HashMap<Long, Integer> fitness = fittest.getFitnessValues();
		out.println( "species count: " + genotype.getSpecies().size() );
		out.println( "fittest chromosome: " + fittest.getId() + ", score == " + fitnessToString(fitness) + 
				" and # genes == " + fittest.size() );	
	}
}

private String fitnessToString(HashMap<Long, Integer> fitness) {
	String ret = "(";
	for (Entry<Long, Integer> entry : fitness.entrySet())
		ret += "{" + entry.getKey() + ":" + entry.getValue() + "},";
	return ret.substring(0, ret.length() - 1) + ")";
}

}

