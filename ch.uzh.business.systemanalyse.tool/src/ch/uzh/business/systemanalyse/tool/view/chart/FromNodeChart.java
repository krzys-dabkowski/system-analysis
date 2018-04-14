/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.chart;

import org.jfree.chart.axis.ValueAxis;

import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.Path;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class FromNodeChart extends AbstractChartView {

	public static final String ID = "ch.uzh.business.systemanalyse.tool.views.fromnodechart";
	
	
	/**
	 * 
	 */
	public FromNodeChart() {
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.chart.AbstractChartView#setDataSources()
	 */
	@Override
	protected void setDataSources() {
		yVal = new double[ModelProvider.INSTANCE.getFromPaths().size()];
		xVal = new double[ModelProvider.INSTANCE.getFromPaths().size()];
		int count = 0;
		for(Path path : ModelProvider.INSTANCE.getFromPaths()) {
			xVal[count] = path.getEffect();
			yVal[count] = path.getDelay();
			count++;
		}
		setZValues();
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.chart.AbstractChartView#getBackgroundAnnotationText(int)
	 */
	@Override
	protected String getBackgroundAnnotationText(int i) {
		return "";
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.chart.AbstractChartView#getChartTitle()
	 */
	@Override
	protected String getChartTitle() {
		return "All paths from node A - Chart";
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.chart.AbstractChartView#getXAxisLabel()
	 */
	@Override
	protected String getXAxisLabel() {
		return "Effect";
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.chart.AbstractChartView#getYAxisLabel()
	 */
	@Override
	protected String getYAxisLabel() {
		return "Delay";
	}

	@Override
	protected String getElementAnnotation(int index) {
		return String.valueOf(ModelProvider.INSTANCE.getFromPaths().get(index).getIndex());
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
		return false;
	}
	
	@Override
	protected boolean makeYZeroMiddle() {
		return true;
	}

	/*@Override
	protected String getPartNameProperty() {
		return "TO_NODE_CHART";
	}*/
	
}
