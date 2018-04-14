/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.systempaths;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A class for executing Tarjan.
 * 
 * @author krzysztof.dabkowski
 *
 */
public class AdjacencyList {

	private Map<Node, List<Edge>> adjacencies = new HashMap<Node, List<Edge>>();
	
	public void addEdge(Node source, Node target) {
		List<Edge> list;
		if(!adjacencies.containsKey(source)) {
			list = new ArrayList<Edge>();
			adjacencies.put(source, list);
		} else {
			list = adjacencies.get(source);
		}
		list.add(new Edge(source, target));
	}
	
	public List<Edge> getAdjacent(Node source) {
		return adjacencies.get(source);
	}
	
	public void reverseEdge(Edge e) {
		adjacencies.get(e.getFrom()).remove(e);
		addEdge(e.getTo(), e.getFrom());
	}
	
	private AdjacencyList getReversedList() {
		AdjacencyList newList = new AdjacencyList();
		for(List<Edge> list : adjacencies.values()) {
			for(Edge edge : list) {
				newList.addEdge(edge.getTo(), edge.getFrom());
			}
		}
		return newList;
	}	
	
	public void reverseGraph() {
		adjacencies = getReversedList().adjacencies;
	}
	
	public Set<Node> getSourceNodeSet() {
		return adjacencies.keySet();
	}
	
	public Collection<Edge> getAllEdges() {
		List<Edge> allEdges = new ArrayList<Edge>();
		for(List<Edge> list : adjacencies.values()) {
			allEdges.addAll(list);
		}
		return allEdges;
	}
}
