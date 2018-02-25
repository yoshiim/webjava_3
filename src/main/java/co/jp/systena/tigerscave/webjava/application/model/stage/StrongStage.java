package co.jp.systena.tigerscave.webjava.application.model.stage;


import java.util.ArrayList;
import co.jp.systena.tigerscave.webjava.application.model.chara.EnemyDemon;
import co.jp.systena.tigerscave.webjava.application.model.chara.EnemyGoblin;
import co.jp.systena.tigerscave.webjava.application.model.chara.EnemySkeleton;

public class StrongStage extends StageBase {

  /**
   * コンストラクタ.
   */
  public StrongStage() {
    enemyList = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
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
