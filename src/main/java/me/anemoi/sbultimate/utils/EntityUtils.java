package me.anemoi.sbultimate.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vector3d;

public class EntityUtils {
    public static Vector3d interpolateEntity(Entity entity, float time) {
        Vector3d vector3d = new Vector3d();
        vector3d.x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) time;
        vector3d.y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) time;
        vector3d.z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) time;
        return vector3d;
    }
}
