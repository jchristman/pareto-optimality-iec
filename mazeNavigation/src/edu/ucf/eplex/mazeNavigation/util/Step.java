/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.util;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Node;

/**
 * 
 * @author Brian Woolley (brian.woolley@ieee.org)
 *
 */
public class Step {
	
	@SuppressWarnings("unused")
	private long id;
	@SuppressWarnings("unused")
	private Action action;
	@SuppressWarnings("unused")
	private long evaluations;
	@SuppressWarnings("unused")
	private long champFitness;
	@SuppressWarnings("unused")
	private long archiveCount;
	@SuppressWarnings("unused")
	private long speciesCount;
	private List<Chromosome> chromosomes = new LinkedList<Chromosome>();
	
	public Step(Node xmlNode) {
        if (xmlNode.getNodeName().equalsIgnoreCase("STEP_TAG")) {
        	id = Util.getLong(xmlNode, ID_TAG);
        	Node child = xmlNode.getFirstChild();
        	while (child != null) {
        		if (child.getNodeName().equals(ACTION_TAG)) {
        			action = parseAction(child.getTextContent());
        		} else if (child.getNodeName().equals(EVALUATIONS_TAG)) {
        			evaluations = Util.getLong(child);
        		} else if (child.getNodeName().equals(CHAMP_FITNESS_TAG)) {
        			champFitness = Util.getLong(child);
        		} else if (child.getNodeName().equals(ARCHIVE_TAG)) {
        			archiveCount = Util.getLong(child);
        		} else if (child.getNodeName().equals(SPECIES_TAG)) {
        			speciesCount = Util.getLong(child);
        		} else if (child.getNodeName().equals(CHROMOSOME_TAG)) {
        			chromosomes.add(new Chromosome(child));
        		}
        		child = child.getNextSibling();
        	}
        }
	}
	
	protected void setId(int i) {
		if (i >= 0) id = i;		
	}
	
	private Action parseAction(String actionStr) {
		if (actionStr.equalsIgnoreCase("fitness")) {
			return Action.FITNESS;
		} else if (actionStr.equalsIgnoreCase("novelty")) {
			return Action.NOVELTY;
		} else if (actionStr.equalsIgnoreCase("step")) {
			return Action.STEP;
		} else {
			return Action.UNDEFINED;
		}
	}

	@SuppressWarnings("unused")
	private static final String STEP_TAG = "step";
	private static final String ID_TAG = "id";
	private static final String ACTION_TAG = "action";
	private static final String EVALUATIONS_TAG = "evaluations";
	private static final String CHAMP_FITNESS_TAG = "action";
	private static final String ARCHIVE_TAG = "archive";
	private static final String SPECIES_TAG = "action";
	private static final String CHROMOSOME_TAG = "chromosome";
	private static final String SELECTION_TAG = "selection";
	private static final String SOLUTION_TAG = "solution";
	private static final String FITNESS_SCORE_TAG = "fitness";
	private static final String NOVELTY_SCORE_TAG = "novelty";
	private static final String NODE_COUNT_TAG = "nodes";
	private static final String CONNECTION_COUNT_TAG = "connections";
	
	/**
	 * @author bwoolley
	 *
	 */
	private class Chromosome {
		
		@SuppressWarnings("unused")
		private boolean selected;
		@SuppressWarnings("unused")
		private boolean solved;
		@SuppressWarnings("unused")
		private long fitness;
		@SuppressWarnings("unused")
		private long novelty;
		@SuppressWarnings("unused")
		private long nodes;
		@SuppressWarnings("unused")
		private long connections;

		private Chromosome(Node xmlNode) {
	        if (xmlNode.getNodeName().equalsIgnoreCase("CHROMOSOME_TAG")) {
	        	id = Util.getLong(xmlNode, ID_TAG);
	        	Node child = xmlNode.getFirstChild();
	        	while (child != null) {
	        		if (child.getNodeName().equals(SELECTION_TAG)) {
	        			selected = Boolean.parseBoolean(child.getTextContent());
	        		} else if (child.getNodeName().equals(SOLUTION_TAG)) {
	        			solved = Boolean.parseBoolean(child.getTextContent());
	        		} else if (child.getNodeName().equals(FITNESS_SCORE_TAG)) {
	        			fitness = Util.getLong(child);
	        		} else if (child.getNodeName().equals(NOVELTY_SCORE_TAG)) {
	        			novelty = Util.getLong(child);
	        		} else if (child.getNodeName().equals(NODE_COUNT_TAG)) {
	        			nodes = Util.getLong(child);
	        		} else if (child.getNodeName().equals(CONNECTION_COUNT_TAG)) {
	        			connections = Util.getLong(child);
	        		}
	        		child = child.getNextSibling();
	        	}
	        }
		}
	}

}

