/**
 * * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.poaiecFramework.domain;

import com.anji.util.Configurable;

import edu.ucf.eplex.poaiecFramework.PoaiecSession;
import edu.ucf.eplex.poaiecFramework.controller.PoaiecController;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.RenderedImage;
import java.util.*;

import org.jgap.BehaviorVector;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.EvaluationFunction;

/**
 *
 * @param <Result> 
 * @see EvaluationFunction
 * @see NoveltyMetric
 *
 * @author Brian Woolley (brian.woolley at ieee.org)
 */
public abstract class EvaluationDomain<Result> implements Configurable {
    protected Map<Candidate, Result> allUserEvaluatedSubjects = new HashMap<Candidate, Result>();
    protected Map<Candidate, EvaluationPanel> evaluationPanels = new HashMap<Candidate, EvaluationPanel>();
    protected Map<EvaluationPanel, Result> simulations = new HashMap<EvaluationPanel, Result>();
    protected Map<Candidate, Result> population = new HashMap<Candidate, Result>();
    protected LinkedList<Candidate> subjects = new LinkedList<Candidate>();
    protected Map<Chromosome, Candidate> chromCandMap = new HashMap<Chromosome, Candidate>();
    private volatile int timeStep = 0;
    private PoaiecDomainProperties f_properties;
    protected PoaiecSession session = null;
    
    protected final boolean DEFAULT_VIEWER = false;
    protected boolean viewerEnabled = DEFAULT_VIEWER;
    public PoaiecController ctrl = null; // This is really a hack for automated search

    public EvaluationDomain() {}
    
    public EvaluationDomain(PoaiecDomainProperties props) {
        setDomainProperties(props);
    }
    
    public final void setCandidates(List<Candidate> candidates) {
        subjects.addAll(candidates);
        while (subjects.size() > 500) {
            subjects.removeFirst();
        }
                
        System.out.println("population size before == " + population.size());
        population.keySet().retainAll(subjects);
        System.out.println("population size after  == " + population.size());
        for (Candidate subject : candidates) {
            if (population.containsKey(subject)) {
                // do nothing
            } else {
                evaluate(subject);
            }
        }        
    }
    
    public Candidate getCandidate(Chromosome chrom) {
    	if (session == null) {
    		return ctrl.getCandidate(chrom); // Hack for automated search
    	}
    	return session.poaiecSessionCtlr.getCandidate(chrom);
    }

    /**
     * Sets the
     *
     * @param candidates 
     */
    public void setUserEvaluatedCandidates(List<Candidate> candidates) {
        subjects.removeAll(candidates);
        subjects.addAll(candidates);
        while (subjects.size() > 500) {
            subjects.removeFirst();
        }

        population.keySet().retainAll(subjects);
        simulations.clear();

        // assign each candidate to a panel, matching the order of candidates with panels
        for (int i = 0; i < candidates.size() && i < getRegisteredPanels().size(); i++) {
            Candidate subject = candidates.get(i);
            EvaluationPanel panel = getRegisteredPanels().get(i);
            evaluationPanels.put(subject, panel);

            if (!allUserEvaluatedSubjects.containsKey(subject)) {
                evaluate(subject);
                allUserEvaluatedSubjects.put(subject, population.get(subject));
            }
            
            Result result = allUserEvaluatedSubjects.get(subject);
            population.put(subject, result);
            simulations.put(panel, result);
        }
        setCurrentTimeStep(0);
    }
    
	/**
     * 
     */
    public void step() {
        timeStep++;
    }

    /**
     * 
     * @return 
     */
    public int getCurrentTimeStep() {
        return timeStep;
    }


    public void setCurrentTimeStep(int aNewTimeStep) {
        timeStep = aNewTimeStep;
    }
    
    /**
     *
     * @param panel 
     * @param g
     * @param size
     */
    public abstract void paintComponent(EvaluationPanel panel, Graphics g, Dimension size);

    /**
     *
     * @return
     */
    public PoaiecDomainProperties getProperties() {
        return f_properties;
    }

    /**
     * 
     * @param props 
     */
    public void setDomainProperties(PoaiecDomainProperties props) {
        assert(props != null);
        f_properties = props;        
    }
    
    /**
     *
     * @param simulationTimeStep
     */
    public abstract void gotoSimulationTimestep(int simulationTimeStep);

    /**
     *
     * @return
     */
    public abstract int getMaxSimulationTimesteps();

    /**
     *
     * @return
     */
    public abstract boolean atLastStep();

    /**
     *
     */
    private List<EvaluationPanel> f_panels = new ArrayList<EvaluationPanel>();

    /**
     * 
     * @param panel
     * @return 
     */
    public boolean removePanel(EvaluationPanel panel) {
        return f_panels.remove(panel);
    }

    /**
     * 
     * @param panel
     * @return 
     */
    public boolean registerPanel(EvaluationPanel panel) {
        return f_panels.add(panel);
    }

    /**
     * 
     * @return 
     */
    public List<EvaluationPanel> getRegisteredPanels() {
        return f_panels;
    }

    /**
     * 
     */
    public void repaintAllPanels() {
        for (EvaluationPanel panel : f_panels) {
            panel.repaint();
        }
    }    
    
    public Result getResult(Candidate chrom) {
    	return population.get(chrom);
    }
    
    /**
     * 
     * @param chrom
     */
    public abstract void evaluate(Candidate chrom);
    
    /**
     *
     * @return
     */
	public abstract Map<Candidate, BehaviorVector> getAllPointsVisited();

	public abstract int evaluateNovelty(Candidate candidate, boolean evaluatePopulation);
	
    /**
     * 
     * @return 
     */
    public abstract int getMaxFitnessValue();
    
    /**
     * 
     * @return 
     */
    public abstract double getNoveltyThreshold();
    
    /**
     * 
     * @param aNewNoveltyThreshold 
     */
    public abstract void setNoveltyThreshold(double aNewNoveltyThreshold);
    
    /**
     * Reports the current size of the novelty archive.
     *
     * @return The size of the novelty archive.
     */
    public abstract int getNoveltyArchiveSize();

    /**
     * 
     * @param subject The candidate behavior to render
     * @param size The size of the image to render
     * @return A sequence of phenotype behavior images
     */
    public abstract List<RenderedImage> getPhenotypeBehavior(Candidate subject, Dimension size);

    /**
     * 
     * @param size 
     * @return 
     */
    public abstract RenderedImage getPhenotypeBehaviorCurrent(Dimension size);
    
    /**
     * Must add all of the evaluation functions to the config
     */
    public abstract void addEvaluationFunctions(Configuration config);

    /**
     * Used to return the subset of functions used for optimization
     * @return
     */
	public abstract List<EvaluationFunction> getOptimizationFunctions();

}
