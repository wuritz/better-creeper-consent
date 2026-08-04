package wuritz.bcc.client.utils

import net.minecraft.resources.Identifier
import wuritz.bcc.BetterCreeperConsent

enum class CreeperPersonalities {
    Normal, Official, Bleeh
}

data class CreeperAnswers(val allow: String, val deny: String, val gambling: String)
data class CreeperTooltips(val allow: String, val deny: String, val gambling: String)

data class Creeper(val imagePath: String, val personality: CreeperPersonalities, val answers: CreeperAnswers, val tooltips: CreeperTooltips)

class Creepers {

    val creepers = listOf(
        Creeper("creepers/crp1.png",
            CreeperPersonalities.Normal,
            CreeperAnswers("Allow", "Deny", "Roll!"),
            CreeperTooltips("The creeper explodes.", "No explosion.", "Roll the outcome.")),

        Creeper("creepers/crp1.png",
            CreeperPersonalities.Official,
            CreeperAnswers("Grant", "Refuse", "Gamble!"),
            CreeperTooltips("Grant the creeper permission to initiate detonation", "Prohibit the creeper's detonation", "Proceed with an uncalculated risk")),

        Creeper("creepers/crp1.png",
            CreeperPersonalities.Bleeh,
            CreeperAnswers("allow :3", "deny ::(", "GAMBLING!! :D"),
            CreeperTooltips("i will explode!", "i will go away :((", "YAY I LOVE GAMBLING!!")),
    )

    fun getRandomCreeper() : Creeper {
        return creepers.random()
    }

    fun getImage(imagePath: String) : Identifier {
        return BetterCreeperConsent.id(imagePath)
    }
}