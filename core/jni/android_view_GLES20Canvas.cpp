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

#include <EGL/egl.h>

#include "jni.h"
#include "GraphicsJNI.h"
#include <nativehelper/JNIHelp.h>
<<<<<<< HEAD

#include <android_runtime/AndroidRuntime.h>
#include <android_runtime/android_graphics_SurfaceTexture.h>
#include <gui/SurfaceTexture.h>

#include <androidfw/ResourceTypes.h>

#include <private/hwui/DrawGlInfo.h>

#include <cutils/properties.h>
=======
#include <android_runtime/AndroidRuntime.h>
#include <android_runtime/android_graphics_SurfaceTexture.h>
#include <cutils/properties.h>
#include <utils/ResourceTypes.h>

#include <gui/SurfaceTexture.h>
>>>>>>> upstream/master

#include <SkBitmap.h>
#include <SkCanvas.h>
#include <SkMatrix.h>
#include <SkPaint.h>
#include <SkRegion.h>
#include <SkScalerContext.h>
#include <SkTemplates.h>
#include <SkXfermode.h>

#include <DisplayListRenderer.h>
#include <LayerRenderer.h>
#include <OpenGLRenderer.h>
#include <SkiaShader.h>
#include <SkiaColorFilter.h>
#include <Rect.h>

#include <TextLayout.h>
<<<<<<< HEAD
#include <TextLayoutCache.h>
=======
>>>>>>> upstream/master

namespace android {

using namespace uirenderer;

/**
 * Note: OpenGLRenderer JNI layer is generated and compiled only on supported
 *       devices. This means all the logic must be compiled only when the
 *       preprocessor variable USE_OPENGL_RENDERER is defined.
 */
#ifdef USE_OPENGL_RENDERER

<<<<<<< HEAD
// ----------------------------------------------------------------------------
// Defines
// ----------------------------------------------------------------------------
=======
///////////////////////////////////////////////////////////////////////////////
// Defines
///////////////////////////////////////////////////////////////////////////////
>>>>>>> upstream/master

// Debug
#define DEBUG_RENDERER 0

// Debug
#if DEBUG_RENDERER
<<<<<<< HEAD
    #define RENDERER_LOGD(...) ALOGD(__VA_ARGS__)
=======
    #define RENDERER_LOGD(...) LOGD(__VA_ARGS__)
>>>>>>> upstream/master
#else
    #define RENDERER_LOGD(...)
#endif

#define MODIFIER_SHADOW 1
#define MODIFIER_SHADER 2
#define MODIFIER_COLOR_FILTER 4

// ----------------------------------------------------------------------------

static struct {
    jmethodID set;
} gRectClassInfo;

// ----------------------------------------------------------------------------
<<<<<<< HEAD
// Caching
// ----------------------------------------------------------------------------

=======
// Misc
// ----------------------------------------------------------------------------

static jboolean android_view_GLES20Canvas_preserveBackBuffer(JNIEnv* env, jobject clazz) {
    EGLDisplay display = eglGetCurrentDisplay();
    EGLSurface surface = eglGetCurrentSurface(EGL_DRAW);

    eglGetError();
    eglSurfaceAttrib(display, surface, EGL_SWAP_BEHAVIOR, EGL_BUFFER_PRESERVED);

    EGLint error = eglGetError();
    if (error != EGL_SUCCESS) {
        RENDERER_LOGD("Could not enable buffer preserved swap behavior (%x)", error);
    }

    return error == EGL_SUCCESS;
}

static jboolean android_view_GLES20Canvas_isBackBufferPreserved(JNIEnv* env, jobject clazz) {
    EGLDisplay display = eglGetCurrentDisplay();
    EGLSurface surface = eglGetCurrentSurface(EGL_DRAW);
    EGLint value;

    eglGetError();
    eglQuerySurface(display, surface, EGL_SWAP_BEHAVIOR, &value);

    EGLint error = eglGetError();
    if (error != EGL_SUCCESS) {
        RENDERER_LOGD("Could not query buffer preserved swap behavior (%x)", error);
    }

    return error == EGL_SUCCESS && value == EGL_BUFFER_PRESERVED;
}

static void android_view_GLES20Canvas_disableVsync(JNIEnv* env, jobject clazz) {
    EGLDisplay display = eglGetCurrentDisplay();

    eglGetError();
    eglSwapInterval(display, 0);

    EGLint error = eglGetError();
    if (error != EGL_SUCCESS) {
        RENDERER_LOGD("Could not disable v-sync (%x)", error);
    }
}

>>>>>>> upstream/master
static void android_view_GLES20Canvas_flushCaches(JNIEnv* env, jobject clazz,
        Caches::FlushMode mode) {
    if (Caches::hasInstance()) {
        Caches::getInstance().flush(mode);
    }
}

static void android_view_GLES20Canvas_initCaches(JNIEnv* env, jobject clazz) {
    if (Caches::hasInstance()) {
        Caches::getInstance().init();
    }
}

static void android_view_GLES20Canvas_terminateCaches(JNIEnv* env, jobject clazz) {
    if (Caches::hasInstance()) {
        Caches::getInstance().terminate();
    }
}

// ----------------------------------------------------------------------------
// Constructors
// ----------------------------------------------------------------------------

static OpenGLRenderer* android_view_GLES20Canvas_createRenderer(JNIEnv* env, jobject clazz) {
    RENDERER_LOGD("Create OpenGLRenderer");
    return new OpenGLRenderer;
}

static void android_view_GLES20Canvas_destroyRenderer(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer) {
    RENDERER_LOGD("Destroy OpenGLRenderer");
    delete renderer;
}

// ----------------------------------------------------------------------------
// Setup
// ----------------------------------------------------------------------------

static void android_view_GLES20Canvas_setViewport(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jint width, jint height) {
    renderer->setViewport(width, height);
}

<<<<<<< HEAD
static int android_view_GLES20Canvas_prepare(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jboolean opaque) {
    return renderer->prepare(opaque);
}

static int android_view_GLES20Canvas_prepareDirty(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jint left, jint top, jint right, jint bottom,
        jboolean opaque) {
    return renderer->prepareDirty(left, top, right, bottom, opaque);
=======
static void android_view_GLES20Canvas_prepare(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jboolean opaque) {
    renderer->prepare(opaque);
}

static void android_view_GLES20Canvas_prepareDirty(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jint left, jint top, jint right, jint bottom,
        jboolean opaque) {
    renderer->prepareDirty(left, top, right, bottom, opaque);
>>>>>>> upstream/master
}

static void android_view_GLES20Canvas_finish(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer) {
    renderer->finish();
}

<<<<<<< HEAD
static jint android_view_GLES20Canvas_getStencilSize(JNIEnv* env, jobject clazz) {
    return OpenGLRenderer::getStencilSize();
}

// ----------------------------------------------------------------------------
// Functor
// ----------------------------------------------------------------------------

static jint android_view_GLES20Canvas_callDrawGLFunction(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, Functor* functor) {
=======
static bool android_view_GLES20Canvas_callDrawGLFunction(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, Functor *functor) {
>>>>>>> upstream/master
    android::uirenderer::Rect dirty;
    return renderer->callDrawGLFunction(functor, dirty);
}

<<<<<<< HEAD
static void android_view_GLES20Canvas_detachFunctor(JNIEnv* env,
        jobject clazz, OpenGLRenderer* renderer, Functor* functor) {
    renderer->detachFunctor(functor);
}

static void android_view_GLES20Canvas_attachFunctor(JNIEnv* env,
        jobject clazz, OpenGLRenderer* renderer, Functor* functor) {
    renderer->attachFunctor(functor);
}

static jint android_view_GLES20Canvas_invokeFunctors(JNIEnv* env,
        jobject clazz, OpenGLRenderer* renderer, jobject dirty) {
    android::uirenderer::Rect bounds;
    status_t status = renderer->invokeFunctors(bounds);
    if (status != DrawGlInfo::kStatusDone && dirty != NULL) {
        env->CallVoidMethod(dirty, gRectClassInfo.set,
                int(bounds.left), int(bounds.top), int(bounds.right), int(bounds.bottom));
    }
    return status;
}

// ----------------------------------------------------------------------------
// Misc
// ----------------------------------------------------------------------------

=======
>>>>>>> upstream/master
static jint android_view_GLES20Canvas_getMaxTextureWidth(JNIEnv* env, jobject clazz) {
    return Caches::getInstance().maxTextureSize;
}

static jint android_view_GLES20Canvas_getMaxTextureHeight(JNIEnv* env, jobject clazz) {
    return Caches::getInstance().maxTextureSize;
}

// ----------------------------------------------------------------------------
// State
// ----------------------------------------------------------------------------

static jint android_view_GLES20Canvas_save(JNIEnv* env, jobject clazz, OpenGLRenderer* renderer,
        jint flags) {
    return renderer->save(flags);
}

static jint android_view_GLES20Canvas_getSaveCount(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer) {
    return renderer->getSaveCount();
}

static void android_view_GLES20Canvas_restore(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer) {
    renderer->restore();
}

static void android_view_GLES20Canvas_restoreToCount(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jint saveCount) {
    renderer->restoreToCount(saveCount);
}

// ----------------------------------------------------------------------------
// Layers
// ----------------------------------------------------------------------------

static jint android_view_GLES20Canvas_saveLayer(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jfloat left, jfloat top, jfloat right, jfloat bottom,
        SkPaint* paint, jint saveFlags) {
    return renderer->saveLayer(left, top, right, bottom, paint, saveFlags);
}

static jint android_view_GLES20Canvas_saveLayerClip(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, SkPaint* paint, jint saveFlags) {
    const android::uirenderer::Rect& bounds(renderer->getClipBounds());
    return renderer->saveLayer(bounds.left, bounds.top, bounds.right, bounds.bottom,
            paint, saveFlags);
}

static jint android_view_GLES20Canvas_saveLayerAlpha(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jfloat left, jfloat top, jfloat right, jfloat bottom,
        jint alpha, jint saveFlags) {
    return renderer->saveLayerAlpha(left, top, right, bottom, alpha, saveFlags);
}

static jint android_view_GLES20Canvas_saveLayerAlphaClip(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jint alpha, jint saveFlags) {
    const android::uirenderer::Rect& bounds(renderer->getClipBounds());
    return renderer->saveLayerAlpha(bounds.left, bounds.top, bounds.right, bounds.bottom,
            alpha, saveFlags);
}

// ----------------------------------------------------------------------------
// Clipping
// ----------------------------------------------------------------------------

static bool android_view_GLES20Canvas_quickReject(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jfloat left, jfloat top, jfloat right, jfloat bottom,
        SkCanvas::EdgeType edge) {
    return renderer->quickReject(left, top, right, bottom);
}

static bool android_view_GLES20Canvas_clipRectF(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jfloat left, jfloat top, jfloat right, jfloat bottom,
        SkRegion::Op op) {
    return renderer->clipRect(left, top, right, bottom, op);
}

static bool android_view_GLES20Canvas_clipRect(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jint left, jint top, jint right, jint bottom,
        SkRegion::Op op) {
    return renderer->clipRect(float(left), float(top), float(right), float(bottom), op);
}

static bool android_view_GLES20Canvas_getClipBounds(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jobject rect) {
    const android::uirenderer::Rect& bounds(renderer->getClipBounds());

    env->CallVoidMethod(rect, gRectClassInfo.set,
            int(bounds.left), int(bounds.top), int(bounds.right), int(bounds.bottom));

    return !bounds.isEmpty();
}

// ----------------------------------------------------------------------------
// Transforms
// ----------------------------------------------------------------------------

static void android_view_GLES20Canvas_translate(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jfloat dx, jfloat dy) {
    renderer->translate(dx, dy);
}

static void android_view_GLES20Canvas_rotate(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jfloat degrees) {
    renderer->rotate(degrees);
}

static void android_view_GLES20Canvas_scale(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jfloat sx, jfloat sy) {
    renderer->scale(sx, sy);
}

static void android_view_GLES20Canvas_skew(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jfloat sx, jfloat sy) {
    renderer->skew(sx, sy);
}

static void android_view_GLES20Canvas_setMatrix(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, SkMatrix* matrix) {
    renderer->setMatrix(matrix);
}

static void android_view_GLES20Canvas_getMatrix(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, SkMatrix* matrix) {
    renderer->getMatrix(matrix);
}

static void android_view_GLES20Canvas_concatMatrix(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, SkMatrix* matrix) {
    renderer->concatMatrix(matrix);
}

// ----------------------------------------------------------------------------
// Drawing
// ----------------------------------------------------------------------------

static void android_view_GLES20Canvas_drawBitmap(JNIEnv* env, jobject clazz,
<<<<<<< HEAD
        OpenGLRenderer* renderer, SkBitmap* bitmap, jbyteArray buffer,
        jfloat left, jfloat top, SkPaint* paint) {
=======
        OpenGLRenderer* renderer, SkBitmap* bitmap, jbyteArray buffer, float left,
        float top, SkPaint* paint) {
>>>>>>> upstream/master
    // This object allows the renderer to allocate a global JNI ref to the buffer object.
    JavaHeapBitmapRef bitmapRef(env, bitmap, buffer);

    renderer->drawBitmap(bitmap, left, top, paint);
}

static void android_view_GLES20Canvas_drawBitmapRect(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, SkBitmap* bitmap, jbyteArray buffer,
        float srcLeft, float srcTop, float srcRight, float srcBottom,
        float dstLeft, float dstTop, float dstRight, float dstBottom, SkPaint* paint) {
    // This object allows the renderer to allocate a global JNI ref to the buffer object.
    JavaHeapBitmapRef bitmapRef(env, bitmap, buffer);

    renderer->drawBitmap(bitmap, srcLeft, srcTop, srcRight, srcBottom,
            dstLeft, dstTop, dstRight, dstBottom, paint);
}

static void android_view_GLES20Canvas_drawBitmapMatrix(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, SkBitmap* bitmap, jbyteArray buffer, SkMatrix* matrix,
        SkPaint* paint) {
    // This object allows the renderer to allocate a global JNI ref to the buffer object.
    JavaHeapBitmapRef bitmapRef(env, bitmap, buffer);

    renderer->drawBitmap(bitmap, matrix, paint);
}

<<<<<<< HEAD
static void android_view_GLES20Canvas_drawBitmapData(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jintArray colors, jint offset, jint stride,
        jfloat left, jfloat top, jint width, jint height, jboolean hasAlpha, SkPaint* paint) {
    SkBitmap* bitmap = new SkBitmap;
    bitmap->setConfig(hasAlpha ? SkBitmap::kARGB_8888_Config : SkBitmap::kRGB_565_Config,
            width, height);

    if (!bitmap->allocPixels()) {
        delete bitmap;
        return;
    }

    if (!GraphicsJNI::SetPixels(env, colors, offset, stride, 0, 0, width, height, *bitmap)) {
        delete bitmap;
        return;
    }

    renderer->drawBitmapData(bitmap, left, top, paint);

    // If the renderer is a deferred renderer it will own the bitmap
    if (!renderer->isDeferred()) {
        delete bitmap;
    }
}

=======
>>>>>>> upstream/master
static void android_view_GLES20Canvas_drawBitmapMesh(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, SkBitmap* bitmap, jbyteArray buffer,
        jint meshWidth, jint meshHeight, jfloatArray vertices, jint offset,
        jintArray colors, jint colorOffset, SkPaint* paint) {
    // This object allows the renderer to allocate a global JNI ref to the buffer object.
    JavaHeapBitmapRef bitmapRef(env, bitmap, buffer);

    jfloat* verticesArray = vertices ? env->GetFloatArrayElements(vertices, NULL) + offset : NULL;
    jint* colorsArray = colors ? env->GetIntArrayElements(colors, NULL) + colorOffset : NULL;

    renderer->drawBitmapMesh(bitmap, meshWidth, meshHeight, verticesArray, colorsArray, paint);

    if (vertices) env->ReleaseFloatArrayElements(vertices, verticesArray, 0);
    if (colors) env->ReleaseIntArrayElements(colors, colorsArray, 0);
}

static void android_view_GLES20Canvas_drawPatch(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, SkBitmap* bitmap, jbyteArray buffer, jbyteArray chunks,
        float left, float top, float right, float bottom, SkPaint* paint) {
    // This object allows the renderer to allocate a global JNI ref to the buffer object.
    JavaHeapBitmapRef bitmapRef(env, bitmap, buffer);

    jbyte* storage = env->GetByteArrayElements(chunks, NULL);
    Res_png_9patch* patch = reinterpret_cast<Res_png_9patch*>(storage);
    Res_png_9patch::deserialize(patch);

    renderer->drawPatch(bitmap, &patch->xDivs[0], &patch->yDivs[0],
            &patch->colors[0], patch->numXDivs, patch->numYDivs, patch->numColors,
            left, top, right, bottom, paint);

    env->ReleaseByteArrayElements(chunks, storage, 0);
}

static void android_view_GLES20Canvas_drawColor(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jint color, SkXfermode::Mode mode) {
    renderer->drawColor(color, mode);
}

static void android_view_GLES20Canvas_drawRect(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jfloat left, jfloat top, jfloat right, jfloat bottom,
        SkPaint* paint) {
    renderer->drawRect(left, top, right, bottom, paint);
}

static void android_view_GLES20Canvas_drawRoundRect(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jfloat left, jfloat top, jfloat right, jfloat bottom,
        jfloat rx, jfloat ry, SkPaint* paint) {
    renderer->drawRoundRect(left, top, right, bottom, rx, ry, paint);
}

static void android_view_GLES20Canvas_drawCircle(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jfloat x, jfloat y, jfloat radius, SkPaint* paint) {
    renderer->drawCircle(x, y, radius, paint);
}

static void android_view_GLES20Canvas_drawOval(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jfloat left, jfloat top, jfloat right, jfloat bottom,
        SkPaint* paint) {
    renderer->drawOval(left, top, right, bottom, paint);
}

static void android_view_GLES20Canvas_drawArc(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jfloat left, jfloat top, jfloat right, jfloat bottom,
        jfloat startAngle, jfloat sweepAngle, jboolean useCenter, SkPaint* paint) {
    renderer->drawArc(left, top, right, bottom, startAngle, sweepAngle, useCenter, paint);
}

static void android_view_GLES20Canvas_drawRects(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, SkRegion* region, SkPaint* paint) {
    SkRegion::Iterator it(*region);
    while (!it.done()) {
        const SkIRect& r = it.rect();
        renderer->drawRect(r.fLeft, r.fTop, r.fRight, r.fBottom, paint);
        it.next();
    }
}

static void android_view_GLES20Canvas_drawPoints(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jfloatArray points, jint offset, jint count, SkPaint* paint) {
    jfloat* storage = env->GetFloatArrayElements(points, NULL);
    renderer->drawPoints(storage + offset, count, paint);
    env->ReleaseFloatArrayElements(points, storage, 0);
}

static void android_view_GLES20Canvas_drawPath(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, SkPath* path, SkPaint* paint) {
    renderer->drawPath(path, paint);
}

static void android_view_GLES20Canvas_drawLines(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jfloatArray points, jint offset, jint count, SkPaint* paint) {
    jfloat* storage = env->GetFloatArrayElements(points, NULL);
    renderer->drawLines(storage + offset, count, paint);
    env->ReleaseFloatArrayElements(points, storage, 0);
}

// ----------------------------------------------------------------------------
// Shaders and color filters
// ----------------------------------------------------------------------------

static void android_view_GLES20Canvas_resetModifiers(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jint modifiers) {
    if (modifiers & MODIFIER_SHADOW) renderer->resetShadow();
    if (modifiers & MODIFIER_SHADER) renderer->resetShader();
    if (modifiers & MODIFIER_COLOR_FILTER) renderer->resetColorFilter();
}

static void android_view_GLES20Canvas_setupShader(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, SkiaShader* shader) {
    renderer->setupShader(shader);
}

static void android_view_GLES20Canvas_setupColorFilter(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, SkiaColorFilter* filter) {
    renderer->setupColorFilter(filter);
}

static void android_view_GLES20Canvas_setupShadow(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jfloat radius, jfloat dx, jfloat dy, jint color) {
    renderer->setupShadow(radius, dx, dy, color);
}

// ----------------------------------------------------------------------------
<<<<<<< HEAD
// Draw filters
// ----------------------------------------------------------------------------

static void android_view_GLES20Canvas_setupPaintFilter(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jint clearBits, jint setBits) {
    renderer->setupPaintFilter(clearBits, setBits);
}

static void android_view_GLES20Canvas_resetPaintFilter(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer) {
    renderer->resetPaintFilter();
}

// ----------------------------------------------------------------------------
=======
>>>>>>> upstream/master
// Text
// ----------------------------------------------------------------------------

static void renderText(OpenGLRenderer* renderer, const jchar* text, int count,
        jfloat x, jfloat y, int flags, SkPaint* paint) {
<<<<<<< HEAD
    sp<TextLayoutValue> value = TextLayoutEngine::getInstance().getValue(paint,
            text, 0, count, count, flags);
    if (value == NULL) {
        return;
    }
=======
#if RTL_USE_HARFBUZZ
    sp<TextLayoutCacheValue> value;
#if USE_TEXT_LAYOUT_CACHE
    value = TextLayoutCache::getInstance().getValue(paint, text, 0, count, count, flags);
    if (value == NULL) {
        LOGE("Cannot get TextLayoutCache value");
        return ;
    }
#else
    value = new TextLayoutCacheValue();
    value->computeValues(paint, text, 0, count, count, flags);
#endif
>>>>>>> upstream/master
    const jchar* glyphs = value->getGlyphs();
    size_t glyphsCount = value->getGlyphsCount();
    int bytesCount = glyphsCount * sizeof(jchar);
    renderer->drawText((const char*) glyphs, bytesCount, glyphsCount, x, y, paint);
<<<<<<< HEAD
}

static void renderTextOnPath(OpenGLRenderer* renderer, const jchar* text, int count,
        SkPath* path, jfloat hOffset, jfloat vOffset, int flags, SkPaint* paint) {
    sp<TextLayoutValue> value = TextLayoutEngine::getInstance().getValue(paint,
            text, 0, count, count, flags);
    if (value == NULL) {
        return;
    }
    const jchar* glyphs = value->getGlyphs();
    size_t glyphsCount = value->getGlyphsCount();
    int bytesCount = glyphsCount * sizeof(jchar);
    renderer->drawTextOnPath((const char*) glyphs, bytesCount, glyphsCount, path,
            hOffset, vOffset, paint);
=======
#else
    const jchar *workText;
    jchar* buffer = NULL;
    int32_t workBytes;
    if (TextLayout::prepareText(paint, text, count, flags, &workText, &workBytes, &buffer)) {
        renderer->drawText((const char*) workText, workBytes, count, x, y, paint);
        free(buffer);
    }
#endif
>>>>>>> upstream/master
}

static void renderTextRun(OpenGLRenderer* renderer, const jchar* text,
        jint start, jint count, jint contextCount, jfloat x, jfloat y,
        int flags, SkPaint* paint) {
<<<<<<< HEAD
    sp<TextLayoutValue> value = TextLayoutEngine::getInstance().getValue(paint,
            text, start, count, contextCount, flags);
    if (value == NULL) {
        return;
    }
=======
#if RTL_USE_HARFBUZZ
    sp<TextLayoutCacheValue> value;
#if USE_TEXT_LAYOUT_CACHE
    value = TextLayoutCache::getInstance().getValue(paint, text, start, count, contextCount, flags);
    if (value == NULL) {
        LOGE("Cannot get TextLayoutCache value");
        return ;
    }
#else
    value = new TextLayoutCacheValue();
    value->computeValues(paint, text, start, count, contextCount, flags);
#endif
>>>>>>> upstream/master
    const jchar* glyphs = value->getGlyphs();
    size_t glyphsCount = value->getGlyphsCount();
    int bytesCount = glyphsCount * sizeof(jchar);
    renderer->drawText((const char*) glyphs, bytesCount, glyphsCount, x, y, paint);
<<<<<<< HEAD
=======
#else
    uint8_t rtl = flags & 0x1;
    if (rtl) {
        SkAutoSTMalloc<80, jchar> buffer(contextCount);
        jchar* shaped = buffer.get();
        if (TextLayout::prepareRtlTextRun(text, start, count, contextCount, shaped)) {
            renderer->drawText((const char*) shaped, count << 1, count, x, y, paint);
        } else {
            LOGW("drawTextRun error");
        }
    } else {
        renderer->drawText((const char*) (text + start), count << 1, count, x, y, paint);
    }
#endif
>>>>>>> upstream/master
}

static void android_view_GLES20Canvas_drawTextArray(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jcharArray text, jint index, jint count,
        jfloat x, jfloat y, jint flags, SkPaint* paint) {
    jchar* textArray = env->GetCharArrayElements(text, NULL);
    renderText(renderer, textArray + index, count, x, y, flags, paint);
    env->ReleaseCharArrayElements(text, textArray, JNI_ABORT);
}

static void android_view_GLES20Canvas_drawText(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jstring text, jint start, jint end,
        jfloat x, jfloat y, jint flags, SkPaint* paint) {
    const jchar* textArray = env->GetStringChars(text, NULL);
    renderText(renderer, textArray + start, end - start, x, y, flags, paint);
    env->ReleaseStringChars(text, textArray);
}

<<<<<<< HEAD
static void android_view_GLES20Canvas_drawTextArrayOnPath(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jcharArray text, jint index, jint count,
        SkPath* path, jfloat hOffset, jfloat vOffset, jint flags, SkPaint* paint) {
    jchar* textArray = env->GetCharArrayElements(text, NULL);
    renderTextOnPath(renderer, textArray + index, count, path,
            hOffset, vOffset, flags, paint);
    env->ReleaseCharArrayElements(text, textArray, JNI_ABORT);
}

static void android_view_GLES20Canvas_drawTextOnPath(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jstring text, jint start, jint end,
        SkPath* path, jfloat hOffset, jfloat vOffset, jint flags, SkPaint* paint) {
    const jchar* textArray = env->GetStringChars(text, NULL);
    renderTextOnPath(renderer, textArray + start, end - start, path,
            hOffset, vOffset, flags, paint);
    env->ReleaseStringChars(text, textArray);
}

=======
>>>>>>> upstream/master
static void android_view_GLES20Canvas_drawTextRunArray(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jcharArray text, jint index, jint count,
        jint contextIndex, jint contextCount, jfloat x, jfloat y, jint dirFlags,
        SkPaint* paint) {
    jchar* textArray = env->GetCharArrayElements(text, NULL);
    renderTextRun(renderer, textArray + contextIndex, index - contextIndex,
            count, contextCount, x, y, dirFlags, paint);
    env->ReleaseCharArrayElements(text, textArray, JNI_ABORT);
 }

static void android_view_GLES20Canvas_drawTextRun(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jstring text, jint start, jint end,
        jint contextStart, int contextEnd, jfloat x, jfloat y, jint dirFlags,
        SkPaint* paint) {
    const jchar* textArray = env->GetStringChars(text, NULL);
    jint count = end - start;
    jint contextCount = contextEnd - contextStart;
    renderTextRun(renderer, textArray + contextStart, start - contextStart,
            count, contextCount, x, y, dirFlags, paint);
    env->ReleaseStringChars(text, textArray);
}

<<<<<<< HEAD
static void renderPosText(OpenGLRenderer* renderer, const jchar* text, int count,
        const jfloat* positions, jint dirFlags, SkPaint* paint) {
    sp<TextLayoutValue> value = TextLayoutEngine::getInstance().getValue(paint,
            text, 0, count, count, dirFlags);
    if (value == NULL) {
        return;
    }
    const jchar* glyphs = value->getGlyphs();
    size_t glyphsCount = value->getGlyphsCount();
    if (count < int(glyphsCount)) glyphsCount = count;
    int bytesCount = glyphsCount * sizeof(jchar);

    renderer->drawPosText((const char*) glyphs, bytesCount, glyphsCount, positions, paint);
}

static void android_view_GLES20Canvas_drawPosTextArray(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jcharArray text, jint index, jint count,
        jfloatArray pos, SkPaint* paint) {
    jchar* textArray = env->GetCharArrayElements(text, NULL);
    jfloat* positions = env->GetFloatArrayElements(pos, NULL);

    renderPosText(renderer, textArray + index, count, positions, kBidi_LTR, paint);

    env->ReleaseFloatArrayElements(pos, positions, JNI_ABORT);
    env->ReleaseCharArrayElements(text, textArray, JNI_ABORT);
}

static void android_view_GLES20Canvas_drawPosText(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, jstring text, jint start, jint end,
        jfloatArray pos, SkPaint* paint) {
    const jchar* textArray = env->GetStringChars(text, NULL);
    jfloat* positions = env->GetFloatArrayElements(pos, NULL);

    renderPosText(renderer, textArray + start, end - start, positions, kBidi_LTR, paint);

    env->ReleaseFloatArrayElements(pos, positions, JNI_ABORT);
    env->ReleaseStringChars(text, textArray);
}

=======
>>>>>>> upstream/master
// ----------------------------------------------------------------------------
// Display lists
// ----------------------------------------------------------------------------

static DisplayList* android_view_GLES20Canvas_getDisplayList(JNIEnv* env,
        jobject clazz, DisplayListRenderer* renderer, DisplayList* displayList) {
    return renderer->getDisplayList(displayList);
}

static jint android_view_GLES20Canvas_getDisplayListSize(JNIEnv* env,
        jobject clazz, DisplayList* displayList) {
    return displayList->getSize();
}

<<<<<<< HEAD
static void android_view_GLES20Canvas_setDisplayListName(JNIEnv* env,
        jobject clazz, DisplayList* displayList, jstring name) {
    if (name != NULL) {
        const char* textArray = env->GetStringUTFChars(name, NULL);
        displayList->setName(textArray);
        env->ReleaseStringUTFChars(name, textArray);
    }
}

=======
>>>>>>> upstream/master
static OpenGLRenderer* android_view_GLES20Canvas_createDisplayListRenderer(JNIEnv* env,
        jobject clazz) {
    return new DisplayListRenderer;
}

static void android_view_GLES20Canvas_resetDisplayListRenderer(JNIEnv* env,
        jobject clazz, DisplayListRenderer* renderer) {
    renderer->reset();
}

static void android_view_GLES20Canvas_destroyDisplayList(JNIEnv* env,
        jobject clazz, DisplayList* displayList) {
<<<<<<< HEAD
    DisplayList::destroyDisplayListDeferred(displayList);
}

static jint android_view_GLES20Canvas_drawDisplayList(JNIEnv* env,
        jobject clazz, OpenGLRenderer* renderer, DisplayList* displayList,
        jobject dirty, jint flags) {
    android::uirenderer::Rect bounds;
    status_t status = renderer->drawDisplayList(displayList, bounds, flags);
    if (status != DrawGlInfo::kStatusDone && dirty != NULL) {
        env->CallVoidMethod(dirty, gRectClassInfo.set,
                int(bounds.left), int(bounds.top), int(bounds.right), int(bounds.bottom));
    }
    return status;
=======
    delete displayList;
}

static bool android_view_GLES20Canvas_drawDisplayList(JNIEnv* env,
        jobject clazz, OpenGLRenderer* renderer, DisplayList* displayList,
        jint width, jint height, jobject dirty) {
    android::uirenderer::Rect bounds;
    bool redraw = renderer->drawDisplayList(displayList, width, height, bounds);
    if (redraw && dirty != NULL) {
        env->CallVoidMethod(dirty, gRectClassInfo.set,
                int(bounds.left), int(bounds.top), int(bounds.right), int(bounds.bottom));
    }
    return redraw;
>>>>>>> upstream/master
}

static void android_view_GLES20Canvas_outputDisplayList(JNIEnv* env,
        jobject clazz, OpenGLRenderer* renderer, DisplayList* displayList) {
    renderer->outputDisplayList(displayList);
}

// ----------------------------------------------------------------------------
// Layers
// ----------------------------------------------------------------------------

static void android_view_GLES20Canvas_interrupt(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer) {
    renderer->interrupt();
}

static void android_view_GLES20Canvas_resume(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer) {
    renderer->resume();
}

static OpenGLRenderer* android_view_GLES20Canvas_createLayerRenderer(JNIEnv* env,
        jobject clazz, Layer* layer) {
    if (layer) {
        return new LayerRenderer(layer);
    }
    return NULL;
}

static Layer* android_view_GLES20Canvas_createTextureLayer(JNIEnv* env, jobject clazz,
        jboolean isOpaque, jintArray layerInfo) {
    Layer* layer = LayerRenderer::createTextureLayer(isOpaque);

    if (layer) {
        jint* storage = env->GetIntArrayElements(layerInfo, NULL);
        storage[0] = layer->getTexture();
        env->ReleaseIntArrayElements(layerInfo, storage, 0);
    }

    return layer;
}

static Layer* android_view_GLES20Canvas_createLayer(JNIEnv* env, jobject clazz,
        jint width, jint height, jboolean isOpaque, jintArray layerInfo) {
    Layer* layer = LayerRenderer::createLayer(width, height, isOpaque);

    if (layer) {
        jint* storage = env->GetIntArrayElements(layerInfo, NULL);
        storage[0] = layer->getWidth();
        storage[1] = layer->getHeight();
        env->ReleaseIntArrayElements(layerInfo, storage, 0);
    }

    return layer;
}

static void android_view_GLES20Canvas_resizeLayer(JNIEnv* env, jobject clazz,
        Layer* layer, jint width, jint height, jintArray layerInfo) {
    LayerRenderer::resizeLayer(layer, width, height);

    jint* storage = env->GetIntArrayElements(layerInfo, NULL);
    storage[0] = layer->getWidth();
    storage[1] = layer->getHeight();
    env->ReleaseIntArrayElements(layerInfo, storage, 0);
}

static void android_view_GLES20Canvas_updateTextureLayer(JNIEnv* env, jobject clazz,
        Layer* layer, jint width, jint height, jboolean isOpaque, jobject surface) {
    float transform[16];
    sp<SurfaceTexture> surfaceTexture(SurfaceTexture_getSurfaceTexture(env, surface));

<<<<<<< HEAD
    if (surfaceTexture->updateTexImage() == NO_ERROR) {
        surfaceTexture->getTransformMatrix(transform);
        GLenum renderTarget = surfaceTexture->getCurrentTextureTarget();

        LayerRenderer::updateTextureLayer(layer, width, height, isOpaque, renderTarget, transform);
    }
}

static void android_view_GLES20Canvas_updateRenderLayer(JNIEnv* env, jobject clazz,
        Layer* layer, OpenGLRenderer* renderer, DisplayList* displayList,
        jint left, jint top, jint right, jint bottom) {
    layer->updateDeferred(renderer, displayList, left, top, right, bottom);
=======
    surfaceTexture->updateTexImage();
    surfaceTexture->getTransformMatrix(transform);
    GLenum renderTarget = surfaceTexture->getCurrentTextureTarget();

    LayerRenderer::updateTextureLayer(layer, width, height, isOpaque, renderTarget, transform);
>>>>>>> upstream/master
}

static void android_view_GLES20Canvas_setTextureLayerTransform(JNIEnv* env, jobject clazz,
        Layer* layer, SkMatrix* matrix) {

    layer->getTransform().load(*matrix);
}

static void android_view_GLES20Canvas_destroyLayer(JNIEnv* env, jobject clazz, Layer* layer) {
    LayerRenderer::destroyLayer(layer);
}

static void android_view_GLES20Canvas_destroyLayerDeferred(JNIEnv* env,
        jobject clazz, Layer* layer) {
    LayerRenderer::destroyLayerDeferred(layer);
}

<<<<<<< HEAD
static void android_view_GLES20Canvas_flushLayer(JNIEnv* env, jobject clazz, Layer* layer) {
    LayerRenderer::flushLayer(layer);
}

=======
>>>>>>> upstream/master
static void android_view_GLES20Canvas_drawLayer(JNIEnv* env, jobject clazz,
        OpenGLRenderer* renderer, Layer* layer, jfloat x, jfloat y, SkPaint* paint) {
    renderer->drawLayer(layer, x, y, paint);
}

static jboolean android_view_GLES20Canvas_copyLayer(JNIEnv* env, jobject clazz,
        Layer* layer, SkBitmap* bitmap) {
    return LayerRenderer::copyLayer(layer, bitmap);
}

#endif // USE_OPENGL_RENDERER

// ----------------------------------------------------------------------------
// Common
// ----------------------------------------------------------------------------

static jboolean android_view_GLES20Canvas_isAvailable(JNIEnv* env, jobject clazz) {
#ifdef USE_OPENGL_RENDERER
    char prop[PROPERTY_VALUE_MAX];
    if (property_get("ro.kernel.qemu", prop, NULL) == 0) {
        // not in the emulator
        return JNI_TRUE;
    }
    // In the emulator this property will be set to 1 when hardware GLES is
    // enabled, 0 otherwise. On old emulator versions it will be undefined.
    property_get("ro.kernel.qemu.gles", prop, "0");
    return atoi(prop) == 1 ? JNI_TRUE : JNI_FALSE;
#else
    return JNI_FALSE;
#endif
}

// ----------------------------------------------------------------------------
// Logging
// ----------------------------------------------------------------------------

static void
android_app_ActivityThread_dumpGraphics(JNIEnv* env, jobject clazz, jobject javaFileDescriptor) {
#ifdef USE_OPENGL_RENDERER
    int fd = jniGetFDFromFileDescriptor(env, javaFileDescriptor);
    android::uirenderer::DisplayList::outputLogBuffer(fd);
#endif // USE_OPENGL_RENDERER
}

// ----------------------------------------------------------------------------
// JNI Glue
// ----------------------------------------------------------------------------

const char* const kClassPathName = "android/view/GLES20Canvas";

static JNINativeMethod gMethods[] = {
    { "nIsAvailable",       "()Z",             (void*) android_view_GLES20Canvas_isAvailable },

#ifdef USE_OPENGL_RENDERER
<<<<<<< HEAD
    { "nFlushCaches",       "(I)V",            (void*) android_view_GLES20Canvas_flushCaches },
    { "nInitCaches",        "()V",             (void*) android_view_GLES20Canvas_initCaches },
    { "nTerminateCaches",   "()V",             (void*) android_view_GLES20Canvas_terminateCaches },
=======
    { "nIsBackBufferPreserved", "()Z",         (void*) android_view_GLES20Canvas_isBackBufferPreserved },
    { "nPreserveBackBuffer",    "()Z",         (void*) android_view_GLES20Canvas_preserveBackBuffer },
    { "nDisableVsync",          "()V",         (void*) android_view_GLES20Canvas_disableVsync },
    { "nFlushCaches",           "(I)V",        (void*) android_view_GLES20Canvas_flushCaches },
    { "nInitCaches",            "()V",         (void*) android_view_GLES20Canvas_initCaches },
    { "nTerminateCaches",       "()V",         (void*) android_view_GLES20Canvas_terminateCaches },
>>>>>>> upstream/master

    { "nCreateRenderer",    "()I",             (void*) android_view_GLES20Canvas_createRenderer },
    { "nDestroyRenderer",   "(I)V",            (void*) android_view_GLES20Canvas_destroyRenderer },
    { "nSetViewport",       "(III)V",          (void*) android_view_GLES20Canvas_setViewport },
<<<<<<< HEAD
    { "nPrepare",           "(IZ)I",           (void*) android_view_GLES20Canvas_prepare },
    { "nPrepareDirty",      "(IIIIIZ)I",       (void*) android_view_GLES20Canvas_prepareDirty },
    { "nFinish",            "(I)V",            (void*) android_view_GLES20Canvas_finish },

    { "nGetStencilSize",    "()I",             (void*) android_view_GLES20Canvas_getStencilSize },

    { "nCallDrawGLFunction", "(II)I",          (void*) android_view_GLES20Canvas_callDrawGLFunction },
    { "nDetachFunctor",      "(II)V",          (void*) android_view_GLES20Canvas_detachFunctor },
    { "nAttachFunctor",      "(II)V",          (void*) android_view_GLES20Canvas_attachFunctor },
    { "nInvokeFunctors",     "(ILandroid/graphics/Rect;)I",
            (void*) android_view_GLES20Canvas_invokeFunctors },
=======
    { "nPrepare",           "(IZ)V",           (void*) android_view_GLES20Canvas_prepare },
    { "nPrepareDirty",      "(IIIIIZ)V",       (void*) android_view_GLES20Canvas_prepareDirty },
    { "nFinish",            "(I)V",            (void*) android_view_GLES20Canvas_finish },

    { "nCallDrawGLFunction", "(II)Z",
            (void*) android_view_GLES20Canvas_callDrawGLFunction },
>>>>>>> upstream/master

    { "nSave",              "(II)I",           (void*) android_view_GLES20Canvas_save },
    { "nRestore",           "(I)V",            (void*) android_view_GLES20Canvas_restore },
    { "nRestoreToCount",    "(II)V",           (void*) android_view_GLES20Canvas_restoreToCount },
    { "nGetSaveCount",      "(I)I",            (void*) android_view_GLES20Canvas_getSaveCount },

    { "nSaveLayer",         "(IFFFFII)I",      (void*) android_view_GLES20Canvas_saveLayer },
    { "nSaveLayer",         "(III)I",          (void*) android_view_GLES20Canvas_saveLayerClip },
    { "nSaveLayerAlpha",    "(IFFFFII)I",      (void*) android_view_GLES20Canvas_saveLayerAlpha },
    { "nSaveLayerAlpha",    "(III)I",          (void*) android_view_GLES20Canvas_saveLayerAlphaClip },

    { "nQuickReject",       "(IFFFFI)Z",       (void*) android_view_GLES20Canvas_quickReject },
    { "nClipRect",          "(IFFFFI)Z",       (void*) android_view_GLES20Canvas_clipRectF },
    { "nClipRect",          "(IIIIII)Z",       (void*) android_view_GLES20Canvas_clipRect },

    { "nTranslate",         "(IFF)V",          (void*) android_view_GLES20Canvas_translate },
    { "nRotate",            "(IF)V",           (void*) android_view_GLES20Canvas_rotate },
    { "nScale",             "(IFF)V",          (void*) android_view_GLES20Canvas_scale },
    { "nSkew",              "(IFF)V",          (void*) android_view_GLES20Canvas_skew },

    { "nSetMatrix",         "(II)V",           (void*) android_view_GLES20Canvas_setMatrix },
    { "nGetMatrix",         "(II)V",           (void*) android_view_GLES20Canvas_getMatrix },
    { "nConcatMatrix",      "(II)V",           (void*) android_view_GLES20Canvas_concatMatrix },

    { "nDrawBitmap",        "(II[BFFI)V",      (void*) android_view_GLES20Canvas_drawBitmap },
    { "nDrawBitmap",        "(II[BFFFFFFFFI)V",(void*) android_view_GLES20Canvas_drawBitmapRect },
    { "nDrawBitmap",        "(II[BII)V",       (void*) android_view_GLES20Canvas_drawBitmapMatrix },
<<<<<<< HEAD
    { "nDrawBitmap",        "(I[IIIFFIIZI)V",  (void*) android_view_GLES20Canvas_drawBitmapData },
=======
>>>>>>> upstream/master

    { "nDrawBitmapMesh",    "(II[BII[FI[III)V",(void*) android_view_GLES20Canvas_drawBitmapMesh },

    { "nDrawPatch",         "(II[B[BFFFFI)V",  (void*) android_view_GLES20Canvas_drawPatch },

    { "nDrawColor",         "(III)V",          (void*) android_view_GLES20Canvas_drawColor },
    { "nDrawRect",          "(IFFFFI)V",       (void*) android_view_GLES20Canvas_drawRect },
    { "nDrawRects",         "(III)V",          (void*) android_view_GLES20Canvas_drawRects },
    { "nDrawRoundRect",     "(IFFFFFFI)V",     (void*) android_view_GLES20Canvas_drawRoundRect },
    { "nDrawCircle",        "(IFFFI)V",        (void*) android_view_GLES20Canvas_drawCircle },
    { "nDrawOval",          "(IFFFFI)V",       (void*) android_view_GLES20Canvas_drawOval },
    { "nDrawArc",           "(IFFFFFFZI)V",    (void*) android_view_GLES20Canvas_drawArc },
    { "nDrawPoints",        "(I[FIII)V",       (void*) android_view_GLES20Canvas_drawPoints },

    { "nDrawPath",          "(III)V",          (void*) android_view_GLES20Canvas_drawPath },
    { "nDrawLines",         "(I[FIII)V",       (void*) android_view_GLES20Canvas_drawLines },

    { "nResetModifiers",    "(II)V",           (void*) android_view_GLES20Canvas_resetModifiers },
    { "nSetupShader",       "(II)V",           (void*) android_view_GLES20Canvas_setupShader },
    { "nSetupColorFilter",  "(II)V",           (void*) android_view_GLES20Canvas_setupColorFilter },
    { "nSetupShadow",       "(IFFFI)V",        (void*) android_view_GLES20Canvas_setupShadow },

<<<<<<< HEAD
    { "nSetupPaintFilter",  "(III)V",          (void*) android_view_GLES20Canvas_setupPaintFilter },
    { "nResetPaintFilter",  "(I)V",            (void*) android_view_GLES20Canvas_resetPaintFilter },

=======
>>>>>>> upstream/master
    { "nDrawText",          "(I[CIIFFII)V",    (void*) android_view_GLES20Canvas_drawTextArray },
    { "nDrawText",          "(ILjava/lang/String;IIFFII)V",
            (void*) android_view_GLES20Canvas_drawText },

<<<<<<< HEAD
    { "nDrawTextOnPath",    "(I[CIIIFFII)V",   (void*) android_view_GLES20Canvas_drawTextArrayOnPath },
    { "nDrawTextOnPath",    "(ILjava/lang/String;IIIFFII)V",
            (void*) android_view_GLES20Canvas_drawTextOnPath },

=======
>>>>>>> upstream/master
    { "nDrawTextRun",       "(I[CIIIIFFII)V",  (void*) android_view_GLES20Canvas_drawTextRunArray },
    { "nDrawTextRun",       "(ILjava/lang/String;IIIIFFII)V",
            (void*) android_view_GLES20Canvas_drawTextRun },

<<<<<<< HEAD
    { "nDrawPosText",       "(I[CII[FI)V",     (void*) android_view_GLES20Canvas_drawPosTextArray },
    { "nDrawPosText",       "(ILjava/lang/String;II[FI)V",
            (void*) android_view_GLES20Canvas_drawPosText },

=======
>>>>>>> upstream/master
    { "nGetClipBounds",     "(ILandroid/graphics/Rect;)Z",
            (void*) android_view_GLES20Canvas_getClipBounds },

    { "nGetDisplayList",         "(II)I",      (void*) android_view_GLES20Canvas_getDisplayList },
    { "nDestroyDisplayList",     "(I)V",       (void*) android_view_GLES20Canvas_destroyDisplayList },
    { "nGetDisplayListSize",     "(I)I",       (void*) android_view_GLES20Canvas_getDisplayListSize },
<<<<<<< HEAD
    { "nSetDisplayListName",     "(ILjava/lang/String;)V",
            (void*) android_view_GLES20Canvas_setDisplayListName },
    { "nDrawDisplayList",        "(IILandroid/graphics/Rect;I)I",
            (void*) android_view_GLES20Canvas_drawDisplayList },

    { "nCreateDisplayListRenderer", "()I",     (void*) android_view_GLES20Canvas_createDisplayListRenderer },
    { "nResetDisplayListRenderer",  "(I)V",    (void*) android_view_GLES20Canvas_resetDisplayListRenderer },

=======
    { "nCreateDisplayListRenderer", "()I",     (void*) android_view_GLES20Canvas_createDisplayListRenderer },
    { "nResetDisplayListRenderer", "(I)V",     (void*) android_view_GLES20Canvas_resetDisplayListRenderer },
    { "nDrawDisplayList",        "(IIIILandroid/graphics/Rect;)Z",
                                               (void*) android_view_GLES20Canvas_drawDisplayList },
>>>>>>> upstream/master
    { "nOutputDisplayList",      "(II)V",      (void*) android_view_GLES20Canvas_outputDisplayList },
    { "nInterrupt",              "(I)V",       (void*) android_view_GLES20Canvas_interrupt },
    { "nResume",                 "(I)V",       (void*) android_view_GLES20Canvas_resume },

    { "nCreateLayerRenderer",    "(I)I",       (void*) android_view_GLES20Canvas_createLayerRenderer },
    { "nCreateLayer",            "(IIZ[I)I",   (void*) android_view_GLES20Canvas_createLayer },
    { "nResizeLayer",            "(III[I)V" ,  (void*) android_view_GLES20Canvas_resizeLayer },
    { "nCreateTextureLayer",     "(Z[I)I",     (void*) android_view_GLES20Canvas_createTextureLayer },
    { "nUpdateTextureLayer",     "(IIIZLandroid/graphics/SurfaceTexture;)V",
<<<<<<< HEAD
            (void*) android_view_GLES20Canvas_updateTextureLayer },
    { "nUpdateRenderLayer",      "(IIIIIII)V", (void*) android_view_GLES20Canvas_updateRenderLayer },
    { "nDestroyLayer",           "(I)V",       (void*) android_view_GLES20Canvas_destroyLayer },
    { "nDestroyLayerDeferred",   "(I)V",       (void*) android_view_GLES20Canvas_destroyLayerDeferred },
    { "nFlushLayer",             "(I)V",       (void*) android_view_GLES20Canvas_flushLayer },
    { "nDrawLayer",              "(IIFFI)V",   (void*) android_view_GLES20Canvas_drawLayer },
    { "nCopyLayer",              "(II)Z",      (void*) android_view_GLES20Canvas_copyLayer },

    { "nSetTextureLayerTransform", "(II)V",    (void*) android_view_GLES20Canvas_setTextureLayerTransform },

=======
                                               (void*) android_view_GLES20Canvas_updateTextureLayer },
    { "nSetTextureLayerTransform", "(II)V",    (void*) android_view_GLES20Canvas_setTextureLayerTransform },
    { "nDestroyLayer",           "(I)V",       (void*) android_view_GLES20Canvas_destroyLayer },
    { "nDestroyLayerDeferred",   "(I)V",       (void*) android_view_GLES20Canvas_destroyLayerDeferred },
    { "nDrawLayer",              "(IIFFI)V",   (void*) android_view_GLES20Canvas_drawLayer },
    { "nCopyLayer",              "(II)Z",      (void*) android_view_GLES20Canvas_copyLayer },

>>>>>>> upstream/master
    { "nGetMaximumTextureWidth",  "()I",       (void*) android_view_GLES20Canvas_getMaxTextureWidth },
    { "nGetMaximumTextureHeight", "()I",       (void*) android_view_GLES20Canvas_getMaxTextureHeight },

#endif
};

static JNINativeMethod gActivityThreadMethods[] = {
    { "dumpGraphicsInfo",        "(Ljava/io/FileDescriptor;)V",
                                               (void*) android_app_ActivityThread_dumpGraphics }
};


#ifdef USE_OPENGL_RENDERER
    #define FIND_CLASS(var, className) \
            var = env->FindClass(className); \
            LOG_FATAL_IF(! var, "Unable to find class " className);

    #define GET_METHOD_ID(var, clazz, methodName, methodDescriptor) \
            var = env->GetMethodID(clazz, methodName, methodDescriptor); \
            LOG_FATAL_IF(! var, "Unable to find method " methodName);
#else
    #define FIND_CLASS(var, className)
    #define GET_METHOD_ID(var, clazz, methodName, methodDescriptor)
#endif

int register_android_view_GLES20Canvas(JNIEnv* env) {
    jclass clazz;
    FIND_CLASS(clazz, "android/graphics/Rect");
    GET_METHOD_ID(gRectClassInfo.set, clazz, "set", "(IIII)V");

    return AndroidRuntime::registerNativeMethods(env, kClassPathName, gMethods, NELEM(gMethods));
}

const char* const kActivityThreadPathName = "android/app/ActivityThread";

int register_android_app_ActivityThread(JNIEnv* env) {
    return AndroidRuntime::registerNativeMethods(env, kActivityThreadPathName,
            gActivityThreadMethods, NELEM(gActivityThreadMethods));
}

};
