/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jgap;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bwoolley
 */
public abstract class Selector {

    protected float survivalRate = 0.0f;
    protected int numChromosomes = 0;
    protected boolean elitism = false;
    protected int elitismMinSpecieSize = 6;
    protected List<Chromosome> elite = new ArrayList<Chromosome>();

    /**
     * If elitism is enabled, places appropriate chromosomes in <code>elite</code> list.  Elitism follows
     * methodolofy in <a href="http://nn.cs.utexas.edu/downloads/papers/stanley.ec02.pdf">NEAT</a>.  Passes
     * everything else to subclass <code>add( Configuration config, Chromosome c )</code> method.
     * @param config
     * @param chroms <code>List</code> contains Chromosome objects
     */
    public abstract void add(Configuration config, List<Chromosome> chroms);

    /**
     * @param config
     * @param c chromosome to add to selection pool
     */
    protected abstract void add(Configuration config, Chromosome c);

    /**
     * clear pool of candidate chromosomes
     * @see NaturalSelector#emptyImpl()
     */
    public void empty() {
        numChromosomes = 0;
        elite.clear();
        emptyImpl();
    }

    /**
     * @see NaturalSelector#empty()
     */
    protected abstract void emptyImpl();

    /**
     * @return minimum size a specie must be to support an elite chromosome
     */
    public int getElitismMinSpecieSize() {
        return elitismMinSpecieSize;
    }

    /**
     * @return float survival rate
     */
    public float getSurvivalRate() {
        return survivalRate;
    }

    /**
     * Select a given number of Chromosomes from the pool that will move on
     * to the next generation population. This selection should be guided by
     * the fitness values.
     * Elite chromosomes always survivie, unless there are more elite than the survival rate permits.  In this case,
     * elite with highest fitness are chosen.  Remainder of survivors are determined by subclass
     * <code>select( Configuration config, int numToSurvive )</code> method.
     * @param config
     * @return List contains Chromosome objects
     */
    public abstract List<Chromosome> select(Configuration config);

    /**
     * @param config
     * @param numToSurvive
     * @return <code>List</code> contains <code>Chromosome</code> objects, those that have survived; size
     * of this list should be <code>numToSurvive</code>, unless fewer than that number of chromosomes have
     * been added to selector
     */
    protected abstract List<Chromosome> select(Configuration config, int numToSurvive);

    /**
     * @param b true if elitisim is to be enabled
     */
    public void setElitism(boolean b) {
        elitism = b;
    }

    /**
     * @param i minimum size a specie must be to support an elite chromosome
     */
    public void setElitismMinSpecieSize(int i) {
        elitismMinSpecieSize = i;
    }

    /**
     * @param aSurvivalRate
     */
    public void setSurvivalRate(float aSurvivalRate) {
        if (aSurvivalRate < 0.0 || aSurvivalRate > 1.0) {
            throw new IllegalArgumentException("0.0 <= survivalRate <= 1.0");
        }
        this.survivalRate = aSurvivalRate;
    }
}
