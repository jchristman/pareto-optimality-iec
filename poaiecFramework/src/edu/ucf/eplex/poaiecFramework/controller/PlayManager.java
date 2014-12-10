/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.poaiecFramework.controller;

import edu.ucf.eplex.poaiecFramework.domain.EvaluationDomain;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JSlider;

/**
 *
 * @author Brian Woolley (brian.woolley at ieee.org)
 */
public class PlayManager extends Thread {

    private volatile boolean playing = false;
    private volatile boolean running = false;
    /**
     * Sets the time between frames in mS, (e.g., 100 is 10 frames per second).
     */
    private int frameRate = 100;
    private long hack = System.currentTimeMillis();
    private volatile boolean repeat = true;
    private EvaluationDomain<?> f_domain;
    private JSlider progressBar;

    @Override
    public void run() {
        running = true;
        long delay;
        while (running) {
            hack = System.currentTimeMillis();
            if (playing) {
                if (repeat && f_domain.atLastStep()) {
                    f_domain.gotoSimulationTimestep(0);
                } else {
                    f_domain.step();
                }
                if (progressBar != null) {
                    progressBar.setValue(getProgress());
                }
                f_domain.repaintAllPanels();
            }
//            for (EvaluationPanel panel : panels) {
//                panel.repaint();
//            }
            delay = frameRate - (System.currentTimeMillis() - hack);
            try {
                sleep(Math.max(delay, 0));
            } catch (InterruptedException ex) {
                Logger.getLogger(PlayManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    synchronized public void play() {
        playing = true;
    }

    synchronized public boolean isPlaying() {
        return playing;
    }

    synchronized public void pause() {
        playing = false;
    }

    synchronized public boolean isPaused() {
        return !playing;
    }

    synchronized public void kill() {
        running = false;
    }

    synchronized public int getFrameRate() {
        return frameRate;
    }

    synchronized public void setFrameRate(int aNewFrameRate) {
        frameRate = aNewFrameRate;
    }

    private int getProgress() {
        float current = (float) f_domain.getCurrentTimeStep();
        float max = (float) f_domain.getMaxSimulationTimesteps();
        return Math.round(current / max * progressBar.getMaximum());
    }

    public void registerSlider(JSlider aNewProgressBar) {
        progressBar = aNewProgressBar;
    }

    public synchronized void setRepeat(boolean setRepeat) {
        repeat = setRepeat;
    }

    public synchronized void goTo(float ratio) {
        if (progressBar != null) {
                int targetFrame = Math.round(ratio * f_domain.getMaxSimulationTimesteps());
                f_domain.gotoSimulationTimestep(targetFrame);
                f_domain.repaintAllPanels();
        }
    }

    public void registerDomain(EvaluationDomain<?> domain) {
        if (domain != null) {
            f_domain = domain;
        }
    }
}
