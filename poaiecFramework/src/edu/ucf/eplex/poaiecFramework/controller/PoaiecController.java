/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.poaiecFramework.controller;

import com.anji.Copyright;
import com.anji.hyperneat.HyperNeatConfiguration;
import com.anji.integration.LogEventListener;
import com.anji.integration.PersistenceEventListener;
import com.anji.neat.NeatConfiguration;
import com.anji.persistence.Persistence;
import com.anji.run.Run;
import com.anji.util.Properties;
import com.anji.util.Reset;

import edu.ucf.eplex.poaiecFramework.domain.Candidate;
import edu.ucf.eplex.poaiecFramework.domain.EvaluationDomain;

import java.awt.Dimension;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jgap.*;
import org.jgap.event.GeneticEvent;

/**
 *
 * @author Brian Woolley (brian.woolley at ieee.org)
 */
public class PoaiecController {

	private static final String RUN_KEY = "run.name";
	private final String RESET_KEY = "run.reset";
	private Properties props;
	private NeatConfiguration config;
	private Genotype genotype;
	private Persistence db = null;
	private final EvaluationDomain<?> f_domain;
	private PoaiecRun history;
	private final long UUID = 2013L;
	/**
     *
     */
	private Map<Chromosome, Candidate> candidates = new HashMap<Chromosome, Candidate>();

	public PoaiecController(EvaluationDomain<?> domain) {
		assert (domain != null);
		f_domain = domain;
		history = new PoaiecRun(f_domain.getProperties().getProperty(RUN_KEY));

		System.out.println(Copyright.STRING);

		try {
			props = f_domain.getProperties();

			boolean doReset = props.getBooleanProperty(RESET_KEY, false);
			if (doReset) {
				logger.warning("Resetting previous run !!!");
				Reset resetter = new Reset(props);
				resetter.setUserInteraction(false);
				resetter.reset();
			}

			String encoding = props.getProperty("encoding", "neat");
			if (encoding.equals("hyperneat")) {
				config = new HyperNeatConfiguration(props);
			} else {
				config = new NeatConfiguration(props);
			}
			System.out.println("Created config object...");

			// peristence
			db = (Persistence) props
					.singletonObjectProperty(Persistence.PERSISTENCE_CLASS_KEY);

			// run
			Run run = (Run) props.singletonObjectProperty(Run.class);
			db.startRun(run.getName());
			config.getEventManager().addEventListener(
					GeneticEvent.GENOTYPE_EVALUATED_EVENT, run);

			// logging
			LogEventListener logListener = new LogEventListener(config);
			config.getEventManager().addEventListener(
					GeneticEvent.GENOTYPE_EVOLVED_EVENT, logListener);
			config.getEventManager().addEventListener(
					GeneticEvent.GENOTYPE_EVALUATED_EVENT, logListener);

			// persistence
			PersistenceEventListener dbListener = new PersistenceEventListener(
					config, run);
			dbListener.init(props);
			config.getEventManager().addEventListener(
					GeneticEvent.GENOTYPE_START_GENETIC_OPERATORS_EVENT,
					dbListener);
			config.getEventManager().addEventListener(
					GeneticEvent.GENOTYPE_FINISH_GENETIC_OPERATORS_EVENT,
					dbListener);
			config.getEventManager().addEventListener(
					GeneticEvent.GENOTYPE_EVALUATED_EVENT, dbListener);
			config.getEventManager().addEventListener(
					GeneticEvent.RUN_COMPLETED_EVENT, dbListener);

			// fitness function
			f_domain.addEvaluationFunctions(config);

			f_domain.init(props);

			genotype = Genotype.emptyInitialGenotype(config);
			System.out.println("Created an empty genotype...");

		} catch (Exception ex) {
			Logger.getLogger(PoaiecController.class.getName()).log(Level.SEVERE,
					"IECsessionBean ctr failure: ", ex);
		}
	}

	public void back() {
		System.out.println("in iecController.back()");
		genotype.setChromosomes(history.back());
		System.out.println("genotype updated...");
		System.out.println("setting candidates in the evaluationDomain...");
		f_domain.setUserEvaluatedCandidates(getCandidates());
		System.out.println("set candidates in the evalationDomain");
	}

	public void forward() {
		genotype.setChromosomes(history.forward());
		f_domain.setUserEvaluatedCandidates(getCandidates());
	}

	public void doStep() {
		System.out.println("in controller.doStep()");
		// Create a new IEC population based on user selections
		// -------------------------------------
		history.startStepFunction(genotype, f_domain);
		genotype.clearStepCounter();
		genotype.doIECstep();
		System.out.println("end of controller.doStep()");
	}

	public void doNovelty() {
		System.out.println("in controller.doNovelty()");
		history.startNoveltyFunction(genotype, f_domain);
		genotype.clearStepCounter();
		genotype.doShortTermNoveltySearch();
		System.out.println("end of controller.doNovelty()");
	}
	
	public void doPareto() {
		System.out.println("in controller.doPareto()");
		history.startParetoFunction(genotype, f_domain);
		genotype.clearStepCounter();
		genotype.doShortTermParetoOptimization();
		System.out.println("end of controller.doPareto()");
	}

	public void optimize(int index) {
		System.out.println("in controller.doOptimize()");
		history.startOptimizeFunction(genotype, f_domain);
		genotype.clearStepCounter();
		genotype.doShortTermOptimization(f_domain.getOptimizationFunctions());
		System.out.println("end of controller.doOptimize()");
	}

	/**
     *
     */
	public void save() {
		// throw new
		// UnsupportedOperationException("The SAVE function is currently undefined.");
	}

	/**
	 *
	 * @param index
	 */
	public void publish(int index) {
		Dimension size = new Dimension(512, 512);
		Candidate subject = getCandidate(index);

		try {
			for (RenderedImage image : f_domain.getPhenotypeBehavior(subject,
					size)) {
				// renderer.applyBackground(bi.getGraphics(), size,
				// Color.WHITE);
				db.store(image, subject.getId());
			}
		} catch (IOException ex) {
			Logger.getLogger(PoaiecController.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	public final long getCandidateId(int index) {
		return getCandidate(index).getId();
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	public final boolean isCandidateSolution(int index) {
		return getCandidate(index).isSolution();
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	public final boolean isCandidateSelected(int index) {
		return getCandidate(index).isSelectedByTheUser();
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	public final HashMap<Long, Integer> getCandidateFitness(int index) {
		return getCandidate(index).getFitnessValues();
	}
	
	public final int getCandidateDominates(int index) {
		return getCandidate(index).getDominates();
	}
	
	public final int getCandidateDominatedBy(int index) {
		return getCandidate(index).getDominatedBy();
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	public final long getCandidateNovelty(int index) {
		return getCandidate(index).getNoveltyValue();
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	public boolean toggelCandidateIsSelected(int index) {
		Candidate subject = getCandidate(index);
		subject.toggelSelectedByTheUser();
		return subject.isSelectedByTheUser();
	}

	/**
     *
     */
	private Chromosome getChromosome(int index) {
		return getChromosomes().get(index);
	}

	/**
	 *
	 * @return
	 */
	public List<Chromosome> getChromosomes() {
		return genotype.getChromosomes();
	}

	// public Environment getPhenotypeBehavior(int index) {
	// return getPhenotypeBehavior(getChromosome(index));
	// }
	//
	// public Environment getPhenotypeBehavior(Chromosome subject) {
	// // Build ANN Behavior from subject.
	// // ------------------------------
	// Behavior phenotype = new ANN_Behavior(subject);
	// Environment env = new Environment(phenotype, NoveltySearch.getMaze());
	//
	// // Evaluate over x timesteps (or until distToGoal <= 5)
	// for (int i = 0; i < 400; i++) {
	// env.step();
	// if (env.distToGoal() <= goalThreshold) {
	// break;
	// }
	// }
	// return env;
	// }
	// public Collection<Position> getAllPts() {
	// return noveltyFunction.getAllPts();
	// }
	// public Collection<Position> getArchivedBehaviors() {
	// return noveltyFunction.getArchivedBehaviors();
	// }
	private final Logger logger = Logger.getLogger(Genotype.class.getName());

	/**
	 *
	 * @return
	 */
	public boolean previousStatesExist() {
		return history.previousStatesExist();
	}

	/**
	 *
	 * @return
	 */
	public boolean futureStatesExist() {
		return history.futureStatesExist();
	}

	/**
	 *
	 * @param value
	 */
	public void setNoveltyThresholdMultiplyer(double value) {
		genotype.setIECnoveltyThresholdMultiplyer(value);
	}

	public void toggleDynamicPOPV() {
		config.setDynamicPOPV(!config.isDynamicPOPV());
	}
	
	/**
     *
     */
	public void cancel() {
		genotype.cancel();
	}

	/**
	 *
	 * @param aProgressListener
	 */
	public void addProgressListener(ProgressListener aProgressListener) {
		genotype.registerProgressListener(aProgressListener);
	}

	/**
     *
     */
	public void recordEndOfStep() {
		try {
			f_domain.setUserEvaluatedCandidates(getCandidates());
			// f_domain.evaluateFitness(getCandidates());
			// f_domain.evaluateNovelty(getCandidates());
			history.recordCurrentSessionState(genotype, f_domain);
			db.storeXml(history);
			System.out.println("end of controller.recordEndOfStep()");
		} catch (IOException ex) {
			Logger.getLogger(PoaiecController.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	/**
	 *
	 * @return
	 */
	public int getUserEvaluationCount() {
		return history.getSeriesUserActionCount();
	}

	/**
	 *
	 * @return
	 */
	public int getTotalEvaluationCount() {
		return genotype.getSeriesEvaluationCount();
	}

	/**
	 *
	 * @param value
	 */
	public void setStepMutationPower(int value) {
		value = Math.min(Math.max(value, 0), 100);
		float originalMutationPower = props
				.getFloatProperty("weight.mutation.std.dev");
		float iecMutationRate = (float) Math.pow(value / 100f, 2)
				* originalMutationPower;
		genotype.setIECmutationRate(iecMutationRate);
	}

	/**
	 *
	 * @param value
	 */
	public void setStepMutationRate(int value) {
		value = Math.min(Math.max(value, 0), 100);
		float iecMutationRate = (float) Math.pow(value / 100f, 2);
		genotype.setIECmutationRate(iecMutationRate);
	}

	/**
	 *
	 * @param value
	 */
	public void setFitnessEvaluationLimit(int value) {
		try {
			config.setFitnessSearchLimit(value);
		} catch (InvalidConfigurationException ex) {
		}
	}

	/**
	 *
	 * @param value
	 */
	public void setFitnessMutationRate(int value) {
		value = Math.min(Math.max(value, 0), 100);
		float originalMutationPower = props
				.getFloatProperty("weight.mutation.std.dev");
		float fitnessMutation = (float) Math.pow(value / 100f, 2);
		genotype.setFitnessMutationPower(fitnessMutation
				* originalMutationPower);
		genotype.setFitnessMutationRate(fitnessMutation);
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	public boolean hasCandidate(int index) {
		if (index < genotype.getGenotypeSize()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *
	 * @return
	 */
	public int getIECpopulationSize() {
		return genotype.getGenotypeSize();
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	public Candidate getCandidate(int index) {
		Chromosome chrom = getChromosome(index);
		return getCandidate(chrom);
	}

	/**
	 *
	 * @param subject
	 * @return
	 */
	public Candidate getCandidate(Chromosome chrom) {
		if (!candidates.containsKey(chrom)) {
			candidates.put(chrom, new Candidate(chrom, config));
		}
		return candidates.get(chrom);
	}

	/**
	 *
	 * @return
	 */
	public List<Candidate> getCandidates() {
		return getCandidates(getChromosomes());
	}

	/**
	 *
	 * @param someChroms
	 * @return
	 */
	private List<Candidate> getCandidates(List<Chromosome> someChroms) {
		List<Candidate> retVal = new ArrayList<Candidate>();
		for (Chromosome chrom : someChroms) {
			retVal.add(getCandidate(chrom));
		}
		return retVal;
	}

	public int getMaxFitnessValue() {
		return f_domain.getMaxFitnessValue();
	}

	public double getNoveltyThreshold() {
		return f_domain.getNoveltyThreshold();
	}

	public void setNoveltyThreshold(double aNewNoveltyThreshold) {
		f_domain.setNoveltyThreshold(aNewNoveltyThreshold);
	}

	public int getNoveltyArchiveSize() {
		return f_domain.getNoveltyArchiveSize();
	}

	public Map<Chromosome, BehaviorVector> getAllPointsVisited() {
		Map<Chromosome, BehaviorVector> result = new HashMap<Chromosome, BehaviorVector>();
		Map<Candidate, BehaviorVector> original = f_domain
				.getAllPointsVisited();

		for (Candidate candidate : original.keySet()) {
			result.put(candidate.getChromosome(), original.get(candidate));
		}
		return result;
	}

	public long getEvaluationFunctionUUID() {
		return UUID;
	}

}