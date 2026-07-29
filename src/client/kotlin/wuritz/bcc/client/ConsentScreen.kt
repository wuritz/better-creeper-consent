package wuritz.bcc.client

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import wuritz.bcc.network.payloads.ResponsePayload

class ConsentScreen(val creeperId: Int) : Screen(Component.literal("Consent Screen")) {

    // box
    val BOX_WIDTH = 230
    val BOX_HEIGHT = 120

    // button
    val BUTTON_WIDTH = 85
    val BUTTON_HEIGHT = 20
    val BUTTON_GAP = 12

    override fun init() {
        val allowButtonX = getAllowButtonX()
        val denyButtonX = getDenyButtonX()
        val buttonY = getButtonY()

        addRenderableWidget(Button.builder(
            Component.literal("allow :3")
        ) { button -> onAllowPressed() }
            .bounds(allowButtonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)
            .build())

        addRenderableWidget(Button.builder(
            Component.literal("deny ::(")
        ) { button -> onDenyPressed() }
            .bounds(denyButtonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)
            .build())
    }

    /**
     * Render
     */
    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        graphics.fill(0, 0, width, height, 0xAA050A05.toInt())

        val boxX = this.width / 2 - BOX_WIDTH / 2;
        val boxY = this.height / 2 - BOX_HEIGHT / 2;

        graphics.fill(boxX, boxY, boxX + BOX_WIDTH, boxY + BOX_HEIGHT, 0xFF102610.toInt());

        graphics.fill(boxX + 3, boxY + 3, boxX + BOX_WIDTH - 3, boxY + BOX_HEIGHT - 3, 0xFF0B180B.toInt());

        drawButtonAura(graphics, getAllowButtonX(), getButtonY(), BUTTON_WIDTH, BUTTON_HEIGHT, true);
        drawButtonAura(graphics, getDenyButtonX(), getButtonY(), BUTTON_WIDTH, BUTTON_HEIGHT, false);

        super.extractRenderState(graphics, mouseX, mouseY, a)
    }

    private fun drawButtonAura(
        graphics: GuiGraphicsExtractor,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        allowButton: Boolean
    ) {
        if (allowButton) {
            // Red creepy boom button
            graphics.fill(x - 2, y - 2, x + width + 2, y + height + 2, -0x55c60000)
            graphics.fill(x - 1, y - 1, x + width + 1, y + height + 1, -0x77b0f5f6)
            graphics.outline(x - 2, y - 2, width + 4, height + 4, -0x55ddde)
        } else {
            // Green creepy stop button
            graphics.fill(x - 2, y - 2, x + width + 2, y + height + 2, -0x55f9c5fa)
            graphics.fill(x - 1, y - 1, x + width + 1, y + height + 1, -0x77cd55ce)
            graphics.outline(x - 2, y - 2, width + 4, height + 4, -0xc300a6)
        }
    }

    /**
     * Click handlers
     */
    fun onAllowPressed() {
        ClientPlayNetworking.send(ResponsePayload(creeperId, true))
        onClose()
    }

    fun onDenyPressed() {
        ClientPlayNetworking.send(ResponsePayload(creeperId, false))
        onClose()
    }


    /**
     * Position helpers
     */

    private fun getButtonY() : Int {
        val boxY = height / 2 - BOX_HEIGHT / 2
        return boxY + BOX_HEIGHT - 25
    }

    private fun getAllowButtonX(): Int {
        val totalButtonWidth = BUTTON_WIDTH * 2 + BUTTON_GAP
        return width / 2 - totalButtonWidth / 2
    }

    private fun getDenyButtonX(): Int {
        return getAllowButtonX() + BUTTON_WIDTH + BUTTON_GAP
    }

    /**
     * Neccessary overrides
     */
    override fun shouldCloseOnEsc(): Boolean {
        return false
    }

    override fun isPauseScreen(): Boolean {
        return false
    }

}