/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.preferences;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.uzh.business.systemanalyse.tool.Activator;
import ch.uzh.business.systemanalyse.tool.IProjectSettingsConstants;
import ch.uzh.business.systemanalyse.tool.model.ModelProvider;

/**
 * A preference page for color of the application
 * (bubble color, table highlight color).
 * 
 * @author Krzysztof Dabkowski
 *
 */
public class ColorsPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	private String[] colorFieldEditorsKeys = {"TABLE_ROW_HIGHLIGHT_LIGHT", "TABLE_ROW_HIGHLIGHT_DARK", "CHART_BUBBLE"};
	private String[] colorFieldEditorsLabels = {"Table highlight light", "Table highlight dark", "Chart bubble"};
	private Map<String, ColorFieldEditor> colorEditorMap = new HashMap<String, ColorFieldEditor>();
	/**
	 * 
	 */
	public ColorsPreferencePage() {
		super(GRID);
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Color settings");
	}
	
	private void createColorFieldEditors() {
		ColorFieldEditor colorFieldEditor = null;

        for (int i = 0; i < colorFieldEditorsKeys.length; i++) {
            colorFieldEditor = new ColorFieldEditor(colorFieldEditorsKeys[i], colorFieldEditorsLabels[i], getFieldEditorParent());
            colorFieldEditor.setPreferenceStore(this.getPreferenceStore());
            colorFieldEditor.load();
            addField(colorFieldEditor);
            colorEditorMap.put(colorFieldEditorsKeys[i], colorFieldEditor);
        }
	}
	
	@Override
	public void performDefaults() {
		colorEditorMap.get("TABLE_ROW_HIGHLIGHT_LIGHT").getColorSelector().setColorValue(IProjectSettingsConstants.DEFAULT_TABLE_ROW_HIGHLIGHT_LIGHT);
		colorEditorMap.get("TABLE_ROW_HIGHLIGHT_DARK").getColorSelector().setColorValue(IProjectSettingsConstants.DEFAULT_TABLE_ROW_HIGHLIGHT_DARK);
		colorEditorMap.get("CHART_BUBBLE").getColorSelector().setColorValue(IProjectSettingsConstants.DEFAULT_CHART_BUBBLE);
	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		Iterator<ColorFieldEditor> iterator = colorEditorMap.values().iterator();
		for(; iterator.hasNext();) {
			iterator.next().store();
		}
		ModelProvider.INSTANCE.modelChanged(false);
		return super.performOk();
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	@Override
	protected void createFieldEditors() {
		createColorFieldEditors();
		performDefaults();
		
	}

}
