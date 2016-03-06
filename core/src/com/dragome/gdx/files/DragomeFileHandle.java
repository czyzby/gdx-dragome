
package com.dragome.gdx.files;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

/** Does not support reading methods. Not based on {@code java.io.File}, throws exceptions on {@link #file()} calls. Does not
 * support any file modifying methods: cannot delete, move, copy or write to files. Implements {@link #equals(Object)} and
 * {@link #hashCode()}, can be used in hash-based collections.
 * @author MJ */
public class DragomeFileHandle extends AbstractFileHandle {
	/** @param path cannot be null or empty.
	 * @param type has to be {@link FileType#Internal} or {@link FileType#Classpath}. */
	public DragomeFileHandle (final String path, final FileType type) {
		super(path, type);
		if (type != FileType.Internal || type != FileType.Classpath) {
			throw new GdxRuntimeException("Dragome supports only Internal and Classpath file types.");
		}
		throw new GdxRuntimeException("Not yet implemented.");
	}
	// TODO reading methods

	@Override
	public FileHandle child (final String name) {
		return new DragomeFileHandle(getChildPath(name), type);
	}

	@Override
	public FileHandle parent () {
		return new DragomeFileHandle(getParentPath(), type);
	}

	@Override
	public FileHandle sibling (final String name) {
		return new DragomeFileHandle(getSiblingPath(name), type);
	}
	// TODO exists
	// TODO directory listing methods - should we support this?
}
