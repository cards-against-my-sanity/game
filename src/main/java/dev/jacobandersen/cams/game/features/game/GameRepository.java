package dev.jacobandersen.cams.game.features.game;

import dev.jacobandersen.cams.game.security.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface GameRepository extends ListCrudRepository<Game, UUID> {
    Game findByHostId(UUID hostId);
}
