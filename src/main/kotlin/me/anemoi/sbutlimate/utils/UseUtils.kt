package me.anemoi.sbutlimate.utils

import floppaclient.utils.fakeactions.FakeActionUtils
import java.util.*


object UseUtils {
    //delay int ticks
    fun useItemWithDelay(itemSlot: Int, swapBack: Boolean = true, fromInv: Boolean = false, delay: Int = 1) {
        //delay using some timer from kotlin a tick is a 20th of a second
        //delay is in ticks
        Timer().schedule(object : TimerTask() {
            override fun run() {
                FakeActionUtils.useItem(itemSlot, swapBack, fromInv)
            }
        }, 50L * delay)
    }
}