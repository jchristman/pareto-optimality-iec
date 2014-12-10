/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.behaviorFramework;

import edu.ucf.eplex.mazeNavigation.model.Robot;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class Action {

	/**
	 * 
	 * @param velocity
	 */
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
	
	/**
	 * 
	 * @param turnRate
	 */
	public void setTurnRate(double turnRate) {
		this.turnRate = turnRate;
	}
	
	/**
	 * 
	 * @param robot
	 */
	public void execute(Robot robot) {
		robot.setVelocity(velocity);
		robot.setTurnRate(turnRate);
	}

	protected double velocity = 0.5;
	protected double turnRate = 0.5;
}
