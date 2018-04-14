/**
 * 
 */
package ch.uzh.business.systemanalyse.tool;

import org.eclipse.swt.graphics.RGB;

/**
 * @author Krzysztof Dabkowski
 * 
 */
public interface IProjectSettingsConstants {

	public static final int NUMBER_OF_UNEDITABLE_COLUMNS = 2;
	public static final float[] DEFAULT_IMPACT_VALUES = { -1.5f, -1f, -2f / 3f,
			0f, 2f / 3f, 1f, 1.5f };
	public static final int IMPACT_TABLE_COLUMN_WIDTH = 70;
	public static final float[] DEFAULT_DELAY_VALUES = { 0f, 1f, 2f, 5f, 10f };
	public static final String[] AS_PS_CHART_BCKGRND_ANN = { "buffering",
			"active", "critical", "reactive" };
	public static final String[] PD_RD_CHART_BCKGRND_ANN = { "lively",
			"braking", "sluggish", "accelerative" };
	// public static final String [] AS_PD_CHART_BCKGRND_ANN = {
	// "lively-buffering", "lively-active", "braking-active",
	// "braking-buffering"};
	// public static final String [] PS_RD_CHART_BCKGRND_ANN = {
	// "lively-buffering", "lively-reactive", "accelerating-reactive",
	// "accelerating-buffering",};
	public static final String[] AS_PD_CHART_BCKGRND_ANN = { "III", "II", "I",
			"IV" };
	public static final String[] PS_RD_CHART_BCKGRND_ANN = { "III", "II", "I",
			"IV" };

	public static final RGB DEFAULT_TABLE_ROW_HIGHLIGHT_LIGHT = new RGB(173,
			216, 230);
	public static final RGB DEFAULT_TABLE_ROW_HIGHLIGHT_DARK = new RGB(8, 91,
			186);
	public static final RGB DEFAULT_CHART_BUBBLE = new RGB(0, 51, 204);

	public static final int MAX_VALUE_NEW_MODEL = 30;

	public static final String OS = System.getProperty("os.name");

	public static final String MAC_ICON_FOLDER = "mac/";

	public static final String ICONS_BASE_FOLDER = "icons/";

	public static final String LICENCE_INVALID_TITLE = "Invalid licence";

	public static final String LICENCE_INVALID_TEXT = "No valid licence specified.";

}
