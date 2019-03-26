package com.example.freakdeveloper.kep.model;

public class Ranking {
    private String NickName;
    private String Total;
    private String TotalBuenas;

    public Ranking(String nickName, String total, String totalBuenas) {
        NickName = nickName;
        Total = total;
        TotalBuenas = totalBuenas;
    }

    public Ranking() {
    }
}
