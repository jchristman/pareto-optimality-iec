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
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class SensorArray {

	public void setPose(Position pose) {
		for (RangeFinder a : rangeFinders)
			a.setPose(pose);		
		for (GoalDetector b : goalDetectors)
			b.setPose(pose);
	}

	public void addRangeFinder(double angle, double maxRange) {
		rangeFinders.add(new RangeFinder(angle, maxRange));
	}
	
	public void addGoalDetector(double angle, double fieldOfView) {
		goalDetectors.add(new GoalDetector(angle, fieldOfView));
	}
	
	public List<Double> getRangeReadings() {
		List<Double> ranges = new ArrayList<Double>();
		for(RangeFinder x : rangeFinders) {
			ranges.add(x.reading());
		}
		return ranges;
	}

	public List<Line2D> getBeams() {
		List<Line2D> beams = new ArrayList<Line2D>();
		for(RangeFinder x : rangeFinders) {
			beams.add(x.getBeam());
		}
		return beams;
	}
	
	public List<Double> getGoalReadings() {
		List<Double> goalDetection = new ArrayList<Double>();
		for(GoalDetector x : goalDetectors) {
			goalDetection.add(x.reading());
		}
		return goalDetection;
	}

	public List<Arc2D> getGoalArcs() {
		List<Arc2D> sensingArcs = new ArrayList<Arc2D>();
		for(GoalDetector x : goalDetectors) {
			if (x.isActive())
				sensingArcs.add(x.getArc());
		}
		return sensingArcs;
	}
	
	public List<GoalDetector> getGoalDetectors() {
		return goalDetectors;
	}
	
	private final List<RangeFinder> rangeFinders = new ArrayList<RangeFinder>();
	private final List<GoalDetector> goalDetectors = new ArrayList<GoalDetector>();
}
