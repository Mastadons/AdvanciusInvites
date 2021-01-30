package net.advancius.command.defined;

import lombok.Data;
import net.advancius.AdvanciusInvites;
import net.advancius.command.Command;
import net.advancius.storage.structure.GuildStorage;
import net.advancius.storage.structure.RewardStorage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RewardsCommand implements Command {

    @Override
    public void executeCommand(User author, Guild guild, TextChannel channel, Message message, String[] arguments) {
        GuildStorage storage = AdvanciusInvites.getStorageManager().loadGuildStorage(guild);
        storage.removeInvalidRewards(guild);

        if (storage.getRewardStorageList().isEmpty()) {
            throw new IllegalStateException("There are no rewards in this server.");
        }

        StringBuilder messageBuilder = new StringBuilder("__**Advancius Invite Rewards**__\n");

        int entryIndex = 0;
        for (RewardStorage rewardStorage : storage.getRewardStorageList()) {
            messageBuilder.append("**" + (++entryIndex) + ".** " + AdvanciusInvites.getReference().getRoleById(rewardStorage.getRole()).getName() + ": " + rewardStorage.getInviteRequirement() + " invites\n");
        }

        channel.sendMessage(messageBuilder.toString()).queue();
    }
}
