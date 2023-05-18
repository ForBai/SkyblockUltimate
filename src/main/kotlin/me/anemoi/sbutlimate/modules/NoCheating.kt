package me.anemoi.sbutlimate.modules

import floppaclient.FloppaClient
import floppaclient.module.Category
import floppaclient.module.Module
import floppaclient.module.ModuleManager
import floppaclient.module.SelfRegisterModule
import gg.essential.api.utils.WebUtil
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

@SelfRegisterModule
object NoCheating : Module(
    "No Cheater",
    category = Category.PLAYER,
    description = "Disables Modules that are currently detected by the server"
) {

    @SubscribeEvent
    fun preMove(event: TickEvent.RenderTickEvent) {
        if (FloppaClient.mc.thePlayer == null || FloppaClient.mc.theWorld == null) return
        WebUtil.fetchString("https://gist.githubusercontent.com/ForBai/1d5debef5cc7c2cdb5e763ae92fa4430/raw/fd92b161eb76789391f321f950f74414ef5673d8/bannedmods")
            ?.split(",\n")
            ?.forEach {
                val mod = ModuleManager.getModuleByName(it)
                if (mod?.enabled == true) mod.toggle()
            }

    }

}