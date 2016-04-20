
package com.dragome.gdx.input;

import org.w3c.dom.html.HTMLCanvasElement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntSet;
import com.dragome.commons.javascript.ScriptHelper;
import com.dragome.gdx.DragomeApplication;

/** Handles {@link Input} events in the Dragome application. Supports only keyboard and touch events.
 * @author MJ */
@SuppressWarnings("unused")
public class DragomeInput implements ResettableInput {
	private static final int MAX_TOUCHES = 20;

	private final HTMLCanvasElement canvas;
	private InputProcessor processor;

	private final IntMap<Integer> touchMap = new IntMap<Integer>(20);
	private final IntSet pressedButtons = new IntSet();
	private final boolean[] touched = new boolean[MAX_TOUCHES];
	private final int[] touchX = new int[MAX_TOUCHES];
	private final int[] touchY = new int[MAX_TOUCHES];
	private final int[] deltaX = new int[MAX_TOUCHES];
	private final int[] deltaY = new int[MAX_TOUCHES];
	private final boolean[] pressedKeys = new boolean[256];
	private final boolean[] justPressedKeys = new boolean[256];

	private final boolean hasFocus = true;
	private boolean justTouched;
	private int pressedKeyCount;
	private boolean keyJustPressed;
	private char lastKeyCharPressed;
	private float keyRepeatTimer;
	private long currentEventTimeStamp;

	public DragomeInput (final DragomeApplication application) {
		canvas = application.getCanvas();
		addEventListeners();
		addInputPolyfills();
	}

	protected void addInputPolyfills () {
		// navigator.pointer:
		ScriptHelper.evalNoResult("if (!navigator.pointer) {navigator.pointer = navigator.webkitPointer || navigator.mozPointer;}",
			this);
		// exitPointerLock():
		ScriptHelper.evalNoResult(
			"if (!document.exitPointerLock) { document.exitPointerLock = (function() { return document.webkitExitPointerLock || document.mozExitPointerLock || function() {if (navigator.pointer) { navigator.pointer.unlock(); } }; })();}",
			this);
	}

	/** Adds input event listeners. */
	protected void addEventListeners () {
		// TODO implement event listeners
		// canvas.addEventListener(canvas, "mousedown", this, true);
		// addEventListener(Document.get(), "mousedown", this, true);
		// addEventListener(canvas, "mouseup", this, true);
		// addEventListener(Document.get(), "mouseup", this, true);
		// addEventListener(canvas, "mousemove", this, true);
		// addEventListener(Document.get(), "mousemove", this, true);
		// addEventListener(canvas, getMouseWheelEvent(), this, true);
		// addEventListener(Document.get(), "keydown", this, false);
		// addEventListener(Document.get(), "keyup", this, false);
		// addEventListener(Document.get(), "keypress", this, false);
		//
		// addEventListener(canvas, "touchstart", this, true);
		// addEventListener(canvas, "touchmove", this, true);
		// addEventListener(canvas, "touchcancel", this, true);
		// addEventListener(canvas, "touchend", this, true);
	}
	/*
	 * TODO separate into multiple listeners.
	 *
	 * private void handleEvent (NativeEvent e) { if (e.getType().equals("mousedown")) { if (!e.getEventTarget().equals(canvas) ||
	 * touched[0]) { float mouseX = getRelativeX(e, canvas); float mouseY = getRelativeY(e, canvas); if (mouseX < 0 || mouseX >
	 * Gdx.graphics.getWidth() || mouseY < 0 || mouseY > Gdx.graphics.getHeight()) { hasFocus = false; } return; } hasFocus = true;
	 * this.justTouched = true; this.touched[0] = true; this.pressedButtons.add(getButton(e.getButton())); this.deltaX[0] = 0;
	 * this.deltaY[0] = 0; if (isCursorCatched()) { this.touchX[0] += getMovementXJSNI(e); this.touchY[0] += getMovementYJSNI(e); }
	 * else { this.touchX[0] = getRelativeX(e, canvas); this.touchY[0] = getRelativeY(e, canvas); } this.currentEventTimeStamp =
	 * TimeUtils.nanoTime(); if (processor != null) processor.touchDown(touchX[0], touchY[0], 0, getButton(e.getButton())); }
	 *
	 * if (e.getType().equals("mousemove")) { if (isCursorCatched()) { this.deltaX[0] = (int)getMovementXJSNI(e); this.deltaY[0] =
	 * (int)getMovementYJSNI(e); this.touchX[0] += getMovementXJSNI(e); this.touchY[0] += getMovementYJSNI(e); } else {
	 * this.deltaX[0] = getRelativeX(e, canvas) - touchX[0]; this.deltaY[0] = getRelativeY(e, canvas) - touchY[0]; this.touchX[0] =
	 * getRelativeX(e, canvas); this.touchY[0] = getRelativeY(e, canvas); } this.currentEventTimeStamp = TimeUtils.nanoTime(); if
	 * (processor != null) { if (touched[0]) processor.touchDragged(touchX[0], touchY[0], 0); else processor.mouseMoved(touchX[0],
	 * touchY[0]); } }
	 *
	 * if (e.getType().equals("mouseup")) { if (!touched[0]) return; this.pressedButtons.remove(getButton(e.getButton()));
	 * this.touched[0] = pressedButtons.size > 0; if (isCursorCatched()) { this.deltaX[0] = (int)getMovementXJSNI(e);
	 * this.deltaY[0] = (int)getMovementYJSNI(e); this.touchX[0] += getMovementXJSNI(e); this.touchY[0] += getMovementYJSNI(e); }
	 * else { this.deltaX[0] = getRelativeX(e, canvas) - touchX[0]; this.deltaY[0] = getRelativeY(e, canvas) - touchY[0];
	 * this.touchX[0] = getRelativeX(e, canvas); this.touchY[0] = getRelativeY(e, canvas); } this.currentEventTimeStamp =
	 * TimeUtils.nanoTime(); this.touched[0] = false; if (processor != null) processor.touchUp(touchX[0], touchY[0], 0,
	 * getButton(e.getButton())); } if (e.getType().equals(getMouseWheelEvent())) { if (processor != null) {
	 * processor.scrolled((int)getMouseWheelVelocity(e)); } this.currentEventTimeStamp = TimeUtils.nanoTime(); e.preventDefault();
	 * } if (e.getType().equals("keydown") && hasFocus) { // System.out.println("keydown"); int code = keyForCode(e.getKeyCode());
	 * if (code == 67) { e.preventDefault(); if (processor != null) { processor.keyDown(code); processor.keyTyped('\b'); } } else {
	 * if (!pressedKeys[code]) { pressedKeyCount++; pressedKeys[code] = true; keyJustPressed = true; justPressedKeys[code] = true;
	 * if (processor != null) { processor.keyDown(code); } } } }
	 *
	 * if (e.getType().equals("keypress") && hasFocus) { // System.out.println("keypress"); char c = (char)e.getCharCode(); if
	 * (processor != null) processor.keyTyped(c); }
	 *
	 * if (e.getType().equals("keyup") && hasFocus) { // System.out.println("keyup"); int code = keyForCode(e.getKeyCode()); if
	 * (pressedKeys[code]) { pressedKeyCount--; pressedKeys[code] = false; } if (processor != null) { processor.keyUp(code); } }
	 *
	 * if (e.getType().equals("touchstart")) { this.justTouched = true; JsArray<Touch> touches = e.getChangedTouches(); for (int i
	 * = 0, j = touches.length(); i < j; i++) { Touch touch = touches.get(i); int real = touch.getIdentifier(); int touchId;
	 * touchMap.put(real, touchId = getAvailablePointer()); touched[touchId] = true; touchX[touchId] = getRelativeX(touch, canvas);
	 * touchY[touchId] = getRelativeY(touch, canvas); deltaX[touchId] = 0; deltaY[touchId] = 0; if (processor != null) {
	 * processor.touchDown(touchX[touchId], touchY[touchId], touchId, Buttons.LEFT); } } this.currentEventTimeStamp =
	 * TimeUtils.nanoTime(); e.preventDefault(); } if (e.getType().equals("touchmove")) { JsArray<Touch> touches =
	 * e.getChangedTouches(); for (int i = 0, j = touches.length(); i < j; i++) { Touch touch = touches.get(i); int real =
	 * touch.getIdentifier(); int touchId = touchMap.get(real); deltaX[touchId] = getRelativeX(touch, canvas) - touchX[touchId];
	 * deltaY[touchId] = getRelativeY(touch, canvas) - touchY[touchId]; touchX[touchId] = getRelativeX(touch, canvas);
	 * touchY[touchId] = getRelativeY(touch, canvas); if (processor != null) { processor.touchDragged(touchX[touchId],
	 * touchY[touchId], touchId); } } this.currentEventTimeStamp = TimeUtils.nanoTime(); e.preventDefault(); } if
	 * (e.getType().equals("touchcancel")) { JsArray<Touch> touches = e.getChangedTouches(); for (int i = 0, j = touches.length();
	 * i < j; i++) { Touch touch = touches.get(i); int real = touch.getIdentifier(); int touchId = touchMap.get(real);
	 * touchMap.remove(real); touched[touchId] = false; deltaX[touchId] = getRelativeX(touch, canvas) - touchX[touchId];
	 * deltaY[touchId] = getRelativeY(touch, canvas) - touchY[touchId]; touchX[touchId] = getRelativeX(touch, canvas);
	 * touchY[touchId] = getRelativeY(touch, canvas); if (processor != null) { processor.touchUp(touchX[touchId], touchY[touchId],
	 * touchId, Buttons.LEFT); } } this.currentEventTimeStamp = TimeUtils.nanoTime(); e.preventDefault(); } if
	 * (e.getType().equals("touchend")) { JsArray<Touch> touches = e.getChangedTouches(); for (int i = 0, j = touches.length(); i <
	 * j; i++) { Touch touch = touches.get(i); int real = touch.getIdentifier(); int touchId = touchMap.get(real);
	 * touchMap.remove(real); touched[touchId] = false; deltaX[touchId] = getRelativeX(touch, canvas) - touchX[touchId];
	 * deltaY[touchId] = getRelativeY(touch, canvas) - touchY[touchId]; touchX[touchId] = getRelativeX(touch, canvas);
	 * touchY[touchId] = getRelativeY(touch, canvas); if (processor != null) { processor.touchUp(touchX[touchId], touchY[touchId],
	 * touchId, Buttons.LEFT); } } this.currentEventTimeStamp = TimeUtils.nanoTime(); e.preventDefault(); } // if(hasFocus)
	 * e.preventDefault(); }
	 */

	@Override
	public void setInputProcessor (final InputProcessor processor) {
		this.processor = processor;
	}

	@Override
	public InputProcessor getInputProcessor () {
		return processor;
	}

	@Override
	public void reset () {
		justTouched = false;
		if (keyJustPressed) {
			keyJustPressed = false;
			for (int i = 0; i < justPressedKeys.length; i++) {
				justPressedKeys[i] = false;
			}
		}
	}

	@Override
	public int getX () {
		return touchX[0];
	}

	@Override
	public int getX (final int pointer) {
		return touchX[pointer];
	}

	@Override
	public int getDeltaX () {
		return deltaX[0];
	}

	@Override
	public int getDeltaX (final int pointer) {
		return deltaX[pointer];
	}

	@Override
	public int getY () {
		return touchY[0];
	}

	@Override
	public int getY (final int pointer) {
		return touchY[pointer];
	}

	@Override
	public int getDeltaY () {
		return deltaY[0];
	}

	@Override
	public int getDeltaY (final int pointer) {
		return deltaY[pointer];
	}

	@Override
	public boolean isTouched () {
		for (int pointer = 0; pointer < MAX_TOUCHES; pointer++) {
			if (touched[pointer]) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean justTouched () {
		return justTouched;
	}

	@Override
	public boolean isTouched (final int pointer) {
		return touched[pointer];
	}

	@Override
	public boolean isButtonPressed (final int button) {
		return pressedButtons.contains(button) && touched[0];
	}

	@Override
	public boolean isKeyPressed (final int key) {
		if (key == Keys.ANY_KEY) {
			return pressedKeyCount > 0;
		}
		if (key < 0 || key > 255) {
			return false;
		}
		return pressedKeys[key];
	}

	@Override
	public boolean isKeyJustPressed (final int key) {
		if (key == Keys.ANY_KEY) {
			return keyJustPressed;
		}
		if (key < 0 || key > 255) {
			return false;
		}
		return justPressedKeys[key];
	}

	@Override
	public void getTextInput (final TextInputListener listener, final String title, final String text, final String hint) {
		Gdx.app.error(DragomeApplication.LOGGING_TAG, "DragomeInput#getTextInput is not supported.");
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
		return currentEventTimeStamp;
	}

	@Override
	public void setCursorCatched (final boolean catched) {
		if (catched) {
			ScriptHelper.put("_elem", canvas, this);
			ScriptHelper.evalNoResult(
				"if (!element.requestPointerLock) { element.requestPointerLock = (function() { return element.webkitRequestPointerLock || element.mozRequestPointerLock || function() { if (navigator.pointer) { navigator.pointer.lock(element); } }; })();} element.requestPointerLock()",
				this);
		} else {
			ScriptHelper.evalNoResult("document.exitPointerLock()", this);
		}
	}

	@Override
	public boolean isCursorCatched () {
		return ScriptHelper.evalBoolean(
			"(function(){if(navigator.pointer){if(typeof (navigator.pointer.isLocked) === 'boolean') {return navigator.pointer.isLocked;} else if (typeof (navigator.pointer.isLocked) === 'function') {return navigator.pointer.isLocked();} else if (typeof (navigator.pointer.islocked) === 'function') {return navigator.pointer.islocked();} } return false;})()",
			this);
	}

	@Override
	public void setCursorPosition (final int x, final int y) {
		Gdx.app.error(DragomeApplication.LOGGING_TAG, "DragomeInput#setCursorPosition is not supported.");
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
