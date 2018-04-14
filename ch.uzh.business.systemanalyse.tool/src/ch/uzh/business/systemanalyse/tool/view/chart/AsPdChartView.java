/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.chart;

import org.jfree.chart.axis.ValueAxis;

import ch.uzh.business.systemanalyse.tool.IProjectSettingsConstants;
import ch.uzh.business.systemanalyse.tool.model.ModelProvider;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class AsPdChartView extends AbstractChartView {

	public static final String ID = "ch.uzh.business.systemanalyse.tool.views.aspdchartview";

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.chart.AbstractChartView#setDataSources()
	 */
	@Override
	protected void setDataSources() {
		yVal = ModelProvider.INSTANCE.getProducedDelays();
		xVal = ModelProvider.INSTANCE.getActiveSums();
		//zVal = new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1};
		setZValues();
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.chart.AbstractChartView#getBackgroundAnnotationText(int)
	 */
	@Override
	protected String getBackgroundAnnotationText(int i) {
		return IProjectSettingsConstants.AS_PD_CHART_BCKGRND_ANN[i];
	}

	@Override
	protected String getChartTitle() {
		return "AS - PD Chart";
	}

	@Override
	protected String getYAxisLabel() {
		return "Produced Delay";
	}

	@Override
	protected String getXAxisLabel() {
		return "Active Sum";
	}

	@Override
	protected String getElementAnnotation(int index) {
		return String.valueOf(ModelProvider.INSTANCE.getVariables().get(index).getIndex());
	}

	@Override
	protected void setAxesInversion(ValueAxis xAxis, ValueAxis yAxis) {
		xAxis.setInverted(true);
	}

	@Override
	protected boolean xZeroInclude() {
		return true;
	}

	@Override
	protected boolean yZeroInclude() {
		return true;
	}

	@Override
	protected boolean makeYZeroMiddle() {
		return false;
	}

}
