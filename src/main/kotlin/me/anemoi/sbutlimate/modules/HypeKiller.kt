package me.anemoi.sbutlimate.modules

import floppaclient.FloppaClient.Companion.mc
import floppaclient.events.PositionUpdateEvent
import floppaclient.module.Category
import floppaclient.module.Module
import floppaclient.module.SelfRegisterModule
import floppaclient.module.settings.Visibility
import floppaclient.module.settings.impl.BooleanSetting
import floppaclient.module.settings.impl.ColorSetting
import floppaclient.module.settings.impl.NumberSetting
import floppaclient.module.settings.impl.StringSetting
import floppaclient.utils.Utils.containsOneOf
import floppaclient.utils.fakeactions.FakeActionUtils
import floppaclient.utils.inventory.SkyblockItem
import gg.essential.universal.ChatColor
import net.minecraft.entity.item.EntityArmorStand
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color


@SelfRegisterModule
object HypeKiller : Module(
    "Hype Killer",
    category = Category.PLAYER,
    description = "Kills all hype"
) {
    private val radius: Double by NumberSetting(
        "Radius",
        5.0,
        0.2,
        7.0,
        0.1,
        Visibility.VISIBLE,
        "Radius of the hype aura"
    )
    private val customMobs: Boolean by BooleanSetting(
        "Custom Mobs",
        false,
        Visibility.VISIBLE,
        "If enabled, only kills custom mobs"
    )
    private val customMobsString: String by StringSetting(
        "Custom Mobs",
        "",
        64,
        Visibility.VISIBLE,
        "Custom mobs to kill, separated by commas"
    )
    private val hypeDelay: Double by NumberSetting(
        "Hype Delay",
        5.0,
        1.0,
        20.0,
        1.0,
        Visibility.VISIBLE,
        "Delay between clicks in ticks"
    )
    private val renderAsSphere: Boolean by BooleanSetting(
        "Render as Sphere",
        false,
        Visibility.VISIBLE,
        "Renders the aura as a sphere"
    )
    private val renderRadius: Boolean by BooleanSetting(
        "Render Radius",
        false,
        Visibility.VISIBLE,
        "Renders the radius of the aura"
    )

    private val radiusColor: Color by ColorSetting(
        "Radius Color",
        Color(255, 0, 0, 255),
        true,
        Visibility.VISIBLE,
        "Color of the radius"
    )

    private var lastClickTime = 0L

    private val armorstandEntitiesInRadius = mutableListOf<EntityArmorStand>()

    @SubscribeEvent
    fun preMove(event: PositionUpdateEvent.Pre) {
        if (mc.thePlayer == null || mc.theWorld == null) return
        updateArmorstandEntitiesInRadius()
        if (armorstandEntitiesInRadius.isNotEmpty()) {
            if (lastClickTime >= hypeDelay) {
                //check if player has hype if yes click with it
                if (FakeActionUtils.useItem(SkyblockItem.HYPERION, swapBack = false, fromInv = false)) {
                    lastClickTime = 0
                }
            }
        }
        lastClickTime++
    }

    private fun updateArmorstandEntitiesInRadius() {
        armorstandEntitiesInRadius.clear()
        mc.theWorld.loadedEntityList.forEach {
            if (it is EntityArmorStand) {
                if (it.getDistanceToEntity(mc.thePlayer) <= radius) {
                    if (customMobs) {
                        val customMobs = customMobsString.split(",")
                        if (ChatColor.stripControlCodes(it.displayName.unformattedText)!!.containsOneOf(customMobs)) {
                            armorstandEntitiesInRadius.add(it)
                        }
                    } else if (ChatColor.stripControlCodes(it.displayName.unformattedText)!!
                            .contains(Regex("❤")) && !customMobs
                    ) {
                        armorstandEntitiesInRadius.add(it)
                    }
                }
            }
        }
    }


}