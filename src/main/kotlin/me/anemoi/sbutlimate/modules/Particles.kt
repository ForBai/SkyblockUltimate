package me.anemoi.sbutlimate.modules

import floppaclient.FloppaClient.Companion.mc
import floppaclient.module.Category
import floppaclient.module.Module
import floppaclient.module.SelfRegisterModule
import floppaclient.module.settings.impl.BooleanSetting
import floppaclient.module.settings.impl.ColorSetting
import floppaclient.module.settings.impl.NumberSetting
import me.anemoi.sbultimate.utils.PrivateGetter.*
import me.anemoi.sbultimate.utils.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.sqrt


@SelfRegisterModule
object Particles : Module(
    "Particles",
    category = Category.RENDER,
    description = "Cool Looking Particles"
) {

    private val colorLight: Color by ColorSetting("Color", Color(136, 0, 255, 155), true)
    private val colorLight2: Color by ColorSetting("Color2", Color(136, 0, 255, 155), true)
    private val colorLight3: Color by ColorSetting("Color3", Color(136, 0, 255, 155), true)
    private val colorLight4: Color by ColorSetting("Color4", Color(136, 0, 255, 155), true)

    private val selfp: Boolean by BooleanSetting("Self", false)
    private val speedor: Double by NumberSetting("Time", 8000.0, 1.0, 10000.0, 100.0)
    private val speedor2: Double by NumberSetting("Speed", 20.0, 1.0, 1000.0, 20.0)
    private val entities: Boolean by BooleanSetting("Entities", false)

    val particles = ArrayList<Particle>()

    @SubscribeEvent
    fun onUpdate(event: TickEvent) {
        if (mc.theWorld != null && mc.thePlayer != null) {
            for (player in mc.theWorld.playerEntities) {
                if (!selfp && player === mc.thePlayer) {
                    continue
                }
                if (player.hurtTime > 0) {
                    var col: Color? = null
                    when (random(0, 3)) {
                        0 -> {
                            col = colorLight
                        }
                        1 -> {
                            col = colorLight2
                        }
                        2 -> {
                            col = colorLight3
                        }
                        3 -> {
                            col = colorLight4
                        }
                    }
                    particles.add(
                        Particle(
                            random(-0.05f, 0.05f).toDouble(),
                            random(
                                (player.posY + player.height),
                                player.posY
                            ),
                            random(-0.05f, 0.05f).toDouble(),
                            col!!
                        )
                    )
                    particles.add(
                        Particle(
                            player.posX,
                            random(
                                (player.posY + player.height),
                                (player.posY + 0.1f)
                            ),
                            player.posZ,
                            col!!
                        )
                    )
                    particles.add(
                        Particle(
                            player.posX,
                            random(
                                (player.posY + player.height),
                                (player.posY + 0.1f)
                            ),
                            player.posZ,
                            col!!
                        )
                    )
                }
                for (i in particles.size - 1 downTo 0) {
                    if (System.currentTimeMillis() - particles[i].time >= speedor) {
                        particles.removeAt(i)
                    }
                }

            }
        }
    }

    @SubscribeEvent
    fun onRender3D(event: RenderWorldLastEvent?) {
        if (mc.thePlayer != null && mc.theWorld != null) {
            for (particle in particles) {
                particle.render()
            }

        }
    }

    fun random(min: Double, max: Double): Double {
        return Math.random() * (max - min) + min
    }

    fun random(min: Float, max: Float): Float {
        return (Math.random() * (max - min) + min).toFloat()
    }

    fun random(min: Int, max: Int): Int {
        return (Math.random() * (max - min) + min).toInt()
    }

    class Particle(var x: Double, var y: Double, var z: Double, color: Color) {
        var alpha = 180
        var motionX: Double
        var motionY: Double
        var motionZ: Double
        var time: Long
        var color: Color

        init {
            motionX = random(-speedor2 / 1000f, speedor2 / 1000f)
            motionY = random(-speedor2 / 1000f, speedor2 / 1000f)
            motionZ = random(-speedor2 / 1000f, speedor2 / 1000f)
            time = System.currentTimeMillis()
            this.color = color
        }

        fun update() {
            val yEx = 0.0
            val sp = sqrt(motionX * motionX + motionZ * motionZ) * 1
            x += motionX
            y += motionY
            if (posBlock(x, y, z)) {
                motionY = -motionY / 1.1
            } else {
                if (posBlock(x, y, z) ||
                    posBlock(x, y - yEx, z) ||
                    posBlock(x, y + yEx, z) ||
                    posBlock(x - sp, y, z - sp) ||
                    posBlock(x + sp, y, z + sp) ||
                    posBlock(x + sp, y, z - sp) ||
                    posBlock(x - sp, y, z + sp) ||
                    posBlock(x + sp, y, z) ||
                    posBlock(x - sp, y, z) ||
                    posBlock(x, y, z + sp) ||
                    posBlock(x, y, z - sp) ||
                    posBlock(x - sp, y - yEx, z - sp) ||
                    posBlock(x + sp, y - yEx, z + sp) ||
                    posBlock(x + sp, y - yEx, z - sp) ||
                    posBlock(x - sp, y - yEx, z + sp) ||
                    posBlock(x + sp, y - yEx, z) ||
                    posBlock(x - sp, y - yEx, z) ||
                    posBlock(x, y - yEx, z + sp) ||
                    posBlock(x, y - yEx, z - sp) ||
                    posBlock(x - sp, y + yEx, z - sp) ||
                    posBlock(x + sp, y + yEx, z + sp) ||
                    posBlock(x + sp, y + yEx, z - sp) ||
                    posBlock(x - sp, y + yEx, z + sp) ||
                    posBlock(x + sp, y + yEx, z) ||
                    posBlock(x - sp, y + yEx, z) ||
                    posBlock(x, y + yEx, z + sp) ||
                    posBlock(x, y + yEx, z - sp)
                ) {
                    motionX = -motionX + motionZ
                    motionZ = -motionZ + motionX
                }
            }
            z += motionZ
            motionX /= 1.005
            motionZ /= 1.005
            motionY /= 1.005
        }

        fun render() {
            color = RenderUtils.injectAlpha(color, alpha)
            update()
            alpha -= 0.1.toInt()
            val scale = 0.07f
            GlStateManager.enableDepth()
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glEnable(GL11.GL_LINE_SMOOTH)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            try {
                val posX: Double = x - getRenderPosX()
                val posY: Double = y - getRenderPosY()
                val posZ: Double = z - getRenderPosZ()
                val distanceFromPlayer: Double = mc.thePlayer.getDistance(x, y - 1, z)
                var quality = (distanceFromPlayer * 4 + 10).toInt()
                if (quality > 350) quality = 350
                GL11.glPushMatrix()
                GL11.glTranslated(posX, posY, posZ)
                GL11.glScalef(-scale, -scale, -scale)
                GL11.glRotated((-mc.renderManager.playerViewY).toDouble(), 0.0, 1.0, 0.0)
                GL11.glRotated(mc.renderManager.playerViewX.toDouble(), 1.0, 0.0, 0.0)
                RenderUtils.drawFilledCircleNoGL(0, 0, 0.7, color.hashCode(), quality)
                if (distanceFromPlayer < 4) RenderUtils.drawFilledCircleNoGL(
                    0,
                    0,
                    1.4,
                    Color(color.getRed(), color.getGreen(), color.getBlue(), 50).hashCode(),
                    quality
                )
                if (distanceFromPlayer < 20) RenderUtils.drawFilledCircleNoGL(
                    0,
                    0,
                    2.3,
                    Color(color.getRed(), color.getGreen(), color.getBlue(), 30).hashCode(),
                    quality
                )
                GL11.glScalef(0.8f, 0.8f, 0.8f)
                GL11.glPopMatrix()
            } catch (ignored: ConcurrentModificationException) {
            }
            GL11.glDisable(GL11.GL_LINE_SMOOTH)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glColor3d(255.0, 255.0, 255.0)
        }

        private fun posBlock(x: Double, y: Double, z: Double): Boolean {
            return mc.theWorld.getBlockState(BlockPos(x, y, z)).getBlock() !== Blocks.air && mc.theWorld.getBlockState(
                BlockPos(x, y, z)
            ).getBlock() !== Blocks.water && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.lava && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.bed && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.cake && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.tallgrass && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.flower_pot && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.red_flower && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.yellow_flower && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.sapling && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.vine && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.acacia_fence && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.acacia_fence_gate && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.birch_fence && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.birch_fence_gate && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.dark_oak_fence && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.dark_oak_fence_gate && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.jungle_fence && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.jungle_fence_gate && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.nether_brick_fence && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.oak_fence && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.oak_fence_gate && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.spruce_fence && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.spruce_fence_gate && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.enchanting_table && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.end_portal_frame && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.double_plant && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.standing_sign && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.wall_sign && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.skull && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.daylight_detector && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.daylight_detector_inverted && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.stone_slab && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.wooden_slab && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.carpet && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.deadbush && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.vine && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.redstone_wire && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.reeds && mc.theWorld.getBlockState(BlockPos(x, y, z))
                .getBlock() !== Blocks.snow_layer
        }
    }
}