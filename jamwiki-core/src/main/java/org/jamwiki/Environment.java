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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jamwiki.db.QueryHandler;
import org.jamwiki.utils.ResourceUtil;
import org.jamwiki.utils.SortedProperties;
import org.jamwiki.utils.WikiLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

/**
 * The <code>Environment</code> class is instantiated as a singleton to
 * provides access to JAMWiki property values stored in the
 * <code>jamwiki.properties</code> file.
 * @author Christian P. Lerch (additions and changes)
 */
public final class Environment {
	private static final WikiLogger log = WikiLogger.getLogger(Environment.class.getName());
    
    public static final String PROP_BASE_COOKIE_EXPIRE = "cookie-expire";
	public static final String PROP_BASE_DEFAULT_TOPIC = "default-topic";
	public static final String PROP_BASE_FILE_DIR = "homeDir";
	public static final String PROP_BASE_INITIALIZED = "props-initialized";
	public static final String PROP_BASE_LOGO_IMAGE = "logo-image";
	public static final String PROP_BASE_META_DESCRIPTION = "meta-description";
	public static final String PROP_BASE_PERSISTENCE_TYPE = "persistenceType";
	public static final String PROP_BASE_SEARCH_ENGINE = "search-engine";
	public static final String PROP_BASE_WIKI_VERSION = "wiki-version";
	public static final String PROP_DB_DRIVER = "driver";
	public static final String PROP_DB_PASSWORD = "db-password";
	public static final String PROP_DB_TYPE = "database-type";
	public static final String PROP_DB_URL = "url";
	public static final String PROP_DB_USERNAME = "db-user";
	public static final String PROP_DBCP_MAX_ACTIVE = "dbcp-max-active";
	public static final String PROP_DBCP_MAX_IDLE = "dbcp-max-idle";
	public static final String PROP_DBCP_MAX_OPEN_PREPARED_STATEMENTS = "dbcp-max-open-prepared-statements";
	public static final String PROP_DBCP_MIN_EVICTABLE_IDLE_TIME = "dbcp-min-evictable-idle-time";
	public static final String PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN = "dbcp-num-tests-per-eviction-run";
	public static final String PROP_DBCP_POOL_PREPARED_STATEMENTS = "dbcp-pool-prepared-statements";
	public static final String PROP_DBCP_TEST_ON_BORROW = "dbcp-test-on-borrow";
	public static final String PROP_DBCP_TEST_ON_RETURN = "dbcp-test-on-return";
	public static final String PROP_DBCP_TEST_WHILE_IDLE = "dbcp-test-while-idle";
	public static final String PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS = "dbcp-time-between-eviction-runs";
	public static final String PROP_DBCP_WHEN_EXHAUSTED_ACTION = "dbcp-when-exhausted-action";
	public static final String PROP_EMAIL_SMTP_ENABLE = "smtp-enable";
	public static final String PROP_EMAIL_SMTP_REQUIRES_AUTH = "smtp-authentication";
	public static final String PROP_EMAIL_SMTP_USERNAME = "smtp-username";
	public static final String PROP_EMAIL_SMTP_PASSWORD = "smtp-userpass";
	public static final String PROP_EMAIL_SMTP_USE_SSL = "smtp-use-ssl";
	public static final String PROP_EMAIL_REPLY_ADDRESS = "smtp-reply-to";
	public static final String PROP_EMAIL_SMTP_HOST = "smtp-host";
	public static final String PROP_EMAIL_SMTP_PORT = "smtp-port";
	public static final String PROP_EMAIL_ADDRESS_SEPARATOR = "smtp-address-separator";
	public static final String PROP_EMAIL_DEFAULT_CONTENT_TYPE = "smtp-content-type";
	public static final String PROP_EMAIL_SERVICE_FORGOT_PASSWORD = "smtp-service-forgot-password-enable";
	public static final String PROP_EMAIL_SERVICE_FORGOT_PASSWORD_CHALLENGE_TIMEOUT = "smtp-service-forgot-password-challenge-timeout";
	public static final String PROP_EMAIL_SERVICE_FORGOT_PASSWORD_CHALLENGE_RETRIES = "smtp-service-forgot-password-challenge-retries";
	public static final String PROP_EMAIL_SERVICE_FORGOT_PASSWORD_IP_LOCK_DURATION = "smtp-service-forgot-password-ip-lock-duration";
	public static final String PROP_ENCRYPTION_ALGORITHM = "encryption-algorithm";
	public static final String PROP_EXTERNAL_LINK_NEW_WINDOW = "external-link-new-window";
	public static final String PROP_FILE_BLACKLIST = "file-blacklist";
	public static final String PROP_FILE_BLACKLIST_TYPE = "file-blacklist-type";
	public static final String PROP_FILE_DIR_FULL_PATH = "file-dir-full-path";
	public static final String PROP_FILE_DIR_RELATIVE_PATH = "file-dir-relative-path";
	public static final String PROP_FILE_MAX_FILE_SIZE = "max-file-size";
	public static final String PROP_FILE_SERVER_URL = "file-server-url";
	public static final String PROP_FILE_UPLOAD_STORAGE = "file-upload-storage";
	public static final String PROP_FILE_WHITELIST = "file-whitelist";
	public static final String PROP_HONEYPOT_ACCESS_KEY = "honeypot-access-key";
	public static final String PROP_HONEYPOT_FILTER_ENABLED = "honeypot-enabled";
	public static final String PROP_IMAGE_RESIZE_INCREMENT = "image-resize-increment";
	public static final String PROP_MAX_RECENT_CHANGES = "max-recent-changes";
	public static final String PROP_MAX_TOPIC_VERSION_EXPORT = "max-topic-version-export";
	public static final String PROP_PARSER_ALLOW_CAPITALIZATION = "allow-capitalization";
	public static final String PROP_PARSER_ALLOW_HTML = "allowHTML";
	public static final String PROP_PARSER_ALLOW_JAVASCRIPT = "allow-javascript";
	public static final String PROP_PARSER_ALLOW_TEMPLATES = "allow-templates";
	public static final String PROP_PARSER_CLASS = "parser";
	public static final String PROP_PARSER_DISPLAY_INTERWIKI_LINKS_INLINE = "parser-interwiki-links-inline";
	public static final String PROP_PARSER_DISPLAY_SPECIAL_PAGE_VIRTUAL_WIKI_LINKS = "display-special-virtual-wiki";
	public static final String PROP_PARSER_DISPLAY_VIRTUALWIKI_LINKS_INLINE = "parser-virtualwiki-links-inline";
	/** Maximum number of template inclusions allowed on a page. */
	public static final String PROP_PARSER_MAX_INCLUSIONS = "parser-max-inclusions";
	/** This constant controls how many infinite loops a topic can hold before parsing aborts. */
	public static final String PROP_PARSER_MAXIMUM_INFINITE_LOOP_LIMIT = "parser-infinite-loop-limit";
	/** Maximum number of parser iterations allowed for a single parsing run. */
	public static final String PROP_PARSER_MAX_PARSER_ITERATIONS = "parser-max-iterations";
	/** Maximum depth to which templates can be included for a single parsing run. */
	public static final String PROP_PARSER_MAX_TEMPLATE_DEPTH = "parser-max-template-depth";
	public static final String PROP_PARSER_SIGNATURE_DATE_PATTERN = "signature-date";
	public static final String PROP_PARSER_SIGNATURE_USER_PATTERN = "signature-user";
	public static final String PROP_PARSER_TOC = "allow-toc";
	public static final String PROP_PARSER_TOC_DEPTH = "toc-depth";
	public static final String PROP_PARSER_USE_NUMBERED_HTML_LINKS = "use-numbered-html-links";
	public static final String PROP_PATTERN_INVALID_NAMESPACE_NAME = "pattern-namespace-name-invalid";
	public static final String PROP_PATTERN_INVALID_ROLE_NAME = "pattern-role-name-invalid";
	public static final String PROP_PATTERN_INVALID_TOPIC_PATTERN = "pattern-topic-name-invalid";
	public static final String PROP_PATTERN_VALID_USER_LOGIN = "pattern-login-valid";
	public static final String PROP_PATTERN_VALID_VIRTUAL_WIKI = "pattern-virtualwiki-valid";
	public static final String PROP_PRINT_NEW_WINDOW = "print-new-window";
	public static final String PROP_RECAPTCHA_EDIT = "recaptcha-edit";
	public static final String PROP_RECAPTCHA_PRIVATE_KEY = "recaptcha-private-key";
	public static final String PROP_RECAPTCHA_PUBLIC_KEY = "recaptcha-public-key";
	public static final String PROP_RECAPTCHA_REGISTER = "recaptcha-register";
	public static final String PROP_RECENT_CHANGES_NUM = "recent-changes-days";
	public static final String PROP_RSS_ALLOWED = "rss-allowed";
	public static final String PROP_RSS_TITLE = "rss-title";
	public static final String PROP_SERVER_URL = "server-url";
	public static final String PROP_SHARED_UPLOAD_VIRTUAL_WIKI = "shared-upload-virtual-wiki";
	public static final String PROP_SITE_NAME = "site-name";
	public static final String PROP_TOPIC_EDITOR = "default-editor";
	public static final String PROP_TOPIC_SPAM_FILTER = "use-spam-filter";
	public static final String PROP_TOPIC_USE_PREVIEW = "use-preview";
	public static final String PROP_TOPIC_USE_SHOW_CHANGES = "use-show-changes";
	public static final String PROP_VIRTUAL_WIKI_DEFAULT = "virtual-wiki-default";
	public static final String PROP_ROLE_ADMIN = "role-admin";
	public static final String PROP_ROLE_ANONYMOUS = "role-anonymous";
	public static final String PROP_ROLE_EDIT_EXISTING = "role-edit-existing";
	public static final String PROP_ROLE_EDIT_NEW = "role-edit-new";
	public static final String PROP_ROLE_EMBEDDED = "role-embedded";
	public static final String PROP_ROLE_IMPORT = "role-import";
	public static final String PROP_ROLE_MOVE = "role-move";
	public static final String PROP_ROLE_NO_ACCOUNT = "role-no-account";
	public static final String PROP_ROLE_SYSADMIN = "role-sysadmin";
	public static final String PROP_ROLE_TRANSLATE = "role-translate";
	public static final String PROP_ROLE_UPLOAD = "role-upload";
	public static final String PROP_ROLE_VIEW = "role-view";
	public static final String PROP_ROLE_REGISTER = "role-register";
    public static final String PROP_ADMIN_USER = "admin-user";
    public static final String PROP_ADMIN_PWD = "admin-pwd";
    
	// Lookup properties file location from system properties first.
	private static final String PROPERTY_FILE_NAME = System.getProperty("jamwiki.property.file", "jamwiki.properties");

    private static final String BOOL_TRUE = Boolean.TRUE.toString();
    private static final String BOOL_FALSE = Boolean.FALSE.toString();

	private static Environment INSTANCE = null;
	private Properties defaults = null;
	private SortedProperties props = null;

	/**
	 * The constructor loads property values from the property file.
	 */
	private Environment() {
		initDefaultProperties();
		log.debug("Default properties initialized: " + defaults.toString());
        props = new SortedProperties(defaults);
		props.putAll(loadProperties((filePath) -> findWikiPropertyFile(filePath), PROPERTY_FILE_NAME));
		if ("true".equals(System.getProperty("jamwiki.override.file.properties"))) {
			overrideFromSystemProperties();
		}
		log.debug("JAMWiki properties initialized: " + props.toString());
	}

	/**
	* Overrides file properties from system properties. Iterates over all properties
	* and checks if application server has defined overriding property. System wide
	* properties are prefixed with "jamwiki". These properties may be used to define
	* dynamic runtime properties (eg. upload path depends on environment).
	*/
	private void overrideFromSystemProperties() {
		log.info("Overriding file properties with system properties.");
		Map<String, String> properties = propertiesToMap(props);
		for (String key : properties.keySet()) {
			String value = System.getProperty("jamwiki." + key);
			if (value != null) {
				props.setProperty(key, value);
				log.info("Replaced property " + key + " with value: " + value);
			}
		}
	}

	/**
	 * Convert a Properties object to a Map object.
	 */
    @SuppressWarnings("unchekced")
	private static Map<String, String> propertiesToMap(Properties properties) {
        return (Map<String, String>)(Map<?, ?>)properties;
	}

    /**
     * Check if all required properties without defaults are set
     * @return true if all required properties are set (i.e. not empty)
     */
    public static boolean isCompleted() {
        Properties env = getInstance();
        if (StringUtils.isBlank(env.getProperty(PROP_ADMIN_USER))) return false;
        if (StringUtils.isBlank(env.getProperty(PROP_ADMIN_PWD))) return false;     // blank pwd not allowed!
        if (StringUtils.isBlank(env.getProperty(PROP_BASE_FILE_DIR))) return false;
        if (StringUtils.isBlank(env.getProperty(PROP_FILE_DIR_FULL_PATH))) return false;
        if (StringUtils.isBlank(env.getProperty(PROP_FILE_DIR_RELATIVE_PATH))) return false;
        if (StringUtils.isBlank(env.getProperty(PROP_SERVER_URL))) return false;
        if (env.getProperty(PROP_BASE_PERSISTENCE_TYPE).equals(WikiBase.PERSISTENCE_EXTERNAL)) {
            if (StringUtils.isBlank(env.getProperty(PROP_DB_DRIVER))) return false;
            if (StringUtils.isBlank(env.getProperty(PROP_DB_URL))) return false;
            if (StringUtils.isBlank(env.getProperty(PROP_DB_USERNAME))) return false;
            if (StringUtils.isBlank(env.getProperty(PROP_DB_PASSWORD))) return false;
        }
        return true;
    }

	/**
	 * Initialize the default property values.
	 */
	private void initDefaultProperties() {
		defaults = new Properties();
        
        // required properties without defaults
		defaults.setProperty(PROP_BASE_FILE_DIR, "");
		defaults.setProperty(PROP_FILE_DIR_FULL_PATH, "");
		defaults.setProperty(PROP_FILE_DIR_RELATIVE_PATH, "");
		defaults.setProperty(PROP_SERVER_URL, "");
        
        // required properties with defaults
		defaults.setProperty(PROP_FILE_UPLOAD_STORAGE, WikiBase.UPLOAD_STORAGE.DOCROOT.toString());
		defaults.setProperty(PROP_BASE_PERSISTENCE_TYPE, WikiBase.PERSISTENCE_INTERNAL);
		defaults.setProperty(PROP_DB_TYPE, QueryHandler.QUERY_HANDLER_HSQL);

        // conditionally required properties without defaults
		defaults.setProperty(PROP_DB_DRIVER, "");
		defaults.setProperty(PROP_DB_URL, "");
		defaults.setProperty(PROP_DB_USERNAME, "");
		defaults.setProperty(PROP_DB_PASSWORD, "");
        
        // optional properties without defaults
		defaults.setProperty(PROP_BASE_META_DESCRIPTION, "");
		defaults.setProperty(PROP_EMAIL_SMTP_HOST,"");
		defaults.setProperty(PROP_EMAIL_SMTP_USERNAME,"");
		defaults.setProperty(PROP_EMAIL_SMTP_PASSWORD,"");
		defaults.setProperty(PROP_EMAIL_REPLY_ADDRESS,"");
		defaults.setProperty(PROP_FILE_SERVER_URL, "");

        // other properties
        defaults.setProperty(PROP_BASE_COOKIE_EXPIRE, "31104000");
		defaults.setProperty(PROP_BASE_DEFAULT_TOPIC, "StartingPoints");
		defaults.setProperty(PROP_BASE_INITIALIZED, BOOL_FALSE);
		defaults.setProperty(PROP_BASE_LOGO_IMAGE, "logo.gif");
		defaults.setProperty(PROP_BASE_SEARCH_ENGINE, SearchEngine.SEARCH_ENGINE_LUCENE);
		defaults.setProperty(PROP_BASE_WIKI_VERSION, "2.0.0");
		defaults.setProperty(PROP_DBCP_MAX_ACTIVE, "15");
		defaults.setProperty(PROP_DBCP_MAX_IDLE, "15");
		defaults.setProperty(PROP_DBCP_MAX_OPEN_PREPARED_STATEMENTS, "20");
		defaults.setProperty(PROP_DBCP_MIN_EVICTABLE_IDLE_TIME, "600");
		defaults.setProperty(PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN, "5");
		defaults.setProperty(PROP_DBCP_POOL_PREPARED_STATEMENTS, BOOL_TRUE);
		defaults.setProperty(PROP_DBCP_TEST_ON_BORROW, BOOL_TRUE);
		defaults.setProperty(PROP_DBCP_TEST_ON_RETURN, BOOL_FALSE);
		defaults.setProperty(PROP_DBCP_TEST_WHILE_IDLE, BOOL_FALSE);
		defaults.setProperty(PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS, "120");
		defaults.setProperty(PROP_DBCP_WHEN_EXHAUSTED_ACTION, "0");  //org.apache.commons.pool.impl.GenericObjectPool.WHEN_EXHAUSTED_GROW
		defaults.setProperty(PROP_EMAIL_SMTP_ENABLE,BOOL_FALSE);
		defaults.setProperty(PROP_EMAIL_SMTP_REQUIRES_AUTH,BOOL_FALSE);
		defaults.setProperty(PROP_EMAIL_SMTP_USE_SSL, BOOL_FALSE);
		defaults.setProperty(PROP_EMAIL_SMTP_PORT,"25");
		defaults.setProperty(PROP_EMAIL_ADDRESS_SEPARATOR,";");
		defaults.setProperty(PROP_EMAIL_DEFAULT_CONTENT_TYPE,"text/plain");
		defaults.setProperty(PROP_EMAIL_SERVICE_FORGOT_PASSWORD, BOOL_FALSE);
		defaults.setProperty(PROP_EMAIL_SERVICE_FORGOT_PASSWORD_CHALLENGE_TIMEOUT, "60"); // minutes
		defaults.setProperty(PROP_EMAIL_SERVICE_FORGOT_PASSWORD_CHALLENGE_RETRIES, "3");
		defaults.setProperty(PROP_EMAIL_SERVICE_FORGOT_PASSWORD_IP_LOCK_DURATION, "1440"); // minutes = 24h
		defaults.setProperty(PROP_ENCRYPTION_ALGORITHM, "SHA-512");
		defaults.setProperty(PROP_EXTERNAL_LINK_NEW_WINDOW, BOOL_FALSE);
		defaults.setProperty(PROP_FILE_BLACKLIST, "bat,bin,exe,htm,html,js,jsp,php,sh");
		defaults.setProperty(PROP_FILE_BLACKLIST_TYPE, String.valueOf(WikiBase.UPLOAD_BLACKLIST));
		defaults.setProperty(PROP_FILE_MAX_FILE_SIZE, "5000000");   // size is in bytes
		defaults.setProperty(PROP_FILE_WHITELIST, "bmp,gif,jpeg,jpg,pdf,png,properties,svg,txt,zip");
		defaults.setProperty(PROP_HONEYPOT_ACCESS_KEY, "");
		defaults.setProperty(PROP_HONEYPOT_FILTER_ENABLED, BOOL_FALSE);
		defaults.setProperty(PROP_IMAGE_RESIZE_INCREMENT, "100");
		defaults.setProperty(PROP_MAX_RECENT_CHANGES, "10000");
		defaults.setProperty(PROP_MAX_TOPIC_VERSION_EXPORT, "1000");
		defaults.setProperty(PROP_PARSER_ALLOW_CAPITALIZATION, BOOL_TRUE);
		defaults.setProperty(PROP_PARSER_ALLOW_HTML, BOOL_TRUE);
		defaults.setProperty(PROP_PARSER_ALLOW_JAVASCRIPT, BOOL_FALSE);
		defaults.setProperty(PROP_PARSER_ALLOW_TEMPLATES, BOOL_TRUE);
		defaults.setProperty(PROP_PARSER_CLASS, "org.jamwiki.parser.jflex.JFlexParser");
		defaults.setProperty(PROP_PARSER_DISPLAY_INTERWIKI_LINKS_INLINE, BOOL_FALSE);
		defaults.setProperty(PROP_PARSER_DISPLAY_SPECIAL_PAGE_VIRTUAL_WIKI_LINKS, BOOL_TRUE);
		defaults.setProperty(PROP_PARSER_DISPLAY_VIRTUALWIKI_LINKS_INLINE, BOOL_FALSE);
		defaults.setProperty(PROP_PARSER_MAX_INCLUSIONS, "250");
		defaults.setProperty(PROP_PARSER_MAXIMUM_INFINITE_LOOP_LIMIT, "5");
		defaults.setProperty(PROP_PARSER_MAX_PARSER_ITERATIONS, "100");
		defaults.setProperty(PROP_PARSER_MAX_TEMPLATE_DEPTH, "100");
		defaults.setProperty(PROP_PARSER_SIGNATURE_DATE_PATTERN, "HH:mm, dd MMMM yyyy (z)");
		defaults.setProperty(PROP_PARSER_SIGNATURE_USER_PATTERN, "[[{0}|{4}]]");
		defaults.setProperty(PROP_PARSER_TOC, BOOL_TRUE);
		defaults.setProperty(PROP_PARSER_TOC_DEPTH, "5");
		defaults.setProperty(PROP_PARSER_USE_NUMBERED_HTML_LINKS, BOOL_TRUE);
		defaults.setProperty(PROP_PATTERN_INVALID_NAMESPACE_NAME, "([\\n\\r\\\\<>\\[\\]\\:_%/?&#]+)");
		defaults.setProperty(PROP_PATTERN_INVALID_ROLE_NAME, "([A-Za-z0-9_]+)");
		defaults.setProperty(PROP_PATTERN_INVALID_TOPIC_PATTERN, "[\\n\\r\\\\<>\\[\\]?#]");
		defaults.setProperty(PROP_PATTERN_VALID_USER_LOGIN, "([A-Za-z0-9_]+)");
		defaults.setProperty(PROP_PATTERN_VALID_VIRTUAL_WIKI, "([A-Za-z0-9_]+)");
		defaults.setProperty(PROP_PRINT_NEW_WINDOW, BOOL_FALSE);
		defaults.setProperty(PROP_RECAPTCHA_EDIT, "0");
		defaults.setProperty(PROP_RECAPTCHA_PRIVATE_KEY, "");
		defaults.setProperty(PROP_RECAPTCHA_PUBLIC_KEY, "");
		defaults.setProperty(PROP_RECAPTCHA_REGISTER, "0");
		defaults.setProperty(PROP_RECENT_CHANGES_NUM, "100");
		defaults.setProperty(PROP_RSS_ALLOWED, BOOL_TRUE);
		defaults.setProperty(PROP_RSS_TITLE, "Wiki Recent Changes");
		defaults.setProperty(PROP_SHARED_UPLOAD_VIRTUAL_WIKI, "");
		defaults.setProperty(PROP_SITE_NAME, "JamWiki2");
		defaults.setProperty(PROP_TOPIC_EDITOR, "toolbar");     // FIXME - hard coding
		defaults.setProperty(PROP_TOPIC_SPAM_FILTER, BOOL_TRUE);
		defaults.setProperty(PROP_TOPIC_USE_PREVIEW, BOOL_TRUE);
		defaults.setProperty(PROP_TOPIC_USE_SHOW_CHANGES, BOOL_TRUE);
		defaults.setProperty(PROP_VIRTUAL_WIKI_DEFAULT, "en");
		defaults.setProperty(PROP_ROLE_ADMIN, "ROLE_ADMIN");
		defaults.setProperty(PROP_ROLE_ANONYMOUS, "ROLE_ANONYMOUS");
		defaults.setProperty(PROP_ROLE_EDIT_EXISTING, "ROLE_EDIT_EXISTING");
		defaults.setProperty(PROP_ROLE_EDIT_NEW, "ROLE_EDIT_NEW");
		defaults.setProperty(PROP_ROLE_EMBEDDED, "ROLE_EMBEDDED");
		defaults.setProperty(PROP_ROLE_IMPORT, "ROLE_IMPORT");
		defaults.setProperty(PROP_ROLE_MOVE, "ROLE_MOVE");
		defaults.setProperty(PROP_ROLE_NO_ACCOUNT, "ROLE_NO_ACCOUNT");
		defaults.setProperty(PROP_ROLE_SYSADMIN, "ROLE_SYSADMIN");
		defaults.setProperty(PROP_ROLE_TRANSLATE, "ROLE_TRANSLATE");
		defaults.setProperty(PROP_ROLE_UPLOAD, "ROLE_UPLOAD");
		defaults.setProperty(PROP_ROLE_VIEW, "ROLE_VIEW");
		defaults.setProperty(PROP_ROLE_REGISTER, "ROLE_REGISTER");
	}

	/**
	 * Return an instance of the current properties object.  The property instance
	 * returned should not be directly modified.
	 *
	 * @return Returns an instance of the current system properties.
	 */
	public static Properties getInstance() {
		if (INSTANCE == null) {
			// initialize the singleton instance
			INSTANCE = new Environment();
		}
		return INSTANCE.props;
	}

	/**
	 * Get the value of a boolean property.
	 * Returns <code>true</code> if the property is equal, ignoring case,
	 * to the string "true".
	 * Returns false in all other cases (eg: "false", "yes", "1")
	 *
	 * @param name The name of the property whose value is to be retrieved.
	 * @return The value of the property.
	 */
	public static boolean getBooleanValue(String name) {
		return Boolean.valueOf(getValue(name));
	}

	/**
	 * Get the value of an integer property.
	 *
	 * @param name The name of the property whose value is to be retrieved.
	 * @return The value of the property.
	 */
	public static int getIntValue(String name) {
		int value = NumberUtils.toInt(getValue(name), -1);
		if (value == -1) {
			log.warn("Invalid integer property " + name + " with value " + value);
		}
		// FIXME - should this otherwise indicate an invalid property?
		return value;
	}

	/**
	 * Get the value of a long property.
	 *
	 * @param name The name of the property whose value is to be retrieved.
	 * @return The value of the property.
	 */
	public static long getLongValue(String name) {
		long value = NumberUtils.toLong(getValue(name), -1);
		if (value == -1) {
			log.warn("Invalid long property " + name + " with value " + value);
		}
		// FIXME - should this otherwise indicate an invalid property?
		return value;
	}

	/**
	 * Returns the value of a property.
	 *
	 * @param name The name of the property whose value is to be retrieved.
	 * @return The value of the property.
	 */
	public static String getValue(String name) {
		return getInstance().getProperty(name);
	}

	/**
	 * Return <code>true</code> if wiki properties have been initialized,
	 * <code>false</code> otherwise.
	 *
	 * @return <code>true</code> if wiki properties have been initialized,
	 * <code>false</code> otherwise.
	 */
	public static boolean isInitialized() {
		return getBooleanValue(Environment.PROP_BASE_INITIALIZED);
	}
    
    /**
     * Declare the wiki properties as initialized.
     */
    protected static void setInitialized() {
        setBooleanValue(Environment.PROP_BASE_INITIALIZED, true);
    }
    
	/**
	 * Given a property file name, load the property file and return an object
	 * representing the property values.
	 *
	 * @param propertyFile The name of the property file to load.
	 * @return The loaded SortedProperties object.
	 */
	public static SortedProperties loadProperties(String propertyFile) {
		return loadProperties(propertyFile, new Properties());
	}

	public static SortedProperties loadProperties(String propertyFile, Properties def) {
		return loadProperties((filePath) -> findPropertyFile(filePath), propertyFile, def);
	}

    private static SortedProperties loadProperties(Function<String, File> fileFinder, String propertyFile) {
        return loadProperties(fileFinder, propertyFile, new Properties());
    }
    /**
	 * Given a property file name, load the property file and return an object
	 * representing the property values.
	 *
	 * @param propertyFile The name of the property file to load.
	 * @param def Default property values, or <code>null</code> if there are no defaults.
	 * @return The loaded SortedProperties object.
	 */
	private static SortedProperties loadProperties(Function<String, File> fileFinder, String propertyFile, Properties def) {
		SortedProperties properties = new SortedProperties(def);
		File file;
		FileInputStream fis = null;
		try {
			file = fileFinder.apply(propertyFile);
			if (file == null) {
				log.warn("Property file " + propertyFile + " does not exist");
			} else if (!file.exists()) {
				log.warn("Property file " + file.getPath() + " does not exist");
			} else {
				log.info("Loading properties from " + file.getPath());
				fis = new FileInputStream(file);
				properties.load(fis);
			}
		} catch (Exception e) {
			log.error("Failure while trying to load wiki property file", e);
		} finally {
			IOUtils.closeQuietly(fis);
		}
		return properties;
	}
    
	/**
	 * Find the initial wiki property file.
	 *
	 * @param fileSpec The path of the property file to be loaded.  This path can be
	 *  either absolute or relative to the directory from which the JVM was loaded.
	 * @return A File object containing the properties file instance.
	 * @throws IOException Thrown if the specified property file cannot
	 *  be located.
	 */
	private static File findWikiPropertyFile(String fileSpec) {

		File propFile = new File(fileSpec);
        
        if (propFile.exists()) {
            if (propFile.isFile() && propFile.canRead()) {
                return propFile;  // absolute | relative, existing & accessible property file
            } else {
                throw new RuntimeException(String.format("Property file not accessible: %s", fileSpec));
            }
        }
        // assert(!propFile.exists());
        if (propFile.isAbsolute()) {
            throw new RuntimeException(String.format("Property file not found: %s", fileSpec));
        }
        // assert(!propFile.exists() && !propFile.isAbsolute());
        // non-existing relative property file:
        // try default wiki properties path
        propFile = new File("data", "jamwiki.properties");
        
        if (propFile.exists()) {
            if (propFile.isFile() && propFile.canRead()) {
                return propFile;
            } else {
                throw new RuntimeException(String.format("Property file not accessible: %s", fileSpec));
            }
        }
                
		//- DONT: search for file in class loader path
		//- return retrievePropertyFile(fileSpec);
        return null;
	}

	/**
	 * Find a property file.
	 *
	 * @param fileSpec The path of the property file to be loaded.  This path can be
	 *  either absolute or relative to the directory from which the JVM was loaded.
	 * @return A File object containing the properties file instance.
	 * @throws IOException Thrown if the specified property file cannot
	 *  be located.
	 */
	private static File findPropertyFile(String fileSpec) {

		File propFile = new File(fileSpec);
        
        if (propFile.exists()) {
            if (propFile.isFile() && propFile.canRead()) {
                return propFile;  // absolute | relative, existing & accessible property file
            } else {
                throw new RuntimeException(String.format("Property file not accessible: %s", fileSpec));
            }
        }
        // assert(!propFile.exists());
        if (propFile.isAbsolute()) {
            throw new RuntimeException(String.format("Property file not found: %s", fileSpec));
        }
        // assert(!propFile.exists() && !propFile.isAbsolute());
        // non-existing relative property file:
        // try to find it in BASE_FILE_DIR before searching classpath
        String fileName = propFile.getName();
        propFile = new File(getValue(PROP_BASE_FILE_DIR), fileName);
        
        if (propFile.exists()) {
            if (propFile.isFile() && propFile.canRead()) {
                return propFile;
            } else {
                throw new RuntimeException(String.format("Property file not accessible: %s", fileSpec));
            }
        }
                
        // search for file in class loader path
		return findClasspathPropertyFile(fileSpec);
	}

	/**
	 * Find property file from the class path. Based on
	 * code from the org.apache.log4j.helpers.Loader class.
	 *
	 * @param filename Given a filename return a File object for the file.  The filename
	 *  may be relative to the class path or the directory from which the JVM was
	 *  initialized.
	 * @return Returns a file representing the filename, or <code>null</code> if
	 *  the file cannot be found.
	 */
	private static File findClasspathPropertyFile(String filename) {
		try {
			return ResourceUtil.getClassLoaderFile(filename);
		} catch (IOException e) {
			// NOPMD file might not exist
		}
		try {
			return new File(ResourceUtil.getClassLoaderRoot(), filename);
		} catch (IOException e) {
			log.error("Error while searching for resource " + filename, e);
		}
		return null;
	}

	/**
	 * Persist the current wiki configuration and reload all values.
	 *
	 */
	public static void saveConfiguration() {
		try {
			saveWikiProperties(getInstance(), null);
			// do not use WikiBase.getDataHandler() directly since properties are
			// being changed
			WikiBase.getDataHandler().writeConfiguration(propertiesToMap(getInstance()));
		} catch (WikiException | IOException | DataAccessException e) {
            log.error("Error while saving configuration: " + e.toString());
			throw new WikiException(new WikiMessage("error.unknown", e.getMessage()));
		}
	}

	/**
	 * Save property values to file jamwiki.properties in the default location.
	 *
	 * @param properties The properties object that is to be saved.
	 * @param comments A comment to save in the properties file.
	 * @throws IOException Thrown if the file cannot be found or if an I/O
	 *  error occurs.
	 */
	private static void saveWikiProperties(Properties properties, String comments) throws IOException {
		File file = new File(getValue(PROP_BASE_FILE_DIR), "jamwiki.properties");
        storeProperties(file, properties, comments);
	}
    
    private static void storeProperties(File file, Properties properties, String comments) throws IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			properties.store(out, comments);
		} finally {
			IOUtils.closeQuietly(out);
		}
    }
    
	/**
	 * * Save property values to the specified file.
	 *
     * @param propertyFile The name of the property file to save.
     * @param properties The properties object that is to be saved.
	 * @param comments A comment to save in the properties file.
	 * @throws IOException Thrown if the file cannot be found or if an I/O
	 *  error occurs.
	 */
    public static void saveProperties(String propertyFile, Properties properties, String comments) throws IOException {
        File file = findPropertyFile(propertyFile);
		storeProperties(file, properties, comments);
    }

	/**
	 * Set a new boolean value for the given property name.
	 *
	 * @param name The name of the property whose value is to be set.
	 * @param value The value of the property being set.
	 */
	public static void setBooleanValue(String name, boolean value) {
		getInstance().setProperty(name, Boolean.toString(value));
	}

	/**
	 * Sets a new integer value for the given property name.
	 *
	 * @param name The name of the property whose value is to be set.
	 * @param value The value of the property being set.
	 */
	public static void setIntValue(String name, int value) {
		getInstance().setProperty(name, Integer.toString(value));
	}

	/**
	 * Sets a new value for the given property name.
	 *
	 * @param name The name of the property whose value is to be set.
	 * @param value The value of the property being set.
	 */
	public static void setValue(String name, String value) {
		// it is invalid to set a property value null, so convert to empty string
		if (value == null) {
			value = "";
		}
		getInstance().setProperty(name, value);
	}
}
