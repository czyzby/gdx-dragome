
package com.dragome.gdx.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

/** Not yet implemented. */
public class DragomeMusic implements Music {
	public DragomeMusic (final FileHandle file) {
		throw new GdxRuntimeException("Not yet implemented.");
	}

	@Override
	public void play () {
		// TODO Implement music.
	}

	@Override
	public void pause () {
		// TODO Implement music.
	}

	@Override
	public void stop () {
		// TODO Implement music.
	}

	@Override
	public boolean isPlaying () {
		return false; // TODO Implement music.
	}

	@Override
	public void setLooping (final boolean isLooping) {
		// TODO Implement music.
	}

	@Override
	public boolean isLooping () {
		return false; // TODO Implement music.
	}

	@Override
	public void setVolume (final float volume) {
		// TODO Implement music.
	}

	@Override
	public float getVolume () {
		return 0; // TODO Implement music.
	}

	@Override
	public void setPan (final float pan, final float volume) {
		// TODO Implement music.
	}

	@Override
	public void setPosition (final float position) {
		// TODO Implement music.
	}

	@Override
	public float getPosition () {
		return 0; // TODO Implement music.
	}

	@Override
	public void dispose () {
		// TODO Implement music.
	}

	@Override
	public void setOnCompletionListener (final OnCompletionListener listener) {
		// TODO Implement music.
	}
}
