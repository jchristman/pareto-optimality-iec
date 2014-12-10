/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.model;

import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

import edu.ucf.eplex.mazeNavigation.util.Util;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class GoalDetector {

	public GoalDetector(double centerlineAngle, double fieldOfView) {
		assert(fieldOfView > 0);

		angSt = Util.normalize(centerlineAngle - fieldOfView/2);
		angExt = fieldOfView;
	}

	public void setPose(Position pose) {
		double x = pose.getX(),
			   y = pose.getY(),
			   heading = pose.getTheta();
		arc.setArcByCenter(x, y, 1, toDeg(angSt-heading), toDeg(angExt), Arc2D.OPEN);
	}
	
	private double toDeg(double rad) {
		return 180*rad/Math.PI;
	}

	public boolean isActive() {
		return isActive;
	}
	
	public void setActive(boolean goalDetected) {
		isActive = goalDetected;
	}
	
	public double reading() {
		if (isActive) return 1;
		else		  return 0;
	}
	
	public Arc2D getArc() {
		return arc;
	}
	
	public Point2D getP1() {
		return arc.getStartPoint();
	}
	
	public Point2D getP2() {
		return arc.getEndPoint();
	}

	private boolean isActive = false;
	private final Arc2D arc = new Arc2D.Double();
	private final double angSt, angExt;
}
