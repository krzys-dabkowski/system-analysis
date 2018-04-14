/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.systempaths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.Path;
import ch.uzh.business.systemanalyse.tool.model.Variable;
import ch.uzh.business.systemanalyse.tool.model.VariableValue;

/**
 * Implements an algorithm for finding paths between
 * nodes (variables).
 * 
 * @author Krzysztof Dabkowski
 *
 */
public class PathsFinder {

	// a map storing all the edges: for each node (first integer)
	// there is a map storing an index of an edge and the connected node
	private Map<Integer, ArrayList<Integer>> edges;

	public PathsFinder() {
		edges = findEdges();
	}

	public PathsFinder(ArrayList<Integer> variableIndices) {
		edges = findEdges(variableIndices);
	}

	private Map<Integer, ArrayList<Integer>> findEdges() {
		Map<Integer, ArrayList<Integer>> cEdges = new HashMap<Integer, ArrayList<Integer>>();

		for (Variable node : ModelProvider.INSTANCE.getVariables()) {
			ArrayList<Integer> subList = new ArrayList<Integer>();
			for(Variable node2 : ModelProvider.INSTANCE.getVariables()) {
				if(node.getDelayValueToVariable(node2.getIndex()) != 0 && node.getImpactValueToVariable(node2.getIndex()) != 0) {
					subList.add(node2.getIndex());
				}
			}
			
			cEdges.put(node.getIndex(), subList);
		}
		return cEdges;
	}

	private Map<Integer, ArrayList<Integer>> findEdges(ArrayList<Integer> variableIndices) {
		Map<Integer, ArrayList<Integer>> cEdges = new HashMap<Integer, ArrayList<Integer>>();
		//int noNodes = ModelProvider.INSTANCE.getVariables().size();

		for (Variable node : ModelProvider.INSTANCE.getVariables()) {
			//if(node.getIndex() != variableIndex) {
				ArrayList<Integer> subList = new ArrayList<Integer>();
				for(Variable node2 : ModelProvider.INSTANCE.getVariables()) {
					if(!variableIndices.contains(node2.getIndex())) {
						if(node.getDelayValueToVariable(node2.getIndex()) != 0 && node.getImpactValueToVariable(node2.getIndex()) != 0) {
							subList.add(node2.getIndex());
						}
					}
				}
				/*for(int j = 1; j <= noNodes; j++) {
					if(!variableIndices.contains(j)) {
						if(node.getDelayValue(j) != 0 && node.getImpactValue(j) != 0) {
							subList.add(ModelProvider.INSTANCE.getVariable(j).getIndex());
						}
					}
				}*/
				cEdges.put(node.getIndex(), subList);
			//}
		}
		return cEdges;
	}
	
	public ArrayList<Path> findPathsTo(int start) {
		ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();
		
		for(Variable var : ModelProvider.INSTANCE.getVariables()) {
			if(var.getIndex() == start) {
				ArrayList<ArrayList<Integer>> pathsWithStart = new ArrayList<ArrayList<Integer>>();
				for(int i : edges.get(var.getIndex())) {
					ArrayList<Integer> path = new ArrayList<Integer>();
					path.add(i);
					pathsWithStart.addAll(findPaths(start, i, path));
				}
				for(ArrayList<Integer> p : pathsWithStart) {
					p.add(0, var.getIndex());
				}
				paths.addAll(pathsWithStart);
			} else {
				paths.addAll(findPaths(start, var.getIndex()));
			}
		}
		
		ArrayList<Path> pathsArray = new ArrayList<Path>();
		
		int count = 1;
		for(ArrayList<Integer> path1 : paths) {
			Path path = new Path();
			path.setDelay(calculateDelay(path1));
			path.setEffect(calculateEffect(path1));
			path.setNodes(path1);
			path.setIndex(count);
			pathsArray.add(path);
			count++;
		}
		
		return pathsArray;
	}

	public ArrayList<Path> findPaths(int end) {
		ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();
		
		for(Variable var : ModelProvider.INSTANCE.getVariables()) {

			if(var.getIndex() == end) {
				ArrayList<ArrayList<Integer>> pathsWithEnd = new ArrayList<ArrayList<Integer>>();
				for(int i : edges.get(var.getIndex())) {
					ArrayList<Integer> path = new ArrayList<Integer>();
					path.add(i);
					pathsWithEnd.addAll(findPaths(i, end, path));
				}
				for(ArrayList<Integer> p : pathsWithEnd) {
					p.add(0, var.getIndex());
				}
				paths.addAll(pathsWithEnd);
			} else {
				paths.addAll(findPaths(var.getIndex(), end));
			}
		}

		ArrayList<Path> pathsArray = new ArrayList<Path>();

		int count = 1;
		for(ArrayList<Integer> path1 : paths) {
			Path path = new Path();
			path.setDelay(calculateDelay(path1));
			path.setEffect(calculateEffect(path1));
			path.setNodes(path1);
			path.setIndex(count);
			pathsArray.add(path);
			count++;
		}

		return pathsArray;


	}

	public ArrayList<Path> findAllPaths(int start, int end) {

		ArrayList<ArrayList<Integer>> paths = findPaths(start, end);

		ArrayList<Path> pathsArray = new ArrayList<Path>();

		int count = 1;
		for(ArrayList<Integer> path1 : paths) {
			Path pPath = new Path();
			pPath.setDelay(calculateDelay(path1));
			pPath.setEffect(calculateEffect(path1));
			pPath.setNodes(path1);
			pPath.setIndex(count);
			pathsArray.add(pPath);
			count++;
		}

		return pathsArray;
	}
	
	public ArrayList<Path> findLoops() {
		
		int [] rueck = new int[5];
		for(int i = 0; i< 5; i++) {
			rueck[i] = 0;
		}
		
		//int w = 0;
		
		int z = ModelProvider.INSTANCE.getVariables().size();
		float [][][] wert = new float[z][z][3];
		for(int x = 0; x< z; x++) {
			for(int y = 0; y < z; y++) {
				wert[x][y][0] = 0;
				wert[x][y][1] = 0;
				wert[x][y][2] = 0;
			}
		}

		float [][][] variables = new float[z][z][2];

		int x = 0;
		int y = 0;
		for(Variable var : ModelProvider.INSTANCE.getVariables()) {
			y = 0;
			for(VariableValue vv : var.getImpactArray()) {
				variables[x][y][0] = vv.getVariableValue();
				variables[x][y][1] = var.getIndex();

				y++;
			}
			x++;
		}
		
		int [] kanten = new int[z];

		for(int i = 0; i < z; i++) {
			int w = 0;
			for(int j = 0; j < z; j++) {
				if(variables[i][j][0] != 0) {
					wert[i][w][0] = j;
					wert[i][w][1] = variables[i][j][0];
					wert[i][w][2] = variables[i][j][1];
					w++;
				}
			}
			kanten[i] = w;
		}

		int [] wArray = new int[z];
		int [] kArray = new int[z];
		int [] kontrollw = new int[z];
		int [] kontrollk = new int[z];
		float [][] schritt = new float[z][4];
		
		int c = 0;
		int d = 0;
		int h = 0;
		int res = 0;
		int r = 0;
		float [][] resultat = new float[99999][43];
		//int [] rueck = new int [5];
		
		for(int i = 0; i < z; i++) {
			for(int j = 0; j < z; j++) {
				wArray[j] = 1;
				kArray[j] = 0;
				for(c = 0; c < kanten[j]; c++) {
					if(wert[y][c][0] < i) {
						wArray[j]++;
					}
				}
				kontrollw[j] = wArray[j];
				if(wArray[j] > kanten[j]) {
					kArray[j] = 1;
				}
				kontrollk[j] = kArray[j];
			}

			schritt[0][3] = 1;
			c = 0;
			d = i;
			h = kanten[i];
			
			while(h > 0) {
				h = h - 1;
				int f = 1;
				while(f > 0) {
					c = c + 1;
					schritt[c][0] = c;
					schritt[c][1] = d;
					schritt[c][2] = wert[d][wArray[d]][0];
					schritt[c][3] = schritt[c - 1][4] * wert[d][wArray[d]][1];
					kArray[d]++;
					wArray[d]++;
					d = (int) schritt[c][2];
					if(d == 0) {
						c = c--;
						break;
					}
					
					
				}
				//Falls gueltige Loesung
				
				if(d == i && c > 0) {
					res++;
					if(res == 100000) {
						System.err.println("Die maximale Anzahl von Pfaden (100'000) ist überschritten!!!");
						return null;
					}
					resultat[res][0] = schritt[c][0];
					resultat[res][1] = schritt[c][3];
					if(resultat[res][1] == 1) {
						rueck[2] ++;
					} else {
						rueck[3]++;
					}
					
					for(r = 0; r < c; r++) {
						resultat[res][2 + r] = schritt[r][1];
					}
				}
				
				d = (int) schritt[c][1];
				while(c>0) {
					kArray[d] = kontrollk[d];
					if(!(wArray[d] > kanten[d])) {
						c--;
						break;
					} else {
						wArray[d] = kontrollw[d];
						c--;
						d = (int) schritt[c][1];
						kArray[d] = kontrollk[d];
					}
					f = c;
				}
				
				/*'Falls gueltige Loesung
	            If d = x And c > 0 Then
	                res = res + 1
	                If res = 100000 Then
	                    Debug.Print MsgBox("Die maximale Anzahl von Pfaden (100'000) ist überschritten!!!" + Strings.Chr(13) + "Die Berechnungen werden abgebrochen :-(", 48, "Tipp")
	                    Exit Function
	                    End If
	                resultat(res, 1) = schritt(c, 1)
	                resultat(res, 2) = schritt(c, 4)
	                If resultat(res, 2) = 1 Then
	                    rueck(2) = rueck(2) + 1
	                    Else
	                    rueck(3) = rueck(3) + 1
	                    End If
	                For r = 1 To c
	                    resultat(res, 2 + r) = schritt(r, 2)
	                    Next r
	                End If
	            'Suchen einer neuen Abzweigung
	            d = schritt(c, 2)
	            Do While c > 0
	                k(d) = kontrollk(d)
	                If Not w(d) > kanten(d) Then
	                    c = c - 1
	                    Exit Do
	                    Else
	                    w(d) = kontrollw(d)
	                    c = c - 1
	                    d = schritt(c, 2)
	                    k(d) = kontrollk(d)
	                    End If
	                Loop
	             f = c
	             Loop*/
			}
		}

		ArrayList<Path> pathsArray = new ArrayList<Path>();

		
		return pathsArray;
	}

	private ArrayList<ArrayList<Integer>> findPaths(int start, int end) {
		ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();


		ArrayList<Integer> path = new ArrayList<Integer>();
		path.add(start);
		paths.addAll(findPaths(start, end, path));

		return paths;
	}

	private ArrayList<ArrayList<Integer>> findPaths(int start, int end, ArrayList<Integer> path) {

		ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();
		if(!edges.isEmpty() && edges.get(start) != null) {
		for(int startPlus : edges.get(start)) {

			// check if my current path contains the current node already
			if (!path.contains(startPlus)) {

				ArrayList<Integer> newPath = new ArrayList<Integer>(path);
				// if it's end
				if (startPlus == end) {
					newPath.add(startPlus);
					paths.add(newPath);
				} else {
					// if it's a next node
					newPath.add(startPlus);
					paths.addAll(findPaths(startPlus, end, newPath));
				}
			}
		}
		}
		return paths;
	}

	public static float calculateDelay(ArrayList<Integer> path) {
		float delay = 0f;
		for(int i = 0; i < path.size() - 1; i++){
			delay += ModelProvider.INSTANCE.getVariable(path.get(i)).getDelayValueToVariable(path.get(i + 1));
		}
		return delay;
	}

	public static float calculateEffect(ArrayList<Integer> path) {
		float effect = 1f;
		for(int i = 0; i < path.size() - 1; i++){
			effect *= ModelProvider.INSTANCE.getVariable(path.get(i)).getImpactValueToVariable(path.get(i + 1));
		}
		return effect;
	}
	
	/**
	 * Checks, whether {@link Path} A and B do intersect. If they start and end in the same {@link Node} (two loops intersecting at a key variable),
	 * the first and the last {@link Node} of A are not considered in the check. 
	 * @author Alexander Schmid
	 * @return boolean, true if there is an intersection
	 * */
	public static boolean haveIntersection(Path pathA, Path pathB){
		
		if (!(pathA.getNodes().size() > 0) | !(pathB.getNodes().size() > 0) ){
			return false;
		}
		
		Path pathC = new Path();
		ArrayList<Integer> nodeList = new ArrayList<Integer>();
		for (Integer n : pathA.getNodes()){
			nodeList.add(n);
		}
		pathC.setNodes(nodeList);
		
		if (pathC.getNodes().get(0).equals(pathC.getNodes().get(pathC.getNodes().size()-1)) && pathB.getNodes().get(0).equals(pathB.getNodes().get(pathB.getNodes().size()-1))){
			pathC.getNodes().remove(pathC.getNodes().size()-1);
			pathC.getNodes().remove(0);
		}
		
		boolean intersect = false;
		for (Integer node : pathC.getNodes()){
			if(pathB.getNodes().contains(node)){
				intersect = true;
				break;
			}
		}
		return intersect;
	}
}
