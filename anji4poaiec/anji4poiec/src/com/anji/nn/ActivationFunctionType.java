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
 * created by Philip Tucker on Jun 4, 2003
 */
package com.anji.nn;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerated type representing flavors of neurons: input, output, hidden.
 * Values returned in
 * <code>toString()</code> correspond to values in <a
 * href="http://nevt.sourceforge.net/">NEVT </a> XML data model.
 *
 * @author Philip Tucker
 */
public class ActivationFunctionType {

    /**
     * for hibernate
     */
    private Long id;
    private String name = null;
    private static Map<String, ActivationFunctionType> types = null;
    /**
     * cppn
     */
    public final static ActivationFunctionType CPPN = new ActivationFunctionType("cppn");
    /**
     * linear
     */
    public final static ActivationFunctionType LINEAR = new ActivationFunctionType("linear");
    /**
     * sigmoid
     */
    public final static ActivationFunctionType SIGMOID = new ActivationFunctionType("sigmoid");
    /**
     * signed sigmoid
     */
    public final static ActivationFunctionType SIGNED_SIGMOID = new ActivationFunctionType("signed.sigmoid");
    /**
     * tanh
     */
    public final static ActivationFunctionType TANH = new ActivationFunctionType("tanh");
    /**
     * tanh cubic
     */
    public final static ActivationFunctionType TANH_CUBIC = new ActivationFunctionType("tanh-cubic");
    /**
     * clamped linear
     */
    public final static ActivationFunctionType CLAMPED_LINEAR = new ActivationFunctionType("clamped-linear");
    /**
     * signed clamped linear
     */
    public final static ActivationFunctionType SIGNED_CLAMPED_LINEAR = new ActivationFunctionType("signed-clamped-linear");
    /**
     * clamped linear
     */
    public final static ActivationFunctionType STEP = new ActivationFunctionType("step");
    /**
     * clamped linear
     */
    public final static ActivationFunctionType SIGNED_STEP = new ActivationFunctionType("signed-step");
    /**
     * Gaussian
     */
    public final static ActivationFunctionType GAUSSIAN = new ActivationFunctionType("gaussian");
    /**
     * Gaussian
     */
    public final static ActivationFunctionType SIGNED_GAUSSIAN = new ActivationFunctionType("signed-gaussian");
    /**
     * sine
     */
    public final static ActivationFunctionType SINE = new ActivationFunctionType("sine");
    /**
     * cosine
     */
    public final static ActivationFunctionType COSINE = new ActivationFunctionType("cosine");

    /**
     * @param newName id of type
     */
    private ActivationFunctionType(String newName) {
        name = newName;
    }

    /**
     * @param name id of type
     * @return
     * <code>ActivationFunctionType</code> enumerated type corresponding to
     * <code>name</code>
     */
    public static ActivationFunctionType valueOf(String name) {
        if (types == null) {
            types = new HashMap<String, ActivationFunctionType>();
            types.put(ActivationFunctionType.CPPN.toString(), ActivationFunctionType.CPPN);
            types.put(ActivationFunctionType.LINEAR.toString(), ActivationFunctionType.LINEAR);
            types.put(ActivationFunctionType.SIGMOID.toString(), ActivationFunctionType.SIGMOID);
            types.put(ActivationFunctionType.SIGNED_SIGMOID.toString(), ActivationFunctionType.SIGNED_SIGMOID);
            types.put(ActivationFunctionType.TANH.toString(), ActivationFunctionType.TANH);
            types.put(ActivationFunctionType.TANH_CUBIC.toString(), ActivationFunctionType.TANH_CUBIC);
            types.put(ActivationFunctionType.CLAMPED_LINEAR.toString(), ActivationFunctionType.CLAMPED_LINEAR);
            types.put(ActivationFunctionType.SIGNED_STEP.toString(), ActivationFunctionType.SIGNED_STEP);
            types.put(ActivationFunctionType.SIGNED_CLAMPED_LINEAR.toString(), ActivationFunctionType.SIGNED_CLAMPED_LINEAR);
            types.put(ActivationFunctionType.GAUSSIAN.toString(), ActivationFunctionType.GAUSSIAN);
            types.put(ActivationFunctionType.SIGNED_GAUSSIAN.toString(), ActivationFunctionType.SIGNED_GAUSSIAN);
            types.put(ActivationFunctionType.SINE.toString(), ActivationFunctionType.SINE);
            types.put(ActivationFunctionType.COSINE.toString(), ActivationFunctionType.COSINE);
        }
        return types.get(name);
    }

    /**
     * @param o 
     * @return 
     * @see Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        return (this == o);
    }

    /**
     * @return 
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * define this so objects may be used in hash tables
     *
     * @return 
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * for hibernate
     *
     * @return unique id
     */
    @SuppressWarnings("unused")
	private Long getId() {
        return id;
    }

    /**
     * for hibernate
     *
     * @param aId
     */
    @SuppressWarnings("unused")
	private void setId(Long aId) {
        id = aId;
    }
}
