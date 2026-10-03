package wuritz.bcc.network.services

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.monster.Creeper
import wuritz.bcc.network.payloads.outgoing.OpenConsentPayload
import wuritz.bcc.platform.interfaces.SendConsentScreen

class SendConsentScreenF : SendConsentScreen {

    override fun sendConsentScreen(
        player: ServerPlayer,
        creeper: Creeper
    ) {
        ServerPlayNetworking.send(player, OpenConsentPayload(creeper.id))
    }

}