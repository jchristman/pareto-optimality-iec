/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.gui;

import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JTextField;

import edu.ucf.eplex.mazeNavigation.behaviorFramework.KeyboardCtlr;
import edu.ucf.eplex.mazeNavigation.model.Environment;
import edu.ucf.eplex.mazeNavigation.model.Maze;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class HumanController extends JFrame {

    public HumanController(Maze theMap) {
        super("Deceptive Maze Simulation Viewer");

        f_keyboardCtlr = new KeyboardCtlr();
        f_env = new Environment(f_keyboardCtlr, theMap);

        f_panel = new MapPanel(f_env);
        f_input = new JTextField();
        f_input.addKeyListener(f_keyboardCtlr);

        getContentPane().add(f_input, BorderLayout.NORTH);
        getContentPane().add(f_panel, BorderLayout.CENTER);
        setSize(440, 480);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        run();
    }

    private void run() {
        long hack, delay;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                hack = System.currentTimeMillis();
                f_env.step();

//                BufferedImage bi = new BufferedImage(f_panel.getWidth(), f_panel.getHeight(), BufferedImage.TYPE_INT_RGB);
//                renderer.setBackground(Color.WHITE, bi.getGraphics(), f_panel.getSize());
//                renderer.renderEnvironment(f_env.getMap(), f_env.getRobot(), f_env.getPath(), bi.getGraphics(), f_panel.getSize());
//                ImageIO.write(bi, "png", new File("demo/a" + i + ".png"));
                
                delay = Math.min(System.currentTimeMillis() - hack, 100);
                Thread.sleep(100 - delay);
            } catch (InterruptedException ex) {
                Logger.getLogger(HumanController.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(HumanController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("hard.map")) {
                new HumanController(Maze.getHardMap());
            } else if (args[0].equalsIgnoreCase("medium.map")) {
                new HumanController(Maze.getMediumMap());
            } else {
                System.out.println("The values:  [medium.map] and [hard.map] are currently supported.");
            }
        } else {
            new HumanController(Maze.getHardMap());
        }
    }
    private final Environment f_env;
    private final KeyboardCtlr f_keyboardCtlr;
    private final MapPanel f_panel;
    private final JTextField f_input;
    /**
     * *
     */
    private static final long serialVersionUID = 6102710692892382207L;
}
