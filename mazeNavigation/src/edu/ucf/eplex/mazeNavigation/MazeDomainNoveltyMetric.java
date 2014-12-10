/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation;

import org.jgap.BehaviorVector;

import com.anji.util.Properties;

import edu.ucf.eplex.mazeNavigation.model.Path;
import edu.ucf.eplex.poaiecFramework.domain.DomainNoveltyMetric;

/**
 * @author Brian Woolley (brian.woolley at ieee.org)
 *
 */
public class MazeDomainNoveltyMetric extends DomainNoveltyMetric<Path> {

	public MazeDomainNoveltyMetric(Properties props) {
        super(props);
    }
    
    @Override
    public BehaviorVector computeBehaviorVector(Path path) {
        return new BehaviorVector(path.getLast().toArray());
    }

}
