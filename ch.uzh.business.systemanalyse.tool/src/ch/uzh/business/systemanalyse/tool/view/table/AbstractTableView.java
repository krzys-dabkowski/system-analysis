/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.table;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import ch.uzh.business.systemanalyse.tool.Activator;
import ch.uzh.business.systemanalyse.tool.IProjectSettingsConstants;
import ch.uzh.business.systemanalyse.tool.model.IModelChangeListener;
import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableLabelProvider;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableModelProvider;
import ch.uzh.business.systemanalyse.tool.table.TableContentProvider;
import ch.uzh.business.systemanalyse.tool.table.VariableNameEditingSupport;

/**
 * @author Krzysztof Dabkowski
 * 
 */
public abstract class AbstractTableView extends AbstractTitledView implements
		IModelChangeListener {
	public static final String ID = "";

	protected TableViewer viewer;
	private TableViewerColumn varNoColumn;
	private TableViewerColumn varNameColumn;
	private final List<TableViewerColumn> variableValuesColumns;
	private TableViewerColumn activeSumColumn;
	private Composite parent;

	IPropertyChangeListener preferenceListener = new IPropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			String p = event.getProperty();
			if (p.equals("TABLE_ROW_HIGHLIGHT_LIGHT")
					|| p.equals("TABLE_ROW_HIGHLIGHT_DARK")) {
				updateTable();
			}
		}
	};

	/**
	 * 
	 */
	public AbstractTableView() {
		super();
		variableValuesColumns = new ArrayList<TableViewerColumn>();
		Activator.getDefault().getPreferenceStore()
				.addPropertyChangeListener(preferenceListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		// GridLayout gridLayout = new GridLayout();
		// gridLayout.numColumns = 1;
		// parent.setLayout(gridLayout);
		//
		// viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
		// | SWT.V_SCROLL | SWT.FULL_SELECTION);
		//
		// viewer.getTable().setLayoutData(
		// new GridData(SWT.FILL, SWT.FILL, true, true));
		this.parent = parent;
		createColumns();

		updateTable();
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);

		setColumnWidths();
		getSite().setSelectionProvider(viewer);
		/*
		 * MenuManager menuManager = new MenuManager(); Menu menu =
		 * menuManager.createContextMenu(viewer.getTable());
		 * viewer.getTable().setMenu(menu);
		 * getSite().registerContextMenu(menuManager, viewer);
		 * getSite().setSelectionProvider(viewer);
		 */

		ModelProvider.INSTANCE.registerListener(this);
	}

	public void createColumns() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		parent.setLayout(gridLayout);

		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);

		viewer.getTable().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));
		if (viewer.getTable().isDisposed()) {
			MessageDialog.openInformation(PlatformUI.getWorkbench()
					.getDisplay().getActiveShell(), "DISPOSED",
					"Table is disposed" + "\n" + this.getClass());
		} else {
			viewer.getTable().setLayoutData(
					new GridData(SWT.FILL, SWT.FILL, true, true));
		}
		varNoColumn = new TableViewerColumn(viewer, SWT.NONE);
		// varNoColumn.getColumn().setWidth(20);
		varNoColumn.getColumn().setResizable(true);
		varNoColumn.getColumn().setMoveable(true);

		varNameColumn = new TableViewerColumn(viewer, SWT.CENTER);
		// varNameColumn.getColumn().setWidth(120);
		varNameColumn.getColumn().setResizable(true);
		varNameColumn.getColumn().setMoveable(true);

		for (int i = IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS; i < ModelProvider.INSTANCE
				.getVariables().size()
				+ IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS; i++) {
			TableViewerColumn column = new TableViewerColumn(viewer, SWT.CENTER);
			column.getColumn().setWidth(
					IProjectSettingsConstants.IMPACT_TABLE_COLUMN_WIDTH);
			column.getColumn().setResizable(true);
			column.getColumn().setMoveable(true);
			variableValuesColumns.add(column);
		}

		activeSumColumn = new TableViewerColumn(viewer, SWT.NONE);
		activeSumColumn.getColumn().setWidth(
				IProjectSettingsConstants.IMPACT_TABLE_COLUMN_WIDTH);
		activeSumColumn.getColumn().setResizable(true);
		activeSumColumn.getColumn().setMoveable(true);
	}

	public void clearTable() {
		// try {
		// for (TableColumn col : viewer.getTable().getColumns()) {
		// col.dispose();
		// }
		// } catch (Exception e) {
		// StringWriter sw = new StringWriter();
		// PrintWriter pw = new PrintWriter(sw);
		// e.printStackTrace(pw);
		// System.out
		// .println("Encountered an Exception clearing table (disposing columns): \n"
		// + sw.toString());
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		// System.out.println("Unregistering me: " + this.getClass());
		ModelProvider.INSTANCE.unregisterListener(this);
		System.out.println("Hiding view: " + this.getClass());
		page.hideView(this);
		// } finally {
		// variableValuesColumns.clear();
		// }

	}

	abstract protected void registerMenus();

	// abstract protected String getTitle();

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		updateTable();
		viewer.getControl().setFocus();
	}

	public TableViewer getViewer() {
		return viewer;
	}

	protected final void updateTable() {
		setColumnsText();
		setEditingSupports();
		viewer.setLabelProvider(createLabelProvider());
		viewer.setContentProvider(new TableContentProvider());
		viewer.setInput(createModelProvider().getRows());
		setColumnWidths();
		viewer.refresh();
	}

	public void addVariableColumn() {
		if (variableValuesColumns.size() != ModelProvider.INSTANCE
				.getVariables().size()) {
			variableValuesColumns.add(activeSumColumn);
			activeSumColumn = new TableViewerColumn(viewer, SWT.NONE);
			activeSumColumn
					.getColumn()
					.setText(
							String.valueOf(viewer.getTable().getColumnCount()
									- IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS));
			// activeSumColumn.getColumn().setWidth(50);
			activeSumColumn.getColumn().setResizable(true);
			activeSumColumn.getColumn().setMoveable(true);
			// column.setEditingSupport(new VariableFieldEditingSupport(viewer,
			// viewer.getTable().getColumnCount()));
			updateTable();
		}
	}

	public void deleteVariableColumn(int varIndex) {
		variableValuesColumns.remove(variableValuesColumns.size() - 1);
		viewer.getTable().getColumn(viewer.getTable().getColumnCount() - 1)
				.dispose();
		updateTable();
	}

	abstract protected void setColumnsText();

	/**
	 * 
	 */
	protected void setEditingSupports() {
		varNameColumn.setEditingSupport(new VariableNameEditingSupport(
				getViewer()));
		for (TableViewerColumn column : variableValuesColumns) {
			column.setEditingSupport(createEditingSupport(viewer, column));
		}
	}

	abstract protected EditingSupport createEditingSupport(TableViewer viewer,
			TableViewerColumn column);

	abstract protected AbstractTableModelProvider createModelProvider();

	abstract protected AbstractTableLabelProvider createLabelProvider();

	private void setColumnWidths() {
		int[] columnOrder = viewer.getTable().getColumnOrder();
		int numberOfColumns = viewer.getTable().getColumnCount();
		for (int i = 0; i < viewer.getTable().getColumns().length; i++) {
			TableColumn column = viewer.getTable().getColumn(i);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.uzh.business.systemanalyse.tool.model.IModelChangeListener#fireModelChange
	 * ()
	 */
	@Override
	public void fireModelChange(boolean clear) {
		if (clear) {
			System.out.println("Executing model change with \"clear\": "
					+ clear);
			clearTable();
			// createColumns();
		} else {
			updateTable();
		}
		// if (viewer.getLabelProvider() instanceof AbstractTableLabelProvider)
		// {
		// AbstractTableLabelProvider labelProvider =
		// (AbstractTableLabelProvider) viewer
		// .getLabelProvider();
		// /*
		// * if(!labelProvider.isValid()) { //this.set checkValidation(); }
		// */
		// System.out.println(this.getTitle() + " " + labelProvider.isValid());
		// }
	}

	abstract protected void checkValidation();

	@Override
	public void dispose() {
		super.dispose();
		ModelProvider.INSTANCE.unregisterListener(this);
		System.out.println("Disposing View: " + this.getClass());
	}

}
