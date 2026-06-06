package de.fuballer.mcendgame.main.mixin.wolf_entity;

import de.fuballer.mcendgame.main.accessor.WolfEntityColorAndVariantAccessor;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Wolf.class)
public abstract class WolfColorAndVariantMixin implements WolfEntityColorAndVariantAccessor {
    @Invoker("setVariant")
    public abstract void mcendgame$callSetVariant(Holder<WolfVariant> variant);

    @Invoker("setCollarColor")
    public abstract void mcendgame$callSetCollarColor(DyeColor color);
}