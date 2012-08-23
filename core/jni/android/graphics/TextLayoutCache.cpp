/*
 * Copyright (C) 2011 The Android Open Source Project
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

#define LOG_TAG "TextLayoutCache"

#include "TextLayoutCache.h"
#include "TextLayout.h"
<<<<<<< HEAD
#include "SkFontHost.h"
#include <unicode/unistr.h>
#include <unicode/normlzr.h>
#include <unicode/uchar.h>
=======
>>>>>>> upstream/master

extern "C" {
  #include "harfbuzz-unicode.h"
}

namespace android {

//--------------------------------------------------------------------------------------------------
<<<<<<< HEAD
#define TYPEFACE_ARABIC "/system/fonts/DroidNaskh-Regular-SystemUI.ttf"
#define TYPE_FACE_HEBREW_REGULAR "/system/fonts/DroidSansHebrew-Regular.ttf"
#define TYPE_FACE_HEBREW_BOLD "/system/fonts/DroidSansHebrew-Bold.ttf"
#define TYPEFACE_BENGALI "/system/fonts/Lohit-Bengali.ttf"
#define TYPEFACE_DEVANAGARI_REGULAR "/system/fonts/DroidSansDevanagari-Regular.ttf"
#define TYPEFACE_TAMIL_REGULAR "/system/fonts/DroidSansTamil-Regular.ttf"
#define TYPEFACE_TAMIL_BOLD "/system/fonts/DroidSansTamil-Bold.ttf"
#define TYPEFACE_THAI "/system/fonts/DroidSansThai.ttf"

ANDROID_SINGLETON_STATIC_INSTANCE(TextLayoutEngine);

//--------------------------------------------------------------------------------------------------

TextLayoutCache::TextLayoutCache(TextLayoutShaper* shaper) :
        mShaper(shaper),
        mCache(GenerationCache<TextLayoutCacheKey, sp<TextLayoutValue> >::kUnlimitedCapacity),
=======
#if USE_TEXT_LAYOUT_CACHE
    ANDROID_SINGLETON_STATIC_INSTANCE(TextLayoutCache);
#endif
//--------------------------------------------------------------------------------------------------

TextLayoutCache::TextLayoutCache() :
        mCache(GenerationCache<TextLayoutCacheKey, sp<TextLayoutCacheValue> >::kUnlimitedCapacity),
>>>>>>> upstream/master
        mSize(0), mMaxSize(MB(DEFAULT_TEXT_LAYOUT_CACHE_SIZE_IN_MB)),
        mCacheHitCount(0), mNanosecondsSaved(0) {
    init();
}

TextLayoutCache::~TextLayoutCache() {
    mCache.clear();
}

void TextLayoutCache::init() {
    mCache.setOnEntryRemovedListener(this);

    mDebugLevel = readRtlDebugLevel();
    mDebugEnabled = mDebugLevel & kRtlDebugCaches;
<<<<<<< HEAD
    ALOGD("Using debug level = %d - Debug Enabled = %d", mDebugLevel, mDebugEnabled);
=======
    LOGD("Using debug level: %d - Debug Enabled: %d", mDebugLevel, mDebugEnabled);
>>>>>>> upstream/master

    mCacheStartTime = systemTime(SYSTEM_TIME_MONOTONIC);

    if (mDebugEnabled) {
<<<<<<< HEAD
        ALOGD("Initialization is done - Start time = %lld", mCacheStartTime);
=======
        LOGD("Initialization is done - Start time: %lld", mCacheStartTime);
>>>>>>> upstream/master
    }

    mInitialized = true;
}

<<<<<<< HEAD
/**
 *  Callbacks
 */
void TextLayoutCache::operator()(TextLayoutCacheKey& text, sp<TextLayoutValue>& desc) {
    size_t totalSizeToDelete = text.getSize() + desc->getSize();
    mSize -= totalSizeToDelete;
    if (mDebugEnabled) {
        ALOGD("Cache value %p deleted, size = %d", desc.get(), totalSizeToDelete);
=======
/*
 * Size management
 */

uint32_t TextLayoutCache::getSize() {
    return mSize;
}

uint32_t TextLayoutCache::getMaxSize() {
    return mMaxSize;
}

void TextLayoutCache::setMaxSize(uint32_t maxSize) {
    mMaxSize = maxSize;
    removeOldests();
}

void TextLayoutCache::removeOldests() {
    while (mSize > mMaxSize) {
        mCache.removeOldest();
    }
}

/**
 *  Callbacks
 */
void TextLayoutCache::operator()(TextLayoutCacheKey& text, sp<TextLayoutCacheValue>& desc) {
    if (desc != NULL) {
        size_t totalSizeToDelete = text.getSize() + desc->getSize();
        mSize -= totalSizeToDelete;
        if (mDebugEnabled) {
            LOGD("Cache value deleted, size = %d", totalSizeToDelete);
        }
        desc.clear();
>>>>>>> upstream/master
    }
}

/*
 * Cache clearing
 */
void TextLayoutCache::clear() {
    mCache.clear();
}

/*
 * Caching
 */
<<<<<<< HEAD
sp<TextLayoutValue> TextLayoutCache::getValue(const SkPaint* paint,
=======
sp<TextLayoutCacheValue> TextLayoutCache::getValue(SkPaint* paint,
>>>>>>> upstream/master
            const jchar* text, jint start, jint count, jint contextCount, jint dirFlags) {
    AutoMutex _l(mLock);
    nsecs_t startTime = 0;
    if (mDebugEnabled) {
        startTime = systemTime(SYSTEM_TIME_MONOTONIC);
    }

    // Create the key
    TextLayoutCacheKey key(paint, text, start, count, contextCount, dirFlags);

    // Get value from cache if possible
<<<<<<< HEAD
    sp<TextLayoutValue> value = mCache.get(key);
=======
    sp<TextLayoutCacheValue> value = mCache.get(key);
>>>>>>> upstream/master

    // Value not found for the key, we need to add a new value in the cache
    if (value == NULL) {
        if (mDebugEnabled) {
            startTime = systemTime(SYSTEM_TIME_MONOTONIC);
        }

<<<<<<< HEAD
        value = new TextLayoutValue(contextCount);

        // Compute advances and store them
        mShaper->computeValues(value.get(), paint,
                reinterpret_cast<const UChar*>(text), start, count,
                size_t(contextCount), int(dirFlags));

        if (mDebugEnabled) {
            value->setElapsedTime(systemTime(SYSTEM_TIME_MONOTONIC) - startTime);
        }
=======
        value = new TextLayoutCacheValue();

        // Compute advances and store them
        value->computeValues(paint, text, start, count, contextCount, dirFlags);

        nsecs_t endTime = systemTime(SYSTEM_TIME_MONOTONIC);
>>>>>>> upstream/master

        // Don't bother to add in the cache if the entry is too big
        size_t size = key.getSize() + value->getSize();
        if (size <= mMaxSize) {
            // Cleanup to make some room if needed
            if (mSize + size > mMaxSize) {
                if (mDebugEnabled) {
<<<<<<< HEAD
                    ALOGD("Need to clean some entries for making some room for a new entry");
                }
                while (mSize + size > mMaxSize) {
                    // This will call the callback
                    bool removedOne = mCache.removeOldest();
                    LOG_ALWAYS_FATAL_IF(!removedOne, "The cache is non-empty but we "
                            "failed to remove the oldest entry.  "
                            "mSize = %u, size = %u, mMaxSize = %u, mCache.size() = %u",
                            mSize, size, mMaxSize, mCache.size());
=======
                    LOGD("Need to clean some entries for making some room for a new entry");
                }
                while (mSize + size > mMaxSize) {
                    // This will call the callback
                    mCache.removeOldest();
>>>>>>> upstream/master
                }
            }

            // Update current cache size
            mSize += size;

            // Copy the text when we insert the new entry
            key.internalTextCopy();
<<<<<<< HEAD

            bool putOne = mCache.put(key, value);
            LOG_ALWAYS_FATAL_IF(!putOne, "Failed to put an entry into the cache.  "
                    "This indicates that the cache already has an entry with the "
                    "same key but it should not since we checked earlier!"
                    " - start = %d, count = %d, contextCount = %d - Text = '%s'",
                    start, count, contextCount, String8(text + start, count).string());

            if (mDebugEnabled) {
                nsecs_t totalTime = systemTime(SYSTEM_TIME_MONOTONIC) - startTime;
                ALOGD("CACHE MISS: Added entry %p "
                        "with start = %d, count = %d, contextCount = %d, "
                        "entry size %d bytes, remaining space %d bytes"
                        " - Compute time %0.6f ms - Put time %0.6f ms - Text = '%s'",
                        value.get(), start, count, contextCount, size, mMaxSize - mSize,
                        value->getElapsedTime() * 0.000001f,
                        (totalTime - value->getElapsedTime()) * 0.000001f,
                        String8(text + start, count).string());
            }
        } else {
            if (mDebugEnabled) {
                ALOGD("CACHE MISS: Calculated but not storing entry because it is too big "
                        "with start = %d, count = %d, contextCount = %d, "
                        "entry size %d bytes, remaining space %d bytes"
                        " - Compute time %0.6f ms - Text = '%s'",
                        start, count, contextCount, size, mMaxSize - mSize,
                        value->getElapsedTime() * 0.000001f,
                        String8(text + start, count).string());
=======
            mCache.put(key, value);

            if (mDebugEnabled) {
                // Update timing information for statistics
                value->setElapsedTime(endTime - startTime);

                LOGD("CACHE MISS: Added entry with "
                        "count=%d, entry size %d bytes, remaining space %d bytes"
                        " - Compute time in nanos: %d - Text='%s' ",
                        count, size, mMaxSize - mSize, value->getElapsedTime(),
                        String8(text, count).string());
            }
        } else {
            if (mDebugEnabled) {
                LOGD("CACHE MISS: Calculated but not storing entry because it is too big "
                        "with start=%d count=%d contextCount=%d, "
                        "entry size %d bytes, remaining space %d bytes"
                        " - Compute time in nanos: %lld - Text='%s'",
                        start, count, contextCount, size, mMaxSize - mSize, endTime,
                        String8(text, count).string());
>>>>>>> upstream/master
            }
        }
    } else {
        // This is a cache hit, just log timestamp and user infos
        if (mDebugEnabled) {
            nsecs_t elapsedTimeThruCacheGet = systemTime(SYSTEM_TIME_MONOTONIC) - startTime;
            mNanosecondsSaved += (value->getElapsedTime() - elapsedTimeThruCacheGet);
            ++mCacheHitCount;

            if (value->getElapsedTime() > 0) {
                float deltaPercent = 100 * ((value->getElapsedTime() - elapsedTimeThruCacheGet)
                        / ((float)value->getElapsedTime()));
<<<<<<< HEAD
                ALOGD("CACHE HIT #%d with start = %d, count = %d, contextCount = %d"
                        "- Compute time %0.6f ms - "
                        "Cache get time %0.6f ms - Gain in percent: %2.2f - Text = '%s'",
                        mCacheHitCount, start, count, contextCount,
                        value->getElapsedTime() * 0.000001f,
                        elapsedTimeThruCacheGet * 0.000001f,
                        deltaPercent,
                        String8(text + start, count).string());
=======
                LOGD("CACHE HIT #%d with start=%d count=%d contextCount=%d"
                        "- Compute time in nanos: %d - "
                        "Cache get time in nanos: %lld - Gain in percent: %2.2f - Text='%s' ",
                        mCacheHitCount, start, count, contextCount,
                        value->getElapsedTime(), elapsedTimeThruCacheGet, deltaPercent,
                        String8(text, count).string());
>>>>>>> upstream/master
            }
            if (mCacheHitCount % DEFAULT_DUMP_STATS_CACHE_HIT_INTERVAL == 0) {
                dumpCacheStats();
            }
        }
    }
    return value;
}

void TextLayoutCache::dumpCacheStats() {
    float remainingPercent = 100 * ((mMaxSize - mSize) / ((float)mMaxSize));
    float timeRunningInSec = (systemTime(SYSTEM_TIME_MONOTONIC) - mCacheStartTime) / 1000000000;
<<<<<<< HEAD

    size_t bytes = 0;
    size_t cacheSize = mCache.size();
    for (size_t i = 0; i < cacheSize; i++) {
        bytes += mCache.getKeyAt(i).getSize() + mCache.getValueAt(i)->getSize();
    }

    ALOGD("------------------------------------------------");
    ALOGD("Cache stats");
    ALOGD("------------------------------------------------");
    ALOGD("pid       : %d", getpid());
    ALOGD("running   : %.0f seconds", timeRunningInSec);
    ALOGD("entries   : %d", cacheSize);
    ALOGD("max size  : %d bytes", mMaxSize);
    ALOGD("used      : %d bytes according to mSize, %d bytes actual", mSize, bytes);
    ALOGD("remaining : %d bytes or %2.2f percent", mMaxSize - mSize, remainingPercent);
    ALOGD("hits      : %d", mCacheHitCount);
    ALOGD("saved     : %0.6f ms", mNanosecondsSaved * 0.000001f);
    ALOGD("------------------------------------------------");
=======
    LOGD("------------------------------------------------");
    LOGD("Cache stats");
    LOGD("------------------------------------------------");
    LOGD("pid       : %d", getpid());
    LOGD("running   : %.0f seconds", timeRunningInSec);
    LOGD("entries   : %d", mCache.size());
    LOGD("size      : %d bytes", mMaxSize);
    LOGD("remaining : %d bytes or %2.2f percent", mMaxSize - mSize, remainingPercent);
    LOGD("hits      : %d", mCacheHitCount);
    LOGD("saved     : %lld milliseconds", mNanosecondsSaved / 1000000);
    LOGD("------------------------------------------------");
>>>>>>> upstream/master
}

/**
 * TextLayoutCacheKey
 */
TextLayoutCacheKey::TextLayoutCacheKey(): text(NULL), start(0), count(0), contextCount(0),
        dirFlags(0), typeface(NULL), textSize(0), textSkewX(0), textScaleX(0), flags(0),
        hinting(SkPaint::kNo_Hinting)  {
}

TextLayoutCacheKey::TextLayoutCacheKey(const SkPaint* paint, const UChar* text,
        size_t start, size_t count, size_t contextCount, int dirFlags) :
            text(text), start(start), count(count), contextCount(contextCount),
            dirFlags(dirFlags) {
    typeface = paint->getTypeface();
    textSize = paint->getTextSize();
    textSkewX = paint->getTextSkewX();
    textScaleX = paint->getTextScaleX();
    flags = paint->getFlags();
    hinting = paint->getHinting();
}

TextLayoutCacheKey::TextLayoutCacheKey(const TextLayoutCacheKey& other) :
        text(NULL),
        textCopy(other.textCopy),
        start(other.start),
        count(other.count),
        contextCount(other.contextCount),
        dirFlags(other.dirFlags),
        typeface(other.typeface),
        textSize(other.textSize),
        textSkewX(other.textSkewX),
        textScaleX(other.textScaleX),
        flags(other.flags),
        hinting(other.hinting) {
    if (other.text) {
        textCopy.setTo(other.text, other.contextCount);
    }
}

int TextLayoutCacheKey::compare(const TextLayoutCacheKey& lhs, const TextLayoutCacheKey& rhs) {
    int deltaInt = lhs.start - rhs.start;
    if (deltaInt != 0) return (deltaInt);

    deltaInt = lhs.count - rhs.count;
    if (deltaInt != 0) return (deltaInt);

    deltaInt = lhs.contextCount - rhs.contextCount;
    if (deltaInt != 0) return (deltaInt);

    if (lhs.typeface < rhs.typeface) return -1;
    if (lhs.typeface > rhs.typeface) return +1;

    if (lhs.textSize < rhs.textSize) return -1;
    if (lhs.textSize > rhs.textSize) return +1;

    if (lhs.textSkewX < rhs.textSkewX) return -1;
    if (lhs.textSkewX > rhs.textSkewX) return +1;

    if (lhs.textScaleX < rhs.textScaleX) return -1;
    if (lhs.textScaleX > rhs.textScaleX) return +1;

    deltaInt = lhs.flags - rhs.flags;
    if (deltaInt != 0) return (deltaInt);

    deltaInt = lhs.hinting - rhs.hinting;
    if (deltaInt != 0) return (deltaInt);

    deltaInt = lhs.dirFlags - rhs.dirFlags;
    if (deltaInt) return (deltaInt);

    return memcmp(lhs.getText(), rhs.getText(), lhs.contextCount * sizeof(UChar));
}

void TextLayoutCacheKey::internalTextCopy() {
    textCopy.setTo(text, contextCount);
    text = NULL;
}

<<<<<<< HEAD
size_t TextLayoutCacheKey::getSize() const {
=======
size_t TextLayoutCacheKey::getSize() {
>>>>>>> upstream/master
    return sizeof(TextLayoutCacheKey) + sizeof(UChar) * contextCount;
}

/**
 * TextLayoutCacheValue
 */
<<<<<<< HEAD
TextLayoutValue::TextLayoutValue(size_t contextCount) :
        mTotalAdvance(0), mElapsedTime(0) {
    // Give a hint for advances and glyphs vectors size
    mAdvances.setCapacity(contextCount);
    mGlyphs.setCapacity(contextCount);
}

size_t TextLayoutValue::getSize() const {
    return sizeof(TextLayoutValue) + sizeof(jfloat) * mAdvances.capacity() +
            sizeof(jchar) * mGlyphs.capacity();
}

void TextLayoutValue::setElapsedTime(uint32_t time) {
    mElapsedTime = time;
}

uint32_t TextLayoutValue::getElapsedTime() {
    return mElapsedTime;
}

TextLayoutShaper::TextLayoutShaper() : mShaperItemGlyphArraySize(0) {
    init();

    mFontRec.klass = &harfbuzzSkiaClass;
    mFontRec.userData = 0;
=======
TextLayoutCacheValue::TextLayoutCacheValue() :
        mTotalAdvance(0), mElapsedTime(0) {
}

void TextLayoutCacheValue::setElapsedTime(uint32_t time) {
    mElapsedTime = time;
}

uint32_t TextLayoutCacheValue::getElapsedTime() {
    return mElapsedTime;
}

void TextLayoutCacheValue::computeValues(SkPaint* paint, const UChar* chars,
        size_t start, size_t count, size_t contextCount, int dirFlags) {
    // Give a hint for advances, glyphs and log clusters vectors size
    mAdvances.setCapacity(contextCount);
    mGlyphs.setCapacity(contextCount);

    computeValuesWithHarfbuzz(paint, chars, start, count, contextCount, dirFlags,
            &mAdvances, &mTotalAdvance, &mGlyphs);
#if DEBUG_ADVANCES
    LOGD("Advances - start=%d, count=%d, countextCount=%d, totalAdvance=%f", start, count,
            contextCount, mTotalAdvance);
#endif
}

size_t TextLayoutCacheValue::getSize() {
    return sizeof(TextLayoutCacheValue) + sizeof(jfloat) * mAdvances.capacity() +
            sizeof(jchar) * mGlyphs.capacity();
}

void TextLayoutCacheValue::initShaperItem(HB_ShaperItem& shaperItem, HB_FontRec* font,
        FontData* fontData, SkPaint* paint, const UChar* chars, size_t contextCount) {
    // Zero the Shaper struct
    memset(&shaperItem, 0, sizeof(shaperItem));

    font->klass = &harfbuzzSkiaClass;
    font->userData = 0;
>>>>>>> upstream/master

    // The values which harfbuzzSkiaClass returns are already scaled to
    // pixel units, so we just set all these to one to disable further
    // scaling.
<<<<<<< HEAD
    mFontRec.x_ppem = 1;
    mFontRec.y_ppem = 1;
    mFontRec.x_scale = 1;
    mFontRec.y_scale = 1;

    memset(&mShaperItem, 0, sizeof(mShaperItem));

    mShaperItem.font = &mFontRec;
    mShaperItem.font->userData = &mShapingPaint;
}

void TextLayoutShaper::init() {
    mDefaultTypeface = SkFontHost::CreateTypeface(NULL, NULL, NULL, 0, SkTypeface::kNormal);
    mArabicTypeface = NULL;
    mHebrewRegularTypeface = NULL;
    mHebrewBoldTypeface = NULL;
    mBengaliTypeface = NULL;
    mThaiTypeface = NULL;
    mDevanagariRegularTypeface = NULL;
    mTamilRegularTypeface = NULL;
    mTamilBoldTypeface = NULL;
}

void TextLayoutShaper::unrefTypefaces() {
    SkSafeUnref(mDefaultTypeface);
    SkSafeUnref(mArabicTypeface);
    SkSafeUnref(mHebrewRegularTypeface);
    SkSafeUnref(mHebrewBoldTypeface);
    SkSafeUnref(mBengaliTypeface);
    SkSafeUnref(mThaiTypeface);
    SkSafeUnref(mDevanagariRegularTypeface);
    SkSafeUnref(mTamilRegularTypeface);
    SkSafeUnref(mTamilBoldTypeface);
}

TextLayoutShaper::~TextLayoutShaper() {
    unrefTypefaces();
    deleteShaperItemGlyphArrays();
}

void TextLayoutShaper::computeValues(TextLayoutValue* value, const SkPaint* paint, const UChar* chars,
        size_t start, size_t count, size_t contextCount, int dirFlags) {

    computeValues(paint, chars, start, count, contextCount, dirFlags,
            &value->mAdvances, &value->mTotalAdvance, &value->mGlyphs);
#if DEBUG_ADVANCES
    ALOGD("Advances - start = %d, count = %d, contextCount = %d, totalAdvance = %f", start, count,
            contextCount, value->mTotalAdvance);
#endif
}

void TextLayoutShaper::computeValues(const SkPaint* paint, const UChar* chars,
=======
    font->x_ppem = 1;
    font->y_ppem = 1;
    font->x_scale = 1;
    font->y_scale = 1;

    // Reset kerning
    shaperItem.kerning_applied = false;

    // Define font data
    fontData->typeFace = paint->getTypeface();
    fontData->textSize = paint->getTextSize();
    fontData->textSkewX = paint->getTextSkewX();
    fontData->textScaleX = paint->getTextScaleX();
    fontData->flags = paint->getFlags();
    fontData->hinting = paint->getHinting();

    shaperItem.font = font;
    shaperItem.font->userData = fontData;

    shaperItem.face = HB_NewFace(NULL, harfbuzzSkiaGetTable);

    // We cannot know, ahead of time, how many glyphs a given script run
    // will produce. We take a guess that script runs will not produce more
    // than twice as many glyphs as there are code points plus a bit of
    // padding and fallback if we find that we are wrong.
    createGlyphArrays(shaperItem, (contextCount + 2) * 2);

    // Set the string properties
    shaperItem.string = chars;
    shaperItem.stringLength = contextCount;
}

void TextLayoutCacheValue::freeShaperItem(HB_ShaperItem& shaperItem) {
    deleteGlyphArrays(shaperItem);
    HB_FreeFace(shaperItem.face);
}

void TextLayoutCacheValue::shapeRun(HB_ShaperItem& shaperItem, size_t start, size_t count,
        bool isRTL) {
    // Update Harfbuzz Shaper
    shaperItem.item.pos = start;
    shaperItem.item.length = count;
    shaperItem.item.bidiLevel = isRTL;

    shaperItem.item.script = isRTL ? HB_Script_Arabic : HB_Script_Common;

    // Shape
    assert(shaperItem.item.length > 0); // Harfbuzz will overwrite other memory if length is 0.
    while (!HB_ShapeItem(&shaperItem)) {
        // We overflowed our arrays. Resize and retry.
        // HB_ShapeItem fills in shaperItem.num_glyphs with the needed size.
        deleteGlyphArrays(shaperItem);
        createGlyphArrays(shaperItem, shaperItem.num_glyphs << 1);
    }
}

void TextLayoutCacheValue::computeValuesWithHarfbuzz(SkPaint* paint, const UChar* chars,
>>>>>>> upstream/master
        size_t start, size_t count, size_t contextCount, int dirFlags,
        Vector<jfloat>* const outAdvances, jfloat* outTotalAdvance,
        Vector<jchar>* const outGlyphs) {
        if (!count) {
            *outTotalAdvance = 0;
            return;
        }

        UBiDiLevel bidiReq = 0;
        bool forceLTR = false;
        bool forceRTL = false;

        switch (dirFlags) {
            case kBidi_LTR: bidiReq = 0; break; // no ICU constant, canonical LTR level
            case kBidi_RTL: bidiReq = 1; break; // no ICU constant, canonical RTL level
            case kBidi_Default_LTR: bidiReq = UBIDI_DEFAULT_LTR; break;
            case kBidi_Default_RTL: bidiReq = UBIDI_DEFAULT_RTL; break;
            case kBidi_Force_LTR: forceLTR = true; break; // every char is LTR
            case kBidi_Force_RTL: forceRTL = true; break; // every char is RTL
        }

<<<<<<< HEAD
=======
        HB_ShaperItem shaperItem;
        HB_FontRec font;
        FontData fontData;

        // Initialize Harfbuzz Shaper
        initShaperItem(shaperItem, &font, &fontData, paint, chars, contextCount);

>>>>>>> upstream/master
        bool useSingleRun = false;
        bool isRTL = forceRTL;
        if (forceLTR || forceRTL) {
            useSingleRun = true;
        } else {
            UBiDi* bidi = ubidi_open();
            if (bidi) {
                UErrorCode status = U_ZERO_ERROR;
#if DEBUG_GLYPHS
<<<<<<< HEAD
                ALOGD("******** ComputeValues -- start");
                ALOGD("      -- string = '%s'", String8(chars + start, count).string());
                ALOGD("      -- start = %d", start);
                ALOGD("      -- count = %d", count);
                ALOGD("      -- contextCount = %d", contextCount);
                ALOGD("      -- bidiReq = %d", bidiReq);
=======
                LOGD("computeValuesWithHarfbuzz -- bidiReq=%d", bidiReq);
>>>>>>> upstream/master
#endif
                ubidi_setPara(bidi, chars, contextCount, bidiReq, NULL, &status);
                if (U_SUCCESS(status)) {
                    int paraDir = ubidi_getParaLevel(bidi) & kDirection_Mask; // 0 if ltr, 1 if rtl
                    ssize_t rc = ubidi_countRuns(bidi, &status);
#if DEBUG_GLYPHS
<<<<<<< HEAD
                    ALOGD("      -- dirFlags = %d", dirFlags);
                    ALOGD("      -- paraDir = %d", paraDir);
                    ALOGD("      -- run-count = %d", int(rc));
=======
                    LOGD("computeValuesWithHarfbuzz -- dirFlags=%d run-count=%d paraDir=%d",
                            dirFlags, rc, paraDir);
>>>>>>> upstream/master
#endif
                    if (U_SUCCESS(status) && rc == 1) {
                        // Normal case: one run, status is ok
                        isRTL = (paraDir == 1);
                        useSingleRun = true;
                    } else if (!U_SUCCESS(status) || rc < 1) {
<<<<<<< HEAD
                        ALOGW("Need to force to single run -- string = '%s',"
                                " status = %d, rc = %d",
                                String8(chars + start, count).string(), status, int(rc));
=======
                        LOGW("computeValuesWithHarfbuzz -- need to force to single run");
>>>>>>> upstream/master
                        isRTL = (paraDir == 1);
                        useSingleRun = true;
                    } else {
                        int32_t end = start + count;
                        for (size_t i = 0; i < size_t(rc); ++i) {
                            int32_t startRun = -1;
                            int32_t lengthRun = -1;
                            UBiDiDirection runDir = ubidi_getVisualRun(bidi, i, &startRun, &lengthRun);

                            if (startRun == -1 || lengthRun == -1) {
                                // Something went wrong when getting the visual run, need to clear
                                // already computed data before doing a single run pass
<<<<<<< HEAD
                                ALOGW("Visual run is not valid");
=======
                                LOGW("computeValuesWithHarfbuzz -- visual run is not valid");
>>>>>>> upstream/master
                                outGlyphs->clear();
                                outAdvances->clear();
                                *outTotalAdvance = 0;
                                isRTL = (paraDir == 1);
                                useSingleRun = true;
                                break;
                            }

                            if (startRun >= end) {
                                continue;
                            }
                            int32_t endRun = startRun + lengthRun;
                            if (endRun <= int32_t(start)) {
                                continue;
                            }
                            if (startRun < int32_t(start)) {
                                startRun = int32_t(start);
                            }
                            if (endRun > end) {
                                endRun = end;
                            }

                            lengthRun = endRun - startRun;
                            isRTL = (runDir == UBIDI_RTL);
                            jfloat runTotalAdvance = 0;
#if DEBUG_GLYPHS
<<<<<<< HEAD
                            ALOGD("Processing Bidi Run = %d -- run-start = %d, run-len = %d, isRTL = %d",
                                    i, startRun, lengthRun, isRTL);
#endif
                            computeRunValues(paint, chars + startRun, lengthRun, isRTL,
=======
                            LOGD("computeValuesWithHarfbuzz -- run-start=%d run-len=%d isRTL=%d",
                                    startRun, lengthRun, isRTL);
#endif
                            computeRunValuesWithHarfbuzz(shaperItem, paint,
                                    startRun, lengthRun, isRTL,
>>>>>>> upstream/master
                                    outAdvances, &runTotalAdvance, outGlyphs);

                            *outTotalAdvance += runTotalAdvance;
                        }
                    }
                } else {
<<<<<<< HEAD
                    ALOGW("Cannot set Para");
=======
                    LOGW("computeValuesWithHarfbuzz -- cannot set Para");
>>>>>>> upstream/master
                    useSingleRun = true;
                    isRTL = (bidiReq = 1) || (bidiReq = UBIDI_DEFAULT_RTL);
                }
                ubidi_close(bidi);
            } else {
<<<<<<< HEAD
                ALOGW("Cannot ubidi_open()");
=======
                LOGW("computeValuesWithHarfbuzz -- cannot ubidi_open()");
>>>>>>> upstream/master
                useSingleRun = true;
                isRTL = (bidiReq = 1) || (bidiReq = UBIDI_DEFAULT_RTL);
            }
        }

        // Default single run case
        if (useSingleRun){
#if DEBUG_GLYPHS
<<<<<<< HEAD
            ALOGD("Using a SINGLE BiDi Run "
                    "-- run-start = %d, run-len = %d, isRTL = %d", start, count, isRTL);
#endif
            computeRunValues(paint, chars + start, count, isRTL,
                    outAdvances, outTotalAdvance, outGlyphs);
        }

#if DEBUG_GLYPHS
        ALOGD("      -- Total returned glyphs-count = %d", outGlyphs->size());
        ALOGD("******** ComputeValues -- end");
=======
            LOGD("computeValuesWithHarfbuzz -- Using a SINGLE Run "
                    "-- run-start=%d run-len=%d isRTL=%d", start, count, isRTL);
#endif
            computeRunValuesWithHarfbuzz(shaperItem, paint,
                    start, count, isRTL,
                    outAdvances, outTotalAdvance, outGlyphs);
        }

        // Cleaning
        freeShaperItem(shaperItem);

#if DEBUG_GLYPHS
        LOGD("computeValuesWithHarfbuzz -- total-glyphs-count=%d", outGlyphs->size());
>>>>>>> upstream/master
#endif
}

static void logGlyphs(HB_ShaperItem shaperItem) {
<<<<<<< HEAD
    ALOGD("         -- glyphs count=%d", shaperItem.num_glyphs);
    for (size_t i = 0; i < shaperItem.num_glyphs; i++) {
        ALOGD("         -- glyph[%d] = %d, offset.x = %0.2f, offset.y = %0.2f", i,
                shaperItem.glyphs[i],
=======
    LOGD("Got glyphs - count=%d", shaperItem.num_glyphs);
    for (size_t i = 0; i < shaperItem.num_glyphs; i++) {
        LOGD("      glyph[%d]=%d - offset.x=%f offset.y=%f", i, shaperItem.glyphs[i],
>>>>>>> upstream/master
                HBFixedToFloat(shaperItem.offsets[i].x),
                HBFixedToFloat(shaperItem.offsets[i].y));
    }
}

<<<<<<< HEAD
void TextLayoutShaper::computeRunValues(const SkPaint* paint, const UChar* chars,
        size_t count, bool isRTL,
        Vector<jfloat>* const outAdvances, jfloat* outTotalAdvance,
        Vector<jchar>* const outGlyphs) {
    if (!count) {
        // We cannot shape an empty run.
=======
void TextLayoutCacheValue::computeRunValuesWithHarfbuzz(HB_ShaperItem& shaperItem, SkPaint* paint,
        size_t start, size_t count, bool isRTL,
        Vector<jfloat>* const outAdvances, jfloat* outTotalAdvance,
        Vector<jchar>* const outGlyphs) {
    if (!count) {
>>>>>>> upstream/master
        *outTotalAdvance = 0;
        return;
    }

<<<<<<< HEAD
    // To be filled in later
    for (size_t i = 0; i < count; i++) {
        outAdvances->add(0);
    }
    UErrorCode error = U_ZERO_ERROR;
    bool useNormalizedString = false;
    for (ssize_t i = count - 1; i >= 0; --i) {
        UChar ch1 = chars[i];
        if (::ublock_getCode(ch1) == UBLOCK_COMBINING_DIACRITICAL_MARKS) {
            // So we have found a diacritic, let's get now the main code point which is paired
            // with it. As we can have several diacritics in a row, we need to iterate back again
#if DEBUG_GLYPHS
            ALOGD("The BiDi run '%s' is containing a Diacritic at position %d",
                    String8(chars, count).string(), int(i));
#endif
            ssize_t j = i - 1;
            for (; j >= 0;  --j) {
                UChar ch2 = chars[j];
                if (::ublock_getCode(ch2) != UBLOCK_COMBINING_DIACRITICAL_MARKS) {
                    break;
                }
            }

            // We could not found the main code point, so we will just use the initial chars
            if (j < 0) {
                break;
            }

#if DEBUG_GLYPHS
            ALOGD("Found main code point at index %d", int(j));
#endif
            // We found the main code point, so we can normalize the "chunk" and fill
            // the remaining with ZWSP so that the Paint.getTextWidth() APIs will still be able
            // to get one advance per char
            mBuffer.remove();
            Normalizer::normalize(UnicodeString(chars + j, i - j + 1),
                    UNORM_NFC, 0 /* no options */, mBuffer, error);
            if (U_SUCCESS(error)) {
                if (!useNormalizedString) {
                    useNormalizedString = true;
                    mNormalizedString.setTo(false /* not terminated*/, chars, count);
                }
                // Set the normalized chars
                for (ssize_t k = j; k < j + mBuffer.length(); ++k) {
                    mNormalizedString.setCharAt(k, mBuffer.charAt(k - j));
                }
                // Fill the remain part with ZWSP (ZWNJ and ZWJ would lead to weird results
                // because some fonts are missing those glyphs)
                for (ssize_t k = j + mBuffer.length(); k <= i; ++k) {
                    mNormalizedString.setCharAt(k, UNICODE_ZWSP);
                }
            }
            i = j - 1;
        }
    }

    // Reverse "BiDi mirrored chars" in RTL mode only
    // See: http://www.unicode.org/Public/6.0.0/ucd/extracted/DerivedBinaryProperties.txt
    // This is a workaround because Harfbuzz is not able to do mirroring in all cases and
    // script-run splitting with Harfbuzz is splitting on parenthesis
    if (isRTL) {
        for (ssize_t i = 0; i < ssize_t(count); i++) {
            UChar32 ch = chars[i];
            if (!u_isMirrored(ch)) continue;
            if (!useNormalizedString) {
                useNormalizedString = true;
                mNormalizedString.setTo(false /* not terminated*/, chars, count);
            }
            UChar result =  (UChar) u_charMirror(ch);
            mNormalizedString.setCharAt(i, result);
#if DEBUG_GLYPHS
            ALOGD("Rewriting codepoint '%d' to '%d' at position %d",
                    ch, mNormalizedString[i], int(i));
#endif
        }
    }

#if DEBUG_GLYPHS
    if (useNormalizedString) {
        ALOGD("Will use normalized string '%s', length = %d",
                    String8(mNormalizedString.getTerminatedBuffer(),
                            mNormalizedString.length()).string(),
                    mNormalizedString.length());
    } else {
        ALOGD("Normalization is not needed or cannot be done, using initial string");
    }
#endif

    assert(mNormalizedString.length() == count);

    // Set the string properties
    mShaperItem.string = useNormalizedString ? mNormalizedString.getTerminatedBuffer() : chars;
    mShaperItem.stringLength = count;

    // Define shaping paint properties
    mShapingPaint.setTextSize(paint->getTextSize());
    mShapingPaint.setTextSkewX(paint->getTextSkewX());
    mShapingPaint.setTextScaleX(paint->getTextScaleX());
    mShapingPaint.setFlags(paint->getFlags());
    mShapingPaint.setHinting(paint->getHinting());

    // Split the BiDi run into Script runs. Harfbuzz will populate the pos, length and script
    // into the shaperItem
    ssize_t indexFontRun = isRTL ? mShaperItem.stringLength - 1 : 0;
    unsigned numCodePoints = 0;
    jfloat totalAdvance = 0;
    while ((isRTL) ?
            hb_utf16_script_run_prev(&numCodePoints, &mShaperItem.item, mShaperItem.string,
                    mShaperItem.stringLength, &indexFontRun):
            hb_utf16_script_run_next(&numCodePoints, &mShaperItem.item, mShaperItem.string,
                    mShaperItem.stringLength, &indexFontRun)) {

        ssize_t startScriptRun = mShaperItem.item.pos;
        size_t countScriptRun = mShaperItem.item.length;
        ssize_t endScriptRun = startScriptRun + countScriptRun;

#if DEBUG_GLYPHS
        ALOGD("-------- Start of Script Run --------");
        ALOGD("Shaping Script Run with");
        ALOGD("         -- isRTL = %d", isRTL);
        ALOGD("         -- HB script = %d", mShaperItem.item.script);
        ALOGD("         -- startFontRun = %d", int(startScriptRun));
        ALOGD("         -- endFontRun = %d", int(endScriptRun));
        ALOGD("         -- countFontRun = %d", countScriptRun);
        ALOGD("         -- run = '%s'", String8(chars + startScriptRun, countScriptRun).string());
        ALOGD("         -- string = '%s'", String8(chars, count).string());
#endif

        // Initialize Harfbuzz Shaper and get the base glyph count for offsetting the glyphIDs
        // and shape the Font run
        size_t glyphBaseCount = shapeFontRun(paint, isRTL);

#if DEBUG_GLYPHS
        ALOGD("Got from Harfbuzz");
        ALOGD("         -- glyphBaseCount = %d", glyphBaseCount);
        ALOGD("         -- num_glypth = %d", mShaperItem.num_glyphs);
        ALOGD("         -- kerning_applied = %d", mShaperItem.kerning_applied);
        ALOGD("         -- isDevKernText = %d", paint->isDevKernText());

        logGlyphs(mShaperItem);
#endif

        if (mShaperItem.advances == NULL || mShaperItem.num_glyphs == 0) {
#if DEBUG_GLYPHS
            ALOGD("Advances array is empty or num_glypth = 0");
#endif
            continue;
        }

#if DEBUG_GLYPHS
        ALOGD("Returned logclusters");
        for (size_t i = 0; i < mShaperItem.num_glyphs; i++) {
            ALOGD("         -- lc[%d] = %d, hb-adv[%d] = %0.2f", i, mShaperItem.log_clusters[i],
                    i, HBFixedToFloat(mShaperItem.advances[i]));
        }
#endif
        // Get Advances and their total
        jfloat currentAdvance = HBFixedToFloat(mShaperItem.advances[mShaperItem.log_clusters[0]]);
        jfloat totalFontRunAdvance = currentAdvance;
        outAdvances->replaceAt(currentAdvance, startScriptRun);
        for (size_t i = 1; i < countScriptRun; i++) {
            size_t clusterPrevious = mShaperItem.log_clusters[i - 1];
            size_t cluster = mShaperItem.log_clusters[i];
            if (cluster != clusterPrevious) {
                currentAdvance = HBFixedToFloat(mShaperItem.advances[mShaperItem.log_clusters[i]]);
                outAdvances->replaceAt(currentAdvance, startScriptRun + i);
            }
        }
        // TODO: can be removed and go back in the previous loop when Harfbuzz log clusters are fixed
        for (size_t i = 1; i < mShaperItem.num_glyphs; i++) {
            currentAdvance = HBFixedToFloat(mShaperItem.advances[i]);
            totalFontRunAdvance += currentAdvance;
        }
        totalAdvance += totalFontRunAdvance;

#if DEBUG_ADVANCES
        ALOGD("Returned advances");
        for (size_t i = 0; i < countScriptRun; i++) {
            ALOGD("         -- hb-adv[%d] = %0.2f, log_clusters = %d, total = %0.2f", i,
                    (*outAdvances)[i], mShaperItem.log_clusters[i], totalFontRunAdvance);
        }
#endif

        // Get Glyphs and reverse them in place if RTL
        if (outGlyphs) {
            size_t countGlyphs = mShaperItem.num_glyphs;
#if DEBUG_GLYPHS
            ALOGD("Returned script run glyphs -- count = %d", countGlyphs);
#endif
            for (size_t i = 0; i < countGlyphs; i++) {
                jchar glyph = glyphBaseCount +
                        (jchar) mShaperItem.glyphs[(!isRTL) ? i : countGlyphs - 1 - i];
#if DEBUG_GLYPHS
                ALOGD("         -- glyph[%d] = %d", i, glyph);
#endif
                outGlyphs->add(glyph);
            }
        }
    }

    *outTotalAdvance = totalAdvance;

#if DEBUG_GLYPHS
    ALOGD("-------- End of Script Run --------");
#endif
}

/**
 * Return the first typeface in the logical change, starting with this typeface,
 * that contains the specified unichar, or NULL if none is found.
 * 
 * Note that this function does _not_ increment the reference count on the typeface, as the
 * assumption is that its lifetime is managed elsewhere - in particular, the fallback typefaces
 * for the default font live in a global cache.
 */
SkTypeface* TextLayoutShaper::typefaceForUnichar(const SkPaint* paint, SkTypeface* typeface,
        SkUnichar unichar, HB_Script script) {
    // Set the correct Typeface depending on the script
    switch (script) {
    case HB_Script_Arabic:
        typeface = getCachedTypeface(&mArabicTypeface, TYPEFACE_ARABIC);
#if DEBUG_GLYPHS
        ALOGD("Using Arabic Typeface");
#endif
        break;

    case HB_Script_Hebrew:
        if (typeface) {
            switch (typeface->style()) {
            case SkTypeface::kBold:
            case SkTypeface::kBoldItalic:
                typeface = getCachedTypeface(&mHebrewBoldTypeface, TYPE_FACE_HEBREW_BOLD);
#if DEBUG_GLYPHS
                ALOGD("Using Hebrew Bold/BoldItalic Typeface");
#endif
                break;

            case SkTypeface::kNormal:
            case SkTypeface::kItalic:
            default:
                typeface = getCachedTypeface(&mHebrewRegularTypeface, TYPE_FACE_HEBREW_REGULAR);
#if DEBUG_GLYPHS
                ALOGD("Using Hebrew Regular/Italic Typeface");
#endif
                break;
            }
        } else {
            typeface = getCachedTypeface(&mHebrewRegularTypeface, TYPE_FACE_HEBREW_REGULAR);
#if DEBUG_GLYPHS
            ALOGD("Using Hebrew Regular Typeface");
#endif
        }
        break;

    case HB_Script_Bengali:
        typeface = getCachedTypeface(&mBengaliTypeface, TYPEFACE_BENGALI);
#if DEBUG_GLYPHS
        ALOGD("Using Bengali Typeface");
#endif
        break;

    case HB_Script_Thai:
        typeface = getCachedTypeface(&mThaiTypeface, TYPEFACE_THAI);
#if DEBUG_GLYPHS
        ALOGD("Using Thai Typeface");
#endif
        break;

    case HB_Script_Devanagari:
       typeface = getCachedTypeface(&mDevanagariRegularTypeface, TYPEFACE_DEVANAGARI_REGULAR);
#if DEBUG_GLYPHS
       ALOGD("Using Devanagari Regular Typeface");
#endif
        break;

    case HB_Script_Tamil:
        if (typeface) {
            switch (typeface->style()) {
            case SkTypeface::kBold:
            case SkTypeface::kBoldItalic:
                typeface = getCachedTypeface(&mTamilBoldTypeface, TYPEFACE_TAMIL_BOLD);
#if DEBUG_GLYPHS
                ALOGD("Using Tamil Bold Typeface");
#endif
                break;

            case SkTypeface::kNormal:
            case SkTypeface::kItalic:
            default:
                typeface = getCachedTypeface(&mTamilRegularTypeface, TYPEFACE_TAMIL_REGULAR);
#if DEBUG_GLYPHS
                ALOGD("Using Tamil Regular Typeface");
#endif
                break;
            }
        } else {
            typeface = getCachedTypeface(&mTamilRegularTypeface, TYPEFACE_TAMIL_REGULAR);
#if DEBUG_GLYPHS
            ALOGD("Using Tamil Regular Typeface");
#endif
        }
        break;

    default:
#if DEBUG_GLYPHS
        if (typeface) {
            ALOGD("Using Paint Typeface");
        }
#endif
        break;
    }
    return typeface;
}

size_t TextLayoutShaper::shapeFontRun(const SkPaint* paint, bool isRTL) {
    // Reset kerning
    mShaperItem.kerning_applied = false;

    // Update Harfbuzz Shaper
    mShaperItem.item.bidiLevel = isRTL;

    SkTypeface* typeface = paint->getTypeface();

    // Get the glyphs base count for offsetting the glyphIDs returned by Harfbuzz
    // This is needed as the Typeface used for shaping can be not the default one
    // when we are shaping any script that needs to use a fallback Font.
    // If we are a "common" script we dont need to shift
    size_t baseGlyphCount = 0;
    SkUnichar firstUnichar = 0;
    switch (mShaperItem.item.script) {
    case HB_Script_Arabic:
    case HB_Script_Hebrew:
    case HB_Script_Bengali:
    case HB_Script_Devanagari:
    case HB_Script_Tamil:
    case HB_Script_Thai:{
        const uint16_t* text16 = (const uint16_t*)(mShaperItem.string + mShaperItem.item.pos);
        const uint16_t* text16End = text16 + mShaperItem.item.length;
        firstUnichar = SkUTF16_NextUnichar(&text16);
        while (firstUnichar == ' ' && text16 < text16End) {
            firstUnichar = SkUTF16_NextUnichar(&text16);
        }
        baseGlyphCount = paint->getBaseGlyphCount(firstUnichar);
        break;
    }
    default:
        break;
    }

    // We test the baseGlyphCount to see if the typeface supports the requested script
    if (baseGlyphCount != 0) {
        typeface = typefaceForUnichar(paint, typeface, firstUnichar, mShaperItem.item.script);
    }

    if (!typeface) {
        typeface = mDefaultTypeface;
#if DEBUG_GLYPHS
        ALOGD("Using Default Typeface");
#endif
    }
    mShapingPaint.setTypeface(typeface);
    mShaperItem.face = getCachedHBFace(typeface);

#if DEBUG_GLYPHS
    ALOGD("Run typeface = %p, uniqueID = %d, hb_face = %p",
            typeface, typeface->uniqueID(), mShaperItem.face);
#endif

    // Shape
    assert(mShaperItem.item.length > 0); // Harfbuzz will overwrite other memory if length is 0.
    ensureShaperItemGlyphArrays(mShaperItem.item.length * 3 / 2);
    mShaperItem.num_glyphs = mShaperItemGlyphArraySize;
    while (!HB_ShapeItem(&mShaperItem)) {
        // We overflowed our glyph arrays. Resize and retry.
        // HB_ShapeItem fills in shaperItem.num_glyphs with the needed size.
        ensureShaperItemGlyphArrays(mShaperItem.num_glyphs * 2);
        mShaperItem.num_glyphs = mShaperItemGlyphArraySize;
    }
    return baseGlyphCount;
}

void TextLayoutShaper::ensureShaperItemGlyphArrays(size_t size) {
    if (size > mShaperItemGlyphArraySize) {
        deleteShaperItemGlyphArrays();
        createShaperItemGlyphArrays(size);
    }
}

void TextLayoutShaper::createShaperItemGlyphArrays(size_t size) {
#if DEBUG_GLYPHS
    ALOGD("Creating Glyph Arrays with size = %d", size);
#endif
    mShaperItemGlyphArraySize = size;

    // These arrays are all indexed by glyph.
    mShaperItem.glyphs = new HB_Glyph[size];
    mShaperItem.attributes = new HB_GlyphAttributes[size];
    mShaperItem.advances = new HB_Fixed[size];
    mShaperItem.offsets = new HB_FixedPoint[size];
=======
    shapeRun(shaperItem, start, count, isRTL);

#if DEBUG_GLYPHS
    LOGD("HARFBUZZ -- num_glypth=%d - kerning_applied=%d", shaperItem.num_glyphs,
            shaperItem.kerning_applied);
    LOGD("         -- string= '%s'", String8(shaperItem.string + start, count).string());
    LOGD("         -- isDevKernText=%d", paint->isDevKernText());

    logGlyphs(shaperItem);
#endif

    if (shaperItem.advances == NULL || shaperItem.num_glyphs == 0) {
#if DEBUG_GLYPHS
    LOGD("HARFBUZZ -- advances array is empty or num_glypth = 0");
#endif
        outAdvances->insertAt(0, outAdvances->size(), count);
        *outTotalAdvance = 0;
        return;
    }

    // Get Advances and their total
    jfloat currentAdvance = HBFixedToFloat(shaperItem.advances[shaperItem.log_clusters[0]]);
    jfloat totalAdvance = currentAdvance;
    outAdvances->add(currentAdvance);
    for (size_t i = 1; i < count; i++) {
        size_t clusterPrevious = shaperItem.log_clusters[i - 1];
        size_t cluster = shaperItem.log_clusters[i];
        if (cluster == clusterPrevious) {
            outAdvances->add(0);
        } else {
            currentAdvance = HBFixedToFloat(shaperItem.advances[shaperItem.log_clusters[i]]);
            totalAdvance += currentAdvance;
            outAdvances->add(currentAdvance);
        }
    }
    *outTotalAdvance = totalAdvance;

#if DEBUG_ADVANCES
    for (size_t i = 0; i < count; i++) {
        LOGD("hb-adv[%d] = %f - log_clusters = %d - total = %f", i,
                (*outAdvances)[i], shaperItem.log_clusters[i], totalAdvance);
    }
#endif

    // Get Glyphs and reverse them in place if RTL
    if (outGlyphs) {
        size_t countGlyphs = shaperItem.num_glyphs;
        for (size_t i = 0; i < countGlyphs; i++) {
            jchar glyph = (jchar) shaperItem.glyphs[(!isRTL) ? i : countGlyphs - 1 - i];
#if DEBUG_GLYPHS
            LOGD("HARFBUZZ  -- glyph[%d]=%d", i, glyph);
#endif
            outGlyphs->add(glyph);
        }
    }
}

void TextLayoutCacheValue::deleteGlyphArrays(HB_ShaperItem& shaperItem) {
    delete[] shaperItem.glyphs;
    delete[] shaperItem.attributes;
    delete[] shaperItem.advances;
    delete[] shaperItem.offsets;
    delete[] shaperItem.log_clusters;
}

void TextLayoutCacheValue::createGlyphArrays(HB_ShaperItem& shaperItem, int size) {
    shaperItem.num_glyphs = size;

    // These arrays are all indexed by glyph
    shaperItem.glyphs = new HB_Glyph[size];
    shaperItem.attributes = new HB_GlyphAttributes[size];
    shaperItem.advances = new HB_Fixed[size];
    shaperItem.offsets = new HB_FixedPoint[size];
>>>>>>> upstream/master

    // Although the log_clusters array is indexed by character, Harfbuzz expects that
    // it is big enough to hold one element per glyph.  So we allocate log_clusters along
    // with the other glyph arrays above.
<<<<<<< HEAD
    mShaperItem.log_clusters = new unsigned short[size];
}

void TextLayoutShaper::deleteShaperItemGlyphArrays() {
    delete[] mShaperItem.glyphs;
    delete[] mShaperItem.attributes;
    delete[] mShaperItem.advances;
    delete[] mShaperItem.offsets;
    delete[] mShaperItem.log_clusters;
}

SkTypeface* TextLayoutShaper::getCachedTypeface(SkTypeface** typeface, const char path[]) {
    if (!*typeface) {
        *typeface = SkTypeface::CreateFromFile(path);
        // CreateFromFile(path) can return NULL if the path is non existing
        if (!*typeface) {
#if DEBUG_GLYPHS
        ALOGD("Font path '%s' is not valid, will use default font", path);
#endif
            return mDefaultTypeface;
        }
        (*typeface)->ref();
#if DEBUG_GLYPHS
        ALOGD("Created SkTypeface from file '%s' with uniqueID = %d", path, (*typeface)->uniqueID());
#endif
    }
    return *typeface;
}

HB_Face TextLayoutShaper::getCachedHBFace(SkTypeface* typeface) {
    SkFontID fontId = typeface->uniqueID();
    ssize_t index = mCachedHBFaces.indexOfKey(fontId);
    if (index >= 0) {
        return mCachedHBFaces.valueAt(index);
    }
    HB_Face face = HB_NewFace(typeface, harfbuzzSkiaGetTable);
    if (face) {
#if DEBUG_GLYPHS
        ALOGD("Created HB_NewFace %p from paint typeface = %p", face, typeface);
#endif
        mCachedHBFaces.add(fontId, face);
    }
    return face;
}

void TextLayoutShaper::purgeCaches() {
    size_t cacheSize = mCachedHBFaces.size();
    for (size_t i = 0; i < cacheSize; i++) {
        HB_FreeFace(mCachedHBFaces.valueAt(i));
    }
    mCachedHBFaces.clear();
    unrefTypefaces();
    init();
}

TextLayoutEngine::TextLayoutEngine() {
    mShaper = new TextLayoutShaper();
#if USE_TEXT_LAYOUT_CACHE
    mTextLayoutCache = new TextLayoutCache(mShaper);
#else
    mTextLayoutCache = NULL;
#endif
}

TextLayoutEngine::~TextLayoutEngine() {
    delete mTextLayoutCache;
    delete mShaper;
}

sp<TextLayoutValue> TextLayoutEngine::getValue(const SkPaint* paint, const jchar* text,
        jint start, jint count, jint contextCount, jint dirFlags) {
    sp<TextLayoutValue> value;
#if USE_TEXT_LAYOUT_CACHE
    value = mTextLayoutCache->getValue(paint, text, start, count,
            contextCount, dirFlags);
    if (value == NULL) {
        ALOGE("Cannot get TextLayoutCache value for text = '%s'",
                String8(text + start, count).string());
    }
#else
    value = new TextLayoutValue(count);
    mShaper->computeValues(value.get(), paint,
            reinterpret_cast<const UChar*>(text), start, count, contextCount, dirFlags);
#endif
    return value;
}

void TextLayoutEngine::purgeCaches() {
#if USE_TEXT_LAYOUT_CACHE
    mTextLayoutCache->clear();
    mShaper->purgeCaches();
#if DEBUG_GLYPHS
    ALOGD("Purged TextLayoutEngine caches");
#endif
#endif
}


=======
    shaperItem.log_clusters = new unsigned short[size];
}

>>>>>>> upstream/master
} // namespace android
