package me.anemoi.sbutlimate.commands

import floppaclient.module.ModuleManager
import floppaclient.utils.ChatUtils
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class ToggleCommand : CommandBase() {
    override fun getCommandName(): String {
        return "toggle"
    }

    override fun getCommandAliases(): List<String> {
        return listOf(
            "fctoggle"
        )
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "/$commandName <module>"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.isNotEmpty()){
            val module = ModuleManager.getModuleByName(args[0])
            if (module != null){
                module.toggle()
                ChatUtils.modMessage("§aToggled ${module.name} ${if (module.enabled) "on" else "off"}.")
            }else{
                ChatUtils.modMessage("§cModule not found.")
            }
        }else{
            ChatUtils.modMessage("§cPlease specify a module.")
        }
    }
}