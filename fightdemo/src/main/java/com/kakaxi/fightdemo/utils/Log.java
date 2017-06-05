package com.kakaxi.fightdemo.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

public class Log extends Logger implements ExtStorageHelper.ExtStorageNotifyListener {


	private static final String APP_TAG = "fightdemo";
	private static final String LOG_FILE_NAME = "test.txt";
	private static PrintStream logStream;
	private static final String LOG_ENTRY_FORMAT = "[%tF %tT]%s";
	private boolean mIsSdMounted = true;

	public Log(String name) {
		super(name);
	}

	@Override
	protected void debug(String str) {
		android.util.Log.d(APP_TAG, str);
		write(str, null);
	}

	@Override
	protected void error(String str) {
		android.util.Log.e(APP_TAG, str);
		write(str, null);
	}

	@Override
	protected void info(String str) {
		android.util.Log.i(APP_TAG, str);
		write(str, null);
	}

	@Override
	protected void warn(String str) {
		android.util.Log.w(APP_TAG, str);
		write(str, null);
	}

	@Override
	protected void debug(String str, Throwable tr) {
		android.util.Log.d(APP_TAG, str);
		write(str, tr);
	}

	@Override
	protected void error(String str, Throwable tr) {
		android.util.Log.e(APP_TAG, str);
		write(str, tr);
	}

	@Override
	protected void info(String str, Throwable tr) {
		android.util.Log.i(APP_TAG, str);
		write(str, tr);
	}

	@Override
	protected void warn(String str, Throwable tr) {
		android.util.Log.w(APP_TAG, str);
		write(str, tr);
	}

	private void write(String msg, Throwable tr) {
		if (!Log.DBG) {
            return;
        }
		try {
			if (mIsSdMounted) {
				if (null == logStream) {
					synchronized (Log.class) {
						if (null == logStream) {
							init();
						}
					}
				}

				Date now = new Date();
				if (null != logStream) {
					logStream.printf(LOG_ENTRY_FORMAT, now, now, msg);
					logStream.print("\n");
					
				}
				if (null != tr) {
					tr.printStackTrace(logStream);
					if (null != logStream) {
						logStream.print("\n");
						
					}
				}
			}
		} catch (Throwable t) {
           
		}
	}

	public static void init() {
		if (!Log.DBG) {
            return;
        }
		try {
			File sdRoot = null;
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				sdRoot = Environment.getExternalStorageDirectory();
			}
			if (sdRoot != null) {
				File logFile = new File(sdRoot, LOG_FILE_NAME);
				android.util.Log.d(APP_TAG, "Log to file : " + logFile);
				logStream = new PrintStream(new FileOutputStream(logFile, true), true);
			}
		} catch (Throwable e) {
           
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			super.finalize();
			if (logStream != null) {
                logStream.close();
            }
		} catch (Throwable t) {
            // Empty catch block
		}
	}

	@Override
	public void onChangeStorageState(boolean bAvailable) {

		mIsSdMounted = bAvailable;
	}
}
