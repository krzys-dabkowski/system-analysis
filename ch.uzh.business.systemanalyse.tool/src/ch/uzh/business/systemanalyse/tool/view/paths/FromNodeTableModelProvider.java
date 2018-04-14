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
public class FromNodeTableModelProvider {

private List<Path> rows;
	
	private FromNodeTableModelProvider() {
		rows = ModelProvider.INSTANCE.getFromPaths();
	}
	
	public List<Path> getRows() {
		return rows;
	}
}
