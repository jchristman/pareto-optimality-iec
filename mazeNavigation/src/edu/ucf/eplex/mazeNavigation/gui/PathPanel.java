/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.gui;

import edu.ucf.eplex.mazeNavigation.model.Environment;
import edu.ucf.eplex.mazeNavigation.model.Maze;
import edu.ucf.eplex.mazeNavigation.model.Path;
import edu.ucf.eplex.mazeNavigation.model.Position;
import edu.ucf.eplex.mazeNavigation.util.MazeRenderingTool;
import java.awt.Graphics;
import java.util.Collection;
import java.util.List;
import javax.swing.JPanel;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class PathPanel extends JPanel {

    public PathPanel() {
        this(Maze.getHardMap());
    }

    public PathPanel(Maze map) {
        this(map, null, null);
    }

    public PathPanel(Maze map, Path path) {
        this(map, path, null);
    }

    public PathPanel(Maze map, Path path, Collection<Position> archived) {
        theMap = map;
        thePath = path;
        theArchive = archived;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        render.renderEnvironment(theMap, thePath, g, getSize());
//        Graphics2D g2 = (Graphics2D) g;
////        g2.translate(f_padding / 2, f_padding / 2);
//
//        int x, y, dx, dy;
//        // Draw the selected/unselected boarder
////        g.setColor(borderColor);
////        x = scale(0);
////        y = scale(0);
////        dx = scale(theMap.getHeight() + f_padding);
////        dy = scale(theMap.getWidth() + f_padding);
////        g2.draw(new Rectangle2D.Double(x, y, dx, dy));
//
//        // Draw the walls of the maze map
//        g.setColor(Color.DARK_GRAY);
//        for (Line2D wall : theMap.getWalls()) {
//            g2.draw(scale(wall));
//        }
//
//        // Draw the starting position
//        x = scale(theMap.getStart().getX() - f_startSize / 2);
//        y = scale(theMap.getStart().getY() - f_startSize / 2);
//        dx = scale(f_startSize);
//        dy = scale(f_startSize);
//        g2.draw(new Ellipse2D.Double(x, y, dx, dy));
//
//        // Draw the goal position
//        x = scale(theMap.getGoal().getX() - f_goalSize / 2);
//        y = scale(theMap.getGoal().getY() - f_goalSize / 2);
//        dx = scale(f_goalSize);
//        dy = scale(f_goalSize);
//        g2.draw(new Ellipse2D.Double(x, y, dx, dy));
//
//        if (theArchive != null) {
//            // Draw the archived robot positions
//            g2.setColor(Color.DARK_GRAY);
//            for (Position pt : theArchive) {
//                x = scale(pt.getX() - 1);
//                y = scale(pt.getY() - 1);
//                dx = 2;
//                dy = 2;
//                g2.fill(new Ellipse2D.Double(x, y, dx, dy));
//            }
//        }
//
//        if (thePath != null) {
//            // Draw the robot path as positions that fade from dark gray (55, 55, 55) to white (255, 255, 255)
//            double color = 200.0;
//            double step = color / thePath.size();
//
//            for (Position pt : thePath) {
//                g2.setColor(new Color((int) color, (int) color, (int) color));
//                x = scale(pt.getX() - 2);
//                y = scale(pt.getY() - 2);
//                dx = scale(4);
//                dy = scale(4);
//                g2.fill(new Ellipse2D.Double(x, y, dx, dy));
//                color -= step;
//                color = Math.max(Math.min(color, 255), 0);
//            }
//        }
//    }
//
//    private Line2D scale(Line2D line) {
//        Point2D p1 = scale(line.getP1()),
//                p2 = scale(line.getP2());
//        return new Line2D.Double(p1, p2);
//    }
//
//    private Point2D scale(Point2D pt) {
//        int x = scale(pt.getX()),
//                y = scale(pt.getY());
//        return new Point2D.Double(x, y);
//    }
//
//    private int scale(double a) {
//        double mapWidth = theMap.getWidth(),
//                mapHeight = theMap.getHeight(),
//                frac = mapWidth / mapHeight;
//
//        if (getWidth() / frac < getHeight()) {
//            return (int) (a * (getWidth() - f_padding) / mapWidth) + f_padding/2;
//        } else {
//            return (int) (a * (getHeight() - f_padding) / mapHeight) + f_padding/2;
//        }
    }

    public Collection<Position> getTheArchive() {
        return theArchive;
    }

    public void setTheArchive(Collection<Position> aNewArchive) {
        theArchive = aNewArchive;
    }

    public Maze getTheMap() {
        return theMap;
    }

    public List<Position> getThePath() {
        return thePath;
    }
    
    public void update(Environment env) {
        update(env, null);
    }
    
    public void update(Environment env, Collection<Position> archivePts) {
        theMap = env.getMap();
        thePath = env.getPath();
        theArchive = archivePts;
        repaint();
    }

    private MazeRenderingTool render = new MazeRenderingTool();
    private Maze theMap;
    private Path thePath;
    private Collection<Position> theArchive;
    /** **/
    private static final long serialVersionUID = 4753232631345036886L;

}
