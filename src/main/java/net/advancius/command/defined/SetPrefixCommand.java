package net.advancius.command.defined;

import net.advancius.AdvanciusInvites;
import net.advancius.command.Command;
import net.advancius.commons.Commons;
import net.advancius.storage.structure.GuildStorage;
import net.advancius.storage.structure.RewardStorage;
import net.advancius.storage.structure.UserStorage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.Color;
import java.time.Instant;

public class SetPrefixCommand implements Command {

    @Override
    public void executeCommand(User author, Guild guild, TextChannel channel, Message message, String[] arguments) {
        if (arguments.length != 1) {
            throw new IllegalArgumentException("Correct syntax: setprefix <prefix>");
        }

        GuildStorage storage = AdvanciusInvites.getStorageManager().loadGuildStorage(guild);
        storage.setCommandPrefix(arguments[0]);

        AdvanciusInvites.getStorageManager().saveGuildStorage(guild, storage);

        channel.sendMessage("The command prefix has been set to: `" + arguments[0] + "`").queue();
    }
}
