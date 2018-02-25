package co.jp.systena.tigerscave.webjava.application.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import co.jp.systena.tigerscave.webjava.application.model.BattleForm;
import co.jp.systena.tigerscave.webjava.application.model.TurnForm;
import co.jp.systena.tigerscave.webjava.domain.service.BattleService;
import co.jp.systena.tigerscave.webjava.infrastructure.dao.Enemy;
import co.jp.systena.tigerscave.webjava.infrastructure.dao.Player;

@Controller  // Viewあり。Viewを返却するアノテーション
public class BattleStartController {

  @Autowired                            // UserServiceの注入（ここではinterfaceを指定）
  private BattleService service;          // 使用時にインスタンスが生成される
  @Autowired
  HttpSession session;                  // セッション管理

  enum TurnProcess {
    STANDBY,
    PLAYER,
    ENEMY
  }
  /**
   * Root処理.
   * @param mav
   * @return
   */
  @RequestMapping(value="/", method = RequestMethod.GET)
  public ModelAndView root(ModelAndView mav) {
    // バトルへリダイレクトさせる.
    return new ModelAndView("redirect:/battle");
  }

  /**
   * バトル処理.
   * @param mav
   * @return
   */
  @RequestMapping(value="/battle", method = RequestMethod.GET)          // URLとのマッピング
  public ModelAndView index(ModelAndView mav) {
    Player player = service.getPlayer();
    List<Enemy> enemyList = service.getEnemyList();
    if (player == null || enemyList == null) {
      // プレイヤーも敵もなければなので、バトル情報を入力させる.
      mav.addObject("battleForm", new BattleForm());
    } else {
      Enemy enemy = null;
      // プレイヤーが何をするかのフォーム.
      TurnForm turnForm = new TurnForm();
      // 現在何回戦かを敵のHPから計算.
      for (int i = 0; i < enemyList.size(); i++) {
        if (enemyList.get(i).getHitPoint() > 0) {
          // 0からカウントしているので+1する.
          turnForm.setTimes(String.valueOf(i + 1));
          // View側に渡す敵情報も保存.
          enemy = enemyList.get(i);
          break;
        }
      }
      if (enemy == null) {
        // nullの場合はもうすべて倒したことになる.
        // 敵とターンは最後のものにしておく.
        int turn = enemyList.size();
        enemy = enemyList.get(turn - 1);
        turnForm.setTimes(String.valueOf(turn));
      }
      mav.addObject("turnForm", turnForm);

      // プレイヤーと敵の両方がいればそれを渡す.
      mav.addObject("player", service.getPlayer());
      mav.addObject("enemy", enemy);

      // メッセージ情報を設定する.
      List<String> messageList = new ArrayList<>();
      // プレイヤーの攻撃.
      Object temp = session.getAttribute("playerAttackDamage");
      if (temp != null) {
        messageList.add("プレイヤーの攻撃！");
        messageList.add("敵に" + temp + "ダメージあたえた！");
        session.removeAttribute("playerAttackDamage");
      }
      // プレイヤーの魔法攻撃.
      temp = session.getAttribute("playerMagicDamage");
      if (temp != null) {
        messageList.add("プレイヤーの魔法攻撃！");
        messageList.add("敵に" + temp + "ダメージあたえた！");
        session.removeAttribute("playerMagicDamage");
      }
      // プレイヤーの回復.
      temp = session.getAttribute("playerHeal");
      if (temp != null) {
        if (player.getHitPoint() >= player.getMaxHitPoint()) {
          messageList.add("プレイヤーは回復アイテムを使った！");
          messageList.add("プレイヤーのＨＰは満タンになった！");
        } else {
          messageList.add("プレイヤーは回復アイテムを使った！");
          messageList.add("プレイヤーは" + temp + "ＨＰ回復した！");
        }
        session.removeAttribute("playerHeal");
      }
      // プレイヤーの逃げ.
      temp = session.getAttribute("playerAway");
      if (temp != null) {
        messageList.add("プレイヤーは逃げようとした！");
        if ((boolean)temp) {
          messageList.add("なんと成功した！敵から逃げ切った！");
        } else {
          messageList.add("失敗した！");
        }
        session.removeAttribute("playerAway");
      }
      messageList.add("　");
      // 敵の攻撃.
      temp = session.getAttribute("enemyDamage");
      if (temp != null) {
        if ((int)temp == -1) {
          messageList.add("敵を倒した！");
          temp = session.getAttribute("playerReward");
          if (temp != null) {
            messageList.add("回復アイテムがドロップした！");
            if (player.getHitPoint() >= player.getMaxHitPoint()) {
              messageList.add("プレイヤーのＨＰは満タンになった！");
            } else {
              messageList.add("プレイヤーはＨＰを" + temp + "回復した！");
            }
          }
        } else if ((int)temp == -2) {
          // クリア済みなので何もしない.
        } else {
          messageList.add("敵の攻撃！");
          messageList.add("プレイヤーは" + temp + "ダメージをうけた！");
          if (player.getHitPoint() <= 0) {
            messageList.add("プレイヤーは倒されてしまった！");
          }
        }
        session.removeAttribute("enemyDamage");
      }

      // 作成したメッセージリストを追加.
      mav.addObject("messageList", messageList);

    }

    // Viewのテンプレート名を設定
    mav.setViewName("battle");
    return mav;
  }


  /**
   * プレイヤーとステージ選択時処理.
   * @param mav
   * @param battleForm
   * @param bindingResult
   * @param request
   * @return
   */
  @RequestMapping(value="/battle", method = RequestMethod.POST)  // URLとのマッピング
  private ModelAndView selectPlayerAndStage(ModelAndView mav, @Valid BattleForm battleForm, BindingResult bindingResult, HttpServletRequest request) {
    // プレイヤーとステージ情報を保存.
    service.init(battleForm.getPlayer(), battleForm.getStage(), battleForm.getDifficulty());

    session.setAttribute("form", battleForm);

    return new ModelAndView("redirect:/battle");        // リダイレクト
  }

  /**
   * プレイヤーのターンセレクト処理.
   * @param mav
   * @param turnForm
   * @param bindingResult
   * @param request
   * @return
   */
  @RequestMapping(value="/battle/turn", method = RequestMethod.POST)  // URLとのマッピング
  private ModelAndView turn(ModelAndView mav, @Valid TurnForm turnForm, BindingResult bindingResult, HttpServletRequest request) {

    List<Enemy> enemyList = service.getEnemyList();
    int count;
    for (count = 0; count < enemyList.size(); count++) {
      if (enemyList.get(count).getHitPoint() > 0) {
        break;
      }
    }
    // プレイヤーが選択したものを取得.
    String playerSelect = turnForm.getSelect();

    boolean isRunAway = false;
    switch (playerSelect) {
      case "attack":
        int attackDamage = attackPlayer(false);
        session.setAttribute("playerAttackDamage", attackDamage);
        break;
      case "magic":
        int magicDamage = attackPlayer(true);
        session.setAttribute("playerMagicDamage", magicDamage);
        break;
      case "heal":
        int heal = healPlayer();
        session.setAttribute("playerHeal", heal);
        break;
      case "away":
        isRunAway = runAway();
        session.setAttribute("playerAway", isRunAway);
        break;
      default:
        break;
    }

    // 逃げが成功していない場合のみ.
    if (!isRunAway) {
      // 敵が倒されたかどうかを確認する.
      int enemyDamage = -1;
      if (enemyList.get(count).getHitPoint() > 0) {
        // HPが残っているなら敵の攻撃.
        enemyDamage = attackEnemy();
      } else {
        // 残っていないならプレイヤー回復.
        int reward = enemyList.get(count).getReward();
        Player player = service.getPlayer();
        if (player.getHitPoint() + reward > player.getMaxHitPoint()) {
          player.setHitPoint(player.getMaxHitPoint());
        } else {
          player.setHitPoint(player.getHitPoint() + reward);
        }
        service.updatePlayer(player);
        session.setAttribute("playerReward", reward);
      }
      session.setAttribute("enemyDamage", enemyDamage);
    }

    return new ModelAndView("redirect:/battle");        // リダイレクト
  }

  /**
   * ゲーム終了時処理.
   * @param mav
   * @param bindingResult
   * @param request
   * @return
   */
  @RequestMapping(value="/battle/end", method = RequestMethod.POST)  // URLとのマッピング
  private ModelAndView turn(ModelAndView mav, BindingResult bindingResult, HttpServletRequest request) {
    // データは初期化.
    service.deleteAll();
    return new ModelAndView("redirect:/battle");        // リダイレクト
  }

  /**
   * プレイヤーの攻撃.
   * @param isMagic 魔法か.
   * @return ダメージ数.
   */
  private int attackPlayer(final boolean isMagic) {
    // プレイヤーを取得.
    Player player = service.getPlayer();

    // 現在の敵を取得。
    List<Enemy> enemyList = service.getEnemyList();
    Enemy currentEnemy = null;
    for (Enemy enemy : enemyList) {
      if (enemy.getHitPoint() > 0) {
        currentEnemy = enemy;
        break;
      }
    }

    // プレイヤーの攻撃値を取得.
    int attack = isMagic ? player.getMagicAttack() : player.getAttack();
    // 攻撃は1.5～2.0倍にする.
    attack = (int) (attack * (Math.random()  * 0.5 + 1.5));
    // プレイヤーの攻撃は1.5倍する.
    attack = (int)(attack * 1.5);

    // 10%の確率で急所.
    int random = (int) (Math.random() * 100);
    attack = random % 10 != 0 ? attack : attack * 2;

    int defense = isMagic ? currentEnemy.getMagicDefense() : currentEnemy.getDefense();

    // ダメージを計算.
    int damage = attack - defense;
    if (damage <= 0) {
      // 最低でも1ダメージ.
      damage = 1;
    }

    // 負にならないようにする.
    if (currentEnemy.getHitPoint() - damage >= 0) {
      currentEnemy.setHitPoint(currentEnemy.getHitPoint() - damage);
    } else {
      currentEnemy.setHitPoint(0);
    }
    service.updateEnemyList(enemyList);

    return damage;
  }

  /**
   * 敵の攻撃.
   * @return
   */
  private int attackEnemy() {
    // プレイヤーを取得.
    Player player = service.getPlayer();

    // 現在の敵を取得。
    List<Enemy> enemyList = service.getEnemyList();
    Enemy currentEnemy = null;
    for (Enemy enemy : enemyList) {
      if (enemy.getHitPoint() > 0) {
        currentEnemy = enemy;
        break;
      }
    }
    if (currentEnemy == null || currentEnemy.getHitPoint() <= 0) {
      return -2;
    }

    boolean isMagic;
    if (currentEnemy.getAttack() == currentEnemy.getMagicAttack()) {
      // 攻撃力と魔法攻撃力が同じ場合はどちらで攻撃するかランダム.
      int random = (int) (Math.random() * 2);
      isMagic = random == 0;
    } else {
      isMagic = currentEnemy.getMagicAttack() > currentEnemy.getAttack();
    }

    // 敵の攻撃値を取得.
    int attack = isMagic ? currentEnemy.getMagicAttack() : currentEnemy.getAttack();
    // 攻撃は1.5～2.0倍にする.
    attack = (int) (attack * (Math.random()  * 0.5 + 1.5));

    // 10%の確率で急所.
    int random = (int) (Math.random() * 100);
    attack = random % 10 != 0 ? attack : attack * 2;

    int defense = isMagic ? player.getMagicDefense() : player.getDefense();

    // ダメージを計算.
    int damage = attack - defense;
    if (damage <= 0) {
      // 最低でも1ダメージ.
      damage = 1;
    }

    // 負にならないようにする.
    if (player.getHitPoint() - damage >= 0) {
      player.setHitPoint(player.getHitPoint() - damage);
    } else {
      player.setHitPoint(0);
    }
    service.updatePlayer(player);

    return damage;
  }

  /**
   * プレイヤーの回復.
   * @return 回復値.
   */
  private int healPlayer() {
    // 30の0.5～1.5倍分回復する.
    int recovery = (int) (30 * (Math.random() + 0.5));
    Player player = service.getPlayer();
    if (player.getHitPoint() + recovery >= player.getMaxHitPoint()) {
      player.setHitPoint(player.getMaxHitPoint());
    } else {
      player.setHitPoint(player.getHitPoint() + recovery);
    }
    service.updatePlayer(player);
    return recovery;
  }
  
  /**
   * 逃げる処理.
   * @return
   */
  private boolean runAway() {
    int random = (int) (Math.random() * 100);
    if (random % 5 == 0) {
      List<Enemy> enemyList = service.getEnemyList();
      for (Enemy enemy : enemyList) {
        if (enemy.getHitPoint() > 0) {
          enemy.setHitPoint(0);
          break;
        }
      }
      service.updateEnemyList(enemyList);
      return true;
    } else {
      return false;
    }
  }
}
