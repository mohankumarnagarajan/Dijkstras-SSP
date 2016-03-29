import java.util.*;

public class FibonacciHeap {
	
	//min ptr for the heap
    private fibHeapNode minNodePtr;
    //keeps count of heap size
    private int heapSize;
    private Map<Integer, fibHeapNode> mapOfNodes;
    
    //constructor for fibnocci class
    public FibonacciHeap() {
		minNodePtr = null;
		heapSize = 0;
		mapOfNodes = new HashMap<Integer, fibHeapNode>();
	}
    
    //supporting methods for further functionalities
    
    public Integer returnMin() {			// method to return the current minNodePtr value of heap 
        
        return minNodePtr.getNodeValue();
    }

    public boolean isEmpty() {				//method to check for emptiness of heap
        return minNodePtr == null;
    }

 
    public int returnSize() {					//method to return no of elements in the heap
        return heapSize;
    }
    
    //method to insert node into heap 
    
    public void insertIntoHeap(Integer NV, int EW) {
        fibHeapNode newfibHeapNode = new fibHeapNode(NV, EW);    
        mapOfNodes.put(NV, newfibHeapNode);    
        minNodePtr = mergeTrees(minNodePtr, newfibHeapNode);
        heapSize += 1;			//one new element is added in heap
    }
    
    //method to remove min from the heap
    
    public Integer removeMin() {
        heapSize -= 1;			//reducing size by one since min is removed
        fibHeapNode minNodePtrCopy = minNodePtr;
        if (minNodePtr.rightSibling == minNodePtr) { 
            minNodePtr = null; 
        }
        else {
            minNodePtr.leftSibling.rightSibling = minNodePtr.rightSibling;
            minNodePtr.rightSibling.leftSibling = minNodePtr.leftSibling;
            minNodePtr = minNodePtr.rightSibling; 
        }
        
        if (minNodePtrCopy.child != null) {
            
            fibHeapNode n = minNodePtrCopy.child;
            
            do {
                n.parent = null;
                n = n.rightSibling;
            } while (n != minNodePtrCopy.child);
        }

        minNodePtr = mergeTrees(minNodePtr, minNodePtrCopy.child);

        if (minNodePtr == null) return minNodePtrCopy.getNodeValue();
        
        List<fibHeapNode> treeTable = new ArrayList<fibHeapNode>();
        List<fibHeapNode> toVisit = new ArrayList<fibHeapNode>();

        for (fibHeapNode n = minNodePtr; toVisit.isEmpty() || toVisit.get(0) != n; n = n.rightSibling)
            toVisit.add(n);

        for (fibHeapNode n: toVisit) {    
            while (true) {   
                while (n.degree >= treeTable.size())
                    treeTable.add(null);

                if (treeTable.get(n.degree) == null) {
                    treeTable.set(n.degree, n);
                    break;
                }
                
                fibHeapNode tree2 = treeTable.get(n.degree);
                treeTable.set(n.degree, null); 

                fibHeapNode minNodePtr = (tree2.edgeWeight < n.edgeWeight)? tree2 : n;
                fibHeapNode max = (tree2.edgeWeight < n.edgeWeight)? n  : tree2;

                max.rightSibling.leftSibling = max.leftSibling;
                max.leftSibling.rightSibling = max.rightSibling;

                max.rightSibling = max;
                max.leftSibling = max;
                minNodePtr.child = mergeTrees(minNodePtr.child, max);
                
                max.parent = minNodePtr;
                max.childCut = false;
                minNodePtr.degree += 1;               
                n = minNodePtr;
            }

            if (n.edgeWeight <= minNodePtr.edgeWeight) minNodePtr = n;
        }
        return minNodePtrCopy.getNodeValue();
    }
    
    //method to delete a node from the heap

    public void delete(fibHeapNode deleteNode) throws Exception{  
        decreaseKey(deleteNode.getNodeValue(), Integer.MIN_VALUE);
        removeMin();
    }
    
    //method to merge two trees in the heap 
    
    private fibHeapNode mergeTrees(fibHeapNode tree1, fibHeapNode tree2) {
       
        if (tree1 == null && tree2 == null) { // Both null, resulting list is null.
            return null;
        }
        else if (tree1 != null && tree2 == null) { // Two is null, result is one.
            return tree1;
        }
        else if (tree1 == null && tree2 != null) { // One is null, result is two.
            return tree2;
        }
        else { 
            fibHeapNode oneNext = tree1.rightSibling;
            tree1.rightSibling = tree2.rightSibling;
            tree1.rightSibling.leftSibling = tree1;
            tree2.rightSibling = oneNext;
            tree2.rightSibling.leftSibling = tree2;
            
            return tree1.edgeWeight < tree2.edgeWeight? tree1 : tree2;
        }
    }
    
    
    //method to perform childcut
    
    private void childCutFibHeapNode(fibHeapNode tempNode) {
        
    	tempNode.childCut = false;
        
        if (tempNode.parent == null) return;

        if (tempNode.rightSibling != tempNode) { 
        	tempNode.rightSibling.leftSibling = tempNode.leftSibling;
        	tempNode.leftSibling.rightSibling = tempNode.rightSibling;
        }
        
        if (tempNode.parent.child == tempNode) {
            
            if (tempNode.rightSibling != tempNode) {
            	tempNode.parent.child = tempNode.rightSibling;
            }
            else {
            	tempNode.parent.child = null;
            }
        }

       
        tempNode.parent.degree -= 1;

        tempNode.leftSibling = tempNode.rightSibling = tempNode;
        minNodePtr = mergeTrees(minNodePtr, tempNode);
        
        if (tempNode.parent.childCut)
            childCutFibHeapNode(tempNode.parent);
        else
        	tempNode.parent.childCut = true;
        
        tempNode.parent = null;
    }
    
    //method to perform decreasekey
    
    public void decreaseKey(Integer nodeLabel, int edgeWeight) {
    	fibHeapNode tempNode = mapOfNodes.get(nodeLabel);
    	if(tempNode != null){
    		tempNode.edgeWeight = edgeWeight;
    	       
            if (tempNode.parent != null && tempNode.edgeWeight <= tempNode.parent.edgeWeight)
                childCutFibHeapNode(tempNode);
          
            if (tempNode.edgeWeight <= minNodePtr.edgeWeight)
                minNodePtr = tempNode;    		
    	}
        
    }

   

    
 //fibonocci heap node 
    
    private class fibHeapNode {
        private int     	degree = 0;		//degree of node
        private fibHeapNode leftSibling;	//left sibling of current node
        private fibHeapNode rightSibling;	//right sibling of current node
        private fibHeapNode parent;			//parent of given node
        private fibHeapNode child;  		//child of given node
        private Integer     nodeValue;   	//the value contained in the given node  
        private int 		edgeWeight; 	//weight of the edge connecting the node to prev node
        private boolean 	childCut = false; //childcut by default set as false


        //getter setters for nodeValue and weight
        public Integer getNodeValue() {
            return nodeValue;
        }

//        public double getWeight() {
//            return edgeWeight;
//        }
//       
//        public void setNodeValue(Integer nodeValue) {
//            this.nodeValue = nodeValue;
//        }


        //constructor for fibonocci node
        private fibHeapNode(Integer NV, int edgeWeight) {
        	this.nodeValue = NV;
            this.edgeWeight = edgeWeight;
            this.rightSibling = this;
            this.leftSibling = this;            
        }
        
    }
    
    
}