
package com.dragome.gdx.lifecycle;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.utils.ObjectSet;
import com.dragome.commons.javascript.ScriptHelper;

/** Manages {@link LifecycleListener}s. Notified when application's lifecycle events occur.
 *
 * @author MJ */
public class LifecycleManager {
	private final ObjectSet<LifecycleListener> listeners = new ObjectSet<LifecycleListener>();

	public LifecycleManager (final ApplicationListener listener) {
		// Attaching pause/resume listener:
		final EventTarget document = ScriptHelper.evalCasting("document", EventTarget.class, this);
		document.addEventListener("visibilitychange", new EventListener() {
			@Override
			public void handleEvent (final Event evt) {
				if (ScriptHelper.evalBoolean("document.hidden", this)) {
					pause();
					listener.pause();
				} else {
					resume();
					listener.resume();
				}
			}
		}, false);
	}

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

	/** Should be called when application is paused. Notifies lifecycle listeners. */
	public void pause () {
		for (final LifecycleListener listener : listeners) {
			listener.pause();
		}
	}

	/** Should be called when the application is resumed after pausing. Notifies lifecycle listeners. */
	public void resume () {
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
