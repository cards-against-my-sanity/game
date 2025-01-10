package dev.jacobandersen.cams.game.features.deck;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "decker")
public interface DeckClient {
    @RequestMapping(method = RequestMethod.GET, path = "/decks")
    List<Deck> getDecks();

    @RequestMapping(method = RequestMethod.GET, path = "/decks")
    List<DeckWithCards> getDecksWithCards(@RequestParam("id") List<UUID> ids);

    @RequestMapping(method = RequestMethod.GET, path = "/deck/{deckId}")
    DeckWithCards getDeckWithCards(@PathVariable("deckId") UUID deckId);
}
