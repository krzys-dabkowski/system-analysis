/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.policyonoff;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import ch.uzh.business.systemanalyse.tool.model.IModelChangeListener;
import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.Variable;

/**
 * @author Krzysztof Dabkowski
 * 
 */
public class PolicyOnOffView extends ViewPart implements IModelChangeListener {

	public final static String ID = "ch.uzh.business.systemanalyse.tool.views.policyonoff";

	private Layout parentLayout;

	private Composite mainComposite;

	private final ArrayList<Combo> offCombos = new ArrayList<Combo>();

	private final ArrayList<PolicyComposite> policies = new ArrayList<PolicyComposite>();

	private boolean calculateAutomatic = false;

	/**
	 * 
	 */
	public PolicyOnOffView() {
		ModelProvider.INSTANCE.registerListener(this);
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
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		sc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		mainComposite = new Composite(sc, SWT.NONE);
		parentLayout = new GridLayout();
		mainComposite.setLayout(parentLayout);

		createPolicyComposite(mainComposite);
		sc.setContent(mainComposite);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private void updateView() {
		// if(calculateAutomatic) {
		reset();
		for (PolicyComposite pc : policies) {
			if (pc.getComposite().isVisible()) {
				pc.initFinder();
				pc.setTexts();
			}
		}
		// }

		/*
		 * for(Combo combo : offCombos) { combo.setItems(getComboItems()); }
		 */
	}

	private void createPolicyComposite(Composite parent) {
		((GridLayout) parentLayout).numColumns = 2;

		policies.add(createComposite(true));// , sumText, positiveText,
											// negativeText, shortestText,
											// longestText);
		policies.add(createComposite(true));
		policies.add(createComposite(true));
		policies.add(createComposite(true));
		policies.add(createComposite(true));
		policies.add(createComposite(true));
		policies.add(createComposite(true));
		policies.add(createComposite(false));
		policies.get(1).getComposite().setVisible(false);
		policies.get(2).getComposite().setVisible(false);
		policies.get(3).getComposite().setVisible(false);
		policies.get(4).getComposite().setVisible(false);
		policies.get(5).getComposite().setVisible(false);
		policies.get(6).getComposite().setVisible(false);
		policies.get(7).getComposite().setVisible(false);

		updateView();
	}

	private PolicyComposite createComposite(boolean extendable
	// , Text sumText, Text positiveText, Text negativeText, Text shortestText,
	// Text longestText
	) {

		final PolicyComposite pc = new PolicyComposite();

		pc.setComposite(new Composite(mainComposite, SWT.NONE));
		GridLayout gridLayout = new GridLayout();
		pc.getComposite().setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true,
				false);
		gridData.horizontalSpan = 1;
		pc.getComposite().setLayoutData(gridData);

		Composite labelTextComposite = new Group(pc.getComposite(), SWT.NONE);
		GridLayout ltLayout = new GridLayout();
		ltLayout.numColumns = 2;
		GridData ltData = new GridData(GridData.FILL, GridData.CENTER, true,
				false);
		labelTextComposite.setLayout(ltLayout);
		labelTextComposite.setLayoutData(ltData);

		pc.setHeadline(new Label(labelTextComposite, SWT.NONE));

		if (pc.getIndex() == 0) {
			pc.setTitle("ON Analysis");
		} else {
			StringBuilder excludedString = new StringBuilder();
			for (Integer i : pc.getVariableIndices()) {
				excludedString.append(i.toString()).append(" ");
			}
			pc.setTitle("OFF Analysis" + (pc.getIndex()));
		}
		pc.getHeadline().setText(pc.getTitle());
		GridData headlineData = new GridData();
		// headlineData.grabExcessHorizontalSpace = true;
		headlineData.horizontalSpan = 2;
		headlineData.horizontalSpan = GridData.FILL;
		pc.getHeadline().setLayoutData(headlineData);

		Label summaryLabel = new Label(labelTextComposite, SWT.NONE);
		summaryLabel.setText("Summary:");
		GridData summaryLayoutData = new GridData();
		summaryLayoutData.horizontalSpan = 2;
		summaryLabel.setLayoutData(summaryLayoutData);

		Label sumLabel = new Label(labelTextComposite, SWT.NONE);
		sumLabel.setText("Amount of feedback loops");
		pc.setSumText(new Text(labelTextComposite, SWT.SINGLE | SWT.BORDER
				| SWT.READ_ONLY));
		pc.getSumText().setLayoutData(
				new GridData(GridData.FILL, GridData.CENTER, true, false));
		pc.getSumText().addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (pc.getStartButton() != null
						&& !pc.getStartButton().isDisposed()) {
					if (pc.getSumText().getText().equals("")
							|| pc.getSumText().getText().equals("0")) {
						pc.getStartButton().setEnabled(false);
					} else {
						pc.getStartButton().setEnabled(true);
					}
				}
			}
		});

		Label positiveLabel = new Label(labelTextComposite, SWT.NONE);
		positiveLabel.setText("Amount of positive feedback loops");
		pc.setPositiveText(new Text(labelTextComposite, SWT.SINGLE | SWT.BORDER
				| SWT.READ_ONLY));
		pc.getPositiveText().setLayoutData(
				new GridData(GridData.FILL, GridData.CENTER, true, false));

		Label negativeLabel = new Label(labelTextComposite, SWT.NONE);
		negativeLabel.setText("Amount of negative feedback loops");
		pc.setNegativeText(new Text(labelTextComposite, SWT.SINGLE | SWT.BORDER
				| SWT.READ_ONLY));
		pc.getNegativeText().setLayoutData(
				new GridData(GridData.FILL, GridData.CENTER, true, false));

		Label shortestLabel = new Label(labelTextComposite, SWT.NONE);
		shortestLabel.setText("Shortest path");
		pc.setShortestText(new Text(labelTextComposite, SWT.SINGLE | SWT.BORDER
				| SWT.READ_ONLY));
		pc.getShortestText().setLayoutData(
				new GridData(GridData.FILL, GridData.CENTER, true, false));

		Label longestLabel = new Label(labelTextComposite, SWT.NONE);
		longestLabel.setText("Longest Path");
		pc.setLongestText(new Text(labelTextComposite, SWT.SINGLE | SWT.BORDER
				| SWT.READ_ONLY));
		pc.getLongestText().setLayoutData(
				new GridData(GridData.FILL, GridData.CENTER, true, false));

		Composite buttonsComposite = new Composite(labelTextComposite, SWT.NONE);
		buttonsComposite.setLayout(new GridLayout(3, false));
		GridData buttonsData = new GridData();
		buttonsData.horizontalSpan = 2;
		buttonsComposite.setLayoutData(buttonsData);

		Button showPathsButton = new Button(buttonsComposite, SWT.PUSH);
		showPathsButton.setText("Show all paths");
		GridData showPathsData = new GridData();
		// showPathsData.horizontalSpan = 2;
		showPathsButton.setLayoutData(showPathsData);
		showPathsButton
				.addSelectionListener(pc.getShowPathsSelectionListener());

		if (pc.getIndex() == 0) {
			final Button calculateButton = new Button(buttonsComposite,
					SWT.PUSH);
			calculateButton.setText("Refresh");
			calculateButton.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					reset();
					/*
					 * pc.initFinder(); pc.setTexts();
					 */
					updateView();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {

				}
			});

			Button automatic = new Button(buttonsComposite, SWT.CHECK);
			automatic.setText("Refresh automatically");
			automatic.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					boolean auto = ((Button) e.widget).getSelection();
					calculateButton.setEnabled(!auto);
					calculateAutomatic = auto;
					updateView();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {

				}
			});

			/*
			 * pc.initFinder(); pc.setTexts();
			 */
		}

		if (extendable) {
			Composite startComposite = new Group(pc.getComposite(), SWT.NONE);
			GridLayout startLayout = new GridLayout();
			startLayout.numColumns = 2;
			GridData startData = new GridData(GridData.FILL, GridData.CENTER,
					false, false);
			startComposite.setLayout(startLayout);
			startComposite.setLayoutData(startData);
			final Combo offCombo = new Combo(startComposite, SWT.NONE);
			offCombos.add(offCombo);

			/*
			 * int count = 0; String [] endComboItems = new
			 * String[ModelProvider.INSTANCE.getVariables().size()];
			 * for(Variable v : ModelProvider.INSTANCE.getVariables()) {
			 * endComboItems [count] = String.valueOf(v.getIndex()); count++; }
			 */
			offCombo.setItems(getComboItems());
			Button start = new Button(startComposite, SWT.PUSH);
			start.setText("Start OFF analysis");
			start.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					PolicyComposite toShow = policies.get(pc.getIndex() + 1);

					// CalculatorRunnable calc = new
					// CalculatorRunnable("Thread-"
					// + pc.getIndex(), pc, offCombo);
					// StopCalculationWindow window = new StopCalculationWindow(
					// Display.getCurrent().getActiveShell());
					// window.create();
					// window.open();
					//
					// Display.getDefault().syncExec(calc);
					// Display.getDefault().
					for (PolicyComposite further : policies) {
						if (further.getIndex() > pc.getIndex()) {
							if (offCombo.getSelectionIndex() != -1) {
								further.setVariableIndex(Integer
										.valueOf(offCombo.getItem(offCombo
												.getSelectionIndex())), pc
										.getVariableIndices());
								further.setTexts();
							}

						}
					}
					toShow.getComposite().setVisible(true);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
			start.setEnabled(false);
			pc.setStartButton(start);

			if (pc.getIndex() == 0) {
				startLayout.numColumns = 3;
				Button reset = new Button(startComposite, SWT.PUSH);
				reset.setText("Reset the analysis");
				reset.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						/*
						 * for (PolicyComposite toClose : policies) {
						 * if(toClose.getIndex() != 0) {
						 * toClose.getComposite().setVisible(false); } }
						 */
						reset();
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});
			}
		}

		return pc;
	}

	private class CalculatorRunnable implements Runnable {

		private Thread t;
		private final String threadName;
		private final PolicyComposite pc;
		private final Combo offCombo;

		CalculatorRunnable(String name, PolicyComposite pc, Combo offCombo) {
			this.threadName = name;
			this.pc = pc;
			this.offCombo = offCombo;
			System.out.println("Creating " + threadName);
		}

		@Override
		public void run() {
			System.out.println("Running " + threadName);

			for (PolicyComposite further : policies) {
				if (further.getIndex() > pc.getIndex()) {
					if (offCombo.getSelectionIndex() != -1) {
						further.setVariableIndex(Integer.valueOf(offCombo
								.getItem(offCombo.getSelectionIndex())), pc
								.getVariableIndices());
						further.setTexts();
					}

				}
			}
		}

		public void start() {
			System.out.println("Starting " + threadName);
			if (t == null) {
				t = new Thread(this, threadName);
				t.start();
			}
		}
	}

	private void reset() {
		for (PolicyComposite toClose : policies) {
			if (toClose.getIndex() != 0) {
				toClose.getComposite().setVisible(false);
			}
		}
	}

	private String[] getComboItems() {
		int count = 0;
		String[] endComboItems = new String[ModelProvider.INSTANCE
				.getVariables().size()];
		for (Variable v : ModelProvider.INSTANCE.getVariables()) {
			endComboItems[count] = String.valueOf(v.getIndex());
			count++;
		}
		if (endComboItems.length == 0) {
			endComboItems = new String[1];
			endComboItems[0] = "  ";
		}
		return endComboItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {

	}

	@Override
	public void fireModelChange(boolean clear) {
		// updateView();
		if (calculateAutomatic) {
			reset();
			for (PolicyComposite pc : policies) {
				if (pc.getComposite().isVisible()) {
					pc.initFinder();
					pc.setTexts();
				}
			}
		}

		for (Combo combo : offCombos) {
			combo.setItems(getComboItems());
		}
	}

	public ArrayList<PolicyComposite> getPolicies() {
		return policies;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		ModelProvider.INSTANCE.unregisterListener(this);
	}
}
