/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.util;

import edu.ucf.eplex.mazeNavigation.model.*;
import edu.ucf.eplex.poaiecFramework.RenderingTool;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.util.Collection;

/**
 *
 * @author Brian Woolley <brian.woolley at ieee.org>
 */
public class MazeRenderingTool extends RenderingTool{

    private Maze theMaze;
    private Robot theRobot;
    private Collection<Position> theArchive;
    private Dimension panelSize;
    private final int f_startSize = 4;
    private final int f_goalSize = 6;
    private final int f_padding = 10;

    @SuppressWarnings("unused")
	public void renderEnvironment(Maze maze, Position pose, Path path, Graphics g, Dimension size) {
    	assert (theMaze != null);
    	theMaze = maze;
    	theRobot = new Robot(theMaze, pose);
    	
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);

        drawTheMaze(theMaze, g, size);
//        drawTheBreadcrumbTrail(theMaze, g, size);

        drawRobotPath(path, g, size);

        drawRangefinderBeams(theRobot, g, size);
        drawTheRobot(theRobot, g, size);
        drawTheGoalDetector(theRobot, g, size);
        drawTheRobotHeading(theRobot, g, size);
    }

    @SuppressWarnings("unused")
	public void renderEnvironment(Maze theMaze, Robot theRobot, Path path, Graphics g, Dimension size) {
        this.theMaze = theMaze;
        this.theRobot = theRobot;
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);

        drawTheMaze(theMaze, g, size);
//        drawTheBreadcrumbTrail(theMaze, g, size);


        if (showPath) {
        	drawRobotPath(path, g, size);
        }

        if (showRobot) {
        	drawRangefinderBeams(theRobot, g, size);
        	drawTheRobot(theRobot, g, size);
        	drawTheGoalDetector(theRobot, g, size);
        	drawTheRobotHeading(theRobot, g, size);
        }
    }

    public void renderEnvironment(Maze theMaze, Robot theRobot, Graphics g, Dimension size) {
    	renderEnvironment(theMaze, theRobot, new Path(), g, size);
    }

    public void renderEnvironment(Maze theMaze, Path path, Graphics g, Dimension size) {
        this.theMaze = theMaze;
        drawTheMaze(theMaze, g, size);

        if (theArchive != null) {
            drawTheArchivedRobotPositions(theArchive, g, size);
        }

        if (path != null) {
            drawRobotPath(path, g, size);
        }
    }

	public void renderEnvironment(Maze theMaze, Path path, int currentTimeStep, Graphics g, Dimension size) {
		Robot robot = new Robot(theMaze);
		robot.setPosition(path.get(currentTimeStep));
		renderEnvironment(theMaze, robot, path, g, size);
	}

	public void renderPointsVisited(Maze theMaze, Collection<Point2D> points, Graphics g, Dimension size) {
        this.theMaze = theMaze;
        setBackground(Color.WHITE, g, size);
//        drawTheMaze(theMaze, g, size);
        drawPoints(points, Color.BLACK, g, size);
    }

    private Arc2D scale(Arc2D arc) {
        int x = scale(arc.getCenterX());
        int y = scale(arc.getCenterY());
        int radius = scale(2.0 * theRobot.getRadius());
        int type = arc.getArcType();
        double angSt = arc.getAngleStart();
        double angExt = arc.getAngleExtent();

        Arc2D newArc = new Arc2D.Double();
        newArc.setArcByCenter(x, y, radius, angSt, angExt, type);
        return newArc;
    }

    private Line2D scale(Line2D line) {
        Point2D p1 = scale(line.getP1()),
                p2 = scale(line.getP2());
        return new Line2D.Double(p1, p2);
    }

    private Point2D scale(Point2D pt) {
        int x = scale(pt.getX()),
                y = scale(pt.getY());
        return new Point2D.Double(x, y);
    }

    private int scale(double a) {
        double mapWidth = theMaze.getWidth();
        double mapHeight = theMaze.getHeight();
        double frac = mapWidth / mapHeight;

        if (getWidth() / frac < getHeight()) {
            return (int) (a * (getWidth() - f_padding) / mapWidth); 
        } else {
            return (int) (a * (getHeight() - f_padding) / mapHeight);
        }
    }

    private int getWidth() {
        return panelSize.width;
    }

    private int getHeight() {
        return panelSize.height;
    }

    private void setSize(Dimension size) {
        panelSize = size;
    }

    private void drawTheMaze(Maze theMaze, Graphics g, Dimension size) {
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);

        int x, y, dx, dy;

        // Draw the walls of the maze map
        g.setColor(Color.DARK_GRAY);
        for (Line2D wall : theMaze.getWalls()) {
            g2.draw(scale(wall));
        }

        // Draw the starting position
        x = scale(theMaze.getStart().getX() - f_startSize / 2);
        y = scale(theMaze.getStart().getY() - f_startSize / 2);
        dx = scale(f_startSize);
        dy = scale(f_startSize);
        g2.draw(new Ellipse2D.Double(x, y, dx, dy));

        // Draw the goal position
        x = scale(theMaze.getGoal().getX() - f_goalSize / 2);
        y = scale(theMaze.getGoal().getY() - f_goalSize / 2);
        dx = scale(f_goalSize);
        dy = scale(f_goalSize);
        g2.draw(new Ellipse2D.Double(x, y, dx, dy));
    }

    @SuppressWarnings("unused")
	private void drawTheBreadcrumbTrail(Maze theMaze, Graphics g, Dimension size) {
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);

        int x, y, dx, dy;
        g.setColor(Color.GREEN);
        for (Position crumb : theMaze.getBreadCrumbs()) {
            x = scale(crumb.getX() - f_startSize / 2);
            y = scale(crumb.getY() - f_startSize / 2);
            dx = scale(f_startSize);
            dy = scale(f_startSize);
            g2.fill(new Ellipse2D.Double(x, y, dx, dy));
        }
    }

    private void drawRangefinderBeams(Robot theRobot, Graphics g, Dimension size) {
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);

        g2.setColor(Color.RED);
        for (Line2D beam : theRobot.getSensorArray().getBeams()) {
            g2.draw(scale(beam));
        }
    }

    private void drawTheRobot(Robot theRobot, Graphics g, Dimension size) {
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);

        int x, y, dx, dy;
        x = scale(theRobot.getPosition().getX() - theRobot.getRadius());
        y = scale(theRobot.getPosition().getY() - theRobot.getRadius());
        dx = scale(2 * theRobot.getRadius());
        dy = scale(2 * theRobot.getRadius());
        g2.setColor(Color.BLUE);
        g2.fill(new Ellipse2D.Double(x, y, dx, dy));

    }

    private void drawTheGoalDetector(Robot theRobot, Graphics g, Dimension size) {
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);

        g2.setColor(Color.BLUE);
        for (Arc2D goalArc : theRobot.getSensorArray().getGoalArcs()) {
            g2.draw(scale(goalArc));
        }
    }

    private void drawTheRobotHeading(Robot theRobot, Graphics g, Dimension size) {
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);

        int x, y, dx, dy;
        x = scale(theRobot.getPosition().getX());
        y = scale(theRobot.getPosition().getY());
        dx = x + scale(theRobot.getRadius() * Math.cos(theRobot.getPosition().getTheta()));
        dy = y + scale(theRobot.getRadius() * Math.sin(theRobot.getPosition().getTheta()));
        g2.setColor(Color.WHITE);
        g2.draw(new Line2D.Double(x, y, dx, dy));
    }

    private void drawTheArchivedRobotPositions(Collection<Position> theArchive, Graphics g, Dimension size) {
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);

        int x, y, dx, dy;
        g2.setColor(Color.DARK_GRAY);
        for (Position pt : theArchive) {
            x = scale(pt.getX() - 1);
            y = scale(pt.getY() - 1);
            dx = 2;
            dy = 2;
            g2.fill(new Ellipse2D.Double(x, y, dx, dy));
        }
    }

    private void drawRobotPath(Path thePath, Graphics g, Dimension size) {
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);

        int x, y, dx, dy;
        // Draw the robot path as positions that fade from dark gray (55, 55, 55) to white (255, 255, 255)
        double color = 200.0;
        double step = color / thePath.size();

        for (Position pt : thePath) {
            g2.setColor(new Color((int) color, (int) color, (int) color));
            x = scale(pt.getX() - 4);
            y = scale(pt.getY() - 4);
            dx = scale(8);
            dy = scale(8);
            g2.fill(new Ellipse2D.Double(x, y, dx, dy));
            color -= step;
            color = Math.max(Math.min(color, 255), 0);
        }
    }

    public void setBackground(Color aColor, Graphics g, Dimension size) {
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);
        g2.setColor(aColor);
        g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
    }

    private void drawPoints(Collection<Point2D> points, Color aColor, Graphics g, Dimension size) {
        Graphics2D g2 = (Graphics2D) g;
        setSize(size);

        int x, y, dx, dy;
        g2.setColor(aColor);

        for (Point2D pt : points) {
            x = scale(pt.getX() - 1);
            y = scale(pt.getY() - 1);
            dx = scale(3);
            dy = scale(3);
            g2.fill(new Ellipse2D.Double(x, y, dx, dy));
        }
    }

    private boolean showRobot = true;
	public void setShowRobot(boolean selected) {
		showRobot = selected;
	}

	private boolean showPath = true;
	public void setShowPath(boolean selected) {
		showPath = selected;
	}
}
