package org.jgap.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgap.Chromosome;
import org.jgap.ChromosomeDominanceComparator;
import org.jgap.Configuration;

/**
 * A basic implementation of the selection operator used in the Nondominated Sorting
 * Genetic Algorithm (NSGA) that uses Pareto Dominance as a ranking operator.
 */
public class MultiObjectiveNSSelector extends FitnessSelector {

	private List<Chromosome> chromosomes = new ArrayList<Chromosome>();

	@Override
	protected void add(Configuration config, Chromosome c) {
		chromosomes.add(c);
	}

	@Override
	protected void emptyImpl() {
		chromosomes.clear();
	}

	/**
	 * This function uses the ChromosomeDominanceComparator to sort the chromosomes
	 * according to their Pareto Dominance
	 */
	@Override
	protected List<Chromosome> select(Configuration config, int numToSurvive) {
		Collections.sort(chromosomes, new ChromosomeDominanceComparator(config));
        List<Chromosome> result = new ArrayList<Chromosome>(numToSurvive);
        for (Chromosome next : chromosomes) {
            if (result.size() < numToSurvive) {
                result.add(next);
            }
        }
        return result;
	}

}
