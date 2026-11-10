package demo.webapps.play;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {
	private static final Logger log = LoggerFactory.getLogger(LogUtils.class);

	private LogUtils() {
	}

	/**
	 * Logs the Log4j 2 version, the configuration that was loaded, and the level,
	 * additivity, and appenders of every configured logger.
	 */
	public static void logLog4j2VersionAndLevels() {
		log.info("Log4j version {}", PropertiesUtil.class.getPackage().getImplementationVersion());
		LoggerContext context = (LoggerContext) LogManager.getContext(false);
		Configuration configuration = context.getConfiguration();
		log.info("Configuration loaded from {}", configurationLocation(configuration));
		logLoggerLevels(configuration);
	}

	private static String configurationLocation(Configuration configuration) {
		ConfigurationSource source = configuration.getConfigurationSource();
		if (source == null || source.getLocation() == null) {
			return "(none)";
		}
		URI uri = source.getURI();
		if (uri != null && "file".equals(uri.getScheme())) {
			try {
				return Path.of(uri).toAbsolutePath().toString();
			}
			catch (RuntimeException ex) {
				return source.getLocation();
			}
		}
		return source.getLocation();
	}

	/**
	 * A {@link LoggerConfig} exists for every logger in the configuration. This logs
	 * its name, level, additivity, and appenders.
	 */
	private static void logLoggerLevels(Configuration configuration) {
		Map<String, LoggerConfig> loggerConfigs = configuration.getLoggers();
		TreeSet<String> loggerNames = new TreeSet<>(loggerConfigs.keySet());

		StringBuilder lines = new StringBuilder("Logger Levels:");
		for (String loggerName : loggerNames) {
			LoggerConfig loggerConfig = loggerConfigs.get(loggerName);
			String displayName = loggerName.isEmpty() ? "Root" : loggerName;
			lines.append('\n').append(displayName).append(": ").append(loggerConfig.getLevel());
			if (!loggerConfig.isAdditive()) {
				lines.append("; additivity=false");
			}
			List<AppenderRef> appenderRefs = loggerConfig.getAppenderRefs();
			if (!appenderRefs.isEmpty()) {
				lines.append("; appenders=");
				boolean first = true;
				for (AppenderRef appenderRef : appenderRefs) {
					if (!first) {
						lines.append(',');
					}
					first = false;
					lines.append(appenderRef);
				}
			}
		}
		log.info(lines.toString());
	}
}
