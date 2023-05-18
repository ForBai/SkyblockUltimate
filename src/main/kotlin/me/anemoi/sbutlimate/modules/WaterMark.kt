/*
 * Thanks to Pan4ur for the original code from his client, ThunderHackPlus(https://github.com/Pan4ur/ThunderHackPlus)
 * Modified and converted to kotlin by Anemoi/ForBai for use in SBUltimate
 */
package me.anemoi.sbutlimate.modules

import floppaclient.FloppaClient.Companion.mc
import floppaclient.module.Category
import floppaclient.module.Module
import floppaclient.module.RegisterHudElement
import floppaclient.module.SelfRegisterModule
import floppaclient.module.settings.impl.ColorSetting
import floppaclient.ui.clickgui.util.FontUtil
import floppaclient.ui.hud.HudElement
import me.anemoi.sbultimate.utils.RenderUtils
import me.anemoi.sbultimate.utils.RoundedShader
import me.anemoi.sbultimate.utils.Timer
import java.awt.Color


@SelfRegisterModule
object WaterMark : Module(
    "Water Mark",
    category = Category.RENDER,
    description = "A Cool Looking Water Mark. Thanks to ThunderHack+ for the idea."
) {

    var i = 0
    var timer: Timer = Timer()

    val color1: Color by ColorSetting("Color", Color(255, 255, 255, 255), true)
    val color2: Color by ColorSetting("Color", Color(16, 16, 16, 255), true)
    val shadowColor: Color by ColorSetting("Color", Color(16, 16, 16, 255), true)


    @RegisterHudElement
    object WaterMarkElement : HudElement(
        this,
        0,
        150,
        260,
        14
    ) {
        override fun renderHud() {

            RenderUtils.drawBlurredShadow(
                4F,
                4F,
                FontUtil.getStringWidth(
                    "ThunderHack" + "  |  " + mc.thePlayer.name + "  |  " + -1 + " ms  |  " + if (mc.currentServerData == null) "SinglePlayer" else mc.currentServerData.serverIP
                ) + 18F,
                12F,
                10,
                shadowColor
            )
            RoundedShader.drawRound(
                4F,
                4F,
                FontUtil.getStringWidth(
                    "ThunderHack" + "  |  " + mc.thePlayer.name + "  |  " + -1 + " ms  |  " + if (mc.currentServerData == null) "SinglePlayer" else mc.currentServerData.serverIP
                ) + 18F,
                13F,
                2f,
                color2
            )

            if (timer.passedMs(350)) {
                ++i
                timer.reset()
            }

            if (i == 24) {
                i = 0
            }

            val w1 = "_"
            val w2 = "F_"
            val w3 = "Fl_"
            val w4 = "Flo_"
            val w5 = "Flop_"
            val w6 = "Flopp_"
            val w7 = "Floppa_"
            val w8 = "FloppaC_"
            val w9 = "FloppaCl_"
            val w10 = "FloppaCli_"
            val w11 = "FloppaClie_"
            val w12 = "FloppaClien_"
            val w13 = "FloppaClient"
            val w14 = "FloppaClien_"
            val w15 = "FloppaClie_"
            val w16 = "FloppaCli_"
            val w17 = "FloppaCl_"
            val w18 = "FloppaC_"
            val w19 = "Floppa_"
            val w20 = "Flopp_"
            val w21 = "Flop_"
            val w22 = "Flo_"
            val w23 = "Fl_"
            val w24 = "F_"
            val w25 = "_"
            var text = ""

            if (i == 0) {
                text = w1
            }
            if (i == 1) {
                text = w2
            }
            if (i == 2) {
                text = w3
            }
            if (i == 3) {
                text = w4
            }
            if (i == 4) {
                text = w5
            }
            if (i == 5) {
                text = w6
            }
            if (i == 6) {
                text = w7
            }
            if (i == 7) {
                text = w8
            }
            if (i == 8) {
                text = w9
            }
            if (i == 9) {
                text = w10
            }
            if (i == 10) {
                text = w11
            }
            if (i == 11) {
                text = w12
            }
            if (i == 12) {
                text = w13
            }
            if (i == 13) {
                text = w14
            }
            if (i == 14) {
                text = w15
            }
            if (i == 15) {
                text = w16
            }
            if (i == 16) {
                text = w17
            }
            if (i == 17) {
                text = w18
            }
            if (i == 18) {
                text = w19
            }
            if (i == 19) {
                text = w20
            }
            if (i == 20) {
                text = w21
            }
            if (i == 21) {
                text = w22
            }
            if (i == 22) {
                text = w23
            }
            if (i == 23) {
                text = w24
            }
            if (i == 23) {
                text = w25
            }


            FontUtil.drawString(text, 9, 7, color1.rgb)
            FontUtil.drawString(
                "  |  " + mc.thePlayer.name + "  |  " + -1 + " ms  |  " + if (mc.currentServerData == null) "SinglePlayer" else mc.currentServerData.serverIP,
                FontUtil.getStringWidth("ThunderHack") + 5,
                7,
                color1.rgb
            )


        }

    }

}