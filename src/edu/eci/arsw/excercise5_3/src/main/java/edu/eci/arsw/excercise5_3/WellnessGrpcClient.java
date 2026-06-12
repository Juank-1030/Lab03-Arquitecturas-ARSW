package edu.eci.arsw.excercise5_3;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Scanner;

public class WellnessGrpcClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50061)
                .usePlaintext()
                .build();

        AppointmentServiceGrpc.AppointmentServiceBlockingStub stub =
                AppointmentServiceGrpc.newBlockingStub(channel);

        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Sistema de Bienestar Universitario ===");

        while (true) {
            System.out.println();
            System.out.println("Operaciones disponibles:");
            System.out.println("  1. Solicitar cita");
            System.out.println("  2. Cancelar cita");
            System.out.println("  3. Consultar citas de un estudiante");
            System.out.println("  4. Salir");
            System.out.print("Seleccione una opcion: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    System.out.print("ID del estudiante: ");
                    String studentId = scanner.nextLine().trim();
                    System.out.print("Tipo de servicio (MEDICINE, PSYCHOLOGY, DENTISTRY): ");
                    String serviceTypeStr = scanner.nextLine().trim().toUpperCase();
                    ServiceType serviceType;
                    try {
                        serviceType = ServiceType.valueOf(serviceTypeStr);
                    } catch (IllegalArgumentException e) {
                        System.out.println("ERROR: tipo de servicio invalido");
                        break;
                    }
                    System.out.print("Fecha (YYYY-MM-DD): ");
                    String date = scanner.nextLine().trim();

                    AppointmentRequest request = AppointmentRequest.newBuilder()
                            .setStudentId(studentId)
                            .setServiceType(serviceType)
                            .setDate(date)
                            .build();
                    AppointmentResponse response = stub.requestAppointment(request);
                    if (response.getSuccess()) {
                        System.out.println(response.getMessage());
                        Appointment a = response.getAppointment();
                        System.out.println("  ID cita: " + a.getId()
                                + " | Fecha: " + a.getDate()
                                + " | Estado: " + a.getStatus());
                    } else {
                        System.out.println(response.getMessage());
                    }
                    break;

                case "2":
                    System.out.print("ID de la cita a cancelar: ");
                    String appointmentId = scanner.nextLine().trim();
                    CancelRequest cancelReq = CancelRequest.newBuilder()
                            .setAppointmentId(appointmentId)
                            .build();
                    CancelResponse cancelRes = stub.cancelAppointment(cancelReq);
                    System.out.println(cancelRes.getMessage());
                    break;

                case "3":
                    System.out.print("ID del estudiante: ");
                    String consultStudentId = scanner.nextLine().trim();
                    StudentRequest studentReq = StudentRequest.newBuilder()
                            .setStudentId(consultStudentId)
                            .build();
                    AppointmentList list = stub.getAppointments(studentReq);
                    if (list.getAppointmentsCount() == 0) {
                        System.out.println("No se encontraron citas para el estudiante " + consultStudentId);
                    } else {
                        System.out.println("Citas de " + consultStudentId + ":");
                        for (Appointment a : list.getAppointmentsList()) {
                            System.out.println("  " + a.getId()
                                    + " | " + a.getServiceType()
                                    + " | " + a.getDate()
                                    + " | " + a.getStatus());
                        }
                    }
                    break;

                case "4":
                    System.out.println("Saliendo...");
                    channel.shutdown();
                    return;

                default:
                    System.out.println("Opcion invalida. Use 1-4.");
            }
        }
    }
}