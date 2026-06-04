package de.fuballer.mcendgame.main.accessor;

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute;
import de.fuballer.mcendgame.main.component.world.VanillaTypeWorldAttributeInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.function.Predicate;

public interface WorldAttributesAccessor {
    int mcendgame$getAttributeUpdateCount();

    void mcendgame$addCustomAttribute(CustomAttribute attribute, Predicate<LivingEntity> applies);

    void mcendgame$removeCustomAttribute(CustomAttribute attribute, Predicate<LivingEntity> applies);

    List<CustomAttribute> mcendgame$getCustomTypeAttributes(LivingEntity entity);

    List<VanillaTypeWorldAttributeInstance> mcendgame$getVanillaTypeAttributesHistory(LivingEntity entity);
}
