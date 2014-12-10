package edu.ucf.eplex.mazeNavigation.evaluationFunctions;

import java.awt.geom.Line2D;
import java.util.Iterator;

import org.jgap.Chromosome;

import edu.ucf.eplex.mazeNavigation.model.Maze;
import edu.ucf.eplex.mazeNavigation.model.Path;
import edu.ucf.eplex.mazeNavigation.model.Position;
import edu.ucf.eplex.mazeNavigation.model.Robot;
import edu.ucf.eplex.poaiecFramework.domain.EvaluationDomain;

@SuppressWarnings("serial")
public class PathSmoothnessHeuristic extends FitnessStrategy {

	double _sep = 0.5;
	double minSeparation = Robot.MAX_VELOCITY * _sep;
	
	public PathSmoothnessHeuristic(Maze maze, EvaluationDomain<Path> domain) {
		super(maze, domain);
	}

	@Override
	public int evaluateFitness(Chromosome subject, Path path) {
		double maxTotalAngle = 180.0 / Math.PI * Robot.MAX_TURN_RATE * path.size() / _sep;
		
		Iterator<Position> pathIter = path.iterator();
		Position p1 = pathIter.next();
		Position p2, p3;
		
		double totalAngle = 0;
		while (pathIter.hasNext()) {
			Position next = pathIter.next();
			if (CommonFunctions.distance(p1, next) > minSeparation) {
				p2 = next;
				
				while (pathIter.hasNext()) {
					next = pathIter.next();
					
					if (CommonFunctions.distance(p2, next) > minSeparation) {
						p3 = next;
						Line2D line1 = new Line2D.Double(p1.getPoint(), p2.getPoint());
						Line2D line2 = new Line2D.Double(p2.getPoint(), p3.getPoint());
						
						double angle = CommonFunctions.angle(line1, line2);
						totalAngle += Math.abs(angle);
						
						p1 = p2;
						p2 = p3;
					}
				}
				
				break;
			}
		}
		//System.out.println(totalAngle + " --- " + maxTotalAngle);
		int fitness = (int) (MAX_FITNESS * Math.min(1, (1 - (totalAngle * 50 / maxTotalAngle))));
		return fitness;
	}

	public long getEvaluationFunctionUUID() {
		return 9003;
	}

	public String shortName() {
		return "Path Smoothness";
	}

}
