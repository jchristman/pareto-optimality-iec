/**
 * ----------------------------------------------------------------------------|
 * Created on Apr 12, 2003
 */
package com.anji.util;

import java.util.*;

import org.jgap.BehaviorVector;
import org.jgap.EvaluationFunction;
import org.jgap.Chromosome;

/**
 * @author Philip Tucker
 */
@SuppressWarnings("serial")
public class DummyBulkFitnessFunction implements EvaluationFunction {

    private Random rand = null;
    private long UUID;

    /**
     * ctor
     *
     * @param newRand
     */
    public DummyBulkFitnessFunction(Random newRand, long UUID) {
        rand = newRand;
        this.UUID = UUID;
    }

    /**
     * ctor
     */
    public DummyBulkFitnessFunction() {
        rand = new Random();
    }

    @Override
    public void evaluateFitness(Chromosome a_subject) {
    	int dummy = rand.nextInt();
        a_subject.setFitnessValue(dummy, this);
    }

    /**
     * @param aSubjects
     * @see org.jgap.EvaluationFunction#evaluateFitness(java.util.List)
     */
    @Override
    public void evaluateFitness(List<Chromosome> aSubjects) {
        for (Chromosome c : aSubjects) {
            evaluateFitness(c);
        }
    }

    /**
     * @see org.jgap.EvaluationFunction#getMaxFitnessValue()
     */
    @Override
    public int getMaxFitnessValue() {
        return 100;
    }

    @Override
    public void evaluateNovelty(List<Chromosome> subjects) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getNoveltyThreshold() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setNoveltyThreshold(double aNewNoveltyThreshold) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getNoveltyArchiveSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void evaluate(List<Chromosome> subjects) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void evaluate(Chromosome subject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<Chromosome, BehaviorVector> getAllPointsVisited() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	@Override
	public long getEvaluationFunctionUUID() {
		return UUID;
	}

	@Override
	public String shortName() {
		return "";
	}

}
