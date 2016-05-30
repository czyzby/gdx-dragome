
package com.dragome.gdx.lifecycle;

import com.dragome.commons.javascript.ScriptHelper;

/** Contains data about the current client's browser and system. Allows to implement browser- or system-specific code.
 *
 * @author MJ */
public class AgentInfo {
	private final boolean firefox;
	private final boolean chrome;
	private final boolean safari;
	private final boolean opera;
	private final boolean internetExplorer;

	private final boolean mac;
	private final boolean linux;
	private final boolean windows;
	private final boolean android;
	private final boolean iOS;
	private final boolean blackBerry;

	public AgentInfo () {
		final String userAgent = ScriptHelper.evalCasting("navigator.userAgent.toLowerCase()", String.class, this);
		// Browser:
		firefox = userAgent.contains("firefox");
		chrome = userAgent.contains("chrome");
		safari = userAgent.contains("safari");
		opera = userAgent.contains("opera");
		internetExplorer = userAgent.contains("msie");
		// OS:
		mac = userAgent.contains("mac");
		linux = userAgent.contains("linux");
		windows = userAgent.contains("win");
		android = userAgent.contains("android");
		iOS = userAgent.contains("ipad") | userAgent.contains("iphone") || userAgent.contains("ipod");
		blackBerry = userAgent.contains("blackberry") || userAgent.contains("playbook") || userAgent.contains("bb10");
	}

	public boolean isFirefox () {
		return firefox;
	}

	public boolean isChrome () {
		return chrome;
	}

	public boolean isSafari () {
		return safari;
	}

	public boolean isOpera () {
		return opera;
	}

	public boolean isInternetExplorer () {
		return internetExplorer;
	}

	public boolean isMac () {
		return mac;
	}

	public boolean isLinux () {
		return linux;
	}

	public boolean isWindows () {
		return windows;
	}

	public boolean isAndroid () {
		return android;
	}

	public boolean isIOS () {
		return iOS;
	}

	public boolean isBlackBerry () {
		return blackBerry;
	}
}
