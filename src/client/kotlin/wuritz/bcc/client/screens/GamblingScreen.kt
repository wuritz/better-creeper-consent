package wuritz.bcc.client.screens

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import wuritz.bcc.client.utils.timer.CacheTimer
import wuritz.bcc.network.payloads.ResponsePayload
import java.awt.Color

class GamblingScreen(val creeperId: Int) : Screen(Component.literal("Consent Gambling")) {

    var state = State.ALLOW

    val secTimer = CacheTimer()
    val rollTimer = CacheTimer()
    val endTimer = CacheTimer()
    val overTimer = CacheTimer()

    val steps = listOf(50, 100, 250, 500)
    var currentStep = 0
    var trigger = false

    var isOver = false

    override fun init() {
        secTimer.reset()
        rollTimer.reset()
        endTimer.reset()

        isOver = false
    }

    /**
     * Render functions
     */

    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        // Background
        graphics.fill(0, 0, width, height, 0xAA050A05.toInt())

        if (!isOver) {
            renderScaledText(graphics, getStateString(),
                width / 2, height / 2, 20, Color.WHITE.rgb, 2f)
            isRollOver()
        } else {
            shouldSendPacket()

            val renderText = if(state == State.ALLOW) "Allowed" else "Denied"
            renderScaledText(graphics, renderText,
                width / 2 - Minecraft.getInstance().font.width(renderText), height / 2 + Minecraft.getInstance().font.lineHeight, 20,
                if (state == State.ALLOW) Color.GREEN.rgb else Color.RED.rgb,
                4f)
        }

        super.extractRenderState(graphics, mouseX, mouseY, a)
    }

    private fun isRollOver() {
        if (!endTimer.passed(5000)) return
        isOver = true

        overTimer.reset()
    }

    private fun shouldSendPacket() {
        if (!overTimer.passed(2000)) return

        ClientPlayNetworking.send(ResponsePayload(creeperId, state == State.ALLOW))
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
        }

        if (trigger) {
            state = State.ALLOW
            return "Allow"
        } else {
            state = State.DENY
            return "Deny"
        }
    }

    /**
     * Helpers
     */

    private fun renderScaledText(graphics: GuiGraphicsExtractor, text: String, textX: Int, textY: Int, textWidth: Int, color: Int, scale: Float) {
        val matrices = graphics.pose()

        matrices.pushMatrix()
        matrices.scale(scale, scale)

        graphics.textWithBackdrop(Minecraft.getInstance().font,
            Component.literal(text),
            (textX / scale).toInt(), (textY / scale).toInt(), textWidth, color)

        matrices.popMatrix()
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