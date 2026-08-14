package wuritz.bcc.client.screens.options

import wuritz.bcc.client.utils.CreeperPersonalities

data class OptionsRecord(
    val crp_animation: Boolean,
    val crp_response: Boolean,
    val crp_disableResponseOnDiscarded: Boolean,
    val dlg_disableGambling: Boolean,
    val dlg_chooseFixedCreeper: Boolean,
    val dlg_fixedCreeperType: CreeperPersonalities?,
    val g_logging: Boolean,
    val g_extendedLogging: Boolean?,
    val g_disableNewVersionAlert: Boolean
)