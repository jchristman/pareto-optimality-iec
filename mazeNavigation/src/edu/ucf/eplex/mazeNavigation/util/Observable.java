/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.util;

import edu.ucf.eplex.mazeNavigation.model.Environment;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Brian Woolley <brian.woolley at ieee.org>
 */
public class Observable {
    
	public void notifyObservers(Environment env) {
		for(SimulationObserver o : evoObservers) {
			o.update(env);
		}
	}
	
	public void registerObserver(SimulationObserver o) {
		evoObservers.add(o);
	}
	
	public boolean removeObserver(SimulationObserver o) {
		return evoObservers.remove(o);
	}
	
	private Set<SimulationObserver> evoObservers = new HashSet<SimulationObserver>();
}
