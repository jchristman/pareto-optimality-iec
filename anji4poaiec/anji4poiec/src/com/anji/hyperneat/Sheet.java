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

import java.util.ArrayList;
import java.util.List;

import com.anji.nn.Neuron;

/**
 * @author Brian Woolley
 *
 */
public abstract class Sheet {

    public ArrayList<Point> getAllPoints() {
        return pointList;
    }

    public List<Neuron> getNeurons() {
        List<Neuron> neurons = new ArrayList<Neuron>();
        for (Point p : pointList) {
            neurons.add(p.neuron);
        }
        return neurons;
    }
    protected final ArrayList<Point> pointList = new ArrayList<Point>();
}
