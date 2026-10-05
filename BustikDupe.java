package ru.bustik.dupe;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class BustikDupe implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> register(dispatcher));
    }

    private static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("dupe")
            .executes(ctx -> dupe(ctx.getSource())));
    }

    private static int dupe(ServerCommandSource source) {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        ItemStack held = player.getMainHandStack();

        if (held.isEmpty()) {
            player.sendMessage(Text.literal("Возьми предмет в главную руку."), false);
            return 0;
        }

        ItemStack copy = held.copy();
        if (!player.getInventory().insertStack(copy)) {
            player.dropItem(copy, false);
        }

        player.sendMessage(Text.literal("Предмет продублирован."), false);
        return 1;
    }
}
