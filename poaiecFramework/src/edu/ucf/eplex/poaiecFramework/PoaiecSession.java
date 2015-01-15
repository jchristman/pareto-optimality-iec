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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jgap.EvaluationFunction;
import org.jgap.ProgressListener;

import edu.ucf.eplex.poaiecFramework.controller.PoaiecController;
import edu.ucf.eplex.poaiecFramework.controller.PlayManager;
import edu.ucf.eplex.poaiecFramework.domain.DomainOptionsPanel;
import edu.ucf.eplex.poaiecFramework.domain.EvaluationDomain;
import edu.ucf.eplex.poaiecFramework.domain.EvaluationPanel;
import edu.ucf.eplex.poaiecFramework.domain.OptimizeOptionsPanel;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 */
public class PoaiecSession extends JFrame implements ProgressListener {

    private EvaluationDomain<?> f_domain;
    public PoaiecController poaiecSessionCtlr;
    private final List<EvaluationPanel> candidatePanels = new ArrayList<EvaluationPanel>();
    private Collection<Long> seenSolutions = new ArrayList<Long>();
    private ProgressMonitor progressMonitor;
    private PlayManager playMngr = new PlayManager();

    /**
     * Creates new form iecFrameworkApp
     *
     * @param domainClass
     * @throws IOException 
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public PoaiecSession(EvaluationDomain<?> domain) {

        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;


                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PoaiecSession.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PoaiecSession.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PoaiecSession.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PoaiecSession.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        try {
			initComponents();
		} catch (IOException ex) {
            java.util.logging.Logger.getLogger(PoaiecSession.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
        candidatePanels.add(Member000);
        candidatePanels.add(Member001);
        candidatePanels.add(Member2);
        candidatePanels.add(Member3);
        candidatePanels.add(Member4);
        candidatePanels.add(Member5);
        candidatePanels.add(Member6);
        candidatePanels.add(Member7);
        candidatePanels.add(Member8);
        candidatePanels.add(Member9);
        candidatePanels.add(Member010);
        candidatePanels.add(Member011);
        candidatePanels.add(Member12);
        candidatePanels.add(Member13);
        candidatePanels.add(Member14);
        candidatePanels.add(Member15);
        candidatePanels.add(Member16);
        candidatePanels.add(Member17);
        candidatePanels.add(Member18);
        candidatePanels.add(Member19);
        candidatePanels.add(Member20);
        candidatePanels.add(Member21);
        candidatePanels.add(Member22);
        candidatePanels.add(Member23);
        candidatePanels.add(Member24);
        candidatePanels.add(Member25);
        candidatePanels.add(Member26);
        candidatePanels.add(Member27);
        candidatePanels.add(Member28);
        candidatePanels.add(Member29);
        candidatePanels.add(Member30);
        candidatePanels.add(Member31);
        candidatePanels.add(Member32);
        candidatePanels.add(Member33);
        candidatePanels.add(Member34);
        candidatePanels.add(Member35);
        candidatePanels.add(Member36);
        candidatePanels.add(Member37);
        candidatePanels.add(Member38);
        candidatePanels.add(Member39);
        candidatePanels.add(Member40);
        candidatePanels.add(Member41);
        candidatePanels.add(Member42);
        candidatePanels.add(Member43);
        candidatePanels.add(Member44);
        candidatePanels.add(Member45);
        candidatePanels.add(Member46);
        candidatePanels.add(Member47);
        candidatePanels.add(Member48);
        candidatePanels.add(Member49);
        candidatePanels.add(Member50);
        candidatePanels.add(Member51);
        candidatePanels.add(Member52);
        candidatePanels.add(Member53);
        candidatePanels.add(Member54);
        candidatePanels.add(Member55);
        candidatePanels.add(Member56);
        candidatePanels.add(Member57);
        candidatePanels.add(Member58);
        candidatePanels.add(Member59);
        candidatePanels.add(Member60);
        candidatePanels.add(Member61);
        candidatePanels.add(Member62);
        candidatePanels.add(Member63);
        candidatePanels.add(Member64);
        candidatePanels.add(Member65);
        candidatePanels.add(Member66);
        candidatePanels.add(Member67);
        candidatePanels.add(Member68);
        candidatePanels.add(Member69);
        candidatePanels.add(Member70);
        candidatePanels.add(Member71);
        candidatePanels.add(Member72);
        candidatePanels.add(Member73);
        candidatePanels.add(Member74);
        candidatePanels.add(Member75);
        candidatePanels.add(Member76);
        candidatePanels.add(Member77);
        candidatePanels.add(Member78);
        candidatePanels.add(Member79);
        candidatePanels.add(Member80);
        candidatePanels.add(Member81);
        candidatePanels.add(Member82);
        candidatePanels.add(Member83);
        candidatePanels.add(Member84);
        candidatePanels.add(Member85);
        candidatePanels.add(Member86);
        candidatePanels.add(Member87);
        candidatePanels.add(Member88);
        candidatePanels.add(Member89);
        candidatePanels.add(Member90);
        candidatePanels.add(Member91);
        candidatePanels.add(Member92);
        candidatePanels.add(Member93);
        candidatePanels.add(Member94);
        candidatePanels.add(Member95);
        candidatePanels.add(Member96);
        candidatePanels.add(Member97);
        candidatePanels.add(Member98);
        candidatePanels.add(Member99);
        candidatePanels.add(Member100);
        candidatePanels.add(Member101);
        candidatePanels.add(Member102);
        candidatePanels.add(Member103);
        candidatePanels.add(Member104);
        candidatePanels.add(Member105);
        candidatePanels.add(Member106);
        candidatePanels.add(Member107);
        candidatePanels.add(Member108);
        candidatePanels.add(Member109);
        candidatePanels.add(Member110);
        candidatePanels.add(Member111);
        candidatePanels.add(Member112);
        candidatePanels.add(Member113);
        candidatePanels.add(Member114);
        candidatePanels.add(Member115);
        candidatePanels.add(Member116);
        candidatePanels.add(Member117);
        candidatePanels.add(Member118);
        candidatePanels.add(Member119);
        candidatePanels.add(Member120);
        candidatePanels.add(Member121);
        candidatePanels.add(Member122);
        candidatePanels.add(Member123);
        candidatePanels.add(Member124);
        candidatePanels.add(Member125);
        candidatePanels.add(Member126);
        candidatePanels.add(Member127);
        candidatePanels.add(Member128);
        candidatePanels.add(Member129);
        candidatePanels.add(Member130);
        candidatePanels.add(Member131);
        candidatePanels.add(Member132);
        candidatePanels.add(Member133);
        candidatePanels.add(Member134);
        candidatePanels.add(Member135);
        candidatePanels.add(Member136);
        candidatePanels.add(Member137);
        candidatePanels.add(Member138);
        candidatePanels.add(Member139);
        candidatePanels.add(Member140);
        candidatePanels.add(Member141);
        candidatePanels.add(Member142);
        candidatePanels.add(Member143);

        assert (domain != null);
        f_domain = domain;
        for (EvaluationPanel panel : candidatePanels) {
            f_domain.registerPanel(panel);
            panel.setDomain(f_domain);
        }

        System.out.println("Setting up PoaiecController...");
        poaiecSessionCtlr = new PoaiecController(f_domain);
        System.out.println("Done setting up PoaiecController.");

        // Configure the play manager...
        playMngr.registerDomain(f_domain);
        playMngr.registerSlider(AnimationProgressBar);
        playMngr.start();
        updateAllPanels();

        setVisible(true);
        //NoveltyMouseClicked(null);
        ParetoMouseClicked(null);
    }

//    public void start() {
//        playMngr.start();
//    }
//
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     * @throws IOException 
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() throws IOException {
        java.awt.GridBagConstraints gridBagConstraints;

        icons = new HashMap<String, Icon>();
        icons.put("fitness", new ImageIcon(ImageIO.read(getClass().getResourceAsStream("icons/fitness.png"))));
        icons.put("loop", new ImageIcon(ImageIO.read(getClass().getResourceAsStream("icons/loop.png"))));
        icons.put("novelty", new ImageIcon(ImageIO.read(getClass().getResourceAsStream("icons/novelty.png"))));
        icons.put("pause", new ImageIcon(ImageIO.read(getClass().getResourceAsStream("icons/pause.png"))));
        icons.put("play", new ImageIcon(ImageIO.read(getClass().getResourceAsStream("icons/play.png"))));
        icons.put("publish", new ImageIcon(ImageIO.read(getClass().getResourceAsStream("icons/publish.png"))));
        icons.put("quit", new ImageIcon(ImageIO.read(getClass().getResourceAsStream("icons/quit.png"))));
        icons.put("redo", new ImageIcon(ImageIO.read(getClass().getResourceAsStream("icons/redo.png"))));
        icons.put("repeat", new ImageIcon(ImageIO.read(getClass().getResourceAsStream("icons/repeat.png"))));
        icons.put("save", new ImageIcon(ImageIO.read(getClass().getResourceAsStream("icons/save.png"))));
        icons.put("step", new ImageIcon(ImageIO.read(getClass().getResourceAsStream("icons/step.png"))));
        icons.put("undo", new ImageIcon(ImageIO.read(getClass().getResourceAsStream("icons/undo.png"))));
        
        Controls = new JPanel();
        Quit = new JButton();
        Back = new JButton();
        Forward = new JButton();
        Step = new JButton();
        Novelty = new JButton();
        Pareto = new JButton();
        Optimize = new JButton();
        Save = new JButton();
        Publish = new JButton();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel19 = new JLabel();
        jLabel20 = new JLabel();
        jLabel21 = new JLabel();
        jLabel22 = new JLabel();
        jLabel23 = new JLabel();
        jLabel24 = new JLabel();
        jLabel25 = new JLabel();
        OptionTabs = new JTabbedPane();
        StepOptions = new JPanel();
        jLabel13 = new JLabel();
        stepMutationPower = new JSlider();
        jLabel14 = new JLabel();
        jLabel15 = new JLabel();
        stepMutationRate = new JSlider();
        jLabel16 = new JLabel();
        jLabel17 = new JLabel();
        jLabel18 = new JLabel();
        NoveltyOptions = new JPanel();
        showHideArchive = new JToggleButton();
        jLabel12 = new JLabel();
        jLabel1 = new JLabel();
        noveltyThreshold = new JSlider();
        jLabel2 = new JLabel();
        jLabel11 = new JLabel();
        
        ParetoOptions = new JPanel();
        toggleDynamicPOPV = new JToggleButton();
        
        OptimizeOptions = new JPanel();
        jLabel5 = new JLabel();
        optimizeEvaluationLimit = new JSlider();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        optimizeMutationRate = new JSlider();
        jLabel8 = new JLabel();
        jLabel9 = new JLabel();
        jLabel10 = new JLabel();
        optimizeOptionsPanel1 = new OptimizeOptionsPanel();
        Annimation = new JPanel();
        AnimationProgressBar = new JSlider();
        PlayPause = new JButton();
        Repeat = new JToggleButton();
        domainOptionsPanel1 = new DomainOptionsPanel();
        TabbedPopulation = new JTabbedPane();
        Population1 = new JPanel();
        Member000 = new EvaluationPanel();
        Member001 = new EvaluationPanel();
        Member2 = new EvaluationPanel();
        Member3 = new EvaluationPanel();
        Member4 = new EvaluationPanel();
        Member5 = new EvaluationPanel();
        Member6 = new EvaluationPanel();
        Member7 = new EvaluationPanel();
        Member8 = new EvaluationPanel();
        Member9 = new EvaluationPanel();
        Member010 = new EvaluationPanel();
        Member011 = new EvaluationPanel();
        Population2 = new JPanel();
        Member12 = new EvaluationPanel();
        Member13 = new EvaluationPanel();
        Member14 = new EvaluationPanel();
        Member15 = new EvaluationPanel();
        Member16 = new EvaluationPanel();
        Member17 = new EvaluationPanel();
        Member18 = new EvaluationPanel();
        Member19 = new EvaluationPanel();
        Member20 = new EvaluationPanel();
        Member21 = new EvaluationPanel();
        Member22 = new EvaluationPanel();
        Member23 = new EvaluationPanel();
        Population3 = new JPanel();
        Member24 = new EvaluationPanel();
        Member25 = new EvaluationPanel();
        Member26 = new EvaluationPanel();
        Member27 = new EvaluationPanel();
        Member28 = new EvaluationPanel();
        Member29 = new EvaluationPanel();
        Member30 = new EvaluationPanel();
        Member31 = new EvaluationPanel();
        Member32 = new EvaluationPanel();
        Member33 = new EvaluationPanel();
        Member34 = new EvaluationPanel();
        Member35 = new EvaluationPanel();
        Population4 = new JPanel();
        Member36 = new EvaluationPanel();
        Member37 = new EvaluationPanel();
        Member38 = new EvaluationPanel();
        Member39 = new EvaluationPanel();
        Member40 = new EvaluationPanel();
        Member41 = new EvaluationPanel();
        Member42 = new EvaluationPanel();
        Member43 = new EvaluationPanel();
        Member44 = new EvaluationPanel();
        Member45 = new EvaluationPanel();
        Member46 = new EvaluationPanel();
        Member47 = new EvaluationPanel();
        Population5 = new JPanel();
        Member48 = new EvaluationPanel();
        Member49 = new EvaluationPanel();
        Member50 = new EvaluationPanel();
        Member51 = new EvaluationPanel();
        Member52 = new EvaluationPanel();
        Member53 = new EvaluationPanel();
        Member54 = new EvaluationPanel();
        Member55 = new EvaluationPanel();
        Member56 = new EvaluationPanel();
        Member57 = new EvaluationPanel();
        Member58 = new EvaluationPanel();
        Member59 = new EvaluationPanel();
        Population6 = new JPanel();
        Member60 = new EvaluationPanel();
        Member61 = new EvaluationPanel();
        Member62 = new EvaluationPanel();
        Member63 = new EvaluationPanel();
        Member64 = new EvaluationPanel();
        Member65 = new EvaluationPanel();
        Member66 = new EvaluationPanel();
        Member67 = new EvaluationPanel();
        Member68 = new EvaluationPanel();
        Member69 = new EvaluationPanel();
        Member70 = new EvaluationPanel();
        Member71 = new EvaluationPanel();
        Population7 = new JPanel();
        Member72 = new EvaluationPanel();
        Member73 = new EvaluationPanel();
        Member74 = new EvaluationPanel();
        Member75 = new EvaluationPanel();
        Member76 = new EvaluationPanel();
        Member77 = new EvaluationPanel();
        Member78 = new EvaluationPanel();
        Member79 = new EvaluationPanel();
        Member80 = new EvaluationPanel();
        Member81 = new EvaluationPanel();
        Member82 = new EvaluationPanel();
        Member83 = new EvaluationPanel();
        Population8 = new JPanel();
        Member84 = new EvaluationPanel();
        Member85 = new EvaluationPanel();
        Member86 = new EvaluationPanel();
        Member87 = new EvaluationPanel();
        Member88 = new EvaluationPanel();
        Member89 = new EvaluationPanel();
        Member90 = new EvaluationPanel();
        Member91 = new EvaluationPanel();
        Member92 = new EvaluationPanel();
        Member93 = new EvaluationPanel();
        Member94 = new EvaluationPanel();
        Member95 = new EvaluationPanel();
        Population9 = new JPanel();
        Member96 = new EvaluationPanel();
        Member97 = new EvaluationPanel();
        Member98 = new EvaluationPanel();
        Member99 = new EvaluationPanel();
        Member100 = new EvaluationPanel();
        Member101 = new EvaluationPanel();
        Member102 = new EvaluationPanel();
        Member103 = new EvaluationPanel();
        Member104 = new EvaluationPanel();
        Member105 = new EvaluationPanel();
        Member106 = new EvaluationPanel();
        Member107 = new EvaluationPanel();
        Population10 = new JPanel();
        Member108 = new EvaluationPanel();
        Member109 = new EvaluationPanel();
        Member110 = new EvaluationPanel();
        Member111 = new EvaluationPanel();
        Member112 = new EvaluationPanel();
        Member113 = new EvaluationPanel();
        Member114 = new EvaluationPanel();
        Member115 = new EvaluationPanel();
        Member116 = new EvaluationPanel();
        Member117 = new EvaluationPanel();
        Member118 = new EvaluationPanel();
        Member119 = new EvaluationPanel();
        Population11 = new JPanel();
        Member120 = new EvaluationPanel();
        Member121 = new EvaluationPanel();
        Member122 = new EvaluationPanel();
        Member123 = new EvaluationPanel();
        Member124 = new EvaluationPanel();
        Member125 = new EvaluationPanel();
        Member126 = new EvaluationPanel();
        Member127 = new EvaluationPanel();
        Member128 = new EvaluationPanel();
        Member129 = new EvaluationPanel();
        Member130 = new EvaluationPanel();
        Member131 = new EvaluationPanel();
        Population12 = new JPanel();
        Member132 = new EvaluationPanel();
        Member133 = new EvaluationPanel();
        Member134 = new EvaluationPanel();
        Member135 = new EvaluationPanel();
        Member136 = new EvaluationPanel();
        Member137 = new EvaluationPanel();
        Member138 = new EvaluationPanel();
        Member139 = new EvaluationPanel();
        Member140 = new EvaluationPanel();
        Member141 = new EvaluationPanel();
        Member142 = new EvaluationPanel();
        Member143 = new EvaluationPanel();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Pareto Optimality-Assisted IEC Framework");
        setName("PopulationFrame");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        Controls.setBorder(BorderFactory.createTitledBorder("Evolution Controls"));
        Controls.setPreferredSize(new java.awt.Dimension(640, 125));
        Controls.setLayout(new java.awt.GridBagLayout());
        
        Quit.setIcon(icons.get("quit"));
        Quit.setToolTipText("Quit");
        Quit.setBorder(null);
        Quit.setBorderPainted(false);
        Quit.setHorizontalTextPosition(SwingConstants.CENTER);
        Quit.setPreferredSize(new java.awt.Dimension(75, 80));
        Quit.setRolloverEnabled(true);
        Quit.setVerticalTextPosition(SwingConstants.BOTTOM);
        Quit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                QuitMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 20;
        Controls.add(Quit, gridBagConstraints);

        Back.setIcon(icons.get("undo"));
        Back.setToolTipText("Back");
        Back.setBorder(null);
        Back.setHorizontalTextPosition(SwingConstants.CENTER);
        Back.setPreferredSize(new java.awt.Dimension(75, 80));
        Back.setRolloverEnabled(true);
        Back.setVerticalTextPosition(SwingConstants.BOTTOM);
        Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BackMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 10;
        Controls.add(Back, gridBagConstraints);

        Forward.setIcon(icons.get("redo"));
        Forward.setToolTipText("Forward");
        Forward.setBorder(null);
        Forward.setHorizontalTextPosition(SwingConstants.CENTER);
        Forward.setPreferredSize(new java.awt.Dimension(75, 80));
        Forward.setRolloverEnabled(true);
        Forward.setVerticalTextPosition(SwingConstants.BOTTOM);
        Forward.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ForwardMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 10;
        Controls.add(Forward, gridBagConstraints);

//        Step.setIcon(icons.get("step"));
//        Step.setToolTipText("Step");
//        Step.setBorder(null);
//        Step.setHorizontalTextPosition(SwingConstants.CENTER);
//        Step.setPreferredSize(new java.awt.Dimension(85, 80));
//        Step.setRolloverEnabled(true);
//        Step.setVerticalTextPosition(SwingConstants.BOTTOM);
//        Step.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                StepMouseClicked(evt);
//            }
//        });
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 3;
//        gridBagConstraints.gridy = 0;
//        gridBagConstraints.ipadx = 14;
//        gridBagConstraints.ipady = 20;
//        Controls.add(Step, gridBagConstraints);

//        Novelty.setIcon(icons.get("novelty"));
//        Novelty.setToolTipText("Novelty");
//        Novelty.setBorder(null);
//        Novelty.setHorizontalTextPosition(SwingConstants.CENTER);
//        Novelty.setPreferredSize(new java.awt.Dimension(115, 80));
//        Novelty.setRolloverEnabled(true);
//        Novelty.setVerticalTextPosition(SwingConstants.BOTTOM);
//        Novelty.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                NoveltyMouseClicked(evt);
//            }
//        });
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 4;
//        gridBagConstraints.gridy = 0;
//        gridBagConstraints.ipadx = 15;
//        gridBagConstraints.ipady = 20;
//        Controls.add(Novelty, gridBagConstraints);
        
        Pareto.setIcon(icons.get("novelty"));
        Pareto.setToolTipText("Pareto");
        Pareto.setBorder(null);
        Pareto.setHorizontalTextPosition(SwingConstants.CENTER);
        Pareto.setPreferredSize(new java.awt.Dimension(115, 80));
        Pareto.setRolloverEnabled(true);
        Pareto.setVerticalTextPosition(SwingConstants.BOTTOM);
        Pareto.addMouseListener(new java.awt.event.MouseAdapter() {
        	public void mouseClicked(java.awt.event.MouseEvent evt) {
        		ParetoMouseClicked(evt);
        	}
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 20;
        Controls.add(Pareto, gridBagConstraints);

        Optimize.setIcon(icons.get("fitness"));
        Optimize.setToolTipText("Optimize");
        Optimize.setBorder(null);
        Optimize.setHorizontalTextPosition(SwingConstants.CENTER);
        Optimize.setPreferredSize(new java.awt.Dimension(85, 80));
        Optimize.setRolloverEnabled(true);
        Optimize.setVerticalTextPosition(SwingConstants.BOTTOM);
        Optimize.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OptimizeMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 26;
        gridBagConstraints.ipady = 20;
        Controls.add(Optimize, gridBagConstraints);

        Save.setIcon(icons.get("save"));
        Save.setToolTipText("Save");
        Save.setBorder(null);
        Save.setHorizontalTextPosition(SwingConstants.CENTER);
        Save.setPreferredSize(new java.awt.Dimension(75, 80));
        Save.setRolloverEnabled(true);
        Save.setVerticalTextPosition(SwingConstants.BOTTOM);
        Save.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SaveMouseClicked(evt);
            }
        });
        Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 20;
        Controls.add(Save, gridBagConstraints);

        Publish.setIcon(icons.get("publish"));
        Publish.setToolTipText("Publish");
        Publish.setBorder(null);
        Publish.setHorizontalTextPosition(SwingConstants.CENTER);
        Publish.setPreferredSize(new java.awt.Dimension(95, 80));
        Publish.setRolloverEnabled(true);
        Publish.setVerticalTextPosition(SwingConstants.BOTTOM);
        Publish.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PublishMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 13;
        gridBagConstraints.ipady = 20;
        Controls.add(Publish, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        jLabel3.setText("QUIT");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        Controls.add(jLabel3, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        jLabel4.setText("BACK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        Controls.add(jLabel4, gridBagConstraints);

        jLabel19.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        jLabel19.setText("FORWARD");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        Controls.add(jLabel19, gridBagConstraints);

//        jLabel20.setFont(new java.awt.Font("Lucida Grande", 1, 14));
//        jLabel20.setText("STEP");
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridy = 1;
//        Controls.add(jLabel20, gridBagConstraints);

//        jLabel21.setFont(new java.awt.Font("Lucida Grande", 1, 14));
//        jLabel21.setText("NOVELTY");
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridy = 1;
//        Controls.add(jLabel21, gridBagConstraints);
        
        jLabel25.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        jLabel25.setText("PARETO");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        Controls.add(jLabel25, gridBagConstraints);
        
        jLabel22.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        jLabel22.setText("OPTIMIZE");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        Controls.add(jLabel22, gridBagConstraints);

        jLabel23.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        jLabel23.setText("SAVE");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        Controls.add(jLabel23, gridBagConstraints);

        jLabel24.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        jLabel24.setText("PUBLISH");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        Controls.add(jLabel24, gridBagConstraints);

        OptionTabs.setBorder(BorderFactory.createTitledBorder("Evolution Options"));
        OptionTabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        StepOptions.setLayout(new java.awt.GridBagLayout());

        jLabel13.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel13.setText("Low");
        StepOptions.add(jLabel13, new java.awt.GridBagConstraints());

        stepMutationPower.setMajorTickSpacing(25);
        stepMutationPower.setPaintTicks(true);
        stepMutationPower.setToolTipText("Novelty Threshold");
        stepMutationPower.setValue(25);
        stepMutationPower.setMinimumSize(new java.awt.Dimension(190, 38));
        stepMutationPower.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                stepMutationPowerStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        stepMutationPower.setEnabled(true);
        StepOptions.add(stepMutationPower, gridBagConstraints);

        jLabel14.setText("High");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 12);
        StepOptions.add(jLabel14, gridBagConstraints);

        jLabel15.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel15.setText("0%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        StepOptions.add(jLabel15, gridBagConstraints);

        stepMutationRate.setMajorTickSpacing(25);
        stepMutationRate.setPaintTicks(true);
        stepMutationRate.setToolTipText("Novelty Threshold");
        stepMutationRate.setValue(25);
        stepMutationRate.setMinimumSize(new java.awt.Dimension(190, 38));
        stepMutationRate.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                stepMutationRateStateChanged(evt);
            }
        });
        stepMutationRate.setEnabled(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        StepOptions.add(stepMutationRate, gridBagConstraints);

        jLabel16.setText("100%");
        StepOptions.add(jLabel16, new java.awt.GridBagConstraints());

        jLabel17.setText("Jiggle");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 12);
        StepOptions.add(jLabel17, gridBagConstraints);

        jLabel18.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel18.setText("Proportion");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        StepOptions.add(jLabel18, gridBagConstraints);

        OptionTabs.addTab("Step", StepOptions);

//        NoveltyOptions.setLayout(new java.awt.GridBagLayout());
//
//        showHideArchive.setText("Show/Hide");
//        showHideArchive.setToolTipText("Show/Hide Novelty Archive");
//        showHideArchive.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                showHideArchiveActionPerformed(evt);
//            }
//        });
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 12);
//        NoveltyOptions.add(showHideArchive, gridBagConstraints);
//
//        jLabel12.setHorizontalAlignment(SwingConstants.RIGHT);
//        jLabel12.setText("Novelty Archive");
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridy = 1;
//        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 12);
//        NoveltyOptions.add(jLabel12, gridBagConstraints);
//
//        jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
//        jLabel1.setText("Less Novel ");
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
//        NoveltyOptions.add(jLabel1, gridBagConstraints);
//
//        noveltyThreshold.setMajorTickSpacing(1);
//        noveltyThreshold.setMaximum(5);
//        noveltyThreshold.setMinimum(1);
//        noveltyThreshold.setPaintTicks(true);
//        noveltyThreshold.setToolTipText("Evaluation Threshold");
//        noveltyThreshold.setValue(2);
//        noveltyThreshold.setMinimumSize(new java.awt.Dimension(190, 38));
//        noveltyThreshold.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent evt) {
//                noveltyThresholdStateChanged(evt);
//            }
//        });
//        NoveltyOptions.add(noveltyThreshold, new java.awt.GridBagConstraints());
//
//        jLabel2.setText("More Novel");
//        NoveltyOptions.add(jLabel2, new java.awt.GridBagConstraints());
//
//        jLabel11.setText("Evaluation Threshold");
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 1;
//        gridBagConstraints.gridy = 1;
//        gridBagConstraints.gridwidth = 3;
//        NoveltyOptions.add(jLabel11, gridBagConstraints);
//
//        OptionTabs.addTab("Novelty", NoveltyOptions);

        ParetoOptions.setLayout(new java.awt.GridBagLayout());

        toggleDynamicPOPV.setText("Turn Dynamic POPV Off");
        toggleDynamicPOPV.setSelected(true);
        toggleDynamicPOPV.setToolTipText("Toggle Dynamic Pareto Optimal Pointing Vector");
        toggleDynamicPOPV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleDynamicPOPVActionPerformed(evt);
                if (toggleDynamicPOPV.isSelected())
                	toggleDynamicPOPV.setText("Turn Dynamic POPV Off");
                else
                	toggleDynamicPOPV.setText("Turn Dynamic POPV On");
            }
        });
        toggleDynamicPOPV.setEnabled(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 12);
        ParetoOptions.add(toggleDynamicPOPV, gridBagConstraints);

//        jLabel12.setHorizontalAlignment(SwingConstants.RIGHT);
//        jLabel12.setText("Novelty Archive");
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridy = 1;
//        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 12);
//        ParetoOptions.add(jLabel12, gridBagConstraints);
//
//        jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
//        jLabel1.setText("Less Novel ");
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
//        ParetoOptions.add(jLabel1, gridBagConstraints);
//
//        noveltyThreshold.setMajorTickSpacing(1);
//        noveltyThreshold.setMaximum(5);
//        noveltyThreshold.setMinimum(1);
//        noveltyThreshold.setPaintTicks(true);
//        noveltyThreshold.setToolTipText("Evaluation Threshold");
//        noveltyThreshold.setValue(2);
//        noveltyThreshold.setMinimumSize(new java.awt.Dimension(190, 38));
//        noveltyThreshold.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent evt) {
//                noveltyThresholdStateChanged(evt);
//            }
//        });
//        ParetoOptions.add(noveltyThreshold, new java.awt.GridBagConstraints());
//
//        jLabel2.setText("More Novel");
//        ParetoOptions.add(jLabel2, new java.awt.GridBagConstraints());
//
//        jLabel11.setText("Evaluation Threshold");
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 1;
//        gridBagConstraints.gridy = 1;
//        gridBagConstraints.gridwidth = 3;
//        ParetoOptions.add(jLabel11, gridBagConstraints);

        OptionTabs.addTab("Pareto", ParetoOptions);

        OptimizeOptions.setLayout(new java.awt.GridBagLayout());

        jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel5.setText("250");
        OptimizeOptions.add(jLabel5, new java.awt.GridBagConstraints());

        optimizeEvaluationLimit.setMajorTickSpacing(250);
        optimizeEvaluationLimit.setMaximum(2500);
        optimizeEvaluationLimit.setMinimum(250);
        optimizeEvaluationLimit.setPaintTicks(true);
        optimizeEvaluationLimit.setToolTipText("Novelty Threshold");
        optimizeEvaluationLimit.setValue(500);
        optimizeEvaluationLimit.setMinimumSize(new java.awt.Dimension(190, 38));
        optimizeEvaluationLimit.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                optimizeEvaluationLimitStateChanged(evt);
            }
        });
        optimizeEvaluationLimit.setEnabled(true);
        OptimizeOptions.add(optimizeEvaluationLimit, new java.awt.GridBagConstraints());

        jLabel6.setText("2,500");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 12);
        OptimizeOptions.add(jLabel6, gridBagConstraints);

        jLabel7.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel7.setText("Low");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        OptimizeOptions.add(jLabel7, gridBagConstraints);

        optimizeMutationRate.setMajorTickSpacing(25);
        optimizeMutationRate.setPaintTicks(true);
        optimizeMutationRate.setToolTipText("Novelty Threshold");
        optimizeMutationRate.setValue(25);
        optimizeMutationRate.setMinimumSize(new java.awt.Dimension(190, 38));
        optimizeMutationRate.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                optimizeMutationRateStateChanged(evt);
            }
        });
        optimizeMutationRate.setEnabled(true);
        OptimizeOptions.add(optimizeMutationRate, new java.awt.GridBagConstraints());

        jLabel8.setText("High");
        OptimizeOptions.add(jLabel8, new java.awt.GridBagConstraints());

        jLabel9.setText("Evalution Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        OptimizeOptions.add(jLabel9, gridBagConstraints);

        jLabel10.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel10.setText("Mutation Rate/Power");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        OptimizeOptions.add(jLabel10, gridBagConstraints);

        OptionTabs.addTab("Optimize", OptimizeOptions);
        
        OptionTabs.addTab("Optimize Functions", optimizeOptionsPanel1);

        AnimationProgressBar.setMajorTickSpacing(166);
        AnimationProgressBar.setMaximum(1000);
        AnimationProgressBar.setPaintTicks(true);
        AnimationProgressBar.setValue(0);
        AnimationProgressBar.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                AnimationProgressBarStateChanged(evt);
            }
        });

        PlayPause.setIcon(icons.get("play"));
        PlayPause.setToolTipText("Play");
        PlayPause.setBorderPainted(false);
        PlayPause.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PlayPauseMouseClicked(evt);
            }
        });

        Repeat.setIcon(icons.get("repeat"));
        Repeat.setSelected(true);
        Repeat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RepeatMouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout AnnimationLayout = new org.jdesktop.layout.GroupLayout(Annimation);
        Annimation.setLayout(AnnimationLayout);
        AnnimationLayout.setHorizontalGroup(
            AnnimationLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(AnnimationLayout.createSequentialGroup()
                .addContainerGap()
                .add(PlayPause)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(AnimationProgressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 636, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(Repeat)
                .addContainerGap())
        );
        AnnimationLayout.setVerticalGroup(
            AnnimationLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(AnnimationLayout.createSequentialGroup()
                .addContainerGap()
                .add(AnnimationLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(AnimationProgressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(PlayPause)
                    .add(Repeat))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        OptionTabs.addTab("Player", Annimation);

//        ShowBehavior.setSelected(true);
//        ShowBehavior.setText("Show Behavior Characterization");
//        ShowBehavior.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                ShowBehaviorActionPerformed(evt);
//            }
//        });
//
//        ShowTrajectory.setSelected(true);
//        ShowTrajectory.setText("Show Arm Tip Trajectory ");
//        ShowTrajectory.setToolTipText("Arm Tip Trajectory");
//        ShowTrajectory.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                ShowTrajectoryActionPerformed(evt);
//            }
//        });
//
//        org.jdesktop.layout.GroupLayout domainOptionsPanel1Layout = new org.jdesktop.layout.GroupLayout(domainOptionsPanel1);
//        domainOptionsPanel1.setLayout(domainOptionsPanel1Layout);
//        domainOptionsPanel1Layout.setHorizontalGroup(
//            domainOptionsPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
//            .add(domainOptionsPanel1Layout.createSequentialGroup()
//                .addContainerGap()
//                .add(domainOptionsPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
//                    .add(ShowBehavior)
//                    .add(ShowTrajectory))
//                .addContainerGap(531, Short.MAX_VALUE))
//        );
//        domainOptionsPanel1Layout.setVerticalGroup(
//            domainOptionsPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
//            .add(domainOptionsPanel1Layout.createSequentialGroup()
//                .add(ShowBehavior)
//                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                .add(ShowTrajectory)
//                .addContainerGap())
//        );

        OptionTabs.addTab("Domain", domainOptionsPanel1);

        OptionTabs.setSelectedIndex(4);

        TabbedPopulation.setBorder(BorderFactory.createTitledBorder(populationBoarderTitle()));

        Population1.setLayout(new java.awt.GridLayout(3, 4, 2, 2));

        Member000.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member0MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member0Layout = new org.jdesktop.layout.GroupLayout(Member000);
        Member000.setLayout(Member0Layout);
        Member0Layout.setHorizontalGroup(
            Member0Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member0Layout.setVerticalGroup(
            Member0Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population1.add(Member000);

        Member001.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member1MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member1Layout = new org.jdesktop.layout.GroupLayout(Member001);
        Member001.setLayout(Member1Layout);
        Member1Layout.setHorizontalGroup(
            Member1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member1Layout.setVerticalGroup(
            Member1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population1.add(Member001);

        Member2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member2MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member2Layout = new org.jdesktop.layout.GroupLayout(Member2);
        Member2.setLayout(Member2Layout);
        Member2Layout.setHorizontalGroup(
            Member2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member2Layout.setVerticalGroup(
            Member2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population1.add(Member2);

        Member3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member3MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member3Layout = new org.jdesktop.layout.GroupLayout(Member3);
        Member3.setLayout(Member3Layout);
        Member3Layout.setHorizontalGroup(
            Member3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member3Layout.setVerticalGroup(
            Member3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population1.add(Member3);

        Member4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member4MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member4Layout = new org.jdesktop.layout.GroupLayout(Member4);
        Member4.setLayout(Member4Layout);
        Member4Layout.setHorizontalGroup(
            Member4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member4Layout.setVerticalGroup(
            Member4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population1.add(Member4);

        Member5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member5MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member5Layout = new org.jdesktop.layout.GroupLayout(Member5);
        Member5.setLayout(Member5Layout);
        Member5Layout.setHorizontalGroup(
            Member5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member5Layout.setVerticalGroup(
            Member5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population1.add(Member5);

        Member6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member6MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member6Layout = new org.jdesktop.layout.GroupLayout(Member6);
        Member6.setLayout(Member6Layout);
        Member6Layout.setHorizontalGroup(
            Member6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member6Layout.setVerticalGroup(
            Member6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population1.add(Member6);

        Member7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member7MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member7Layout = new org.jdesktop.layout.GroupLayout(Member7);
        Member7.setLayout(Member7Layout);
        Member7Layout.setHorizontalGroup(
            Member7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member7Layout.setVerticalGroup(
            Member7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population1.add(Member7);

        Member8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member8MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member8Layout = new org.jdesktop.layout.GroupLayout(Member8);
        Member8.setLayout(Member8Layout);
        Member8Layout.setHorizontalGroup(
            Member8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member8Layout.setVerticalGroup(
            Member8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population1.add(Member8);

        Member9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member9MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member9Layout = new org.jdesktop.layout.GroupLayout(Member9);
        Member9.setLayout(Member9Layout);
        Member9Layout.setHorizontalGroup(
            Member9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member9Layout.setVerticalGroup(
            Member9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population1.add(Member9);

        Member010.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member10MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member10Layout = new org.jdesktop.layout.GroupLayout(Member010);
        Member010.setLayout(Member10Layout);
        Member10Layout.setHorizontalGroup(
            Member10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member10Layout.setVerticalGroup(
            Member10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population1.add(Member010);

        Member011.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member11MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member11Layout = new org.jdesktop.layout.GroupLayout(Member011);
        Member011.setLayout(Member11Layout);
        Member11Layout.setHorizontalGroup(
            Member11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member11Layout.setVerticalGroup(
            Member11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population1.add(Member011);

        TabbedPopulation.addTab("Candidates 1-12", Population1);

        Population2.setLayout(new java.awt.GridLayout(3, 4, 2, 2));

        Member12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member12MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member12Layout = new org.jdesktop.layout.GroupLayout(Member12);
        Member12.setLayout(Member12Layout);
        Member12Layout.setHorizontalGroup(
            Member12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member12Layout.setVerticalGroup(
            Member12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population2.add(Member12);

        Member13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member13MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member13Layout = new org.jdesktop.layout.GroupLayout(Member13);
        Member13.setLayout(Member13Layout);
        Member13Layout.setHorizontalGroup(
            Member13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member13Layout.setVerticalGroup(
            Member13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population2.add(Member13);

        Member14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member14MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member14Layout = new org.jdesktop.layout.GroupLayout(Member14);
        Member14.setLayout(Member14Layout);
        Member14Layout.setHorizontalGroup(
            Member14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member14Layout.setVerticalGroup(
            Member14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population2.add(Member14);

        Member15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member15MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member15Layout = new org.jdesktop.layout.GroupLayout(Member15);
        Member15.setLayout(Member15Layout);
        Member15Layout.setHorizontalGroup(
            Member15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member15Layout.setVerticalGroup(
            Member15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population2.add(Member15);

        Member16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member16MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member16Layout = new org.jdesktop.layout.GroupLayout(Member16);
        Member16.setLayout(Member16Layout);
        Member16Layout.setHorizontalGroup(
            Member16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member16Layout.setVerticalGroup(
            Member16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population2.add(Member16);

        Member17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member17MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member17Layout = new org.jdesktop.layout.GroupLayout(Member17);
        Member17.setLayout(Member17Layout);
        Member17Layout.setHorizontalGroup(
            Member17Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member17Layout.setVerticalGroup(
            Member17Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population2.add(Member17);

        Member18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member18MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member18Layout = new org.jdesktop.layout.GroupLayout(Member18);
        Member18.setLayout(Member18Layout);
        Member18Layout.setHorizontalGroup(
            Member18Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member18Layout.setVerticalGroup(
            Member18Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population2.add(Member18);

        Member19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member19MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member19Layout = new org.jdesktop.layout.GroupLayout(Member19);
        Member19.setLayout(Member19Layout);
        Member19Layout.setHorizontalGroup(
            Member19Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member19Layout.setVerticalGroup(
            Member19Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population2.add(Member19);

        Member20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member20MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member20Layout = new org.jdesktop.layout.GroupLayout(Member20);
        Member20.setLayout(Member20Layout);
        Member20Layout.setHorizontalGroup(
            Member20Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member20Layout.setVerticalGroup(
            Member20Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population2.add(Member20);

        Member21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member21MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member21Layout = new org.jdesktop.layout.GroupLayout(Member21);
        Member21.setLayout(Member21Layout);
        Member21Layout.setHorizontalGroup(
            Member21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member21Layout.setVerticalGroup(
            Member21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population2.add(Member21);

        Member22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member22MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member22Layout = new org.jdesktop.layout.GroupLayout(Member22);
        Member22.setLayout(Member22Layout);
        Member22Layout.setHorizontalGroup(
            Member22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member22Layout.setVerticalGroup(
            Member22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population2.add(Member22);

        Member23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member23MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member23Layout = new org.jdesktop.layout.GroupLayout(Member23);
        Member23.setLayout(Member23Layout);
        Member23Layout.setHorizontalGroup(
            Member23Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member23Layout.setVerticalGroup(
            Member23Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population2.add(Member23);

        TabbedPopulation.addTab("-24", Population2);

        Population3.setLayout(new java.awt.GridLayout(3, 4, 2, 2));

        Member24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member24MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member24Layout = new org.jdesktop.layout.GroupLayout(Member24);
        Member24.setLayout(Member24Layout);
        Member24Layout.setHorizontalGroup(
            Member24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member24Layout.setVerticalGroup(
            Member24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population3.add(Member24);

        Member25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member25MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member25Layout = new org.jdesktop.layout.GroupLayout(Member25);
        Member25.setLayout(Member25Layout);
        Member25Layout.setHorizontalGroup(
            Member25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member25Layout.setVerticalGroup(
            Member25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population3.add(Member25);

        Member26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member26MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member26Layout = new org.jdesktop.layout.GroupLayout(Member26);
        Member26.setLayout(Member26Layout);
        Member26Layout.setHorizontalGroup(
            Member26Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member26Layout.setVerticalGroup(
            Member26Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population3.add(Member26);

        Member27.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member27MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member27Layout = new org.jdesktop.layout.GroupLayout(Member27);
        Member27.setLayout(Member27Layout);
        Member27Layout.setHorizontalGroup(
            Member27Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member27Layout.setVerticalGroup(
            Member27Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population3.add(Member27);

        Member28.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member28MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member28Layout = new org.jdesktop.layout.GroupLayout(Member28);
        Member28.setLayout(Member28Layout);
        Member28Layout.setHorizontalGroup(
            Member28Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member28Layout.setVerticalGroup(
            Member28Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population3.add(Member28);

        Member29.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member29MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member29Layout = new org.jdesktop.layout.GroupLayout(Member29);
        Member29.setLayout(Member29Layout);
        Member29Layout.setHorizontalGroup(
            Member29Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member29Layout.setVerticalGroup(
            Member29Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population3.add(Member29);

        Member30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member30MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member30Layout = new org.jdesktop.layout.GroupLayout(Member30);
        Member30.setLayout(Member30Layout);
        Member30Layout.setHorizontalGroup(
            Member30Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member30Layout.setVerticalGroup(
            Member30Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population3.add(Member30);

        Member31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member31MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member31Layout = new org.jdesktop.layout.GroupLayout(Member31);
        Member31.setLayout(Member31Layout);
        Member31Layout.setHorizontalGroup(
            Member31Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member31Layout.setVerticalGroup(
            Member31Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population3.add(Member31);

        Member32.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member32MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member32Layout = new org.jdesktop.layout.GroupLayout(Member32);
        Member32.setLayout(Member32Layout);
        Member32Layout.setHorizontalGroup(
            Member32Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member32Layout.setVerticalGroup(
            Member32Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population3.add(Member32);

        Member33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member33MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member33Layout = new org.jdesktop.layout.GroupLayout(Member33);
        Member33.setLayout(Member33Layout);
        Member33Layout.setHorizontalGroup(
            Member33Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member33Layout.setVerticalGroup(
            Member33Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population3.add(Member33);

        Member34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member34MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member34Layout = new org.jdesktop.layout.GroupLayout(Member34);
        Member34.setLayout(Member34Layout);
        Member34Layout.setHorizontalGroup(
            Member34Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member34Layout.setVerticalGroup(
            Member34Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population3.add(Member34);

        Member35.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member35MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member35Layout = new org.jdesktop.layout.GroupLayout(Member35);
        Member35.setLayout(Member35Layout);
        Member35Layout.setHorizontalGroup(
            Member35Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member35Layout.setVerticalGroup(
            Member35Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population3.add(Member35);

        TabbedPopulation.addTab("-36", Population3);

        Population4.setLayout(new java.awt.GridLayout(3, 4, 2, 2));

        Member36.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member36MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member36Layout = new org.jdesktop.layout.GroupLayout(Member36);
        Member36.setLayout(Member36Layout);
        Member36Layout.setHorizontalGroup(
            Member36Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member36Layout.setVerticalGroup(
            Member36Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population4.add(Member36);

        Member37.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member37MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member37Layout = new org.jdesktop.layout.GroupLayout(Member37);
        Member37.setLayout(Member37Layout);
        Member37Layout.setHorizontalGroup(
            Member37Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member37Layout.setVerticalGroup(
            Member37Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population4.add(Member37);

        Member38.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member38MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member38Layout = new org.jdesktop.layout.GroupLayout(Member38);
        Member38.setLayout(Member38Layout);
        Member38Layout.setHorizontalGroup(
            Member38Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member38Layout.setVerticalGroup(
            Member38Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population4.add(Member38);

        Member39.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member39MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member39Layout = new org.jdesktop.layout.GroupLayout(Member39);
        Member39.setLayout(Member39Layout);
        Member39Layout.setHorizontalGroup(
            Member39Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member39Layout.setVerticalGroup(
            Member39Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population4.add(Member39);

        Member40.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member40MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member40Layout = new org.jdesktop.layout.GroupLayout(Member40);
        Member40.setLayout(Member40Layout);
        Member40Layout.setHorizontalGroup(
            Member40Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member40Layout.setVerticalGroup(
            Member40Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population4.add(Member40);

        Member41.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member41MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member41Layout = new org.jdesktop.layout.GroupLayout(Member41);
        Member41.setLayout(Member41Layout);
        Member41Layout.setHorizontalGroup(
            Member41Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member41Layout.setVerticalGroup(
            Member41Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population4.add(Member41);

        Member42.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member42MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member42Layout = new org.jdesktop.layout.GroupLayout(Member42);
        Member42.setLayout(Member42Layout);
        Member42Layout.setHorizontalGroup(
            Member42Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member42Layout.setVerticalGroup(
            Member42Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population4.add(Member42);

        Member43.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member43MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member43Layout = new org.jdesktop.layout.GroupLayout(Member43);
        Member43.setLayout(Member43Layout);
        Member43Layout.setHorizontalGroup(
            Member43Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member43Layout.setVerticalGroup(
            Member43Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population4.add(Member43);

        Member44.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member44MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member44Layout = new org.jdesktop.layout.GroupLayout(Member44);
        Member44.setLayout(Member44Layout);
        Member44Layout.setHorizontalGroup(
            Member44Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member44Layout.setVerticalGroup(
            Member44Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population4.add(Member44);

        Member45.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member45MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member45Layout = new org.jdesktop.layout.GroupLayout(Member45);
        Member45.setLayout(Member45Layout);
        Member45Layout.setHorizontalGroup(
            Member45Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member45Layout.setVerticalGroup(
            Member45Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population4.add(Member45);

        Member46.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member46MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member46Layout = new org.jdesktop.layout.GroupLayout(Member46);
        Member46.setLayout(Member46Layout);
        Member46Layout.setHorizontalGroup(
            Member46Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member46Layout.setVerticalGroup(
            Member46Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population4.add(Member46);

        Member47.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member47MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member47Layout = new org.jdesktop.layout.GroupLayout(Member47);
        Member47.setLayout(Member47Layout);
        Member47Layout.setHorizontalGroup(
            Member47Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member47Layout.setVerticalGroup(
            Member47Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population4.add(Member47);

        TabbedPopulation.addTab("-48", Population4);

        Population5.setLayout(new java.awt.GridLayout(3, 4, 2, 2));

        Member48.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member48MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member48Layout = new org.jdesktop.layout.GroupLayout(Member48);
        Member48.setLayout(Member48Layout);
        Member48Layout.setHorizontalGroup(
            Member48Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member48Layout.setVerticalGroup(
            Member48Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population5.add(Member48);

        Member49.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member49MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member49Layout = new org.jdesktop.layout.GroupLayout(Member49);
        Member49.setLayout(Member49Layout);
        Member49Layout.setHorizontalGroup(
            Member49Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member49Layout.setVerticalGroup(
            Member49Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population5.add(Member49);

        Member50.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member50MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member50Layout = new org.jdesktop.layout.GroupLayout(Member50);
        Member50.setLayout(Member50Layout);
        Member50Layout.setHorizontalGroup(
            Member50Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member50Layout.setVerticalGroup(
            Member50Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population5.add(Member50);

        Member51.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member51MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member51Layout = new org.jdesktop.layout.GroupLayout(Member51);
        Member51.setLayout(Member51Layout);
        Member51Layout.setHorizontalGroup(
            Member51Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member51Layout.setVerticalGroup(
            Member51Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population5.add(Member51);

        Member52.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member52MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member52Layout = new org.jdesktop.layout.GroupLayout(Member52);
        Member52.setLayout(Member52Layout);
        Member52Layout.setHorizontalGroup(
            Member52Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member52Layout.setVerticalGroup(
            Member52Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population5.add(Member52);

        Member53.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member53MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member53Layout = new org.jdesktop.layout.GroupLayout(Member53);
        Member53.setLayout(Member53Layout);
        Member53Layout.setHorizontalGroup(
            Member53Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member53Layout.setVerticalGroup(
            Member53Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population5.add(Member53);

        Member54.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member54MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member54Layout = new org.jdesktop.layout.GroupLayout(Member54);
        Member54.setLayout(Member54Layout);
        Member54Layout.setHorizontalGroup(
            Member54Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member54Layout.setVerticalGroup(
            Member54Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population5.add(Member54);

        Member55.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member55MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member55Layout = new org.jdesktop.layout.GroupLayout(Member55);
        Member55.setLayout(Member55Layout);
        Member55Layout.setHorizontalGroup(
            Member55Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member55Layout.setVerticalGroup(
            Member55Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population5.add(Member55);

        Member56.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member56MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member56Layout = new org.jdesktop.layout.GroupLayout(Member56);
        Member56.setLayout(Member56Layout);
        Member56Layout.setHorizontalGroup(
            Member56Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member56Layout.setVerticalGroup(
            Member56Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population5.add(Member56);

        Member57.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member57MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member57Layout = new org.jdesktop.layout.GroupLayout(Member57);
        Member57.setLayout(Member57Layout);
        Member57Layout.setHorizontalGroup(
            Member57Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member57Layout.setVerticalGroup(
            Member57Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population5.add(Member57);

        Member58.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member58MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member58Layout = new org.jdesktop.layout.GroupLayout(Member58);
        Member58.setLayout(Member58Layout);
        Member58Layout.setHorizontalGroup(
            Member58Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member58Layout.setVerticalGroup(
            Member58Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population5.add(Member58);

        Member59.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member59MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member59Layout = new org.jdesktop.layout.GroupLayout(Member59);
        Member59.setLayout(Member59Layout);
        Member59Layout.setHorizontalGroup(
            Member59Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member59Layout.setVerticalGroup(
            Member59Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population5.add(Member59);

        TabbedPopulation.addTab("-60", Population5);

        Population6.setLayout(new java.awt.GridLayout(3, 4, 2, 2));

        Member60.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member60MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member60Layout = new org.jdesktop.layout.GroupLayout(Member60);
        Member60.setLayout(Member60Layout);
        Member60Layout.setHorizontalGroup(
            Member60Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member60Layout.setVerticalGroup(
            Member60Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population6.add(Member60);

        Member61.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member61MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member61Layout = new org.jdesktop.layout.GroupLayout(Member61);
        Member61.setLayout(Member61Layout);
        Member61Layout.setHorizontalGroup(
            Member61Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member61Layout.setVerticalGroup(
            Member61Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population6.add(Member61);

        Member62.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member62MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member62Layout = new org.jdesktop.layout.GroupLayout(Member62);
        Member62.setLayout(Member62Layout);
        Member62Layout.setHorizontalGroup(
            Member62Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member62Layout.setVerticalGroup(
            Member62Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population6.add(Member62);

        Member63.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member63MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member63Layout = new org.jdesktop.layout.GroupLayout(Member63);
        Member63.setLayout(Member63Layout);
        Member63Layout.setHorizontalGroup(
            Member63Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member63Layout.setVerticalGroup(
            Member63Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population6.add(Member63);

        Member64.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member64MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member64Layout = new org.jdesktop.layout.GroupLayout(Member64);
        Member64.setLayout(Member64Layout);
        Member64Layout.setHorizontalGroup(
            Member64Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member64Layout.setVerticalGroup(
            Member64Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population6.add(Member64);

        Member65.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member65MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member65Layout = new org.jdesktop.layout.GroupLayout(Member65);
        Member65.setLayout(Member65Layout);
        Member65Layout.setHorizontalGroup(
            Member65Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member65Layout.setVerticalGroup(
            Member65Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population6.add(Member65);

        Member66.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member66MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member66Layout = new org.jdesktop.layout.GroupLayout(Member66);
        Member66.setLayout(Member66Layout);
        Member66Layout.setHorizontalGroup(
            Member66Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member66Layout.setVerticalGroup(
            Member66Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population6.add(Member66);

        Member67.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member67MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member67Layout = new org.jdesktop.layout.GroupLayout(Member67);
        Member67.setLayout(Member67Layout);
        Member67Layout.setHorizontalGroup(
            Member67Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member67Layout.setVerticalGroup(
            Member67Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population6.add(Member67);

        Member68.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member68MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member68Layout = new org.jdesktop.layout.GroupLayout(Member68);
        Member68.setLayout(Member68Layout);
        Member68Layout.setHorizontalGroup(
            Member68Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member68Layout.setVerticalGroup(
            Member68Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population6.add(Member68);

        Member69.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member69MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member69Layout = new org.jdesktop.layout.GroupLayout(Member69);
        Member69.setLayout(Member69Layout);
        Member69Layout.setHorizontalGroup(
            Member69Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member69Layout.setVerticalGroup(
            Member69Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population6.add(Member69);

        Member70.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member70MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member70Layout = new org.jdesktop.layout.GroupLayout(Member70);
        Member70.setLayout(Member70Layout);
        Member70Layout.setHorizontalGroup(
            Member70Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member70Layout.setVerticalGroup(
            Member70Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population6.add(Member70);

        Member71.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member71MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member71Layout = new org.jdesktop.layout.GroupLayout(Member71);
        Member71.setLayout(Member71Layout);
        Member71Layout.setHorizontalGroup(
            Member71Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member71Layout.setVerticalGroup(
            Member71Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population6.add(Member71);

        TabbedPopulation.addTab("-72", Population6);

        Population7.setLayout(new java.awt.GridLayout(3, 4, 2, 2));

        Member72.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member72MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member72Layout = new org.jdesktop.layout.GroupLayout(Member72);
        Member72.setLayout(Member72Layout);
        Member72Layout.setHorizontalGroup(
            Member72Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member72Layout.setVerticalGroup(
            Member72Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population7.add(Member72);

        Member73.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member73MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member73Layout = new org.jdesktop.layout.GroupLayout(Member73);
        Member73.setLayout(Member73Layout);
        Member73Layout.setHorizontalGroup(
            Member73Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member73Layout.setVerticalGroup(
            Member73Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population7.add(Member73);

        Member74.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member74MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member74Layout = new org.jdesktop.layout.GroupLayout(Member74);
        Member74.setLayout(Member74Layout);
        Member74Layout.setHorizontalGroup(
            Member74Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member74Layout.setVerticalGroup(
            Member74Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population7.add(Member74);

        Member75.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member75MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member75Layout = new org.jdesktop.layout.GroupLayout(Member75);
        Member75.setLayout(Member75Layout);
        Member75Layout.setHorizontalGroup(
            Member75Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member75Layout.setVerticalGroup(
            Member75Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population7.add(Member75);

        Member76.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member76MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member76Layout = new org.jdesktop.layout.GroupLayout(Member76);
        Member76.setLayout(Member76Layout);
        Member76Layout.setHorizontalGroup(
            Member76Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member76Layout.setVerticalGroup(
            Member76Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population7.add(Member76);

        Member77.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member77MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member77Layout = new org.jdesktop.layout.GroupLayout(Member77);
        Member77.setLayout(Member77Layout);
        Member77Layout.setHorizontalGroup(
            Member77Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member77Layout.setVerticalGroup(
            Member77Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population7.add(Member77);

        Member78.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member78MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member78Layout = new org.jdesktop.layout.GroupLayout(Member78);
        Member78.setLayout(Member78Layout);
        Member78Layout.setHorizontalGroup(
            Member78Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member78Layout.setVerticalGroup(
            Member78Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population7.add(Member78);

        Member79.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member79MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member79Layout = new org.jdesktop.layout.GroupLayout(Member79);
        Member79.setLayout(Member79Layout);
        Member79Layout.setHorizontalGroup(
            Member79Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member79Layout.setVerticalGroup(
            Member79Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population7.add(Member79);

        Member80.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member80MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member80Layout = new org.jdesktop.layout.GroupLayout(Member80);
        Member80.setLayout(Member80Layout);
        Member80Layout.setHorizontalGroup(
            Member80Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member80Layout.setVerticalGroup(
            Member80Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population7.add(Member80);

        Member81.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member81MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member81Layout = new org.jdesktop.layout.GroupLayout(Member81);
        Member81.setLayout(Member81Layout);
        Member81Layout.setHorizontalGroup(
            Member81Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member81Layout.setVerticalGroup(
            Member81Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population7.add(Member81);

        Member82.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member82MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member82Layout = new org.jdesktop.layout.GroupLayout(Member82);
        Member82.setLayout(Member82Layout);
        Member82Layout.setHorizontalGroup(
            Member82Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member82Layout.setVerticalGroup(
            Member82Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population7.add(Member82);

        Member83.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member83MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member83Layout = new org.jdesktop.layout.GroupLayout(Member83);
        Member83.setLayout(Member83Layout);
        Member83Layout.setHorizontalGroup(
            Member83Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member83Layout.setVerticalGroup(
            Member83Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population7.add(Member83);

        TabbedPopulation.addTab("-84", Population7);

        Population8.setLayout(new java.awt.GridLayout(3, 4, 2, 2));

        Member84.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member84MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member84Layout = new org.jdesktop.layout.GroupLayout(Member84);
        Member84.setLayout(Member84Layout);
        Member84Layout.setHorizontalGroup(
            Member84Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member84Layout.setVerticalGroup(
            Member84Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population8.add(Member84);

        Member85.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member85MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member85Layout = new org.jdesktop.layout.GroupLayout(Member85);
        Member85.setLayout(Member85Layout);
        Member85Layout.setHorizontalGroup(
            Member85Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member85Layout.setVerticalGroup(
            Member85Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population8.add(Member85);

        Member86.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member86MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member86Layout = new org.jdesktop.layout.GroupLayout(Member86);
        Member86.setLayout(Member86Layout);
        Member86Layout.setHorizontalGroup(
            Member86Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member86Layout.setVerticalGroup(
            Member86Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population8.add(Member86);

        Member87.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member87MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member87Layout = new org.jdesktop.layout.GroupLayout(Member87);
        Member87.setLayout(Member87Layout);
        Member87Layout.setHorizontalGroup(
            Member87Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member87Layout.setVerticalGroup(
            Member87Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population8.add(Member87);

        Member88.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member88MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member88Layout = new org.jdesktop.layout.GroupLayout(Member88);
        Member88.setLayout(Member88Layout);
        Member88Layout.setHorizontalGroup(
            Member88Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member88Layout.setVerticalGroup(
            Member88Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population8.add(Member88);

        Member89.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member89MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member89Layout = new org.jdesktop.layout.GroupLayout(Member89);
        Member89.setLayout(Member89Layout);
        Member89Layout.setHorizontalGroup(
            Member89Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member89Layout.setVerticalGroup(
            Member89Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population8.add(Member89);

        Member90.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member90MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member90Layout = new org.jdesktop.layout.GroupLayout(Member90);
        Member90.setLayout(Member90Layout);
        Member90Layout.setHorizontalGroup(
            Member90Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member90Layout.setVerticalGroup(
            Member90Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population8.add(Member90);

        Member91.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member91MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member91Layout = new org.jdesktop.layout.GroupLayout(Member91);
        Member91.setLayout(Member91Layout);
        Member91Layout.setHorizontalGroup(
            Member91Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member91Layout.setVerticalGroup(
            Member91Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population8.add(Member91);

        Member92.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member92MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member92Layout = new org.jdesktop.layout.GroupLayout(Member92);
        Member92.setLayout(Member92Layout);
        Member92Layout.setHorizontalGroup(
            Member92Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member92Layout.setVerticalGroup(
            Member92Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population8.add(Member92);

        Member93.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member93MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member93Layout = new org.jdesktop.layout.GroupLayout(Member93);
        Member93.setLayout(Member93Layout);
        Member93Layout.setHorizontalGroup(
            Member93Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member93Layout.setVerticalGroup(
            Member93Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population8.add(Member93);

        Member94.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member94MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member94Layout = new org.jdesktop.layout.GroupLayout(Member94);
        Member94.setLayout(Member94Layout);
        Member94Layout.setHorizontalGroup(
            Member94Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member94Layout.setVerticalGroup(
            Member94Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population8.add(Member94);

        Member95.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member95MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member95Layout = new org.jdesktop.layout.GroupLayout(Member95);
        Member95.setLayout(Member95Layout);
        Member95Layout.setHorizontalGroup(
            Member95Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member95Layout.setVerticalGroup(
            Member95Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population8.add(Member95);

        TabbedPopulation.addTab("-96", Population8);

        Population9.setLayout(new java.awt.GridLayout(3, 4, 2, 2));

        Member96.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member96MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member96Layout = new org.jdesktop.layout.GroupLayout(Member96);
        Member96.setLayout(Member96Layout);
        Member96Layout.setHorizontalGroup(
            Member96Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member96Layout.setVerticalGroup(
            Member96Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population9.add(Member96);

        Member97.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member97MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member97Layout = new org.jdesktop.layout.GroupLayout(Member97);
        Member97.setLayout(Member97Layout);
        Member97Layout.setHorizontalGroup(
            Member97Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member97Layout.setVerticalGroup(
            Member97Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population9.add(Member97);

        Member98.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member98MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member98Layout = new org.jdesktop.layout.GroupLayout(Member98);
        Member98.setLayout(Member98Layout);
        Member98Layout.setHorizontalGroup(
            Member98Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member98Layout.setVerticalGroup(
            Member98Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population9.add(Member98);

        Member99.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member99MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member99Layout = new org.jdesktop.layout.GroupLayout(Member99);
        Member99.setLayout(Member99Layout);
        Member99Layout.setHorizontalGroup(
            Member99Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member99Layout.setVerticalGroup(
            Member99Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population9.add(Member99);

        Member100.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member100MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member100Layout = new org.jdesktop.layout.GroupLayout(Member100);
        Member100.setLayout(Member100Layout);
        Member100Layout.setHorizontalGroup(
            Member100Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member100Layout.setVerticalGroup(
            Member100Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population9.add(Member100);

        Member101.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member101MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member101Layout = new org.jdesktop.layout.GroupLayout(Member101);
        Member101.setLayout(Member101Layout);
        Member101Layout.setHorizontalGroup(
            Member101Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member101Layout.setVerticalGroup(
            Member101Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population9.add(Member101);

        Member102.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member102MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member102Layout = new org.jdesktop.layout.GroupLayout(Member102);
        Member102.setLayout(Member102Layout);
        Member102Layout.setHorizontalGroup(
            Member102Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member102Layout.setVerticalGroup(
            Member102Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population9.add(Member102);

        Member103.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member103MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member103Layout = new org.jdesktop.layout.GroupLayout(Member103);
        Member103.setLayout(Member103Layout);
        Member103Layout.setHorizontalGroup(
            Member103Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member103Layout.setVerticalGroup(
            Member103Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population9.add(Member103);

        Member104.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member104MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member104Layout = new org.jdesktop.layout.GroupLayout(Member104);
        Member104.setLayout(Member104Layout);
        Member104Layout.setHorizontalGroup(
            Member104Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member104Layout.setVerticalGroup(
            Member104Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population9.add(Member104);

        Member105.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member105MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member105Layout = new org.jdesktop.layout.GroupLayout(Member105);
        Member105.setLayout(Member105Layout);
        Member105Layout.setHorizontalGroup(
            Member105Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member105Layout.setVerticalGroup(
            Member105Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population9.add(Member105);

        Member106.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member106MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member106Layout = new org.jdesktop.layout.GroupLayout(Member106);
        Member106.setLayout(Member106Layout);
        Member106Layout.setHorizontalGroup(
            Member106Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member106Layout.setVerticalGroup(
            Member106Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population9.add(Member106);

        Member107.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member107MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member107Layout = new org.jdesktop.layout.GroupLayout(Member107);
        Member107.setLayout(Member107Layout);
        Member107Layout.setHorizontalGroup(
            Member107Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member107Layout.setVerticalGroup(
            Member107Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population9.add(Member107);

        TabbedPopulation.addTab("-108", Population9);

        Population10.setLayout(new java.awt.GridLayout(3, 4, 2, 2));

        Member108.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member108MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member108Layout = new org.jdesktop.layout.GroupLayout(Member108);
        Member108.setLayout(Member108Layout);
        Member108Layout.setHorizontalGroup(
            Member108Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member108Layout.setVerticalGroup(
            Member108Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population10.add(Member108);

        Member109.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member109MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member109Layout = new org.jdesktop.layout.GroupLayout(Member109);
        Member109.setLayout(Member109Layout);
        Member109Layout.setHorizontalGroup(
            Member109Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member109Layout.setVerticalGroup(
            Member109Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population10.add(Member109);

        Member110.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member110MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member110Layout = new org.jdesktop.layout.GroupLayout(Member110);
        Member110.setLayout(Member110Layout);
        Member110Layout.setHorizontalGroup(
            Member110Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member110Layout.setVerticalGroup(
            Member110Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population10.add(Member110);

        Member111.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member111MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member111Layout = new org.jdesktop.layout.GroupLayout(Member111);
        Member111.setLayout(Member111Layout);
        Member111Layout.setHorizontalGroup(
            Member111Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member111Layout.setVerticalGroup(
            Member111Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population10.add(Member111);

        Member112.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member112MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member112Layout = new org.jdesktop.layout.GroupLayout(Member112);
        Member112.setLayout(Member112Layout);
        Member112Layout.setHorizontalGroup(
            Member112Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member112Layout.setVerticalGroup(
            Member112Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population10.add(Member112);

        Member113.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member113MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member113Layout = new org.jdesktop.layout.GroupLayout(Member113);
        Member113.setLayout(Member113Layout);
        Member113Layout.setHorizontalGroup(
            Member113Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member113Layout.setVerticalGroup(
            Member113Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population10.add(Member113);

        Member114.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member114MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member114Layout = new org.jdesktop.layout.GroupLayout(Member114);
        Member114.setLayout(Member114Layout);
        Member114Layout.setHorizontalGroup(
            Member114Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member114Layout.setVerticalGroup(
            Member114Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population10.add(Member114);

        Member115.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member115MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member115Layout = new org.jdesktop.layout.GroupLayout(Member115);
        Member115.setLayout(Member115Layout);
        Member115Layout.setHorizontalGroup(
            Member115Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member115Layout.setVerticalGroup(
            Member115Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population10.add(Member115);

        Member116.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member116MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member116Layout = new org.jdesktop.layout.GroupLayout(Member116);
        Member116.setLayout(Member116Layout);
        Member116Layout.setHorizontalGroup(
            Member116Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member116Layout.setVerticalGroup(
            Member116Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population10.add(Member116);

        Member117.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member117MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member117Layout = new org.jdesktop.layout.GroupLayout(Member117);
        Member117.setLayout(Member117Layout);
        Member117Layout.setHorizontalGroup(
            Member117Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member117Layout.setVerticalGroup(
            Member117Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population10.add(Member117);

        Member118.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member118MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member118Layout = new org.jdesktop.layout.GroupLayout(Member118);
        Member118.setLayout(Member118Layout);
        Member118Layout.setHorizontalGroup(
            Member118Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member118Layout.setVerticalGroup(
            Member118Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population10.add(Member118);

        Member119.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member119MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member119Layout = new org.jdesktop.layout.GroupLayout(Member119);
        Member119.setLayout(Member119Layout);
        Member119Layout.setHorizontalGroup(
            Member119Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member119Layout.setVerticalGroup(
            Member119Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population10.add(Member119);

        TabbedPopulation.addTab("-120", Population10);

        Population11.setLayout(new java.awt.GridLayout(3, 4, 2, 2));

        Member120.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member120MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member120Layout = new org.jdesktop.layout.GroupLayout(Member120);
        Member120.setLayout(Member120Layout);
        Member120Layout.setHorizontalGroup(
            Member120Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member120Layout.setVerticalGroup(
            Member120Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population11.add(Member120);

        Member121.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member121MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member121Layout = new org.jdesktop.layout.GroupLayout(Member121);
        Member121.setLayout(Member121Layout);
        Member121Layout.setHorizontalGroup(
            Member121Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member121Layout.setVerticalGroup(
            Member121Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population11.add(Member121);

        Member122.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member122MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member122Layout = new org.jdesktop.layout.GroupLayout(Member122);
        Member122.setLayout(Member122Layout);
        Member122Layout.setHorizontalGroup(
            Member122Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member122Layout.setVerticalGroup(
            Member122Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population11.add(Member122);

        Member123.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member123MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member123Layout = new org.jdesktop.layout.GroupLayout(Member123);
        Member123.setLayout(Member123Layout);
        Member123Layout.setHorizontalGroup(
            Member123Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member123Layout.setVerticalGroup(
            Member123Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population11.add(Member123);

        Member124.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member124MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member124Layout = new org.jdesktop.layout.GroupLayout(Member124);
        Member124.setLayout(Member124Layout);
        Member124Layout.setHorizontalGroup(
            Member124Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member124Layout.setVerticalGroup(
            Member124Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population11.add(Member124);

        Member125.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member125MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member125Layout = new org.jdesktop.layout.GroupLayout(Member125);
        Member125.setLayout(Member125Layout);
        Member125Layout.setHorizontalGroup(
            Member125Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member125Layout.setVerticalGroup(
            Member125Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population11.add(Member125);

        Member126.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member126MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member126Layout = new org.jdesktop.layout.GroupLayout(Member126);
        Member126.setLayout(Member126Layout);
        Member126Layout.setHorizontalGroup(
            Member126Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member126Layout.setVerticalGroup(
            Member126Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population11.add(Member126);

        Member127.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member127MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member127Layout = new org.jdesktop.layout.GroupLayout(Member127);
        Member127.setLayout(Member127Layout);
        Member127Layout.setHorizontalGroup(
            Member127Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member127Layout.setVerticalGroup(
            Member127Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population11.add(Member127);

        Member128.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member128MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member128Layout = new org.jdesktop.layout.GroupLayout(Member128);
        Member128.setLayout(Member128Layout);
        Member128Layout.setHorizontalGroup(
            Member128Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member128Layout.setVerticalGroup(
            Member128Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population11.add(Member128);

        Member129.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member129MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member129Layout = new org.jdesktop.layout.GroupLayout(Member129);
        Member129.setLayout(Member129Layout);
        Member129Layout.setHorizontalGroup(
            Member129Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member129Layout.setVerticalGroup(
            Member129Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population11.add(Member129);

        Member130.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member130MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member130Layout = new org.jdesktop.layout.GroupLayout(Member130);
        Member130.setLayout(Member130Layout);
        Member130Layout.setHorizontalGroup(
            Member130Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member130Layout.setVerticalGroup(
            Member130Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population11.add(Member130);

        Member131.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member131MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member131Layout = new org.jdesktop.layout.GroupLayout(Member131);
        Member131.setLayout(Member131Layout);
        Member131Layout.setHorizontalGroup(
            Member131Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member131Layout.setVerticalGroup(
            Member131Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population11.add(Member131);

        TabbedPopulation.addTab("-132", Population11);

        Population12.setLayout(new java.awt.GridLayout(3, 4, 2, 2));

        Member132.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member132MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member132Layout = new org.jdesktop.layout.GroupLayout(Member132);
        Member132.setLayout(Member132Layout);
        Member132Layout.setHorizontalGroup(
            Member132Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member132Layout.setVerticalGroup(
            Member132Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population12.add(Member132);

        Member133.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member133MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member133Layout = new org.jdesktop.layout.GroupLayout(Member133);
        Member133.setLayout(Member133Layout);
        Member133Layout.setHorizontalGroup(
            Member133Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member133Layout.setVerticalGroup(
            Member133Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population12.add(Member133);

        Member134.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member134MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member134Layout = new org.jdesktop.layout.GroupLayout(Member134);
        Member134.setLayout(Member134Layout);
        Member134Layout.setHorizontalGroup(
            Member134Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member134Layout.setVerticalGroup(
            Member134Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population12.add(Member134);

        Member135.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member135MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member135Layout = new org.jdesktop.layout.GroupLayout(Member135);
        Member135.setLayout(Member135Layout);
        Member135Layout.setHorizontalGroup(
            Member135Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member135Layout.setVerticalGroup(
            Member135Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population12.add(Member135);

        Member136.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member136MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member136Layout = new org.jdesktop.layout.GroupLayout(Member136);
        Member136.setLayout(Member136Layout);
        Member136Layout.setHorizontalGroup(
            Member136Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member136Layout.setVerticalGroup(
            Member136Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population12.add(Member136);

        Member137.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member137MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member137Layout = new org.jdesktop.layout.GroupLayout(Member137);
        Member137.setLayout(Member137Layout);
        Member137Layout.setHorizontalGroup(
            Member137Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member137Layout.setVerticalGroup(
            Member137Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population12.add(Member137);

        Member138.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member138MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member138Layout = new org.jdesktop.layout.GroupLayout(Member138);
        Member138.setLayout(Member138Layout);
        Member138Layout.setHorizontalGroup(
            Member138Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member138Layout.setVerticalGroup(
            Member138Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population12.add(Member138);

        Member139.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member139MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member139Layout = new org.jdesktop.layout.GroupLayout(Member139);
        Member139.setLayout(Member139Layout);
        Member139Layout.setHorizontalGroup(
            Member139Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member139Layout.setVerticalGroup(
            Member139Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population12.add(Member139);

        Member140.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member140MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member140Layout = new org.jdesktop.layout.GroupLayout(Member140);
        Member140.setLayout(Member140Layout);
        Member140Layout.setHorizontalGroup(
            Member140Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member140Layout.setVerticalGroup(
            Member140Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population12.add(Member140);

        Member141.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member141MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member141Layout = new org.jdesktop.layout.GroupLayout(Member141);
        Member141.setLayout(Member141Layout);
        Member141Layout.setHorizontalGroup(
            Member141Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member141Layout.setVerticalGroup(
            Member141Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population12.add(Member141);

        Member142.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member142MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member142Layout = new org.jdesktop.layout.GroupLayout(Member142);
        Member142.setLayout(Member142Layout);
        Member142Layout.setHorizontalGroup(
            Member142Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member142Layout.setVerticalGroup(
            Member142Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population12.add(Member142);

        Member143.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Member143MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout Member143Layout = new org.jdesktop.layout.GroupLayout(Member143);
        Member143.setLayout(Member143Layout);
        Member143Layout.setHorizontalGroup(
            Member143Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 187, Short.MAX_VALUE)
        );
        Member143Layout.setVerticalGroup(
            Member143Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 172, Short.MAX_VALUE)
        );

        Population12.add(Member143);

        TabbedPopulation.addTab("-144", Population12);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(OptionTabs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .add(Controls, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(TabbedPopulation)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(Controls, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 126, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(OptionTabs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 124, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(TabbedPopulation, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 596, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        OptionTabs.getAccessibleContext().setAccessibleName("OptionTabs");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Member1MouseClicked(MouseEvent evt) {//GEN-FIRST:event_Member1MouseClicked
        Member001.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(1)));
    }//GEN-LAST:event_Member1MouseClicked

    private void Member2MouseClicked(MouseEvent evt) {//GEN-FIRST:event_Member2MouseClicked
        Member2.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(2)));
    }//GEN-LAST:event_Member2MouseClicked

    private void Member3MouseClicked(MouseEvent evt) {//GEN-FIRST:event_Member3MouseClicked
        Member3.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(3)));
    }//GEN-LAST:event_Member3MouseClicked

    private void Member4MouseClicked(MouseEvent evt) {
        Member4.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(4)));
    }

    private void Member5MouseClicked(MouseEvent evt) {
        Member5.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(5)));
    }

    private void Member6MouseClicked(MouseEvent evt) {
        Member6.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(6)));
    }

    private void Member7MouseClicked(MouseEvent evt) {
        Member7.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(7)));
    }

    private void Member8MouseClicked(MouseEvent evt) {
        Member8.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(8)));
    }

    private void Member9MouseClicked(MouseEvent evt) {
        Member9.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(9)));
    }

    private void Member10MouseClicked(MouseEvent evt) {
        Member010.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(10)));
    }

    private void Member11MouseClicked(MouseEvent evt) {
        Member011.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(11)));
    }

    private void QuitMouseClicked(MouseEvent evt) {
        quitSafely();
    }

    private void BackMouseClicked(MouseEvent evt) {
        if (!running) {
            System.out.println("in BackMouseClicked()");
            poaiecSessionCtlr.back();
            updateAllPanels();
            System.out.println("end of BackMouseClicked()");
        }
    }

    private void ForwardMouseClicked(MouseEvent evt) {
        if (!running) {
            System.out.println("in ForwardMouseClicked()");
            poaiecSessionCtlr.forward();
            updateAllPanels();
            System.out.println("end of ForwardMouseClicked()");
        }
    }

    @SuppressWarnings("unused")
	private void StepMouseClicked(MouseEvent evt) {
        if (!running) {
            System.out.println("in StepMouseClicked()");
            progressMonitor = new ProgressMonitor(this, "Generating a new IEC population.", "", 0, 100);
            progressMonitor.setProgress(0);
            poaiecSessionCtlr.addProgressListener(this);

            pausePlayer();
            running = true;

        	// Generate a new population based on selected candidate(s)
            poaiecSessionCtlr.doStep();
            System.out.println("end of StepMouseClicked()");
        }
    }

    @SuppressWarnings("unused")
	private void NoveltyMouseClicked(MouseEvent evt) {
        if (!running) {
            progressMonitor = new ProgressMonitor(this, "Running a Short-Term Novelty Search", "", 0, 100);
            progressMonitor.setProgress(0);
            poaiecSessionCtlr.addProgressListener(this);

            pausePlayer();
            running = true;

            // Search for novel candidates based on selected candidate(s)
            poaiecSessionCtlr.doNovelty();
        }
    }
    
    private void ParetoMouseClicked(MouseEvent evt) {
    	if (!running) {
    		progressMonitor = new ProgressMonitor(this, "Running a Pareto Optimality Search", "", 0, 100);
    		progressMonitor.setProgress(0);
    		poaiecSessionCtlr.addProgressListener(this);
    		
    		pausePlayer();
    		running = true;
    		
    		poaiecSessionCtlr.doPareto();
    	}
    }

    private void OptimizeMouseClicked(MouseEvent evt) {//GEN-FIRST:event_OptimizeMouseClicked
        if (!running) {
            int numberOfCandidatesSelected = 0;
            int indexOfSelectedCandidate = -1;
            for (int i = 0; i < poaiecSessionCtlr.getIECpopulationSize(); i++) {
                if (poaiecSessionCtlr.isCandidateSelected(i)) {
                    numberOfCandidatesSelected++;
                    indexOfSelectedCandidate = i;
                }
            }
            if (numberOfCandidatesSelected > 1) {
                // Notify user that only one member can be selected
                JOptionPane.showMessageDialog(this, "Only one member can be optimized at a time.", "Optimize Function", JOptionPane.INFORMATION_MESSAGE);
            } else if (numberOfCandidatesSelected < 1) {
                // Notify user that exactly one member must be selected
                JOptionPane.showMessageDialog(this, "Please select one member to optimize.", "Optimize Function", JOptionPane.INFORMATION_MESSAGE);
            } else {
                progressMonitor = new ProgressMonitor(this, "Running a Short-Term Fitness Search", "", 0, 100);
                progressMonitor.setProgress(0);
                poaiecSessionCtlr.addProgressListener(this);

                pausePlayer();
                running = true;

                // Optimize the selected candiate
                poaiecSessionCtlr.optimize(indexOfSelectedCandidate);
            }
        }
    }//GEN-LAST:event_OptimizeMouseClicked

    private void SaveMouseClicked(MouseEvent evt) {//GEN-FIRST:event_SaveMouseClicked
        poaiecSessionCtlr.save();
    }//GEN-LAST:event_SaveMouseClicked

    private void PublishMouseClicked(MouseEvent evt) {//GEN-FIRST:event_PublishMouseClicked
        if (running) {
            return;
        }

        int numberOfCandidatesSelected = 0;
        int indexOfSelectedCandidate = -1;
        for (int i = 0; i < poaiecSessionCtlr.getIECpopulationSize(); i++) {
            if (poaiecSessionCtlr.isCandidateSelected(i)) {
                numberOfCandidatesSelected++;
                indexOfSelectedCandidate = i;
            }
        }
        if (numberOfCandidatesSelected > 1) {
            // Notify user that only one member can be selected
            JOptionPane.showMessageDialog(this, "Only one member can be published at a time.", "Publish", JOptionPane.INFORMATION_MESSAGE);
        } else if (numberOfCandidatesSelected < 1) {
            // Notify user that exactly one member must be selected
            JOptionPane.showMessageDialog(this, "Please select a member to publish.", "Publish", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Save the selected candiate and its lineage
            poaiecSessionCtlr.publish(indexOfSelectedCandidate);
            int result = JOptionPane.showConfirmDialog(this, "The solution has been published successfully.\nDo you want to start again?", "Solution Published", JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                poaiecSessionCtlr = new PoaiecController(f_domain);
                seenSolutions.clear();
                updateAllPanels();
            }
        }
    }//GEN-LAST:event_PublishMouseClicked

    private void formWindowClosing(WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        quitSafely();
    }

    private void quitSafely() {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Unsaved Data", JOptionPane.YES_NO_OPTION);
        if (result == 0) {
            System.exit(0);
        }
    }//GEN-LAST:event_formWindowClosing

    @SuppressWarnings("unused")
	private void showHideArchiveActionPerformed(ActionEvent evt) {//GEN-FIRST:event_showHideArchiveActionPerformed
        if (!running) {
            updateAllPanels(false);
        }
    }//GEN-LAST:event_showHideArchiveActionPerformed

    private void toggleDynamicPOPVActionPerformed(ActionEvent evt) {//GEN-FIRST:event_showHideArchiveActionPerformed
        if (!running) {
            poaiecSessionCtlr.toggleDynamicPOPV();
        }
    }//GEN-LAST:event_toggleDynamicPOPVActionPerformed

    @SuppressWarnings("unused")
	private void noveltyThresholdStateChanged(ChangeEvent evt) {//GEN-FIRST:event_noveltyThresholdStateChanged
        if (!running) {
            poaiecSessionCtlr.setNoveltyThresholdMultiplyer((double) noveltyThreshold.getValue());
        }
    }//GEN-LAST:event_noveltyThresholdStateChanged

    private void optimizeEvaluationLimitStateChanged(ChangeEvent evt) {//GEN-FIRST:event_optimizeEvaluationLimitStateChanged
        if (!running) {
            poaiecSessionCtlr.setFitnessEvaluationLimit(optimizeEvaluationLimit.getValue());
        }
    }//GEN-LAST:event_optimizeEvaluationLimitStateChanged

    private void optimizeMutationRateStateChanged(ChangeEvent evt) {//GEN-FIRST:event_optimizeMutationRateStateChanged
        if (!running) {
            poaiecSessionCtlr.setFitnessMutationRate(optimizeMutationRate.getValue());
        }
    }//GEN-LAST:event_optimizeMutationRateStateChanged

    private void stepMutationPowerStateChanged(ChangeEvent evt) {//GEN-FIRST:event_stepMutationPowerStateChanged
        if (!running) {
            poaiecSessionCtlr.setStepMutationPower(stepMutationPower.getValue());
        }
    }//GEN-LAST:event_stepMutationPowerStateChanged

    private void stepMutationRateStateChanged(ChangeEvent evt) {//GEN-FIRST:event_stepMutationRateStateChanged
        if (!running) {
            poaiecSessionCtlr.setStepMutationRate(stepMutationRate.getValue());
        }
    }//GEN-LAST:event_stepMutationRateStateChanged

    private void SaveActionPerformed(ActionEvent evt) {//GEN-FIRST:event_SaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SaveActionPerformed

    private void Member0MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member0MouseClicked
        Member000.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(0)));
     }//GEN-LAST:event_Member0MouseClicked

    private void PlayPauseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayPauseMouseClicked
        if (playMngr.isPlaying()) {
            pausePlayer();
        } else if (playMngr.isPaused()) {
            startPlayer();
        }
    }//GEN-LAST:event_PlayPauseMouseClicked

    private void AnimationProgressBarStateChanged(ChangeEvent evt) {//GEN-FIRST:event_AnimationProgressBarStateChanged
        playMngr.goTo((float) AnimationProgressBar.getValue() / AnimationProgressBar.getMaximum());
    }//GEN-LAST:event_AnimationProgressBarStateChanged

    private void RepeatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RepeatMouseClicked
        playMngr.setRepeat(Repeat.isSelected());
    }//GEN-LAST:event_RepeatMouseClicked

    private void Member12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member12MouseClicked
        Member12.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(12)));
    }//GEN-LAST:event_Member12MouseClicked

    private void Member13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member13MouseClicked
        Member13.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(13)));
    }//GEN-LAST:event_Member13MouseClicked

    private void Member14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member14MouseClicked
        Member14.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(14)));
    }//GEN-LAST:event_Member14MouseClicked

    private void Member15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member15MouseClicked
        Member15.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(15)));
    }//GEN-LAST:event_Member15MouseClicked

    private void Member16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member16MouseClicked
        Member16.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(16)));
    }//GEN-LAST:event_Member16MouseClicked

    private void Member17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member17MouseClicked
        Member17.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(17)));
    }//GEN-LAST:event_Member17MouseClicked

    private void Member18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member18MouseClicked
        Member18.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(18)));
    }//GEN-LAST:event_Member18MouseClicked

    private void Member19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member19MouseClicked
        Member19.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(19)));
    }//GEN-LAST:event_Member19MouseClicked

    private void Member20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member20MouseClicked
        Member20.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(20)));
    }//GEN-LAST:event_Member20MouseClicked

    private void Member21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member21MouseClicked
        Member21.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(21)));
    }//GEN-LAST:event_Member21MouseClicked

    private void Member22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member22MouseClicked
        Member22.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(22)));
    }//GEN-LAST:event_Member22MouseClicked

    private void Member23MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member23MouseClicked
        Member23.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(23)));
    }//GEN-LAST:event_Member23MouseClicked

    private void Member24MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member24MouseClicked
        Member24.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(24)));
    }//GEN-LAST:event_Member24MouseClicked

    private void Member25MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member25MouseClicked
        Member25.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(25)));
    }//GEN-LAST:event_Member25MouseClicked

    private void Member26MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member26MouseClicked
        Member26.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(26)));
    }//GEN-LAST:event_Member26MouseClicked

    private void Member27MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member27MouseClicked
        Member27.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(27)));
    }//GEN-LAST:event_Member27MouseClicked

    private void Member28MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member28MouseClicked
        Member28.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(28)));
    }//GEN-LAST:event_Member28MouseClicked

    private void Member29MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member29MouseClicked
        Member29.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(29)));
    }//GEN-LAST:event_Member29MouseClicked

    private void Member30MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member30MouseClicked
        Member30.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(30)));
    }//GEN-LAST:event_Member30MouseClicked

    private void Member31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member31MouseClicked
        Member31.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(31)));
    }//GEN-LAST:event_Member31MouseClicked

    private void Member32MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member32MouseClicked
        Member32.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(32)));
    }//GEN-LAST:event_Member32MouseClicked

    private void Member33MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member33MouseClicked
        Member33.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(33)));
    }//GEN-LAST:event_Member33MouseClicked

    private void Member34MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member34MouseClicked
        Member34.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(34)));
    }//GEN-LAST:event_Member34MouseClicked

    private void Member35MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member35MouseClicked
        Member35.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(35)));
    }//GEN-LAST:event_Member35MouseClicked

    private void Member36MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member36MouseClicked
        Member36.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(36)));
    }//GEN-LAST:event_Member36MouseClicked

    private void Member37MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member37MouseClicked
        Member37.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(37)));
    }//GEN-LAST:event_Member37MouseClicked

    private void Member38MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member38MouseClicked
        Member38.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(38)));
    }//GEN-LAST:event_Member38MouseClicked

    private void Member39MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member39MouseClicked
        Member39.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(39)));
    }//GEN-LAST:event_Member39MouseClicked

    private void Member40MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member40MouseClicked
        Member40.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(40)));
    }//GEN-LAST:event_Member40MouseClicked

    private void Member41MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member41MouseClicked
        Member41.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(41)));
    }//GEN-LAST:event_Member41MouseClicked

    private void Member42MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member42MouseClicked
        Member42.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(42)));
    }//GEN-LAST:event_Member42MouseClicked

    private void Member43MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member43MouseClicked
        Member43.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(43)));
    }//GEN-LAST:event_Member43MouseClicked

    private void Member44MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member44MouseClicked
        Member44.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(44)));
    }//GEN-LAST:event_Member44MouseClicked

    private void Member45MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member45MouseClicked
        Member45.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(45)));
    }//GEN-LAST:event_Member45MouseClicked

    private void Member46MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member46MouseClicked
        Member46.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(46)));
    }//GEN-LAST:event_Member46MouseClicked

    private void Member47MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member47MouseClicked
        Member47.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(47)));
    }//GEN-LAST:event_Member47MouseClicked

    private void Member48MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member48MouseClicked
        Member48.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(48)));
    }//GEN-LAST:event_Member48MouseClicked

    private void Member49MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member49MouseClicked
        Member49.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(49)));
    }//GEN-LAST:event_Member49MouseClicked

    private void Member50MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member50MouseClicked
        Member50.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(50)));
    }//GEN-LAST:event_Member50MouseClicked

    private void Member51MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member51MouseClicked
        Member51.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(51)));
    }//GEN-LAST:event_Member51MouseClicked

    private void Member52MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member52MouseClicked
        Member52.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(52)));
    }//GEN-LAST:event_Member52MouseClicked

    private void Member53MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member53MouseClicked
        Member53.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(53)));
    }//GEN-LAST:event_Member53MouseClicked

    private void Member54MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member54MouseClicked
        Member54.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(54)));
    }//GEN-LAST:event_Member54MouseClicked

    private void Member55MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member55MouseClicked
        Member55.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(55)));
    }//GEN-LAST:event_Member55MouseClicked

    private void Member56MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member56MouseClicked
        Member56.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(56)));
    }//GEN-LAST:event_Member56MouseClicked

    private void Member57MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member57MouseClicked
        Member57.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(57)));
    }//GEN-LAST:event_Member57MouseClicked

    private void Member58MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member58MouseClicked
        Member58.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(58)));
    }//GEN-LAST:event_Member58MouseClicked

    private void Member59MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member59MouseClicked
        Member59.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(59)));
    }//GEN-LAST:event_Member59MouseClicked

    private void Member60MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member60MouseClicked
        Member60.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(60)));
    }//GEN-LAST:event_Member60MouseClicked

    private void Member61MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member61MouseClicked
        Member61.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(61)));
    }//GEN-LAST:event_Member61MouseClicked

    private void Member62MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member62MouseClicked
        Member62.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(62)));
    }//GEN-LAST:event_Member62MouseClicked

    private void Member63MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member63MouseClicked
        Member63.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(63)));
    }//GEN-LAST:event_Member63MouseClicked

    private void Member64MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member64MouseClicked
        Member64.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(64)));
    }//GEN-LAST:event_Member64MouseClicked

    private void Member65MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member65MouseClicked
        Member65.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(65)));
    }//GEN-LAST:event_Member65MouseClicked

    private void Member66MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member66MouseClicked
        Member66.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(66)));
    }//GEN-LAST:event_Member66MouseClicked

    private void Member67MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member67MouseClicked
        Member67.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(67)));
    }//GEN-LAST:event_Member67MouseClicked

    private void Member68MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member68MouseClicked
        Member68.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(68)));
    }//GEN-LAST:event_Member68MouseClicked

    private void Member69MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member69MouseClicked
        Member69.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(69)));
    }//GEN-LAST:event_Member69MouseClicked

    private void Member70MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member70MouseClicked
        Member70.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(70)));
    }//GEN-LAST:event_Member70MouseClicked

    private void Member71MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member71MouseClicked
        Member71.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(71)));
    }//GEN-LAST:event_Member71MouseClicked

    private void Member72MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member72MouseClicked
        Member72.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(72)));
    }//GEN-LAST:event_Member72MouseClicked

    private void Member73MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member73MouseClicked
        Member73.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(73)));
    }//GEN-LAST:event_Member73MouseClicked

    private void Member74MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member74MouseClicked
        Member74.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(74)));
    }//GEN-LAST:event_Member74MouseClicked

    private void Member75MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member75MouseClicked
        Member75.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(75)));
    }//GEN-LAST:event_Member75MouseClicked

    private void Member76MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member76MouseClicked
        Member76.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(76)));
    }//GEN-LAST:event_Member76MouseClicked

    private void Member77MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member77MouseClicked
        Member77.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(77)));
    }//GEN-LAST:event_Member77MouseClicked

    private void Member78MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member78MouseClicked
        Member78.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(78)));
    }//GEN-LAST:event_Member78MouseClicked

    private void Member79MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member79MouseClicked
        Member79.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(79)));
    }//GEN-LAST:event_Member79MouseClicked

    private void Member80MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member80MouseClicked
        Member80.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(80)));
    }//GEN-LAST:event_Member80MouseClicked

    private void Member81MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member81MouseClicked
        Member81.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(81)));
    }//GEN-LAST:event_Member81MouseClicked

    private void Member82MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member82MouseClicked
        Member82.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(82)));
    }//GEN-LAST:event_Member82MouseClicked

    private void Member83MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member83MouseClicked
        Member83.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(83)));
    }//GEN-LAST:event_Member83MouseClicked

    private void Member84MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member84MouseClicked
        Member84.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(84)));
    }//GEN-LAST:event_Member84MouseClicked

    private void Member85MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member85MouseClicked
        Member85.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(85)));
    }//GEN-LAST:event_Member85MouseClicked

    private void Member86MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member86MouseClicked
        Member86.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(86)));
    }//GEN-LAST:event_Member86MouseClicked

    private void Member87MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member87MouseClicked
        Member87.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(87)));
    }//GEN-LAST:event_Member87MouseClicked

    private void Member88MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member88MouseClicked
        Member88.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(88)));
    }//GEN-LAST:event_Member88MouseClicked

    private void Member89MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member89MouseClicked
        Member89.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(89)));
    }//GEN-LAST:event_Member89MouseClicked

    private void Member90MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member90MouseClicked
        Member90.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(90)));
    }//GEN-LAST:event_Member90MouseClicked

    private void Member91MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member91MouseClicked
        Member91.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(91)));
    }//GEN-LAST:event_Member91MouseClicked

    private void Member92MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member92MouseClicked
        Member92.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(92)));
    }//GEN-LAST:event_Member92MouseClicked

    private void Member93MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member93MouseClicked
        Member93.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(93)));
    }//GEN-LAST:event_Member93MouseClicked

    private void Member94MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member94MouseClicked
        Member94.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(94)));
    }//GEN-LAST:event_Member94MouseClicked

    private void Member95MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member95MouseClicked
        Member95.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(95)));
    }//GEN-LAST:event_Member95MouseClicked

    private void Member96MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member96MouseClicked
        Member96.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(96)));
    }//GEN-LAST:event_Member96MouseClicked

    private void Member97MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member97MouseClicked
        Member97.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(97)));
    }//GEN-LAST:event_Member97MouseClicked

    private void Member98MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member98MouseClicked
        Member98.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(98)));
    }//GEN-LAST:event_Member98MouseClicked

    private void Member99MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member99MouseClicked
        Member99.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(99)));
    }//GEN-LAST:event_Member99MouseClicked

    private void Member100MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member100MouseClicked
        Member100.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(100)));
    }//GEN-LAST:event_Member100MouseClicked

    private void Member101MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member101MouseClicked
        Member101.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(101)));
    }//GEN-LAST:event_Member101MouseClicked

    private void Member102MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member102MouseClicked
        Member102.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(102)));
    }//GEN-LAST:event_Member102MouseClicked

    private void Member103MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member103MouseClicked
        Member103.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(103)));
    }//GEN-LAST:event_Member103MouseClicked

    private void Member104MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member104MouseClicked
        Member104.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(104)));
    }//GEN-LAST:event_Member104MouseClicked

    private void Member105MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member105MouseClicked
        Member105.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(105)));
    }//GEN-LAST:event_Member105MouseClicked

    private void Member106MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member106MouseClicked
        Member106.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(106)));
    }//GEN-LAST:event_Member106MouseClicked

    private void Member107MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member107MouseClicked
        Member107.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(107)));
    }//GEN-LAST:event_Member107MouseClicked

    private void Member108MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member108MouseClicked
        Member108.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(108)));
    }//GEN-LAST:event_Member108MouseClicked

    private void Member109MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member109MouseClicked
        Member109.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(109)));
    }//GEN-LAST:event_Member109MouseClicked

    private void Member110MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member110MouseClicked
        Member110.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(110)));
    }//GEN-LAST:event_Member110MouseClicked

    private void Member111MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member111MouseClicked
        Member111.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(111)));
    }//GEN-LAST:event_Member111MouseClicked

    private void Member112MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member112MouseClicked
        Member112.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(112)));
    }//GEN-LAST:event_Member112MouseClicked

    private void Member113MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member113MouseClicked
        Member113.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(113)));
    }//GEN-LAST:event_Member113MouseClicked

    private void Member114MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member114MouseClicked
        Member114.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(114)));
    }//GEN-LAST:event_Member114MouseClicked

    private void Member115MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member115MouseClicked
        Member115.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(115)));
    }//GEN-LAST:event_Member115MouseClicked

    private void Member116MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member116MouseClicked
        Member116.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(116)));
    }//GEN-LAST:event_Member116MouseClicked

    private void Member117MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member117MouseClicked
        Member117.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(117)));
    }//GEN-LAST:event_Member117MouseClicked

    private void Member118MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member118MouseClicked
        Member118.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(118)));
    }//GEN-LAST:event_Member118MouseClicked

    private void Member119MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member119MouseClicked
        Member119.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(119)));
    }//GEN-LAST:event_Member119MouseClicked

    private void Member120MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member120MouseClicked
        Member120.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(120)));
    }//GEN-LAST:event_Member120MouseClicked

    private void Member121MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member121MouseClicked
        Member121.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(121)));
    }//GEN-LAST:event_Member121MouseClicked

    private void Member122MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member122MouseClicked
        Member122.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(122)));
    }//GEN-LAST:event_Member122MouseClicked

    private void Member123MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member123MouseClicked
        Member123.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(123)));
    }//GEN-LAST:event_Member123MouseClicked

    private void Member124MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member124MouseClicked
        Member124.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(124)));
    }//GEN-LAST:event_Member124MouseClicked

    private void Member125MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member125MouseClicked
        Member125.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(125)));
    }//GEN-LAST:event_Member125MouseClicked

    private void Member126MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member126MouseClicked
        Member126.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(126)));
    }//GEN-LAST:event_Member126MouseClicked

    private void Member127MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member127MouseClicked
        Member127.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(127)));
    }//GEN-LAST:event_Member127MouseClicked

    private void Member128MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member128MouseClicked
        Member128.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(128)));
    }//GEN-LAST:event_Member128MouseClicked

    private void Member129MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member129MouseClicked
        Member129.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(129)));
    }//GEN-LAST:event_Member129MouseClicked

    private void Member130MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member130MouseClicked
        Member130.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(130)));
    }//GEN-LAST:event_Member130MouseClicked

    private void Member131MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member131MouseClicked
        Member131.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(131)));
    }//GEN-LAST:event_Member131MouseClicked

    private void Member132MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member132MouseClicked
        Member132.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(132)));
    }//GEN-LAST:event_Member132MouseClicked

    private void Member133MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member133MouseClicked
        Member133.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(133)));
    }//GEN-LAST:event_Member133MouseClicked

    private void Member134MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member134MouseClicked
        Member134.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(134)));
    }//GEN-LAST:event_Member134MouseClicked

    private void Member135MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member135MouseClicked
        Member135.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(135)));
    }//GEN-LAST:event_Member135MouseClicked

    private void Member136MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member136MouseClicked
        Member136.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(136)));
    }//GEN-LAST:event_Member136MouseClicked

    private void Member137MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member137MouseClicked
        Member137.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(137)));
    }//GEN-LAST:event_Member137MouseClicked

    private void Member138MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member138MouseClicked
        Member138.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(138)));
    }//GEN-LAST:event_Member138MouseClicked

    private void Member139MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member139MouseClicked
        Member139.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(139)));
    }//GEN-LAST:event_Member139MouseClicked

    private void Member140MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member140MouseClicked
        Member140.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(140)));
    }//GEN-LAST:event_Member140MouseClicked

    private void Member141MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member141MouseClicked
        Member141.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(141)));
    }//GEN-LAST:event_Member141MouseClicked

    private void Member142MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member142MouseClicked
        Member142.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(142)));
    }//GEN-LAST:event_Member142MouseClicked

    private void Member143MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Member143MouseClicked
        Member143.setBorder(isSelected(poaiecSessionCtlr.toggelCandidateIsSelected(143)));
    }//GEN-LAST:event_Member143MouseClicked

//    private void ShowBehaviorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowBehaviorActionPerformed
//        poaiecSessionCtlr.setShowBehavior(ShowBehavior.isSelected());
//    }//GEN-LAST:event_ShowBehaviorActionPerformed
//
//    private void ShowTrajectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowTrajectoryActionPerformed
//        poaiecSessionCtlr.setShowPath(ShowTrajectory.isSelected());
//    }//GEN-LAST:event_ShowTrajectoryActionPerformed
//
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JSlider AnimationProgressBar;
    private JPanel Annimation;
    private JButton Back;
    private JPanel Controls;
    private JButton Forward;
    private Map<String, Icon> icons;
    private EvaluationPanel Member000;
    private EvaluationPanel Member001;
    private EvaluationPanel Member010;
    private EvaluationPanel Member100;
    private EvaluationPanel Member101;
    private EvaluationPanel Member102;
    private EvaluationPanel Member103;
    private EvaluationPanel Member104;
    private EvaluationPanel Member105;
    private EvaluationPanel Member106;
    private EvaluationPanel Member107;
    private EvaluationPanel Member108;
    private EvaluationPanel Member109;
    private EvaluationPanel Member011;
    private EvaluationPanel Member110;
    private EvaluationPanel Member111;
    private EvaluationPanel Member112;
    private EvaluationPanel Member113;
    private EvaluationPanel Member114;
    private EvaluationPanel Member115;
    private EvaluationPanel Member116;
    private EvaluationPanel Member117;
    private EvaluationPanel Member118;
    private EvaluationPanel Member119;
    private EvaluationPanel Member12;
    private EvaluationPanel Member120;
    private EvaluationPanel Member121;
    private EvaluationPanel Member122;
    private EvaluationPanel Member123;
    private EvaluationPanel Member124;
    private EvaluationPanel Member125;
    private EvaluationPanel Member126;
    private EvaluationPanel Member127;
    private EvaluationPanel Member128;
    private EvaluationPanel Member129;
    private EvaluationPanel Member13;
    private EvaluationPanel Member130;
    private EvaluationPanel Member131;
    private EvaluationPanel Member132;
    private EvaluationPanel Member133;
    private EvaluationPanel Member134;
    private EvaluationPanel Member135;
    private EvaluationPanel Member136;
    private EvaluationPanel Member137;
    private EvaluationPanel Member138;
    private EvaluationPanel Member139;
    private EvaluationPanel Member14;
    private EvaluationPanel Member140;
    private EvaluationPanel Member141;
    private EvaluationPanel Member142;
    private EvaluationPanel Member143;
    private EvaluationPanel Member15;
    private EvaluationPanel Member16;
    private EvaluationPanel Member17;
    private EvaluationPanel Member18;
    private EvaluationPanel Member19;
    private EvaluationPanel Member2;
    private EvaluationPanel Member20;
    private EvaluationPanel Member21;
    private EvaluationPanel Member22;
    private EvaluationPanel Member23;
    private EvaluationPanel Member24;
    private EvaluationPanel Member25;
    private EvaluationPanel Member26;
    private EvaluationPanel Member27;
    private EvaluationPanel Member28;
    private EvaluationPanel Member29;
    private EvaluationPanel Member3;
    private EvaluationPanel Member30;
    private EvaluationPanel Member31;
    private EvaluationPanel Member32;
    private EvaluationPanel Member33;
    private EvaluationPanel Member34;
    private EvaluationPanel Member35;
    private EvaluationPanel Member36;
    private EvaluationPanel Member37;
    private EvaluationPanel Member38;
    private EvaluationPanel Member39;
    private EvaluationPanel Member4;
    private EvaluationPanel Member40;
    private EvaluationPanel Member41;
    private EvaluationPanel Member42;
    private EvaluationPanel Member43;
    private EvaluationPanel Member44;
    private EvaluationPanel Member45;
    private EvaluationPanel Member46;
    private EvaluationPanel Member47;
    private EvaluationPanel Member48;
    private EvaluationPanel Member49;
    private EvaluationPanel Member5;
    private EvaluationPanel Member50;
    private EvaluationPanel Member51;
    private EvaluationPanel Member52;
    private EvaluationPanel Member53;
    private EvaluationPanel Member54;
    private EvaluationPanel Member55;
    private EvaluationPanel Member56;
    private EvaluationPanel Member57;
    private EvaluationPanel Member58;
    private EvaluationPanel Member59;
    private EvaluationPanel Member6;
    private EvaluationPanel Member60;
    private EvaluationPanel Member61;
    private EvaluationPanel Member62;
    private EvaluationPanel Member63;
    private EvaluationPanel Member64;
    private EvaluationPanel Member65;
    private EvaluationPanel Member66;
    private EvaluationPanel Member67;
    private EvaluationPanel Member68;
    private EvaluationPanel Member69;
    private EvaluationPanel Member7;
    private EvaluationPanel Member70;
    private EvaluationPanel Member71;
    private EvaluationPanel Member72;
    private EvaluationPanel Member73;
    private EvaluationPanel Member74;
    private EvaluationPanel Member75;
    private EvaluationPanel Member76;
    private EvaluationPanel Member77;
    private EvaluationPanel Member78;
    private EvaluationPanel Member79;
    private EvaluationPanel Member8;
    private EvaluationPanel Member80;
    private EvaluationPanel Member81;
    private EvaluationPanel Member82;
    private EvaluationPanel Member83;
    private EvaluationPanel Member84;
    private EvaluationPanel Member85;
    private EvaluationPanel Member86;
    private EvaluationPanel Member87;
    private EvaluationPanel Member88;
    private EvaluationPanel Member89;
    private EvaluationPanel Member9;
    private EvaluationPanel Member90;
    private EvaluationPanel Member91;
    private EvaluationPanel Member92;
    private EvaluationPanel Member93;
    private EvaluationPanel Member94;
    private EvaluationPanel Member95;
    private EvaluationPanel Member96;
    private EvaluationPanel Member97;
    private EvaluationPanel Member98;
    private EvaluationPanel Member99;
    @SuppressWarnings("unused")
	private JButton Novelty;
    @SuppressWarnings("unused")
	private JPanel NoveltyOptions;
    private JButton Pareto;
    private JPanel ParetoOptions;
    private JButton Optimize;
    private JPanel OptimizeOptions;
    private OptimizeOptionsPanel optimizeOptionsPanel1;
    private JTabbedPane OptionTabs;
    private JButton PlayPause;
    private JPanel Population1;
    private JPanel Population10;
    private JPanel Population11;
    private JPanel Population12;
    private JPanel Population2;
    private JPanel Population3;
    private JPanel Population4;
    private JPanel Population5;
    private JPanel Population6;
    private JPanel Population7;
    private JPanel Population8;
    private JPanel Population9;
    private JButton Publish;
    private JButton Quit;
    private JToggleButton Repeat;
    private JButton Save;
//    private JCheckBox ShowBehavior;
//    private JCheckBox ShowTrajectory;
    @SuppressWarnings("unused")
	private JButton Step;
    private JPanel StepOptions;
    private JTabbedPane TabbedPopulation;
    private DomainOptionsPanel domainOptionsPanel1;
    @SuppressWarnings("unused")
	private JLabel jLabel1;
    private JLabel jLabel10;
    @SuppressWarnings("unused")
	private JLabel jLabel11;
    @SuppressWarnings("unused")
	private JLabel jLabel12;
    private JLabel jLabel13;
    private JLabel jLabel14;
    private JLabel jLabel15;
    private JLabel jLabel16;
    private JLabel jLabel17;
    private JLabel jLabel18;
    private JLabel jLabel19;
    @SuppressWarnings("unused")
	private JLabel jLabel2;
    @SuppressWarnings("unused")
	private JLabel jLabel20;
	@SuppressWarnings("unused")
	private JLabel jLabel21;
    private JLabel jLabel22;
    private JLabel jLabel23;
    private JLabel jLabel24;
    private JLabel jLabel25;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JSlider noveltyThreshold;
    private JSlider optimizeEvaluationLimit;
    private JSlider optimizeMutationRate;
    @SuppressWarnings("unused")
	private JToggleButton showHideArchive;
    private JToggleButton toggleDynamicPOPV;
    private JSlider stepMutationPower;
    private JSlider stepMutationRate;
    // End of variables declaration//GEN-END:variables

    private Color isSolution(boolean candidateSolution) {
        if (candidateSolution) {
            return Color.WHITE;
        } else {
            return Population1.getBackground();
        }
    }

    private Border isSelected(boolean candidateIsSelected) {
        if (candidateIsSelected) {
            return BorderFactory.createLineBorder(new Color(0, 192, 0), 4);
        } else {
            return BorderFactory.createLineBorder(Color.BLUE, 4);
        }
    }

    private void updateAllPanels() {
        updateAllPanels(true);
    }

    private void updateAllPanels(boolean resetTabPanel) {
        // Updates the number of evaluations that have been performed so far
        // --------------------------
        TabbedPopulation.setBorder(BorderFactory.createTitledBorder(populationBoarderTitle()));

        if (resetTabPanel) {
            TabbedPopulation.setSelectedIndex(0);
        }
//        int lastCandidate = iecSessionController.getIECpopulationSize();
        int lastTab = (int) Math.ceil(poaiecSessionCtlr.getIECpopulationSize() / 12.0);
        for (int i = 0; i < TabbedPopulation.getTabCount(); i++) {
            TabbedPopulation.setEnabledAt(i, lastTab > i);
        }

        // Update each candidate panel in the IEC population
        // --------------------------
        int index;
        for (EvaluationPanel panel : candidatePanels) {
            index = candidatePanels.indexOf(panel);
            if (index < poaiecSessionCtlr.getIECpopulationSize()) {
                panel.setVisible(true);
                panel.setBorder(isSelected(poaiecSessionCtlr.isCandidateSelected(index)));
                panel.setBackground(isSolution(poaiecSessionCtlr.isCandidateSolution(index)));
                
                HashMap<Long,Integer> fitness = poaiecSessionCtlr.getCandidateFitness(index);
                HashMap<Long,EvaluationFunction> funcNames = poaiecSessionCtlr.getCandidate(index).getFuncNames();
                HashMap<String,Integer> funcNameMap = new HashMap<String,Integer>();
                for (Entry<Long,Integer> entry : fitness.entrySet()) {
                	String funcName = funcNames.get(entry.getKey()).shortName();
                	funcNameMap.put(funcName, entry.getValue());
                }
                String tooltip = "<html>ChromID: " + poaiecSessionCtlr.getCandidateId(index) + " <br/>"
                        + "Fitness: " + funcNameMap + " <br/>"
                        + "Dominates " + poaiecSessionCtlr.getCandidateDominates(index) + " others<br/>"
                        + "Dominated by " + poaiecSessionCtlr.getCandidateDominatedBy(index) + " others<br/>"
                        + "Novelty: " + poaiecSessionCtlr.getCandidateNovelty(index) + "</html>";
                
                panel.setToolTipText(tooltip);

                panel.repaint();

//                if (showHideArchive.isSelected()) {
//                    panel.setSubject(iecSessionController.getPhenotypeBehavior(index), iecSessionController.getArchivedPts());
//                } else {
//                    panel.setSubject(iecSessionController.getPhenotypeBehavior(index));
//                }
            } else {
                panel.setVisible(false);
            }
        }


        // Do previous IEC populations exist?
        // --------------------------
        Back.setEnabled(poaiecSessionCtlr.previousStatesExist());

        // Do future IEC populations exist?
        // --------------------------
        Forward.setEnabled(poaiecSessionCtlr.futureStatesExist());

        // Update the controller with the current evolutionary options
        // --------------------------
        poaiecSessionCtlr.setNoveltyThresholdMultiplyer((double) noveltyThreshold.getValue());
        poaiecSessionCtlr.setStepMutationPower(stepMutationPower.getValue());
        poaiecSessionCtlr.setStepMutationRate(stepMutationRate.getValue());
        poaiecSessionCtlr.setFitnessEvaluationLimit(optimizeEvaluationLimit.getValue());
        poaiecSessionCtlr.setFitnessMutationRate(optimizeMutationRate.getValue());
//        poaiecSessionCtlr.setShowBehavior(ShowBehavior.isSelected());
//        poaiecSessionCtlr.setShowPath(ShowTrajectory.isSelected());

        // Notify the user if a solution was found
        // --------------------------
        notifyIfNewSolutionsFound();

        // Record the progress of the current evolutionary effort
        // --------------------------
        poaiecSessionCtlr.save();
    }

    private void notifyIfNewSolutionsFound() {
        List<Long> newSolutions = new ArrayList<Long>();
        for (int i = 0; i < candidatePanels.size(); i++) {
            if (poaiecSessionCtlr.hasCandidate(i)) {
                if (poaiecSessionCtlr.isCandidateSolution(i)) {
                    if (seenSolutions.contains(poaiecSessionCtlr.getCandidateId(i))) {
                        // Skip and Do Nothing
                    } else {
                        newSolutions.add(poaiecSessionCtlr.getCandidateId(i));
                    }
                }
            }
        }
        if (newSolutions.isEmpty()) {
            // Do Nothing
        } else if (newSolutions.size() == 1) {
            JOptionPane.showMessageDialog(this, "You found a solution to the maze (Agent " + newSolutions.get(0) + ").\nIf satisfied, please publish the solution.", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
        } else if (newSolutions.size() == 2) {
            JOptionPane.showMessageDialog(this, "You found two solutions to the maze (Agents " + newSolutions.get(0) + " & " + newSolutions.get(1) + ").\nIf satisfied, please publish one of the solutions.", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "You found several solution to the maze simultaniously.\nIf satisfied, please publish one of the solutions.", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
        }
        seenSolutions.addAll(newSolutions);
    }

    public void progressStarted() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void progressUpdate(int progress, int evaluations) {
        String message = String.format("Completed %d%% of task.", progress);
//      String message = String.format("Total Evaluations "+evaluations+"\n");
        progressMonitor.setNote(message);
        progressMonitor.setProgress(progress);
        if (progressMonitor.isCanceled()) {
            poaiecSessionCtlr.cancel();
        }
    }

    public void progressFinished() {
        poaiecSessionCtlr.recordEndOfStep();
        if (progressMonitor.isCanceled()) {
            poaiecSessionCtlr.back();
        }
        progressMonitor.close();
        Toolkit.getDefaultToolkit().beep();
        updateAllPanels();
        enableControlls();
    }
    private boolean running = false;

    private void enableControlls() {
        running = false;
    }

    private String populationBoarderTitle() {
        if (poaiecSessionCtlr != null) {
            int totalEvaluations = poaiecSessionCtlr.getTotalEvaluationCount();
            return "Population  (Evaluations: " + totalEvaluations + ")";
        } else {
            return "Population";
        }
    }

    private static final long serialVersionUID = 0x61c42a60;

    private void startPlayer() {
        playMngr.play();
        PlayPause.setIcon(icons.get("pause"));
        PlayPause.setToolTipText("Pause");
    }

    private void pausePlayer() {
        playMngr.pause();
        PlayPause.setIcon(icons.get("play"));
        PlayPause.setToolTipText("Play");
    }

    /**
     * Provides generic access for applications to add individual domain options to the interface.
     * @return The domain options panel.
     */
	public DomainOptionsPanel getDomainOptionsPanel() {
		return domainOptionsPanel1;
	}

	public OptimizeOptionsPanel getOptimizeOptionsPanel() {
		return optimizeOptionsPanel1;
	}
}