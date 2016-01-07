package com.product.guessNumber.po;

import java.util.Date;

/**
 * Created by harrysa66 on 2016/1/7.
 */
public class NumberGameRound {

    private int id;
    private int gameId;
    private String openId;
    private String guessNumber;
    private Date guessTime;
    private String guessResult;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getGuessNumber() {
        return guessNumber;
    }

    public void setGuessNumber(String guessNumber) {
        this.guessNumber = guessNumber;
    }

    public Date getGuessTime() {
        return guessTime;
    }

    public void setGuessTime(Date guessTime) {
        this.guessTime = guessTime;
    }

    public String getGuessResult() {
        return guessResult;
    }

    public void setGuessResult(String guessResult) {
        this.guessResult = guessResult;
    }
}
