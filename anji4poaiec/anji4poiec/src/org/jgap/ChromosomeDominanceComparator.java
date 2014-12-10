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

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Enables sorting of chromosomes by their Pareto Dominance.
 *
 * @author Joshua Christman
 */
public class ChromosomeDominanceComparator implements Comparator<Chromosome> {
	Configuration _configuration;

    public ChromosomeDominanceComparator(Configuration configuration) {
    	_configuration = configuration;
    }

    /**
     * Joshua Christman - Checks to see if one dominates the other
     * 
     * TODO: Add reference point based pareto dominance. Also add check for the POPV
     * 
     * @param c1
     * @param c2
     * @return
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Chromosome c1, Chromosome c2) {
    	int paretoResult = paretoFrontCompare(c1, c2);
    	if (paretoResult != 0)
    		return paretoResult;
		else { // If neither is dominant, return which one is closer to the POPV
			HashMap<Long,Double> POPV = _configuration.getPOPV();
			HashMap<Long,Double> coeffs = new HashMap<Long,Double>();
			for (Entry<Long,Double> entry : POPV.entrySet()) {
				if (entry == null) continue;
				// Find the coefficients as a percentage of values based on the POPV
				coeffs.put(entry.getKey(), (_configuration.getMaxValue(entry.getKey()) + 
						_configuration.getMinValue(entry.getKey())) * POPV.get(entry.getKey()));
			}
			//System.out.println(c1.getId() + "," + distance(c1, coeffs) + " --- " + c2.getId() + "," + distance(c2, coeffs));
			return (distance(c1, coeffs) <= distance(c2, coeffs) ? -1 : 1);
		}
    }
    
    public int paretoFrontCompare(Chromosome c1, Chromosome c2) {
    	int c1dominatedCount = c1.numDominatedBy();
    	int c2dominatedCount = c2.numDominatedBy();
    	
    	if (c1dominatedCount < c2dominatedCount) 		return 1;  // c1 is in a better pareto front
    	else if (c2dominatedCount < c1dominatedCount) 	return -1; // c2 is in a better pareto front
    	else											return 0;  // They are in the same pareto front
    }
    
    public int dominanceCompare(Chromosome c1, Chromosome c2) {
    	boolean c1Dominated = true;
    	boolean c2Dominated = true;
		for (Long funcId : c1.getFitnessValues().keySet()) {
			// Check to see if we dominate the current one in any way
			if (c1.getFitnessValues().get(funcId) > c2.getFitnessValues().get(funcId)) {
				c1Dominated = false;
			}
			// Check to see if the current one is dominated
			if (c1.getFitnessValues().get(funcId) < c2.getFitnessValues().get(funcId)) {
				c2Dominated = false;
			}
		}
		if (c1Dominated) // If c1 is dominated, than c2 is bigger
			return -1;
		else if (c2Dominated) // If c2 is dominated, than c1 is bigger
			return 1;
		else
			return 0;
    }

    public int dominanceCompare(Chromosome c1, Chromosome c2,
			List<EvaluationFunction> funcs) {
    	boolean c1Dominated = true;
    	boolean c2Dominated = true;
		for (EvaluationFunction func : funcs) {
			long funcId = func.getEvaluationFunctionUUID();
			// Check to see if we dominate the current one in any way
			if (c1.getFitnessValues().get(funcId) > c2.getFitnessValues().get(funcId)) {
				c1Dominated = false;
			}
			// Check to see if the current one is dominated
			if (c1.getFitnessValues().get(funcId) < c2.getFitnessValues().get(funcId)) {
				c2Dominated = false;
			}
		}
		if (c1Dominated) // If c1 is dominated, than c2 is bigger
			return -1;
		else if (c2Dominated) // If c2 is dominated, than c1 is bigger
			return 1;
		else
			return 0;
	}
    
    private double distance(Chromosome chrom, HashMap<Long,Double> coeffs) {
    	double denominator = 0;
    	for (Entry<Long, Double> entry : coeffs.entrySet()) {
    		denominator += Math.pow(entry.getValue(), 2);
    	}
    	denominator = Math.sqrt(denominator);
    	
    	double numerator = 0;
    	for (Entry<Long, Integer> entry : chrom.getFitnessValues().entrySet()) {
    		numerator += coeffs.get(entry.getKey()) * entry.getValue();
    	}
    	
    	return Math.abs(numerator) / denominator;
    }

}
