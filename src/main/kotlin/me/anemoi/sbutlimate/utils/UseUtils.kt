package me.anemoi.sbutlimate.utils

import floppaclient.utils.fakeactions.FakeActionUtils
import floppaclient.utils.inventory.InventoryUtils
import java.util.*
import kotlin.concurrent.schedule


object UseUtils {
    //delay int ticks
    fun useItemWithDelay(itemSlot: Int, swapBack: Boolean = true, fromInv: Boolean = false, delay: Int = 1) {
        //delay using some timer from kotlin a tick is a 20th of a second
        //delay is in ticks
        Timer().schedule(50L * delay) { FakeActionUtils.useItem(itemSlot, swapBack, fromInv) }
    }

    fun useItemWithDelay(
        name: String,
        swapBack: Boolean = true,
        fromInv: Boolean = false,
        delay: Int = 1,
        ignoreCase: Boolean = false
    ): Boolean {
        val itemSlot = InventoryUtils.findItem(name, ignoreCase, fromInv) ?: return false
        this.useItemWithDelay(itemSlot, swapBack, fromInv, delay)
        return true
    }
}