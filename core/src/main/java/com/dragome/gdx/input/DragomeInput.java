
package com.dragome.gdx.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.dragome.commons.javascript.ScriptHelper;
import com.dragome.gdx.DragomeApplication;

/** Handles {@link Input} events in the Dragome application. Supports only keyboard and touch events.
 * @author MJ */
public class DragomeInput implements Input {
	private InputProcessor processor;

	@Override
	public void setInputProcessor (final InputProcessor processor) {
		this.processor = processor;
	}

	@Override
	public InputProcessor getInputProcessor () {
		return processor;
	}

	@Override
	public int getX () {
		return 0; // TODO implement mouse
	}

	@Override
	public int getX (final int pointer) {
		return 0; // TODO implement mouse
	}

	@Override
	public int getDeltaX () {
		return 0;// TODO implement mouse
	}

	@Override
	public int getDeltaX (final int pointer) {
		return 0;// TODO implement mouse
	}

	@Override
	public int getY () {
		return 0; // TODO implement mouse
	}

	@Override
	public int getY (final int pointer) {
		return 0; // TODO implement mouse
	}

	@Override
	public int getDeltaY () {
		return 0; // TODO implement mouse
	}

	@Override
	public int getDeltaY (final int pointer) {
		return 0; // TODO implement mouse
	}

	@Override
	public boolean isTouched () {
		return false; // TODO implement mouse
	}

	@Override
	public boolean justTouched () {
		return false; // TODO implement mouse
	}

	@Override
	public boolean isTouched (final int pointer) {
		return false; // TODO implement mouse
	}

	@Override
	public boolean isButtonPressed (final int button) {
		return false; // TODO implement keyboard
	}

	@Override
	public boolean isKeyPressed (final int key) {
		return false; // TODO implement keyboard
	}

	@Override
	public boolean isKeyJustPressed (final int key) {
		return false; // TODO implement keyboard
	}

	@Override
	public void getTextInput (final TextInputListener listener, final String title, final String text, final String hint) {
		// TODO implement keyboard
	}

	@Override
	public boolean isPeripheralAvailable (final Peripheral peripheral) {
		return peripheral == Peripheral.MultitouchScreen && isTouchScreen();
	}

	/** @return true if the browser supports multitouch. */
	public boolean isTouchScreen () {
		return ScriptHelper.evalBoolean("'ontouchstart' in window||(navigator&&navigator.msMaxTouchPoints>0)", this);
	}

	@Override
	public long getCurrentEventTime () {
		return 0; // TODO implement current event time
	}

	@Override
	public void setCursorCatched (final boolean catched) {
		// TODO implement cursor catching
	}

	@Override
	public boolean isCursorCatched () {
		return false; // TODO implement cursor catching
	}

	@Override
	public void setCursorPosition (final int x, final int y) {
		// TODO implement cursor position setting
	}

	@Override
	public Orientation getNativeOrientation () {
		return Orientation.Landscape;
	}

	// Unsupported operations:
	/** Displays error log after invoking method of unsupported functionality.
	 * @param functionality begins the error message sentence. */
	protected void logNotSupportedMessage (final String functionality) {
		Gdx.app.log(DragomeApplication.LOGGING_TAG, functionality + " is not supported on Dragome backend.");
	}

	@Override
	public float getAccelerometerX () {
		logNotSupportedMessage("Accelerometer");
		return 0f;
	}

	@Override
	public float getAccelerometerY () {
		logNotSupportedMessage("Accelerometer");
		return 0f;
	}

	@Override
	public float getAccelerometerZ () {
		logNotSupportedMessage("Accelerometer");
		return 0f;
	}

	@Override
	public float getGyroscopeX () {
		logNotSupportedMessage("Gyroscope");
		return 0f;
	}

	@Override
	public float getGyroscopeY () {
		logNotSupportedMessage("Gyroscope");
		return 0f;
	}

	@Override
	public float getGyroscopeZ () {
		logNotSupportedMessage("Gyroscope");
		return 0f;
	}

	@Override
	public void setOnscreenKeyboardVisible (final boolean visible) {
		logNotSupportedMessage("On screen keyboard");
	}

	@Override
	public void vibrate (final int milliseconds) {
		logNotSupportedMessage("Vibrate");
	}

	@Override
	public void vibrate (final long[] pattern, final int repeat) {
		logNotSupportedMessage("Vibrate");
	}

	@Override
	public void cancelVibrate () {
		logNotSupportedMessage("Vibrate");
	}

	@Override
	public float getAzimuth () {
		logNotSupportedMessage("Azimuth");
		return 0f;
	}

	@Override
	public float getPitch () {
		logNotSupportedMessage("Pitch");
		return 0;
	}

	@Override
	public float getRoll () {
		logNotSupportedMessage("Roll");
		return 0;
	}

	@Override
	public void getRotationMatrix (final float[] matrix) {
		logNotSupportedMessage("Rotation");
	}

	@Override
	public int getRotation () {
		logNotSupportedMessage("Rotation");
		return 0;
	}

	@Override
	public void setCatchBackKey (final boolean catchBack) {
		logNotSupportedMessage("Back key");
	}

	@Override
	public boolean isCatchBackKey () {
		logNotSupportedMessage("Back key");
		return false;
	}

	@Override
	public void setCatchMenuKey (final boolean catchMenu) {
		logNotSupportedMessage("Menu key");
	}

	@Override
	public boolean isCatchMenuKey () {
		logNotSupportedMessage("Menu key");
		return false;
	}
}
