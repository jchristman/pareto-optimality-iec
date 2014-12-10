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
 * Created on 20 October 2009 by Brian Woolley
 */
package com.anji.nn;

/**
 * Gaussian activation function.
 *
 * @author Brian Woolley
 */
public class SignedGaussianActivationFunction implements ActivationFunction {

    /**
     * identifying string
     */
    public final static String NAME = ActivationFunctionType.SIGNED_GAUSSIAN.toString();

    /**
     * @return @see Object#toString()
     */
    @Override
    public String toString() {
        return NAME;
    }

    /**
     * This class should only be instantiated by the ActivationFunction
     * enumeration.
     *
     * @see com.anji.activationFunction.ActivationFunction
     */
    protected SignedGaussianActivationFunction() {
    }

    /**
     * Return
     * <code>input</code> with no transformation.
     *
     * @see com.anji.activationFunction.ActivationFunctionStrategy#apply(double)
     */
    @Override
    public double apply(double input) {
        return (2 * Math.exp(-Math.pow(input, 2))) - 1;
    }

    /**
     * @see com.anji.activationFunction.ActivationFunctionStrategy#getMaxValue()
     */
    @Override
    public double getMaxValue() {
        return 1.0;
    }

    /**
     * @see com.anji.activationFunction.ActivationFunctionStrategy#getMinValue()
     */
    @Override
    public double getMinValue() {
        return -1.0;
    }

    /**
     * @see com.anji.activationFunction.ActivationFunctionStrategy#cost()
     */
    @Override
    public long cost() {
        return 42;
    }
}
