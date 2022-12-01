package edu.sjsu.cmpe275.nfttradingmarket.service;

import edu.sjsu.cmpe275.nfttradingmarket.entity.Nft;
import edu.sjsu.cmpe275.nfttradingmarket.repository.NftRepository;
import org.springframework.stereotype.Service;

@Service
public class NftService {
    private final NftRepository nftRepository;

    public NftService(NftRepository nftRepository) {
        this.nftRepository = nftRepository;
    }

    public Nft createNFT(Nft nft){
        return nftRepository.save(nft);
    }
}
