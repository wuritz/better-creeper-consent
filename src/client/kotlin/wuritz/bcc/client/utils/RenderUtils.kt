package wuritz.bcc.client.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.network.chat.Component

object RenderUtils {

    fun renderScaledText(graphics: GuiGraphicsExtractor, text: String, textX: Int, textY: Int, textWidth: Int, color: Int, scale: Float) {
        val matrices = graphics.pose()

        matrices.pushMatrix()
        matrices.scale(scale, scale)

        graphics.textWithBackdrop(Minecraft.getInstance().font,
            Component.literal(text),
            (textX / scale).toInt(), (textY / scale).toInt(), textWidth, color)

        matrices.popMatrix()
    }

}