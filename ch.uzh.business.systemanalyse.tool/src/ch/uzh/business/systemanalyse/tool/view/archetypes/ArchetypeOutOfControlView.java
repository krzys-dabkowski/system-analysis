package ch.uzh.business.systemanalyse.tool.view.archetypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;

import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.Path;
import ch.uzh.business.systemanalyse.tool.model.Variable;
import ch.uzh.business.systemanalyse.tool.systempaths.PathsFinder;

public class ArchetypeOutOfControlView extends AbstractArchetypeView implements
		IZoomableWorkbenchPart {

	public static final String ID = "ch.uzh.business.systemanalyse.tool.views.archetypes.outofcontrol";

	private String controlVariableName;
	private Combo controlVarCombo;
	protected int controlIndex;
	protected int keyIndex;

	public ArchetypeOutOfControlView() {
		super();
		archetypeName = "Out_Of_Control";
		keyVariableName = "Problem to control";
		controlVariableName = "Control action";
		icReinforcing = false;
		ucReinforcing = true;
		ucDelay = true;
		description = "The following loop constellation consists of a balancing intended consequence loop and a reinforcing unintended consequence loop.\n"
				+ "Special cases thereof are 'Fixes That Fail', 'Shifting The Burden' or 'Accidental Adversaries' archetypes.";
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
		GridLayout pathsLayout = new GridLayout(3, false);
		compositeLoops.setLayout(pathsLayout);
		GridData gdc = new GridData(SWT.FILL, SWT.FILL, true, true);
		gdc.verticalSpan = 2;
		compositeLoops.setLayoutData(gdc);

		GridData gdl = new GridData(SWT.FILL, SWT.FILL, true, false);
		gdl.horizontalSpan = 3;
		Label archetypeLabel = new Label(compositeLoops, SWT.WRAP);
		archetypeLabel.setLayoutData(gdl);
		archetypeLabel.setText(description);

		GridData separatorGridData = new GridData(GridData.FILL_HORIZONTAL);
		separatorGridData.horizontalSpan = 3;
		Label separator0 = new Label(compositeLoops, SWT.HORIZONTAL
				| SWT.SEPARATOR);
		separator0.setLayoutData(separatorGridData);

		keyVarLabel = new Label(compositeLoops, SWT.NONE);
		keyVarLabel.setText(keyVariableName + ":");

		keyVarCombo = new Combo(compositeLoops, SWT.READ_ONLY);
		GridData gdc1 = new GridData(SWT.FILL, SWT.FILL, true, false);
		gdc1.horizontalSpan = 2;
		keyVarCombo.setLayoutData(gdc1);
		keyVarCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				controlVarCombo.deselectAll();

				/*
				 * int selectionKeyIndex = keyVarCombo.getSelectionIndex(); int
				 * selectionControlIndex = controlVarCombo.getSelectionIndex();
				 * 
				 * if (selectionKeyIndex >= 0 && selectionControlIndex >= 0) {
				 * PlatformUI
				 * .getWorkbench().getActiveWorkbenchWindow().getShell(
				 * ).setCursor
				 * (PlatformUI.getWorkbench().getActiveWorkbenchWindow
				 * ().getShell().getDisplay().
				 * getSystemCursor(SWT.CURSOR_WAIT));
				 * 
				 * keyIndex =
				 * Integer.valueOf(keyVarCombo.getItems()[selectionKeyIndex
				 * ].substring
				 * ((keyVarCombo.getItems()[selectionKeyIndex].length() -
				 * 5)).replaceAll("\\D+","")); controlIndex =
				 * Integer.valueOf(keyVarCombo
				 * .getItems()[selectionControlIndex].
				 * substring((keyVarCombo.getItems
				 * ()[selectionControlIndex].length() -
				 * 5)).replaceAll("\\D+",""));
				 * ModelProvider.INSTANCE.setToPaths(keyIndex);
				 * ModelProvider.INSTANCE.setToPaths(controlIndex);
				 * ModelProvider.INSTANCE.setFromToPaths(controlIndex,
				 * keyIndex);
				 * 
				 * tableViewer1.setLabelProvider(new LabelProviderLoopTable());
				 * tableViewer1.setContentProvider(new
				 * ContentProviderLoopTable()); tableViewer1.setInput(new
				 * ModelProviderICLoopOoC(keyIndex, controlIndex, icReinforcing,
				 * ucReinforcing).getRows()); tableViewer1.refresh();
				 * 
				 * tableViewer2.setLabelProvider(new LabelProviderLoopTable());
				 * tableViewer2.setContentProvider(new
				 * ContentProviderLoopTable()); // tableViewer2.setInput(new
				 * ModelProviderUCLoopOoC(variableIndex,
				 * reinforcingUnintendedConsequence).getRows()); //
				 * tableViewer2.refresh();
				 * 
				 * PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(
				 * )
				 * .setCursor(PlatformUI.getWorkbench().getActiveWorkbenchWindow
				 * ().getShell().getDisplay(). getSystemCursor(SWT.DEFAULT)); }
				 */
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		Label controlVarLabel = new Label(compositeLoops, SWT.NONE);
		controlVarLabel.setText(controlVariableName + ":");
		controlVarCombo = new Combo(compositeLoops, SWT.READ_ONLY);
		controlVarCombo.setText("Var no.");
		controlVarCombo.setLayoutData(gdc1);
		controlVarCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				int selectionKeyIndex = keyVarCombo.getSelectionIndex();
				int selectionControlIndex = controlVarCombo.getSelectionIndex();

				if (selectionKeyIndex >= 0 && selectionControlIndex >= 0) {
					PlatformUI
							.getWorkbench()
							.getActiveWorkbenchWindow()
							.getShell()
							.setCursor(
									PlatformUI.getWorkbench()
											.getActiveWorkbenchWindow()
											.getShell().getDisplay()
											.getSystemCursor(SWT.CURSOR_WAIT));

					keyIndex = Integer.valueOf(keyVarCombo.getItems()[selectionKeyIndex]
							.substring(
									(keyVarCombo.getItems()[selectionKeyIndex]
											.length() - 5)).replaceAll("\\D+",
									""));
					controlIndex = Integer
							.valueOf(keyVarCombo.getItems()[selectionControlIndex]
									.substring(
											(keyVarCombo.getItems()[selectionControlIndex]
													.length() - 5)).replaceAll(
											"\\D+", ""));
					ModelProvider.INSTANCE.setToPaths(keyIndex);
					ModelProvider.INSTANCE.setToPaths(controlIndex);
					ModelProvider.INSTANCE.setFromToPaths(controlIndex,
							keyIndex);

					tableViewer1
							.setLabelProvider(new LabelProviderLoopTableOoC(
									controlIndex));
					tableViewer1
							.setContentProvider(new ContentProviderLoopTable());
					tableViewer1.setInput(new ModelProviderICLoopOoC(keyIndex,
							controlIndex, icReinforcing, ucReinforcing)
							.getRows());
					tableViewer1.refresh();

					tableViewer2.setLabelProvider(new LabelProviderLoopTable());
					tableViewer2
							.setContentProvider(new ContentProviderLoopTable());
					// tableViewer2.setInput(new
					// ModelProviderUCLoopOoC(variableIndex,
					// reinforcingUnintendedConsequence).getRows());
					// tableViewer2.refresh();

					PlatformUI
							.getWorkbench()
							.getActiveWorkbenchWindow()
							.getShell()
							.setCursor(
									PlatformUI.getWorkbench()
											.getActiveWorkbenchWindow()
											.getShell().getDisplay()
											.getSystemCursor(SWT.DEFAULT));
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		this.setComboItems();

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
		final GridData gdIC= new GridData(SWT.FILL, SWT.FILL, true, true);
		compositeIC.setLayoutData(gdIC);
		gdIC.horizontalSpan = 2;
		gdIC.minimumHeight = 100;

		tableViewer1 = new TableViewer(compositeIC, SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.SINGLE | SWT.CENTER);
		this.initializeTable(tableViewer1, true);
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

							tableViewer2.setInput(new ModelProviderUCLoopOoC(
									keyIndex, controlIndex, ucReinforcing,
									selectedPath, ucDelay).getRows());
							tableViewer2.refresh();
							showArchetype();
						}
					}
				});

		Label separator2 = new Label(compositeLoops, SWT.HORIZONTAL
				| SWT.SEPARATOR);
		separator2.setLayoutData(separatorGridData);

		Label icLabel2 = new Label(compositeLoops, SWT.BOLD);
		icLabel2.setText("Unintended consequence");
		Button btnDelay = new Button(compositeLoops, SWT.CHECK);
		btnDelay.setText("delayed reaction");
		btnDelay.setSelection(ucDelay);
		GridData labelGridData2 = new GridData(SWT.RIGHT, SWT.FILL, true, false);
		btnDelay.setLayoutData(labelGridData2);
		btnDelay.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ucDelay = ((Button) e.widget).getSelection();

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

					tableViewer2.setInput(new ModelProviderUCLoopOoC(keyIndex,
							controlIndex, ucReinforcing, selectedPath, ucDelay)
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
		final GridData gdUC= new GridData(SWT.FILL, SWT.FILL, true, true);
		compositeUC.setLayoutData(gdUC);
		gdUC.horizontalSpan = 2;
		gdUC.minimumHeight = 100;
		compositeLoops.addListener(SWT.Resize, new Listener () {

			@Override
			public void handleEvent(Event event) {
				int height = (int) (PlatformUI.getWorkbench().getDisplay().getActiveShell().getSize().y*0.23);
				gdIC.heightHint = height;
				gdUC.heightHint = height;
				compositeLoops.layout(true, true);
			}
		});

		tableViewer2 = new TableViewer(compositeUC, SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.SINGLE | SWT.CENTER);
		this.initializeTable(tableViewer2, false);
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

		PathsViewerComparatorOoC comparator1 = new PathsViewerComparatorOoC();
		tableViewer1.setComparator(comparator1);
		PathsViewerComparatorOoC comparator2 = new PathsViewerComparatorOoC();
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

	@Override
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
		controlVarCombo.setItems(comboItems);
	}

	protected void initializeTable(TableViewer tblViewer, boolean loop) {
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
		TableViewerColumn delayColumn2;

		loopNoColumn = new TableViewerColumn(tblViewer, SWT.NONE);
		loopNoColumn.getColumn().setResizable(true);
		loopNoColumn.getColumn().setMoveable(true);
		loopNoColumn.getColumn().setWidth(35);
		loopNoColumn.getColumn().setText("No.");
		loopNoColumn.getColumn().addSelectionListener(
				getSelectionAdapterOoC(loopNoColumn.getColumn(), 0, tblViewer));

		noOfEdgesColumn = new TableViewerColumn(tblViewer, SWT.NONE);
		noOfEdgesColumn.getColumn().setResizable(true);
		noOfEdgesColumn.getColumn().setMoveable(true);
		noOfEdgesColumn.getColumn().setWidth(50);
		noOfEdgesColumn.getColumn().setText("Edges");
		noOfEdgesColumn.getColumn().addSelectionListener(
				getSelectionAdapterOoC(noOfEdgesColumn.getColumn(), 1,
						tblViewer));

		delayColumn = new TableViewerColumn(tblViewer, SWT.NONE);
		delayColumn.getColumn().setResizable(true);
		delayColumn.getColumn().setMoveable(true);
		delayColumn.getColumn().setWidth(50);
		delayColumn.getColumn().setText("Delay");
		delayColumn.getColumn().addSelectionListener(
				getSelectionAdapterOoC(delayColumn.getColumn(), 2, tblViewer));

		if (loop) {
			delayColumn2 = new TableViewerColumn(tblViewer, SWT.NONE);
			delayColumn2.getColumn().setResizable(true);
			delayColumn2.getColumn().setMoveable(true);
			delayColumn2.getColumn().setWidth(80);
			delayColumn2.getColumn().setText("Action Delay");
			//delayColumn2.getColumn().addSelectionListener(getSelectionAdapterOoC(delayColumn2.getColumn(), 3,tblViewer));
		}

		effectColumn = new TableViewerColumn(tblViewer, SWT.NONE);
		effectColumn.getColumn().setResizable(true);
		effectColumn.getColumn().setMoveable(true);
		effectColumn.getColumn().setWidth(50);
		effectColumn.getColumn().setText("Effect");
		effectColumn.getColumn().addSelectionListener(
				getSelectionAdapterOoC(effectColumn.getColumn(), 4, tblViewer));

		loopColumn = new TableViewerColumn(tblViewer, SWT.NONE);
		loopColumn.getColumn().setResizable(true);
		loopColumn.getColumn().setMoveable(true);
		loopColumn.getColumn().setWidth(400);
		if (loop)
			loopColumn.getColumn().setText("Loop");
		else
			loopColumn.getColumn().setText("Path");
		tblViewer.refresh();
	}

	protected SelectionAdapter getSelectionAdapterOoC(final TableColumn column,
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
				((PathsViewerComparatorOoC) tblViewer.getComparator()).setColumn(index);
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

	public class PathsViewerComparatorOoC extends ViewerComparator {

		private int propertyIndex;
		private static final int DESCENDING = 1;
		private int direction = DESCENDING;

		public PathsViewerComparatorOoC() {

		}

		public void setColumn(int column) {
			if (column == this.propertyIndex) {
				direction = 1 - direction;
			} else {
				this.propertyIndex = column;
				direction = DESCENDING;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface
		 * .viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			Path p1 = (Path) e1;
			Path p2 = (Path) e2;

			boolean loop = false;
			if (p1.getNodes().get(0)
					.equals(p1.getNodes().get(p1.getNodes().size() - 1))) {
				loop = true;
			}

			int rc = 0;
			switch (propertyIndex) {

			case 1:
				rc = (p1.getNodes().size() > p2.getNodes().size() ? 1 : -1);
				break;
			case 2:
				rc = (p1.getDelay() > p2.getDelay() ? 1 : -1);
				break;
			case 3:
				ArrayList<Integer> path1 = new ArrayList<Integer>();
				ArrayList<Integer> path2 = new ArrayList<Integer>();
				boolean after = false;
				for (Integer node : p1.getNodes()) {
					if (node.intValue() == controlIndex) {
						after = true;
					}
					if (after) {
						path1.add(node);
					}
				}

				after = false;
				for (Integer node : p2.getNodes()) {
					if (node.intValue() == controlIndex) {
						after = true;
					}
					if (after) {
						path2.add(node);
					}
				}
				rc = (PathsFinder.calculateDelay(path1) > PathsFinder
						.calculateDelay(path2) ? 1 : -1);
				break;
			case 4:
				rc = (p1.getEffect() > p2.getEffect() ? 1 : -1);
				break;
			case 0:
				rc = (p1.getIndex() < p2.getIndex() ? 1 : -1);
				break;
			default:
				rc = 0;
				break;
			}

			if (direction == DESCENDING) {
				rc = -rc;
			}
			return rc;
		}

	}

}