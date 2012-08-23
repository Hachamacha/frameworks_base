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

package android.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
<<<<<<< HEAD
import android.graphics.Rect;
=======
>>>>>>> upstream/master

/**
 * A hardware layer can be used to render graphics operations into a hardware
 * friendly buffer. For instance, with an OpenGL backend, a hardware layer
 * would use a Frame Buffer Object (FBO.) The hardware layer can be used as
 * a drawing cache when a complex set of graphics operations needs to be
 * drawn several times.
 */
abstract class HardwareLayer {
    /**
     * Indicates an unknown dimension (width or height.)
     */
    static final int DIMENSION_UNDEFINED = -1;
    
    int mWidth;
    int mHeight;
<<<<<<< HEAD
    DisplayList mDisplayList;
=======
>>>>>>> upstream/master

    boolean mOpaque;

    /**
     * Creates a new hardware layer with undefined dimensions.
     */
    HardwareLayer() {
        this(DIMENSION_UNDEFINED, DIMENSION_UNDEFINED, false);
    }

    /**
     * Creates a new hardware layer at least as large as the supplied
     * dimensions.
     * 
     * @param width The minimum width of the layer
     * @param height The minimum height of the layer
     * @param isOpaque Whether the layer should be opaque or not
     */
    HardwareLayer(int width, int height, boolean isOpaque) {
        mWidth = width;
        mHeight = height;
        mOpaque = isOpaque;
    }

    /**
     * Returns the minimum width of the layer.
     * 
     * @return The minimum desired width of the hardware layer 
     */
    int getWidth() {
        return mWidth;
    }

    /**
     * Returns the minimum height of the layer.
     * 
     * @return The minimum desired height of the hardware layer 
     */
    int getHeight() {
        return mHeight;
    }

    /**
<<<<<<< HEAD
     * Returns the DisplayList for the layer.
     *
     * @return The DisplayList of the hardware layer
     */
    DisplayList getDisplayList() {
        return mDisplayList;
    }

    /**
     * Sets the DisplayList for the layer.
     *
     * @param displayList The new DisplayList for this layer
     */
    void setDisplayList(DisplayList displayList) {
        mDisplayList = displayList;
    }

    /**
=======
>>>>>>> upstream/master
     * Returns whether or not this layer is opaque.
     * 
     * @return True if the layer is opaque, false otherwise
     */
    boolean isOpaque() {
        return mOpaque;
    }

    /**
     * Indicates whether this layer can be rendered.
     * 
     * @return True if the layer can be rendered into, false otherwise
     */
    abstract boolean isValid();

    /**
     * Resize the layer, if necessary, to be at least as large
     * as the supplied dimensions.
     * 
     * @param width The new desired minimum width for this layer
     * @param height The new desired minimum height for this layer
     */
    abstract void resize(int width, int height);

    /**
     * Returns a hardware canvas that can be used to render onto
     * this layer.
     * 
     * @return A hardware canvas, or null if a canvas cannot be created
     */
    abstract HardwareCanvas getCanvas();

    /**
     * Destroys resources without waiting for a GC. 
     */
    abstract void destroy();

    /**
<<<<<<< HEAD
     * Flush the render queue associated with this layer.
     */
    abstract void flush();

    /**
=======
>>>>>>> upstream/master
     * This must be invoked before drawing onto this layer.
     * @param currentCanvas
     */
    abstract HardwareCanvas start(Canvas currentCanvas);
    
    /**
     * This must be invoked after drawing onto this layer.
     * @param currentCanvas
     */
    abstract void end(Canvas currentCanvas);

    /**
     * Copies this layer into the specified bitmap.
     * 
     * @param bitmap The bitmap to copy they layer into
     * 
     * @return True if the copy was successful, false otherwise
     */
    abstract boolean copyInto(Bitmap bitmap);

    /**
     * Update the layer's properties. This method should be used
     * when the underlying storage is modified by an external entity.
     * To change the underlying storage, use the {@link #resize(int, int)}
     * method instead.
     * 
     * @param width The new width of this layer
     * @param height The new height of this layer
     * @param isOpaque Whether this layer is opaque
     */
    void update(int width, int height, boolean isOpaque) {
        mWidth = width;
        mHeight = height;
        mOpaque = isOpaque;
    }

    /**
     * Sets an optional transform on this layer.
     * 
     * @param matrix The transform to apply to the layer.
     */
    abstract void setTransform(Matrix matrix);
<<<<<<< HEAD

    /**
     * Specifies the display list to use to refresh the layer.
     *
     * @param displayList The display list containing the drawing commands to
     *                    execute in this layer
     * @param dirtyRect The dirty region of the layer that needs to be redrawn
     */
    abstract void redraw(DisplayList displayList, Rect dirtyRect);
=======
>>>>>>> upstream/master
}
