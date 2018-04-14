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
public class PdRdChartView extends AbstractChartView {

	public static final String ID = "ch.uzh.business.systemanalyse.tool.views.pdrdchartview";
	
	

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.views.AbstractChartView#setDataSources()
	 */
	@Override
	protected void setDataSources() {
		xVal = ModelProvider.INSTANCE.getProducedDelays();
		yVal = ModelProvider.INSTANCE.getReceivedDelays();
		//zVal = new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1};
		setZValues();
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.views.AbstractChartView#getBackgroundAnnotationText(int)
	 */
	@Override
	protected String getBackgroundAnnotationText(int i) {
		return IProjectSettingsConstants.PD_RD_CHART_BCKGRND_ANN[i];
	}

	@Override
	protected String getChartTitle() {
		return "PD - RD Chart";
	}

	@Override
	protected String getYAxisLabel() {
		return "Received Delay";
	}

	@Override
	protected String getXAxisLabel() {
		return "Produced Delay";
	}

	@Override
	protected String getElementAnnotation(int index) {
		return String.valueOf(ModelProvider.INSTANCE.getVariables().get(index).getIndex());
	}

	@Override
	protected void setAxesInversion(ValueAxis xAxis, ValueAxis yAxis) {
		
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
