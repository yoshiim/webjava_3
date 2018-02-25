package co.jp.systena.tigerscave.webjava.application.model.stage;


import java.util.ArrayList;
import co.jp.systena.tigerscave.webjava.application.model.chara.EnemyBase;

public abstract class StageBase {
  protected ArrayList<EnemyBase> enemyList;

  public ArrayList<EnemyBase> getEnemyList() {
    return enemyList;
  }
}
