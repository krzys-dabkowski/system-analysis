/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.policyonoff;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.business.systemanalyse.tool.model.Path;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class PathsDialogTableModelProvider {

private List<Path> rows;
	
	public PathsDialogTableModelProvider(ArrayList<Path> paths) {
		rows = paths;
	}
	
	public List<Path> getRows() {
		return rows;
	}
}
