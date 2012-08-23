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

#ifndef NinePatchPeeker_h
#define NinePatchPeeker_h

#include "SkImageDecoder.h"
<<<<<<< HEAD
#include <androidfw/ResourceTypes.h>
=======
#include <utils/ResourceTypes.h>
>>>>>>> upstream/master

using namespace android;

class NinePatchPeeker : public SkImageDecoder::Peeker {
    SkImageDecoder* fHost;
public:
    NinePatchPeeker(SkImageDecoder* host) {
        // the host lives longer than we do, so a raw ptr is safe
        fHost = host;
<<<<<<< HEAD
        fPatch = NULL;
        fLayoutBounds = NULL;
    }

    ~NinePatchPeeker() {
        free(fPatch);
        delete fLayoutBounds;
    }

    Res_png_9patch*  fPatch;
    int    *fLayoutBounds;
=======
        fPatchIsValid = false;
    }

    ~NinePatchPeeker() {
        if (fPatchIsValid) {
            free(fPatch);
        }
    }

    bool    fPatchIsValid;
    Res_png_9patch*  fPatch;
>>>>>>> upstream/master

    virtual bool peek(const char tag[], const void* data, size_t length);
};

#endif // NinePatchPeeker_h
