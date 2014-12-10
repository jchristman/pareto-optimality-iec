/**
 * Copyright (C) 2011 Brian Woolley and Kenneth Stanley
 * 
 * This file is part of the octopusArm simulator.
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
 * Created on 11 February 2011 by Brian Woolley
 */
package com.anji.hyperneat;

import com.anji.nn.ActivationFunction;
import com.anji.nn.Neuron;

/**
 * @author Brian Woolley
 *
 */
public class RadialSheet extends Sheet {

    public RadialSheet(int angularResolution, ActivationFunction activation) {
        assert (angularResolution > 0);
        dimTheta = angularResolution;
        assert (activation != null);

        angularInterval = 2 * Math.PI / angularResolution;

        double x = 0, y = 0;
//		pointList.add(new Point(x, y, new Neuron(factory.get(activation.toString()))));
        for (int i = 0; i < dimTheta; i++) {
            x = xProjection(i * angularInterval, radius);
            y = yProjection(i * angularInterval, radius);
            pointList.add(new Point(x, y, new Neuron(activation)));
        }
    }

    public int getIndex(double theta) {
        // normalize the radian angle:  -dA < theta < 2PI-dA
        while (theta < -angularInterval / 2) {
            theta += 2 * Math.PI;
        }
        while (theta > -angularInterval / 2 + 2 * Math.PI) {
            theta -= 2 * Math.PI;
        }

        // What angular region contains the point (theta, r)
        int i = 0;
        double upper = angularInterval / 2;
        while (theta > upper) {
            // Shift the angular region
            i++;
            upper += angularInterval;
        }
        // The discrete region containing theta
        return i;
    }

    private double xProjection(double theta, double radius) {
        return radius * Math.cos(theta);
    }

    private double yProjection(double theta, double radius) {
        return radius * Math.sin(theta);
    }
    public final int dimTheta;
    private final double angularInterval, radius = 0.5;
}
