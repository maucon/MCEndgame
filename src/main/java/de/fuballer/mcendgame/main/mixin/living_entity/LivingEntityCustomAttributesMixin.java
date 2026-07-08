package de.fuballer.mcendgame.main.mixin.living_entity;

import de.fuballer.mcendgame.main.accessor.LivingEntityCustomAttributesAccessor;
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
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
    private final String CUSTOM_ATTRIBUTES_NBT_KEY = "CustomAttributes";

    // TODO: #236 don't sync attributes with clients
    @Unique
    private static final TrackedData<List<CustomAttribute>> CUSTOM_ATTRIBUTES =
            DataTracker.registerData(LivingEntity.class, CustomAttribute.Companion.getLIST_TRACKED_DATA_HANDLER());

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(CUSTOM_ATTRIBUTES, new LinkedList<>());
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    void writeNbt(NbtCompound nbt, CallbackInfo ci) {
        var attributes = mcendgame$getCustomAttributes();
        if (attributes.isEmpty()) return;

        nbt.put(CUSTOM_ATTRIBUTES_NBT_KEY, CustomAttribute.Companion.getLIST_CODEC().encodeStart(NbtOps.INSTANCE, attributes).getOrThrow());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    void readNbt(NbtCompound nbt, CallbackInfo ci) {
        var attributes = CustomAttribute.Companion.getLIST_CODEC().parse(NbtOps.INSTANCE, nbt.get(CUSTOM_ATTRIBUTES_NBT_KEY)).result().orElse(List.of());

        var entity = (LivingEntity) (Object) this;
        var dataTracker = entity.getDataTracker();
        dataTracker.set(CUSTOM_ATTRIBUTES, attributes);
    }

    @Override
    public void mcendgame$addCustomAttribute(CustomAttribute customAttribute) {
        var entity = (LivingEntity) (Object) this;
        var dataTracker = entity.getDataTracker();
        var attributes = new LinkedList<>(dataTracker.get(CUSTOM_ATTRIBUTES));
        attributes.add(customAttribute);
        dataTracker.set(CUSTOM_ATTRIBUTES, attributes);
    }

    @Override
    public List<CustomAttribute> mcendgame$getCustomAttributes() {
        var entity = (LivingEntity) (Object) this;
        var dataTracker = entity.getDataTracker();
        return dataTracker.get(CUSTOM_ATTRIBUTES);
    }
}