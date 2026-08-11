package wuritz.bcc.network

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object CreeperQueue {
    private val pending: HashMap<UUID, UUID> = HashMap()
    private val approved: MutableSet<UUID> = ConcurrentHashMap.newKeySet()
    private val successful: MutableSet<UUID> = ConcurrentHashMap.newKeySet()

    fun markPending(creeper: UUID, player: UUID) : Boolean {
        return pending.put(creeper, player) == null && !successful.contains(creeper)
    }

    fun approve(uuid: UUID) : Boolean {
        approved.add(uuid)
        successful.add(uuid)
        return pending.remove(uuid) != null
    }

    fun consumeApproved(uuid: UUID) : Boolean {
        return approved.remove(uuid)
    }

    fun clearEntry(uuid: UUID) {
        pending.remove(uuid)
        approved.remove(uuid)
        successful.remove(uuid)
    }

    fun wasAlreadyApproved(uuid: UUID) : Boolean = successful.contains(uuid)

    fun getPendingCreeperForPlayer(player: UUID) : UUID? {
        for (creeper in pending.keys) {
            if (pending[creeper] == player) return creeper
        }

        return null
    }

    fun consumePending(uuid: UUID) : Boolean {
        return pending.remove(uuid) != null
    }
}