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

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Abstract class for mutation operators. Handles iteration over population and updating
 * chromosome material with changes generated by subclass implementation of
 * <code>mutate( final Configuration config, 
 * final ChromosomeMaterial material, Set genesToAdd, Set genesToRemove )</code>.
 * @author Philip Tucker
 */
public abstract class MutationOperator {

private float mutationRate = 0.0f;

/**
 * @param aMutationRate
 */
public MutationOperator( float aMutationRate ) {
	mutationRate = aMutationRate;
}

/**
 * Leaves <code>material</code> unmodified, but updates <code>allelesToAdd</code> and
 * <code>allelesToRemove</code> with modifications. This interface was chosen at a time when
 * we wanted to have the mutation operators not augment each other; i.e., each one operated on
 * the original <code>material</code>, and the total <code>allelesToAdd</code> and
 * <code>allelesToRemove</code> from all mutations would be applied at once. We have gone back
 * to updating <code>material</code> after each mutation operator, but left the interface this
 * way in case we decide to switch again.
 * @param config
 * @param target chromosome material before mutation
 * @param allelesToAdd alleles added by this mutation, <code>Set</code> contains
 * <code>Allele</code> objects
 * @param allelesToRemove alleles removed by this mutation, <code>Set</code> contains
 * <code>Allele</code> objects
 * @throws InvalidConfigurationException
 */
@SuppressWarnings("rawtypes")
protected abstract void mutate( final Configuration config, final ChromosomeMaterial target,
		Set allelesToAdd, Set allelesToRemove ) throws InvalidConfigurationException;

/**
 * The operate method will be invoked on each of the mutation operators referenced by the
 * current Configuration object during the evolution phase. Operators are given an opportunity
 * to run in the order that they are added to the Configuration.
 * @param config The current active genetic configuration.
 * @param offspring <code>List</code> Contains <code>ChromosomeMaterial</code> objects from
 * the current evolution. Material in this <code>List</code> should be modified directly.
 * @throws InvalidConfigurationException
 */
@SuppressWarnings("rawtypes")
public void mutate( final Configuration config, final List<ChromosomeMaterial> offspring )
		throws InvalidConfigurationException {
	for (ChromosomeMaterial material : offspring) {
		Set allelesToAdd = new HashSet();
		Set allelesToRemove = new HashSet();
		mutate( config, material, allelesToAdd, allelesToRemove );
		updateMaterial( material, allelesToAdd, allelesToRemove );
	}
}

/**
 * @return int number of mutations given <code>rand</code> random number generator,
 * <code>numOpportunities</code> number of oppurtunities for the mutation to occur, and the
 * configured mutation rate
 * @param rand
 * @param numOpportunities
 */
protected int numMutations( Random rand, int numOpportunities ) {
	// TODO - use some kind of stats lib to calculate this in a more clever way?
	int result = 0;
	if ( getMutationRate() > 0 ) {
		for ( int i = 0; i < numOpportunities; ++i ) {
			if ( doesMutationOccur( rand ) )
				++result;
		}
	}
	return result;
}

/**
 * @param rand
 * @return <code>true</code> when mutation rate and random chance dictate a mutation should
 * occur
 */
protected boolean doesMutationOccur( Random rand ) {
	return doesMutationOccur( rand, getMutationRate() );
}

/**
 * @param rand
 * @param mutationRate
 * @return <code>true</code> when mutation rate and random chance dictate a mutation should
 * occur
 */
protected static boolean doesMutationOccur( Random rand, float mutationRate ) {
	return ( rand.nextDouble() < mutationRate );
}

/**
 * @return mutation rate
 */
public float getMutationRate() {
	return mutationRate;
}

/**
 * updates <code>material</code> with specified sets of alleles; alleles present in both lists
 * will be added (or replaced if the gene existed on original material)
 * @param material
 * @param allelesToAdd <code>Set</code> contains <code>Allele</code> objecs
 * @param allelesToRemove <code>Set</code> contains <code>Allele</code> objects
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
protected static void updateMaterial( ChromosomeMaterial material, Set allelesToAdd,
		Set allelesToRemove ) {
	// remove before add because some genes that have been modified are in both lists
	material.getAlleles().removeAll( allelesToRemove );
	material.getAlleles().addAll( allelesToAdd );
}

/**
 * @param aMutationRate
 */
protected void setMutationRate( float aMutationRate ) {
	mutationRate = aMutationRate;
}

}
