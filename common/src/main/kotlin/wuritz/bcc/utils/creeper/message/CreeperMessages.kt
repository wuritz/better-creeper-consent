/*
 * Copyright (c) 2026. wuritz
 * SPDX-License-Identifier: Apache-2.0
 */

package wuritz.bcc.utils.creeper.message

object CreeperMessages {

    val ALLOW_MSG = listOf(
        "Yay!", "The best choice!", "Sorry for your buildings :(", "I hope you don't have any pets around >:("
    )

    val DENY_MSG = listOf(
        "Aw :(", "Maybe next time :(", "Your buildings are saved for now..", "My relatives will have a talk with you..",
        "I'm gonna go elsewhere then..", "Bye! :D", "Fair enough. :(", "Next time reconsider it please! :("
    )

    val ALLOW_CAVE_MSG = listOf(
        "Yay!", "The best choice!", "At least we're underground!", "I hope you don't have any pets around >:(",
        "Sorry to my mob friends around here!"
    )

    val DENY_CAVE_MSG = listOf(
        "Aw :(", "Maybe next time :(", "My relatives will have a talk with you..",
        "I'm gonna go elsewhere then..", "Bye! :D", "Fair enough. :(", "Next time reconsider it please! :("
    )

    fun randomAllowMessage() : String {
        return ALLOW_MSG.random()
    }

    fun randomDenyMessage() : String {
        return DENY_MSG.random()
    }

    fun randomCaveAllowMessage() : String {
        return ALLOW_CAVE_MSG.random()
    }

    fun randomCaveDenyMessage() : String {
        return DENY_CAVE_MSG.random()
    }

}