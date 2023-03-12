package me.anemoi.sbutlimate.utils

import floppaclient.utils.ChatUtils
import floppaclient.utils.fakeactions.FakeActionUtils
import floppaclient.utils.inventory.InventoryUtils
import me.anemoi.sbutlimate.modules.MainModule
import java.util.*
import kotlin.concurrent.schedule


object UseUtils {
    //delay int ticks
    private fun useItemWithDelayOld(itemSlot: Int, swapBack: Boolean = true, fromInv: Boolean = false, delay: Int = 1) {
        //delay using some timer from kotlin a tick is a 20th of a second
        //delay is in ticks
        Timer().schedule(50L * delay) { FakeActionUtils.useItem(itemSlot, swapBack, fromInv) }
    }

    fun useItemWithDelayOld(
        name: String,
        swapBack: Boolean = true,
        fromInv: Boolean = false,
        delay: Int = 1,
        ignoreCase: Boolean = false
    ): Boolean {
        val itemSlot = InventoryUtils.findItem(name, ignoreCase, fromInv) ?: return false
        this.useItemWithDelayOld(itemSlot, swapBack, fromInv, delay)
        return true
    }

    fun use(itemName: String) {
        FakeActionUtils.useItem(
            itemName,
            ignoreCase = true
        )
    }

    fun use(itemName: String, amount: Int) {
        for (i in 0..amount) {
            use(itemName)
        }
    }

    fun useWithDelay(itemName: String, delay: Int) {
        Timer().schedule(50L * delay) { use(itemName) }
    }

    fun useWithDelay(itemName: String, amount: Int, delay: Int) {
        Thread {
            for (i in 0..amount) {
                useWithDelay(itemName, delay)
                Thread.sleep(MainModule.timeBetweenUseActions.toLong())
            }
        }.start()
    }

    fun useFromInv(itemName: String) {
        FakeActionUtils.useItem(
            itemName,
            ignoreCase = true,
            fromInv = true
        )
    }

    fun useFromInv(itemName: String, amount: Int) {
        Thread {
            for (i in 0..amount) {
                useFromInv(itemName)
                Thread.sleep(MainModule.timeBetweenUseActions.toLong())
            }
        }.start()
    }

    fun useFromInvWithDelay(itemName: String, delay: Int) {
        Timer().schedule(50L * delay) { useFromInv(itemName) }
    }

    fun useFromInvWithDelay(itemName: String, amount: Int, delay: Int) {
        Thread {
            for (i in 0..amount) {
                useFromInvWithDelay(itemName, delay)
                Thread.sleep(MainModule.timeBetweenUseActions.toLong())
            }
        }.start()
    }

    fun useWithRotation(itemName: String, pitch: Double, yaw: Double) {
        ChatUtils.modMessage("§cUse with rotation is not implemented yet.")
    }

    fun useWithRotation(itemName: String, amount: Int, pitch: Double, yaw: Double) {
        Thread {
            for (i in 0..amount) {
                useWithRotation(itemName, pitch, yaw)
                Thread.sleep(MainModule.timeBetweenUseActions.toLong())
            }
        }.start()
    }

    fun useWithDelayAndRotation(itemName: String, delay: Int, pitch: Double, yaw: Double) {
        Timer().schedule(50L * delay) { useWithRotation(itemName, pitch, yaw) }
    }

    fun useWithDelayAndRotation(itemName: String, amount: Int, delay: Int, pitch: Double, yaw: Double) {
        Thread {
            for (i in 0..amount) {
                useWithDelayAndRotation(itemName, delay, pitch, yaw)
                Thread.sleep(MainModule.timeBetweenUseActions.toLong())
            }
        }.start()

    }

    fun useFromInvWithRotation(itemName: String, pitch: Double, yaw: Double) {
        ChatUtils.modMessage("§cUse from inv with rotation is not implemented yet.")
    }

    fun useFromInvWithRotation(itemName: String, amount: Int, pitch: Double, yaw: Double) {
        Thread {
            for (i in 0..amount) {
                useFromInvWithRotation(itemName, pitch, yaw)
                Thread.sleep(MainModule.timeBetweenUseActions.toLong())
            }
        }.start()
    }

    fun useFromInvWithDelayAndRotation(itemName: String, delay: Int, pitch: Double, yaw: Double) {
        Timer().schedule(50L * delay) { useFromInvWithRotation(itemName, pitch, yaw) }
    }

    fun useFromInvWithDelayAndRotation(itemName: String, amount: Int, delay: Int, pitch: Double, yaw: Double) {
        Thread {
            for (i in 0..amount) {
                useFromInvWithDelayAndRotation(itemName, delay, pitch, yaw)
                Thread.sleep(MainModule.timeBetweenUseActions.toLong())
            }
        }.start()
    }


}