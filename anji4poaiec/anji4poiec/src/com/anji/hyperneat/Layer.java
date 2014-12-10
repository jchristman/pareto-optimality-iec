/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anji.hyperneat;

import com.anji.util.Properties;

/**
 *
 * @author Brian Woolley <brian.woolley at ieee.org>
 */
public class Layer {
    protected final int layerId;
    protected final String activation;
    protected final String type;
    protected final int x;
    protected final int y;

    private String layerKey = "hyperneat.substrate.layer";
    private String activationKey = "activation";
    private String typeKey = "type";
    private String xDimKey = "x";
    private String yDimKey = "y";
    

    protected Layer(Properties props, int i) {
        layerKey = layerKey + i + ".";

        layerId = i;
        activation = props.getProperty(layerKey + activationKey, "sigmoid");
        type = props.getProperty(layerKey + typeKey, "cartesian");
        x = props.getIntProperty(layerKey + xDimKey, 2);
        y = props.getIntProperty(layerKey + yDimKey, 2);
    }
}
