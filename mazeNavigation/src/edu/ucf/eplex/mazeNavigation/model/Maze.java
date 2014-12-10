/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.model;

import java.awt.Dimension;
import java.awt.geom.Line2D;
import java.util.*;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 */
public class Maze {

    public static final int HARD_MAP = 0,
            MEDIUM_MAP = 1;

    private Maze(int mapType) {
        if (mapType == HARD_MAP) {
            setupClassicHardMap();
        }
        if (mapType == MEDIUM_MAP) {
            setupClassicMedMap();
        }
        walls = Collections.unmodifiableCollection(walls);
        breadCrumbs = Collections.unmodifiableList(breadCrumbs);
    }
    
    /**
     * 
     * @return 
     */
    public static Maze getMediumMap() {
        return instanceOfMediumMap;
    }
    
    /**
     * 
     * @return 
     */
    public static Maze getHardMap() {
        return instanceOfHardMap;
    }
    
    @SuppressWarnings("unused")
	private void loadMap(String aFile) {
        // TODO Open file stream
        // walls.clear();
        // TODO set the starting position and heading
        // TODO set the goal location
        // TODO Create walls from file
        calculateDimensions();
    }

    private void calculateDimensions() {
        int x = 0, y = 0;
        for (Line2D wall : walls) {
            x = (int) Math.max(x, Math.max(wall.getX1(), wall.getX2()));
            y = (int) Math.max(y, Math.max(wall.getY1(), wall.getY2()));
        }
        dimensions = new Dimension(x, y);
    }

    public Dimension getSize() {
        return dimensions;
    }

    public int getWidth() {
        return getSize().width;
    }

    public int getHeight() {
        return getSize().height;
    }

    public Collection<Line2D> getWalls() {
        return walls;
    }

    public List<Position> getBreadCrumbs() {
        return breadCrumbs;
    }
    
    public Position getStart() {
        return start;
    }

    public Position getGoal() {
        return goal;
    }

    private void setupClassicHardMap() {
        start = new Position(36, 184, 0);
        goal = new Position(31, 20, 0);


        walls.clear();
        walls.add(new Line2D.Double(41, 5, 3, 8));
        walls.add(new Line2D.Double(3, 8, 4, 49));
        walls.add(new Line2D.Double(4, 49, 57, 53));
        walls.add(new Line2D.Double(4, 49, 7, 202));
        walls.add(new Line2D.Double(7, 202, 195, 198));
        walls.add(new Line2D.Double(195, 198, 186, 8));
        walls.add(new Line2D.Double(186, 8, 39, 5));
        walls.add(new Line2D.Double(56, 54, 56, 157));
        walls.add(new Line2D.Double(57, 106, 158, 162));
        walls.add(new Line2D.Double(77, 201, 108, 164));
        walls.add(new Line2D.Double(6, 80, 33, 121));
        walls.add(new Line2D.Double(192, 146, 87, 91));
        walls.add(new Line2D.Double(56, 55, 133, 30));
        calculateDimensions();
        
        breadCrumbs.add(new Position(100, 150, 0));
        breadCrumbs.add(new Position(157, 180, 0));
        breadCrumbs.add(new Position(162, 148, 0));
        breadCrumbs.add(new Position(82, 104, 0));
        breadCrumbs.add(new Position(85, 70, 0));
        breadCrumbs.add(new Position(135, 19, 0));
//        breadCrumbs.add(new Position(65, 30, 0));
        breadCrumbs.add(goal);
    }

    private void setupClassicMedMap() {
        start = new Position(30, 22, 0);
        goal = new Position(270, 100, 0);

        walls.clear();
        walls.add(new Line2D.Double(293, 7, 289, 130));
        walls.add(new Line2D.Double(289, 130, 6, 134));
        walls.add(new Line2D.Double(6, 134, 8, 5));
        walls.add(new Line2D.Double(8, 5, 292, 7));
        walls.add(new Line2D.Double(241, 130, 58, 65));
        walls.add(new Line2D.Double(114, 7, 73, 42));
        walls.add(new Line2D.Double(130, 91, 107, 46)); //
        walls.add(new Line2D.Double(196, 8, 139, 51));
        walls.add(new Line2D.Double(219, 122, 182, 63)); //
        walls.add(new Line2D.Double(267, 9, 214, 63));
        walls.add(new Line2D.Double(271, 129, 237, 88));
        calculateDimensions();
        
        breadCrumbs.add(new Position(65, 52, 0));
        breadCrumbs.add(new Position(100, 35, 0));
        breadCrumbs.add(new Position(128, 58, 0));
        breadCrumbs.add(new Position(173, 49, 0));
        breadCrumbs.add(new Position(204, 75, 0));
        breadCrumbs.add(new Position(230, 75, 0));
        breadCrumbs.add(goal);
//        setHardMapSolutionPath();
    }

    @SuppressWarnings("unused")
	private void setupHardMap() {
        start = new Position(36, 184, 0);
        goal = new Position(31, 20, 0);

        walls.clear();
        walls.add(new Line2D.Double(200, 0, 0, 0));
        walls.add(new Line2D.Double(0, 0, 0, 200));
        walls.add(new Line2D.Double(0, 50, 57, 50));
        walls.add(new Line2D.Double(0, 200, 200, 200));
        walls.add(new Line2D.Double(200, 200, 200, 0));
        walls.add(new Line2D.Double(57, 50, 57, 157));
        walls.add(new Line2D.Double(57, 106, 158, 162));
        walls.add(new Line2D.Double(77, 200, 108, 164));
        walls.add(new Line2D.Double(0, 80, 33, 121));
        walls.add(new Line2D.Double(200, 146, 95, 91));
        walls.add(new Line2D.Double(57, 50, 135, 30));
        calculateDimensions();
    }

    @SuppressWarnings("unused")
	private void setupMedMap() {
        start = new Position(30, 22, 0);
        goal = new Position(270, 100, 0);

        walls.clear();
        walls.add(new Line2D.Double(300, 0, 300, 135));
        walls.add(new Line2D.Double(300, 135, 0, 135));
        walls.add(new Line2D.Double(0, 135, 0, 0));
        walls.add(new Line2D.Double(0, 0, 300, 0));
        walls.add(new Line2D.Double(241, 135, 58, 65));
        walls.add(new Line2D.Double(114, 0, 73, 42));
        walls.add(new Line2D.Double(134, 94, 104, 49)); //
        walls.add(new Line2D.Double(196, 0, 139, 51));
        walls.add(new Line2D.Double(217, 126, 180, 67)); //
        walls.add(new Line2D.Double(267, 0, 214, 63));
        walls.add(new Line2D.Double(271, 135, 237, 88));
        calculateDimensions();
    }

    private static Maze instanceOfMediumMap = new Maze(MEDIUM_MAP);    
    private static Maze instanceOfHardMap = new Maze(HARD_MAP);
    protected Position start;
    protected Position goal;
    protected Collection<Line2D> walls = new HashSet<Line2D>();
    protected List<Position> breadCrumbs = new ArrayList<Position>();
    protected Dimension dimensions;

    @SuppressWarnings("unused")
	private void setHardMapSolutionPath() {
breadCrumbs.add(new Position(37, 184, 0));
breadCrumbs.add(new Position(39, 184, 0));
breadCrumbs.add(new Position(41, 184, 0));
breadCrumbs.add(new Position(43, 184, 0));
breadCrumbs.add(new Position(46, 183, 0));
breadCrumbs.add(new Position(49, 183, 0));
breadCrumbs.add(new Position(52, 182, 0));
breadCrumbs.add(new Position(55, 181, 0));
breadCrumbs.add(new Position(58, 180, 0));
breadCrumbs.add(new Position(61, 179, 0));
breadCrumbs.add(new Position(64, 178, 0));
breadCrumbs.add(new Position(66, 176, 0));
breadCrumbs.add(new Position(69, 175, 0));
breadCrumbs.add(new Position(71, 173, 0));
breadCrumbs.add(new Position(74, 171, 0));
breadCrumbs.add(new Position(76, 169, 0));
breadCrumbs.add(new Position(78, 167, 0));
breadCrumbs.add(new Position(80, 165, 0));
breadCrumbs.add(new Position(83, 163, 0));
breadCrumbs.add(new Position(85, 161, 0));
breadCrumbs.add(new Position(87, 159, 0));
breadCrumbs.add(new Position(89, 157, 0));
breadCrumbs.add(new Position(92, 155, 0));
breadCrumbs.add(new Position(94, 153, 0));
breadCrumbs.add(new Position(96, 152, 0));
breadCrumbs.add(new Position(99, 150, 0));
breadCrumbs.add(new Position(101, 148, 0));
breadCrumbs.add(new Position(104, 147, 0));
breadCrumbs.add(new Position(107, 146, 0));
breadCrumbs.add(new Position(110, 145, 0));
breadCrumbs.add(new Position(112, 146, 0));
breadCrumbs.add(new Position(115, 147, 0));
breadCrumbs.add(new Position(117, 149, 0));
breadCrumbs.add(new Position(120, 151, 0));
breadCrumbs.add(new Position(122, 152, 0));
breadCrumbs.add(new Position(125, 154, 0));
breadCrumbs.add(new Position(127, 157, 0));
breadCrumbs.add(new Position(129, 159, 0));
breadCrumbs.add(new Position(131, 161, 0));
breadCrumbs.add(new Position(132, 163, 0));
breadCrumbs.add(new Position(134, 166, 0));
breadCrumbs.add(new Position(136, 168, 0));
breadCrumbs.add(new Position(137, 171, 0));
breadCrumbs.add(new Position(138, 174, 0));
breadCrumbs.add(new Position(140, 177, 0));
breadCrumbs.add(new Position(141, 179, 0));
breadCrumbs.add(new Position(142, 182, 0));
breadCrumbs.add(new Position(143, 185, 0));
breadCrumbs.add(new Position(144, 188, 0));
breadCrumbs.add(new Position(145, 190, 0));
breadCrumbs.add(new Position(145, 190, 0));
breadCrumbs.add(new Position(148, 191, 0));
breadCrumbs.add(new Position(151, 191, 0));
breadCrumbs.add(new Position(154, 191, 0));
breadCrumbs.add(new Position(157, 190, 0));
breadCrumbs.add(new Position(160, 190, 0));
breadCrumbs.add(new Position(163, 189, 0));
breadCrumbs.add(new Position(166, 189, 0));
breadCrumbs.add(new Position(169, 188, 0));
breadCrumbs.add(new Position(172, 187, 0));
breadCrumbs.add(new Position(175, 186, 0));
breadCrumbs.add(new Position(177, 185, 0));
breadCrumbs.add(new Position(180, 184, 0));
breadCrumbs.add(new Position(183, 182, 0));
breadCrumbs.add(new Position(185, 181, 0));
breadCrumbs.add(new Position(186, 178, 0));
breadCrumbs.add(new Position(186, 175, 0));
breadCrumbs.add(new Position(185, 172, 0));
breadCrumbs.add(new Position(185, 169, 0));
breadCrumbs.add(new Position(184, 166, 0));
breadCrumbs.add(new Position(183, 163, 0));
breadCrumbs.add(new Position(182, 160, 0));
breadCrumbs.add(new Position(181, 157, 0));
breadCrumbs.add(new Position(180, 155, 0));
breadCrumbs.add(new Position(178, 152, 0));
breadCrumbs.add(new Position(177, 150, 0));
breadCrumbs.add(new Position(175, 147, 0));
breadCrumbs.add(new Position(173, 145, 0));
breadCrumbs.add(new Position(170, 144, 0));
breadCrumbs.add(new Position(168, 142, 0));
breadCrumbs.add(new Position(165, 141, 0));
breadCrumbs.add(new Position(162, 140, 0));
breadCrumbs.add(new Position(159, 139, 0));
breadCrumbs.add(new Position(156, 139, 0));
breadCrumbs.add(new Position(153, 138, 0));
breadCrumbs.add(new Position(150, 138, 0));
breadCrumbs.add(new Position(147, 137, 0));
breadCrumbs.add(new Position(144, 137, 0));
breadCrumbs.add(new Position(141, 137, 0));
breadCrumbs.add(new Position(138, 138, 0));
breadCrumbs.add(new Position(135, 138, 0));
breadCrumbs.add(new Position(132, 138, 0));
breadCrumbs.add(new Position(130, 137, 0));
breadCrumbs.add(new Position(130, 137, 0));
breadCrumbs.add(new Position(127, 136, 0));
breadCrumbs.add(new Position(124, 134, 0));
breadCrumbs.add(new Position(122, 132, 0));
breadCrumbs.add(new Position(120, 130, 0));
breadCrumbs.add(new Position(117, 128, 0));
breadCrumbs.add(new Position(115, 126, 0));
breadCrumbs.add(new Position(113, 124, 0));
breadCrumbs.add(new Position(111, 122, 0));
breadCrumbs.add(new Position(110, 119, 0));
breadCrumbs.add(new Position(108, 117, 0));
breadCrumbs.add(new Position(106, 114, 0));
breadCrumbs.add(new Position(105, 112, 0));
breadCrumbs.add(new Position(103, 109, 0));
breadCrumbs.add(new Position(101, 107, 0));
breadCrumbs.add(new Position(98, 106, 0));
breadCrumbs.add(new Position(95, 105, 0));
breadCrumbs.add(new Position(93, 104, 0));
breadCrumbs.add(new Position(90, 103, 0));
breadCrumbs.add(new Position(87, 102, 0));
breadCrumbs.add(new Position(84, 102, 0));
breadCrumbs.add(new Position(81, 101, 0));
breadCrumbs.add(new Position(78, 101, 0));
breadCrumbs.add(new Position(75, 101, 0));
breadCrumbs.add(new Position(72, 100, 0));
breadCrumbs.add(new Position(69, 100, 0));
breadCrumbs.add(new Position(66, 100, 0));
breadCrumbs.add(new Position(64, 97, 0));
breadCrumbs.add(new Position(64, 94, 0));
breadCrumbs.add(new Position(64, 91, 0));
breadCrumbs.add(new Position(64, 88, 0));
breadCrumbs.add(new Position(65, 86, 0));
breadCrumbs.add(new Position(65, 83, 0));
breadCrumbs.add(new Position(66, 80, 0));
breadCrumbs.add(new Position(67, 77, 0));
breadCrumbs.add(new Position(68, 74, 0));
breadCrumbs.add(new Position(69, 71, 0));
breadCrumbs.add(new Position(71, 69, 0));
breadCrumbs.add(new Position(72, 66, 0));
breadCrumbs.add(new Position(74, 63, 0));
breadCrumbs.add(new Position(75, 61, 0));
breadCrumbs.add(new Position(77, 59, 0));
breadCrumbs.add(new Position(79, 56, 0));
breadCrumbs.add(new Position(82, 55, 0));
breadCrumbs.add(new Position(85, 54, 0));
breadCrumbs.add(new Position(88, 54, 0));
breadCrumbs.add(new Position(91, 53, 0));
breadCrumbs.add(new Position(94, 53, 0));
breadCrumbs.add(new Position(97, 52, 0));
breadCrumbs.add(new Position(100, 52, 0));
breadCrumbs.add(new Position(103, 53, 0));
breadCrumbs.add(new Position(106, 53, 0));
breadCrumbs.add(new Position(109, 53, 0));
breadCrumbs.add(new Position(112, 54, 0));
breadCrumbs.add(new Position(114, 55, 0));
breadCrumbs.add(new Position(117, 55, 0));
breadCrumbs.add(new Position(120, 56, 0));
breadCrumbs.add(new Position(123, 57, 0));
breadCrumbs.add(new Position(126, 59, 0));
breadCrumbs.add(new Position(128, 60, 0));
breadCrumbs.add(new Position(131, 61, 0));
breadCrumbs.add(new Position(134, 63, 0));
breadCrumbs.add(new Position(137, 64, 0));
breadCrumbs.add(new Position(139, 65, 0));
breadCrumbs.add(new Position(142, 66, 0));
breadCrumbs.add(new Position(145, 67, 0));
breadCrumbs.add(new Position(148, 69, 0));
breadCrumbs.add(new Position(150, 69, 0));
breadCrumbs.add(new Position(153, 70, 0));
breadCrumbs.add(new Position(156, 71, 0));
breadCrumbs.add(new Position(159, 71, 0));
breadCrumbs.add(new Position(162, 72, 0));
breadCrumbs.add(new Position(165, 72, 0));
breadCrumbs.add(new Position(168, 72, 0));
breadCrumbs.add(new Position(171, 72, 0));
breadCrumbs.add(new Position(174, 71, 0));
breadCrumbs.add(new Position(177, 71, 0));
breadCrumbs.add(new Position(180, 70, 0));
breadCrumbs.add(new Position(181, 68, 0));
breadCrumbs.add(new Position(181, 65, 0));
breadCrumbs.add(new Position(180, 62, 0));
breadCrumbs.add(new Position(180, 59, 0));
breadCrumbs.add(new Position(179, 56, 0));
breadCrumbs.add(new Position(178, 53, 0));
breadCrumbs.add(new Position(177, 50, 0));
breadCrumbs.add(new Position(176, 47, 0));
breadCrumbs.add(new Position(175, 44, 0));
breadCrumbs.add(new Position(174, 42, 0));
breadCrumbs.add(new Position(172, 39, 0));
breadCrumbs.add(new Position(171, 36, 0));
breadCrumbs.add(new Position(169, 34, 0));
breadCrumbs.add(new Position(167, 32, 0));
breadCrumbs.add(new Position(165, 29, 0));
breadCrumbs.add(new Position(163, 27, 0));
breadCrumbs.add(new Position(161, 25, 0));
breadCrumbs.add(new Position(159, 23, 0));
breadCrumbs.add(new Position(156, 22, 0));
breadCrumbs.add(new Position(154, 20, 0));
breadCrumbs.add(new Position(151, 18, 0));
breadCrumbs.add(new Position(148, 17, 0));
breadCrumbs.add(new Position(146, 16, 0));
breadCrumbs.add(new Position(143, 15, 0));
breadCrumbs.add(new Position(140, 15, 0));
breadCrumbs.add(new Position(137, 15, 0));
breadCrumbs.add(new Position(134, 16, 0));
breadCrumbs.add(new Position(131, 16, 0));
breadCrumbs.add(new Position(128, 17, 0));
breadCrumbs.add(new Position(125, 17, 0));
breadCrumbs.add(new Position(122, 18, 0));
breadCrumbs.add(new Position(119, 19, 0));
breadCrumbs.add(new Position(116, 21, 0));
breadCrumbs.add(new Position(114, 22, 0));
breadCrumbs.add(new Position(111, 23, 0));
breadCrumbs.add(new Position(109, 25, 0));
breadCrumbs.add(new Position(106, 27, 0));
breadCrumbs.add(new Position(103, 28, 0));
breadCrumbs.add(new Position(101, 30, 0));
breadCrumbs.add(new Position(98, 31, 0));
breadCrumbs.add(new Position(96, 33, 0));
breadCrumbs.add(new Position(93, 34, 0));
breadCrumbs.add(new Position(90, 35, 0));
breadCrumbs.add(new Position(88, 36, 0));
breadCrumbs.add(new Position(85, 37, 0));
breadCrumbs.add(new Position(82, 37, 0));
breadCrumbs.add(new Position(79, 37, 0));
breadCrumbs.add(new Position(76, 37, 0));
breadCrumbs.add(new Position(73, 37, 0));
breadCrumbs.add(new Position(70, 37, 0));
breadCrumbs.add(new Position(67, 37, 0));
breadCrumbs.add(new Position(64, 36, 0));
breadCrumbs.add(new Position(61, 35, 0));
breadCrumbs.add(new Position(58, 34, 0));
breadCrumbs.add(new Position(55, 33, 0));
breadCrumbs.add(new Position(52, 32, 0));
breadCrumbs.add(new Position(49, 31, 0));
breadCrumbs.add(new Position(47, 30, 0));
breadCrumbs.add(new Position(44, 29, 0));
breadCrumbs.add(new Position(41, 28, 0));
breadCrumbs.add(new Position(38, 27, 0));
breadCrumbs.add(new Position(36, 26, 0));
breadCrumbs.add(new Position(33, 25, 0));    }
}
