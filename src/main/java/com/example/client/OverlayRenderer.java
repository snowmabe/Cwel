package com.example.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class OverlayRenderer {
    public static boolean enabled = false;

    public static void register() {
        WorldRenderEvents.LAST.register(context -> {
            if (!enabled) return;
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.player == null || client.world == null) return;

            Camera camera = client.gameRenderer.getCamera();
            float tickDelta = context.tickDelta();
            MatrixStack matrices = context.matrixStack();

            for (PlayerEntity p : client.world.getPlayers()) {
                if (p == client.player) continue;

                double px = p.prevX + (p.getX() - p.prevX) * tickDelta;
                double py = p.prevY + (p.getY() - p.prevY) * tickDelta;
                double pz = p.prevZ + (p.getZ() - p.prevZ) * tickDelta;

                Vec3d camPos = camera.getPos();
                double x = px - camPos.x;
                double y = py - camPos.y + p.getHeight() + 0.5;
                double z = pz - camPos.z;

                matrices.push();
                matrices.translate(x, y, z);
                matrices.multiply(client.getEntityRenderDispatcher().getRotation());

                float scale = 0.025f;
                matrices.scale(-scale, -scale, scale);

                String name = p.getEntityName();
                int hp = Math.max(0, Math.min(20, Math.round(p.getHealth())));

                ItemStack main = p.getMainHandStack();
                ItemStack off = p.getOffHandStack();
                String mainName = main.isEmpty() ? "empty" : main.getName().getString();
                String offName = off.isEmpty() ? "empty" : off.getName().getString();

                String text = String.format("%s HP:%d | %s | %s", name, hp, truncate(mainName, 24), truncate(offName, 24));

                // draw centered
                int textWidth = client.textRenderer.getWidth(Text.literal(text));
                client.textRenderer.draw(matrices, Text.literal(text), -textWidth / 2f, 0, 0xFFFFFF);

                matrices.pop();
            }
        });
    }

    private static String truncate(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max - 1) + "…";
    }
}
