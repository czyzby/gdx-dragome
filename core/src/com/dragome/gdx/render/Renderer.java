
package com.dragome.gdx.render;

/** Manages application's rendering.
 *
 * @author MJ */
public interface Renderer {
	/** Should start continuous rendering of the application. */
	void start ();

	/** Should cancel any scheduled application rendering events. */
	void stop ();

	/** @param runnable should be invoked on the next render call. */
	void postRunnable (Runnable runnable);

	/** @return time passed since last render (in seconds). Might return 0 if application was not rendered yet. */
	float getDeltaTime ();

	/** @return current frame ID. Might always return 0 if renderer does not keep meta-data. */
	long getFrameId ();

	/** @return estimate amount of frames rendered this second. Might always return 0 if renderer does not keep meta-data. */
	int getFramesPerSecond ();
}
