/**
 * 
 */
package obsolete;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.IParameterValues;

import ch.uzh.business.systemanalyse.tool.IProjectSettingsConstants;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class CreateModelParameterValues implements IParameterValues {

	
	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IParameterValues#getParameterValues()
	 */
	@Override
	public Map<Integer, Integer> getParameterValues() {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for(int i = 1; i <= IProjectSettingsConstants.MAX_VALUE_NEW_MODEL; i++) {
			map.put(i, i);
		}
		return map;
	}

}
