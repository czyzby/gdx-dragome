
package com.dragome.gdx.graphics.webgl;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.badlogic.gdx.graphics.GL20;

/** Mocks {@link GL20} object. Every method call does nothing. Somewhat useful for debugging.
 * @author MJ */ // TODO This class might be removed after introducing proper GL20 implementation.
public class MockUpGL20 implements GL20 {
	@Override
	public void glActiveTexture (final int texture) {
	}

	@Override
	public void glBindTexture (final int target, final int texture) {
	}

	@Override
	public void glBlendFunc (final int sfactor, final int dfactor) {
	}

	@Override
	public void glClear (final int mask) {
	}

	@Override
	public void glClearColor (final float red, final float green, final float blue, final float alpha) {
	}

	@Override
	public void glClearDepthf (final float depth) {
	}

	@Override
	public void glClearStencil (final int s) {
	}

	@Override
	public void glColorMask (final boolean red, final boolean green, final boolean blue, final boolean alpha) {
	}

	@Override
	public void glCompressedTexImage2D (final int target, final int level, final int internalformat, final int width,
		final int height, final int border, final int imageSize, final Buffer data) {
	}

	@Override
	public void glCompressedTexSubImage2D (final int target, final int level, final int xoffset, final int yoffset,
		final int width, final int height, final int format, final int imageSize, final Buffer data) {
	}

	@Override
	public void glCopyTexImage2D (final int target, final int level, final int internalformat, final int x, final int y,
		final int width, final int height, final int border) {
	}

	@Override
	public void glCopyTexSubImage2D (final int target, final int level, final int xoffset, final int yoffset, final int x,
		final int y, final int width, final int height) {
	}

	@Override
	public void glCullFace (final int mode) {
	}

	@Override
	public void glDeleteTextures (final int n, final IntBuffer textures) {
	}

	@Override
	public void glDeleteTexture (final int texture) {
	}

	@Override
	public void glDepthFunc (final int func) {
	}

	@Override
	public void glDepthMask (final boolean flag) {
	}

	@Override
	public void glDepthRangef (final float zNear, final float zFar) {
	}

	@Override
	public void glDisable (final int cap) {
	}

	@Override
	public void glDrawArrays (final int mode, final int first, final int count) {
	}

	@Override
	public void glDrawElements (final int mode, final int count, final int type, final Buffer indices) {
	}

	@Override
	public void glEnable (final int cap) {
	}

	@Override
	public void glFinish () {
	}

	@Override
	public void glFlush () {
	}

	@Override
	public void glFrontFace (final int mode) {
	}

	@Override
	public void glGenTextures (final int n, final IntBuffer textures) {
	}

	@Override
	public int glGenTexture () {
		return 0;
	}

	@Override
	public int glGetError () {
		return 0;
	}

	@Override
	public void glGetIntegerv (final int pname, final IntBuffer params) {
	}

	@Override
	public String glGetString (final int name) {
		return null;
	}

	@Override
	public void glHint (final int target, final int mode) {
	}

	@Override
	public void glLineWidth (final float width) {
	}

	@Override
	public void glPixelStorei (final int pname, final int param) {
	}

	@Override
	public void glPolygonOffset (final float factor, final float units) {
	}

	@Override
	public void glReadPixels (final int x, final int y, final int width, final int height, final int format, final int type,
		final Buffer pixels) {
	}

	@Override
	public void glScissor (final int x, final int y, final int width, final int height) {
	}

	@Override
	public void glStencilFunc (final int func, final int ref, final int mask) {
	}

	@Override
	public void glStencilMask (final int mask) {
	}

	@Override
	public void glStencilOp (final int fail, final int zfail, final int zpass) {
	}

	@Override
	public void glTexImage2D (final int target, final int level, final int internalformat, final int width, final int height,
		final int border, final int format, final int type, final Buffer pixels) {
	}

	@Override
	public void glTexParameterf (final int target, final int pname, final float param) {
	}

	@Override
	public void glTexSubImage2D (final int target, final int level, final int xoffset, final int yoffset, final int width,
		final int height, final int format, final int type, final Buffer pixels) {
	}

	@Override
	public void glViewport (final int x, final int y, final int width, final int height) {
	}

	@Override
	public void glAttachShader (final int program, final int shader) {
	}

	@Override
	public void glBindAttribLocation (final int program, final int index, final String name) {
	}

	@Override
	public void glBindBuffer (final int target, final int buffer) {
	}

	@Override
	public void glBindFramebuffer (final int target, final int framebuffer) {
	}

	@Override
	public void glBindRenderbuffer (final int target, final int renderbuffer) {
	}

	@Override
	public void glBlendColor (final float red, final float green, final float blue, final float alpha) {
	}

	@Override
	public void glBlendEquation (final int mode) {
	}

	@Override
	public void glBlendEquationSeparate (final int modeRGB, final int modeAlpha) {
	}

	@Override
	public void glBlendFuncSeparate (final int srcRGB, final int dstRGB, final int srcAlpha, final int dstAlpha) {
	}

	@Override
	public void glBufferData (final int target, final int size, final Buffer data, final int usage) {
	}

	@Override
	public void glBufferSubData (final int target, final int offset, final int size, final Buffer data) {
	}

	@Override
	public int glCheckFramebufferStatus (final int target) {
		return 0;
	}

	@Override
	public void glCompileShader (final int shader) {
	}

	@Override
	public int glCreateProgram () {
		return 0;
	}

	@Override
	public int glCreateShader (final int type) {
		return 0;
	}

	@Override
	public void glDeleteBuffer (final int buffer) {
	}

	@Override
	public void glDeleteBuffers (final int n, final IntBuffer buffers) {
	}

	@Override
	public void glDeleteFramebuffer (final int framebuffer) {
	}

	@Override
	public void glDeleteFramebuffers (final int n, final IntBuffer framebuffers) {
	}

	@Override
	public void glDeleteProgram (final int program) {
	}

	@Override
	public void glDeleteRenderbuffer (final int renderbuffer) {
	}

	@Override
	public void glDeleteRenderbuffers (final int n, final IntBuffer renderbuffers) {
	}

	@Override
	public void glDeleteShader (final int shader) {
	}

	@Override
	public void glDetachShader (final int program, final int shader) {
	}

	@Override
	public void glDisableVertexAttribArray (final int index) {
	}

	@Override
	public void glDrawElements (final int mode, final int count, final int type, final int indices) {
	}

	@Override
	public void glEnableVertexAttribArray (final int index) {
	}

	@Override
	public void glFramebufferRenderbuffer (final int target, final int attachment, final int renderbuffertarget,
		final int renderbuffer) {
	}

	@Override
	public void glFramebufferTexture2D (final int target, final int attachment, final int textarget, final int texture,
		final int level) {
	}

	@Override
	public int glGenBuffer () {
		return 0;
	}

	@Override
	public void glGenBuffers (final int n, final IntBuffer buffers) {
	}

	@Override
	public void glGenerateMipmap (final int target) {
	}

	@Override
	public int glGenFramebuffer () {
		return 0;
	}

	@Override
	public void glGenFramebuffers (final int n, final IntBuffer framebuffers) {
	}

	@Override
	public int glGenRenderbuffer () {
		return 0;
	}

	@Override
	public void glGenRenderbuffers (final int n, final IntBuffer renderbuffers) {
	}

	@Override
	public String glGetActiveAttrib (final int program, final int index, final IntBuffer size, final Buffer type) {
		return null;
	}

	@Override
	public String glGetActiveUniform (final int program, final int index, final IntBuffer size, final Buffer type) {
		return null;
	}

	@Override
	public void glGetAttachedShaders (final int program, final int maxcount, final Buffer count, final IntBuffer shaders) {
	}

	@Override
	public int glGetAttribLocation (final int program, final String name) {
		return 0;
	}

	@Override
	public void glGetBooleanv (final int pname, final Buffer params) {
	}

	@Override
	public void glGetBufferParameteriv (final int target, final int pname, final IntBuffer params) {
	}

	@Override
	public void glGetFloatv (final int pname, final FloatBuffer params) {
	}

	@Override
	public void glGetFramebufferAttachmentParameteriv (final int target, final int attachment, final int pname,
		final IntBuffer params) {
	}

	@Override
	public void glGetProgramiv (final int program, final int pname, final IntBuffer params) {
	}

	@Override
	public String glGetProgramInfoLog (final int program) {
		return null;
	}

	@Override
	public void glGetRenderbufferParameteriv (final int target, final int pname, final IntBuffer params) {
	}

	@Override
	public void glGetShaderiv (final int shader, final int pname, final IntBuffer params) {
	}

	@Override
	public String glGetShaderInfoLog (final int shader) {
		return null;
	}

	@Override
	public void glGetShaderPrecisionFormat (final int shadertype, final int precisiontype, final IntBuffer range,
		final IntBuffer precision) {
	}

	@Override
	public void glGetTexParameterfv (final int target, final int pname, final FloatBuffer params) {
	}

	@Override
	public void glGetTexParameteriv (final int target, final int pname, final IntBuffer params) {
	}

	@Override
	public void glGetUniformfv (final int program, final int location, final FloatBuffer params) {
	}

	@Override
	public void glGetUniformiv (final int program, final int location, final IntBuffer params) {
	}

	@Override
	public int glGetUniformLocation (final int program, final String name) {
		return 0;
	}

	@Override
	public void glGetVertexAttribfv (final int index, final int pname, final FloatBuffer params) {
	}

	@Override
	public void glGetVertexAttribiv (final int index, final int pname, final IntBuffer params) {
	}

	@Override
	public void glGetVertexAttribPointerv (final int index, final int pname, final Buffer pointer) {
	}

	@Override
	public boolean glIsBuffer (final int buffer) {
		return false;
	}

	@Override
	public boolean glIsEnabled (final int cap) {
		return false;
	}

	@Override
	public boolean glIsFramebuffer (final int framebuffer) {
		return false;
	}

	@Override
	public boolean glIsProgram (final int program) {
		return false;
	}

	@Override
	public boolean glIsRenderbuffer (final int renderbuffer) {
		return false;
	}

	@Override
	public boolean glIsShader (final int shader) {
		return false;
	}

	@Override
	public boolean glIsTexture (final int texture) {
		return false;
	}

	@Override
	public void glLinkProgram (final int program) {
	}

	@Override
	public void glReleaseShaderCompiler () {
	}

	@Override
	public void glRenderbufferStorage (final int target, final int internalformat, final int width, final int height) {
	}

	@Override
	public void glSampleCoverage (final float value, final boolean invert) {
	}

	@Override
	public void glShaderBinary (final int n, final IntBuffer shaders, final int binaryformat, final Buffer binary,
		final int length) {
	}

	@Override
	public void glShaderSource (final int shader, final String string) {
	}

	@Override
	public void glStencilFuncSeparate (final int face, final int func, final int ref, final int mask) {
	}

	@Override
	public void glStencilMaskSeparate (final int face, final int mask) {
	}

	@Override
	public void glStencilOpSeparate (final int face, final int fail, final int zfail, final int zpass) {
	}

	@Override
	public void glTexParameterfv (final int target, final int pname, final FloatBuffer params) {
	}

	@Override
	public void glTexParameteri (final int target, final int pname, final int param) {
	}

	@Override
	public void glTexParameteriv (final int target, final int pname, final IntBuffer params) {
	}

	@Override
	public void glUniform1f (final int location, final float x) {
	}

	@Override
	public void glUniform1fv (final int location, final int count, final FloatBuffer v) {
	}

	@Override
	public void glUniform1fv (final int location, final int count, final float[] v, final int offset) {
	}

	@Override
	public void glUniform1i (final int location, final int x) {
	}

	@Override
	public void glUniform1iv (final int location, final int count, final IntBuffer v) {
	}

	@Override
	public void glUniform1iv (final int location, final int count, final int[] v, final int offset) {
	}

	@Override
	public void glUniform2f (final int location, final float x, final float y) {
	}

	@Override
	public void glUniform2fv (final int location, final int count, final FloatBuffer v) {
	}

	@Override
	public void glUniform2fv (final int location, final int count, final float[] v, final int offset) {
	}

	@Override
	public void glUniform2i (final int location, final int x, final int y) {
	}

	@Override
	public void glUniform2iv (final int location, final int count, final IntBuffer v) {
	}

	@Override
	public void glUniform2iv (final int location, final int count, final int[] v, final int offset) {
	}

	@Override
	public void glUniform3f (final int location, final float x, final float y, final float z) {
	}

	@Override
	public void glUniform3fv (final int location, final int count, final FloatBuffer v) {
	}

	@Override
	public void glUniform3fv (final int location, final int count, final float[] v, final int offset) {
	}

	@Override
	public void glUniform3i (final int location, final int x, final int y, final int z) {
	}

	@Override
	public void glUniform3iv (final int location, final int count, final IntBuffer v) {
	}

	@Override
	public void glUniform3iv (final int location, final int count, final int[] v, final int offset) {
	}

	@Override
	public void glUniform4f (final int location, final float x, final float y, final float z, final float w) {
	}

	@Override
	public void glUniform4fv (final int location, final int count, final FloatBuffer v) {
	}

	@Override
	public void glUniform4fv (final int location, final int count, final float[] v, final int offset) {
	}

	@Override
	public void glUniform4i (final int location, final int x, final int y, final int z, final int w) {
	}

	@Override
	public void glUniform4iv (final int location, final int count, final IntBuffer v) {
	}

	@Override
	public void glUniform4iv (final int location, final int count, final int[] v, final int offset) {
	}

	@Override
	public void glUniformMatrix2fv (final int location, final int count, final boolean transpose, final FloatBuffer value) {
	}

	@Override
	public void glUniformMatrix2fv (final int location, final int count, final boolean transpose, final float[] value,
		final int offset) {
	}

	@Override
	public void glUniformMatrix3fv (final int location, final int count, final boolean transpose, final FloatBuffer value) {
	}

	@Override
	public void glUniformMatrix3fv (final int location, final int count, final boolean transpose, final float[] value,
		final int offset) {
	}

	@Override
	public void glUniformMatrix4fv (final int location, final int count, final boolean transpose, final FloatBuffer value) {
	}

	@Override
	public void glUniformMatrix4fv (final int location, final int count, final boolean transpose, final float[] value,
		final int offset) {
	}

	@Override
	public void glUseProgram (final int program) {
	}

	@Override
	public void glValidateProgram (final int program) {
	}

	@Override
	public void glVertexAttrib1f (final int indx, final float x) {
	}

	@Override
	public void glVertexAttrib1fv (final int indx, final FloatBuffer values) {
	}

	@Override
	public void glVertexAttrib2f (final int indx, final float x, final float y) {
	}

	@Override
	public void glVertexAttrib2fv (final int indx, final FloatBuffer values) {
	}

	@Override
	public void glVertexAttrib3f (final int indx, final float x, final float y, final float z) {
	}

	@Override
	public void glVertexAttrib3fv (final int indx, final FloatBuffer values) {
	}

	@Override
	public void glVertexAttrib4f (final int indx, final float x, final float y, final float z, final float w) {
	}

	@Override
	public void glVertexAttrib4fv (final int indx, final FloatBuffer values) {
	}

	@Override
	public void glVertexAttribPointer (final int indx, final int size, final int type, final boolean normalized, final int stride,
		final Buffer ptr) {
	}

	@Override
	public void glVertexAttribPointer (final int indx, final int size, final int type, final boolean normalized, final int stride,
		final int ptr) {
	}
}
