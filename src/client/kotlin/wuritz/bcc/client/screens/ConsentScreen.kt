package wuritz.bcc.client.screens

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.AbstractStringWidget
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.ImageWidget
import net.minecraft.client.gui.components.MultiLineTextWidget
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import wuritz.bcc.BetterCreeperConsent
import wuritz.bcc.client.utils.Creepers
import wuritz.bcc.client.utils.RenderUtils
import wuritz.bcc.network.payloads.ResponsePayload
import java.awt.Color

class ConsentScreen(val creeperId: Int) : Screen(Component.literal("Consent")) {

    val creeper = Creepers().getRandomCreeper()
    val creeperVisual = Creepers().getImage(creeper.imagePath)
    val signId = BetterCreeperConsent.id("others/sign.png")

    // box
    val BOX_HEIGHT = 120

    // button
    val BUTTON_HEIGHT = 20

    var allowedToClose = false

    val totalButtonWidth = 150

    override fun init() {
        /**
         * Text
         */
        addRenderableWidget(StringWidget(Component.literal("A creeper is asking for permission to explode."), Minecraft.getInstance().font))

        //.setPosition(width / 2 - RenderUtils.getTextWidth("A creeper is asking for permission to explode.") / 2,
                //height / 8)
        addRenderableWidget(StringWidget(Component.literal("Make your decision!"), Minecraft.getInstance().font))
            .setPosition(width / 2 - RenderUtils.getTextWidth("Make your decision!") / 2,
                height / 8 + Minecraft.getInstance().font.lineHeight + 5)

        /**
         * Pictures
         */
        addRenderableWidget(ImageWidget.texture(150, 150, creeperVisual, 150, 150))
            .setPosition(getPictureX(), getPictureY())

        val signY = (height * 0.75 - 70).toInt()
        addRenderableWidget(ImageWidget.texture(100, 50, signId, 100, 50))
            .setPosition(getPictureX() + 30, signY)
        val signWidget = MultiLineTextWidget(Component.literal(creeper.question), Minecraft.getInstance().font)
        signWidget.setMaxWidth(90)
                .setCentered(true)
        val signWidgetWidth = signWidget.width
        val signWidgetHeight = signWidget.height
        signWidget.setPosition(getPictureX() + 30 + 50 - signWidgetWidth / 2, signY + 25 - signWidgetHeight / 2) // genius shit
        addRenderableWidget(signWidget)

        /**
         * Buttons
         */

        // Allow
        addRenderableWidget(Button.builder(Component.literal(creeper.answers.allow)) { b -> pressedAllow() }
            .bounds(getButtonX(), getAllowButtonY(), totalButtonWidth, BUTTON_HEIGHT)
            .tooltip(Tooltip.create(Component.literal(
                creeper.tooltips.allow
            ).withStyle(ChatFormatting.GREEN)))
            .build())

        // Deny
        addRenderableWidget(Button.builder(Component.literal(creeper.answers.deny)) { b -> pressedDeny() }
            .bounds(getButtonX(), getDenyButtonY(), totalButtonWidth, BUTTON_HEIGHT)
            .tooltip(Tooltip.create(Component.literal(
                creeper.tooltips.deny
            ).withStyle(ChatFormatting.RED)))
            .build())

        // Gambling
        addRenderableWidget(Button.builder(Component.literal(creeper.answers.gambling)) { b -> pressedGambling() }
            .bounds(getButtonX(), getGamblingButtonY(), totalButtonWidth, BUTTON_HEIGHT)
            .tooltip(Tooltip.create(Component.literal(
                creeper.tooltips.gambling
            ).withStyle(ChatFormatting.YELLOW)))
            .build())
    }

    /**
     * Render
     */
    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        //graphics.fill(width / 2 - 2, height, width / 2 + 2, 0, Color(26, 125, 74, 255).rgb)
        //graphics.fill(0, height / 2 - 2, width, height / 2 + 2, Color(26, 125, 74, 255).rgb)

        // Background
        graphics.fill(0, 0, width, height, 0xAA050A05.toInt())

        // Another background
        /*graphics.fill(
            getPictureX() - 25,
            getPictureY() - 25,
            getDenyButtonX() + BUTTON_WIDTH + 45,
            getPictureY() + 300,
            Color(45, 61, 43, 180).rgb)
        graphics.fill(
            getPictureX() - 20,
            getPictureY() - 20,
            getDenyButtonX() + BUTTON_WIDTH + 40,
            getPictureY() + 295,
            Color(17, 23, 16, 180).rgb)*/

        // Middle separator
        graphics.fill(width / 2 - 1, height / 4, width / 2 + 1, (height * 0.75).toInt(), Color(100, 100, 100, 255).rgb)

        // Button auras
        graphics.fill(getButtonX() - 2, getAllowButtonY() - 2,
            getButtonX() + totalButtonWidth + 2, getAllowButtonY() + 2 + BUTTON_HEIGHT,
            Color(87, 255, 92, 200).rgb)

        graphics.fill(getButtonX() - 2, getDenyButtonY() - 2,
            getButtonX() + totalButtonWidth + 2, getDenyButtonY() + 2 + BUTTON_HEIGHT,
            Color(255, 110, 110, 200).rgb)

        graphics.fill(getButtonX() - 2, getGamblingButtonY() - 2,
            getButtonX() + totalButtonWidth + 2, getGamblingButtonY() + BUTTON_HEIGHT + 2,
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

    private fun getButtonX() : Int {
        return width / 2 + 70
    }

    private fun getAllowButtonY() : Int {
        val boxY = height / 2 - BUTTON_HEIGHT - 10
        return boxY
    }

    private fun getDenyButtonY() : Int {
        return getAllowButtonY() + BUTTON_HEIGHT + 10
    }

    private fun getGamblingButtonY() : Int {
        return (height * 0.75 - 40).toInt()
    }

    private fun getPictureX() : Int {
        return width / 2 - 190
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