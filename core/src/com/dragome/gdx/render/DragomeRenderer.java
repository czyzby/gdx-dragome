
package com.dragome.gdx.render;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.dragome.commons.javascript.ScriptHelper;
import com.dragome.web.html.dom.Window;

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
	private long lastRender;
	private final Runnable loopRunnable = Window.wrapRunnableForDebugging(new Runnable() {
		@Override
		public void run () {
			try {
			loop();
			} catch (final GdxRuntimeException exception) {
			throw exception;
			} catch (final Exception exception) {
			throw new GdxRuntimeException("Exception occurred on rendering loop.", exception);
			}
		}
	});

	/** @param listener will be informed of rendering events. */
	public DragomeRenderer (final ApplicationListener listener) {
		this.listener = listener;
		// Date.now() polyfill:
		ScriptHelper.evalNoResult("if(!Date.now){Date.now=function now(){return new Date().getTime();};}", this);
		// window.requestAnimationFrame polyfill:
		ScriptHelper.evalNoResult(
			"(function(){for(var n=0,e=['ms','moz','webkit','o'],i=0;i<e.length&&!window.requestAnimationFrame;++i)window.requestAnimationFrame=window[e[i]+'RequestAnimationFrame'],window.cancelAnimationFrame=window[e[i]+'CancelAnimationFrame']||window[e[i]+'CancelRequestAnimationFrame'];window.requestAnimationFrame||(window.requestAnimationFrame=function(e,i){var a=(new Date).getTime(),o=Math.max(0,16-(a-n)),t=window.setTimeout(function(){e(a+o)},o);return n=a+o,t},window.cancelAnimationFrame=function(n){clearTimeout(n)})})();",
			this);
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
		lastRender = now();
		requestAnimationFrame(loopRunnable);
	}

	@Override
	public void stop () {
		cancelAnimationFrame();
	}

	/** @return current time millies. */
	protected long now () { // System.currentTimeMillis() unnecessarily creates a new Date object.
		return ScriptHelper.evalLong("Date.now();");
	}

	/** @param runnable will be executed as the animation frame. If it is the main loop runnable, it should recursively invoke
	 *           this method passing itself as the parameter. */
	protected void requestAnimationFrame (final Runnable runnable) {
		ScriptHelper.put("_loop", runnable, this);
		ScriptHelper.evalNoResult("this._frameId=window.requestAnimationFrame(function(){_loop.$run$void();})", this);
	}

	/** Uses last stored frame ID to cancel the request. */
	protected void cancelAnimationFrame () {
		ScriptHelper.evalNoResult("if(typeof this._frameId==='number'){window.cancelAnimationFrame(this._frameId);}", this);
	}

	/** Main application's loop. */
	protected void loop () {
		final long now = now();
		deltaTime = (now - lastRender) / 1000f;
		lastRender = now;
		if (runnables.size > 0) {
			runnablesToInvoke.addAll(runnables);
			runnables.clear();
			for (final Runnable runnable : runnablesToInvoke) {
			runnable.run();
			}
			runnablesToInvoke.clear();
		}
		listener.render();
		requestAnimationFrame(loopRunnable);
	}

	@Override
	public void postRunnable (final Runnable runnable) {
		runnables.add(runnable);
	}
}
