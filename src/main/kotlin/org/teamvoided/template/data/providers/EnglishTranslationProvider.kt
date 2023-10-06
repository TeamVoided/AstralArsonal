package org.teamvoided.template.data.providers

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.util.Identifier
import org.apache.commons.lang3.text.WordUtils
import org.teamvoided.template.items.AAItems.items
import org.teamvoided.voidlib.core.id
class EnglishTranslationProvider(o: FabricDataOutput) : FabricLanguageProvider(o) {
    override fun generateTranslations(build: TranslationBuilder) {
        items.forEach { build.add(it.translationKey, genLang(it.id)) }
    }


    private fun genLang(identifier: Identifier): String =
        WordUtils.capitalize(identifier.path.replace("_", " "))
}