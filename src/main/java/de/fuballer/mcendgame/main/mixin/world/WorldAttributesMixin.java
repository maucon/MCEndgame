package de.fuballer.mcendgame.main.mixin.world;

import de.fuballer.mcendgame.main.accessor.WorldAttributesAccessor;
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute;
import de.fuballer.mcendgame.main.component.custom_attribute.data.VanillaAttributeType;
import de.fuballer.mcendgame.main.component.world.VanillaTypeWorldAttributeInstance;
import de.fuballer.mcendgame.main.component.world.WorldAttributeAction;
import de.fuballer.mcendgame.main.component.world.WorldAttributeInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Mixin(ServerLevel.class)
public class WorldAttributesMixin implements WorldAttributesAccessor {
    @Unique
    private int worldAttributesUpdate = 0;

    @Unique
    private final ArrayList<WorldAttributeInstance> customTypeAttributes = new ArrayList<>();

    @Unique
    private final ArrayList<VanillaTypeWorldAttributeInstance> vanillaTypeAttributesHistory = new ArrayList<>();

    @Override
    public int mcendgame$getAttributeUpdateCount() {
        return worldAttributesUpdate;
    }

    @Override
    public void mcendgame$addCustomAttribute(CustomAttribute attribute, Predicate<LivingEntity> applies) {
        if (attribute.getType() instanceof VanillaAttributeType) {
            vanillaTypeAttributesHistory.add(
                    new VanillaTypeWorldAttributeInstance(attribute, applies, WorldAttributeAction.ADD, ++worldAttributesUpdate)
            );
        } else {
            customTypeAttributes.add(new WorldAttributeInstance(attribute, applies));
        }
    }

    @Override
    public void mcendgame$removeCustomAttribute(CustomAttribute attribute, Predicate<LivingEntity> applies) {
        if (attribute.getType() instanceof VanillaAttributeType) {
            var instance = new VanillaTypeWorldAttributeInstance(attribute, applies, WorldAttributeAction.REMOVE, ++worldAttributesUpdate);
            vanillaTypeAttributesHistory.add(instance);
        } else {
            customTypeAttributes.removeIf(inst ->
                    inst.getAttribute().equals(attribute) && inst.getApplies().equals(applies)
            );
        }
    }

    @Override
    public List<CustomAttribute> mcendgame$getCustomTypeAttributes(LivingEntity entity) {
        return customTypeAttributes
                .stream()
                .filter(i -> i.getApplies().test(entity))
                .map(WorldAttributeInstance::getAttribute)
                .toList();
    }

    @Override
    public List<VanillaTypeWorldAttributeInstance> mcendgame$getVanillaTypeAttributesHistory(LivingEntity entity) {
        return vanillaTypeAttributesHistory
                .stream()
                .filter(i -> i.getApplies().test(entity))
                .toList();
    }
}
