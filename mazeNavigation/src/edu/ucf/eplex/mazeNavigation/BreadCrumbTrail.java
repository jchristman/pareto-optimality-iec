/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation;

import edu.ucf.eplex.mazeNavigation.evaluationFunctions.FitnessStrategy;
import edu.ucf.eplex.mazeNavigation.model.Maze;
import edu.ucf.eplex.mazeNavigation.model.Path;
import edu.ucf.eplex.mazeNavigation.model.Position;
import edu.ucf.eplex.mazeNavigation.model.Robot;
import edu.ucf.eplex.poaiecFramework.domain.EvaluationDomain;

import java.util.LinkedList;

import org.jgap.Chromosome;

/**
 *
 * @author Brian Woolley (brian.woolley at ieee.org)
 */
@SuppressWarnings("serial")
public class BreadCrumbTrail extends FitnessStrategy {
	
    /**
     *
     * @param maze
     */
    public BreadCrumbTrail(Maze maze, EvaluationDomain<Path> domain) {
        super(maze, domain);
    }

    @SuppressWarnings("unused")
	private int calculateFitness_old(Path path) {
        LinkedList<Position> crumbs = new LinkedList<Position>(theMaze.getBreadCrumbs());

        if (path.getLast().distance(theMaze.getGoal()) <= Robot.RADIUS) {
            return getMaxFitnessValue();
        }

        int fitness = 0;
        Position prev = theMaze.getStart();
        Position next = crumbs.removeFirst();
        for (Position point : path) {
            if (next.distance(point) <= Robot.RADIUS) {
                fitness += 30;
                prev = next;
                next = crumbs.removeFirst();
            }
        }
        double dist = path.getLast().distance(next);
        double len = prev.distance(next);
        double score = Math.max(1.0 - dist / len, 0.0);
        fitness += (int) Math.round(30 * score);
        return fitness;
    }

	public int evaluateFitness(Chromosome subject, Path path) {
        LinkedList<Position> crumbs = new LinkedList<Position>(theMaze.getBreadCrumbs());

        double euclidianDist = path.getLast().distance(theMaze.getGoal());
        if (euclidianDist <= 5) {
            int fitness = (int) Math.round(getMaxFitnessValue() - euclidianDist);
            return fitness;
        }

        int fitness = 0;
        Position prev = theMaze.getStart();
        Position next = crumbs.removeFirst();
        for (Position point : path) {
            if (!crumbs.isEmpty()) {
                if (next.distance(point) < 2 * Robot.RADIUS) {
                    fitness += 30;
                    prev = next;
                    next = crumbs.removeFirst();
                }
            }
        }
        double dist = path.getLast().distance(next);
        double len = prev.distance(next);
        double score = Math.max(1.0 - dist / len, 0.0);
        fitness += (int) Math.round(30 * score);
        
        return fitness;
	}
	
	public long getEvaluationFunctionUUID() {
		return 7002;
	}

	public String shortName() {
		return "Bread Crumb Trail";
	}

}
