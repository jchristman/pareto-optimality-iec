/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jgap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.w3c.dom.Node;

/**
 *
 * @author Brian Woolley <brian.woolley at ieee.org>
 */
public class BehaviorVector extends ArrayList<Double> {
    private static final long serialVersionUID = 2084739851754987469L;
    public static final String XML_TAG = "behavior";

    /**
     * 
     */
    public BehaviorVector() {}
    
    /**
     * 
     * @param behavior 
     */
    public BehaviorVector(double[] behavior) {
        for (int i=0; i<behavior.length; i++) {
            this.add(behavior[i]);
        }
    }
    
    /**
     * 
     * @param aBehaviorVector 
     */
    public BehaviorVector(BehaviorVector aBehaviorVector) {
        this(aBehaviorVector.getValues());
    }
    
    public BehaviorVector(Node behaviorNode) {
        
        Node dimensionNode = behaviorNode.getFirstChild();
        Map<String, Double> dimensions = new HashMap<String, Double>();
        while (true) {
            if (dimensionNode.getNodeName().matches("d[0-9]++")) {
                dimensions.put(dimensionNode.getNodeName(), Double.parseDouble(dimensionNode.getTextContent()));
            }

            dimensionNode = dimensionNode.getNextSibling();
            if (dimensionNode == null) {
                break;
            }
        }
        
        for (int i = 0; i<dimensions.size(); i++) {
            this.add(i, dimensions.get("d"+i));
        }
    }

    /**
     * 
     * @return 
     */
    public double[] getValues() {
        double[] result = new double[this.size()];
        for (int i=0; i<this.size(); i++) {
            result[i] = this.get(i);
        }
        return result;
    }

    /**
     * Computes the euclidian distance between behavior vectors of the same length.
     * @param other the other BehaviorVector
     * @return the Euclidian distance or -1 if the ordinal lengths are not equal
     */
    public double distanceEuclidian(BehaviorVector other) {
        if (this.size() != other.size()) {            
            return -1;
        }
        
        double dist = 0;
        for (int i=0; i<this.size(); i++) {
            dist += square(this.get(i) - other.get(i));
        }
        return Math.sqrt(dist);
    }
    
    private double square(double x) {
        return Math.pow(x, 2);
    }

    public BehaviorVector deconflict(long seed) {
        BehaviorVector result = new BehaviorVector(this);
        result.add(new Random(seed).nextDouble() - 0.5);
        return result;
    }
}
