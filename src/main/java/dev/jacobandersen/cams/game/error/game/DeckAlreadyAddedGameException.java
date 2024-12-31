package dev.jacobandersen.cams.game.error.game;

import dev.jacobandersen.cams.game.features.deck.Deck;
import dev.jacobandersen.cams.game.features.game.Game;

public class DeckAlreadyAddedGameException extends GameException {
    private final Deck deck;

    public DeckAlreadyAddedGameException(final Game game, final Deck deck) {
        super(game, String.format("Deck %s (%s) already added", deck.name(), deck.id()));
        this.deck = deck;
    }

    public Deck getDeck() {
        return deck;
    }
}
