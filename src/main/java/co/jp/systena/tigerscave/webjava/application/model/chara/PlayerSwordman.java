package co.jp.systena.tigerscave.webjava.application.model.chara;

public class PlayerSwordman extends PlayerBase {

  /**
   * コンストラクタ.
   */
  public PlayerSwordman() {
    setName("剣士");
    setHitPoint(100);
    setAttack(15);
    setDefense(10);
    setMagicAttack(5);
    setMagicDefense(10);
  }

  @Override
  public int getMaxHitPoint() {
    return 100;
  }

  @Override
  public String getJob() {
    return "剣士";
  }

}
