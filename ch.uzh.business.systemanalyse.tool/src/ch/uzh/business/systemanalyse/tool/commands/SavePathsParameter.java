/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.IParameterValues;

import ch.uzh.business.systemanalyse.tool.model.Path;

/**
 * Command parameter for passing paths from a popup
 * window (Policy ON/OFF show paths) to the command
 * handler.
 * 
 * @author krzysztof.dabkowski
 *
 */
public class SavePathsParameter implements IParameterValues {

	private Map<Integer, String> parametersMap = new HashMap<Integer, String>();

	public SavePathsParameter(List<Path> paths) {
		for(Path path : paths) {
			String nodes = "";
			for(Integer node : path.getNodes()) {
				nodes += String.valueOf(node) + "->";
			}
			nodes = nodes.substring(0, nodes.length() - 2);
			
			String pathString = String.valueOf(path.getNodes().size()) + "|" + 
					String.valueOf(path.getDelay()) + "|" + 
					String.valueOf(path.getEffect()) + "|" +
					nodes;
			
			parametersMap.put(path.getIndex(), pathString);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IParameterValues#getParameterValues()
	 */
	@Override
	public Map<Integer, String> getParameterValues() {
		return parametersMap;
	}

}
