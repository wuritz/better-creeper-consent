package wuritz.bcc.network

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object CreeperQueue {
    private val pending: MutableSet<UUID> = ConcurrentHashMap.newKeySet()
    private val approved: MutableSet<UUID> = ConcurrentHashMap.newKeySet()
    private val successful: MutableSet<UUID> = ConcurrentHashMap.newKeySet()

    fun markPending(uuid: UUID) : Boolean {
        return pending.add(uuid) && !successful.contains(uuid)
    }

    fun approve(uuid: UUID) : Boolean {
        approved.add(uuid)
        successful.add(uuid)
        return pending.remove(uuid)
    }

    fun consumeApproved(uuid: UUID) : Boolean {
        return approved.remove(uuid)
    }

    fun clearEntry(uuid: UUID) {
        pending.remove(uuid)
        approved.remove(uuid)
    }
}