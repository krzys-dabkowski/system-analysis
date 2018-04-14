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
public class ToNodeTableModelProvider {

private List<Path> rows;
	
	private ToNodeTableModelProvider() {
		rows = ModelProvider.INSTANCE.getToPaths();
	}
	
	public List<Path> getRows() {
		return rows;
	}
}
