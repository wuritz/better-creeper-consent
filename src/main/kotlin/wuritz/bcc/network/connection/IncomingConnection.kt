package wuritz.bcc.network.connection

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.monster.Creeper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import wuritz.bcc.BetterCreeperConsent
import wuritz.bcc.network.CreeperQueue
import wuritz.bcc.network.payloads.LuckyPayload
import wuritz.bcc.network.payloads.OpenConsentPayload
import wuritz.bcc.network.payloads.ResponsePayload
import wuritz.bcc.utils.MessageSender
import wuritz.bcc.utils.Utils

object IncomingConnection {

    fun init() {
        PayloadTypeRegistry.clientboundPlay().register(OpenConsentPayload.TYPE, OpenConsentPayload.CODEC)
        PayloadTypeRegistry.serverboundPlay().register(ResponsePayload.TYPE, ResponsePayload.CODEC)
        PayloadTypeRegistry.serverboundPlay().register(LuckyPayload.TYPE, LuckyPayload.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(
            ResponsePayload.TYPE
        ) { payload, context ->
            val player = context.player()

            context.server().execute { handleResponse(player, payload.creeperId, payload.allowed, payload.playerInitialized) }
        }

        ServerPlayNetworking.registerGlobalReceiver(
            LuckyPayload.TYPE
        ) { payload, context ->
            val player = context.player()

            context.server().execute { handleLucky(player, payload.creeperId) }
        }
    }

    private fun handleLucky(player: ServerPlayer, creeperId: Int) {
        BetterCreeperConsent.LOG.info("Halo")
        val world = player.level()

        val creeper = world.getEntity(creeperId)
        if (creeper !is Creeper) return
        val pos = creeper.blockPosition()

        // Spawn
        val stack = ItemStack(Items.GUNPOWDER)
        val entity = ItemEntity(world, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, stack)
        world.addFreshEntity(entity)
    }

    private fun handleResponse(player: ServerPlayer, creeperId: Int, allowed: Boolean, playerInitialized: Boolean) {
        val world = player.level()
        val creeper = world.getEntity(creeperId)

        if (creeper !is Creeper) return BetterCreeperConsent.LOG.error("{} sent a consent to a non-creeper entity: id {}", player.name, creeperId)
        val creeperUuid = creeper.uuid

        //TODO: test this
        val distance = player.distanceTo(creeper)
        if (distance > Utils.EXPLOSION_RADIUS) {
            BetterCreeperConsent.LOG.error("{} sent a response, but is now out of the creeper's (id {}) radius.", player.name, creeperId)
            CreeperQueue.clearEntry(creeperUuid)
            return
        }

        if (allowed) {
            BetterCreeperConsent.LOG.info("{} allowed creeper id {} to explode", player.name, creeperId)

            creeper.swellDir = 1 // normal behaviour
            creeper.ignite()
            CreeperQueue.approve(creeperUuid)

            MessageSender.sendAllowMsg(player)
        } else {
            BetterCreeperConsent.LOG.info("{} denied creeper id {}", player.name, creeperId)

            CreeperQueue.clearEntry(creeperUuid)
            creeper.discard()

            if (playerInitialized) MessageSender.sendDenyMsg(player)
        }
    }

}