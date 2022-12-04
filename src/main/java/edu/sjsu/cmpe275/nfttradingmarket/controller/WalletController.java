package edu.sjsu.cmpe275.nfttradingmarket.controller;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.WalletDto;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Nft;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Wallet;
import edu.sjsu.cmpe275.nfttradingmarket.service.WalletService;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {
    private final ModelMapper modelMapper;
    private final WalletService walletService;

    public WalletController(ModelMapper modelMapper, WalletService walletService) {
        this.modelMapper = modelMapper;
        this.walletService = walletService;
    }

    @PostMapping(path = "/createWallet", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WalletDto> createWallet(@RequestBody WalletDto walletDto)
    {
        //convert DTO to entity
        this.modelMapper.typeMap(Wallet.class, WalletDto.class)
                .addMapping(src->src.getUser().getId(), WalletDto::setUserId);
        Wallet walletRequest = modelMapper.map(walletDto, Wallet.class);

        Wallet wallet = walletService.createWallet(walletRequest);

        //entity to DTO
        WalletDto WalletResponse = modelMapper.map(wallet, WalletDto.class);
        return ResponseEntity.ok().body(WalletResponse);
    }

    @PostMapping(path = "/createNft", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NftDto> createNft(@RequestBody NftDto nftDto){
        this.modelMapper.typeMap(Nft.class, NftDto.class)
                        .addMapping(src->src.getCreator().getId(), NftDto::setCreatorId)
                        .addMapping(src->src.getOwner().getId(), NftDto::setOwnerId);

        //Convert DTO to entity
        Nft nftRequest = modelMapper.map(nftDto, Nft.class);

        Nft nft = walletService.createNFT(nftRequest);

        //entity to DTO
        NftDto nftResponse = modelMapper.map(nft, NftDto.class);
        return ResponseEntity.ok().body(nftResponse);
    }

    @GetMapping(path="/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NftDto> getNftsById(@PathVariable("userId") UUID userId)
    {
        return walletService.getNfsById(userId);
    }
}
