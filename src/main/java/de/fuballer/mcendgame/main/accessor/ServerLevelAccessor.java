package de.fuballer.mcendgame.main.accessor;

public interface ServerLevelAccessor {
    void mcendgame$setTickWhenEmpty(boolean tickWhenEmpty);

    boolean mcendgame$shouldTick();
}
