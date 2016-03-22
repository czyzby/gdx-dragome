
package com.dragome.gdx.net;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.XMLHttpRequest;

import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.net.HttpStatus;

/** Implements {@link HttpResponse} wrapping around the actual response from {@link XMLHttpRequest}.
 * @author MJ */
public class DragomeHttpResponse implements HttpResponse {
	private final XMLHttpRequest request;
	private final HttpStatus status;

	public DragomeHttpResponse (final XMLHttpRequest request) {
		this.request = request;
		status = new HttpStatus(request.getStatus());
	}

	@Override
	public byte[] getResult () {
		return getResultAsString().getBytes();
	}

	@Override
	public String getResultAsString () {
		return request.getResponseText();
	}

	@Override
	public InputStream getResultAsStream () {
		return new ByteArrayInputStream(getResult());
	}

	@Override
	public HttpStatus getStatus () {
		return status;
	}

	@Override
	public String getHeader (final String name) {
		return request.getResponseHeader(name);
	}

	@Override
	public Map<String, List<String>> getHeaders () {
		final Map<String, List<String>> headers = new HashMap<String, List<String>>();
		final String responseHeaders = request.getAllResponseHeaders();
		if (responseHeaders == null || responseHeaders.length() == 0) {
			return headers;
		}
		// http://www.w3.org/TR/XMLHttpRequest/#the-getallresponseheaders%28%29-method
		final String[] headerPairs = responseHeaders.split("\r?\n");
		for (final String header : headerPairs) {
			final int index = header.indexOf(": ");
			if (index > 0) {
			final String key = header.substring(0, index);
			final String value = header.substring(index + 2);
			getHeadersList(headers, key).add(value);
			}
		}
		return headers;
	}

	private static List<String> getHeadersList (final Map<String, List<String>> headers, final String key) {
		if (!headers.containsKey(key)) {
			final List<String> list = new ArrayList<String>();
			headers.put(key, list);
			return list;
		}
		return headers.get(key);
	}
}
