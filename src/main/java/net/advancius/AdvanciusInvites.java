package net.advancius;

import com.google.gson.Gson;
import lombok.Data;
import lombok.Getter;
import net.advancius.command.CommandManager;
import net.advancius.configuration.Configuration;
import net.advancius.configuration.EnvironmentConfiguration;
import net.advancius.configuration.FileConfiguration;
import net.advancius.database.Database;
import net.advancius.listener.MessageListener;
import net.advancius.storage.StorageManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.awt.TextComponent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

@Data
public class AdvanciusInvites {

    public static final Gson GSON = new Gson();

    @Getter private static JDA reference;

    @Getter private static Configuration configuration;
    @Getter private static Database database;

    @Getter private static StorageManager storageManager;
    @Getter private static CommandManager commandManager;

    public static void main(String[] arguments) throws LoginException, InterruptedException, IOException {
        System.out.println("Starting bot...");

        if (System.getProperty("environmentConfiguration") == null) {
            System.out.println("Initializing configuration from configuration.properties file.");
            System.out.println("To use environment variables for configuration apply the -DenvironmentConfiguration option on run.");

            FileReader reader = new FileReader("configuration.properties");
            configuration = new FileConfiguration(reader);
        }
        else {
            System.out.println("Initializing configuration using environment variables.");
            configuration = new EnvironmentConfiguration();
        }

        String databaseName = configuration.getProperty("databaseName");
        String databaseHostname = configuration.getProperty("databaseHostname");
        String databaseUsername = configuration.getProperty("databaseUsername");
        String databasePassword = configuration.getProperty("databasePassword");

        if (databaseName == null || databaseHostname == null || databaseUsername == null || databasePassword == null) {
            throw new IllegalStateException("One or more database properties (databaseName, databaseHostname, databaseUsername, databasePassword) were not provided in configuration.");
        }

        try {
            database = new Database(databaseName, databaseHostname, databaseUsername, databasePassword);
            database.connect();

            System.out.println("Database connection established.");
        } catch (SQLException | ClassNotFoundException exception) {
            throw new IllegalStateException("Encountered exception connecting to database.", exception);
        }

        String databaseTable = configuration.getProperty("databaseTable");

        if (databaseTable == null) {
            throw new IllegalStateException("Database table property (databaseTable) was not provided in configuration.");
        }

        storageManager = new StorageManager(databaseTable);
        try {
            storageManager.load();
        } catch (SQLException exception) {
            throw new IllegalStateException("Encountered exception starting storage manager.", exception);
        }

        commandManager = new CommandManager();
        commandManager.load();

        if (arguments.length == 0) {
            throw new IllegalStateException("Bot token must be supplied as first argument on program execution.");
        }

        String token = arguments[0];
        System.out.println("Initializing bot instance with token: \"" + token + "\"");

        JDABuilder referenceBuilder = JDABuilder.createDefault(arguments[0]);

        referenceBuilder.setActivity(Activity.watching("invitations"));

        System.out.println("Building bot instance...");
        reference = referenceBuilder.build();
        reference.awaitReady();

        reference.addEventListener(new MessageListener());

        System.out.println("Bot successfully started!");
    }
}
