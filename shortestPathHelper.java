import java.util.AbstractMap;
import java.util.Map;


public class shortestPathHelper {
	public Map.Entry<Integer[], Integer[]> shortestPathFinder (Graph gObj, String source){		

		try{
			gObj.setSource(new Integer(source));
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		FibonacciHeap fibHeapObj = new FibonacciHeap();
		Integer[] distance = new Integer[gObj.getNumOfVertices()];
		Integer[] previousVertex = new Integer[gObj.getNumOfVertices()];
		

		for(int i=0;i<gObj.getNumOfVertices();i++){
			Integer nodeLabel = new Integer(i);
			if (!nodeLabel.equals(gObj.getSource())){
				distance[i] = Integer.MAX_VALUE;
				
			}else
				distance[i] = 0;
			previousVertex[i] = -1;
			fibHeapObj.insertIntoHeap(nodeLabel, distance[i]);
		}
		while(!fibHeapObj.isEmpty()){
			Integer tempMinCost = fibHeapObj.removeMin();
			for (Map.Entry<Integer, Integer> eachAdjVertex: gObj.getMapOfAdjVertices(tempMinCost)){
				Integer adjacentVertex = eachAdjVertex.getKey();
			    Integer cost = eachAdjVertex.getValue();
			    int alt = distance[tempMinCost] + cost;
			    if(alt < distance[adjacentVertex]){
			    	distance[adjacentVertex] = alt;
			    	previousVertex[adjacentVertex] = tempMinCost;
			    	fibHeapObj.decreaseKey(adjacentVertex, alt);
			    }
			}
		}
		Map.Entry<Integer[],Integer[]> result = new AbstractMap.SimpleEntry<Integer[], Integer[]>(distance, previousVertex);
		return result;
	}

}

