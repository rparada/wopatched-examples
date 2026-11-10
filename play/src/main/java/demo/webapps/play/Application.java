package demo.webapps.play;

import com.webobjects.foundation.NSLog;

import er.extensions.appserver.ERXApplication;

public class Application extends ERXApplication {
	public static void main(String[] argv) {
		LogUtils.logLog4j2VersionAndLevels();
		ERXApplication.main(argv, Application.class);
	}

	public Application() {
		super();
		NSLog.out.appendln("Welcome to " + name() + " !");
		
		/* ** put your initialization code in here ** */
		
		parsley.Parsley.configure()
			.inlineErrors( isDevelopmentModeSafe() )
			.controls( isDevelopmentModeSafe() )
			.register();
	}
}
