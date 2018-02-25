package co.jp.systena.tigerscave.webjava.infrastructure.dao;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity                 // JAPのエンティティ宣言
@Table(name = "enemy")  // テーブル名を明示的に指名
public class Enemy {
  @Id               // PrimaryKey
  @GeneratedValue   // 自動採番
  private Integer id;

  /** 名前. */
  private String name;
  /** HP. */
  private int hitPoint;
  /** 攻撃力. */
  private int attack;
  /** 魔法攻撃力. */
  private int magicAttack;
  /** 防御力. */
  private int defense;
  /** 魔法防御力. */
  private int magicDefense;
  /** 最大HP. */
  protected int maxHitPoint;
  /** 報酬. */
  private int reward;

  /**
   * 名前を設定.
   * @param name 名前.
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * 名前を取得.
   * @return 名前.
   */
  public String getName() {
    return this.name;
  }

  /**
   * HPを設定.
   * @param hitPoint HP.
   */
  public void setHitPoint(final int hitPoint) {
    this.hitPoint = hitPoint;
  }

  /**
   * HPを取得.
   * @return HP.
   */
  public int getHitPoint() {
    return this.hitPoint;
  }

  /**
   * 攻撃力を設定.
   * @param attack 攻撃力.
   */
  public void setAttack(final int attack) {
    this.attack = attack;
  }

  /**
   * 攻撃力を取得.
   * @return 攻撃力.
   */
  public int getAttack() {
    return this.attack;
  }

  /**
   * 魔法攻撃力を設定.
   * @param magicAttack 魔法攻撃力.
   */
  public void setMagicAttack(final int magicAttack) {
    this.magicAttack = magicAttack;
  }

  /**
   * 魔法攻撃力を取得.
   * @return 魔法攻撃力.
   */
  public int getMagicAttack() {
    return this.magicAttack;
  }

  /**
   * 防御力を設定.
   * @param defense  防御力.
   */
  public void setDefense(final int defense) {
    this.defense = defense;
  }

  /**
   * 防御力を取得.
   * @return 防御力.
   */
  public int getDefense() {
    return this.defense;
  }

  /**
   * 魔法防御力を設定.
   * @param magicDefense  魔法防御力.
   */
  public void setMagicDefense(final int magicDefense) {
    this.magicDefense = magicDefense;
  }

  /**
   * 魔法防御力を取得.
   * @return 魔法防御力.
   */
  public int getMagicDefense() {
    return this.magicDefense;
  }

  /**
   * 最大HPを設定.
   * @param maxHitPoint 最大HP.
   */
  public void setMaxHitPoint(final int maxHitPoint) {
    this.maxHitPoint = maxHitPoint;
  }

  /**
   * 最大HPを取得.
   * @return 最大HP.
   */
  public int getMaxHitPoint() {
    return maxHitPoint;
  }

  /**
   * 報酬を設定.
   * @param reward 報酬.
   */
  public void setReward(final int reward) {
    this.reward = reward;
  }

  /**
   * 報酬を取得.
   * @return 報酬.
   */
  public int getReward() {
    return this.reward;
  }
}
