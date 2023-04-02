package me.anemoi.sbultimate.utils;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import static floppaclient.FloppaClient.mc;

public class PrivateGetter {
    public static double getRenderPosX() {
        //get the private field renderPosX by using reflection
        RenderManager renderManager = mc.getRenderManager();
        return ReflectionHelper.getPrivateValue(RenderManager.class, renderManager, "renderPosX");
    }

    public static double getRenderPosY() {
        //get the private field renderPosY by using reflection
        RenderManager renderManager = mc.getRenderManager();
        return ReflectionHelper.getPrivateValue(RenderManager.class, renderManager, "renderPosY");
    }

    public static double getRenderPosZ() {
        //get the private field renderPosZ by using reflection
        RenderManager renderManager = mc.getRenderManager();
        return ReflectionHelper.getPrivateValue(RenderManager.class, renderManager, "renderPosZ");
    }

}
