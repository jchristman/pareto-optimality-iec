/**
 * ----------------------------------------------------------------------------| Created on Apr
 * 12, 2003
 */
package com.anji.util;

import java.util.List;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.impl.FitnessReproductionOperator;

/**
 * @author Philip Tucker
 */
public class DummyReproductionOperator extends FitnessReproductionOperator {

	/**
	 * @see org.jgap.ReproductionOperator#reproduce(org.jgap.Configuration, java.util.List, int,
	 * java.util.List)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void reproduce( final Configuration config, final List parentChroms, int numOffspring,
			List offspring ) {
		for ( int i = 0; i < numOffspring; ++i ) {
			Chromosome c = (Chromosome) parentChroms.get( 0 );
			offspring.add( c.cloneMaterial() );
		}
	}
}
