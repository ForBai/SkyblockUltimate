package me.anemoi.sbutlimate.modules;

import floppaclient.FloppaClient;
import floppaclient.module.Category;
import floppaclient.module.Module;
import floppaclient.module.SelfRegisterModule;
import floppaclient.module.settings.Visibility;
import floppaclient.module.settings.impl.BooleanSetting;
import floppaclient.module.settings.impl.ColorSetting;
import floppaclient.module.settings.impl.NumberSetting;
import floppaclient.module.settings.impl.StringSetting;
import floppaclient.utils.ChatUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@SelfRegisterModule
public class MainModule extends Module {
    @NotNull
    public static final MainModule INSTANCE = new MainModule();

    public MainModule() {
        super("Skyblock Ultimate", Category.PLAYER, "Main toggle for Skyblock Ultimate");
    }

    private final BooleanSetting testSetting1 = register(new BooleanSetting("Funny Setting", false, Visibility.VISIBLE, "It does nothing"));
    private final ColorSetting testSetting2 = register(new ColorSetting("Funny Setting", new Color(255, 0, 0, 0), true, Visibility.VISIBLE, "It does nothing"));
    private final NumberSetting testSetting3 = register(new NumberSetting("Funny Setting", 5, 0, 10, 1, Visibility.VISIBLE, "It does nothing"));
    private final StringSetting testSetting5 = register(new StringSetting("Funny Setting", "ok why am i here", 100, Visibility.VISIBLE, "It does nothing"));


    @Override
    public void onDisable() {
        ChatUtils.INSTANCE.modMessage("Fuck you, you should not disable this!", true);
        this.toggle();
    }

    @Override
    public void onEnable() {
        System.out.println("Funne, ha ha");
    }

    @Override
    public void onInitialize() {
        System.out.println("Initialized main module of skyblock ultimate!!!!!");
    }
}
