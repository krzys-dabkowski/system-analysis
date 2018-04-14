/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.policyonoff;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import ch.uzh.business.systemanalyse.tool.model.Path;
import ch.uzh.business.systemanalyse.tool.view.paths.PathTableContentProvider;
import ch.uzh.business.systemanalyse.tool.view.paths.PathTableLabelProvider;
import ch.uzh.business.systemanalyse.tool.view.paths.PathsViewerComparator;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class PathsDialog extends TitleAreaDialog {

	private ArrayList<Path> feedbackLoops = null;
	
	private TableViewer viewer;
	private TableViewerColumn pathNoColumn;
	private TableViewerColumn noOfEdgesColumn;
	private TableViewerColumn delayColumn;
	private TableViewerColumn effectColumn;
	private TableViewerColumn pathColumn;
	
	private PathsViewerComparator comparator;
	
	
	public PathsDialog(Shell parentShell) {
		super(parentShell);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#create()
	 */
	@Override
	public void create() {
		super.create();
		setTitle("Policy ON/OFF");
		setMessage("Details about all feedback loops", IMessageProvider.INFORMATION);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite p) {
		
		final ScrolledComposite sc = new ScrolledComposite(p, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		sc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		Composite c = new Composite(sc, SWT.NONE);
		c.setLayout(new GridLayout(1, true));
		
		/*Label pathLabel = new Label(c, SWT.NONE);
		pathLabel.setText("Path");
		
		Label delayLabel = new Label(c, SWT.NONE);
		delayLabel.setText("Delay");
		
		Label impactLabel = new Label(c, SWT.NONE);
		impactLabel.setText("Impact");
		
		Label nodesLabel = new Label(c, SWT.NONE);
		nodesLabel.setText("Number of nodes");*/
		
		createTable(c);
		createProviders();
		comparator = new PathsViewerComparator();
		viewer.setComparator(comparator);
		/*for(Path loop : feedbackLoops) {
			Text pathText = new Text(c, SWT.READ_ONLY);
			String text = "";
			for(Integer node : loop.getNodes()) {
				text += String.valueOf(node) + "->";
			}
			text = text.substring(0, text.length() - 2);
			pathText.setText(text);
			
			Text delayText = new Text(c, SWT.READ_ONLY);
			delayText.setText(String.valueOf(loop.getDelay()));
			
			Text impactText = new Text(c, SWT.READ_ONLY);
			impactText.setText(String.format("%.2f", loop.getEffect()));
			
			Text nodesText = new Text(c, SWT.READ_ONLY);
			nodesText.setText(String.valueOf(loop.getNodes().size()));
		}*/
		
		sc.setContent(c);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		return p;
	}
	
	public void setFeedbackLoops(ArrayList<Path> loops) {
		this.feedbackLoops = loops;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton(IDialogConstants.CANCEL_ID).setVisible(false);
	}
	
	private void createTable(Composite c) {
		viewer = new TableViewer(c, SWT.MULTI //| SWT.H_SCROLL | SWT.V_SCROLL 
				| SWT.FULL_SELECTION | SWT.FILL);
		
		//viewer.set
		
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
		
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
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
	
	private void createProviders() {
		viewer.setLabelProvider(new PathTableLabelProvider());
		viewer.setContentProvider(new PathTableContentProvider());
		//if(ModelProvider.i)
		viewer.setInput(new PathsDialogTableModelProvider(feedbackLoops).getRows());
		
	}

}
