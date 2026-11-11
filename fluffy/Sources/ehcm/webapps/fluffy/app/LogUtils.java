package ehcm.webapps.fluffy.app;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.TreeSet;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;
import org.slf4j.LoggerFactory;

public class LogUtils {
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(LogUtils.class);

	private LogUtils() {
	}

	/**
	 * Logs the reload4j version, the configuration that was loaded, and the level,
	 * additivity, and appenders of every logger reload4j has created. This is the
	 * reload4j equivalent of the log4j2 startup dump.
	 */
	public static void logReload4jVersionAndLevels() {
		Package reload4j = Logger.class.getPackage();
		log.info("reload4j version {}", reload4j.getImplementationVersion());
		log.info("Configuration loaded from {}", configurationLocation());
		logLoggerLevels();
	}

	/**
	 * Resolves the configuration the same way reload4j did at startup: the
	 * {@code log4j.configuration} property when set, otherwise {@code log4j.xml}
	 * and then {@code log4j.properties} on the classpath.
	 */
	private static String configurationLocation() {
		String configured = System.getProperty(LogManager.DEFAULT_CONFIGURATION_KEY);
		URL url;
		if (configured == null) {
			url = Loader.getResource("log4j.xml");
			if (url == null) {
				url = Loader.getResource(LogManager.DEFAULT_CONFIGURATION_FILE);
			}
		}
		else {
			try {
				url = new URL(configured);
			}
			catch (MalformedURLException ex) {
				url = Loader.getResource(configured);
			}
		}
		if (url == null) {
			return configured != null ? configured : LogManager.DEFAULT_CONFIGURATION_FILE;
		}
		if ("file".equals(url.getProtocol())) {
			try {
				return Path.of(url.toURI()).toAbsolutePath().toString();
			}
			catch (URISyntaxException ex) {
				return url.toString();
			}
		}
		return url.toString();
	}

	/**
	 * reload4j creates a logger for the root and for every logger named in the
	 * configuration, plus any logger requested since then. {@code getLevel()} is
	 * null when a logger inherits its level.
	 */
	private static void logLoggerLevels() {
		Logger root = Logger.getRootLogger();
		TreeSet<String> loggerNames = new TreeSet<>();
		Enumeration<?> currentLoggers = LogManager.getCurrentLoggers();
		while (currentLoggers.hasMoreElements()) {
			loggerNames.add(((Logger) currentLoggers.nextElement()).getName());
		}

		StringBuilder lines = new StringBuilder("Logger Levels:");
		appendLoggerLevel(lines, root, "Root");
		for (String loggerName : loggerNames) {
			appendLoggerLevel(lines, LogManager.getLogger(loggerName), loggerName);
		}
		log.info(lines.toString());
	}

	private static void appendLoggerLevel(StringBuilder lines, Logger logger, String displayName) {
		Level level = logger.getLevel();
		lines.append('\n').append(displayName).append(": ");
		if (level != null) {
			lines.append(level);
		}
		else {
			lines.append(logger.getEffectiveLevel()).append(" (inherited)");
		}
		if (!logger.getAdditivity()) {
			lines.append("; additivity=false");
		}
		Enumeration<?> appenders = logger.getAllAppenders();
		if (appenders.hasMoreElements()) {
			lines.append("; appenders=");
			boolean first = true;
			while (appenders.hasMoreElements()) {
				if (!first) {
					lines.append(',');
				}
				first = false;
				lines.append(((Appender) appenders.nextElement()).getName());
			}
		}
	}
}
