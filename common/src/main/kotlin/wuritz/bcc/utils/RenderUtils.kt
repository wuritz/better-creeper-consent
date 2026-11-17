/*
 * Copyright (c) 2026. wuritz
 * SPDX-License-Identifier: Apache-2.0
 */

package wuritz.bcc.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.network.chat.Component
import kotlin.math.ceil

object RenderUtils {

    /**
     * @return array of scaled width & height
     */
    fun renderScaledText(graphics: GuiGraphicsExtractor, text: String, textX: Int, textY: Int, textWidth: Int, color: Int, scale: Float, dropShadow: Boolean) {
        val matrices = graphics.pose()

        matrices.pushMatrix()
        matrices.scale(scale, scale)

        graphics.text(Minecraft.getInstance().font,
            Component.literal(text),
            (textX / scale).toInt(), (textY / scale).toInt(), color, dropShadow)

        matrices.popMatrix()
    }

    fun getTextWidth(text: String): Int {
        return Minecraft.getInstance().font.width(text)
    }

}