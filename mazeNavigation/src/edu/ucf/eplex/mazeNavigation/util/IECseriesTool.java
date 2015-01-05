/**
 * This software is a work of the U.S. Government. It is not subject to copyright 
 * protection and is in the public domain. It may be used as-is or modified and 
 * re-used. The author and the Air Force Institute of Technology would appreciate 
 * credit if this software or parts of it are used or modified for re-use.
 * 
 * Created by Brian Woolley on Sep 23, 2011
 */
package edu.ucf.eplex.mazeNavigation.util;

import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.sun.tools.javac.util.Pair;

import edu.ucf.eplex.mazeNavigation.model.Position;

/**
 * @author Brian Woolley (brian.woolley@ieee.org)
 */
public class IECseriesTool {

	/**
	 * 
	 * @param fileName The file to write the image out to.
	 * @throws IOException 
	 */
	public Collection<Point2D> getAllPoints(List<String> inFiles) {
		Collection<Point2D> allPoints = new LinkedList<Point2D>();
		for (String inFile : inFiles) {
			//System.out.println("Opening " + inFile);
    		try {
        		InputStream in;
				in = new FileInputStream(inFile);
    	        DocumentBuilder builder;
    	        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    	        Document doc = builder.parse(in);
    	        Node series = doc.getFirstChild();
    			allPoints.addAll(loadAllEndpoints(series));
	    		in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return allPoints;
	}
	
	

	/**
	 * Loads points from an XML node structured as follows.
	 * <series>
	 *   ...
	 *   <allPoints count="3">
     *     <position>
     *        <x>14.530334463546723</x>
     *        <y>176.56306971689247</y>
     *        <theta>0.7208702373730593</theta>
     *     </position>
     *     <position>
     *        <x>14.846209443036399</x>
     *        <y>189.14088306109156</y>
     *        <theta>-0.8845826869949545</theta>
     *     </position>
     *     <position>
     *        <x>101.7959785661266</x>
     *        <y>140.03940944049148</y>
     *        <theta>-1.9634949934727128</theta>
     *     </position>
     *   </allPoints>
     * </series>
     *  
     *  
	 * @param seriesNode Assumes that this node is the Series handle
	 * @return
	 */
	private Collection<Point2D> loadAllPoints(Node seriesNode) {
		long allPointsCount = 0;
		Collection<Point2D> allPoints = new LinkedList<Point2D>();
		
        if (seriesNode.getNodeName().equalsIgnoreCase(SERIES_TAG)) {
        	Node allPtsNode = seriesNode.getFirstChild();
        	while (allPtsNode != null) {
        		if (allPtsNode.getNodeName().equals(ALL_POINTS_TAG)) {
        			allPointsCount = Util.getLong(allPtsNode, POSITION_COUNT_TAG);
        			
        			System.out.println("Preaparing to load " + allPointsCount + " points from the XML node structure.");
        			Node pointNode = allPtsNode.getFirstChild();
        			while (pointNode != null) {
        				if (pointNode.getNodeName().equals(POSITION_TAG)) {
        					allPoints.add(new Position(pointNode));
        				}
        				pointNode = pointNode.getNextSibling();
        			} // end while
        		}
       			allPtsNode = allPtsNode.getNextSibling();
        	} // end while
        }
        return allPoints;
	}

	private Collection<Point2D> loadAllEndpoints(Node seriesNode) {
		Collection<Point2D> allPoints = new LinkedList<Point2D>();
		
        if (seriesNode.getNodeName().equalsIgnoreCase(ENDPOINTS_TAG)) {
        	Node allPtsNode = seriesNode.getFirstChild();
        	while (allPtsNode != null) {
        		if (allPtsNode.getNodeName().equals(ENDPOINT_TAG)) {
                    allPoints.add(new Position(allPtsNode));
        		}
       			allPtsNode = allPtsNode.getNextSibling();
        	} // end while
        }
        return allPoints;
	}
	
	public void printSummary(List<String> inFiles, String label) {
		long evaluations = 0;
		long connections = 0;
		long nodes = 0;
		long archiveSize = 0;
		long timeElapsed = 0;
		long userOperations = 0;
		long step = 0;
		long novelty = 0;
		long optimize = 0;
		long back = 0;
		long forward = 0;
		long pareto = 0;
		// A list of Pairs where the first item is the number of members of the pareto front and the second
		// is the total novelty of the pareto front. I'm not computing anything to allow processing of the data
		// in different ways later
		ArrayList<Pair<Integer,Integer>> paretoFrontInfo = new ArrayList<Pair<Integer,Integer>>();
		// Calculates how many of the 12 most novel individuals are in the first pareto front
		ArrayList<Pair<Integer,Integer>> mostNovelPerPF = new ArrayList<Pair<Integer,Integer>>();
		// Calculates how many of the 12 most novel individuals are in the first 12 solutions presented to the
		// user.
		int mostNovelInFirst12 = 0;
				
		for (String inFile : inFiles) {
//			System.out.println("Opening " + inFile);
    		try {
        		InputStream in;
				in = new FileInputStream(inFile);
    	        DocumentBuilder builder;
    	        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    	        Document doc = builder.parse(in);
    	        Node series = doc.getFirstChild();

    	        evaluations += getEvaluations(series);
    	        connections += getChampConnectionCount(series);
    	        nodes += getChampNodeCount(series);
    	        archiveSize += getArchiveSize(series);
    	        timeElapsed += getTimeElapsed(series);
    	        userOperations += getTotalOperationCount(series);
    	        step += getStepOperationCount(series);
    	        novelty += getNoveltyOperationCount(series);
    	        optimize += getFitnessOperationCount(series);
    	        back += getBackOperationCount(series);
    	        forward += getFwdOperationCount(series);
    	        pareto += getParetoOperationCount(series);
    	        getParetoFrontInfo(series, paretoFrontInfo);
    	        getMostNovelPerPF(series, mostNovelPerPF);
    	        mostNovelInFirst12 = getMostNovelInFirst12();

    	        in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		String pfInfo = "";
		for (Pair<Integer,Integer> pair : paretoFrontInfo)
			pfInfo += pair.fst + ":" + pair.snd + ";";
		pfInfo = pfInfo.substring(0, pfInfo.length() - 1);
		
		String novelPerPF = "";
		for (Pair<Integer,Integer> pair : mostNovelPerPF)
			novelPerPF += pair.fst + ":" + pair.snd + ";";
		novelPerPF = novelPerPF.substring(0, novelPerPF.length() - 1);
		
		System.out.println(novelPerPF);
		
		System.out.println(label + ", " + evaluations + ", " + connections + ", " + 
				nodes + ", " + archiveSize + ", " + timeElapsed + ", " + userOperations + ", " + 
				step + ", " + novelty + ", " + optimize + ", " + back + ", " + forward + ", " + 
				pareto + ", " + pfInfo);
		
    	// TODO: generate output that matches this:
//    	Run0	Run1	Run2	Run3	Run4	Run5	Run6	Run7	Run8	Run9	Run10	Run11	Run12	Run13	Run14	Run15	Run16	Run17	Run18	Run19	Run20	Run21	Run22	Run23	Run24	Run25	Run26	Run27	Run28	Run29	Run30
//    	Evaluations	3768	22251	3346	1568	1249	1748	750	1748	4493	5992	5996	3103	3539	7678	1029	18199	13373	2527	3498	28028	32410	4491	6426	1749	5706	999	3333	1994	4050	6838	7527
//    	Connections	22	23	21	22	23	22	23	23	26	24	23	23	19	20	21	21	24	22	24	22	23	21	24	21	23	23	23	24	23	22	23
//    	Nodes	14	13	13	13	13	13	13	13	14	13	15	13	13	13	13	13	13	13	13	14	14	13	13	13	13	13	13	13	14	13	13
//    	ArchiveSize	1246	1693	875	689	615	788	319	509	1248	1196	928	522	741	1281	295	341	1677	1129	736	1267	1267	976	1301	658	1259	520	1335	914	319	1351	1482
//    	TimeElapsed	253782	1351046	190935	102000	64159	41002	24816	32755	79873	76118	67038	305717	265215	489865	85508	1045660	542759	106687	124022	939855	1165163	245803	221681	86837	136672	27388	267671	110774	125260	235690	354180
//    	UserEvaluations	15	86	17	12	4	6	2	6	14	19	18	15	16	42	6	103	67	12	13	117	178	17	22	6	19	3	13	7	17	31	44
//    	STEP	3	17	5	7	0	0	0	0	0	0	0	6	4	18	3	48	28	3	0	32	80	0	0	0	0	0	1	0	5	9	19
//    	NOVELTY	7	63	8	4	3	4	1	4	7	10	7	2	5	10	1	13	36	8	10	71	71	13	18	6	12	2	12	6	6	12	13
//    	OPTIMIZE	5	6	4	1	1	2	1	2	7	9	11	7	7	14	2	42	3	1	3	14	27	4	4	0	7	1	0	1	6	10	12
//    	BACK	0	3	2	0	0	0	0	0	0	0	0	0	0	1	0	0	0	0	0	0	11	3	1	0	0	0	3	0	5	0	2
//    	FORWARD	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	3	0	0	0	0    	
//    	PARETO	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	3	0	0	0	0    	
		
	}

	private int getMostNovelInFirst12() {
		// TODO Auto-generated method stub
		return 0;
	}



	private void getMostNovelPerPF(Node series,
			ArrayList<Pair<Integer,Integer>> mostNovelPerPF) {
		List<Pair<Integer,Integer>> mostNovelIndividuals = new LinkedList<Pair<Integer,Integer>>();
		ArrayList<Integer> paretoFronts = new ArrayList<Integer>();
		
		if (series.getNodeName().equalsIgnoreCase(SERIES_TAG)) {
        	Node seriesAttribute = series.getFirstChild();
        	while (seriesAttribute != null) {
        		if (seriesAttribute.getNodeName().equals(IEC_STEPS_TAG)) {
        			Node stepNode = seriesAttribute.getFirstChild();
        			while (stepNode != null) {
        				Node stepAttribute = stepNode.getFirstChild();
        				while (stepAttribute != null) {
        					if (stepAttribute.getNodeName().equals(POPULATION_TAG)) {
        						Node chromosome = stepAttribute.getFirstChild();
        						while (chromosome != null) {
        							if (chromosome.getNodeName().equals(CHROMOSOME_TAG)) {
        								Node chromData = chromosome.getFirstChild();
        								Node pf = null, nov = null;
        								while (chromData != null) {
        									if (chromData.getNodeName().equals(NOVELTY_TAG)) nov = chromData;
        									else if (chromData.getNodeName().equals(PF_TAG)) pf = chromData;
        									chromData = chromData.getNextSibling();
        								}
        								
        								int paretoFront = Integer.parseInt(pf.getTextContent());
        								int noveltyValue = Integer.parseInt(nov.getTextContent());
        								if (!paretoFronts.contains(paretoFront)) paretoFronts.add(paretoFront);
        								
        								if (mostNovelIndividuals.size() < 12) {
        									mostNovelIndividuals.add(new Pair<Integer,Integer>(paretoFront, noveltyValue));
        								} else {
        									Pair<Integer,Integer> toReplace = null;
        									for (Pair<Integer,Integer> solution : mostNovelIndividuals) {
        										if (solution.snd < noveltyValue || (solution.snd == noveltyValue && solution.fst < paretoFront)) {
        											if (toReplace == null) toReplace = solution;
        											else if (toReplace.snd < solution.snd || (toReplace.snd == solution.snd && toReplace.fst < solution.fst)) toReplace = solution;
        										}
        									}
        									if (toReplace != null) {
        										mostNovelIndividuals.add(mostNovelIndividuals.indexOf(toReplace), new Pair<Integer,Integer>(paretoFront, noveltyValue));
        										mostNovelIndividuals.remove(toReplace);
        									}
        								}
        							}
        							chromosome = chromosome.getNextSibling();
        						}
        						break;
        					}
        					stepAttribute = stepAttribute.getNextSibling();
        				}
        				if (true) {
        					
        				}
        				stepNode = stepNode.getNextSibling();
        			}
        		}
        		seriesAttribute = seriesAttribute.getNextSibling();
        	}
        }

		Collections.sort(paretoFronts);
		for (Pair<Integer,Integer> pair : mostNovelIndividuals) {
			mostNovelPerPF.add(new Pair<Integer,Integer>(paretoFronts.indexOf(pair.fst), pair.snd));
		}
	}



	private void getParetoFrontInfo(Node series,
			ArrayList<Pair<Integer, Integer>> paretoFrontInfo) {
		TreeMap<Integer, Pair<Integer,Integer>> pfInfo = new TreeMap<Integer, Pair<Integer,Integer>>();
		
        if (series.getNodeName().equalsIgnoreCase(SERIES_TAG)) {
        	Node seriesAttribute = series.getFirstChild();
        	while (seriesAttribute != null) {
        		if (seriesAttribute.getNodeName().equals(IEC_STEPS_TAG)) {
        			Node stepNode = seriesAttribute.getFirstChild();
        			while (stepNode != null) {
        				Node stepAttribute = stepNode.getFirstChild();
        				while (stepAttribute != null) {
        					if (stepAttribute.getNodeName().equals(POPULATION_TAG)) {
        						Node chromosome = stepAttribute.getFirstChild();
        						while (chromosome != null) {
        							if (chromosome.getNodeName().equals(CHROMOSOME_TAG)) {
        								Node chromData = chromosome.getFirstChild();
        								Node pf = null, nov = null;
        								while (chromData != null) {
        									if (chromData.getNodeName().equals(NOVELTY_TAG)) nov = chromData;
        									else if (chromData.getNodeName().equals(PF_TAG)) pf = chromData;
        									chromData = chromData.getNextSibling();
        								}
        								
        								int paretoFront = Integer.parseInt(pf.getTextContent());
        								int noveltyValue = Integer.parseInt(nov.getTextContent());
        								if (!pfInfo.containsKey(paretoFront)) {
        									pfInfo.put(paretoFront, new Pair<Integer,Integer>(1,noveltyValue));
        								} else {
        									Pair<Integer,Integer> cur = pfInfo.get(paretoFront);
        									pfInfo.put(paretoFront, new Pair<Integer,Integer>(
        											cur.fst + 1, cur.snd + noveltyValue));
        								}
        							}
        							chromosome = chromosome.getNextSibling();
        						}
        						break;
        					}
        					stepAttribute = stepAttribute.getNextSibling();
        				}
        				if (true) {
        					
        				}
        				stepNode = stepNode.getNextSibling();
        			}
        		}
        		seriesAttribute = seriesAttribute.getNextSibling();
        	}
        }
        
        for (Entry<Integer,Pair<Integer,Integer>> entry : pfInfo.entrySet()) {
        	paretoFrontInfo.add(entry.getValue());
        }
	}



	private long getEvaluations(Node series) {
        if (series.getNodeName().equalsIgnoreCase(SERIES_TAG)) {
        	Node seriesChild = series.getFirstChild();
        	while (seriesChild != null) {
        		if (seriesChild.getNodeName().equals(TOTAL_EVALUATIONS_TAG)) {
        			return Util.getLong(seriesChild);
        		}
        		seriesChild = seriesChild.getNextSibling();
        	}
        }
    	return 0;
	}

	/**
	 * Returns the number of connections in the solution chromosome.
	 * @param series
	 * @return
	 */
	private long getChampConnectionCount(Node series) {
		Node chrom = getSolutionChrom(series);
		if (chrom != null) {
			Node attribute = chrom.getFirstChild();
			while (attribute != null) {
				if (attribute.getNodeName().equals(CONNECTION_COUNT_TAG)) {
					return Util.getLong(attribute);
				}
				attribute = attribute.getNextSibling();
			}
		}
		return 0;
	}

	/**
	 * Returns the number of nodes in the solution chromosome.
	 * @param series
	 * @return
	 */
	private long getChampNodeCount(Node series) {
		Node chrom = getSolutionChrom(series);
		if (chrom != null) {
			Node attribute = chrom.getFirstChild();
			while (attribute != null) {
				if (attribute.getNodeName().equals(NODE_COUNT_TAG)) {
					return Util.getLong(attribute);
				}
				attribute = attribute.getNextSibling();
			}
		}
		return 0;
	}

	/**
	 * Returns the first solution CHROMOSOME in the SERIES, returns null otherwise.
	 * @param series
	 * @return
	 */
	private Node getSolutionChrom(Node series) {
		if (series.getNodeName().equalsIgnoreCase(SERIES_TAG)) {
        	Node atrribute = series.getFirstChild();
        	while (atrribute != null) {
        		if (atrribute.getNodeName().equals(IEC_STEPS_TAG)) {
        			Node step = atrribute.getFirstChild();
        			while(step != null) {
        				if (step.getNodeName().equals(STEP_TAG)) {
        					Node chrom = step.getFirstChild();
        					while (chrom != null) {
        						if (chrom.getNodeName().equals(CHROMOSOME_TAG)) {
        							Node solution = chrom.getFirstChild();
        							while (solution != null) {
        								if (solution.getNodeName().equals(SOLUTION_TAG)) {
        									if (Util.getBoolean(solution)) {
        										return chrom;
        									}
        								}
        								solution = solution.getNextSibling();
        							}
        						}
        						chrom = chrom.getNextSibling();
        					}
        				}
        				step = step.getNextSibling();
        			}
        		}
        		atrribute = atrribute.getNextSibling();
        	}
        }
		return null;
	}

	private long getArchiveSize(Node series) {
        if (series.getNodeName().equalsIgnoreCase(SERIES_TAG)) {
        	Node seriesChild = series.getFirstChild();
        	while (seriesChild != null) {
        		if (seriesChild.getNodeName().equals(TOTAL_ARCHIVE_TAG)) {
        			return Util.getLong(seriesChild);
        		}
        		seriesChild = seriesChild.getNextSibling();
        	}
        }
		return 0;
	}

	private long getTimeElapsed(Node series) {
        if (series.getNodeName().equalsIgnoreCase(SERIES_TAG)) {
        	Node seriesChild = series.getFirstChild();
        	while (seriesChild != null) {
        		if (seriesChild.getNodeName().equals(TIME_ELAPSED_TAG)) {
        			return Util.getLong(seriesChild);
        		}
        		seriesChild = seriesChild.getNextSibling();
        	}
        }
		return 0;
	}

	private long[] getOperationCount(Node series) {
		long total = 0;
		long initial = 0;
		long step = 0;
		long novelty = 0;
		long fitness = 0;
		long back = 0;
		long forward = 0;
		long pareto = 0;
		
		// TODO Loop through each steps in the series, counting the number of Step operations taken, returns the count.
        if (series.getNodeName().equalsIgnoreCase(SERIES_TAG)) {
        	Node seriesAttribute = series.getFirstChild();
        	while (seriesAttribute != null) {
        		if (seriesAttribute.getNodeName().equals(IEC_STEPS_TAG)) {
        			Node stepNode = seriesAttribute.getFirstChild();
        			while (stepNode != null) {
        				Node stepAttribute = stepNode.getFirstChild();
        				while (stepAttribute != null) {
        					if (stepAttribute.getNodeName().equals(ACTION_TAG)) {
        						total++;

        						String action = stepAttribute.getTextContent();
        						if (action.equalsIgnoreCase("INITIAL")) {
        							initial++;
        						} else if (action.equalsIgnoreCase("STEP")) {
        							step++;
        						} else if (action.equalsIgnoreCase("NOVELTY")) {
        							novelty++;
        						} else if (action.equalsIgnoreCase("FITNESS")) {
        							fitness++;
        						} else if (action.equalsIgnoreCase("BACK")) {
        							back++;
        						} else if (action.equalsIgnoreCase("FORWARD")) {
        							forward++;
        						} else if (action.equalsIgnoreCase("PARETO")) {
        							pareto++;
        						}
        						break;
        					}
        					stepAttribute = stepAttribute.getNextSibling();
        				}
        				if (true) {
        					
        				}
        				stepNode = stepNode.getNextSibling();
        			}
        		}
        		seriesAttribute = seriesAttribute.getNextSibling();
        	}
        }
		return new long[] {total, initial, step, novelty, fitness, back, forward, pareto};
	}

	private long getTotalOperationCount(Node series) {
		return getOperationCount(series)[0];
	}

	private long getStepOperationCount(Node series) {
		return getOperationCount(series)[2];
	}
	
	private long getNoveltyOperationCount(Node series) {
		return getOperationCount(series)[3];
	}

	private long getFitnessOperationCount(Node series) {
		return getOperationCount(series)[4];
	}

	private long getBackOperationCount(Node series) {
		return getOperationCount(series)[5];
	}

	private long getFwdOperationCount(Node series) {
		return getOperationCount(series)[6];
	}

	private long getParetoOperationCount(Node series) {
		return getOperationCount(series)[7];
	}

	private static final String ACTION_TAG = "action";
	private static final String ALL_POINTS_TAG = "allPoints";
	private static final String CHROMOSOME_TAG = "chromosome";
	private static final String CONNECTION_COUNT_TAG = "connections";
	private static final String ENDPOINTS_TAG = "endpoints";
	private static final String ENDPOINT_TAG = "endpoint";
	private static final String IEC_STEPS_TAG = "iecSteps";
	private static final String NODE_COUNT_TAG = "nodes";
	private static final String NOVELTY_TAG = "noveltyValue";
	private static final String PF_TAG = "paretoFront";
	private static final String POSITION_COUNT_TAG = "count";
	private static final String POSITION_TAG = "position";
	private static final String POPULATION_TAG = "population";
	private static final String SERIES_TAG = "series";
	private static final String SOLUTION_TAG = "solution";
	private static final String STEP_TAG = "step";
	private static final String TIME_ELAPSED_TAG = "time";
	private static final String TOTAL_ARCHIVE_TAG = "archive";
	private static final String TOTAL_EVALUATIONS_TAG = "evaluations";
}
