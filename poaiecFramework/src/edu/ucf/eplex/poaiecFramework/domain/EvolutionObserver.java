/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.poaiecFramework.domain;

import java.util.Collection;

/**
 * @param <T>
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public interface EvolutionObserver<T> {

    /**
     *
     * @param population
     * @param archive
     * @param fittest
     * @param mostNovel  
     */
    public void update(Collection<T> population, Collection<T> archive, T fittest, T mostNovel);
}
