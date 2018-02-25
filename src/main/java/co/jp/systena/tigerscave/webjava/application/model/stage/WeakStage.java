package co.jp.systena.tigerscave.webjava.application.model.stage;


import java.util.ArrayList;
import co.jp.systena.tigerscave.webjava.application.model.chara.EnemyDemon;
import co.jp.systena.tigerscave.webjava.application.model.chara.EnemyGoblin;
import co.jp.systena.tigerscave.webjava.application.model.chara.EnemySkeleton;

public class WeakStage extends StageBase {

  /**
   * コンストラクタ.
   */
  public WeakStage() {
    enemyList = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      int random = (int)(Math.random() * 3);
      switch (random) {
        case 0:
          enemyList.add(new EnemySkeleton());
          break;
        case 1:
          enemyList.add(new EnemyDemon());
          break;
        case 2:
          enemyList.add(new EnemyGoblin());
          break;
        default:
          break;
      }
    }
  }
}
