package net.advancius.storage.structure;

import lombok.Data;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class GuildStorage {

    private String commandPrefix = ">";

    private Map<Long, UserStorage> userStorageMap = new HashMap<>();

    private List<RewardStorage> rewardStorageList = new ArrayList<>();

    public UserStorage loadUserStorage(User invitee) {
        return loadUserStorage(invitee.getIdLong());
    }

    public UserStorage loadUserStorage(long inviteeId) {
        if (!userStorageMap.containsKey(inviteeId)) userStorageMap.put(inviteeId, new UserStorage());

        return userStorageMap.get(inviteeId);
    }

    public void addReward(long roleId, int count) {
        rewardStorageList.add(new RewardStorage(count, roleId));
    }

    public boolean removeReward(long roleId) {
        return rewardStorageList.removeIf(rewardStorage -> rewardStorage.getRole() == roleId);
    }

    public List<RewardStorage> getRewards(int minimumExclusive, int maximumInclusive) {
        List<RewardStorage> includedRewardList = new ArrayList<>();

        for (RewardStorage rewardStorage : rewardStorageList) {
            if (rewardStorage.getInviteRequirement() > minimumExclusive && rewardStorage.getInviteRequirement() <= maximumInclusive)
                includedRewardList.add(rewardStorage);
        }

        return includedRewardList;
    }

    public List<RewardStorage> getRewards(int inviteCount) {
        return getRewards(inviteCount-1, inviteCount);
    }

    public void removeInvalidRewards(Guild guild) {
        rewardStorageList.removeIf(rewardStorage -> guild.getRoleById(rewardStorage.getRole()) == null);
    }
}
