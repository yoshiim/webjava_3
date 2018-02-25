package co.jp.systena.tigerscave.webjava.domain.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.jp.systena.tigerscave.webjava.application.model.chara.EnemyBase;
import co.jp.systena.tigerscave.webjava.application.model.chara.PlayerAx;
import co.jp.systena.tigerscave.webjava.application.model.chara.PlayerBase;
import co.jp.systena.tigerscave.webjava.application.model.chara.PlayerMagicalSwordsman;
import co.jp.systena.tigerscave.webjava.application.model.chara.PlayerSwordman;
import co.jp.systena.tigerscave.webjava.application.model.chara.PlayerWitch;
import co.jp.systena.tigerscave.webjava.application.model.stage.NormalStage;
import co.jp.systena.tigerscave.webjava.application.model.stage.StageBase;
import co.jp.systena.tigerscave.webjava.application.model.stage.StrongStage;
import co.jp.systena.tigerscave.webjava.application.model.stage.WeakStage;
import co.jp.systena.tigerscave.webjava.infrastructure.dao.Enemy;
import co.jp.systena.tigerscave.webjava.infrastructure.dao.Player;
import co.jp.systena.tigerscave.webjava.infrastructure.repository.EnemyRepository;
import co.jp.systena.tigerscave.webjava.infrastructure.repository.PlayerRepository;

@Service    // サービス層クラス（ビジネスロジック）
public class BattleService {
  @Autowired    // DI設定。依存関係をなくす
  private PlayerRepository playerRepository;
  @Autowired
  private EnemyRepository enemyRepository;

  /**
   * 初期化処理.
   * @param playerNumber プレイヤー番号.
   * @param stageNumber ステージ番号.
   * @param difficulty 難易度.
   */
  public void init(final String playerNumber, final String stageNumber, final String difficulty) {

    // 各リポジトリの情報を削除する.
    playerRepository.deleteAll();
    enemyRepository.deleteAll();

    // プレイヤーを選択.
    PlayerBase selectPlayer;
    switch (playerNumber) {
      case "1":
        selectPlayer = new PlayerSwordman();
        break;
      case "2":
        selectPlayer = new PlayerWitch();
        break;
      case "3":
        selectPlayer = new PlayerAx();
        break;
      case "4":
        selectPlayer = new PlayerMagicalSwordsman();
        break;
      default:
        selectPlayer = new PlayerSwordman();
        break;
    }

    // プレイヤー情報を保存.
    Player player = new Player();
    player.setAttack(selectPlayer.getAttack());
    player.setDefense(selectPlayer.getDefense());
    player.setHitPoint(selectPlayer.getHitPoint());
    player.setJob(selectPlayer.getJob());
    player.setMagicAttack(selectPlayer.getMagicAttack());
    player.setMagicDefense(selectPlayer.getMagicDefense());
    player.setMaxHitPoint(selectPlayer.getMaxHitPoint());
    player.setName(selectPlayer.getName());
    playerRepository.save(player);

    // ステージを選択.
    StageBase stage;
    switch (stageNumber) {
      case "1":
        stage = new WeakStage();
        break;
      case "2":
        stage = new NormalStage();
        break;
      case "3":
        stage = new StrongStage();
        break;
      default:
        stage = new NormalStage();
        break;
    }
    
    // 難易度倍率を取得.
    double magnification;
    switch (difficulty) {
      case "1":
        magnification = 0.9;
        break;
      case "2":
        magnification = 1;
        break;
      case "3":
        magnification = 1.1;
        break;
      default:
        magnification = 1;
    }

    // ステージの敵すべてを保存する.
    for (EnemyBase selectEnemy : stage.getEnemyList()) {
      Enemy enemy = new Enemy();
      enemy.setAttack((int) (selectEnemy.getAttack() * magnification));
      enemy.setDefense((int) (selectEnemy.getDefense() * magnification));
      enemy.setHitPoint((int) (selectEnemy.getHitPoint() * magnification));
      enemy.setMagicAttack((int) (selectEnemy.getMagicAttack() * magnification));
      enemy.setMagicDefense((int) (selectEnemy.getMagicDefense() * magnification));
      enemy.setMaxHitPoint((int) (selectEnemy.getMaxHitPoint() * magnification));
      enemy.setName(selectEnemy.getName());
      enemy.setReward(selectEnemy.getReward());
      enemyRepository.save(enemy);
    }
  }

  /**
   * プレイヤー情報更新.
   * @param player プレイヤー.
   */
  public void updatePlayer(final Player player) {
    playerRepository.save(player);
    if (playerRepository.count() > 1) {
      // もしプレイヤー情報が複数となった場合は決して1個にする.
      playerRepository.deleteAll();
      playerRepository.save(player);
    }
  }

  /**
   * 敵情報更新.
   * @param enemyList 敵リスト.
   */
  public void updateEnemyList(final List<Enemy> enemyList) {
    int size = enemyList.size();
    enemyRepository.save(enemyList);
    if (size != enemyRepository.count()) {
      // もし情報数が異なる場合は今の情報数に合わせることとする.
      enemyRepository.deleteAll();
      enemyRepository.save(enemyList);
    }
  }

  /**
   * プレイヤー情報取得.
   * @return プレイヤー.
   */
  public Player getPlayer() {
    List<Player> list = playerRepository.findAll();
    if (list == null || list.size() == 0) {
      return null;
    } else {
      return list.get(0);
    }
  }

  /**
   * 敵情報リスト取得.
   * @return
   */
  public List<Enemy> getEnemyList() {
    return enemyRepository.findAll();
  }
  
  public void deleteAll() {
    playerRepository.deleteAll();
    enemyRepository.deleteAll();
  }
}
