package net.advancius.storage.structure;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RewardStorage {

    private int inviteRequirement;

    private long role;
}
