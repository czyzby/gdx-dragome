
package com.dragome.gdx.lifecycle;

import com.badlogic.gdx.ApplicationListener;

/** Additionally to managing application's main loop, this renderer keeps track of current frame ID and FPS value.
 *
 * @author MJ */
public class DebugDragomeRenderer extends DragomeRenderer {
	private long frameId = -1L;
	private float timePassed;
	private int frames;
	private int fps;

	/** @param listener will be informed of rendering events. */
	public DebugDragomeRenderer (final ApplicationListener listener) {
		super(listener);
	}

	@Override
	public long getFrameId () {
		return frameId;
	}

	@Override
	public int getFramesPerSecond () {
		return fps;
	}

	@Override
	protected void loop () {
		frameId++;
		frames++;
		timePassed += getDeltaTime();
		while (timePassed >= 1f) {
			timePassed -= 1f;
			fps = frames;
			frames = 0;
		}
		super.loop();
	}
}
