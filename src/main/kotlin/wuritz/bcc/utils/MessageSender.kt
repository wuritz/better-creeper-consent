package wuritz.bcc.utils

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import kotlin.random.Random

object MessageSender {

    val ALLOW_MSG = listOf(
        "Yay!", "The best choice!", "Sorry for your buildings :(", "I hope you don't have any pets around >:("
    )

    val DENY_MSG = listOf(
        "Aw :(", "Maybe next time :(", "Your buildings are saved for now..", "My relatives will have a talk with you..",
        "I'm gonna go elsewhere then..", "Bye! :D", "Fair enough. :(", "Next time reconsider it please :("
    )

    val prefix = Component.literal("<Creeper> ")
        .withStyle(ChatFormatting.WHITE)

    fun sendAllowMsg(player: ServerPlayer) {
        val output = Component.literal("")
        output.append(prefix)

        player.sendSystemMessage(
            output.append(
                Component.literal(
                    ALLOW_MSG[Random.nextInt(0, ALLOW_MSG.size - 1)])
                    .withStyle(ChatFormatting.GREEN)
            )
        )
    }

    fun sendDenyMsg(player: ServerPlayer) {
        val output = Component.literal("")
        output.append(prefix)

        player.sendSystemMessage(
            output.append(
                Component.literal(
                    DENY_MSG[Random.nextInt(0, DENY_MSG.size - 1)])
                    .withStyle(ChatFormatting.GREEN)
            )
        )
    }

    fun sendTntMsg(player: ServerPlayer) {
        val output = Component.literal("")
        output.append(prefix)

        player.sendSystemMessage(
            output.append(
                Component.literal(
                    "Here's a gift for you :3")
                    .withStyle(ChatFormatting.GREEN)
            )
        )
    }
}