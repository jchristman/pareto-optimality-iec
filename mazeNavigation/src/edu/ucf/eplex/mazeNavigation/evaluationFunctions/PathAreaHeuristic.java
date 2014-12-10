package edu.ucf.eplex.mazeNavigation.evaluationFunctions;

import org.jgap.Chromosome;

import edu.ucf.eplex.mazeNavigation.model.Maze;
import edu.ucf.eplex.mazeNavigation.model.Path;
import edu.ucf.eplex.mazeNavigation.model.Position;
import edu.ucf.eplex.poaiecFramework.domain.EvaluationDomain;

@SuppressWarnings("serial")
public class PathAreaHeuristic extends FitnessStrategy {

	private double max_area;
	
	public PathAreaHeuristic(Maze maze, EvaluationDomain<Path> domain) {
		super(maze, domain);
		
		// The maximum area is the area of the map
		max_area = theMaze.getHeight() * theMaze.getWidth();
	}

	@Override
	public int evaluateFitness(Chromosome subject, Path path) {
		double min_x = theMaze.getWidth(), min_y = theMaze.getHeight();
		double max_x = 0, max_y = 0;
		
		for (Position p : path) {
			if (p.getX() < min_x) min_x = p.getX();
			if (p.getX() > max_x) max_x = p.getX();
			if (p.getY() < min_y) min_y = p.getY();
			if (p.getY() > max_y) max_y = p.getY();
		}
		
		double area = (max_x - min_x) * (max_y - min_y);
		int fitness = (int) (MAX_FITNESS * (area / max_area));
		
		return fitness;
	}

	public long getEvaluationFunctionUUID() {
		return 9002;
	}

	public String shortName() {
		return "Explored Area";
	}

}
