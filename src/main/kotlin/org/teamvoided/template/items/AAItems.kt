package org.teamvoided.template.items

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.template.Template.id
import java.util.*

object AAItems {
    val items = LinkedList<Item>()

    val GUN: Item = GunItem()

    fun init() {
        register("gun", GUN)

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS)
            .register(ItemGroupEvents.ModifyEntries { items.forEach { item -> it.prepend(item) } })
    }


    private fun register(id: String, item: Item): Item {
        items.add(item)
        return Registry.register(Registries.ITEM, id(id), item)
    }
}