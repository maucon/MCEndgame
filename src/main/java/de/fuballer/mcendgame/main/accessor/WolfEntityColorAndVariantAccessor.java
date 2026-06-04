package de.fuballer.mcendgame.main.accessor;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.item.DyeColor;

public interface WolfEntityColorAndVariantAccessor {
    void mcendgame$callSetVariant(Holder<WolfVariant> variant);

    void mcendgame$callSetCollarColor(DyeColor color);
}
