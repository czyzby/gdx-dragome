/*
 *  Copyright 2015 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.dragome.gdx.graphics.webgl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.w3c.dom.typedarray.Float32Array;
import org.w3c.dom.typedarray.Float64Array;
import org.w3c.dom.typedarray.Int16Array;
import org.w3c.dom.typedarray.Int32Array;
import org.w3c.dom.typedarray.Int8Array;
import org.w3c.dom.typedarray.Uint8Array;
import org.w3c.dom.webgl.WebGLActiveInfo;
import org.w3c.dom.webgl.WebGLBuffer;
import org.w3c.dom.webgl.WebGLFramebuffer;
import org.w3c.dom.webgl.WebGLProgram;
import org.w3c.dom.webgl.WebGLRenderbuffer;
import org.w3c.dom.webgl.WebGLRenderingContext;
import org.w3c.dom.webgl.WebGLShader;
import org.w3c.dom.webgl.WebGLTexture;
import org.w3c.dom.webgl.WebGLUniformLocation;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntMap;
import com.dragome.web.html.dom.w3c.WebGLRenderingContextExtension;

/** Default implementation of {@link GL20} for Dragome applications. Wraps around WebGL.
 * @author MJ */
public class DragomeGL20 implements GL20 {
	// Note: look for GdxRuntimeExceptions for missing features that might have to be fixed eventually.
	private final IntMap<WebGLProgram> programs = new IntMap<WebGLProgram>();
	private int nextProgramId = 1;
	private final IntMap<WebGLShader> shaders = new IntMap<WebGLShader>();
	private int nextShaderId = 1;
	private final IntMap<WebGLBuffer> buffers = new IntMap<WebGLBuffer>();
	private int nextBufferId = 1;
	private final IntMap<WebGLFramebuffer> frameBuffers = new IntMap<WebGLFramebuffer>();
// private final int nextFrameBufferId = 1;
	private final IntMap<WebGLRenderbuffer> renderBuffers = new IntMap<WebGLRenderbuffer>();
	private int nextRenderBufferId = 1;
	private final IntMap<WebGLTexture> textures = new IntMap<WebGLTexture>();
	private int nextTextureId = 1;
	private final IntMap<IntMap<WebGLUniformLocation>> uniforms = new IntMap<IntMap<WebGLUniformLocation>>();
	private int nextUniformId = 1;
	private int currProgram = 0;

	protected final WebGLRenderingContextExtension gl;

	public DragomeGL20 (final WebGLRenderingContext gl) {
		this.gl = (WebGLRenderingContextExtension)gl;
		this.gl.pixelStorei(WebGLRenderingContext.UNPACK_PREMULTIPLY_ALPHA_WEBGL, 0);
	}

	public Float32Array copy (FloatBuffer buffer) {
		buffer = buffer.duplicate();
		final Float32Array result = TypedArrays.createFloat32Array(buffer.remaining());
		float[] tmp;
		if (buffer.hasArray()) {
			tmp = buffer.array();
		} else {
			tmp = new float[buffer.remaining()];
			buffer.get(tmp);
		}
		for (int i = 0; i < tmp.length; ++i) {
			result.set(i, tmp[i]);
		}
		return result;
	}

	public Float64Array copy (DoubleBuffer buffer) {
		buffer = buffer.duplicate();
		final Float64Array result = TypedArrays.createFloat64Array(buffer.remaining());
		double[] tmp;
		if (buffer.hasArray()) {
			tmp = buffer.array();
		} else {
			tmp = new double[buffer.remaining()];
			buffer.get(tmp);
		}
		for (int i = 0; i < tmp.length; ++i) {
			result.set(i, tmp[i]);
		}
		return result;
	}

	public Int16Array copy (ShortBuffer buffer) {
		buffer = buffer.duplicate();
		final Int16Array result = TypedArrays.createInt16Array(buffer.remaining());
		short[] tmp;
		if (buffer.hasArray()) {
			tmp = buffer.array();
		} else {
			tmp = new short[buffer.remaining()];
			buffer.get(tmp);
		}
		for (int i = 0; i < tmp.length; ++i) {
			result.set(i, tmp[i]);
		}
		return result;
	}

	public Int32Array copy (IntBuffer buffer) {
		buffer = buffer.duplicate();
		final Int32Array result = TypedArrays.createInt32Array(buffer.remaining());
		int[] tmp;
		if (buffer.hasArray()) {
			tmp = buffer.array();
		} else {
			tmp = new int[buffer.remaining()];
			buffer.get(tmp);
		}
		for (int i = 0; i < tmp.length; ++i) {
			result.set(i, tmp[i]);
		}
		return result;
	}

	public Int8Array copy (ByteBuffer buffer) {
		buffer = buffer.duplicate();
		final Int8Array result = TypedArrays.createInt8Array(buffer.remaining());
		byte[] tmp;
		if (buffer.hasArray()) {
			tmp = buffer.array();
		} else {
			tmp = new byte[buffer.remaining()];
			buffer.get(tmp);
		}
		for (int i = 0; i < tmp.length; ++i) {
			result.set(i, tmp[i]);
		}
		return result;
	}

	public Uint8Array copyU (ByteBuffer buffer) {
		buffer = buffer.duplicate();
		final Uint8Array result = TypedArrays.createUint8Array(buffer.remaining());
		byte[] tmp;
		if (buffer.hasArray()) {
			tmp = buffer.array();
		} else {
			tmp = new byte[buffer.remaining()];
			buffer.get(tmp);
		}
		for (int i = 0; i < tmp.length; ++i) {
			result.set(i, tmp[i]);
		}
		return result;
	}

	private int allocateUniformLocationId (final int program, final WebGLUniformLocation location) {
		IntMap<WebGLUniformLocation> progUniforms = uniforms.get(program);
		if (progUniforms == null) {
			progUniforms = new IntMap<WebGLUniformLocation>();
			uniforms.put(program, progUniforms);
		}
		// FIXME Check if uniform already stored.
		final int id = nextUniformId++;
		progUniforms.put(id, location);
		return id;
	}

	private WebGLUniformLocation getUniformLocation (final int location) {
		return uniforms.get(currProgram).get(location);
	}

	private int allocateShaderId (final WebGLShader shader) {
		final int id = nextShaderId++;
		shaders.put(id, shader);
		return id;
	}

	private void deallocateShaderId (final int id) {
		shaders.remove(id);
	}

	private int allocateProgramId (final WebGLProgram program) {
		final int id = nextProgramId++;
		programs.put(id, program);
		return id;
	}

	private void deallocateProgramId (final int id) {
		uniforms.remove(id);
		programs.remove(id);
	}

	private int allocateBufferId (final WebGLBuffer buffer) {
		final int id = nextBufferId++;
		buffers.put(id, buffer);
		return id;
	}

	private void deallocateBufferId (final int id) {
		buffers.remove(id);
	}

	private int allocateFrameBufferId (final WebGLFramebuffer frameBuffer) {
		final int id = nextBufferId++;
		frameBuffers.put(id, frameBuffer);
		return id;
	}

	private void deallocateFrameBufferId (final int id) {
		frameBuffers.remove(id);
	}

	private int allocateRenderBufferId (final WebGLRenderbuffer renderBuffer) {
		final int id = nextRenderBufferId++;
		renderBuffers.put(id, renderBuffer);
		return id;
	}

	private void deallocateRenderBufferId (final int id) {
		renderBuffers.remove(id);
	}

	private int allocateTextureId (final WebGLTexture texture) {
		final int id = nextTextureId++;
		textures.put(id, texture);
		return id;
	}

	private void deallocateTextureId (final int id) {
		textures.remove(id);
	}

	@Override
	public void glActiveTexture (final int texture) {
		gl.activeTexture(texture);
	}

	@Override
	public void glBindTexture (final int target, final int texture) {
		gl.bindTexture(target, textures.get(texture));
	}

	@Override
	public void glBlendFunc (final int sfactor, final int dfactor) {
		gl.blendFunc(sfactor, dfactor);
	}

	@Override
	public void glClear (final int mask) {
		gl.clear(mask);
	}

	@Override
	public void glClearColor (final float red, final float green, final float blue, final float alpha) {
		gl.clearColor(red, green, blue, alpha);
	}

	@Override
	public void glClearDepthf (final float depth) {
		gl.clearDepth(depth);
	}

	@Override
	public void glClearStencil (final int s) {
		gl.clearStencil(s);
	}

	@Override
	public void glColorMask (final boolean red, final boolean green, final boolean blue, final boolean alpha) {
		gl.colorMask(red, green, blue, alpha);
	}

	@Override
	public void glCompressedTexImage2D (final int target, final int level, final int internalformat, final int width,
		final int height, final int border, final int imageSize, final Buffer data) {
		throw new GdxRuntimeException("compressed textures not supported by WebGL backend.");
	}

	@Override
	public void glCompressedTexSubImage2D (final int target, final int level, final int xoffset, final int yoffset,
		final int width, final int height, final int format, final int imageSize, final Buffer data) {
		throw new GdxRuntimeException("compressed textures not supported by WebGL backend.");
	}

	@Override
	public void glCopyTexImage2D (final int target, final int level, final int internalformat, final int x, final int y,
		final int width, final int height, final int border) {
		gl.copyTexImage2D(target, level, internalformat, x, y, width, height, border);
	}

	@Override
	public void glCopyTexSubImage2D (final int target, final int level, final int xoffset, final int yoffset, final int x,
		final int y, final int width, final int height) {
		gl.copyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
	}

	@Override
	public void glCullFace (final int mode) {
		gl.cullFace(mode);
	}

	@Override
	public void glDeleteTextures (final int n, final IntBuffer textures) {
		for (int i = 0; i < n; i++) {
			final int id = textures.get();
			final WebGLTexture texture = this.textures.get(id);
			deallocateTextureId(id);
			gl.deleteTexture(texture);
		}
	}

	@Override
	public void glDeleteTexture (final int id) {
		final WebGLTexture texture = textures.get(id);
		deallocateTextureId(id);
		gl.deleteTexture(texture);
	}

	@Override
	public void glDepthFunc (final int func) {
		gl.depthFunc(func);
	}

	@Override
	public void glDepthMask (final boolean flag) {
		gl.depthMask(flag);
	}

	@Override
	public void glDepthRangef (final float zNear, final float zFar) {
		gl.depthRange(zNear, zFar);
	}

	@Override
	public void glDisable (final int cap) {
		gl.disable(cap);
	}

	@Override
	public void glDrawArrays (final int mode, final int first, final int count) {
		gl.drawArrays(mode, first, count);
	}

	@Override
	public void glDrawElements (final int mode, final int count, final int type, final Buffer indices) {
		gl.drawElements(mode, count, type, indices.position());
		// FIXME This is assuming WebGL supports client side buffers.
	}

	@Override
	public void glEnable (final int cap) {
		gl.enable(cap);
	}

	@Override
	public void glFinish () {
		gl.finish();
	}

	@Override
	public void glFlush () {
		gl.flush();
	}

	@Override
	public void glFrontFace (final int mode) {
		gl.frontFace(mode);
	}

	@Override
	public void glGenTextures (final int n, final IntBuffer textures) {
		final WebGLTexture texture = gl.createTexture();
		final int id = allocateTextureId(texture);
		textures.put(id);
	}

	@Override
	public int glGenTexture () {
		final WebGLTexture texture = gl.createTexture();
		return allocateTextureId(texture);
	}

	@Override
	public int glGetError () {
		return gl.getError();
	}

	@Override
	public void glGetIntegerv (final int pname, final IntBuffer params) {
		if (pname == GL20.GL_ACTIVE_TEXTURE || pname == GL20.GL_ALPHA_BITS || pname == GL20.GL_BLEND_DST_ALPHA
			|| pname == GL20.GL_BLEND_DST_RGB || pname == GL20.GL_BLEND_EQUATION_ALPHA || pname == GL20.GL_BLEND_EQUATION_RGB
			|| pname == GL20.GL_BLEND_SRC_ALPHA || pname == GL20.GL_BLEND_SRC_RGB || pname == GL20.GL_BLUE_BITS
			|| pname == GL20.GL_CULL_FACE_MODE || pname == GL20.GL_DEPTH_BITS || pname == GL20.GL_DEPTH_FUNC
			|| pname == GL20.GL_FRONT_FACE || pname == GL20.GL_GENERATE_MIPMAP_HINT || pname == GL20.GL_GREEN_BITS
			|| pname == GL20.GL_IMPLEMENTATION_COLOR_READ_FORMAT || pname == GL20.GL_IMPLEMENTATION_COLOR_READ_TYPE
			|| pname == GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS || pname == GL20.GL_MAX_CUBE_MAP_TEXTURE_SIZE
			|| pname == GL20.GL_MAX_FRAGMENT_UNIFORM_VECTORS || pname == GL20.GL_MAX_RENDERBUFFER_SIZE
			|| pname == GL20.GL_MAX_TEXTURE_IMAGE_UNITS || pname == GL20.GL_MAX_TEXTURE_SIZE || pname == GL20.GL_MAX_VARYING_VECTORS
			|| pname == GL20.GL_MAX_VERTEX_ATTRIBS || pname == GL20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS
			|| pname == GL20.GL_MAX_VERTEX_UNIFORM_VECTORS || pname == GL20.GL_NUM_COMPRESSED_TEXTURE_FORMATS
			|| pname == GL20.GL_PACK_ALIGNMENT || pname == GL20.GL_RED_BITS || pname == GL20.GL_SAMPLE_BUFFERS
			|| pname == GL20.GL_SAMPLES || pname == GL20.GL_STENCIL_BACK_FAIL || pname == GL20.GL_STENCIL_BACK_FUNC
			|| pname == GL20.GL_STENCIL_BACK_PASS_DEPTH_FAIL || pname == GL20.GL_STENCIL_BACK_PASS_DEPTH_PASS
			|| pname == GL20.GL_STENCIL_BACK_REF || pname == GL20.GL_STENCIL_BACK_VALUE_MASK
			|| pname == GL20.GL_STENCIL_BACK_WRITEMASK || pname == GL20.GL_STENCIL_BITS || pname == GL20.GL_STENCIL_CLEAR_VALUE
			|| pname == GL20.GL_STENCIL_FAIL || pname == GL20.GL_STENCIL_FUNC || pname == GL20.GL_STENCIL_PASS_DEPTH_FAIL
			|| pname == GL20.GL_STENCIL_PASS_DEPTH_PASS || pname == GL20.GL_STENCIL_REF || pname == GL20.GL_STENCIL_VALUE_MASK
			|| pname == GL20.GL_STENCIL_WRITEMASK || pname == GL20.GL_SUBPIXEL_BITS || pname == GL20.GL_UNPACK_ALIGNMENT) {
			params.put(0, (Integer)gl.getParameter(pname));
		} else {
			throw new GdxRuntimeException("glGetFloat not supported by WebGL backend.");
		}
	}

	@Override
	public String glGetString (final int name) {
		return (String)gl.getParameter(name);
	}

	@Override
	public void glHint (final int target, final int mode) {
		gl.hint(target, mode);
	}

	@Override
	public void glLineWidth (final float width) {
		gl.lineWidth(width);
	}

	@Override
	public void glPixelStorei (final int pname, final int param) {
		gl.pixelStorei(pname, param);
	}

	@Override
	public void glPolygonOffset (final float factor, final float units) {
		gl.polygonOffset(factor, units);
	}

	@Override
	public void glReadPixels (final int x, final int y, final int width, final int height, final int format, final int type,
		final Buffer pixels) {
		// verify request
		if (format != WebGLRenderingContext.RGBA || type != WebGLRenderingContext.UNSIGNED_BYTE) {
			throw new GdxRuntimeException("Only format RGBA and type UNSIGNED_BYTE are currently supported for glReadPixels(...).");
		}
		if (!(pixels instanceof ByteBuffer)) {
			throw new GdxRuntimeException("Inputed pixels buffer needs to be of type ByteBuffer for glReadPixels(...).");
		}

		// create new ArrayBufferView (4 bytes per pixel)
		final int size = 4 * width * height;
		final Uint8Array buffer = TypedArrays.createUint8Array(size);

		// read bytes to ArrayBufferView
		gl.readPixels(x, y, width, height, format, type, buffer);

		// copy ArrayBufferView to our pixels array
		final ByteBuffer pixelsByte = (ByteBuffer)pixels;
		for (int i = 0; i < size; i++) {
			pixelsByte.put(buffer.get(i));
		}
	}

	@Override
	public void glScissor (final int x, final int y, final int width, final int height) {
		gl.scissor(x, y, width, height);
	}

	@Override
	public void glStencilFunc (final int func, final int ref, final int mask) {
		gl.stencilFunc(func, ref, mask);
	}

	@Override
	public void glStencilMask (final int mask) {
		gl.stencilMask(mask);
	}

	@Override
	public void glStencilOp (final int fail, final int zfail, final int zpass) {
		gl.stencilOp(fail, zfail, zpass);
	}

	@Override
	public void glTexImage2D (final int target, final int level, final int internalformat, final int width, final int height,
		final int border, final int format, final int type, final Buffer pixels) {
		if (pixels instanceof ByteBuffer) {
			gl.texImage2D(target, level, internalformat, width, height, border, format, type, copyU((ByteBuffer)pixels));
		} else if (pixels instanceof ShortBuffer) {
			gl.texImage2D(target, level, internalformat, width, height, border, format, type, copy((ShortBuffer)pixels));
		} else if (pixels instanceof IntBuffer) {
			gl.texImage2D(target, level, internalformat, width, height, border, format, type, copy((IntBuffer)pixels));
		} else if (pixels instanceof FloatBuffer) {
			gl.texImage2D(target, level, internalformat, width, height, border, format, type, copy((FloatBuffer)pixels));
		} else if (pixels instanceof DoubleBuffer) {
			gl.texImage2D(target, level, internalformat, width, height, border, format, type, copy((DoubleBuffer)pixels));
		} else {
			throw new GdxRuntimeException("Can't copy pixels to texture");
		}
	}

	@Override
	public void glTexParameterf (final int target, final int pname, final float param) {
		gl.texParameterf(target, pname, param);
	}

	@Override
	public void glTexSubImage2D (final int target, final int level, final int xoffset, final int yoffset, final int width,
		final int height, final int format, final int type, final Buffer pixels) {
		if (pixels instanceof ByteBuffer) {
			gl.texSubImage2D(target, level, xoffset, yoffset, width, height, format, type, copy((ByteBuffer)pixels));
		} else if (pixels instanceof ShortBuffer) {
			gl.texImage2D(target, level, xoffset, yoffset, width, height, format, type, copy((ShortBuffer)pixels));
		} else if (pixels instanceof IntBuffer) {
			gl.texImage2D(target, level, xoffset, yoffset, width, height, format, type, copy((IntBuffer)pixels));
		} else if (pixels instanceof FloatBuffer) {
			gl.texImage2D(target, level, xoffset, yoffset, width, height, format, type, copy((FloatBuffer)pixels));
		} else if (pixels instanceof DoubleBuffer) {
			gl.texImage2D(target, level, xoffset, yoffset, width, height, format, type, copy((DoubleBuffer)pixels));
		} else {
			throw new GdxRuntimeException("Can't copy pixels to texture");
		}
	}

	@Override
	public void glViewport (final int x, final int y, final int width, final int height) {
		gl.viewport(x, y, width, height);
	}

	@Override
	public void glAttachShader (final int program, final int shader) {
		final WebGLProgram glProgram = programs.get(program);
		final WebGLShader glShader = shaders.get(shader);
		gl.attachShader(glProgram, glShader);
	}

	@Override
	public void glBindAttribLocation (final int program, final int index, final String name) {
		final WebGLProgram glProgram = programs.get(program);
		gl.bindAttribLocation(glProgram, index, name);
	}

	@Override
	public void glBindBuffer (final int target, final int buffer) {
		gl.bindBuffer(target, buffers.get(buffer));
	}

	@Override
	public void glBindFramebuffer (final int target, final int framebuffer) {
		gl.bindFramebuffer(target, frameBuffers.get(framebuffer));
	}

	@Override
	public void glBindRenderbuffer (final int target, final int renderbuffer) {
		gl.bindRenderbuffer(target, renderBuffers.get(renderbuffer));
	}

	@Override
	public void glBlendColor (final float red, final float green, final float blue, final float alpha) {
		gl.blendColor(red, green, blue, alpha);
	}

	@Override
	public void glBlendEquation (final int mode) {
		gl.blendEquation(mode);
	}

	@Override
	public void glBlendEquationSeparate (final int modeRGB, final int modeAlpha) {
		gl.blendEquationSeparate(modeRGB, modeAlpha);
	}

	@Override
	public void glBlendFuncSeparate (final int srcRGB, final int dstRGB, final int srcAlpha, final int dstAlpha) {
		gl.blendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
	}

	@Override
	public void glBufferData (final int target, final int size, final Buffer data, final int usage) {
		if (data instanceof FloatBuffer) {
			gl.bufferData(target, copy((FloatBuffer)data), usage);
		} else if (data instanceof ShortBuffer) {
			gl.bufferData(target, copy((ShortBuffer)data), usage);
		} else if (data instanceof IntBuffer) {
			gl.bufferData(target, copy((IntBuffer)data), usage);
		} else if (data instanceof ByteBuffer) {
			gl.bufferData(target, copy((ByteBuffer)data), usage);
		} else {
			throw new GdxRuntimeException("Can only cope with FloatBuffer and ShortBuffer at the moment");
		}
	}

	@Override
	public void glBufferSubData (final int target, final int offset, final int size, final Buffer data) {
		if (data instanceof FloatBuffer) {
			gl.bufferSubData(target, offset, copy((FloatBuffer)data));
		} else if (data instanceof ShortBuffer) {
			gl.bufferSubData(target, offset, copy((ShortBuffer)data));
		} else if (data instanceof IntBuffer) {
			gl.bufferSubData(target, offset, copy((IntBuffer)data));
		} else if (data instanceof ByteBuffer) {
			gl.bufferSubData(target, offset, copy((ByteBuffer)data));
		} else {
			throw new GdxRuntimeException("Can only cope with FloatBuffer and ShortBuffer at the moment");
		}
	}

	@Override
	public int glCheckFramebufferStatus (final int target) {
		return gl.checkFramebufferStatus(target);
	}

	@Override
	public void glCompileShader (final int shader) {
		final WebGLShader glShader = shaders.get(shader);
		gl.compileShader(glShader);
	}

	@Override
	public int glCreateProgram () {
		final WebGLProgram program = gl.createProgram();
		return allocateProgramId(program);
	}

	@Override
	public int glCreateShader (final int type) {
		final WebGLShader shader = gl.createShader(type);
		return allocateShaderId(shader);
	}

	@Override
	public void glDeleteBuffers (final int n, final IntBuffer buffers) {
		for (int i = 0; i < n; i++) {
			final int id = buffers.get();
			final WebGLBuffer buffer = this.buffers.get(id);
			deallocateBufferId(id);
			gl.deleteBuffer(buffer);
		}
	}

	@Override
	public void glDeleteBuffer (final int id) {
		final WebGLBuffer buffer = buffers.get(id);
		deallocateBufferId(id);
		gl.deleteBuffer(buffer);
	}

	@Override
	public void glDeleteFramebuffers (final int n, final IntBuffer framebuffers) {
		for (int i = 0; i < n; i++) {
			final int id = framebuffers.get();
			final WebGLFramebuffer fb = frameBuffers.get(id);
			deallocateFrameBufferId(id);
			gl.deleteFramebuffer(fb);
		}
	}

	@Override
	public void glDeleteFramebuffer (final int id) {
		final WebGLFramebuffer fb = frameBuffers.get(id);
		deallocateFrameBufferId(id);
		gl.deleteFramebuffer(fb);
	}

	@Override
	public void glDeleteProgram (final int program) {
		final WebGLProgram prog = programs.get(program);
		deallocateProgramId(program);
		gl.deleteProgram(prog);
	}

	@Override
	public void glDeleteRenderbuffers (final int n, final IntBuffer renderbuffers) {
		for (int i = 0; i < n; i++) {
			final int id = renderbuffers.get();
			final WebGLRenderbuffer rb = renderBuffers.get(id);
			deallocateRenderBufferId(id);
			gl.deleteRenderbuffer(rb);
		}
	}

	@Override
	public void glDeleteRenderbuffer (final int id) {
		final WebGLRenderbuffer rb = renderBuffers.get(id);
		deallocateRenderBufferId(id);
		gl.deleteRenderbuffer(rb);
	}

	@Override
	public void glDeleteShader (final int shader) {
		final WebGLShader sh = shaders.get(shader);
		deallocateShaderId(shader);
		gl.deleteShader(sh);
	}

	@Override
	public void glDetachShader (final int program, final int shader) {
		gl.detachShader(programs.get(program), shaders.get(shader));
	}

	@Override
	public void glDisableVertexAttribArray (final int index) {
		gl.disableVertexAttribArray(index);
	}

	@Override
	public void glDrawElements (final int mode, final int count, final int type, final int indices) {
		gl.drawElements(mode, count, type, indices);
	}

	@Override
	public void glEnableVertexAttribArray (final int index) {
		gl.enableVertexAttribArray(index);
	}

	@Override
	public void glFramebufferRenderbuffer (final int target, final int attachment, final int renderbuffertarget,
		final int renderbuffer) {
		gl.framebufferRenderbuffer(target, attachment, renderbuffertarget, renderBuffers.get(renderbuffer));
	}

	@Override
	public void glFramebufferTexture2D (final int target, final int attachment, final int textarget, final int texture,
		final int level) {
		gl.framebufferTexture2D(target, attachment, textarget, textures.get(texture), level);
	}

	@Override
	public void glGenBuffers (final int n, final IntBuffer buffers) {
		for (int i = 0; i < n; i++) {
			final WebGLBuffer buffer = gl.createBuffer();
			final int id = allocateBufferId(buffer);
			buffers.put(id);
		}
	}

	@Override
	public int glGenBuffer () {
		final WebGLBuffer buffer = gl.createBuffer();
		return allocateBufferId(buffer);
	}

	@Override
	public void glGenerateMipmap (final int target) {
		gl.generateMipmap(target);
	}

	@Override
	public void glGenFramebuffers (final int n, final IntBuffer framebuffers) {
		for (int i = 0; i < n; i++) {
			final WebGLFramebuffer fb = gl.createFramebuffer();
			final int id = allocateFrameBufferId(fb);
			framebuffers.put(id);
		}
	}

	@Override
	public int glGenFramebuffer () {
		final WebGLFramebuffer fb = gl.createFramebuffer();
		return allocateFrameBufferId(fb);
	}

	@Override
	public void glGenRenderbuffers (final int n, final IntBuffer renderbuffers) {
		for (int i = 0; i < n; i++) {
			final WebGLRenderbuffer rb = gl.createRenderbuffer();
			final int id = allocateRenderBufferId(rb);
			renderbuffers.put(id);
		}
	}

	@Override
	public int glGenRenderbuffer () {
		final WebGLRenderbuffer rb = gl.createRenderbuffer();
		return allocateRenderBufferId(rb);
	}

	@Override
	public String glGetActiveAttrib (final int program, final int index, final IntBuffer size, final Buffer type) {
		final WebGLActiveInfo activeAttrib = gl.getActiveAttrib(programs.get(program), index);
		size.put(activeAttrib.getSize());
		((IntBuffer)type).put(activeAttrib.getType());
		return activeAttrib.getName();
	}

	@Override
	public String glGetActiveUniform (final int program, final int index, final IntBuffer size, final Buffer type) {
		final WebGLActiveInfo activeUniform = gl.getActiveUniform(programs.get(program), index);
		size.put(activeUniform.getSize());
		((IntBuffer)type).put(activeUniform.getType());
		return activeUniform.getName();
	}

	@Override
	public void glGetAttachedShaders (final int program, final int maxcount, final Buffer count, final IntBuffer shaders) {
		throw new GdxRuntimeException("glGetAttachedShaders not supported by WebGL backend.");
	}

	@Override
	public int glGetAttribLocation (final int program, final String name) {
		final WebGLProgram prog = programs.get(program);
		return gl.getAttribLocation(prog, name);
	}

	@Override
	public void glGetBooleanv (final int pname, final Buffer params) {
		throw new GdxRuntimeException("glGetBoolean not supported by WebGL backend.");
	}

	@Override
	public void glGetBufferParameteriv (final int target, final int pname, final IntBuffer params) {
		throw new GdxRuntimeException("glGetBufferParameteriv not supported by WebGL backend.");
	}

	@Override
	public void glGetFloatv (final int pname, final FloatBuffer params) {
		if (pname == GL20.GL_DEPTH_CLEAR_VALUE || pname == GL20.GL_LINE_WIDTH || pname == GL20.GL_POLYGON_OFFSET_FACTOR
			|| pname == GL20.GL_POLYGON_OFFSET_UNITS || pname == GL20.GL_SAMPLE_COVERAGE_VALUE) {
			params.put(0, (Float)gl.getParameter(pname));
		} else {
			throw new GdxRuntimeException("glGetFloat not supported by WebGL backend.");
		}
	}

	@Override
	public void glGetFramebufferAttachmentParameteriv (final int target, final int attachment, final int pname,
		final IntBuffer params) {
		throw new GdxRuntimeException("glGetFramebufferAttachmentParameteriv not supported by WebGL backend.");
	}

	@Override
	public void glGetProgramiv (final int program, final int pname, final IntBuffer params) {
		if (pname == GL20.GL_DELETE_STATUS || pname == GL20.GL_LINK_STATUS || pname == GL20.GL_VALIDATE_STATUS) {
			final boolean result = (Boolean)gl.getProgramParameter(programs.get(program), pname);
			params.put(result ? GL20.GL_TRUE : GL20.GL_FALSE);
		} else {
			params.put((Integer)gl.getProgramParameter(programs.get(program), pname));
		}
	}

	@Override
	public String glGetProgramInfoLog (final int program) {
		return gl.getProgramInfoLog(programs.get(program));
	}

	@Override
	public void glGetRenderbufferParameteriv (final int target, final int pname, final IntBuffer params) {
		throw new GdxRuntimeException("glGetRenderbufferParameteriv not supported by WebGL backend.");
	}

	@Override
	public void glGetShaderiv (final int shader, final int pname, final IntBuffer params) {
		if (pname == GL20.GL_COMPILE_STATUS || pname == GL20.GL_DELETE_STATUS) {
			final boolean result = (Boolean)gl.getShaderParameter(shaders.get(shader), pname);
			params.put(result ? GL20.GL_TRUE : GL20.GL_FALSE);
		} else {
			final int result = (Integer)gl.getShaderParameter(shaders.get(shader), pname);
			params.put(result);
		}
	}

	@Override
	public String glGetShaderInfoLog (final int shader) {
		return gl.getShaderInfoLog(shaders.get(shader));
	}

	@Override
	public void glGetShaderPrecisionFormat (final int shadertype, final int precisiontype, final IntBuffer range,
		final IntBuffer precision) {
		throw new GdxRuntimeException("glGetShaderPrecisionFormat not supported by WebGL backend.");
	}

	@Override
	public void glGetTexParameterfv (final int target, final int pname, final FloatBuffer params) {
		throw new GdxRuntimeException("glGetTexParameter not supported by WebGL backend.");
	}

	@Override
	public void glGetTexParameteriv (final int target, final int pname, final IntBuffer params) {
		throw new GdxRuntimeException("glGetTexParameter not supported by WebGL backend.");
	}

	@Override
	public void glGetUniformfv (final int program, final int location, final FloatBuffer params) {
		throw new GdxRuntimeException("glGetUniformfv not supported by WebGL backend.");
	}

	@Override
	public void glGetUniformiv (final int program, final int location, final IntBuffer params) {
		throw new GdxRuntimeException("glGetUniformiv not supported by WebGL backend.");
	}

	@Override
	public int glGetUniformLocation (final int program, final String name) {
		final WebGLUniformLocation location = gl.getUniformLocation(programs.get(program), name);
		return allocateUniformLocationId(program, location);
	}

	@Override
	public void glGetVertexAttribfv (final int index, final int pname, final FloatBuffer params) {
		throw new GdxRuntimeException("glGetVertexAttribfv not supported by WebGL backend.");
	}

	@Override
	public void glGetVertexAttribiv (final int index, final int pname, final IntBuffer params) {
		throw new GdxRuntimeException("glGetVertexAttribiv not supported by WebGL backend.");
	}

	@Override
	public void glGetVertexAttribPointerv (final int index, final int pname, final Buffer pointer) {
		throw new GdxRuntimeException("glGetVertexAttribPointer not supported by WebGL backend.");
	}

	@Override
	public boolean glIsBuffer (final int buffer) {
		return gl.isBuffer(buffers.get(buffer));
	}

	@Override
	public boolean glIsEnabled (final int cap) {
		return gl.isEnabled(cap);
	}

	@Override
	public boolean glIsFramebuffer (final int framebuffer) {
		return gl.isFramebuffer(frameBuffers.get(framebuffer));
	}

	@Override
	public boolean glIsProgram (final int program) {
		return gl.isProgram(programs.get(program));
	}

	@Override
	public boolean glIsRenderbuffer (final int renderbuffer) {
		return gl.isRenderbuffer(renderBuffers.get(renderbuffer));
	}

	@Override
	public boolean glIsShader (final int shader) {
		return gl.isShader(shaders.get(shader));
	}

	@Override
	public boolean glIsTexture (final int texture) {
		return gl.isTexture(textures.get(texture));
	}

	@Override
	public void glLinkProgram (final int program) {
		gl.linkProgram(programs.get(program));
	}

	@Override
	public void glReleaseShaderCompiler () {
		throw new GdxRuntimeException("not implemented");
	}

	@Override
	public void glRenderbufferStorage (final int target, final int internalformat, final int width, final int height) {
		gl.renderbufferStorage(target, internalformat, width, height);
	}

	@Override
	public void glSampleCoverage (final float value, final boolean invert) {
		gl.sampleCoverage(value, invert);
	}

	@Override
	public void glShaderBinary (final int n, final IntBuffer shaders, final int binaryformat, final Buffer binary,
		final int length) {
		throw new GdxRuntimeException("glShaderBinary not supported by WebGL backend.");
	}

	@Override
	public void glShaderSource (final int shader, final String source) {
		gl.shaderSource(shaders.get(shader), source);
	}

	@Override
	public void glStencilFuncSeparate (final int face, final int func, final int ref, final int mask) {
		gl.stencilFuncSeparate(face, func, ref, mask);
	}

	@Override
	public void glStencilMaskSeparate (final int face, final int mask) {
		gl.stencilMaskSeparate(face, mask);
	}

	@Override
	public void glStencilOpSeparate (final int face, final int fail, final int zfail, final int zpass) {
		gl.stencilOpSeparate(face, fail, zfail, zpass);
	}

	@Override
	public void glTexParameterfv (final int target, final int pname, final FloatBuffer params) {
		gl.texParameterf(target, pname, params.get());
	}

	@Override
	public void glTexParameteri (final int target, final int pname, final int param) {
		gl.texParameterf(target, pname, param);
	}

	@Override
	public void glTexParameteriv (final int target, final int pname, final IntBuffer params) {
		gl.texParameterf(target, pname, params.get());
	}

	@Override
	public void glUniform1f (final int location, final float x) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform1f(loc, x);
	}

	@Override
	public void glUniform1fv (final int location, final int count, final FloatBuffer v) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform1fv(loc, copy(v));
	}

	@Override
	public void glUniform1fv (final int location, final int count, final float[] v, final int offset) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform1fv(loc, v);
	}

	@Override
	public void glUniform1i (final int location, final int x) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform1i(loc, x);
	}

	@Override
	public void glUniform1iv (final int location, final int count, final IntBuffer v) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform1iv(loc, copy(v));
	}

	@Override
	public void glUniform1iv (final int location, final int count, final int[] v, final int offset) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform1iv(loc, v);
	}

	@Override
	public void glUniform2f (final int location, final float x, final float y) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform2f(loc, x, y);
	}

	@Override
	public void glUniform2fv (final int location, final int count, final FloatBuffer v) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform2fv(loc, copy(v));
	}

	@Override
	public void glUniform2fv (final int location, final int count, final float[] v, final int offset) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform2fv(loc, v);
	}

	@Override
	public void glUniform2i (final int location, final int x, final int y) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform2i(loc, x, y);
	}

	@Override
	public void glUniform2iv (final int location, final int count, final IntBuffer v) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform2iv(loc, copy(v));
	}

	@Override
	public void glUniform2iv (final int location, final int count, final int[] v, final int offset) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform2iv(loc, v);
	}

	@Override
	public void glUniform3f (final int location, final float x, final float y, final float z) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform3f(loc, x, y, z);
	}

	@Override
	public void glUniform3fv (final int location, final int count, final FloatBuffer v) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform3fv(loc, copy(v));
	}

	@Override
	public void glUniform3fv (final int location, final int count, final float[] v, final int offset) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform3fv(loc, v);
	}

	@Override
	public void glUniform3i (final int location, final int x, final int y, final int z) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform3i(loc, x, y, z);
	}

	@Override
	public void glUniform3iv (final int location, final int count, final IntBuffer v) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform3iv(loc, copy(v));
	}

	@Override
	public void glUniform3iv (final int location, final int count, final int[] v, final int offset) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform3iv(loc, v);
	}

	@Override
	public void glUniform4f (final int location, final float x, final float y, final float z, final float w) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform4f(loc, x, y, z, w);
	}

	@Override
	public void glUniform4fv (final int location, final int count, final FloatBuffer v) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform4fv(loc, copy(v));
	}

	@Override
	public void glUniform4fv (final int location, final int count, final float[] v, final int offset) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform4fv(loc, v);
	}

	@Override
	public void glUniform4i (final int location, final int x, final int y, final int z, final int w) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform4i(loc, x, y, z, w);
	}

	@Override
	public void glUniform4iv (final int location, final int count, final IntBuffer v) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform4iv(loc, copy(v));
	}

	@Override
	public void glUniform4iv (final int location, final int count, final int[] v, final int offset) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniform4iv(loc, v);
	}

	@Override
	public void glUniformMatrix2fv (final int location, final int count, final boolean transpose, final FloatBuffer value) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniformMatrix2fv(loc, transpose, copy(value));
	}

	@Override
	public void glUniformMatrix2fv (final int location, final int count, final boolean transpose, final float[] value,
		final int offset) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniformMatrix2fv(loc, transpose, value);
	}

	@Override
	public void glUniformMatrix3fv (final int location, final int count, final boolean transpose, final FloatBuffer value) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniformMatrix3fv(loc, transpose, copy(value));
	}

	@Override
	public void glUniformMatrix3fv (final int location, final int count, final boolean transpose, final float[] value,
		final int offset) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniformMatrix3fv(loc, transpose, value);
	}

	@Override
	public void glUniformMatrix4fv (final int location, final int count, final boolean transpose, final FloatBuffer value) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniformMatrix4fv(loc, transpose, copy(value));
	}

	@Override
	public void glUniformMatrix4fv (final int location, final int count, final boolean transpose, final float[] value,
		final int offset) {
		final WebGLUniformLocation loc = getUniformLocation(location);
		gl.uniformMatrix4fv(loc, transpose, value);
	}

	@Override
	public void glUseProgram (final int program) {
		currProgram = program;
		gl.useProgram(programs.get(program));
	}

	@Override
	public void glValidateProgram (final int program) {
		gl.validateProgram(programs.get(program));
	}

	@Override
	public void glVertexAttrib1f (final int indx, final float x) {
		gl.vertexAttrib1f(indx, x);
	}

	@Override
	public void glVertexAttrib1fv (final int indx, final FloatBuffer values) {
		gl.vertexAttrib1fv(indx, copy(values));
	}

	@Override
	public void glVertexAttrib2f (final int indx, final float x, final float y) {
		gl.vertexAttrib2f(indx, x, y);
	}

	@Override
	public void glVertexAttrib2fv (final int indx, final FloatBuffer values) {
		gl.vertexAttrib2fv(indx, copy(values));
	}

	@Override
	public void glVertexAttrib3f (final int indx, final float x, final float y, final float z) {
		gl.vertexAttrib3f(indx, x, y, z);
	}

	@Override
	public void glVertexAttrib3fv (final int indx, final FloatBuffer values) {
		gl.vertexAttrib3fv(indx, copy(values));
	}

	@Override
	public void glVertexAttrib4f (final int indx, final float x, final float y, final float z, final float w) {
		gl.vertexAttrib4f(indx, x, y, z, w);
	}

	@Override
	public void glVertexAttrib4fv (final int indx, final FloatBuffer values) {
		gl.vertexAttrib4fv(indx, copy(values));
	}

	@Override
	public void glVertexAttribPointer (final int indx, final int size, final int type, final boolean normalized, final int stride,
		final Buffer ptr) {
		throw new GdxRuntimeException("not implemented, vertex arrays aren't support in WebGL it seems");
	}

	@Override
	public void glVertexAttribPointer (final int indx, final int size, final int type, final boolean normalized, final int stride,
		final int ptr) {
		gl.vertexAttribPointer(indx, size, type, normalized, stride, ptr);
	}
}
