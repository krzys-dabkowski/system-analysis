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
import ch.uzh.business.systemanalyse.tool.view.table.IncomingInfluenceTableView;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class IncomingInfluenceChart extends AbstractChartView {

	public final static String ID = "ch.uzh.business.systemanalyse.tool.views.incominginfluencechart";

	private int analyzedVariableIndex;
	/**
	 * 
	 */
	public IncomingInfluenceChart() {
		
	}
	
	private void setVariable() {
		IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(IncomingInfluenceTableView.ID);
		analyzedVariableIndex = ((AbstractInfluenceTableView)part).analyzedVariableIndex;
	}

	

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.chart.AbstractChartView#setDataSources()
	 */
	@Override
	protected void setDataSources() {
		setVariable();
		if(analyzedVariableIndex != 0) {
			xVal = new double[ModelProvider.INSTANCE.getShortestPathsTo(ModelProvider.INSTANCE.getVariable(analyzedVariableIndex)).size()];
			yVal = new double[ModelProvider.INSTANCE.getStrongestEffectTo(ModelProvider.INSTANCE.getVariable(analyzedVariableIndex)).size()];
			int count = 0;

			for(VariableValue val : ModelProvider.INSTANCE.getShortestPathsTo(ModelProvider.INSTANCE.getVariable(analyzedVariableIndex))) {
				if(Float.isInfinite(val.getVariableValue())) {
					yVal[count] = Double.NaN;
				} else {
					yVal[count] = val.getVariableValue();
				}
				count++;
			}
			count = 0;
			for(VariableValue val : ModelProvider.INSTANCE.getStrongestEffectTo(ModelProvider.INSTANCE.getVariable(analyzedVariableIndex))) {
				if(Float.isInfinite(val.getVariableValue())) {
					yVal[count] = Double.NaN;
				} else {
					xVal[count] = val.getVariableValue();
				}
				count++;
			}
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
		return "Incoming Influence Chart";
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
		//return String.valueOf(ModelProvider.INSTANCE.getStrongestEffectTo(ModelProvider.INSTANCE.getVariable(analyzedVariableIndex)).get(index).getVariableIndex());
		return String.valueOf(index + 1);
		
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
