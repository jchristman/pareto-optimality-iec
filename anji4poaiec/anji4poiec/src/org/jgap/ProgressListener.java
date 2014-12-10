/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jgap;

/**
 *
 * @author bwoolley
 */
public interface ProgressListener {
    
    public void progressStarted();
    public void progressUpdate(int progress, int evaluations);
    public void progressFinished();
    
}
