package de.fuballer.mcendgame.main.mixin.runtime_worlds;

import net.minecraft.world.level.gamerules.GameRuleMap;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameRules.class)
public interface GameRulesAccessor {
    @Accessor
    GameRuleMap getRules();
}
