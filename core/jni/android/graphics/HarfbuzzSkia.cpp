/*
 * Copyright 2011, The Android Open Source Project
 * Copyright 2011, Google Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

<<<<<<< HEAD
#define LOG_TAG "HarfbuzzSkia"

=======
>>>>>>> upstream/master
#include "HarfbuzzSkia.h"

#include "SkFontHost.h"

#include "SkPaint.h"
#include "SkPath.h"
#include "SkPoint.h"
#include "SkRect.h"
#include "SkTypeface.h"

#include <utils/Log.h>

extern "C" {
#include "harfbuzz-shaper.h"
}

// This file implements the callbacks which Harfbuzz requires by using Skia
// calls. See the Harfbuzz source for references about what these callbacks do.

namespace android {

<<<<<<< HEAD
static HB_Bool stringToGlyphs(HB_Font hbFont, const HB_UChar16* characters, hb_uint32 length,
        HB_Glyph* glyphs, hb_uint32* glyphsSize, HB_Bool isRTL)
{
    SkPaint* paint = static_cast<SkPaint*>(hbFont->userData);
    paint->setTextEncoding(SkPaint::kUTF16_TextEncoding);

    uint16_t* skiaGlyphs = reinterpret_cast<uint16_t*>(glyphs);
    int numGlyphs = paint->textToGlyphs(characters, length * sizeof(uint16_t), skiaGlyphs);
=======
static void setupPaintWithFontData(SkPaint* paint, FontData* data) {
    paint->setTypeface(data->typeFace);
    paint->setTextSize(data->textSize);
    paint->setTextSkewX(data->textSkewX);
    paint->setTextScaleX(data->textScaleX);
    paint->setFlags(data->flags);
    paint->setHinting(data->hinting);
}

static HB_Bool stringToGlyphs(HB_Font hbFont, const HB_UChar16* characters, hb_uint32 length,
        HB_Glyph* glyphs, hb_uint32* glyphsSize, HB_Bool isRTL)
{
    FontData* data = reinterpret_cast<FontData*>(hbFont->userData);
    SkPaint paint;
    setupPaintWithFontData(&paint, data);

    paint.setTextEncoding(SkPaint::kUTF16_TextEncoding);
    uint16_t* skiaGlyphs = reinterpret_cast<uint16_t*>(glyphs);
    int numGlyphs = paint.textToGlyphs(characters, length * sizeof(uint16_t), skiaGlyphs);
>>>>>>> upstream/master

    // HB_Glyph is 32-bit, but Skia outputs only 16-bit numbers. So our
    // |glyphs| array needs to be converted.
    for (int i = numGlyphs - 1; i >= 0; --i) {
        glyphs[i] = skiaGlyphs[i];
    }

    *glyphsSize = numGlyphs;
    return 1;
}

static void glyphsToAdvances(HB_Font hbFont, const HB_Glyph* glyphs, hb_uint32 numGlyphs,
        HB_Fixed* advances, int flags)
{
<<<<<<< HEAD
    SkPaint* paint = static_cast<SkPaint*>(hbFont->userData);
    paint->setTextEncoding(SkPaint::kGlyphID_TextEncoding);
=======
    FontData* data = reinterpret_cast<FontData*>(hbFont->userData);
    SkPaint paint;
    setupPaintWithFontData(&paint, data);

    paint.setTextEncoding(SkPaint::kGlyphID_TextEncoding);
>>>>>>> upstream/master

    uint16_t* glyphs16 = new uint16_t[numGlyphs];
    if (!glyphs16)
        return;
    for (unsigned i = 0; i < numGlyphs; ++i)
        glyphs16[i] = glyphs[i];
    SkScalar* scalarAdvances = reinterpret_cast<SkScalar*>(advances);
<<<<<<< HEAD
    paint->getTextWidths(glyphs16, numGlyphs * sizeof(uint16_t), scalarAdvances);
=======
    paint.getTextWidths(glyphs16, numGlyphs * sizeof(uint16_t), scalarAdvances);
>>>>>>> upstream/master

    // The |advances| values which Skia outputs are SkScalars, which are floats
    // in Chromium. However, Harfbuzz wants them in 26.6 fixed point format.
    // These two formats are both 32-bits long.
    for (unsigned i = 0; i < numGlyphs; ++i) {
        advances[i] = SkScalarToHBFixed(scalarAdvances[i]);
#if DEBUG_ADVANCES
<<<<<<< HEAD
        ALOGD("glyphsToAdvances -- advances[%d]=%d", i, advances[i]);
=======
        LOGD("glyphsToAdvances -- advances[%d]=%d", i, advances[i]);
>>>>>>> upstream/master
#endif
    }
    delete glyphs16;
}

static HB_Bool canRender(HB_Font hbFont, const HB_UChar16* characters, hb_uint32 length)
{
<<<<<<< HEAD
    SkPaint* paint = static_cast<SkPaint*>(hbFont->userData);
    paint->setTextEncoding(SkPaint::kUTF16_TextEncoding);

    uint16_t* glyphs16 = new uint16_t[length];
    int numGlyphs = paint->textToGlyphs(characters, length * sizeof(uint16_t), glyphs16);
=======
    FontData* data = reinterpret_cast<FontData*>(hbFont->userData);
    SkPaint paint;
    setupPaintWithFontData(&paint, data);

    paint.setTextEncoding(SkPaint::kUTF16_TextEncoding);

    uint16_t* glyphs16 = new uint16_t[length];
    int numGlyphs = paint.textToGlyphs(characters, length * sizeof(uint16_t), glyphs16);
>>>>>>> upstream/master

    bool result = true;
    for (int i = 0; i < numGlyphs; ++i) {
        if (!glyphs16[i]) {
            result = false;
            break;
        }
    }
    delete glyphs16;
    return result;
}

static HB_Error getOutlinePoint(HB_Font hbFont, HB_Glyph glyph, int flags, hb_uint32 point,
        HB_Fixed* xPos, HB_Fixed* yPos, hb_uint32* resultingNumPoints)
{
<<<<<<< HEAD
=======
    FontData* data = reinterpret_cast<FontData*>(hbFont->userData);
    SkPaint paint;
    setupPaintWithFontData(&paint, data);

>>>>>>> upstream/master
    if (flags & HB_ShaperFlag_UseDesignMetrics)
        // This is requesting pre-hinted positions. We can't support this.
        return HB_Err_Invalid_Argument;

<<<<<<< HEAD
    SkPaint* paint = static_cast<SkPaint*>(hbFont->userData);
    paint->setTextEncoding(SkPaint::kGlyphID_TextEncoding);

    uint16_t glyph16 = glyph;
    SkPath path;
    paint->getTextPath(&glyph16, sizeof(glyph16), 0, 0, &path);
    uint32_t numPoints = path.getPoints(0, 0);
    if (point >= numPoints)
        return HB_Err_Invalid_SubTable;
    SkPoint* points = static_cast<SkPoint*>(malloc(sizeof(SkPoint) * (point + 1)));
=======
    paint.setTextEncoding(SkPaint::kGlyphID_TextEncoding);
    uint16_t glyph16 = glyph;
    SkPath path;
    paint.getTextPath(&glyph16, sizeof(glyph16), 0, 0, &path);
    uint32_t numPoints = path.getPoints(0, 0);
    if (point >= numPoints)
        return HB_Err_Invalid_SubTable;
    SkPoint* points = reinterpret_cast<SkPoint*>(malloc(sizeof(SkPoint) * (point + 1)));
>>>>>>> upstream/master
    if (!points)
        return HB_Err_Invalid_SubTable;
    // Skia does let us get a single point from the path.
    path.getPoints(points, point + 1);
    *xPos = SkScalarToHBFixed(points[point].fX);
    *yPos = SkScalarToHBFixed(points[point].fY);
    *resultingNumPoints = numPoints;
    delete points;

    return HB_Err_Ok;
}

static void getGlyphMetrics(HB_Font hbFont, HB_Glyph glyph, HB_GlyphMetrics* metrics)
{
<<<<<<< HEAD
    SkPaint* paint = static_cast<SkPaint*>(hbFont->userData);
    paint->setTextEncoding(SkPaint::kGlyphID_TextEncoding);

    uint16_t glyph16 = glyph;
    SkScalar width;
    SkRect bounds;
    paint->getTextWidths(&glyph16, sizeof(glyph16), &width, &bounds);
=======
    FontData* data = reinterpret_cast<FontData*>(hbFont->userData);
    SkPaint paint;
    setupPaintWithFontData(&paint, data);

    paint.setTextEncoding(SkPaint::kGlyphID_TextEncoding);
    uint16_t glyph16 = glyph;
    SkScalar width;
    SkRect bounds;
    paint.getTextWidths(&glyph16, sizeof(glyph16), &width, &bounds);
>>>>>>> upstream/master

    metrics->x = SkScalarToHBFixed(bounds.fLeft);
    metrics->y = SkScalarToHBFixed(bounds.fTop);
    metrics->width = SkScalarToHBFixed(bounds.width());
    metrics->height = SkScalarToHBFixed(bounds.height());

    metrics->xOffset = SkScalarToHBFixed(width);
    // We can't actually get the |y| correct because Skia doesn't export
    // the vertical advance. However, nor we do ever render vertical text at
    // the moment so it's unimportant.
    metrics->yOffset = 0;
}

static HB_Fixed getFontMetric(HB_Font hbFont, HB_FontMetric metric)
{
<<<<<<< HEAD
    SkPaint* paint = static_cast<SkPaint*>(hbFont->userData);

    SkPaint::FontMetrics skiaMetrics;
    paint->getFontMetrics(&skiaMetrics);
=======
    FontData* data = reinterpret_cast<FontData*>(hbFont->userData);
    SkPaint paint;
    setupPaintWithFontData(&paint, data);

    SkPaint::FontMetrics skiaMetrics;
    paint.getFontMetrics(&skiaMetrics);
>>>>>>> upstream/master

    switch (metric) {
    case HB_FontAscent:
        return SkScalarToHBFixed(-skiaMetrics.fAscent);
    // We don't support getting the rest of the metrics and Harfbuzz doesn't seem to need them.
    default:
        return 0;
    }
    return 0;
}

const HB_FontClass harfbuzzSkiaClass = {
    stringToGlyphs,
    glyphsToAdvances,
    canRender,
    getOutlinePoint,
    getGlyphMetrics,
    getFontMetric,
};

<<<<<<< HEAD
HB_Error harfbuzzSkiaGetTable(void* font, const HB_Tag tag, HB_Byte* buffer, HB_UInt* len)
{
    SkTypeface* typeface = static_cast<SkTypeface*>(font);

    if (!typeface) {
        ALOGD("Typeface cannot be null");
        return HB_Err_Invalid_Argument;
    }
    const size_t tableSize = SkFontHost::GetTableSize(typeface->uniqueID(), tag);
    if (!tableSize)
        return HB_Err_Invalid_Argument;
    // If Harfbuzz specified a NULL buffer then it's asking for the size of the table.
    if (!buffer) {
        *len = tableSize;
        return HB_Err_Ok;
    }

    if (*len < tableSize)
        return HB_Err_Invalid_Argument;
    SkFontHost::GetTableData(typeface->uniqueID(), tag, 0, tableSize, buffer);
    return HB_Err_Ok;
=======
HB_Error harfbuzzSkiaGetTable(void* voidface, const HB_Tag tag, HB_Byte* buffer, HB_UInt* len)
{
    return HB_Err_Invalid_Argument;
>>>>>>> upstream/master
}

}  // namespace android
