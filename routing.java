import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;



public class routing {

	public static void main(String args[]){
		
		//inputFile1 is the file with the graph parameters and  inputFile2 is file with list of IPs
		File inputFile1 = new File(args[0]);
		File inputFile2 = new File(args[1]);
		String cmdSource = args[2];
		String cmdDestination = args[3];
		
		//initialize public variables numOfVertices 
		int numOfVertices = 0;
		
		//finding numOfEdges to pass it into the IP file reading method
		if(inputFile1.exists()) {
			try {
				Scanner scan = new Scanner(inputFile1);
				numOfVertices = scan.nextInt();				
				scan.close();
			} catch(Exception ex){
				System.out.println(ex.getMessage());
				System.exit(1);
			}
		}

		Graph graphObj = new Graph();
		Map<Integer, String>	ips = new HashMap<Integer, String>();
		
		// loading input file having the graph vertices and edges
		try{
			graphObj.readInputFile(inputFile1);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
			System.exit(1);
		}
		
		//loading file with ips for the vertices 
		try{
			ips = readIPFile(inputFile2,numOfVertices);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
			System.exit(1);
		}
		
		
		//assigning source and destination obtained from command line input
		int source = Integer.parseInt(cmdSource);
		int destination = Integer.parseInt(cmdDestination);
		int minDistance = -1;
	
		//Create Routing table 
		shortestPathHelper spfObj = new shortestPathHelper();
		for(int i=0;i<graphObj.getNumOfVertices();i++){	
			Map.Entry<Integer[], Integer[]> eachEntry = spfObj.shortestPathFinder(graphObj, i+"");
				Integer[] distance = eachEntry.getKey();
				Integer[] previous = eachEntry.getValue();
			
			
 			for(int j=0;j<graphObj.getNumOfVertices();j++){	
	 			if(i != j){	
		 				Integer storeJ = j;
		 				while(!previous[storeJ].equals(graphObj.getSource())){
		 					storeJ = previous[storeJ];
		 				}	 				
		 				
		 				try{
		 					graphObj.addEntryRoutingTable(i, ips.get(j), storeJ);
		 				}
		 				
		 				catch(Exception ex){
		 					ex.printStackTrace();
		 					System.exit(1);
		 				}
		 		}
	 			if(i ==  source && j == destination){
	 					minDistance = distance[destination];
	 			}
			}
 			graphObj.traverseNode(i);
		}
		//simulating packet transfer between source and destination using network 
		System.out.println(minDistance);
		Integer traverseNode = source;
		while(traverseNode != destination){
			Map.Entry<Integer, String> inputIPFileEntry  = graphObj.getNextHop(traverseNode, ips.get(destination));
			System.out.print(inputIPFileEntry.getValue()+" ");
			traverseNode = inputIPFileEntry.getKey();
		}
		
	}

	//read the input file with IPs and map the IPs to the corresponding vertices
	public static Map<Integer, String> readIPFile(File inputIPFile, int numOfVertices) throws Exception{
		Map<Integer, String> mapOfIPs = new HashMap<Integer, String>();
		Scanner scan = new Scanner(inputIPFile);
		for(int i=0;i<numOfVertices;i++) {
	        String binaryIP =  formatIP(ipToBinary(scan.next()));
            mapOfIPs.put(i, binaryIP);
		}
		scan.close();
		return mapOfIPs;
	}
	
	//format the IP to get 32 bit binary string, if not 32 bits, pad zeroes at the end to make 32 bit
	public static String formatIP(String ip){
		while(ip.length() < 32){
			ip = "0"+ip;
		}
		return ip;
	}
	
	//converting to binary IP string
	public static String ipToBinary(String ip) throws Exception{
		InetAddress ipAdr = InetAddress.getByName(ip);
		byte[] ipInBytes = ipAdr.getAddress();	
		String binaryIPstring = new BigInteger(1, ipInBytes).toString(2);
		return binaryIPstring;
	}
	
	
}
