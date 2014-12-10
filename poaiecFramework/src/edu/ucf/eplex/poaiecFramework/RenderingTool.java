/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.poaiecFramework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class RenderingTool {

	public RenderingTool() {
		super();
	}

	public void applyBackground(Graphics g, Dimension size, Color color) {
	    g.setColor(color);
	    Graphics2D g2 = (Graphics2D) g;
	    g2.fill(new Rectangle2D.Double(0, 0, size.width, size.height));
	}

}