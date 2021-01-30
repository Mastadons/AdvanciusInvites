package net.advancius.command.defined;

import lombok.Data;
import net.advancius.AdvanciusInvites;
import net.advancius.command.Command;
import net.advancius.commons.Commons;
import net.advancius.storage.structure.GuildStorage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LeaderboardCommand implements Command {

    @Override
    public void executeCommand(User author, Guild guild, TextChannel channel, Message message, String[] arguments) {
        GuildStorage storage = AdvanciusInvites.getStorageManager().loadGuildStorage(guild);

        StringBuilder messageBuilder = new StringBuilder("__**Advancius Invite Leaderboard**__\n");

        List<LeaderboardEntry> leaderboardEntryList = new ArrayList<>();
        storage.getUserStorageMap().forEach((userId, userStorage) -> {
            if (userStorage.getInvites() > 0) {
                leaderboardEntryList.add(new LeaderboardEntry(userId, userStorage.getInvites()));
            }
        });

        if (leaderboardEntryList.isEmpty()) {
            throw new IllegalStateException("There are no users with invites in this server.");
        }

        Collections.sort(leaderboardEntryList, Comparator.comparingInt(LeaderboardEntry::getInvites).reversed());

        int entryIndex = 0;
        for (LeaderboardEntry leaderboardEntry : leaderboardEntryList) {
            messageBuilder.append("**" + (++entryIndex) + ".** " + AdvanciusInvites.getReference().retrieveUserById(leaderboardEntry.user).complete().getName() + ": " + leaderboardEntry.invites + " invites ");
            if (entryIndex == 1) messageBuilder.append(":first_place:");
            if (entryIndex == 2) messageBuilder.append(":second_place:");
            if (entryIndex == 3) messageBuilder.append(":third_place:");
            messageBuilder.append("\n");
        }

        channel.sendMessage(messageBuilder.toString()).queue();
    }

    @Data
    class LeaderboardEntry {

        private final long user;
        private final int invites;
    }
}
