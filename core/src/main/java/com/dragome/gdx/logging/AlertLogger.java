
package com.dragome.gdx.logging;

import com.dragome.commons.javascript.ScriptHelper;

/** Delegates all logging to the alert mechanism. Should be used only for debugging.
 *
 * @author MJ */
public class AlertLogger extends AbstractLogger {
	@Override
	public void log (final String tag, final String message) {
		ScriptHelper.put("_tag", tag, this);
		ScriptHelper.put("_msg", message, this);
		ScriptHelper.evalNoResult("alert(_tag+' '+ _msg)", this);
	}

	@Override
	public void log (final String tag, final String message, final Throwable exception) {
		ScriptHelper.put("_tag", tag, this);
		ScriptHelper.put("_msg", message, this);
		ScriptHelper.put("_exp", exception.getMessage(), this);
		ScriptHelper.evalNoResult("console.log(_tag+' '+_msg+' '+_exp)", this);
	}
}
