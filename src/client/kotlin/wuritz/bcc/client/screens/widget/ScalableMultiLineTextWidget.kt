package wuritz.bcc.client.screens.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.MultiLineTextWidget
import net.minecraft.network.chat.Component
import java.awt.TextComponent

class ScalableMultiLineTextWidget(text: Component, mcFont: Font, val scale: Float) : MultiLineTextWidget(text, mcFont) {

    init {
        this.width = (width * scale).toInt()
        this.height = (height * scale).toInt()
    }

    override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        val pose = graphics.pose()

        pose.pushMatrix()
        pose.translate(x.toFloat(), y.toFloat())
        pose.scale(scale, scale)
        pose.translate(-x.toFloat(), -y.toFloat())
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a)
        pose.popMatrix()
    }

}