package wuritz.bcc.client.screens.options

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.ImageWidget
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import wuritz.bcc.BetterCreeperConsent
import java.awt.Color

/**
 * I'm trying to do this with more FP-like methods, I think.
 * Just saw a reel today and thought I could try it out lol
 */
class OptionsScreen(val id: Int) : Screen(Component.literal("Options Menu")) {

    var boxWidth = 0
    var boxHeight = 0

    override fun init() {
        boxWidth = calcBoxWidth()
        boxHeight = calcBoxHeight()

        addRenderableWidget(ImageWidget.texture(350, 100, getTitleImage(), 350, 100))
            .setPosition(boxEdgeLeft(), boxEdgeTop())

        val buttonWidth = 60
        val buttonHeight = 25
        val buttonGap = 5

        val buttonsBoxWidth = buttonWidth * 2 + buttonGap
        val cancelX = boxEdgeRight() - buttonsBoxWidth - 5
        val cancelY = boxEdgeBottom() - 5
        val saveX = cancelX + buttonWidth + buttonGap
        val saveY = cancelY

        addRenderableWidget(Button.builder(Component.literal("Cancel")) { pressedCancel() }
            .bounds(cancelX, cancelY, buttonWidth, buttonHeight)
            .build())

        addRenderableWidget(Button.builder(Component.literal("Save")) { pressedSave() }
            .bounds(saveX, saveY, buttonWidth, buttonHeight)
            .build())

        BetterCreeperConsent.LOG.info(height.toString())
        BetterCreeperConsent.LOG.info(boxHeight.toString())
    }

    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        // Background for positioning
        graphics.fill(boxEdgeLeft(), boxEdgeTop(), boxEdgeRight(), boxEdgeBottom(), Color(80, 80, 80, 155).rgb)

        super.extractRenderState(graphics, mouseX, mouseY, a)
    }

    private fun getTitleImage() : Identifier {
        return BetterCreeperConsent.id("others/title.png")
    }

    /**
     * Position
     */
    fun boxEdgeLeft() : Int {
        return (width - boxWidth) / 2
    }

    fun boxEdgeRight() : Int {
        return boxEdgeLeft() + boxWidth
    }

    fun boxEdgeBottom() : Int {
        return boxHeight
    }

    fun boxEdgeTop() : Int {
        return boxEdgeBottom() - boxHeight
    }

    fun calcBoxWidth() : Int {
        return (width * 0.6).toInt()
    }

    fun calcBoxHeight() : Int {
        return (height * 0.9).toInt()
    }

    /**
     * Button presses
     */
    fun pressedCancel() {
        onClose()
    }

    fun pressedSave() {
        onClose()
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