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

/**
 * 
 * @author Brian Woolley (brian.woolley@ieee.org)
 *
 */
public interface SimulationObserver {

	public void update(Environment env);
}
