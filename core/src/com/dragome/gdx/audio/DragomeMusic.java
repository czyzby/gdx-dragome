
package com.dragome.gdx.audio;

import org.w3c.dom.html.HTMLAudioElement;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.dragome.commons.javascript.ScriptHelper;

/** Default implementation of {@link Music} in Dragome applications. Uses {@code audio} HTML element to play sounds.
 * @author Alexey Andreev
 * @author MJ */ // Based on unfinished TeaVM backend.
public class DragomeMusic implements Music, Runnable {
	private HTMLAudioElement element;
	private boolean started;
	private OnCompletionListener listener;
	private final Runnable onEnded;

	public DragomeMusic (final FileHandle file) {
		element = ScriptHelper.evalCasting("document.createElement('audio');", HTMLAudioElement.class, this);
		element.setSrc(file.path()); // TODO Correct path after Files implementation (if necessary).
		onEnded = this;
		ScriptHelper.put("_elem", element, this);
		ScriptHelper.put("_end", onEnded, this);
		ScriptHelper.evalNoResult("_elem.onended=function(){_end.$run$void();};document.body.appendChild(_elem);", this);
	}

	@Override
	public void run () {
		listener.onCompletion(this);
	}

	private void checkDisposed () {
		if (element == null) {
			throw new IllegalStateException("This music instance is already disposed.");
		}
	}

	@Override
	public void play () {
		checkDisposed();
		element.play();
		started = true;
	}

	@Override
	public void pause () {
		checkDisposed();
		element.pause();
	}

	@Override
	public void stop () {
		checkDisposed();
		element.pause();
		element.setCurrentTime(0);
		started = false;
	}

	@Override
	public boolean isPlaying () {
		checkDisposed();
		return started && !element.getPaused() && element.getEnded();
	}

	@Override
	public void setLooping (final boolean isLooping) {
		checkDisposed();
		element.setLoop(isLooping);
	}

	@Override
	public boolean isLooping () {
		checkDisposed();
		return element.getLoop();
	}

	@Override
	public void setVolume (final float volume) {
		checkDisposed();
		element.setVolume(volume);
	}

	@Override
	public float getVolume () {
		checkDisposed();
		return (float)element.getVolume();
	}

	@Override
	public void setPan (final float pan, final float volume) {
		checkDisposed(); // TODO pan is unsupported.
		element.setVolume(volume);
	}

	@Override
	public void setPosition (final float position) {
		checkDisposed();
		element.setCurrentTime(position);
	}

	@Override
	public float getPosition () {
		checkDisposed();
		return (float)element.getCurrentTime();
	}

	@Override
	public void dispose () {
		if (element != null) {
			element.getParentNode().removeChild(element);
			element = null;
		}
	}

	@Override
	public void setOnCompletionListener (final OnCompletionListener listener) {
		this.listener = listener;
	}
}
