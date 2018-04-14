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
public class ModelProviderUCLoop {

	private List<Path> rows;

	public ModelProviderUCLoop(int loopSource, boolean reinforcing) {
		PathsFinder pathsFinder = new PathsFinder();
		ArrayList<Path> paths = pathsFinder.findPaths(loopSource);

		for (int i = paths.size() - 1; i >= 0; i--) {
			if (!paths.get(i).getNodes().get(0).equals(loopSource)
					| (paths.get(i).getEffect() < 0) == reinforcing) {
				paths.remove(i);
			}
		}

		this.rows = this.sort(paths);
		this.reindex();
	}
	
	public ModelProviderUCLoop(int keyVariable, boolean ucReinforcing, Path icLoop, boolean delayed) {
		PathsFinder pathsFinder = new PathsFinder();
		ArrayList<Path> paths = pathsFinder.findPaths(keyVariable);

		for (int i = paths.size() - 1; i >= 0; i--) {
			if (!paths.get(i).getNodes().get(0).equals(keyVariable)
					| (paths.get(i).getEffect() < 0) == ucReinforcing) {
				paths.remove(i);
			} else {
				
				icLoop.setDelay(PathsFinder.calculateDelay(icLoop.getNodes()));

				if (((paths.get(i).getDelay() < icLoop.getDelay()+1) && delayed) | PathsFinder.haveIntersection(paths.get(i), icLoop)) paths.remove(i);
			}
		}

		this.rows = this.sort(paths);
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
