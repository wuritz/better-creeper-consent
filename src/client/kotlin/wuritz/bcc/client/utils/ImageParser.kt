package wuritz.bcc.client.utils

import net.minecraft.resources.Identifier
import wuritz.bcc.BetterCreeperConsent

object ImageParser {

    val images = listOf(
        BetterCreeperConsent.id("creepers/crp1.png"),
        BetterCreeperConsent.id("creepers/crp2.png"),
    )

    fun getRandomCreeperImage() : Identifier {
        return images.random()
    }

}