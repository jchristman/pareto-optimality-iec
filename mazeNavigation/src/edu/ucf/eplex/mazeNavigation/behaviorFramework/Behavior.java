/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.behaviorFramework;

/**
 * The behavior interface is a strategy pattern that allows the {@link Robot} to have many types controllers 
 * behaviors without the need to know the details of their implementation, only that they implement this interface.
 * 
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public interface Behavior {
	/**
	 * Agents must be able to provide an action recommendation based on the current {@link ArmState}.
	 * 
	 * For more information, see Unified behavior framework for reactive robot control, Journal of Intelligent & Robotic
	 * Systems, 55(2-3), by Brian G. Woolley and Gilbert L. Peterson at <href>http://www.springerlink.com/content/8468r442420x47t6/</href>
	 * 
	 * @param currentState The current {@link State}.
	 * @return The recommended action to take based on the current {@link State}.
	 */
	public Action genAction(State currentState);

}
