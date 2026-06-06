package de.fuballer.mcendgame.main.accessor;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public interface LivingEntityTemporaryAttributeModifierAccessor {
    void mcendgame$addTemporaryAttributeModifier(
            Holder<Attribute> type,
            Identifier identifier,
            int ticks,
            double value,
            AttributeModifier.Operation operation
    );
}
