package com.metrostate.edu.decentrovote.models.electoralsystems;

import org.springframework.stereotype.Service;

/**
 * The class {@link ElectionProtocolSingleton} is a Singleton class that creates a single global point
 * to accessing and utilizing the {@link AbstractElectoralSystem} object.
 */
@Service
public class ElectionProtocolSingleton {
    private volatile static ElectionProtocolSingleton electionProtocolSingleton;
    private AbstractElectoralSystem electoralSystem;

    private ElectionProtocolSingleton () {}

    public static ElectionProtocolSingleton getInstance() {
        if (electionProtocolSingleton == null) {
            synchronized (ElectionProtocolSingleton.class) {
                electionProtocolSingleton = new ElectionProtocolSingleton();
            }
        }
        return electionProtocolSingleton;
    }

    public void setElectoralSystem (AbstractElectoralSystem electoralSystem) {
        this.electoralSystem = electoralSystem;
    }

    public AbstractElectoralSystem getElectoralSystem () {
        return electoralSystem;
    }
}
