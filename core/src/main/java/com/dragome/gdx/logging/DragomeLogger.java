
package com.dragome.gdx.logging;

/** Handles logging of a Dragome application.
 *
 * @author MJ
 * @see AbstractLogger
 * @see AlertLogger
 * @see ConsoleLogger
 * @see MixedLogger
 * @see MockUpLogger */
public interface DragomeLogger {
	/** @return current logging level.
	 * @see com.badlogic.gdx.Application#LOG_DEBUG */
	int getLevel ();

	/** @param level logging level.
	 * @see com.badlogic.gdx.Application#LOG_DEBUG */
	void setLevel (int level);

	/** @param level logging level.
	 * @param tag optional logging tag.
	 * @param message message to log. */
	void log (int level, String tag, String message);

	/** @param level logging level.
	 * @param tag optional logging tag.
	 * @param message message to log.
	 * @param exception cause of the log. Can be null. */
	void log (int level, String tag, String message, Throwable exception);
}
