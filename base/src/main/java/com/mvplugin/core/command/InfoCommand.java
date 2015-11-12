package com.mvplugin.core.command;

import com.mvplugin.core.MultiverseWorld;
import com.mvplugin.core.exceptions.MultiverseException;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Perms;
import org.jetbrains.annotations.NotNull;
import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.command.CommandProvider;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;

import static com.mvplugin.core.util.Language.Command.Info.*;

@CommandInfo(
        primaryAlias = "info",
        desc = "Creates a new world.",
        usage = "[WORLD]",
        directlyPrefixedAliases = {"i", "info"},
        min = 0,
        max = 1
)
public class InfoCommand extends MultiverseCommand {
    protected InfoCommand(@NotNull final CommandProvider<MultiverseCore> plugin) {
        super(plugin);
    }

    @Override
    public Perm getPerm() {
        return Perms.CMD_INFO;
    }

    @NotNull
    @Override
    public Message getHelp() {
        return HELP;
    }

    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        try {
            MultiverseWorld world = getWorldFromContext(sender, context, 0);
            getMessager().message(sender, INFO,
                    world.getAmbientSpawnLimit(),
                    world.getWaterAnimalSpawnLimit(),
                    world.getMonsterSpawnLimit(),
                    world.getAnimalSpawnLimit(),
                    world.getTicksPerMonsterSpawn(),
                    world.getTicksPerAnimalSpawn(),
                    world.isKeepSpawnInMemoryEnabled(),
                    world.getWorldBlacklist(), // TODO: probably format this better
                    world.getRespawnToWorld(),
                    world.isBedRespawnEnabled(),
                    world.isAdjustSpawnEnabled(),
                    world.getGenerator(),
                    world.getSeed(),
                    world.getEnvironment().name(),
                    world.isAutoLoadEnabled(),
                    world.isWeatherEnabled(),
                    world.getCurrency(),
                    world.getPrice(),
                    world.isHidden(),
                    world.isFormatChatEnabled(),
                    world.getPlayerLimit(),
                    world.isAutoHealEnabled(),
                    world.isPVPEnabled(),
                    world.isHungerEnabled(),
                    world.getDifficulty(),
                    world.getGameMode(),
                    world.getSpawnLocation(), // TODO: probably format this better
                    world.getAlias(),
                    world.getName());
        } catch (MultiverseException e) {
            e.sendException(getMessager(), sender);
        }
        return true;
    }
}
