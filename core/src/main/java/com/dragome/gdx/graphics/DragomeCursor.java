
package com.dragome.gdx.graphics;

import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.GdxRuntimeException;

/** Allows to set a custom cursor for the application.
 *
 * @author MJ */
public class DragomeCursor implements Cursor {
	private String css;

	/** @param pixmap its width and height have to be powers-of-two.
	 * @param xHotspot has to be within pixmap bounds.
	 * @param yHotspot has to be within pixmap bounds. */
	public DragomeCursor (final Pixmap pixmap, final int xHotspot, final int yHotspot) {
		if (pixmap == null) {
			css = "auto";
			return;
		}
		validateData(pixmap, xHotspot, yHotspot);
		css = "auto"; // TODO prepare cursor when valid CSS
	}

	private static void validateData (final Pixmap pixmap, final int xHotspot, final int yHotspot) {
		if (pixmap.getFormat() != Pixmap.Format.RGBA8888) {
			throw new GdxRuntimeException("Cursor image pixmap is not in RGBA8888 format.");
		}
		if ((pixmap.getWidth() & pixmap.getWidth() - 1) != 0) {
			throw new GdxRuntimeException(
				"Cursor image pixmap width of " + pixmap.getWidth() + " is not a power-of-two greater than zero.");
		}
		if ((pixmap.getHeight() & pixmap.getHeight() - 1) != 0) {
			throw new GdxRuntimeException(
				"Cursor image pixmap height of " + pixmap.getHeight() + " is not a power-of-two greater than zero.");
		}
		if (xHotspot < 0 || xHotspot >= pixmap.getWidth()) {
			throw new GdxRuntimeException(
				"xHotspot coordinate of " + xHotspot + " is not within image width bounds: [0, " + pixmap.getWidth() + ").");
		}
		if (yHotspot < 0 || yHotspot >= pixmap.getHeight()) {
			throw new GdxRuntimeException(
				"yHotspot coordinate of " + yHotspot + " is not within image height bounds: [0, " + pixmap.getHeight() + ").");
		}
	}

	/** @param systemCursor type of system cursor.
	 * @return browser name of the system cursor. */
	public static String getNameForSystemCursor (final SystemCursor systemCursor) { // Based on GWT backend.
		if (systemCursor == SystemCursor.Arrow || systemCursor == null) {
			return "default";
		} else if (systemCursor == SystemCursor.Crosshair) {
			return "crosshair";
		} else if (systemCursor == SystemCursor.Hand) {
			return "pointer"; // Don't change to 'hand', 'hand' doesn't work in the newer IEs.
		} else if (systemCursor == SystemCursor.HorizontalResize) {
			return "ew-resize";
		} else if (systemCursor == SystemCursor.VerticalResize) {
			return "ns-resize";
		} else if (systemCursor == SystemCursor.Ibeam) {
			return "text";
		} else {
			throw new GdxRuntimeException("Unknown system cursor: " + systemCursor);
		}
	}

	@Override
	public void dispose () {
	} // Pixmap has to be disposed manually.

	/** @return CSS value of the cursor. */
	public String getCss () {
		return css;
	}
}
