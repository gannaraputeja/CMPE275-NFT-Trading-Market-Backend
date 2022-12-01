package edu.sjsu.cmpe275.nfttradingmarket.controller;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftDto;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Nft;
import edu.sjsu.cmpe275.nfttradingmarket.service.NftService;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {
    private final ModelMapper modelMapper;
    private final NftService nftService;

    public WalletController(ModelMapper modelMapper, NftService nftService) {
        this.modelMapper = modelMapper;
        this.nftService = nftService;
    }

    @PostMapping(path = "/createNft", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NftDto> createNft(@RequestBody NftDto nftDto){
        this.modelMapper.typeMap(Nft.class, NftDto.class)
                        .addMapping(src->src.getCreator().getId(), NftDto::setCreatorId)
                        .addMapping(src->src.getOwner().getId(), NftDto::setOwnerId);

        //Convert DTO to entity
        Nft nftRequest = modelMapper.map(nftDto, Nft.class);

        Nft nft = nftService.createNFT(nftRequest);

        //entity to DTO
        NftDto nftResponse = modelMapper.map(nft, NftDto.class);
        return ResponseEntity.ok().body(nftResponse);
    }
}
