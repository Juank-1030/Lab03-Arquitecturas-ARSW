package edu.eci.arsw.excercise4_3;

import java.io.Serializable;

public class Equipment implements Serializable {
    private String code;
    private String name;
    private String lab;
    private boolean reserved;

    public Equipment(String code, String name, String lab) {
        this.code = code;
        this.name = name;
        this.lab = lab;
        this.reserved = false;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getLab() {
        return lab;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    @Override
    public String toString() {
        return code + " - " + name + " (" + lab + ") - " + (reserved ? "RESERVADO" : "DISPONIBLE");
    }
}
