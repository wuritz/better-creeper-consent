package wuritz.bcc

import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import org.apache.logging.log4j.core.appender.ConsoleAppender
import wuritz.bcc.network.IncomingConnectionNF
import wuritz.bcc.utils.Constants

@Mod(Constants.MOD_ID)
class BCCNeoForge(eventBus: IEventBus) {

    init {
        BCCCommon.init()
        Constants.LOG.info("Initializing on server-side...")

        eventBus.register(IncomingConnectionNF)

        Constants.LOG.info("Server-side initialized!")
    }

}