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

public class ResetInviterCommand implements Command {

    @Override
    public void executeCommand(User author, Guild guild, TextChannel channel, Message message, String[] arguments) {
        if (arguments.length == 0) {
            throw new IllegalArgumentException("Correct syntax: resetinviter <user mention>");
        }

        User invitee = Commons.getMentionedUser(arguments[0]);
        if (invitee == null)
            throw new IllegalArgumentException("Unknown mentioned user " + arguments[0]);

        GuildStorage storage = AdvanciusInvites.getStorageManager().loadGuildStorage(guild);
        storage.loadUserStorage(invitee).setSelectedInviter(false);

        AdvanciusInvites.getStorageManager().saveGuildStorage(guild, storage);

        channel.sendMessage(invitee.getName() + " can now select another user as their inviter.").queue();
    }
}
