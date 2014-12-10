/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.model;

import edu.ucf.eplex.mazeNavigation.behaviorFramework.Behavior;
import edu.ucf.eplex.mazeNavigation.util.Observable;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class Environment extends Observable implements Simulatable {

    public Environment(Behavior aBehavior, Maze map) {
        assert (aBehavior != null);
        myRobotBehavior = aBehavior;
        myMap = map;
        myRobot = new Robot(myMap);
        path = new Path();
    }

    /**
     * @see edu.mazeNavigation.model.Simulatable#step()
     */
    public void step() {
        myRobotBehavior.genAction(myRobot.getState()).execute(myRobot);
        myRobot.step();
        updateRobotPose();
        notifyObservers(this);
        path.add(myRobot.getPosition());
    }

    protected void updateRobotPose() {

        for (int i = 0; i < RESOLUTION; i++) {
            myRobot.setPosition(tick(myRobot));
        }
    }

    private Position tick(Robot robot) {
        double dv = robot.getVelocity() / RESOLUTION,
                dTheta = robot.getTurnRate() / RESOLUTION,
                theta = robot.getPosition().getTheta(),
                dx = dv * Math.cos(theta),
                dy = dv * Math.sin(theta);

        Position next = robot.getPosition().add(dx, dy, dTheta);

        if (isValid(next)) {
            return (next);
        } else {
            return (robot.getPosition().add(0, 0, dTheta));
        }

        // Perhaps, in the future, the robot may adjust it's acceleration by these same tick units as well
    }

    private boolean isValid(Position pose) {
        for (Line2D wall : myMap.getWalls()) {
            if (wall.ptSegDist(pose) < myRobot.getRadius()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param position the location of the observer
     * @param a the first angular detection boundary
     * @param b the second angular detection boundary
     * @return
     * <code>true</code> if the goal is between the angles a and b;
     * <code>false</code> otherwise.
     */
    protected boolean isGoalDetected(Point2D position, double angA, double angB) {
        Point2D ptA = new Point2D.Double(Math.acos(angA) + position.getX(),
                Math.asin(angA) + position.getY());
        Point2D ptB = new Point2D.Double(Math.acos(angB) + position.getX(),
                Math.asin(angB) + position.getY());

        // Creates a segment from a to b (within the robot) and a segment from the current position
        // to the goal.  If the segments intersect then the method returns true; false otherwise
        Line2D segAB = new Line2D.Double(ptA, ptB);
        Line2D pathToGoal = new Line2D.Double(position, myMap.getGoal());
        return segAB.intersectsLine(pathToGoal);
    }

    public double distToGoal() {
        return myRobot.getPosition().distance(myMap.getGoal());
    }

    public Maze getMap() {
        return myMap;
    }

    public Robot getRobot() {
        return myRobot;
    }

    public Path getPath() {
        return new Path(path);
    }
    
    private final Maze myMap;
    private final Robot myRobot;
    private final Behavior myRobotBehavior;
    private Path path;
    private static final int RESOLUTION = 1;
}
