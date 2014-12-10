/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucf.eplex.poaiecFramework;

import java.awt.Dimension;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jgap.BehaviorVector;
import org.jgap.Chromosome;
import org.jgap.Genotype;
import org.jgap.InvalidConfigurationException;
import org.jgap.PopulationListener;
import org.jgap.event.GeneticEvent;

import com.anji.Copyright;
import com.anji.hyperneat.HyperNeatConfiguration;
import com.anji.integration.LogEventListener;
import com.anji.integration.PersistenceEventListener;
import com.anji.integration.PresentationEventListener;
import com.anji.neat.NeatChromosomeUtility;
import com.anji.neat.NeatConfiguration;
import com.anji.neat.NeuronType;
import com.anji.persistence.Persistence;
import com.anji.run.Run;
import com.anji.util.Properties;
import com.anji.util.Reset;

import edu.ucf.eplex.poaiecFramework.domain.Candidate;
import edu.ucf.eplex.poaiecFramework.domain.EvaluationDomain;
import edu.ucf.eplex.poaiecFramework.domain.PoaiecDomainProperties;

/**
 *
 * @author Brian Woolley <brian.woolley at ieee.org>
 */
public class Evolver implements PopulationListener {

    /**
     * properties key, target fitness value - after reaching this run will halt
     */
    public final String FITNESS_TARGET_KEY = "fitness.target";
    private final String FITNESS_THRESHOLD_KEY = "fitness.threshold";
    private final String EVOLUTION_MODE_NOVELTY_KEY = "evaluation.mode";
    private final String BEHAVIOR_SPACE_KEY = "naiec.behavior.space";
    private final String BEHAVIOR_SPACE_RECORD_KEY = BEHAVIOR_SPACE_KEY + ".record";
    private final String BEHAVIOR_SPACE_SIZE_X = BEHAVIOR_SPACE_KEY + ".size.x";
    private final String BEHAVIOR_SPACE_SIZE_Y = BEHAVIOR_SPACE_KEY + ".size.y";
    private final boolean DEFAULT_RECORD_BEHAVIOR_SPACE = false;
    private static final Logger logger = Logger.getLogger("NoveltyEvolver");
    /**
     * properties key, # generations in run
     */
    public final String NUM_GENERATIONS_KEY = "num.generations";
    private final String PRESENTATION_ACTIVE_KEY = "presentation.active";
    private final String RESET_KEY = "run.reset";

    /**
     * Log summary data of run including generation in which the first solution
     * occurred, and the champion of the final generation.
     *
     * @param generationOfFirstSolution
     * @param champ
     */
    private void logConclusion(int generationOfFirstSolution, Chromosome champ) {
        logger.log(Level.INFO, "generation of first solution == {0}", generationOfFirstSolution);
        logger.log(Level.INFO, "champ # connections == {0}", NeatChromosomeUtility.getConnectionList(champ.getAlleles()).size());
        logger.log(Level.INFO, "champ # hidden nodes == {0}", NeatChromosomeUtility.getNeuronList(champ.getAlleles(), NeuronType.HIDDEN).size());
    }

    /**
     * Main program used to perform an evolutionary run.
     *
     * @param args command line arguments; args[0] used as properties file
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws InvalidConfigurationException
     * @throws Exception
     */
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InvalidConfigurationException, Exception {
        System.out.println(Copyright.STRING);

        if (args.length != 2) {
            usage();
            System.exit(-1);
        }

        // Load properties from the properties file specified by args[1]
        File propertiesFile = new File(args[1]);
        if (propertiesFile.exists()) {
            logger.info("Located the properties file: " + args[1]);
        } else {
            logger.warning("Could not locate the properties file: " + args[1]);
        }
        PoaiecDomainProperties props = new PoaiecDomainProperties(args[1]);

        Class<?> aClass = Class.forName(args[0]);
        Object o = aClass.newInstance();
        if (o instanceof EvaluationDomain) {
            EvaluationDomain<?> domain = (EvaluationDomain<?>) o;
            domain.setDomainProperties(props);
            new Evolver(domain).run();
            System.exit(0);
        } else {
            logger.log(Level.WARNING, "The class: {0} was not an instance of EvaluationDomain", args[0]);
            System.exit(-1);
        }
    }

    /**
     * command line usage
     */
    private static void usage() {
        System.err.println("usage: <cmd> <evaluationDomain-class> <properties-file>");
    }
    private final EvaluationDomain<?> f_domain;
    private boolean noveltySearch = true;
    private boolean recordBehaviorSpace = DEFAULT_RECORD_BEHAVIOR_SPACE;
    private Dimension behaviorSpaceSize = null;
    private NeatConfiguration config = null;
    private Chromosome fitnessChamp = null;
    private Persistence db = null;
    private Genotype genotype = null;
    private int domainMaxFitness;
    private int numEvolutions = 0;
    private double targetFitness = 0.0d;
    private double thresholdFitness = 0.0d;

    public Evolver(EvaluationDomain<?> domain) throws IOException, InvalidConfigurationException {

        assert (domain != null);
        f_domain = domain;
        init(f_domain.getProperties());

    }

    /**
     * @return champion of last generation
     */
    public Chromosome getChamp() {
        return fitnessChamp;
    }

    /**
     * Fitness of current champ, 0 ... 1
     *
     * @return maximum fitness value
     */
    public HashMap<Long, Double> getChampAdjustedFitness() {
    	if (fitnessChamp == null) {
    		return null;
    	} else {
    		return adjustFitnessToPop(fitnessChamp);
    	}
    }
    
    private HashMap<Long, Double> adjustFitnessToPop(Chromosome chrom) {
		HashMap<Long,Double> adjusted = new HashMap<Long,Double>();
		for (Entry<Long,Integer> entry : chrom.getFitnessValues().entrySet()) {
			adjusted.put(entry.getKey(), entry.getValue().doubleValue() / 
					(double) config.getBulkFitnessFunctions().get(entry.getKey()).getMaxFitnessValue());
		}
		
		return adjusted;
    }
    
    private HashMap<Long, Double> adjustFitnessToDomain(Chromosome chrom) {
		HashMap<Long,Double> adjusted = new HashMap<Long,Double>();
		for (Entry<Long,Integer> entry : chrom.getFitnessValues().entrySet()) {
			adjusted.put(entry.getKey(), entry.getValue().doubleValue() / 
					(double) domainMaxFitness);
		}
		
		return adjusted;
    }

    /**
     * @return target fitness value, 0 ... 1
     */
    public double getTargetFitness() {
        return targetFitness;
    }

    /**
     * @return threshold fitness value, 0 ... 1
     */
    public double getThresholdFitness() {
        return thresholdFitness;
    }

    /**
     * Construct new evolver with given properties. See <a href="
     * {@docRoot}/params.htm" target="anji_params">Parameter Details </a> for
     * specific property settings.
     *
     * @param props
     * @throws IOException
     * @throws InvalidConfigurationException
     * @see com.anji.util.Configurable#init(com.anji.util.Properties)
     */
    private void init(Properties props) throws IOException, InvalidConfigurationException {

        boolean doReset = props.getBooleanProperty(RESET_KEY, false);
        if (doReset) {
            logger.warning("Resetting previous run !!!");
            Reset resetter = new Reset(props);
            resetter.setUserInteraction(false);
            resetter.reset();
        }

        if (props.getProperty("encoding").equals("hyperneat")) {
            config = new HyperNeatConfiguration(props);
        } else {
            config = new NeatConfiguration(props);
        }

        // peristence
        db = (Persistence) props.singletonObjectProperty(Persistence.PERSISTENCE_CLASS_KEY);

        numEvolutions = props.getIntProperty(NUM_GENERATIONS_KEY);
        noveltySearch = setEvolutionMode(props.getProperty(EVOLUTION_MODE_NOVELTY_KEY));
        recordBehaviorSpace = props.getBooleanProperty(BEHAVIOR_SPACE_RECORD_KEY, DEFAULT_RECORD_BEHAVIOR_SPACE);
        if (recordBehaviorSpace) {
            int width = props.getIntProperty(BEHAVIOR_SPACE_SIZE_X, 512);
            int height = props.getIntProperty(BEHAVIOR_SPACE_SIZE_Y, 512);
            behaviorSpaceSize = new Dimension(width, height);
        }
        targetFitness = props.getDoubleProperty(FITNESS_TARGET_KEY, 1.0d);
        thresholdFitness = props.getDoubleProperty(FITNESS_THRESHOLD_KEY, targetFitness);

        //
        // event listeners
        //

        // run
        Run run = (Run) props.singletonObjectProperty(Run.class);
        db.startRun(run.getName());
        config.getEventManager().addEventListener(GeneticEvent.GENOTYPE_EVALUATED_EVENT, run);

        // logging
        LogEventListener logListener = new LogEventListener(config);
        config.getEventManager().addEventListener(GeneticEvent.GENOTYPE_EVOLVED_EVENT, logListener);
        config.getEventManager().addEventListener(GeneticEvent.GENOTYPE_EVALUATED_EVENT, logListener);

        // persistence
        PersistenceEventListener dbListener = new PersistenceEventListener(config, run);
        dbListener.init(props);
        config.getEventManager().addEventListener(GeneticEvent.GENOTYPE_START_GENETIC_OPERATORS_EVENT, dbListener);
        config.getEventManager().addEventListener(GeneticEvent.GENOTYPE_FINISH_GENETIC_OPERATORS_EVENT, dbListener);
        config.getEventManager().addEventListener(GeneticEvent.GENOTYPE_EVALUATED_EVENT, dbListener);
        config.getEventManager().addEventListener(GeneticEvent.RUN_COMPLETED_EVENT, dbListener);


        // presentation
        if (props.getBooleanProperty(PRESENTATION_ACTIVE_KEY, false)) {
            PresentationEventListener presListener = new PresentationEventListener(run);
            presListener.init(props);
            config.getEventManager().addEventListener(GeneticEvent.GENOTYPE_EVALUATED_EVENT, presListener);
            config.getEventManager().addEventListener(GeneticEvent.RUN_COMPLETED_EVENT, presListener);
        }
        // fitness function
        f_domain.addEvaluationFunctions(config);
        domainMaxFitness = f_domain.getMaxFitnessValue();

        // load population, either from previous run or random
        genotype = db.loadGenotype(config);
//        if (genotype != null) {
//            logger.info("genotype from previous run");
//        } else {
            genotype = Genotype.randomInitialGenotype(config);
            logger.info("random genotype");
//        }
        genotype.registerPopulationListener(this);
    }

    public void update(List<Chromosome> population) {
        f_domain.setCandidates(getCandidates());
    }
    
    private boolean checkAdjustedFitness(HashMap<Long, Double> adjustedFitness, double test) {
    	for (double val : adjustedFitness.values())
    		if (val > test)
    			return false;
    	return true;
    }

    /**
     * Perform a single run.
     *
     * @throws Exception
     */
    public void run() {
        // run start time
        Date runStartDate = Calendar.getInstance().getTime();
        logger.info("Run: start");
        DateFormat fmt = new SimpleDateFormat("HH:mm:ss");

        // initialize result data
        int generationOfFirstSolution = -1;
        fitnessChamp = genotype.getFittestChromosome();
        if (fitnessChamp != null) {
            System.out.println("fitnessChamp != null");
        } else {
            System.out.println("fitnessChamp == null");            
        }
        HashMap<Long, Double> adjustedFitness = adjustFitnessToDomain(fitnessChamp);

        // generational evolutions
        for (int generation = 0; (generation < numEvolutions && checkAdjustedFitness(adjustedFitness, targetFitness)); ++generation) {

            // generation start time
            Date generationStartDate = Calendar.getInstance().getTime();
            logger.log(Level.INFO, "Generation {0}: start", generation);

            // next iteration
            if (noveltySearch) {
                genotype.evolveForNovelty();
            } else {
                genotype.evolveForFitness();
            }

            fitnessChamp = genotype.getFittestChromosome();
            candidates.keySet().retainAll(genotype.getChromosomes());

            adjustedFitness = adjustFitnessToDomain(fitnessChamp);
            if (!checkAdjustedFitness(adjustedFitness, targetFitness) && generationOfFirstSolution == -1) {
                generationOfFirstSolution = generation;
            }

            // generation finish
            Date generationEndDate = Calendar.getInstance().getTime();
            long durationMillis = generationEndDate.getTime() - generationStartDate.getTime();
            logger.log(Level.INFO, "Generation {0}: end [{1} - {2}] [{3}]", new Object[]{generation, fmt.format(generationStartDate), fmt.format(generationEndDate), durationMillis});

            // Wright out the behavior space image...
            if (recordBehaviorSpace) {
                recordBehaviorSapce();
            }

            if (!checkAdjustedFitness(adjustedFitness, targetFitness)) {
                break;
            }
        }

        // run finish
        config.getEventManager().fireGeneticEvent(
                new GeneticEvent(GeneticEvent.RUN_COMPLETED_EVENT, genotype));
        try {
            recordSimulation(fitnessChamp);
        } catch (IOException ex) {
            Logger.getLogger(Evolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        logConclusion(generationOfFirstSolution, fitnessChamp);
        Date runEndDate = Calendar.getInstance().getTime();
        long durationMillis = runEndDate.getTime() - runStartDate.getTime();
        logger.log(Level.INFO, "Run: end [{0} - {1}] [{2}]", new Object[]{fmt.format(runStartDate), fmt.format(runEndDate), durationMillis});
    }

    private boolean setEvolutionMode(String evolutionMode) {
        if (evolutionMode.equalsIgnoreCase("novelty")) {
            noveltySearch = true;
        } else {
            noveltySearch = false;
        }
        return noveltySearch;
    }
    /**
     *
     */
    private Map<Chromosome, Candidate> candidates = new HashMap<Chromosome, Candidate>();

    /**
     *
     * @param subject
     * @return
     */
    private Candidate getCandidate(Chromosome chrom) {
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
        return getCandidates(genotype.getChromosomes());
    }

    /**
     *
     * @param someChroms
     * @return
     */
    public List<Candidate> getCandidates(List<Chromosome> someChroms) {
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
        Map<Candidate, BehaviorVector> original = f_domain.getAllPointsVisited();
        
        for(Candidate candidate : original.keySet()) {
            result.put(candidate.getChromosome(), original.get(candidate));
        }
        return result;
    }

    private void recordBehaviorSapce() {
        // Wright out the behavior space image...
        RenderedImage image = f_domain.getPhenotypeBehaviorCurrent(behaviorSpaceSize);
        try {
            db.store(image);
        } catch (Exception ex) {
            Logger.getLogger(Evolver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void recordSimulation(Chromosome chromToSimulate) throws IOException {
        Dimension size = new Dimension(512, 512);
        Candidate subject = getCandidate(chromToSimulate);
        
        for (RenderedImage image : f_domain.getPhenotypeBehavior(subject, size)) {
            //renderer.applyBackground(bi.getGraphics(), size, Color.WHITE);
        	db.store(image, chromToSimulate.getId());
        }
    }

}
