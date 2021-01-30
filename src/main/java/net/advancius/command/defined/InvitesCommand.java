package net.advancius.command.defined;

import net.advancius.AdvanciusInvites;
import net.advancius.command.Command;
import net.advancius.commons.Commons;
import net.advancius.storage.structure.GuildStorage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class InvitesCommand implements Command {

    @Override
    public void executeCommand(User author, Guild guild, TextChannel channel, Message message, String[] arguments) {
        User inviter = author;

        if (arguments.length == 1) {
            inviter = Commons.getMentionedUser(arguments[0]);
            if (inviter == null) {
                throw new IllegalArgumentException("Unknown mentioned user " + arguments[0]);
            }
        }

        GuildStorage storage = AdvanciusInvites.getStorageManager().loadGuildStorage(guild);
        int invites = storage.loadUserStorage(inviter).getInvites();

        channel.sendMessage(inviter.getName() + " has **" + invites + "** invites to the server.").queue();
    }
}
