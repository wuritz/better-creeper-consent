package wuritz.bcc.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import wuritz.bcc.BetterCreeperConsent
import wuritz.bcc.client.screens.ConsentScreen
import wuritz.bcc.network.payloads.OpenConsentPayload
import wuritz.bcc.network.payloads.ResponsePayload

object BetterCreeperConsentClient : ClientModInitializer {
	override fun onInitializeClient() {
		BetterCreeperConsent.LOG.info("[Init] Initializing on client-side...")

		ClientPlayNetworking.registerGlobalReceiver(OpenConsentPayload.TYPE) { payload, context ->
            val creeperId = payload.creeperId

			context.client().execute {
				if (context.client().player == null) return@execute

				BetterCreeperConsent.LOG.info("Received open screen packet for creeper {}", creeperId)

				if (!context.client().canInterruptScreen()) ClientPlayNetworking.send(ResponsePayload(creeperId, false, false))
				else context.client().setScreen(ConsentScreen(creeperId))
			}
        }

		BetterCreeperConsent.LOG.info("[Init] Client-side initialized!")
    }
}