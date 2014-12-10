/*
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
 * Created on Feb 3, 2003 by Philip Tucker
 */
package org.jgap.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jgap.ChromosomeMaterial;
import org.jgap.Configuration;
import org.jgap.InvalidConfigurationException;
import org.jgap.ReproductionOperatorInterface;
import org.jgap.Specie;
import org.jgap.impl.SimpleNoveltySelector;
import org.jgap.impl.WeightedNoveltySelector;



/**
 * Abstract class for reproduction operators.  Handles intra-species breeding and someday will handle
 * inter-species breeding.  Each specie gets a number of offspring relative to its fitness, following 
 * <a href="http://nn.cs.utexas.edu/downloads/papers/stanley.ec02.pdf">NEAT</a> paradigm.
 * @author Philip Tucker
 */
public abstract class NoveltyReproductionOperator implements ReproductionOperatorInterface {

    private float slice = 0.0f;

    /**
     * The reproduce method will be invoked on each of the reproduction operators referenced by the current 
     * Configuration object during the evolution phase. Operators are given an opportunity to run in the order
     * they are added to the Configuration.  Iterates over species and determines each one's number of offspring
     * based on fitness, then passes control to subclass <code>reproduce( final Configuration config, 
     * final List parents, int numOffspring, List offspring )</code> method to perform specific reproduction.
     *
     * @param config The current active genetic configuration.
     * @param parentSpecies <code>List</code> contains <code>Specie</code> objects containing parent 
     * chromosomes from which to produce offspring.
     * @param offspring <code>List</code> contains offspring <code>ChromosomeMaterial</code> objects; this
     * method adds new offspring to this list
     * @throws InvalidConfigurationException
     * @see ReproductionOperator#reproduce(Configuration, List, int, List)
     */
    @Override
    final public void reproduce(final Configuration config, final List<Specie> parentSpecies, List<ChromosomeMaterial> offspring)
            throws InvalidConfigurationException {
        if (parentSpecies.isEmpty()) {
            throw new IllegalStateException("no parent species from which to produce offspring");
        }

        if (config.isSteadyState()) {
            if (offspring.size() < 1) {
                Specie parentSpecie = null;

                if (config.getNoveltySelector() instanceof WeightedNoveltySelector) {
                    double threshold = Math.random();
                    double totalSpeciesNovelty = 0;
                    double ladder = 0.0;
                    for (Specie specie : parentSpecies) {
                        totalSpeciesNovelty += specie.getNoveltyValue();
                    }
                    Collections.shuffle(parentSpecies);
                    for (Specie currSpecie : parentSpecies) {
                        ladder += currSpecie.getNoveltyValue() / totalSpeciesNovelty;
                        if (ladder >= threshold) {
                            parentSpecie = currSpecie;
                        }
                    }
                }
                if (config.getNoveltySelector() instanceof SimpleNoveltySelector) {
                    for (Specie currSpecie : parentSpecies) {
                        if (parentSpecie == null) {
                            parentSpecie = currSpecie;
                        }
                        if (currSpecie.getNoveltyValue() > parentSpecie.getNoveltyValue()) {
                            parentSpecie = currSpecie;
                        }
                    }
                }
                // Select a species based on the average novelty
                reproduce(config, parentSpecie.getChromosomes(), 1, offspring);
                if (offspring.size() > 1) {
                    System.out.println("WARNING  Reproduction operator attempted to create " + offspring.size() + " offspring");
                    Collections.shuffle(offspring);
                    while (offspring.size() > 1) {
                        offspring.remove(0);
                    }
                }
            }
        } else {
            int targetNewOffspringCount = (int) ((config.getIECpopulationSize() * getSlice()) + 0.5);

            List<ChromosomeMaterial> newOffspring = new ArrayList<ChromosomeMaterial>(targetNewOffspringCount);

            // calculate total novlety
            double totalSpeciesNovelty = 0;
            for (Specie specie : parentSpecies) {
                totalSpeciesNovelty += specie.getNoveltyValue();
            }

            // reproduce from each specie relative to its percentage of total novelty
            for (Specie specie : parentSpecies) {
                double percentNovelty = specie.getNoveltyValue() / totalSpeciesNovelty;
                int numSpecieOffspring = (int) ((percentNovelty * targetNewOffspringCount) + 0.5);
                reproduce(config, specie.getChromosomes(), numSpecieOffspring, newOffspring);
            }

            // allow for rounding error - adjust by removing or cloning random offspring
            while (newOffspring.size() > targetNewOffspringCount) {
                int idx = config.getRandomGenerator().nextInt(newOffspring.size());
                newOffspring.remove(idx);
            }
            while (newOffspring.size() < targetNewOffspringCount) {
                int idx = config.getRandomGenerator().nextInt(newOffspring.size());
                ChromosomeMaterial clonee = (ChromosomeMaterial) newOffspring.get(idx);
                newOffspring.add(clonee.clone(null));
            }

            offspring.addAll(newOffspring);
        }
    }

    /**
     * @return float slice of population this reproduction operator will fill with offspring
     */
    @Override
    final public float getSlice() {
        return slice;
    }

    /**
     * @param aSlice slice of population this reproduction operator will fill with offspring
     */
    @Override
    final public void setSlice(float aSlice) {
        this.slice = aSlice;
    }
}
