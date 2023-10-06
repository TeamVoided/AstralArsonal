package org.teamvoided.template

import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory
import org.teamvoided.template.items.AAItems
import org.teamvoided.template.items.GunItem

@Suppress("unused")
object Template {
    const val MODID = "astral_armory"
    val OFF_HAND_FIRE = id("off_hand_fire")

    @JvmField
    val LOGGER = LoggerFactory.getLogger(Template::class.java)

    fun commonInit() {
        LOGGER.info("Hello from Common")
        AAItems.init()
        ClientPreAttackCallback.EVENT.register { _, player, clicks ->
            val x = player.offHandStack
            if (x.item is GunItem && clicks != 0) {
                ClientPlayNetworking.send(OFF_HAND_FIRE, PacketByteBuf(Unpooled.buffer()))
                return@register true
            }

            return@register false
        }
        ServerPlayNetworking.registerGlobalReceiver(OFF_HAND_FIRE) { _, player, _, _, _ ->
            val x = player.offHandStack
            (x.item as GunItem).preAttack(x,player, player.world)
        }
    }

    fun clientInit() {
        LOGGER.info("Hello from Client")
    }

    fun id(path: String) = Identifier(MODID, path)
}