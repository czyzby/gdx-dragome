
package com.dragome.gdx.audio;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

/** Not yet implemented. */
public class DragomeSound implements Sound {
	public DragomeSound (final FileHandle file) {
		throw new GdxRuntimeException("Not yet implemented.");
	}

	@Override
	public long play () {
		return 0; // TODO Implement sound.
	}

	@Override
	public long play (final float volume) {
		return 0; // TODO Implement sound.
	}

	@Override
	public long play (final float volume, final float pitch, final float pan) {
		return 0; // TODO Implement sound.
	}

	@Override
	public long loop () {
		return 0; // TODO Implement sound.
	}

	@Override
	public long loop (final float volume) {
		return 0; // TODO Implement sound.
	}

	@Override
	public long loop (final float volume, final float pitch, final float pan) {
		return 0; // TODO Implement sound.
	}

	@Override
	public void stop () {
		// TODO Implement sound.
	}

	@Override
	public void pause () {
		// TODO Implement sound.
	}

	@Override
	public void resume () {
		// TODO Implement sound.
	}

	@Override
	public void dispose () {
		// TODO Implement sound.
	}

	@Override
	public void stop (final long soundId) {
		// TODO Implement sound.
	}

	@Override
	public void pause (final long soundId) {
		// TODO Implement sound.
	}

	@Override
	public void resume (final long soundId) {
		// TODO Implement sound.
	}

	@Override
	public void setLooping (final long soundId, final boolean looping) {
		// TODO Implement sound.
	}

	@Override
	public void setPitch (final long soundId, final float pitch) {
		// TODO Implement sound.
	}

	@Override
	public void setVolume (final long soundId, final float volume) {
		// TODO Implement sound.
	}

	@Override
	public void setPan (final long soundId, final float pan, final float volume) {
		// TODO Implement sound.
	}
}
