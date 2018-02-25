package co.jp.systena.tigerscave.webjava.application.model.chara;

public abstract class EnemyBase extends Character {
  protected EnemyBase() {

  }

  /**
   * 報酬取得.
   * @return 報酬.
   */
  public abstract int getReward();
}
