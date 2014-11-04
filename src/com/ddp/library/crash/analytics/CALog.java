package com.ddp.library.crash.analytics;

import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.ReportField;

import android.app.Application;

/**
 * 
 * Android Application logger with ACRA support
 * @author Zeljko Drascic 
 *
 */
public class CALog {

	private static LogLevel level = LogLevel.Trace;
	private static boolean acra = true;
	
	private final String p;
	
	private static boolean acraInitialized = false;
	
	private CALog(String p/*Logger log*/) {
		this.p = p/*Logger log*/;
		//this.log = log;
	}
	
	public static LogLevel getLevel() {
		return level;
	}
	
	public static CALog getLog(Package p) {
		return new CALog(p.getName());
	}
	
	public void init(Application app) {
			try {
				//level = LogLevel.valueOf(config.get("log.level", LogLevel.Debug.toString()));
				level = LogLevel.Trace;
				android.util.Log.e("LOG", "LogLevel.Trace");
			} catch(Exception e) {
				android.util.Log.e("LOG", "Log setup failed!");
				level = LogLevel.OFF;
			}
			//acra = config.getBoolean("log.acra", acra);
			acra = true;
		
		if(acra) {
			ACRA.init(app);
			info("ACRA reporter started");
			
			ACRAConfiguration ac = ACRA.getConfig();
			
			ReportField[] fields = new ReportField[]{
					ReportField.ANDROID_VERSION,
					ReportField.APP_VERSION_CODE,
					ReportField.APP_VERSION_NAME,
					ReportField.APPLICATION_LOG,
					ReportField.AVAILABLE_MEM_SIZE,
					ReportField.BRAND,
					ReportField.CUSTOM_DATA,
					ReportField.DISPLAY,
					ReportField.ENVIRONMENT,
					ReportField.INSTALLATION_ID,
					ReportField.IS_SILENT,
					ReportField.PACKAGE_NAME,
					ReportField.PHONE_MODEL,
					ReportField.PRODUCT,
					ReportField.REPORT_ID,
					ReportField.TOTAL_MEM_SIZE,
					ReportField.USER_APP_START_DATE,
					ReportField.USER_CRASH_DATE,
					ReportField.USER_IP,
					ReportField.STACK_TRACE
			};
			ac.setCustomReportContent(fields);
			//config.setApplicationLogFile(LOG_LOCATION);
			//config.setApplicationLogFileLines(100);
			ACRA.setConfig(ac);
			
			acraInitialized = true;
		}
	}
	
	/**
	 * @param message
	 * @param args
	 */
	public void trace(String message, Object... args) {
		if(message != null && level.isTrace()) {
			if(args == null || args.length == 0) {
				android.util.Log.v(p, message);
			} else {
				android.util.Log.v(p, format(message, args));
			}
		}
	}
	
	/**
	 * @param message
	 * @param t
	 * @param args
	 */
	public void trace(String message, Throwable t, Object... args) {
		if(message != null && level.isTrace()) {
			if(args == null || args.length == 0) {
				android.util.Log.v(p, message, t);
			} else {
				android.util.Log.v(p, String.format(message, args), t);
			}
		}
	}
	
	/**
	 * @param message
	 * @param args
	 */
	public void debug(String message, Object... args) {
		if(message != null && level.isDebug()) {
			if(args == null || args.length == 0) {
				android.util.Log.d(p, message);
			} else {
				android.util.Log.d(p, format(message, args));
			}
		}
	}
	
	/**
	 * @param message
	 * @param t
	 * @param args
	 */
	public void debug(String message, Throwable t, Object... args) {
		if(message != null && level.isDebug()) {
			if(args == null || args.length == 0) {
				android.util.Log.d(p, message, t);
			} else {
				android.util.Log.d(p, format(message, args), t);
			}
		}
	}
	
	/**
	 * @param message
	 * @param args
	 */
	public void info(String message, Object... args) {
		if(message != null && level.isInfo()) {
			if(args == null || args.length == 0) {
				android.util.Log.i(p, message);
			} else {
				android.util.Log.i(p, format(message, args));
			}
		}
	}
	
	/**
	 * @param message
	 * @param t
	 * @param args
	 */
	public void info(String message, Throwable t, Object... args) {
		if(message != null && level.isInfo()) {
			if(args == null || args.length == 0) {
				android.util.Log.i(p, message, t);
			} else {
				android.util.Log.i(p, format(message, args), t);
			}
		}
	}
	
	/**
	 * @param message
	 * @param args
	 */
	public void warn(String message, Object... args) {
		if(message != null && level.isWarn()) {
			if(args == null || args.length == 0) {
				android.util.Log.w(p, message);
			} else {
				android.util.Log.w(p, format(message, args));
			}
		}
	}
	
	/**
	 * @param message
	 * @param t
	 * @param args
	 */
	public void warn(String message, Throwable t, Object... args){
		if(message != null && level.isWarn()) {
			if(args == null || args.length == 0) {
				android.util.Log.w(p, message, t);
			} else {
				android.util.Log.w(p, format(message, args), t);
			}
		}
	}

	/**
	 * @param message
	 * @param args
	 */
	public void error(String message, Object... args) {
		if(message != null && level.isError()) {
			if(args == null || args.length == 0) {
				android.util.Log.e(p, message);
			} else {
				message = format(message, args);
				android.util.Log.e(p, message);
			}
		}
		if(acra) {
			acrafy(message, new Throwable(message));
		}
	}
	
	/**
	 * @param message
	 * @param t
	 * @param args
	 */
	public void error(String message, Throwable t, Object... args) {
		if(message != null && level.isError()) {
			if(args == null || args.length == 0) {
				android.util.Log.e(p, message, t);
			} else {
				message = format(message, args);
				android.util.Log.e(p, message, t);
			}
		}
		if(acra) {
			acrafy(message, t);
		}
	}
	
	/**
	 * @param message
	 * @param t
	 */
	private void acrafy(String message, Throwable t) {
		if(!acraInitialized) {
			return;
		}
		
		ACRA.getErrorReporter().putCustomData("message", message);
		ACRA.getErrorReporter().handleSilentException(t);
	}
	
	/**
	 * @param message
	 * @param args
	 * @return
	 */
	private String format(String message, Object... args) {
		try {
			return String.format(message, args);
		} catch(Exception e) {
			error("Error in arguments for string formating", e);
			for (Object a: args) {
				message += " " + a;
			}
			return message;
		}
	}
}
