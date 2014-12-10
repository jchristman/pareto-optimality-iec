/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.evaluationFunctions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jgap.BehaviorVector;
import org.jgap.Chromosome;
import org.jgap.EvaluationFunction;

import edu.ucf.eplex.mazeNavigation.model.Maze;
import edu.ucf.eplex.mazeNavigation.model.Path;
import edu.ucf.eplex.poaiecFramework.domain.Candidate;
import edu.ucf.eplex.poaiecFramework.domain.EvaluationDomain;

/**
 *
 * @author Brian Woolley (brian.woolley at ieee.org)
 */
@SuppressWarnings("serial")
public abstract class FitnessStrategy implements EvaluationFunction {
    protected final Maze theMaze;
    protected EvaluationDomain<Path> _domain;

    /**
     * 
     * @param maze 
     */
    public FitnessStrategy(Maze maze, EvaluationDomain<Path> domain) {
        theMaze = maze;
        _domain = domain;
    }
    
    public static int staticGetMaxFitnessValue() {
    	return MAX_FITNESS;
    }
    
    public int getMaxFitnessValue() {
        return MAX_FITNESS;
    }

	public abstract int evaluateFitness(Chromosome subject, Path path);
	
	public void evaluateFitness(Chromosome subject) {
		Candidate cand = _domain.getCandidate(subject);
		_domain.evaluate(cand);
		int fitness = evaluateFitness(subject, _domain.getResult(cand));
		
		// Set fitness to MIN_FITNESS if it's less than that
        fitness = (fitness < MIN_FITNESS ? MIN_FITNESS : fitness);
        fitness = (fitness > MAX_FITNESS ? MAX_FITNESS : fitness);
		subject.setFitnessValue(fitness, this);
	}

	public void evaluateFitness(List<Chromosome> subjects) {
		for (Chromosome subject : subjects)
			evaluateFitness(subject);
	}

	public void evaluateNovelty(List<Chromosome> subjects) {
		int count = 0;
		for (Chromosome chrom : subjects) {
			int novelty = _domain.evaluateNovelty(_domain.getCandidate(chrom), (count == 0));
			chrom.setNoveltyValue(novelty);
			count++;
		}
	}

	public double getNoveltyThreshold() {
		return _domain.getNoveltyThreshold();
	}

	public void setNoveltyThreshold(double aNewNoveltyThreshold) {
		_domain.setNoveltyThreshold(aNewNoveltyThreshold);
	}

	public int getNoveltyArchiveSize() {
		return _domain.getNoveltyArchiveSize();
	}

	public void evaluate(List<Chromosome> subjects) {
		evaluateFitness(subjects);
	}

	public void evaluate(Chromosome subject) {
		evaluateFitness(subject);
	}

	public Map<Chromosome, BehaviorVector> getAllPointsVisited() {
		Map<Chromosome, BehaviorVector> result = new HashMap<Chromosome, BehaviorVector>();
		for (Entry<Candidate, BehaviorVector> entry : _domain.getAllPointsVisited().entrySet())
			result.put(entry.getKey().getChromosome(), entry.getValue());
		return result;
	}
	
    public static final int MIN_FITNESS = 1;
    public static final int MAX_FITNESS = 100;

}
