package ch.uzh.business.systemanalyse.tool.view.archetypes;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.internal.ZoomManager;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import ch.uzh.business.systemanalyse.tool.Activator;
import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.Variable;
import ch.uzh.business.systemanalyse.tool.model.VariableValue;
import ch.uzh.business.systemanalyse.tool.view.table.AbstractTitledView;

public class CausalLoopDiagramView extends AbstractTitledView implements
		IZoomableWorkbenchPart {

	public static final String ID = "ch.uzh.business.systemanalyse.tool.views.causalloopdiagram";

	private GraphViewer viewer;
	private Graph graph;
	private Composite composite;
	private int layout = 1;
	private HashMap<Variable, GraphNode> variablesAndNodes;
	private ArrayList<Variable> variables;
	private ArrayList<Object> undoList, redoList;
	protected ZoomManager zoomManager;

	public CausalLoopDiagramView() {
		super();
		this.initialize();
	}

	@Override
	public void createPartControl(Composite parent) {
		this.composite = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout(1, true);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		this.createTitle(composite, 1);
		viewer = new GraphViewer(composite, SWT.NONE);
		viewer.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));

		this.initializeGraph(composite);
	}

	@SuppressWarnings("restriction")
	private void initializeGraph(Composite parent) {
		// Graph will hold all other objects
		graph = viewer.getGraphControl();
		this.initializeNodes();

		for (Variable variable : variables) {
			for (VariableValue impact : variable.getImpactArray()) {
				if (impact.getVariableValue() != 0) {
					GraphConnection gC = new GraphConnection(graph,
							ZestStyles.CONNECTIONS_DIRECTED,
							variablesAndNodes.get(variable),
							variablesAndNodes.get(variables.get(impact
									.getVariableIndex() - 1)));

					// gC.setText(impact.getVariableValue() + " (" +
					// variable.getDelayArray() .get(impact.getVariableIndex() -
					// 1).getVariableValue() + ")");
					gC.setTooltip(new org.eclipse.draw2d.Label("Effect: "
							+ variable.getImpactValueToVariable(variables.get(
									impact.getVariableIndex() - 1).getIndex())
							+ "/Delay: "
							+ variable.getDelayValueToVariable(variables.get(
									impact.getVariableIndex() - 1).getIndex())));
					gC.setHighlightColor(parent.getDisplay().getSystemColor(
							SWT.COLOR_GREEN));

					if (impact.getVariableValue() > 0) {
						gC.setLineColor(org.eclipse.draw2d.ColorConstants.blue);
					} else {
						gC.setLineColor(org.eclipse.draw2d.ColorConstants.red);
					}

					// if(variables.get(impact.getVariableIndex() -
					// 1).getImpactArray().get(variable.getIndex() -
					// 1).getVariableValue() != 0){
					gC.setCurveDepth(10);
					// }

					variablesAndNodes.get(variable).setBorderWidth(0);
					variablesAndNodes.get(variable).setBackgroundColor(
							org.eclipse.draw2d.ColorConstants.white);
					variablesAndNodes.get(variable).setForegroundColor(
							org.eclipse.draw2d.ColorConstants.black);
				}
			}
		}

		graph.setLayoutAlgorithm(new SpringLayoutAlgorithm(
				LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);

		graph.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println(e);
			}

		});

		// graph.addKeyListener(new DelKeyListener());

		zoomManager = new ZoomManager(graph.getRootLayer(), graph.getViewport());
		zoomManager.setZoomAsText("90%");
		this.createToolbar();
	}

	public void setLayoutManager() {
		switch (layout) {
		case 1:
			graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(
					LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
			layout++;
			break;
		case 2:
			graph.setLayoutAlgorithm(new SpringLayoutAlgorithm(
					LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
			layout = 1;
			break;
		}

	}

	private void initialize() {
		undoList = new ArrayList<Object>();
		redoList = new ArrayList<Object>();
		variablesAndNodes = new HashMap<Variable, GraphNode>();
		variables = (ArrayList<Variable>) ModelProvider.INSTANCE.getVariables();
	}

	private void initializeNodes() {
		for (Variable variable : variables) {
			GraphNode gN = new GraphNode(graph, SWT.NONE);
			String label = variable.getName() + " (" + variable.getIndex()
					+ ")";
			gN.setText(TextBreaker.brokenLines(label, 30));
			if (label.length() >= 60) {
				gN.setSize(170, -1);
			} else {
				gN.setSize(170, 40);
			}
			gN.setNodeStyle(ZestStyles.NONE);
			variablesAndNodes.put(variable, gN);
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */

	@Override
	public void setFocus() {
	}

	private void createToolbar() {

		Action saveImage = new Action("Save image") {
			@Override
			public void run() {
				org.eclipse.draw2d.geometry.Rectangle bounds = graph
						.getContents().getBounds();
				Point size = new Point(graph.getContents().getSize().width,
						graph.getContents().getSize().height);
				org.eclipse.draw2d.geometry.Point viewLocation = graph
						.getViewport().getViewLocation();
				final Image image = new Image(null, size.x, size.y);
				GC gc = new GC(image);
				SWTGraphics swtGraphics = new SWTGraphics(gc);
				swtGraphics.translate(-1 * bounds.x + viewLocation.x, -1
						* bounds.y + viewLocation.y);
				graph.getViewport().paint(swtGraphics);
				try {
					gc.copyArea(image, 0, 0);
					ImageLoader imageLoader = new ImageLoader();
					imageLoader.data = new ImageData[] { image.getImageData() };
					String path = Activator.getDefault().getPreferenceStore()
							.getString("SAVE_PATH");
					imageLoader.save(path + "\\Causal_Loop_Diagram.png",
							SWT.IMAGE_PNG);
				} finally {
					image.dispose();
					gc.dispose();
				}
			}
		};

		Action recycleGraph = new Action("Reset graph") {
			@Override
			public void run() {
				Shell lShell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell();
				MessageBox dialog = new MessageBox(lShell, SWT.ICON_QUESTION
						| SWT.OK | SWT.CANCEL);
				dialog.setText("Reset graph");
				dialog.setMessage("This will reset the graph to its underlying model. The former graph will be lost.\n\n Press OK if you want to continue.");
				int returnCode = dialog.open();

				if (returnCode == SWT.OK) {
					graph.dispose();
					viewer = new GraphViewer(composite, SWT.NONE);
					viewer.getControl().setLayoutData(
							new GridData(SWT.FILL, SWT.FILL, true, true));
					initialize();
					initializeGraph(composite);
					viewer.refresh();
					composite.layout();
				}
			}
		};

		Action undoAction = new Action("Undo deletion") {
			@Override
			public void run() {
				if (undoList.size() != 0) {
					((GraphItem) undoList.get(undoList.size() - 1))
							.setVisible(true);
					redoList.add(undoList.remove(undoList.size() - 1));
				}
			}
		};

		Action redoAction = new Action("Redo deletion") {
			@Override
			public void run() {
				if (redoList.size() != 0) {
					((GraphItem) redoList.get(redoList.size() - 1))
							.setVisible(false);
					undoList.add(redoList.remove(redoList.size() - 1));
				}
			}
		};

		Action itemRearrange = new Action("Rearrange variables") {
			@Override
			public void run() {
				Shell lShell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell();
				MessageBox dialog = new MessageBox(lShell, SWT.ICON_QUESTION
						| SWT.OK | SWT.CANCEL);
				dialog.setText("Rearrange graph");
				dialog.setMessage("This will rearrange the variables in the graph. The former layout will be lost.\n\n Press OK if you want to continue.");
				int returnCode = dialog.open();

				if (returnCode == SWT.OK) {
					graph.setLayoutAlgorithm(new SpringLayoutAlgorithm(
							LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
				}
			}

		};

		Action deleteSelection = new Action("Delete selected items") {
			@Override
			public void run() {
				for (Object gI : graph.getSelection().toArray()) {
					undoList.add(gI);
					((GraphItem) gI).setVisible(false);
					// ((GraphItem) gI).dispose();
				}
				;
			}
		};

		Action extractGraph = new Action("Extract selected items") {
			@Override
			public void run() {
				for (Object gI : graph.getNodes().toArray()) {
					if (!graph.getSelection().contains(gI)) {
						if (gI instanceof GraphNode) {
							undoList.add(gI);
							((GraphItem) gI).setVisible(false);
							// ((GraphNode) gI).dispose();
						}
					}
				}
				;
			}
		};

		Action zoomIn = new Action("Zoom in") {
			@Override
			public void run() {
				zoomIn();
			}
		};

		Action zoomOut = new Action("Zoom out") {
			@Override
			public void run() {
				zoomOut();
			}
		};

		saveImage.setImageDescriptor(Activator.getIcon("save"));
		getViewSite().getActionBars().getToolBarManager().add(saveImage);

		recycleGraph.setImageDescriptor(Activator.getIcon("synchronize"));
		getViewSite().getActionBars().getToolBarManager().add(recycleGraph);

		undoAction.setImageDescriptor(Activator.getIcon("undo"));
		getViewSite().getActionBars().getToolBarManager().add(undoAction);

		redoAction.setImageDescriptor(Activator.getIcon("redo"));
		getViewSite().getActionBars().getToolBarManager().add(redoAction);

		deleteSelection.setImageDescriptor(Activator.getIcon("delete"));

		getViewSite().getActionBars().getToolBarManager().add(deleteSelection);

		extractGraph.setImageDescriptor(Activator.getIcon("remove"));
		getViewSite().getActionBars().getToolBarManager().add(extractGraph);

		itemRearrange.setImageDescriptor(Activator.getIcon("refresh"));
		getViewSite().getActionBars().getToolBarManager().add(itemRearrange);

		zoomIn.setImageDescriptor(Activator.getIcon("zoom_in"));
		getViewSite().getActionBars().getToolBarManager().add(zoomIn);

		zoomOut.setImageDescriptor(Activator.getIcon("zoom_out"));
		getViewSite().getActionBars().getToolBarManager().add(zoomOut);

		// ZoomContributionViewItem toolbarZoomContributionViewItem = new
		// ZoomContributionViewItem(this);
		// getViewSite().getActionBars().getToolBarManager().add(toolbarZoomContributionViewItem);
	}

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return viewer;
	}

	private static class TextBreaker {
		private static int lastSpacePosition;
		private static int start;
		private static int i;

		public static String brokenLines(String text, int maxLength) {
			lastSpacePosition = 0;
			start = 1;
			StringBuilder stringToBreak = new StringBuilder(text);
			i = 0;

			while (i < text.length()) {

				if (start == maxLength) {
					stringToBreak.replace(lastSpacePosition,
							lastSpacePosition + 1, "\n");
					start = 0;
				}

				if (text.charAt(i) == ' ') {
					lastSpacePosition = i;
				}

				start++;
				i++;
			}

			return stringToBreak.toString();
		}

	}

	private void zoomIn() {
		zoomManager.setZoom(zoomManager.getZoom() + 0.1);
	}

	private void zoomOut() {
		zoomManager.setZoom(zoomManager.getZoom() - 0.1);
	}
}
