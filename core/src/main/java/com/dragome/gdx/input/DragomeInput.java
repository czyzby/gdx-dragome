
package com.dragome.gdx.input;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.KeyboardEvent;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.html.HTMLCanvasElement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.TimeUtils;
import com.dragome.commons.javascript.ScriptHelper;
import com.dragome.gdx.DragomeApplication;
import com.dragome.web.enhancers.jsdelegate.JsCast;

/** Handles {@link Input} events in the Dragome application. Supports only keyboard and touch events.
 * @author MJ */
@SuppressWarnings("unused")
public class DragomeInput implements ResettableInput {
	private static final int MAX_TOUCHES = 20;
	/** The left mouse button. */
	public static final int BUTTON_LEFT = 1;
	/** The middle mouse button. */
	public static final int BUTTON_MIDDLE = 4;
	/** The right mouse button. */
	public static final int BUTTON_RIGHT = 2;

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

	private boolean hasFocus = true;
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
		final EventTarget canvasTarget = JsCast.castTo(canvas, EventTarget.class);
		final EventTarget document = ScriptHelper.evalCasting("document", EventTarget.class, this);

		org.w3c.dom.events.EventListener listener = getMouseDownListener();
		canvasTarget.addEventListener("mousedown", listener, true);
		document.addEventListener("mousedown", listener, true);

		listener = getMouseUpListener();
		canvasTarget.addEventListener("mouseup", listener, true);
		document.addEventListener("mouseup", listener, true);

		listener = getMouseMoveListener();
		canvasTarget.addEventListener("mousemove", listener, true);
		document.addEventListener("mousemove", listener, true);

		listener = getMouseScrollListener();
		canvasTarget.addEventListener("mousemove", listener, true);
		canvasTarget.addEventListener("DOMMouseScroll", listener, true);

		listener = getKeyDownListener();
		document.addEventListener("keydown", listener, false);
		listener = getKeyUpListener();
		document.addEventListener("keyup", listener, false);
		listener = getKeyPressListener();
		document.addEventListener("keypress", listener, false);

		// TODO touch events
		// addEventListener(canvas, "touchstart", this, true);
		// addEventListener(canvas, "touchmove", this, true);
		// addEventListener(canvas, "touchcancel", this, true);
		// addEventListener(canvas, "touchend", this, true);
	}

	// TODO Implement relative X and Y.
	protected int getRelativeX (final MouseEvent event, final HTMLCanvasElement target) {
		/** Kindly borrowed from PlayN. **/
		// final float xScaleRatio = target.getWidth() * 1f / target.getClientWidth(); // Correct for canvas CSS scaling
		// return Math.round(xScaleRatio
		// * (e.getClientX() - target.getAbsoluteLeft() + target.getScrollLeft() + target.getOwnerDocument().getScrollLeft()));
		return event.getClientX();
	}

	protected int getRelativeY (final MouseEvent event, final HTMLCanvasElement target) {
		/** Kindly borrowed from PlayN. **/
		// final float yScaleRatio = target.getHeight() * 1f / target.getClientHeight(); // Correct for canvas CSS scaling
		// return Math.round(yScaleRatio
		// * (e.getClientY() - target.getAbsoluteTop() + target.getScrollTop() + target.getOwnerDocument().getScrollTop()));
		return event.getClientY();
	}

	/** @param button native button code.
	 * @return {@link Buttons} code. */
	protected int getButton (final int button) {
		if (button == BUTTON_LEFT) {
			return Buttons.LEFT;
		} else if (button == BUTTON_RIGHT) {
			return Buttons.RIGHT;
		} else if (button == BUTTON_MIDDLE) {
			return Buttons.MIDDLE;
		}
		return Buttons.LEFT;
	}

	protected float getMovementX (final MouseEvent event) {
		ScriptHelper.put("_event", event, this);
		return ScriptHelper.evalFloat("event.movementX || event.webkitMovementX || 0", this);
	}

	protected float getMovementY (final MouseEvent event) {
		ScriptHelper.put("_event", event, this);
		return ScriptHelper.evalFloat("event.movementY || event.webkitMovementY || 0", this);
	}

	/** @return listener handling "mousedown" event. */
	protected org.w3c.dom.events.EventListener getMouseDownListener () {
		return new org.w3c.dom.events.EventListener() {
			@Override
			public void handleEvent (final Event evt) {
				final MouseEvent event = JsCast.castTo(evt, MouseEvent.class);
				if (!event.getTarget().equals(canvas) || touched[0]) { // TODO getTarget or getCurrentTarget?
					final float mouseX = getRelativeX(event, canvas);
					final float mouseY = getRelativeY(event, canvas);
					if (mouseX < 0 || mouseX > Gdx.graphics.getWidth() || mouseY < 0 || mouseY > Gdx.graphics.getHeight()) {
						hasFocus = false;
					}
					return;
				}
				hasFocus = true;
				justTouched = true;
				touched[0] = true;
				pressedButtons.add(getButton(event.getButton()));
				deltaX[0] = 0;
				deltaY[0] = 0;
				if (isCursorCatched()) {
					touchX[0] += getMovementX(event);
					touchY[0] += getMovementY(event);
				} else {
					touchX[0] = getRelativeX(event, canvas);
					touchY[0] = getRelativeY(event, canvas);
				}
				currentEventTimeStamp = TimeUtils.nanoTime();
				if (processor != null) {
					processor.touchDown(touchX[0], touchY[0], 0, getButton(event.getButton()));
				}
			}
		};
	}

	/** @return handles "mouseup" event. */
	protected org.w3c.dom.events.EventListener getMouseUpListener () {
		return new org.w3c.dom.events.EventListener() {
			@Override
			public void handleEvent (final Event evt) {
				final MouseEvent event = JsCast.castTo(evt, MouseEvent.class);
				if (!touched[0]) {
					return;
				}
				pressedButtons.remove(getButton(event.getButton()));
				touched[0] = pressedButtons.size > 0;
				if (isCursorCatched()) {
					deltaX[0] = (int)getMovementX(event);
					deltaY[0] = (int)getMovementY(event);
					touchX[0] += getMovementX(event);
					touchY[0] += getMovementY(event);
				} else {
					deltaX[0] = getRelativeX(event, canvas) - touchX[0];
					deltaY[0] = getRelativeY(event, canvas) - touchY[0];
					touchX[0] = getRelativeX(event, canvas);
					touchY[0] = getRelativeY(event, canvas);
				}
				currentEventTimeStamp = TimeUtils.nanoTime();
				touched[0] = false;
				if (processor != null) {
					processor.touchUp(touchX[0], touchY[0], 0, getButton(event.getButton()));
				}
			}
		};
	}

	/** @return handles "mousemove" event. */
	protected org.w3c.dom.events.EventListener getMouseMoveListener () {
		return new org.w3c.dom.events.EventListener() {
			@Override
			public void handleEvent (final Event evt) {
				final MouseEvent event = JsCast.castTo(evt, MouseEvent.class);
				if (isCursorCatched()) {
					deltaX[0] = (int)getMovementX(event);
					deltaY[0] = (int)getMovementY(event);
					touchX[0] += getMovementX(event);
					touchY[0] += getMovementY(event);
				} else {
					deltaX[0] = getRelativeX(event, canvas) - touchX[0];
					deltaY[0] = getRelativeY(event, canvas) - touchY[0];
					touchX[0] = getRelativeX(event, canvas);
					touchY[0] = getRelativeY(event, canvas);
				}
				currentEventTimeStamp = TimeUtils.nanoTime();
				if (processor != null) {
					if (touched[0]) {
						processor.touchDragged(touchX[0], touchY[0], 0);
					} else {
						processor.mouseMoved(touchX[0], touchY[0]);
					}
				}
			}
		};
	}

	/** @return handles "mousewheel" and "DOMMouseScroll" events. */
	protected org.w3c.dom.events.EventListener getMouseScrollListener () {
		return new org.w3c.dom.events.EventListener() {
			@Override
			public void handleEvent (final Event evt) {
				if (processor != null) {
					processor.scrolled((int)getMouseWheelVelocity(evt));
				}
				currentEventTimeStamp = TimeUtils.nanoTime();
				evt.preventDefault();
			}

			private float getMouseWheelVelocity (final Event evt) {
				// TODO Implement mouse wheel velocity.
				return 0f;
				// var delta = 0.0;
				// var agentInfo = @com.badlogic.gdx.backends.gwt.GwtApplication::agentInfo()();
				//
				// if (agentInfo.isFirefox) {
				// if (agentInfo.isMacOS) {
				// delta = 1.0 * evt.detail;
				// } else {
				// delta = 1.0 * evt.detail / 3;
				// }
				// } else if (agentInfo.isOpera) {
				// if (agentInfo.isLinux) {
				// delta = -1.0 * evt.wheelDelta / 80;
				// } else {
				// // on mac
				// delta = -1.0 * evt.wheelDelta / 40;
				// }
				// } else if (agentInfo.isChrome || agentInfo.isSafari) {
				// delta = -1.0 * evt.wheelDelta / 120;
				// // handle touchpad for chrome
				// if (Math.abs(delta) < 1) {
				// if (agentInfo.isWindows) {
				// delta = -1.0 * evt.wheelDelta;
				// } else if (agentInfo.isMacOS) {
				// delta = -1.0 * evt.wheelDelta / 3;
				// }
				// }
				// }
				// return delta;
			}
		};
	}

	/** @return handles "keydown" events. */
	protected EventListener getKeyDownListener () {
		return new org.w3c.dom.events.EventListener() {
			@Override
			public void handleEvent (final Event evt) {
				final int code = KeyCodes.toKey(JsCast.castTo(evt, KeyboardEvent.class).getKeyCode());
				if (code == 67) {
					evt.preventDefault();
					if (processor != null) {
						processor.keyDown(code);
						processor.keyTyped('\b');
					}
				} else {
					if (!pressedKeys[code]) {
						pressedKeyCount++;
						pressedKeys[code] = true;
						keyJustPressed = true;
						justPressedKeys[code] = true;
						if (processor != null) {
							processor.keyDown(code);
						}
					}
				}
			}
		};
	}

	/** @return handles "keyup" events. */
	protected org.w3c.dom.events.EventListener getKeyUpListener () {
		return new org.w3c.dom.events.EventListener() {
			@Override
			public void handleEvent (final Event evt) {
				final int code = KeyCodes.toKey(JsCast.castTo(evt, KeyboardEvent.class).getKeyCode());
				if (pressedKeys[code]) {
					pressedKeyCount--;
					pressedKeys[code] = false;
				}
				if (processor != null) {
					processor.keyUp(code);
				}
			}
		};
	}

	/** @return handles "keypress" events. */
	protected org.w3c.dom.events.EventListener getKeyPressListener () {
		return new org.w3c.dom.events.EventListener() {
			@Override
			public void handleEvent (final Event evt) {
				final char character = (char)JsCast.castTo(evt, KeyboardEvent.class).getCharCode();
				if (processor != null) {
					processor.keyTyped(character);
				}
			}
		};
	}

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
