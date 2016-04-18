
package com.dragome.gdx.util;

/** Utilities for exceptions. Do not initiate, use static methods.
 *
 * @author MJ */
public class Exceptions {
	/** Use for all empty catch blocks to document that the exceptions are expected and handled, and should be ignored. Along with
	 * unused exception variables warning, this allows to avoid rethrowing decorated exceptions without the cause included in
	 * exception constructor.
	 *
	 * @param exception will be ignored. */
	public static void ignore (final Throwable exception) {
	}
}
