package co.jp.systena.tigerscave.webjava.infrastructure.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import co.jp.systena.tigerscave.webjava.infrastructure.dao.Enemy;

@Repository
public interface EnemyRepository extends CrudRepository<Enemy, Integer> {
  List<Enemy> findAll();
}
