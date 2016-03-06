
package com.dragome.gdx.files;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.dragome.gdx.DragomeApplication;

/** Abstract base for {@link FileHandle}s that are not based on {@code java.io.File}. Analyzes file path to return its name,
 * extension and other data. Assumes the file handle is in read-only mode and cannot write to files - throws exceptions upon
 * invocation of any writing operation. Implements {@link #equals(Object)} and {@link #hashCode()}, can be used in hash-based
 * collections.
 * <p>
 * Methods to override: {@link #child(String)}, {@link #sibling(String)}, {@link #parent()}, {@link #exists()}, directory listing
 * methods, access (read-only) operations.
 * @author MJ */
public abstract class AbstractFileHandle extends FileHandle {
	private final String path;

	/** @param path cannot be null. Back slashes will be replaced with regular slashes.
	 * @param type cannot be null. */
	public AbstractFileHandle (final String path, final FileType type) {
		if (path == null || type == null) {
			throw new GdxRuntimeException("Cannot create file with unknown path or type.");
		}
		this.path = normalize(path);
		this.type = type;
	}

	/** @param path requested file path.
	 * @return path with corrected slashes. */
	protected static String normalize (String path) {
		path = path.replace('\\', '/');
		return path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
	}

	@Override
	public String path () {
		return path;
	}

	@Override
	public String name () {
		final int index = path.lastIndexOf('/');
		return index < 0 ? path : path.substring(index + 1);
	}

	@Override
	public String extension () {
		final String name = name();
		final int index = name.lastIndexOf('.');
		return index < 0 ? "" : name.substring(index + 1);
	}

	@Override
	public String nameWithoutExtension () {
		final String name = name();
		final int index = name.lastIndexOf('.');
		return index < 0 ? name : name.substring(0, index);
	}

	@Override
	public String pathWithoutExtension () {
		final String name = name();
		final int index = name.lastIndexOf('.');
		return index < 0 ? path : path.substring(0, path.lastIndexOf('.'));
	}

	/** @return path of parent directory. Empty string if in root folder. */
	public String getParentPath () {
		final int index = path.lastIndexOf('/');
		return index < 0 ? "" : path.substring(0, index);
	}

	/** @param sibling name of a sibling file in the same directory as this file.
	 * @return complete path to the sibling file. */
	public String getSiblingPath (final String sibling) {
		return sibling.startsWith("/") ? getParentPath() + sibling : getParentPath() + "/" + sibling;
	}

	/** Should be used only if the file is a directory, otherwise it cannot have children.
	 * @param child name of a child file.
	 * @return name of the child file. */
	public String getChildPath (final String child) {
		return child.startsWith("/") ? path + child : path + "/" + child;
	}

	@Override
	public File file () {
		throw new GdxRuntimeException("This FileHandle is not based on java.io.File.");
	}

	@Override
	public OutputStream write (final boolean append) {
		throw new GdxRuntimeException("This FileHandle is in read-only mode.");
	}

	@Override
	public OutputStream write (final boolean append, final int bufferSize) {
		throw new GdxRuntimeException("This FileHandle is in read-only mode.");
	}

	@Override
	public void write (final InputStream input, final boolean append) {
		throw new GdxRuntimeException("This FileHandle is in read-only mode.");
	}

	@Override
	public void writeBytes (final byte[] bytes, final boolean append) {
		throw new GdxRuntimeException("This FileHandle is in read-only mode.");
	}

	@Override
	public void writeBytes (final byte[] bytes, final int offset, final int length, final boolean append) {
		throw new GdxRuntimeException("This FileHandle is in read-only mode.");
	}

	@Override
	public Writer writer (final boolean append) {
		throw new GdxRuntimeException("This FileHandle is in read-only mode.");
	}

	@Override
	public Writer writer (final boolean append, final String charset) {
		throw new GdxRuntimeException("This FileHandle is in read-only mode.");
	}

	@Override
	public void writeString (final String string, final boolean append) {
		throw new GdxRuntimeException("This FileHandle is in read-only mode.");
	}

	@Override
	public void writeString (final String string, final boolean append, final String charset) {
		throw new GdxRuntimeException("This FileHandle is in read-only mode.");
	}

	@Override
	public void mkdirs () {
		throw new GdxRuntimeException("This FileHandle is in read-only mode.");
	}

	@Override
	public boolean delete () {
		throw new GdxRuntimeException("This FileHandle is in read-only mode.");
	}

	@Override
	public boolean deleteDirectory () {
		throw new GdxRuntimeException("This FileHandle is in read-only mode.");
	}

	@Override
	public void emptyDirectory () {
		throw new GdxRuntimeException("This FileHandle is in read-only mode.");
	}

	@Override
	public void emptyDirectory (final boolean preserveTree) {
		throw new GdxRuntimeException("This FileHandle is in read-only mode.");
	}

	@Override
	public void copyTo (final FileHandle dest) {
		throw new GdxRuntimeException("This FileHandle is in read-only mode.");
	}

	@Override
	public void moveTo (final FileHandle dest) {
		throw new GdxRuntimeException("This FileHandle is in read-only mode.");
	}

	@Override
	public long lastModified () {
		Gdx.app.log(DragomeApplication.LOGGING_TAG, "Last modification time is not available for Dragome files.");
		return 0;
	}

	@Override
	public String toString () {
		return path;
	}

	@Override
	public boolean equals (final Object obj) {
		return obj == this || obj instanceof AbstractFileHandle && ((FileHandle)obj).path().equals(path);
	}

	@Override
	public int hashCode () {
		return path.hashCode();
	}
}
