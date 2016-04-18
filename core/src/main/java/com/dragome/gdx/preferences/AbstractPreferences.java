
package com.dragome.gdx.preferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Preferences;

/** Abstract base for {@link Preferences} implementation that keeps all values serialized as strings. Delegates all put and get
 * methods to their string counterparts. Might throw exceptions if getters with no default return values are invoked and requested
 * preference keys are missing.
 *
 * @author MJ */
public abstract class AbstractPreferences implements Preferences {
	@Override
	public Preferences putBoolean (final String key, final boolean val) {
		return putString(key, String.valueOf(val));
	}

	@Override
	public Preferences putInteger (final String key, final int val) {
		return putString(key, String.valueOf(val));
	}

	@Override
	public Preferences putLong (final String key, final long val) {
		return putString(key, String.valueOf(val));
	}

	@Override
	public Preferences putFloat (final String key, final float val) {
		return putString(key, String.valueOf(val));
	}

	@Override
	public Preferences put (final Map<String, ?> vals) {
		for (final Entry<String, ?> entry : vals.entrySet()) {
			putString(entry.getKey(), String.valueOf(entry.getValue()));
		}
		return this;
	}

	@Override
	public boolean getBoolean (final String key) {
		return Boolean.parseBoolean(getString(key));
	}

	@Override
	public int getInteger (final String key) {
		return Integer.parseInt(getString(key));
	}

	@Override
	public long getLong (final String key) {
		return Long.parseLong(getString(key));
	}

	@Override
	public float getFloat (final String key) {
		return Float.parseFloat(key);
	}

	@Override
	public boolean getBoolean (final String key, final boolean defValue) {
		return contains(key) ? getBoolean(key) : defValue;
	}

	@Override
	public int getInteger (final String key, final int defValue) {
		return contains(key) ? getInteger(key) : defValue;
	}

	@Override
	public long getLong (final String key, final long defValue) {
		return contains(key) ? getLong(key) : defValue;
	}

	@Override
	public float getFloat (final String key, final float defValue) {
		return contains(key) ? getFloat(key) : defValue;
	}

	@Override
	public String getString (final String key, final String defValue) {
		return contains(key) ? getString(key) : defValue;
	}

	@Override
	public Map<String, ?> get () {
		final Map<String, Object> preferences = new HashMap<String, Object>();
		fillPreferences(preferences);
		return preferences;
	}

	/** @param preferences an empty {@link Map} implementation that should be filled will all current preferences. */
	protected abstract void fillPreferences (Map<String, Object> preferences);
}
