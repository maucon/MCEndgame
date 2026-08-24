package de.fuballer.mcendgame.main.mixin.world;

import de.fuballer.mcendgame.main.accessor.DungeonWorldAccessor;
import de.fuballer.mcendgame.main.component.dungeon.type.DungeonType;
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(ServerLevel.class)
public class DungeonWorldMixin implements DungeonWorldAccessor {
    @Unique
    private boolean isTraining = false;
    @Unique
    private boolean isCompleted = false;
    @Unique
    private long dungeonSeed = 0L;
    @Unique
    private int level = 0;
    @Unique
    private long creationTime = 0L;
    @Unique
    private int totalBossCount = 0;
    @Unique
    private int bossesKilled = 0;

    @Unique
    private EntityReference<Player> opener;

    @Unique
    private Map<AspectItem, Integer> aspects = new HashMap<>();
    @Unique
    private DungeonType dungeonType = DungeonType.STRONGHOLD;
    @Unique
    private GlobalPos dungeonExitPos = new GlobalPos(Level.OVERWORLD, new BlockPos(0, 0, 0));

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
    public long mcendgame$getDungeonSeed() {
        return dungeonSeed;
    }

    @Override
    public void mcendgame$setDungeonSeed(long dungeonSeed) {
        this.dungeonSeed = dungeonSeed;
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
    public long mcendgame$getCreationTime() {
        return creationTime;
    }

    @Override
    public void mcendgame$setCreationTime(long creationTime) {
        this.creationTime = creationTime;
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
    public void mcendgame$setOpener(Player opener) {
        this.opener = EntityReference.of(opener);
    }

    @Override
    public Player mcendgame$getOpener() {
        var world = (Level) (Object) this;
        return EntityReference.getPlayer(opener, world);
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