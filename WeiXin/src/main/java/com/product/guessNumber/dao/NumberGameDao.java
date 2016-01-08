package com.product.guessNumber.dao;

import com.product.guessNumber.po.NumberGame;
import com.product.guessNumber.vo.UserScoreVO;
import com.weixin.dao.BaseDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by harrysa66 on 2016/1/7.
 */
public class NumberGameDao extends BaseDao {

    /**
     * ������Ϸ��Ϣ
     *
     * @param game ��Ϸ����
     * @return gameId
     */
    public int saveGame(NumberGame game) {
        int gameId;
        // ������Ϸ
        Object[] params = new Object[5];
        params[0] = game.getOpenId();
        params[1] = game.getGameAnswer();
        params[2] = game.getCreateTime();
        params[3] = game.getGameStatus();
        params[4] = game.getFinishTime();
        String sql = "insert into number_game(open_id, game_answer, create_time, game_status, finish_time) values(?, ?, ?, ?, ?)";
        executeUpdate(sql, params);
        // ��ȡ��Ϸ��id
        Object[] params_get = new Object[2];
        params_get[0] = game.getOpenId();
        params_get[1] = game.getGameAnswer();
        String  sql_get = "select id from number_game where open_id=? and game_answer=? order by id desc limit 0,1";
        Object o  = executeQuerySingle(sql_get,params_get);
        gameId = Integer.parseInt(o==null?"":o.toString());
        return gameId;
    }

    /**
     * ��ȡ�û����һ�δ�������Ϸ <br>
     *
     * @param openId �û���OpendID
     * @return
     */
    public NumberGame getLastGame(String openId) {
        Object[] params = new Object[1];
        params[0] = openId;
        String sql = "select id,open_id as openId, game_answer as gameAnswer, create_time as createTime, game_status as gameStatus, finish_time as finishTime from number_game where open_id=? order by id desc limit 0,1";
        NumberGame game = (NumberGame) executeQueryObject(sql, NumberGame.class, params);
        return game;
    }

    /**
     * ������Ϸid�޸���Ϸ״̬�����ʱ��
     *
     * @param gameId ��Ϸid
     * @param gameStatus ��Ϸ״̬��0:��Ϸ�� 1:�ɹ� 2:ʧ�� 3:ȡ����
     * @param finishTime ��Ϸ���ʱ��
     */
    public int updateGame(int gameId, String gameStatus, Date finishTime) {
        Object[] params = new Object[3];
        params[0] = gameStatus;
        params[1] = finishTime;
        params[2] = gameId;
        String sql = "update number_game set game_status=?, finish_time=? where id=?";
        int line = executeUpdate(sql,params);
        return line;
    }

    /**
     * ��ȡ�û���ս��
     * @param openId
     * @return
     */
    public List<UserScoreVO> getScoreByOpenId(String openId) {
        List<UserScoreVO> scoreList = new ArrayList<UserScoreVO>();
        // ����id��������
        Object[] params = new Object[1];
        params[0] = openId;
        String sql = "select game_status as gameStatus,count(*) as count from number_game where open_id=? group by game_status order by game_status asc";
        scoreList = executeQuery(sql,UserScoreVO.class,params);
        return scoreList;
    }
}
