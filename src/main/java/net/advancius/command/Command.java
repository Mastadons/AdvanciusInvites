package net.advancius.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public interface Command {

    void executeCommand(User author, Guild guild, TextChannel channel, Message message, String[] arguments);
}
