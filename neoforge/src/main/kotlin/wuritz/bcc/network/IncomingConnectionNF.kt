package wuritz.bcc.network

import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.network.ClientPacketDistributor
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import wuritz.bcc.network.handling.ClientPayloadHandler
import wuritz.bcc.network.payloads.incoming.LuckyPayload
import wuritz.bcc.network.payloads.incoming.ResponsePayload
import wuritz.bcc.network.payloads.outgoing.OpenConsentPayload
import wuritz.bcc.screens.ConsentScreen
import wuritz.bcc.utils.Constants

object IncomingConnectionNF {

    @SubscribeEvent
    fun init(event: RegisterPayloadHandlersEvent) {
        registerClient(event.registrar("1"))
        registerServer(event.registrar("1"))
    }

    fun registerServer(registrar: PayloadRegistrar) {
        registrar.playToServer(
            ResponsePayload.TYPE,
            ResponsePayload.CODEC
        ) { payload, context ->
            if (context.player() !is ServerPlayer) return@playToServer
            ClientPayloadHandler.handleResponse(context.player() as ServerPlayer, payload.creeperId, payload.allowed, payload.playerInitialized)
        }

        registrar.playToServer(
            LuckyPayload.TYPE,
            LuckyPayload.CODEC
        ) { payload, context ->
            if (context.player() !is ServerPlayer) return@playToServer
            ClientPayloadHandler.handleLucky(context.player() as ServerPlayer, payload.creeperId)
        }
    }

    fun registerClient(registrar: PayloadRegistrar) {
        Constants.LOG.info("Registering client...")
        registrar.playToClient(
            OpenConsentPayload.TYPE,
            OpenConsentPayload.CODEC
        ) { payload, context ->
            if (Minecraft.getInstance().player == null) return@playToClient

            val creeperId = payload.creeperId

            if (!Minecraft.getInstance().gui.canInterruptScreen()) {
                val response = ResponsePayload(creeperId,
                    false, playerInitialized = false)

                ClientPacketDistributor.sendToServer(response)
            } else {
                Minecraft.getInstance().gui.setScreen(ConsentScreen(creeperId))
            }
        }
        Constants.LOG.info("Client registered!")
    }

}