/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.policyonoff;

import java.util.ArrayList;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IParameterValues;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.services.IEvaluationService;

import ch.uzh.business.systemanalyse.tool.commands.SavePathsParameter;
import ch.uzh.business.systemanalyse.tool.commands.SavePathsXls;
import ch.uzh.business.systemanalyse.tool.model.Path;
import ch.uzh.business.systemanalyse.tool.view.paths.PathTableContentProvider;
import ch.uzh.business.systemanalyse.tool.view.paths.PathTableLabelProvider;
import ch.uzh.business.systemanalyse.tool.view.paths.PathsViewerComparator;

/**
 * @author krzysztof.dabkowski
 *
 */
public class PathsWindow extends Window {

	private ArrayList<Path> feedbackLoops = null;
	private String title;

	private TableViewer viewer;
	private TableViewerColumn pathNoColumn;
	private TableViewerColumn noOfEdgesColumn;
	private TableViewerColumn delayColumn;
	private TableViewerColumn effectColumn;
	private TableViewerColumn pathColumn;

	private PathsViewerComparator comparator;
	
	private Control windowArea;
	private Control buttonBar;

	protected PathsWindow(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, 0);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		windowArea = createWindowArea(composite);
		
		buttonBar = createButtonBar(composite);

		//getShell().setSize(parent.getBounds().width, parent.getBounds().height);
		return composite;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
	}
	
	private Control createButtonBar(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(0) // this is incremented
				// by createButton
				.equalWidth(true).applyTo(composite);

		GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).span(2, 1)
		.applyTo(composite);
		composite.setFont(parent.getFont());
		// Add the buttons to the button bar.
		createButtonsForButtonBar(composite);
		return composite;
	}

	private void createButtonsForButtonBar(Composite composite) {
		createButton(composite, "OK", true, new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				close();
			}
		});
		createButton(composite, "Save as", false, new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IParameterValues parameterValues = new SavePathsParameter(feedbackLoops);
				ICommandService cmdService = (ICommandService) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getService(ICommandService.class);
				Command cmd = cmdService.getCommand(SavePathsXls.ID);
				IEvaluationService evalService = (IEvaluationService) PlatformUI.getWorkbench().getService(IEvaluationService.class);
				IEvaluationContext ctx = evalService.getCurrentState();
				ExecutionEvent event = new ExecutionEvent(cmd, parameterValues.getParameterValues(), feedbackLoops, ctx);
				try {
					close();
					cmd.executeWithChecks(event);
					
				} catch (ExecutionException e1) {
					e1.printStackTrace();
				} catch (NotDefinedException e1) {
					e1.printStackTrace();
				} catch (NotEnabledException e1) {
					e1.printStackTrace();
				} catch (NotHandledException e1) {
					e1.printStackTrace();
				}
			}
			
		});
	}

	protected Button createButton(Composite parent, String label,
			boolean defaultButton, SelectionListener selectionListener) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.addSelectionListener(selectionListener);
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		//setButtonLayoutData(button);
		return button;
	}

	/*protected void setButtonLayoutData(Button button) {
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		data.widthHint = Math.max(widthHint, minSize.x);
		button.setLayoutData(data);
	}*/
	
	private Control createWindowArea(Composite p) {
		final ScrolledComposite sc = new ScrolledComposite(p, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		sc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		Composite c = new Composite(sc, SWT.NONE);
		c.setLayout(new GridLayout(1, true));


		createTable(c);
		createProviders();
		comparator = new PathsViewerComparator();
		viewer.setComparator(comparator);


		sc.setContent(c);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		return p;
	}

	public void setFeedbackLoops(ArrayList<Path> loops) {
		this.feedbackLoops = loops;
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
