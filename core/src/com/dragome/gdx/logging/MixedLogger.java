
package com.dragome.gdx.logging;

/** Delegates logging to the console. If an exception is included, an alert is also displayed.
 *
 * @author MJ */
public class MixedLogger extends ConsoleLogger {
	private final AlertLogger alert = new AlertLogger();

	@Override
	public void log (final String tag, final String message, final Throwable exception) {
		super.log(tag, message, exception);
		alert.log(tag, message, exception);
	}
}
