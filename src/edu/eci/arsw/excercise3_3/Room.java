package edu.eci.arsw.excercise3_3;

public class Room {
    private String code;
    private boolean reserved;

    public Room(String code) {
        this.code = code;
        this.reserved = false;
    }

    public String getCode() {
        return code;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public String toStatusText() {
        return reserved ? "SALON_RESERVADO" : "SALON_DISPONIBLE";
    }
}
