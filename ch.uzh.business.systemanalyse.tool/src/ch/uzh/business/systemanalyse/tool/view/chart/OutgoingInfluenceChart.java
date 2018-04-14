/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.chart;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.jfree.chart.axis.ValueAxis;

import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.VariableValue;
import ch.uzh.business.systemanalyse.tool.view.table.AbstractInfluenceTableView;
import ch.uzh.business.systemanalyse.tool.view.table.OutgoingInfluenceTableView;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class OutgoingInfluenceChart extends AbstractChartView {

	public final static String ID = "ch.uzh.business.systemanalyse.tool.views.outgoinginfluencechart";

	private int analyzedVariableIndex;
	/**
	 * 
	 */
	public OutgoingInfluenceChart() {
		
	}

	private void setVariable() {
		IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(OutgoingInfluenceTableView.ID);
		analyzedVariableIndex = ((AbstractInfluenceTableView)part).analyzedVariableIndex;
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.chart.AbstractChartView#getElementAnnotation(int)
	 */
	@Override
	protected String getElementAnnotation(int index) {

		return String.valueOf(index + 1);
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.chart.AbstractChartView#setDataSources()
	 */
	@Override
	protected void setDataSources() {
		setVariable();
		if(ModelProvider.INSTANCE.getVariable(analyzedVariableIndex) == null) {
			yVal = null;
			xVal = null;
			zVal = null;
		} else {
		yVal = new double[ModelProvider.INSTANCE.getVariable(analyzedVariableIndex).getShortestPathsArray().size()];
		xVal = new double[ModelProvider.INSTANCE.getVariable(analyzedVariableIndex).getStrongestEffectsArray().size()];
		int count = 0;
		/*for(Path path : ModelProvider.INSTANCE.getFromToPaths()) {
			yVal[count] = path.getEffect();
			xVal[count] = path.getDelay();
			count++;
		}*/
		for(VariableValue val : ModelProvider.INSTANCE.getVariable(analyzedVariableIndex).getShortestPathsArray()) {
			if(Float.isInfinite(val.getVariableValue())) {
				yVal[count] = Double.NaN;
			} else {
				yVal[count] = val.getVariableValue();
			}
			count++;
		}
		count = 0;
		for(VariableValue val : ModelProvider.INSTANCE.getVariable(analyzedVariableIndex).getStrongestEffectsArray()) {
			if(Float.isInfinite(val.getVariableValue())) {
				xVal[count] = Double.NaN;
			} else {
				xVal[count] = val.getVariableValue();
			}
			count++;

		}
		
		setZValues();
		//zVal[7] = 0.000001;
		}
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
		return "Outgoing Influence Chart";
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
