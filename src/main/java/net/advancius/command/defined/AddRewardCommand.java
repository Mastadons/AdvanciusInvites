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

public class AddRewardCommand implements Command {

    @Override
    public void executeCommand(User author, Guild guild, TextChannel channel, Message message, String[] arguments) {
        if (arguments.length != 2) {
            throw new IllegalArgumentException("Correct syntax: addreward <role mention> <count>");
        }

        Role role = Commons.getMentionedRole(arguments[0]);
        if (role == null) {
            throw new IllegalArgumentException("Unknown mentioned role " + arguments[0]);
        }

        int count = Integer.parseInt(arguments[1]);

        GuildStorage storage = AdvanciusInvites.getStorageManager().loadGuildStorage(guild);
        storage.addReward(role.getIdLong(), count);

        AdvanciusInvites.getStorageManager().saveGuildStorage(guild, storage);

        channel.sendMessage(role.getName() + " has been added as a reward for **" + count + "** invites to the server.").queue();
    }
}
