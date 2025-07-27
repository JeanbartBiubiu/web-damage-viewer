package xyz.game.controller.global;

import lombok.Data;

@Data
public class EncryptedData {
    private String ephemeralPublicKey;
    private String iv;
    private String data;

    public EncryptedData() {}
}