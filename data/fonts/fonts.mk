# Copyright (C) 2008 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Warning: this is actually a product definition, to be inherited from

<<<<<<< HEAD
PRODUCT_COPY_FILES := \
    frameworks/base/data/fonts/system_fonts.xml:$(TARGET_COPY_OUT_SYSTEM)/etc/system_fonts.xml \
    frameworks/base/data/fonts/fallback_fonts.xml:$(TARGET_COPY_OUT_SYSTEM)/etc/fallback_fonts.xml

PRODUCT_PACKAGES := \
    DroidSansFallback.ttf \
    Roboto-Regular.ttf \
    Roboto-Bold.ttf \
    Roboto-Italic.ttf \
    Roboto-BoldItalic.ttf \
    Roboto-Light.ttf \
    Roboto-LightItalic.ttf \
    RobotoCondensed-Regular.ttf \
    RobotoCondensed-Bold.ttf \
    RobotoCondensed-Italic.ttf \
    RobotoCondensed-BoldItalic.ttf \
    DroidNaskh-Regular.ttf \
    DroidNaskh-Regular-SystemUI.ttf \
    DroidSansDevanagari-Regular.ttf \
    DroidSansHebrew-Regular.ttf \
    DroidSansHebrew-Bold.ttf \
    DroidSansThai.ttf \
    DroidSerif-Regular.ttf \
    DroidSerif-Bold.ttf \
    DroidSerif-Italic.ttf \
    DroidSerif-BoldItalic.ttf \
    DroidSansMono.ttf \
    DroidSansArmenian.ttf \
    DroidSansGeorgian.ttf \
    AndroidEmoji.ttf \
    Clockopia.ttf \
    AndroidClock.ttf \
    AndroidClock_Highlight.ttf \
    AndroidClock_Solid.ttf \
=======
# On space-constrained devices, we include a subset of fonts:
# First, core/required fonts
PRODUCT_COPY_FILES := \
    frameworks/base/data/fonts/Roboto-Regular.ttf:system/fonts/Roboto-Regular.ttf \
    frameworks/base/data/fonts/Roboto-Bold.ttf:system/fonts/Roboto-Bold.ttf \
    frameworks/base/data/fonts/Roboto-Italic.ttf:system/fonts/Roboto-Italic.ttf \
    frameworks/base/data/fonts/Roboto-BoldItalic.ttf:system/fonts/Roboto-BoldItalic.ttf \
    frameworks/base/data/fonts/DroidNaskh-Regular.ttf:system/fonts/DroidNaskh-Regular.ttf \
    frameworks/base/data/fonts/DroidSansHebrew-Regular.ttf:system/fonts/DroidSansHebrew-Regular.ttf \
    frameworks/base/data/fonts/DroidSansHebrew-Bold.ttf:system/fonts/DroidSansHebrew-Bold.ttf \
    frameworks/base/data/fonts/DroidSansThai.ttf:system/fonts/DroidSansThai.ttf \
    frameworks/base/data/fonts/DroidSerif-Regular.ttf:system/fonts/DroidSerif-Regular.ttf \
    frameworks/base/data/fonts/DroidSerif-Bold.ttf:system/fonts/DroidSerif-Bold.ttf \
    frameworks/base/data/fonts/DroidSerif-Italic.ttf:system/fonts/DroidSerif-Italic.ttf \
    frameworks/base/data/fonts/DroidSerif-BoldItalic.ttf:system/fonts/DroidSerif-BoldItalic.ttf \
    frameworks/base/data/fonts/DroidSansMono.ttf:system/fonts/DroidSansMono.ttf \
    frameworks/base/data/fonts/DroidSansArmenian.ttf:system/fonts/DroidSansArmenian.ttf \
    frameworks/base/data/fonts/DroidSansGeorgian.ttf:system/fonts/DroidSansGeorgian.ttf \
    frameworks/base/data/fonts/Clockopia.ttf:system/fonts/Clockopia.ttf \
    frameworks/base/data/fonts/AndroidClock.ttf:system/fonts/AndroidClock.ttf \
    frameworks/base/data/fonts/AndroidClock_Highlight.ttf:system/fonts/AndroidClock_Highlight.ttf \
    frameworks/base/data/fonts/AndroidClock_Solid.ttf:system/fonts/AndroidClock_Solid.ttf \
    frameworks/base/data/fonts/system_fonts.xml:system/etc/system_fonts.xml \
    frameworks/base/data/fonts/fallback_fonts.xml:system/etc/fallback_fonts.xml

# Next, include additional fonts, depending on how much space we have.
# Details see module definitions in Android.mk.
PRODUCT_PACKAGES := \
    DroidSansFallback.ttf
>>>>>>> upstream/master
