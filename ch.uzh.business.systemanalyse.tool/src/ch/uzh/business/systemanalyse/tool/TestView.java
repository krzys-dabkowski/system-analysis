package ch.uzh.business.systemanalyse.tool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.ViewPart;

/**
 * A view class for trying out things. It is declared as a view in plugin.xml.
 * To make it visible, just add it to the perspective.
 * 
 * @author krzysztof.dabkowski
 *
 */
public class TestView extends ViewPart {

	public final static String ID = "ch.uzh.business.systemanalyse.tool.view1";
	public TestView() {
		
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, true));
		final Canvas canvas = new Canvas(parent, SWT.NONE);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		Display display = Display.getCurrent();
		canvas.setSize(200, 200);
		
		//System.out.println("parent size: " + parent.g + " " + parent.getBounds().y);
		System.out.println("display size: " + display.getBounds().x + " " + display.getBounds().y);
		
		Font font1 = new Font(display, "Tahoma", 14, SWT.BOLD);
		
		
		Color black = display.getSystemColor(SWT.COLOR_BLACK);
		Color bckgr = parent.getBackground();
		
		final TextLayout layout = new TextLayout(display);
		TextStyle style = new TextStyle(font1, black, bckgr);
		
		layout.setText("English \u65E5\u672C\u8A9E  \u0627\u0644\u0639\u0631\u0628\u064A\u0651\u0629");
		layout.setStyle(style, 0, 13);
		
		canvas.addListener(SWT.Paint, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				layout.draw(event.gc, 10, 10);
			}
		});
		
		Button button = new Button(parent, SWT.PUSH);
		button.setText("OK");
	}

	@Override
	public void setFocus() {

	}

}
