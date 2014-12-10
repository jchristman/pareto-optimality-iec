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
import java.util.List;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class State {

	public State(Robot robot) {
		assert(robot != null);
		myRobot = robot;
	}

	public double getVelocity() {
		return myRobot.getVelocity();
	}
	
	public double getTurnRate() {
		return myRobot.getTurnRate();		
	}
	
	public double getX() {
		return myRobot.getPosition().getX();
	}
	
	public double getY() {
		return myRobot.getPosition().getY();
	}
	
	public double getTheta() {
		return myRobot.getPosition().getTheta();
	}
	
	public List<Double> getRangeReadings() {
		return myRobot.getSensorArray().getRangeReadings();
	}
	
	public List<Double> getGoalDetection() {
		return myRobot.getSensorArray().getGoalReadings();
	}
	
	private final Robot myRobot;
}
