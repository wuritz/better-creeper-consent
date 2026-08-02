package wuritz.bcc

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.item.PrimedTnt
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import wuritz.bcc.utils.MessageSender
import kotlin.random.Random

class LuckyAction(val pos: BlockPos, val level: ServerLevel, val player: ServerPlayer) {

    val COMMON_ITEMS = listOf(
        Items.GUNPOWDER, Items.STRING, Items.DIRT, Items.COBBLESTONE, Items.OAK_LOG
    )

    val RARE_ITEMS = listOf(
        Items.COD, Items.TNT
    )

    val LEGENDARY_ITEMS = listOf(
        Items.CREEPER_HEAD, Items.DIAMOND, Items.GOLDEN_APPLE
    )

    /**
     * Common: 70%
     * Rare: 20%
     * Legendary: 10%
     */

    /**
     * Tnt: 5%
     */
    fun run() {
        // decide whether to drop item or ignite tnt
        val rand = Random.nextInt(100)
        if (rand <= 5) tnt()
        else drop()
    }

    fun drop() {
        val rand = Random.nextInt(100)

        val stack: ItemStack = if (rand <= 10) ItemStack(LEGENDARY_ITEMS.random())
        else if (rand <= 20) ItemStack(RARE_ITEMS.random())
        else ItemStack(COMMON_ITEMS.random())

        val entity = ItemEntity(level, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, stack)
        level.addFreshEntity(entity)
    }

    fun tnt() {
        val tnt = PrimedTnt(level, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, null)
        tnt.fuse = 80
        level.addFreshEntity(tnt)
        MessageSender.sendTntMsg(player)
    }

}