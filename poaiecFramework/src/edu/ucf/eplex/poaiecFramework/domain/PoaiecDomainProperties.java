/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.poaiecFramework.domain;

import com.anji.util.Properties;
import java.io.IOException;

/**
 *
 * @author Brian Woolley (brian.woolley at ieee.org)
 */
public class PoaiecDomainProperties extends Properties {

    public PoaiecDomainProperties(String string) throws IOException {
        super(string);
    }
    
    public PoaiecDomainProperties() {
        super();
        put("run.name", "unknown");
        put("run.reset", "false");

        // evolution
        //------------------
        put("evolution.mode", "steady.state");
        put("num.generations", "1000");
        put("topology.mutation.classic", "true");
        put("add.connection.mutation.rate", "0.10");
        put("remove.connection.mutation.rate", "0.00");
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
        put("popul.size", "12");

        // fitness function
        //------------------
        put("fitness.popul.size", "250");
        put("fitness.function.adjust.for.network.size.factor", "0");
        put("fitness.target", "1.00"); // For every fitness function?
        put("fitness.threshold", "0.95");
        put("fitness.short-term.limit", "250");
        
        // novelty function
        //------------------
        put("novelty.popul.size", "250");
        put("novelty.knn.value", "15");
        put("novelty.threshold.initial", "3.0");
        put("novelty.threshold.adjust", "2500");
        put("novelty.short-term.limit", "250");

        // persistence
        //------------------
        put("persistence.class", "com.anji.persistence.FilePersistence");
        put("persistence.base.dir", "./db_1009");
        put("persist.all", "false");
        put("persist.champions", "true");
        put("persist.last", "true");
        put("id.file", "./db_1009/id.xml");
        put("neat.id.file", "./db_1009/neatid.xml");
    }

	public String getProperty(String key, String defaultVal) {
		return super.getProperty(key, defaultVal);
	}

    /** Auto-generated serialID */
	private static final long serialVersionUID = 4847230563563535608L;
}
