
package com.dragome.gdx.files;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

/** Not yet implemented. */ // TODO Implement DragomeFileHandle.
public class DragomeFileHandle extends FileHandle {
	public DragomeFileHandle (final String path) {
		this(path, FileType.Internal);
	}

	public DragomeFileHandle (final String path, final FileType type) {
		super(path, type);
		throw new GdxRuntimeException("Not yet implemented.");
	}
}
