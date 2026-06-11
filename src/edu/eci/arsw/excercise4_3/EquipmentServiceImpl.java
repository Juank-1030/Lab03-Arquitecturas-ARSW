package edu.eci.arsw.excercise4_3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipmentServiceImpl extends UnicastRemoteObject implements EquipmentService {
    private Map<String, Equipment> equipment = new HashMap<>();

    public EquipmentServiceImpl() throws RemoteException {
        equipment.put("LAP001", new Equipment("LAP001", "Laptop Dell XPS 15", "Lab de Computacion"));
        equipment.put("LAP002", new Equipment("LAP002", "Laptop HP Spectre", "Lab de Computacion"));
        equipment.put("OSC001", new Equipment("OSC001", "Osciloscopio Tektronix", "Lab de Electronica"));
        equipment.put("MIC001", new Equipment("MIC001", "Microscopio Olympus", "Lab de Biologia"));
        equipment.put("CEN001", new Equipment("CEN001", "Centrifuga Eppendorf", "Lab de Biologia"));
    }

    @Override
    public List<String> consultarEquipos() throws RemoteException {
        List<String> result = new ArrayList<>();
        for (Equipment eq : equipment.values()) {
            result.add(eq.toString());
        }
        return result;
    }

    @Override
    public String consultarEquipo(String codigo) throws RemoteException {
        Equipment eq = equipment.get(codigo);
        if (eq == null) return "ERROR: equipo no encontrado";
        return eq.toString();
    }

    @Override
    public boolean reservarEquipo(String codigo) throws RemoteException {
        Equipment eq = equipment.get(codigo);
        if (eq == null) return false;
        if (eq.isReserved()) return false;
        eq.setReserved(true);
        return true;
    }

    @Override
    public boolean liberarEquipo(String codigo) throws RemoteException {
        Equipment eq = equipment.get(codigo);
        if (eq == null) return false;
        if (!eq.isReserved()) return false;
        eq.setReserved(false);
        return true;
    }
}
