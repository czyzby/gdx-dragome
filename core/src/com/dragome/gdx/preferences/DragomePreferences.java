
package com.dragome.gdx.preferences;

import java.util.Map;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.dragome.commons.javascript.ScriptHelper;

/** Dragome implementation of {@link Preferences} when local storage is available. Name of the preferences and its keys have to
 * contain valid characters - they will be used as keys in local storage.
 *
 * @author MJ */
public class DragomePreferences extends AbstractPreferences {
	private final String name;

	/** @param name used to identify preferences in local storage. Has to contain valid local storage key characters. */
	public DragomePreferences (final String name) {
		if (name == null || name.length() == 0) {
			throw new GdxRuntimeException("Preferences name cannot be empty.");
		}
		this.name = name + ".";
	}

	@Override
	public Preferences putString (final String key, final String val) {
		ScriptHelper.put("_key", toKey(key), this);
		ScriptHelper.put("_val", val, this);
		ScriptHelper.eval("localStorage.setItem(_key,_val)", this);
		return this;
	}

	/** @param key name of a specific preference.
	 * @return actual key value of the preference. */
	protected String toKey (final String key) {
		return name + key;
	}

	@Override
	public String getString (final String key) {
		ScriptHelper.put("_key", toKey(key), this);
		final Object preference = ScriptHelper.eval("localStorage.getItem(_key)", this);
		return preference == null ? null : String.valueOf(preference);
	}

	@Override
	public boolean contains (final String key) {
		ScriptHelper.put("_key", toKey(key), this);
		return ScriptHelper.eval("localStorage.getItem(_key)", this) != null;
	}

	@Override
	public void remove (final String key) {
		ScriptHelper.put("_key", toKey(key), this);
		ScriptHelper.eval("localStorage.removeItem(_key)", this);
	}

	@Override
	public void clear () {
		ScriptHelper.put("_name", name, this);
		// From length-1 to avoid stale index. When starting with 0, var 'i' has to be decremented after each removal.
		ScriptHelper.eval(
			"for(var i=localStorage.length-1;i>=0;i--){var key=localStorage.key(i);if(key.startsWith(_name)){localStorage.removeItem(key);}}",
			this);
	}

	@Override
	protected void fillPreferences (final Map<String, Object> preferences) {
		for (int i = 0, length = ScriptHelper.evalInt("localStorage.length", this); i < length; i++) {
			ScriptHelper.put("_i", i, this);
			final String key = String.valueOf(ScriptHelper.eval("localStorage.key(_i)", this));
			if (key.startsWith(name)) {
			preferences.put(key, getString(key));
			}
		}
	}

	@Override
	public void flush () {
		// Storage automatically saves stored preferences.
	}
}
