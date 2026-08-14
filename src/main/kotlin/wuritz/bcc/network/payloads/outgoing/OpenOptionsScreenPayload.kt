package wuritz.bcc.network.payloads

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import wuritz.bcc.BetterCreeperConsent

@JvmRecord
data class OpenOptionsScreenPayload(val id: Int) : CustomPacketPayload {

    companion object {
        val PAYLOAD_ID = Identifier.fromNamespaceAndPath(BetterCreeperConsent.MOD_ID, "open_options_screen")
        val TYPE: CustomPacketPayload.Type<OpenOptionsScreenPayload> = CustomPacketPayload.Type(PAYLOAD_ID)
        val CODEC: StreamCodec<RegistryFriendlyByteBuf, OpenOptionsScreenPayload> =
            StreamCodec.composite(ByteBufCodecs.INT, OpenOptionsScreenPayload::id, ::OpenOptionsScreenPayload)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        return TYPE
    }


}