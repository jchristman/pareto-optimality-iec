/*
 * Copyright 2001-2003 Neil Rotstan
 * Copyright (C) 2004  Derek James and Philip Tucker
 *
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Modified on Feb 3, 2003 by Philip Tucker
 */
package org.jgap.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jgap.Chromosome;
import org.jgap.ChromosomeDominanceComparator;
import org.jgap.Configuration;
import org.jgap.Selector;
import org.jgap.Specie;

/**
 * Natural selectors are responsible for actually selecting a specified number
 * of Chromosome specimens from a population, using the fitness values as a
 * guide. Usually fitness is treated as a statistic probability of survival,
 * not as the sole determining factor. Therefore, Chromosomes with higher
 * fitness values are more likely to survive than those with lesser fitness
 * values, but it's not guaranteed.
 */
public abstract class FitnessSelector extends Selector {
	
    /**
     * If elitism is enabled, places appropriate chromosomes in <code>elite</code> list.  Elitism follows
     * methodolofy in <a href="http://nn.cs.utexas.edu/downloads/papers/stanley.ec02.pdf">NEAT</a>.  Passes 
     * everything else to subclass <code>add( Configuration config, Chromosome c )</code> method.
     * @param config
     * @param chroms <code>List</code> contains Chromosome objects
     */
    @Override
    public final void add(Configuration config, List<Chromosome> chroms) {
        numChromosomes += chroms.size();

        for (Chromosome c : chroms) {
            Specie specie = c.getSpecie();
            if (elitism && specie != null
                    && specie.getChromosomes().size() >= getElitismMinSpecieSize()
                    && specie.getFittest().equals(c)) {
                c.setIsSelectedForNextGeneration(true);
                elite.add(c);
            } else {
                add(config, c);
            }
        }
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
    @Override
    public List<Chromosome> select(Configuration config) {
        List<Chromosome> result = new ArrayList<Chromosome>(elite);

        int numToSelect = (int) ((numChromosomes * getSurvivalRate()) + 0.5);

        if (result.size() > numToSelect) {
            Collections.sort(result, new ChromosomeDominanceComparator(config));
            int numToRemove = result.size() - numToSelect;
            for (int i = 0; i < numToRemove; ++i) {
                result.remove(0);
            }
        } else if (result.size() < numToSelect) {
            int moreToSelect = numToSelect - result.size();
            List<Chromosome> more = select(config, moreToSelect);
            result.addAll(more);
        }

        return result;
    }
}
