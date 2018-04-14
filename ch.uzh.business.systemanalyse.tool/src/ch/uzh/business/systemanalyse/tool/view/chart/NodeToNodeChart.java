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
public class NodeToNodeChart extends AbstractChartView {

	public static final String ID = "ch.uzh.business.systemanalyse.tool.views.nodetonodechart";
	
	
	/**
	 * 
	 */
	public NodeToNodeChart() {
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.chart.AbstractChartView#setDataSources()
	 */
	@Override
	protected void setDataSources() {
		
		yVal = new double[ModelProvider.INSTANCE.getFromToPaths().size()];
		xVal = new double[ModelProvider.INSTANCE.getFromToPaths().size()];
		int count = 0;
		for(Path path : ModelProvider.INSTANCE.getFromToPaths()) {
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
		return "All paths from node A to node B - Chart";
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
		return String.valueOf(ModelProvider.INSTANCE.getFromToPaths().get(index).getIndex());
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
}
