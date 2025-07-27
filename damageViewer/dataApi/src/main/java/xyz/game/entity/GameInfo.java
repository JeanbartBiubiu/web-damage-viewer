package xyz.game.entity;

import java.io.Serializable;

/**
 * (GameInfo)实体类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public class GameInfo implements Serializable {
    private static final long serialVersionUID = 344407635255814702L;

    private Integer gameId;

    private String gameCode;

    private String gameName;

    private String gameImg;


    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameImg() {
        return gameImg;
    }

    public void setGameImg(String gameImg) {
        this.gameImg = gameImg;
    }

}

