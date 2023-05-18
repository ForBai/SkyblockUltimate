/*
 * Thanks to Pan4ur for the original code from his client, ThunderHackPlus(https://github.com/Pan4ur/ThunderHackPlus)
 * Modified and converted to kotlin by Anemoi/ForBai for use in SBUltimate
 */
package me.anemoi.sbutlimate.modules

import floppaclient.FloppaClient.Companion.mc
import floppaclient.module.Category
import floppaclient.module.Module
import floppaclient.module.SelfRegisterModule
import floppaclient.module.impl.dungeon.DungeonKillAura
import floppaclient.module.settings.impl.ColorSetting
import floppaclient.module.settings.impl.StringSelectorSetting
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

@SelfRegisterModule
object JumpCircles : Module(
    "Jump Circles",
    category = Category.RENDER,
    description = "I like Circles"
) {
    const val TYPE = 0
    const val MAX_JC_TIME: Byte = 20
    var circles = mutableListOf<Circle>()
    private val jumpcircleMode = StringSelectorSetting("JumpCircle Mode", "Default", arrayListOf("Default", "Disc"))
    private val jumpCircleColor: Color by ColorSetting("JumpCircle Color", Color(255, 255, 255, 255))

    init {
        this.addSettings(
            jumpcircleMode
        )
    }

    @SubscribeEvent
    fun onJump(event: TickEvent) {
        if (mc.thePlayer.motionY === 0.33319999363422365) {
            //if (!mc.thePlayer.o()) {
            handleEntityJump(mc.thePlayer)
            //}
        }
        onLocalPlayerUpdate(mc.thePlayer)
    }

    @SubscribeEvent
    fun onRender(event: RenderWorldLastEvent) {
        val mode: String = this.jumpcircleMode.selected
        Minecraft.getMinecraft()
        val client: EntityPlayerSP = mc.thePlayer
        val mc = Minecraft.getMinecraft()
        val ix = -(client.lastTickPosX + (client.posX - client.lastTickPosX) * event.partialTicks.toDouble())
        val iy = -(client.lastTickPosY + (client.posY - client.lastTickPosY) * event.partialTicks.toDouble())
        val iz = -(client.lastTickPosZ + (client.posZ - client.lastTickPosZ) * event.partialTicks.toDouble())
        if (mode.equals("Disc", ignoreCase = true)) {
            GL11.glPushMatrix()
            GL11.glTranslated(ix, iy, iz)
            GL11.glDisable(2884)
            GL11.glEnable(3042)
            GL11.glDisable(3553)
            GL11.glDisable(3008)
            GL11.glDisable(2929)
            GL11.glBlendFunc(770, 771)
            GL11.glShadeModel(7425)
            circles.reverse()
            try {
                for (c in circles) {
                    val k = c.existed.toFloat() / 20.0f
                    val x: Double = c.position().xCoord
                    val y: Double = c.position().yCoord - k.toDouble() * 0.5
                    val z: Double = c.position().zCoord
                    val end = k + 1.0f - k
                    GL11.glBegin(8)
                    var i = 0
                    while (i <= 360) {
                        GL11.glColor4f(
                            c.color().xCoord.toFloat(),
                            c.color().yCoord.toFloat(),
                            c.color().zCoord.toFloat(),
                            (0.2f * (1.0f - c.existed.toFloat() / 20.0f)) as Float
                        )
                        GL11.glVertex3d(
                            (x + cos(Math.toRadians((i * 4).toDouble())) * k.toDouble()) as Double,
                            y, (z + sin(
                                Math.toRadians((i * 4).toDouble())
                            ) * k.toDouble()) as Double
                        )
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, (0.01f * (1.0f - c.existed.toFloat() / 20.0f)) as Float)
                        GL11.glVertex3d(
                            (x + cos(Math.toRadians(i.toDouble())) * end.toDouble()) as Double,
                            (y + sin((k * 8.0f).toDouble()) * 0.5),
                            (z + sin(Math.toRadians(i.toDouble()) * end.toDouble()))
                        )
                        i += 5
                    }
                    GL11.glEnd()
                }
            } catch (exception: Exception) {
                // empty catch block
            }
            Collections.reverse(circles)
            GL11.glEnable(3553)
            GL11.glDisable(3042)
            GL11.glShadeModel(7424)
            GL11.glEnable(2884)
            GL11.glEnable(2929)
            GL11.glEnable(3008)
            GlStateManager.resetColor()
            GL11.glPopMatrix()
        } else if (mode.equals("Default", ignoreCase = true)) {
            GL11.glPushMatrix()
            GL11.glTranslated(ix, iy, iz)
            GL11.glDisable(2884)
            GL11.glEnable(3042)
            GL11.glDisable(3553)
            GL11.glDisable(3008)
            GL11.glBlendFunc(770, 771)
            GL11.glShadeModel(7425)
            circles.reverse()
            for (c in circles) {
                val red = ((jumpCircleColor.rgb shr 16 and 0xFF).toFloat() / 100.0f).toInt()
                val green = ((jumpCircleColor.rgb shr 8 and 0xFF).toFloat() / 100.0f).toInt()
                val blue = ((jumpCircleColor.rgb and 0xFF).toFloat() / 100.0f).toInt()
                val x: Double = c.position().xCoord
                val y: Double = c.position().yCoord
                val z: Double = c.position().zCoord
                val k = c.existed.toFloat() / 20.0f
                val start = k * 1.5f
                val end = start + 0.5f - k
                GL11.glBegin(8)
                var i = 0
                block13@ while (i <= 360) {
                    GL11.glColor4f(
                        c.color().xCoord.toFloat(),
                        c.color().yCoord.toFloat(),
                        c.color().zCoord.toFloat(),
                        (0.7f * (1.0f - c.existed.toFloat() / 20.0f)) as Float
                    )
                    when (0) {
                        0 -> {
                            GL11.glVertex3d(
                                (x + cos(Math.toRadians(i.toDouble())) * start.toDouble()) as Double,
                                y, (z + sin(Math.toRadians(i.toDouble())) * start.toDouble()) as Double
                            )
                        }
                        1 -> {
                            GL11.glVertex3d(
                                (x + cos(Math.toRadians((i * 2).toDouble())) * start.toDouble()) as Double,
                                y, (z + sin(Math.toRadians((i * 2).toDouble())) * start.toDouble()) as Double
                            )
                        }
                    }
                    GL11.glColor4f(
                        red.toFloat(),
                        green.toFloat(),
                        blue.toFloat(),
                        (0.01f * (1.0f - c.existed.toFloat() / 20.0f)) as Float
                    )
                    when (0) {
                        0 -> {
                            GL11.glVertex3d(
                                (x + cos(Math.toRadians(i.toDouble())) * end.toDouble()) as Double,
                                y, (z + sin(Math.toRadians(i.toDouble())) * end.toDouble()) as Double
                            )
                            i += 5
                            continue@block13
                        }
                        1 -> {
                            GL11.glVertex3d(
                                (x + cos(Math.toRadians(-i.toDouble())) * end.toDouble()) as Double,
                                y, (z + sin(Math.toRadians(-i.toDouble())) * end.toDouble()) as Double
                            )
                        }
                    }
                    i += 5
                }
                GL11.glEnd()
            }
            circles.reverse()
            GL11.glEnable(3553)
            GL11.glDisable(3042)
            GL11.glShadeModel(7424)
            GL11.glEnable(2884)
            GL11.glEnable(3008)
            GlStateManager.resetColor()
            GL11.glPopMatrix()
        }
    }

    fun onLocalPlayerUpdate(instance: EntityPlayerSP?) {
        circles.removeIf { obj: Circle -> obj.update() }
    }

    fun handleEntityJump(entity: Entity) {
        val red = ((jumpCircleColor.rgb shr 16 and 0xFF).toFloat() / 100.0f).toInt()
        val green = ((jumpCircleColor.rgb shr 8 and 0xFF).toFloat() / 100.0f).toInt()
        val blue = ((jumpCircleColor.rgb and 0xFF).toFloat() / 100.0f).toInt()
        val color: Vec3 = Vec3(red.toDouble(), green.toDouble(), blue.toDouble())
        circles.add(Circle(entity.positionVector, color))
    }

    class Circle(vec: Vec3, color: Vec3) {
        private val vec: Vec3
        private val color: Vec3
        var existed: Byte = 0

        init {
            this.vec = vec
            this.color = color
        }

        fun position(): Vec3 {
            return vec
        }

        fun color(): Vec3 {
            return color
        }

        fun update(): Boolean {
            existed = (existed + 1).toByte()
            return existed > 20
        }
    }
}