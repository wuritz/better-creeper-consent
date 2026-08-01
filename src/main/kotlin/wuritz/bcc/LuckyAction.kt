package wuritz.bcc

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.item.PrimedTnt
import net.minecraft.world.item.Items
import kotlin.random.Random

class LuckyAction(val pos: BlockPos, val level: ServerLevel) {

    val COMMON_ITEMS = listOf(
        Items.GUNPOWDER, Items.STRING, Items.DIRT, Items.COBBLESTONE
    )

    val RARE_ITEMS = listOf(
        Items.COOKED_COD, Items.TNT
    )

    val LEGENDARY_ITEMS = listOf(
        Items.CREEPER_HEAD, Items.DIAMOND, Items.GOLDEN_APPLE
    )

    /**
     * Common: 90%
     * Rare: 9%
     * Legendary: 1%
     */

    /**
     * Tnt: 5%
     */
    fun run() {
        // decide whether to drop item or ignite tnt
        val rand = Random.nextInt(100)
        if (rand <= 5) tnt()
        else tnt()
    }

    fun drop() {

    }

    fun tnt() {
        val tnt = PrimedTnt(level, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, null)
        tnt.fuse = 80
        level.addFreshEntity(tnt)
    }

}