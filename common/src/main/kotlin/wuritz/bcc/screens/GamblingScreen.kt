/*
 * Copyright (c) 2026. wuritz
 * SPDX-License-Identifier: Apache-2.0
 */

package wuritz.bcc.screens

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.ImageWidget
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvents
import wuritz.bcc.network.payloads.incoming.LuckyPayload
import wuritz.bcc.network.payloads.incoming.ResponsePayload
import wuritz.bcc.platform.Services
import wuritz.bcc.utils.RenderUtils
import wuritz.bcc.utils.timer.CacheTimer
import java.awt.Color
import java.util.concurrent.TimeUnit
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

class GamblingScreen(val creeperId: Int, val creeperImage: Identifier, val creeperWidth: Int) : Screen(Component.literal("Consent Gambling")) {

    var state = State.ALLOW

    val secTimer = CacheTimer()
    val rollTimer = CacheTimer()
    val endTimer = CacheTimer()
    val overTimer = CacheTimer()
    val sliderTimer = CacheTimer()
    val rollTextTimer = CacheTimer()

    val steps = listOf( // 50, 100, 250, 500
        Random.nextInt(30, 80),
        Random.nextInt(80, 110),
        Random.nextInt(230, 280),
        Random.nextInt(380, 550)
    )

    var currentStep = 0
    var trigger = false

    var isOver = false
    val mcFont = Minecraft.getInstance().font

    // Rolling
    var rollingText = ""
    var rtState = 0
    var rollingSliderPercentage = 1f
    val initR = 0 // Random.nextInt(0, 255)
    val initG = 255 // Random.nextInt(0, 255)
    val initB = 0 // Random.nextInt(0, 255)

    override fun init() {
        secTimer.reset()
        rollTimer.reset()
        endTimer.reset()
        sliderTimer.reset()
        rollTextTimer.reset()

        val imageSize = calcImageSize()
        addRenderableWidget(ImageWidget.texture(imageSize, imageSize, creeperImage, imageSize, imageSize)
        ).setPosition(getPictureX(), getPictureY())

        if (Random.nextInt() % 2 == 0) trigger = true
        rollingSliderPercentage = 1f
        isOver = false
    }

    /**
     * Render functions
     */

    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        // Background
        graphics.fill(0, 0, width, height, 0xAA050A05.toInt())

        // Middle separator
        graphics.fill(width / 2 - 1, height / 4, width / 2 + 1, (height * 0.75).toInt(), Color(100, 100, 100, 255).rgb)

        // Roll mechanics
        if (!isOver) isRollOver()
        else shouldSendPacket()

        // Rolling text
        drawRollingText(graphics)

        // Result
        val resultString = getResultString()
        val resultColor = if (!isOver) calculateRollingTextColor() else
            if (state == State.ALLOW) Color.GREEN.rgb else Color.RED.rgb

        // Result backgrounds
        val calculatedMaxWidth = drawResultWithBackground(resultString, graphics, resultColor)

        // Slider action
        drawSlider(graphics, calculatedMaxWidth)

        super.extractRenderState(graphics, mouseX, mouseY, a)
    }

    private fun drawRollingText(graphics: GuiGraphicsExtractor) {
        rollingText = if (isOver) "Your result is:"
        else updateRollingText()

        RenderUtils.renderScaledText(
            graphics, rollingText,
            getRollingX() - 5, getRollingY(), 10,
            Color.WHITE.rgb,
            1.5f, true
        )
    }

    private fun calculateRollingTextColor() : Int {
        val min = 100
        val max = 200

        val period = 2.5 // seconds per full cycle

        val t = System.nanoTime() / 1e9
        val mid = (min + max) / 2.0
        val amp = (max - min) / 2.0

        val g = (mid + amp * sin(2 * Math.PI * t / period)).roundToInt()

        return Color(g, g, g).rgb
    }

    private fun drawSlider(graphics: GuiGraphicsExtractor, maxWidth: Int) {
        if (!isOver) {
            val passed = (endTimer.getElapsedTime(TimeUnit.MILLISECONDS) / 50).toInt()

            rollingSliderPercentage = 1f - passed / 100f
            val sliderToDraw = (maxWidth * (rollingSliderPercentage)).toInt()

            val rColor = (initR + (255 - initR) * passed / 100)
            val gColor = (initG + (255 - initG) * passed / 100)
            val bColor = (initB + (255 - initB) * passed / 100)
            graphics.fill(
                getSliderX(),
                getSliderY(),
                sliderToDraw + getSliderX(),
                getSliderY() - 3,
                Color(rColor, gColor, bColor, 255).rgb
            )
        }
    }

    private fun drawResultWithBackground(
        resultString: String,
        graphics: GuiGraphicsExtractor,
        resultColor: Int
    ): Int {
        val font = Minecraft.getInstance().font
        val textScale = 4f

        val rawWidth = font.width(Component.literal(resultString))
        val rawHeight = font.lineHeight
        val scaledWidth = ceil(rawWidth * textScale).toInt()
        val scaledHeight = ceil(rawHeight * textScale).toInt()

        graphics.fill(
            getResultX() - 10,
            getResultY() - 10,
            getResultX() + scaledWidth + 10,
            getResultY() + scaledHeight,
            //Color(40, 40, 40, 100).rgb
                    Color(40, 40, 40, 100).rgb
        )
        graphics.fill(
            getResultX() - 7,
            getResultY() - 7,
            getResultX() + scaledWidth + 7,
            getResultY() + scaledHeight - 3,
            Color(102, 102, 102, 100).rgb
        )

        RenderUtils.renderScaledText(
            graphics, resultString,
            getResultX(), getResultY(), 20, resultColor, 4f, isOver
        )

        return scaledWidth
    }

    /**
     * Position helpers
     */

    private fun calcImageSize() : Int {
        return creeperWidth
    }

    private fun getRollingX() : Int {
        return width / 2 + width / 16 + 2
    }

    private fun getRollingY() : Int {
        return height / 2 - mcFont.lineHeight - 27
    }

    private fun getResultX() : Int {
        return getRollingX() + 2
    }

    private fun getResultY() : Int {
        return getRollingY() + mcFont.lineHeight * 4 - 10
    }

    private fun getSliderX() : Int {
        return getResultX() - 10
    }

    private fun getSliderY() : Int {
        return getResultY() + 40
    }

    private fun getPictureX() : Int {
        return width / 2 - calcImageSize() - width / 16 + (calcImageSize() / 6)
    }

    private fun getPictureY() : Int {
        return height / 2 - calcImageSize() / 2
    }

    /**
     * Other helpers
     */

    private fun getResultString() : String {
        return if (!isOver) getStateString()
        else if(state == State.ALLOW) "Allowed" else "Denied"
    }

    private fun updateRollingText() : String {
        val output = StringBuilder()
        output.append("Rolling")

        if (rollTextTimer.passed(250)) {
            rollTextTimer.reset()
            rtState++
            if (rtState > 3) rtState = 0
        }

        for (i in 0..<rtState) {
            output.append(".")
        }

        return output.toString()
    }

    private fun isRollOver() {
        if (!endTimer.passed(5000)) return
        isOver = true

        overTimer.reset()
        if (state == State.DENY) Minecraft.getInstance().player?.playSound(SoundEvents.PLAYER_LEVELUP)
        else {
            Minecraft.getInstance().player?.playSound(SoundEvents.CREEPER_PRIMED)
            Minecraft.getInstance().player?.playSound(SoundEvents.VILLAGER_NO)
        }
    }

    private fun shouldSendPacket() {
        if (!overTimer.passed(2000)) return

        if (state == State.DENY) Services.SEND_PAYLOAD.sendPayload(LuckyPayload(creeperId))
        Services.SEND_PAYLOAD.sendPayload(ResponsePayload(creeperId, state == State.ALLOW, playerInitialized = true))
        onClose()
    }

    private fun getStateString() : String {
        if (secTimer.passed(1000)) {
            if (currentStep != 3) currentStep += 1
            secTimer.reset()
        }

        if (rollTimer.passed(steps[currentStep])) {
            trigger = !trigger
            rollTimer.reset()

            Minecraft.getInstance().player?.playSound(SoundEvents.FLINTANDSTEEL_USE)
        }


        if (trigger) {
            state = State.ALLOW
            return "Allow"
        } else {
            state = State.DENY
            return "Deny"
        }
    }

    enum class State {
        ALLOW, DENY
    }

    /**
     * Necessary overrides
     */
    override fun shouldCloseOnEsc(): Boolean {
        return false
    }

    override fun isPauseScreen(): Boolean {
        return false
    }
}