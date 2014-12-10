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
import java.util.List;
import java.util.Map;

/**
 * Bulk fitness functions are used to determine how optimal a group of solutions are relative to
 * each other. Bulk fitness functions can be useful (vs. normal fitness functions) when fitness
 * of a particular solution cannot be easily computed in isolation, but instead is dependent
 * upon the fitness of its fellow solutions that are also under consideration. This abstract
 * class should be extended and the <code>evaluate(List)</code> method implemented to evaluate
 * each of the Chromosomes given in an array and set their fitness values prior to returning.
 */
public interface EvaluationFunction extends Serializable {
	
    /**
     * Calculates and sets the fitness values on the given Candidate via its
     * setFitnessValue() method.
     * 
     * @param subject is a <code>Candidate</code> object for which the fitness 
     * values must be computed and set.
     */
    public void evaluateFitness(Chromosome subject);

    /**
     * Calculates and sets the fitness values on each of the given Candidates via their
     * setFitnessValue() method. Pass a reference to "this" to the chromosome so they can
     * grab the UUID off of the class.
     * 
     * @param subjects <code>Collection</code> contains <code>Candidate</code> objects for which
     * the fitness values must be computed and set.
     */
    public void evaluateFitness(List<Chromosome> subjects);
    
    /**
     * The maximum fitness score an individual can achieve.  This value based one {@link FitnessStrategy#getMaxFitnessValue()}.
     * @return The maximum theoretic fitness value that can be returned by this function.
     */
    public int getMaxFitnessValue();

    /**
     * 
     * @param subjects 
     */
    public void evaluateNovelty(List<Chromosome> subjects);

    /**
     * 
     * @return 
     */
    public double getNoveltyThreshold();
    
    /**
     * 
     * @param aNewNoveltyThreshold 
     */
    public void setNoveltyThreshold(double aNewNoveltyThreshold);
    
    /**
     * 
     * @return 
     */
    public int getNoveltyArchiveSize();

    /**
     * Calculates and sets the fitness values on each of the given Chromosomes via their
     * setFitnessValue() method.
     * 
     * @param subjects <code>List</code> contains <code>Chromosome</code> objects for which
     * the fitness values must be computed and set.
     */
    public void evaluate(List<Chromosome> subjects);

    /**
     * Calculates and sets the fitness values on each of the given Chromosomes via their
     * setFitnessValue() method.
     * 
     * @param subject 
     */
    public void evaluate(Chromosome subject);
    
    /**
     * 
     * @return 
     */
    public Map<Chromosome, BehaviorVector> getAllPointsVisited();

    /**
     * Returns a unique ID associated with this evaluation function. Necessary for the chromosomes
     * to distinguish between the different EvaluationFunctions that are modifying the fitness
     * values. Each EvaluationFunction MUST have a unique ID.
     * 
     * @return
     */
    public long getEvaluationFunctionUUID();
    
    public String shortName();

}
