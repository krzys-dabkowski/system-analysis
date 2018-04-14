/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.systempaths;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.Variable;

/**
 * A class executing Tarjan algorithm.
 * Not used in the production.
 * 
 * @author krzysztof.dabkowski
 * 
 */
public class Tarjan {

	private int index = 0;
	private ArrayList<Node> stack = new ArrayList<Node>();
	private ArrayList<ArrayList<Node>> SCC = new ArrayList<ArrayList<Node>>();
	
	public void findCycles() {
		AdjacencyList graph = new AdjacencyList();
		for(Variable variable : ModelProvider.INSTANCE.getVariables()) {
			for(Variable variable2 : ModelProvider.INSTANCE.getVariables()) {
				if(variable.getDelayValueToVariable(variable2.getIndex()) != 0
						&& variable.getImpactValueToVariable(variable2.getIndex()) != 0) {
					graph.addEdge(new Node(variable.getIndex()), new Node(variable2.getIndex()));
				}
			}
		}
		executeTarjan(graph);
	}

	/* The funtion tarjan has to be called for every unvisited node of the graph */
	private ArrayList<ArrayList<Node>> executeTarjan(AdjacencyList graph) {
		SCC.clear();
		index = 0;
		stack.clear();
		if (graph != null) {
			List<Node> nodeList = new ArrayList<Node>(graph.getSourceNodeSet());
			if (nodeList != null) {
				for (Node node : nodeList) {
					if (node.index == -1) {
						tarjan(node, graph);
					}
				}
			}
		}
		return SCC;
	}

	private ArrayList<ArrayList<Node>> tarjan(Node investigatedNode, AdjacencyList list) {
		investigatedNode.index = index;
		investigatedNode.lowLink = index;
		index++;
		stack.add(0, investigatedNode);
		for (Edge edge : list.getAdjacent(investigatedNode)) {
			Node to = edge.getTo();
			if (to.index == -1) {
				tarjan(to, list);
				investigatedNode.lowLink = Math.min(investigatedNode.lowLink, to.lowLink);
			} else if (stack.contains(to)) {
				investigatedNode.lowLink = Math.min(investigatedNode.lowLink, to.index);
			}
		}
		if (investigatedNode.lowLink == investigatedNode.index) {
			Node n;
			ArrayList<Node> component = new ArrayList<Node>();
			do {
				n = stack.remove(0);
				component.add(n);
			} while (n != investigatedNode);
			SCC.add(component);
		}
		return SCC;
	}
}
