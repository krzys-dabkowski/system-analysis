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
public class PsRdChartView extends AbstractChartView {

	public static final String ID = "ch.uzh.business.systemanalyse.tool.views.psrdchartview";

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.chart.AbstractChartView#setDataSources()
	 */
	@Override
	protected void setDataSources() {
		yVal = ModelProvider.INSTANCE.getReceivedDelays();
		xVal = ModelProvider.INSTANCE.getPassiveSums();
		//zVal = new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1};
		setZValues();
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.chart.AbstractChartView#getBackgroundAnnotationText(int)
	 */
	@Override
	protected String getBackgroundAnnotationText(int i) {
		return IProjectSettingsConstants.PS_RD_CHART_BCKGRND_ANN[i];
	}

	@Override
	protected String getChartTitle() {
		return "PS - RD Chart";
	}

	@Override
	protected String getYAxisLabel() {
		return "Received Delay";
	}

	@Override
	protected String getXAxisLabel() {
		return "Passive Sum";
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
