package edu.ucf.eplex.mazeNavigation.evaluationFunctions;

import org.jgap.Chromosome;

import edu.ucf.eplex.mazeNavigation.model.Maze;
import edu.ucf.eplex.mazeNavigation.model.Path;
import edu.ucf.eplex.mazeNavigation.model.Position;
import edu.ucf.eplex.mazeNavigation.model.Robot;
import edu.ucf.eplex.poaiecFramework.domain.EvaluationDomain;

@SuppressWarnings("serial")
public class ConsistentRobotSpeedHeuristic extends FitnessStrategy {

	public ConsistentRobotSpeedHeuristic(Maze maze,
			EvaluationDomain<Path> domain) {
		super(maze, domain);
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
		
		double averageDistancePerStep = distance / (path.size() - 1);
		
		double worstTotalDiffFromAvg = Robot.MAX_VELOCITY * path.size();
		// Now calculate how far each step is from the average speed. If it stays the same speed the whole time, this will
		// be close to 0.
		double totalDiffFromAvg = 0;
		for (int i = 1; i < path.size(); i++) {
            last = path.get(i - 1);
            cur = path.get(i);
            distance = CommonFunctions.distance(last, cur);
            totalDiffFromAvg += Math.abs(distance - averageDistancePerStep);
		}
		
        int fitness = (int) (MAX_FITNESS * (1 - (totalDiffFromAvg / worstTotalDiffFromAvg)));
        return fitness;	
	}

	public long getEvaluationFunctionUUID() {
		return 9006;
	}

	public String shortName() {
		return "Consistent Speed";
	}

}
