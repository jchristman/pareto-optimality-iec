/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import edu.ucf.eplex.mazeNavigation.model.Position;

/**
 * 
 * @author Brian Woolley (brian.woolley@ieee.org)
 *
 */
public class Series {

	private long evaluations;
	private long archive;
	private long timeElapsed;
	private long stepCount;
	private List<Step> steps = new LinkedList<Step>();
	private long allPointsCount;
	private List<Position> allPoints = new LinkedList<Position>();
	
	public Series(String seriesFileName) {
		InputStream in;
		try {
			in = new FileInputStream(seriesFileName);
	        DocumentBuilder builder;
	        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	        Document doc = builder.parse(in);
	        Node series = doc.getFirstChild();
	        
	        if (series.getNodeName().equalsIgnoreCase("SERIES_TAG")) {
	        	Node seriesChild = series.getFirstChild();
	        	while (seriesChild != null) {
	        		if (seriesChild.getNodeName().equals(TOTAL_EVALUATIONS_TAG)) {
	        			evaluations = Util.getLong(seriesChild);
	        		} else if (seriesChild.getNodeName().equals(TOTAL_ARCHIVE_TAG)) {
	        			archive = Util.getLong(seriesChild);
	        		} else if (seriesChild.getNodeName().equals(TIME_ELAPSED_TAG)) {
	        			timeElapsed = Util.getLong(seriesChild);
	        		} else if (seriesChild.getNodeName().equals(IEC_STEPS_TAG)) {
	        			loadIecSteps(seriesChild);
	        		} else if (seriesChild.getNodeName().equals(ALL_POINTS_TAG)) {
	        			loadAllPoints(seriesChild);
	        		}
	        		seriesChild = seriesChild.getNextSibling();
	        	}
	        }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (stepCount != steps.size()) throw new IllegalStateException("Step count value was " + stepCount + " while " + steps.size() + " steps elements were added.");
		if (allPointsCount != allPoints.size()) throw new IllegalStateException("Point/position count value was " + allPointsCount + " while " + allPoints.size() + " point/position elements were added.");
		System.out.println("Loaded Series: " + evaluations + ", " + archive + ", " + timeElapsed + ", " + stepCount + ", " + allPointsCount);
	}
	
	/**
	 * Builds a single series from a set of series objects
	 * @param setOfSeries
	 */
	public Series(List<Series> setOfSeries) {
		for (Series curr : setOfSeries) {
			evaluations += curr.evaluations;
			archive += curr.archive;
			timeElapsed += curr.timeElapsed;
			stepCount += curr.stepCount;
			steps.addAll(curr.steps);
			allPointsCount += curr.allPointsCount;
			allPoints.addAll(curr.allPoints);
		}

		if (stepCount != steps.size()) throw new IllegalStateException("Step count value was " + stepCount + " while " + steps.size() + " steps elements were added.");
		if (allPointsCount != allPoints.size()) throw new IllegalStateException("Point/position count value was " + allPointsCount + " while " + allPoints.size() + " point/position elements were added.");

		// now run through the steps and renumber their id values
		int i = 0;
		for (Step s : steps) {
			s.setId(i++);
		}
	}

	private void loadIecSteps(Node iecStepsNode) {
		stepCount = Util.getLong(iecStepsNode, IEC_STEP_COUNT_TAG);
		Node step = iecStepsNode.getFirstChild();
		while(step != null) {
			if (step.getNodeName().equals(STEP_TAG)) {
				steps.add(new Step(step));	
			}
			step = step.getNextSibling();
		}
	}

	private void loadAllPoints(Node seriesChild) {
		allPointsCount = Util.getLong(seriesChild, POSITION_COUNT_TAG);
		Node step = seriesChild.getFirstChild();
		while(step != null) {
			if (step.getNodeName().equals(POSITION_TAG)) {
				allPoints.add(new Position(step));
			}
			step = step.getNextSibling();
		}
	}

	/**
	 * Returns a collection of all points visited during this series
	 * @return
	 */
	public Collection<Position> getAllPoints() {
		return allPoints;
	}

	private static final String TOTAL_EVALUATIONS_TAG = "evaluations";
	private static final String TOTAL_ARCHIVE_TAG = "archive";
	private static final String TIME_ELAPSED_TAG = "time";
	private static final String IEC_STEPS_TAG = "iecSteps";
	private static final String IEC_STEP_COUNT_TAG = "count";
	private static final String STEP_TAG = "step";
	private static final String ALL_POINTS_TAG = "allPoints";
	private static final String POSITION_COUNT_TAG = "count";
	private static final String POSITION_TAG = "position";

}

