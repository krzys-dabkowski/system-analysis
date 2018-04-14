/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.welcome;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

import ch.uzh.business.systemanalyse.tool.Activator;
import ch.uzh.business.systemanalyse.tool.commands.ShowWelcomeWizard;
import ch.uzh.business.systemanalyse.tool.view.chart.AsPdChartView;
import ch.uzh.business.systemanalyse.tool.view.chart.AsPsChartView;
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

/**
 * This is a welcome view, which is shown after the start of the application.
 * 
 * @author Krzysztof Dabkowski
 * 
 */
public class WelcomeView extends ViewPart {

	public final static String ID = "ch.uzh.business.systemanalyse.tool.views.welcomeview";

	private class ButtonSelectionListener implements SelectionListener {

		private String viewId;

		ButtonSelectionListener(String id) {
			viewId = id;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().showView(viewId);
			} catch (PartInitException e1) {
				e1.printStackTrace();
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {

		}

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
		GridLayout layout = new GridLayout(2, false);
		// layout.numColumns = 2;
		parent.setLayout(layout);
		parent.setBackground(PlatformUI.getWorkbench().getDisplay()
				.getSystemColor(SWT.COLOR_LIST_SELECTION));

		Color colorWhite = PlatformUI.getWorkbench().getDisplay()
				.getSystemColor(SWT.COLOR_WHITE);

		// Create a composite with the white background
		Composite canvasComposite = new Composite(parent, SWT.NONE);
		canvasComposite.setLayout(new GridLayout(1, false));
		canvasComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		canvasComposite.setBackground(colorWhite);

		/*
		 * Composite logoComposite = new Composite(canvasComposite, SWT.NONE);
		 * logoComposite.setLayout(new GridLayout(1, false));
		 * logoComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
		 * true)); logoComposite.setBackground(colorWhite);
		 */

		// Add a canvas with an embedded uzh logo
		Canvas canvasUzh = new Canvas(canvasComposite, SWT.NONE);
		canvasUzh.setBackground(colorWhite);
		canvasUzh.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		ImageDescriptor uzhDescriptor = Activator
				.getImageDescriptor("icons/uzh_logo.jpg");
		final Image uzhLogo = uzhDescriptor.createImage();
		canvasUzh.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				if (uzhLogo != null) {
					e.gc.drawImage(uzhLogo, 0, 0);
				}
			}
		});

		// Add a canvas with an embedded mba program logo
		Canvas canvasMba = new Canvas(canvasComposite, SWT.NONE);
		canvasMba.setBackground(colorWhite);
		canvasMba.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		ImageDescriptor mbaDesc = Activator
				.getImageDescriptor("icons/mba_logo.png");
		final Image mbaLogo = mbaDesc.createImage();
		// final Image logo = new Image(PlatformUI.getWorkbench().getDisplay(),
		// logoDesc.getImageData());
		canvasMba.addPaintListener(new PaintListener() {
			public void paintControl(final PaintEvent event) {
				if (mbaLogo != null) {
					event.gc.drawImage(mbaLogo, 0, 0);
				}
			}
		});

		// Add a canvas with an embedded welcome text
		String welcomeText = "Welcome to System Analysis Tool";
		Canvas textCanvas = new Canvas(canvasComposite, SWT.NONE);
		textCanvas.setBackground(colorWhite);
		textCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Display display = Display.getCurrent();
		Font font = new Font(display, "Tahoma1", 36, SWT.BOLD);
		Color black = display.getSystemColor(SWT.COLOR_BLACK);
		final TextLayout textLayout = new TextLayout(display);
		TextStyle style = new TextStyle(font, black, colorWhite);
		textLayout.setText(welcomeText);
		textLayout.setStyle(style, 0, welcomeText.length());
		textCanvas.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				textLayout.draw(e.gc, 10, 10);
			}
		});

		Button startButton = new Button(canvasComposite, SWT.NONE);
		startButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				true));
		startButton.setText("Quick Start");
		startButton.setImage(display.getSystemImage(SWT.ICON_INFORMATION));
		// startButton.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));
		startButton.setFont(new Font(PlatformUI.getWorkbench().getDisplay(),
				new FontData("startButton font", 20, SWT.BOLD)));
		startButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IHandlerService handlerService = (IHandlerService) getSite()
						.getService(IHandlerService.class);
				try {
					handlerService.executeCommand(ShowWelcomeWizard.ID, null);
				} catch (ExecutionException ex) {
					ex.printStackTrace();
				} catch (NotDefinedException ex) {
					ex.printStackTrace();
				} catch (NotEnabledException ex) {
					ex.printStackTrace();
				} catch (NotHandledException ex) {
					ex.printStackTrace();
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		// createButtons(parent);
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		// System.out.println("init");
		// IHandlerService handlerService =
		// (IHandlerService)getSite().getService(IHandlerService.class);
		// try {
		// handlerService.executeCommand(ShowWelcomeWizard.ID, null);
		// } catch (ExecutionException e) {
		// e.printStackTrace();
		// } catch (NotDefinedException e) {
		// e.printStackTrace();
		// } catch (NotEnabledException e) {
		// e.printStackTrace();
		// } catch (NotHandledException e) {
		// e.printStackTrace();
		// }

	}

	/**
	 * Creates buttons, which link to views.
	 * 
	 * @param parent
	 */
	private void createButtons(Composite parent) {

		Composite buttonComposite = new Composite(parent, SWT.NULL);

		Layout filllayout = new FillLayout(SWT.VERTICAL);
		buttonComposite.setLayout(filllayout);
		buttonComposite.setLayoutData(new GridData(GridData.END, GridData.FILL,
				false, true));

		Composite row1 = new Composite(buttonComposite, SWT.NULL);
		FillLayout layout1 = new FillLayout();
		row1.setLayout(layout1);
		Button button1 = new Button(row1, SWT.PUSH);
		button1.setText("Cross-Impact Matrix");
		button1.addSelectionListener(new ButtonSelectionListener(
				ImpactTableView.ID));
		Button button3 = new Button(row1, SWT.PUSH);
		button3.setText("Cross-Time Matrix");
		button3.addSelectionListener(new ButtonSelectionListener(
				DelayTableView.ID));

		Composite row2 = new Composite(buttonComposite, SWT.NULL);
		FillLayout layout2 = new FillLayout();
		row2.setLayout(layout2);
		Button button2 = new Button(row2, SWT.PUSH);
		button2.setText("AS-PS Chart");
		button2.addSelectionListener(new ButtonSelectionListener(
				AsPsChartView.ID));
		Button button4 = new Button(row2, SWT.PUSH);
		button4.setText("PD-RD Chart");
		button4.addSelectionListener(new ButtonSelectionListener(
				PdRdChartView.ID));

		Composite row3 = new Composite(buttonComposite, SWT.NULL);
		FillLayout layout3 = new FillLayout();
		row3.setLayout(layout3);
		row3.setBackground(PlatformUI.getWorkbench().getDisplay()
				.getSystemColor(SWT.COLOR_CYAN));
		row3.setForeground(PlatformUI.getWorkbench().getDisplay()
				.getSystemColor(SWT.COLOR_CYAN));
		Button button5 = new Button(row3, SWT.PUSH);
		button5.setText("AS-PD Chart");
		button5.addSelectionListener(new ButtonSelectionListener(
				AsPdChartView.ID));
		Button button6 = new Button(row3, SWT.PUSH);
		button6.setText("PS-RD Chart");

		Composite row4 = new Composite(buttonComposite, SWT.NULL);
		FillLayout layout4 = new FillLayout();
		row4.setLayout(layout4);
		button6.addSelectionListener(new ButtonSelectionListener(
				PsRdChartView.ID));
		Button button7 = new Button(row4, SWT.PUSH);
		button7.setText("Cross-Delay Matrix");
		button7.addSelectionListener(new ButtonSelectionListener(
				CrossDelayTableView.ID));
		Button button8 = new Button(row4, SWT.PUSH);
		button8.setText("Cross-Effect Matrix");
		button8.addSelectionListener(new ButtonSelectionListener(
				CrossEffectTableView.ID));

		Composite row6 = new Composite(buttonComposite, SWT.NULL);
		FillLayout layout6 = new FillLayout();
		row6.setLayout(layout6);
		Button button15 = new Button(row6, SWT.PUSH);
		button15.setText("Incoming influence");
		button15.addSelectionListener(new ButtonSelectionListener(
				IncomingInfluenceTableView.ID));
		Button button16 = new Button(row6, SWT.PUSH);
		button16.setText("Outgoing influence");
		button16.addSelectionListener(new ButtonSelectionListener(
				OutgoingInfluenceTableView.ID));

		Composite row7 = new Composite(buttonComposite, SWT.NULL);
		FillLayout layout7 = new FillLayout();
		row7.setLayout(layout7);
		Button button17 = new Button(row7, SWT.PUSH);
		button17.setText("Incoming influence Chart");
		button17.addSelectionListener(new ButtonSelectionListener(
				IncomingInfluenceChart.ID));
		Button button18 = new Button(row7, SWT.PUSH);
		button18.setText("Outgoing influence Chart");
		button18.addSelectionListener(new ButtonSelectionListener(
				OutgoingInfluenceChart.ID));

		Composite row5 = new Composite(buttonComposite, SWT.NULL);
		FillLayout layout5 = new FillLayout();
		row5.setLayout(layout5);
		Button button9 = new Button(row5, SWT.PUSH);
		button9.setText("Paths A to B");
		button9.addSelectionListener(new ButtonSelectionListener(
				NodeToNodeTable.ID));
		Button button13 = new Button(row5, SWT.PUSH);
		button13.setText("Paths to B");
		button13.addSelectionListener(new ButtonSelectionListener(
				ToNodeTable.ID));
		Button button13a = new Button(row5, SWT.PUSH);
		button13a.setText("Paths from B");
		button13a.addSelectionListener(new ButtonSelectionListener(
				FromNodeTable.ID));

		Composite row8 = new Composite(buttonComposite, SWT.NULL);
		FillLayout layout8 = new FillLayout();
		row8.setLayout(layout8);
		Button button10 = new Button(row8, SWT.PUSH);
		button10.setText("Paths A to B Chart");
		button10.addSelectionListener(new ButtonSelectionListener(
				NodeToNodeChart.ID));
		Button button14 = new Button(row8, SWT.PUSH);
		button14.setText("Paths to B Chart");
		button14.addSelectionListener(new ButtonSelectionListener(
				ToNodeChart.ID));

		Composite row9 = new Composite(buttonComposite, SWT.NULL);
		FillLayout layout9 = new FillLayout();
		row9.setLayout(layout9);
		Button button12 = new Button(row9, SWT.PUSH);
		button12.setText("Policy ON/OFF");
		button12.addSelectionListener(new ButtonSelectionListener(
				PolicyOnOffView.ID));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {

	}

}
