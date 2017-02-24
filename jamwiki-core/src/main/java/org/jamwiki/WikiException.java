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

/**
 * Custom exception class for JAMWiki errors.  This exception type allows an error
 * message key to be passed back to the front end for display to the user.
 * @author Christian P. Lerch (additon and changes)
 */
public class WikiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	private final WikiMessage wikiMessage;

	/**
	 * Constructor for a WikiException containing a Wiki message value.
	 *
	 * @param wikiMessage The message information for the exception.
	 */
	public WikiException(WikiMessage wikiMessage) {
		super();
		this.wikiMessage = wikiMessage;
	}

	/**
	 * Constructor for a WikiException containing a Wiki message value.
	 *
	 * @param wikiMessage The message information for the exception.
	 * @param t The exception that is the cause of this WikiException.
	 */
	public WikiException(WikiMessage wikiMessage, Throwable t) {
		super(t);
		this.wikiMessage = wikiMessage;
	}

	/**
	 * Return the WikiMessage object associated with this exception.
	 *
	 * @return The WikiMessage object assocated with this exception.
	 */
	public WikiMessage getWikiMessage() {
		return this.wikiMessage;
	}
}
