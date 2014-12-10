/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import edu.ucf.eplex.mazeNavigation.model.Environment;
import edu.ucf.eplex.mazeNavigation.model.Maze;
import edu.ucf.eplex.mazeNavigation.model.Path;
import edu.ucf.eplex.mazeNavigation.model.Robot;
import edu.ucf.eplex.mazeNavigation.util.MazeRenderingTool;
import edu.ucf.eplex.mazeNavigation.util.Observable;
import edu.ucf.eplex.mazeNavigation.util.SimulationObserver;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class MapPanel extends JPanel implements SimulationObserver {

    public MapPanel(Observable subject) {
        assert (subject != null);
        subject.registerObserver(this);
        repaint();
    }

    public void update(Environment env) {
        theMap = env.getMap();
        theRobot = env.getRobot();
        thePath = env.getPath();
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        renderer.renderEnvironment(theMap, theRobot, thePath, g, getSize());
    }

    private Maze theMap;
    private Robot theRobot;
    private Path thePath;
    private MazeRenderingTool renderer = new MazeRenderingTool();
    /**
     * 
     */
    private static final long serialVersionUID = -8870295260824507665L;
}
