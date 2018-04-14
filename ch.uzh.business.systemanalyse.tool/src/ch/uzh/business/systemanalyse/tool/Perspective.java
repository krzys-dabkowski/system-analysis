package ch.uzh.business.systemanalyse.tool;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.view.chart.AsPdChartView;
import ch.uzh.business.systemanalyse.tool.view.chart.AsPsChartView;
import ch.uzh.business.systemanalyse.tool.view.chart.FromNodeChart;
import ch.uzh.business.systemanalyse.tool.view.chart.IncomingInfluenceChart;
import ch.uzh.business.systemanalyse.tool.view.chart.NodeToNodeChart;
import ch.uzh.business.systemanalyse.tool.view.chart.OutgoingInfluenceChart;
import ch.uzh.business.systemanalyse.tool.view.chart.PdRdChartView;
import ch.uzh.business.systemanalyse.tool.view.chart.PsRdChartView;
import ch.uzh.business.systemanalyse.tool.view.chart.ToNodeChart;
import ch.uzh.business.systemanalyse.tool.view.paths.FromNodeTable;
import ch.uzh.business.systemanalyse.tool.view.paths.NodeToNodeTable;
import ch.uzh.business.systemanalyse.tool.view.paths.ToNodeTable;
import ch.uzh.business.systemanalyse.tool.view.policyonoff.PolicyOnOffView;
import ch.uzh.business.systemanalyse.tool.view.table.CrossDelayTableView;
import ch.uzh.business.systemanalyse.tool.view.table.CrossEffectTableView;
import ch.uzh.business.systemanalyse.tool.view.table.DelayTableView;
import ch.uzh.business.systemanalyse.tool.view.table.ImpactTableView;
import ch.uzh.business.systemanalyse.tool.view.table.IncomingInfluenceTableView;
import ch.uzh.business.systemanalyse.tool.view.table.OutgoingInfluenceTableView;
import ch.uzh.business.systemanalyse.tool.view.welcome.WelcomeView;

/**
 * A perspective generates the initial page layout. More concrete, it creates a
 * folder and adds all the views to it.
 * 
 * @author krzysztof.dabkowski
 * 
 */
public class Perspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		ModelProvider modelProvider = ModelProvider.INSTANCE;
		modelProvider.init();
		IFolderLayout folder = layout.createFolder("folder", IPageLayout.TOP,
				1.f, editorArea);
		folder.addView(WelcomeView.ID);
		folder.addView(ImpactTableView.ID);
		folder.addView(AsPsChartView.ID);
		folder.addView(DelayTableView.ID);
		folder.addView(PdRdChartView.ID);
		folder.addView(AsPdChartView.ID);
		folder.addView(PsRdChartView.ID);
		folder.addView(CrossDelayTableView.ID);
		folder.addView(CrossEffectTableView.ID);
		folder.addView(IncomingInfluenceTableView.ID);
		folder.addView(IncomingInfluenceChart.ID);
		folder.addView(OutgoingInfluenceTableView.ID);
		folder.addView(OutgoingInfluenceChart.ID);
		folder.addView(NodeToNodeTable.ID);
		folder.addView(NodeToNodeChart.ID);
		folder.addView(ToNodeTable.ID);
		folder.addView(ToNodeChart.ID);
		folder.addView(FromNodeTable.ID);
		folder.addView(FromNodeChart.ID);
		folder.addView(PolicyOnOffView.ID);
		// folder.addView(ConsoleView.ID);

	}

}
