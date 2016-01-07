package com.product.guessNumber.po;

import java.util.Date;

/**
 * Created by harrysa66 on 2016/1/7.
 */
public class NumberGame {

    private int id;
    private String openId;
    private String gameAnswer;
    private Date createTime;
    private String gameStatus;
    private Date finishTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getGameAnswer() {
        return gameAnswer;
    }

    public void setGameAnswer(String gameAnswer) {
        this.gameAnswer = gameAnswer;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
}
