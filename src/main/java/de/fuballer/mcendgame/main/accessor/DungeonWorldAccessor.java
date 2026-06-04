package de.fuballer.mcendgame.main.accessor;

import de.fuballer.mcendgame.main.component.dungeon.type.DungeonType;
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.player.Player;

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

    void mcendgame$setOpener(Player opener);

    Player mcendgame$getOpener();

    void mcendgame$setAspects(Map<AspectItem, Integer> aspects);

    Map<AspectItem, Integer> mcendgame$getAspects();

    void mcendgame$setDungeonType(DungeonType type);

    DungeonType mcendgame$getDungeonType();

    void mcendgame$setDungeonExitPos(GlobalPos pos);

    GlobalPos mcendgame$getDungeonExitPos();
}
