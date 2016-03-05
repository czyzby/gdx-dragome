
package com.dragome.gdx.graphics.resizing;

import com.badlogic.gdx.Gdx;

/** Resizes current application listener.
 * @author MJ */
public class Resizer implements Runnable {
	private final int width;
	private final int height;

	public Resizer (final int width, final int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void run () {
		Gdx.app.getApplicationListener().resize(width, height);
	}
}
