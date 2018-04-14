/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.preferences;

import ch.uzh.business.systemanalyse.tool.IProjectSettingsConstants;


/**
 * A preference page for defining allowed values
 * for Impact.
 * 
 * @author Krzysztof Dabkowski
 *
 */
public class ImpactValuesPreferencePage extends AbstractValuesPreferencePage {

	@Override
	protected String getPreferenceName() {
		return "IMPACT_VALUES";
	}

	@Override
	protected String[] getDefaults() {
		String [] values = new String [IProjectSettingsConstants.DEFAULT_IMPACT_VALUES.length];
		for(int i = 0 ; i < IProjectSettingsConstants.DEFAULT_IMPACT_VALUES.length; i++){
			values[i] = String.format("%.2f", IProjectSettingsConstants.DEFAULT_IMPACT_VALUES[i]);
		}
		return values;
	}

	

}
