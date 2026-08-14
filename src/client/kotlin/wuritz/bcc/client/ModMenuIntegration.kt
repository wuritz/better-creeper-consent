package wuritz.bcc.client

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import dev.isxander.yacl3.api.*
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import net.minecraft.network.chat.Component

class ModMenuIntegration : ModMenuApi {

    var asdbool = false

    override fun getModConfigScreenFactory(): ConfigScreenFactory<*>? {
        return ConfigScreenFactory { parentScreen ->
            YetAnotherConfigLib.createBuilder()
                .title(text("Better Creeper Consent Settings"))
                .category(
                    ConfigCategory.createBuilder()
                        .name(text("Creeper Behaviour"))
                        .group(
                            OptionGroup.createBuilder()
                                .name(text("ASD"))
                                .description(OptionDescription.of(text("asd description")))
                                .option(
                                    Option.createBuilder<Boolean>()
                                        .name(text("Bool option yay"))
                                        .description(OptionDescription.of(text("desc ewfewfewf")))
                                        .binding(true, { asdbool }, { new -> asdbool = new })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .build()
                .generateScreen(parentScreen)
        }
    }

    fun text(str: String) : Component {
        return Component.literal(str)
    }
}