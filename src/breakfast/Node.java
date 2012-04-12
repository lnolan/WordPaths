package breakfast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Node {
	public String word;
	public ArrayList<Node> links = new ArrayList<Node>();
	public int partition = -1;

	public Node(String w){
		word = w;
	}

	public void addLink(Node n){
		links.add(n);
	}



	// Breadth First Search
	public List<String> findPathToBreadthFirst(Node to, Set<Node> searched) {
		searched.add(this);
		Queue<List<Node>> searchState = new LinkedList<List<Node>>();

		List<Node> root = new ArrayList<Node>();
		root.add(this);
		searchState.add(root);

		while(!searchState.isEmpty()){
			List<Node> current = searchState.remove();

			Node currentNode = current.get(current.size()-1);
			if(currentNode.word.equals(to.word)){
				return getStringList(current);
			}else{
				for(Node n : currentNode.links){
					if(!searched.contains(n)){
						List<Node> next = new ArrayList<Node>();
						next.addAll(current);
						next.add(n);
						searchState.add(next);
						searched.add(n);
					}
				}
			}
		}

		return null; // not reachable
	}



	private List<String> getStringList(List<Node> current) {
		List<String> result = new ArrayList<String>();
		for(Node n : current){
			result.add(n.word);
		}

		return result;
	}

	// Depth first search
	public List<String> findPathToDepthFirst(Node to, Set<Node> searched) {
		searched.add(this);

		if(to.word.equalsIgnoreCase(word)){
			ArrayList<String> result = new ArrayList<String>();
			result.add(word);
			return result;
		}else{
			for(Node n : links){
				if(!searched.contains(n)){ // avoid loops
					List<String> result = n.findPathToDepthFirst(to, searched);
					if(result != null){
						result.add(word);
						return result;
					}
				}
			}
		}

		return null; // not reachable
	}

	// heuristic search - try to move towards words with lowest edit distance
	public List<String> findPathToHeuristic(Node to, Set<Node> searched) {
		searched.add(this);

		if(to.word.equalsIgnoreCase(word)){
			ArrayList<String> result = new ArrayList<String>();
			result.add(word);
			return result;
		}else{
			Node[] linksInHeuristicOrder = sortLinks(to.word);

			for(Node n : linksInHeuristicOrder){
				if(!searched.contains(n)){ // avoid loops
					List<String> result = n.findPathToHeuristic(to, searched);
					if(result != null){
						result.add(word);
						return result;
					}
				}
			}
		}

		return null; // not reachable
	}

	private Node[] sortLinks(String target) {
		HeuristicNodeSorter hns = new HeuristicNodeSorter(target);
		Node[] nodes = new Node[links.size()];

		for(int i = 0; i < links.size(); i++){
			nodes[i] = links.get(i);
		}
		Arrays.sort(nodes, hns);
		return nodes;
	}
}

class HeuristicNodeSorter implements Comparator<Node>{
	String theWord;

	public HeuristicNodeSorter(String w){
		theWord = w;
	}

	@Override
	public int compare(Node arg0, Node arg1) {
		int lev0 = lev(arg0.word);
		int lev1 = lev(arg1.word);

		if(lev0 == lev1) return 0;
		else if(lev0 < lev1) return -1;
		else return 1;
	}

	private int lev(String s){
		int lev = 0;
		for(int i = 0; i < theWord.length(); i++){
			if(theWord.charAt(i) != s.charAt(i)) lev++;
		}
		return lev;
	}
}