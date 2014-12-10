/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anji.hyperneat;

import com.anji.integration.Activator;
import com.anji.integration.AnjiActivator;
import com.anji.neat.NeatChromosomeUtility;
import com.anji.neat.NeuronAllele;
import com.anji.neat.NeuronType;
import com.anji.nn.*;
import java.util.*;
import org.jgap.Allele;
import org.jgap.Chromosome;
import org.jgap.ChromosomeMaterial;

/**
 *
 * @author Brian Woolley <brian.woolley at ieee.org>
 */
public class HyperNeatChromosomeUtility extends NeatChromosomeUtility {

    /**
     * 
     * @param cppnChromMat
     * @param config
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param bias  
     */
    public static void addLinkExpressionOutput(ChromosomeMaterial cppnChromMat, 
            HyperNeatConfiguration config, NeuronAllele bias) {

        SortedSet<Allele> cppnAlleles = cppnChromMat.getAlleles();
        
        // Add a step function NodeType.OUTPUT
        NeuronAllele dx = config.newNeuronAllele(NeuronType.INPUT, ActivationFunctionType.LINEAR);
        NeuronAllele dy = config.newNeuronAllele(NeuronType.INPUT, ActivationFunctionType.LINEAR);
        NeuronAllele leo = config.newNeuronAllele(NeuronType.OUTPUT, ActivationFunctionType.SIGNED_STEP);
        NeuronAllele xGauss = config.newNeuronAllele(NeuronType.HIDDEN, ActivationFunctionType.SIGNED_GAUSSIAN);
        NeuronAllele yGauss = config.newNeuronAllele(NeuronType.HIDDEN, ActivationFunctionType.SIGNED_GAUSSIAN);
        
        cppnAlleles.add(dx);
        cppnAlleles.add(dy);
        cppnAlleles.add(leo);
        cppnAlleles.add(xGauss);
        cppnAlleles.add(yGauss);
        
        double xBias = (config.getLeoXbias() > 0.0) ? config.getLeoXbias() : 1.0;
        double yBias = (config.getLeoYbias() > 0.0) ? config.getLeoYbias() : 1.0;
        cppnAlleles.add(config.newConnectionAllele(dx.getInnovationId(), xGauss.getInnovationId(), xBias));
        cppnAlleles.add(config.newConnectionAllele(dy.getInnovationId(), yGauss.getInnovationId(), yBias));

        cppnAlleles.add(config.newConnectionAllele(xGauss.getInnovationId(), leo.getInnovationId(), 1.0));
        cppnAlleles.add(config.newConnectionAllele(yGauss.getInnovationId(), leo.getInnovationId(), 1.0));
        cppnAlleles.add(config.newConnectionAllele(bias.getInnovationId(), leo.getInnovationId()));
    }

    /**
     * 
     * @param chrom
     * @return 
     */
    public static Activator generateCPPN(Chromosome chrom) {
        return generateANN(chrom);
    }

    /**
     *
     * @param config
     * @param cppnChrom 
     * @return
     */
    public static Activator generateHypercubeANN(HyperNeatConfiguration config, Chromosome cppnChrom) {
        Activator cppn = generateCPPN(cppnChrom);

        if (config.getLayerCount() < 2) {
            // There must be at lease two layers, an input layer and an output layer
            return null;
        }

        int i = 0;
        Map<Integer, Sheet> allSheets = new HashMap<Integer, Sheet>();
        Map<Integer, Sheet> hiddenSheets = new HashMap<Integer, Sheet>();

        // Create the input sheet
        Sheet inputSheet = createNewSheet(config.getLayer(i));
        allSheets.put(i, inputSheet);

        // Create the hidden layers, if any 
        for (i = 1; i < config.getLayerCount() - 1; i++) {
            Sheet hiddenSheet = createNewSheet(config.getLayer(i));
            hiddenSheets.put(i, hiddenSheet);
            allSheets.put(i, hiddenSheet);
        }

        // Create the output sheet
        Sheet outputSheet = createNewSheet(config.getLayer(i));
        allSheets.put(i, outputSheet);

        List<Neuron> someInNeurons = inputSheet.getNeurons();
        List<Neuron> someHiddenNeurons = new ArrayList<Neuron>();
        for (Sheet hiddenSheet : hiddenSheets.values()) {
            someHiddenNeurons.addAll(hiddenSheet.getNeurons());
        }
        List<Neuron> someOutNeurons = outputSheet.getNeurons();

        Collection<Neuron> someNeurons = new ArrayList<Neuron>();
        someNeurons.addAll(someInNeurons);
        someNeurons.addAll(someHiddenNeurons);
        someNeurons.addAll(someOutNeurons);

        Collection<CacheNeuronConnection> someRecurrentConns = new ArrayList<CacheNeuronConnection>();
        CacheNeuronConnection link;
        double scaling = config.getMaxConnectionWeight();
        double threshold = config.getLinkExpressionThreshold();

        for (LayerBinding edge : config.getLinks()) {
            // create connections between two sheets:  O(n^2)
            for (Point in : allSheets.get(edge.from.layerId).getAllPoints()) {
                for (Point out : allSheets.get(edge.to.layerId).getAllPoints()) {

                    double[] input = new double[cppn.getInputDimension()];

                    input[ 0] = in.x;
                    input[ 1] = in.y;
                    input[ 2] = out.x;
                    input[ 3] = out.y;
                    input[ 4] = 1; // bias
                    if (config.isLEOenabled()) {
                        input[ 5] = in.x - out.x;
                        input[ 6] = in.y - out.y;
                    }

                    // Activate CPPN to find connection weight
                    double[] response = cppn.next(input);
                    double wt = response[edge.cppnOutput];

                    // Express connections based on weight or LEO
                    if (config.isLEOenabled()) {
                        if (response[config.getLEOnode()] > 0) {
                            // Express LEO connection
                            link = new CacheNeuronConnection(in.neuron, wt * scaling);
                            out.neuron.addIncomingConnection(link);
                            someRecurrentConns.add(link);
                        }
                    } else {
                        if (wt < -threshold || wt > threshold) {
                            // Express connection weights > 0.2
                            link = new CacheNeuronConnection(in.neuron, wt * scaling);
                            out.neuron.addIncomingConnection(link);
                            someRecurrentConns.add(link);
                        }
                    }

                }
            }
        }
        
        AnjiNet net = new AnjiNet(someNeurons, someInNeurons, someOutNeurons, someRecurrentConns, "hypercubeANN" + cppn.toString());
        return new AnjiActivator(net, 1);
    }

    private static Sheet createNewSheet(Layer layer) {
        ActivationFunctionFactory activationFunctions = ActivationFunctionFactory.getInstance();
        ActivationFunction activationFunction = activationFunctions.get(layer.activation);

        if (layer.type.equalsIgnoreCase("cartesian")) {
            return new CartesianSheet(layer.x, layer.y, activationFunction);
        } else if (layer.type.equalsIgnoreCase("radial")) {
            return new RadialSheet(layer.x, activationFunction);
        } else {
            throw new UnsupportedOperationException(layer.type + " not yet implemented");
        }
    }

}
