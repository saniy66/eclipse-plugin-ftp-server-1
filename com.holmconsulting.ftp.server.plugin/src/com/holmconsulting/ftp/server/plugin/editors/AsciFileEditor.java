package com.holmconsulting.ftp.server.plugin.editors;

import java.io.File;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.holmconsulting.ftp.server.plugin.Activator;
import com.holmconsulting.ftp.server.plugin.util.FileStore;


/**
 * A single-tab editor for inspecting viewing the following file type
 * <ul>
 * <li>.txt
 * <li>.java
 * <li>.rtf
 * </ul>
 */
public class AsciFileEditor extends MultiPageEditorPart {

	public static final String ID = "com.holmconsulting.ftp.server.plugin.editors.AsciFileEditor";

	/* Locale Specific Resource Bundle */
	private static final ResourceBundle BUNDLE = Activator.getResourceBundle();

	/* File Data */
	private File file;

	

	public AsciFileEditor() {
		super();
	}

	
	/**
	 * Creates a page/tab to display the text email for preview.
	 */
	void createTextPage() {


		Composite composite = new Composite(getContainer(), SWT.NONE);

		FillLayout layout = new FillLayout();
		composite.setLayout(layout);

		StyledText text = new StyledText(composite, SWT.H_SCROLL | SWT.V_SCROLL);
		text.setEditable(false);
		text.setText(FileStore.getData(file));

		int index = addPage(composite);
		setPageText(index, BUNDLE.getString("editor.textFormat"));
	}

	

	/**
	 * Creates the pages of the multi-page editor.
	 */
	@Override
	protected void createPages() {
	
		// Create Text-only View of Email if necessary
		createTextPage();
	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * Saves the multi-page editor's document.
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}

	/**
	 * Saves the multi-page editor's document as another file. Also updates the
	 * text for page 0's tab, and updates this multi-page editor's input to
	 * correspond to the nested editor's.
	 */
	@Override
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	/**
	 * The <code>AsciFileEditor</code> implementation of this method
	 * checks that the input is an instance of <code>AsciFileEditorInput</code>.
	 */
	@Override
	public void init(IEditorSite site, IEditorInput editorInput)
			throws PartInitException {
		super.init(site, editorInput);
		if (!(editorInput instanceof AsciFileEditorInput)) {
			throw new PartInitException(
					"Invalid Input: Must be AsciFileEditorInput");
		}

		setSite(site);
		setInput(editorInput);

		file = ((AsciFileEditorInput) editorInput).getFile();


		setPartName(((AsciFileEditorInput) editorInput).getName());
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart.
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 2) {
			return; // Do nothing
		}
	}

	
}