package wuritz.bcc.network

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.monster.Creeper
import wuritz.bcc.network.handling.ClientPayloadHandler
import wuritz.bcc.utils.LuckyAction
import wuritz.bcc.network.payloads.incoming.LuckyPayload
import wuritz.bcc.network.payloads.outgoing.OpenConsentPayload
import wuritz.bcc.network.payloads.outgoing.OpenOptionsScreenPayload
import wuritz.bcc.network.payloads.incoming.ResponsePayload
import wuritz.bcc.utils.Constants
import wuritz.bcc.utils.MessageSender

object IncomingConnectionF {

    fun init() {
        PayloadTypeRegistry.clientboundPlay().register(OpenConsentPayload.TYPE, OpenConsentPayload.CODEC)
        PayloadTypeRegistry.clientboundPlay().register(OpenOptionsScreenPayload.TYPE, OpenOptionsScreenPayload.CODEC)
        PayloadTypeRegistry.serverboundPlay().register(ResponsePayload.TYPE, ResponsePayload.CODEC)
        PayloadTypeRegistry.serverboundPlay().register(LuckyPayload.TYPE, LuckyPayload.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(
            ResponsePayload.TYPE
        ) { payload, context ->
            context.server().execute { ClientPayloadHandler.handleResponse(context.player(), payload.creeperId, payload.allowed, payload.playerInitialized) }
        }

        ServerPlayNetworking.registerGlobalReceiver(
            LuckyPayload.TYPE
        ) { payload, context ->
            context.server().execute { ClientPayloadHandler.handleLucky(context.player(), payload.creeperId) }
        }

        ServerLivingEntityEvents.AFTER_DEATH.register { entity, _ ->
            if (entity is Creeper) CreeperQueue.clearEntry(entity.uuid)
        }
    }
}