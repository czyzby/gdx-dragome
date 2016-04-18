
package com.dragome.gdx.audio;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

/** {@link Audio} handler for Dragome applications. Does not support {@link AudioDevice} or {@link AudioRecorder}.
 * @author MJ */
public class DragomeAudio implements Audio {
	@Override
	public Sound newSound (final FileHandle file) {
		return new DragomeSound(file);
	}

	@Override
	public Music newMusic (final FileHandle file) {
		return new DragomeMusic(file);
	}

	@Override
	public AudioDevice newAudioDevice (final int samplingRate, final boolean isMono) {
		throw new GdxRuntimeException("AudioDevice is not supported on Dragome backend.");
	}

	@Override
	public AudioRecorder newAudioRecorder (final int samplingRate, final boolean isMono) {
		throw new GdxRuntimeException("AudioRecorder is not supported on Dragome backend.");
	}
}
