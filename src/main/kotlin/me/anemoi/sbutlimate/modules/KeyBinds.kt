/*
 * Thanks to Pan4ur for the original code from his client, ThunderHackPlus(https://github.com/Pan4ur/ThunderHackPlus)
 * Modified and converted to kotlin by Anemoi/ForBai for use in SBUltimate
 */
package me.anemoi.sbutlimate.modules

import floppaclient.FloppaClient.Companion.mc
import floppaclient.module.*
import floppaclient.module.settings.impl.BooleanSetting
import floppaclient.module.settings.impl.ColorSetting
import floppaclient.module.settings.impl.NumberSetting
import floppaclient.ui.clickgui.util.FontUtil
import floppaclient.ui.hud.EditHudGUI
import floppaclient.ui.hud.HudElement
import me.anemoi.sbultimate.utils.RenderUtils
import me.anemoi.sbultimate.utils.RoundedShader
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
import java.awt.Color

@SelfRegisterModule
object KeyBinds : Module(
    "KeyBinds",
    Category.RENDER,
    description = "Keybinds for Skyblock Ultimate",
) {


    val actAsArrayList: Boolean by BooleanSetting("ActAsArrayList", true)

    val shadowColor: Color by ColorSetting("ShadowColor", Color(16, 16, 16, 255), true)
    val color2: Color by ColorSetting("Color", Color(16, 16, 16, 255), true)
    val color3: Color by ColorSetting("Color2", Color(197, 155, 155, 155), true)
    val textColor: Color by ColorSetting("TextColor", Color(190, 190, 190, 255), true)
    val oncolor: Color by ColorSetting("OnColor", Color(190, 190, 190, 255), true)
    val offcolor: Color by ColorSetting("OffColor", Color(100, 100, 100, 255), true)
    val psize: Double by NumberSetting("Size", 1.0, 0.1, 2.0, 0.1)

    var posX: Double by NumberSetting("X", 0.0, -1000.0, 1000.0, 1.0)
    var posY: Double by NumberSetting("Y", 0.0, -1000.0, 1000.0, 1.0)


    var x1 = 0f
    var y1 = 0f

    @RegisterHudElement
    object KeyBindsElement : HudElement(
        this,
        0,
        0,
        260,
        14
    ) {
        override fun renderHud() {
            y1 = posY.toFloat()
            x1 = posX.toFloat()


            var y_offset1 = 0
            var longest = 100
            for (feature in ModuleManager.modules) {
                val keyString = if (feature.keyCode > 0)
                    Keyboard.getKeyName(feature.keyCode) ?: "Err"
                else if (feature.keyCode < 0)
                    Mouse.getButtonName(feature.keyCode + 100)
                else
                    ".."

                if (actAsArrayList) {
                    if (feature.enabled) {
                        y_offset1 += 10
                        if (FontUtil.getStringWidth(feature.name) > longest) {
                            longest = FontUtil.getStringWidth(feature.name)
                        }
                    }
                } else if (keyString != ".." && (feature.name.lowercase() != "clickgui") && (feature.name.lowercase() != "skyblock ultimate" && feature.name.lowercase() != "add new key bind")) {
                    y_offset1 += 10
                }
            }

            GlStateManager.pushMatrix()
            size(x1 + 50.0, y1 + (20 + y_offset1) / 2.0, psize)

            RenderUtils.drawBlurredShadow(
                x1.toFloat(), y1.toFloat(),
                longest.toFloat(), (20 + y_offset1).toFloat(), 20, shadowColor
            )

            RoundedShader.drawRound(
                x1.toFloat(),
                y1.toFloat(),
                longest.toFloat(),
                (20 + y_offset1).toFloat(),
                7f,
                color2
            )
            FontUtil.drawString(if (actAsArrayList) "Modules" else "KeyBinds", x1 + 25.0, y1 + 5.0, textColor.rgb)
            RoundedShader.drawRound((x1 + 2).toFloat(), (y1 + 13).toFloat(), longest.toFloat() - 4, 1F, 0.5f, color3)

            val sortedListOfFeatures: List<Module> = if (actAsArrayList) {
                ModuleManager.modules.sortedBy { it.name.length }.reversed()
            } else {
                ModuleManager.modules
            }

            var y_offset = 0
            for (feature in sortedListOfFeatures) {
                if (actAsArrayList) {
                    if (feature.enabled) {
                        GlStateManager.pushMatrix()
                        GlStateManager.resetColor()
                        FontUtil.drawString(
                            feature.name,
                            x1 + 5.0,
                            y1 + 18.0 + y_offset,
                            if (feature.enabled) oncolor.rgb else offcolor.rgb
                        )
                        GlStateManager.resetColor()
                        GlStateManager.popMatrix()
                        y_offset += 10
                    }
                } else if (feature.keyCode != 0 && feature.name.lowercase() != "clickgui" && feature.name.lowercase() != "skyblock ultimate" && feature.name.lowercase() != "add new key bind") {
                    GlStateManager.pushMatrix()
                    GlStateManager.resetColor()
                    val keyString = if (feature.keyCode > 0)
                        Keyboard.getKeyName(feature.keyCode) ?: "Err"
                    else if (feature.keyCode < 0)
                        Mouse.getButtonName(feature.keyCode + 100)
                    else
                        ".."
                    FontUtil.drawString(
                        "[${keyString}]  ${feature.name}",
                        x1 + 5.0,
                        y1 + 18.0 + y_offset,
                        if (feature.enabled) oncolor.rgb else offcolor.rgb
                    )
                    GlStateManager.resetColor()
                    GlStateManager.popMatrix()
                    y_offset += 10
                }
            }

            GlStateManager.popMatrix()

            if (mc.currentScreen is GuiChat || mc.currentScreen is EditHudGUI) {
                if (isHovering()) {
                    if (Mouse.isButtonDown(0) && mousestate) {
                        posX = (((normaliseX() - dragX).toDouble()))
                        posY = (((normaliseY() - dragY).toDouble()))
                    }
                }
            }
            if (Mouse.isButtonDown(0) && isHovering()) {
                if (!mousestate) {
                    dragX = ((normaliseX() - (posX)).toInt())
                    dragY = ((normaliseY() - (posY)).toInt())
                }
                mousestate = true;
            } else {
                mousestate = false;
            }
        }
    }

    var dragX: Int = 0
    var dragY: Int = 0
    var mousestate = false
    fun normaliseX(): Int {
        return (Mouse.getX() / 2f).toInt()
    }

    fun normaliseY(): Int {
        val sr = ScaledResolution(mc)
        return (-Mouse.getY() + sr.getScaledHeight() + sr.getScaledHeight()) / 2
    }

    fun isHovering(): Boolean {
        return normaliseX() > x1 - 10 && normaliseX() < x1 + 100 && normaliseY() > y1 && normaliseY() < y1 + 100
    }

    fun size(width: Double, height: Double, animation: Double) {
        GL11.glTranslated(width, height, 0.0)
        GL11.glScaled(animation, animation, 1.0)
        GL11.glTranslated(-width, -height, 0.0)
    }

}