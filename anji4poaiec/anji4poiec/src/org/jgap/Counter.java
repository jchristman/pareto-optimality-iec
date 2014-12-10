/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jgap;

/**
 *
 * @author bwoolley
 */
public class Counter {
    private int count = 0;
    
    public void tick() {
        if (count == Integer.MAX_VALUE) {
            throw new IllegalStateException("The counter has exceeded the max integer value.");
        }
        count++;
    }
    
    public int getCount() {
        return count;
    }
    
    public void clear() {
        count = 0;
    }
}
