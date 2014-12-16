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
import com.anji.util.XmlPersistable;

import edu.ucf.eplex.poaiecFramework.domain.EvaluationDomain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.jgap.Chromosome;
import org.jgap.Genotype;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Brian Woolley (brian.woolley at ieee.org)
 */
public class PoaiecRun implements XmlPersistable {

    // Xml Persistable Session Data
    // --------------------------------
    private final String name;
    private final long startTime = System.currentTimeMillis();
    private Integer archiveSize = null;
    private Long seriesBranchedFrom;
    private Integer evaluations = new Integer(0);
    private Long timeElapsed = new Long(0);
    private final LinkedList<IecStep> userActionLog = new LinkedList<IecStep>();
    private IecStep current = new IecStep(userActionLog.size(), StepType.INITIAL);
    private final LinkedList<IecStep> history = new LinkedList<IecStep>();
    private final LinkedList<IecStep> future = new LinkedList<IecStep>();
    private List<HashMap<Long, Double>> POPVhistory = new LinkedList<HashMap<Long,Double>>();
//    private List<BehaviorVector> archivedPoints = new LinkedList<BehaviorVector>();
//    private List<BehaviorVector> allPoints = new LinkedList<BehaviorVector>();

    public PoaiecRun(String aName) {
        name = aName;
    }

    public Integer getSeriesArchiveCount() {
        return archiveSize;
    }

    public void setSeriesArchiveCount(Integer seriesArchiveCount) {
        archiveSize = seriesArchiveCount;
    }

    public Long getSeriesBranchedFrom() {
        return seriesBranchedFrom;
    }

    public void setSeriesBranchedFrom(Long aSeriesBranchedFrom) {
        seriesBranchedFrom = aSeriesBranchedFrom;
    }

    public Integer getSeriesEvaluationCount() {
        return evaluations;
    }

    public void setSeriesEvaluationCount(Integer seriesEvaluationCount) {
        evaluations = seriesEvaluationCount;
    }

    public Long getSeriesTimeElapsed() {
        return timeElapsed;
    }

    public void setSeriesTimeElapsed(Long seriesTimeElapsed) {
        timeElapsed = seriesTimeElapsed;
    }

    public int getSeriesUserActionCount() {
        return getSeriesStepCount()
                + getSeriesNoveltyCount()
                + getSeriesOptimizeCount();
    }

    public int getSeriesStepCount() {
        return actionCount(StepType.STEP);
    }

    public int getSeriesNoveltyCount() {
        return actionCount(StepType.NOVELTY);
    }

    public int getSeriesOptimizeCount() {
        return actionCount(StepType.FITNESS);
    }

    public int getSeriesBackCount() {
        return actionCount(StepType.BACK);
    }

    public int getSeriesForwardCount() {
        return actionCount(StepType.FORWARD);
    }

    private int actionCount(StepType type) {
        int counter = 0;
        for (IecStep step : userActionLog) {
            if (step.getAction().equals(type)) {
                counter++;
            }
        }
        return counter;
    }

    public int getChampConnectionCount() {
        for (IecStep step : userActionLog) {
            if (step.hasSolution()) {
                return step.getChampConnectionCount();
            }
        }
        return -1;
    }

    public int getChampNodeCount() {
        for (IecStep step : userActionLog) {
            if (step.hasSolution()) {
                return step.getChampNodeCount();
            }
        }
        return -1;
    }

    // Session Operations
    // --------------------------------
    public List<Chromosome> back() {
        if (previousStatesExist()) {
            future.push(current);
            current = history.pop();
            userActionLog.add(new IecStep(null, userActionLog.size(), StepType.BACK));
        }
        return current.getChromosomes();
    }

    public boolean previousStatesExist() {
        return !history.isEmpty();
    }

    public List<Chromosome> forward() {
        if (futureStatesExist()) {
            history.push(current);
            current = future.pop();
            userActionLog.add(new IecStep(null, userActionLog.size(), StepType.FORWARD));
        }
        return current.getChromosomes();
    }

    public boolean futureStatesExist() {
        return !future.isEmpty();
    }

    public void startStepFunction(Genotype genotype, EvaluationDomain<?> domain) {
        updateSession(genotype, domain);
        current.setAction(StepType.STEP);
    }

    public void startNoveltyFunction(Genotype genotype, EvaluationDomain<?> domain) {
        updateSession(genotype, domain);
        if (history.isEmpty()) {
            current.setAction(StepType.INITIAL);
        } else {
            current.setAction(StepType.NOVELTY);
        }

        // Create a novel IEC population based on user selections
        // -------------------------------------
    }
    
    public void startParetoFunction(Genotype genotype, EvaluationDomain<?> domain) {
    	updateSession(genotype, domain);
        if (history.isEmpty()) {
            current.setAction(StepType.INITIAL);
        } else {
        	current.setAction(StepType.FITNESS);
        }
    }

    public void startOptimizeFunction(Genotype genotype, EvaluationDomain<?> domain) {
        updateSession(genotype, domain);
        current.setAction(StepType.FITNESS);

        // Optimize the user selection based on fitness
        // -------------------------------------
    }

    public void updateSession(Genotype genotype, EvaluationDomain<?> domain) {
        System.out.println("in iecSession.updateSession()...currentAction == " + current.getAction().name());
        evaluations = genotype.getSeriesEvaluationCount();
        //POPVhistory.add(genotype.m_activeConfiguration.getPOPV());
//        allPoints = domain.getAllPointsVisited();
//        archivedPoints = domain.getArchivedBehaviors();
        archiveSize = domain.getNoveltyArchiveSize();
        current.update(genotype);
        setSeriesTimeElapsed(System.currentTimeMillis() - startTime);
        System.out.println(current.toXml());
    }

    protected void recordCurrentSessionState(Genotype genotype, EvaluationDomain<?> domain) {
        System.out.println("in iecSession.recordCurrentSessionState()...currentAction == " + current.getAction().name());
//        allPoints = noveltyMetric.getAllPts();
//        archivedPoints = noveltyMetric.getArchivedBehaviors();
        evaluations = genotype.getSeriesEvaluationCount();
        POPVhistory.add(new HashMap<Long,Double>(genotype.m_activeConfiguration.getPOPV()));
        archiveSize = domain.getNoveltyArchiveSize();
        current.update(genotype);
        current.recordEndOfStep();
        userActionLog.add(new IecStep(current));
        history.push(current);

        current = new IecStep(genotype, history.size(), StepType.NULL);
        future.clear();
        setSeriesTimeElapsed(System.currentTimeMillis() - startTime);
        System.out.println(current.toXml());
    }

    public String getXmlRootTag() {
        return "series";
    }

    public String getXmld() {
        return name;
    }

    public String toXml() {
        StringBuilder result = new StringBuilder();

        result.append("<?xml version = \"1.0\" encoding = \"UTF-8\"?>\n");
//        result.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \n"
//                     + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");

        if (seriesBranchedFrom != null) {
            result.append(open(IEC_SERIES_TAG, BRANCHED_FROM_TAG + "=\"" + seriesBranchedFrom + "\""));
        } else {
            result.append(open(IEC_SERIES_TAG));
        }

        // TODO: Add totalUserActions here
        result.append(indent(1)).append(textContentElement(EVALUATION_COUNT_TAG, evaluations));
        result.append(indent(1)).append(textContentElement(ARCHIVE_TAG, archiveSize));
        result.append(indent(1)).append(textContentElement(TIME_ELAPSED_TAG, timeElapsed));

        result.append(indent(1)).append(open(IEC_STEP_TAG, COUNT_TAG + "=\"" + getSeriesUserActionCount() + "\""));
        int i = 0;
        for (IecStep step : userActionLog) {
        	step.setLastPOPV(POPVhistory.get(i++));
            result.append(step.toXml());
        }
        result.append(indent(1)).append(close(IEC_STEP_TAG));
//        result.append(indent(1)).append(open(ALL_PTS_TAG, COUNT_TAG + "=\"" + allPoints.size() + "\""));
//        for (Position point : allPoints) {
//            result.append(point.toXml());
//        }
        result.append(indent(1)).append(close(ALL_PTS_TAG));
        result.append(close(IEC_SERIES_TAG));

        return result.toString();
    }

    public void loadFromXml(List<Document> docs) {
        for (Document doc : docs) {
            PoaiecRun subSession = new PoaiecRun("unknown");
            subSession.loadFromXml(doc);

            setSeriesEvaluationCount(evaluations + subSession.evaluations);
            setSeriesArchiveCount(subSession.archiveSize);
            setSeriesTimeElapsed(timeElapsed + subSession.timeElapsed);
            userActionLog.addAll(subSession.userActionLog);
//            archivedPoints.addAll(subSession.archivedPoints);
//            allPoints.addAll(subSession.allPoints);
        }
    }

    public void loadFromXml(Document doc) {

        Node series = doc.getFirstChild();
        if (series.getNodeName().equalsIgnoreCase(IEC_SERIES_TAG)) {

            if (series.getAttributes().getNamedItem(BRANCHED_FROM_TAG) != null) {
                if (series.getAttributes().getNamedItem(BRANCHED_FROM_TAG).getNodeValue().matches("[0-9]+")) {
                    setSeriesBranchedFrom(Long.parseLong(series.getAttributes().getNamedItem(BRANCHED_FROM_TAG).getNodeValue()));
                }
            }

            Node child = series.getFirstChild();
            while (child != null) {
                if (child.getNodeName().equalsIgnoreCase(EVALUATION_COUNT_TAG)) {
                    setSeriesEvaluationCount(Integer.parseInt(child.getTextContent()));
                }

                if (child.getNodeName().equalsIgnoreCase(ARCHIVE_TAG)) {
                    setSeriesArchiveCount(Integer.parseInt(child.getTextContent()));
                }

                if (child.getNodeName().equalsIgnoreCase(TIME_ELAPSED_TAG)) {
                    setSeriesTimeElapsed(Long.parseLong(child.getTextContent()));
                }

                if (child.getNodeName().equalsIgnoreCase(IEC_STEP_TAG)) {

                    Node step = child.getFirstChild();
                    while (step != null) {
                        if (step.getNodeName().equalsIgnoreCase(IecStep.IEC_STEP_TAG)) {
                            userActionLog.add(new IecStep(step));
                        }
                        step = step.getNextSibling();
                    }
                }

                if (child.getNodeName().equalsIgnoreCase(ALL_PTS_TAG)) {
//                    Node position = child.getFirstChild();
//                    while (position != null) {
//                        if (position.getNodeName().equalsIgnoreCase(Position.POSITION_TAG)) {
//                            allPoints.add(new Position(position));
//                        }
//                        position = position.getNextSibling();
//                    }
                }

                child = child.getNextSibling();
            }
        }

        /*
         * <series branchedFrom="709" > <evaluations>4786</evaluations>
         * <archive>1187</archive> <time>1345483</time> <iecSteps count="18" >
         * ... </iecSteps> </series>
         */

    }

    public HashMap<Long, Integer> fitnessAtEvaluation(int evaluationTarget) {
        HashMap<Long, Integer> champFitness = new HashMap<Long, Integer>();
        int evaluationCounter = 0;

        for (IecStep step : userActionLog) {
            if (step.getEvaluationCount() != null) {
                evaluationCounter += step.getEvaluationCount();
                champFitness = step.getChampFitness();
                if (evaluationCounter >= evaluationTarget) {
                    break;
                }
            }
        }
        return champFitness;
    }

    public int archiveSizeAtEvaluation(int evaluationTarget) {
        int archive = 0;
        int evaluationCounter = 0;

        for (IecStep step : userActionLog) {
            if (step.getEvaluationCount() != null) {
                evaluationCounter += step.getEvaluationCount();
                archive = step.getArchiveSize();
                if (evaluationCounter >= evaluationTarget) {
                    break;
                }
            }
        }
        return archive;
    }

    public double getSeriesPercentage(int i, StepType type) {
        if (i >= 10) {
            return 0.0;
        }

        double minEvaluation = i * evaluations / 10.0;
        double maxEvaluation = (i + 1) * evaluations / 10.0;
        int stepCount = 0;
        int allCount = 0;

        int currentEvaluation = 0;
        while (currentEvaluation < maxEvaluation) {
            if (currentEvaluation >= minEvaluation) {
                IecStep step = getStepAtEvaluation(currentEvaluation);
                switch (step.getAction()) {
                    case STEP:
                        if (step.getAction().equals(type)) {
                            stepCount++;
                        }
                        allCount++;
                        break;
                    case NOVELTY:
                        if (step.getAction().equals(type)) {
                            stepCount++;
                        }
                        allCount++;
                        break;
                    case FITNESS:
                        if (step.getAction().equals(type)) {
                            stepCount++;
                        }
                        allCount++;
                        break;
                    default:
                        break;
                }
            }
            currentEvaluation++;
        }
        return (double) stepCount / allCount;
    }

    private IecStep getStepAtEvaluation(int currentEvaluation) {
        int anEvaluation = 0;
        IecStep lastStep = null;
        for (IecStep step : userActionLog) {
            if (step.getEvaluationCount() != null) {
                lastStep = step;
                anEvaluation += step.getEvaluationCount();
            }
            if (anEvaluation > currentEvaluation) {
                return step;
            }
        }
        return lastStep;
    }

    private String open(String label) {
        return "<" + label + ">\n";
    }

    private String open(String label, String attributes) {
        return new StringBuilder().append("<").append(label).append(" ").append(attributes).append(">\n").toString();
    }

    private String close(String label) {
        return "</" + label + ">\n";
    }

    private String textContentElement(String label, Object value) {
        return "<" + label + ">" + value.toString() + "</" + label + ">\n";
    }

    private String indent(int x) {
        StringBuilder indention = new StringBuilder();
        for (int i = 0; i < x; i++) {
            indention.append("    ");
        }
        return indention.toString();
    }
    public final static String ID_TAG = "id";
    public final static String IEC_SERIES_TAG = "series";
    public final static String BRANCHED_FROM_TAG = "branchedFrom";
    public final static String EVALUATION_COUNT_TAG = "evaluations";
    public final static String ARCHIVE_TAG = "archive";
    public final static String TIME_ELAPSED_TAG = "time";
    public final static String LINEAGE_TAG = "lineage";
    public final static String IEC_STEP_TAG = "iecSteps";
    public final static String ALL_PTS_TAG = "allPoints";
    public final static String POPULATION_TAG = "population";
    public final static String COUNT_TAG = "count";
}
