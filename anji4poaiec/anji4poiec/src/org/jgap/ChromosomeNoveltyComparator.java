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
 * Created on Sept 19, 2011 by Brian Woolley
 */
package org.jgap;

import java.util.Comparator;

/**
 * Enables sorting of chromosomes by their novelty score.
 *
 * @author Brian Woolley
 */
public class ChromosomeNoveltyComparator implements Comparator<Chromosome> {

    private boolean isAscending = true;
    private boolean isSpeciated = true;

    /**
     * @see ChromosomeDominanceComparator#ChromosomeFitnessComparator(boolean,
     * boolean)
     */
    public ChromosomeNoveltyComparator() {
        this(true, true);
    }

    /**
     * Enables sorting of chromosomes in order of fitness. Ascending order if
     * <code>ascending</code> is true, descending otherwise. Uses fitness
     * adjusted for species fitness sharing if
     * <code>speciated</code> is true, raw fitness otherwise.
     *
     * @param ascending
     * @param speciated
     */
    public ChromosomeNoveltyComparator(boolean ascending, boolean speciated) {
        super();
        isAscending = ascending;
        isSpeciated = speciated;
    }

    /**
     * @param c1
     * @param c2
     * @return
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Chromosome c1, Chromosome c2) {
        int novelty1 = (isSpeciated ? c1.getSpeciatedNoveltyValue() : c1.getNoveltyValue());
        int novelty2 = (isSpeciated ? c2.getSpeciatedNoveltyValue() : c2.getNoveltyValue());
        return isAscending ? novelty1 - novelty2 : novelty2 - novelty1;
    }
}
