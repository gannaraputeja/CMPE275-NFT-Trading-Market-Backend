package edu.sjsu.cmpe275.nfttradingmarket.schedules;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.sjsu.cmpe275.nfttradingmarket.entity.Offer;
import edu.sjsu.cmpe275.nfttradingmarket.entity.OfferStatus;
import edu.sjsu.cmpe275.nfttradingmarket.repository.OfferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

	@Autowired
	private OfferRepository offerRepository;

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(fixedRate = 36000)
	public void reportCurrentTime() {
		List<Offer> offers = offerRepository.findAllByStatus(OfferStatus.NEW);
		offers.stream().filter(offer -> offer.getExpirationTime() != null && offer.getExpirationTime().before(new Date()))
				.forEach(offer -> {
					offer.setStatus(OfferStatus.CANCELLED);
					offerRepository.save(offer);
				});
	}
}