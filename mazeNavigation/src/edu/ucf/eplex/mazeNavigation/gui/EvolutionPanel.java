/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.gui;

import edu.ucf.eplex.mazeNavigation.model.Maze;
import edu.ucf.eplex.mazeNavigation.model.Position;
import edu.ucf.eplex.poaiecFramework.domain.EvolutionObserver;
import edu.ucf.eplex.poaiecFramework.domain.Observable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Collection;

import javax.swing.JPanel;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class EvolutionPanel extends JPanel implements EvolutionObserver<Position> {

    public EvolutionPanel() {}
    
    public EvolutionPanel(Observable<Position> subject) {
        assert (subject != null);
        subject.registerObserver(this);
        setBackground(Color.WHITE);
    }

    public void update(Collection<Position> results, Collection<Position> archive, Position fittest, Position mostNovel) {
//        theMap = null;
        pts = results;
        archived = archive;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(f_padding / 2, f_padding / 2);

        int x, y, dx, dy;

        if (theMap != null) {

            // Draw the walls of the maze map
            g.setColor(Color.WHITE);
            for (Line2D wall : theMap.getWalls()) {
                g2.draw(scale(wall));
            }

            // Draw the starting position
            x = scale(theMap.getStart().getX()) - scale(f_startSize / 2);
            y = scale(theMap.getStart().getY()) - scale(f_startSize / 2);
            dx = scale(f_startSize);
            dy = scale(f_startSize);
            g2.draw(new Ellipse2D.Double(x, y, dx, dy));

            // Draw the goal position
            x = scale(theMap.getGoal().getX()) - scale(f_goalSize / 2);
            y = scale(theMap.getGoal().getY()) - scale(f_goalSize / 2);
            dx = scale(f_goalSize);
            dy = scale(f_goalSize);
            g2.draw(new Ellipse2D.Double(x, y, dx, dy));
        }

        if (archived != null) {
            // Draw the archived robot positions
            g2.setColor(Color.BLACK);
            for (Position pt : archived) {
                x = scale(pt.getX()) - scale(1.5);
                y = scale(pt.getY()) - scale(1.5);
                dx = scale(3);
                dy = scale(3);
                g2.fill(new Ellipse2D.Double(x, y, dx, dy));
            }
        }

        if (pts != null) {
            // Draw the final robot positions
            g2.setColor(Color.RED);
            for (Position pt : pts) {
                x = scale(pt.getX()) - scale(1);
                y = scale(pt.getY()) - scale(1);
                dx = scale(2);
                dy = scale(2);
                g2.fill(new Ellipse2D.Double(x, y, dx, dy));
            }
        }
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
        double mapWidth = theMap.getWidth(),
                mapHeight = theMap.getHeight(),
                frac = mapWidth / mapHeight;

        if (getWidth() / frac < getHeight()) {
            return (int) (a * (getWidth() - f_padding) / mapWidth);
        } else {
            return (int) (a * (getHeight() - f_padding) / mapHeight);
        }
    }
    private Maze theMap = Maze.getHardMap();
    private Collection<Position> pts;
    private Collection<Position> archived;
    private final int f_startSize = 6;
    private final int f_goalSize = 10;
    private final int f_padding = 10;
    /** **/
    private static final long serialVersionUID = -8870295260824507665L;
}
