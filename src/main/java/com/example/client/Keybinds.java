package com.example.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    public static KeyBinding TOGGLE_GUI;

    public static void registerKeybinds() {
        TOGGLE_GUI = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.v9sndlc.toggle_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                "category.v9sndlc"
        ));
    }
}
