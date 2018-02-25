package co.jp.systena.tigerscave.webjava.infrastructure.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import co.jp.systena.tigerscave.webjava.infrastructure.dao.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Integer> {
  List<Player> findAll();
}
