package de.fuballer.mcendgame.client.mixin.player;

import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HidePlayerModelPartArmor;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Avatar.class)
public class AvatarMixin {
    @Inject(method = "isModelPartShown", at = @At("HEAD"), cancellable = true)
    public void isModelPartVisible(PlayerModelPart modelPart, CallbackInfoReturnable<Boolean> cir) {
        var playerLikeEntity = (Avatar) (Object) this;

        var armorItems = List.of(
                playerLikeEntity.getItemBySlot(EquipmentSlot.HEAD).getItem(),
                playerLikeEntity.getItemBySlot(EquipmentSlot.CHEST).getItem(),
                playerLikeEntity.getItemBySlot(EquipmentSlot.LEGS).getItem(),
                playerLikeEntity.getItemBySlot(EquipmentSlot.FEET).getItem()
        );

        for (Item item : armorItems) {
            if (!(item instanceof HidePlayerModelPartArmor armor)) continue;

            if (!armor.hidesModelPart(modelPart)) continue;
            cir.setReturnValue(false);
            return;
        }
    }
}
