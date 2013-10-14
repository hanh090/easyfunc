package edu.hcmut.easyfunc.preferences;

import org.eclipse.jdt.internal.ui.preferences.PreferencesMessages;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import edu.hcmut.easyfunc.Activator;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class EasyFuncPref extends PreferencePage implements
		IWorkbenchPreferencePage {
	
	public EasyFuncPref() {
		super();
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("EasyFunc general configure:");
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		
	}

	@Override
	protected Control createContents(Composite parent) {
		initializeDialogUnits(parent);

		Composite result= new Composite(parent, SWT.NONE);
		GridLayout layout= new GridLayout();
		layout.marginHeight= convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth= 0;
		layout.verticalSpacing= convertVerticalDLUsToPixels(10);
		layout.horizontalSpacing= convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		result.setLayout(layout);
		
		Group apiDocGroup= new Group(result, SWT.NONE);
		apiDocGroup.setLayout(new GridLayout());
		apiDocGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		apiDocGroup.setText("API Document directory:");
		
		
		
		return result;
	}

}