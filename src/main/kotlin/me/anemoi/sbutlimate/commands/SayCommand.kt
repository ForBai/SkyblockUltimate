package me.anemoi.sbutlimate.commands

import floppaclient.FloppaClient
import floppaclient.utils.ChatUtils
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class SayCommand : CommandBase(){
    override fun getCommandName(): String {
        return "floppasay"
    }

    override fun getCommandAliases(): List<String> {
        return listOf(
            "fcsay"
        )
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "/$commandName <message>"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.isNotEmpty()){
            val message = args.joinToString(" ")
            FloppaClient.mc.thePlayer.sendChatMessage(message)
            ChatUtils.modMessage("§aSent message: §f$message")
        }else{
            ChatUtils.modMessage("§cPlease specify a message.")
        }
    }
}