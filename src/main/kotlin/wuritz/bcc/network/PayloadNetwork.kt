package wuritz.bcc.network

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import wuritz.bcc.network.payloads.OpenConsentPayload

class PayloadNetwork {

    fun init() {
        PayloadTypeRegistry.clientboundPlay().register(OpenConsentPayload.TYPE, OpenConsentPayload.CODEC)
        PayloadTypeRegistry.serverboundPlay().register()
    }

}