/*******************************************************************************
 * Copyright (c) 2010 - 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <lars.Vogel@gmail.com> - Bug 419770
 *******************************************************************************/
package ch.uzh.business.systemanalyse.tool.licence.tool.parts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ch.uzh.business.systemanalyse.tool.licence.tool.filegenerator.Licence;
import ch.uzh.business.systemanalyse.tool.licence.tool.filegenerator.LicenceGenerator;

public class LicenceFileGeneration {

	private Text text;
	private DateTime dateSelector;

	private final LicenceGenerator licenceGenerator = new LicenceGenerator();;

	@Inject
	private MDirtyable dirty;

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		Composite calendarComposite = new Composite(parent, SWT.NONE);
		calendarComposite.setLayout(new GridLayout(1, false));
		dateSelector = new DateTime(calendarComposite, SWT.CALENDAR
				| SWT.BORDER);

		Composite labelTextComposite = new Composite(parent, SWT.NONE);
		labelTextComposite.setLayout(new GridLayout(1, false));
		Label label = new Label(labelTextComposite, SWT.HORIZONTAL);
		label.setText("Selected validity date: ");
		text = new Text(labelTextComposite, SWT.BORDER);
		text.setText("");

		dateSelector.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				text.setText(dateSelector.getDay() + "."
						+ (dateSelector.getMonth() + 1) + "."
						+ dateSelector.getYear());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		Button generateButton = new Button(parent, SWT.PUSH);
		generateButton.setText("Generate licence file");
		generateButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell shell = e.display.getActiveShell();
				DirectoryDialog dirDialog = new DirectoryDialog(shell);
				dirDialog.setText("Select location for the licence file");
				String location = dirDialog.open();
				System.out.println(location);

				Calendar validityCalendar = Calendar.getInstance();
				validityCalendar.set(Calendar.YEAR, dateSelector.getYear());
				validityCalendar.set(Calendar.MONTH, dateSelector.getMonth());
				validityCalendar.set(Calendar.DAY_OF_MONTH,
						dateSelector.getDay());

				System.out.println(validityCalendar.getTime());

				Licence licence = new Licence(validityCalendar.getTime()
						.getTime());
				try {
					FileOutputStream fout = new FileOutputStream(location
							+ File.separator + "licence.dat");
					ObjectOutputStream oos = new ObjectOutputStream(fout);
					oos.writeObject(licence);
					oos.close();
					MessageDialog.openInformation(Display.getDefault()
							.getActiveShell(), "Licence generation",
							"Successfully created licence file.");
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Focus
	public void setFocus() {
		// tableViewer.getTable().setFocus();
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
}