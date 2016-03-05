
package com.dragome.gdx;

import com.dragome.gdx.graphics.DragomeGraphics.OrientationLockType;

/** Allows to configure {@link DragomeApplication}.
 *
 * @author MJ */
public class DragomeApplicationConfiguration {
	private OrientationLockType fullscreenOrientation;
	private String canvasId = "#dragomeCanvas";
	private boolean useGl30;
	private boolean stencil;

	/** @param fullscreenOrientation if not null, will attempt locking as the application enters full-screen-mode.
	 * @return this, for chaining.
	 * @see com.dragome.gdx.graphics.DragomeGraphics.CommonOrientationLockType */
	public DragomeApplicationConfiguration setFullscreenOrientation (final OrientationLockType fullscreenOrientation) {
		this.fullscreenOrientation = fullscreenOrientation;
		return this;
	}

	/** @param stencil if true, stencil buffer format will be used.
	 * @return this, for chaining. */
	public DragomeApplicationConfiguration setStencil (final boolean stencil) {
		this.stencil = stencil;
		return this;
	}

	/** @param canvasId HTML selector that should return a single HTML canvas element. By default, this value is set to '
	 *           {@literal #}dragomeCanvas'.
	 * @return this, for chaining. */
	public DragomeApplicationConfiguration setCanvasId (final String canvasId) {
		this.canvasId = canvasId;
		return this;
	}

	/** @param useGl30 if true, GL30 will be used where available. Defaults to false. WebGL currently seems to support only GL20,
	 *           so this value is mostly ignored by default.
	 * @return this, for chaining. */
	public DragomeApplicationConfiguration setUseGl30 (final boolean useGl30) {
		this.useGl30 = useGl30;
		return this;
	}

	/** @return orientation type that should be used during fullscreen mode. Can be null. */
	public OrientationLockType getFullscreenOrientation () {
		return fullscreenOrientation;
	}

	/** @return selector for a HTML canvas element. */
	public String getCanvasId () {
		return canvasId;
	}

	/** @return if true, should use stencil buffer format. */
	public boolean isStencil () {
		return stencil;
	}

	/** @return true if GL30 should be used instead of GL20. Most likely not supported. */
	public boolean useGl30 () {
		return useGl30;
	}
}
