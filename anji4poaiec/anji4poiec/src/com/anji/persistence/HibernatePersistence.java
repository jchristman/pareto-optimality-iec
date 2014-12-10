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
 * Created on Jun 5, 2005 by Philip Tucker
 */
package com.anji.persistence;

import com.anji.integration.Activator;
import com.anji.run.Run;
import com.anji.util.Properties;
import com.anji.util.XmlPersistable;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Genotype;

/**
 * @author Philip Tucker
 */
public class HibernatePersistence implements Persistence {

    private static final Logger logger = Logger.getLogger(HibernatePersistence.class.getName());
    private Connection conn = null;
    private String runName;

    /**
     * default ctor; must call
     * <code>init()</code> before using this object
     */
    public HibernatePersistence() {
        super();
    }

    /**
     * @see com.anji.util.Configurable#init(com.anji.util.Properties)
     */
    @Override
    public void init(Properties props) {
        try {
        } catch (Exception e) {
            String msg = "error initializing hibernate";
            logger.log(Level.SEVERE, msg, e);
            throw new IllegalArgumentException(msg + ": " + e);
        }
    }

    /**
     * @see com.anji.persistence.Persistence#reset()
     */
    @Override
    public void reset() {
        try {
        } catch (Throwable th) {
            String msg = "error resetting run " + runName;
            logger.log(Level.SEVERE, msg, th);
            try {
                conn.rollback();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "error on rollback", e);
            }
            throw new IllegalStateException(msg + ": " + th);
        }
    }

    /**
     * @see com.anji.persistence.Persistence#store(org.jgap.Chromosome)
     */
    @Override
    public void store(Chromosome c) throws IOException {
    }

    /**
     * @see
     * com.anji.persistence.Persistence#store(com.anji.integration.Activator)
     */
    @Override
    public void store(Activator a) throws IOException {
    }

    /**
     * @see com.anji.persistence.Persistence#store(com.anji.run.Run)
     */
    @Override
    public void store(Run r) throws IOException {
    }

    @Override
    public void store(RenderedImage i) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void store(RenderedImage i, long chromId) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void storeXml(XmlPersistable x) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @see com.anji.persistence.Persistence#loadChromosome(java.lang.String,
     * org.jgap.Configuration)
     */
    @Override
    public Chromosome loadChromosome(String id, Configuration config) {
        return null;
    }

    /**
     * @see com.anji.persistence.Persistence#deleteChromosome(java.lang.String)
     */
    @Override
    public void deleteChromosome(String id) throws Exception {
    }

    /**
     * @param aConfig
     * @see
     * com.anji.persistence.Persistence#loadGenotype(org.jgap.Configuration)
     */
    @Override
    public Genotype loadGenotype(Configuration aConfig) {
        return null;
    }

    /**
     * @param aRunId
     * @see com.anji.persistence.Persistence#startRun(java.lang.String)
     */
    @Override
    public void startRun(String aRunId) {
    }
}
