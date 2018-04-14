package ch.uzh.business.systemanalyse.tool.view.chart;

import org.jfree.chart.axis.ValueAxis;

import ch.uzh.business.systemanalyse.tool.IProjectSettingsConstants;
import ch.uzh.business.systemanalyse.tool.model.ModelProvider;

public class AsPsChartView extends AbstractChartView {

	public static final String ID = "ch.uzh.business.systemanalyse.tool.views.aspschartview";
	
	@Override
	protected String getBackgroundAnnotationText(int i) {
		return IProjectSettingsConstants.AS_PS_CHART_BCKGRND_ANN[i];
	}

	@Override
	protected void setDataSources() {
		yVal = ModelProvider.INSTANCE.getPassiveSums();
		xVal = ModelProvider.INSTANCE.getActiveSums();
		//zVal = new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1};
		setZValues();
	}

	@Override
	protected String getChartTitle() {
		return "AS - PS Chart";
	}

	@Override
	protected String getYAxisLabel() {
		return "Passive Sum";
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
