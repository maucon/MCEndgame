package de.fuballer.mcendgame.main.mixin.companion;

import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Animal.class)
public class AnimalCompanionRemoveWhenFarAwayMixin {
    @Inject(method = "removeWhenFarAway", at = @At("HEAD"), cancellable = true)
    void mcendgame$removeCompanionWhenFarAway(
            double distSqr,
            CallbackInfoReturnable<Boolean> cir
    ) {
        var animal = (Animal) (Object) this;
        if (!EntityMixinExtension.INSTANCE.isCompanion(animal)) return;
        if (!(animal instanceof TamableAnimal tamableAnimal)) return;

        var owner = tamableAnimal.getOwner();
        var shouldRemove = owner == null || !owner.isAlive();
        cir.setReturnValue(shouldRemove);
    }
}
