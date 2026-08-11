package wuritz.bcc.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wuritz.bcc.BetterCreeperConsent;
import wuritz.bcc.network.CreeperQueue;

import java.util.UUID;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Shadow
    public abstract ServerLevel level();

    @Inject(method = "die", at = @At("TAIL"))
    private void injectDie(DamageSource source, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer)(Object)this;
        UUID playerUUID = player.getUUID();
        UUID creeperUUID = CreeperQueue.INSTANCE.getPendingCreeperForPlayer(playerUUID);
        if (creeperUUID == null) return;

        Creeper creeper = (Creeper) this.level().getEntity(creeperUUID);
        if (creeper == null) return;
        if (CreeperQueue.INSTANCE.consumePending(creeperUUID)) {
            creeper.discard();
            BetterCreeperConsent.INSTANCE.getLOG().info("{} has died, and a pending creeper was discarded.", player.getDisplayName().getString());
        }
    }

}
