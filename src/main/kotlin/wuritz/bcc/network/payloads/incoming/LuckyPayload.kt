package wuritz.bcc.network.payloads

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import wuritz.bcc.BetterCreeperConsent

@JvmRecord
data class LuckyPayload(val creeperId: Int) : CustomPacketPayload {

    companion object {
        val PAYLOAD_ID = Identifier.fromNamespaceAndPath(BetterCreeperConsent.MOD_ID, "lucky_payload")
        val TYPE: CustomPacketPayload.Type<LuckyPayload> = CustomPacketPayload.Type(PAYLOAD_ID)
        val CODEC: StreamCodec<RegistryFriendlyByteBuf, LuckyPayload> =
            StreamCodec.composite(
                ByteBufCodecs.INT, LuckyPayload::creeperId,
                ::LuckyPayload)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        return TYPE
    }
}