package de.fuballer.mcendgame.main.mixin.living_entity;

import de.fuballer.mcendgame.main.accessor.LivingEntityWitherDamageImmunityAccessor;
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public class LivingEntityWitherDamageImmunityMixin implements LivingEntityWitherDamageImmunityAccessor {
    @Override
    public boolean mcendgame$isImmuneToWitherDamage() {
        var entity = (LivingEntity) (Object) this;

        var allAttributes = CustomAttributesExtensions.INSTANCE.getAllCustomAttributes(entity);
        var attributes = allAttributes.get(CustomAttributeTypes.INSTANCE.getWITHER_DAMAGE_IMMUNITY());
        return (attributes != null && !attributes.isEmpty());
    }
}
