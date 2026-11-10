package demo.webapps.fluffy;

import com.webobjects.foundation.NSLog;

import er.extensions.appserver.ERXApplication;

public class Application extends ERXApplication {
	public static void main(String[] argv) {
		LogUtils.logReload4jVersionAndLevels();
		ERXApplication.main(argv, Application.class);
	}

	public Application() {
		super();
		NSLog.out.appendln("Welcome to " + name() + " !");
		/* ** put your initialization code in here ** */
	}
}
