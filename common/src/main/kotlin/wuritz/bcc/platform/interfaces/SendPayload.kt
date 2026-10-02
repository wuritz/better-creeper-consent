package wuritz.bcc.platform.interfaces

import net.minecraft.network.protocol.common.custom.CustomPacketPayload

interface SendPayload {

    fun sendPayload(payload: CustomPacketPayload)

}