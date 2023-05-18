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
        return "/$commandName [-pitch=<pitch> -yaw=<yaw> -frominv=<true/false> -delay=<delay> -amount=<amount>] <item>"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        try {
            if (args.isEmpty()) {
                ChatUtils.modMessage("§cPlease specify an item.")
                ChatUtils.modMessage("§cUsage: ${getCommandUsage(sender)}")
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
                } else if (arg.startsWith("-amount=")) {
                    amount = arg.substring(8).toInt()
                    setAmount = true;
                } else {
                    itemName += " $arg"
                }
            }

            // make it fully nested so all combinations work
            if (rotate) {
                if (fromInv) {
                    if (doDelay) {
                        if (setAmount) {
                            UseUtils.useFromInvWithDelayAndRotation(itemName, amount, delay, pitch, yaw)
                        } else {
                            UseUtils.useFromInvWithDelayAndRotation(itemName, delay, pitch, yaw)
                        }
                    } else {
                        if (setAmount) {
                            UseUtils.useFromInvWithRotation(itemName, amount, pitch, yaw)
                        } else {
                            UseUtils.useFromInvWithRotation(itemName, pitch, yaw)
                        }
                    }
                } else {
                    if (doDelay) {
                        if (setAmount) {
                            UseUtils.useWithDelayAndRotation(itemName, amount, delay, pitch, yaw)
                        } else {
                            UseUtils.useWithDelayAndRotation(itemName, delay, pitch, yaw)
                        }
                    } else {
                        if (setAmount) {
                            UseUtils.useWithRotation(itemName, amount, pitch, yaw)
                        } else {
                            UseUtils.useWithRotation(itemName, pitch, yaw)
                        }
                    }
                }
            } else {
                if (fromInv) {
                    if (doDelay) {
                        if (setAmount) {
                            UseUtils.useFromInvWithDelay(itemName, amount, delay)
                        } else {
                            UseUtils.useFromInvWithDelay(itemName, delay)
                        }
                    } else {
                        if (setAmount) {
                            UseUtils.useFromInv(itemName, amount)
                        } else {
                            UseUtils.useFromInv(itemName)
                        }
                    }
                } else {
                    if (doDelay) {
                        if (setAmount) {
                            UseUtils.useWithDelay(itemName, amount, delay)
                        } else {
                            UseUtils.useWithDelay(itemName, delay)
                        }
                    } else {
                        if (setAmount) {
                            UseUtils.use(itemName, amount)
                        } else {
                            UseUtils.use(itemName)
                        }
                    }
                }
            }

        } catch (e: Throwable) {
            ChatUtils.modMessage("§cArguments error.")
        }
    }
}