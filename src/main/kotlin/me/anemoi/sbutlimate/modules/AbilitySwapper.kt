package me.anemoi.sbutlimate.modules

import floppaclient.FloppaClient.Companion.mc
import floppaclient.module.Category
import floppaclient.module.Module
import floppaclient.module.SelfRegisterModule
import floppaclient.module.settings.Visibility
import floppaclient.module.settings.impl.NumberSetting
import floppaclient.utils.ChatUtils
import gg.essential.universal.ChatColor
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*

@SelfRegisterModule
object AbilitySwapper : Module(
    name = "AbilitySwapper",
    description = "Swap your hotm abilities",
    category = Category.MISC
) {
    private val swapDelay: Double by NumberSetting(
        "Swap Delay",
        125.0,
        25.0,
        1000.0,
        25.0,
        Visibility.VISIBLE,
        "Delay between interactions in ms"
    )

    private val startDelay: Double by NumberSetting(
        "Start Delay",
        25.0,
        0.0,
        10000.0,
        100.0,
        Visibility.VISIBLE,
        "Delay before starting in ms"
    )

    private var stage = 0;

    override fun onKeyBind() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                ChatUtils.sendChat("/hotm")
                stage = 1
            }
        }, startDelay.toLong())
    }

    @SubscribeEvent
    fun onGuiTick(event: GuiOpenEvent){
        val gui = event.gui
        if (gui !is GuiChest) return
        val chest = gui as GuiChest
        val inventory = (chest.inventorySlots as ContainerChest).lowerChestInventory ?: return

        if (ChatColor.stripControlCodes(inventory.displayName.unformattedText)?.lowercase().equals("heart of the mountain")){
            if (stage == 1){
                if (inventory.getStackInSlot(29).isItemEnchanted){
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, 33, 2, 3, mc.thePlayer)
                            stage = 0
                        }
                    }, swapDelay.toLong())
                }else if (inventory.getStackInSlot(33).isItemEnchanted){
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, 29, 2, 3, mc.thePlayer)
                            stage = 0
                        }
                    }, swapDelay.toLong())
                }else{
                    ChatUtils.modMessage("Gui didn't open correctly, please try again")
                    stage = 0
                }
            }
        }
    }
}