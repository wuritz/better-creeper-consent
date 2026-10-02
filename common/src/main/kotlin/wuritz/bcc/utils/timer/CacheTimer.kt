package wuritz.bcc.client.utils.timer

import java.util.concurrent.TimeUnit

/**
 * From genyo
 */
class CacheTimer : Timer {

    // The cached time since last reset which indicates the time passed since
    // the last timer reset
    private var time: Long = 0

    private var lastResetTime: Long = 0

    constructor() {
        time = System.nanoTime()
    }

    /**
     * Returns <tt>true</tt> if the time since the last reset has exceeded
     * the param time.
     *
     * @param time The param time in ms
     * @return <tt>true</tt> if the time since the last reset has exceeded
     * the param time
     */
    override fun passed(time: Number): Boolean {
        if (time.toLong() <= 0) {
            return true
        }
        return getElapsedTime() > time.toLong()
    }

    /**
     * Returns <tt>true</tt> if the time since the last reset has exceeded
     * the param time which is in the param units.
     *
     * @param time The param time
     * @param unit The unit of the time
     * @return <tt>true</tt> if the time since the last reset has exceeded
     * the param time
     * @see .passed
     */
    fun passed(time: Number, unit: TimeUnit): Boolean {
        return passed(unit.toMillis(time.toLong()))
    }

    /**
     * @return
     */
    override fun getElapsedTime(): Long {
        return toMillis(System.nanoTime() - time)
    }

    /**
     * @param time
     */
    override fun setElapsedTime(time: Number) {
        this.time = if (time.toInt() == MAX_TIME) 0 else System.nanoTime() - time.toLong()
    }

    fun setDelay(delay: Number) {
        this.time += delay.toLong()
    }

    /**
     * @return
     */
    fun getElapsedTime(unit: TimeUnit): Long {
        return unit.convert(getElapsedTime(), TimeUnit.MILLISECONDS)
    }

    fun getLastResetTime(): Long {
        return lastResetTime
    }

    /**
     * Sets the cached time since the last reset to the current time
     */
    override fun reset() {
        val time = System.nanoTime()
        lastResetTime = time - this.time

        this.time = time
    }

    /**
     * @return
     */
    private fun toMillis(nanos: Long): Long {
        return nanos / 1000000
    }

}