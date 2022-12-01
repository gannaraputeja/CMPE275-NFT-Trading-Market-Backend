package edu.sjsu.cmpe275.nfttradingmarket.service;

import edu.sjsu.cmpe275.nfttradingmarket.entity.Nft;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Wallet;
import edu.sjsu.cmpe275.nfttradingmarket.repository.NftRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    private final NftRepository nftRepository;
    private final WalletRepository walletRepository;

    public WalletService(NftRepository nftRepository, WalletRepository walletRepository) {
        this.nftRepository = nftRepository;
        this.walletRepository = walletRepository;
    }

    public Nft createNFT(Nft nft){
        return nftRepository.save(nft);
    }

    public Wallet createWallet(Wallet wallet){
        return walletRepository.save(wallet);
    }
}
