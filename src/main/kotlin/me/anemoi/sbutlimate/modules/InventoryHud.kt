package me.anemoi.sbutlimate.modules

import floppaclient.FloppaClient
import floppaclient.module.Category
import floppaclient.module.Module
import floppaclient.module.RegisterHudElement
import floppaclient.module.SelfRegisterModule
import floppaclient.module.settings.impl.ColorSetting
import floppaclient.ui.hud.HudElement
import floppaclient.utils.render.HUDRenderUtils
import net.minecraft.item.ItemStack
import java.awt.Color


@SelfRegisterModule
object InventoryHud : Module(
    "Inventory Hud",
    category = Category.RENDER,
    description = "A Cool Looking Inventory Hud"
) {

    private val bgColor: Color by ColorSetting("BG Color", Color(28, 28, 28, 255), true)
    private val itemBGColor: Color by ColorSetting("Item BG Color", Color(54, 54, 54, 109), true)

    @RegisterHudElement
    object InventoryHudElement : HudElement(this, 0, 150, 164, 74) {
        override fun renderHud() {
            // BG
            HUDRenderUtils.renderRect(0.0, 0.0, width.toDouble(), height.toDouble(), bgColor)

            var currentItem: ItemStack
            var itemX = 2
            var itemY = 2
            var slot = 0

            for (yRow in 1..4) {
                loop@
                for (xRow in 1..9) {

                    if (FloppaClient.mc.thePlayer.inventory.getStackInSlot(slot) == null
                    ) {
                        HUDRenderUtils.renderRect(itemX.toDouble(), itemY.toDouble(), 16.0, 16.0, itemBGColor)
                        itemX += 16 + 2
                        slot++
                        continue@loop
                    } else {
                        currentItem = FloppaClient.mc.thePlayer.inventory.getStackInSlot(slot)
                        HUDRenderUtils.renderRect(itemX.toDouble(), itemY.toDouble(), 16.0, 16.0, itemBGColor)
                        FloppaClient.mc.renderItem.renderItemAndEffectIntoGUI(currentItem, itemX, itemY)
                        itemX += 16 + 2
                        slot++
                        continue@loop
                    }
                }
                itemX = 2
                itemY += 2 + 16
            }
        }
    }
}
