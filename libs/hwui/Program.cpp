/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#define LOG_TAG "OpenGLRenderer"

#include "Program.h"

namespace android {
namespace uirenderer {

///////////////////////////////////////////////////////////////////////////////
// Base program
///////////////////////////////////////////////////////////////////////////////

<<<<<<< HEAD
// TODO: Program instance should be created from a factory method
Program::Program(const ProgramDescription& description, const char* vertex, const char* fragment) {
    mInitialized = false;
    mHasColorUniform = false;
    mHasSampler = false;
    mUse = false;

    // No need to cache compiled shaders, rely instead on Android's
    // persistent shaders cache
    mVertexShader = buildShader(vertex, GL_VERTEX_SHADER);
    if (mVertexShader) {
        mFragmentShader = buildShader(fragment, GL_FRAGMENT_SHADER);
        if (mFragmentShader) {
            mProgramId = glCreateProgram();

            glAttachShader(mProgramId, mVertexShader);
            glAttachShader(mProgramId, mFragmentShader);

            position = bindAttrib("position", kBindingPosition);
            if (description.hasTexture || description.hasExternalTexture) {
                texCoords = bindAttrib("texCoords", kBindingTexCoords);
            } else {
                texCoords = -1;
            }

            glLinkProgram(mProgramId);

            GLint status;
            glGetProgramiv(mProgramId, GL_LINK_STATUS, &status);
            if (status != GL_TRUE) {
                ALOGE("Error while linking shaders:");
                GLint infoLen = 0;
                glGetProgramiv(mProgramId, GL_INFO_LOG_LENGTH, &infoLen);
                if (infoLen > 1) {
                    GLchar log[infoLen];
                    glGetProgramInfoLog(mProgramId, infoLen, 0, &log[0]);
                    ALOGE("%s", log);
                }

                glDetachShader(mProgramId, mVertexShader);
                glDetachShader(mProgramId, mFragmentShader);

                glDeleteShader(mVertexShader);
                glDeleteShader(mFragmentShader);

                glDeleteProgram(mProgramId);
            } else {
                mInitialized = true;
            }
        } else {
            glDeleteShader(mVertexShader);
        }
    }

    if (mInitialized) {
=======
Program::Program(const char* vertex, const char* fragment) {
    mInitialized = false;

    vertexShader = buildShader(vertex, GL_VERTEX_SHADER);
    if (vertexShader) {

        fragmentShader = buildShader(fragment, GL_FRAGMENT_SHADER);
        if (fragmentShader) {

            id = glCreateProgram();
            glAttachShader(id, vertexShader);
            glAttachShader(id, fragmentShader);
            glLinkProgram(id);

            GLint status;
            glGetProgramiv(id, GL_LINK_STATUS, &status);
            if (status != GL_TRUE) {
                LOGE("Error while linking shaders:");
                GLint infoLen = 0;
                glGetProgramiv(id, GL_INFO_LOG_LENGTH, &infoLen);
                if (infoLen > 1) {
                    GLchar log[infoLen];
                    glGetProgramInfoLog(id, infoLen, 0, &log[0]);
                    LOGE("%s", log);
                }
                glDeleteShader(vertexShader);
                glDeleteShader(fragmentShader);
                glDeleteProgram(id);
            } else {
                mInitialized = true;
            }
        }
    }

    mUse = false;

    if (mInitialized) {
        position = addAttrib("position");
>>>>>>> upstream/master
        transform = addUniform("transform");
    }
}

Program::~Program() {
    if (mInitialized) {
<<<<<<< HEAD
        glDetachShader(mProgramId, mVertexShader);
        glDetachShader(mProgramId, mFragmentShader);

        glDeleteShader(mVertexShader);
        glDeleteShader(mFragmentShader);

        glDeleteProgram(mProgramId);
=======
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        glDeleteProgram(id);
>>>>>>> upstream/master
    }
}

int Program::addAttrib(const char* name) {
<<<<<<< HEAD
    int slot = glGetAttribLocation(mProgramId, name);
    mAttributes.add(name, slot);
    return slot;
}

int Program::bindAttrib(const char* name, ShaderBindings bindingSlot) {
    glBindAttribLocation(mProgramId, bindingSlot, name);
    mAttributes.add(name, bindingSlot);
    return bindingSlot;
}

int Program::getAttrib(const char* name) {
    ssize_t index = mAttributes.indexOfKey(name);
    if (index >= 0) {
        return mAttributes.valueAt(index);
=======
    int slot = glGetAttribLocation(id, name);
    attributes.add(name, slot);
    return slot;
}

int Program::getAttrib(const char* name) {
    ssize_t index = attributes.indexOfKey(name);
    if (index >= 0) {
        return attributes.valueAt(index);
>>>>>>> upstream/master
    }
    return addAttrib(name);
}

int Program::addUniform(const char* name) {
<<<<<<< HEAD
    int slot = glGetUniformLocation(mProgramId, name);
    mUniforms.add(name, slot);
=======
    int slot = glGetUniformLocation(id, name);
    uniforms.add(name, slot);
>>>>>>> upstream/master
    return slot;
}

int Program::getUniform(const char* name) {
<<<<<<< HEAD
    ssize_t index = mUniforms.indexOfKey(name);
    if (index >= 0) {
        return mUniforms.valueAt(index);
=======
    ssize_t index = uniforms.indexOfKey(name);
    if (index >= 0) {
        return uniforms.valueAt(index);
>>>>>>> upstream/master
    }
    return addUniform(name);
}

GLuint Program::buildShader(const char* source, GLenum type) {
    GLuint shader = glCreateShader(type);
    glShaderSource(shader, 1, &source, 0);
    glCompileShader(shader);

    GLint status;
    glGetShaderiv(shader, GL_COMPILE_STATUS, &status);
    if (status != GL_TRUE) {
        // Some drivers return wrong values for GL_INFO_LOG_LENGTH
        // use a fixed size instead
        GLchar log[512];
        glGetShaderInfoLog(shader, sizeof(log), 0, &log[0]);
<<<<<<< HEAD
        ALOGE("Error while compiling shader: %s", log);
=======
        LOGE("Error while compiling shader: %s", log);
>>>>>>> upstream/master
        glDeleteShader(shader);
        return 0;
    }

    return shader;
}

void Program::set(const mat4& projectionMatrix, const mat4& modelViewMatrix,
        const mat4& transformMatrix, bool offset) {
    mat4 t(projectionMatrix);
    if (offset) {
<<<<<<< HEAD
        // offset screenspace xy by an amount that compensates for typical precision
        // issues in GPU hardware that tends to paint hor/vert lines in pixels shifted
        // up and to the left.
        // This offset value is based on an assumption that some hardware may use as
        // little as 12.4 precision, so we offset by slightly more than 1/16.
=======
        // offset screenspace xy by an amount that compensates for typical precision issues
        // in GPU hardware that tends to paint hor/vert lines in pixels shifted up and to the left.
        // This offset value is based on an assumption that some hardware may use as little
        // as 12.4 precision, so we offset by slightly more than 1/16.
>>>>>>> upstream/master
        t.translate(.375, .375, 0);
    }
    t.multiply(transformMatrix);
    t.multiply(modelViewMatrix);

    glUniformMatrix4fv(transform, 1, GL_FALSE, &t.data[0]);
}

void Program::setColor(const float r, const float g, const float b, const float a) {
<<<<<<< HEAD
    if (!mHasColorUniform) {
        mColorUniform = getUniform("color");
        mHasColorUniform = true;
    }
    glUniform4f(mColorUniform, r, g, b, a);
}

void Program::use() {
    glUseProgram(mProgramId);
    if (texCoords >= 0 && !mHasSampler) {
        glUniform1i(getUniform("sampler"), 0);
        mHasSampler = true;
    }
    mUse = true;
=======
    glUniform4f(getUniform("color"), r, g, b, a);
}

void Program::use() {
    glUseProgram(id);
    mUse = true;

    glEnableVertexAttribArray(position);
>>>>>>> upstream/master
}

void Program::remove() {
    mUse = false;
<<<<<<< HEAD
=======

    glDisableVertexAttribArray(position);
>>>>>>> upstream/master
}

}; // namespace uirenderer
}; // namespace android
