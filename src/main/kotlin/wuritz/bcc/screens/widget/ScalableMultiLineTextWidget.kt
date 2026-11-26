/*
 * Copyright (c) 2026. wuritz
 * SPDX-License-Identifier: Apache-2.0
 */

package wuritz.bcc.screens.widget

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.MultiLineTextWidget
import net.minecraft.network.chat.Component

// May need this later
class ScalableMultiLineTextWidget(text: Component, mcFont: Font, val scale: Float) : MultiLineTextWidget(text, mcFont) {

    init {
        this.width = (width * scale).toInt()
        this.height = (height * scale).toInt()
    }

    override fun renderWidget(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        val pose = graphics.pose()

        pose.pushPose()
        pose.translate(x.toFloat(), y.toFloat(), 0f)
        pose.scale(scale, scale, 1f)
        pose.translate(-x.toFloat(), -y.toFloat(), 0f)
        super.renderWidget(graphics, mouseX, mouseY, partialTick)
        pose.popPose()
    }

}
