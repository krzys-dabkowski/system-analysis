/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.archetypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
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
import ch.uzh.business.systemanalyse.tool.IProjectSettingsConstants;
import ch.uzh.business.systemanalyse.tool.model.IModelChangeListener;
import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.Path;
import ch.uzh.business.systemanalyse.tool.model.Variable;
import ch.uzh.business.systemanalyse.tool.model.VariableValue;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableLabelProvider;
import ch.uzh.business.systemanalyse.tool.table.TableContentProvider;
import ch.uzh.business.systemanalyse.tool.view.paths.PathsViewerComparator;
import ch.uzh.business.systemanalyse.tool.view.table.AbstractTitledView;

/**
 * @author Alexander Schmid
 * 
 */
public abstract class AbstractArchetypeView extends AbstractTitledView
		implements IModelChangeListener, IZoomableWorkbenchPart {

	public static final String ID = "";

	protected ZoomManager zoomManager;
	protected GraphViewer graphViewer;
	protected Graph graph;
	protected int layout = 1;
	protected String archetypeName, description, keyVariableName;
	protected HashMap<Variable, GraphNode> variablesAndNodes;
	protected ArrayList<Variable> variables;
	protected ArrayList<Object> undoList, redoList;
	protected ArrayList<ArrayList<Object>> connections;
	protected Label keyVarLabel;
	protected Combo keyVarCombo;
	protected Button showLoopsButton;

	protected TableViewer tableViewer1, tableViewer2;
	protected TableViewerColumn varNoColumn;
	protected TableViewerColumn varNameColumn;
	protected List<TableViewerColumn> variableValuesColumns;
	protected TableViewerColumn activeSumColumn;
	protected boolean icReinforcing, ucReinforcing, ucDelay;

	protected Path icLoop, ucLoop;

	public AbstractArchetypeView() {
		super();
		undoList = new ArrayList<Object>();
		redoList = new ArrayList<Object>();
		variablesAndNodes = new HashMap<Variable, GraphNode>();
		variables = (ArrayList<Variable>) ModelProvider.INSTANCE.getVariables();
	}

	@Override
	public void createPartControl(Composite parent) {
		ModelProvider.INSTANCE.init();

		GridLayout layout = new GridLayout(3, true);
		parent.setLayout(layout);
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		this.createTitle(parent, 2);
		this.createToolbar();

		final Composite compositeLoops = new Composite(parent, SWT.BORDER);
		GridLayout pathsLayout = new GridLayout(2, false);
		compositeLoops.setLayout(pathsLayout);
		GridData gdc = new GridData(SWT.FILL, SWT.FILL, true, true);
		gdc.verticalSpan = 2;
		compositeLoops.setLayoutData(gdc);

		GridData gdl = new GridData(SWT.FILL, SWT.FILL, true, false);
		gdl.horizontalSpan = 2;
		Label archetypeLabel = new Label(compositeLoops, SWT.WRAP);
		archetypeLabel.setLayoutData(gdl);
		archetypeLabel.setText(description);

		GridData separatorGridData = new GridData(GridData.FILL_HORIZONTAL);
		separatorGridData.horizontalSpan = 2;
		Label separator0 = new Label(compositeLoops, SWT.HORIZONTAL
				| SWT.SEPARATOR);
		separator0.setLayoutData(separatorGridData);

		keyVarLabel = new Label(compositeLoops, SWT.NONE);
		keyVarLabel.setText(keyVariableName + ":");
		keyVarCombo = new Combo(compositeLoops, SWT.READ_ONLY);
		keyVarCombo.setText("Var no.");
		this.setComboItems();
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		keyVarCombo.setLayoutData(gd);
		keyVarCombo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PlatformUI
						.getWorkbench()
						.getActiveWorkbenchWindow()
						.getShell()
						.setCursor(
								PlatformUI.getWorkbench()
										.getActiveWorkbenchWindow().getShell()
										.getDisplay()
										.getSystemCursor(SWT.CURSOR_WAIT));
				// String [] items =
				int startSelection = keyVarCombo.getSelectionIndex();
				int variableIndex = 0;
				if (startSelection != -1) {
					int varIndex = Integer.valueOf(keyVarCombo.getItems()[startSelection]
							.substring(
									(keyVarCombo.getItems()[startSelection]
											.length() - 5)).replaceAll("\\D+",
									""));
					variableIndex = varIndex;
					ModelProvider.INSTANCE.setToPaths(varIndex);
				}

				tableViewer1.setLabelProvider(new LabelProviderLoopTable());
				tableViewer1.setContentProvider(new ContentProviderLoopTable());
				tableViewer1.setInput(new ModelProviderICLoop(variableIndex,
						icReinforcing, ucReinforcing).getRows());
				tableViewer1.refresh();

				tableViewer2.setLabelProvider(new LabelProviderLoopTable());
				tableViewer2.setContentProvider(new ContentProviderLoopTable());
				tableViewer2.setInput(new ModelProviderUCLoop(variableIndex,
						ucReinforcing).getRows());
				tableViewer2.refresh();
				PlatformUI
						.getWorkbench()
						.getActiveWorkbenchWindow()
						.getShell()
						.setCursor(
								PlatformUI.getWorkbench()
										.getActiveWorkbenchWindow().getShell()
										.getDisplay()
										.getSystemCursor(SWT.DEFAULT));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		Label separator1 = new Label(compositeLoops, SWT.HORIZONTAL
				| SWT.SEPARATOR);
		separator1.setLayoutData(separatorGridData);

		GridData labelGridData1 = new GridData(SWT.FILL, SWT.FILL, false, false);
		labelGridData1.horizontalSpan = 3;
		Label icLabel1 = new Label(compositeLoops, SWT.BOLD);
		icLabel1.setText("Intendend consequence");
		icLabel1.setLayoutData(labelGridData1);

		final Composite compositeIC = new Composite(compositeLoops, SWT.NONE);
		GridLayout tableLayout = new GridLayout(2, false);
		compositeIC.setLayout(tableLayout);
		final GridData gdIC = new GridData(SWT.FILL, SWT.FILL, true, true);
		compositeIC.setLayoutData(gdIC);
		gdIC.horizontalSpan = 2;
		gdIC.minimumHeight = 100;

		tableViewer1 = new TableViewer(compositeIC, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.FULL_SELECTION | SWT.SINGLE | SWT.CENTER);
		this.initializeTable(tableViewer1);
		tableViewer1
				.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						if (!event.getSelection().isEmpty()) {
							String[] selection = ((StructuredSelection) event
									.getSelection()).getFirstElement()
									.toString().split(" ");
							ArrayList<Integer> selectedNodes = new ArrayList<Integer>();

							for (int i = 0; i < selection.length; i++) {
								selectedNodes.add(Integer
										.parseInt(selection[i]));
							}

							Path selectedPath = new Path();
							selectedPath.setNodes(selectedNodes);
							icLoop = selectedPath;
							ucLoop = selectedPath;

							tableViewer2.setInput(new ModelProviderUCLoop(
									selectedNodes.get(0), ucReinforcing,
									selectedPath, ucDelay).getRows());
							tableViewer2.refresh();
							showArchetype();
						}
					}
				});

		Label separator2 = new Label(compositeLoops, SWT.HORIZONTAL
				| SWT.SEPARATOR);
		separator2.setLayoutData(separatorGridData);

		GridData labelGridData2 = new GridData(SWT.RIGHT, SWT.FILL, false,
				false);
		Label icLabel2 = new Label(compositeLoops, SWT.BOLD);
		icLabel2.setText("Unintended consequence");
		Button btnDelay = new Button(compositeLoops, SWT.CHECK);
		btnDelay.setText("delayed");
		btnDelay.setSelection(ucDelay);
		btnDelay.setLayoutData(labelGridData2);
		btnDelay.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ucDelay = ((Button) e.widget).getSelection();
				// tableViewer1.setSelection(StructuredSelection.EMPTY);
				// tableViewer2.getTable().removeAll();
				// tableViewer2.getTable().redraw();
				if (!tableViewer1.getSelection().isEmpty()) {
					String[] selection = ((StructuredSelection) tableViewer1
							.getSelection()).getFirstElement().toString()
							.split(" ");
					ArrayList<Integer> selectedNodes = new ArrayList<Integer>();

					for (int i = 0; i < selection.length; i++) {
						selectedNodes.add(Integer.parseInt(selection[i]));
					}

					Path selectedPath = new Path();
					selectedPath.setNodes(selectedNodes);
					icLoop = selectedPath;
					ucLoop = selectedPath;

					tableViewer2.setInput(new ModelProviderUCLoop(selectedNodes
							.get(0), ucReinforcing, selectedPath, ucDelay)
							.getRows());
					tableViewer2.refresh();
					showArchetype();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		final Composite compositeUC = new Composite(compositeLoops, SWT.NONE);
		GridLayout tableLayoutUC = new GridLayout(2, false);
		compositeUC.setLayout(tableLayoutUC);
		final GridData gdUC = new GridData(SWT.FILL, SWT.FILL, true, true);
		compositeUC.setLayoutData(gdUC);
		gdUC.horizontalSpan = 2;
		gdUC.minimumHeight = 100;
		compositeLoops.addListener(SWT.Resize, new Listener() {

			@Override
			public void handleEvent(Event event) {
				int height = (int) (PlatformUI.getWorkbench().getDisplay()
						.getActiveShell().getSize().y * 0.28);
				gdIC.heightHint = height;
				gdUC.heightHint = height;
				compositeLoops.layout(true, true);
			}
		});

		tableViewer2 = new TableViewer(compositeUC, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.FULL_SELECTION | SWT.SINGLE | SWT.CENTER);
		this.initializeTable(tableViewer2);
		tableViewer2
				.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						if (!event.getSelection().isEmpty()) {
							String[] selection = ((StructuredSelection) event
									.getSelection()).getFirstElement()
									.toString().split(" ");
							ArrayList<Integer> selectedNodes = new ArrayList<Integer>();

							for (int i = 0; i < selection.length; i++) {
								selectedNodes.add(Integer
										.parseInt(selection[i]));
							}

							Path selectedPath = new Path();
							selectedPath.setNodes(selectedNodes);
							ucLoop = selectedPath;
							showArchetype();
						}
					}
				});

		PathsViewerComparator comparator1 = new PathsViewerComparator();
		tableViewer1.setComparator(comparator1);
		PathsViewerComparator comparator2 = new PathsViewerComparator();
		tableViewer2.setComparator(comparator2);

		icLoop = new Path();
		ucLoop = new Path();

		ArrayList<Integer> l = new ArrayList<Integer>();
		l.add(0);
		icLoop.setNodes(l);
		ucLoop.setNodes(l);

		GridData graphGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		graphGridData.horizontalSpan = 2;
		graphViewer = new GraphViewer(parent, SWT.NONE);
		graphViewer.getControl().setLayoutData(graphGridData);
		this.initializeGraph(parent);
	}

	void showArchetype() {
		ArrayList<Integer> archetypePaths = new ArrayList<Integer>();
		archetypePaths.addAll(icLoop.getNodes());
		archetypePaths.addAll(ucLoop.getNodes());
		ArrayList<Integer> archetypeICPaths = new ArrayList<Integer>();
		archetypeICPaths.addAll(icLoop.getNodes());
		ArrayList<Integer> archetypeUCPaths = new ArrayList<Integer>();
		archetypeUCPaths.addAll(ucLoop.getNodes());

		for (Variable v : variables) {
			if (!archetypePaths.contains(v.getIndex())) {
				variablesAndNodes.get(v).setVisible(false);
			} else {
				variablesAndNodes.get(v).setVisible(true);
			}
		}
		for (ArrayList<Object> gC : connections) {
			((GraphConnection) gC.get(2)).setVisible(false);
			((GraphConnection) gC.get(2)).setLineStyle(SWT.LINE_SOLID);
			for (int i = 0; i < archetypeUCPaths.size() - 1; i++) {
				if (((Variable) gC.get(0)).getIndex() == archetypeUCPaths
						.get(i)
						&& ((Variable) gC.get(1)).getIndex() == archetypeUCPaths
								.get(i + 1)) {
					((GraphItem) gC.get(2)).setVisible(true);
					((GraphConnection) gC.get(2)).setLineStyle(SWT.LINE_DOT);
				}
			}
			for (int i = 0; i < archetypeICPaths.size() - 1; i++) {
				if (((Variable) gC.get(0)).getIndex() == archetypeICPaths
						.get(i)
						&& ((Variable) gC.get(1)).getIndex() == archetypeICPaths
								.get(i + 1)) {
					((GraphItem) gC.get(2)).setVisible(true);
					((GraphConnection) gC.get(2)).setLineStyle(SWT.LINE_SOLID);
				}
			}
		}
		graphViewer.refresh();
	}

	protected void setComboItems() {
		String[] comboItems = new String[ModelProvider.INSTANCE.getVariables()
				.size()];

		List<List<Object>> list = new ArrayList<List<Object>>();
		for (Variable v : ModelProvider.INSTANCE.getVariables()) {

			double pValue = ModelProvider.INSTANCE.getActiveSums()[v.getIndex() - 1]
					* ModelProvider.INSTANCE.getPassiveSums()[v.getIndex() - 1];

			int index = 0;
			if (list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					if (pValue <= (double) (Double) list.get(i).get(0)) {
						index++;
					}
				}
			}

			if (index >= list.size()) {
				list.add(Arrays.asList(pValue, v));
			} else {
				list.add(index, Arrays.asList(pValue, v));
			}
		}

		int size = list.size();
		for (int i = 0; i < size; i++) {
			Variable var = (Variable) list.get(i).get(1);
			comboItems[i] = String.valueOf(var.getName() + " ("
					+ var.getIndex() + ")");
		}

		keyVarCombo.setItems(comboItems);
	}

	@SuppressWarnings("restriction")
	protected void initializeGraph(Composite parent) {
		// Graph will hold all other objects
		graph = graphViewer.getGraphControl();
		connections = new ArrayList<ArrayList<Object>>();
		this.initializeNodes();

		for (Variable variable : variables) {
			for (VariableValue impact : variable.getImpactArray()) {
				if (impact.getVariableValue() != 0) {
					GraphConnection gC = new GraphConnection(graph,
							ZestStyles.CONNECTIONS_DIRECTED,
							variablesAndNodes.get(variable),
							variablesAndNodes.get(variables.get(impact
									.getVariableIndex() - 1)));

					gC.setHighlightColor(parent.getDisplay().getSystemColor(
							SWT.COLOR_GREEN));
					gC.setTooltip(new org.eclipse.draw2d.Label("Effect: "
							+ variable.getImpactValueToVariable(variables.get(
									impact.getVariableIndex() - 1).getIndex())
							+ "/Delay: "
							+ variable.getDelayValueToVariable(variables.get(
									impact.getVariableIndex() - 1).getIndex())));

					if (impact.getVariableValue() > 0) {
						gC.setLineColor(org.eclipse.draw2d.ColorConstants.blue);
					} else {
						gC.setLineColor(org.eclipse.draw2d.ColorConstants.red);
					}

					gC.setCurveDepth(10);
					ArrayList<Object> con = new ArrayList<Object>();
					con.add(0, variable);
					con.add(1, variables.get(impact.getVariableIndex() - 1));
					con.add(2, gC);
					connections.add(con);
					gC.setVisible(false);

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
				// System.out.println(e);
			}

		});

		graph.addKeyListener(new DelKeyListener());

		zoomManager = new ZoomManager(graph.getRootLayer(), graph.getViewport());
		zoomManager.setZoomAsText("90%");
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

	private void initializeNodes() {
		for (Variable variable : variables) {
			GraphNode gN = new GraphNode(graph, SWT.NONE);
			String labelText = variable.getName() + " (" + variable.getIndex()
					+ ")";
			// org.eclipse.draw2d.Label label = new
			// org.eclipse.draw2d.Label(labelText);
			gN.setText(TextBreaker.brokenLines(labelText, 30));
			if (labelText.length() >= 60) {
				gN.setSize(190, -1);
			} else {
				gN.setSize(190, 40);
			}
			gN.setNodeStyle(SWT.CENTER);
			gN.setVisible(false);
			variablesAndNodes.put(variable, gN);
		}
	}

	@Override
	public void setFocus() {
	}

	protected void createToolbar() {

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
					imageLoader.save(path + "\\" + archetypeName + ".png",
							SWT.IMAGE_PNG);
				} finally {
					image.dispose();
					gc.dispose();
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

		/*
		 * undoAction.setImageDescriptor(Activator.getImageDescriptor(
		 * "icons/undo.ico"));
		 * getViewSite().getActionBars().getToolBarManager().add(undoAction);
		 * 
		 * redoAction.setImageDescriptor(Activator.getImageDescriptor(
		 * "icons/redo.ico"));
		 * getViewSite().getActionBars().getToolBarManager().add(redoAction);
		 * 
		 * 
		 * deleteSelection.setImageDescriptor(Activator
		 * .getImageDescriptor("icons/delete.ico"));
		 * getViewSite().getActionBars(
		 * ).getToolBarManager().add(deleteSelection);
		 * 
		 * extractGraph.setImageDescriptor(Activator
		 * .getImageDescriptor("icons/remove.ico"));
		 * getViewSite().getActionBars().getToolBarManager().add(extractGraph);
		 */

		itemRearrange.setImageDescriptor(Activator.getIcon("refresh"));
		getViewSite().getActionBars().getToolBarManager().add(itemRearrange);

		zoomIn.setImageDescriptor(Activator.getIcon("zoom_in"));
		getViewSite().getActionBars().getToolBarManager().add(zoomIn);

		zoomOut.setImageDescriptor(Activator.getIcon("zoom_out"));
		getViewSite().getActionBars().getToolBarManager().add(zoomOut);
	}

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return graphViewer;
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

	private final void updateTable() {
		tableViewer1.setContentProvider(new TableContentProvider());
		setColumnWidths();
		tableViewer1.refresh();
	}

	public TableViewer getViewer() {
		return tableViewer1;
	}

	public void clearTable() {
		for (TableColumn col : tableViewer1.getTable().getColumns()) {
			col.dispose();
		}
		variableValuesColumns.clear();
	}

	public void createColumns(TableViewer tblView) {
		// tblView.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL,
		// true, true));
		varNoColumn = new TableViewerColumn(tblView, SWT.NONE);
		// varNoColumn.getColumn().setWidth(20);
		varNoColumn.getColumn().setResizable(true);
		varNoColumn.getColumn().setMoveable(true);

		varNameColumn = new TableViewerColumn(tblView, SWT.CENTER);
		// varNameColumn.getColumn().setWidth(120);
		varNameColumn.getColumn().setResizable(true);
		varNameColumn.getColumn().setMoveable(true);

		for (int i = IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS; i < ModelProvider.INSTANCE
				.getVariables().size()
				+ IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS; i++) {
			TableViewerColumn column = new TableViewerColumn(tblView,
					SWT.CENTER);
			column.getColumn().setWidth(
					IProjectSettingsConstants.IMPACT_TABLE_COLUMN_WIDTH);
			column.getColumn().setResizable(true);
			column.getColumn().setMoveable(true);
			// variableValuesColumns.add(column);
		}

		activeSumColumn = new TableViewerColumn(tblView, SWT.NONE);
		activeSumColumn.getColumn().setWidth(
				IProjectSettingsConstants.IMPACT_TABLE_COLUMN_WIDTH);
		activeSumColumn.getColumn().setResizable(true);
		activeSumColumn.getColumn().setMoveable(true);
	}

	@Override
	public void fireModelChange(boolean clear) {
		if (clear) {
			clearTable();
			// createColumns();
		}
		updateTable();
		if (tableViewer1.getLabelProvider() instanceof AbstractTableLabelProvider) {
			AbstractTableLabelProvider labelProvider = (AbstractTableLabelProvider) tableViewer1
					.getLabelProvider();
			/*
			 * if(!labelProvider.isValid()) { //this.set checkValidation(); }
			 */
			System.out.println(this.getTitle() + " " + labelProvider.isValid());
		}
	}

	private void setColumnWidths() {
		int[] columnOrder = tableViewer1.getTable().getColumnOrder();
		int numberOfColumns = tableViewer1.getTable().getColumnCount();
		for (int i = 0; i < tableViewer1.getTable().getColumns().length; i++) {
			TableColumn column = tableViewer1.getTable().getColumn(i);
			switch (columnOrder[i]) {
			case 0:
				column.setWidth(30);
				break;

			case 1:
				column.setWidth(120);
				break;

			default:
				if (columnOrder[i] == numberOfColumns - 1) {
					column.setWidth(150);
				} else {
					column.setWidth(60);
				}
				break;
			}
		}

	}

	protected void initializeTable(TableViewer tblViewer) {
		GridData gridDataTable = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridDataTable.horizontalSpan = 3;
		tblViewer.getTable().setLayoutData(gridDataTable);

		tblViewer.getTable().setHeaderVisible(true);
		tblViewer.getTable().setLinesVisible(true);

		TableViewerColumn loopNoColumn;
		TableViewerColumn noOfEdgesColumn;
		TableViewerColumn delayColumn;
		TableViewerColumn effectColumn;
		TableViewerColumn loopColumn;

		loopNoColumn = new TableViewerColumn(tblViewer, SWT.NONE);
		loopNoColumn.getColumn().setResizable(true);
		loopNoColumn.getColumn().setMoveable(true);
		loopNoColumn.getColumn().setWidth(35);
		loopNoColumn.getColumn().setText("No.");
		loopNoColumn.getColumn().addSelectionListener(
				getSelectionAdapter(loopNoColumn.getColumn(), 0, tblViewer));

		noOfEdgesColumn = new TableViewerColumn(tblViewer, SWT.NONE);
		noOfEdgesColumn.getColumn().setResizable(true);
		noOfEdgesColumn.getColumn().setMoveable(true);
		noOfEdgesColumn.getColumn().setWidth(50);
		noOfEdgesColumn.getColumn().setText("Edges");
		noOfEdgesColumn.getColumn().addSelectionListener(
				getSelectionAdapter(noOfEdgesColumn.getColumn(), 1, tblViewer));

		delayColumn = new TableViewerColumn(tblViewer, SWT.NONE);
		delayColumn.getColumn().setResizable(true);
		delayColumn.getColumn().setMoveable(true);
		delayColumn.getColumn().setWidth(50);
		delayColumn.getColumn().setText("Delay");
		delayColumn.getColumn().addSelectionListener(
				getSelectionAdapter(delayColumn.getColumn(), 2, tblViewer));

		effectColumn = new TableViewerColumn(tblViewer, SWT.NONE);
		effectColumn.getColumn().setResizable(true);
		effectColumn.getColumn().setMoveable(true);
		effectColumn.getColumn().setWidth(50);
		effectColumn.getColumn().setText("Effect");
		effectColumn.getColumn().addSelectionListener(
				getSelectionAdapter(effectColumn.getColumn(), 3, tblViewer));

		loopColumn = new TableViewerColumn(tblViewer, SWT.NONE);
		loopColumn.getColumn().setResizable(true);
		loopColumn.getColumn().setMoveable(true);
		loopColumn.getColumn().setWidth(400);
		loopColumn.getColumn().setText("Loop");
		tblViewer.refresh();
	}

	protected SelectionAdapter getSelectionAdapter(final TableColumn column,
			final int index, final TableViewer tblViewer) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				((PathsViewerComparator) tblViewer.getComparator())
						.setColumn(index);
				int dir = tblViewer.getTable().getSortDirection();
				if (tblViewer.getTable().getSortColumn() == column) {
					dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
				} else {
					dir = SWT.DOWN;
				}
				tblViewer.getTable().setSortDirection(dir);
				tblViewer.getTable().setSortColumn(column);
				tblViewer.refresh();
			}

		};
		return selectionAdapter;
	}

	private void zoomIn() {
		zoomManager.setZoom(zoomManager.getZoom() + 0.1);
	}

	private void zoomOut() {
		zoomManager.setZoom(zoomManager.getZoom() - 0.1);
	}
}
