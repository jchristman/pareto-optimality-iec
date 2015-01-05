/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.model;

import edu.ucf.eplex.mazeNavigation.util.Util;

import java.awt.geom.Point2D;

import org.w3c.dom.Node;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class Position extends Point2D {

    public Position(Node anXmlPosition) {
        loadFromXml(anXmlPosition);
    }
    
    public Position(Position pose) {
        this(pose.getX(), pose.getY(), pose.getTheta());
    }

    public Position(Point2D location, double theta) {
        this(location.getX(), location.getY(), theta);
    }

    public Position(double x, double y, double theta) {
        super();
        setLocation(x, y, theta);
    }

    public boolean equals(Position pose) {
        boolean equal = true;
        if (x != pose.x) {
            equal = false;
        }
        if (y != pose.y) {
            equal = false;
        }
        if (theta != pose.theta) {
            equal = false;
        }
        return equal;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    public double getTheta() {
        return theta;
    }
    
    public Point2D getPoint() {
    	return new Point2D.Double(x, y);
    }

    @Override
    public void setLocation(double x, double y) {
        setLocation(x, y, theta);
    }

    public void setLocation(double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = Util.normalize(theta);
    }

    public void setLocation(Position pose) {
        setLocation(pose.getX(), pose.getY(), pose.getTheta());
    }

    public void setLocation(Point2D pt, double theta) {
        setLocation(pt.getX(), pt.getY(), theta);
    }

    public Position add(double dx, double dy, double dTheta) {
        double x = this.x + dx,
               y = this.y + dy,
               t = Util.normalize(theta + dTheta);
        return new Position(x, y, t);
    }
    
	public double[] toArray() {
		double[] retVal = {getX(), getY(), getTheta()};
		return retVal;
	}

	public String toXml() {
        StringBuilder result = new StringBuilder();
        
        result.append(indent(2)).append(open(POSITION_TAG));
        result.append(indent(3)).append(textContentElement(X_COORD_TAG, x));
        result.append(indent(3)).append(textContentElement(Y_COORD_TAG, y));
        result.append(indent(3)).append(textContentElement(THETA_TAG, theta));
        result.append(indent(2)).append(close(POSITION_TAG));
        
        return result.toString();
    }
    
    public void loadFromXml(Node pose) {
//      <position>
//          <x>87.6263944596585</x>
//          <y>124.21195825030019</y>
//          <theta>-2.5594879033321343</theta>
//      </position>
        if (pose.getNodeName().equalsIgnoreCase(POSITION_TAG) ||
        	pose.getNodeName().equalsIgnoreCase(ENDPOINT_TAG)) {
            Node coord = pose.getFirstChild();
            while (coord != null) {
                if (coord.getNodeName().equalsIgnoreCase(X_COORD_TAG)) {
                    x = java.lang.Double.parseDouble(coord.getTextContent());                 
                }
                
                if (coord.getNodeName().equalsIgnoreCase(Y_COORD_TAG)) {
                    y = java.lang.Double.parseDouble(coord.getTextContent());                                     
                }
                
                if (coord.getNodeName().equalsIgnoreCase(THETA_TAG)) {                    
                    theta = java.lang.Double.parseDouble(coord.getTextContent());                 
                }

                coord = coord.getNextSibling();
            }
        }

    }
    
    private String open(String label) {
        return "<" + label + ">\n";
    }

    @SuppressWarnings("unused")
	private String open(String label, String attributes) {
        return new StringBuilder().append("<").append(label).append(" ").append(attributes).append(">\n").toString();
    }

    private String close(String label) {
        return "</" + label + ">\n";
    }

    private String textContentElement(String label, Object value) {
        return "<" + label + ">" + value.toString() + "</" + label + ">\n";
    }

    private String indent(int x) {
        StringBuilder indention = new StringBuilder();
        for (int i = 0; i < x; i++) {
            indention.append("    ");
        }
        return indention.toString();
    }

    private double x, y, theta;
    
    public final static String POSITION_TAG = "position";
    public final static String ENDPOINT_TAG = "endpoint";
    public final static String X_COORD_TAG = "x";
    public final static String Y_COORD_TAG = "y";
    public final static String THETA_TAG = "theta";

    
    /** Auto-generated serialID */
    @SuppressWarnings("unused")
	private static final long serialVersionUID = 5599701409360107978L;
}
