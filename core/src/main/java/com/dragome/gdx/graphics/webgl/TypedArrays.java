
package com.dragome.gdx.graphics.webgl;

import org.w3c.dom.typedarray.Float32Array;
import org.w3c.dom.typedarray.Float64Array;
import org.w3c.dom.typedarray.Int16Array;
import org.w3c.dom.typedarray.Int32Array;
import org.w3c.dom.typedarray.Int8Array;
import org.w3c.dom.typedarray.Uint16Array;
import org.w3c.dom.typedarray.Uint32Array;
import org.w3c.dom.typedarray.Uint8Array;

import com.dragome.web.html.dom.w3c.TypedArraysFactory;

/** Utilities for working with typed arrays.
 * @author MJ */
public class TypedArrays {
	private TypedArrays () {
	}

	/** @param size size of the array.
	 * @return a new instance of {@link Uint8Array}. */
	public static Uint8Array createUint8Array (final int size) {
		return TypedArraysFactory.createInstanceOf(Uint8Array.class, size);
	}

	/** @param array its values will be copied.
	 * @return a new instance of {@link Uint8Array} with values copied from the passed array. */
	public static Uint8Array copyUnsigned (final byte[] array) {
		final Uint8Array result = createUint8Array(array.length);
		for (int i = 0, l = array.length; i < l; i++) {
			result.set(i, array[i]);
		}
		return result;
	}

	/** @param size size of the array.
	 * @return a new instance of {@link Uint16Array}. */
	public static Uint16Array createUint16Array (final int size) {
		return TypedArraysFactory.createInstanceOf(Uint16Array.class, size);
	}

	/** @param array its values will be copied.
	 * @return a new instance of {@link Uint16Array} with values copied from the passed array. */
	public static Uint16Array copyUnsigned (final short[] array) {
		final Uint16Array result = createUint16Array(array.length);
		for (int i = 0, l = array.length; i < l; i++) {
			result.set(i, array[i]);
		}
		return result;
	}

	/** @param size size of the array.
	 * @return a new instance of {@link Uint32Array}. */
	public static Uint32Array createUint32Array (final int size) {
		return TypedArraysFactory.createInstanceOf(Uint32Array.class, size);
	}

	/** @param array its values will be copied.
	 * @return a new instance of {@link Uint32Array} with values copied from the passed array. */
	public static Uint32Array copyUnsigned (final int[] array) {
		final Uint32Array result = createUint32Array(array.length);
		for (int i = 0, l = array.length; i < l; i++) {
			result.set(i, array[i]);
		}
		return result;
	}

	/** @param size size of the array.
	 * @return a new instance of {@link Float32Array}. */
	public static Float32Array createFloat32Array (final int size) {
		return TypedArraysFactory.createInstanceOf(Float32Array.class, size);
	}

	/** @param array its values will be copied.
	 * @return a new instance of {@link Float32Array} with values copied from the passed array. */
	public static Float32Array copy (final float[] array) {
		final Float32Array result = createFloat32Array(array.length);
		for (int i = 0, l = array.length; i < l; i++) {
			result.set(i, array[i]);
		}
		return result;
	}

	/** @param size size of the array.
	 * @return a new instance of {@link Float64Array}. */
	public static Float64Array createFloat64Array (final int size) {
		return TypedArraysFactory.createInstanceOf(Float64Array.class, size);
	}

	/** @param array its values will be copied.
	 * @return a new instance of {@link Float64Array} with values copied from the passed array. */
	public static Float64Array copy (final double[] array) {
		final Float64Array result = createFloat64Array(array.length);
		for (int i = 0, l = array.length; i < l; i++) {
			result.set(i, array[i]);
		}
		return result;
	}

	/** @param size size of the array.
	 * @return a new instance of {@link Int8Array}. */
	public static Int8Array createInt8Array (final int size) {
		return TypedArraysFactory.createInstanceOf(Int8Array.class, size);
	}

	/** @param array its values will be copied.
	 * @return a new instance of {@link Int8Array} with values copied from the passed array. */
	public static Int8Array copy (final byte[] array) {
		final Int8Array result = createInt8Array(array.length);
		for (int i = 0, l = array.length; i < l; i++) {
			result.set(i, array[i]);
		}
		return result;
	}

	/** @param size size of the array.
	 * @return a new instance of {@link Int16Array}. */
	public static Int16Array createInt16Array (final int size) {
		return TypedArraysFactory.createInstanceOf(Int16Array.class, size);
	}

	/** @param array its values will be copied.
	 * @return a new instance of {@link Int16Array} with values copied from the passed array. */
	public static Int16Array copy (final short[] array) {
		final Int16Array result = createInt16Array(array.length);
		for (int i = 0, l = array.length; i < l; i++) {
			result.set(i, array[i]);
		}
		return result;
	}

	/** @param size size of the array.
	 * @return a new instance of {@link Int32Array}. */
	public static Int32Array createInt32Array (final int size) {
		return TypedArraysFactory.createInstanceOf(Int32Array.class, size);
	}

	/** @param array its values will be copied.
	 * @return a new instance of {@link Int32Array} with values copied from the passed array. */
	public static Int32Array copy (final int[] array) {
		final Int32Array result = createInt32Array(array.length);
		for (int i = 0, l = array.length; i < l; i++) {
			result.set(i, array[i]);
		}
		return result;
	}
}
