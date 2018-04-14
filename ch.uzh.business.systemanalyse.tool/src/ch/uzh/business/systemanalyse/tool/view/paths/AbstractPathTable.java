/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.paths;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import ch.uzh.business.systemanalyse.tool.model.IModelChangeListener;
import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.view.table.AbstractTitledView;

/**
 * @author Krzysztof Dabkowski
 *
 */
public abstract class AbstractPathTable extends AbstractTitledView implements IModelChangeListener {

	public static final String ID = "";

	protected TableViewer viewer;
	private TableViewerColumn pathNoColumn;
	private TableViewerColumn noOfEdgesColumn;
	private TableViewerColumn delayColumn;
	private TableViewerColumn effectColumn;
	private TableViewerColumn pathColumn;

	
	private PathsViewerComparator comparator;
	
	
	/**
	 * 
	 */
	

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		//ModelProvider.INSTANCE.setFromToPaths(1, 4);
		//ModelProvider.INSTANCE.setToPaths(4);
		
//		GridLayout layout = new GridLayout(getNumberOfGrids(), false);
		GridLayout layout = new GridLayout(1, false);
		parent.setLayout(layout);
		
//		Composite titleComposite = new Composite(parent, SWT.NONE);
//		titleComposite.setLayout(new GridLayout(1, false));
		createTitle(parent, getNumberOfGrids());
		
//		Composite widgetsComposite = new Composite(parent, SWT.NONE);
//		widgetsComposite.setLayout(new GridLayout(1, false))
		Composite widgetsComposite = new Composite(parent, SWT.BORDER);
		GridLayout widgetsCompositeLayout = new GridLayout(getNumberOfGrids(), false);
		widgetsComposite.setLayout(widgetsCompositeLayout);
		widgetsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		createWidgets(widgetsComposite);
		createTable(parent);
		createProviders();
		getSite().setSelectionProvider(viewer);
		
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		adjustGridData();
		
		comparator = new PathsViewerComparator();
		viewer.setComparator(comparator);
		
		ModelProvider.INSTANCE.registerListener(this);
	}
	
	protected void adjustGridData() {
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = getNumberOfGrids();
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {

	}
	
	private void createTable(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		
		pathNoColumn = new TableViewerColumn(viewer, SWT.NONE);
		pathNoColumn.getColumn().setResizable(true);
		pathNoColumn.getColumn().setMoveable(true);
		pathNoColumn.getColumn().setWidth(70);
		pathNoColumn.getColumn().setText("Path No.");
		pathNoColumn.getColumn().addSelectionListener(getSelectionAdapter(pathNoColumn.getColumn(), 0));
		
		noOfEdgesColumn = new TableViewerColumn(viewer, SWT.NONE);
		noOfEdgesColumn.getColumn().setResizable(true);
		noOfEdgesColumn.getColumn().setMoveable(true);
		noOfEdgesColumn.getColumn().setWidth(80);
		noOfEdgesColumn.getColumn().setText("No. of Edges");
		noOfEdgesColumn.getColumn().addSelectionListener(getSelectionAdapter(noOfEdgesColumn.getColumn(), 1));
		
		delayColumn = new TableViewerColumn(viewer, SWT.NONE);
		delayColumn.getColumn().setResizable(true);
		delayColumn.getColumn().setMoveable(true);
		delayColumn.getColumn().setWidth(50);
		delayColumn.getColumn().setText("Delay");
		delayColumn.getColumn().addSelectionListener(getSelectionAdapter(delayColumn.getColumn(), 2));
		
		effectColumn = new TableViewerColumn(viewer, SWT.NONE);
		effectColumn.getColumn().setResizable(true);
		effectColumn.getColumn().setMoveable(true);
		effectColumn.getColumn().setWidth(50);
		effectColumn.getColumn().setText("Effect");
		effectColumn.getColumn().addSelectionListener(getSelectionAdapter(effectColumn.getColumn(), 3));
		
		pathColumn = new TableViewerColumn(viewer, SWT.NONE);
		pathColumn.getColumn().setResizable(true);
		pathColumn.getColumn().setMoveable(true);
		pathColumn.getColumn().setWidth(400);
		pathColumn.getColumn().setText("Path");
	}
	
	private SelectionAdapter getSelectionAdapter(final TableColumn column,
			final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {

			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
				int dir = viewer.getTable().getSortDirection();
				if(viewer.getTable().getSortColumn() == column) {
					dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
				} else {
					dir = SWT.DOWN;
				}
				viewer.getTable().setSortDirection(dir);
				viewer.getTable().setSortColumn(column);
				viewer.refresh();
			}
			
		};
		return selectionAdapter;
	}
	
	protected void updateTable() {
		createProviders();
		viewer.refresh();
	}
	
	public TableViewer getViewer() {
		return viewer;
	}
	
	protected abstract void createProviders();
		
	protected abstract void createWidgets(Composite parent);
	
	protected abstract int getNumberOfGrids();

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.model.IModelChangeListener#fireModelChange()
	 */
	@Override
	public void fireModelChange(boolean clear) {
		setComboItems();
		updateTable();
	}
	
	abstract protected void setComboItems();
	

	@Override
	public void dispose() {
		super.dispose();
		ModelProvider.INSTANCE.unregisterListener(this);
	}

}
