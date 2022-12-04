package edu.sjsu.cmpe275.nfttradingmarket.service;

import edu.sjsu.cmpe275.nfttradingmarket.dto.ListingDto;
import edu.sjsu.cmpe275.nfttradingmarket.dto.NftDto;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Listing;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Nft;
import edu.sjsu.cmpe275.nfttradingmarket.entity.Offer;
import edu.sjsu.cmpe275.nfttradingmarket.entity.User;
import edu.sjsu.cmpe275.nfttradingmarket.exception.NoListingsFoundException;
import edu.sjsu.cmpe275.nfttradingmarket.exception.UserNotFoundException;
import edu.sjsu.cmpe275.nfttradingmarket.repository.ListingRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.NftRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.OfferRepository;
import edu.sjsu.cmpe275.nfttradingmarket.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ListingService {
    private final ListingRepository listingRepository;
    private final OfferRepository offerRepository;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final NftRepository nftRepository;

    public ListingService(ListingRepository listingRepository, OfferRepository offerRepository, UserRepository userRepository, ModelMapper modelMapper, NftRepository nftRepository) {
        this.listingRepository = listingRepository;
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.nftRepository = nftRepository;
    }

    public Listing createNFT(Listing listing){
        return listingRepository.save(listing);
    }

    public Offer makeOffer(Offer offer){
        return offerRepository.save(offer);
    }

    public List<ListingDto> getAllListingsById(UUID userId)
    {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent())
        {
            List<Listing> result = listingRepository.findAllListingsByUser(user);
            if(!result.isEmpty()) {
                List<ListingDto> listingDtoList = result
                        .stream()
                        .map(Listing -> modelMapper.map(Listing, ListingDto.class))
                        .collect(Collectors.toList());

                return listingDtoList;
            }
            else
                throw new NoListingsFoundException("No listings found for User");
        }
        else
            throw new UserNotFoundException("User not found exception");
    }

    public List<NftDto> getAllNftListingsByUser(UUID userId){
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent())
        {
            List<Listing> result = listingRepository.findAllListingsByUser(user);
            if(!result.isEmpty())
            {
                List<Optional<Nft>> nftList = new ArrayList<>();
                for(Listing listing: result) {
                    nftList.add((nftRepository.findById(listing.getNft().getTokenId())));
                }

                if(!nftList.isEmpty())
                {
                    List<NftDto> nftDtoList = nftList
                            .stream()
                            .map(Nft -> modelMapper.map(Nft, NftDto.class))
                            .collect(Collectors.toList());

                    return nftDtoList;
                }
            }
            else
                throw new ResourceNotFoundException();
        }
        else
            throw new ResourceNotFoundException();
        return null;
    }
}
