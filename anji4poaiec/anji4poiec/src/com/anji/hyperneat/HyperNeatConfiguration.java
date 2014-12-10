/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anji.hyperneat;

import com.anji.integration.Activator;
import com.anji.neat.NeatConfiguration;
import com.anji.neat.NeuronAllele;
import com.anji.neat.NeuronGene;
import com.anji.neat.NeuronType;
import com.anji.nn.ActivationFunctionType;
import com.anji.util.Properties;
import java.util.*;
import org.jgap.Chromosome;
import org.jgap.ChromosomeMaterial;
import org.jgap.InvalidConfigurationException;

/**
 *
 * @author Brian Woolley <brian.woolley at ieee.org>
 */
public class HyperNeatConfiguration extends NeatConfiguration {

    private static final long serialVersionUID = 1867424085938719764L;
    
    private final String LAYER_COUNT_KEY = "hyperneat.substrate.layer.count";
    private final int DEFAULT_LAYER_COUNT = 2;
    private int layerCount = DEFAULT_LAYER_COUNT;
    private final Map<Integer, Layer> layers = new HashMap<Integer, Layer>();
    private final Collection<LayerBinding> links = new ArrayList<LayerBinding>();

    /**
     *
     * @return
     */
    public int getLayerCount() {
        return layers.size();
    }

    /**
     *
     * @param layerId
     * @return
     */
    public Layer getLayer(int layerId) {
        return layers.get(layerId);
    }
    
    public Collection<LayerBinding> getLinks() {
        return new ArrayList<LayerBinding>(links);
    }

    private Integer leoOutput = null;
    private Integer leoDx = null;
    private Integer leoDy = null;
    private final String LEO_ENABLED_KEY = "hyperneat.leo.enabled";
    private final boolean DEFAULT_LEO_ENABLED = false;
    private boolean leoEnabled = DEFAULT_LEO_ENABLED;
    
    /**
     * 
     * @return 
     */
    public boolean isLEOenabled() {
        return leoEnabled;
    }
    
    
    private final String LEO_X_BIAS_KEY = "hyperneat.leo.locality.bias.x";
    private final double DEFAULT_LEO_X_BIAS = 1.0;
    private double leoXbias = DEFAULT_LEO_X_BIAS;
    
    public double getLeoXbias() {
        return leoXbias;
    }
    
    private final String LEO_Y_BIAS_KEY = "hyperneat.leo.locality.bias.y";
    private final double DEFAULT_LEO_Y_BIAS = 1.0;
    private double leoYbias = DEFAULT_LEO_Y_BIAS;
    
    public double getLeoYbias() {
        return leoYbias;
    }
    
    public HyperNeatConfiguration(Properties newProps) throws InvalidConfigurationException {
        super(newProps);
        init(newProps);
    }

    private void init(Properties props) {
        layerCount = props.getIntProperty(LAYER_COUNT_KEY, DEFAULT_LAYER_COUNT);
        for (int i = 0; i < layerCount; i++) {
            layers.put(i, new Layer(props, i));
        }

        // put("hyperneat.substrate.connect0-->1", "cppn.output.0");
        for (Integer i : layers.keySet()) {
            for (Integer j : layers.keySet()) {
                try {
                    String layerBinding = props.getProperty("hyperneat.substrate.connect" + i + "-->" + j);
                    if (layerBinding != null) {
                        if (layerBinding.matches("cppn\\.output\\.\\d+")) {
                            Layer from = layers.get(i);
                            Layer to = layers.get(j);
                            int cppnOutputNode = Integer.parseInt(layerBinding.substring(12));
                            links.add(new LayerBinding(from, to, cppnOutputNode)); // 
                        }
                    }
                } catch (IllegalArgumentException e) {
                }
            }
        }
        
        leoEnabled = props.getBooleanProperty(LEO_ENABLED_KEY, DEFAULT_LEO_ENABLED);
        if (leoEnabled) {
            leoXbias = props.getDoubleProperty(LEO_X_BIAS_KEY, DEFAULT_LEO_X_BIAS);
            leoYbias = props.getDoubleProperty(LEO_Y_BIAS_KEY, DEFAULT_LEO_Y_BIAS);
            ChromosomeMaterial cppnMat = getSampleChromosomeMaterial();
            List<NeuronAllele> in = HyperNeatChromosomeUtility.getNeuronList(cppnMat.getAlleles(), NeuronType.INPUT);
            leoOutput = HyperNeatChromosomeUtility.getNeuronList(cppnMat.getAlleles(), NeuronType.OUTPUT).size();
            leoDx = HyperNeatChromosomeUtility.getNeuronList(cppnMat.getAlleles(), NeuronType.INPUT).size();
            leoDy = HyperNeatChromosomeUtility.getNeuronList(cppnMat.getAlleles(), NeuronType.INPUT).size() + 1;
            HyperNeatChromosomeUtility.addLinkExpressionOutput(cppnMat, this, in.get(4));
        } else {
            linkExpresionThreshold = props.getDoubleProperty(LINK_EXPRESSION_THRESHOLD_KEY, DEFAULT_LINK_EXPRESSION_THRESHOLD);
        }
    }

    /**
     * 
     * @return 
     */
    public Integer getLEOnode() {
        return leoOutput;
    }

    /**
     * 
     * @return 
     */
    public Integer getLEOdx() {
        return leoDx;
    }

    /**
     * 
     * @return 
     */
    public Integer getLEOdy() {
        return leoDy;
    }

    private final String LINK_EXPRESSION_THRESHOLD_KEY = "hyperneat.link.expression.threshold";
    private final double DEFAULT_LINK_EXPRESSION_THRESHOLD = 0.2;
    private double linkExpresionThreshold = DEFAULT_LINK_EXPRESSION_THRESHOLD;

    /**
     * 
     * @return 
     */
    public double getLinkExpressionThreshold() {
        return linkExpresionThreshold;
    }
       
    /**
     * factory method to construct new neuron allele with unique innovation ID of specified
     * <code>type</code> and <code>activation</code>
     * 
     * @param type
     * @param act 
     * @return NeuronAllele
     */
    public NeuronAllele newNeuronAllele(NeuronType type, ActivationFunctionType act) {
        NeuronGene gene = new NeuronGene(type, nextInnovationId(), act);
        return new NeuronAllele(gene);
    }

    @Override
    public Activator createANN(Chromosome aChromosome) {
        return HyperNeatChromosomeUtility.generateHypercubeANN(this, aChromosome);
    }
}
