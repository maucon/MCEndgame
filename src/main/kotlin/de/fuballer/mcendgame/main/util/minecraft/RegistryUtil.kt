package de.fuballer.mcendgame.main.util.minecraft

import de.fuballer.mcendgame.main.component.item.custom.armor.materials.CustomArmorMaterial
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItem
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.component.ComponentType
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.Items
import net.minecraft.item.equipment.EquipmentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Rarity
import net.minecraft.util.math.BlockPos

object RegistryUtil {
    fun registerItem(factory: (Item.Settings) -> Item, settings: Item.Settings, name: String): Item =
        Items.register(RegistryKeyUtil.createItemKey(name), factory, settings)

    fun registerBlock(factory: (AbstractBlock.Settings) -> Block, settings: AbstractBlock.Settings, name: String): Block =
        Blocks.register(RegistryKeyUtil.createBlockKey(name), factory, settings)
            .also { Items.register(it) }

    fun <T : BlockEntity> registerBlockEntityType(factory: (BlockPos, BlockState) -> T, block: Block, name: String): BlockEntityType<T> =
        Registry.register(Registries.BLOCK_ENTITY_TYPE, IdentifierUtil.default(name), FabricBlockEntityTypeBuilder.create(factory, block).build())

    fun <T : Entity> registerEntity(key: RegistryKey<EntityType<*>>, type: EntityType.Builder<T>): EntityType<T> =
        Registry.register(Registries.ENTITY_TYPE, key, type.build(key))

    fun <T : Entity> registerEntity(name: String, type: EntityType.Builder<T>): EntityType<T> =
        registerEntity(RegistryKeyUtil.createEntityKey(name), type)

    fun <T> registerDataComponentType(componentType: ComponentType<T>, name: String): ComponentType<T> =
        Registry.register(Registries.DATA_COMPONENT_TYPE, RegistryKeyUtil.createDataComponentTypeKey(name), componentType)

    fun registerArmorItem(material: CustomArmorMaterial, type: EquipmentType, name: String) =
        registerItem(
            ::Item,
            Item.Settings().armor(material.instance, type),
            name,
        )

    fun registerArmorItem(factory: (Item.Settings) -> Item, material: CustomArmorMaterial, type: EquipmentType, name: String) =
        registerItem(
            factory,
            Item.Settings().armor(material.instance, type),
            name,
        )

    fun <T : ScreenHandler> registerScreenHandler(name: String, screenHandlerType: ScreenHandlerType<T>): ScreenHandlerType<T> =
        Registry.register(Registries.SCREEN_HANDLER, IdentifierUtil.default(name), screenHandlerType)

    fun registerItemGroup(key: RegistryKey<ItemGroup>, type: ItemGroup.Builder): ItemGroup =
        Registry.register(Registries.ITEM_GROUP, key, type.build())

    fun registerStatusEffect(name: String, effect: StatusEffect): RegistryEntry<StatusEffect> =
        Registry.registerReference(Registries.STATUS_EFFECT, IdentifierUtil.default(name), effect)

    fun registerAspectItem(factory: (Item.Settings) -> Item, name: String, rarity: Rarity = Rarity.UNCOMMON) = registerItem(factory, Item.Settings().rarity(rarity), name) as AspectItem

    fun registerCrystalItem(factory: (Item.Settings) -> Item, name: String, rarity: Rarity = Rarity.UNCOMMON) = registerItem(factory, Item.Settings().rarity(rarity), name) as CrystalItem

    fun registerTotemItem(factory: (Item.Settings) -> Item, name: String, rarity: Rarity = Rarity.UNCOMMON) =
        registerItem(factory, Item.Settings().rarity(rarity).maxCount(1), name) as TotemItem

    fun registerSoundEvent(name: String): SoundEvent {
        val id = IdentifierUtil.default(name)
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id))
    }

    fun registerSoundEventReference(name: String): RegistryEntry.Reference<SoundEvent> {
        val id = IdentifierUtil.default(name)
        return Registry.registerReference(Registries.SOUND_EVENT, id, SoundEvent.of(id))
    }
}