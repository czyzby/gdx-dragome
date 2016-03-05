
package com.dragome.gdx.graphics.resizing;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

/** Resize callback. Invoked when the application's window is resized. Invokes {@link ApplicationListener#resize(int, int)} only
 * if the canvas was actually affected and only during the rendering.
 * @author MJ */
public class ResizeListener implements Runnable {
	private int lastWidth;
	private int lastHeight;

	public ResizeListener (final int lastWidth, final int lastHeight) {
		this.lastWidth = lastWidth;
		this.lastHeight = lastHeight;
	}

	@Override
	public void run () {
		final int width = Gdx.graphics.getWidth();
		final int height = Gdx.graphics.getHeight();
		if (width != lastWidth || height != lastHeight) {
			Gdx.app.postRunnable(new Resizer(width, height));
			lastWidth = width;
			lastHeight = height;
		}
	}
}
