/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * created by Brian Woolley on Sept 23, 2011
 */
package edu.ucf.eplex.mazeNavigation;

import java.io.IOException;

import edu.ucf.eplex.poaiecFramework.domain.PoaiecDomainProperties;


/**
 *
 * @author Brian Woolley (brian.woolley@ieee.org)
 */
public class MazeDomainProperties extends PoaiecDomainProperties {

	public MazeDomainProperties(String properties) throws IOException {
		super(properties);
	}
	
	public MazeDomainProperties() {
        put("run.name", "hard.map.novelty");
        put("run.reset", "true");

        // evolution
        //------------------
        put("evolution.mode", "steady.state");
        put("num.generations", "1000");
        put("popul.size", "12");
        put("topology.mutation.classic", "true");
        put("add.connection.mutation.rate", "0.10");
        put("remove.connection.mutation.rate", "0.01");
        put("remove.connection.max.weight", "5.0");
        put("add.neuron.mutation.rate", "0.05");
        put("prune.mutation.rate", "1.0");
        put("weight.mutation.rate", "0.8");
        put("weight.mutation.std.dev", "1.5");
        put("weight.max", "5.0");
        put("weight.min", "-5.0");
        put("survival.rate", "0.10");
        put("selector.elitism", "true");
        put("selector.roulette", "true");
        put("selector.elitism.min.specie.size", "1");

        // speciation
        //------------------
        put("chrom.compat.excess.coeff", "1.0");
        put("chrom.compat.disjoint.coeff", "1.0");
        put("chrom.compat.common.coeff", "0.04");
        put("speciation.threshold", "0.2");

        // iec function
        //------------------
        put("iec.popul.size", 12);

        // fitness function
        //------------------
        put("fitness.popul.size", "250");
        put("fitness_function.class", "edu.mazeNavigation.NoveltySearch");
        put("fitness.function.adjust.for.network.size.factor", "0");
        put("fitness.threshold", "0.98333333");
        put("fitness.target", "0.98333333");
        put("fitness.short-term.limit", "500");
        
        // novelty function
        //------------------
        put("novelty.popul.size", "250");
        put("novelty_function.class", "edu.mazeNavigation.NoveltySearch");
        put("novelty.knn.value", "15");
        put("novelty.threshold.initial", "3.0");
        put("novelty.threshold.adjust", "2500");
        put("novelty.short-term.limit", "250");

        // network arch
        //------------------
        put("stimulus.size", "11");
        put("response.size", "2");
        put("initial.topology.activation", "sigmoid");
        put("initial.topology.activation.input", "linear");
        put("initial.topology.activation.output", "sigmoid");
        put("initial.topology.fully.connected", "true");
        put("initial.topology.num.hidden.neurons", "0");
        put("recurrent", "best_guess");
        put("recurrent.cycles", "1");
        put("ann.type", "anji");

        // experiment
        //------------------
        put("mazeDomain.map", "hard.map");
        put("mazeDomain.timesteps", "400");
        put("mazeDomain.goalThreshold", "5");
        put("mazeDomain.enableViwer", "false");

        // persistence
        //------------------
//        put("persistence.class", "com.anji.persistence.FilePersistence");
//        put("persistence.base.dir", "./db");
//        put("persist.all", "false");
//        put("persist.champions", "true");
//        put("persist.last", "false");
//        put("id.file", "./db/id.xml");
//        put("neat.id.file", "./db/neatid.xml");
//        put("presentation.dir", "./nevt");
//        put("presentation.active", "false");

    }

    /**
	 * 
	 */
	private static final long serialVersionUID = 7524249214404824872L;
}
