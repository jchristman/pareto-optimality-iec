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
public class CartesianSheet extends Sheet {

    public CartesianSheet(int dimX, int dimY, ActivationFunction activation) {
        assert (dimX > 0);
        xSize = dimX;
        assert (dimY > 0);
        ySize = dimY;
        assert (activation != null);

        xInterval = 2.0 / (xSize + 1);
        yInterval = 2.0 / (ySize + 1);

        double x, y;
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                x = (i * xInterval) - 1.0 + xInterval;
                y = (j * yInterval) - 1.0 + yInterval;
                pointList.add(new Point(x, y, new Neuron(activation)));
            }
        }
    }
    public final int xSize, ySize;
    private final double xInterval, yInterval;
}
