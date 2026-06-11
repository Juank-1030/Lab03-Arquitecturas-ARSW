package edu.eci.arsw.excercise4_3;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class EquipmentRmiClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 24000);
        EquipmentService service = (EquipmentService) registry.lookup("equipmentService");

        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Sistema de Inventario de Laboratorios ===");

        while (true) {
            System.out.println();
            System.out.println("Operaciones disponibles:");
            System.out.println("  1. Listar todos los equipos");
            System.out.println("  2. Consultar un equipo");
            System.out.println("  3. Reservar un equipo");
            System.out.println("  4. Liberar un equipo");
            System.out.println("  5. Salir");
            System.out.print("Seleccione una opcion: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    List<String> equipos = service.consultarEquipos();
                    System.out.println("Equipos disponibles:");
                    for (String eq : equipos) {
                        System.out.println("  " + eq);
                    }
                    break;
                case "2":
                    System.out.print("Ingrese codigo del equipo: ");
                    String codConsultar = scanner.nextLine().trim();
                    System.out.println("Resultado: " + service.consultarEquipo(codConsultar));
                    break;
                case "3":
                    System.out.print("Ingrese codigo del equipo: ");
                    String codReservar = scanner.nextLine().trim();
                    boolean reservo = service.reservarEquipo(codReservar);
                    System.out.println(reservo ? "RESERVA_EXITOSA" : "ERROR: no se pudo reservar (no existe o ya reservado)");
                    break;
                case "4":
                    System.out.print("Ingrese codigo del equipo: ");
                    String codLiberar = scanner.nextLine().trim();
                    boolean libero = service.liberarEquipo(codLiberar);
                    System.out.println(libero ? "LIBERACION_EXITOSA" : "ERROR: no se pudo liberar (no existe o ya disponible)");
                    break;
                case "5":
                    System.out.println("Saliendo...");
                    return;
                default:
                    System.out.println("Opcion invalida. Use 1-5.");
            }
        }
    }
}
