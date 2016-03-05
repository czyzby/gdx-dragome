
package com.dragome.gdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Clipboard;
import com.dragome.gdx.clipboard.DragomeClipboard;
import com.dragome.gdx.clipboard.MockUpClipboard;
import com.dragome.gdx.graphics.DragomeGraphics;
import com.dragome.gdx.lifecycle.LifecycleManager;
import com.dragome.gdx.logging.ConsoleLogger;
import com.dragome.gdx.logging.DragomeLogger;
import com.dragome.gdx.net.DragomeNet;
import com.dragome.gdx.preferences.PreferencesResolver;
import com.dragome.gdx.render.DebugDragomeRenderer;
import com.dragome.gdx.render.DragomeRenderer;
import com.dragome.gdx.render.Renderer;
import com.dragome.view.DefaultVisualActivity;
import com.dragome.web.html.dom.w3c.BrowserDomHandler;

/** Default base implementation of LibGDX {@link Application}. Extends and override any of the create(...) methods to modify the
 * behavior of the class. Use {@link DragomeApplicationConfiguration} for basic settings. Pass your {@link ApplicationListener}
 * instance in the constructor.
 *
 * <p>
 * Settings to consider: {@link #createClipboard()} returns {@link DragomeClipboard} which might not be supported by all browsers.
 * While it fallbacks to the same functionality as {@link MockUpClipboard}, it is generally safer to use the second one.
 * {@link #createRenderer()} returns {@link DragomeRenderer}, which does not keep rendering meta-data, like current frame ID or
 * FPS. Use {@link DebugDragomeRenderer} instead if you need these. {@link #createLogger()} returns {@link ConsoleLogger} that
 * delegates all logging to the default JS console. See {@link DragomeLogger} docs for more provided options.
 *
 * @author MJ */
public class DragomeApplication extends DefaultVisualActivity implements Application {
	/** Used by Dragome-specific classes to report informations, warnings and errors. */
	public static final String LOGGING_TAG = "Dragome";

	// LibGDX core classes:
	private final ApplicationListener applicationListener;
	private final Net net;
	private final Audio audio;
	private final Files files;
	private final Input input;
	private final Graphics graphics;
	// Helpers:
	private final BrowserDomHandler domBrowser = new BrowserDomHandler();
	private final DragomeApplicationConfiguration configuration;
	private final LifecycleManager lifecycleManager;
	private final PreferencesResolver preferencesResolver;
	private final DragomeLogger logger;
	private final Clipboard clipboard;
	private final Renderer renderer;

	/** @param applicationListener handles application events. Uses default configuration. */
	public DragomeApplication (final ApplicationListener applicationListener) {
		this(applicationListener, new DragomeApplicationConfiguration());
	}

	/** @param applicationListener handles application events.
	 * @param configuration configures Dragome application. */
	public DragomeApplication (final ApplicationListener applicationListener,
		final DragomeApplicationConfiguration configuration) {
		this.applicationListener = applicationListener;
		this.configuration = configuration;

		logger = createLogger();
		preferencesResolver = createPreferencesResolver();
		lifecycleManager = createLifecycleManager();
		clipboard = createClipboard();
		renderer = createRenderer();

		net = createNet();
		audio = createAudio();
		files = createFiles();
		input = createInput();
		graphics = createGraphics();
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return applicationListener;
	}

	/** @return a new instance of logger object that prints logs received by the application instance.
	 * @see DragomeLogger */
	protected DragomeLogger createLogger () {
		return new ConsoleLogger();
	}

	/** @return a new instance of renderer which manages application's main loop. Note that default Dragome renderer does not keep
	 *         track of current frame ID and FPS value; use {@link DebugDragomeRenderer} if you need such data.
	 * @see DragomeRenderer
	 * @see DebugDragomeRenderer */
	protected Renderer createRenderer () {
		return new DragomeRenderer(applicationListener);
	}

	/** @return a new instance of manager that maintains {@link LifecycleListener}s and is notified about application lifecycle
	 *         events. */
	protected LifecycleManager createLifecycleManager () {
		return new LifecycleManager();
	}

	/** @return a new instance of manager that creates and maintains {@link Preferences} instances. Default implementation uses
	 *         local storage when possible and mock up preferences (never saved, unavailable in next sessions) when necessary. */
	protected PreferencesResolver createPreferencesResolver () {
		return new PreferencesResolver();
	}

	/** @return a new instance of clipboard manager. Allows to copy and paste string values.
	 * @see DragomeClipboard
	 * @see MockUpClipboard */
	protected Clipboard createClipboard () {
		return new DragomeClipboard();
	}

	/** @return a new instance of {@link Net} implementation, handling application's networking.
	 * @see DragomeNet */
	protected Net createNet () {
		return new DragomeNet();
	}

	/** @return a new instance of {@link Audio} implementation, handling application's sound support. */
	protected Audio createAudio () {
		return null; // TODO implement Audio, add @see
	}

	/** @return a new instance of {@link Files} implementation, handling application's files support. */
	protected Files createFiles () {
		return null; // TODO implement Files, add @see
	}

	/** @return a new instance of {@link Graphics} implementation, handling application's graphics.
	 * @see DragomeGraphics */
	protected Graphics createGraphics () {
		return new DragomeGraphics(this);
	}

	/** @return a new instance of {@link Input} implementation, handling user's input. */
	protected Input createInput () {
		return null; // TODO implement Input, add @see
	}

	@Override
	public Graphics getGraphics () {
		return graphics;
	}

	@Override
	public Audio getAudio () {
		return audio;
	}

	@Override
	public Input getInput () {
		return input;
	}

	@Override
	public Files getFiles () {
		return files;
	}

	@Override
	public Net getNet () {
		return net;
	}

	@Override
	public void log (final String tag, final String message) {
		logger.log(LOG_INFO, tag, message);
	}

	@Override
	public void log (final String tag, final String message, final Throwable exception) {
		logger.log(LOG_INFO, tag, message, exception);
	}

	@Override
	public void error (final String tag, final String message) {
		logger.log(LOG_ERROR, tag, message);
	}

	@Override
	public void error (final String tag, final String message, final Throwable exception) {
		logger.log(LOG_ERROR, tag, message, exception);
	}

	@Override
	public void debug (final String tag, final String message) {
		logger.log(LOG_DEBUG, tag, message);
	}

	@Override
	public void debug (final String tag, final String message, final Throwable exception) {
		logger.log(LOG_DEBUG, tag, message, exception);
	}

	@Override
	public void setLogLevel (final int logLevel) {
		logger.setLevel(logLevel);
	}

	@Override
	public int getLogLevel () {
		return logger.getLevel();
	}

	@Override
	public ApplicationType getType () {
		return ApplicationType.WebGL;
	}

	@Override
	public int getVersion () {
		error(LOGGING_TAG, "Note: Application#getVersion is not supported on Dragome backend.");
		return 0;
	}

	@Override
	public long getJavaHeap () {
		error(LOGGING_TAG, "Note: Application#getJavaHeap is not supported on Dragome backend.");
		return 0;
	}

	@Override
	public long getNativeHeap () {
		error(LOGGING_TAG, "Note: Application#getNativeHeap is not supported on Dragome backend.");
		return 0;
	}

	@Override
	public Preferences getPreferences (final String name) {
		return preferencesResolver.getPreferences(name);
	}

	@Override
	public Clipboard getClipboard () {
		return clipboard;
	}

	@Override
	public void postRunnable (final Runnable runnable) {
		renderer.postRunnable(runnable);
	}

	@Override
	public void addLifecycleListener (final LifecycleListener listener) {
		lifecycleManager.addListener(listener);
	}

	@Override
	public void removeLifecycleListener (final LifecycleListener listener) {
		lifecycleManager.removeListener(listener);
	}

	/** @return lifecycle manager that should be notified about every lifecycle event (pause, resume, dispose). */
	public LifecycleManager getLifecycleManager () {
		return lifecycleManager;
	}

	/** @return configuration object used to initiate the application. Modifying most settings after application is already
	 *         initiated does nothing. */
	public DragomeApplicationConfiguration getConfiguration () {
		return configuration;
	}

	/** @return DOM browser, allowing to find HTML elements by selectors. */
	public BrowserDomHandler getDomBrowser () {
		return domBrowser;
	}

	/** @return a renderer implementation, handling application's main loop. */
	public Renderer getRenderer () {
		return renderer;
	}

	@Override
	public void exit () {
		renderer.stop();
		lifecycleManager.dispose();
	}

	@Override
	public void build () {
		assignGdxStatics();
		applicationListener.create();
		applicationListener.resize(graphics.getWidth(), graphics.getHeight());
		renderer.start();
	}

	/** Override this method to customize how {@link Gdx} static fields are initialized. */
	protected void assignGdxStatics () {
		Gdx.app = this;
		Gdx.net = net;
		Gdx.audio = audio;
		Gdx.files = files;
		Gdx.input = input;
		Gdx.graphics = graphics;
		if (configuration.useGl30() && graphics.isGL30Available()) { // Should never happen, but we allow
			Gdx.gl = Gdx.gl20 = Gdx.gl30 = graphics.getGL30(); // the users to simulate/implement GL30.
		} else {
			Gdx.gl = Gdx.gl20 = graphics.getGL20();
		}
	}
}
