/*
 * Copyright (C) 2017 Christian P. Lerch
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jamwiki;

import java.sql.SQLException;

/**
 * Custom exception class for JAMWiki data errors.  This class will typically
 * wrap <code>SQLException</code> or other exception types.
 * @author Christian P. Lerch (additions and changes)
 */
public class DataAccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	/** SQLException can bury useful info in getNextException(), so add special handling. */
	private SQLException nextException;

	/**
	 * Constructor for an exception containing a message.
	 *
	 * @param message The message information for the exception.
	 */
	public DataAccessException(String message) {
		super(message);
		this.init(null);
	}

	/**
	 * Constructor for an exception containing a message and wrapping another
	 * exception.
	 *
	 * @param message The message information for the exception.
	 * @param t The exception that is the cause of this exception.
	 */
	public DataAccessException(String message, Throwable t) {
		super(message, t);
		this.init(t);
	}

	/**
	 * Constructor for an exception that wraps another exception.
	 *
	 * @param t The exception that is the cause of this exception.
	 */
	public DataAccessException(Throwable t) {
		super(t);
		this.init(t);
	}

	/**
	 *
	 */
	private void init(Throwable t) {
		if (t instanceof SQLException) {
			this.nextException = ((SQLException)t).getNextException();
		}
	}

	/**
	 * Override the parent method to add special handling for SQLException.getNextException().
	 */
	public String getMessage() {
		String message = super.getMessage();
		if (this.nextException != null) {
			message = (message == null) ? "" : message;
			message += " [ " + this.nextException.toString() + " ]";
		}
		return message;
	}
}
