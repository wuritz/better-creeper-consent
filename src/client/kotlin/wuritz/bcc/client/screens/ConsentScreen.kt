package wuritz.bcc.client.screens

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.ImageWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import wuritz.bcc.client.utils.CreeperPersonalities
import wuritz.bcc.client.utils.Creepers
import wuritz.bcc.network.payloads.ResponsePayload
import java.awt.Color

class ConsentScreen(val creeperId: Int) : Screen(Component.literal("Consent")) {

    val creeper = Creepers().getRandomCreeper()
    val creeperVisual = Creepers().getImage(creeper.imagePath)

    // box
    val BOX_WIDTH = 230
    val BOX_HEIGHT = 120

    // button
    val BUTTON_WIDTH = 85
    val BUTTON_HEIGHT = 20
    val BUTTON_GAP = 12

    var allowedToClose = false

    val totalButtonWidth = BUTTON_WIDTH * 2 + BUTTON_GAP

    override fun init() {
        val buttonY = getButtonY()
        val allowButtonX = getAllowButtonX()
        val denyButtonX = getDenyButtonX()
        val gamblingButtonX = getGamblingButtonX()

        addRenderableWidget(ImageWidget.texture(200, 200, creeperVisual, 200, 200))
            .setPosition(getPictureX(), getPictureY())

        /**
         * Allow
         */
        addRenderableWidget(Button.builder(Component.literal(creeper.answers.allow)) { b -> pressedAllow() }
            .bounds(allowButtonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)
            .tooltip(Tooltip.create(Component.literal(
                creeper.tooltips.allow
            )))
            .build())

        /**
         * Deny
         */
        addRenderableWidget(Button.builder(Component.literal(creeper.answers.deny)) { b -> pressedDeny() }
            .bounds(denyButtonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)
            .tooltip(Tooltip.create(Component.literal(
                creeper.tooltips.deny
            )))
            .build())

        /**
         * Gambling
         */
        addRenderableWidget(Button.builder(Component.literal(creeper.answers.gambling)) { b -> pressedGambling() }
            .bounds(gamblingButtonX, getGamblingButtonY(), totalButtonWidth, BUTTON_HEIGHT)
            .tooltip(Tooltip.create(Component.literal(
                creeper.tooltips.gambling
            ).withStyle(ChatFormatting.YELLOW)))
            .build())
    }

    /**
     * Render
     */
    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        // Background
        graphics.fill(0, 0, width, height, 0xAA050A05.toInt())
        /*
        // Another background
        graphics.fill(
            getPictureX() - 25,
            getPictureY() - 25,
            getDenyButtonX() + BUTTON_WIDTH + 25,
            getPictureY() + 225,
            Color(45, 61, 43, 180).rgb)
        graphics.fill(
            getPictureX() - 20,
            getPictureY() - 20,
            getDenyButtonX() + BUTTON_WIDTH + 20,
            getPictureY() + 220,
            Color(17, 23, 16, 180).rgb)*/

        graphics.fill(getAllowButtonX() - 2, getButtonY() - 2,
            getAllowButtonX() + BUTTON_WIDTH + 2, getButtonY() + 2 + BUTTON_HEIGHT,
            Color(87, 255, 92, 200).rgb)

        graphics.fill(getDenyButtonX() - 2, getButtonY() - 2,
            getDenyButtonX() + BUTTON_WIDTH + 2, getButtonY() + 2 + BUTTON_HEIGHT,
            Color(255, 110, 110, 200).rgb)

        graphics.fill(getGamblingButtonX() - 2, getGamblingButtonY() - 2,
            getGamblingButtonX() + totalButtonWidth + 2, getGamblingButtonY() + BUTTON_HEIGHT + 2,
            Color(244, 255, 110, 200).rgb)

        super.extractRenderState(graphics, mouseX, mouseY, a)
    }

    /**
     * Click handlers
     */
    fun pressedAllow() {
        ClientPlayNetworking.send(ResponsePayload(creeperId, true, playerInitialized = true))
        allowedToClose = true
        onClose()
    }

    fun pressedDeny() {
        ClientPlayNetworking.send(ResponsePayload(creeperId, false, playerInitialized = true))
        allowedToClose = true
        onClose()
    }

    fun pressedGambling() {
        allowedToClose = true
        onClose()
        Minecraft.getInstance().setScreenAndShow(GamblingScreen(creeperId, creeperVisual))
    }

    override fun onClose() {
        if (!allowedToClose) ClientPlayNetworking.send(ResponsePayload(creeperId, false, playerInitialized = true))
        super.onClose()
    }

    /**
     * Position helpers
     */

    private fun getButtonY() : Int {
        val boxY = height / 2 - BOX_HEIGHT / 2 - 10
        return boxY + 50
    }

    private fun getAllowButtonX(): Int {
        val totalButtonWidth = BUTTON_WIDTH * 2 + BUTTON_GAP
        return width / 2 - totalButtonWidth / 2 + 110
    }

    private fun getDenyButtonX(): Int {
        return getAllowButtonX() + BUTTON_WIDTH + BUTTON_GAP
    }

    private fun getGamblingButtonX() : Int {
        return getAllowButtonX()
    }

    private fun getGamblingButtonY() : Int {
        return getButtonY() + BUTTON_HEIGHT + 10
    }

    private fun getPictureX() : Int {
        return width / 2 - 200
    }

    private fun getPictureY() : Int {
        return height / 2 - 100
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