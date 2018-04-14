/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.paths;

import java.util.List;

import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.Path;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class NodeToNodeTableModelProvider {

	private List<Path> rows;
	
	private NodeToNodeTableModelProvider() {
		rows = ModelProvider.INSTANCE.getFromToPaths();
	}
	
	public List<Path> getRows() {
		return rows;
	}
}
