package ch.uzh.business.systemanalyse.tool;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 * 
 * The activators IPreferenceStore is used to store preferences for the whole
 * application.
 * 
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "ch.uzh.business.systemanalyse.tool"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		IPreferenceStore store = getPreferenceStore();
		if (!(store.contains("TICK_FONT_SIZE") && store
				.contains("LABEL_FONT_SIZE"))) {
			// If no preferences have been set yet, store the default ones.
			// It should happen when the application is started for the first
			// time
			// or the eclipse preferences file has been deleted.
			restoreDefaultPreferences(store);
			String filePath = Activator.getDefault().getPreferenceStore()
					.getString("LICENCE_FILE");
			if (filePath == null || filePath.equals("")) {
				MessageDialog
						.openInformation(Display.getDefault().getActiveShell(),
								"Licence file missing",
								"Please specify a valid licence file in the Preferences.");
			}
		}
	}

	/**
	 * @param store
	 * 
	 *            This method sets the default preferences in the preference
	 *            store
	 */
	private void restoreDefaultPreferences(IPreferenceStore store) {
		store.setDefault("TICK_FONT_SIZE", 16);
		store.setValue("LABEL_FONT_SIZE", 16);
		store.setValue("TICK_FONT_STYLE", "plain");
		store.setValue("LABEL_FONT_STYLE", "plain");
		store.setValue("DELAY_VALUES",
				convert(IProjectSettingsConstants.DEFAULT_DELAY_VALUES));
		store.setValue("IMPACT_VALUES",
				convert(IProjectSettingsConstants.DEFAULT_IMPACT_VALUES));
		PreferenceConverter.setValue(store, "TABLE_ROW_HIGHLIGHT_DARK",
				IProjectSettingsConstants.DEFAULT_TABLE_ROW_HIGHLIGHT_DARK);
		PreferenceConverter.setValue(store, "TABLE_ROW_HIGHLIGHT_LIGHT",
				IProjectSettingsConstants.DEFAULT_TABLE_ROW_HIGHLIGHT_LIGHT);
		PreferenceConverter.setValue(store, "CHART_BUBBLE",
				IProjectSettingsConstants.DEFAULT_CHART_BUBBLE);
	}

	private String convert(float[] list) {
		String string = null;
		if (list.length > 0) {
			string = "";
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < list.length; i++) {
				if (i == 0) {
					builder.append(string).append(list[i]);
				} else {
					builder.append(string).append("v").append(list[i]);
				}
			}
			string = builder.toString();
		}
		return string;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static ImageDescriptor getIcon(String name) {
		if (IProjectSettingsConstants.OS.contains("Win")) {
			return Activator
					.getImageDescriptor(IProjectSettingsConstants.ICONS_BASE_FOLDER
							+ name + ".ico");
		} else {
			return Activator
					.getImageDescriptor(IProjectSettingsConstants.ICONS_BASE_FOLDER
							+ IProjectSettingsConstants.MAC_ICON_FOLDER
							+ name
							+ ".icns");
		}
	}

}
