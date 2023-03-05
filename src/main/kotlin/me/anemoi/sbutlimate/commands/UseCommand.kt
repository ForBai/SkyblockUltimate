package me.anemoi.sbutlimate.commands

import floppaclient.utils.ChatUtils
import floppaclient.utils.fakeactions.FakeActionUtils
import me.anemoi.sbutlimate.utils.UseUtils
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class UseCommand : CommandBase() {
    override fun getCommandName(): String {
        return "use"
    }

    override fun getCommandAliases(): List<String> {
        return listOf(
            "fcuse"
        )
    }

    override fun getCommandUsage(sender: ICommandSender): String {
        return "/$commandName [-pitch=<pitch> -yaw=<yaw> -frominv=<true/false> -delay=<delay>] <item>"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        try {
            if (args.isEmpty()) {
                ChatUtils.modMessage("§cPlease specify an item.")
                return
            }
            var pitch = 0.0
            var yaw = 0.0
            var rotate = false
            var fromInv = true
            var delay = 0
            var doDelay = false
            var amount = 1;
            var setAmount = false;
            var itemName = ""
            for (arg in args) {
                if (arg.startsWith("-pitch=")) {
                    pitch = arg.substring(7).toDouble()
                    rotate = true
                } else if (arg.startsWith("-yaw=")) {
                    yaw = arg.substring(5).toDouble()
                    rotate = true
                } else if (arg.startsWith("-frominv=")) {
                    fromInv = arg.substring(9).toBoolean()
                } else if (arg.startsWith("-delay=")) {
                    delay = arg.substring(7).toInt()
                    doDelay = true
                } else {
                    itemName += " $arg"
                }
            }

            if (rotate) {
                //not implemented
            } else if (fromInv) {
                if (doDelay) {
                    UseUtils.useItemWithDelay(
                        itemName,
                        swapBack = true,
                        fromInv = true,
                        delay = delay,
                        ignoreCase = true
                    )
                } else {
                    FakeActionUtils.useItem(
                        itemName,
                        swapBack = true,
                        fromInv = true,
                        ignoreCase = true
                    )
                }
            }

        } catch (e: Throwable) {
            ChatUtils.modMessage("§cArguments error.")
        }
    }
}