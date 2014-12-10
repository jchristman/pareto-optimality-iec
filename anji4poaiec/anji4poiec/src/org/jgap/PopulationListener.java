/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jgap;

import java.util.List;

/**
 *
 * @author bwoolley
 */
public interface PopulationListener {    
    public void update(List<Chromosome> population);
}
