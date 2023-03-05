package me.anemoi.sbutlimate.modules

import floppaclient.module.Category
import floppaclient.module.Module
import floppaclient.module.SelfRegisterModule
import floppaclient.module.settings.Visibility
import floppaclient.module.settings.impl.BooleanSetting
import floppaclient.module.settings.impl.ColorSetting
import floppaclient.module.settings.impl.NumberSetting
import floppaclient.module.settings.impl.StringSetting
import floppaclient.utils.ChatUtils.modMessage
import me.anemoi.sbutlimate.commands.SayCommand
import me.anemoi.sbutlimate.commands.ToggleCommand
import me.anemoi.sbutlimate.commands.UseCommand
import net.minecraftforge.client.ClientCommandHandler
import java.awt.Color

@SelfRegisterModule
object MainModule : Module(
    "Skyblock Ultimate",
    category = Category.PLAYER,
    description = "Main toggle for Skyblock Ultimate"
) {

    private val mySetting: Boolean by BooleanSetting("Funny Setting", false, Visibility.VISIBLE, "It does nothing")
    private val mySetting2: Color by ColorSetting(
        "Funny Setting",
        Color(255, 0, 0, 0),
        true,
        Visibility.VISIBLE,
        "It does nothing"
    )
    private val mySetting3: Double by NumberSetting(
        "Funny Setting",
        5.0,
        0.0,
        10.0,
        1.0,
        Visibility.VISIBLE,
        "It does nothing"
    )
    private val mySetting5: String by StringSetting(
        "Funny Setting",
        "ok why am i here",
        100,
        Visibility.VISIBLE,
        "It does nothing"
    )

    override fun onDisable() {
        modMessage("Fuck you, you should not disable this!", true)
        toggle()
    }

    override fun onEnable() {
        println("Funne, ha ha")
    }

    override fun onInitialize() {
        toggle()
        ClientCommandHandler.instance.registerCommand(UseCommand())
        ClientCommandHandler.instance.registerCommand(ToggleCommand())
        ClientCommandHandler.instance.registerCommand(SayCommand())
        println("Initialized main module of skyblock ultimate!!!!!")
    }
}