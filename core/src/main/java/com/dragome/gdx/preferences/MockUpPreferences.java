
package com.dragome.gdx.preferences;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.dragome.gdx.DragomeApplication;

/** Maintains a map of preferences mapped to string keys. Preferences are not actually saved. Used when local storage is not
 * supported.
 *
 * @author MJ */
public class MockUpPreferences extends AbstractPreferences {
	private final Map<String, String> preferences = new HashMap<String, String>();

	@Override
	public Preferences putString (final String key, final String val) {
		preferences.put(key, val);
		return this;
	}

	@Override
	public String getString (final String key) {
		return preferences.get(key);
	}

	@Override
	public boolean contains (final String key) {
		return preferences.containsKey(key);
	}

	@Override
	public void clear () {
		preferences.clear();
	}

	@Override
	public void remove (final String key) {
		preferences.remove(key);
	}

	@Override
	protected void fillPreferences (final Map<String, Object> preferences) {
		preferences.putAll(this.preferences);
	}

	@Override
	public void flush () {
		Gdx.app.error(DragomeApplication.LOGGING_TAG, "Local storage not supported. Preferences cannot be saved.");
	}
}
