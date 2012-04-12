package breakfast;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Graph {
	Map<String, Node> nodes = new HashMap<String, Node>();
	int numNodes = 0;

	public Graph(List<String> words){
		for(int i = 0; i < words.size(); i++){
			add(words.get(i));
		}

		partition(); // costs 10 msec, saves 6
	}

	// precondition - things must be added in order
	private void add(String word){
		Node n = new Node(word);
		nodes.put(word,  n);

		// get set of words 1 edit away (100 words)
		// for each one, check if exists in the graph - if so, link
		// this algorithm is constant time-ish (the binary search part is O log n)
		// rather than exponential with the number of words in graph
		// which would be the case if we searched all entries to see if they were neighbours
		String[] neighbours = NeighbourGenerator.neighbourGenerator(word);

		for(String s : neighbours){
			Node sNode = nodes.get(s);
			if(sNode != null){
				sNode.addLink(n);
				n.addLink(sNode);
			}
		}
	}

	public Node findNode(String word){
		return nodes.get(word);
	}

	public List<String> findPathFrom(Node from, Node to){
		if(from.partition != to.partition){ // no path
			return null; // skipping this part changes the heuristic search from 4 to 10 seconds
		}

		// Just for fun - three variants of the search
		// return from.findPathToDepthFirst(to, new HashSet<Node>()); // a minute
		return from.findPathToHeuristic(to, new HashSet<Node>()); // 4 seconds
		//return from.findPathToBreadthFirst(to, new HashSet<Node>()); // 98 seconds
	}

	public void partition(){
		int currentPartition = 0;

		int count = 0;
		for(String word : nodes.keySet()){
			Node curNode = nodes.get(word);
			if(curNode.partition == -1){
				recursivelyMarkNodeColour(curNode, currentPartition);
				currentPartition++;
			}
			count++;
		}
	}

	private void recursivelyMarkNodeColour(Node curNode,
			int currentColour) {
		curNode.partition = currentColour;

		for(Node child : curNode.links){
			if(child != null && child.partition == -1)
				recursivelyMarkNodeColour(child, currentColour);
		}
	}
}
