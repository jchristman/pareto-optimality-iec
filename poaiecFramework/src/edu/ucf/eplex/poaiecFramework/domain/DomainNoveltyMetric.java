/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.poaiecFramework.domain;

import com.anji.util.Properties;

import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jgap.BehaviorVector;


/**
 *
 * @param <Behavior>
 * @author Brian Woolley (brian.woolley at ieee.org)
 */
public abstract class DomainNoveltyMetric<Behavior> {

    private int evaluations = 0;
    private int newArchiveMembers = 0;
    private LinkedList<Candidate> subjects = new LinkedList<Candidate>();
    private Map<Candidate, Behavior> evaluated = new HashMap<Candidate, Behavior>();
    private Map<Candidate, Behavior> archive = new HashMap<Candidate, Behavior>();
    private KDTree<Candidate> behaviorTree;
    private int k = DEFAULT_KNN_VALUE;
    private int minNovelty = DEFAULT_MIN_NOVELTY;
    private int maxNovelty = DEFAULT_MAX_NOVELTY;
    private double threshold = DEFAULT_INITIAL_THRESHOLD;
    private int adjustmentRate = DEFAULT_THRESHOLD_ADJUSTMENT_VALUE;
    private int maxArchiveSize = DEFAULT_MAX_ARCHIVE_SIZE;
    private int dimensions = DEFAULT_BEHAVOR_SPACE_DIMENSIONALITY;
    @SuppressWarnings("unused")
	private long timer = 0;

    public DomainNoveltyMetric(Properties props) {
        k = props.getIntProperty(KNN_VALUE_KEY, DEFAULT_KNN_VALUE);
        minNovelty = props.getIntProperty(MIN_NOVELTY_KEY, DEFAULT_MIN_NOVELTY);
        maxNovelty = props.getIntProperty(MAX_NOVELTY_KEY, DEFAULT_MAX_NOVELTY);
        threshold = props.getDoubleProperty(INITIAL_THRESHOLD_VALUE_KEY, DEFAULT_INITIAL_THRESHOLD);
        adjustmentRate = props.getIntProperty(THRESHOLD_ADJUSTMENT_KEY, DEFAULT_THRESHOLD_ADJUSTMENT_VALUE);
        maxArchiveSize = props.getIntProperty(MAX_ARCHIVE_SIZE_KEY, DEFAULT_MAX_ARCHIVE_SIZE);
        dimensions = props.getIntProperty(BEHAVOR_SPACE_DIMENSIONALITY_KEY, DEFAULT_BEHAVOR_SPACE_DIMENSIONALITY);
        behaviorTree = new KDTree<Candidate>(dimensions + 2); // FIXME: Theta?
    }

    public abstract BehaviorVector computeBehaviorVector(Behavior aBehavior);

    /**
     * This private method differentiates candidates that have the same behavior
     * by adding an additional dimension to the behavior vector that is based on
     * the Candidate's ID.  The additional dimension is computed by seeding a random
     * number generator with the Candidate's ID and then taking the first double
     * value.  In this way, a small pseudo-random value, between zero (0) and one (1)
     * can differentiates candidates that have the same behavior without impacting
     * the overall distance between behaviors.  Furthermore, this additional dimension
     * can be recreated because it was seeded by a known value, i.e. the candidate ID.
     * 
     * @param subject The @link<Candidate> that created a behavior
     * @param aBehavior The behavior of the @link<Candidate>
     * @return The expanded behavior vector with one additional dimension
     */
    private double[] computeBehaviorVector(Candidate subject, Behavior aBehavior) {
        BehaviorVector behaviorVector = computeBehaviorVector(aBehavior);

        Random rand = new Random(subject.getId());
        behaviorVector.add(rand.nextDouble());
        return behaviorVector.getValues();
    }

    public Map<Candidate, Integer> score(Map<Candidate, Behavior> population) {
        long start = System.currentTimeMillis();
        Map<Candidate, Integer> results = new HashMap<Candidate, Integer>();

        double[] location;

        // Calling removeAll and then addAll keeps 
        // duplicates from entering the subject list.
        // Additionally, this approach keeps the newest
        // individuals at the end of the linked list.
        subjects.removeAll(population.keySet());
        subjects.addAll(population.keySet());
        
        while (subjects.size() > maxArchiveSize) {
        	// Remove the oldest (LRU) subject
            subjects.removeFirst();
        }
        
        // Remove evaluated individuals if they are not in the population.
        // evaluated.keySet().retainAll(population.keySet());
        evaluated.keySet().retainAll(subjects);
        archive.keySet().retainAll(subjects);
        rebuildBehaivorTree();

        // Add new members to the behiavorTree and the evaluated list.
        for (Candidate subject : population.keySet()) {
            if (!evaluated.containsKey(subject) && !archive.containsKey(subject)) {
                try {
                    location = computeBehaviorVector(subject, population.get(subject));
                    behaviorTree.insert(location, subject);
                    evaluated.put(subject, population.get(subject));
                    evaluations++;
                } catch (KeySizeException ex) {
                    Logger.getLogger(DomainNoveltyMetric.class.getName()).log(Level.SEVERE, null, ex);
                } catch (KeyDuplicateException ex) {
                    // Do nothing
                }
            }
        }

        // Compute the novelty score for each individual in the population
        for (Candidate subject : population.keySet()) {
        	double score = getNoveltyScore(subject, population);
        	results.put(subject, (int)Math.round(score));
        }
        timer = System.currentTimeMillis() - start;
//        System.out.println("INFO  NoveltyMetric: [time elapsed == " + timer + "mS] [archive size == " + archive.size() + "] [threshold == " + Math.round(threshold * 100) / 100.0 + "]");
        return results;
    }
    
    public double getNoveltyScore(Candidate subject, Map<Candidate, Behavior> population) {
        double[] location;
        double score = 0.0;
	    try {
	        location = computeBehaviorVector(subject, population.get(subject));
	
	        Collection<Candidate> nearestNeighbors = behaviorTree.nearest(location, k);
	        if (nearestNeighbors.isEmpty()) {
	            score = threshold;
	        } else {
	            double[] other;
	            for (Candidate neighbor : nearestNeighbors) {
	                if (evaluated.containsKey(neighbor)) {
	                    other = computeBehaviorVector(neighbor, evaluated.get(neighbor));
	                } else {
	                    other = computeBehaviorVector(neighbor, archive.get(neighbor));
	                }
	                score += distEuclidian(location, other);
	            }
	        }
	        score /= nearestNeighbors.size();
	        score = Math.max(Math.min(score, maxNovelty - 1), minNovelty);
	
	        // Add this point to the archive if it exceeds the novelty threshold
	        // and the Chromosome is not already in the archive
	        if (score >= threshold) {
	            if (!archive.containsKey(subject)) {
	                // Rather than putting it into the behaviorTree, removing it
	                // from the evaluated list keeps it in the behaivorTree.
	                archive.put(subject, evaluated.remove(subject));                        
	                newArchiveMembers++;
	            }
	        }
	
	        // Novelty Metric Maintenance
	        if (evaluations > adjustmentRate) {
	            if (newArchiveMembers == 0) {
	                threshold -= 0.05 * threshold;
	            }
	            if (newArchiveMembers > 4) {
	                threshold += 0.05 * threshold;
	            }
	            newArchiveMembers = 0;
	            evaluations = 0;
	//                    System.out.println("INFO  NoveltyMetric: [time elapsed == " + timer + "mS] [archive size == " + archive.size() + "] [threshold == " + Math.round(threshold * 100) / 100.0 + "]");
	        }
	    } catch (KeySizeException ex) {
	        Logger.getLogger(DomainNoveltyMetric.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (IllegalArgumentException ex) {
	        Logger.getLogger(DomainNoveltyMetric.class.getName()).log(Level.SEVERE, null, ex);
	    }
    	
	    return score;
    }

    public double getNoveltyThreshold() {
        return threshold;
    }

    public void setNoveltyThreshold(double aNewNoveltyThreshold) {
        threshold = Math.max(aNewNoveltyThreshold, 0.0);
    }

    public int getArchiveSize() {
        return archive.size();
    }

    private void rebuildBehaivorTree() {
        double[] location;
        behaviorTree = new KDTree<Candidate>(dimensions + 2);
        try {
            for (Candidate subject : archive.keySet()) {
                location = computeBehaviorVector(subject, archive.get(subject));
                behaviorTree.insert(location, subject);
            }
            for (Candidate subject : evaluated.keySet()) {
                location = computeBehaviorVector(subject, evaluated.get(subject));
                behaviorTree.insert(location, subject);
            }
            
        } catch (KeySizeException ex) {
            Logger.getLogger(DomainNoveltyMetric.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyDuplicateException ex) {
            Logger.getLogger(DomainNoveltyMetric.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private double distEuclidian(double[] a, double[] b) {
        double dist = 0;
        if (a.length == b.length) {
            for (int i = 0; i < a.length; i++) {
                dist += (a[i] - b[i]) * (a[i] - b[i]);
            }
        }
        return Math.sqrt(dist);
    }

    public Collection<Behavior> getEvaluatedBehaviors() {
        return evaluated.values();
    }
    
    public Collection<Behavior> getArchivedBehaviors() {
        return archive.values();
    }
    
    private static String KNN_VALUE_KEY = "novelty.knn.value";
    private static String INITIAL_THRESHOLD_VALUE_KEY = "novelty.archive.threshold.initial";
    private static String THRESHOLD_ADJUSTMENT_KEY = "novelty.archive.threshold.adjust";
    private static String MAX_ARCHIVE_SIZE_KEY = "novelty.archive.max.size";
    private static String BEHAVOR_SPACE_DIMENSIONALITY_KEY = "novelty.behavior.space.dimensionality";
    private static String MIN_NOVELTY_KEY = "novelty.min.score";
    private static String MAX_NOVELTY_KEY = "novelty.max.score";
    private static Integer DEFAULT_KNN_VALUE = 15;
    private static Double DEFAULT_INITIAL_THRESHOLD = 3.0;
    private static Integer DEFAULT_THRESHOLD_ADJUSTMENT_VALUE = 15;
    private static Integer DEFAULT_MAX_ARCHIVE_SIZE = 500;
    private static Integer DEFAULT_BEHAVOR_SPACE_DIMENSIONALITY = 2;
    private static Integer DEFAULT_MIN_NOVELTY = 1;
    private static Integer DEFAULT_MAX_NOVELTY = 300;
}
