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

import java.io.IOException;
import java.util.Locale;
import org.jamwiki.db.AnsiDataHandler;
import org.jamwiki.model.WikiGroup;
import org.jamwiki.model.WikiUser;
import org.jamwiki.utils.WikiUtil;
import org.jamwiki.utils.WikiCache;
import org.jamwiki.utils.WikiLogger;

/**
 * <code>WikiBase</code> is loaded as a singleton class and provides access
 * to all core wiki structures.  In addition this class provides utility methods
 * for resetting core structures including caches and permissions.
 * @author Christian P. Lerch (additon and changes)
 * @see org.jamwiki.SearchEngine
 */
public final class WikiBase {

	private static final WikiLogger logger = WikiLogger.getLogger(WikiBase.class.getName());
    
	/** The data handler that performs all database operations. */
	private static AnsiDataHandler dataHandler = null;
	/** The search engine instance. */
	private static SearchEngine searchEngine = null;
	/** An instance of the current parser. */
	private static JAMWikiParser parserInstance = null;

	/** Cache name for the cache of parsed topic content. */
	public static final WikiCache<String, String> CACHE_PARSED_TOPIC_CONTENT = 
            new WikiCache<>("org.jamwiki.WikiBase.CACHE_PARSED_TOPIC_CONTENT");
	/** Default group for registered users. */
	private static WikiGroup GROUP_REGISTERED_USER = null;
	/** Data stored using an external database */
	public static final String PERSISTENCE_EXTERNAL = "DATABASE";
	/** Data stored using an internal copy of the HSQL database */
	public static final String PERSISTENCE_INTERNAL = "INTERNAL";
	/** Name of the default starting points topic. */
	public static final String SPECIAL_PAGE_STARTING_POINTS = "StartingPoints";
	/** Name of the default left menu topic. */
	public static final String SPECIAL_PAGE_SIDEBAR = "JAMWiki:Sidebar";
	/** Name of the default footer topic. */
	public static final String SPECIAL_PAGE_FOOTER = "JAMWiki:Footer";
	/** Name of the default header topic. */
	public static final String SPECIAL_PAGE_HEADER = "JAMWiki:Header";
	/** Name of the jamwiki.css topic that holds system styles. */
	public static final String SPECIAL_PAGE_SYSTEM_CSS = "JAMWiki:System.css";
	/** Name of the jamwiki.css topic that holds custom styles. */
	public static final String SPECIAL_PAGE_CUSTOM_CSS = "JAMWiki:Custom.css";
	/** Allow file uploads of any file type. */
	public static final int UPLOAD_ALL = 0;
	/** Use a blacklist to determine what file types can be uploaded. */
	public static final int UPLOAD_BLACKLIST = 2;
	/** Disable all file uploads. */
	public static final int UPLOAD_NONE = 1;
	/** Use a whitelist to determine what file types can be uploaded. */
	public static final int UPLOAD_WHITELIST = 3;
	/** Enum indicating where uploaded files are stored. */
	public static enum UPLOAD_STORAGE { JAMWIKI, DOCROOT, DATABASE }

	private WikiBase() {}

	/**
	 * Get an instance of the current data handler.
	 *
	 * @return The current data handler instance, or <code>null</code>
	 *  if the handler has not yet been initialized.
	 */
	public static AnsiDataHandler getDataHandler() {
		if (WikiBase.dataHandler == null) {
			WikiBase.dataHandler = new AnsiDataHandler();
		}
		return WikiBase.dataHandler;
	}

	/**
	 *
     * @return 
	 */
	public static WikiGroup getGroupRegisteredUser() {
		if (WikiUtil.isFirstUse() || WikiUtil.isUpgrade()) {
			throw new IllegalStateException("Cannot retrieve group information prior to completing setup/upgrade");
		}
		if (WikiBase.GROUP_REGISTERED_USER == null) {
			try {
				WikiBase.GROUP_REGISTERED_USER = WikiBase.getDataHandler().lookupWikiGroup(WikiGroup.GROUP_REGISTERED_USER);
			} catch (DataAccessException e) {
				throw new DataAccessException("Unable to retrieve registered users group", e);
			}
		}
		return WikiBase.GROUP_REGISTERED_USER;
	}

	/**
	 * Get an instance of the current parser instance.
	 *
	 * @return The current parser instance.
	 */
	public static JAMWikiParser getParserInstance() {
        if (WikiBase.parserInstance == null) {
            WikiBase.parserInstance = WikiUtil.parserInstance();
        }
		return WikiBase.parserInstance;
	}

	/**
	 * Get an instance of the current search engine.
	 *
	 * @return The current search engine instance.
	 */
	public static SearchEngine getSearchEngine() {
        if (WikiBase.searchEngine == null) {
            WikiBase.searchEngine = WikiUtil.searchEngineInstance();
        }
		return WikiBase.searchEngine;
	}

	/**
	 * Reload the data handler, user handler, and other basic wiki
	 * data structures.
	 */
	public static void reload() {
		WikiConfiguration.reset();
		WikiBase.dataHandler = new AnsiDataHandler();
		if (WikiBase.searchEngine != null) {
            try {
                WikiBase.searchEngine.shutdown();
            } catch (IOException ex) {
                logger.error("Fatal error during search engine shutdown", ex);
                throw new RuntimeException(ex);
            }
		}
		WikiBase.searchEngine = WikiUtil.searchEngineInstance();
		WikiBase.parserInstance = WikiUtil.parserInstance();
	}

	/**
	 * Reset the WikiBase object, re-initializing the data handler and
	 * other values.
	 *
	 * @param locale The locale to be used if any system pages need to be set up
	 *  as a part of the initialization process.
	 * @param user A sysadmin user to be used in case any system pages need to
	 *  be created as a part of the initialization process.
	 * @param username The admin user's username (login).
	 * @param encryptedPassword The admin user's encrypted password.  This value
	 *  is only required when creating a new admin user.
	 * @throws DataAccessException Thrown if an error occurs during re-initialization..
	 * @throws WikiException Thrown if an error occurs during re-initialization.
	 */
	public static void reset(Locale locale, WikiUser user, String username, String encryptedPassword) {
		WikiCache.initialize();
        WikiBase.dataHandler = null;
		WikiBase.getDataHandler().setup(locale, user, username, encryptedPassword);
	}
}
