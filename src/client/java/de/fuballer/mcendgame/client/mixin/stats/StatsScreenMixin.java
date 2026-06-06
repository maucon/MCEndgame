package de.fuballer.mcendgame.client.mixin.stats;

import de.fuballer.mcendgame.client.component.screen.CustomStatsListWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(StatsScreen.class)
public class StatsScreenMixin {
    @Shadow
    @Final
    HeaderAndFooterLayout layout;

    @ModifyArg(
            method = "onStatsUpdated",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/tabs/TabNavigationBar$Builder;addTabs([Lnet/minecraft/client/gui/components/tabs/Tab;)Lnet/minecraft/client/gui/components/tabs/TabNavigationBar$Builder;"
            )
    )
    private Tab[] addModTab(Tab[] originalTabs) {
        StatsScreen self = (StatsScreen) (Object) this;

        Tab[] newTabs = new Tab[originalTabs.length + 1];
        System.arraycopy(originalTabs, 0, newTabs, 0, originalTabs.length);
        newTabs[originalTabs.length] = self.new StatisticsTab(
                Component.translatable("container.mcendgame.stats_screen.tab.title"),
                new CustomStatsListWidget(Minecraft.getInstance(), self.width, layout.getContentHeight())
        );
        return newTabs;
    }
}
