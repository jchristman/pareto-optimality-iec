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
 * Created on Apr 4, 2004 by Philip Tucker
 */
package com.anji.integration;

import com.anji.neat.NeatConfiguration;
import com.anji.run.Run;
import com.anji.util.Properties;
import com.anji.util.XmlPersistable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jgap.BehaviorVector;
import org.jgap.Chromosome;

/**
 * Converts run data to XML presentation format. Complexity, fitness, and
 * speciation are represented with the same XML structure, different
 * stylesheets. A new
 * <code>XmlPersistableRun</code> object should be created new for each use,
 * because it caches the XML it produces, and does not automatically detect
 * changes to the underlying
 * <code>Run</code> object.
 *
 * @author Philip Tucker
 */
public class XmlPersistableRun implements XmlPersistable {

    /**
     * base XML tag
     */
    public final static String RUN_TAG = "run";
    public final static String ALL_BEHAVIORS_TAG = "allBehaviors";
    public final static String BEHAVIOR_TAG = "behavior";
    public final static String ID_TAG = "id";
    public final static String FITNESS_TAG = "fitness";
    public final static String FUNCTION_TAG = "function";
    public final static String DIMENSION_TAG = "d";
    
    /**
     * XML parameters tag
     */
    public final static String PARAMETERS_TAG = "parameters";
    private Run run;
    /**
     * XML parameter tag
     */
    public final static String PARAMETER_TAG = "parameter";
    private final static String vers = "<?xml version = \"1.0\" encoding = \"UTF-8\"?>\n";
    private final static String dtd = "<!DOCTYPE run SYSTEM \"../run.dtd\">\n";
    private final static String endTag = "</" + RUN_TAG + ">\n";
    private final static String complexityStylesheet = "<?xml-stylesheet type=\"text/xsl\" href=\"./graphComplexity.xsl\" ?>\n";
    private final static String complexityRefreshStylesheet = "<?xml-stylesheet type=\"text/xsl\" href=\"./graphComplexityRefresh.xsl\" ?>\n";
    private final static String fitnessStylesheet = "<?xml-stylesheet type=\"text/xsl\" href=\"./graphFitness.xsl\" ?>\n";
    private final static String fitnessRefreshStylesheet = "<?xml-stylesheet type=\"text/xsl\" href=\"./graphFitnessRefresh.xsl\" ?>\n";
    private final static String speciesStylesheet = "<?xml-stylesheet type=\"text/xsl\" href=\"./graphSpecies.xsl\" ?>\n";
    private final static String speciesRefreshStylesheet = "<?xml-stylesheet type=\"text/xsl\" href=\"./graphSpeciesRefresh.xsl\" ?>\n";
    private StringBuffer params = new StringBuffer();
    private String cachedRunXml;

    /**
     * ctor; must call
     * <code>init()</code> before using this object
     *
     * @param aRun
     */
    public XmlPersistableRun(Run aRun) {
        run = aRun;

        // parameters
        Properties props = run.getProps();
        if (props != null) {
            params.append(openTag("evolution-summary"));
            params.append(inLine("population-size", props.getIntProperty(NeatConfiguration.IEC_POPUL_SIZE_KEY)));
            params.append(inLine("evaluations", run.getEvaluationCount()));
            params.append(inLine("timeElapsed-mS", System.currentTimeMillis() - run.getStartTime().getTimeInMillis()));
            params.append(closeTag("evolution-summary"));
        }

        cachedRunXml = null;
    }

    /**
     * @param includeDtd include DTD tag if true
     * @param result representation of run
     */
    protected void appendToString(boolean includeDtd, StringBuffer result) {
        if (cachedRunXml == null) {
            StringBuilder cacheBuffer = new StringBuilder();
            if (includeDtd) {
                cacheBuffer.append(dtd);
            }
            DateFormat fmt = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            String startTag = "<" + RUN_TAG + " name=\"" + run.getName() + "\" timedatestamp=\"["
                    + fmt.format(run.getStartTime().getTime()) + " - "
                    + fmt.format(Calendar.getInstance().getTime()) + "]\" >\n";
            cacheBuffer.append(startTag);
            cacheBuffer.append(params);

            for (Generation g : run.getGenerations()) {
                cacheBuffer.append(g.toXml());
            }

            HashMap<Long, Map<Chromosome, BehaviorVector>> allPointsVisitedSet = run.getAllPointsVisited();
            for (Entry<Long, Map<Chromosome, BehaviorVector>> entry : allPointsVisitedSet.entrySet()) {
            	Map<Chromosome, BehaviorVector> allPointsVisited = entry.getValue();
            	cacheBuffer.append(openTag(ALL_BEHAVIORS_TAG, allPointsVisited.size()));
	            for (Chromosome chrom : allPointsVisited.keySet()) {
	                BehaviorVector pt = allPointsVisited.get(chrom);
	                cacheBuffer.append("  ").append(openTag(BEHAVIOR_TAG));
	                cacheBuffer.append("    ").append(inLine(ID_TAG, chrom.getId()));
	                cacheBuffer.append("    ").append(inLine(FITNESS_TAG, FUNCTION_TAG, chrom.getFitnessValues()));
	                for (int i=0; i<pt.size(); i++) {
	                    cacheBuffer.append("    ").append(inLine(DIMENSION_TAG+i, pt.get(i)));
	                }
	                cacheBuffer.append("  ").append(closeTag(BEHAVIOR_TAG));
	            }
	            cacheBuffer.append(closeTag(ALL_BEHAVIORS_TAG));
            }

            cacheBuffer.append(endTag);
            cachedRunXml = cacheBuffer.toString();
        }
        result.append(cachedRunXml);
    }


	/**
     * @param isRunCompleted
     * <code>true</code> iff this is the last call to
     * @return XML representation of population's complexity throughout run
     */
    public String toComplexityString(boolean isRunCompleted) {
        StringBuffer result = new StringBuffer();
        result.append(vers);
        result.append(isRunCompleted ? complexityStylesheet : complexityRefreshStylesheet);
        appendToString(true, result);
        return result.toString();
    }

    /**
     * @param isRunCompleted
     * <code>true</code> iff this is the last call to
     * @return XML representation of population's fitness throughout run
     */
    public String toFitnessString(boolean isRunCompleted) {
        StringBuffer result = new StringBuffer();
        result.append(vers);
        result.append(isRunCompleted ? fitnessStylesheet : fitnessRefreshStylesheet);
        appendToString(true, result);
        return result.toString();
    }

    /**
     * @param isRunCompleted
     * <code>true</code> iff this is the last call to
     * @return XML representation of population's species and their size
     * thorughout run
     */
    public String toSpeciesString(boolean isRunCompleted) {
        StringBuffer result = new StringBuffer();
        result.append(vers);
        result.append(isRunCompleted ? speciesStylesheet : speciesRefreshStylesheet);
        appendToString(true, result);
        return result.toString();
    }

    /**
     * @return @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return toXml();
    }

    /**
     * @see com.anji.util.XmlPersistable#toXml()
     */
    @Override
    public String toXml() {
        StringBuffer result = new StringBuffer();
        appendToString(false, result);
        return result.toString();
    }

    /**
     * @see com.anji.util.XmlPersistable#getXmlRootTag()
     */
    @Override
    public String getXmlRootTag() {
        return RUN_TAG;
    }

    /**
     * @see com.anji.util.XmlPersistable#getXmld()
     */
    @Override
    public String getXmld() {
        return run.getName();
    }

    /**
     * @return unique run ID
     */
    public String getName() {
        return run.getName();
    }

    private String openTag(String tag) {
        return ("<" + tag + ">\n");
    }

    private String openTag(String tag, double value) {
        return ("<" + tag + " count=\""+value+"\">\n");
    }

    private String closeTag(String tag) {
        return ("</" + tag + ">\n");
    }

    private String inLine(String tag, int value) {
        return inLine(tag, Integer.toString(value));
    }
    
    private String inLine(String tag, long value) {
        return inLine(tag, Long.toString(value));        
    }

    private String inLine(String tag, double value) {
        return inLine(tag, Double.toString(value));        
    }

    private String inLine(String tag, String value) {
        return ("<" + tag + ">" + value + "</" + tag + ">\n");
    }
    
    private String inLine(String tag, long tag_value, int value) {
		return inLine(tag, tag_value, Integer.toString(value));
	}
    
    private String inLine(String tag, String subtag, HashMap<Long, Integer> values) {
    	String ret = "<" + tag + ">\n";
    	for (Entry<Long, Integer> val : values.entrySet()) {
    		ret += inLine(subtag, val.getKey(), val.getValue());
    	}
		return ret;
    }
    
    private String inLine(String tag, long tag_value, String value) {
        return ("<" + tag + " value=\"" + tag_value + "\">" + value + "</" + tag + ">\n");
    }
}
