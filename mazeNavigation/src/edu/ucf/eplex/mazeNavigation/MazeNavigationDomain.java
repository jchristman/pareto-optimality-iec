/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation;

import edu.ucf.eplex.mazeNavigation.behaviorFramework.ANN_Behavior;
import edu.ucf.eplex.mazeNavigation.behaviorFramework.Behavior;
import edu.ucf.eplex.mazeNavigation.evaluationFunctions.EndpointDistanceToGoalHeuristic;
import edu.ucf.eplex.mazeNavigation.evaluationFunctions.FitnessStrategy;
import edu.ucf.eplex.mazeNavigation.evaluationFunctions.LongestPathHeuristic;
import edu.ucf.eplex.mazeNavigation.evaluationFunctions.PathAreaHeuristic;
import edu.ucf.eplex.mazeNavigation.evaluationFunctions.PathSmoothnessHeuristic;
import edu.ucf.eplex.mazeNavigation.evaluationFunctions.RobotSpeedHeuristic;
import edu.ucf.eplex.mazeNavigation.gui.EvolutionPanel;
import edu.ucf.eplex.mazeNavigation.model.Environment;
import edu.ucf.eplex.mazeNavigation.model.Maze;
import edu.ucf.eplex.mazeNavigation.model.Path;
import edu.ucf.eplex.mazeNavigation.model.Position;
import edu.ucf.eplex.mazeNavigation.util.MazeRenderingTool;
import edu.ucf.eplex.poaiecFramework.PoaiecSession;
import edu.ucf.eplex.poaiecFramework.domain.*;

import com.anji.util.Properties;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.*;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JCheckBox;
import javax.swing.LayoutStyle;

import org.jgap.BehaviorVector;
import org.jgap.Configuration;
import org.jgap.EvaluationFunction;
import org.jgap.InvalidConfigurationException;

/**
 *
 * @author Brian Woolley (brian.woolley at ieee.org)
 */
public class MazeNavigationDomain extends EvaluationDomain<Path> {

    /**
     * @param args the command line arguments
     * @throws IOException 
     */
    public static void main(final String args[]) throws IOException {
    	MazeDomainProperties props;
    	if (args.length == 1) {
    		props = new MazeDomainProperties("./properties/" + args[0]);
    	} else {
    		props = new MazeDomainProperties();
    	}
    	
    	new MazeNavigationDomain(props);
    	// TODO  setup domain
    	// TODO  launch the NA-IEC session window 
    }

    private Map<Candidate, BehaviorVector> allPoints;
    private MazeDomainNoveltyMetric noveltyMetric;
    private Map<Candidate, Integer> noveltyScores = new HashMap<Candidate, Integer>();
    private MazeRenderingTool render = new MazeRenderingTool();

    public MazeNavigationDomain() {
        super();
    }
    
    public MazeNavigationDomain(PoaiecDomainProperties props) {
        setDomainProperties(props);
        session = new PoaiecSession(this);
        setDomainOptions(session.getDomainOptionsPanel());
        setOptimizeOptions(session.getOptimizeOptionsPanel());
    }
    
	@Override
    public final void setDomainProperties(PoaiecDomainProperties props) {
        super.setDomainProperties(props);
        init(getProperties());
    }

    @SuppressWarnings("unused")
	private Collection<Position> getPopulationPoints() {
        Collection<Position> results = new HashSet<Position>();
        for (Path path : population.values()) {
            results.add(path.getLast());
        }
        return results;
    }

    @SuppressWarnings("unused")
	private Collection<Position> getArchivePoints() {
        Collection<Position> results = new HashSet<Position>();
        for (Path path : noveltyMetric.getArchivedBehaviors()) {
            results.add(path.getLast());
        }
        return results;
    }

    @Override
    public void paintComponent(EvaluationPanel panel, Graphics g, Dimension size) {
        if (simulations.containsKey(panel)) {
            Path path = simulations.get(panel);
            render.renderEnvironment(getMaze(), path, getCurrentTimeStep(), g, size);
        }
    }

    @Override
    public void gotoSimulationTimestep(int simulationTimeStep) {
        simulationTimeStep = Math.max(Math.min(simulationTimeStep, getMaxSimulationTimesteps()), 0);
        setCurrentTimeStep(simulationTimeStep);
    }

    @Override
    public int getMaxSimulationTimesteps() {
        return maxTimesteps;
    }

    @Override
    public boolean atLastStep() {
        return getCurrentTimeStep() >= getMaxSimulationTimesteps();
    }

    @Override
    public void evaluate(Candidate subject) {
        assert (subject != null);

        if (population.containsKey(subject)) {
            return;
        }

        // Build ANN Behavior from Chrom
        Behavior phenotype = new ANN_Behavior(subject);
        Environment env = new Environment(phenotype, getMaze());

        // Evaluate over x timesteps (or until distToGoal <= 5)
        for (int i = 0; i < maxTimesteps; i++) {
            env.step();
            if (env.distToGoal() <= goalThreshold) {
                System.out.println("<--------------------MAZE SOLVED!  GOAL FOUND BY CHROMOSOME " + subject.getId() + "!-------------------->");
                subject.setAsSolution(true);
//                for (Position pt : env.getPath()) {
//                    System.out.println("breadCrumbs.add(new Position(" + Math.round(pt.getX()) + ", " + Math.round(pt.getY()) + ", 0));");
//                }
                break;
            }
        }
        population.put(subject, env.getPath());
        allPoints.put(subject, new BehaviorVector(env.getPath().getLast().toArray()));
//        evaluateFitness(subject);
//        observationPoint.notifyObservers(getPopulationPoints(), getArchivePoints());
    }

	@Override
	public Map<Candidate, BehaviorVector> getAllPointsVisited() {
		return allPoints;
	}

	@Override
	public int evaluateNovelty(Candidate candidate, boolean evaluatePopulation) {
		if (evaluatePopulation)
			noveltyScores = noveltyMetric.score(population);
		return noveltyScores.get(candidate);
	}

    @Override
    public int getMaxFitnessValue() {
        return FitnessStrategy.staticGetMaxFitnessValue();
    }

    @Override
    public double getNoveltyThreshold() {
        return noveltyMetric.getNoveltyThreshold();
    }

    @Override
    public void setNoveltyThreshold(double aNewNoveltyThreshold) {
        noveltyMetric.setNoveltyThreshold(aNewNoveltyThreshold);
    }

    @Override
    public int getNoveltyArchiveSize() {
        return noveltyMetric.getArchiveSize();
    }

    @Override
    public List<RenderedImage> getPhenotypeBehavior(Candidate subject, Dimension size) {
    	List<RenderedImage> phenotypeBehaviors = new ArrayList<RenderedImage>();
    	
    	if (!population.containsKey(subject)) {
    		evaluate(subject);
    	} 
    	
		Path result = population.get(subject);
		MazeRenderingTool renderer = new MazeRenderingTool();
		
		BufferedImage bi;
        for (Position pose : result) {
            bi = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
            renderer.applyBackground(bi.getGraphics(), size, Color.WHITE);
            renderer.renderEnvironment(getMaze(), pose, result, bi.getGraphics(), size);
            phenotypeBehaviors.add(bi);
        }
        return phenotypeBehaviors;
    }

    @Override
    public RenderedImage getPhenotypeBehaviorCurrent(Dimension size) {
        EvolutionPanel panel = new EvolutionPanel();
        panel.setSize(size);
        BufferedImage bi = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        panel.paintComponent(bi.getGraphics());
        return bi;
    }

    public void init(Properties props) {
    	if (props == null) {
            System.out.println("WARN:  Property object is NULL!");
        }
        mazeType = props.getProperty(MAZE_TYPE_KEY, DEFAULT_MAZE_TYPE);
        maxTimesteps = props.getIntProperty(TIMESTEPS_KEY, DEFAULT_TIMESTEPS);
        goalThreshold = props.getIntProperty(GOAL_THRESHOLD_KEY, DEFAULT_GOAL_THRESHOLD);

        fitnessFunctions = getFitnessFunctions();
        optimizeFunctions = new LinkedList<EvaluationFunction>();
        for (FitnessStrategy func : fitnessFunctions) {
        	if (func.getEvaluationFunctionUUID() == 9000)
        		optimizeFunctions.add(func);
        }
        
        viewerEnabled = props.getBooleanProperty(VIEWER_KEY, DEFAULT_VIEWER);

        noveltyMetric = new MazeDomainNoveltyMetric(props);
        population = new HashMap<Candidate, Path>();
        simulations = new HashMap<EvaluationPanel, Path>();
        allPoints = new HashMap<Candidate, BehaviorVector>();
    }
    private final static String MAZE_TYPE_KEY = "mazeDomain.map";
    private final static String DEFAULT_MAZE_TYPE = "medium.map";
    private static String mazeType = DEFAULT_MAZE_TYPE;

    /**
     *
     * The values:
     * <code>medium.map</code> and
     * <code>hard.map</code> are currently supported.
     *
     * @return
     */
    public Maze getMaze() {
        if (mazeType.equals("medium.map")) {
            return Maze.getMediumMap();
        } else if (mazeType.equals("hard.map")) {
            return Maze.getHardMap();
        } else {
            return Maze.getMediumMap();
        }
    }
    private final String TIMESTEPS_KEY = "mazeDomain.timesteps";
    private final int DEFAULT_TIMESTEPS = 1000;
    private int maxTimesteps = DEFAULT_TIMESTEPS;

    /**
     * The maximum number of timesteps in each trial. Trials end early when the
     * goal is reached Set by property
     * <code>mazeDomain.timesteps</code>.
     *
     * @return The maximum duration of each trial.
     */
    public int getMaxTimesteps() {
        return maxTimesteps;
    }
    private final String GOAL_THRESHOLD_KEY = "mazeDomain.goalThreshold";
    private final int DEFAULT_GOAL_THRESHOLD = 5;
    private int goalThreshold = DEFAULT_GOAL_THRESHOLD;

    /**
     * The maximum number of timesteps in each trial. Trials end early when the
     * goal is reached Set by property
     * <code>mazeDomain.timesteps</code>.
     *
     * @return The maximum duration of each trial.
     */
    public int getGoalThreshold() {
        return goalThreshold;
    }
    private List<FitnessStrategy> fitnessFunctions = getFitnessFunctions();
    private List<EvaluationFunction> optimizeFunctions = new LinkedList<EvaluationFunction>();

    public List<FitnessStrategy> getFitnessFunctions() {
    	List<FitnessStrategy> funcs = new LinkedList<FitnessStrategy>();
 
    	funcs.add(new EndpointDistanceToGoalHeuristic(getMaze(), this));
        funcs.add(new LongestPathHeuristic(getMaze(), this));
        funcs.add(new PathSmoothnessHeuristic(getMaze(), this));
        funcs.add(new PathAreaHeuristic(getMaze(), this));
        //funcs.add(new PathCenteringHeuristic(getMaze(), this));
        funcs.add(new RobotSpeedHeuristic(getMaze(), this));
        //funcs.add(new ConsistentRobotSpeedHeuristic(getMaze(), this));
        
        return funcs;
    }    

    /****************************/
    private void checkBoxActionPerformed(ActionEvent evt) {
    	String funcShortName = evt.getActionCommand();
    	for (FitnessStrategy func : fitnessFunctions) {
    		if (funcShortName.equals(func.shortName())) {
    			if (((JCheckBox)evt.getSource()).isSelected()) {
    				optimizeFunctions.add(func);
    			} else {
    				optimizeFunctions.remove(func);
    			}
    		}
    	}
    }
    
    
    private void setOptimizeOptions(OptimizeOptionsPanel options) {
	    GroupLayout optimizeOptionsPanel1Layout = new GroupLayout(options);
	    options.setLayout(optimizeOptionsPanel1Layout);
	    SequentialGroup horizGroup = optimizeOptionsPanel1Layout.createSequentialGroup();
    	ParallelGroup vertGroup = optimizeOptionsPanel1Layout.createParallelGroup();
    	
	    for (FitnessStrategy func : fitnessFunctions) {
    		JCheckBox funcBox = new javax.swing.JCheckBox();
            funcBox.setSelected(false);
            if (func.getEvaluationFunctionUUID() == 9000)
            	funcBox.setSelected(true);
    		funcBox.setText(func.shortName());
    		funcBox.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent evt) {
    				checkBoxActionPerformed(evt);
    			}
    		});
    		funcBox.setEnabled(false);
    		
		    horizGroup.addGroup(
		    		optimizeOptionsPanel1Layout.createSequentialGroup()
		    		.addComponent(funcBox).addContainerGap());
		    vertGroup.addComponent(funcBox);
    	}
	    optimizeOptionsPanel1Layout.setHorizontalGroup(
	    		optimizeOptionsPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	    		.addGroup(optimizeOptionsPanel1Layout.createSequentialGroup()
	    				.addContainerGap()
	    				.addGroup(horizGroup)
	    				.addContainerGap(531, Short.MAX_VALUE)));
        optimizeOptionsPanel1Layout.setVerticalGroup(
        		optimizeOptionsPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
        		.addGroup(vertGroup)
        		);
    }

    /****************************/
    private JCheckBox showRobotBehavior;
    private void ShowBehaviorActionPerformed(ActionEvent evt) {
        render.setShowRobot(showRobotBehavior.isSelected());
    }

    private JCheckBox showPath;
    private void ShowTrajectoryActionPerformed(ActionEvent evt) {
        render.setShowPath(showPath.isSelected());
    }
    
    private void setDomainOptions(DomainOptionsPanel options) {
        showRobotBehavior = new javax.swing.JCheckBox();
        showPath = new javax.swing.JCheckBox();
        
        showRobotBehavior.setSelected(true);
        showRobotBehavior.setText("Show Robot Behavior");
        showRobotBehavior.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		ShowBehaviorActionPerformed(evt);
        	}
        });
        showRobotBehavior.setEnabled(false);

        showPath.setSelected(true);
        showPath.setText("Show Robot Path");
        showPath.setToolTipText("Show Robot Path");
        showPath.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		ShowTrajectoryActionPerformed(evt);
        	}
        });
        showPath.setEnabled(false);

        GroupLayout domainOptionsPanel1Layout = new GroupLayout(options);
        options.setLayout(domainOptionsPanel1Layout);
        domainOptionsPanel1Layout.setHorizontalGroup(
        		domainOptionsPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
        		.addGroup(domainOptionsPanel1Layout.createSequentialGroup()
        				.addContainerGap()
        				.addGroup(domainOptionsPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
        						.addComponent(showRobotBehavior)
        						.addComponent(showPath))
        						.addContainerGap(531, Short.MAX_VALUE))
        		);
        domainOptionsPanel1Layout.setVerticalGroup(
        		domainOptionsPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
        		.addGroup(domainOptionsPanel1Layout.createSequentialGroup()
        				.addComponent(showRobotBehavior)
        				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addComponent(showPath)
        				.addContainerGap())
        		);
	}

    /****************************/
    private final String VIEWER_KEY = "mazeDomain.enableViwer";

	@Override
	public void addEvaluationFunctions(Configuration config) {
		try {
			for (FitnessStrategy fitnessFunction : fitnessFunctions)
			config.addEvaluationFunction(fitnessFunction, 0.5);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<EvaluationFunction> getOptimizationFunctions() {
		return optimizeFunctions;
	}

}
