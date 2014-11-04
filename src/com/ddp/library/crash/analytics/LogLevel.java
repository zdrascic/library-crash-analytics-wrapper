package com.ddp.library.crash.analytics;

/**
 * LogLevel enumeration
 * @author Zeljko Drascic
 *
 */
public enum LogLevel {
	Trace,
	Debug,
	Info,
	Warn,
	Error,
	OFF;
	
	public boolean isTrace() {
		return ordinal() <= Trace.ordinal();
	}

	public boolean isDebug() {
		return ordinal() <= Debug.ordinal();
	}
	
	public boolean isInfo() {
		return ordinal() <= Info.ordinal();
	}
	
	public boolean isWarn() {
		return ordinal() <= Warn.ordinal();
	}
	
	public boolean isError() {
		return ordinal() <= Error.ordinal();
	}
}
