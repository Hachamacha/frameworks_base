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

package android.media;

<<<<<<< HEAD
import android.graphics.Rect;
=======
>>>>>>> upstream/master
import android.os.Parcel;
import android.util.Log;
import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

/**
<<<<<<< HEAD
 * Class to hold the timed text's metadata, including:
 * <ul>
 * <li> The characters for rendering</li>
 * <li> The rendering position for the timed text</li>
 * </ul>
 *
 * <p> To render the timed text, applications need to do the following:
 *
 * <ul>
 * <li> Implement the {@link MediaPlayer.OnTimedTextListener} interface</li>
 * <li> Register the {@link MediaPlayer.OnTimedTextListener} callback on a MediaPlayer object that is used for playback</li>
 * <li> When a onTimedText callback is received, do the following:
 * <ul>
 * <li> call {@link #getText} to get the characters for rendering</li>
 * <li> call {@link #getBounds} to get the text rendering area/region</li>
 * </ul>
 * </li>
 * </ul>
 *
 * @see android.media.MediaPlayer
 */
public final class TimedText
=======
 * Class to hold the timed text's metadata.
 *
 * {@hide}
 */
public class TimedText
>>>>>>> upstream/master
{
    private static final int FIRST_PUBLIC_KEY                 = 1;

    // These keys must be in sync with the keys in TextDescription.h
<<<<<<< HEAD
    private static final int KEY_DISPLAY_FLAGS                 = 1; // int
    private static final int KEY_STYLE_FLAGS                   = 2; // int
    private static final int KEY_BACKGROUND_COLOR_RGBA         = 3; // int
    private static final int KEY_HIGHLIGHT_COLOR_RGBA          = 4; // int
    private static final int KEY_SCROLL_DELAY                  = 5; // int
    private static final int KEY_WRAP_TEXT                     = 6; // int
    private static final int KEY_START_TIME                    = 7; // int
    private static final int KEY_STRUCT_BLINKING_TEXT_LIST     = 8; // List<CharPos>
    private static final int KEY_STRUCT_FONT_LIST              = 9; // List<Font>
    private static final int KEY_STRUCT_HIGHLIGHT_LIST         = 10; // List<CharPos>
    private static final int KEY_STRUCT_HYPER_TEXT_LIST        = 11; // List<HyperText>
    private static final int KEY_STRUCT_KARAOKE_LIST           = 12; // List<Karaoke>
    private static final int KEY_STRUCT_STYLE_LIST             = 13; // List<Style>
    private static final int KEY_STRUCT_TEXT_POS               = 14; // TextPos
    private static final int KEY_STRUCT_JUSTIFICATION          = 15; // Justification
    private static final int KEY_STRUCT_TEXT                   = 16; // Text
=======
    public static final int KEY_DISPLAY_FLAGS                 = 1; // int
    public static final int KEY_STYLE_FLAGS                   = 2; // int
    public static final int KEY_BACKGROUND_COLOR_RGBA         = 3; // int
    public static final int KEY_HIGHLIGHT_COLOR_RGBA          = 4; // int
    public static final int KEY_SCROLL_DELAY                  = 5; // int
    public static final int KEY_WRAP_TEXT                     = 6; // int
    public static final int KEY_START_TIME                    = 7; // int
    public static final int KEY_STRUCT_BLINKING_TEXT_LIST     = 8; // List<CharPos>
    public static final int KEY_STRUCT_FONT_LIST              = 9; // List<Font>
    public static final int KEY_STRUCT_HIGHLIGHT_LIST         = 10; // List<CharPos>
    public static final int KEY_STRUCT_HYPER_TEXT_LIST        = 11; // List<HyperText>
    public static final int KEY_STRUCT_KARAOKE_LIST           = 12; // List<Karaoke>
    public static final int KEY_STRUCT_STYLE_LIST             = 13; // List<Style>
    public static final int KEY_STRUCT_TEXT_POS               = 14; // TextPos
    public static final int KEY_STRUCT_JUSTIFICATION          = 15; // Justification
    public static final int KEY_STRUCT_TEXT                   = 16; // Text
>>>>>>> upstream/master

    private static final int LAST_PUBLIC_KEY                  = 16;

    private static final int FIRST_PRIVATE_KEY                = 101;

    // The following keys are used between TimedText.java and
    // TextDescription.cpp in order to parce the Parcel.
    private static final int KEY_GLOBAL_SETTING               = 101;
    private static final int KEY_LOCAL_SETTING                = 102;
    private static final int KEY_START_CHAR                   = 103;
    private static final int KEY_END_CHAR                     = 104;
    private static final int KEY_FONT_ID                      = 105;
    private static final int KEY_FONT_SIZE                    = 106;
    private static final int KEY_TEXT_COLOR_RGBA              = 107;

    private static final int LAST_PRIVATE_KEY                 = 107;

    private static final String TAG = "TimedText";

<<<<<<< HEAD
=======
    private Parcel mParcel = Parcel.obtain();
>>>>>>> upstream/master
    private final HashMap<Integer, Object> mKeyObjectMap =
            new HashMap<Integer, Object>();

    private int mDisplayFlags = -1;
    private int mBackgroundColorRGBA = -1;
    private int mHighlightColorRGBA = -1;
    private int mScrollDelay = -1;
    private int mWrapText = -1;

    private List<CharPos> mBlinkingPosList = null;
    private List<CharPos> mHighlightPosList = null;
    private List<Karaoke> mKaraokeList = null;
    private List<Font> mFontList = null;
    private List<Style> mStyleList = null;
    private List<HyperText> mHyperTextList = null;

<<<<<<< HEAD
    private Rect mTextBounds = null;
    private String mTextChars = null;

    private Justification mJustification;
=======
    private TextPos mTextPos;
    private Justification mJustification;
    private Text mTextStruct;

    /**
     * Helper class to hold the text length and text content of
     * one text sample. The member variables in this class are
     * read-only.
     */
    public class Text {
        /**
         * The byte-count of this text sample
         */
        public int textLen;

        /**
         * The text sample
         */
        public byte[] text;

        public Text() { }
    }
>>>>>>> upstream/master

    /**
     * Helper class to hold the start char offset and end char offset
     * for Blinking Text or Highlight Text. endChar is the end offset
     * of the text (startChar + number of characters to be highlighted
     * or blinked). The member variables in this class are read-only.
<<<<<<< HEAD
     * {@hide}
     */
    public static final class CharPos {
        /**
         * The offset of the start character
         */
        public final int startChar;
=======
     */
    public class CharPos {
        /**
         * The offset of the start character
         */
        public int startChar = -1;
>>>>>>> upstream/master

        /**
         * The offset of the end character
         */
<<<<<<< HEAD
        public final int endChar;

        /**
         * Constuctor
         * @param startChar the offset of the start character.
         * @param endChar the offset of the end character.
         */
        public CharPos(int startChar, int endChar) {
            this.startChar = startChar;
            this.endChar = endChar;
        }
=======
        public int endChar = -1;

        public CharPos() { }
    }

    /**
     * Helper class to hold the box position to display the text sample.
     * The member variables in this class are read-only.
     */
    public class TextPos {
        /**
         * The top position of the text
         */
        public int top = -1;

        /**
         * The left position of the text
         */
        public int left = -1;

        /**
         * The bottom position of the text
         */
        public int bottom = -1;

        /**
         * The right position of the text
         */
        public int right = -1;

        public TextPos() { }
>>>>>>> upstream/master
    }

    /**
     * Helper class to hold the justification for text display in the text box.
     * The member variables in this class are read-only.
<<<<<<< HEAD
     * {@hide}
     */
    public static final class Justification {
        /**
         * horizontal justification  0: left, 1: centered, -1: right
         */
        public final int horizontalJustification;

        /**
         * vertical justification  0: top, 1: centered, -1: bottom
         */
        public final int verticalJustification;

        /**
         * Constructor
         * @param horizontal the horizontal justification of the text.
         * @param vertical the vertical justification of the text.
         */
        public Justification(int horizontal, int vertical) {
            this.horizontalJustification = horizontal;
            this.verticalJustification = vertical;
        }
=======
     */
    public class Justification {
        /**
         * horizontalJustification  0: left, 1: centered, -1: right
         */
        public int horizontalJustification = -1;

        /**
         * verticalJustification  0: top, 1: centered, -1: bottom
         */
        public int verticalJustification = -1;

        public Justification() { }
>>>>>>> upstream/master
    }

    /**
     * Helper class to hold the style information to display the text.
     * The member variables in this class are read-only.
<<<<<<< HEAD
     * {@hide}
     */
    public static final class Style {
        /**
         * The offset of the start character which applys this style
         */
        public final int startChar;
=======
     */
    public class Style {
        /**
         * The offset of the start character which applys this style
         */
        public int startChar = -1;
>>>>>>> upstream/master

        /**
         * The offset of the end character which applys this style
         */
<<<<<<< HEAD
        public final int endChar;
=======
        public int endChar = -1;
>>>>>>> upstream/master

        /**
         * ID of the font. This ID will be used to choose the font
         * to be used from the font list.
         */
<<<<<<< HEAD
        public final int fontID;
=======
        public int fontID = -1;
>>>>>>> upstream/master

        /**
         * True if the characters should be bold
         */
<<<<<<< HEAD
        public final boolean isBold;
=======
        public boolean isBold = false;
>>>>>>> upstream/master

        /**
         * True if the characters should be italic
         */
<<<<<<< HEAD
        public final boolean isItalic;
=======
        public boolean isItalic = false;
>>>>>>> upstream/master

        /**
         * True if the characters should be underlined
         */
<<<<<<< HEAD
        public final boolean isUnderlined;
=======
        public boolean isUnderlined = false;
>>>>>>> upstream/master

        /**
         * The size of the font
         */
<<<<<<< HEAD
        public final int fontSize;
=======
        public int fontSize = -1;
>>>>>>> upstream/master

        /**
         * To specify the RGBA color: 8 bits each of red, green, blue,
         * and an alpha(transparency) value
         */
<<<<<<< HEAD
        public final int colorRGBA;

        /**
         * Constructor
         * @param startChar the offset of the start character which applys this style
         * @param endChar the offset of the end character which applys this style
         * @param fontId the ID of the font.
         * @param isBold whether the characters should be bold.
         * @param isItalic whether the characters should be italic.
         * @param isUnderlined whether the characters should be underlined.
         * @param fontSize the size of the font.
         * @param colorRGBA red, green, blue, and alpha value for color.
         */
        public Style(int startChar, int endChar, int fontId,
                     boolean isBold, boolean isItalic, boolean isUnderlined,
                     int fontSize, int colorRGBA) {
            this.startChar = startChar;
            this.endChar = endChar;
            this.fontID = fontId;
            this.isBold = isBold;
            this.isItalic = isItalic;
            this.isUnderlined = isUnderlined;
            this.fontSize = fontSize;
            this.colorRGBA = colorRGBA;
        }
=======
        public int colorRGBA = -1;

        public Style() { }
>>>>>>> upstream/master
    }

    /**
     * Helper class to hold the font ID and name.
     * The member variables in this class are read-only.
<<<<<<< HEAD
     * {@hide}
     */
    public static final class Font {
        /**
         * The font ID
         */
        public final int ID;
=======
     */
    public class Font {
        /**
         * The font ID
         */
        public int ID = -1;
>>>>>>> upstream/master

        /**
         * The font name
         */
<<<<<<< HEAD
        public final String name;

        /**
         * Constructor
         * @param id the font ID.
         * @param name the font name.
         */
        public Font(int id, String name) {
            this.ID = id;
            this.name = name;
        }
=======
        public String name;

        public Font() { }
>>>>>>> upstream/master
    }

    /**
     * Helper class to hold the karaoke information.
     * The member variables in this class are read-only.
<<<<<<< HEAD
     * {@hide}
     */
    public static final class Karaoke {
=======
     */
    public class Karaoke {
>>>>>>> upstream/master
        /**
         * The start time (in milliseconds) to highlight the characters
         * specified by startChar and endChar.
         */
<<<<<<< HEAD
        public final int startTimeMs;
=======
        public int startTimeMs = -1;
>>>>>>> upstream/master

        /**
         * The end time (in milliseconds) to highlight the characters
         * specified by startChar and endChar.
         */
<<<<<<< HEAD
        public final int endTimeMs;
=======
        public int endTimeMs = -1;
>>>>>>> upstream/master

        /**
         * The offset of the start character to be highlighted
         */
<<<<<<< HEAD
        public final int startChar;
=======
        public int startChar = -1;
>>>>>>> upstream/master

        /**
         * The offset of the end character to be highlighted
         */
<<<<<<< HEAD
        public final int endChar;

        /**
         * Constructor
         * @param startTimeMs the start time (in milliseconds) to highlight
         * the characters between startChar and endChar.
         * @param endTimeMs the end time (in milliseconds) to highlight
         * the characters between startChar and endChar.
         * @param startChar the offset of the start character to be highlighted.
         * @param endChar the offset of the end character to be highlighted.
         */
        public Karaoke(int startTimeMs, int endTimeMs, int startChar, int endChar) {
            this.startTimeMs = startTimeMs;
            this.endTimeMs = endTimeMs;
            this.startChar = startChar;
            this.endChar = endChar;
        }
=======
        public int endChar = -1;

        public Karaoke() { }
>>>>>>> upstream/master
    }

    /**
     * Helper class to hold the hyper text information.
     * The member variables in this class are read-only.
<<<<<<< HEAD
     * {@hide}
     */
    public static final class HyperText {
        /**
         * The offset of the start character
         */
        public final int startChar;
=======
     */
    public class HyperText {
        /**
         * The offset of the start character
         */
        public int startChar = -1;
>>>>>>> upstream/master

        /**
         * The offset of the end character
         */
<<<<<<< HEAD
        public final int endChar;
=======
        public int endChar = -1;
>>>>>>> upstream/master

        /**
         * The linked-to URL
         */
<<<<<<< HEAD
        public final String URL;
=======
        public String URL;
>>>>>>> upstream/master

        /**
         * The "alt" string for user display
         */
<<<<<<< HEAD
        public final String altString;


        /**
         * Constructor
         * @param startChar the offset of the start character.
         * @param endChar the offset of the end character.
         * @param url the linked-to URL.
         * @param alt the "alt" string for display.
         */
        public HyperText(int startChar, int endChar, String url, String alt) {
            this.startChar = startChar;
            this.endChar = endChar;
            this.URL = url;
            this.altString = alt;
        }
=======
        public String altString;

        public HyperText() { }
>>>>>>> upstream/master
    }

    /**
     * @param obj the byte array which contains the timed text.
     * @throws IllegalArgumentExcept if parseParcel() fails.
     * {@hide}
     */
<<<<<<< HEAD
    public TimedText(Parcel parcel) {
        if (!parseParcel(parcel)) {
=======
    public TimedText(byte[] obj) {
        mParcel.unmarshall(obj, 0, obj.length);

        if (!parseParcel()) {
>>>>>>> upstream/master
            mKeyObjectMap.clear();
            throw new IllegalArgumentException("parseParcel() fails");
        }
    }

    /**
<<<<<<< HEAD
     * Get the characters in the timed text.
     *
     * @return the characters as a String object in the TimedText. Applications
     * should stop rendering previous timed text at the current rendering region if
     * a null is returned, until the next non-null timed text is received.
     */
    public String getText() {
        return mTextChars;
    }

    /**
     * Get the rectangle area or region for rendering the timed text as specified
     * by a Rect object.
     *
     * @return the rectangle region to render the characters in the timed text.
     * If no bounds information is available (a null is returned), render the
     * timed text at the center bottom of the display.
     */
    public Rect getBounds() {
        return mTextBounds;
    }

    /*
=======
>>>>>>> upstream/master
     * Go over all the records, collecting metadata keys and fields in the
     * Parcel. These are stored in mKeyObjectMap for application to retrieve.
     * @return false if an error occurred during parsing. Otherwise, true.
     */
<<<<<<< HEAD
    private boolean parseParcel(Parcel parcel) {
        parcel.setDataPosition(0);
        if (parcel.dataAvail() == 0) {
            return false;
        }

        int type = parcel.readInt();
        if (type == KEY_LOCAL_SETTING) {
            type = parcel.readInt();
            if (type != KEY_START_TIME) {
                return false;
            }
            int mStartTimeMs = parcel.readInt();
            mKeyObjectMap.put(type, mStartTimeMs);

            type = parcel.readInt();
=======
    private boolean parseParcel() {
        mParcel.setDataPosition(0);
        if (mParcel.dataAvail() == 0) {
            return false;
        }

        int type = mParcel.readInt();
        if (type == KEY_LOCAL_SETTING) {
            type = mParcel.readInt();
            if (type != KEY_START_TIME) {
                return false;
            }
            int mStartTimeMs = mParcel.readInt();
            mKeyObjectMap.put(type, mStartTimeMs);

            type = mParcel.readInt();
>>>>>>> upstream/master
            if (type != KEY_STRUCT_TEXT) {
                return false;
            }

<<<<<<< HEAD
            int textLen = parcel.readInt();
            byte[] text = parcel.createByteArray();
            if (text == null || text.length == 0) {
                mTextChars = null;
            } else {
                mTextChars = new String(text);
            }
=======
            mTextStruct = new Text();
            mTextStruct.textLen = mParcel.readInt();

            mTextStruct.text = mParcel.createByteArray();
            mKeyObjectMap.put(type, mTextStruct);
>>>>>>> upstream/master

        } else if (type != KEY_GLOBAL_SETTING) {
            Log.w(TAG, "Invalid timed text key found: " + type);
            return false;
        }

<<<<<<< HEAD
        while (parcel.dataAvail() > 0) {
            int key = parcel.readInt();
=======
        while (mParcel.dataAvail() > 0) {
            int key = mParcel.readInt();
>>>>>>> upstream/master
            if (!isValidKey(key)) {
                Log.w(TAG, "Invalid timed text key found: " + key);
                return false;
            }

            Object object = null;

            switch (key) {
                case KEY_STRUCT_STYLE_LIST: {
<<<<<<< HEAD
                    readStyle(parcel);
=======
                    readStyle();
>>>>>>> upstream/master
                    object = mStyleList;
                    break;
                }
                case KEY_STRUCT_FONT_LIST: {
<<<<<<< HEAD
                    readFont(parcel);
=======
                    readFont();
>>>>>>> upstream/master
                    object = mFontList;
                    break;
                }
                case KEY_STRUCT_HIGHLIGHT_LIST: {
<<<<<<< HEAD
                    readHighlight(parcel);
=======
                    readHighlight();
>>>>>>> upstream/master
                    object = mHighlightPosList;
                    break;
                }
                case KEY_STRUCT_KARAOKE_LIST: {
<<<<<<< HEAD
                    readKaraoke(parcel);
=======
                    readKaraoke();
>>>>>>> upstream/master
                    object = mKaraokeList;
                    break;
                }
                case KEY_STRUCT_HYPER_TEXT_LIST: {
<<<<<<< HEAD
                    readHyperText(parcel);
=======
                    readHyperText();
>>>>>>> upstream/master
                    object = mHyperTextList;

                    break;
                }
                case KEY_STRUCT_BLINKING_TEXT_LIST: {
<<<<<<< HEAD
                    readBlinkingText(parcel);
=======
                    readBlinkingText();
>>>>>>> upstream/master
                    object = mBlinkingPosList;

                    break;
                }
                case KEY_WRAP_TEXT: {
<<<<<<< HEAD
                    mWrapText = parcel.readInt();
=======
                    mWrapText = mParcel.readInt();
>>>>>>> upstream/master
                    object = mWrapText;
                    break;
                }
                case KEY_HIGHLIGHT_COLOR_RGBA: {
<<<<<<< HEAD
                    mHighlightColorRGBA = parcel.readInt();
=======
                    mHighlightColorRGBA = mParcel.readInt();
>>>>>>> upstream/master
                    object = mHighlightColorRGBA;
                    break;
                }
                case KEY_DISPLAY_FLAGS: {
<<<<<<< HEAD
                    mDisplayFlags = parcel.readInt();
=======
                    mDisplayFlags = mParcel.readInt();
>>>>>>> upstream/master
                    object = mDisplayFlags;
                    break;
                }
                case KEY_STRUCT_JUSTIFICATION: {
<<<<<<< HEAD

                    int horizontal = parcel.readInt();
                    int vertical = parcel.readInt();
                    mJustification = new Justification(horizontal, vertical);
=======
                    mJustification = new Justification();

                    mJustification.horizontalJustification = mParcel.readInt();
                    mJustification.verticalJustification = mParcel.readInt();
>>>>>>> upstream/master

                    object = mJustification;
                    break;
                }
                case KEY_BACKGROUND_COLOR_RGBA: {
<<<<<<< HEAD
                    mBackgroundColorRGBA = parcel.readInt();
=======
                    mBackgroundColorRGBA = mParcel.readInt();
>>>>>>> upstream/master
                    object = mBackgroundColorRGBA;
                    break;
                }
                case KEY_STRUCT_TEXT_POS: {
<<<<<<< HEAD
                    int top = parcel.readInt();
                    int left = parcel.readInt();
                    int bottom = parcel.readInt();
                    int right = parcel.readInt();
                    mTextBounds = new Rect(left, top, right, bottom);

                    break;
                }
                case KEY_SCROLL_DELAY: {
                    mScrollDelay = parcel.readInt();
=======
                    mTextPos = new TextPos();

                    mTextPos.top = mParcel.readInt();
                    mTextPos.left = mParcel.readInt();
                    mTextPos.bottom = mParcel.readInt();
                    mTextPos.right = mParcel.readInt();

                    object = mTextPos;
                    break;
                }
                case KEY_SCROLL_DELAY: {
                    mScrollDelay = mParcel.readInt();
>>>>>>> upstream/master
                    object = mScrollDelay;
                    break;
                }
                default: {
                    break;
                }
            }

            if (object != null) {
                if (mKeyObjectMap.containsKey(key)) {
                    mKeyObjectMap.remove(key);
                }
<<<<<<< HEAD
                // Previous mapping will be replaced with the new object, if there was one.
=======
>>>>>>> upstream/master
                mKeyObjectMap.put(key, object);
            }
        }

<<<<<<< HEAD
        return true;
    }

    /*
     * To parse and store the Style list.
     */
    private void readStyle(Parcel parcel) {
        boolean endOfStyle = false;
        int startChar = -1;
        int endChar = -1;
        int fontId = -1;
        boolean isBold = false;
        boolean isItalic = false;
        boolean isUnderlined = false;
        int fontSize = -1;
        int colorRGBA = -1;
        while (!endOfStyle && (parcel.dataAvail() > 0)) {
            int key = parcel.readInt();
            switch (key) {
                case KEY_START_CHAR: {
                    startChar = parcel.readInt();
                    break;
                }
                case KEY_END_CHAR: {
                    endChar = parcel.readInt();
                    break;
                }
                case KEY_FONT_ID: {
                    fontId = parcel.readInt();
                    break;
                }
                case KEY_STYLE_FLAGS: {
                    int flags = parcel.readInt();
                    // In the absence of any bits set in flags, the text
                    // is plain. Otherwise, 1: bold, 2: italic, 4: underline
                    isBold = ((flags % 2) == 1);
                    isItalic = ((flags % 4) >= 2);
                    isUnderlined = ((flags / 4) == 1);
                    break;
                }
                case KEY_FONT_SIZE: {
                    fontSize = parcel.readInt();
                    break;
                }
                case KEY_TEXT_COLOR_RGBA: {
                    colorRGBA = parcel.readInt();
=======
        mParcel.recycle();
        return true;
    }

    /**
     * To parse and store the Style list.
     */
    private void readStyle() {
        Style style = new Style();
        boolean endOfStyle = false;

        while (!endOfStyle && (mParcel.dataAvail() > 0)) {
            int key = mParcel.readInt();
            switch (key) {
                case KEY_START_CHAR: {
                    style.startChar = mParcel.readInt();
                    break;
                }
                case KEY_END_CHAR: {
                    style.endChar = mParcel.readInt();
                    break;
                }
                case KEY_FONT_ID: {
                    style.fontID = mParcel.readInt();
                    break;
                }
                case KEY_STYLE_FLAGS: {
                    int flags = mParcel.readInt();
                    // In the absence of any bits set in flags, the text
                    // is plain. Otherwise, 1: bold, 2: italic, 4: underline
                    style.isBold = ((flags % 2) == 1);
                    style.isItalic = ((flags % 4) >= 2);
                    style.isUnderlined = ((flags / 4) == 1);
                    break;
                }
                case KEY_FONT_SIZE: {
                    style.fontSize = mParcel.readInt();
                    break;
                }
                case KEY_TEXT_COLOR_RGBA: {
                    style.colorRGBA = mParcel.readInt();
>>>>>>> upstream/master
                    break;
                }
                default: {
                    // End of the Style parsing. Reset the data position back
<<<<<<< HEAD
                    // to the position before the last parcel.readInt() call.
                    parcel.setDataPosition(parcel.dataPosition() - 4);
=======
                    // to the position before the last mParcel.readInt() call.
                    mParcel.setDataPosition(mParcel.dataPosition() - 4);
>>>>>>> upstream/master
                    endOfStyle = true;
                    break;
                }
            }
        }

<<<<<<< HEAD
        Style style = new Style(startChar, endChar, fontId, isBold,
                                isItalic, isUnderlined, fontSize, colorRGBA);
=======
>>>>>>> upstream/master
        if (mStyleList == null) {
            mStyleList = new ArrayList<Style>();
        }
        mStyleList.add(style);
    }

<<<<<<< HEAD
    /*
     * To parse and store the Font list
     */
    private void readFont(Parcel parcel) {
        int entryCount = parcel.readInt();

        for (int i = 0; i < entryCount; i++) {
            int id = parcel.readInt();
            int nameLen = parcel.readInt();

            byte[] text = parcel.createByteArray();
            final String name = new String(text, 0, nameLen);

            Font font = new Font(id, name);
=======
    /**
     * To parse and store the Font list
     */
    private void readFont() {
        int entryCount = mParcel.readInt();

        for (int i = 0; i < entryCount; i++) {
            Font font = new Font();

            font.ID = mParcel.readInt();
            int nameLen = mParcel.readInt();

            byte[] text = mParcel.createByteArray();
            font.name = new String(text, 0, nameLen);
>>>>>>> upstream/master

            if (mFontList == null) {
                mFontList = new ArrayList<Font>();
            }
            mFontList.add(font);
        }
    }

<<<<<<< HEAD
    /*
     * To parse and store the Highlight list
     */
    private void readHighlight(Parcel parcel) {
        int startChar = parcel.readInt();
        int endChar = parcel.readInt();
        CharPos pos = new CharPos(startChar, endChar);
=======
    /**
     * To parse and store the Highlight list
     */
    private void readHighlight() {
        CharPos pos = new CharPos();

        pos.startChar = mParcel.readInt();
        pos.endChar = mParcel.readInt();
>>>>>>> upstream/master

        if (mHighlightPosList == null) {
            mHighlightPosList = new ArrayList<CharPos>();
        }
        mHighlightPosList.add(pos);
    }

<<<<<<< HEAD
    /*
     * To parse and store the Karaoke list
     */
    private void readKaraoke(Parcel parcel) {
        int entryCount = parcel.readInt();

        for (int i = 0; i < entryCount; i++) {
            int startTimeMs = parcel.readInt();
            int endTimeMs = parcel.readInt();
            int startChar = parcel.readInt();
            int endChar = parcel.readInt();
            Karaoke kara = new Karaoke(startTimeMs, endTimeMs,
                                       startChar, endChar);
=======
    /**
     * To parse and store the Karaoke list
     */
    private void readKaraoke() {
        int entryCount = mParcel.readInt();

        for (int i = 0; i < entryCount; i++) {
            Karaoke kara = new Karaoke();

            kara.startTimeMs = mParcel.readInt();
            kara.endTimeMs = mParcel.readInt();
            kara.startChar = mParcel.readInt();
            kara.endChar = mParcel.readInt();
>>>>>>> upstream/master

            if (mKaraokeList == null) {
                mKaraokeList = new ArrayList<Karaoke>();
            }
            mKaraokeList.add(kara);
        }
    }

<<<<<<< HEAD
    /*
     * To parse and store HyperText list
     */
    private void readHyperText(Parcel parcel) {
        int startChar = parcel.readInt();
        int endChar = parcel.readInt();

        int len = parcel.readInt();
        byte[] url = parcel.createByteArray();
        final String urlString = new String(url, 0, len);

        len = parcel.readInt();
        byte[] alt = parcel.createByteArray();
        final String altString = new String(alt, 0, len);
        HyperText hyperText = new HyperText(startChar, endChar, urlString, altString);

=======
    /**
     * To parse and store HyperText list
     */
    private void readHyperText() {
        HyperText hyperText = new HyperText();

        hyperText.startChar = mParcel.readInt();
        hyperText.endChar = mParcel.readInt();

        int len = mParcel.readInt();
        byte[] url = mParcel.createByteArray();
        hyperText.URL = new String(url, 0, len);

        len = mParcel.readInt();
        byte[] alt = mParcel.createByteArray();
        hyperText.altString = new String(alt, 0, len);
>>>>>>> upstream/master

        if (mHyperTextList == null) {
            mHyperTextList = new ArrayList<HyperText>();
        }
        mHyperTextList.add(hyperText);
    }

<<<<<<< HEAD
    /*
     * To parse and store blinking text list
     */
    private void readBlinkingText(Parcel parcel) {
        int startChar = parcel.readInt();
        int endChar = parcel.readInt();
        CharPos blinkingPos = new CharPos(startChar, endChar);
=======
    /**
     * To parse and store blinking text list
     */
    private void readBlinkingText() {
        CharPos blinkingPos = new CharPos();

        blinkingPos.startChar = mParcel.readInt();
        blinkingPos.endChar = mParcel.readInt();
>>>>>>> upstream/master

        if (mBlinkingPosList == null) {
            mBlinkingPosList = new ArrayList<CharPos>();
        }
        mBlinkingPosList.add(blinkingPos);
    }

<<<<<<< HEAD
    /*
=======
    /**
>>>>>>> upstream/master
     * To check whether the given key is valid.
     * @param key the key to be checked.
     * @return true if the key is a valid one. Otherwise, false.
     */
<<<<<<< HEAD
    private boolean isValidKey(final int key) {
=======
    public boolean isValidKey(final int key) {
>>>>>>> upstream/master
        if (!((key >= FIRST_PUBLIC_KEY) && (key <= LAST_PUBLIC_KEY))
                && !((key >= FIRST_PRIVATE_KEY) && (key <= LAST_PRIVATE_KEY))) {
            return false;
        }
        return true;
    }

<<<<<<< HEAD
    /*
=======
    /**
>>>>>>> upstream/master
     * To check whether the given key is contained in this TimedText object.
     * @param key the key to be checked.
     * @return true if the key is contained in this TimedText object.
     *         Otherwise, false.
     */
<<<<<<< HEAD
    private boolean containsKey(final int key) {
=======
    public boolean containsKey(final int key) {
>>>>>>> upstream/master
        if (isValidKey(key) && mKeyObjectMap.containsKey(key)) {
            return true;
        }
        return false;
    }
<<<<<<< HEAD

    /*
     * @return a set of the keys contained in this TimedText object.
     */
    private Set keySet() {
        return mKeyObjectMap.keySet();
    }

    /*
=======
    /**
     * @return a set of the keys contained in this TimedText object.
     */
    public Set keySet() {
        return mKeyObjectMap.keySet();
    }

    /**
>>>>>>> upstream/master
     * To retrieve the object associated with the key. Caller must make sure
     * the key is present using the containsKey method otherwise a
     * RuntimeException will occur.
     * @param key the key used to retrieve the object.
<<<<<<< HEAD
     * @return an object. The object could be 1) an instance of Integer; 2) a
     * List of CharPos, Karaoke, Font, Style, and HyperText, or 3) an instance of
     * Justification.
     */
    private Object getObject(final int key) {
=======
     * @return an object. The object could be an instanceof Integer, List, or
     * any of the helper classes such as TextPos, Justification, and Text.
     */
    public Object getObject(final int key) {
>>>>>>> upstream/master
        if (containsKey(key)) {
            return mKeyObjectMap.get(key);
        } else {
            throw new IllegalArgumentException("Invalid key: " + key);
        }
    }
}
