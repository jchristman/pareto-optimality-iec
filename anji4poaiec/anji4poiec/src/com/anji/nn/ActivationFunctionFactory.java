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
 * Created on Feb 27, 2004 by Philip Tucker
 */
package com.anji.nn;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory to hide implementation details of creating activation functions and
 * ensure there is always only one of each.
 *
 * @author Philip Tucker
 */
public class ActivationFunctionFactory {

    private Map<String, ActivationFunction> activationFunctions = new HashMap<String, ActivationFunction>();
    private static final ActivationFunctionFactory instance = new ActivationFunctionFactory();

    /**
     * singleton; initialize map of activation functions
     */
    private ActivationFunctionFactory() {
        super();
        activationFunctions.put(LinearActivationFunction.NAME, new LinearActivationFunction());
        activationFunctions.put(SigmoidActivationFunction.NAME, new SigmoidActivationFunction());
        activationFunctions.put(SignedSigmoidActivationFunction.NAME, new SignedSigmoidActivationFunction());
        activationFunctions.put(TanhActivationFunction.NAME, new TanhActivationFunction());
        activationFunctions.put(TanhCubicActivationFunction.NAME, new TanhCubicActivationFunction());
        activationFunctions.put(EvSailSigmoidActivationFunction.NAME, new EvSailSigmoidActivationFunction());
        activationFunctions.put(InverseAbsActivationFunction.NAME, new InverseAbsActivationFunction());
        activationFunctions.put(StepActivationFunction.NAME, new StepActivationFunction());
        activationFunctions.put(SignedStepActivationFunction.NAME, new SignedStepActivationFunction());
        activationFunctions.put(ClampedLinearActivationFunction.NAME, new ClampedLinearActivationFunction());
        activationFunctions.put(SignedClampedLinearActivationFunction.NAME, new SignedClampedLinearActivationFunction());
        activationFunctions.put(GaussianActivationFunction.NAME, new GaussianActivationFunction());
        activationFunctions.put(SignedGaussianActivationFunction.NAME, new SignedGaussianActivationFunction());
        activationFunctions.put(SineActivationFunction.NAME, new SineActivationFunction());
        activationFunctions.put(CosineActivationFunction.NAME, new CosineActivationFunction());
    }

    /**
     * @return singleton instance
     */
    public static ActivationFunctionFactory getInstance() {
        return instance;
    }

    /**
     * @param key ID of activation function; these are defined in each concrete
     * <code>ActivationFunction</code> class; e.g.,
     * <code>SigmoidActivationFunction</code>,
     * <code>LinearActivationFunction</code>, and
     * <code>TanhActivationFunction</code>.
     * @return ActivationFunction
     */
    public ActivationFunction get(String key) {
        return activationFunctions.get(key);
    }

    /**
     * @return linear activation function
     */
    public ActivationFunction getLinear() {
        return activationFunctions.get(LinearActivationFunction.NAME);
    }

    /**
     * @return sigmoid activation function
     */
    public ActivationFunction getStep() {
        return activationFunctions.get(StepActivationFunction.NAME);
    }

    /**
     * @return sigmoid activation function
     */
    public ActivationFunction getSignedStep() {
        return activationFunctions.get(SignedStepActivationFunction.NAME);
    }

    /**
     * @return sigmoid activation function
     */
    public ActivationFunction getSigmoid() {
        return activationFunctions.get(SigmoidActivationFunction.NAME);
    }

    /**
     * @return signed-sigmoid activation function
     */
    public ActivationFunction getSignedSigmoid() {
        return activationFunctions.get(SignedSigmoidActivationFunction.NAME);
    }

    /**
     * @return sigmoid approximation activation function
     */
    public ActivationFunction getEvSailSigmoid() {
        return activationFunctions.get(EvSailSigmoidActivationFunction.NAME);
    }

    /**
     * @return hyperbolic tangent activation function
     */
    public ActivationFunction getTanh() {
        return activationFunctions.get(TanhActivationFunction.NAME);
    }

    /**
     * @return hyperbolic tangent of cubic activation function
     */
    public ActivationFunction getTanhCubic() {
        return activationFunctions.get(TanhCubicActivationFunction.NAME);
    }

    /**
     * @return inverse absolute value activation function
     */
    public ActivationFunction getInverseAbs() {
        return activationFunctions.get(InverseAbsActivationFunction.NAME);
    }

    /**
     * @return clamped linear
     */
    public ActivationFunction getClampedLinear() {
        return activationFunctions.get(ClampedLinearActivationFunction.NAME);
    }

    /**
     * @return signed clamped linear
     */
    public ActivationFunction getSignedClampedLinear() {
        return activationFunctions.get(SignedClampedLinearActivationFunction.NAME);
    }

    /**
     * @return Gaussian
     */
    public ActivationFunction getGaussian() {
        return activationFunctions.get(GaussianActivationFunction.NAME);
    }

    /**
     * @return sine
     */
    public ActivationFunction getSine() {
        return activationFunctions.get(SineActivationFunction.NAME);
    }

    /**
     * @return cosine
     */
    public ActivationFunction getCosine() {
        return activationFunctions.get(CosineActivationFunction.NAME);
    }
}
