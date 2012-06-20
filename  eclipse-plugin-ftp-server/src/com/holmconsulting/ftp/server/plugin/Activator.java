package com.holmconsulting.ftp.server.plugin;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.BindException;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.holmconsulting.ftp.server.FTPServer;
import com.holmconsulting.ftp.server.Settings;
import com.holmconsulting.ftp.server.Util;
import com.holmconsulting.ftp.server.plugin.preferences.PreferenceConstants;
import com.holmconsulting.ftp.server.plugin.util.FileUtils;
import com.holmconsulting.ftp.server.plugin.views.FTPServerView;
import com.holmconsulting.ftp.server.plugin.views.FTPServerView.ViewContentProvider;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements PropertyChangeListener, IStartup {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.holmconsulting.ftp.server.plugin"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	// Locale Specific Resource Bundle
		private static final ResourceBundle BUNDLE = ResourceBundle
				.getBundle("i18n.Resources");

	// FTP  Server
	private final FTPServer server = new FTPServer();
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framewActivatorork
	 * .BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		// Start Server if:
		// a) View is open
		// b) User preference selected autostart
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {

				// Check if view is loaded
				FTPServerView view = (FTPServerView) PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.findView(FTPServerView.ID);

				// If view is loaded, check user preference
				if (view != null) {
					// Get user preferences
					IPreferenceStore store = Activator.getDefault()
							.getPreferenceStore();

					// Start server if the preferences indicate such.
					if (store.getBoolean(PreferenceConstants.PARAM_STARTUP)) {
						Activator.getDefault().startServer();
					}
				}
			}
		});
	}
	
	
	

	/**
	 * Creates a new ThreadGroup for the SMTP Server.
	 */
	public void startServer() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		boolean debug = store.getBoolean(PreferenceConstants.PARAM_DEBUG);
		int port = store.getInt(PreferenceConstants.PARAM_PORT);
		String username = store.getString(PreferenceConstants.PARAM_USERNAME);
		String password = store.getString(PreferenceConstants.PARAM_PASSWORD);
		
		// default root directory always 
		File rootDir = Activator.getDefault().getStateLocation().toFile();
		if(rootDir.isDirectory()) {
			Settings.setChrootdir(rootDir);	
		}else {
			Settings.setChrootdir(rootDir.getParentFile());
		}
		
		
		
	     if(debug) {
	    	 Settings.setConsoleLogLevel(Level.INFO);
	     }else {
	    	 Settings.setConsoleLogLevel(Level.SEVERE);
	     }
	     
	     Settings.setPortNumber(port);
	     Settings.setUsername(username);
	     Settings.setPassword(password);
	     Util.addListener(this);
		
		ThreadGroup tg = new ServerThreadGroup("FTPServer");
		
		// allowing the FTPServer to have its own GroupThread for capturing uncauth exception only 
		// can be server.start since this method will create the thread and run it 
		Runnable runnable = new Runnable() {
			public void run() {
				server.start();
			}
		};
		
		new Thread(tg, runnable).start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		// Get Persist Preference
		boolean persist = plugin.getPreferenceStore().getBoolean(
				PreferenceConstants.PARAM_PERSIST);
		

		// If not persisting, delete all messages
		if (!persist) {
		   File rootDir = Activator.getDefault().getStateLocation().toFile();
		   FileUtils.getInstance().deletFiles(rootDir);
			 
	    }
			 

		if (server != null) {
			Activator.getDefault().stopServer();
		}

		super.stop(context);
	}

	public void stopServer() {
		server.stop();
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	/**
	 * Returns the resource bundle
	 * @return the resource bundle
	 */
	public static ResourceBundle getResourceBundle() {
		return BUNDLE;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	

 /**
  * We will get informed by the upload and delete file
  * This is invoked by FTPServe thread and we would like to keep invoke 
  * it on the GUI thread since it is not thread save.
  * from the FTP server
  * @param evt
  */
 @Override
 public void propertyChange(final PropertyChangeEvent evt) {
	 
	 Display.getDefault().syncExec(new Runnable() {
			public void run() {
			 doPropertyChange(evt);
			}
		});
	 
 } 
	
 /**
  * This is being invoked on the GUI thread 
  * now
  */
 protected void doPropertyChange(final PropertyChangeEvent evt) {
	 
	 FTPServerView view = null;
	 
	 IWorkbenchWindow workbanchWindow = null;
	 
	 // we can not know if the button is clicked to stop the server or the IWorkBenchWindow is closing down.
	 try {
		 workbanchWindow =  PlatformUI.getWorkbench().getActiveWorkbenchWindow(); 
	 }catch(org.eclipse.swt.SWTException ex) {
		 return; // terminate now
	 }finally {
		 // no cleaning 
	 }
			 
			
	 if(workbanchWindow == null) {
		 return ;
	 }
	 
	 IWorkbenchPage workbenchPage =  workbanchWindow.getActivePage();
	 if(workbenchPage == null) {
		 return;
	 }
	 
	 view = (FTPServerView)workbenchPage.findView(FTPServerView.ID);
	 
	 if(view == null) {
		 System.out.println("view is nulll from doPropertyChange(evt)... return now");
		 return ;
	 }
	 
	String propertyChangeName = evt.getPropertyName();
	Util.ActionTaken action = Util.ActionTaken.getActionTaken(propertyChangeName);
	if(action != null && action == Util.ActionTaken.FILE_UPLOADED) {
	 File file = (File)evt.getNewValue();
	 if(file != null) {
		 ViewContentProvider provider = (ViewContentProvider) view.getViewer().getContentProvider();
		 provider.add(file);
	 }
	}else if(action != null && action == Util.ActionTaken.FILE_DELTED) {
		 File file = (File)evt.getOldValue();
		 if(file != null) {
			// FileWrapper wrapper = new FileWrapper(file, new Date());
			 ViewContentProvider provider = (ViewContentProvider) view.getViewer().getContentProvider();
			 provider.remove(file);
			 view.reloadFiles(); // the refresh doesn't not work  FIXME later find out later
		 }
	}else if(action != null && action == Util.ActionTaken.SERVER_STARTED) {
		view.reloadFiles();
		view.disableStartServer();
		view.enableStopServer();
		
	}else if(action != null && action == Util.ActionTaken.SERVER_STOPPED) {
		view.disableStopServer();
		view.enableStartServer();
		
	}
	
 }
	 
	
 /**
	 * Registers plugin for startup when Workbench starts Can be overridden by
	 * user in Preferences > General > Startup
	 * 
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	public void earlyStartup() {
		// TODO Auto-generated method stub
	}
	
	
   /**
    * It is only used by this class; no need to reference instance variable
    * 
    * @author alan mehio
    *
    */
   private static  class ServerThreadGroup extends ThreadGroup {

		public ServerThreadGroup(String name) {
			super(name);
			
		}
		
		@Override
		public void uncaughtException(Thread t, final Throwable e) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {

					FTPServerView view = (FTPServerView) PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage()
							.findView(FTPServerView.ID);

					view.disableStopServer();
					view.enableStartServer();
                  
					if (e.getCause() instanceof BindException) {

						int port = Activator.getDefault().getPreferenceStore()
								.getInt(PreferenceConstants.PARAM_PORT);

						IStatus status = new Status(
								IStatus.ERROR,
								Activator.PLUGIN_ID,
								"The application couldn't bind to the specified port.  Check to make sure the port isn't in use by another process or you have permission to bind to the port.",
								e);

						ResourceBundle bundle = Activator.getResourceBundle();

						ErrorDialog.openError(view.getViewer().getControl()
								.getShell(), null, String.format(
								bundle.getString("exception.port.bind"), port),
								status);
					}else {
					
						IStatus status = new Status(
								IStatus.ERROR,
								Activator.PLUGIN_ID,
								"The application has thrown an exception; please report to alan.mehio@gmail.com",
								e);

						ResourceBundle bundle = Activator.getResourceBundle();

						ErrorDialog.openError(view.getViewer().getControl()
								.getShell(), null, String.format(
								bundle.getString("exception.other"), ""),
								status);
					}
				}
			});

	 }
		
   } // AServerThreadGroup class



   
} 
