package wuritz.bcc.network

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.monster.Creeper
import wuritz.bcc.BetterCreeperConsent
import wuritz.bcc.network.payloads.OpenConsentPayload
import wuritz.bcc.network.payloads.ResponsePayload
import wuritz.bcc.utils.Utils

object PayloadNetwork {

    fun init() {
        PayloadTypeRegistry.clientboundPlay().register(OpenConsentPayload.TYPE, OpenConsentPayload.CODEC)
        PayloadTypeRegistry.serverboundPlay().register(ResponsePayload.TYPE, ResponsePayload.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(
            ResponsePayload.TYPE
        ) { payload, context ->
            val player = context.player()

            context.server().execute { handleResponse(player, payload.creeperId, payload.allowed) }
        }
    }

    private fun handleResponse(player: ServerPlayer, creeperId: Int, allowed: Boolean) {
        val world = player.level()
        val creeper = world.getEntity(creeperId)

        if (creeper !is Creeper) return BetterCreeperConsent.LOG.error("{} sent a consent to a non-creeper entity: id {}", player.name, creeperId)
        val creeperUuid = creeper.uuid

        //TODO: test this
        val distance = player.distanceTo(creeper)
        if (distance > Utils.EXPLOSION_RADIUS) {
            BetterCreeperConsent.LOG.error("{} sent a response, but is now out of the creeper's (id {}) radius.", player.name, creeperId)
            CreeperQueue.clearEntry(creeperUuid)
            return
        }

        if (allowed) {
            BetterCreeperConsent.LOG.info("{} allowed creeper id {} to explode", player.name, creeperId)

            CreeperQueue.approve(creeperUuid)
            creeper.swellDir = 1 // normal behaviour
            creeper.ignite()

            player.sendSystemMessage(Component.literal("Explosion allowed."))
        } else {
            BetterCreeperConsent.LOG.info("{} denied creeper id {}", player.name, creeperId)

            CreeperQueue.clearEntry(creeperUuid)
            creeper.discard()

            player.sendSystemMessage(Component.literal("Explosion discarded."))
        }
    }

}