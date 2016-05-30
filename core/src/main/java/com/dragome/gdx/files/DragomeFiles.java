
package com.dragome.gdx.files;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.dragome.gdx.DragomeApplication;

/** Dragome {@link Files} implementation. Supports only {@link FileType#Classpath} and {@link FileType#Internal} file types.
 * @author MJ */
public class DragomeFiles implements Files {
	/** @param path path to the file. Cannot be null.
	 * @param type has to be {@link FileType#Internal} or {@link FileType#Classpath}.
	 * @throws GdxRuntimeException if invalid file type is requested. */
	@Override
	public FileHandle getFileHandle (final String path, final FileType type) {
		return new DragomeFileHandle(path, type);
	}

	@Override
	public FileHandle classpath (final String path) {
		return new DragomeFileHandle(path, FileType.Classpath);
	}

	@Override
	public FileHandle internal (final String path) {
		return new DragomeFileHandle(path, FileType.Internal);
	}

	@Override
	public FileHandle external (final String path) {
		throw new GdxRuntimeException("File type not supported: " + FileType.External);
	}

	@Override
	public FileHandle absolute (final String path) {
		throw new GdxRuntimeException("File type not supported: " + FileType.Absolute);
	}

	@Override
	public FileHandle local (final String path) {
		throw new GdxRuntimeException("File type not supported: " + FileType.Local);
	}

	@Override
	public String getExternalStoragePath () {
		Gdx.app.log(DragomeApplication.LOGGING_TAG, "External storage not supported.");
		return null;
	}

	@Override
	public boolean isExternalStorageAvailable () {
		return false;
	}

	@Override
	public String getLocalStoragePath () {
		Gdx.app.log(DragomeApplication.LOGGING_TAG, "Local storage not supported.");
		return null;
	}

	@Override
	public boolean isLocalStorageAvailable () {
		return false;
	}
}
