/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.chart;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.experimental.chart.swt.ChartComposite;

import ch.uzh.business.systemanalyse.tool.Activator;
import ch.uzh.business.systemanalyse.tool.model.IModelChangeListener;
import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.plot.CustomBubbleRenderer;

/**
 * An abstract view containing a graph. In order to create a concrete chart
 * view, extend it and
 * 
 * @author Krzysztof Dabkowski
 * 
 */
public abstract class AbstractChartView extends ViewPart implements
		IModelChangeListener {

	public static final String ID = "";
	private static final String ANNOTATION_SEPARATOR = ", ";

	protected static double[] xVal;
	protected static double[] yVal;
	protected static double[] zVal;
	protected static ArrayList<DataElement> dataElements;
	boolean tooManyPaths;
	Font backgroundAnnotationFont = new Font("SansSerifBig", Font.PLAIN, 50);

	private Composite chartComposite;
	private final ArrayList<XYTextAnnotation> bubbleAnnotations;
	private XYBubbleRenderer renderer;
	private JFreeChart chart;
	XYPlot plot;
	DefaultXYZDataset data;

	IPropertyChangeListener preferenceListener = new IPropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			String p = event.getProperty();
			if (p.equals("TICK_FONT_STYLE") || p.equals("TICK_FONT_SIZE")
					|| p.equals("LABEL_STYLE") || p.equals("LABEL_FONT_SIZE")) {
				setAxisFonts();
			}

			/*
			 * if(p.equals(getPartNameProperty())) { if(p) dispose(); }
			 */
		}
	};

	/**
	 * 
	 */
	public AbstractChartView() {
		super();
		Activator.getDefault().getPreferenceStore()
				.addPropertyChangeListener(preferenceListener);
		dataElements = new ArrayList<DataElement>();
		setData();
		ModelProvider.INSTANCE.registerListener(this);
		bubbleAnnotations = new ArrayList<XYTextAnnotation>();
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
		if (setData()) {
			setRanges(plot);
			refresh();
			chartComposite.redraw();
			createBackgroundAnnotations(renderer, plot);
			drawBubbleAnnotations(renderer, plot);
			setRanges(plot);
		}
		doTooManyPaths();
	}

	/**
	 * if the number of points to be drawn exceeds the limit the tooManyPaths
	 * variable is set to true and this method draws an annotation instead of
	 * the proper graph.
	 */
	private void doTooManyPaths() {
		if (tooManyPaths) {
			System.out.println("Too many paths");
			double y = plot.getRangeAxis().getRange().getCentralValue();
			double x = plot.getDomainAxis().getRange().getCentralValue();
			XYTextAnnotation errorAnnotation = new XYTextAnnotation(
					"Too many points", x, y);
			// Font backgroundAnnotationFont = new Font("SansSerifBig",
			// Font.PLAIN, 50);
			errorAnnotation.setFont(backgroundAnnotationFont);
			errorAnnotation.setPaint(Color.BLACK);
			renderer.addAnnotation(errorAnnotation);
		}
	}

	private class DataElement {
		double x;
		double y;
		double z;
		StringBuilder annotation;

		DataElement(double x, double y, double z, String annotation) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.annotation = new StringBuilder(annotation);
		}

		void appendAnnotation(String a) {
			this.annotation.append(ANNOTATION_SEPARATOR).append(a);// =
																	// this.annotation
																	// + ", " +
			// a;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (obj == null || obj.getClass() != this.getClass()) {
				return false;
			}

			DataElement element = (DataElement) obj;
			if (this.x == element.x && this.y == element.y) {
				return true;
			}
			return false;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (((Double) this.x).hashCode());
			result = prime * result + (((Double) this.y).hashCode());
			return result;
		}

	}

	private boolean setData() {
		try {
			setDataSources();
		} catch (NullPointerException np) {
			System.out.println("No Data to display.");
		}

		if (xVal != null && xVal.length != 0) {
			if (xVal.length > 50) {
				tooManyPaths = true;
			} else {
				tooManyPaths = false;

				ArrayList<DataElement> elements = new ArrayList<DataElement>();
				for (int i = 0; i < xVal.length; i++) {
					elements.add(new DataElement(xVal[i], yVal[i], zVal[i],
							getElementAnnotation(i)));
				}

				ArrayList<DataElement> newElements = findRepetitions1(elements);

				xVal = new double[newElements.size()];
				yVal = new double[newElements.size()];
				zVal = new double[newElements.size()];

				int counter = 0;
				for (DataElement element : newElements) {
					xVal[counter] = element.x;
					yVal[counter] = element.y;
					// System.out.println(element.z);
					zVal[counter] = element.z;
					counter++;
				}
				dataElements.clear();
				dataElements.addAll(newElements);
				return true;
			}
		}
		xVal = new double[0];
		yVal = new double[0];
		zVal = new double[0];
		return false;
	}

	private ArrayList<DataElement> findRepetitions(
			ArrayList<DataElement> elements) {
		int coun = 1;
		int compare1 = 0;

		ArrayList<DataElement> newElements = new ArrayList<AbstractChartView.DataElement>();
		for (DataElement element : elements) {
			double repetitions = element.z;
			int compare2 = 0;
			for (DataElement otherElement : elements) {
				System.out.println(coun++);
				if (element != otherElement) {
					// System.out.println("comparing " + compare1
					// + " with " + compare2);
					if (element.x == otherElement.x
							&& element.y == otherElement.y) {
						// System.out.println("and they are the same...");
						repetitions++;
						// if (compare2 == 16 && compare1 == 15) {
						// System.out.println();
						// }
						element.appendAnnotation(otherElement.annotation
								.toString());
						// System.out.println(element.annotation);
						// System.out.println("			"
						// + Runtime.getRuntime().totalMemory());

						// System.out.println("found repetition in "
						// + element.x + " " + element.y);
					}
				}
				compare2++;
			}

			compare1++;
			System.out.println(Runtime.getRuntime().totalMemory());
			if (compare1 == 16) {
				System.out.println();
			}
			element.z = repetitions;
			boolean exists = false;
			for (DataElement newElementsElement : newElements) {
				if (newElementsElement.x == element.x
						&& newElementsElement.y == element.y) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				newElements.add(element);
			}
		}
		return newElements;
	}

	private ArrayList<DataElement> findRepetitions1(
			ArrayList<DataElement> elements) {

		ArrayList<DataElement> newElements = new ArrayList<AbstractChartView.DataElement>();

		for (DataElement element : elements) {
			int index = newElements.indexOf(element);
			if (index != -1) {
				DataElement existingElement = newElements.get(index);
				existingElement.appendAnnotation(element.annotation.toString());
				existingElement.z++;
			} else {
				newElements.add(element);
			}
		}
		return newElements;
	}

	protected abstract void setDataSources();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		data = new DefaultXYZDataset();
		if (xVal != null) {
			data.addSeries("1", new double[][] { xVal, yVal, zVal });
		}
		chart = createChart(data);
		chartComposite = new ChartComposite(parent, SWT.NONE, chart);

		ModelProvider.INSTANCE.registerListener(this);
		doTooManyPaths();
	}

	public JFreeChart getChart() {
		return chart;
	}

	public Point getSize() {
		return chartComposite.getSize();
	}

	public void refresh() {
		// chartComposite.redraw();
		data.removeSeries("1");
		data.addSeries("1", new double[][] { xVal, yVal, zVal });
		RGB rgb = PreferenceConverter.getColor(Activator.getDefault()
				.getPreferenceStore(), "CHART_BUBBLE");
		renderer.setSeriesPaint(0, new Color(rgb.red, rgb.green, rgb.blue));
	}

	private JFreeChart createChart(XYZDataset dataset) {
		JFreeChart chart = ChartFactory.createBubbleChart(getChartTitle(),
				getXAxisLabel(), getYAxisLabel(), dataset,
				PlotOrientation.HORIZONTAL, false, true, false);
		plot = (XYPlot) chart.getPlot();
		plot.setForegroundAlpha(0.65f);

		renderer = new CustomBubbleRenderer();
		RGB rgb = PreferenceConverter.getColor(Activator.getDefault()
				.getPreferenceStore(), "CHART_BUBBLE");
		renderer.setSeriesPaint(0, new Color(rgb.red, rgb.green, rgb.blue));

		setRanges(plot);

		createBackgroundAnnotations(renderer, plot);
		drawBubbleAnnotations(renderer, plot);
		plot.setRenderer(renderer);
		setAxisFonts();
		// increase the margins to account for the fact that the auto-range
		// doesn't take into account the bubble size...
		setAxesInversion(plot.getRangeAxis(), plot.getDomainAxis());
		return chart;
	}

	private void setAxisFonts() {
		NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		int tickFontSize = store.getInt("TICK_FONT_SIZE");
		String tickFontStyleString = store.getString("TICK_FONT_STYLE");
		Font tickFont;
		if (tickFontStyleString == "bold") {
			tickFont = new Font("tickfont", Font.BOLD, tickFontSize);
		} else if (tickFontStyleString == "italic") {
			tickFont = new Font("tickfont", Font.ITALIC, tickFontSize);
		} else {
			tickFont = new Font("tickfont", Font.PLAIN, tickFontSize);
		}

		int labelFontSize = store.getInt("LABEL_FONT_SIZE");
		String labelFontStyleString = store.getString("LABEL_STYLE");
		Font labelFont;
		if (labelFontStyleString == "bold") {
			labelFont = new Font("tickfont", Font.BOLD, labelFontSize);
		} else if (labelFontStyleString == "italic") {
			labelFont = new Font("tickfont", Font.ITALIC, labelFontSize);
		} else {
			labelFont = new Font("tickfont", Font.PLAIN, labelFontSize);
		}
		domainAxis.setTickLabelFont(tickFont);
		domainAxis.setLabelFont(labelFont);
		rangeAxis.setTickLabelFont(tickFont);
		rangeAxis.setLabelFont(labelFont);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {

	}

	/**
	 * This method sets ranges so that all the bubbles are visible and possibly
	 * include 0 of the X or Y axis and if needed sets the zero in the middle.
	 * Uses methods: yZeroInclude() xZeroInclude() makeYzeroMiddle()
	 * 
	 * @param plot
	 *            The plot, for which the method sets the ranges.
	 */
	private void setRanges(XYPlot plot) {
		// make sure the xVal array is not null
		// that means there are some points to render
		if (xVal != null) {
			// set minY to the highest possible value
			double minY = Double.POSITIVE_INFINITY;
			// set maxY to the lowest possible value
			double maxY = Double.NEGATIVE_INFINITY;

			// iterate over all the yVal elements
			// to find the maximal and minimal value
			// of y
			for (double x : xVal) {
				if (x < minY) {
					minY = x;
				}
				if (x > maxY) {
					maxY = x;
				}
			}

			// the min and max values were set to
			// "reasonable" values - not infinities
			// In this if we set range for Y axis
			if (minY != Double.POSITIVE_INFINITY
					&& maxY != Double.NEGATIVE_INFINITY) {

				// if the Zero on Y axis is needed in the middle of the plot
				if (makeYZeroMiddle()) {
					// find which distance is longer: from 0 to minY
					// or from 0 to maxY and set the longer on both sides

					// if minY to 0 is bigger than maxY to 0
					if (Math.abs(minY) >= Math.abs(maxY)) {
						if (minY < 0) {
							maxY = -minY;
						} else {
							// shouldn't happen, because it would mean
							// that minY > maxY..
							System.out.println("shouldn't happen");
						}
					} else {
						minY = -maxY;
					}
				} else {
					// do not make y = 0 in the middle
					if (yZeroInclude()) {
						if (plot.getDomainAxis().isInverted()) {
							maxY = 0d;
						} else {
							minY = 0d;
						}
					}
				}

				// rangeX is the distance between maxX and minX
				double rangeY = maxY - minY;

				minY -= rangeY * 0.1;
				maxY += rangeY * 0.1;
				if ((maxY - minY) > 0) {
					plot.getDomainAxis().setRange(new Range(minY, maxY));
				} else {
					System.out.println("Range of length " + (maxY - minY)
							+ " cannot be set.");
				}
			}

			double minX = Double.POSITIVE_INFINITY;
			double maxX = Double.NEGATIVE_INFINITY;
			for (double y : yVal) {
				if (y < minX) {
					minX = y;
				}
				if (y > maxX) {
					maxX = y;
				}
			}
			if (minX != Double.POSITIVE_INFINITY
					&& maxX != Double.NEGATIVE_INFINITY) {
				if (xZeroInclude()) {
					if (plot.getDomainAxis().isInverted()) {
						maxX = 0d;
					} else {
						minX = 0d;
					}
				}
				double rangeX = maxX - minX;

				minX -= rangeX * 0.1;
				maxX += rangeX * 0.1;

				if ((maxX - minX) > 0) {
					plot.getRangeAxis().setRange(new Range(minX, maxX));
				} else {
					System.out.println("Range of length " + (maxX - minX)
							+ " cannot be set.");
				}
			}

		}
		// System.out.println("range: " +
		// plot.getRangeAxis().getRange().getLowerBound() + " - " +
		// plot.getRangeAxis().getRange().getUpperBound());
		// System.out.println("range: " +
		// plot.getDomainAxis().getRange().getLowerBound() + " - " +
		// plot.getDomainAxis().getRange().getUpperBound());
	}

	private void drawBubbleAnnotations(XYItemRenderer renderer, XYPlot plot) {
		if (xVal != null) {
			for (XYTextAnnotation oldAnnotation : bubbleAnnotations) {
				renderer.removeAnnotation(oldAnnotation);
			}
			bubbleAnnotations.clear();

			Font bubbleAnnotationFont = new Font("SansSerif", Font.BOLD, 16);
			// renderer.setsca
			for (int i = 0; i < xVal.length; i++) {
				/*
				 * XYTextAnnotation annotation = new
				 * XYTextAnnotation(String.valueOf
				 * (ModelProvider.INSTANCE.getVariables().get(i).getIndex()),
				 * xVal[i], yVal[i]);
				 */
				DataElement element = dataElements.get(i);
				XYTextAnnotation annotation = new XYTextAnnotation(
						element.annotation.toString(), element.x, element.y);
				bubbleAnnotations.add(annotation);
				annotation.setFont(bubbleAnnotationFont);
				renderer.addAnnotation(annotation);
			}
		}
	}

	private void createBackgroundAnnotations(XYItemRenderer renderer,
			XYPlot plot) {
		renderer.removeAnnotations();
		// Font backgroundAnnotationFont = new Font("SansSerifBig", Font.PLAIN,
		// 50);
		Range rangeRange = plot.getRangeAxis().getRange();
		// System.out.println("range: " + rangeRange.getLowerBound() + " to " +
		// rangeRange.getUpperBound());
		Range domainRange = plot.getDomainAxis().getRange();
		// System.out.println("domain: " + domainRange.getLowerBound() + " to "
		// + domainRange.getUpperBound());
		double rangeRangeQuarter = (rangeRange.getUpperBound() - rangeRange
				.getLowerBound()) / 4;
		double domainRangeQuarter = (domainRange.getUpperBound() - domainRange
				.getLowerBound()) / 4;
		XYTextAnnotation backgroundAnnotationLeftBottom = new XYTextAnnotation(
				getBackgroundAnnotationText(0), domainRange.getLowerBound()
						+ domainRangeQuarter, rangeRange.getLowerBound()
						+ rangeRangeQuarter);
		backgroundAnnotationLeftBottom.setFont(backgroundAnnotationFont);
		backgroundAnnotationLeftBottom.setPaint(Color.GRAY);
		renderer.addAnnotation(backgroundAnnotationLeftBottom);

		XYTextAnnotation backgroundAnnotationLeftTop = new XYTextAnnotation(
				getBackgroundAnnotationText(1), domainRange.getLowerBound()
						+ domainRangeQuarter * 3, rangeRange.getLowerBound()
						+ rangeRangeQuarter);
		backgroundAnnotationLeftTop.setFont(backgroundAnnotationFont);
		backgroundAnnotationLeftTop.setPaint(Color.GRAY);
		renderer.addAnnotation(backgroundAnnotationLeftTop);

		XYTextAnnotation backgroundAnnotationRightTop = new XYTextAnnotation(
				getBackgroundAnnotationText(2), domainRange.getLowerBound()
						+ domainRangeQuarter * 3, rangeRange.getLowerBound()
						+ rangeRangeQuarter * 3);
		backgroundAnnotationRightTop.setFont(backgroundAnnotationFont);
		backgroundAnnotationRightTop.setPaint(Color.GRAY);
		renderer.addAnnotation(backgroundAnnotationRightTop);

		XYTextAnnotation backgroundAnnotationRightBottom = new XYTextAnnotation(
				getBackgroundAnnotationText(3), domainRange.getLowerBound()
						+ domainRangeQuarter, rangeRange.getLowerBound()
						+ rangeRangeQuarter * 3);
		backgroundAnnotationRightBottom.setFont(backgroundAnnotationFont);
		backgroundAnnotationRightBottom.setPaint(Color.GRAY);
		renderer.addAnnotation(backgroundAnnotationRightBottom);

	}

	abstract protected String getBackgroundAnnotationText(int i);

	protected void setZValues() {
		// int numberOfVars = ModelProvider.INSTANCE.getVariables().size();
		int numberOfVars = yVal.length;
		zVal = new double[numberOfVars];
		for (int i = 0; i < numberOfVars; i++) {
			// if (Double.isNaN(xVal[i]) || Double.isNaN(yVal[i])) {
			// xVal[i] = 1;
			// yVal[i] = 1;
			// zVal[i] = 0;
			// } else {
			zVal[i] = 1;
			// }
		}
	}

	abstract protected String getElementAnnotation(int index);

	abstract protected String getChartTitle();

	abstract protected String getXAxisLabel();

	abstract protected String getYAxisLabel();

	abstract protected void setAxesInversion(ValueAxis xAxis, ValueAxis yAxis);

	abstract protected boolean xZeroInclude();

	abstract protected boolean yZeroInclude();

	abstract protected boolean makeYZeroMiddle();

	@Override
	public void dispose() {
		super.dispose();
		ModelProvider.INSTANCE.unregisterListener(this);
	}

	// abstract protected String getPartNameProperty();
}
