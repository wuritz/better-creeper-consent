/*
 * Copyright (c) 2026. wuritz
 * SPDX-License-Identifier: Apache-2.0
 */

package wuritz.bcc.screens

import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.*
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import wuritz.bcc.BCCCommon
import wuritz.bcc.utils.Creepers
import wuritz.bcc.network.payloads.incoming.ResponsePayload
import wuritz.bcc.platform.Services
import java.awt.Color

class ConsentScreen(val creeperId: Int) : Screen(Component.literal("Consent")) {

    val creeper = Creepers().getRandomCreeper()
    val creeperVisual = Creepers().getImage(creeper.imagePath)
    val signId = BCCCommon.id("others/sign.png")

    // button
    val BUTTON_HEIGHT = 20

    var allowedToClose = false

    val totalButtonWidth = 130
    lateinit var allowButton: Button

    // Coordinates

    override fun init() {
        /**
         * Buttons
         */

        // Allow
        allowButton = Button.builder(Component.literal(creeper.answers.allow)) { pressedAllow() }
            .bounds(getButtonX(), getAllowButtonY(), totalButtonWidth, BUTTON_HEIGHT)
            .tooltip(Tooltip.create(Component.literal(
                creeper.tooltips.allow
            ).withStyle(ChatFormatting.GREEN)))
            .build()

        addRenderableWidget(allowButton)

        // Deny
        addRenderableWidget(Button.builder(Component.literal(creeper.answers.deny)) { pressedDeny() }
            .bounds(getButtonX(), getDenyButtonY(), totalButtonWidth, BUTTON_HEIGHT)
            .tooltip(Tooltip.create(Component.literal(
                creeper.tooltips.deny
            ).withStyle(ChatFormatting.RED)))
            .build())

        // Gambling
        addRenderableWidget(Button.builder(Component.literal(creeper.answers.gambling)) { pressedGambling() }
            .bounds(getButtonX(), getGamblingButtonY(), totalButtonWidth, BUTTON_HEIGHT)
            .tooltip(Tooltip.create(Component.literal(
                creeper.tooltips.gambling
            ).withStyle(ChatFormatting.YELLOW)))
            .build())

        /**
         * Text
         */

        addRenderableWidget(MultiLineTextWidget(Component.literal("A creeper is asking for permission to explode."), Minecraft.getInstance().font))
            .setMaxWidth(130)
            .setPosition(getButtonX(), getAllowButtonY() - 55)
        addRenderableWidget(StringWidget(Component.literal("Make your decision!"), Minecraft.getInstance().font))
            .setPosition(getButtonX(), getAllowButtonY() - 25)

        /**
         * Pictures
         */
        val imageSize = calcImageSize()
        addRenderableWidget(ImageWidget.texture(
            imageSize, imageSize,
            creeperVisual,
            imageSize, imageSize))
            .setPosition(getPictureX(), getPictureY())

        val signX = calcSignX()
        val signY = calcSignY()
        val signW = calcSignSize()
        val signH = signW / 2

        addRenderableWidget(ImageWidget.texture(signW, signH, signId, signW, signH))
            .setPosition(signX, signY)

        //val signTextScale = width / 960f * guiScale / 2
        val signTextW = MultiLineTextWidget(Component.literal(creeper.question), Minecraft.getInstance().font)

        signTextW.setMaxWidth(90)
                .setCentered(true)

        val signWidgetWidth = signTextW.width
        val signWidgetHeight = signTextW.height

        signTextW.setPosition(signX + (signW / 2) - signWidgetWidth / 2, signY + (signH / 2) - signWidgetHeight / 2) // genius shit
        addRenderableWidget(signTextW)
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
        drawButtonAuras(mouseX, mouseY, graphics)

        super.extractRenderState(graphics, mouseX, mouseY, a)
    }

    private fun drawButtonAuras(mouseX: Int, mouseY: Int, graphics: GuiGraphicsExtractor) {
        val auraX = arrayOf(getButtonX() - 2, getButtonX() + totalButtonWidth + 2) // x0, x1

        val allowAura = arrayOf(getAllowButtonY() - 2, getAllowButtonY() + 2 + BUTTON_HEIGHT) // y0, y1
        val denyAura = arrayOf(getDenyButtonY() - 2, getDenyButtonY() + 2 + BUTTON_HEIGHT) // y0, y1
        val gamblingAura = arrayOf(getGamblingButtonY() - 2, getGamblingButtonY() + BUTTON_HEIGHT + 2) // y0, y1

        var allowAuraColor = Color(87, 255, 92, 100).rgb
        var denyAuraColor = Color(255, 110, 110, 100).rgb
        var gamblingAuraColor = Color(244, 255, 110, 100).rgb

        if (mouseX >= auraX[0] && mouseX <= auraX[1]) {
            allowAuraColor = if (mouseY >= allowAura[0] && mouseY <= allowAura[1])
                Color(87, 255, 92, 230).rgb
            else
                Color(87, 255, 92, 100).rgb

            denyAuraColor = if (mouseY >= denyAura[0] && mouseY <= denyAura[1])
                Color(255, 110, 110, 230).rgb
            else
                Color(255, 110, 110, 100).rgb

            gamblingAuraColor = if (mouseY >= gamblingAura[0] && mouseY <= gamblingAura[1])
                Color(244, 255, 110, 230).rgb
            else
                Color(244, 255, 110, 100).rgb
        }

        graphics.fill(
            auraX[0], allowAura[0], auraX[1], allowAura[1],
            allowAuraColor
        )

        graphics.fill(
            auraX[0], denyAura[0], auraX[1], denyAura[1],
            denyAuraColor
        )

        graphics.fill(
            auraX[0], gamblingAura[0], auraX[1], gamblingAura[1],
            gamblingAuraColor
        )
    }

    /**
     * Click handlers
     */
    fun pressedAllow() {
        Services.SEND_PAYLOAD.sendPayload(ResponsePayload(creeperId, true, playerInitialized = true))
        allowedToClose = true
        onClose()
    }

    fun pressedDeny() {
        Services.SEND_PAYLOAD.sendPayload(ResponsePayload(creeperId, false, playerInitialized = true))
        allowedToClose = true
        onClose()
    }

    fun pressedGambling() {
        allowedToClose = true
        onClose()
        Minecraft.getInstance().setScreenAndShow(GamblingScreen(creeperId, creeperVisual, allowButton.width))
    }

    override fun onClose() {
        if (!allowedToClose) Services.SEND_PAYLOAD.sendPayload(ResponsePayload(creeperId, false, playerInitialized = true))
        super.onClose()
    }

    /**
     * Position helpers
     */

    private fun getButtonX() : Int {
        return width / 2 + width / 16
    }

    private fun getAllowButtonY() : Int {
        val boxY = height / 2 - BUTTON_HEIGHT
        return boxY
    }

    private fun getDenyButtonY() : Int {
        return getAllowButtonY() + BUTTON_HEIGHT + 10
    }

    private fun getGamblingButtonY() : Int {
        //return (height * 0.75 - 40).toInt()
        return getDenyButtonY() + BUTTON_HEIGHT + 10
    }

    private fun calcImageSize() : Int {
        //return width / 6 * (minecraft.window.guiScale / 2)
        return allowButton.width
    }

    private fun getPictureX() : Int {
        return width / 2 - calcImageSize() - width / 16 + (calcImageSize() / 6)
    }

    private fun getPictureY() : Int {
        return height / 2 - calcImageSize() / 2 - height / 16
    }

    private fun calcSignX() : Int {
        return getPictureX() + (calcImageSize() / 2 - calcSignSize() / 2)
    }

    private fun calcSignY() : Int {
        return (height * 0.75 - height / 6).toInt()
    }

    private fun calcSignSize() : Int {
        return (calcImageSize() * 0.8).toInt()
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