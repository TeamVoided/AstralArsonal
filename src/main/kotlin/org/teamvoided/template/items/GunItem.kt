package org.teamvoided.template.items

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.BlockState
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.*
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.teamvoided.voidlib.core.nbt.getInt
import org.teamvoided.voidlib.core.nbt.putInt


class GunItem(settings: FabricItemSettings, val reloadSpeed: Int = 20, val ammoCapacity: Int = 2) : Item(settings) {
    constructor(reloadSpeed: Int) : this(FabricItemSettings().maxCount(1), reloadSpeed)
    constructor() : this(FabricItemSettings().maxCount(1))

    override fun canMine(state: BlockState?, world: World?, pos: BlockPos?, miner: PlayerEntity): Boolean =
        !miner.isCreative

    fun preAttack(stack: ItemStack, player: PlayerEntity, world: World) {
        if (!player.itemCooldownManager.isCoolingDown(this))
            fire(world, player, Hand.OFF_HAND)

    }

    override fun appendTooltip(
        stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext,
    ) {
        super.appendTooltip(stack, world, tooltip, context)
        tooltip.add(Text.literal("${stack.getAmmo()}/$ammoCapacity").formatted(Formatting.GRAY))
    }

    override fun getDefaultStack(): ItemStack {
        val stack = super.getDefaultStack()
        stack.setAmmo(ammoCapacity)
        return stack
    }

    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient) {
            return TypedActionResult.pass(player.getStackInHand(hand))
        }
        if (!world.isClient && hand == Hand.MAIN_HAND) fire(world, player, hand)
        return TypedActionResult.pass(player.getStackInHand(hand))
    }

    fun fire(world: World, player: PlayerEntity, hand: Hand) {
        val gun = player.getStackInHand(hand)
        val ammoCount = gun.getAmmo()
        if (ammoCount > 0) {
            val stack = Items.ARROW.defaultStack
            for (i in 0..5) {
                val persistentProjectileEntity = (Items.ARROW as ArrowItem).createArrow(world, stack, player)
                persistentProjectileEntity
                    .setProperties(player, player.pitch, player.yaw, 1.0f, 3.0f, 1.0f)

                world.spawnEntity(persistentProjectileEntity)
            }
            gun.setAmmo(ammoCount - 1)
        } else {
            player.sendMessage(Text.of("reloading..."), true)
            player.itemCooldownManager.set(this, reloadSpeed)
            gun.setAmmo(ammoCapacity)
        }
    }

    companion object {

        const val AMMO = "ammo"

        fun ItemStack.getAmmo(): Int = this.getInt(AMMO)
        fun ItemStack.setAmmo(amount: Int) = this.putInt(AMMO, amount)


        fun PlayerEntity.getAmmoType(stack: ItemStack): ItemStack {
            val item = stack.item
            return if (item is RangedWeaponItem) {
                var predicate = item.heldProjectiles
                val itemStack = RangedWeaponItem.getHeldProjectile(this, predicate)
                if (!itemStack.isEmpty) {
                    itemStack
                } else {
                    predicate = item.projectiles
                    for (i in 0 until this.inventory.size()) {
                        val itemStack2: ItemStack = this.inventory.getStack(i)
                        if (predicate.test(itemStack2)) {
                            return itemStack2
                        }
                    }
                    if (this.abilities.creativeMode) ItemStack(Items.ARROW) else ItemStack.EMPTY
                }
            } else ItemStack.EMPTY
        }

    }
}