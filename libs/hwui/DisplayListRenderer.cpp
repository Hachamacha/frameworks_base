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

<<<<<<< HEAD
#include <SkCamera.h>

#include <private/hwui/DrawGlInfo.h>

#include "DisplayListLogBuffer.h"
#include "DisplayListRenderer.h"
=======

#include "DisplayListLogBuffer.h"
#include "DisplayListRenderer.h"
#include <utils/String8.h>
>>>>>>> upstream/master
#include "Caches.h"

namespace android {
namespace uirenderer {

<<<<<<< HEAD
=======

>>>>>>> upstream/master
///////////////////////////////////////////////////////////////////////////////
// Display list
///////////////////////////////////////////////////////////////////////////////

const char* DisplayList::OP_NAMES[] = {
    "Save",
    "Restore",
    "RestoreToCount",
    "SaveLayer",
    "SaveLayerAlpha",
    "Translate",
    "Rotate",
    "Scale",
    "Skew",
    "SetMatrix",
    "ConcatMatrix",
    "ClipRect",
    "DrawDisplayList",
    "DrawLayer",
    "DrawBitmap",
    "DrawBitmapMatrix",
    "DrawBitmapRect",
<<<<<<< HEAD
    "DrawBitmapData",
=======
>>>>>>> upstream/master
    "DrawBitmapMesh",
    "DrawPatch",
    "DrawColor",
    "DrawRect",
    "DrawRoundRect",
    "DrawCircle",
    "DrawOval",
    "DrawArc",
    "DrawPath",
    "DrawLines",
    "DrawPoints",
    "DrawText",
<<<<<<< HEAD
    "DrawTextOnPath",
    "DrawPosText",
=======
>>>>>>> upstream/master
    "ResetShader",
    "SetupShader",
    "ResetColorFilter",
    "SetupColorFilter",
    "ResetShadow",
    "SetupShadow",
<<<<<<< HEAD
    "ResetPaintFilter",
    "SetupPaintFilter",
=======
>>>>>>> upstream/master
    "DrawGLFunction"
};

void DisplayList::outputLogBuffer(int fd) {
    DisplayListLogBuffer& logBuffer = DisplayListLogBuffer::getInstance();
    if (logBuffer.isEmpty()) {
        return;
    }

    FILE *file = fdopen(fd, "a");

    fprintf(file, "\nRecent DisplayList operations\n");
    logBuffer.outputCommands(file, OP_NAMES);

    String8 cachesLog;
    Caches::getInstance().dumpMemoryUsage(cachesLog);
    fprintf(file, "\nCaches:\n%s", cachesLog.string());
    fprintf(file, "\n");

    fflush(file);
}

<<<<<<< HEAD
DisplayList::DisplayList(const DisplayListRenderer& recorder) :
    mTransformMatrix(NULL), mTransformCamera(NULL), mTransformMatrix3D(NULL),
    mStaticMatrix(NULL), mAnimationMatrix(NULL) {

=======
DisplayList::DisplayList(const DisplayListRenderer& recorder) {
>>>>>>> upstream/master
    initFromDisplayListRenderer(recorder);
}

DisplayList::~DisplayList() {
    clearResources();
}

<<<<<<< HEAD
void DisplayList::initProperties() {
    mLeft = 0;
    mTop = 0;
    mRight = 0;
    mBottom = 0;
    mClipChildren = true;
    mAlpha = 1;
    mMultipliedAlpha = 255;
    mHasOverlappingRendering = true;
    mTranslationX = 0;
    mTranslationY = 0;
    mRotation = 0;
    mRotationX = 0;
    mRotationY= 0;
    mScaleX = 1;
    mScaleY = 1;
    mPivotX = 0;
    mPivotY = 0;
    mCameraDistance = 0;
    mMatrixDirty = false;
    mMatrixFlags = 0;
    mPrevWidth = -1;
    mPrevHeight = -1;
    mWidth = 0;
    mHeight = 0;
    mPivotExplicitlySet = false;
    mCaching = false;
}

void DisplayList::destroyDisplayListDeferred(DisplayList* displayList) {
    if (displayList) {
        DISPLAY_LIST_LOGD("Deferring display list destruction");
        Caches::getInstance().deleteDisplayListDeferred(displayList);
    }
}

void DisplayList::clearResources() {
    sk_free((void*) mReader.base());

    delete mTransformMatrix;
    delete mTransformCamera;
    delete mTransformMatrix3D;
    delete mStaticMatrix;
    delete mAnimationMatrix;
    mTransformMatrix = NULL;
    mTransformCamera = NULL;
    mTransformMatrix3D = NULL;
    mStaticMatrix = NULL;
    mAnimationMatrix = NULL;

=======
void DisplayList::clearResources() {
    sk_free((void*) mReader.base());

>>>>>>> upstream/master
    Caches& caches = Caches::getInstance();

    for (size_t i = 0; i < mBitmapResources.size(); i++) {
        caches.resourceCache.decrementRefcount(mBitmapResources.itemAt(i));
    }
    mBitmapResources.clear();

<<<<<<< HEAD
    for (size_t i = 0; i < mOwnedBitmapResources.size(); i++) {
        SkBitmap* bitmap = mOwnedBitmapResources.itemAt(i);
        caches.resourceCache.decrementRefcount(bitmap);
        caches.resourceCache.destructor(bitmap);
    }
    mOwnedBitmapResources.clear();

=======
>>>>>>> upstream/master
    for (size_t i = 0; i < mFilterResources.size(); i++) {
        caches.resourceCache.decrementRefcount(mFilterResources.itemAt(i));
    }
    mFilterResources.clear();

    for (size_t i = 0; i < mShaders.size(); i++) {
        caches.resourceCache.decrementRefcount(mShaders.itemAt(i));
        caches.resourceCache.destructor(mShaders.itemAt(i));
    }
    mShaders.clear();

    for (size_t i = 0; i < mPaints.size(); i++) {
        delete mPaints.itemAt(i);
    }
    mPaints.clear();

    for (size_t i = 0; i < mPaths.size(); i++) {
        SkPath* path = mPaths.itemAt(i);
        caches.pathCache.remove(path);
        delete path;
    }
    mPaths.clear();

<<<<<<< HEAD
    for (size_t i = 0; i < mSourcePaths.size(); i++) {
        caches.resourceCache.decrementRefcount(mSourcePaths.itemAt(i));
    }
    mSourcePaths.clear();

=======
>>>>>>> upstream/master
    for (size_t i = 0; i < mMatrices.size(); i++) {
        delete mMatrices.itemAt(i);
    }
    mMatrices.clear();
}

void DisplayList::initFromDisplayListRenderer(const DisplayListRenderer& recorder, bool reusing) {
    const SkWriter32& writer = recorder.writeStream();
    init();

    if (writer.size() == 0) {
        return;
    }

    if (reusing) {
        // re-using display list - clear out previous allocations
        clearResources();
    }
<<<<<<< HEAD
    initProperties();
=======
>>>>>>> upstream/master

    mSize = writer.size();
    void* buffer = sk_malloc_throw(mSize);
    writer.flatten(buffer);
    mReader.setMemory(buffer, mSize);

    Caches& caches = Caches::getInstance();

<<<<<<< HEAD
    const Vector<SkBitmap*>& bitmapResources = recorder.getBitmapResources();
=======
    const Vector<SkBitmap*> &bitmapResources = recorder.getBitmapResources();
>>>>>>> upstream/master
    for (size_t i = 0; i < bitmapResources.size(); i++) {
        SkBitmap* resource = bitmapResources.itemAt(i);
        mBitmapResources.add(resource);
        caches.resourceCache.incrementRefcount(resource);
    }

<<<<<<< HEAD
    const Vector<SkBitmap*> &ownedBitmapResources = recorder.getOwnedBitmapResources();
    for (size_t i = 0; i < ownedBitmapResources.size(); i++) {
        SkBitmap* resource = ownedBitmapResources.itemAt(i);
        mOwnedBitmapResources.add(resource);
        caches.resourceCache.incrementRefcount(resource);
    }

    const Vector<SkiaColorFilter*>& filterResources = recorder.getFilterResources();
=======
    const Vector<SkiaColorFilter*> &filterResources = recorder.getFilterResources();
>>>>>>> upstream/master
    for (size_t i = 0; i < filterResources.size(); i++) {
        SkiaColorFilter* resource = filterResources.itemAt(i);
        mFilterResources.add(resource);
        caches.resourceCache.incrementRefcount(resource);
    }

<<<<<<< HEAD
    const Vector<SkiaShader*>& shaders = recorder.getShaders();
=======
    const Vector<SkiaShader*> &shaders = recorder.getShaders();
>>>>>>> upstream/master
    for (size_t i = 0; i < shaders.size(); i++) {
        SkiaShader* resource = shaders.itemAt(i);
        mShaders.add(resource);
        caches.resourceCache.incrementRefcount(resource);
    }

<<<<<<< HEAD
    const Vector<SkPaint*>& paints = recorder.getPaints();
=======
    const Vector<SkPaint*> &paints = recorder.getPaints();
>>>>>>> upstream/master
    for (size_t i = 0; i < paints.size(); i++) {
        mPaints.add(paints.itemAt(i));
    }

<<<<<<< HEAD
    const Vector<SkPath*>& paths = recorder.getPaths();
=======
    const Vector<SkPath*> &paths = recorder.getPaths();
>>>>>>> upstream/master
    for (size_t i = 0; i < paths.size(); i++) {
        mPaths.add(paths.itemAt(i));
    }

<<<<<<< HEAD
    const SortedVector<SkPath*>& sourcePaths = recorder.getSourcePaths();
    for (size_t i = 0; i < sourcePaths.size(); i++) {
        mSourcePaths.add(sourcePaths.itemAt(i));
        caches.resourceCache.incrementRefcount(sourcePaths.itemAt(i));
    }

    const Vector<SkMatrix*>& matrices = recorder.getMatrices();
=======
    const Vector<SkMatrix*> &matrices = recorder.getMatrices();
>>>>>>> upstream/master
    for (size_t i = 0; i < matrices.size(); i++) {
        mMatrices.add(matrices.itemAt(i));
    }
}

void DisplayList::init() {
    mSize = 0;
    mIsRenderable = true;
}

size_t DisplayList::getSize() {
    return mSize;
}

/**
 * This function is a simplified version of replay(), where we simply retrieve and log the
 * display list. This function should remain in sync with the replay() function.
 */
void DisplayList::output(OpenGLRenderer& renderer, uint32_t level) {
    TextContainer text;

    uint32_t count = (level + 1) * 2;
    char indent[count + 1];
    for (uint32_t i = 0; i < count; i++) {
        indent[i] = ' ';
    }
    indent[count] = '\0';
<<<<<<< HEAD
    ALOGD("%sStart display list (%p, %s, render=%d)", (char*) indent + 2, this,
            mName.string(), isRenderable());

    ALOGD("%s%s %d", indent, "Save", SkCanvas::kMatrix_SaveFlag | SkCanvas::kClip_SaveFlag);
    int saveCount = renderer.getSaveCount() - 1;

    outputViewProperties(renderer, (char*) indent);
=======
    LOGD("%sStart display list (%p)", (char*) indent + 2, this);

    int saveCount = renderer.getSaveCount() - 1;

>>>>>>> upstream/master
    mReader.rewind();

    while (!mReader.eof()) {
        int op = mReader.readInt();
<<<<<<< HEAD
        if (op & OP_MAY_BE_SKIPPED_MASK) {
            int skip = mReader.readInt();
            ALOGD("%sSkip %d", (char*) indent, skip);
            op &= ~OP_MAY_BE_SKIPPED_MASK;
        }
=======
>>>>>>> upstream/master

        switch (op) {
            case DrawGLFunction: {
                Functor *functor = (Functor *) getInt();
<<<<<<< HEAD
                ALOGD("%s%s %p", (char*) indent, OP_NAMES[op], functor);
=======
                LOGD("%s%s %p", (char*) indent, OP_NAMES[op], functor);
>>>>>>> upstream/master
            }
            break;
            case Save: {
                int rendererNum = getInt();
<<<<<<< HEAD
                ALOGD("%s%s %d", (char*) indent, OP_NAMES[op], rendererNum);
            }
            break;
            case Restore: {
                ALOGD("%s%s", (char*) indent, OP_NAMES[op]);
=======
                LOGD("%s%s %d", (char*) indent, OP_NAMES[op], rendererNum);
            }
            break;
            case Restore: {
                LOGD("%s%s", (char*) indent, OP_NAMES[op]);
>>>>>>> upstream/master
            }
            break;
            case RestoreToCount: {
                int restoreCount = saveCount + getInt();
<<<<<<< HEAD
                ALOGD("%s%s %d", (char*) indent, OP_NAMES[op], restoreCount);
=======
                LOGD("%s%s %d", (char*) indent, OP_NAMES[op], restoreCount);
>>>>>>> upstream/master
            }
            break;
            case SaveLayer: {
                float f1 = getFloat();
                float f2 = getFloat();
                float f3 = getFloat();
                float f4 = getFloat();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                int flags = getInt();
                ALOGD("%s%s %.2f, %.2f, %.2f, %.2f, %p, 0x%x", (char*) indent,
                        OP_NAMES[op], f1, f2, f3, f4, paint, flags);
=======
                SkPaint* paint = getPaint();
                int flags = getInt();
                LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %p, 0x%x", (char*) indent,
                    OP_NAMES[op], f1, f2, f3, f4, paint, flags);
>>>>>>> upstream/master
            }
            break;
            case SaveLayerAlpha: {
                float f1 = getFloat();
                float f2 = getFloat();
                float f3 = getFloat();
                float f4 = getFloat();
                int alpha = getInt();
                int flags = getInt();
<<<<<<< HEAD
                ALOGD("%s%s %.2f, %.2f, %.2f, %.2f, %d, 0x%x", (char*) indent,
                        OP_NAMES[op], f1, f2, f3, f4, alpha, flags);
=======
                LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %d, 0x%x", (char*) indent,
                    OP_NAMES[op], f1, f2, f3, f4, alpha, flags);
>>>>>>> upstream/master
            }
            break;
            case Translate: {
                float f1 = getFloat();
                float f2 = getFloat();
<<<<<<< HEAD
                ALOGD("%s%s %.2f, %.2f", (char*) indent, OP_NAMES[op], f1, f2);
=======
                LOGD("%s%s %.2f, %.2f", (char*) indent, OP_NAMES[op], f1, f2);
>>>>>>> upstream/master
            }
            break;
            case Rotate: {
                float rotation = getFloat();
<<<<<<< HEAD
                ALOGD("%s%s %.2f", (char*) indent, OP_NAMES[op], rotation);
=======
                LOGD("%s%s %.2f", (char*) indent, OP_NAMES[op], rotation);
>>>>>>> upstream/master
            }
            break;
            case Scale: {
                float sx = getFloat();
                float sy = getFloat();
<<<<<<< HEAD
                ALOGD("%s%s %.2f, %.2f", (char*) indent, OP_NAMES[op], sx, sy);
=======
                LOGD("%s%s %.2f, %.2f", (char*) indent, OP_NAMES[op], sx, sy);
>>>>>>> upstream/master
            }
            break;
            case Skew: {
                float sx = getFloat();
                float sy = getFloat();
<<<<<<< HEAD
                ALOGD("%s%s %.2f, %.2f", (char*) indent, OP_NAMES[op], sx, sy);
=======
                LOGD("%s%s %.2f, %.2f", (char*) indent, OP_NAMES[op], sx, sy);
>>>>>>> upstream/master
            }
            break;
            case SetMatrix: {
                SkMatrix* matrix = getMatrix();
<<<<<<< HEAD
                ALOGD("%s%s %p", (char*) indent, OP_NAMES[op], matrix);
=======
                LOGD("%s%s %p", (char*) indent, OP_NAMES[op], matrix);
>>>>>>> upstream/master
            }
            break;
            case ConcatMatrix: {
                SkMatrix* matrix = getMatrix();
<<<<<<< HEAD
                ALOGD("%s%s new concat %p: [%f, %f, %f]   [%f, %f, %f]   [%f, %f, %f]",
                        (char*) indent, OP_NAMES[op], matrix, matrix->get(0), matrix->get(1),
                        matrix->get(2), matrix->get(3), matrix->get(4), matrix->get(5),
                        matrix->get(6), matrix->get(7), matrix->get(8));
=======
                LOGD("%s%s %p", (char*) indent, OP_NAMES[op], matrix);
>>>>>>> upstream/master
            }
            break;
            case ClipRect: {
                float f1 = getFloat();
                float f2 = getFloat();
                float f3 = getFloat();
                float f4 = getFloat();
                int regionOp = getInt();
<<<<<<< HEAD
                ALOGD("%s%s %.2f, %.2f, %.2f, %.2f, %d", (char*) indent, OP_NAMES[op],
                        f1, f2, f3, f4, regionOp);
=======
                LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %d", (char*) indent, OP_NAMES[op],
                    f1, f2, f3, f4, regionOp);
>>>>>>> upstream/master
            }
            break;
            case DrawDisplayList: {
                DisplayList* displayList = getDisplayList();
<<<<<<< HEAD
                int32_t flags = getInt();
                ALOGD("%s%s %p, %dx%d, 0x%x %d", (char*) indent, OP_NAMES[op],
                        displayList, mWidth, mHeight, flags, level + 1);
=======
                uint32_t width = getUInt();
                uint32_t height = getUInt();
                LOGD("%s%s %p, %dx%d, %d", (char*) indent, OP_NAMES[op],
                    displayList, width, height, level + 1);
>>>>>>> upstream/master
                renderer.outputDisplayList(displayList, level + 1);
            }
            break;
            case DrawLayer: {
                Layer* layer = (Layer*) getInt();
                float x = getFloat();
                float y = getFloat();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                ALOGD("%s%s %p, %.2f, %.2f, %p", (char*) indent, OP_NAMES[op],
                        layer, x, y, paint);
=======
                SkPaint* paint = getPaint();
                LOGD("%s%s %p, %.2f, %.2f, %p", (char*) indent, OP_NAMES[op],
                    layer, x, y, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawBitmap: {
                SkBitmap* bitmap = getBitmap();
                float x = getFloat();
                float y = getFloat();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                ALOGD("%s%s %p, %.2f, %.2f, %p", (char*) indent, OP_NAMES[op],
                        bitmap, x, y, paint);
=======
                SkPaint* paint = getPaint();
                LOGD("%s%s %p, %.2f, %.2f, %p", (char*) indent, OP_NAMES[op],
                    bitmap, x, y, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawBitmapMatrix: {
                SkBitmap* bitmap = getBitmap();
                SkMatrix* matrix = getMatrix();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                ALOGD("%s%s %p, %p, %p", (char*) indent, OP_NAMES[op],
                        bitmap, matrix, paint);
=======
                SkPaint* paint = getPaint();
                LOGD("%s%s %p, %p, %p", (char*) indent, OP_NAMES[op],
                    bitmap, matrix, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawBitmapRect: {
                SkBitmap* bitmap = getBitmap();
                float f1 = getFloat();
                float f2 = getFloat();
                float f3 = getFloat();
                float f4 = getFloat();
                float f5 = getFloat();
                float f6 = getFloat();
                float f7 = getFloat();
                float f8 = getFloat();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                ALOGD("%s%s %p, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %p",
                        (char*) indent, OP_NAMES[op], bitmap, f1, f2, f3, f4, f5, f6, f7, f8, paint);
            }
            break;
            case DrawBitmapData: {
                SkBitmap* bitmap = getBitmapData();
                float x = getFloat();
                float y = getFloat();
                SkPaint* paint = getPaint(renderer);
                ALOGD("%s%s %.2f, %.2f, %p", (char*) indent, OP_NAMES[op], x, y, paint);
=======
                SkPaint* paint = getPaint();
                LOGD("%s%s %p, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %p",
                    (char*) indent, OP_NAMES[op], bitmap, f1, f2, f3, f4, f5, f6, f7, f8, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawBitmapMesh: {
                int verticesCount = 0;
                uint32_t colorsCount = 0;
                SkBitmap* bitmap = getBitmap();
                uint32_t meshWidth = getInt();
                uint32_t meshHeight = getInt();
                float* vertices = getFloats(verticesCount);
                bool hasColors = getInt();
                int* colors = hasColors ? getInts(colorsCount) : NULL;
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                ALOGD("%s%s", (char*) indent, OP_NAMES[op]);
=======
                SkPaint* paint = getPaint();
                LOGD("%s%s", (char*) indent, OP_NAMES[op]);
>>>>>>> upstream/master
            }
            break;
            case DrawPatch: {
                int32_t* xDivs = NULL;
                int32_t* yDivs = NULL;
                uint32_t* colors = NULL;
                uint32_t xDivsCount = 0;
                uint32_t yDivsCount = 0;
                int8_t numColors = 0;
                SkBitmap* bitmap = getBitmap();
                xDivs = getInts(xDivsCount);
                yDivs = getInts(yDivsCount);
                colors = getUInts(numColors);
                float left = getFloat();
                float top = getFloat();
                float right = getFloat();
                float bottom = getFloat();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                ALOGD("%s%s %.2f, %.2f, %.2f, %.2f", (char*) indent, OP_NAMES[op],
=======
                SkPaint* paint = getPaint();
                LOGD("%s%s %.2f, %.2f, %.2f, %.2f", (char*) indent, OP_NAMES[op],
>>>>>>> upstream/master
                        left, top, right, bottom);
            }
            break;
            case DrawColor: {
                int color = getInt();
                int xferMode = getInt();
<<<<<<< HEAD
                ALOGD("%s%s 0x%x %d", (char*) indent, OP_NAMES[op], color, xferMode);
=======
                LOGD("%s%s 0x%x %d", (char*) indent, OP_NAMES[op], color, xferMode);
>>>>>>> upstream/master
            }
            break;
            case DrawRect: {
                float f1 = getFloat();
                float f2 = getFloat();
                float f3 = getFloat();
                float f4 = getFloat();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                ALOGD("%s%s %.2f, %.2f, %.2f, %.2f, %p", (char*) indent, OP_NAMES[op],
                        f1, f2, f3, f4, paint);
=======
                SkPaint* paint = getPaint();
                LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %p", (char*) indent, OP_NAMES[op],
                    f1, f2, f3, f4, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawRoundRect: {
                float f1 = getFloat();
                float f2 = getFloat();
                float f3 = getFloat();
                float f4 = getFloat();
                float f5 = getFloat();
                float f6 = getFloat();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                ALOGD("%s%s %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %p",
                        (char*) indent, OP_NAMES[op], f1, f2, f3, f4, f5, f6, paint);
=======
                SkPaint* paint = getPaint();
                LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %p",
                    (char*) indent, OP_NAMES[op], f1, f2, f3, f4, f5, f6, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawCircle: {
                float f1 = getFloat();
                float f2 = getFloat();
                float f3 = getFloat();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                ALOGD("%s%s %.2f, %.2f, %.2f, %p",
                        (char*) indent, OP_NAMES[op], f1, f2, f3, paint);
=======
                SkPaint* paint = getPaint();
                LOGD("%s%s %.2f, %.2f, %.2f, %p",
                    (char*) indent, OP_NAMES[op], f1, f2, f3, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawOval: {
                float f1 = getFloat();
                float f2 = getFloat();
                float f3 = getFloat();
                float f4 = getFloat();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                ALOGD("%s%s %.2f, %.2f, %.2f, %.2f, %p",
                        (char*) indent, OP_NAMES[op], f1, f2, f3, f4, paint);
=======
                SkPaint* paint = getPaint();
                LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %p",
                    (char*) indent, OP_NAMES[op], f1, f2, f3, f4, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawArc: {
                float f1 = getFloat();
                float f2 = getFloat();
                float f3 = getFloat();
                float f4 = getFloat();
                float f5 = getFloat();
                float f6 = getFloat();
                int i1 = getInt();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                ALOGD("%s%s %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %d, %p",
                        (char*) indent, OP_NAMES[op], f1, f2, f3, f4, f5, f6, i1, paint);
=======
                SkPaint* paint = getPaint();
                LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %d, %p",
                    (char*) indent, OP_NAMES[op], f1, f2, f3, f4, f5, f6, i1, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawPath: {
                SkPath* path = getPath();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                ALOGD("%s%s %p, %p", (char*) indent, OP_NAMES[op], path, paint);
=======
                SkPaint* paint = getPaint();
                LOGD("%s%s %p, %p", (char*) indent, OP_NAMES[op], path, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawLines: {
                int count = 0;
                float* points = getFloats(count);
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                ALOGD("%s%s", (char*) indent, OP_NAMES[op]);
=======
                SkPaint* paint = getPaint();
                LOGD("%s%s", (char*) indent, OP_NAMES[op]);
>>>>>>> upstream/master
            }
            break;
            case DrawPoints: {
                int count = 0;
                float* points = getFloats(count);
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                ALOGD("%s%s", (char*) indent, OP_NAMES[op]);
=======
                SkPaint* paint = getPaint();
                LOGD("%s%s", (char*) indent, OP_NAMES[op]);
>>>>>>> upstream/master
            }
            break;
            case DrawText: {
                getText(&text);
<<<<<<< HEAD
                int32_t count = getInt();
                float x = getFloat();
                float y = getFloat();
                SkPaint* paint = getPaint(renderer);
                float length = getFloat();
                ALOGD("%s%s %s, %d, %d, %.2f, %.2f, %p, %.2f", (char*) indent, OP_NAMES[op],
                        text.text(), text.length(), count, x, y, paint, length);
            }
            break;
            case DrawTextOnPath: {
                getText(&text);
                int32_t count = getInt();
                SkPath* path = getPath();
                float hOffset = getFloat();
                float vOffset = getFloat();
                SkPaint* paint = getPaint(renderer);
                ALOGD("%s%s %s, %d, %d, %p", (char*) indent, OP_NAMES[op],
                    text.text(), text.length(), count, paint);
            }
            break;
            case DrawPosText: {
                getText(&text);
                int count = getInt();
                int positionsCount = 0;
                float* positions = getFloats(positionsCount);
                SkPaint* paint = getPaint(renderer);
                ALOGD("%s%s %s, %d, %d, %p", (char*) indent, OP_NAMES[op],
                        text.text(), text.length(), count, paint);
            }
            case ResetShader: {
                ALOGD("%s%s", (char*) indent, OP_NAMES[op]);
=======
                int count = getInt();
                float x = getFloat();
                float y = getFloat();
                SkPaint* paint = getPaint();
                LOGD("%s%s %s, %d, %d, %.2f, %.2f, %p", (char*) indent, OP_NAMES[op],
                    text.text(), text.length(), count, x, y, paint);
            }
            break;
            case ResetShader: {
                LOGD("%s%s", (char*) indent, OP_NAMES[op]);
>>>>>>> upstream/master
            }
            break;
            case SetupShader: {
                SkiaShader* shader = getShader();
<<<<<<< HEAD
                ALOGD("%s%s %p", (char*) indent, OP_NAMES[op], shader);
            }
            break;
            case ResetColorFilter: {
                ALOGD("%s%s", (char*) indent, OP_NAMES[op]);
=======
                LOGD("%s%s %p", (char*) indent, OP_NAMES[op], shader);
            }
            break;
            case ResetColorFilter: {
                LOGD("%s%s", (char*) indent, OP_NAMES[op]);
>>>>>>> upstream/master
            }
            break;
            case SetupColorFilter: {
                SkiaColorFilter *colorFilter = getColorFilter();
<<<<<<< HEAD
                ALOGD("%s%s %p", (char*) indent, OP_NAMES[op], colorFilter);
            }
            break;
            case ResetShadow: {
                ALOGD("%s%s", (char*) indent, OP_NAMES[op]);
=======
                LOGD("%s%s %p", (char*) indent, OP_NAMES[op], colorFilter);
            }
            break;
            case ResetShadow: {
                LOGD("%s%s", (char*) indent, OP_NAMES[op]);
>>>>>>> upstream/master
            }
            break;
            case SetupShadow: {
                float radius = getFloat();
                float dx = getFloat();
                float dy = getFloat();
                int color = getInt();
<<<<<<< HEAD
                ALOGD("%s%s %.2f, %.2f, %.2f, 0x%x", (char*) indent, OP_NAMES[op],
                        radius, dx, dy, color);
            }
            break;
            case ResetPaintFilter: {
                ALOGD("%s%s", (char*) indent, OP_NAMES[op]);
            }
            break;
            case SetupPaintFilter: {
                int clearBits = getInt();
                int setBits = getInt();
                ALOGD("%s%s 0x%x, 0x%x", (char*) indent, OP_NAMES[op], clearBits, setBits);
            }
            break;
            default:
                ALOGD("Display List error: op not handled: %s%s",
                        (char*) indent, OP_NAMES[op]);
                break;
        }
    }
    ALOGD("%sDone (%p, %s)", (char*) indent + 2, this, mName.string());
}

void DisplayList::updateMatrix() {
    if (mMatrixDirty) {
        if (!mTransformMatrix) {
            mTransformMatrix = new SkMatrix();
        }
        if (mMatrixFlags == 0 || mMatrixFlags == TRANSLATION) {
            mTransformMatrix->reset();
        } else {
            if (!mPivotExplicitlySet) {
                if (mWidth != mPrevWidth || mHeight != mPrevHeight) {
                    mPrevWidth = mWidth;
                    mPrevHeight = mHeight;
                    mPivotX = mPrevWidth / 2;
                    mPivotY = mPrevHeight / 2;
                }
            }
            if ((mMatrixFlags & ROTATION_3D) == 0) {
                mTransformMatrix->setTranslate(mTranslationX, mTranslationY);
                mTransformMatrix->preRotate(mRotation, mPivotX, mPivotY);
                mTransformMatrix->preScale(mScaleX, mScaleY, mPivotX, mPivotY);
            } else {
                if (!mTransformCamera) {
                    mTransformCamera = new Sk3DView();
                    mTransformMatrix3D = new SkMatrix();
                }
                mTransformMatrix->reset();
                mTransformCamera->save();
                mTransformMatrix->preScale(mScaleX, mScaleY, mPivotX, mPivotY);
                mTransformCamera->rotateX(mRotationX);
                mTransformCamera->rotateY(mRotationY);
                mTransformCamera->rotateZ(-mRotation);
                mTransformCamera->getMatrix(mTransformMatrix3D);
                mTransformMatrix3D->preTranslate(-mPivotX, -mPivotY);
                mTransformMatrix3D->postTranslate(mPivotX + mTranslationX,
                        mPivotY + mTranslationY);
                mTransformMatrix->postConcat(*mTransformMatrix3D);
                mTransformCamera->restore();
            }
        }
        mMatrixDirty = false;
    }
}

void DisplayList::outputViewProperties(OpenGLRenderer& renderer, char* indent) {
    updateMatrix();
    if (mLeft != 0 || mTop != 0) {
        ALOGD("%s%s %d, %d", indent, "Translate (left, top)", mLeft, mTop);
    }
    if (mStaticMatrix) {
        ALOGD("%s%s %p: [%.2f, %.2f, %.2f] [%.2f, %.2f, %.2f] [%.2f, %.2f, %.2f]",
                indent, "ConcatMatrix (static)", mStaticMatrix,
                mStaticMatrix->get(0), mStaticMatrix->get(1),
                mStaticMatrix->get(2), mStaticMatrix->get(3),
                mStaticMatrix->get(4), mStaticMatrix->get(5),
                mStaticMatrix->get(6), mStaticMatrix->get(7),
                mStaticMatrix->get(8));
    }
    if (mAnimationMatrix) {
        ALOGD("%s%s %p: [%.2f, %.2f, %.2f] [%.2f, %.2f, %.2f] [%.2f, %.2f, %.2f]",
                indent, "ConcatMatrix (animation)", mAnimationMatrix,
                mAnimationMatrix->get(0), mAnimationMatrix->get(1),
                mAnimationMatrix->get(2), mAnimationMatrix->get(3),
                mAnimationMatrix->get(4), mAnimationMatrix->get(5),
                mAnimationMatrix->get(6), mAnimationMatrix->get(7),
                mAnimationMatrix->get(8));
    }
    if (mMatrixFlags != 0) {
        if (mMatrixFlags == TRANSLATION) {
            ALOGD("%s%s %f, %f", indent, "Translate", mTranslationX, mTranslationY);
        } else {
            ALOGD("%s%s %p: [%.2f, %.2f, %.2f] [%.2f, %.2f, %.2f] [%.2f, %.2f, %.2f]",
                    indent, "ConcatMatrix", mTransformMatrix,
                    mTransformMatrix->get(0), mTransformMatrix->get(1),
                    mTransformMatrix->get(2), mTransformMatrix->get(3),
                    mTransformMatrix->get(4), mTransformMatrix->get(5),
                    mTransformMatrix->get(6), mTransformMatrix->get(7),
                    mTransformMatrix->get(8));
        }
    }
    if (mAlpha < 1 && !mCaching) {
        // TODO: should be able to store the size of a DL at record time and not
        // have to pass it into this call. In fact, this information might be in the
        // location/size info that we store with the new native transform data.
        int flags = SkCanvas::kHasAlphaLayer_SaveFlag;
        if (mClipChildren) {
            flags |= SkCanvas::kClipToLayer_SaveFlag;
        }
        ALOGD("%s%s %.2f, %.2f, %.2f, %.2f, %d, 0x%x", indent, "SaveLayerAlpha",
                (float) 0, (float) 0, (float) mRight - mLeft, (float) mBottom - mTop,
                mMultipliedAlpha, flags);
    }
    if (mClipChildren) {
        ALOGD("%s%s %.2f, %.2f, %.2f, %.2f", indent, "ClipRect", 0.0f, 0.0f,
                (float) mRight - mLeft, (float) mBottom - mTop);
    }
}

void DisplayList::setViewProperties(OpenGLRenderer& renderer, uint32_t level) {
#if DEBUG_DISPLAY_LIST
        uint32_t count = (level + 1) * 2;
        char indent[count + 1];
        for (uint32_t i = 0; i < count; i++) {
            indent[i] = ' ';
        }
        indent[count] = '\0';
#endif
    updateMatrix();
    if (mLeft != 0 || mTop != 0) {
        DISPLAY_LIST_LOGD("%s%s %d, %d", indent, "Translate (left, top)", mLeft, mTop);
        renderer.translate(mLeft, mTop);
    }
    if (mStaticMatrix) {
        DISPLAY_LIST_LOGD(
                "%s%s %p: [%.2f, %.2f, %.2f] [%.2f, %.2f, %.2f] [%.2f, %.2f, %.2f]",
                indent, "ConcatMatrix (static)", mStaticMatrix,
                mStaticMatrix->get(0), mStaticMatrix->get(1),
                mStaticMatrix->get(2), mStaticMatrix->get(3),
                mStaticMatrix->get(4), mStaticMatrix->get(5),
                mStaticMatrix->get(6), mStaticMatrix->get(7),
                mStaticMatrix->get(8));
        renderer.concatMatrix(mStaticMatrix);
    } else if (mAnimationMatrix) {
        DISPLAY_LIST_LOGD(
                "%s%s %p: [%.2f, %.2f, %.2f] [%.2f, %.2f, %.2f] [%.2f, %.2f, %.2f]",
                indent, "ConcatMatrix (animation)", mAnimationMatrix,
                mAnimationMatrix->get(0), mAnimationMatrix->get(1),
                mAnimationMatrix->get(2), mAnimationMatrix->get(3),
                mAnimationMatrix->get(4), mAnimationMatrix->get(5),
                mAnimationMatrix->get(6), mAnimationMatrix->get(7),
                mAnimationMatrix->get(8));
        renderer.concatMatrix(mAnimationMatrix);
    }
    if (mMatrixFlags != 0) {
        if (mMatrixFlags == TRANSLATION) {
            DISPLAY_LIST_LOGD("%s%s %f, %f", indent, "Translate", mTranslationX, mTranslationY);
            renderer.translate(mTranslationX, mTranslationY);
        } else {
            DISPLAY_LIST_LOGD(
                    "%s%s %p: [%.2f, %.2f, %.2f] [%.2f, %.2f, %.2f] [%.2f, %.2f, %.2f]",
                    indent, "ConcatMatrix", mTransformMatrix,
                    mTransformMatrix->get(0), mTransformMatrix->get(1),
                    mTransformMatrix->get(2), mTransformMatrix->get(3),
                    mTransformMatrix->get(4), mTransformMatrix->get(5),
                    mTransformMatrix->get(6), mTransformMatrix->get(7),
                    mTransformMatrix->get(8));
            renderer.concatMatrix(mTransformMatrix);
        }
    }
    if (mAlpha < 1 && !mCaching) {
        if (!mHasOverlappingRendering) {
            DISPLAY_LIST_LOGD("%s%s %.2f", indent, "SetAlpha", mAlpha);
            renderer.setAlpha(mAlpha);
        } else {
            // TODO: should be able to store the size of a DL at record time and not
            // have to pass it into this call. In fact, this information might be in the
            // location/size info that we store with the new native transform data.
            int flags = SkCanvas::kHasAlphaLayer_SaveFlag;
            if (mClipChildren) {
                flags |= SkCanvas::kClipToLayer_SaveFlag;
            }
            DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %d, 0x%x", indent, "SaveLayerAlpha",
                    (float) 0, (float) 0, (float) mRight - mLeft, (float) mBottom - mTop,
                    mMultipliedAlpha, flags);
            renderer.saveLayerAlpha(0, 0, mRight - mLeft, mBottom - mTop,
                    mMultipliedAlpha, flags);
        }
    }
    if (mClipChildren) {
        DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, %.2f", indent, "ClipRect", 0.0f, 0.0f,
                (float) mRight - mLeft, (float) mBottom - mTop);
        renderer.clipRect(0, 0, mRight - mLeft, mBottom - mTop,
                SkRegion::kIntersect_Op);
    }
=======
                LOGD("%s%s %.2f, %.2f, %.2f, 0x%x", (char*) indent, OP_NAMES[op],
                    radius, dx, dy, color);
            }
            break;
            default:
                LOGD("Display List error: op not handled: %s%s",
                    (char*) indent, OP_NAMES[op]);
                break;
        }
    }

    LOGD("%sDone", (char*) indent + 2);
>>>>>>> upstream/master
}

/**
 * Changes to replay(), specifically those involving opcode or parameter changes, should be mimicked
 * in the output() function, since that function processes the same list of opcodes for the
 * purposes of logging display list info for a given view.
 */
<<<<<<< HEAD
status_t DisplayList::replay(OpenGLRenderer& renderer, Rect& dirty, int32_t flags, uint32_t level) {
    status_t drawGlStatus = DrawGlInfo::kStatusDone;
=======
bool DisplayList::replay(OpenGLRenderer& renderer, Rect& dirty, uint32_t level) {
    bool needsInvalidate = false;
>>>>>>> upstream/master
    TextContainer text;
    mReader.rewind();

#if DEBUG_DISPLAY_LIST
    uint32_t count = (level + 1) * 2;
    char indent[count + 1];
    for (uint32_t i = 0; i < count; i++) {
        indent[i] = ' ';
    }
    indent[count] = '\0';
<<<<<<< HEAD
    Rect* clipRect = renderer.getClipRect();
    DISPLAY_LIST_LOGD("%sStart display list (%p, %s), clipRect: %.0f, %.f, %.0f, %.0f",
            (char*) indent + 2, this, mName.string(), clipRect->left, clipRect->top,
            clipRect->right, clipRect->bottom);
#endif

    renderer.startMark(mName.string());
    int restoreTo = renderer.save(SkCanvas::kMatrix_SaveFlag | SkCanvas::kClip_SaveFlag);
    DISPLAY_LIST_LOGD("%s%s %d %d", indent, "Save",
            SkCanvas::kMatrix_SaveFlag | SkCanvas::kClip_SaveFlag, restoreTo);
    setViewProperties(renderer, level);
    if (renderer.quickReject(0, 0, mWidth, mHeight)) {
        DISPLAY_LIST_LOGD("%s%s %d", (char*) indent, "RestoreToCount", restoreTo);
        renderer.restoreToCount(restoreTo);
        renderer.endMark();
        return drawGlStatus;
    }

=======
    DISPLAY_LIST_LOGD("%sStart display list (%p)", (char*) indent + 2, this);
#endif

>>>>>>> upstream/master
    DisplayListLogBuffer& logBuffer = DisplayListLogBuffer::getInstance();
    int saveCount = renderer.getSaveCount() - 1;
    while (!mReader.eof()) {
        int op = mReader.readInt();
<<<<<<< HEAD
        if (op & OP_MAY_BE_SKIPPED_MASK) {
            int32_t skip = mReader.readInt();
            if (CC_LIKELY(flags & kReplayFlag_ClipChildren)) {
                mReader.skip(skip);
                DISPLAY_LIST_LOGD("%s%s skipping %d bytes", (char*) indent,
                        OP_NAMES[op & ~OP_MAY_BE_SKIPPED_MASK], skip);
                continue;
            } else {
                op &= ~OP_MAY_BE_SKIPPED_MASK;
            }
        }
=======
>>>>>>> upstream/master
        logBuffer.writeCommand(level, op);

        switch (op) {
            case DrawGLFunction: {
                Functor *functor = (Functor *) getInt();
                DISPLAY_LIST_LOGD("%s%s %p", (char*) indent, OP_NAMES[op], functor);
<<<<<<< HEAD
                renderer.startMark("GL functor");
                drawGlStatus |= renderer.callDrawGLFunction(functor, dirty);
                renderer.endMark();
            }
            break;
            case Save: {
                int32_t rendererNum = getInt();
=======
                needsInvalidate |= renderer.callDrawGLFunction(functor, dirty);
            }
            break;
            case Save: {
                int rendererNum = getInt();
>>>>>>> upstream/master
                DISPLAY_LIST_LOGD("%s%s %d", (char*) indent, OP_NAMES[op], rendererNum);
                renderer.save(rendererNum);
            }
            break;
            case Restore: {
                DISPLAY_LIST_LOGD("%s%s", (char*) indent, OP_NAMES[op]);
                renderer.restore();
            }
            break;
            case RestoreToCount: {
<<<<<<< HEAD
                int32_t restoreCount = saveCount + getInt();
=======
                int restoreCount = saveCount + getInt();
>>>>>>> upstream/master
                DISPLAY_LIST_LOGD("%s%s %d", (char*) indent, OP_NAMES[op], restoreCount);
                renderer.restoreToCount(restoreCount);
            }
            break;
            case SaveLayer: {
                float f1 = getFloat();
                float f2 = getFloat();
                float f3 = getFloat();
                float f4 = getFloat();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                int32_t flags = getInt();
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %p, 0x%x", (char*) indent,
                        OP_NAMES[op], f1, f2, f3, f4, paint, flags);
=======
                SkPaint* paint = getPaint();
                int flags = getInt();
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %p, 0x%x", (char*) indent,
                    OP_NAMES[op], f1, f2, f3, f4, paint, flags);
>>>>>>> upstream/master
                renderer.saveLayer(f1, f2, f3, f4, paint, flags);
            }
            break;
            case SaveLayerAlpha: {
                float f1 = getFloat();
                float f2 = getFloat();
                float f3 = getFloat();
                float f4 = getFloat();
<<<<<<< HEAD
                int32_t alpha = getInt();
                int32_t flags = getInt();
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %d, 0x%x", (char*) indent,
                        OP_NAMES[op], f1, f2, f3, f4, alpha, flags);
=======
                int alpha = getInt();
                int flags = getInt();
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %d, 0x%x", (char*) indent,
                    OP_NAMES[op], f1, f2, f3, f4, alpha, flags);
>>>>>>> upstream/master
                renderer.saveLayerAlpha(f1, f2, f3, f4, alpha, flags);
            }
            break;
            case Translate: {
                float f1 = getFloat();
                float f2 = getFloat();
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f", (char*) indent, OP_NAMES[op], f1, f2);
                renderer.translate(f1, f2);
            }
            break;
            case Rotate: {
                float rotation = getFloat();
                DISPLAY_LIST_LOGD("%s%s %.2f", (char*) indent, OP_NAMES[op], rotation);
                renderer.rotate(rotation);
            }
            break;
            case Scale: {
                float sx = getFloat();
                float sy = getFloat();
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f", (char*) indent, OP_NAMES[op], sx, sy);
                renderer.scale(sx, sy);
            }
            break;
            case Skew: {
                float sx = getFloat();
                float sy = getFloat();
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f", (char*) indent, OP_NAMES[op], sx, sy);
                renderer.skew(sx, sy);
            }
            break;
            case SetMatrix: {
                SkMatrix* matrix = getMatrix();
                DISPLAY_LIST_LOGD("%s%s %p", (char*) indent, OP_NAMES[op], matrix);
                renderer.setMatrix(matrix);
            }
            break;
            case ConcatMatrix: {
                SkMatrix* matrix = getMatrix();
<<<<<<< HEAD
                DISPLAY_LIST_LOGD(
                        "%s%s %p: [%.2f, %.2f, %.2f] [%.2f, %.2f, %.2f] [%.2f, %.2f, %.2f]",
                        (char*) indent, OP_NAMES[op], matrix,
                        matrix->get(0), matrix->get(1), matrix->get(2),
                        matrix->get(3), matrix->get(4), matrix->get(5),
                        matrix->get(6), matrix->get(7), matrix->get(8));
=======
                DISPLAY_LIST_LOGD("%s%s %p", (char*) indent, OP_NAMES[op], matrix);
>>>>>>> upstream/master
                renderer.concatMatrix(matrix);
            }
            break;
            case ClipRect: {
                float f1 = getFloat();
                float f2 = getFloat();
                float f3 = getFloat();
                float f4 = getFloat();
<<<<<<< HEAD
                int32_t regionOp = getInt();
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %d", (char*) indent, OP_NAMES[op],
                        f1, f2, f3, f4, regionOp);
=======
                int regionOp = getInt();
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %d", (char*) indent, OP_NAMES[op],
                    f1, f2, f3, f4, regionOp);
>>>>>>> upstream/master
                renderer.clipRect(f1, f2, f3, f4, (SkRegion::Op) regionOp);
            }
            break;
            case DrawDisplayList: {
                DisplayList* displayList = getDisplayList();
<<<<<<< HEAD
                int32_t flags = getInt();
                DISPLAY_LIST_LOGD("%s%s %p, %dx%d, 0x%x %d", (char*) indent, OP_NAMES[op],
                        displayList, mWidth, mHeight, flags, level + 1);
                drawGlStatus |= renderer.drawDisplayList(displayList, dirty, flags, level + 1);
=======
                uint32_t width = getUInt();
                uint32_t height = getUInt();
                DISPLAY_LIST_LOGD("%s%s %p, %dx%d, %d", (char*) indent, OP_NAMES[op],
                    displayList, width, height, level + 1);
                needsInvalidate |= renderer.drawDisplayList(displayList, width, height,
                        dirty, level + 1);
>>>>>>> upstream/master
            }
            break;
            case DrawLayer: {
                Layer* layer = (Layer*) getInt();
                float x = getFloat();
                float y = getFloat();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                if (mCaching) {
                    paint->setAlpha(mMultipliedAlpha);
                }
                DISPLAY_LIST_LOGD("%s%s %p, %.2f, %.2f, %p", (char*) indent, OP_NAMES[op],
                        layer, x, y, paint);
                drawGlStatus |= renderer.drawLayer(layer, x, y, paint);
=======
                SkPaint* paint = getPaint();
                DISPLAY_LIST_LOGD("%s%s %p, %.2f, %.2f, %p", (char*) indent, OP_NAMES[op],
                    layer, x, y, paint);
                renderer.drawLayer(layer, x, y, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawBitmap: {
                SkBitmap* bitmap = getBitmap();
                float x = getFloat();
                float y = getFloat();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                if (mCaching) {
                    paint->setAlpha(mMultipliedAlpha);
                }
                DISPLAY_LIST_LOGD("%s%s %p, %.2f, %.2f, %p", (char*) indent, OP_NAMES[op],
                        bitmap, x, y, paint);
                drawGlStatus |= renderer.drawBitmap(bitmap, x, y, paint);
=======
                SkPaint* paint = getPaint();
                DISPLAY_LIST_LOGD("%s%s %p, %.2f, %.2f, %p", (char*) indent, OP_NAMES[op],
                    bitmap, x, y, paint);
                renderer.drawBitmap(bitmap, x, y, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawBitmapMatrix: {
                SkBitmap* bitmap = getBitmap();
                SkMatrix* matrix = getMatrix();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                DISPLAY_LIST_LOGD("%s%s %p, %p, %p", (char*) indent, OP_NAMES[op],
                        bitmap, matrix, paint);
                drawGlStatus |= renderer.drawBitmap(bitmap, matrix, paint);
=======
                SkPaint* paint = getPaint();
                DISPLAY_LIST_LOGD("%s%s %p, %p, %p", (char*) indent, OP_NAMES[op],
                    bitmap, matrix, paint);
                renderer.drawBitmap(bitmap, matrix, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawBitmapRect: {
                SkBitmap* bitmap = getBitmap();
                float f1 = getFloat();
                float f2 = getFloat();
                float f3 = getFloat();
                float f4 = getFloat();
                float f5 = getFloat();
                float f6 = getFloat();
                float f7 = getFloat();
                float f8 = getFloat();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                DISPLAY_LIST_LOGD("%s%s %p, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %p",
                        (char*) indent, OP_NAMES[op], bitmap,
                        f1, f2, f3, f4, f5, f6, f7, f8,paint);
                drawGlStatus |= renderer.drawBitmap(bitmap, f1, f2, f3, f4, f5, f6, f7, f8, paint);
            }
            break;
            case DrawBitmapData: {
                SkBitmap* bitmap = getBitmapData();
                float x = getFloat();
                float y = getFloat();
                SkPaint* paint = getPaint(renderer);
                DISPLAY_LIST_LOGD("%s%s %p, %.2f, %.2f, %p", (char*) indent, OP_NAMES[op],
                        bitmap, x, y, paint);
                drawGlStatus |= renderer.drawBitmap(bitmap, x, y, paint);
            }
            break;
            case DrawBitmapMesh: {
                int32_t verticesCount = 0;
=======
                SkPaint* paint = getPaint();
                DISPLAY_LIST_LOGD("%s%s %p, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %p",
                    (char*) indent, OP_NAMES[op], bitmap, f1, f2, f3, f4, f5, f6, f7, f8, paint);
                renderer.drawBitmap(bitmap, f1, f2, f3, f4, f5, f6, f7, f8, paint);
            }
            break;
            case DrawBitmapMesh: {
                int verticesCount = 0;
>>>>>>> upstream/master
                uint32_t colorsCount = 0;

                SkBitmap* bitmap = getBitmap();
                uint32_t meshWidth = getInt();
                uint32_t meshHeight = getInt();
                float* vertices = getFloats(verticesCount);
                bool hasColors = getInt();
<<<<<<< HEAD
                int32_t* colors = hasColors ? getInts(colorsCount) : NULL;
                SkPaint* paint = getPaint(renderer);

                DISPLAY_LIST_LOGD("%s%s", (char*) indent, OP_NAMES[op]);
                drawGlStatus |= renderer.drawBitmapMesh(bitmap, meshWidth, meshHeight, vertices,
                        colors, paint);
=======
                int* colors = hasColors ? getInts(colorsCount) : NULL;
                SkPaint* paint = getPaint();

                DISPLAY_LIST_LOGD("%s%s", (char*) indent, OP_NAMES[op]);
                renderer.drawBitmapMesh(bitmap, meshWidth, meshHeight, vertices, colors, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawPatch: {
                int32_t* xDivs = NULL;
                int32_t* yDivs = NULL;
                uint32_t* colors = NULL;
                uint32_t xDivsCount = 0;
                uint32_t yDivsCount = 0;
                int8_t numColors = 0;

                SkBitmap* bitmap = getBitmap();

                xDivs = getInts(xDivsCount);
                yDivs = getInts(yDivsCount);
                colors = getUInts(numColors);

                float left = getFloat();
                float top = getFloat();
                float right = getFloat();
                float bottom = getFloat();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);

                DISPLAY_LIST_LOGD("%s%s", (char*) indent, OP_NAMES[op]);
                drawGlStatus |= renderer.drawPatch(bitmap, xDivs, yDivs, colors,
                        xDivsCount, yDivsCount, numColors, left, top, right, bottom, paint);
            }
            break;
            case DrawColor: {
                int32_t color = getInt();
                int32_t xferMode = getInt();
                DISPLAY_LIST_LOGD("%s%s 0x%x %d", (char*) indent, OP_NAMES[op], color, xferMode);
                drawGlStatus |= renderer.drawColor(color, (SkXfermode::Mode) xferMode);
=======
                SkPaint* paint = getPaint();

                DISPLAY_LIST_LOGD("%s%s", (char*) indent, OP_NAMES[op]);
                renderer.drawPatch(bitmap, xDivs, yDivs, colors, xDivsCount, yDivsCount,
                        numColors, left, top, right, bottom, paint);
            }
            break;
            case DrawColor: {
                int color = getInt();
                int xferMode = getInt();
                DISPLAY_LIST_LOGD("%s%s 0x%x %d", (char*) indent, OP_NAMES[op], color, xferMode);
                renderer.drawColor(color, (SkXfermode::Mode) xferMode);
>>>>>>> upstream/master
            }
            break;
            case DrawRect: {
                float f1 = getFloat();
                float f2 = getFloat();
                float f3 = getFloat();
                float f4 = getFloat();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %p", (char*) indent, OP_NAMES[op],
                        f1, f2, f3, f4, paint);
                drawGlStatus |= renderer.drawRect(f1, f2, f3, f4, paint);
=======
                SkPaint* paint = getPaint();
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %p", (char*) indent, OP_NAMES[op],
                    f1, f2, f3, f4, paint);
                renderer.drawRect(f1, f2, f3, f4, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawRoundRect: {
                float f1 = getFloat();
                float f2 = getFloat();
                float f3 = getFloat();
                float f4 = getFloat();
                float f5 = getFloat();
                float f6 = getFloat();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %p",
                        (char*) indent, OP_NAMES[op], f1, f2, f3, f4, f5, f6, paint);
                drawGlStatus |= renderer.drawRoundRect(f1, f2, f3, f4, f5, f6, paint);
=======
                SkPaint* paint = getPaint();
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %p",
                    (char*) indent, OP_NAMES[op], f1, f2, f3, f4, f5, f6, paint);
                renderer.drawRoundRect(f1, f2, f3, f4, f5, f6, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawCircle: {
                float f1 = getFloat();
                float f2 = getFloat();
                float f3 = getFloat();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, %p",
                        (char*) indent, OP_NAMES[op], f1, f2, f3, paint);
                drawGlStatus |= renderer.drawCircle(f1, f2, f3, paint);
=======
                SkPaint* paint = getPaint();
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, %p",
                    (char*) indent, OP_NAMES[op], f1, f2, f3, paint);
                renderer.drawCircle(f1, f2, f3, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawOval: {
                float f1 = getFloat();
                float f2 = getFloat();
                float f3 = getFloat();
                float f4 = getFloat();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %p",
                        (char*) indent, OP_NAMES[op], f1, f2, f3, f4, paint);
                drawGlStatus |= renderer.drawOval(f1, f2, f3, f4, paint);
=======
                SkPaint* paint = getPaint();
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %p",
                    (char*) indent, OP_NAMES[op], f1, f2, f3, f4, paint);
                renderer.drawOval(f1, f2, f3, f4, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawArc: {
                float f1 = getFloat();
                float f2 = getFloat();
                float f3 = getFloat();
                float f4 = getFloat();
                float f5 = getFloat();
                float f6 = getFloat();
<<<<<<< HEAD
                int32_t i1 = getInt();
                SkPaint* paint = getPaint(renderer);
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %d, %p",
                        (char*) indent, OP_NAMES[op], f1, f2, f3, f4, f5, f6, i1, paint);
                drawGlStatus |= renderer.drawArc(f1, f2, f3, f4, f5, f6, i1 == 1, paint);
=======
                int i1 = getInt();
                SkPaint* paint = getPaint();
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %d, %p",
                    (char*) indent, OP_NAMES[op], f1, f2, f3, f4, f5, f6, i1, paint);
                renderer.drawArc(f1, f2, f3, f4, f5, f6, i1 == 1, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawPath: {
                SkPath* path = getPath();
<<<<<<< HEAD
                SkPaint* paint = getPaint(renderer);
                DISPLAY_LIST_LOGD("%s%s %p, %p", (char*) indent, OP_NAMES[op], path, paint);
                drawGlStatus |= renderer.drawPath(path, paint);
            }
            break;
            case DrawLines: {
                int32_t count = 0;
                float* points = getFloats(count);
                SkPaint* paint = getPaint(renderer);
                DISPLAY_LIST_LOGD("%s%s", (char*) indent, OP_NAMES[op]);
                drawGlStatus |= renderer.drawLines(points, count, paint);
            }
            break;
            case DrawPoints: {
                int32_t count = 0;
                float* points = getFloats(count);
                SkPaint* paint = getPaint(renderer);
                DISPLAY_LIST_LOGD("%s%s", (char*) indent, OP_NAMES[op]);
                drawGlStatus |= renderer.drawPoints(points, count, paint);
=======
                SkPaint* paint = getPaint();
                DISPLAY_LIST_LOGD("%s%s %p, %p", (char*) indent, OP_NAMES[op], path, paint);
                renderer.drawPath(path, paint);
            }
            break;
            case DrawLines: {
                int count = 0;
                float* points = getFloats(count);
                SkPaint* paint = getPaint();
                DISPLAY_LIST_LOGD("%s%s", (char*) indent, OP_NAMES[op]);
                renderer.drawLines(points, count, paint);
            }
            break;
            case DrawPoints: {
                int count = 0;
                float* points = getFloats(count);
                SkPaint* paint = getPaint();
                DISPLAY_LIST_LOGD("%s%s", (char*) indent, OP_NAMES[op]);
                renderer.drawPoints(points, count, paint);
>>>>>>> upstream/master
            }
            break;
            case DrawText: {
                getText(&text);
<<<<<<< HEAD
                int32_t count = getInt();
                float x = getFloat();
                float y = getFloat();
                SkPaint* paint = getPaint(renderer);
                float length = getFloat();
                DISPLAY_LIST_LOGD("%s%s %s, %d, %d, %.2f, %.2f, %p, %.2f", (char*) indent,
                        OP_NAMES[op], text.text(), text.length(), count, x, y, paint, length);
                drawGlStatus |= renderer.drawText(text.text(), text.length(), count, x, y,
                        paint, length);
            }
            break;
            case DrawTextOnPath: {
                getText(&text);
                int32_t count = getInt();
                SkPath* path = getPath();
                float hOffset = getFloat();
                float vOffset = getFloat();
                SkPaint* paint = getPaint(renderer);
                DISPLAY_LIST_LOGD("%s%s %s, %d, %d, %p", (char*) indent, OP_NAMES[op],
                    text.text(), text.length(), count, paint);
                drawGlStatus |= renderer.drawTextOnPath(text.text(), text.length(), count, path,
                        hOffset, vOffset, paint);
            }
            break;
            case DrawPosText: {
                getText(&text);
                int32_t count = getInt();
                int32_t positionsCount = 0;
                float* positions = getFloats(positionsCount);
                SkPaint* paint = getPaint(renderer);
                DISPLAY_LIST_LOGD("%s%s %s, %d, %d, %p", (char*) indent,
                        OP_NAMES[op], text.text(), text.length(), count, paint);
                drawGlStatus |= renderer.drawPosText(text.text(), text.length(), count,
                        positions, paint);
=======
                int count = getInt();
                float x = getFloat();
                float y = getFloat();
                SkPaint* paint = getPaint();
                DISPLAY_LIST_LOGD("%s%s %s, %d, %d, %.2f, %.2f, %p", (char*) indent, OP_NAMES[op],
                    text.text(), text.length(), count, x, y, paint);
                renderer.drawText(text.text(), text.length(), count, x, y, paint);
>>>>>>> upstream/master
            }
            break;
            case ResetShader: {
                DISPLAY_LIST_LOGD("%s%s", (char*) indent, OP_NAMES[op]);
                renderer.resetShader();
            }
            break;
            case SetupShader: {
                SkiaShader* shader = getShader();
                DISPLAY_LIST_LOGD("%s%s %p", (char*) indent, OP_NAMES[op], shader);
                renderer.setupShader(shader);
            }
            break;
            case ResetColorFilter: {
                DISPLAY_LIST_LOGD("%s%s", (char*) indent, OP_NAMES[op]);
                renderer.resetColorFilter();
            }
            break;
            case SetupColorFilter: {
                SkiaColorFilter *colorFilter = getColorFilter();
                DISPLAY_LIST_LOGD("%s%s %p", (char*) indent, OP_NAMES[op], colorFilter);
                renderer.setupColorFilter(colorFilter);
            }
            break;
            case ResetShadow: {
                DISPLAY_LIST_LOGD("%s%s", (char*) indent, OP_NAMES[op]);
                renderer.resetShadow();
            }
            break;
            case SetupShadow: {
                float radius = getFloat();
                float dx = getFloat();
                float dy = getFloat();
<<<<<<< HEAD
                int32_t color = getInt();
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, 0x%x", (char*) indent, OP_NAMES[op],
                        radius, dx, dy, color);
                renderer.setupShadow(radius, dx, dy, color);
            }
            break;
            case ResetPaintFilter: {
                DISPLAY_LIST_LOGD("%s%s", (char*) indent, OP_NAMES[op]);
                renderer.resetPaintFilter();
            }
            break;
            case SetupPaintFilter: {
                int32_t clearBits = getInt();
                int32_t setBits = getInt();
                DISPLAY_LIST_LOGD("%s%s 0x%x, 0x%x", (char*) indent, OP_NAMES[op],
                        clearBits, setBits);
                renderer.setupPaintFilter(clearBits, setBits);
            }
            break;
            default:
                DISPLAY_LIST_LOGD("Display List error: op not handled: %s%s",
                        (char*) indent, OP_NAMES[op]);
=======
                int color = getInt();
                DISPLAY_LIST_LOGD("%s%s %.2f, %.2f, %.2f, 0x%x", (char*) indent, OP_NAMES[op],
                    radius, dx, dy, color);
                renderer.setupShadow(radius, dx, dy, color);
            }
            break;
            default:
                DISPLAY_LIST_LOGD("Display List error: op not handled: %s%s",
                    (char*) indent, OP_NAMES[op]);
>>>>>>> upstream/master
                break;
        }
    }

<<<<<<< HEAD
    DISPLAY_LIST_LOGD("%s%s %d", (char*) indent, "RestoreToCount", restoreTo);
    renderer.restoreToCount(restoreTo);
    renderer.endMark();

    DISPLAY_LIST_LOGD("%sDone (%p, %s), returning %d", (char*) indent + 2, this, mName.string(),
            drawGlStatus);
    return drawGlStatus;
=======
    DISPLAY_LIST_LOGD("%sDone, returning %d", (char*) indent + 2, needsInvalidate);
    return needsInvalidate;
>>>>>>> upstream/master
}

///////////////////////////////////////////////////////////////////////////////
// Base structure
///////////////////////////////////////////////////////////////////////////////

<<<<<<< HEAD
DisplayListRenderer::DisplayListRenderer() : mWriter(MIN_WRITER_SIZE),
        mTranslateX(0.0f), mTranslateY(0.0f), mHasTranslate(false), mHasDrawOps(false) {
=======
DisplayListRenderer::DisplayListRenderer(): mWriter(MIN_WRITER_SIZE), mHasDrawOps(false) {
>>>>>>> upstream/master
}

DisplayListRenderer::~DisplayListRenderer() {
    reset();
}

void DisplayListRenderer::reset() {
    mWriter.reset();

    Caches& caches = Caches::getInstance();
    for (size_t i = 0; i < mBitmapResources.size(); i++) {
        caches.resourceCache.decrementRefcount(mBitmapResources.itemAt(i));
    }
    mBitmapResources.clear();

<<<<<<< HEAD
    for (size_t i = 0; i < mOwnedBitmapResources.size(); i++) {
        SkBitmap* bitmap = mOwnedBitmapResources.itemAt(i);
        caches.resourceCache.decrementRefcount(bitmap);
    }
    mOwnedBitmapResources.clear();

=======
>>>>>>> upstream/master
    for (size_t i = 0; i < mFilterResources.size(); i++) {
        caches.resourceCache.decrementRefcount(mFilterResources.itemAt(i));
    }
    mFilterResources.clear();

    for (size_t i = 0; i < mShaders.size(); i++) {
        caches.resourceCache.decrementRefcount(mShaders.itemAt(i));
    }
    mShaders.clear();
    mShaderMap.clear();

<<<<<<< HEAD
    for (size_t i = 0; i < mSourcePaths.size(); i++) {
        caches.resourceCache.decrementRefcount(mSourcePaths.itemAt(i));
    }
    mSourcePaths.clear();

=======
>>>>>>> upstream/master
    mPaints.clear();
    mPaintMap.clear();

    mPaths.clear();
    mPathMap.clear();

    mMatrices.clear();

    mHasDrawOps = false;
}

///////////////////////////////////////////////////////////////////////////////
// Operations
///////////////////////////////////////////////////////////////////////////////

DisplayList* DisplayListRenderer::getDisplayList(DisplayList* displayList) {
    if (!displayList) {
        displayList = new DisplayList(*this);
    } else {
        displayList->initFromDisplayListRenderer(*this, true);
    }
    displayList->setRenderable(mHasDrawOps);
    return displayList;
}

<<<<<<< HEAD
bool DisplayListRenderer::isDeferred() {
    return true;
}

=======
>>>>>>> upstream/master
void DisplayListRenderer::setViewport(int width, int height) {
    mOrthoMatrix.loadOrtho(0, width, height, 0, -1, 1);

    mWidth = width;
    mHeight = height;
}

<<<<<<< HEAD
int DisplayListRenderer::prepareDirty(float left, float top,
=======
void DisplayListRenderer::prepareDirty(float left, float top,
>>>>>>> upstream/master
        float right, float bottom, bool opaque) {
    mSnapshot = new Snapshot(mFirstSnapshot,
            SkCanvas::kMatrix_SaveFlag | SkCanvas::kClip_SaveFlag);
    mSaveCount = 1;
    mSnapshot->setClip(0.0f, 0.0f, mWidth, mHeight);
    mRestoreSaveCount = -1;
<<<<<<< HEAD
    return DrawGlInfo::kStatusDone; // No invalidate needed at record-time
=======
>>>>>>> upstream/master
}

void DisplayListRenderer::finish() {
    insertRestoreToCount();
<<<<<<< HEAD
    insertTranlate();
=======
    OpenGLRenderer::finish();
>>>>>>> upstream/master
}

void DisplayListRenderer::interrupt() {
}

void DisplayListRenderer::resume() {
}

<<<<<<< HEAD
status_t DisplayListRenderer::callDrawGLFunction(Functor *functor, Rect& dirty) {
    // Ignore dirty during recording, it matters only when we replay
    addOp(DisplayList::DrawGLFunction);
    addInt((int) functor);
    return DrawGlInfo::kStatusDone; // No invalidate needed at record-time
=======
bool DisplayListRenderer::callDrawGLFunction(Functor *functor, Rect& dirty) {
    // Ignore dirty during recording, it matters only when we replay
    addOp(DisplayList::DrawGLFunction);
    addInt((int) functor);
    return false; // No invalidate needed at record-time
>>>>>>> upstream/master
}

int DisplayListRenderer::save(int flags) {
    addOp(DisplayList::Save);
    addInt(flags);
    return OpenGLRenderer::save(flags);
}

void DisplayListRenderer::restore() {
    if (mRestoreSaveCount < 0) {
<<<<<<< HEAD
        restoreToCount(getSaveCount() - 1);
        return;
    }

    mRestoreSaveCount--;
    insertTranlate();
=======
        addOp(DisplayList::Restore);
    } else {
        mRestoreSaveCount--;
    }
>>>>>>> upstream/master
    OpenGLRenderer::restore();
}

void DisplayListRenderer::restoreToCount(int saveCount) {
    mRestoreSaveCount = saveCount;
<<<<<<< HEAD
    insertTranlate();
=======
>>>>>>> upstream/master
    OpenGLRenderer::restoreToCount(saveCount);
}

int DisplayListRenderer::saveLayer(float left, float top, float right, float bottom,
        SkPaint* p, int flags) {
    addOp(DisplayList::SaveLayer);
    addBounds(left, top, right, bottom);
    addPaint(p);
    addInt(flags);
    return OpenGLRenderer::save(flags);
}

int DisplayListRenderer::saveLayerAlpha(float left, float top, float right, float bottom,
        int alpha, int flags) {
    addOp(DisplayList::SaveLayerAlpha);
    addBounds(left, top, right, bottom);
    addInt(alpha);
    addInt(flags);
    return OpenGLRenderer::save(flags);
}

void DisplayListRenderer::translate(float dx, float dy) {
<<<<<<< HEAD
    mHasTranslate = true;
    mTranslateX += dx;
    mTranslateY += dy;
    insertRestoreToCount();
=======
    addOp(DisplayList::Translate);
    addPoint(dx, dy);
>>>>>>> upstream/master
    OpenGLRenderer::translate(dx, dy);
}

void DisplayListRenderer::rotate(float degrees) {
    addOp(DisplayList::Rotate);
    addFloat(degrees);
    OpenGLRenderer::rotate(degrees);
}

void DisplayListRenderer::scale(float sx, float sy) {
    addOp(DisplayList::Scale);
    addPoint(sx, sy);
    OpenGLRenderer::scale(sx, sy);
}

void DisplayListRenderer::skew(float sx, float sy) {
    addOp(DisplayList::Skew);
    addPoint(sx, sy);
    OpenGLRenderer::skew(sx, sy);
}

void DisplayListRenderer::setMatrix(SkMatrix* matrix) {
    addOp(DisplayList::SetMatrix);
    addMatrix(matrix);
    OpenGLRenderer::setMatrix(matrix);
}

void DisplayListRenderer::concatMatrix(SkMatrix* matrix) {
    addOp(DisplayList::ConcatMatrix);
    addMatrix(matrix);
    OpenGLRenderer::concatMatrix(matrix);
}

bool DisplayListRenderer::clipRect(float left, float top, float right, float bottom,
        SkRegion::Op op) {
    addOp(DisplayList::ClipRect);
    addBounds(left, top, right, bottom);
    addInt(op);
    return OpenGLRenderer::clipRect(left, top, right, bottom, op);
}

<<<<<<< HEAD
status_t DisplayListRenderer::drawDisplayList(DisplayList* displayList,
        Rect& dirty, int32_t flags, uint32_t level) {
    // dirty is an out parameter and should not be recorded,
    // it matters only when replaying the display list

    addOp(DisplayList::DrawDisplayList);
    addDisplayList(displayList);
    addInt(flags);
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawLayer(Layer* layer, float x, float y, SkPaint* paint) {
=======
bool DisplayListRenderer::drawDisplayList(DisplayList* displayList,
        uint32_t width, uint32_t height, Rect& dirty, uint32_t level) {
    // dirty is an out parameter and should not be recorded,
    // it matters only when replaying the display list
    addOp(DisplayList::DrawDisplayList);
    addDisplayList(displayList);
    addSize(width, height);
    return false;
}

void DisplayListRenderer::drawLayer(Layer* layer, float x, float y, SkPaint* paint) {
>>>>>>> upstream/master
    addOp(DisplayList::DrawLayer);
    addInt((int) layer);
    addPoint(x, y);
    addPaint(paint);
<<<<<<< HEAD
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawBitmap(SkBitmap* bitmap, float left, float top, SkPaint* paint) {
    const bool reject = quickReject(left, top, left + bitmap->width(), top + bitmap->height());
    uint32_t* location = addOp(DisplayList::DrawBitmap, reject);
    addBitmap(bitmap);
    addPoint(left, top);
    addPaint(paint);
    addSkip(location);
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawBitmap(SkBitmap* bitmap, SkMatrix* matrix, SkPaint* paint) {
    Rect r(0.0f, 0.0f, bitmap->width(), bitmap->height());
    const mat4 transform(*matrix);
    transform.mapRect(r);

    const bool reject = quickReject(r.left, r.top, r.right, r.bottom);
    uint32_t* location = addOp(DisplayList::DrawBitmapMatrix, reject);
    addBitmap(bitmap);
    addMatrix(matrix);
    addPaint(paint);
    addSkip(location);
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawBitmap(SkBitmap* bitmap, float srcLeft, float srcTop,
        float srcRight, float srcBottom, float dstLeft, float dstTop,
        float dstRight, float dstBottom, SkPaint* paint) {
    const bool reject = quickReject(dstLeft, dstTop, dstRight, dstBottom);
    uint32_t* location = addOp(DisplayList::DrawBitmapRect, reject);
=======
}

void DisplayListRenderer::drawBitmap(SkBitmap* bitmap, float left, float top,
        SkPaint* paint) {
    addOp(DisplayList::DrawBitmap);
    addBitmap(bitmap);
    addPoint(left, top);
    addPaint(paint);
}

void DisplayListRenderer::drawBitmap(SkBitmap* bitmap, SkMatrix* matrix,
        SkPaint* paint) {
    addOp(DisplayList::DrawBitmapMatrix);
    addBitmap(bitmap);
    addMatrix(matrix);
    addPaint(paint);
}

void DisplayListRenderer::drawBitmap(SkBitmap* bitmap, float srcLeft, float srcTop,
        float srcRight, float srcBottom, float dstLeft, float dstTop,
        float dstRight, float dstBottom, SkPaint* paint) {
    addOp(DisplayList::DrawBitmapRect);
>>>>>>> upstream/master
    addBitmap(bitmap);
    addBounds(srcLeft, srcTop, srcRight, srcBottom);
    addBounds(dstLeft, dstTop, dstRight, dstBottom);
    addPaint(paint);
<<<<<<< HEAD
    addSkip(location);
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawBitmapData(SkBitmap* bitmap, float left, float top,
        SkPaint* paint) {
    const bool reject = quickReject(left, top, left + bitmap->width(), bitmap->height());
    uint32_t* location = addOp(DisplayList::DrawBitmapData, reject);
    addBitmapData(bitmap);
    addPoint(left, top);
    addPaint(paint);
    addSkip(location);
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawBitmapMesh(SkBitmap* bitmap, int meshWidth, int meshHeight,
=======
}

void DisplayListRenderer::drawBitmapMesh(SkBitmap* bitmap, int meshWidth, int meshHeight,
>>>>>>> upstream/master
        float* vertices, int* colors, SkPaint* paint) {
    addOp(DisplayList::DrawBitmapMesh);
    addBitmap(bitmap);
    addInt(meshWidth);
    addInt(meshHeight);
    addFloats(vertices, (meshWidth + 1) * (meshHeight + 1) * 2);
    if (colors) {
        addInt(1);
        addInts(colors, (meshWidth + 1) * (meshHeight + 1));
    } else {
        addInt(0);
    }
    addPaint(paint);
<<<<<<< HEAD
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawPatch(SkBitmap* bitmap, const int32_t* xDivs,
        const int32_t* yDivs, const uint32_t* colors, uint32_t width, uint32_t height,
        int8_t numColors, float left, float top, float right, float bottom, SkPaint* paint) {
    const bool reject = quickReject(left, top, right, bottom);
    uint32_t* location = addOp(DisplayList::DrawPatch, reject);
=======
}

void DisplayListRenderer::drawPatch(SkBitmap* bitmap, const int32_t* xDivs, const int32_t* yDivs,
        const uint32_t* colors, uint32_t width, uint32_t height, int8_t numColors,
        float left, float top, float right, float bottom, SkPaint* paint) {
    addOp(DisplayList::DrawPatch);
>>>>>>> upstream/master
    addBitmap(bitmap);
    addInts(xDivs, width);
    addInts(yDivs, height);
    addUInts(colors, numColors);
    addBounds(left, top, right, bottom);
    addPaint(paint);
<<<<<<< HEAD
    addSkip(location);
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawColor(int color, SkXfermode::Mode mode) {
    addOp(DisplayList::DrawColor);
    addInt(color);
    addInt(mode);
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawRect(float left, float top, float right, float bottom,
        SkPaint* paint) {
    const bool reject = paint->getStyle() == SkPaint::kFill_Style &&
            quickReject(left, top, right, bottom);
    uint32_t* location = addOp(DisplayList::DrawRect, reject);
    addBounds(left, top, right, bottom);
    addPaint(paint);
    addSkip(location);
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawRoundRect(float left, float top, float right, float bottom,
        float rx, float ry, SkPaint* paint) {
    const bool reject = paint->getStyle() == SkPaint::kFill_Style &&
            quickReject(left, top, right, bottom);
    uint32_t* location = addOp(DisplayList::DrawRoundRect, reject);
    addBounds(left, top, right, bottom);
    addPoint(rx, ry);
    addPaint(paint);
    addSkip(location);
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawCircle(float x, float y, float radius, SkPaint* paint) {
=======
}

void DisplayListRenderer::drawColor(int color, SkXfermode::Mode mode) {
    addOp(DisplayList::DrawColor);
    addInt(color);
    addInt(mode);
}

void DisplayListRenderer::drawRect(float left, float top, float right, float bottom,
        SkPaint* paint) {
    addOp(DisplayList::DrawRect);
    addBounds(left, top, right, bottom);
    addPaint(paint);
}

void DisplayListRenderer::drawRoundRect(float left, float top, float right, float bottom,
            float rx, float ry, SkPaint* paint) {
    addOp(DisplayList::DrawRoundRect);
    addBounds(left, top, right, bottom);
    addPoint(rx, ry);
    addPaint(paint);
}

void DisplayListRenderer::drawCircle(float x, float y, float radius, SkPaint* paint) {
>>>>>>> upstream/master
    addOp(DisplayList::DrawCircle);
    addPoint(x, y);
    addFloat(radius);
    addPaint(paint);
<<<<<<< HEAD
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawOval(float left, float top, float right, float bottom,
=======
}

void DisplayListRenderer::drawOval(float left, float top, float right, float bottom,
>>>>>>> upstream/master
        SkPaint* paint) {
    addOp(DisplayList::DrawOval);
    addBounds(left, top, right, bottom);
    addPaint(paint);
<<<<<<< HEAD
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawArc(float left, float top, float right, float bottom,
=======
}

void DisplayListRenderer::drawArc(float left, float top, float right, float bottom,
>>>>>>> upstream/master
        float startAngle, float sweepAngle, bool useCenter, SkPaint* paint) {
    addOp(DisplayList::DrawArc);
    addBounds(left, top, right, bottom);
    addPoint(startAngle, sweepAngle);
    addInt(useCenter ? 1 : 0);
    addPaint(paint);
<<<<<<< HEAD
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawPath(SkPath* path, SkPaint* paint) {
    float left, top, offset;
    uint32_t width, height;
    computePathBounds(path, paint, left, top, offset, width, height);

    const bool reject = quickReject(left - offset, top - offset, width, height);
    uint32_t* location = addOp(DisplayList::DrawPath, reject);
    addPath(path);
    addPaint(paint);
    addSkip(location);
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawLines(float* points, int count, SkPaint* paint) {
    addOp(DisplayList::DrawLines);
    addFloats(points, count);
    addPaint(paint);
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawPoints(float* points, int count, SkPaint* paint) {
    addOp(DisplayList::DrawPoints);
    addFloats(points, count);
    addPaint(paint);
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawText(const char* text, int bytesCount, int count,
        float x, float y, SkPaint* paint, float length) {
    if (!text || count <= 0) return DrawGlInfo::kStatusDone;

    // TODO: We should probably make a copy of the paint instead of modifying
    //       it; modifying the paint will change its generationID the first
    //       time, which might impact caches. More investigation needed to
    //       see if it matters.
    //       If we make a copy, then drawTextDecorations() should *not* make
    //       its own copy as it does right now.
    // Beware: this needs Glyph encoding (already done on the Paint constructor)
    paint->setAntiAlias(true);
    if (length < 0.0f) length = paint->measureText(text, bytesCount);

    bool reject = false;
    if (CC_LIKELY(paint->getTextAlign() == SkPaint::kLeft_Align)) {
        SkPaint::FontMetrics metrics;
        paint->getFontMetrics(&metrics, 0.0f);
        reject = quickReject(x, y + metrics.fTop, x + length, y + metrics.fBottom);
    }

    uint32_t* location = addOp(DisplayList::DrawText, reject);
=======
}

void DisplayListRenderer::drawPath(SkPath* path, SkPaint* paint) {
    addOp(DisplayList::DrawPath);
    addPath(path);
    addPaint(paint);
}

void DisplayListRenderer::drawLines(float* points, int count, SkPaint* paint) {
    addOp(DisplayList::DrawLines);
    addFloats(points, count);
    addPaint(paint);
}

void DisplayListRenderer::drawPoints(float* points, int count, SkPaint* paint) {
    addOp(DisplayList::DrawPoints);
    addFloats(points, count);
    addPaint(paint);
}

void DisplayListRenderer::drawText(const char* text, int bytesCount, int count,
        float x, float y, SkPaint* paint) {
    if (count <= 0) return;
    addOp(DisplayList::DrawText);
>>>>>>> upstream/master
    addText(text, bytesCount);
    addInt(count);
    addPoint(x, y);
    addPaint(paint);
<<<<<<< HEAD
    addFloat(length);
    addSkip(location);
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawTextOnPath(const char* text, int bytesCount, int count,
        SkPath* path, float hOffset, float vOffset, SkPaint* paint) {
    if (!text || count <= 0) return DrawGlInfo::kStatusDone;
    addOp(DisplayList::DrawTextOnPath);
    addText(text, bytesCount);
    addInt(count);
    addPath(path);
    addFloat(hOffset);
    addFloat(vOffset);
    paint->setAntiAlias(true);
    addPaint(paint);
    return DrawGlInfo::kStatusDone;
}

status_t DisplayListRenderer::drawPosText(const char* text, int bytesCount, int count,
        const float* positions, SkPaint* paint) {
    if (!text || count <= 0) return DrawGlInfo::kStatusDone;
    addOp(DisplayList::DrawPosText);
    addText(text, bytesCount);
    addInt(count);
    addFloats(positions, count * 2);
    paint->setAntiAlias(true);
    addPaint(paint);
    return DrawGlInfo::kStatusDone;
=======
>>>>>>> upstream/master
}

void DisplayListRenderer::resetShader() {
    addOp(DisplayList::ResetShader);
}

void DisplayListRenderer::setupShader(SkiaShader* shader) {
    addOp(DisplayList::SetupShader);
    addShader(shader);
}

void DisplayListRenderer::resetColorFilter() {
    addOp(DisplayList::ResetColorFilter);
}

void DisplayListRenderer::setupColorFilter(SkiaColorFilter* filter) {
    addOp(DisplayList::SetupColorFilter);
    addColorFilter(filter);
}

void DisplayListRenderer::resetShadow() {
    addOp(DisplayList::ResetShadow);
}

void DisplayListRenderer::setupShadow(float radius, float dx, float dy, int color) {
    addOp(DisplayList::SetupShadow);
    addFloat(radius);
    addPoint(dx, dy);
    addInt(color);
}

<<<<<<< HEAD
void DisplayListRenderer::resetPaintFilter() {
    addOp(DisplayList::ResetPaintFilter);
}

void DisplayListRenderer::setupPaintFilter(int clearBits, int setBits) {
    addOp(DisplayList::SetupPaintFilter);
    addInt(clearBits);
    addInt(setBits);
}

=======
>>>>>>> upstream/master
}; // namespace uirenderer
}; // namespace android
