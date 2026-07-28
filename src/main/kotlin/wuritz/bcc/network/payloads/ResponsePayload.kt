package wuritz.bcc.network.payloads

import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import wuritz.bcc.BetterCreeperConsent

@JvmRecord
data class ResponsePayload(val creeperId: Int, val allowed: Boolean) : CustomPacketPayload {

    companion object {
        val PAYLOAD_ID = Identifier.fromNamespaceAndPath(BetterCreeperConsent.MOD_ID, "consent_response")
        val TYPE = CustomPacketPayload.Type<ResponsePayload>(PAYLOAD_ID)
        val
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        TODO("Not yet implemented")
    }


}