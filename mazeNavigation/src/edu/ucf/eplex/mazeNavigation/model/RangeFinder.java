/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.model;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import edu.ucf.eplex.mazeNavigation.util.Util;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class RangeFinder {

	public RangeFinder(double offsetAngle, double maxRange) {
		assert(maxRange > 0);
		beamAngle = Util.normalize(offsetAngle);
		this.maxRange = maxRange;
	}
	
	public void setPose(Position pose) {
		double theta = Util.normalize(pose.getTheta() + beamAngle);
		double dx = Math.cos(theta) * maxRange;
		double dy = Math.sin(theta) * maxRange;
		
		Point2D endPt = new Point2D.Double(pose.getX()+dx, pose.getY()+dy);
		beam = new Line2D.Double(pose, endPt);
	}
	
	public double reading() {
		return beam.getP1().distance(beam.getP2())/maxRange;
	}
	
	public Line2D getBeam() {
		return beam;
	}

	
	private final double beamAngle;
	private final double maxRange;
	private Line2D beam;
}
