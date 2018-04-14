/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.preferences;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.uzh.business.systemanalyse.tool.Activator;

/**
 * A preference page for setting chart specific attributes
 * (chart label fonts).
 * 
 * @author Krzysztof Dabkowski
 *
 */
public class ChartPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	/**
	 * 
	 */
	

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Chart visual settings");
	}

	
	@Override
	protected void createFieldEditors() {
		
		RadioGroupFieldEditor styleRadio = new RadioGroupFieldEditor("TICK_FONT_STYLE",
				"Style for the tick font", 1,
				new String[][]{{"&Plain", "plain"},
				{"&Bold", "bold"},
				{"&Italic", "italic"}}, getFieldEditorParent());
		
		addField(styleRadio);

		ComboFieldEditor sizeCombo = new ComboFieldEditor("TICK_FONT_SIZE",
				"Size for the tick font",
				new String[][]{{"4", "4"},
				{"5", "5"},
				{"6", "6"},
				{"7", "7"},
				{"8", "8"},
				{"10", "10"},
				{"12", "12"},
				{"14", "14"},
				{"16", "16"},
				{"18", "18"},
				{"20", "20"},
				{"22", "22"},
				{"24", "24"},
				{"26", "26"},
				{"28", "28"},
			},
			getFieldEditorParent());
		addField(sizeCombo);
		
		RadioGroupFieldEditor styleLabelRadio = new RadioGroupFieldEditor("LABEL_STYLE",
				"Style for the chart label font", 1,
				new String[][]{{"&Plain", "plain"},
				{"&Bold", "bold"},
				{"&Italic", "italic"}}, getFieldEditorParent());
		
		addField(styleLabelRadio);

		ComboFieldEditor sizeLabelCombo = new ComboFieldEditor("LABEL_FONT_SIZE",
				"Size for the chart label font",
				new String[][]{{"4", "4"},
				{"5", "5"},
				{"6", "6"},
				{"7", "7"},
				{"8", "8"},
				{"10", "10"},
				{"12", "12"},
				{"14", "14"},
				{"16", "16"},
				{"18", "18"},
				{"20", "20"},
				{"22", "22"},
				{"24", "24"},
				{"26", "26"},
				{"28", "28"},
			},
			getFieldEditorParent());
		addField(sizeLabelCombo);
	}

	

}
