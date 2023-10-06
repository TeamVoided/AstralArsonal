package org.teamvoided.template.data.providers

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.model.BlockStateModelGenerator
import net.minecraft.data.client.model.Models
import org.teamvoided.template.items.AAItems.items

class ModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(gen: BlockStateModelGenerator) {}

    override fun generateItemModels(gen: ItemModelGenerator) {
        items.forEach { gen.register(it, Models.SINGLE_LAYER_ITEM) }
    }
}