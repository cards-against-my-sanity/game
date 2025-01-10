package dev.jacobandersen.cams.game.features.deck;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeckService {
    private final DeckClient deckClient;

    public DeckService(DeckClient deckClient) {
        this.deckClient = deckClient;
    }

    public List<Deck> getDecks() {
        return deckClient.getDecks();
    }

    public List<DeckWithCards> getDecksWithCards(List<UUID> ids) {
        return deckClient.getDecksWithCards(ids);
    }

    public DeckWithCards getDeckWithCards(UUID id) {
        return deckClient.getDeckWithCards(id);
    }
}
