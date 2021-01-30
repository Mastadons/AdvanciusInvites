package net.advancius.commons;

import net.advancius.AdvanciusInvites;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.awt.Color;
import java.time.Instant;

public class Commons {

    public static User getMentionedUser(String mention) {
        if (mention.length() > 4 && mention.startsWith("<@!") && mention.endsWith(">")) {
            return AdvanciusInvites.getReference().retrieveUserById(mention.substring(3, mention.length() - 1)).complete();
        }
        return null;
    }

    public static Role getMentionedRole(String mention) {
        if (mention.length() > 4 && mention.startsWith("<@&") && mention.endsWith(">")) {
            return AdvanciusInvites.getReference().getRoleById(mention.substring(3, mention.length() - 1));
        }
        return null;
    }

    public static MessageEmbed generateErrorEmbed(Exception exception) {
        return new EmbedBuilder()
                .setFooter("AdvanciusInvites by Mastadons", AdvanciusInvites.getReference().getSelfUser().getAvatarUrl())
                .setColor(new Color(209, 61, 31))
                .setTitle(":no_entry: " + exception.getClass().getSimpleName())
                .setDescription(exception.getMessage())
                .setTimestamp(Instant.now())
                .build();
    }
}
