/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.util;

import java.awt.Dimension;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.imageio.ImageIO;

import org.jgap.Chromosome;
import org.w3c.dom.Node;

import edu.ucf.eplex.mazeNavigation.gui.PathPanel;
import edu.ucf.eplex.mazeNavigation.model.Environment;
import edu.ucf.eplex.mazeNavigation.model.Maze;

/**
 * 
 * @author Brian Woolley (brian.woolley@ieee.org)
 *
 */
public class Util {

    private Util() {
    }
    private static Util thisInstance = new Util();

    public static Util getInstance() {
        return thisInstance;
    }

    /**
     * Calculates the radian angle from point a to point b .
     * @param a The reference point.
     * @param b Another point in the space.
     * @return The radian angle from point a to point b.
     */
    public static double angleOf(Point2D a, Point2D b) {
        // Calculate angle from a to b
        double x = b.getX() - a.getX();
        double y = b.getY() - a.getY();
        return Math.atan2(y, x);
    }

    /**
     * Adds two radian angles and returns a value between -Pi and Pi.
     * @param a The first radian angle.
     * @param b Another radian angle.
     * @return The sum of two radian angles, normalized to a value between -Pi and Pi.
     */
    public static double addAngles(double a, double b) {
        return Util.normalize(a + b);
    }

    /**
     * Adjusts radian angles to be values between -Pi and Pi.
     * @param raw The raw radian angle to be adjusted.
     * @return The adjusted radian angle (values are between -Pi and Pi).
     */
    public static double normalize(double raw) {
        while (raw < -Math.PI) {
            raw += 2 * Math.PI;
        }
        while (raw > Math.PI) {
            raw -= 2 * Math.PI;
        }
        return raw;
    }
    /**
     * Computes the intersection between two lines. The calculated point is approximate, 
     * since integers are used. If you need a more precise result, use doubles
     * everywhere. 
     * (c) 2007 Alexander Hristov. Use Freely (LGPL license). http://www.ahristov.com
     *
     * @param x1 Point 1 of Line 1
     * @param y1 Point 1 of Line 1
     * @param x2 Point 2 of Line 1
     * @param y2 Point 2 of Line 1
     * @param x3 Point 1 of Line 2
     * @param y3 Point 1 of Line 2
     * @param x4 Point 2 of Line 2
     * @param y4 Point 2 of Line 2
     * @return Point where the segments intersect, or null if they don't
     */
    public static Point2D intersection(Line2D a, Line2D b) {
        double x1 = a.getX1(), y1 = a.getY1(), x2 = a.getX2(), y2 = a.getY2(),
                x3 = b.getX1(), y3 = b.getY1(), x4 = b.getX2(), y4 = b.getY2();
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0) {
            return null;
        }

        double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

        return new Point2D.Double(xi, yi);
    }

    public void saveBehavior(Chromosome chrom, Environment env) {
        // if phenotypes directory does not exist, then create it
        String phenotypeDirStr = "./db/phenotypes";
        File phenotypeDir = new File(phenotypeDirStr);
        File outImg = new File(phenotypeDirStr + "/" + chrom.getId() + ".png");

        if (!phenotypeDir.exists()) {
            phenotypeDir.mkdirs();
        }
        if (!phenotypeDir.exists()) {
            throw new IllegalArgumentException("base directory does not exist: " + phenotypeDirStr);
        }
        if (!phenotypeDir.isDirectory()) {
            throw new IllegalArgumentException("base directory is a file: " + phenotypeDirStr);
        }
        if (!phenotypeDir.canWrite()) {
            throw new IllegalArgumentException("base directory not writable: " + phenotypeDirStr);
        }

        // create a new PathPanel based on the map and the path
        PathPanel pathPanel = new PathPanel(env.getMap(), env.getPath());
        pathPanel.setSize(5 * env.getMap().getWidth(), 5 * env.getMap().getHeight());

        //create a buffered image based on the panel
        BufferedImage bi = new BufferedImage(pathPanel.getWidth(), pathPanel.getHeight(), BufferedImage.TYPE_INT_RGB);

        //paint the map and the robot path onto the buffered image
        bi.getGraphics();
        pathPanel.paint(bi.getGraphics());

        // save image file
        try {
            ImageIO.write(bi, "PNG", outImg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//		throw new UnsupportedOperationException("saveBehavior() not currently implemented.");


    }
    /**
     * 
     * @param allPoints
     * @param aMaze
     * @return
     */
    public static BufferedImage paintTheRunDistribution(Collection<Point2D> allPoints, Maze aMaze) {
        int imageWidth = 4 * (int) Math.round(aMaze.getSize().getWidth());
        int imageHeight = 4 * (int) Math.round(aMaze.getSize().getHeight());
        BufferedImage bi = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Dimension imageDimension = new Dimension(imageWidth, imageHeight);
        new MazeRenderingTool().renderPointsVisited(aMaze, allPoints, bi.getGraphics(), imageDimension);
        return bi;
    }

    /**
     * Writes a buffered image out as a "png" file. Make any directories needed for the target file 
     * name if they don't exit.
     *  
     * @param image The image to be written out.
     * @param fileName The target file to be written, (dirs will be created and ".png" will be appended.
     * @throws IOException
     */
    public static void saveTo(BufferedImage image, String fileName) throws IOException {
        new File(fileName + ".png").mkdirs();
        File outputfile = new File(fileName + ".png");
        ImageIO.write(image, "png", outputfile);
    }

	public static boolean getBoolean(Node xmlNode) {
		return Boolean.parseBoolean(xmlNode.getTextContent());
	}

	public static long getLong(Node xmlNode) {
		return Long.parseLong(xmlNode.getTextContent());
	}

	public static long getLong(Node xmlNode, String attributeLabel) {
		return Long.parseLong(xmlNode.getAttributes().getNamedItem(attributeLabel).getNodeValue());
	}

}
