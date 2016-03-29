import java.util.AbstractMap;
import java.util.Map;

public class BinaryTrieImplementation {
	private baseNode rootOfTrie;
	
	public BinaryTrieImplementation(){
		rootOfTrie = (baseNode) new internalTrieNode();
		rootOfTrie.setLeft(null);
		rootOfTrie.setRight(null);
	}
	

public void postOrderHelper(){ //traverse method becomes triePostOrder helper
	triePostOrder(null, rootOfTrie, -1);
}
	
private void triePostOrder(baseNode p, baseNode node, int d){ //post order = trie postorder
		if (node != null){
			triePostOrder(node, node.getLeft(), 0);
			triePostOrder(node, node.getRight(), 1);
			visitTriebaseNode(p, node, d);
		}
			
	}
	//method to assist in postorder traversal of trie
private void visitTriebaseNode(baseNode p, baseNode n, int d){
	if(n.getKey() == null){
		if (n.getLeft() == null && n.getRight() == null) {
			if(n.getLeft() == null && n.getRight() != null) {
				if( n.getRight().getValue() != null){
						
						if(d == 1){
							p.setRight(n.getRight());
						}
						else{
							p.setLeft(n.getRight());
						}
						
				}
			} else{
				if(n.getLeft() != null && n.getRight() == null){
					if(n.getLeft().getValue() != null){
						if(d == 1){
							p.setRight(n.getLeft());
						}
						else{
							p.setLeft(n.getLeft());
						}
					}
				}
			}				
			} else if(n.getLeft() != null && n.getRight() != null){
				if(n.getLeft().getValue() != null && n.getRight().getValue() != null){
					if(n.getLeft().getValue().equals(n.getRight().getValue())){
						if(d == 1){
							p.setRight(n.getLeft());
						}
						else{
							p.setLeft(n.getLeft());
						}
					}
				}
			}
		}		
	}
	

	//method to insertIntoTrie nodes into trie
	public void insertIntoTrie (String key, Integer value) throws Exception {
		//creating root parent and grandparent for given node
		baseNode tempTrieRoot = rootOfTrie;
		baseNode parentNode = null;
		baseNode grandParentNode = null;
		int level = 0;
		
		while(tempTrieRoot != null){
			grandParentNode = parentNode;
			parentNode = tempTrieRoot;
			if(key.substring(level, level+1).equals("0")){
				tempTrieRoot = tempTrieRoot.getLeft();	
			}
			else{
				tempTrieRoot = tempTrieRoot.getRight();	
			}
			level += 1;
		}
		
		if(parentNode.getKey() == null){ //Prev is not element node
			baseNode newbaseNode = new externalTrieNode(key, value);
			if(key.substring(level-1, level).equals("0")){
				parentNode.setLeft(newbaseNode);
			}
			else{
				parentNode.setRight(newbaseNode);
			}
		}else{ //node is element node
			if(parentNode.getKey().equals(key)){
				throw new Exception("Duplicate key is inserted!!");
			}
			else{
				baseNode n = new internalTrieNode();
				
				if(key.substring(level-2, level-1).equals("0")){
					grandParentNode.setLeft(n);
				}
				else{
					grandParentNode.setRight(n);
				}
				level = level -1;
				while(parentNode.getKey().substring(level, level+1).equals(key.substring(level, level+1))){
					baseNode n2 = new internalTrieNode();
					
					if(key.substring(level, level+1).equals("0"))
						n.setLeft(n2);
					else
						n.setRight(n2);
					
					n = n2;
					level = level + 1;
				}
				
				if(key.substring(level, level+1).equals("0")){
					n.setLeft(new externalTrieNode(key, value));
					n.setRight(parentNode);
				}
				else{
					n.setRight(new externalTrieNode(key, value));
					n.setLeft(parentNode);
				}
			}			
		}
	}
	
	public boolean searchTrieNode(String value){
		
		return false;
	}
	
	//method to perform longest prefix prefixMatcheding of IP
	public Map.Entry<Integer, String> ipPrefixMatch(String key){
		Integer mapOfIPs;
		//the longest matching prefix
		String prefixMatched ="";		
		baseNode checkNode = null;
		baseNode tempRoot = rootOfTrie;
		int ipSize = 0;
		while(tempRoot != null && ipSize < 32){
			checkNode = tempRoot;
			if(key.substring(ipSize, ipSize+1).equals("0")){
				tempRoot = tempRoot.getLeft();
				if(tempRoot != null){prefixMatched = prefixMatched+"0";ipSize += 1;}
			}else{
				tempRoot = tempRoot.getRight();
				if(tempRoot != null){prefixMatched = prefixMatched+"1";ipSize += 1;}
			}
			
		}
		if(ipSize == 32) mapOfIPs = tempRoot.getValue();
		else mapOfIPs = checkNode.getValue();
		Map.Entry<Integer,String> resultingMatch = new AbstractMap.SimpleEntry<Integer, String>(mapOfIPs ,prefixMatched );
		return resultingMatch;
	}
	

	private class baseNode{
		
		//getters for baseNode
		public baseNode getLeft(){return null;} 
		public String getKey(){	return null;}
		public baseNode getRight(){	return null;}
		
		// setters for baseNode
		public void setRight(baseNode r){ }
		public void setLeft(baseNode r){ }
		public Integer getValue() { return null;}
	}
	
	private class internalTrieNode extends baseNode{
		//internal node contains left and right pointers
		private baseNode left;
		private baseNode right;
		
		public internalTrieNode(){
			left = null;
			right = null;
		}
		
		//getters setters for internal trie node
		@Override
		public baseNode getRight(){
			return right;
		}
		
		@Override
		public baseNode getLeft(){
			return left;
		}
		
		public void setRight(baseNode r){
			right = r;
		}
		
		public void setLeft(baseNode r){
			left = r;
		}
	}
	
	private class externalTrieNode extends baseNode{
		private String key;
		private Integer value;
		
		public externalTrieNode(String key, Integer value){
			this.value = value;
			this.key = key;
		}
		
		public String getKey(){
			return key;
		}
		
		public Integer getValue(){
			return value;
		}
		
		
	}
}
