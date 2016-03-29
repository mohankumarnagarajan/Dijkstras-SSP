import java.util.*;
import java.io.*;

public class ssp {
	
	public static void main(String [] args){
		Graph graphObj = new Graph();
		
		//checking validity of the file
		File inputFile = new File(args[0]);
		String cmdSource = args[1];
		String cmdDestination = args[2];
		
		// reading input file having the graph vertices and edges
		try{
			graphObj.readInputFile(inputFile);
		}
		
		catch(Exception ex){
			System.out.println(ex.getMessage());
			System.exit(1);
		}
		
		//set the destination
		try{
			graphObj.setDestination(new Integer(cmdDestination));
		}
		
		catch(Exception ex){
			System.out.println(ex.getMessage());
			System.exit(1);
		}
		
		//find and print shortest path between source and destination
		shortestPathHelper spfObj = new shortestPathHelper();
		
		Map.Entry<Integer[], Integer[]> mapEntry = spfObj.shortestPathFinder(graphObj, cmdSource);		
		Integer[] previousVertex = mapEntry.getValue();
		Integer tempPreviousVertex = previousVertex[graphObj.getDestination()];
		String shortestPath = graphObj.getDestination() + "";
		while(tempPreviousVertex != -1){
			shortestPath = tempPreviousVertex + " "+ shortestPath;
			tempPreviousVertex = previousVertex[tempPreviousVertex];
		}
		
		System.out.println(mapEntry.getKey()[graphObj.getDestination()]);
		System.out.print(shortestPath);

		
	}
	
	

	
	

}
