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

public class SetInvitesCommand implements Command {

    @Override
    public void executeCommand(User author, Guild guild, TextChannel channel, Message message, String[] arguments) {
        if (arguments.length != 2) {
            throw new IllegalArgumentException("Correct syntax: setinvites <user mention> <count>");
        }

        User inviter = Commons.getMentionedUser(arguments[0]);
        if (inviter == null) {
            throw new IllegalArgumentException("Unknown mentioned user " + arguments[0]);
        }

        int count = Integer.parseInt(arguments[1]);

        GuildStorage storage = AdvanciusInvites.getStorageManager().loadGuildStorage(guild);
        UserStorage inviterStorage = storage.loadUserStorage(inviter);
        int initialInvites = inviterStorage.getInvites();
        inviterStorage.setInvites(count);

        AdvanciusInvites.getStorageManager().saveGuildStorage(guild, storage);

        channel.sendMessage(inviter.getName() + " now has **" + inviterStorage.getInvites() + "** invites to the server.").queue();

        EmbedBuilder mentionedEmbed = new EmbedBuilder()
                .setFooter("AdvanciusInvites by Mastadons", AdvanciusInvites.getReference().getSelfUser().getAvatarUrl())
                .setColor(new Color(52, 155, 235))
                .setTitle(":robot: Manual Invite Recognition Received.")
                .setDescription("Invites have been manually added to your invite count.\nYou now have **" + inviterStorage.getInvites() + "** invite(s) to the server.")
                .setTimestamp(Instant.now());

        for (RewardStorage rewardStorage : AdvanciusInvites.getStorageManager().loadGuildStorage(guild).getRewards(initialInvites, inviterStorage.getInvites())) {
            Role role = guild.getRoleById(rewardStorage.getRole());
            guild.addRoleToMember(guild.retrieveMember(inviter).complete(), role).complete();

            mentionedEmbed.addField(":gift: Reward Unlocked!", "You have unlocked the **" + role.getName() + "** role by reaching **" + rewardStorage.getInviteRequirement() + "** invites", false);
        }

        inviter.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage(mentionedEmbed.build())).queue();

        storage.removeInvalidRewards(guild);
    }
}
