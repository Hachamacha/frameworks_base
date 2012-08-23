//
// Copyright 2006 The Android Open Source Project
//
// Build resource files from raw assets.
//

#ifndef IMAGES_H
#define IMAGES_H

#include "ResourceTable.h"
#include "Bundle.h"

#include <utils/String8.h>
#include <utils/RefBase.h>

using android::String8;

<<<<<<< HEAD
status_t preProcessImage(const Bundle* bundle, const sp<AaptAssets>& assets,
                         const sp<AaptFile>& file, String8* outNewLeafName);

status_t preProcessImageToCache(const Bundle* bundle, const String8& source, const String8& dest);
=======
status_t preProcessImage(Bundle* bundle, const sp<AaptAssets>& assets,
                         const sp<AaptFile>& file, String8* outNewLeafName);

status_t preProcessImageToCache(Bundle* bundle, String8 source, String8 dest);
>>>>>>> upstream/master

status_t postProcessImage(const sp<AaptAssets>& assets,
                          ResourceTable* table, const sp<AaptFile>& file);

#endif
