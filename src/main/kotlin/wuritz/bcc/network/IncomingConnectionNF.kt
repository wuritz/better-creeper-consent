/*
 * Copyright (c) 2026. wuritz
 * SPDX-License-Identifier: Apache-2.0
 */

package wuritz.bcc.network

import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
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
        val registrar = event.registrar("1")
        registerServer(registrar)
        registerClient(registrar)
    }

    fun registerServer(registrar: PayloadRegistrar) {
        registrar.playToServer(
            ResponsePayload.TYPE,
            ResponsePayload.CODEC
        ) { payload, context ->
            context.enqueueWork {
                val player = context.player()
                if (player is ServerPlayer) {
                    ClientPayloadHandler.handleResponse(
                        player,
                        payload.creeperId,
                        payload.allowed,
                        payload.playerInitialized
                    )
                }
            }
        }

        registrar.playToServer(
            LuckyPayload.TYPE,
            LuckyPayload.CODEC
        ) { payload, context ->
            context.enqueueWork {
                val player = context.player()
                if (player is ServerPlayer) {
                    ClientPayloadHandler.handleLucky(player, payload.creeperId)
                }
            }
        }
    }

    fun registerClient(registrar: PayloadRegistrar) {
        Constants.LOG.info("Registering client...")
        registrar.playToClient(
            OpenConsentPayload.TYPE,
            OpenConsentPayload.CODEC
        ) { payload, context ->
            context.enqueueWork {
                val minecraft = Minecraft.getInstance()
                if (minecraft.player == null) return@enqueueWork

                val creeperId = payload.creeperId

                if (minecraft.screen != null) {
                    val response = ResponsePayload(creeperId, false, playerInitialized = false)
                    minecraft.connection?.send(ServerboundCustomPayloadPacket(response))
                } else {
                    minecraft.setScreen(ConsentScreen(creeperId))
                }
            }
        }
        Constants.LOG.info("Client registered!")
    }

}
