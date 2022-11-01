package com.metrostate.edu.decentrovote.models.electoralsystems.interfaces;

public interface IQuotaSystem<T extends IElectoralSystem> {
    boolean quotaAchieved (T electoralSystem);
}
