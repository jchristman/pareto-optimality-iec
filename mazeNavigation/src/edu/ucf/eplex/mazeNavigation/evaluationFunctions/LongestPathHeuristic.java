/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Joshua Christman on Nov 17, 2014
 */
package edu.ucf.eplex.mazeNavigation.evaluationFunctions;

import org.jgap.Chromosome;

import edu.ucf.eplex.mazeNavigation.model.Maze;
import edu.ucf.eplex.mazeNavigation.model.Path;
import edu.ucf.eplex.mazeNavigation.model.Position;
import edu.ucf.eplex.mazeNavigation.model.Robot;
import edu.ucf.eplex.poaiecFramework.domain.EvaluationDomain;

/**
 * This heuristic encourages longer paths by assigning a higher fitness to a longer
 * path. While this is a deceptive heuristic in that a longer path may not be better,
 * it should drive evolution to a robot that does not crash into walls and stop, which
 * is desirable in the long-run.
 * 
 * @author joshua
 *
 */
@SuppressWarnings("serial")
public class LongestPathHeuristic extends FitnessStrategy {

	private double max_distance;
	
	public LongestPathHeuristic(Maze maze, EvaluationDomain<Path> domain) {
		super(maze, domain);

		// The theoretical maximum distance travelled is the max velocity of the robot multiplied
        // by the time the robot is alive.
		max_distance = Robot.MAX_VELOCITY * _domain.getMaxSimulationTimesteps();
	}

	@Override
	public int evaluateFitness(Chromosome subject, Path path) {
		Position last = path.get(0);
		Position cur = path.get(1);
		double distance = CommonFunctions.distance(last, cur);
		for (int i = 2; i < path.size(); i++) {
			last = path.get(i - 1);
			cur = path.get(i);
			distance += CommonFunctions.distance(last, cur);
		}
		
        int fitness = (int) (MAX_FITNESS * (distance / max_distance));
        
        return fitness;	
	}

	public long getEvaluationFunctionUUID() {
		return 9001;
	}

	public String shortName() {
		return "Longest Path";
	}

}

