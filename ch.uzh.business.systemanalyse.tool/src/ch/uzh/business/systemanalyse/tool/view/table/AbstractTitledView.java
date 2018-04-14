package ch.uzh.business.systemanalyse.tool.view.table;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.ViewPart;

/**
 * An abstract view, which has a title bar at the top.
 * 
 * @author krzysztof.dabkowski
 *
 */
public abstract class AbstractTitledView extends ViewPart {

	Canvas canvas;
	Font font;
	TextLayout layout;
	
	
	public AbstractTitledView() {
		super();
	}

	protected void createTitle(Composite parent, int numberOfParentColumns) {
		
		canvas = new Canvas(parent, SWT.BORDER);
		GridData titleGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		titleGridData.horizontalSpan = numberOfParentColumns;
		canvas.setLayoutData(titleGridData);
		Display display = Display.getCurrent();
		
		font = new Font(display, "Tahoma", 14, SWT.BOLD);
		
		
		Color black = display.getSystemColor(SWT.COLOR_BLACK);
		Color bckgr = parent.getBackground();
		
		layout = new TextLayout(display);
		TextStyle style = new TextStyle(font, black, bckgr);
		
		layout.setText(getTitle());//"Test title for this view");
		layout.setStyle(style, 0, getTitle().length());
		
		canvas.addListener(SWT.Paint, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				layout.draw(event.gc, 10, 10);
			}
		});
	}

	@Override
	public void dispose() {
		font.dispose();
		layout.dispose();
		super.dispose();
	}

}