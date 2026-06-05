package de.fuballer.mcendgame.main.util.minecraft

import de.fuballer.mcendgame.main.component.item.custom.armor.materials.CustomArmorMaterial
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItem
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.equipment.ArmorType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState

object RegistryUtil {
    fun registerItem(
        factory: (Item.Properties) -> Item,
        settings: Item.Properties,
        name: String
    ): Item {
        val key = RegistryKeyUtil.createItemKey(name)
        return Registry.register(
            BuiltInRegistries.ITEM,
            key,
            factory(settings.setId(key))
        )
    }

    fun registerBlock(
        factory: (BlockBehaviour.Properties) -> Block,
        settings: BlockBehaviour.Properties,
        name: String
    ): Block {
        val blockKey = RegistryKeyUtil.createBlockKey(name)
        val itemKey = RegistryKeyUtil.createItemKey(name)

        return Blocks.register(
            blockKey,
            factory,
            settings.setId(blockKey)
        ).also {
            registerItem(
                { prop -> BlockItem(it, prop) },
                Item.Properties().setId(itemKey),
                name
            )
        }
    }

    fun <T : BlockEntity> registerBlockEntityType(factory: (BlockPos, BlockState) -> T, block: Block, name: String): BlockEntityType<T> =
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, IdentifierUtil.default(name), FabricBlockEntityTypeBuilder.create(factory, block).build())

    fun <T : Entity> registerEntity(key: ResourceKey<EntityType<*>>, type: EntityType.Builder<T>): EntityType<T> =
        Registry.register(BuiltInRegistries.ENTITY_TYPE, key, type.build(key))

    fun <T : Entity> registerEntity(name: String, type: EntityType.Builder<T>): EntityType<T> =
        registerEntity(RegistryKeyUtil.createEntityKey(name), type)

    fun <T : Any> registerDataComponentType(componentType: DataComponentType<T>, name: String): DataComponentType<T> =
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, RegistryKeyUtil.createDataComponentTypeKey(name), componentType)

    fun registerArmorItem(
        factory: (Item.Properties) -> Item,
        material: CustomArmorMaterial,
        type: ArmorType,
        name: String
    ) = registerItem(
        factory,
        Item.Properties()
            .humanoidArmor(material.instance, type),
        name,
    )

    fun <T : AbstractContainerMenu> registerScreenHandler(name: String, screenHandlerType: MenuType<T>): MenuType<T> =
        Registry.register(BuiltInRegistries.MENU, IdentifierUtil.default(name), screenHandlerType)

    fun registerItemGroup(key: ResourceKey<CreativeModeTab>, type: CreativeModeTab.Builder): CreativeModeTab =
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, type.build())

    fun registerStatusEffect(name: String, effect: MobEffect): Holder<MobEffect> =
        Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, IdentifierUtil.default(name), effect)

    fun registerAspectItem(
        factory: (Item.Properties) -> Item,
        name: String,
        rarity: Rarity = Rarity.UNCOMMON
    ) = registerItem(
        factory,
        Item.Properties()
            .rarity(rarity),
        name
    ) as AspectItem

    fun registerCrystalItem(
        factory: (Item.Properties) -> Item,
        name: String,
        rarity: Rarity = Rarity.UNCOMMON
    ) = registerItem(
        factory,
        Item.Properties()
            .rarity(rarity),
        name
    ) as CrystalItem

    fun registerTotemItem(
        factory: (Item.Properties) -> Item,
        name: String,
        rarity: Rarity = Rarity.UNCOMMON
    ) = registerItem(
        factory,
        Item.Properties()
            .rarity(rarity)
            .stacksTo(1),
        name
    ) as TotemItem

    fun registerSoundEvent(name: String): SoundEvent {
        val id = IdentifierUtil.default(name)
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id))
    }
}