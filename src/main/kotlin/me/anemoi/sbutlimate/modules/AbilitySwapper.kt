package me.anemoi.sbutlimate.modules

import floppaclient.FloppaClient.Companion.mc
import floppaclient.module.Category
import floppaclient.module.Module
import floppaclient.module.SelfRegisterModule
import floppaclient.module.settings.Visibility
import floppaclient.module.settings.impl.BooleanSetting
import floppaclient.module.settings.impl.NumberSetting
import floppaclient.utils.ChatUtils
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraft.item.ItemStack
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
        "Delay between interactions in ms, account for ping"
    )

    private val startDelay: Double by NumberSetting(
        "Start Delay",
        50.0,
        0.0,
        10000.0,
        50.0,
        Visibility.VISIBLE,
        "Delay before starting in ms"
    )

//    private val invertStart: Boolean by BooleanSetting(
//        "Invert Start",
//        false,
//        Visibility.VISIBLE,
//        "Inverts the start"
//    )

    private var slot = false;
    private var thread: Thread? = null
    override fun onKeyBind() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                ChatUtils.sendChat("/hotm")
                if (thread == null || !thread!!.isAlive) {
                    thread = Thread({
                        for (i in 0..100) {
                            val slot29 = mc.thePlayer.openContainer.inventorySlots?.get(29)
                            val slot33 = mc.thePlayer.openContainer.inventorySlots?.get(33)
                            if (mc.thePlayer.openContainer is ContainerChest &&
                                mc.thePlayer.openContainer?.inventorySlots?.get(0)?.inventory?.name?.startsWith("Heart") == true &&
                                slot29 != null &&
                                slot33 != null
                            ) {
                                if (slot29.stack.isItemEnchanted) {
                                    mc.playerController.windowClick(
                                        mc.thePlayer.openContainer.windowId,
                                        33,
                                        0,
                                        0,
                                        mc.thePlayer
                                    )
                                } else if (slot33.stack.isItemEnchanted) {
                                    mc.playerController.windowClick(
                                        mc.thePlayer.openContainer.windowId,
                                        29,
                                        0,
                                        0,
                                        mc.thePlayer
                                    )
                                }
                                Thread.sleep(swapDelay.toLong())
                                mc.thePlayer.closeScreen()
                                return@Thread
                            }
                            Thread.sleep(20)
                        }
                        ChatUtils.modMessage("§aSwapping Ability, timed out")
                    }, "Ability Swapper")
                    thread!!.start()
                }

            }
        }, startDelay.toLong())
    }


    fun getItem(slotNum: Int, currentScreen: GuiChest): ItemStack? {
        val container = currentScreen.inventorySlots as ContainerChest
        return container.getSlot(slotNum).stack
    }

}