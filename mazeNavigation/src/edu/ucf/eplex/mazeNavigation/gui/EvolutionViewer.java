/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.gui;

import edu.ucf.eplex.mazeNavigation.model.Position;
import edu.ucf.eplex.poaiecFramework.domain.Observable;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class EvolutionViewer extends JFrame {

	public EvolutionViewer(Observable<Position> subject) {
		super("Deceptive Maze Simulation Viewer");

		f_panel  = new EvolutionPanel(subject);
		getContentPane().add(f_panel,  BorderLayout.CENTER);
		setSize(640, 480);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);		
	}

	private final EvolutionPanel f_panel;

	/**	 **/
	private static final long serialVersionUID = 6102710692892382207L;

}
