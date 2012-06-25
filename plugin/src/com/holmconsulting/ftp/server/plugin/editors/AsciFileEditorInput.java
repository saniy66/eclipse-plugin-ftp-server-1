
package com.holmconsulting.ftp.server.plugin.editors;

import java.io.File;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.holmconsulting.ftp.server.plugin.Activator;

/**
 * @author Kevin Kelley (dev@foos-bar.com)
 */
public class AsciFileEditorInput
		implements IEditorInput {

	/* Locale Specific Resource Bundle */
	private static final ResourceBundle BUNDLE = Activator.getResourceBundle();
	
	private final String participant;
	private final File file;

	public AsciFileEditorInput(File file) {
		super();
		this.participant = file.hashCode() + "";
		this.file = file;
	}

	public File getFile() {
		return this.file;
	}

	

	public boolean exists() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		if (this.file.getName()== null
				|| "".equals(this.file.getName().trim())) {
			return BUNDLE.getString("notify.unknown.name");
		} else {
			return this.file.getName();
		}
	}

	public String getParticipant() {
		return this.participant;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		if (this.file == null) {
			return "";
		}
		return this.file.getPath();
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class arg0) {
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		}

		if (!(obj instanceof AsciFileEditorInput)) {
			return false;
		}

		AsciFileEditorInput other = (AsciFileEditorInput) obj;

		if (this.participant == null) {
			return other.getParticipant() == null;
		}

		return this.participant.equals(other.participant);
	}

	@Override
	public int hashCode() {
		if (this.participant != null) {
			return this.file.hashCode();
		}
		return super.hashCode();
	}
}
