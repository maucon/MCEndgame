package de.fuballer.mcendgame.main.accessor;

import de.fuballer.mcendgame.main.component.dungeon.type.DungeonType;
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.GlobalPos;

import java.util.Map;

public interface DungeonWorldAccessor {
    boolean mcendgame$isTraining();

    void mcendgame$setTraining();

    boolean mcendgame$isCompleted();

    void mcendgame$setCompleted(boolean completed);

    int mcendgame$getLevel();

    void mcendgame$setLevel(int level);

    int mcendgame$getTotalBossCount();

    void mcendgame$setTotalBossCount(int count);

    int mcendgame$getBossesKilled();

    void mcendgame$increaseBossesKilled();

    void mcendgame$setOpener(PlayerEntity opener);

    PlayerEntity mcendgame$getOpener();

    void mcendgame$setAspects(Map<AspectItem, Integer> aspects);

    Map<AspectItem, Integer> mcendgame$getAspects();

    void mcendgame$setDungeonType(DungeonType type);

    DungeonType mcendgame$getDungeonType();

    void mcendgame$setDungeonExitPos(GlobalPos pos);

    GlobalPos mcendgame$getDungeonExitPos();
}
