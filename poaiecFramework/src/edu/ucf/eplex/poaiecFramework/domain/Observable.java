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
import java.util.HashSet;
import java.util.Set;


/**
 * @param <T> 
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class Observable<T> {
	
	public void notifyObservers(Collection<T> population, Collection<T> archive) {
            notifyObservers(population, archive, null, null);
	}
	
	public void notifyObservers(Collection<T> population, Collection<T> archive, T fittest, T mostNovel) {
		for(EvolutionObserver<T> o : evoObservers) {
			o.update(population, archive, fittest, mostNovel);
		}
	}

        public void registerObserver(EvolutionObserver<T> o) {
		evoObservers.add(o);
	}
	
	public boolean removeObserver(EvolutionObserver<T> o) {
		return evoObservers.remove(o);
	}
	
	private Set<EvolutionObserver<T>> evoObservers = new HashSet<EvolutionObserver<T>>();
}
