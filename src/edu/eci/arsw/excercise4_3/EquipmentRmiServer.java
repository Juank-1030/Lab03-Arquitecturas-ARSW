package edu.eci.arsw.excercise4_3;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class EquipmentRmiServer {
    public static void main(String[] args) throws Exception {
        EquipmentService service = new EquipmentServiceImpl();
        Registry registry = LocateRegistry.createRegistry(24000);
        registry.rebind("equipmentService", service);
        System.out.println("EquipmentService RMI publicado en puerto 24000...");
        System.out.println("Nombre del servicio: 'equipmentService'");
    }
}
