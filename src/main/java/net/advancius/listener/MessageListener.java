package net.advancius.listener;

import net.advancius.AdvanciusInvites;
import net.advancius.command.Command;
import net.advancius.commons.Commons;
import net.advancius.storage.structure.GuildStorage;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        GuildStorage storage = AdvanciusInvites.getStorageManager().loadGuildStorage(event.getGuild());

        if (AdvanciusInvites.getReference().getSelfUser().equals(Commons.getMentionedUser(event.getMessage().getContentRaw()))) {
            event.getChannel().sendMessage("Hello, the current prefix is `" + storage.getCommandPrefix() + "`").queue();
            return;
        }

        if (!event.getMessage().getContentRaw().startsWith(storage.getCommandPrefix())) return;

        String commandMessage = event.getMessage().getContentRaw().substring(1);
        String[] components = commandMessage.split(" ");

        if (components.length == 0) return;

        Command command = AdvanciusInvites.getCommandManager().getCommand(components[0]);
        if (command == null) return;

        System.out.println("User " + event.getAuthor().getName() + " executed command " + components[0]);
        try {
            command.executeCommand(event.getAuthor(), event.getGuild(), event.getChannel(), event.getMessage(), Arrays.copyOfRange(components, 1, components.length));
        } catch (Exception exception) {
            event.getChannel().sendMessage(Commons.generateErrorEmbed(exception)).queue();
        }
    }
}
