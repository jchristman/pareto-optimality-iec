/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anji.hyperneat;

/**
 *
 * @author Brian Woolley <brian.woolley at ieee.org>
 */
public class LayerBinding {
    protected final Layer from;
    protected final Layer to;
    protected final int cppnOutput;

    protected LayerBinding(Layer from, Layer to, int cppnOutputNode) {
        this.from = from;
        this.to = to;
        this.cppnOutput = cppnOutputNode;
        System.out.println("Mapping from layer " + from.layerId + " to layer " + to.layerId + " defined by CPPN output " + cppnOutputNode);
    }
}
