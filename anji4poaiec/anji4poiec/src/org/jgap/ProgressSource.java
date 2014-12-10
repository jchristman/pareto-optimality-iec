/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jgap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author bwoolley
 */
public abstract class ProgressSource {
    private Collection<ProgressListener> progressListeners = new ArrayList<ProgressListener>();
    
    public void registerProgressListener(ProgressListener aNewProgressListener) {
        if (!progressListeners.contains(aNewProgressListener)) {
            progressListeners.add(aNewProgressListener);
        }
    }
    
    public void removeProgressListener(ProgressListener oldProgressListener) {
        progressListeners.remove(oldProgressListener);
    }
    
    protected void updateProgressListeners(int progressValue, int evaluations) {
        for (ProgressListener listener : progressListeners) {
            listener.progressUpdate(progressValue, evaluations);
        }
    }

    protected void finishProgressListeners() {
        for (ProgressListener listener : progressListeners) {
            listener.progressFinished();
        }
    }
        
    private Collection<PopulationListener> populationListeners = new ArrayList<PopulationListener>();
    
    public void registerPopulationListener(PopulationListener aNewPopulationListener) {
        if (!populationListeners.contains(aNewPopulationListener)) {
            populationListeners.add(aNewPopulationListener);
        }
    }
    
    public void removePopulationListener(PopulationListener oldPopulationListener) {
        populationListeners.remove(oldPopulationListener);
    }
    
    protected void notifyPopulationListeners(List<Chromosome> population) {
        for (PopulationListener listener : populationListeners) {
            listener.update(population);
        }
    }
}
