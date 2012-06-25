package com.holmconsulting.ftp.server.plugin.views;

import java.io.File;
import java.text.Collator;
import java.util.Date;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * Sorter is responsible for ordering messages based on the selected column. It
 * also has the responsibility of determining if the column should be sorted in
 * ascending or descending order.
 * 
 * Default is descending order for newly selected columns.
 * 
 * @author alan mehio
 */
public class FileWrapperSorter extends ViewerSorter {
	private String columnName;

	private SortDirection direction;

	public FileWrapperSorter() {
		direction = SortDirection.DESCENDING;
		columnName = "receiving";
	}

	public void setColumnName(String columnName) {
		direction = columnName.equalsIgnoreCase(this.columnName) ? direction
				.flip() : SortDirection.DESCENDING;
				this.columnName = columnName;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		if (columnName == null) {
			return 0;
		}

		File m1 = (File) e1;
		File m2 = (File) e2;

		int result = 0;

		if (FTPServerView.COL_SIZE.equals(columnName)) {
			result = compareLong(m1.length(),
					m2.length());
		} else if (FTPServerView.COL_NAME.equals(columnName)) {
			result = Collator.getInstance().compare(m1.getName(), m2.getName());
		} else if (FTPServerView.COL_PATH.equals(columnName)) {
			result = Collator.getInstance().compare(m1.getPath(), m2.getPath());
		} else if (FTPServerView.COL_RECEIVED.equals(columnName)) {
			result = compareLong(m1.lastModified(), m2.lastModified());
		}
		// If descending order, flip the direction
		return direction == SortDirection.DESCENDING ? -result : result;
	}
	
	
	public int compareLong(Long v1, Long v2) {
		return v1.compareTo(v2); 
		
	}

	public int compareDates(Date d1, Date d2) {
		if (d1 == null) {
			return -1;
		}
		if (d2 == null) {
			return 1;
		}
		return d1.compareTo(d2);
	}

	public enum SortDirection {
		ASCENDING,
		DESCENDING;

		public SortDirection flip() {
			if (this == ASCENDING) {
				return DESCENDING;
			} else {
				return ASCENDING;
			}
		}
	}

}
