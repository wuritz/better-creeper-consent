package wuritz.bcc.neoforge;

import net.neoforged.fml.common.Mod;

import wuritz.bcc.ExampleMod;

@Mod(ExampleMod.MOD_ID)
public final class ExampleModNeoForge {
    public ExampleModNeoForge() {
        // Run our common setup.
        ExampleMod.init();
    }
}
