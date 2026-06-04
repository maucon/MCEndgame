package de.fuballer.mcendgame.client.mixin.stats;

import de.fuballer.mcendgame.main.MCEndgame;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(StatsScreen.GeneralStatisticsList.class)
public class GeneralStatsListWidgetMixin {
    @ModifyVariable(
            method = "<init>",
            at = @At("STORE"),
            ordinal = 0
    )
    private ObjectArrayList<Stat<Identifier>> filterModStats(ObjectArrayList<Stat<Identifier>> list) {
        list.removeIf(stat -> stat.getValue().getNamespace().equals(MCEndgame.MOD_ID));
        return list;
    }
}
