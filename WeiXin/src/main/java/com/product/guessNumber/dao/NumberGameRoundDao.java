package com.product.guessNumber.dao;

import com.product.guessNumber.po.NumberGameRound;
import com.weixin.dao.BaseDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harrysa66 on 2016/1/7.
 */
public class NumberGameRoundDao extends BaseDao {
    /**
     * 保存游戏的回合信息
     *
     * @param gameRound 游戏回合对象
     */
    public int saveGameRound(NumberGameRound gameRound) {
        // 保存游戏回合
        Object[] params = new Object[5];
        params[0] = gameRound.getGameId();
        params[1] = gameRound.getOpenId();
        params[2] = gameRound.getGuessNumber();
        params[3] = gameRound.getGuessTime();
        params[4] = gameRound.getGuessResult();
        String sql = "insert into number_game_round(game_id, open_id, guess_number, guess_time, guess_result) values (?, ?, ?, ?, ?)";
        int line = executeUpdate(sql, params);
        return line;
    }

    /**
     * 根据游戏id获取游戏的全部回合<br>
     *
     * @param gameId 游戏id
     * @return
     */
    public List<NumberGameRound> findAllRoundByGameId(int gameId) {
        List<NumberGameRound> roundList = new ArrayList<NumberGameRound>();
        // 根据id升序排序
        Object[] params = new Object[1];
        params[0] = gameId;
        String sql = "select id,game_id as gameId, open_id as openId, guess_number as guessNumber, guess_time as guessTime, guess_result as guessResult from number_game_round where game_id=? order by id asc";
        roundList = executeQuery(sql,NumberGameRound.class,params);
        return roundList;
    }
}
