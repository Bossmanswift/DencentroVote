package com.metrostate.edu.decentrovote.services;

import com.metrostate.edu.decentrovote.models.ipfs.IPFSModel;
import com.metrostate.edu.decentrovote.services.interfaces.IPFSService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class IPFSServiceImpl implements IPFSService {

    @Value("${ipfs.multiaddr.string}")
    private String MULTI_ADDR;

    private IPFSModel ipfsModel;

    public IPFSServiceImpl() {}

    @Override
    public Collection<String> persistToIPFS(byte[] encryptedByte) {
        return ipfsModel.persistToIPFS(encryptedByte);
    }

    @Override
    public byte[] retrieveFromIPFS(String contentIdentifier) {
        return ipfsModel.retrieveFileContent(contentIdentifier);
    }

    @Override
    public void initIPFS() {
        if (ipfsModel == null) {
            ipfsModel = new IPFSModel(MULTI_ADDR);
        }
    }
}
