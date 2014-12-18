/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.poaiecFramework.controller;

import com.anji.integration.StepType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jgap.Chromosome;
import org.jgap.Genotype;
import org.w3c.dom.Node;

/**
 *
 * @author Brian Woolley (brian.woolley at ieee.org)
 */
@XmlRootElement
public class IecStep {

    private Long id;
    private StepType action;
    private HashMap<Long, Integer> champFitness;
    private List<Chromosome> population = new ArrayList<Chromosome>();
    private List<Chromosome> fullPopulation = new ArrayList<Chromosome>();
    private List<Chromosome> selected = new ArrayList<Chromosome>();
    private List<Chromosome> unselected = new ArrayList<Chromosome>();
    private List<Chromosome> solutions = new ArrayList<Chromosome>();
    private Integer evaluationCount;
    private Integer speciesSize;
    private Integer archiveSize;
    private long startTime = System.currentTimeMillis();
    private long iecEvalTime = -1;
    private long runTime = -1;
    private HashMap<Long,Double> lastPOPV;

    public IecStep(long anId, StepType userAction) {
        this(null, anId, userAction);
    }
    
    public IecStep(Genotype aGenotype, long anId, StepType userAction) {

        id = new Long(anId);
        action = userAction;
        if (aGenotype != null) {
            update(aGenotype);
            lastPOPV = aGenotype.m_activeConfiguration.getPOPV();
        }
    }

    IecStep(IecStep anIecStep) {
        id = anIecStep.id;
        action = anIecStep.action;
        champFitness = anIecStep.champFitness;
        population.addAll(anIecStep.population);
        fullPopulation.addAll(anIecStep.fullPopulation);
        selected.addAll(anIecStep.selected);
        unselected.addAll(anIecStep.unselected);
        solutions.addAll(anIecStep.solutions);
        evaluationCount = anIecStep.evaluationCount;
        speciesSize = anIecStep.speciesSize;
        archiveSize = anIecStep.archiveSize;
        startTime = anIecStep.startTime;
        iecEvalTime = anIecStep.iecEvalTime;
        runTime = anIecStep.runTime;
    }
    
    

    IecStep(Node anXmlStep) {
        loadFromXml(anXmlStep);
    }

    public StepType getAction() {
        return action;
    }

    @XmlAttribute
    public void setAction(StepType action) {
        iecEvalTime = Math.max(System.currentTimeMillis() - startTime, 0);
        this.action = action;
    }

    public Integer getArchiveSize() {
        return archiveSize;
    }

    @XmlElement
    public void setArchiveSize(Integer archiveSize) {
        this.archiveSize = archiveSize;
    }

    public Integer getEvaluationCount() {
        return evaluationCount;
    }

    @XmlElement
    public void setEvaluationCount(Integer evaluationCount) {
        this.evaluationCount = evaluationCount;
    }

    public Long getId() {
        return id;
    }

    @XmlAttribute
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSpeciesSize() {
        return speciesSize;
    }

    @XmlElement
    public void setSpeciesSize(Integer speciesSize) {
        this.speciesSize = speciesSize;
    }

    public HashMap<Long, Integer> getChampFitness() {
        return champFitness;
    }

    @XmlElement
    public void setChampFitness(HashMap<Long, Integer> fitness) {
        this.champFitness = fitness;
    }

    public List<Chromosome> getChromosomes() {
        for (Chromosome chrom : population) {
            if (selected.contains(chrom)) {
                chrom.setIsSelectedForNextGeneration(true);
            } else {
                chrom.setIsSelectedForNextGeneration(false);
            }

            if (solutions.contains(chrom)) {
                chrom.setAsSolution(true);
            } else {
                chrom.setAsSolution(false);
            }
        }
        return new ArrayList<Chromosome>(population);
    }

    public void update(Genotype aGenotype) {
        if (aGenotype != null) {
            setSpeciesSize(aGenotype.getSpecies().size());
            setArchiveSize(aGenotype.getCurrentNoveltyArchiveSize());
            setEvaluationCount(aGenotype.getStepEvaluationCount());
            if (aGenotype.getFittestChromosome() != null) {
                setChampFitness(aGenotype.getFittestChromosome().getFitnessValues());
            }
            if (!population.containsAll(aGenotype.getChromosomes())) {
                population.clear();
                population.addAll(aGenotype.getChromosomes());
            }
            if (!fullPopulation.containsAll(aGenotype.getUnfilteredChromosomes())) {
            	fullPopulation.clear();
            	fullPopulation.addAll(aGenotype.getUnfilteredChromosomes());
            }
            for (Chromosome chrom : population) {
                if (chrom.isSolution()) {
                    solutions.add(chrom);
                }
                if (chrom.isSelectedForNextGeneration()) {
                    selected.add(chrom);
                }
                if (!chrom.isSelectedForNextGeneration() && !chrom.isSolution()) {
                    unselected.add(chrom);
                }
            }
        } else {
            speciesSize = null;
            archiveSize = null;
            evaluationCount = null;
        }
    }
    
    public void setLastPOPV(HashMap<Long,Double> popv) {
    	lastPOPV = popv;
    }

    /**
     * @return 
     * @see com.anji.util.XmlPersistable#toXml()
     */
    public String toXml() {
        StringBuilder result = new StringBuilder();

        result.append(indent(2)).append(open(IEC_STEP_TAG, STEP_ID_TAG + "=\"" + id + "\""));
        result.append(indent(3)).append(textContentElement(ACTION_TAG, action));
        if (evaluationCount != null) {
            result.append(indent(3)).append(textContentElement(EVALUATION_COUNT_TAG, evaluationCount));
        }
        result.append(indent(3)).append(textContentElement(IEC_EVAL_TIME_TAG, runTime));
        result.append(indent(3)).append(textContentElement(RUNTIME_TAG, runTime));
        
        if (lastPOPV != null) {
		    result.append(indent(3)).append(open(POPV_TAG, ""));
		    for (Entry<Long, Double> entry : lastPOPV.entrySet()) {
		    	result.append(indent(4)).append(openNoNewLine(OBJECTIVE_FUNC_TAG, OBJECTIVE_ID_TAG + "=\"" + entry.getKey() + "\""));
				result.append(entry.getValue());
				result.append(close(OBJECTIVE_FUNC_TAG));
		    }
			result.append(indent(3)).append(close(POPV_TAG));
        }
        
        if (champFitness != null) {
        	result.append(indent(3)).append(open(CHAMP_FITNESS_TAG,""));
        	for (Entry<Long, Integer> entry : champFitness.entrySet()) {
        		result.append(indent(4)).append(openNoNewLine(OBJECTIVE_FUNC_TAG, OBJECTIVE_ID_TAG + "=\"" + entry.getKey() + "\""));
        		result.append(entry.getValue());
        		result.append(close(OBJECTIVE_FUNC_TAG));
        	}
        	result.append(indent(3)).append(close(CHAMP_FITNESS_TAG));
        }
        if (archiveSize != null) {
            result.append(indent(3)).append(textContentElement(ARCHIVE_TAG, archiveSize));
        }
        if (speciesSize != null) {
            result.append(indent(3)).append(textContentElement(SPECIES_TAG, speciesSize));
        }

        for (Chromosome chrom : population) {
            boolean isSolution = chrom.isSolution();
            boolean isSelected = chrom.isSelectedForNextGeneration();
            if (solutions.contains(chrom) || selected.contains(chrom)) {
                chrom.setAsSolution(solutions.contains(chrom));
                chrom.setIsSelectedForNextGeneration(selected.contains(chrom));
                result.append(chrom.toXml());
                chrom.setAsSolution(isSolution);
                chrom.setIsSelectedForNextGeneration(isSelected);
            }
        }
        
        result.append(indent(3)).append(open(POPULATION_TAG,""));
        for (Chromosome chrom : fullPopulation) {
        	result.append(indent(4)).append(open(CHROMOSOME_TAG, chrom.getId().toString()));
        	result.append(indent(5)).append(textContentElement(NOVELTY_TAG, chrom.getNoveltyValue()));
        	result.append(indent(5)).append(textContentElement(PARETO_TAG, chrom.numDominatedBy()));
        	result.append(indent(4)).append(close(CHROMOSOME_TAG));
        }
        result.append(indent(3)).append(close(POPULATION_TAG));

        result.append(indent(2)).append(close(IEC_STEP_TAG));

        return result.toString();
    }

    private void loadFromXml(Node step) {
        if (step.hasAttributes()) {
            if (step.getAttributes().getNamedItem(STEP_ID_TAG).getNodeValue().matches("[0-9]+")) {
                id = Long.parseLong(step.getAttributes().getNamedItem(STEP_ID_TAG).getNodeValue());
            }
        }

        Node child = step.getFirstChild();
        while (child != null) {
            if (child.getNodeName().equalsIgnoreCase(ACTION_TAG)) {
                action = StepType.valueOf(child.getTextContent());

            }

            if (child.getNodeName().equalsIgnoreCase(EVALUATION_COUNT_TAG)) {
                if (child.getTextContent().matches("[0-9]+")) {
                    evaluationCount = Integer.parseInt(child.getTextContent());
                }
            }

            if (child.getNodeName().equalsIgnoreCase(CHAMP_FITNESS_TAG)) {
            	Node fitnessNode = child.getFirstChild();
            	while (fitnessNode != null) {
            		if (fitnessNode.getNodeName().equalsIgnoreCase(OBJECTIVE_FUNC_TAG)) {
            			champFitness.put(Long.parseLong(fitnessNode.getAttributes().getNamedItem(OBJECTIVE_ID_TAG).getNodeValue())
            					, Integer.parseInt(fitnessNode.getTextContent()));
            		}
            		
            		fitnessNode.getNextSibling();
            	}
            }

            if (child.getNodeName().equalsIgnoreCase(ARCHIVE_TAG)) {
                if (child.getTextContent().matches("[0-9]+")) {
                    archiveSize = Integer.parseInt(child.getTextContent());
                }
            }

            if (child.getNodeName().equalsIgnoreCase(SPECIES_TAG)) {
                if (child.getTextContent().matches("[0-9]+")) {
                    speciesSize = Integer.parseInt(child.getTextContent());
                }
            }

            if (child.getNodeName().equalsIgnoreCase(Chromosome.CHROMOSOME_TAG)) {

                Node xmlChromosome = step.getFirstChild();
                while (xmlChromosome != null) {
                    if (xmlChromosome.getNodeName().equalsIgnoreCase(Chromosome.CHROMOSOME_TAG)) {
                        Chromosome chrom = new Chromosome(xmlChromosome);
                        population.add(chrom);
                    }
                    xmlChromosome = xmlChromosome.getNextSibling();
                }

                solutions = new ArrayList<Chromosome>();
                selected = new ArrayList<Chromosome>();
                for (Chromosome chrom : population) {
                    if (chrom.isSolution()) {
                        solutions.add(chrom);
                    } else {
                        selected.add(chrom);
                    }
                }

            }

            child = child.getNextSibling();
        }
    }

    /*
     *  <step id="0">
     *      <action>INITIAL</action>
     *      <evaluations>250</evaluations>
     *      <archive count="178" />
     *      <species count="1" />
     *      <chromosome id="120" >
     *              ...
     *      </chromosome>
     *  </step>
     */
    private String openNoNewLine(String label, String attributes) {
        return new StringBuilder().append("<").append(label).append(" ").append(attributes).append(">").toString();
    }
    
    private String open(String label, String attributes) {
        return new StringBuilder().append("<").append(label).append(" ").append(attributes).append(">\n").toString();
    }

    private String close(String label) {
        return new StringBuilder().append("</").append(label).append(">\n").toString();
    }

    private String textContentElement(String label, Object value) {
        return new StringBuilder().append("<").append(label).append(">").append(value).append("</").append(label).append(">\n").toString();
    }

    private String indent(int x) {
        StringBuilder indention = new StringBuilder();
        for (int i = 0; i < x; i++) {
            indention.append("    ");
        }
        return indention.toString();
    }
    /**
     * XML base tag
     */
    public final static String IEC_STEP_TAG = "step";
    public final static String STEP_ID_TAG = "id";
    public final static String ACTION_TAG = "action";
    public final static String IEC_EVAL_TIME_TAG = "iecEvalTime";
    public final static String RUNTIME_TAG = "runTime";
    public final static String EVALUATION_COUNT_TAG = "evaluations";
    public final static String CHAMP_FITNESS_TAG = "champFitness";
    public final static String POPV_TAG = "POPV";
    public final static String OBJECTIVE_FUNC_TAG = "objectiveFunction";
    public final static String OBJECTIVE_ID_TAG = "obj_id";
    public final static String SPECIES_TAG = "species";
    public final static String ARCHIVE_TAG = "archive";
    public final static String COMMAND_TAG = "command";
    public final static String POPULATION_TAG = "population";
    public final static String CHROMOSOME_TAG = "chromosome";
    public final static String NOVELTY_TAG = "noveltyValue";
    public final static String PARETO_TAG = "paretoFront";

    public boolean hasSolution() {
        return !solutions.isEmpty();
    }

    public int getChampConnectionCount() {
        if (solutions.isEmpty()) {
            return -1;
        } else {
            return solutions.get(0).countLinks();
        }
    }

    public int getChampNodeCount() {
        if (solutions.isEmpty()) {
            return -1;
        } else {
            return solutions.get(0).countNodes();
        }
    }

    void recordEndOfStep() {
        runTime = Math.max(System.currentTimeMillis() - startTime - iecEvalTime, 0);
    }
    
    /**
     * Reports the time (mS) spent building the current population  
     * @return the number of milliseconds spent by the operation
     */
    public long getRuntime() {
        if (runTime < 0) {
            return System.currentTimeMillis() - startTime;
        } else {
            return runTime;
        }
    }
    
    public long getIecEvalTime() {
        if (iecEvalTime < 0) {
            return System.currentTimeMillis() - startTime;
        } else {
            return iecEvalTime;
        }
    }
}
