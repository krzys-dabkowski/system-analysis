/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.archetypes;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.business.systemanalyse.tool.model.Path;
import ch.uzh.business.systemanalyse.tool.systempaths.PathsFinder;

/**
 * @author Alexander Schmid
 * 
 */
public class ModelProviderUCLoopOoC {

	private List<Path> rows;
	
	public ModelProviderUCLoopOoC(int keyVariable, int controlVariable, boolean ucReinforcing, Path icLoop, boolean delayed) {
		PathsFinder pathsFinder = new PathsFinder();
		ArrayList<Path> allUCPaths = pathsFinder.findAllPaths(controlVariable, keyVariable);
		
		ArrayList<Integer> icNodesToControl = new ArrayList<Integer>();
		ArrayList<Integer> icNodesAfterControl = new ArrayList<Integer>();
		Path icPathToControl = new Path();
		Path icPathAfterControl = new Path();
		ArrayList<Path> ucPaths = new ArrayList<Path>();
		
		boolean end = false;
		for (Integer node : icLoop.getNodes()){
			if (!end){
				icNodesToControl.add(node);
				if (node.equals(controlVariable)) {
					icNodesAfterControl.add(node);
					end = true;
				}
			} else {icNodesAfterControl.add(node);}
		}
		
		float delayAfterControl = PathsFinder.calculateDelay(icNodesAfterControl);
		icNodesAfterControl.remove(icNodesAfterControl.size()-1);
		icNodesAfterControl.remove(0);
		icPathAfterControl.setNodes(icNodesAfterControl);
		icPathToControl.setEffect(PathsFinder.calculateEffect(icNodesToControl));
		icPathToControl.setDelay(PathsFinder.calculateDelay(icNodesToControl));
		icNodesToControl.remove(icNodesToControl.size()-1);
		if (!(icNodesToControl.size() == 0)) icNodesToControl.remove(0);
		icPathToControl.setNodes(icNodesToControl);
		
		for (Path path : allUCPaths){
			if ((((icPathToControl.getEffect() > 0) == (path.getEffect() > 0))
					&& !PathsFinder.haveIntersection(icPathAfterControl, path)
					&& !PathsFinder.haveIntersection(icPathToControl, path)
					&& path.getNodes().size() > 2)
					&& (!delayed | (delayed && (PathsFinder.calculateDelay(path.getNodes()) > delayAfterControl)))){
					ucPaths.add(path);
			}
		}

		this.rows = this.sort(ucPaths);
		this.reindex();
	}

	public List<Path> getRows() {
		return rows;
	}

	private void reindex() {
		for (int i = 0; i < this.rows.size(); i++) {
			this.rows.get(i).setIndex(i + 1);
		}
	}

	private List<Path> sort(List<Path> list) {

		int i, j = 0;
		Path t;
		for (i = 0; i < list.size(); i++) {
			for (j = 1; j < (list.size() - i); j++) {
				if (list.get(j - 1).getNodes().size() > list.get(j).getNodes()
						.size()) {
					t = list.get(j - 1);
					list.set(j - 1, list.get(j));
					list.set(j, t);
				}
			}
		}
		return list;
	}
}
