package ch.uzh.business.systemanalyse.tool.navigation;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import ch.uzh.business.systemanalyse.tool.ConsoleView;
import ch.uzh.business.systemanalyse.tool.view.archetypes.ArchetypeOutOfControlView;
import ch.uzh.business.systemanalyse.tool.view.archetypes.ArchetypeRelativeAchievementView;
import ch.uzh.business.systemanalyse.tool.view.archetypes.ArchetypeRelativeControlView;
import ch.uzh.business.systemanalyse.tool.view.archetypes.ArchetypeUnderachievementView;
import ch.uzh.business.systemanalyse.tool.view.archetypes.CausalLoopDiagramView;
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

public class TreeOverView extends ViewPart {
	protected TreeViewer treeViewer;
	protected TreeViewLabelProvider labelProvider;

	protected Action openView;
	protected ViewerFilter onlyBoardGamesFilter, atLeastThreeFilter;
	protected ViewerSorter booksBoxesGamesSorter, noArticleSorter;

	protected Categories root;
	public final static String ID = "ch.uzh.business.systemanalyse.tool.navigation.treeOverView";

	public TreeOverView() {

	}

	@Override
	public void createPartControl(Composite parent) {
		/*
		 * Create a grid layout object so the text and treeviewer are layed out
		 * the way I want.
		 */
		GridLayout layout = new GridLayout();
		parent.setLayout(layout);

		// Create the tree viewer as a child of the composite parent
		treeViewer = new TreeViewer(parent, SWT.SINGLE);
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		treeViewer.setContentProvider(new TreeViewContentProvider());
		labelProvider = new TreeViewLabelProvider();
		treeViewer.setLabelProvider(new TreeViewLabelProvider());

		treeViewer.setUseHashlookup(true);

		createActions();
		hookListeners();

		treeViewer.setInput(getInitalInput());
		treeViewer.expandAll();

	}

	protected void hookListeners() {
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				try {
					if (!event.getSelection().isEmpty()) {
						IStructuredSelection selection = (IStructuredSelection) event
								.getSelection();
						for (Iterator<?> iterator = selection.iterator(); iterator
								.hasNext();) {
							Object domain = (Model) iterator.next();
							if (domain instanceof ViewItem) {
								PlatformUI.getWorkbench()
										.getActiveWorkbenchWindow()
										.getActivePage()
										.showView(labelProvider.getID(domain));
							} else {
								if (!treeViewer.getExpandedState(domain)) {
									treeViewer.expandToLevel(domain, 1);
								} else {
									treeViewer.collapseToLevel(domain, 1);
								}
							}
						}
					}
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void createActions() {
		openView = new Action("Open View") {
			@Override
			public void run() {

			}
		};
		openView.setChecked(false);

	}

	public Categories getInitalInput() {
		root = new Categories();
		Categories matrices = new Categories("Matrices");
		Categories charts = new Categories("Charts");
		Categories analysis = new Categories("Analysis");
		Categories influence = new Categories("Influence");
		Categories paths = new Categories("Paths");
		Categories archetypes = new Categories("System Archetypes");
		Categories admin = new Categories("Admin");

		root.add(matrices);
		root.add(charts);
		root.add(analysis);
		root.add(admin);

		analysis.add(influence);
		analysis.add(paths);
		analysis.add(archetypes);

		matrices.add(new ViewItem(ImpactTableView.ID, "Cross-Impact Matrix"));
		matrices.add(new ViewItem(DelayTableView.ID, "Cross-Time Matrix"));
		matrices.add(new ViewItem(CrossDelayTableView.ID, "Cross-Delay Matrix"));
		matrices.add(new ViewItem(CrossEffectTableView.ID,
				"Cross-Effect Matrix"));

		charts.add(new ViewItem(AsPsChartView.ID, "AS-PS Chart"));
		charts.add(new ViewItem(PdRdChartView.ID, "PD-RD Chart"));
		charts.add(new ViewItem(AsPdChartView.ID, "AS-PD Chart"));
		charts.add(new ViewItem(PsRdChartView.ID, "PS-RD Chart"));

		influence.add(new ViewItem(IncomingInfluenceTableView.ID,
				"Incoming Influence"));
		influence.add(new ViewItem(IncomingInfluenceChart.ID,
				"Incoming Influence Chart"));
		influence.add(new ViewItem(OutgoingInfluenceTableView.ID,
				"Outgoing Influence"));
		influence.add(new ViewItem(OutgoingInfluenceChart.ID,
				"Outgoing Influence Chart"));

		paths.add(new ViewItem(NodeToNodeTable.ID, "Paths A to B"));
		paths.add(new ViewItem(NodeToNodeChart.ID, "Paths A to B Chart"));
		paths.add(new ViewItem(ToNodeTable.ID, "Paths to A"));
		paths.add(new ViewItem(ToNodeChart.ID, "Paths to A Chart"));
		paths.add(new ViewItem(FromNodeTable.ID, "Paths from A"));
		paths.add(new ViewItem(FromNodeChart.ID, "Paths from A Chart"));

		analysis.add(new ViewItem(PolicyOnOffView.ID, "Policy ON/OFF"));

		archetypes.add(new ViewItem(CausalLoopDiagramView.ID,
				"Causal Loop Diagram"));
		archetypes.add(new ViewItem(ArchetypeUnderachievementView.ID,
				"Underachievement"));
		archetypes.add(new ViewItem(ArchetypeOutOfControlView.ID,
				"Out of Control"));
		archetypes.add(new ViewItem(ArchetypeRelativeAchievementView.ID,
				"Relative Achievement"));
		archetypes.add(new ViewItem(ArchetypeRelativeControlView.ID,
				"Relative Control"));

		admin.add(new ViewItem(ConsoleView.ID, "Console"));

		return root;

	}

	@Override
	public void setFocus() {

	}

}
