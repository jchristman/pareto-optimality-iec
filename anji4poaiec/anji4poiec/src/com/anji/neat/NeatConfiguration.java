/*
 * Copyright (C) 2004 Derek James and Philip Tucker
 * 
 * This file is part of ANJI (Another NEAT Java Implementation).
 * 
 * ANJI is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 * 
 * created by Philip Tucker on Feb 22, 2003
 */
package com.anji.neat;

import com.anji.integration.Activator;
import com.anji.nn.ActivationFunctionType;
import com.anji.util.Properties;
import com.anji.util.Randomizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jgap.*;
import org.jgap.event.EventManager;
import org.jgap.impl.*;

/**
 * Extension of JGAP configuration with NEAT-specific features added.
 *
 * @author Philip Tucker
 */
@SuppressWarnings("serial")
public class NeatConfiguration extends Configuration {

    private static final Logger logger = Logger.getLogger(NeatConfiguration.class.getName());
    /**
     * properties key, file in which unique ID sequence number is stored
     */
    public static final String ID_FACTORY_KEY = "id.file";
    private static final short DEFAULT_STIMULUS_SIZE = 3;
    private static final short DEFAULT_INITIAL_HIDDEN_SIZE = 0;
    private static final short DEFAULT_RESPONSE_SIZE = 3;
    private static final String EVOLUTION_MODE_KEY = "evolution.mode";
    private static final String DEFAULT_EVOLUTION_MODE = "generational";  // or "steady.state"
    private static final String SELECTION_MODE_KEY = "fitness.selection.mode";
    private static final String DEFAULT_SELECTION_MODE = "fitness";  // or "novelty"
    /**
     * default survival rate
     */
    public static final float DEFAULT_SURVIVAL_RATE = 0.20f;
    /**
     * default population size
     */
    public static final int DEFAULT_IEC_POPUL_SIZE = 250;
    /**
     * default novelty population size
     */
    public static final int DEFAULT_NOVELTY_POPUL_SIZE = 250;
    /**
     * default fitness population size
     */
    public static final int DEFAULT_FITNESS_POPUL_SIZE = 250;
    /**
     * default short-term novelty search limit
     */
    public static final int DEFAULT_SHORT_TERM_NOVELTY_SEARCH_LIMIT = 500;
    /**
     * default short-term fitness search limit
     */
    public static final int DEFAULT_SHORT_TERM_FITNESS_SEARCH_LIMIT = 500;
    /**
     * properties key, dimension of neural net stimulus
     */
    public static final String STIMULUS_SIZE_KEY = "stimulus.size";
    /**
     * properties key, dimension of neural net response
     */
    public static final String RESPONSE_SIZE_KEY = "response.size";
    /**
     * properties key, survival rate
     */
    public static final String SURVIVAL_RATE_KEY = "survival.rate";
    /**
     * properties key, topology mutation type; if true, use "classic" method
     * where at most a single topological mutation occurs per generation per
     * individual
     */
    public static final String TOPOLOGY_MUTATION_CLASSIC_KEY = "topology.mutation.classic";
    /**
     * properties key, maximum connection weight
     */
    public static final String WEIGHT_MAX_KEY = "weight.max";
    /**
     * properties key, minimum connection weight
     */
    public static final String WEIGHT_MIN_KEY = "weight.min";
    /**
     * properties key, population size
     */
    public static final String IEC_POPUL_SIZE_KEY = "iec.popul.size";
    /**
     * properties key, novelty population size
     */
    public static final String NOVELTY_POPUL_SIZE_KEY = "novelty.popul.size";
    /**
     * properties key, fitness population size
     */
    public static final String FITNESS_POPUL_SIZE_KEY = "fitness.popul.size";
    /**
     * properties key, short-term novelty search limit
     */
    public static final String SHORT_TERM_NOVELTY_SEARCH_LIMIT_KEY = "novelty.short-term.limit";
    /**
     * properties key, short-term fitness search limit
     */
    public static final String SHORT_TERM_FITNESS_SEARCH_LIMIT_KEY = "fitness.short-term.limit";
    /**
     * properties key, speciation chromosome compatibility excess coefficient
     */
    public final static String CHROM_COMPAT_EXCESS_COEFF_KEY = "chrom.compat.excess.coeff";
    /**
     * properties key, speciation chromosome compatibility disjoint coefficient
     */
    public final static String CHROM_COMPAT_DISJOINT_COEFF_KEY = "chrom.compat.disjoint.coeff";
    /**
     * properties key, speciation chromosome compatibility common coefficient
     */
    public final static String CHROM_COMPAT_COMMON_COEFF_KEY = "chrom.compat.common.coeff";
    /**
     * properties key, speciation threshold
     */
    public final static String SPECIATION_THRESHOLD_KEY = "speciation.threshold";
    /**
     * properties key, elitism enabled
     */
    public final static String ELITISM_KEY = "selector.elitism";
    /**
     * properties key, minimum size a specie must be to produce an elite member
     */
    public final static String ELITISM_MIN_SPECIE_SIZE_KEY = "selector.elitism.min.specie.size";
    /**
     * properties key, enable weighted selection process
     */
    public final static String WEIGHTED_SELECTOR_KEY = "selector.roulette";
    /**
     * properties key, enable fully connected initial topologies
     */
    public final static String INITIAL_TOPOLOGY_FULLY_CONNECTED_KEY = "initial.topology.fully.connected";
    /**
     * properties key, number of hidden neurons in initial topology
     */
    public final static String INITIAL_TOPOLOGY_NUM_HIDDEN_NEURONS_KEY = "initial.topology.num.hidden.neurons";
    /**
     * properties key, activation function type of neurons
     */
    public final static String INITIAL_TOPOLOGY_ACTIVATION_KEY = "initial.topology.activation";
    /**
     * properties key, activation function type of input neurons
     */
    public final static String INITIAL_TOPOLOGY_ACTIVATION_INPUT_KEY = "initial.topology.activation.input";
    /**
     * properties key, activation function type of output neurons
     */
    public final static String INITIAL_TOPOLOGY_ACTIVATION_OUTPUT_KEY = "initial.topology.activation.output";
    private Properties props;
    private CloneFitnessReproductionOperator cloneFitnessOperator = null;
    private NeatCrossoverFitnessReproductionOperator crossoverFitnessOperator = null;
    private CloneNoveltyReproductionOperator cloneNoveltyOperator = null;
    private NeatCrossoverNoveltyReproductionOperator crossoverNoveltyOperator = null;
    private double maxConnectionWeight = Double.MAX_VALUE;
    private double minConnectionWeight = -Double.MAX_VALUE;
    private ActivationFunctionType inputActivationType;
    private ActivationFunctionType outputActivationType;
    private ActivationFunctionType hiddenActivationType;
    private NeatIdMap neatIdMap;

    /**
     * Initialize mutation operators.
     *
     * @throws InvalidConfigurationException
     */
    private void initMutation() throws InvalidConfigurationException {
        // remove connection
        RemoveConnectionMutationOperator removeOperator = (RemoveConnectionMutationOperator) props.singletonObjectProperty(RemoveConnectionMutationOperator.class);
        if ((removeOperator.getMutationRate() > 0.0f)
                && (removeOperator.getMaxWeightRemoved() > 0.0f)) {
            addMutationOperator(removeOperator);
        }

        // add topology
        boolean isTopologyMutationClassic = props.getBooleanProperty(TOPOLOGY_MUTATION_CLASSIC_KEY,
                false);
        if (isTopologyMutationClassic) {
            SingleTopologicalMutationOperator singleOperator = (SingleTopologicalMutationOperator) props.singletonObjectProperty(SingleTopologicalMutationOperator.class);
            if (singleOperator.getMutationRate() > 0.0f) {
                addMutationOperator(singleOperator);
            }
        } else {
            // add connection
            AddConnectionMutationOperator addConnOperator = (AddConnectionMutationOperator) props.singletonObjectProperty(AddConnectionMutationOperator.class);
            if (addConnOperator.getMutationRate() > 0.0f) {
                addMutationOperator(addConnOperator);
            }

            // add neuron
            AddNeuronMutationOperator addNeuronOperator = (AddNeuronMutationOperator) props.singletonObjectProperty(AddNeuronMutationOperator.class);
            if (addNeuronOperator.getMutationRate() > 0.0f) {
                addMutationOperator(addNeuronOperator);
            }
        }

        // modify weight
        WeightMutationOperator weightOperator = (WeightMutationOperator) props.singletonObjectProperty(WeightMutationOperator.class);
        if (weightOperator.getMutationRate() > 0.0f) {
            addMutationOperator(weightOperator);
        }

        // prune
        PruneMutationOperator pruneOperator = (PruneMutationOperator) props.singletonObjectProperty(PruneMutationOperator.class);
        if (pruneOperator.getMutationRate() > 0.0f) {
            addMutationOperator(pruneOperator);
        }
    }

    /**
     * See <a href=" {@docRoot}/params.htm" target="anji_params">Parameter
     * Details </a> for specific property settings.
     *
     * @param newProps configuration parameters; newProps[SURVIVAL_RATE_KEY]
     * should be < 0.50f
     * @throws InvalidConfigurationException
     */
    private void init(Properties newProps) throws InvalidConfigurationException {
        props = newProps;

        Randomizer r = (Randomizer) props.singletonObjectProperty(Randomizer.class);
        setRandomGenerator(r.getRand());
        setEventManager(new EventManager());

        // id persistence
        String s = props.getProperty(ID_FACTORY_KEY, null);
        try {
            if (s != null) {
                setIdFactory(new IdFactory(s));
            }
        } catch (IOException e) {
            String msg = "could not load IDs";
            logger.log(Level.SEVERE, msg, e);
            throw new InvalidConfigurationException(msg);
        }

        String evolutionMode = props.getProperty(EVOLUTION_MODE_KEY, DEFAULT_EVOLUTION_MODE);
        if (evolutionMode == null) {
            setEvolutionMode(GENERATIONAL);
        } else if (evolutionMode.equalsIgnoreCase("steady.state")) {
            setEvolutionMode(STEADY_STATE);
        } else {
            setEvolutionMode(GENERATIONAL);
        }

        String selectionMode = props.getProperty(SELECTION_MODE_KEY, DEFAULT_SELECTION_MODE);
        if (selectionMode.equalsIgnoreCase("novelty")) {
            setSelectionMode(NOVELTY);
        } else {
            setSelectionMode(FITNESS);
        }

        // make sure numbers add up
        float survivalRate = props.getFloatProperty(SURVIVAL_RATE_KEY, DEFAULT_SURVIVAL_RATE);
        float crossoverSlice = 1.0f - (2.0f * survivalRate);
        if (crossoverSlice < 0.0f) {
            throw new InvalidConfigurationException("survival rate too large: " + survivalRate);
        }

        // selector
        FitnessSelector fitnessSelector = new MultiObjectiveNSSelector();
        fitnessSelector.setSurvivalRate(survivalRate);
        fitnessSelector.setElitism(props.getBooleanProperty(ELITISM_KEY, true));
        fitnessSelector.setElitismMinSpecieSize(props.getIntProperty(ELITISM_MIN_SPECIE_SIZE_KEY, 6));
        setFitnessSelector(fitnessSelector);

        // selector
        NoveltySelector noveltySelector;
        if (props.getBooleanProperty(WEIGHTED_SELECTOR_KEY, false)) {
            noveltySelector = new WeightedNoveltySelector();
        } else {
            noveltySelector = new SimpleNoveltySelector();
        }
        noveltySelector.setSurvivalRate(survivalRate);
        noveltySelector.setElitism(props.getBooleanProperty(ELITISM_KEY, true));
        noveltySelector.setElitismMinSpecieSize(props.getIntProperty(ELITISM_MIN_SPECIE_SIZE_KEY, 6));
        setNoveltySelector(noveltySelector);

        // reproduction
        cloneFitnessOperator = new CloneFitnessReproductionOperator();
        cloneFitnessOperator.setSlice(survivalRate);
        addFitnessReproductionOperator(cloneFitnessOperator);

        crossoverFitnessOperator = new NeatCrossoverFitnessReproductionOperator();
        crossoverFitnessOperator.setSlice(crossoverSlice);
        addFitnessReproductionOperator(crossoverFitnessOperator);

        cloneNoveltyOperator = new CloneNoveltyReproductionOperator();
        cloneNoveltyOperator.setSlice(survivalRate);
        addNoveltyReproductionOperator(cloneNoveltyOperator);
 
        crossoverNoveltyOperator = new NeatCrossoverNoveltyReproductionOperator();
        crossoverNoveltyOperator.setSlice(crossoverSlice);
        addNoveltyReproductionOperator(crossoverNoveltyOperator);

        // mutation
        initMutation();

        // population
        setIECpopulationSize(props.getIntProperty(IEC_POPUL_SIZE_KEY, DEFAULT_IEC_POPUL_SIZE));
        setNoveltySearchPopulationSize(props.getIntProperty(NOVELTY_POPUL_SIZE_KEY, DEFAULT_NOVELTY_POPUL_SIZE));
        setFitnessSearchPopulationSize(props.getIntProperty(FITNESS_POPUL_SIZE_KEY, DEFAULT_FITNESS_POPUL_SIZE));

        // short-term search limits
        setShortTermNoveltySearchLimit(props.getIntProperty(SHORT_TERM_NOVELTY_SEARCH_LIMIT_KEY, DEFAULT_SHORT_TERM_NOVELTY_SEARCH_LIMIT));
        setFitnessSearchLimit(props.getIntProperty(SHORT_TERM_FITNESS_SEARCH_LIMIT_KEY, DEFAULT_SHORT_TERM_FITNESS_SEARCH_LIMIT));

        // node activation
        hiddenActivationType = ActivationFunctionType.valueOf(props.getProperty(
                INITIAL_TOPOLOGY_ACTIVATION_KEY, ActivationFunctionType.SIGMOID.toString()));
        inputActivationType = ActivationFunctionType.valueOf(props.getProperty(
                INITIAL_TOPOLOGY_ACTIVATION_INPUT_KEY, null));
        if (inputActivationType == null) {
            inputActivationType = hiddenActivationType;
        }
        outputActivationType = ActivationFunctionType.valueOf(props.getProperty(
                INITIAL_TOPOLOGY_ACTIVATION_OUTPUT_KEY, null));
        if (outputActivationType == null) {
            outputActivationType = hiddenActivationType;
        }
        load();
        short stimulus = props.getShortProperty(STIMULUS_SIZE_KEY, DEFAULT_STIMULUS_SIZE);
        short hidden = props.getShortProperty(INITIAL_TOPOLOGY_NUM_HIDDEN_NEURONS_KEY, DEFAULT_INITIAL_HIDDEN_SIZE);
        short response = props.getShortProperty(RESPONSE_SIZE_KEY, DEFAULT_RESPONSE_SIZE);
        boolean fullyConnected = props.getBooleanProperty(INITIAL_TOPOLOGY_FULLY_CONNECTED_KEY, true);
        ChromosomeMaterial sample = NeatChromosomeUtility.newSampleChromosomeMaterial(stimulus, hidden, response, this, fullyConnected);
        setSampleChromosomeMaterial(sample);
        store();

        // weight bounds
        minConnectionWeight = props.getDoubleProperty(WEIGHT_MIN_KEY, -Double.MAX_VALUE);
        maxConnectionWeight = props.getDoubleProperty(WEIGHT_MAX_KEY, Double.MAX_VALUE);

        // speciation parameters
        initSpeciationParms();
    }

    /**
     * See <a href=" {@docRoot}/params.htm" target="anji_params">Parameter
     * Details </a> for specific property settings.
     *
     * @param newProps
     * @see NeatConfiguration#init(Properties)
     * @throws InvalidConfigurationException
     */
    public NeatConfiguration(Properties newProps) throws InvalidConfigurationException {
        super();
        init(newProps);
    }

    private void initSpeciationParms() {
        try {
            getSpeciationParms().setSpecieCompatExcessCoeff(
                    props.getDoubleProperty(CHROM_COMPAT_EXCESS_COEFF_KEY));
        } catch (RuntimeException e) {
            logger.log(Level.INFO, "no speciation compatibility threshold specified", e);
        }
        try {
            getSpeciationParms().setSpecieCompatDisjointCoeff(
                    props.getDoubleProperty(CHROM_COMPAT_DISJOINT_COEFF_KEY));
        } catch (RuntimeException e) {
            logger.log(Level.INFO, "no speciation compatibility threshold specified", e);
        }
        try {
            getSpeciationParms().setSpecieCompatCommonCoeff(
                    props.getDoubleProperty(CHROM_COMPAT_COMMON_COEFF_KEY));
        } catch (RuntimeException e) {
            logger.log(Level.INFO, "no speciation compatibility threshold specified", e);
        }
        try {
            getSpeciationParms().setSpeciationThreshold(
                    props.getDoubleProperty(SPECIATION_THRESHOLD_KEY));
        } catch (RuntimeException e) {
            logger.log(Level.INFO, "no speciation compatibility threshold specified", e);
        }
    }

    /**
     * factory method to construct new neuron allele with unique innovation ID
     * of specified
     * <code>type</code>
     *
     * @param type
     * @return NeuronAllele
     */
    public NeuronAllele newNeuronAllele(NeuronType type) {
        ActivationFunctionType act;
        if (NeuronType.INPUT.equals(type)) {
            act = inputActivationType;
        } else if (NeuronType.OUTPUT.equals(type)) {
            act = outputActivationType;
        } else if (NeuronType.LEO.equals(type)) {
            act = ActivationFunctionType.GAUSSIAN;
        } else {
            act = hiddenActivationType;
        }
        NeuronGene gene = new NeuronGene(type, nextInnovationId(), act);
        return new NeuronAllele(gene);
    }

    /**
     * Factory method to construct new neuron allele which has replaced
     * connection
     * <code>connectionId</code> according to NEAT add neuron mutation. If a
     * previous mutation has occurred adding a neuron on connection
     * connectionId, returns a neuron with that id - otherwise, a new id.
     *
     * @param connectionId
     * @return NeuronAllele
     */
    public NeuronAllele newNeuronAllele(Long connectionId) {
        if (neatIdMap == null) {
            logger.severe("neatIdMap is null!");
        }
        Long id = neatIdMap.findNeuronId(connectionId);
        if (id == null) {
            id = nextInnovationId();
            neatIdMap.putNeuronId(connectionId, id);
        }
        if (hiddenActivationType.equals(ActivationFunctionType.CPPN)) {
            ActivationFunctionType cppnActivationFunction = getRandomActivationType();
//            System.out.println("adding node with activatin function == " + cppnActivationFunction.toString());
            NeuronGene gene = new NeuronGene(NeuronType.HIDDEN, id, cppnActivationFunction);
            return new NeuronAllele(gene);
        } else {
            NeuronGene gene = new NeuronGene(NeuronType.HIDDEN, id, hiddenActivationType);
            return new NeuronAllele(gene);
        }
    }
    private List<ActivationFunctionType> cppnActivations = null;

    private ActivationFunctionType getRandomActivationType() {
        if (cppnActivations == null) {
            System.out.println("initializing CPPN function array");
            cppnActivations = new ArrayList<ActivationFunctionType>();
            cppnActivations.add(ActivationFunctionType.GAUSSIAN);
            cppnActivations.add(ActivationFunctionType.LINEAR);
            cppnActivations.add(ActivationFunctionType.SINE);
            cppnActivations.add(ActivationFunctionType.COSINE);
            cppnActivations.add(ActivationFunctionType.SIGNED_SIGMOID);
        }
        double rand = Math.random();
        return cppnActivations.get((int) (rand * cppnActivations.size()));
    }

    /**
     * factory method to construct new connection allele from neuron
     * <code>srcNeuronId</code> to neuron
     * <code>destNeuronId</code> according to NEAT add connection mutation; if a
     * previous mutation has occurred adding a connection between srcNeuronId
     * and destNeuronId, returns connection with that id; otherwise, new
     * innovation id
     *
     * @param srcNeuronId
     * @param destNeuronId
     * @return ConnectionAllele
     */
    public ConnectionAllele newConnectionAllele(Long srcNeuronId, Long destNeuronId) {
        return newConnectionAllele(srcNeuronId, destNeuronId, ConnectionAllele.DEFAULT_WEIGHT);
    }

    /**
     * factory method to construct new connection allele from neuron
     * <code>srcNeuronId</code> to neuron
     * <code>destNeuronId</code> according to NEAT add connection mutation; if a
     * previous mutation has occurred adding a connection between srcNeuronId
     * and destNeuronId, returns connection with that id; otherwise, new
     * innovation id
     *
     * @param srcNeuronId
     * @param destNeuronId
     * @param aConnectionWeight
     * @return ConnectionAllele
     */
    public ConnectionAllele newConnectionAllele(Long srcNeuronId, Long destNeuronId, double aConnectionWeight) {
        if (neatIdMap == null) {
            logger.severe("neatIdMap is null!");
        }
        Long id = neatIdMap.findConnectionId(srcNeuronId, destNeuronId);
        if (id == null) {
            id = nextInnovationId();
            neatIdMap.putConnectionId(srcNeuronId, destNeuronId, id);
        }
        ConnectionGene gene = new ConnectionGene(id, srcNeuronId, destNeuronId);
        return new ConnectionAllele(gene, aConnectionWeight);
    }

    /**
     * @return clone reproduction operator used to create mutated asexual
     * offspring
     */
    @SuppressWarnings("unused")
	private CloneFitnessReproductionOperator getCloneOperator() {
        return cloneFitnessOperator;
    }

    /**
     * @return crossover reproduction operator used to create mutated sexual
     * offspring
     */
    @SuppressWarnings("unused")
	private NeatCrossoverFitnessReproductionOperator getCrossoverOperator() {
        return crossoverFitnessOperator;
    }

    /**
     * @return maximum conneciton weight
     */
    public double getMaxConnectionWeight() {
        return maxConnectionWeight;
    }

    /**
     * @return minimum conneciton weight
     */
    public double getMinConnectionWeight() {
        return minConnectionWeight;
    }

    /**
     * Load from persistence.
     *
     * @throws InvalidConfigurationException
     */
    public void load() throws InvalidConfigurationException {
        if (neatIdMap == null) {
            neatIdMap = new NeatIdMap(props);
            try {
                neatIdMap.load();
            } catch (IOException e) {
                String msg = "error loading ID map";
                logger.log(Level.SEVERE, msg, e);
                throw new InvalidConfigurationException(msg);
            }
        }
    }

    /**
     * Store to persistence.
     *
     * @throws InvalidConfigurationException
     */
    public void store() throws InvalidConfigurationException {
        try {
            getIdFactory().store();
            if (neatIdMap.store()) {
                //               neatIdMap = null;
            }
        } catch (IOException e) {
            String msg = "error storing ID map";
            logger.log(Level.SEVERE, msg, e);
            throw new InvalidConfigurationException(msg);
        }
    }

    /**
     * log stats for id maps
     *
     * @param aLogger
     * @param pri priority
     */
    public void logIdMaps(Logger aLogger, Level pri) {
        neatIdMap.log(aLogger, pri);
    }

    /**
     * 
     * @param aChromosome
     * @return
     */
    public Activator createANN(Chromosome aChromosome) {
        return NeatChromosomeUtility.generateANN(aChromosome);
    }
}
