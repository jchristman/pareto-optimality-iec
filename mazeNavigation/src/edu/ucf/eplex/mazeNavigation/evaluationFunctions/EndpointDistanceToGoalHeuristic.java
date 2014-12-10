/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.evaluationFunctions;

import org.jgap.Chromosome;

import edu.ucf.eplex.mazeNavigation.model.Maze;
import edu.ucf.eplex.mazeNavigation.model.Path;
import edu.ucf.eplex.poaiecFramework.domain.EvaluationDomain;

/**
 *
 * @author Brian Woolley (brian.woolley at ieee.org)
 */
@SuppressWarnings("serial")
public class EndpointDistanceToGoalHeuristic extends FitnessStrategy {

	private double max_distance;
    /**
     * 
     * @param maze 
     */
    public EndpointDistanceToGoalHeuristic(Maze maze, EvaluationDomain<Path> domain) {
        super(maze, domain);
        
        // The theoretical max distance from the goal is the diagonal of the maze
        max_distance = Math.sqrt(Math.pow(theMaze.getHeight(), 2) + 
        		Math.pow(theMaze.getWidth(), 2));
    }

	public int evaluateFitness(Chromosome subject, Path path) {
        double distance = CommonFunctions.distance(theMaze.getGoal(), path.getLast());
        int fitness = (int) (MAX_FITNESS * (1 - (distance / max_distance)));
		return fitness;
	}

	public long getEvaluationFunctionUUID() {
		return 9000;
	}

	public String shortName() {
		return "Distance to Goal";
	}

}
