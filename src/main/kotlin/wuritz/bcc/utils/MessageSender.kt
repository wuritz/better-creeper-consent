package wuritz.bcc.utils

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import kotlin.random.Random

object MessageSender {

    val ALLOW_MSG = listOf(
        "Yay!", "The best choice!", "Sorry for your buildings :(", "I hope you don't have pets around >:("
    )

    val DENY_MSG = listOf(
        "Aw :(", "Maybe next time :(", "Your buildings are saved for now..", "My relatives will have a talk with you..",
        "I'm gonna go elsewhere then..", "Bye! :D", "Fair enough. :(", "Next time reconsider it please :("
    )

    fun sendAllowMsg(player: ServerPlayer) {
        player.sendSystemMessage(
            Component.literal(
                ALLOW_MSG[Random.nextInt(ALLOW_MSG.size - 1)]
            ).withStyle(ChatFormatting.GREEN)
        )
    }

    fun sendDenyMsg(player: ServerPlayer) {
        player.sendSystemMessage(
            Component.literal(
                ALLOW_MSG[Random.nextInt(DENY_MSG.size - 1)]
            ).withStyle(ChatFormatting.GREEN)
        )
    }
}