
package com.dragome.gdx.lifecycle;

import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.utils.ObjectSet;

/** Manages {@link LifecycleListener}s. Notified when application's lifecycle events occur.
 *
 * @author MJ */
public class LifecycleManager {
	private final ObjectSet<LifecycleListener> listeners = new ObjectSet<LifecycleListener>();

	/** @param lifecycleListener will receive lifecycle events. */
	public void addListener (final LifecycleListener lifecycleListener) {
		if (lifecycleListener != null) {
			listeners.add(lifecycleListener);
		}
	}

	/** @param lifecycleListener will no longer receive lifecycle events. */
	public void removeListener (final LifecycleListener lifecycleListener) {
		if (lifecycleListener != null) {
			listeners.remove(lifecycleListener);
		}
	}

	/** Should be called when application is paused. Notifies listeners. */
	public void pause () { // TODO Add pause listener, maybe in Graphics.
		for (final LifecycleListener listener : listeners) {
			listener.pause();
		}
	}

	/** Should be called when the application is resumed after pausing. Notifies listeners. */
	public void resume () { // TODO Add resume listener, maybe in Graphics.
		for (final LifecycleListener listener : listeners) {
			listener.resume();
		}
	}

	/** Should be called before the application is closed. */
	public void dispose () {
		for (final LifecycleListener listener : listeners) {
			listener.dispose();
		}
	}
}
