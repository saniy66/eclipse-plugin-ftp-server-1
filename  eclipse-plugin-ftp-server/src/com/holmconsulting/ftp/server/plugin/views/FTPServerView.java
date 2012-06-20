package com.holmconsulting.ftp.server.plugin.views;


import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;

import com.holmconsulting.ftp.server.plugin.Activator;
import com.holmconsulting.ftp.server.plugin.editors.AsciFileEditor;
import com.holmconsulting.ftp.server.plugin.preferences.PreferenceConstants;
import com.holmconsulting.ftp.server.plugin.util.FileUtils;
import com.holmconsulting.ftp.server.plugin.util.NotificationManager;



/**
 * This class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 * @author Alan Mehio alan.mehio@gmail.com
 */

public class FTPServerView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.holmconsulting.plugin.ftp.server.views.FTPServerView";

		// Start Server Icon
		private static final ImageDescriptor IMG_RUN = ImageDescriptor
				.createFromFile(Activator.class, "/icons/run.gif");

		// Stop Server
		private static final ImageDescriptor IMG_STOP = ImageDescriptor
				.createFromFile(Activator.class, "/icons/stop.gif");

		// New Messages
		private static final ImageDescriptor IMG_NEW_MESSAGES = ImageDescriptor
				.createFromFile(Activator.class, "/icons/ftpServerLogo.png");

		// Logo for View
		private static final ImageDescriptor IMG_LOGO = ImageDescriptor
				.createFromFile(Activator.class, "/icons/ftpServerLogo.png");

		// Locale Specific Date Formatter
		private static final DateFormat DATE_FORMATTER = DateFormat
				.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT,
						Locale.getDefault());

		// Locale Specific Resource Bundle
		private static final ResourceBundle BUNDLE = Activator.getResourceBundle();
		
		
		public static final String COL_SIZE = BUNDLE.getString("header.size");
		public static final String COL_SIZE_TOOLTIP = BUNDLE.getString("header.size.tooltip");
		
		public static final String COL_NAME = BUNDLE.getString("header.filename");
		public static final String COL_NAME_TOOLTIP = BUNDLE.getString("header.filename.tooltip");
		
		public static final String COL_PATH = BUNDLE.getString("header.path");
		public static final String COL_PATH_TOOLTIP = BUNDLE.getString("header.path.tooltip");
		
		public static final String COL_RECEIVED = BUNDLE.getString("header.received");
        public static final String COL_RECEIVED_TOOLTIP = BUNDLE.getString("header.received.tooltip");
		
		private TableViewer viewer;

		private Action runServer;
		private Action stopServer;
		private Action openPreferences;

		
		
		/**
		 * The constructor.
		 */
		public FTPServerView() {
			super(); // implicit no need
		}


	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 public class ViewContentProvider implements IStructuredContentProvider {
			List<File> files = new ArrayList<File>();

			public ViewContentProvider(List<File> files) {
				if (files != null) {
					this.files= files;
				}
			}

			public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			}

			public void dispose() {
			}

			public Object[] getElements(Object parent) {
				return files.toArray();
			}

			public void add(File file) {
				files.add(file);
				getViewer().refresh();

				// Sets label bold if the view is unfocused.
				IWorkbenchSiteProgressService service = (IWorkbenchSiteProgressService) getSite()
						.getService(IWorkbenchSiteProgressService.class);

				service.warnOfContentChange();

				// getSite().getPart().setFocus();
				getSite().getPage().activate(getSite().getPart());

				showMessageNotificaiton(file, true);

			}

			public void remove(File file) {
				files.remove(file);
				getViewer().refresh();
				showMessageNotificaiton(file, false);
			}

			public void setRead(File file) {
				int idx = files.indexOf(file);
				if (idx >= 0) {
				//FIXME : do we need that??	files.get(idx).setUnread(false);
				}
				getViewer().refresh();
			}
		}
		
		
		
		/**
		 * Returns the label for each of the columns based on the index for that
		 * column.
		 * 
		 * @author Alan Mehio 
		 */
		class ViewLabelProvider extends LabelProvider implements
		ITableLabelProvider {

			public String getColumnText(Object obj, int index) {
				File file = (File) obj;
				switch (index) {
				case 0:
					return  "" + file.length();
				case 1:
					return file.getName();
				case 2:
					return file.getPath();
				case 3:
					if (file.lastModified() == 0) {
						return "";
					} else {
						return DATE_FORMATTER.format(new Date(file.lastModified()));
					}
				default:
					throw new RuntimeException("Should not happen");
				}
			}

			public Image getColumnImage(Object obj, int index) {
				// no image for any column
				return null;
			}

		
		}

		
	
		private void createColumns(final TableViewer viewer, Composite parent) {

			Table table = viewer.getTable();
			table.setHeaderVisible(true);
			table.setLinesVisible(true);

			String[] titles = { COL_SIZE, COL_NAME, COL_PATH, COL_RECEIVED };
			String[] tooltips= { COL_SIZE_TOOLTIP, COL_NAME_TOOLTIP, COL_PATH_TOOLTIP, COL_RECEIVED_TOOLTIP };
			int[] bounds = { 80, 170, 275,160 };

			for (int i = 0; i < titles.length; i++) {

				final String title = titles[i];
				final String tooltip = tooltips[i];

				TableViewerColumn vColumn = new TableViewerColumn(viewer, SWT.NONE);
				TableColumn column = vColumn.getColumn();
				column.setText(title);
				column.setToolTipText(tooltip);
				column.setWidth(bounds[i]);
				
				column.setResizable(true);
				column.setMoveable(true);

				if (i == 0 || i == titles.length - 1) {
						column.setAlignment(SWT.RIGHT);
				}
				
				// Add Sorting
				column.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {

							FileWrapperSorter sorter = (FileWrapperSorter) viewer
									.getSorter();
							sorter.setColumnName(title);
							Table table = viewer.getTable();
							int dir = table.getSortDirection();
							TableColumn tc = (TableColumn) e.getSource();

							if (table.getSortColumn() == null) {
								dir = SWT.DOWN;
							} else if (table.getSortColumn().getText()
									.equals(title)) {
								dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
							} else {
								dir = SWT.DOWN;
							}

							table.setSortDirection(dir);
							table.setSortColumn(tc);
							viewer.refresh();
						}
					});
			}
		}

		/**
		 * This is a callback that will allow us to create the viewer and initialize
		 * it.
		 */
		@Override
		public void createPartControl(Composite parent) {
			viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
					| SWT.V_SCROLL | SWT.FULL_SELECTION);
			createColumns(viewer, parent);

			File rootDir = Activator.getDefault().getStateLocation().toFile();
			List<File> files = new ArrayList<File>();
			FileUtils.getInstance().loadFiles(rootDir, files);
			viewer.setContentProvider(new ViewContentProvider(files));
			viewer.setLabelProvider(new ViewLabelProvider());
			viewer.setInput(getViewSite());
			getSite().setSelectionProvider(viewer);

			FileWrapperSorter sorter = new FileWrapperSorter();
			viewer.setSorter(sorter);

			// Initial sort
			sorter.setColumnName(COL_RECEIVED);
			viewer.getTable().setSortDirection(SWT.UP);
			viewer.getTable().setSortColumn(
					viewer.getTable().getColumn(
							viewer.getTable().getColumnCount() - 1));
			viewer.refresh();

			// Create the help context id for the viewer's control
			PlatformUI.getWorkbench().getHelpSystem()
			.setHelp(viewer.getControl(), "viewer.context.help");
			makeActions();
			hookContextMenu();
			hookDoubleClickAction();
			contributeToActionBars();

			IContextService contextService = (IContextService) getSite()
					.getService(IContextService.class);
			// IContextActivation contextActivation =
			contextService.activateContext("viewer.context.help");

		}
		
	/**
	 * refresh the view since viewer.refresh() does not work since FileWrapper .. FIXME investigate 
	 */
	public void reloadFiles() {
		File rootDir = Activator.getDefault().getStateLocation().toFile();
		List<File> files = FileUtils.getInstance().reloadFiles(rootDir);
		viewer.getContentProvider().dispose();
		viewer.setContentProvider(new ViewContentProvider(files));
	}

	
	
		public void showMessageNotificaiton(File file, boolean isNew) {
			this.setTitleImage(IMG_NEW_MESSAGES.createImage());

			// Get Preferences
			IPreferenceStore pStore = Activator.getDefault().getPreferenceStore();

			// Display Debug Messages?
			boolean popupEnabled = pStore
					.getBoolean(PreferenceConstants.PARAM_NOTIFICATION_ENABLED);

			if (popupEnabled) {
				NotificationManager.notify(file,isNew, Display.getDefault());
			}
		}

		public void showLogo() {
			this.setTitleImage(IMG_LOGO.createImage());
		}

	
	  

		private void hookContextMenu() {
			MenuManager menuMgr = new MenuManager("#PopupMenu");
			menuMgr.setRemoveAllWhenShown(true);
			menuMgr.addMenuListener(new IMenuListener() {
				public void menuAboutToShow(IMenuManager manager) {
					Object obj = ((IStructuredSelection) viewer.getSelection())
							.getFirstElement();
					if (obj != null && obj instanceof File) {
						FTPServerView.this.fillContextMenu(manager);
					}
				}
			});
			Menu menu = menuMgr.createContextMenu(viewer.getTable());
			viewer.getControl().setMenu(menu);
			getSite().registerContextMenu(menuMgr, viewer);
		}

		private void contributeToActionBars() {
			IActionBars bars = getViewSite().getActionBars();
			fillLocalPullDown(bars.getMenuManager());
			fillLocalToolBar(bars.getToolBarManager());
		}

		private void fillLocalPullDown(IMenuManager manager) {
			manager.add(runServer);
			manager.add(stopServer);
			manager.add(new Separator());
			manager.add(openPreferences);
		}

		private void fillContextMenu(IMenuManager manager) {
			// Other plug-ins can contribute there actions here
			manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		}

		private void fillLocalToolBar(IToolBarManager manager) {
			manager.add(runServer);
			manager.add(stopServer);
		}

		public void enableStartServer() {
			if (runServer != null) {
				runServer.setEnabled(true);
			}
		}

		public void disableStartServer() {
			if (runServer != null) {
				runServer.setEnabled(false);
			}
		}

		public void enableStopServer() {
			if (stopServer != null) {
				stopServer.setEnabled(true);
			}
		}

		public void disableStopServer() {
			if (stopServer != null) {
				stopServer.setEnabled(false);
			}
		}

		private void makeActions() {

			openPreferences = new Action() {
				@Override
				public void run() {
					PreferencesUtil
					.createPreferenceDialogOn(
							viewer.getControl().getShell(),
							"com.holmconsulting.ftp.server.plugin.preferences.PreferencePage",
							new String[] {"com.holmconsulting.ftp.server.plugin.preferences.PreferencePage" },
							null).open();
				}
			};

			openPreferences.setText(BUNDLE.getString("action.preferences"));
			openPreferences.setToolTipText(BUNDLE
					.getString("action.preferences.tooltip"));

			runServer = new Action() {
				@Override
				public void run() {
					Activator.getDefault().startServer();
				}
			};

			runServer.setText(BUNDLE.getString("action.start"));
			runServer.setToolTipText(BUNDLE.getString("action.start.tooltip"));
			runServer.setImageDescriptor(IMG_RUN);

			stopServer = new Action() {
				@Override
				public void run() {
					Activator.getDefault().stopServer();
					
				}
			};

			stopServer.setText(BUNDLE.getString("action.stop"));
			stopServer.setToolTipText(BUNDLE.getString("action.stop.tooltip"));
			stopServer.setImageDescriptor(IMG_STOP);
			stopServer.setEnabled(false);
		}

		
		public void openMessage() {
			openMessageWithSystemEditor();
		}

		@SuppressWarnings("unchecked")
		public void openMessageWithSystemEditor() {
			IStructuredSelection iss = (IStructuredSelection) getViewer()
					.getSelection();
			Iterator<Object> it = iss.iterator();
			while (it.hasNext()) {
				Object obj = it.next();
				if (obj instanceof File) {
					File file = (File) obj;
				    openMessage(file, true);
				}
			}
			return;
		}

		private void openMessage(File file, boolean inSystemEditor) {
			IEditorInput input = inSystemEditor ? getSystemEditorInput(file)
					: getInternalEditorInput(file);
			String editorId = inSystemEditor ? IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID
					: AsciFileEditor.ID;
			try {
				IWorkbenchPage page = getSite().getPage();
				IDE.openEditor(page, input, editorId, true);
				setFileRead(file);
				showLogo();
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}

		private IEditorInput getInternalEditorInput(File file) {
			throw new RuntimeException("This method should not be invoked");
		}

		private IEditorInput getSystemEditorInput(File file) {
			IFileStore store = EFS.getLocalFileSystem().getStore(new Path(file.getAbsolutePath()));
			return new FileStoreEditorInput(store);
		}

		private void setFileRead(File file) {
			ViewContentProvider provider = (ViewContentProvider) getViewer()
					.getContentProvider();
			provider.setRead(file);
		}

		private void hookDoubleClickAction() {
			viewer.addDoubleClickListener(new IDoubleClickListener() {
				public void doubleClick(DoubleClickEvent event) {
					openMessage();
				}
			});
		}

		public TableViewer getViewer() {
			return viewer;
		}

		/**
		 * Passing the focus request to the viewer's control.
		 */
		@Override
		public void setFocus() {
			viewer.getControl().setFocus();
		}
}