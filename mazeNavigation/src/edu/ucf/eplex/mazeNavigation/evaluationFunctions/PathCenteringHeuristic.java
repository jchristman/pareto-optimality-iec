package edu.ucf.eplex.mazeNavigation.evaluationFunctions;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import org.jgap.Chromosome;

import edu.ucf.eplex.mazeNavigation.model.Maze;
import edu.ucf.eplex.mazeNavigation.model.Path;
import edu.ucf.eplex.mazeNavigation.model.Position;
import edu.ucf.eplex.mazeNavigation.model.RangeFinder;
import edu.ucf.eplex.mazeNavigation.model.Robot;
import edu.ucf.eplex.mazeNavigation.util.Util;
import edu.ucf.eplex.poaiecFramework.domain.EvaluationDomain;

@SuppressWarnings("serial")
public class PathCenteringHeuristic extends FitnessStrategy {
	
	double degs = Math.PI/180;
	
	public PathCenteringHeuristic(Maze maze, EvaluationDomain<Path> domain) {
		super(maze, domain);
	}

	@Override
	public int evaluateFitness(Chromosome subject, Path path) {
		List<RangeFinder> sensors = new LinkedList<RangeFinder>();
		sensors.add(new RangeFinder(-90 * degs, Robot.MAX_SENSOR_RANGE));
		sensors.add(new RangeFinder(90 * degs, Robot.MAX_SENSOR_RANGE));

		double idealSensorRatio = path.size();
		double totalSensorRatio = 0;
		for (Position pos : path) {
			for (RangeFinder sensor : sensors)
				sensor.setPose(pos);
			
			List<Line2D> beams = new LinkedList<Line2D>();
			for (RangeFinder sensor : sensors)
				beams.add(sensor.getBeam());
			
			for (Line2D beam : beams) {
	            for (Line2D wall : theMaze.getWalls()) {
	                if (beam.intersectsLine(wall)) {
	                    Point2D p1 = beam.getP1();
	                    Point2D p2 = Util.intersection(beam, wall);
	                    beam.setLine(p1, p2);
	                }
	            }
			}
			
			if (sensors.get(0).reading() < sensors.get(1).reading()) {
				totalSensorRatio += sensors.get(0).reading() / sensors.get(1).reading();
			} else {
				totalSensorRatio += sensors.get(1).reading() / sensors.get(0).reading();
			}
		}
		
		int fitness = (int) (MAX_FITNESS * (totalSensorRatio / idealSensorRatio));
		return fitness;
	}

	public long getEvaluationFunctionUUID() {
		return 9004;
	}

	public String shortName() {
		return "Path Centering";
	}

}
