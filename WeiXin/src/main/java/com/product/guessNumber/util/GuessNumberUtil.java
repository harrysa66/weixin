package com.product.guessNumber.util;

import com.product.guessNumber.dao.NumberGameDao;
import com.product.guessNumber.dao.NumberGameRoundDao;
import com.product.guessNumber.po.NumberGame;
import com.product.guessNumber.po.NumberGameRound;
import com.product.guessNumber.vo.UserScore;
import com.weixin.util.DateUtil;
import com.weixin.util.MessageUtil;

import java.util.List;

/**
 * Created by harrysa66 on 2016/1/7.
 */
public class GuessNumberUtil {

    static NumberGameDao gameDao = new NumberGameDao();
    static NumberGameRoundDao gameRoundDao = new NumberGameRoundDao();

    /**
     * 进行游戏
     * @param toUserName
     * @param fromUserName
     * @param content
     * @return
     */
    public static String processGame(String toUserName, String fromUserName, String content) {
        String message = "";
        // 查看游戏帮助
        if (content.equalsIgnoreCase("help")) {
            message = MessageUtil.initText(toUserName, fromUserName, getGameRule());
        }
        // 查看游戏战绩
        else if (content.equalsIgnoreCase("score")) {
            message = MessageUtil.initText(toUserName, fromUserName, getUserScore(fromUserName));
        }
        // 查看游戏战绩
        else if (content.equalsIgnoreCase("end")) {
            message = MessageUtil.initText(toUserName, fromUserName, endGame(fromUserName));
        }
        // 如果是n（n ≥ 4）位数字并且无重复
        else if (GameUtil.verifyNumber(content,content.length()) && !GameUtil.verifyRepeat(content)) {
            message = MessageUtil.initText(toUserName, fromUserName, process(fromUserName,content));
        }
        // 输入的格式错误
        else {
            message = MessageUtil.initText(toUserName, fromUserName, "请输入n（n ≥ 4）个不重复的数字，例如：0269");
        }
        return message;
    }

    /**
     * “猜数字”游戏玩法
     *
     * @return String
     */
    private static String getGameRule() {
        StringBuffer sb = new StringBuffer();
        sb.append("《猜数字游戏玩法》").append("\n");
        sb.append("系统设定一个没有重复数字的n（n ≥ 4）位数，由玩家来猜，每局10次机会。").append("\n");
        sb.append("每猜一次，系统会给出猜测结果xAyB，x表示数字与位置均正确的数的个数，y表示数字正确但位置不对的数的个数。").append("\n");
        sb.append("玩家根据猜测结果xAyB一直猜，直到猜中(nA0B)为止。").append("\n");
        sb.append("如果10次都没猜中，系统会公布答案，游戏结束。").append("\n");
        sb.append("玩家任意输入一个没有重复数字的n（n ≥ 4）位数即开始游戏，例如：7890").append("\n\n");
        return sb.toString();
    }

    /**
     * “猜数字”游戏战绩
     *
     * @param openId 用户的OpenID
     * @return
     */
    private static String getUserScore(String openId) {
        StringBuffer sb = new StringBuffer();
        List<UserScore> scoreList = gameDao.getScoreByOpenId(openId);
        if (null == scoreList || 0 == scoreList.size()) {
            sb.append("您还没有玩过猜数字游戏！").append("\n");
            sb.append("请输入n（n ≥ 4）个不重复的数字开始新的一局，例如：0269");
        } else {
            sb.append("您的游戏战绩如下：").append("\n");
            for (UserScore score : scoreList) {
                if (score.getGameStatus().equals("0")) {
                    sb.append("游戏中：").append(score.getCount()).append("\n");
                } else if (score.getGameStatus().equals("1")) {
                    sb.append("胜利局数：").append(score.getCount()).append("\n");
                } else if (score.getGameStatus().equals("2")) {
                    sb.append("失败局数：").append(score.getCount()).append("\n");
                } else if (score.getGameStatus().equals("3")) {
                    sb.append("终止局数：").append(score.getCount()).append("\n");
                }
            }
            sb.append("请输入n（n ≥ 4）个不重复的数字开始新的一局，例如：0269");
        }
        return sb.toString();
    }

    /**
     * 结束本局游戏
     * @param openId
     * @return
     */
    private static String endGame(String openId) {
        StringBuffer sb = new StringBuffer();
        NumberGame game = gameDao.getLastGame(openId);
        if (null == game) {
            sb.append("您还没有玩过猜数字游戏！").append("\n");
            sb.append("请输入n（n ≥ 4）个不重复的数字开始新的一局，例如：0269");
        }else{
            gameDao.updateGame(game.getId(), "3", DateUtil.getCurrentDate());
            sb.append("您已结束本局猜数字游戏！").append("\n");
            sb.append("请输入n（n ≥ 4）个不重复的数字开始新的一局，例如：0269");
        }
        return sb.toString();
    }

    /**
     * 开始游戏
     * @param openId
     * @param content
     * @return
     */
    private static String process(String openId, String content) {
        StringBuffer sb = new StringBuffer();
        // 获取用户最近一次创建的游戏
        NumberGame game = gameDao.getLastGame(openId);
        // 本局游戏的正确答案
        String gameAnswer = null;
        // 本回合的猜测结果
        String guessResult = null;
        // 本局游戏的id
        int gameId = -1;
        // 是否是新的一局
        boolean newGameFlag = (null == game || !game.getGameStatus().equals("0"));
        // 新的一局
        if (newGameFlag) {
            // 生成不重复的n位随机数作为答案
            gameAnswer = GameUtil.generateRandNumber(content.length());
            // 计算本回合“猜数字”的结果（xAyB）
            guessResult = GameUtil.guessResult(gameAnswer, content,content.length());
            // 创建game
            game = new NumberGame();
            game.setOpenId(openId);
            game.setGameAnswer(gameAnswer);
            game.setCreateTime(DateUtil.getCurrentDate());
            game.setGameStatus("0");
            // 保存game并获取id
            gameId = gameDao.saveGame(game);
        }
        // 不是新的一局
        else {
            if(content.length() != game.getGameAnswer().length()){
                sb.append("上局游戏为").append(game.getGameAnswer().length()).append("位数！").append("\n");
                sb.append("请重新输入数字或输入“end”结束本局游戏。");
                return sb.toString();
            }
            gameAnswer = game.getGameAnswer();
            guessResult = GameUtil.guessResult(gameAnswer, content,content.length());
            gameId = game.getId();
        }
        // 保存当前游戏回合
        NumberGameRound gameRound = new NumberGameRound();
        gameRound.setGameId(gameId);
        gameRound.setOpenId(openId);
        gameRound.setGuessNumber(content);
        gameRound.setGuessTime(DateUtil.getCurrentDate());
        gameRound.setGuessResult(guessResult);
        gameRoundDao.saveGameRound(gameRound);
        // 查询本局游戏的所有回合
        List<NumberGameRound> roundList = gameRoundDao.findAllRoundByGameId(gameId);
        // 遍历游戏回合，组装给用户回复的消息
        sb.append("查看玩法请回复：help").append("\n");
        sb.append("查看战绩请回复：score").append("\n");
        for (int i = 0; i < roundList.size(); i++) {
            gameRound = roundList.get(i);
            sb.append(String.format("第%d回合： %s    %s", i + 1, gameRound.getGuessNumber(), gameRound.getGuessResult())).append("\n");
        }
        // 猜对
        if (guessResult.equals(content.length()+"A0B")) {
            sb.append("正确答案：").append(gameAnswer).append("\n");
            sb.append("恭喜您猜对了！[强]").append("\n");
            sb.append("重新输入4个不重复的数字开始新的一局。");
            gameDao.updateGame(gameId, "1", DateUtil.getCurrentDate());
        }
        // 10回合仍没猜对
        else if (roundList.size() >= 10) {
            sb.append("正确答案：").append(gameAnswer).append("\n");
            sb.append("唉，10回合都没猜出来，本局结束！[流泪]").append("\n");
            sb.append("重新输入4个不重复的数字开始新的一局。");
            gameDao.updateGame(gameId, "2", DateUtil.getCurrentDate());
        }
        // 本回合没猜对
        else {
            sb.append("请再接再励！");
        }
        return sb.toString();
    }

}
