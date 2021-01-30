package net.advancius.storage;

import lombok.Data;
import net.advancius.AdvanciusInvites;
import net.advancius.storage.structure.GuildStorage;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class StorageManager {

    private final String databaseTable;

    public void load() throws SQLException {
        Connection databaseConnection = AdvanciusInvites.getDatabase().getConnection();

        System.out.println(AdvanciusInvites.getDatabase());
        PreparedStatement statement = databaseConnection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + databaseTable + "` (id BIGINT PRIMARY KEY NOT NULL, storage LONGTEXT);");
        statement.execute();
    }

    public GuildStorage loadGuildStorage(long guildId) {
        try {
            Connection databaseConnection = AdvanciusInvites.getDatabase().getConnection();

            PreparedStatement statement = databaseConnection.prepareStatement("SELECT * FROM `" + databaseTable + "` WHERE id = ?;",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            statement.setLong(1, guildId);
            ResultSet result = statement.executeQuery();

            if (!result.first()) return new GuildStorage();

            GuildStorage storage = AdvanciusInvites.GSON.fromJson(result.getString("storage"), GuildStorage.class);
            return storage;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public GuildStorage loadGuildStorage(Guild guild) {
        return loadGuildStorage(guild.getIdLong());
    }

    public void saveGuildStorage(long guildId, GuildStorage storage) {
        try {
            Connection databaseConnection = AdvanciusInvites.getDatabase().getConnection();

            PreparedStatement statement = databaseConnection.prepareStatement("REPLACE INTO `" + databaseTable + "` VALUES (?, ?);");
            statement.setLong(1, guildId);
            statement.setString(2, AdvanciusInvites.GSON.toJson(storage));
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void saveGuildStorage(Guild guild, GuildStorage storage) {
        saveGuildStorage(guild.getIdLong(), storage);
    }
}
