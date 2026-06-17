package de.fuballer.mcendgame.main.mixin.living_entity;

import de.fuballer.mcendgame.main.accessor.LivingEntityLinkAttributeAccessor;
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll;
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntRoll;
import de.fuballer.mcendgame.main.component.custom_attribute.effects.link.LinkSettings;
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes;
import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealingExtension;
import de.fuballer.mcendgame.main.util.extension.EntityExtension;
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil;
import io.netty.buffer.ByteBuf;
import kotlin.Pair;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.stream.Collectors;

@Mixin(LivingEntity.class)
public abstract class LivingEntityLinkAttributeMixin implements LivingEntityLinkAttributeAccessor {
    @Unique
    private static final StreamCodec<ByteBuf, UUID> UUID_PACKET_CODEC = ByteBufCodecs.STRING_UTF8.map(UUID::fromString, UUID::toString);
    @Unique
    private static final StreamCodec<ByteBuf, Pair<UUID, Long>> UUID_LONG_PAIR_PACKET_CODEC =
            StreamCodec.composite(
                    UUID_PACKET_CODEC,
                    Pair::getFirst,
                    ByteBufCodecs.VAR_LONG,
                    Pair::getSecond,
                    Pair::new
            );
    @Unique
    private static final StreamCodec<ByteBuf, List<Pair<UUID, Long>>> UUID_LONG_PAIR_LIST_PACKET_CODEC
            = UUID_LONG_PAIR_PACKET_CODEC.apply(ByteBufCodecs.list());
    @Unique
    private static final EntityDataSerializer<List<Pair<UUID, Long>>> UUID_LONG_PAIR_LIST_TRACKED_DATA_HANDLER
            = EntityDataSerializer.forValueType(UUID_LONG_PAIR_LIST_PACKET_CODEC);

    @Unique
    private HashSet<UUID> linkedBy = new HashSet<>();

    static {
        FabricEntityDataRegistry.register(IdentifierUtil.INSTANCE.defaultJava("entity_link_attribute_data_tracker"), UUID_LONG_PAIR_LIST_TRACKED_DATA_HANDLER);
    }

    @Unique
    private static final EntityDataAccessor<List<Pair<UUID, Long>>> LINKED_ENTITIES =
            SynchedEntityData.defineId(LivingEntity.class, UUID_LONG_PAIR_LIST_TRACKED_DATA_HANDLER);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void initDataTracker(SynchedEntityData.Builder entityData, CallbackInfo ci) {
        entityData.define(LINKED_ENTITIES, new ArrayList<>());
    }

    @Unique
    public Map<UUID, Long> getLinkedEntitiesMap() {
        return ((LivingEntity) (Object) this)
                .getEntityData()
                .get(LINKED_ENTITIES)
                .stream()
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }

    @Unique
    public void setLinkedEntitiesMap(Map<UUID, Long> map) {
        var list = map.entrySet().stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        ((LivingEntity) (Object) this).getEntityData().set(LINKED_ENTITIES, list);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    void tick(CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        if (entity.level().isClientSide()) return;

        if (entity.tickCount % LinkSettings.LINK_UPDATE_INTERVAL == 0) updateLinkedEntities(entity);
        if (entity.tickCount % LinkSettings.LINK_DAMAGE_INTERVAL == 0) damageLinkedEntities(entity);
    }

    @Unique
    private void updateLinkedEntities(LivingEntity entity) {
        var allAttributes = CustomAttributesExtensions.INSTANCE.getAllCustomAttributes(entity);
        var linkAttributes = allAttributes.get(CustomAttributeTypes.INSTANCE.getLINK_NEARBY_ENEMIES());
        if (linkAttributes == null || linkAttributes.isEmpty()) {
            clearLinkedEntities(entity);
            return;
        }

        var highestAttributeDistance = linkAttributes.stream().mapToInt(a -> ((IntRoll) a.getRolls().getFirst()).getValue()).max().orElse(0);
        var distance = Math.min(highestAttributeDistance, LinkSettings.MAX_LINK_DISTANCE);
        if (distance <= 0) {
            clearLinkedEntities(entity);
            return;
        }

        var world = (ServerLevel) entity.level();

        var paddedDistance = distance + LinkSettings.LINK_DISTANCE_BREAK_PADDING;
        var enemiesInPaddedRange = getEnemiesInRange(world, entity, paddedDistance);
        var enemyUuidsInPaddedRange = new HashMap<UUID, Float>(enemiesInPaddedRange.size());
        enemiesInPaddedRange.forEach((e, d) -> enemyUuidsInPaddedRange.put(e.getUUID(), d));

        var oldLinkedEntities = getLinkedEntitiesMap();
        var updatedLinkedEntities = new HashMap<>(oldLinkedEntities);
        updatedLinkedEntities.keySet().retainAll(enemyUuidsInPaddedRange.keySet());

        var unlinkedEntities = new HashSet<>(oldLinkedEntities.keySet());
        unlinkedEntities.removeAll(updatedLinkedEntities.keySet());
        var linkOriginUuid = entity.getUUID();
        for (UUID unlinkedUuid : unlinkedEntities) {
            var unlinkedEntity = world.getEntity(unlinkedUuid);
            if (unlinkedEntity == null || !unlinkedEntity.isAlive()) continue;
            ((LivingEntityLinkAttributeAccessor) unlinkedEntity).mcendgame$removeLinkedBy(linkOriginUuid);
        }

        var enemyUuidsInNonPaddedRange = new ArrayList<UUID>();
        enemyUuidsInPaddedRange.forEach((uuid, dist) -> {
            if (dist <= distance) enemyUuidsInNonPaddedRange.add(uuid);
        });

        var currentTime = world.getGameTime();
        enemyUuidsInNonPaddedRange.forEach(uuid -> {
            if (updatedLinkedEntities.putIfAbsent(uuid, currentTime) == null) {
                var linkedEntity = world.getEntity(uuid);
                if (linkedEntity != null && linkedEntity.isAlive()) {
                    ((LivingEntityLinkAttributeAccessor) linkedEntity).mcendgame$addLinkedBy(linkOriginUuid);
                }
            }
        });

        setLinkedEntitiesMap(updatedLinkedEntities);
    }

    @Unique
    private Map<Entity, Float> getEnemiesInRange(
            ServerLevel world,
            LivingEntity entity,
            Float distance
    ) {
        return world.getEntities(entity, entity.getBoundingBox().inflate(distance))
                .stream()
                .filter(Entity::isAlive)
                .filter(e -> EntityExtension.INSTANCE.isEnemy(entity, e))
                .filter(e -> canLink(entity, e, world))
                .map(enemy -> new Pair<>(enemy, enemy.distanceTo(entity)))
                .filter(enemyDistancePair -> enemyDistancePair.getSecond() <= distance)
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }

    @Unique
    private boolean canLink(
            Entity entity1,
            Entity entity2,
            ServerLevel world
    ) {
        var vec1 = new Vec3(entity1.getX(), entity1.getY() + entity1.getBbHeight() * LinkSettings.LINK_CONNECTION_HEIGHT, entity1.getZ());
        var vec2 = new Vec3(entity2.getX(), entity2.getY() + entity2.getBbHeight() * LinkSettings.LINK_CONNECTION_HEIGHT, entity2.getZ());
        var context = new ClipContext(vec1, vec2, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity1);
        return world.clip(context).getType() == HitResult.Type.MISS;
    }

    @Unique
    private void clearLinkedEntities(LivingEntity entity) {
        entity.getEntityData().set(LINKED_ENTITIES, new ArrayList<>());
    }

    @Unique
    private void damageLinkedEntities(LivingEntity entity) {
        var allAttributes = CustomAttributesExtensions.INSTANCE.getAllCustomAttributes(entity);
        var linkAttributes = allAttributes.get(CustomAttributeTypes.INSTANCE.getDAMAGE_LINKED_ENEMIES());
        if (linkAttributes == null || linkAttributes.isEmpty()) return;
        var sum = linkAttributes.stream().mapToDouble(attribute -> ((DoubleRoll) attribute.getRolls().getFirst()).getValue()).sum();

        var world = (ServerLevel) entity.level();
        var time = world.getGameTime();
        var linkedEntities = entity.getEntityData().get(LINKED_ENTITIES)
                .stream()
                .map(pair -> new Pair<>(world.getEntity(pair.getFirst()), pair.getSecond()))
                .filter(pair -> pair.getFirst() != null && pair.getFirst().isAlive())
                .filter(pair -> time - pair.getSecond() >= LinkSettings.INSTANCE.getLinkConnectingTime(entity.distanceTo(pair.getFirst())))
                .map(Pair::getFirst)
                .toList();

        linkedEntities.forEach(linkedEntity -> DamageDealingExtension.INSTANCE.dealElementalSpellDamage(linkedEntity, sum, entity, entity));
    }

    @Override
    public Map<UUID, Long> mcendgame$getLinkedEntities() {
        return getLinkedEntitiesMap();
    }

    @Override
    public void mcendgame$addLinkedBy(UUID uuid) {
        mcendgame$getLinkedBy().add(uuid);
    }

    @Override
    public void mcendgame$removeLinkedBy(UUID uuid) {
        mcendgame$getLinkedBy().remove(uuid);
    }

    @Override
    public HashSet<UUID> mcendgame$getLinkedBy() {
        if (linkedBy == null) linkedBy = new HashSet<>();
        return linkedBy;
    }
}
