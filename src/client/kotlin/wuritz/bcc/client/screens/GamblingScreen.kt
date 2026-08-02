package wuritz.bcc.client.screens

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.ImageWidget
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import wuritz.bcc.BetterCreeperConsent
import wuritz.bcc.client.utils.RenderUtils
import wuritz.bcc.client.utils.timer.CacheTimer
import wuritz.bcc.network.payloads.LuckyPayload
import wuritz.bcc.network.payloads.ResponsePayload
import java.awt.Color
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class GamblingScreen(val creeperId: Int) : Screen(Component.literal("Consent Gambling")) {

    val yelopic = BetterCreeperConsent.id("yelo.png")
    var state = State.ALLOW

    val secTimer = CacheTimer()
    val rollTimer = CacheTimer()
    val endTimer = CacheTimer()
    val overTimer = CacheTimer()
    val sliderTimer = CacheTimer()

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
    var rollingText = "Rolling..."
    var rollingSliderPercentage = 1f
    val initR = 0 // Random.nextInt(0, 255)
    val initG = 255 // Random.nextInt(0, 255)
    val initB = 0 // Random.nextInt(0, 255)

    override fun init() {
        secTimer.reset()
        rollTimer.reset()
        endTimer.reset()
        sliderTimer.reset()

        addRenderableWidget(ImageWidget.texture(200, 200, yelopic, 200, 200)
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

        /*
        // Another background
        graphics.fill(
            getPictureX() - 25,
            getPictureY() - 25,
            getResultX() + 171 + 25,
            getPictureY() + 225,
            Color(45, 61, 43, 180).rgb)
        graphics.fill(
            getPictureX() - 20,
            getPictureY() - 20,
            getResultX() + 171 + 20,
            getPictureY() + 220,
            Color(17, 23, 16, 180).rgb)*/

        if (!isOver) isRollOver()
        else shouldSendPacket()

        // Rolling text
        if (isOver) rollingText = "Your result is:"
        RenderUtils.renderScaledText(graphics, rollingText,
            getRollingX() - 5, getRollingY(), 10, Color.WHITE.rgb, 1.5f)

        // Result
        val resultString = getResultString()
        val resultColor = if (!isOver) Color.WHITE.rgb else if (state == State.ALLOW) Color.GREEN.rgb else Color.RED.rgb

        // Result backgrounds
        graphics.fill(getResultX() - 10, getResultY() - 10, getResultX() + 160, getResultY() + 40, Color(40, 40, 40, 100).rgb)
        graphics.fill(getResultX() - 7, getResultY() - 7, getResultX() + 157, getResultY() + 37, Color(102, 102, 102, 100).rgb)

        RenderUtils.renderScaledText(graphics, resultString,
            getResultX(), getResultY(), 20, resultColor, 4f)

        // Slider action
        if (!isOver) {
            val passed = (endTimer.getElapsedTime(TimeUnit.MILLISECONDS) / 50).toInt()

            rollingSliderPercentage = 1f - passed/100f
            val sliderToDraw = (140 * (rollingSliderPercentage)).toInt()

            val rColor = (initR + (255 - initR) * passed/100)
            val gColor = (initG + (255 - initG) * passed/100)
            val bColor = (initB + (255 - initB) * passed/100)
            graphics.fill(getSliderX(), getSliderY(), sliderToDraw + getSliderX(), getSliderY() - 3, Color(rColor, gColor, bColor, 255).rgb)
        }

        super.extractRenderState(graphics, mouseX, mouseY, a)
    }

    /**
     * Position helpers
     */

    private fun getRollingX() : Int {
        return width / 2 + 26
    }

    private fun getRollingY() : Int {
        return height / 2 - mcFont.lineHeight - 27
    }

    private fun getResultX() : Int {
        return getRollingX() + 2
    }

    private fun getResultY() : Int {
        return getRollingY() + mcFont.lineHeight * 4 - 8
    }

    private fun getSliderX() : Int {
        return getResultX() - 10
    }

    private fun getSliderY() : Int {
        return getResultY() + 40
    }

    private fun getPictureX() : Int {
        return width / 2 - 200
    }

    private fun getPictureY() : Int {
        return height / 2 - 100
    }

    /**
     * Other helpers
     */

    private fun getResultString() : String {
        return if (!isOver) getStateString()
        else if(state == State.ALLOW) "Allowed" else "Denied"
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

        if (state == State.DENY) ClientPlayNetworking.send(LuckyPayload(creeperId))
        ClientPlayNetworking.send(ResponsePayload(creeperId, state == State.ALLOW, playerInitialized = true))
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