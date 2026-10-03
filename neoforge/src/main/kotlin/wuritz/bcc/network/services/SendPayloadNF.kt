/*
 * Copyright (c) 2026. wuritz
 * SPDX-License-Identifier: Apache-2.0
 */

package wuritz.bcc.network.services

import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import wuritz.bcc.platform.interfaces.SendPayload

class SendPayloadNF : SendPayload {

    override fun sendPayload(payload: CustomPacketPayload) {
        Minecraft.getInstance().connection?.send(ServerboundCustomPayloadPacket(payload))
    }

}