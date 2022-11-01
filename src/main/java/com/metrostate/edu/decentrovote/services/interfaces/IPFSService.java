package com.metrostate.edu.decentrovote.services.interfaces;

import java.util.Collection;

public interface IPFSService {
    Collection<String> persistToIPFS(byte [] encryptedByte);
    byte [] retrieveFromIPFS(String contentIdentifier);
    default void initIPFS() {}
}
