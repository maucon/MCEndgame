package de.fuballer.mcendgame.main.mixin.corruption;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ResultContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemCombinerMenu.class)
public interface ItemCombinerMenuOutputAccessorMixin {
    @Accessor("resultSlots")
    ResultContainer getResultSlots();

    @Accessor("inputSlots")
    Container getInputSlots();
}
