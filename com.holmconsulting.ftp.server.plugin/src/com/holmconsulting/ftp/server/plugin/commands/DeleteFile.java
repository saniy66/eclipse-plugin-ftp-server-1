package com.holmconsulting.ftp.server.plugin.commands;

import java.io.File;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.holmconsulting.ftp.server.plugin.Activator;
import com.holmconsulting.ftp.server.plugin.views.FTPServerView;
import com.holmconsulting.ftp.server.plugin.views.FTPServerView.ViewContentProvider;

public class DeleteFile extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchPart part = HandlerUtil.getActivePart(event);

		if (part instanceof FTPServerView) {

			final FTPServerView viewer = (FTPServerView) part;

			viewer.showLogo();

			IStructuredSelection iss = (IStructuredSelection) HandlerUtil
					.getCurrentSelection(event);

			int size = iss.size();

			if (size == 0) {
				return null;
			}

			ResourceBundle BUNDLE = Activator.getResourceBundle();

			String message = size == 1 ? BUNDLE
					.getString("action.delete.confirm.single") : String.format(
					BUNDLE.getString("action.delete.confirm.plural"), size);

			boolean confirm = MessageDialog.openConfirm(viewer.getViewer()
					.getControl().getShell(),
					BUNDLE.getString("action.delete.confirm"), message);

			if (confirm) {
				deleteFiles(iss, viewer);
			}
		}
		return null;
	}

	private void deleteFiles(IStructuredSelection iss, FTPServerView viewer) {
		@SuppressWarnings("unchecked")
		Iterator<Object> it = iss.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null && obj instanceof File) {
				File file= (File) obj;
				((ViewContentProvider) viewer.getViewer().getContentProvider())
						.remove(file);
			}
		}
		viewer.getViewer().refresh();
	}

}
