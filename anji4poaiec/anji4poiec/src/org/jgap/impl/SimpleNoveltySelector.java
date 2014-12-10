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
 * created by Philip Tucker on Jan 13, 2004
 */
package org.jgap.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgap.Chromosome;
import org.jgap.ChromosomeNoveltyComparator;
import org.jgap.Configuration;

/**
 * Selects chromosomes based directly on novelty value, as opposed to a statistical probability.
 * @author Philip Tucker
 */
public class SimpleNoveltySelector extends NoveltySelector {

    private List<Chromosome> chromosomes = new ArrayList<Chromosome>();

    /**
     * Add <code>a_chromosomeToAdd</code> to set of chromosomes to be evaluated.
     * @param a_activeConfigurator
     * @param a_chromosomeToAdd
     */
    @Override
    protected void add(Configuration a_activeConfigurator, Chromosome a_chromosomeToAdd) {
        chromosomes.add(a_chromosomeToAdd);
    }

    /**
     * Returns the <code>a_howManyToSelect</code> chromosomes with highest speciated novelty.
     * @param a_activeConfiguration
     * @param a_howManyToSelect
     * @return <code>List</code> contains <code>Chromosome</code> objects
     */
    @Override
    protected List<Chromosome> select(Configuration a_activeConfiguration, int a_howManyToSelect) {
        Collections.sort(chromosomes, new ChromosomeNoveltyComparator(false /* asc */, true /* speciated novelty*/));
        List<Chromosome> result = new ArrayList<Chromosome>(a_howManyToSelect);
        for (Chromosome next : chromosomes) {
            if (result.size() < a_howManyToSelect) {
                result.add(next);
            }
        }
        return result;
    }

    /**
     * empty chromosome list
     */
    @Override
    protected void emptyImpl() {
        chromosomes.clear();
    }
}
