package de.fuballer.mcendgame.main.mixin.damage;

import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Armadillo.class)
public abstract class ArmadilloDamageCalculationMixin extends LivingEntity {
    protected ArmadilloDamageCalculationMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    public abstract boolean isScared();

    @ModifyVariable(
            method = "hurtServer",
            at = @At("HEAD"),
            argsOnly = true,
            name = "source"
    )
    private DamageSource onModifyDamageSource(DamageSource source) {
        var extendedSource = source instanceof ExtendedDamageSource
                ? (ExtendedDamageSource) source
                : new ExtendedDamageSource(source);

        extendedSource.getDamageCalculationConfig().armadilloDamageReduction(isScared());
        return extendedSource;
    }
}
