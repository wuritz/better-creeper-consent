package wuritz.bcc.network.connection

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.monster.Creeper
import wuritz.bcc.BetterCreeperConsent
import wuritz.bcc.LuckyAction
import wuritz.bcc.network.CreeperQueue
import wuritz.bcc.network.payloads.LuckyPayload
import wuritz.bcc.network.payloads.OpenConsentPayload
import wuritz.bcc.network.payloads.ResponsePayload
import wuritz.bcc.utils.MessageSender
import wuritz.bcc.utils.Utils

object IncomingConnection {

    fun init() {
        PayloadTypeRegistry.clientboundPlay().register(OpenConsentPayload.TYPE, OpenConsentPayload.CODEC)
        PayloadTypeRegistry.serverboundPlay().register(ResponsePayload.TYPE, ResponsePayload.CODEC)
        PayloadTypeRegistry.serverboundPlay().register(LuckyPayload.TYPE, LuckyPayload.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(
            ResponsePayload.TYPE
        ) { payload, context ->
            val player = context.player()

            context.server().execute { handleResponse(player, payload.creeperId, payload.allowed, payload.playerInitialized) }
        }

        ServerPlayNetworking.registerGlobalReceiver(
            LuckyPayload.TYPE
        ) { payload, context ->
            val player = context.player()

            context.server().execute { handleLucky(player, payload.creeperId) }
        }

        ServerLivingEntityEvents.AFTER_DEATH.register { entity, _ ->
            if (entity is Creeper) {
                CreeperQueue.clearEntry(entity.uuid)
            }
        }
    }

    private fun handleLucky(player: ServerPlayer, creeperId: Int) {
        val world = player.level()

        val creeper = world.getEntity(creeperId)
        if (creeper !is Creeper) return
        val pos = creeper.blockPosition()

        val lucky = LuckyAction(pos, world, player)
        lucky.run()
    }

    private fun handleResponse(player: ServerPlayer, creeperId: Int, allowed: Boolean, playerInitialized: Boolean) {
        val world = player.level()
        val creeper = world.getEntity(creeperId)

        if (creeper !is Creeper) return BetterCreeperConsent.LOG.error("{} sent a consent to a non-creeper entity: id {}", player.name.string, creeperId)
        val creeperUuid = creeper.uuid

        val distance = player.distanceTo(creeper)
        if (distance > Utils.EXPLOSION_RADIUS) {
            BetterCreeperConsent.LOG.error("{} sent a response, but is now out of the creeper's (id {}) radius.", player.name.string, creeperId)
            creeper.discard()
            CreeperQueue.clearEntry(creeperUuid)
            return
        }

        if (allowed) {
            BetterCreeperConsent.LOG.info("{} allowed creeper id {} to explode", player.name.string, creeperId)

            CreeperQueue.approve(creeperUuid)
            creeper.swellDir = 1 // normal behaviour
            creeper.ignite()

            MessageSender.sendAllowMsg(player)
        } else {
            BetterCreeperConsent.LOG.info("{} denied creeper id {}", player.name.string, creeperId)

            creeper.discard()
            CreeperQueue.clearEntry(creeperUuid)

            //if (playerInitialized) MessageSender.sendDenyMsg(player)
            MessageSender.sendDenyMsg(player)
        }
    }

}