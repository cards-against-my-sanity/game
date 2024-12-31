package dev.jacobandersen.cams.game.features.deck;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeckRepository extends CrudRepository<Deck, UUID> {
}
