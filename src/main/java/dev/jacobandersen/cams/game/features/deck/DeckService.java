package dev.jacobandersen.cams.game.features.deck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeckService {
    private final DeckRepository repository;

    @Autowired
    public DeckService(DeckRepository repository) {
        this.repository = repository;
    }

    public Deck getDeck(UUID deckId) {
        return repository.findById(deckId).orElse(null);
    }
}
