/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.preferences;

import ch.uzh.business.systemanalyse.tool.IProjectSettingsConstants;


/**
 * Preference page for defining allowed values for
 * Delay.
 * 
 * @author Krzysztof Dabkowski
 *
 */
public class DelayValuesPreferencePage extends AbstractValuesPreferencePage {

	@Override
	protected String getPreferenceName() {
		return "DELAY_VALUES";
	}

	@Override
	protected String[] getDefaults() {
		String [] values = new String [IProjectSettingsConstants.DEFAULT_DELAY_VALUES.length];
		for(int i = 0 ; i < IProjectSettingsConstants.DEFAULT_DELAY_VALUES.length; i++){
			values[i] = String.format("%.0f", IProjectSettingsConstants.DEFAULT_DELAY_VALUES[i]);
		}
		return values;
	}

	
}
