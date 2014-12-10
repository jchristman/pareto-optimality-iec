package edu.ucf.eplex.mazeNavigation.evaluationFunctions;

import org.jgap.Chromosome;

import edu.ucf.eplex.mazeNavigation.model.Maze;
import edu.ucf.eplex.mazeNavigation.model.Path;
import edu.ucf.eplex.mazeNavigation.model.Position;
import edu.ucf.eplex.mazeNavigation.model.Robot;
import edu.ucf.eplex.poaiecFramework.domain.EvaluationDomain;

@SuppressWarnings("serial")
public class RobotSpeedHeuristic extends FitnessStrategy {

	public RobotSpeedHeuristic(Maze maze, EvaluationDomain<Path> domain) {
		super(maze, domain);
	}

	@Override
	public int evaluateFitness(Chromosome subject, Path path) {
		double maxTotalVelocity = Robot.MAX_VELOCITY * path.size();
		double totalVelocity = 0;
		
		Position last = path.get(0);
		Position cur = path.get(1);
		totalVelocity += CommonFunctions.getVelocity(last, cur);
		for (int i = 2; i < path.size(); i++) {
			last = path.get(i - 1);
			cur = path.get(i);
			
			totalVelocity += CommonFunctions.getVelocity(last, cur);
		}
        int fitness = (int) (MAX_FITNESS * (totalVelocity / maxTotalVelocity));
        return fitness;	
	}

	public long getEvaluationFunctionUUID() {
		return 9005;
	}

	public String shortName() {
		return "Robot +Speed";
	}

}
