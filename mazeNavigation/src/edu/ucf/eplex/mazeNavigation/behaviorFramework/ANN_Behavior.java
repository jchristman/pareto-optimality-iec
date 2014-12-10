/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.behaviorFramework;

import edu.ucf.eplex.mazeNavigation.behaviorFramework.Action;
import edu.ucf.eplex.mazeNavigation.behaviorFramework.Behavior;
import edu.ucf.eplex.mazeNavigation.behaviorFramework.State;
import edu.ucf.eplex.poaiecFramework.domain.Candidate;

import java.util.List;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class ANN_Behavior implements Behavior {

    public ANN_Behavior(Candidate subject) {
    	assert(subject != null);
    	f_subject = subject;
    }

    /**
     *
     * @see
     * edu.mazeNavigation.behaviorFramework.Behavior#genAction(edu.mazeNavigation.behaviorFramework.State)
     */
    public Action genAction(State currentState) {

        List<Double> range = currentState.getRangeReadings();
        List<Double> goal = currentState.getGoalDetection();

        double[] input = new double[11];
        double[] out;
        // Activate ANN to find the recommended action
        input[ 0] = range.get(0);
        input[ 1] = range.get(1);
        input[ 2] = range.get(2);
        input[ 3] = range.get(3);
        input[ 4] = range.get(4);
        input[ 5] = range.get(5);
        input[ 6] = goal.get(0);
        input[ 7] = goal.get(1);
        input[ 8] = goal.get(2);
        input[ 9] = goal.get(3);
        input[ 10] = 1; // bias

        out = f_subject.next(input);

        Action action = new Action();
        action.setTurnRate(out[0]);
        action.setVelocity(out[1]);
        return action;
    }
    private final Candidate f_subject;
}
