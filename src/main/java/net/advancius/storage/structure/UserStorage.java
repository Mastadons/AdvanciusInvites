package net.advancius.storage.structure;

import lombok.Data;

@Data
public class UserStorage {

    private boolean selectedInviter;
    private long inviter;

    private int invites;

    public int increaseInvites() {
        return increaseInvites(1);
    }

    public int increaseInvites(int count) {
        return invites += count;
    }
}
