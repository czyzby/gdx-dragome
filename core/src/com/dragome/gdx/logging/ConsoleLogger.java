
package com.dragome.gdx.logging;

import com.dragome.commons.javascript.ScriptHelper;

/** Default Dragome logger. Delegates logs to the console.
 *
 * @author MJ */
public class ConsoleLogger extends AbstractLogger {
	@Override
	public void log (final String tag, final String message) {
		ScriptHelper.put("_tag", tag, this);
		ScriptHelper.put("_msg", message, this);
		ScriptHelper.evalNoResult("console.log(_tag, _msg)", this);
	}

	@Override
	public void log (final String tag, final String message, final Throwable exception) {
		ScriptHelper.put("_tag", tag, this);
		ScriptHelper.put("_msg", message, this);
		ScriptHelper.put("_exp", exception, this);
		ScriptHelper.evalNoResult("console.log(_tag, _msg, _exp)", this);
	}
}
