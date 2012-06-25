package com.holmconsulting.ftp.server.plugin.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.holmconsulting.ftp.server.plugin.views.FTPServerView;

public class OpenFileWithSystemEditor  extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if (part instanceof FTPServerView) {
			FTPServerView viewer = (FTPServerView) part;
			viewer.openMessageWithSystemEditor();
		}
		return null;
	}

}
