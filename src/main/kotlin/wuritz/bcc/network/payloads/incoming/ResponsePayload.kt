package wuritz.bcc.network.payloads

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import wuritz.bcc.BetterCreeperConsent

@JvmRecord
data class ResponsePayload(val creeperId: Int, val allowed: Boolean, val playerInitialized: Boolean) : CustomPacketPayload {

    companion object {
        val PAYLOAD_ID = Identifier.fromNamespaceAndPath(BetterCreeperConsent.MOD_ID, "consent_response")
        val TYPE = CustomPacketPayload.Type<ResponsePayload>(PAYLOAD_ID)
        val CODEC: StreamCodec<RegistryFriendlyByteBuf, ResponsePayload> = StreamCodec.composite(ByteBufCodecs.VAR_INT, ResponsePayload::creeperId, ByteBufCodecs.BOOL,
            ResponsePayload::allowed,
            ByteBufCodecs.BOOL, ResponsePayload::playerInitialized, ::ResponsePayload)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        return TYPE
    }


}