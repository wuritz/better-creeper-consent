package wuritz.bcc.client.utils.timer

interface Timer {

    //
    val MAX_TIME: Int
        get() = -0xff

    /**
     * Returns <tt>true</tt> if the time since the last reset has exceeded
     * the param time.
     *
     * @param time The param time
     * @return <tt>true</tt> if the time since the last reset has exceeded
     * the param time
     */
    fun passed(time: Number): Boolean

    /**
     * Resets the current elapsed time state of the timer and restarts the
     * timer from 0.
     */
    fun reset()

    /**
     * Returns the elapsed time since the last reset of the timer.
     *
     * @return The elapsed time since the last reset
     */
    fun getElapsedTime(): Long

    /**
     * @param time
     */
    fun setElapsedTime(time: Number)
}