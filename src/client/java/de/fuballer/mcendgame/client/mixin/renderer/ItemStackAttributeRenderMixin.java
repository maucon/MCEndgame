package de.fuballer.mcendgame.client.mixin.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.fuballer.mcendgame.main.MCEndgame;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.apache.commons.lang3.function.TriConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackAttributeRenderMixin {
    @WrapOperation(
            method = "addAttributeTooltips",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Lorg/apache/commons/lang3/function/TriConsumer;)V"
            )
    )
    private void filterCustomModifiers(
            ItemStack instance,
            EquipmentSlotGroup slot,
            TriConsumer<Holder<Attribute>, AttributeModifier, ItemAttributeModifiers.Display> attributeModifierConsumer,
            Operation<Void> original
    ) {
        TriConsumer<Holder<Attribute>, AttributeModifier, ItemAttributeModifiers.Display> wrappedConsumer
                = (attribute, modifier, display) -> {
            if (modifier.id().getNamespace().equals(MCEndgame.MOD_ID)) return;

            attributeModifierConsumer.accept(attribute, modifier, display);
        };
        original.call(instance, slot, wrappedConsumer);
    }
}
