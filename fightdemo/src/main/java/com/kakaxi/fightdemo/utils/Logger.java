package com.kakaxi.fightdemo.utils;

public abstract class Logger {
	private static Class<? extends Logger> mLoggerClass = null;
	public static final boolean DBG = true;
	public static final boolean LOG = Log.DBG;
	public static final String TAG = null;
	public static final String LINE = "------->";

	private String mTag;

	public Logger() {
	}

	public Logger(String tag) {
		this.mTag = tag;
	}

	public static void registerLogger(Class<? extends Logger> loggerClass) {
		Logger.mLoggerClass = loggerClass;
	}

	public static void unregisterLogger() {
		Logger.mLoggerClass = null;
	}

	public static Logger getLogger(String tag) {
		tag = "[" + tag + "]";
		Logger logger = null;
		if (mLoggerClass != null) {
			try {
				logger = mLoggerClass.newInstance();
				logger.mTag = tag;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if (logger == null) {
			logger = new Log(tag);
		}
		return logger;
	}

	public String getTag() {
		return mTag;
	}

	public void d(String tag, String str) {
		if(DBG) {
			debug(mTag + LINE + str);
		}
	}

	public void i(String tag, String str) {
		if (DBG) {
			info(mTag + LINE + str);
		}
	}

	public void w(String tag, String str) {
		if (DBG) {
			warn(mTag + LINE + str);
		}
	}

	public void e(String tag, String str) {
		if (DBG) {
			error(mTag + LINE + str);
		}
	}

	public void d(String tag, String str, Throwable tr) {
		if (DBG) {
			debug(mTag + LINE + str, tr);
		}
	}

	public void i(String tag, String str, Throwable tr) {
		if (DBG) {
			info(mTag + LINE + str, tr);
		}
	}

	public void w(String tag, String str, Throwable tr) {
		if (DBG) {
			warn(mTag + LINE + str, tr);
		}
	}

	public void e(String tag, String str, Throwable tr) {
		if (DBG) {
			error(mTag + LINE + str, tr);
		}
	}

	protected abstract void debug(String str);

	protected abstract void info(String str);

	protected abstract void warn(String str);

	protected abstract void error(String str);

	protected abstract void debug(String str, Throwable tr);

	protected abstract void info(String str, Throwable tr);

	protected abstract void warn(String str, Throwable tr);

	protected abstract void error(String str, Throwable tr);
}
