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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import edu.ucf.eplex.mazeNavigation.model.Maze;

/**
 * 
 * @author Brian Woolley (brian.woolley@ieee.org)
 *
 */
public class PostProcessor {
	public static String usage = "PostProcessor.jar PATH_TO_RUNLOGS";
	private final static boolean RUN_DISTRO = true;
	private final static boolean SUMMARY = true;
	
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
    	if (args.length != 1) {
    		System.out.println(PostProcessor.usage);
    		return;
    	}
    		
        long hack = System.currentTimeMillis();

    	Maze maze = Maze.getHardMap();
        String basePath = args[0];
        List<String> inFiles = new ArrayList<String>();
		IECseriesTool tool = new IECseriesTool();

    	// This method loops through sequentially numbered directories [0 .. n],
    	// stopping when there is a gap in the sequence of numbers.
    	int i = 0;
    	String runDir = basePath + "/" + i;

    	while(new File(runDir).exists()) {
    		int j = 0;
    		String fileName = runDir + "/" + "main_prev" + j + ".xml";

    		while(new File(fileName).exists()) {
    			inFiles.add(fileName);
        		fileName = runDir + "/" + "main_prev" + (++j) + ".xml";
    		}
    		inFiles.add(runDir + "/" + "main.xml");

        	if (RUN_DISTRO) {
        		List<String> endpointsFile = new LinkedList<String>();
        		endpointsFile.add(runDir + "/" + "endpointMap.xml");
        		//System.out.println("Creating run distribution image with " + tool.getAllPoints(inFiles).size() + " points.");
        		Util.saveTo(
        				Util.paintTheRunDistribution(
        				tool.getAllPoints(endpointsFile), maze), 
        				basePath + "/distributions/run" + i);
            }

            if (SUMMARY) {
            	tool.printSummary(inFiles, "Run" + i);
            }

        	inFiles.clear();
    		runDir = basePath + "/" + (++i);
    	}

        hack = System.currentTimeMillis() - hack;
        System.out.println("done in " + hack + " mS...");
        hack = System.currentTimeMillis();
        inFiles.clear();

        return;
    }

    private final List<Run> runs = new ArrayList<Run>();
    private final int maxGen;
    private final int skipRate;

    public PostProcessor(List<String> runFiles, int max, int skip) {
        for (String runFile : runFiles) {
            runs.add(new Run(runFile));
            System.out.println("Loaded " + runFile);
        }
        skipRate = skip;
        maxGen = max;
    }

    public void toCSV(String outputDir, Maze aMaze) {
        new File(outputDir).mkdirs();

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputDir + "champs.csv");
            out.write(getChampsOverGenerations().getBytes());
            out.close();
            out = new FileOutputStream(outputDir + "fitness.csv");
            out.write(getFitnessOverGenerations().getBytes());
            out.close();
            out = new FileOutputStream(outputDir + "noveltyScore.csv");
            out.write(getNoveltyOverGenerations().getBytes());
            out.close();
            out = new FileOutputStream(outputDir + "connections.csv");
            out.write(getConnectionsOverGenerations().getBytes());
            out.close();
            out = new FileOutputStream(outputDir + "nodes.csv");
            out.write(getNodesOverGenerations().getBytes());
            out.close();
            out = new FileOutputStream(outputDir + "archiveSize.csv");
            out.write(getArchiveSizeOverGenerations().getBytes());
            out.close();
            out = new FileOutputStream(outputDir + "solution.csv");
            out.write(getRunSummary().getBytes());
            out.close();

            paintTheRunDistributions(outputDir, aMaze);

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

    }

    private String getHeader() {
        StringBuilder header = new StringBuilder("Generation");
        for (Run r : runs) {
            header.append(", Run").append(runs.indexOf(r) + 1);
        }
        return header.append("\n").toString();
    }

    private String getChampsOverGenerations() {
        StringBuilder generation = new StringBuilder(getHeader());
        for (int i = 1; i <= maxGen; i += skipRate) {
            generation.append(i);
            for (Run r : runs) {
                int x = i;
                if (i > r.generations.size()) {
                    x = r.generations.size();
                }
                generation.append(", ").append(r.generations.get(x).champId);
            }
            generation.append("\n");
            if (i == 1 && skipRate > 1) {
                i--;
            }
        }
        return generation.toString();
    }

    private String getFitnessOverGenerations() {
        StringBuilder generation = new StringBuilder(getHeader());
        for (int i = 1; i <= maxGen; i += skipRate) {
            generation.append(i);
            for (Run r : runs) {
                int x = i;
                if (i > r.generations.size()) {
                    x = r.generations.size();
                }
                generation.append(", ").append(r.generations.get(x).fitness);
            }
            generation.append("\n");
            if (i == 1 && skipRate > 1) {
                i--;
            }
        }
        return generation.toString();
    }

    private String getNoveltyOverGenerations() {
        StringBuilder generation = new StringBuilder(getHeader());
        for (int i = 1; i <= maxGen; i += skipRate) {
            generation.append(i);
            for (Run r : runs) {
                int x = i;
                if (i > r.generations.size()) {
                    x = r.generations.size();
                }
                generation.append(", ").append(r.generations.get(x).novelty);
            }
            generation.append("\n");
            if (i == 1 && skipRate > 1) {
                i--;
            }
        }
        return generation.toString();
    }

    private String getConnectionsOverGenerations() {
        StringBuilder generation = new StringBuilder(getHeader());
        for (int i = 1; i <= maxGen; i += skipRate) {
            generation.append(i);
            for (Run r : runs) {
                int x = i;
                if (i > r.generations.size()) {
                    x = r.generations.size();
                }
                generation.append(", ").append(r.generations.get(x).connections);
            }
            generation.append("\n");
            if (i == 1 && skipRate > 1) {
                i--;
            }
        }
        return generation.toString();
    }

    private String getNodesOverGenerations() {
        StringBuilder generation = new StringBuilder(getHeader());
        for (int i = 1; i <= maxGen; i += skipRate) {
            generation.append(i);
            for (Run r : runs) {
                int x = i;
                if (i > r.generations.size()) {
                    x = r.generations.size();
                }
                generation.append(", " + r.generations.get(x).nodes);
            }
            generation.append("\n");
            if (i == 1 && skipRate > 1) {
                i--;
            }
        }

        return generation.toString();
    }

    private String getArchiveSizeOverGenerations() {
        StringBuilder generation = new StringBuilder(getHeader());
        for (int i = 1; i <= maxGen; i += skipRate) {
            generation.append(i);
            for (Run r : runs) {
                int x = i;
                if (i > r.generations.size()) {
                    x = r.generations.size();
                }
                generation.append(", " + r.generations.get(x).archiveSize);
            }
            generation.append("\n");
            if (i == 1 && skipRate > 1) {
                i--;
            }
        }
        return generation.toString();
    }

    private String getRunSummary() {
        System.out.println("...in getTimeSoved()...");
        StringBuilder timeToSolution = new StringBuilder(getHeader());
        timeToSolution.append("Solved in (evals)");
        for (Run r : runs) {
            timeToSolution.append(", ").append(r.evaluations);
        }
        timeToSolution.append("\n");

        timeToSolution.append("Connections");
        for (Run r : runs) {
            timeToSolution.append(", ").append(r.generations.get(r.generations.size()).connections);
        }
        timeToSolution.append("\n");

        timeToSolution.append("Nodes");
        for (Run r : runs) {
            timeToSolution.append(", ").append(r.generations.get(r.generations.size()).nodes);
        }
        timeToSolution.append("\n");

        timeToSolution.append("timeElapsed (mS)");
        for (Run r : runs) {
            timeToSolution.append(", ").append(r.timeElapsed);
        }
        timeToSolution.append("\n");

        return timeToSolution.toString();
    }

    private void paintTheRunDistributions(String outputDir, Maze aMaze) throws IOException {
        outputDir = outputDir + "searchDistribution/";
        new File(outputDir).mkdirs();

        int i = 0;
        for (Run run : runs) {
            File outputfile = new File(outputDir + "series" + i + ".png");
            ImageIO.write(Util.paintTheRunDistribution(run.allPoints, aMaze), "png", outputfile);
            i++;
        }
    }

    private class Run {

        private Map<Integer, Generation> generations = new HashMap<Integer, Generation>();
        private long timeElapsed = 0;
        @SuppressWarnings("unused")
		private long timeElapsedMS = 0;
        private long evaluations = 0;
        private Collection<Point2D> allPoints = new HashSet<Point2D>();

        private Run(String runFileXml) {
            InputStream in;
            try {
                in = new FileInputStream(runFileXml);
                DocumentBuilder builder;
                builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.parse(in);
                Node run = doc.getFirstChild();

                if (run.getNodeName().equalsIgnoreCase("run")) {
                    String timeElapsedStr = run.getAttributes().getNamedItem("timedatestamp").getNodeValue();
                    // "[20110806 22:04:14 - 20110806 22:25:35]"
                    //  012345678901234567890123456789012345678
                    Integer startDate = Integer.parseInt(timeElapsedStr.substring(1, 9));
                    Integer startHour = Integer.parseInt(timeElapsedStr.substring(10, 12));
                    Integer startMin = Integer.parseInt(timeElapsedStr.substring(13, 15));
                    Integer startSec = Integer.parseInt(timeElapsedStr.substring(16, 18));

                    Integer endDate = Integer.parseInt(timeElapsedStr.substring(21, 29));
                    Integer endHour = Integer.parseInt(timeElapsedStr.substring(30, 32));
                    Integer endMin = Integer.parseInt(timeElapsedStr.substring(33, 35));
                    Integer endSec = Integer.parseInt(timeElapsedStr.substring(36, 38));

                    endHour += (endDate - startDate) * 24;
                    endMin += (endHour - startHour) * 60;
                    endSec += (endMin - startMin) * 60;
                    timeElapsed = (endSec - startSec) * 1000;
                }

                Node gen = run.getFirstChild();
                while (true) {
                    if (gen.getNodeName().equalsIgnoreCase("evolution-summary")) {
                        Node param = gen.getFirstChild();
                        while (true) {
                            if (param.getNodeName().equalsIgnoreCase("evaluations")) {
                                evaluations = Math.round(Double.parseDouble(param.getTextContent()));
                            }
                            if (param.getNodeName().equalsIgnoreCase("timeelapsed-ms")) {
                                timeElapsedMS = Math.round(Double.parseDouble((param.getTextContent())));
                            }
                            param = param.getNextSibling();
                            if (param == null) {
                                break;
                            }
                        }
                    }

                    if (gen.getNodeName().equalsIgnoreCase("generation")) {
                        Generation generation = new Generation(gen);
                        generations.put(generation.generationId, generation);
                    }

                    if (gen.getNodeName().equalsIgnoreCase("allPoints")) {
                        Node point = gen.getFirstChild();
                        while (true) {
                            if (point.getNodeName().equalsIgnoreCase("point")) {
                                double x = 99999;
                                double y = 99999;
                                Node coord = point.getFirstChild();
                                while (true) {
                                    if (coord.getNodeName().equalsIgnoreCase("x")) {
                                        x = Double.parseDouble(coord.getTextContent());
                                    }
                                    if (coord.getNodeName().equalsIgnoreCase("y")) {
                                        y = Double.parseDouble(coord.getTextContent());
                                    }
                                    coord = coord.getNextSibling();
                                    if (coord == null) {
                                        break;
                                    }
                                }
                                allPoints.add(new Point2D.Double(x, y));
                            }
                            point = point.getNextSibling();
                            if (point == null) {
                                break;
                            }
                        }
                    }
                    gen = gen.getNextSibling();
                    if (gen == null) {
                        break;
                    }
                }



            } catch (FileNotFoundException e) {
            } catch (ParserConfigurationException e) {
            } catch (SAXException e) {
            } catch (IOException e) {
            }
        }
    }

    /**
     * @author Brian Woolley on Nov 9, 2010
     *
     */
    class Generation {

        @SuppressWarnings("unused")
		private int generationId,
                fitness,
                novelty,
                nodes,
                connections,
                archiveSize,
                specieCount;
        private long champId;

        private Generation(Node gen) {
            generationId = Integer.parseInt(gen.getAttributes().getNamedItem("id").getNodeValue());

            Node node = gen.getFirstChild();
            while (true) {
                if (node.getNodeName().equalsIgnoreCase("champ")) {
                    champId = Long.parseLong(node.getAttributes().getNamedItem("id").getNodeValue());
                    Node feature = node.getFirstChild();
                    while (true) {
                        if (feature.getNodeName().equalsIgnoreCase("fitness")) {
                            fitness = Integer.parseInt(feature.getTextContent());
                        }
                        if (feature.getNodeName().equalsIgnoreCase("novelty")) {
                            novelty = Integer.parseInt(feature.getTextContent());
                        }
                        if (feature.getNodeName().equalsIgnoreCase("connections")) {
                            connections = Integer.parseInt(feature.getTextContent());
                        }
                        if (feature.getNodeName().equalsIgnoreCase("nodes")) {
                            nodes = Integer.parseInt(feature.getTextContent());
                        }
                        feature = feature.getNextSibling();
                        if (feature == null) {
                            break;
                        }
                    }
                }

                if (node.getNodeName().equalsIgnoreCase("archive")) {
                    archiveSize = Integer.parseInt(node.getAttributes().getNamedItem("count").getNodeValue());
                }
                if (node.getNodeName().equalsIgnoreCase("species")) {
                    specieCount = Integer.parseInt(node.getAttributes().getNamedItem("count").getNodeValue());
                }

                node = node.getNextSibling();
                if (node == null) {
                    break;
                }
            }
        }
    }
}