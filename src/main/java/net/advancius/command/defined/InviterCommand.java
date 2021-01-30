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
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.Color;
import java.time.Instant;

public class InviterCommand implements Command {

    @Override
    public void executeCommand(User invitee, Guild guild, TextChannel channel, Message message, String[] arguments) {
        if (arguments.length == 0)
            throw new IllegalArgumentException("Correct syntax: inviter <user mention>");

        User inviter = Commons.getMentionedUser(arguments[0]);
        if (inviter == null)
            throw new IllegalArgumentException("Unknown mentioned user " + arguments[0]);

        if (inviter.getIdLong() == invitee.getIdLong())
            throw new IllegalArgumentException("You cannot give invite recognition to yourself!");

        if (inviter.isBot()) {
            throw new IllegalArgumentException("You cannot give invite recognition to this user!");
        }

        GuildStorage storage = AdvanciusInvites.getStorageManager().loadGuildStorage(guild);

        UserStorage inviteeStorage = storage.loadUserStorage(invitee);
        UserStorage inviterStorage = storage.loadUserStorage(inviter);

        if (inviteeStorage.isSelectedInviter())
            throw new IllegalArgumentException("You have already selected an inviter.");

        inviterStorage.increaseInvites();
        inviteeStorage.setSelectedInviter(true);

        MessageEmbed embed = new EmbedBuilder()
                .setFooter("AdvanciusInvites by Mastadons", AdvanciusInvites.getReference().getSelfUser().getAvatarUrl())
                .setColor(new Color(52, 155, 235))
                .setTitle(":white_check_mark: Success!")
                .setDescription(arguments[0] + " has been updated as your inviter!\nThey now have **" + inviterStorage.getInvites() + "** invite to the server.")
                .setTimestamp(Instant.now())
                .build();

        channel.sendMessage(embed).queue();

        EmbedBuilder mentionedEmbed = new EmbedBuilder()
                .setFooter("AdvanciusInvites by Mastadons", AdvanciusInvites.getReference().getSelfUser().getAvatarUrl())
                .setColor(new Color(52, 155, 235))
                .setTitle(":tada: Invite Recognition Received!")
                .setDescription(invitee.getAsTag() + " has selected you as their inviter!\nYou now have **" + inviterStorage.getInvites() + "** invite(s) to the server.")
                .setTimestamp(Instant.now());

        inviter.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage(mentionedEmbed.build())).queue();

        storage.removeInvalidRewards(guild);

        for (RewardStorage rewardStorage : AdvanciusInvites.getStorageManager().loadGuildStorage(guild).getRewards(inviterStorage.getInvites())) {
            Role role = guild.getRoleById(rewardStorage.getRole());
            guild.addRoleToMember(guild.retrieveMember(inviter).complete(), role).complete();

            mentionedEmbed.addField(":gift: Reward Unlocked!", "You have unlocked the **" + role.getName() + "** role by reaching **" + rewardStorage.getInviteRequirement() + "** invites", false);
        }

        AdvanciusInvites.getStorageManager().saveGuildStorage(guild, storage);
    }
}
