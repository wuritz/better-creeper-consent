package wuritz.bcc.network.connection

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.monster.Creeper
import net.minecraft.world.phys.AABB
import wuritz.bcc.BetterCreeperConsent
import wuritz.bcc.network.CreeperQueue
import wuritz.bcc.network.payloads.OpenConsentPayload
import wuritz.bcc.utils.Utils

object OutgoingConnection {

    fun triggerConsent(creeper: Creeper) {
        if (creeper.level() !is ServerLevel) return
        val level = creeper.level() as ServerLevel

        val explosion = AABB(creeper.blockPosition()).inflate(Utils.EXPLOSION_RADIUS.toDouble())

        val nearbyPlayers = level.getPlayers { player ->
            player.boundingBox.intersects(explosion)
        }
        if (nearbyPlayers.isEmpty()) return
        val nearestPlayer = nearbyPlayers[0]

        if (!CreeperQueue.markPending(creeper.uuid, nearestPlayer.uuid)) return

        creeper.swellDir = -1

        BetterCreeperConsent.LOG.info("Sending consent screen to {} for creeper {}", nearestPlayer.name, creeper.uuid)
        sendConsentScreen(nearbyPlayers[0], creeper)
    }

    private fun sendConsentScreen(player: ServerPlayer, creeper: Creeper) {
        creeper.swellDir = -1

        ServerPlayNetworking.send(player, OpenConsentPayload(creeper.id))
        BetterCreeperConsent.LOG.info("Sent consent screen to {} for creeper {} at {}", player.name, creeper.id, player.blockPosition())
    }

}