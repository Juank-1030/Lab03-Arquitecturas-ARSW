package edu.eci.arsw.excercise6_3;

import edu.eci.arsw.excercise6_3.appointment.*;
import edu.eci.arsw.excercise6_3.medical.*;
import edu.eci.arsw.excercise6_3.gym.*;
import edu.eci.arsw.excercise6_3.recreation.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Scanner;

public class WellnessClient {
    public static void main(String[] args) {
        ManagedChannel appointmentChannel = ManagedChannelBuilder
                .forAddress("localhost", 50061).usePlaintext().build();
        ManagedChannel medicalChannel = ManagedChannelBuilder
                .forAddress("localhost", 50062).usePlaintext().build();
        ManagedChannel gymChannel = ManagedChannelBuilder
                .forAddress("localhost", 50063).usePlaintext().build();
        ManagedChannel recreationChannel = ManagedChannelBuilder
                .forAddress("localhost", 50064).usePlaintext().build();

        AppointmentServiceGrpc.AppointmentServiceBlockingStub appointmentStub =
                AppointmentServiceGrpc.newBlockingStub(appointmentChannel);
        MedicalServiceGrpc.MedicalServiceBlockingStub medicalStub =
                MedicalServiceGrpc.newBlockingStub(medicalChannel);
        GymServiceGrpc.GymServiceBlockingStub gymStub =
                GymServiceGrpc.newBlockingStub(gymChannel);
        RecreationServiceGrpc.RecreationServiceBlockingStub recreationStub =
                RecreationServiceGrpc.newBlockingStub(recreationChannel);

        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Sistema de Bienestar (Microservicios) ===");

        while (true) {
            System.out.println("\n--- Menu Principal ---");
            System.out.println("Citas:");
            System.out.println("  1. Solicitar cita");
            System.out.println("  2. Cancelar cita");
            System.out.println("  3. Listar citas por estudiante");
            System.out.println("  4. Eliminar cita (permanente)");
            System.out.println("  5. Reprogramar cita");
            System.out.println("Especialidades Medicas:");
            System.out.println("  6. Consultar especialidad");
            System.out.println("  7. Listar especialidades");
            System.out.println("  8. Agregar especialidad");
            System.out.println("  9. Eliminar especialidad");
            System.out.println("Gimnasio:");
            System.out.println("  10. Reservar sesion");
            System.out.println("  11. Consultar sesiones por estudiante");
            System.out.println("  12. Cancelar sesion");
            System.out.println("  13. Listar todas las sesiones");
            System.out.println("Recreacion:");
            System.out.println("  14. Reservar recurso");
            System.out.println("  15. Listar recursos");
            System.out.println("  16. Devolver recurso");
            System.out.println("  17. Agregar recurso");
            System.out.println("  0. Salir");
            System.out.print("Seleccione una opcion: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                // --- Citas ---
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
                    AppointmentResponse response = appointmentStub.requestAppointment(
                            AppointmentRequest.newBuilder()
                                    .setStudentId(studentId)
                                    .setServiceType(serviceType)
                                    .setDate(date)
                                    .build());
                    if (response.getSuccess()) {
                        System.out.println(response.getMessage());
                        System.out.println("  ID cita: " + response.getAppointment().getId()
                                + " | Fecha: " + response.getAppointment().getDate()
                                + " | Estado: " + response.getAppointment().getStatus());
                    } else {
                        System.out.println(response.getMessage());
                    }
                    break;

                case "2":
                    System.out.print("ID de la cita a cancelar: ");
                    String cancelId = scanner.nextLine().trim();
                    CancelResponse cancelResponse = appointmentStub.cancelAppointment(
                            CancelRequest.newBuilder().setAppointmentId(cancelId).build());
                    System.out.println(cancelResponse.getMessage());
                    break;

                case "3":
                    System.out.print("ID del estudiante: ");
                    String listStudentId = scanner.nextLine().trim();
                    AppointmentList listResponse = appointmentStub.getAppointments(
                            StudentRequest.newBuilder().setStudentId(listStudentId).build());
                    System.out.println("Citas de " + listStudentId + ":");
                    for (Appointment a : listResponse.getAppointmentsList()) {
                        System.out.println("  " + a.getId() + " | " + a.getServiceType()
                                + " | " + a.getDate() + " | " + a.getStatus());
                    }
                    break;

                case "4":
                    System.out.print("ID de la cita a eliminar: ");
                    String deleteId = scanner.nextLine().trim();
                    DeleteAppointmentResponse deleteResponse = appointmentStub.deleteAppointment(
                            DeleteAppointmentRequest.newBuilder().setAppointmentId(deleteId).build());
                    System.out.println(deleteResponse.getMessage());
                    break;

                case "5":
                    System.out.print("ID de la cita: ");
                    String updateId = scanner.nextLine().trim();
                    System.out.print("Nueva fecha (YYYY-MM-DD): ");
                    String newDate = scanner.nextLine().trim();
                    UpdateDateResponse updateResponse = appointmentStub.updateAppointmentDate(
                            UpdateDateRequest.newBuilder()
                                    .setAppointmentId(updateId).setNewDate(newDate).build());
                    System.out.println(updateResponse.getMessage());
                    if (updateResponse.getSuccess()) {
                        System.out.println("  Nueva fecha: " + updateResponse.getAppointment().getDate());
                    }
                    break;

                // --- Especialidades Medicas ---
                case "6":
                    System.out.print("Codigo de especialidad: ");
                    String code = scanner.nextLine().trim();
                    SpecialtyResponse specialty = medicalStub.getSpecialty(
                            SpecialtyRequest.newBuilder().setCode(code).build());
                    if (specialty.getFound()) {
                        System.out.println("Especialidad: " + specialty.getName());
                        System.out.println("Descripcion: " + specialty.getDescription());
                        System.out.println("Disponible: " + (specialty.getAvailable() ? "Si" : "No"));
                    } else {
                        System.out.println("Especialidad no encontrada");
                    }
                    break;

                case "7":
                    SpecialtyList specialtyList = medicalStub.listSpecialties(MedicalEmpty.newBuilder().build());
                    System.out.println("Especialidades disponibles:");
                    for (SpecialtyResponse s : specialtyList.getSpecialtiesList()) {
                        System.out.println("  " + s.getCode() + " - " + s.getName()
                                + " (" + (s.getAvailable() ? "Disponible" : "No disponible") + ")");
                    }
                    break;

                case "8":
                    System.out.print("Codigo de especialidad: ");
                    String newCode = scanner.nextLine().trim();
                    System.out.print("Nombre: ");
                    String newName = scanner.nextLine().trim();
                    System.out.print("Descripcion: ");
                    String newDesc = scanner.nextLine().trim();
                    AddSpecialtyResponse addResponse = medicalStub.addSpecialty(
                            AddSpecialtyRequest.newBuilder()
                                    .setCode(newCode).setName(newName)
                                    .setDescription(newDesc).setAvailable(true).build());
                    System.out.println(addResponse.getMessage());
                    break;

                case "9":
                    System.out.print("Codigo de especialidad a eliminar: ");
                    String removeCode = scanner.nextLine().trim();
                    RemoveSpecialtyResponse removeResponse = medicalStub.removeSpecialty(
                            RemoveSpecialtyRequest.newBuilder().setCode(removeCode).build());
                    System.out.println(removeResponse.getMessage());
                    break;

                // --- Gimnasio ---
                case "10":
                    System.out.print("ID del estudiante: ");
                    String gymStudentId = scanner.nextLine().trim();
                    System.out.print("Horario (ej: Lunes 10:00): ");
                    String timeSlot = scanner.nextLine().trim();
                    SessionResponse sessionResponse = gymStub.reserveSession(
                            SessionRequest.newBuilder()
                                    .setStudentId(gymStudentId).setTimeSlot(timeSlot).build());
                    System.out.println(sessionResponse.getMessage());
                    if (sessionResponse.getSuccess()) {
                        System.out.println("  ID sesion: " + sessionResponse.getSessionId());
                    }
                    break;

                case "11":
                    System.out.print("ID del estudiante: ");
                    String gymStudentId2 = scanner.nextLine().trim();
                    SessionList sessionsByStudent = gymStub.getSessions(
                            StudentSessionsRequest.newBuilder().setStudentId(gymStudentId2).build());
                    System.out.println("Sesiones de " + gymStudentId2 + ":");
                    for (GymSession s : sessionsByStudent.getSessionsList()) {
                        System.out.println("  " + s.getId() + " | " + s.getTimeSlot()
                                + " | " + (s.getActive() ? "Activa" : "Cancelada"));
                    }
                    break;

                case "12":
                    System.out.print("ID de la sesion a cancelar: ");
                    String cancelSessionId = scanner.nextLine().trim();
                    CancelSessionResponse cancelSessionResponse = gymStub.cancelSession(
                            CancelSessionRequest.newBuilder().setSessionId(cancelSessionId).build());
                    System.out.println(cancelSessionResponse.getMessage());
                    break;

                case "13":
                    SessionList allSessions = gymStub.getAllSessions(GymEmpty.newBuilder().build());
                    System.out.println("Todas las sesiones:");
                    for (GymSession s : allSessions.getSessionsList()) {
                        System.out.println("  " + s.getId() + " | Estudiante: " + s.getStudentId()
                                + " | " + s.getTimeSlot()
                                + " | " + (s.getActive() ? "Activa" : "Cancelada"));
                    }
                    break;

                // --- Recreacion ---
                case "14":
                    System.out.print("ID del estudiante: ");
                    String recStudentId = scanner.nextLine().trim();
                    System.out.print("ID del recurso: ");
                    String resourceId = scanner.nextLine().trim();
                    ResourceResponse resourceResponse = recreationStub.reserveResource(
                            ResourceRequest.newBuilder()
                                    .setStudentId(recStudentId).setResourceId(resourceId).build());
                    System.out.println(resourceResponse.getMessage());
                    break;

                case "15":
                    ResourceList resourceList = recreationStub.listResources(
                            RecreationEmpty.newBuilder().build());
                    System.out.println("Recursos recreativos:");
                    for (RecreationResource r : resourceList.getResourcesList()) {
                        System.out.println("  " + r.getId() + " - " + r.getName()
                                + " (" + (r.getAvailable() ? "Disponible" : "Reservado") + ")");
                    }
                    break;

                case "16":
                    System.out.print("ID del recurso a devolver: ");
                    String returnId = scanner.nextLine().trim();
                    ReturnResourceResponse returnResponse = recreationStub.returnResource(
                            ReturnResourceRequest.newBuilder().setResourceId(returnId).build());
                    System.out.println(returnResponse.getMessage());
                    break;

                case "17":
                    System.out.print("ID del nuevo recurso: ");
                    String newRecId = scanner.nextLine().trim();
                    System.out.print("Nombre del recurso: ");
                    String newRecName = scanner.nextLine().trim();
                    AddResourceResponse addRecResponse = recreationStub.addResource(
                            AddResourceRequest.newBuilder()
                                    .setId(newRecId).setName(newRecName).build());
                    System.out.println(addRecResponse.getMessage());
                    break;

                case "0":
                    System.out.println("Saliendo...");
                    appointmentChannel.shutdown();
                    medicalChannel.shutdown();
                    gymChannel.shutdown();
                    recreationChannel.shutdown();
                    return;

                default:
                    System.out.println("Opcion invalida. Use 0-17.");
            }
        }
    }
}
