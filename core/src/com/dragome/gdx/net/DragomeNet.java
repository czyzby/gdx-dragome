
package com.dragome.gdx.net;

import java.util.Map.Entry;

import org.w3c.dom.XMLHttpRequest;
import org.w3c.dom.html.Function;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.dragome.commons.javascript.ScriptHelper;

/** {@link Net} for Dragome backend. Opens URIs in a separate tab. Allows to send and cancel HTTP requests. Does not support TCP
 * server or client sockets. If you need a more robust networking solution, try looking into web sockets.
 * @author MJ */
public class DragomeNet implements Net {
	private final ObjectMap<HttpRequest, XMLHttpRequest> requests = new ObjectMap<HttpRequest, XMLHttpRequest>();
	private final ObjectMap<HttpRequest, HttpResponseListener> listeners = new ObjectMap<HttpRequest, HttpResponseListener>();

	@Override
	public boolean openURI (final String URI) {
		ScriptHelper.put("_uri", URI, this);
		ScriptHelper.eval("window.open(_uri,'_blank')", this);
		return false;
	}

	@Override
	public void sendHttpRequest (final HttpRequest httpRequest, final HttpResponseListener httpResponseListener) {
		if (httpRequest.getUrl() == null || httpRequest.getMethod() == null || httpResponseListener == null) {
			httpResponseListener.failed(new GdxRuntimeException("Unable to send request without URL, method or listener."));
			return;
		}
		final XMLHttpRequest request = (XMLHttpRequest)ScriptHelper.eval("new XMLHttpRequest();", this);
		request.setOnreadystatechange(new Function() { // TODO Is that the correct way to pass function?
			@Override
			public Object call (final Object... arguments) {
			if (request.getReadyState() != XMLHttpRequest.DONE) {
				return null;
			}
			httpResponseListener.handleHttpResponse(new DragomeHttpResponse(request));
			requests.remove(httpRequest);
			listeners.remove(httpRequest);
			return null;
			}
		});
		final boolean valueInUrl = HttpMethods.GET.equalsIgnoreCase(httpRequest.getMethod())
			|| HttpMethods.DELETE.equalsIgnoreCase(httpRequest.getMethod());
		for (final Entry<String, String> header : httpRequest.getHeaders().entrySet()) {
			request.setRequestHeader(header.getKey(), header.getValue());
		}
		request.setTimeout(httpRequest.getTimeOut());
		request.setWithCredentials(httpRequest.getIncludeCredentials());
		if (valueInUrl) {
			request.open(httpRequest.getMethod().toUpperCase(), httpRequest.getUrl() + "?" + httpRequest.getContent());
			request.send();
		} else {
			request.open(httpRequest.getMethod().toUpperCase(), httpRequest.getUrl());
			request.send(httpRequest.getContent());
		}
	}

	@Override
	public void cancelHttpRequest (final HttpRequest httpRequest) {
		final XMLHttpRequest request = requests.remove(httpRequest);
		if (request != null) {
			request.abort();
		}
		final HttpResponseListener listener = listeners.remove(httpRequest);
		if (listener != null) {
			listener.cancelled();
		}
	}

	@Override
	public ServerSocket newServerSocket (final Protocol protocol, final String hostname, final int port,
		final ServerSocketHints hints) {
		throw new UnsupportedOperationException("Dragome application cannot create server sockets.");
	}

	@Override
	public ServerSocket newServerSocket (final Protocol protocol, final int port, final ServerSocketHints hints) {
		throw new UnsupportedOperationException("Dragome application cannot create server sockets.");
	}

	@Override
	public Socket newClientSocket (final Protocol protocol, final String host, final int port, final SocketHints hints) {
		throw new UnsupportedOperationException(
			"Dragome application cannot create TCP client sockets. Look into web socket implementations.");
	}
}
