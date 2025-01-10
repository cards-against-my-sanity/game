package dev.jacobandersen.cams.game.features.deck;

import java.util.List;

public record DeckWithCards(Deck deck, List<BlackCard> blackCards, List<WhiteCard> whiteCards) {
}
