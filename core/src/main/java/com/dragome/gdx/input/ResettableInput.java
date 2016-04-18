
package com.dragome.gdx.input;

import com.badlogic.gdx.Input;

/** Extends {@link Input} with {@link #reset()} method.
 * @author MJ */
public interface ResettableInput extends Input {
	/** Clears {@link Input} control variables. */
	void reset ();
}
