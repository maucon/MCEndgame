package de.fuballer.mcendgame.main.mixin.living_entity;

import de.fuballer.mcendgame.main.accessor.LivingEntityCustomAttributesAccessor;
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;
import java.util.List;

@Mixin(LivingEntity.class)
public class LivingEntityCustomAttributesMixin implements LivingEntityCustomAttributesAccessor {
    @Unique
    private static final String CUSTOM_ATTRIBUTES_NBT_KEY = "CustomAttributes";

    // TODO: #236 don't sync attributes with clients
    @Unique
    private static final EntityDataAccessor<List<CustomAttribute>> CUSTOM_ATTRIBUTES =
            SynchedEntityData.defineId(LivingEntity.class, CustomAttribute.Companion.getLIST_TRACKED_DATA_HANDLER());

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    void initDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(CUSTOM_ATTRIBUTES, new LinkedList<>());
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    void writeNbt(ValueOutput view, CallbackInfo ci) {
        var attributes = mcendgame$getCustomAttributes();
        if (attributes.isEmpty()) return;

        view.store(CUSTOM_ATTRIBUTES_NBT_KEY, CustomAttribute.Companion.getCODEC().listOf(), attributes);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    void readNbt(ValueInput view, CallbackInfo ci) {
        var attributes = view.read(CUSTOM_ATTRIBUTES_NBT_KEY, CustomAttribute.Companion.getCODEC().listOf()).orElse(List.of());

        var entity = (LivingEntity) (Object) this;
        var dataTracker = entity.getEntityData();
        dataTracker.set(CUSTOM_ATTRIBUTES, attributes);
    }

    @Override
    public void mcendgame$addCustomAttribute(CustomAttribute customAttribute) {
        var entity = (LivingEntity) (Object) this;
        var dataTracker = entity.getEntityData();
        var attributes = new LinkedList<>(dataTracker.get(CUSTOM_ATTRIBUTES));
        attributes.add(customAttribute);
        dataTracker.set(CUSTOM_ATTRIBUTES, attributes);
    }

    @Override
    public List<CustomAttribute> mcendgame$getCustomAttributes() {
        var entity = (LivingEntity) (Object) this;
        var dataTracker = entity.getEntityData();
        return dataTracker.get(CUSTOM_ATTRIBUTES);
    }
}