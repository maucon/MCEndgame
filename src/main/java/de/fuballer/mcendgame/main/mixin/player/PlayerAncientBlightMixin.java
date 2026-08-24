package de.fuballer.mcendgame.main.mixin.player;

import de.fuballer.mcendgame.main.component.biome.CustomBiomes;
import de.fuballer.mcendgame.main.component.status_effect.CustomStatusEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerAncientBlightMixin {
    @Unique
    private int ticksInAncientBlightWater = 0;

    @Inject(method = "tick", at = @At("TAIL"))
    private void mcendgame$applyAncientBlight(CallbackInfo ci) {
        var player = (Player) (Object) this;
        if (!(player.level() instanceof ServerLevel level)) return;

        var effectHolder = CustomStatusEffects.INSTANCE.getANCIENT_BLIGHT();
        ticksInAncientBlightWater = player.hasEffect(effectHolder) ?
                Math.min(ticksInAncientBlightWater + 1, 180) : Math.max(ticksInAncientBlightWater - 1, 0);

        if (player.isSpectator()) return;
        if (!player.isInWater()) return;
        var pos = player.blockPosition();
        if (!level.getBiome(pos).is(CustomBiomes.INSTANCE.getBEASTWEAVER_GROVE_DUNGEON())) return;

        var amplifier = ticksInAncientBlightWater / 20;
        var effectInstance = new MobEffectInstance(effectHolder, 19, amplifier, true, true);
        player.addEffect(effectInstance);
    }
}