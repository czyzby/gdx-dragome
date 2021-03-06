
package com.dragome.gdx.graphics;

import org.w3c.dom.ObjectArray;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLCanvasElement;
import org.w3c.dom.webgl.WebGLContextAttributes;
import org.w3c.dom.webgl.WebGLRenderingContext;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.GLVersion;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectSet;
import com.dragome.commons.javascript.ScriptHelper;
import com.dragome.gdx.DragomeApplication;
import com.dragome.gdx.DragomeApplicationConfiguration;
import com.dragome.gdx.graphics.resizing.Resizer;
import com.dragome.gdx.graphics.webgl.DragomeGL20;
import com.dragome.gdx.lifecycle.Renderer;

/** Default implementation of {@link Graphics} for Dragome applications. Wraps around a HTML canvas and WebGL. Does not support
 * GL30. Allows to go to fullscreen mode only if chosen display mode matches current screen size. Allows to change cursors and
 * current page title. Always reports only one {@link Monitor} and {@link DisplayMode}. Does not support vSync settings.
 *
 * @author MJ */
public class DragomeGraphics implements Graphics {
	/** Pixels per inch value. */
	public static final float PPI = 96f;
	public static final float DENSITY = PPI / 160f;
	/** Pixels per centimeters value. */
	public static final float PPC = PPI / 2.54f;
	/** Bits per pixel value for display mode. */
	public static final int BPP = 8;
	/** Refresh rate value for display mode. Roughly matches expected FPS. */
	public static final int REFRESH_RATE = 60;

	private final DragomeApplication application;
	private final HTMLCanvasElement canvas;
	private final Renderer renderer;
	private final WebGLRenderingContext context;
	private final GL20 gl20;
	private final GLVersion version;
	// Cache:
	private final Monitor monitor = new DragomeMonitor(0, 0, DragomeApplication.LOGGING_TAG);
	private final DisplayMode displayMode = new DragomeDisplayMode(getScreenWidth(), getScreenHeight(), REFRESH_RATE, BPP);
	private ObjectSet<String> extensions;
	private int oldWidth;
	private int oldHeight;

	public DragomeGraphics (final DragomeApplication application) {
		this.application = application;
		canvas = application.getCanvas();
		renderer = application.getRenderer();
		oldWidth = canvas.getWidth();
		oldHeight = canvas.getHeight();
		context = (WebGLRenderingContext)canvas.getContext("webgl", getWebGlAttributes(application.getConfiguration()));
		context.viewport(0, 0, oldWidth, oldHeight);
		gl20 = createGL20(context);
		addFullscreenListener();

		final String versionString = gl20.glGetString(GL20.GL_VERSION);
		final String vendorString = gl20.glGetString(GL20.GL_VENDOR);
		final String rendererString = gl20.glGetString(GL20.GL_RENDERER);
		version = new GLVersion(Application.ApplicationType.WebGL, versionString, vendorString, rendererString);
	}

	/** Sets {@link WebGLContextAttributes} according to {@link DragomeApplicationConfiguration}.
	 * @param configuration used to set up WebGL attributes.
	 * @return a configuration object with WebGL attributes. */
	protected WebGLContextAttributes getWebGlAttributes (final DragomeApplicationConfiguration configuration) {
		final WebGLContextAttributes attributes = ScriptHelper.evalCasting("{}", WebGLContextAttributes.class, this);
		attributes.setAlpha(configuration.isAlphaEnabled());
		attributes.setAntialias(configuration.isAntialiasEnabled());
		attributes.setStencil(configuration.isStencil());
		attributes.setPremultipliedAlpha(configuration.isPremultipliedAlpha());
		attributes.setPreserveDrawingBuffer(configuration.isDrawingBufferPreserved());
		return attributes;
	}

	private void addFullscreenListener () {
		final EventTarget document = ScriptHelper.evalCasting("document", EventTarget.class, this);
		final EventListener listener = new EventListener() {
			@Override
			public void handleEvent (final Event event) {
				fullscreenChanged();
			}
		};
		for (final String event : new String[] {"fullscreenchange", "webkitfullscreenchange", "mozfullscreenchange",
			"msfullscreenchange"}) {
			document.addEventListener(event, listener, false);
		}
	}

	/** @param context WebGL rendering context from application's main canvas.
	 * @return {@link GL20} implementation.
	 * @see DragomeGL20 */
	protected GL20 createGL20 (final WebGLRenderingContext context) {
		return new DragomeGL20(context);
	}

	@Override
	public boolean isGL30Available () {
		return false;
	}

	@Override
	public GL30 getGL30 () {
		application.error(DragomeApplication.LOGGING_TAG, "Note: DragomeGraphics#getGL30 is not supported on Dragome backend.");
		return null;
	}

	@Override
	public GL20 getGL20 () {
		return gl20;
	}

	@Override
	public int getWidth () {
		return canvas.getWidth();
	}

	@Override
	public int getHeight () {
		return canvas.getHeight();
	}

	@Override
	public int getBackBufferWidth () {
		return canvas.getWidth();
	}

	@Override
	public int getBackBufferHeight () {
		return canvas.getHeight();
	}

	/** @return width of the whole screen (in pixels) as reported by the browser. */
	public int getScreenWidth () {
		return ScriptHelper.evalInt("window.screen.width", this);
	}

	/** @return height of the whole screen (in pixels) as reported by the browser. */
	public int getScreenHeight () {
		return ScriptHelper.evalInt("window.screen.height", this);
	}

	@Override
	public long getFrameId () {
		return renderer.getFrameId();
	}

	@Override
	public float getDeltaTime () {
		return renderer.getDeltaTime();
	}

	@Override
	public float getRawDeltaTime () {
		return renderer.getDeltaTime();
	}

	@Override
	public int getFramesPerSecond () {
		return renderer.getFramesPerSecond();
	}

	@Override
	public GraphicsType getType () {
		return GraphicsType.WebGL;
	}

	@Override
	public float getPpiX () {
		return PPI;
	}

	@Override
	public float getPpiY () {
		return PPI;
	}

	@Override
	public float getPpcX () {
		return PPC;
	}

	@Override
	public float getPpcY () {
		return PPC;
	}

	@Override
	public float getDensity () {
		return DENSITY;
	}

	@Override
	public boolean supportsDisplayModeChange () { // If true, application can switch to fullscreen mode.
		return ScriptHelper.evalBoolean(
			"document.fullscreenEnabled||document.webkitFullscreenEnabled||document.mozFullScreenEnabled||document.msFullscreenEnabled||false",
			this);
	}

	@Override
	public Monitor getPrimaryMonitor () {
		return monitor;
	}

	@Override
	public Monitor getMonitor () {
		return monitor;
	}

	@Override
	public Monitor[] getMonitors () {
		return new Monitor[] {monitor};
	}

	@Override
	public DisplayMode[] getDisplayModes () {
		return new DisplayMode[] {displayMode};
	}

	@Override
	public DisplayMode[] getDisplayModes (final Monitor monitor) {
		return new DisplayMode[] {displayMode};
	}

	@Override
	public DisplayMode getDisplayMode () {
		return displayMode;
	}

	@Override
	public DisplayMode getDisplayMode (final Monitor monitor) {
		return displayMode;
	}

	@Override
	public void setTitle (final String title) {
		ScriptHelper.put("_title", title, this);
		ScriptHelper.evalNoResult("document.title=_title", this);
	}

	@Override
	public void setVSync (final boolean vsync) {
		application.error(DragomeApplication.LOGGING_TAG, "Note: Graphics#setVSync is not supported in Dragome.");
	}

	@Override
	public BufferFormat getBufferFormat () { // Mimics GWT backend.
		return new BufferFormat(8, 8, 8, 0, 16, application.getConfiguration().isStencil() ? 8 : 0, 0, false);
	}

	@Override
	public boolean supportsExtension (final String extension) {
		if (extension == null) {
			return false;
		}
		if (extensions == null) {
			final ObjectArray<String> availableExtensions = context.getSupportedExtensions();
			for (int i = 0, l = availableExtensions.getLength(); i < l; i++) {
				extensions.add(availableExtensions.getElement(i));
			}
		}
		return extensions.contains(extension);
	}

	@Override
	public boolean isContinuousRendering () {
		return true;
	}

	@Override
	public void setContinuousRendering (final boolean isContinuous) {
		if (!isContinuous) { // TODO GWT does not support this, should we?
			application.error(DragomeApplication.LOGGING_TAG,
				"Note: Graphics#setContinuousRendering is not supported in Dragome. Application is always in continuous rendering mode.");
		}
	}

	@Override
	public void requestRendering () {
	}

	@Override
	public boolean isFullscreen () {
		return ScriptHelper.evalBoolean(
			"document.fullscreenElement!=null||document.msFullscreenElement!=null||document.webkitFullscreenElement!=null||document.mozFullScreenElement!=null||document.webkitIsFullScreen||document.mozFullScreen||false",
			this);
	}

	@Override
	public boolean setFullscreenMode (final DisplayMode displayMode) {
		if (this.displayMode.equals(displayMode)) {
			return enterFullscreen();
		}
		return false;
	}

	/** Attempts to enter fullscreen mode. Returns true if attempted to enter fullscreen mode.
	 * @return true if tried to switch to fullscreen mode. */
	public boolean enterFullscreen () {
		if (supportsDisplayModeChange()) {
			oldWidth = canvas.getWidth();
			oldHeight = canvas.getHeight();
			final int screenWidth = getScreenWidth(), screenHeight = getScreenHeight();
			canvas.setWidth(screenWidth);
			canvas.setHeight(screenHeight);
			addResizeEvent(screenWidth, screenHeight);
			ScriptHelper.put("_elem", canvas, this);
			ScriptHelper.evalNoResult("if(_elem.requestFullscreen){_elem.requestFullscreen();}"
				+ "else if(_elem.webkitRequestFullScreen){_elem.webkitRequestFullScreen(Element.ALLOW_KEYBOARD_INPUT);}"
				+ "else if(_elem.mozRequestFullScreen){_elem.mozRequestFullScreen();}"
				+ "else if(_elem.msRequestFullscreen){_elem.msRequestFullscreen();}", this);
			return true;
		}
		return false;
	}

	/** Will post an event which will resize the game during the next render call. Width and height have to match current
	 * application size.
	 *
	 * @param width current application width.
	 * @param height current application height. */
	protected void addResizeEvent (final int width, final int height) {
		// Change to application.getApplicationListener().resize(width, height); for immediate resize.
		application.postRunnable(new Resizer(width, height));
	}

	/** Should be invoked each time the application exits or enters fullscreen mode. */
	public void fullscreenChanged () {
		if (isFullscreen()) {
			lockOrientation();
		} else {
			if (canvas.getWidth() != oldWidth || canvas.getHeight() != oldHeight) {
				canvas.setWidth(oldWidth);
				canvas.setHeight(oldHeight);
				addResizeEvent(oldWidth, oldHeight);
			}
			unlockOrientation();
		}
	}

	/** Attempts to lock orientation according to application's configuration object. Should be executed after entering fullscreen
	 * mode. */
	public void lockOrientation () {
		final OrientationLockType orientation = application.getConfiguration().getFullscreenOrientation();
		if (orientation != null) {
			ScriptHelper.put("_orient", orientation.getName(), this);
			ScriptHelper.evalNoResult(
				"var lock=screen.lockOrientation||screen.mozLockOrientation||screen.msLockOrientation||screen.webkitLockOrientation;if(lock){lock(_orient);}else if(screen.orientation&&screen.orientation.lock){screen.orientation.lock(_orient);}",
				this);
		}
	}

	/** Attempts to unlock orientation. Should be executed during exiting from fullscreen mode. */
	public void unlockOrientation () {
		if (application.getConfiguration().getFullscreenOrientation() != null) {
			ScriptHelper.evalNoResult(
				"var unlock=screen.unlockOrientation||screen.mozUnlockOrientation||screen.msUnlockOrientation||screen.webkitUnlockOrientation;if(unlock){unlock();}else if(screen.orientation&&screen.orientation.unlock){screen.orientation.unlock();}",
				this);
		}
	}

	@Override
	public boolean setWindowedMode (final int width, final int height) {
		if (isFullscreen()) {
			exitFullscreen();
		}
		canvas.setWidth(width);
		canvas.setHeight(height);
		addResizeEvent(width, height);
		return true;
	}

	/** Attempts to exit full screen mode. Should be a no-op if application is currently in windowed mode. */
	public void exitFullscreen () {
		ScriptHelper.evalNoResult(
			"if(document.exitFullscreen)document.exitFullscreen();if(document.msExitFullscreen)document.msExitFullscreen();if(document.webkitExitFullscreen)document.webkitExitFullscreen();if(document.mozExitFullscreen)document.mozExitFullscreen();if(document.webkitCancelFullScreen)document.webkitCancelFullScreen();",
			this);
	}

	@Override
	public Cursor newCursor (final Pixmap pixmap, final int xHotspot, final int yHotspot) {
		return new DragomeCursor(pixmap, xHotspot, yHotspot);
	}

	@Override
	public void setCursor (final Cursor cursor) {
		if (cursor instanceof DragomeCursor) {
			setCursor(((DragomeCursor)cursor).getCss());
		} else {
			throw new GdxRuntimeException("Do not create Cursor instances manually, use Graphics#newCursor instead.");
		}
	}

	@Override
	public void setSystemCursor (final SystemCursor systemCursor) {
		setCursor(DragomeCursor.getNameForSystemCursor(systemCursor));
	}

	/** @param css will be set in canvas style attribute as "cursor". */
	protected void setCursor (final String css) {
		ScriptHelper.put("_cursor", css, this);
		ScriptHelper.put("_canvas", canvas, this);
		ScriptHelper.evalNoResult("_canvas.style.cursor=_cursor", this);
	}

	/** Allows to support orientation lock during fullscreen mode.
	 *
	 * @author MJ */
	public static interface OrientationLockType {
		/** @return actual name of the orientation. */
		String getName ();
	}

	/** Enum values from http://www.w3.org/TR/screen-orientation. Filtered based on what the browsers actually support. */
	public static enum CommonOrientationLockType implements OrientationLockType { // Based on GWT backend.
		LANDSCAPE("landscape"), PORTRAIT("portrait"), PORTRAIT_PRIMARY("portrait-primary"), PORTRAIT_SECONDARY(
			"portrait-secondary"), LANDSCAPE_PRIMARY("landscape-primary"), LANDSCAPE_SECONDARY("landscape-secondary");

		private final String name;

		private CommonOrientationLockType (final String name) {
			this.name = name;
		}

		@Override
		public String getName () {
			return name;
		}
	}

	/** Allows to create an instance of {@link DisplayMode}, which has a protected constructor.
	 *
	 * @author MJ */ // ...for some unknown reason.
	protected static class DragomeDisplayMode extends DisplayMode {
		public DragomeDisplayMode (final int width, final int height, final int refreshRate, final int bitsPerPixel) {
			super(width, height, refreshRate, bitsPerPixel);
		}

		@Override
		public boolean equals (final Object object) {
			if (object == this) {
				return true;
			} else if (object instanceof DisplayMode) {
				final DisplayMode other = (DisplayMode)object;
				return width == other.width && height == other.height && refreshRate == other.refreshRate
					&& bitsPerPixel == other.bitsPerPixel;
			}
			return false;
		}

		@Override
		public int hashCode () {
			return width + 13 * height + 53 * refreshRate + 163 * bitsPerPixel;
		}
	}

	/** Allows to create an instance of {@link Monitor}, which has a protected constructor.
	 *
	 * @author MJ */ // ...for some unknown reason.
	protected static class DragomeMonitor extends Monitor {
		public DragomeMonitor (final int virtualX, final int virtualY, final String name) {
			super(virtualX, virtualY, name);
		}
	}

	@Override
	public GLVersion getGLVersion () {
		return version;
	}

	@Override
	public void setUndecorated (final boolean undecorated) {
		// Canvas cannot be (un)decorated, and it's unlikely to modify browser window.
	}

	@Override
	public void setResizable (final boolean resizable) {
		// Canvas generally should not be resizable.
	}
}
