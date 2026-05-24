package de.fuballer.mcendgame.main.mixin.world;

import de.fuballer.mcendgame.main.accessor.DungeonWorldAccessor;
import de.fuballer.mcendgame.main.component.dungeon.type.DungeonType;
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(ServerWorld.class)
public class DungeonWorldMixin implements DungeonWorldAccessor {
    @Unique
    private boolean isTraining = false;
    @Unique
    private boolean isCompleted = false;
    @Unique
    private int level = 0;
    @Unique
    private int totalBossCount = 0;
    @Unique
    private int bossesKilled = 0;

    @Unique
    private LazyEntityReference<PlayerEntity> opener;

    @Unique
    private Map<AspectItem, Integer> aspects = new HashMap<>();
    @Unique
    private DungeonType dungeonType = DungeonType.STRONGHOLD;
    @Unique
    private GlobalPos dungeonExitPos = new GlobalPos(World.OVERWORLD, new BlockPos(0, 0, 0));

    @Override
    public boolean mcendgame$isTraining() {
        return isTraining;
    }

    @Override
    public void mcendgame$setTraining() {
        isTraining = true;
    }

    @Override
    public boolean mcendgame$isCompleted() {
        return isCompleted;
    }

    @Override
    public void mcendgame$setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    @Override
    public int mcendgame$getLevel() {
        return level;
    }

    @Override
    public void mcendgame$setLevel(int level) {
        this.level = level;
    }

    @Override
    public int mcendgame$getTotalBossCount() {
        return totalBossCount;
    }

    @Override
    public void mcendgame$setTotalBossCount(int count) {
        totalBossCount = count;
    }

    @Override
    public int mcendgame$getBossesKilled() {
        return bossesKilled;
    }

    @Override
    public void mcendgame$increaseBossesKilled() {
        bossesKilled++;
    }

    @Override
    public void mcendgame$setOpener(PlayerEntity opener) {
        this.opener = LazyEntityReference.of(opener);
    }

    @Override
    public PlayerEntity mcendgame$getOpener() {
        var world = (World) (Object) this;
        return LazyEntityReference.getPlayerEntity(opener, world);
    }

    @Override
    public void mcendgame$setAspects(Map<AspectItem, Integer> aspects) {
        this.aspects = aspects;
    }

    @Override
    public Map<AspectItem, Integer> mcendgame$getAspects() {
        return aspects;
    }

    @Override
    public void mcendgame$setDungeonType(DungeonType type) {
        this.dungeonType = type;
    }

    @Override
    public DungeonType mcendgame$getDungeonType() {
        return dungeonType;
    }

    @Override
    public void mcendgame$setDungeonExitPos(GlobalPos pos) {
        this.dungeonExitPos = pos;
    }

    @Override
    public GlobalPos mcendgame$getDungeonExitPos() {
        return dungeonExitPos;
    }
}