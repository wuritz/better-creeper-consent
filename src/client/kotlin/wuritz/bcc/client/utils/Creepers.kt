package wuritz.bcc.client.utils

import net.minecraft.resources.Identifier
import wuritz.bcc.BetterCreeperConsent

enum class CreeperPersonalities {
    Normal, Official, Bleeh, King
}

data class CreeperAnswers(val allow: String, val deny: String, val gambling: String)
data class CreeperTooltips(val allow: String, val deny: String, val gambling: String)

data class Creeper(val imagePath: String, val personality: CreeperPersonalities, val answers: CreeperAnswers, val tooltips: CreeperTooltips)

class Creepers {
    val creepers = listOf(
        Creeper("creepers/crp_normal.png",
            CreeperPersonalities.Normal,
            CreeperAnswers("Allow", "Deny", "Roll!"),
            CreeperTooltips("The creeper explodes.", "No explosion.", "Roll the outcome.")),

        Creeper("creepers/crp_official.png",
            CreeperPersonalities.Official,
            CreeperAnswers("Grant", "Refuse", "Gamble!"),
            CreeperTooltips("Grant the creeper permission to initiate detonation", "Prohibit the creeper's detonation", "Proceed with an uncalculated risk")),

        Creeper("creepers/crp_bleeh.png",
            CreeperPersonalities.Bleeh,
            CreeperAnswers("allow :3", "deny ::(", "GAMBLING!! :D"),
            CreeperTooltips("i will explode!", "i will go away :((", "YAY I LOVE GAMBLING!!")),

        Creeper("creepers/crp_king.png",
            CreeperPersonalities.King,
            CreeperAnswers("Allow!", "Deny!", "Gambling!"),
            CreeperTooltips("Allow thy king to fulfill his explosive mission", "Refuse to yield the way to thy king", "Leave thy king's passage to the unpredictable whims of chance"))
    )

    fun getRandomCreeper() : Creeper {
        return creepers.random()
    }

    fun getImage(imagePath: String) : Identifier {
        return BetterCreeperConsent.id(imagePath)
    }
}