import java.util.*;
import java.io.*;
import java.util.Map.Entry;


//graph class defines the graph
public class Graph{
	
	//graph class contains an adjacency list for each node and number of vertices and, num of edges
	private Map<Integer, Vertex> vertices;
	private int	numOfVertices;
	private int	numOfEdges;
	private Vertex source;
	private Vertex destination;
	
	//returns the number of vertices in the graph
	public int getNumOfVertices(){
		return numOfVertices;
	}
	
	//returns the source node for the current operations
	public Integer getSource() {
		return source.getValueInVertex();
	}
	
	//used to set the value of source
	public void setSource(Integer source) throws Exception{
		
		if(vertices.get(source) != null)
			this.source = vertices.get(source);
		else{
			throw new Exception("source vertex not found!");
		}
	}
	
	//returns the destination vertex
	public Integer getDestination() {
		return destination.getValueInVertex();
	}
	
	//used to set the destination vertex
	public void setDestination(Integer destination) throws Exception {
		if(vertices.get(destination) != null)
			this.destination = vertices.get(destination);
		else{
			throw new Exception("destinationination vertex not found!");
		}
	}
	
	//returns the mapping of adjacent vertices for any vertex
	public Map<Integer, Vertex> getVertices() {
		return vertices;
	}
	
	//returns the map of adjacent vertices 
	public Set<Entry<Integer, Integer>>  getMapOfAdjVertices(Integer vertex){
		return vertices.get(vertex).getMapOfAdjVertices();
	}
	
	//returns the next hop neighbour during the routing 
	public Map.Entry<Integer, String> getNextHop(Integer n, String destination){
		return vertices.get(n).routingTable.ipPrefixMatch(destination);
	}
	
	//constructor for the graph node 	
	public Graph (){
		numOfVertices = 0;
		numOfEdges = 0;
		vertices = new HashMap<Integer, Vertex>();
		source = null;
		destination = null;
		
	}
	
	//method readInputFile is used to create vertices for the graph based on the file input
	
	public void readInputFile(File inputFile) throws IOException{
		
		//input file is read 
		 Scanner scan = new Scanner(inputFile);
      
		 numOfVertices = scan.nextInt();
		 numOfEdges = scan.nextInt();
		
		//generate all vertices
		for (int i = 0; i < numOfVertices; i++) {
			vertices.put((Integer) i, new Vertex(i));
		}

		for(int i=0;i<numOfEdges;i++) {
			Vertex vertex1 = vertices.get(new Integer(scan.nextInt()));
			Vertex vertex2 = vertices.get(new Integer(scan.nextInt()));
			int w = scan.nextInt();
			vertex1.linkAdjVertex(vertex2.getValueInVertex(), w);
			vertex2.linkAdjVertex(vertex1.getValueInVertex(), w);
		}
		scan.close();
	}	
	
	//do post order traversal of the routing table trie 
	public void traverseNode(Integer n){
		vertices.get(n).getRoutingTable().postOrderHelper();
		
	}
	
	//add an entry to the routing table
	public void addEntryRoutingTable(Integer node, String destination, Integer next) throws Exception{		
		vertices.get(node).addPairRoutingTable(destination, next);
	}
	

	//method to convert IP to a string of binary digits
	public String ipToString(){
		String str = "";
		str += "Source: "+getSource()+"\n";
		str += "Destination: "+getDestination()+"\n";
		for (Map.Entry<Integer, Vertex> entry : vertices.entrySet()) {
		    Integer key = entry.getKey();
		    Vertex value = entry.getValue();
		    str += "Vertex("+key+"):";
		    for(Map.Entry<Integer, Integer> e: value.getMapOfAdjVertices()){
		    	str += "Ady("+e.getKey()+", "+e.getValue()+"), ";
		    }
		    str += "\n";
		}
		
		return str;
	}
	

	//class defining the vertex of a dijkstras graph
	private class Vertex{
		private Integer valueInVertex;
		private Map<Integer, Integer> mapOfAdjVertices; 
		private BinaryTrieImplementation routingTable;
		
		//returns the node ID 
		public Integer getValueInVertex() {
			return valueInVertex;
		}
		
		//puts the incoming vertex in the adjacent list of the given node
		public void linkAdjVertex(Integer v2, Integer weight){
			mapOfAdjVertices.put(v2, weight);			
		}
		
		//intializes the map of all adjacent vertices for the given vertex
		public Set<Entry<Integer, Integer>> getMapOfAdjVertices(){
			return mapOfAdjVertices.entrySet();
		}
		
		//enables inserting of nexvertex for a given destination 
		public void addPairRoutingTable(String destination, Integer nextVertex) throws Exception{ 
			routingTable.insertIntoTrie(destination, nextVertex);
		}
		
		//returns the routing table for a given vertex
		public BinaryTrieImplementation getRoutingTable(){
			return routingTable;
		}
		
		//constructor for the vertex class
		public Vertex(Integer valueInVertex) {
			this.valueInVertex = valueInVertex;
			routingTable = new BinaryTrieImplementation();
			mapOfAdjVertices = new HashMap<Integer, Integer>();
		}
			
	}
	
	

}
