package android.animation;

/**
<<<<<<< HEAD
 * This class provides a simple callback mechanism to listeners that is synchronized with all
 * other animators in the system. There is no duration, interpolation, or object value-setting
 * with this Animator. Instead, it is simply started, after which it proceeds to send out events
 * on every animation frame to its TimeListener (if set), with information about this animator,
 * the total elapsed time, and the elapsed time since the previous animation frame.
=======
 * This class provides a simple callback mechanism to listeners that is synchronized with other
 * animators in the system. There is no duration, interpolation, or object value-setting
 * with this Animator. Instead, it is simply started and proceeds to send out events on every
 * animation frame to its TimeListener (if set), with information about this animator,
 * the total elapsed time, and the time since the last animation frame.
 *
 * @hide
>>>>>>> upstream/master
 */
public class TimeAnimator extends ValueAnimator {

    private TimeListener mListener;
    private long mPreviousTime = -1;

    @Override
    boolean animationFrame(long currentTime) {
<<<<<<< HEAD
=======
        if (mPlayingState == STOPPED) {
            mPlayingState = RUNNING;
            if (mSeekTime < 0) {
                mStartTime = currentTime;
            } else {
                mStartTime = currentTime - mSeekTime;
                // Now that we're playing, reset the seek time
                mSeekTime = -1;
            }
        }
>>>>>>> upstream/master
        if (mListener != null) {
            long totalTime = currentTime - mStartTime;
            long deltaTime = (mPreviousTime < 0) ? 0 : (currentTime - mPreviousTime);
            mPreviousTime = currentTime;
            mListener.onTimeUpdate(this, totalTime, deltaTime);
        }
        return false;
    }

    /**
     * Sets a listener that is sent update events throughout the life of
     * an animation.
     *
     * @param listener the listener to be set.
     */
    public void setTimeListener(TimeListener listener) {
        mListener = listener;
    }

    @Override
    void animateValue(float fraction) {
        // Noop
    }

    @Override
    void initAnimation() {
        // noop
    }

    /**
     * Implementors of this interface can set themselves as update listeners
     * to a <code>TimeAnimator</code> instance to receive callbacks on every animation
     * frame to receive the total time since the animator started and the delta time
<<<<<<< HEAD
     * since the last frame. The first time the listener is called,
     * deltaTime will be zero. The same is true for totalTime, unless the animator was
     * set to a specific {@link ValueAnimator#setCurrentPlayTime(long) currentPlayTime}
     * prior to starting.
=======
     * since the last frame. The first time the listener is called, totalTime and
     * deltaTime should both be zero.
     *
     * @hide
>>>>>>> upstream/master
     */
    public static interface TimeListener {
        /**
         * <p>Notifies listeners of the occurrence of another frame of the animation,
         * along with information about the elapsed time.</p>
         *
         * @param animation The animator sending out the notification.
<<<<<<< HEAD
         * @param totalTime The total time elapsed since the animator started, in milliseconds.
         * @param deltaTime The time elapsed since the previous frame, in milliseconds.
=======
         * @param totalTime The
>>>>>>> upstream/master
         */
        void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime);

    }
}
