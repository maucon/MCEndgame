package de.fuballer.mcendgame.main.mixin.item.bow;

import de.fuballer.mcendgame.main.component.custom_attribute.effects.BowPullUtil;
import de.fuballer.mcendgame.main.util.extension.EntityExtension;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BowItem.class)
public class BowItemDrawSpeedMixin {
    @Redirect(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BowItem;getPowerForTime(I)F"))
    float getPullProgress(int useTicks, ItemStack stack, Level world, LivingEntity user) {
        var fullPullTicks = EntityExtension.INSTANCE.getBowFullPullTicks(user);
        return BowPullUtil.INSTANCE.getPullProgress(useTicks, fullPullTicks);
    }
}
