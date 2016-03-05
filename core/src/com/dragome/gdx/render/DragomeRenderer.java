
package com.dragome.gdx.render;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.utils.Array;

/** Default {@link Renderer} implementation for Dragome applications. Manages main loop. Note that it always reports 0 for every
 * {@link #getFrameId()} and {@link #getFramesPerSecond()}; if you want to keep track of rendering meta-data, use
 * {@link DebugDragomeRenderer}.
 *
 * @author MJ */
public class DragomeRenderer implements Renderer {
	private final ApplicationListener listener;
	private final Array<Runnable> runnables = new Array<Runnable>();
	private final Array<Runnable> runnablesToInvoke = new Array<Runnable>();
	private float deltaTime;

	/** @param listener will be informed of rendering events. */
	public DragomeRenderer (final ApplicationListener listener) {
		this.listener = listener;
	}

	@Override
	public long getFrameId () {
		return 0L;
	}

	@Override
	public int getFramesPerSecond () {
		return 0;
	}

	@Override
	public float getDeltaTime () {
		return deltaTime;
	}

	@Override
	public void start () {
		// TODO requestAnimationFrame
	}

	@Override
	public void stop () {
		// TODO cancelAnimationFrame
	}

	protected void loop () {
		deltaTime = 1f / 60f; // TODO update/get delta time
		if (runnables.size > 0) {
			runnablesToInvoke.addAll(runnables);
			runnables.clear();
			for (final Runnable runnable : runnablesToInvoke) {
			runnable.run();
			}
			runnablesToInvoke.clear();
		}
		listener.render();
	}

	@Override
	public void postRunnable (final Runnable runnable) {
		runnables.add(runnable);
	}
}
