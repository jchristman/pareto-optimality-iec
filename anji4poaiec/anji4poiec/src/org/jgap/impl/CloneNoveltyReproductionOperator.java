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
 * Created on Feb 3, 2003 by Philip Tucker
 */
package org.jgap.impl;

import java.util.List;
import org.jgap.Chromosome;
import org.jgap.ChromosomeMaterial;
import org.jgap.Configuration;

/**
 * Produces offspring be creating clones of parents.
 * @author Philip Tucker
 */
public class CloneNoveltyReproductionOperator extends NoveltyReproductionOperator {

    /**
     * Adds new children of <code>parents</code> to <code>offspring</code>.
     * @param config
     * @param parents <code>List</code> contains Chromosome objects
     * @param numOffspring
     * @param offspring <code>List</code> contains ChromosomeMaterial objects
     * @see org.jgap.ReproductionOperator#reproduce(Configuration, List, int, List)
     */
    @Override
    public void reproduce(Configuration config, List<Chromosome> parents, int numOffspring, List<ChromosomeMaterial> offspring) {
        reproduce(parents, numOffspring, offspring);
    }

    /**
     * Adds new children of <code>parents</code> to <code>offspring</code>.
     * @param parents <code>List</code> contains <code>Chromosome</code> objects
     * @param numOffspring
     * @param offspring <code>List</code> contains <code>ChromosomeMaterial</code> objects
     */
    public static void reproduce(List<Chromosome> parents, int numOffspring, List<ChromosomeMaterial> offspring) {
        // TODO - sort parents by novelty to favor novelty?
        int parentsSize = parents.size();
        for (int i = 0; i < numOffspring; ++i) {
            Chromosome parent = parents.get(i % parentsSize);
            offspring.add(parent.cloneMaterial());
        }
    }
}
