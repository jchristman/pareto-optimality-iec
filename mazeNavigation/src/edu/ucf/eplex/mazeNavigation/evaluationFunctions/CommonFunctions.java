package edu.ucf.eplex.mazeNavigation.evaluationFunctions;

import java.awt.geom.Line2D;

import edu.ucf.eplex.mazeNavigation.model.Position;

public class CommonFunctions {
	public static double distance(Position p1, Position p2) {
		return p1.distance(p2);
	}
	
	public static double angle(Line2D line1, Line2D line2)
    {
        double angle1 = Math.atan2(line1.getY1() - line1.getY2(),
                                   line1.getX1() - line1.getX2());
        double angle2 = Math.atan2(line2.getY1() - line2.getY2(),
                                   line2.getX1() - line2.getX2());
        return Math.abs(angle1) - Math.abs(angle2);
    }
	
	public static double getVelocity(Position p1, Position p2) {
		double dx = p2.getX() - p1.getX(),
			   dv = dx / Math.cos(p1.getTheta());
		return dv;
	}
}
