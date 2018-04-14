/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.uzh.business.systemanalyse.tool.Activator;

/**
 * A preference page for defining a location for saving pictures.
 * 
 * @author Krzysztof Dabkowski
 * 
 */
public class SavePreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	@Override
	protected void createFieldEditors() {
		addField(new DirectoryFieldEditor("SAVE_PATH", "&Directory",
				getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Select directory for saving the print outs");
	}

}
