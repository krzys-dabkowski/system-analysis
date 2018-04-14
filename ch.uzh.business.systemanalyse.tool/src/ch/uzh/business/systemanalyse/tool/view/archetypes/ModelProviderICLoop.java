/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.archetypes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import ch.uzh.business.systemanalyse.tool.model.Path;
import ch.uzh.business.systemanalyse.tool.systempaths.PathsFinder;

/**
 * @author Alexander Schmid
 * 
 */
public class ModelProviderICLoop {

	private List<Path> rows;
	
	public ModelProviderICLoop(int keyVariable, boolean icReinforcing, boolean ucReinforcing) {
		PathsFinder pathsFinder = new PathsFinder();
		ArrayList<Path> allPaths = pathsFinder.findPaths(keyVariable);
		ArrayList<Path> allICLoops = new ArrayList<Path>();
		ArrayList<Path> icLoops = new ArrayList<Path>();
		ArrayList<Path> ucLoops = new ArrayList<Path>();
		
		for(Path path : allPaths){
			if (path.getNodes().get(0).equals(keyVariable) && (path.getEffect() > 0) == icReinforcing){
				allICLoops.add(path);
			}
			if (path.getNodes().get(0).equals(keyVariable) && (path.getEffect() > 0) == ucReinforcing){
				ucLoops.add(path);
			}
		}
		
		for (Path path : allICLoops) {			
			boolean intersectionWithAllUCLoop = true;
			for (Path ucLoop : ucLoops){
				if (!PathsFinder.haveIntersection(path, ucLoop)){
					intersectionWithAllUCLoop = false;
					break;
				}
			}
			if (!intersectionWithAllUCLoop){	
				icLoops.add(path);
			}
		}

		this.rows = this.sort(icLoops);
		this.checkNull();
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

	private void checkNull() {
		if (this.rows.size() == 0) {
//			Path p = new Path();
//			p.setDelay(0);
//			p.setEffect(0);
//			p.setIndex(0);
//			ArrayList<Integer> n = new ArrayList<Integer>();
//			n.add(0);
//			p.setNodes(n);
//			List<Path> pl = new ArrayList<Path>();
//			pl.add(p);
//			this.rows = pl;
			Shell lShell = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell();
			MessageBox dialog = new MessageBox(lShell, SWT.ICON_INFORMATION
					| SWT.OK);
			dialog.setText("No loops found");
			dialog.setMessage("No intended consequence loops found for this varibale.\n\n Press OK to continue.");
			dialog.open();
		}
	}
}
