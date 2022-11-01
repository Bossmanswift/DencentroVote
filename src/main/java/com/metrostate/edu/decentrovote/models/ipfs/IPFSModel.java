package com.metrostate.edu.decentrovote.models.ipfs;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable.ByteArrayWrapper;
import io.ipfs.multihash.Multihash;

import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class IPFSModel {
    private final IPFS ipfs;

    public IPFSModel(String multiAddress) {
        this.ipfs = new IPFS(multiAddress);
    }

    public Set<String> persistToIPFS (byte [] dataBytes) {
        ByteArrayWrapper file = new ByteArrayWrapper(dataBytes);
        List<MerkleNode> merkleNodes;
        try {
            merkleNodes = new ArrayList<>(ipfs.add(file));
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }
        return merkleNodes.stream()
                .map(merkleNode -> merkleNode.hash.toString())
                .collect(Collectors.toSet());
    }

    public byte[] retrieveFileContent (String contentIdentifier) {
        Multihash filePointer = Multihash.fromBase58(contentIdentifier);
        byte[] fileContents;
        try {
            fileContents = ipfs.cat(filePointer);
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }
        return fileContents;
    }
}
