package dev.jacobandersen.cams.game.net.packet.out;

import dev.jacobandersen.cams.game.features.deck.Deck;
import dev.jacobandersen.cams.game.net.packet.Packet;
import dev.jacobandersen.cams.game.net.packet.PacketType;

import java.util.Set;
import java.util.UUID;

public class PacketOutGameDecksUpdated extends Packet {
    private UUID gameId;
    private Set<Deck> decks;

    public PacketOutGameDecksUpdated() {
        super(PacketType.GAME_DECKS_UPDATED);
    }

    public PacketOutGameDecksUpdated(UUID gameId, Set<Deck> decks) {
        this();
        this.gameId = gameId;
        this.decks = decks;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public Set<Deck> getDecks() {
        return decks;
    }

    public void setDecks(Set<Deck> decks) {
        this.decks = decks;
    }
}
