/**
 * ----------------------------------------------------------------------------| Created on Apr
 * 12, 2003
 */
package com.anji.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jgap.Allele;
import org.jgap.ChromosomeMaterial;
import org.jgap.Configuration;
import org.jgap.InvalidConfigurationException;
import org.jgap.InvalidConfigurationRuntimeException;
import org.jgap.impl.FitnessSelector;
import org.jgap.impl.FitnessReproductionOperator;
import org.jgap.impl.MultiObjectiveNSSelector;
import org.jgap.event.EventManager;
import org.jgap.impl.IntegerAllele;

/**
 * @author Philip Tucker
 */
@SuppressWarnings("serial")
public class DummyConfiguration extends Configuration {

private final static int DEFAULT_POPULATION_SIZE = 100;

private final static float DEFAULT_SURVIVAL_RATE = 0.20f;

private final static float DEFAULT_REPRODUCTION_RATE = 0.80f;

    /**
     * ctor
     */
    public DummyConfiguration() {
            super();

            try {
                    setEventManager( new EventManager() );
                    setRandomGenerator( new Random() );
                    addEvaluationFunction( new DummyBulkFitnessFunction( getRandomGenerator(), 1234L ), 1 );
                    addEvaluationFunction( new DummyBulkFitnessFunction( getRandomGenerator(), 2345L ), 0.5 );
                    addEvaluationFunction( new DummyBulkFitnessFunction( getRandomGenerator(), 3456L ), 1 );
                    FitnessSelector selector = new MultiObjectiveNSSelector();
                    selector.setSurvivalRate( DEFAULT_SURVIVAL_RATE );
                    setFitnessSelector( selector );
                    setIECpopulationSize( DEFAULT_POPULATION_SIZE );

                    List<Allele> initAlleles = new ArrayList<Allele>( 1 );
                    IntegerAllele initAllele = new IntegerAllele( this, 0, 10 );
                    initAllele.setValue( new Integer( 1 ) );
                    initAlleles.add( initAllele );
                    setSampleChromosomeMaterial( new ChromosomeMaterial( initAlleles ) );

                    FitnessReproductionOperator repro = new DummyReproductionOperator();
                    repro.setSlice( DEFAULT_REPRODUCTION_RATE );
                    addFitnessReproductionOperator( repro );
            }
            catch ( InvalidConfigurationException e ) {
                    throw new InvalidConfigurationRuntimeException( "error in DummyConfiguration: " + e );
            }
    }

}
