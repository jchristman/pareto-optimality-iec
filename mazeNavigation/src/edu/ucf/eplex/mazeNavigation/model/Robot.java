/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.model;

import edu.ucf.eplex.mazeNavigation.behaviorFramework.State;
import edu.ucf.eplex.mazeNavigation.util.Util;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class Robot implements Simulatable {

	public Robot(Maze map, Position pose) {
		this(map);
		setPosition(pose);
	}
	
	public Robot(Maze map) {
		assert(map != null); 
		myMap = map;
		myPose = map.getStart();
		
		acceleration = MAX_VELOCITY/6;	// velocity accel/decel is 0.5 units per tick
		angAccel = MAX_TURN_RATE/6;		// angular accel/decel is 0.5 deg per tick
		
		setStoped();
		
		// Setup the rangeFinder array
		maxSensorRange = MAX_SENSOR_RANGE;
		sensorArray = new SensorArray();
		sensorArray.addRangeFinder( 90*degs, maxSensorRange);
		sensorArray.addRangeFinder( 45*degs, maxSensorRange);
		sensorArray.addRangeFinder(  0*degs, maxSensorRange);
		sensorArray.addRangeFinder(-45*degs, maxSensorRange);
		sensorArray.addRangeFinder(-90*degs, maxSensorRange);
		sensorArray.addRangeFinder(180*degs, maxSensorRange);

		// Setup the goal sensor array
		sensorArray.addGoalDetector(  0*degs, 90*degs);
		sensorArray.addGoalDetector( 90*degs, 90*degs);
		sensorArray.addGoalDetector(-90*degs, 90*degs);
		sensorArray.addGoalDetector(180*degs, 90*degs);
		
		updateSensors();
		
		myState = new State(this);
	}
	
	/**
	 * @see edu.mazeNavigation.model.Simulatable#step()
	 */
	public void step() {
		currVelocity = tgtVelocity;
		currTurnRate = tgtTurnRate;
		
//		double diff;
//
//		diff = tgtVelocity - currVelocity;
//		if (Math.abs(diff) > acceleration)
//		{
//			if (currVelocity > tgtVelocity)
//				currVelocity -= acceleration;
//			if (currVelocity < tgtVelocity)
//				currVelocity += acceleration;
//		}
//		else currVelocity += diff;	
//		
//		diff = tgtTurnRate - currTurnRate;
//		if (Math.abs(diff) > angAccel)
//		{
//			if (currTurnRate > tgtTurnRate)
//				currTurnRate -= angAccel;
//			if (currTurnRate < tgtTurnRate)
//				currTurnRate += angAccel;
//		}
//		else currTurnRate += diff;
	}
	
	public double getVelocity() {
		return currVelocity;
	}
	
	public void setVelocity(double velocity) {
		tgtVelocity += Math.min(Math.max(velocity-0.5, -acceleration), acceleration);
		//	tgtVelocity = velocity * MAX_VELOCITY;
		tgtVelocity = Math.min(Math.max(tgtVelocity, MIN_VELOCITY), MAX_VELOCITY);
	}
	
	public double getTurnRate() {
		return currTurnRate;
	}
	
	public double getHeading() {
		return myPose.getTheta();
	}
	
	/**
	 * Changes the turn rate incrementally.  Expects an input in the range [0..1], which is then shifted
	 * to the range [-0.5..0.5].  This value is applied as a change in the current turn rate (in degrees). 
	 * @param turnRate
	 */
	public void setTurnRate(double turnRate) {
		tgtTurnRate += Math.min(Math.max((turnRate-0.5)*degs, -angAccel), angAccel);
		//	tgtTurnRate = turnRate * MAX_TURN_RATE;
		tgtTurnRate = Math.min(Math.max(tgtTurnRate, MIN_TURN_RATE), MAX_TURN_RATE);
	}
	
	protected void setStoped() {
		currVelocity = tgtVelocity = 0;
		currTurnRate = tgtTurnRate = 0;
	}
	
	public Position getPosition() {
		return myPose;
	}
	
	public void setPosition(Position pose) {
		setPosition(pose.getX(), pose.getY(), pose.getTheta());
	}

	public void setPosition(Point2D loc, double theta) {
		setPosition(loc.getX(), loc.getY(), theta);
	}
	
	public void setPosition(double x, double y, double theta) {
		myPose = new Position(x, y, theta);
		updateSensors();
	}
	
	public double getRadius() {
		return RADIUS;
	}

	public SensorArray getSensorArray() {
		return sensorArray;
	}
	
	public State getState() {
		return myState;
	}
	
    /**
     * Shorten each rangeFinder beam to end at the point of intersection with
     * the nearest wall
     */
    protected void updateSensors() {
		sensorArray.setPose(myPose);

		// Limits the length of the sensor beam to intersection of the nearest wall
        for (Line2D beam : sensorArray.getBeams()) {
            for (Line2D wall : myMap.getWalls()) {
                if (beam.intersectsLine(wall)) {
                    Point2D p1 = beam.getP1();
                    Point2D p2 = Util.intersection(beam, wall);
                    beam.setLine(p1, p2);
                }
            }
        }
        // Set the sensor active if the goal is detected by the observer in an angular region from A to B
        for (GoalDetector gd : sensorArray.getGoalDetectors()) {
            Line2D testSeg = new Line2D.Double(gd.getP1(), gd.getP2());
            Line2D pathToGoal = new Line2D.Double(myPose, myMap.getGoal());
            gd.setActive(testSeg.intersectsLine(pathToGoal));
        }
    }

	protected Position myPose;
	protected double currVelocity, tgtVelocity, acceleration;
	protected double currTurnRate, tgtTurnRate, angAccel;
	
	private double maxSensorRange;
	private SensorArray sensorArray;
	
	private final Maze myMap;
	private final State myState;
	private static final double degs = Math.PI/180;
	
	public static final double RADIUS =  8.0;
	public static final double MAX_VELOCITY =  3.0;
	public static final double MIN_VELOCITY = -MAX_VELOCITY;
	public static final double MAX_TURN_RATE =  3.0*degs;
	public static final double MIN_TURN_RATE = -MAX_TURN_RATE;
	public static final double MAX_SENSOR_RANGE = 100;
}
