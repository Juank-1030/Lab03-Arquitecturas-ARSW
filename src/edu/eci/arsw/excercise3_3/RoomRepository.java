package edu.eci.arsw.excercise3_3;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RoomRepository {
    private Map<String, Room> rooms = new HashMap<>();

    public RoomRepository() {
        rooms.put("E301", new Room("E301"));
        rooms.put("E302", new Room("E302"));
        rooms.put("E303", new Room("E303"));
        rooms.put("E304", new Room("E304"));
    }

    public Room findById(String code) {
        return rooms.get(code);
    }

    public Collection<Room> findAll() {
        return rooms.values();
    }

    public String consult(String code) {
        Room room = rooms.get(code);
        if (room == null) return "ERROR_SALON_NO_EXISTE";
        return room.toStatusText();
    }

    public String reserve(String code) {
        Room room = rooms.get(code);
        if (room == null) return "ERROR_SALON_NO_EXISTE";
        if (room.isReserved()) return "ERROR_SALON_RESERVADO";
        room.setReserved(true);
        return "RESERVA_EXITOSA";
    }

    public String release(String code) {
        Room room = rooms.get(code);
        if (room == null) return "ERROR_SALON_NO_EXISTE";
        if (!room.isReserved()) return "ERROR_OPERACION_INVALIDA";
        room.setReserved(false);
        return "LIBERACION_EXITOSA";
    }
}
