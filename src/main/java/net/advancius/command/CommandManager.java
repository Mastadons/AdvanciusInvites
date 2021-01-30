package net.advancius.command;

import net.advancius.command.defined.AddRewardCommand;
import net.advancius.command.defined.InviterCommand;
import net.advancius.command.defined.InvitesCommand;
import net.advancius.command.defined.LeaderboardCommand;
import net.advancius.command.defined.RemoveRewardCommand;
import net.advancius.command.defined.ResetInviterCommand;
import net.advancius.command.defined.RewardsCommand;
import net.advancius.command.defined.SetInvitesCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private Map<String, Command> commandMap = new HashMap<>();

    public void load() {
        commandMap.put("dancer", (author, guild, channel, message, arguments) -> message.addReaction(":dancer:").queue());

        commandMap.put("inviter", new InviterCommand());
        commandMap.put("invites", new InvitesCommand());
        commandMap.put("leaderboard", new LeaderboardCommand());
        commandMap.put("setinvites", new SetInvitesCommand());
        commandMap.put("addreward", new AddRewardCommand());
        commandMap.put("removereward", new RemoveRewardCommand());
        commandMap.put("resetinviter", new ResetInviterCommand());
        commandMap.put("rewards", new RewardsCommand());
    }

    public Command getCommand(String name) {
        for (Map.Entry<String, Command> commandEntry : commandMap.entrySet()) {
            if (commandEntry.getKey().equalsIgnoreCase(name)) return commandEntry.getValue();
        }
        return null;
    }
}
