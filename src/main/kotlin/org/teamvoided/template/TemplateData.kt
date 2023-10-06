package org.teamvoided.template

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import org.teamvoided.template.data.providers.EnglishTranslationProvider
import org.teamvoided.template.data.providers.ModelProvider
import org.teamvoided.template.Template.LOGGER

@Suppress("unused")
class TemplateData : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        LOGGER.info("Hello from DataInit")
        val pack: FabricDataGenerator.Pack = gen.createPack()
        pack.addProvider { o: FabricDataOutput -> ModelProvider(o) }
        pack.addProvider { o: FabricDataOutput -> EnglishTranslationProvider(o) }

//        pack.addProvider { o: FabricDataOutput -> RecipeProvider(o) }
//        pack.addProvider { o: FabricDataOutput -> BlockLootTableProvider(o) }
//        pack.addProvider { o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider> -> BlockTagProvider(o, r) }
//        pack.addProvider { o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider> -> ItemTagProvider(o, r) }
    }
}