
package com.dragome.gdx.logging;

import com.badlogic.gdx.Application;

/** Abstract base for loggers. Handles logging levels. By default, the initial logging level is {@link Application#LOG_INFO}.
 *
 * @author MJ */
public abstract class AbstractLogger implements DragomeLogger {
	private int level = Application.LOG_INFO;

	@Override
	public int getLevel () {
		return level;
	}

	@Override
	public void setLevel (final int level) {
		this.level = level;
	}

	@Override
	public void log (final int level, final String tag, final String message) {
		if (this.level >= level) {
			log(tag, message);
		}
	}

	@Override
	public void log (final int level, final String tag, final String message, final Throwable exception) {
		if (this.level >= level) {
			if (exception == null) {
				log(tag, message);
			} else {
				log(tag, message, exception);
			}
		}
	}

	/** @param tag optional logging tag.
	 * @param message message to log. */
	protected abstract void log (String tag, String message);

	/** @param tag optional logging tag.
	 * @param message message to log.
	 * @param exception cause of the log. Never null. */
	protected abstract void log (String tag, String message, Throwable exception);
}
