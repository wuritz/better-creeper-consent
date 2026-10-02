package wuritz.bcc

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import wuritz.bcc.screens.ConsentScreen
import wuritz.bcc.network.payloads.incoming.ResponsePayload
import wuritz.bcc.network.payloads.outgoing.OpenConsentPayload
import wuritz.bcc.network.payloads.outgoing.OpenOptionsScreenPayload
import wuritz.bcc.utils.Constants

object BCCFabricClient : ClientModInitializer {

	override fun onInitializeClient() {
		Constants.LOG.info("[Init] Initializing on client-side...")

		ClientPlayNetworking.registerGlobalReceiver(OpenConsentPayload.TYPE) { payload, context ->
            val creeperId = payload.creeperId

			context.client().execute {
				if (context.client().player == null) return@execute

				//Constants.LOG.info("Received open screen packet for creeper {}", creeperId)

				if (!context.client().gui.canInterruptScreen()) ClientPlayNetworking.send(
					ResponsePayload(
						creeperId,
						false,
						false
					)
				)
				else context.client().gui.setScreen(ConsentScreen(creeperId))
			}
        }

		ClientPlayNetworking.registerGlobalReceiver(OpenOptionsScreenPayload.TYPE) { payload, context ->
			context.client().execute {
				if (context.client().player == null) return@execute
			}
		}

		Constants.LOG.info("[Init] Client-side initialized!")
    }
}