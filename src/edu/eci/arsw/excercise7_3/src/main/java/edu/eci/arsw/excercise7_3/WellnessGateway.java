package edu.eci.arsw.excercise7_3;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import edu.eci.arsw.excercise6_3.appointment.*;
import edu.eci.arsw.excercise6_3.gym.*;
import edu.eci.arsw.excercise6_3.medical.*;
import edu.eci.arsw.excercise6_3.recreation.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

public class WellnessGateway {
    private static final int GATEWAY_PORT = 8083;
    private static final String HOST = "localhost";
    private static final int APPOINTMENT_PORT = 50061;
    private static final int MEDICAL_PORT = 50062;
    private static final int GYM_PORT = 50063;
    private static final int RECREATION_PORT = 50064;

    private final ManagedChannel appointmentChannel;
    private final ManagedChannel medicalChannel;
    private final ManagedChannel gymChannel;
    private final ManagedChannel recreationChannel;
    private final AppointmentServiceGrpc.AppointmentServiceBlockingStub appointmentStub;
    private final MedicalServiceGrpc.MedicalServiceBlockingStub medicalStub;
    private final GymServiceGrpc.GymServiceBlockingStub gymStub;
    private final RecreationServiceGrpc.RecreationServiceBlockingStub recreationStub;

    public WellnessGateway() {
        appointmentChannel = ManagedChannelBuilder.forAddress(HOST, APPOINTMENT_PORT).usePlaintext().build();
        medicalChannel = ManagedChannelBuilder.forAddress(HOST, MEDICAL_PORT).usePlaintext().build();
        gymChannel = ManagedChannelBuilder.forAddress(HOST, GYM_PORT).usePlaintext().build();
        recreationChannel = ManagedChannelBuilder.forAddress(HOST, RECREATION_PORT).usePlaintext().build();
        appointmentStub = AppointmentServiceGrpc.newBlockingStub(appointmentChannel);
        medicalStub = MedicalServiceGrpc.newBlockingStub(medicalChannel);
        gymStub = GymServiceGrpc.newBlockingStub(gymChannel);
        recreationStub = RecreationServiceGrpc.newBlockingStub(recreationChannel);
    }

    public static void main(String[] args) throws Exception {
        WellnessGateway gateway = new WellnessGateway();
        HttpServer server = HttpServer.create(new InetSocketAddress(GATEWAY_PORT), 0);
        server.createContext("/appointment", gateway::handleAppointment);
        server.createContext("/wellness-summary", gateway::handleWellnessSummary);
        server.createContext("/gym/reserve", gateway::handleGymReserve);
        server.createContext("/recreation/reserve", gateway::handleRecreationReserve);
        server.setExecutor(null);
        server.start();
        System.out.println("WellnessGateway HTTP iniciado en puerto " + GATEWAY_PORT);
    }

    private void handleAppointment(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (!"POST".equalsIgnoreCase(method)) {
            sendResponse(exchange, 405, htmlPage("Error", "<h1>Metodo no permitido. Use POST.</h1>"));
            return;
        }
        String query = exchange.getRequestURI().getQuery();
        String studentId = getParam(query, "studentId");
        String serviceTypeStr = getParam(query, "serviceType");
        String date = getParam(query, "date");
        if (studentId == null || serviceTypeStr == null || date == null) {
            sendResponse(exchange, 400, htmlPage("Error",
                    "<h1>ERROR: Faltan parametros (studentId, serviceType, date)</h1>"));
            return;
        }
        ServiceType serviceType;
        try {
            serviceType = ServiceType.valueOf(serviceTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            sendResponse(exchange, 400, htmlPage("Error",
                    "<h1>ERROR: Tipo de servicio invalido: " + serviceTypeStr + "</h1>"));
            return;
        }
        AppointmentResponse response = appointmentStub.requestAppointment(
                AppointmentRequest.newBuilder()
                        .setStudentId(studentId).setServiceType(serviceType).setDate(date).build());
        String body;
        if (response.getSuccess()) {
            body = "<h1>Cita solicitada exitosamente</h1>"
                    + "<p>ID cita: " + response.getAppointment().getId()
                    + " | Fecha: " + response.getAppointment().getDate()
                    + " | Estado: " + response.getAppointment().getStatus() + "</p>";
        } else {
            body = "<h1>" + response.getMessage() + "</h1>";
        }
        sendResponse(exchange, 200, htmlPage("Cita creada", body));
    }

    private void handleWellnessSummary(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String studentId = getParam(query, "studentId");
        if (studentId == null) {
            sendResponse(exchange, 400, htmlPage("Error",
                    "<h1>ERROR: Falta parametro 'studentId'</h1>"));
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>Resumen de Bienestar para ").append(studentId).append("</h1>");

        AppointmentList appointments = appointmentStub.getAppointments(
                StudentRequest.newBuilder().setStudentId(studentId).build());
        sb.append("<h2>Citas</h2>");
        if (appointments.getAppointmentsCount() == 0) {
            sb.append("<p>No hay citas registradas.</p>");
        } else {
            for (Appointment a : appointments.getAppointmentsList()) {
                sb.append("<p>").append(a.getId()).append(" | ").append(a.getServiceType())
                        .append(" | ").append(a.getDate()).append(" | ").append(a.getStatus()).append("</p>");
            }
        }

        SpecialtyList specialties = medicalStub.listSpecialties(MedicalEmpty.newBuilder().build());
        sb.append("<h2>Especialidades Medicas</h2>");
        for (SpecialtyResponse s : specialties.getSpecialtiesList()) {
            sb.append("<p>").append(s.getCode()).append(" - ").append(s.getName())
                    .append(" (").append(s.getAvailable() ? "Disponible" : "No disponible").append(")</p>");
        }

        SessionList sessions = gymStub.getSessions(
                StudentSessionsRequest.newBuilder().setStudentId(studentId).build());
        sb.append("<h2>Sesiones de Gimnasio</h2>");
        if (sessions.getSessionsCount() == 0) {
            sb.append("<p>No hay sesiones registradas.</p>");
        } else {
            for (GymSession s : sessions.getSessionsList()) {
                sb.append("<p>").append(s.getId()).append(" | ").append(s.getTimeSlot())
                        .append(" | ").append(s.getActive() ? "Activa" : "Cancelada").append("</p>");
            }
        }

        ResourceList resources = recreationStub.listResources(RecreationEmpty.newBuilder().build());
        sb.append("<h2>Recursos Recreativos</h2>");
        for (RecreationResource r : resources.getResourcesList()) {
            sb.append("<p>").append(r.getId()).append(" - ").append(r.getName())
                    .append(" (").append(r.getAvailable() ? "Disponible" : "Reservado").append(")</p>");
        }

        sendResponse(exchange, 200, htmlPage("Resumen Bienestar", sb.toString()));
    }

    private void handleGymReserve(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (!"POST".equalsIgnoreCase(method)) {
            sendResponse(exchange, 405, htmlPage("Error", "<h1>Metodo no permitido. Use POST.</h1>"));
            return;
        }
        String query = exchange.getRequestURI().getQuery();
        String studentId = getParam(query, "studentId");
        String timeSlot = getParam(query, "timeSlot");
        if (studentId == null || timeSlot == null) {
            sendResponse(exchange, 400, htmlPage("Error",
                    "<h1>ERROR: Faltan parametros (studentId, timeSlot)</h1>"));
            return;
        }
        SessionResponse response = gymStub.reserveSession(
                SessionRequest.newBuilder().setStudentId(studentId).setTimeSlot(timeSlot).build());
        String body;
        if (response.getSuccess()) {
            body = "<h1>Sesion reservada exitosamente</h1>"
                    + "<p>ID sesion: " + response.getSessionId() + "</p>";
        } else {
            body = "<h1>" + response.getMessage() + "</h1>";
        }
        sendResponse(exchange, 200, htmlPage("Sesion creada", body));
    }

    private void handleRecreationReserve(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (!"POST".equalsIgnoreCase(method)) {
            sendResponse(exchange, 405, htmlPage("Error", "<h1>Metodo no permitido. Use POST.</h1>"));
            return;
        }
        String query = exchange.getRequestURI().getQuery();
        String studentId = getParam(query, "studentId");
        String resourceId = getParam(query, "resourceId");
        if (studentId == null || resourceId == null) {
            sendResponse(exchange, 400, htmlPage("Error",
                    "<h1>ERROR: Faltan parametros (studentId, resourceId)</h1>"));
            return;
        }
        ResourceResponse response = recreationStub.reserveResource(
                ResourceRequest.newBuilder().setStudentId(studentId).setResourceId(resourceId).build());
        sendResponse(exchange, 200, htmlPage("Recurso reservado",
                "<h1>" + response.getMessage() + "</h1>"));
    }

    private String getParam(String query, String key) {
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] parts = param.split("=", 2);
            if (parts.length == 2 && parts[0].equals(key)) {
                return parts[1];
            }
        }
        return null;
    }

    private String htmlPage(String title, String body) {
        return "<html><head><title>" + title + "</title></head><body>" + body + "</body></html>";
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
