package me.anemoi.sbutlimate.modules


import floppaclient.FloppaClient.Companion.mc
import floppaclient.events.ReceivePacketEvent
import floppaclient.module.Category
import floppaclient.module.Module
import floppaclient.module.SelfRegisterModule
import floppaclient.module.settings.impl.BooleanSetting
import floppaclient.module.settings.impl.ColorSetting
import floppaclient.module.settings.impl.NumberSetting
import floppaclient.module.settings.impl.StringSelectorSetting
import me.anemoi.sbultimate.utils.RenderUtils
import me.anemoi.sbultimate.utils.RenderUtils.injectAlpha
import me.anemoi.sbultimate.utils.TimeAnimation
import me.anemoi.sbultimate.utils.Trace
import me.anemoi.sbultimate.utils.Trace.TracePos
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityEnderPearl
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.network.play.server.S0EPacketSpawnObject
import net.minecraft.network.play.server.S13PacketDestroyEntities
import net.minecraft.util.MathHelper
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import java.awt.Color
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer
import kotlin.math.atan2


@SelfRegisterModule
object Predict : Module(
    "Predict",
    category = Category.RENDER,
    "Predicts various things"
) {

    val ORIGIN: Vec3 = Vec3(8.0, 64.0, 8.0)
    private val color: Color by ColorSetting("Color", Color(136, 0, 255, 255), true)
    private val color2: Color by ColorSetting("Color2", Color(136, 0, 255, 255), true)
    private val TriangleColor: Color by ColorSetting("TriangleColor", Color(136, 0, 255, 255), true)
    private val width2: Double by NumberSetting("Width", 1.6, 0.1, 10.0, 0.1)
    private val arrows: Boolean by BooleanSetting("Arrows", false)
    private val pearls: Boolean by BooleanSetting("Pearls", false)
    private val snowballs: Boolean by BooleanSetting("Snowballs", false)
    private val time: Double by NumberSetting("Time", 1.0, 1.0, 10.0, 1.0)
    var entAndTrail: MutableMap<Entity, MutableList<PredictedPosition>> = HashMap()
    var ids: MutableMap<Int, TimeAnimation> = ConcurrentHashMap()
    var traceLists: MutableMap<Int, MutableList<Trace>> = ConcurrentHashMap()
    var traces: MutableMap<Int, Trace> = ConcurrentHashMap()
    private val triangleESP: Boolean by BooleanSetting("TriangleESP", false)
    private val glow: Boolean by BooleanSetting("Glow", false)
    private val width: Double by NumberSetting("TracerHeight", 2.5, 0.1, 5.0, 0.1)
    private val radius: Double by NumberSetting("Radius", 50.0, -50.0, 50.0, 1.0)
    private val rad22ius: Double by NumberSetting("TracerDown", 3.0, 0.1, 20.0, 0.1)
    private val tracerA: Double by NumberSetting("TracerWidth", 0.5, 0.0, 8.0, 0.1)
    private val glowe: Double by NumberSetting("GlowRadius", 10.0, 1.0, 20.0, 1.0)
    private val glowa: Double by NumberSetting("GlowAlpha", 150.0, 0.0, 255.0, 5.0)
    private val mode = StringSelectorSetting("LineMode", "NONE", arrayListOf("NONE", "Mode1", "Mode2", "Both"))

    init {
        addSettings(
            mode
        )
    }

    @SubscribeEvent
    fun onRender3D(event: RenderWorldLastEvent?) {
        val modeS: String = this.mode.selected
        if (modeS.equals("Mode2", true)) {
            PlayerToPearl(event)
        } else if (modeS.equals("Mode1", true)) {
            PearlToDest(event)
        } else if (modeS.equals("Both", true)) {
            PlayerToPearl(event)
            PearlToDest(event)
        }
    }


    @SubscribeEvent
    fun onRender2D(event: RenderGameOverlayEvent.Post?) {
        if (!this.triangleESP) {
            return
        }
        val sr = ScaledResolution(mc)
        for (entity in mc.theWorld.loadedEntityList) {
            if (entity != null) {
                if (entity !is EntityEnderPearl) {
                    continue
                }
                val xOffset: Float = sr.scaledWidth / 2f
                val yOffset: Float = sr.scaledHeight / 2f
                GlStateManager.pushMatrix()
                val yaw: Float = getRotations(entity, event!!.partialTicks) - mc.thePlayer.rotationYaw
                glTranslatef(xOffset, yOffset, 0.0f)
                glRotatef(yaw, 0.0f, 0.0f, 1.0f)
                glTranslatef(-xOffset, -yOffset, 0.0f)
                drawTriangle(
                    xOffset,
                    (yOffset - radius).toFloat(),
                    (width * 5f).toFloat(),
                    TriangleColor.rgb
                )
                glTranslatef(xOffset, yOffset, 0.0f)
                glRotatef(-yaw, 0.0f, 0.0f, 1.0f)
                glTranslatef(-xOffset, -yOffset, 0.0f)
                glColor4f(1f, 1f, 1f, 1f)
                GlStateManager.popMatrix()
            }
        }
    }

    fun getRotations(entity: Entity, partTicks: Float): Float {
        val x: Double = interp(entity.posX, entity.lastTickPosX, partTicks) - interp(
            mc.thePlayer.posX,
            mc.thePlayer.lastTickPosX,
            partTicks
        )
        val z: Double = interp(entity.posZ, entity.lastTickPosZ, partTicks) - interp(
            mc.thePlayer.posZ,
            mc.thePlayer.lastTickPosZ,
            partTicks
        )
        return -(atan2(x, z) * (180 / Math.PI)).toFloat()
    }

    fun interp(d: Double, d2: Double, partTicks: Float): Double {
        return d2 + (d - d2) * partTicks.toDouble()
    }

    override fun onEnable() {
        ids = ConcurrentHashMap<Int, TimeAnimation>()
        traces = ConcurrentHashMap<Int, Trace>()
        traceLists = ConcurrentHashMap<Int, MutableList<Trace>>()
        super.onEnable()
    }


    @SubscribeEvent
    fun onPacketReceive(event: ReceivePacketEvent) {
        if (event.packet is S13PacketDestroyEntities) {
            for (id in (event.packet as S13PacketDestroyEntities).entityIDs) {
                if (ids.containsKey(id)) {
                    ids[id]!!.play()
                }
            }
        }
        if (event.packet is S0EPacketSpawnObject) {
            if (pearls && (event.packet as S0EPacketSpawnObject).type === 65 || arrows && (event.packet as S0EPacketSpawnObject).type === 60 || snowballs && (event.packet as S0EPacketSpawnObject).type === 61) {
                val animation =
                    TimeAnimation(
                        (time * 1000).toLong(),
                        0.0,
                        color.alpha.toDouble(),
                        false,
                        TimeAnimation.AnimationMode.LINEAR
                    )
                animation.stop()
                ids[(event.packet as S0EPacketSpawnObject).entityID] = animation
                traceLists[(event.packet as S0EPacketSpawnObject).entityID] = ArrayList()
                try {
                    traces[(event.packet as S0EPacketSpawnObject).entityID] = Trace(
                        0,
                        null,
                        //mc.theWorld.provider.getDimensionType(),
                        Vec3(
                            (event.packet as S0EPacketSpawnObject).x.toDouble(),
                            (event.packet as S0EPacketSpawnObject).y.toDouble(),
                            (event.packet as S0EPacketSpawnObject).z.toDouble()
                        ),
                        ArrayList()
                    )
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    }

    fun PlayerToPearl(event: RenderWorldLastEvent?) {
        if (mc.theWorld == null || mc.thePlayer == null) return
        for (entry in traceLists.entries) {
            glPushAttrib(GL11.GL_ALL_ATTRIB_BITS)
            GL11.glPushMatrix()
            GL11.glDisable(GL11.GL_ALPHA_TEST)
            glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            GL11.glDepthMask(false)
            glEnable(GL11.GL_CULL_FACE)
            glEnable(GL11.GL_LINE_SMOOTH)
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_FASTEST)
            GL11.glDisable(GL11.GL_LIGHTING)
            GL11.glLineWidth(width2.toFloat())
            val animation: TimeAnimation? = ids[entry.key]
            animation!!.add(event!!.partialTicks)
            glColor4f(
                color.red.toFloat(),
                color.green.toFloat(),
                color.blue.toFloat(),
                MathHelper.clamp_float((color.alpha - animation.current / 255F).toFloat(), 0F, 255F)
            )
            entry.value.forEach { trace ->
                GL11.glBegin(GL11.GL_LINE_STRIP)
                trace.trace.forEach { tracePos: TracePos -> renderVec(tracePos) }
                GL11.glEnd()
            }
            glColor4f(
                color.red.toFloat(),
                color.green.toFloat(),
                color.blue.toFloat(),
                MathHelper.clamp_float((color.alpha - animation.current / 255.0f).toFloat(), 0F, 255F)
            )
            GL11.glBegin(GL11.GL_LINE_STRIP)
            val trace = traces[entry.key]
            trace?.trace?.forEach(Consumer { tracePos: TracePos ->
                renderVec(
                    tracePos
                )
            })
            GL11.glEnd()
            glEnable(GL11.GL_LIGHTING)
            GL11.glDisable(GL11.GL_LINE_SMOOTH)
            glEnable(GL11.GL_TEXTURE_2D)
            glEnable(GL11.GL_DEPTH_TEST)
            GL11.glDisable(GL11.GL_BLEND)
            glEnable(GL11.GL_ALPHA_TEST)
            GL11.glDepthMask(true)
            GL11.glCullFace(GL11.GL_BACK)
            GL11.glPopMatrix()
            GL11.glPopAttrib()
        }
    }

    private fun renderVec(tracePos: TracePos) {
        val x: Double = tracePos.pos.xCoord - mc.renderManager.viewerPosX
        val y: Double = tracePos.pos.yCoord - mc.renderManager.viewerPosY
        val z: Double = tracePos.pos.zCoord - mc.renderManager.viewerPosZ
        GL11.glVertex3d(x, y, z)
    }

    @SubscribeEvent
    fun onUpdate(event: TickEvent) {
        if (mc.theWorld == null) return
        if (ids.keys.isEmpty()) return
        for (id in ids.keys) {
            if (id == null) continue
            if (mc.theWorld.loadedEntityList == null) return
            if (mc.theWorld.loadedEntityList.isEmpty()) return
            var idTrace = traces[id]
            val entity: Entity = mc.theWorld.getEntityByID(id) ?: continue
            if (entity != null) {
                val vec = entity.positionVector ?: continue
                if (vec == ORIGIN) {
                    continue
                }
                if (!traces.containsKey(id) || idTrace == null) {
                    traces[id] = Trace(0, null, vec, ArrayList())
                    idTrace = traces[id]
                }
                var trace = idTrace!!.trace
                val vec3d: Vec3 = if (trace.isEmpty()) vec else trace[trace.size - 1].pos
                if (trace.isNotEmpty() && (vec.distanceTo(vec3d) > 100.0)) {
                    traceLists[id]?.add(idTrace)
                    trace = ArrayList()
                    traces[id] = Trace(traceLists[id]!!.size + 1, null, vec, ArrayList())
                }
                if (trace.isEmpty() || vec != vec3d) {
                    trace.add(TracePos(vec))
                }
            }
            val animation: TimeAnimation? = ids[id]
            if (entity is EntityArrow && (entity.onGround || entity.isCollided || !entity.isAirBorne)) {
                animation!!.play()
            }
            if (animation != null && color
                    .alpha - animation.current <= 0 /*animation.getCurrent() >= color.getAlpha()*/) {
                animation.stop()
                ids.remove(id)
                traceLists.remove(id)
                traces.remove(id)
            }
        }
    }

    fun drawTriangle(x: Float, y: Float, size: Float, color: Int) {
        val blend = GL11.glIsEnabled(GL_BLEND)
        glEnable(GL_BLEND)
        val depth = GL11.glIsEnabled(GL_DEPTH_TEST)
        glDisable(GL_DEPTH_TEST)
        GL11.glDisable(GL_TEXTURE_2D)
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glEnable(GL_LINE_SMOOTH)
        GL11.glPushMatrix()
        hexColor(color)
        GL11.glBegin(7)
        GL11.glVertex2d(x.toDouble(), y.toDouble())
        GL11.glVertex2d(x - size * tracerA, (y + size).toDouble())
        GL11.glVertex2d(x.toDouble(), y + size - rad22ius)
        GL11.glVertex2d(x.toDouble(), y.toDouble())
        GL11.glEnd()
        hexColor(darker(Color(color), 0.8f)!!.rgb)
        GL11.glBegin(7)
        GL11.glVertex2d(x.toDouble(), y.toDouble()) //top
        GL11.glVertex2d(x.toDouble(), y + size - rad22ius) //midle
        GL11.glVertex2d(x + size * tracerA, (y + size).toDouble()) // left right
        GL11.glVertex2d(x.toDouble(), y.toDouble()) //top
        GL11.glEnd()
        hexColor(darker(Color(color), 0.6f)!!.rgb)
        GL11.glBegin(7)
        GL11.glVertex2d(x - size * tracerA, (y + size).toDouble())
        GL11.glVertex2d(x + size * tracerA, (y + size).toDouble()) // left right
        GL11.glVertex2d(x.toDouble(), y + size - rad22ius) //midle
        GL11.glVertex2d(x - size * tracerA, (y + size).toDouble())
        GL11.glEnd()
        GL11.glPopMatrix()
        glEnable(GL_TEXTURE_2D)
        if (!blend) GL11.glDisable(GL_BLEND)
        GL11.glDisable(GL_LINE_SMOOTH)
        if (glow) RenderUtils.drawBlurredShadow2(
            (x - size * tracerA).toFloat(),
            y,
            (x + size * tracerA - (x - size * tracerA)).toFloat(),
            size,
            glowe.toInt(),
            injectAlpha(
                Color(color), glowa.toInt()
            )
        )
        if (depth) glEnable(GL_DEPTH_TEST)
    }

    fun darker(color: Color, FACTOR: Float): Color? {
        return Color(
            (color.red * FACTOR).toInt().coerceAtLeast(0),
            (color.green * FACTOR).toInt().coerceAtLeast(0),
            (color.blue * FACTOR).toInt().coerceAtLeast(0),
            color.alpha
        )
    }

    private fun hexColor(hexColor: Int) {
        val red = (hexColor shr 16 and 0xFF).toFloat() / 255.0f
        val green = (hexColor shr 8 and 0xFF).toFloat() / 255.0f
        val blue = (hexColor and 0xFF).toFloat() / 255.0f
        val alpha = (hexColor shr 24 and 0xFF).toFloat() / 255.0f
        glColor4f(red, green, blue, alpha)
    }

    fun draw(list: MutableList<PredictedPosition>?, entity: Entity, partTicks: Float) {
        var first = true
        val depth = GL11.glIsEnabled(GL_DEPTH_TEST)
        val texture = GL11.glIsEnabled(GL_TEXTURE_2D)
        GL11.glPushMatrix()
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        glColor4f(
            color2.red / 255f,
            color2.green / 255f,
            color2.blue / 255f,
            color2.alpha / 255f
        )
        glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glLineWidth(0.5f)
        GL11.glBegin(GL11.GL_LINE_STRIP)
        for (i in 0 until list!!.size) {
            val pp: PredictedPosition = list!![i]
            var v = Vec3(pp.pos!!.xCoord, pp.pos!!.yCoord, pp.pos!!.zCoord)
            if (list.size > 2 && first) {
                val next: PredictedPosition = list[i + 1]
                v = v.add(
                    (next.pos!!.xCoord - v.xCoord) * partTicks,
                    (next.pos!!.yCoord - v.yCoord) * partTicks,
                    (next.pos!!.zCoord - v.zCoord) * partTicks
                )!!
            }
            GL11.glVertex3d(
                v.xCoord - mc.renderManager.viewerPosX,
                v.yCoord - mc.renderManager.viewerPosY,
                v.zCoord - mc.renderManager.viewerPosZ
            )
            first = false
        }
        list.removeIf { w -> w.tick < entity.ticksExisted }
        GL11.glEnd()
        if (depth) glEnable(GL11.GL_DEPTH_TEST)
        if (texture) glEnable(GL11.GL_TEXTURE_2D)
        GL11.glPopMatrix()
    }

    fun PearlToDest(event: RenderWorldLastEvent?) {
        for (entity in mc.theWorld.loadedEntityList) {
            if (entity is EntityEnderPearl) {
                if (entAndTrail[entity] != null) {
                    draw(entAndTrail[entity], entity, event!!.partialTicks)
                }
            }
            if (entity is EntityArrow) {
                if (entAndTrail[entity] != null) {
                    draw(entAndTrail[entity], entity, event!!.partialTicks)
                }
            }
        }
    }


    class PredictedPosition {
        var color: Color? = null
        var pos: Vec3? = null
        var tick = 0
    }
}

private fun Vec3.add(d: Double, d1: Double, d2: Double): Vec3? {
    return Vec3(xCoord + d, yCoord + d1, zCoord + d2)
}
