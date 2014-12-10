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
public interface ReproductionOperatorInterface {

    /**
     * The reproduce method will be invoked on each of the reproduction operators referenced by the current
     * Configuration object during the evolution phase. Operators are given an opportunity to run in the order
     * they are added to the Configuration.  Iterates over species and determines each one's number of offspring
     * based on fitness, then passes control to subclass <code>reproduce( final Configuration config,
     * final List parents, int numOffspring, List offspring )</code> method to perform specific reproduction.
     *
     * @param config The current active genetic configuration.
     * @param parentSpecies <code>List</code> contains <code>Specie</code> objects containing parent
     * chromosomes from which to produce offspring.
     * @param offspring <code>List</code> contains offspring <code>ChromosomeMaterial</code> objects; this
     * method adds new offspring to this list
     * @throws InvalidConfigurationException
     * @see ReproductionOperator#reproduce(Configuration, List, int, List)
     */
    void reproduce(final Configuration config, final List<Specie> parentSpecies, List<ChromosomeMaterial> offspring)
            throws InvalidConfigurationException;

    /**
     * @param config
     * @param parents List contains chromosome objects
     * @param numOffspring # Chromosomes to return in List
     * @param offspring List contains ChromosomeMaterial objects
     * @throws InvalidConfigurationException
     * @see ReproductionOperator#reproduce(Configuration, List, List)
     */
    void reproduce(final Configuration config, final List<Chromosome> parents, int numOffspring, List<ChromosomeMaterial> offspring) 
            throws InvalidConfigurationException;

    /**
     * @param aSlice slice of population this reproduction operator will fill with offspring
     */
    void setSlice(float aSlice);

    /**
     * @return float slice of population this reproduction operator will fill with offspring
     */
    float getSlice();    
}
