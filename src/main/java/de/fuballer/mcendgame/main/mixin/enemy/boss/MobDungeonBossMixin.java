package de.fuballer.mcendgame.main.mixin.enemy.boss;

import de.fuballer.mcendgame.main.accessor.MobEntityDungeonBossAccessor;
import de.fuballer.mcendgame.main.component.dungeon.enemy.boss.DungeonBossService;
import de.fuballer.mcendgame.main.component.dungeon.generation.data.SpawnPosition;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobDungeonBossMixin implements MobEntityDungeonBossAccessor {
    @Unique
    private static final String DUNGEON_BOSS_NBT = "isDungeonBoss";
    @Unique
    private static final String SPAWN_POSITION_NBT = "SpawnLocation";

    @Unique
    private boolean isDungeonBoss = false;
    @Nullable
    @Unique
    private SpawnPosition spawnPosition = null;

    @Override
    public boolean mcendgame$isDungeonBoss() {
        return isDungeonBoss;
    }

    @Override
    public void mcendgame$setDungeonBoss(boolean isBoss) {
        isDungeonBoss = isBoss;
    }

    @Nullable
    @Override
    public SpawnPosition mcendgame$getSpawnPosition() {
        return spawnPosition;
    }

    @Override
    public void mcendgame$setSpawnPosition(SpawnPosition location) {
        spawnPosition = location;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    void tick(CallbackInfo ci) {
        if (!isDungeonBoss) return;
        var entity = (Mob) (Object) this;
        if (!entity.isNoAi()) return;
        if (!(entity.level() instanceof ServerLevel serverWorld)) return;

        var players = serverWorld.getEntitiesOfClass(Player.class, entity.getBoundingBox().inflate(20.0, 5.0, 20.0));
        players = players.stream().filter(player -> !player.hasInfiniteMaterials()).toList();

        for (Player player : players) {
            if (!entity.hasLineOfSight(player)) continue;

            DungeonBossService.INSTANCE.activateBoss(entity, player);
            return;
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void writeCustomData(ValueOutput view, CallbackInfo ci) {
        if (isDungeonBoss) view.putBoolean(DUNGEON_BOSS_NBT, true);
        if (spawnPosition != null) {
            view.store(SPAWN_POSITION_NBT, SpawnPosition.Companion.getCODEC(), spawnPosition);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readNBT(ValueInput view, CallbackInfo ci) {
        isDungeonBoss = view.getBooleanOr(DUNGEON_BOSS_NBT, false);
        spawnPosition = view.read(SPAWN_POSITION_NBT, SpawnPosition.Companion.getCODEC()).orElse(null);
    }
}
