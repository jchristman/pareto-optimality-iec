package edu.ucf.eplex.mazeNavigation;

import edu.ucf.eplex.poaiecFramework.controller.PoaiecController;
import edu.ucf.eplex.poaiecFramework.domain.Candidate;

public class MOEAMazeNavigationDomain {
	public static void main(String[] args) {
		// Set up the domain
		System.out.println("Setting up the domain");
		MazeDomainProperties props = new MazeDomainProperties();
		MazeNavigationDomain domain = new MazeNavigationDomain();
		domain.setDomainProperties(props);
		System.out.println("Done setting up the domain");
		
		// Set up the controller
		System.out.println("Setting up the controller");
		PoaiecController ctlr = new PoaiecController(domain);
		domain.ctrl = ctlr;
		System.out.println("Done setting up the controller");
		
		int solution = runAutomatedSearch(ctlr);
		if (solution == -1)
			ctlr.publish(0);
		else
			ctlr.publish(solution);
	}
	
	public static int runAutomatedSearch(PoaiecController ctlr) {
		// Run the automated search
		while (ctlr.getTotalEvaluationCount() < 250000) {
			System.out.println("Eval count: " + ctlr.getTotalEvaluationCount());
			ctlr.doAutomatedSearchStep();
			ctlr.recordEndOfStep(false);
			int index = 0;
			for (Candidate cand : ctlr.getCandidates()) {
				if (cand.isSolution()) {
					System.out.println("Found solution!");
					return index;
				}
				index++;
			}
			System.out.println("Eval count: " + ctlr.getTotalEvaluationCount());
		}
		return -1;
	}

}
