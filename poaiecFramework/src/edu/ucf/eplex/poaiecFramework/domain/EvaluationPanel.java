/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * created by Brian Woolley on Sept 23, 2011
 */
package edu.ucf.eplex.poaiecFramework.domain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;

/**
 *
 * @author Brian Woolley (brian.woolley at ieee.org)
 */
public class EvaluationPanel extends JPanel {
    private EvaluationDomain<?> subject;

    /**
     * 
     * @param g 
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (subject == null) {
            g.setColor(Color.BLUE);
            Graphics2D g2 = (Graphics2D) g;
            g2.draw(new Ellipse2D.Double(5, 5, getWidth() - 10, getHeight() - 10));
        } else {
            subject.paintComponent(this, g, getSize());
        }
    }

    private static final long serialVersionUID = 4924058372651239301L;

    public void setDomain(EvaluationDomain<?> domain) {
        if (domain != null) {
            subject = domain;
        }
    }
}
