/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.paths;

import java.util.List;

import ch.uzh.business.systemanalyse.tool.model.Path;

/**
 * @author krzysztof.dabkowski
 *
 */
public class PathsModelProvider {

	private List<Path> rows;
	
	public PathsModelProvider(List<Path> paths) {
		this.rows = paths;
	}
	
	public List<Path> getRows() {
		return rows;
	}
} 
