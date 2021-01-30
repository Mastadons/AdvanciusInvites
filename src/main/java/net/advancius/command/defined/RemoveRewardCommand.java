package net.advancius.command.defined;

import net.advancius.AdvanciusInvites;
import net.advancius.command.Command;
import net.advancius.commons.Commons;
import net.advancius.storage.structure.GuildStorage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class RemoveRewardCommand implements Command {

    @Override
    public void executeCommand(User author, Guild guild, TextChannel channel, Message message, String[] arguments) {
        if (arguments.length != 1) {
            throw new IllegalArgumentException("Correct syntax: removereward <role mention>");
        }

        Role role = Commons.getMentionedRole(arguments[0]);
        if (role == null) {
            throw new IllegalArgumentException("Unknown mentioned role " + arguments[0]);
        }

        GuildStorage storage = AdvanciusInvites.getStorageManager().loadGuildStorage(guild);
        storage.removeInvalidRewards(guild);

        boolean removed = storage.removeReward(role.getIdLong());

        AdvanciusInvites.getStorageManager().saveGuildStorage(guild, storage);

        if (removed) {
            channel.sendMessage(role.getName() + " has been removed as a reward.").queue();
        }
        else channel.sendMessage(role.getName() + " is not a reward.").queue();
    }
}
