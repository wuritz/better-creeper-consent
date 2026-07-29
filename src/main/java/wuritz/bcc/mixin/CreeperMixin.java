package wuritz.bcc.mixin;

import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wuritz.bcc.network.CreeperQueue;
import wuritz.bcc.network.connection.OutgoingConnection;

@Mixin(Creeper.class)
public abstract class CreeperMixin {

    @Inject(method = "explodeCreeper", at = @At("HEAD"), cancellable = true)
    public void explodeCreeperInject(CallbackInfo ci) {
        Creeper creeper = (Creeper)(Object)this;

        if (CreeperQueue.INSTANCE.consumeApproved(creeper.getUUID())) return;

        ci.cancel();

        OutgoingConnection.INSTANCE.triggerConsent(creeper);
    }

}
