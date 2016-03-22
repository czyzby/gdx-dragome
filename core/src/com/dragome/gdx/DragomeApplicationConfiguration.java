
package com.dragome.gdx;

import com.badlogic.gdx.Graphics;
import com.dragome.gdx.graphics.DragomeGraphics.OrientationLockType;

/** Allows to configure {@link DragomeApplication}.
 *
 * @author MJ */
public class DragomeApplicationConfiguration {
	private OrientationLockType fullscreenOrientation;
	private String canvasId = "#dragomeCanvas";
	private boolean useGl30;
	private boolean stencil;
	private boolean antialiasEnabled;
	private boolean alphaEnabled;
	private boolean premultipliedAlpha;
	private boolean drawingBufferPreserved;

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
	 *           so this value is mostly ignored by default. Set to true if you added custom GL30 implementation to
	 *           {@link Graphics}.
	 * @return this, for chaining. */
	public DragomeApplicationConfiguration setUseGl30 (final boolean useGl30) {
		this.useGl30 = useGl30;
		return this;
	}

	/** @param antialiasEnabled true will enable antialiasing.
	 * @return this, for chaining. */
	public DragomeApplicationConfiguration setAntialiasEnabled (final boolean antialiasEnabled) {
		this.antialiasEnabled = antialiasEnabled;
		return this;
	}

	/** @param alphaEnabled if true, an alpha channel will be included in the color buffer to combine the color buffer with the
	 *           rest of the webpage - effectively allowing transparent backgrounds at a performance cost.
	 * @return this, for chaining. */
	public DragomeApplicationConfiguration setAlphaEnabled (final boolean alphaEnabled) {
		this.alphaEnabled = alphaEnabled;
		return this;
	}

	/** @param premultipliedAlpha true to used premultiplied alpha. Might have performance impact.
	 * @return this, for chaining. */
	public DragomeApplicationConfiguration setPremultipliedAlpha (final boolean premultipliedAlpha) {
		this.premultipliedAlpha = premultipliedAlpha;
		return this;
	}

	/** @param drawingBufferPreserved true to preserve back buffer which allows to take screenshots via {@code canvas#toDataUrl}.
	 *           May have performance impact.
	 * @return this, for chaining. */
	public DragomeApplicationConfiguration setDrawingBufferPreserved (final boolean drawingBufferPreserved) {
		this.drawingBufferPreserved = drawingBufferPreserved;
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

	/** @return true if antialias should be enabled. */
	public boolean isAntialiasEnabled () {
		return antialiasEnabled;
	}

	/** @return true if alpha channel should be included in the color buffer. */
	public boolean isAlphaEnabled () {
		return alphaEnabled;
	}

	/** @return true if premultiplied alpha should be used. */
	public boolean isPremultipliedAlpha () {
		return premultipliedAlpha;
	}

	/** @return true to preserve back buffer. */
	public boolean isDrawingBufferPreserved () {
		return drawingBufferPreserved;
	}
}
