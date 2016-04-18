
package com.dragome.gdx.clipboard;

import com.badlogic.gdx.utils.Clipboard;
import com.dragome.commons.javascript.ScriptHelper;
import com.dragome.gdx.util.Exceptions;

/** Clipboard implementation for Dragome applications. Tries hard to use the general system clipboard, but might cause issues on
 * some browsers (especially Safari, which does not support copy and paste commands). Most browsers allow to access clipboard only
 * directly after an user's action, and this is not exactly how the Dragome input processor works. Use {@link MockUpClipboard} if
 * you do not want or need to support global clipboard.
 *
 * @author MJ
 * @see MockUpClipboard */
public class DragomeClipboard implements Clipboard {
	private String cachedContent = "";

	public DragomeClipboard () {
		ScriptHelper.evalNoResult(
			"var textArea=document.createElement('textarea');textArea.style.position='fixed';textArea.style.top=0;textArea.style.left=0;textArea.style.width='2em';textArea.style.height='2em';textArea.style.padding=0;textArea.style.border='none';textArea.style.outline='none';textArea.style.boxShadow='none';textArea.style.background='transparent';this._copy=textArea;",
			this);
	}

	@Override
	public String getContents () {
		try {
			ScriptHelper.put("_cache", cachedContent, this);
			final String content = String.valueOf(ScriptHelper.eval(
				"if(window.clipboardData){return window.clipboardData.getData('Text');}else{document.body.appendChild(this._copy);try{this._copy.select();document.execCommand('paste');return this._copy.value;}catch(err){return _cache;}finally{document.body.removeChild(this._copy;}}",
				this));
			cachedContent = content;
			return content;
		} catch (final Throwable exception) {
			Exceptions.ignore(exception);
			return cachedContent;
		}
	}

	@Override
	public void setContents (final String content) {
		cachedContent = content == null ? "" : content;
		ScriptHelper.evalNoResult(
			"document.body.appendChild(this._copy);try{this._copy.select();document.execCommand('copy');}catch(err){}finally{document.body.removeChild(this._copy);}",
			this);
	}
}
