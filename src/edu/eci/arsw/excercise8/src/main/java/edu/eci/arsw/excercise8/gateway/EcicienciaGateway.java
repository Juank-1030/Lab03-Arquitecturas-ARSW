package edu.eci.arsw.excercise8.gateway;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import edu.eci.arsw.excercise8.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class EcicienciaGateway {
    private static final int GATEWAY_PORT = 8090;

    private final ManagedChannel attendeeChannel;
    private final ManagedChannel agendaChannel;
    private final ManagedChannel workshopChannel;
    private final AttendeeServiceGrpc.AttendeeServiceBlockingStub attendeeStub;
    private final AgendaServiceGrpc.AgendaServiceBlockingStub agendaStub;
    private final WorkshopServiceGrpc.WorkshopServiceBlockingStub workshopStub;

    public EcicienciaGateway() {
        attendeeChannel = ManagedChannelBuilder.forAddress("localhost", 8091).usePlaintext().build();
        agendaChannel = ManagedChannelBuilder.forAddress("localhost", 8092).usePlaintext().build();
        workshopChannel = ManagedChannelBuilder.forAddress("localhost", 8093).usePlaintext().build();
        attendeeStub = AttendeeServiceGrpc.newBlockingStub(attendeeChannel);
        agendaStub = AgendaServiceGrpc.newBlockingStub(agendaChannel);
        workshopStub = WorkshopServiceGrpc.newBlockingStub(workshopChannel);
    }

    public static void main(String[] args) throws Exception {
        EcicienciaGateway gateway = new EcicienciaGateway();
        HttpServer server = HttpServer.create(new InetSocketAddress(GATEWAY_PORT), 0);
        server.createContext("/attendee/register", gateway::handleRegister);
        server.createContext("/attendee", gateway::handleGetAttendee);
        server.createContext("/agenda/activity", gateway::handleActivityDetail);
        server.createContext("/agenda", gateway::handleAgenda);
        server.createContext("/workshop/reserve", gateway::handleReserve);
        server.createContext("/workshop/cancel", gateway::handleCancel);
        server.createContext("/workshop/attendee", gateway::handleAttendeeReservations);
        server.createContext("/consolidated", gateway::handleConsolidated);
        server.setExecutor(null);
        server.start();
        System.out.println("ECICIENCIA Gateway HTTP iniciado en puerto " + GATEWAY_PORT);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Apagando Gateway...");
            server.stop(2);
            gateway.shutdownChannels();
        }));
    }

    private void shutdownChannels() {
        try {
            attendeeChannel.shutdown().awaitTermination(3, TimeUnit.SECONDS);
            agendaChannel.shutdown().awaitTermination(3, TimeUnit.SECONDS);
            workshopChannel.shutdown().awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void handleRegister(HttpExchange exchange) {
        try {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                send(exchange, 405, page("Error", "<h1>Use POST</h1>"));
                return;
            }
            String query = exchange.getRequestURI().getQuery();
            String name = param(query, "name");
            String email = param(query, "email");
            if (name == null || email == null) {
                send(exchange, 400, page("Error", "<h1>Faltan parametros: name, email</h1>"));
                return;
            }
            AttendeeResponse r = attendeeStub.registerAttendee(
                    RegisterRequest.newBuilder().setName(name).setEmail(email).build());
            send(exchange, 200, page("Registrado",
                    "<h1>Asistente registrado</h1><p>ID: " + r.getAttendeeId()
                            + " | Nombre: " + r.getName() + " | Email: " + r.getEmail() + "</p>"));
        } catch (StatusRuntimeException e) {
            sendError(exchange, "Servicio Attendee no disponible: " + e.getStatus());
        } catch (Exception e) {
            sendError(exchange, "Error interno: " + e.getMessage());
        }
    }

    private void handleGetAttendee(HttpExchange exchange) {
        try {
            String query = exchange.getRequestURI().getQuery();
            String idStr = param(query, "id");
            if (idStr == null) {
                send(exchange, 400, page("Error", "<h1>Falta parametro: id</h1>"));
                return;
            }
            int id = parseIntParam(idStr);
            AttendeeResponse r = attendeeStub.getAttendee(
                    AttendeeIdRequest.newBuilder().setAttendeeId(id).build());
            if (r.getFound()) {
                send(exchange, 200, page("Asistente",
                        "<h1>Asistente</h1><p>ID: " + r.getAttendeeId()
                                + " | Nombre: " + r.getName() + " | Email: " + r.getEmail() + "</p>"));
            } else {
                send(exchange, 404, page("No encontrado", "<h1>Asistente no encontrado</h1>"));
            }
        } catch (StatusRuntimeException e) {
            sendError(exchange, "Servicio Attendee no disponible: " + e.getStatus());
        } catch (Exception e) {
            sendError(exchange, "Error interno: " + e.getMessage());
        }
    }

    private void handleAgenda(HttpExchange exchange) {
        try {
            String query = exchange.getRequestURI().getQuery();
            String start = param(query, "start");
            String end = param(query, "end");
            if (start == null || end == null) {
                send(exchange, 400, page("Error", "<h1>Faltan parametros: start, end</h1>"));
                return;
            }
            ActivityList list = agendaStub.getActivitiesByTimeSlot(
                    TimeSlotRequest.newBuilder().setStartTime(start).setEndTime(end).build());
            StringBuilder sb = new StringBuilder("<h1>Actividades: " + start + " - " + end + "</h1>");
            for (ActivityResponse a : list.getActivitiesList()) {
                sb.append("<p>").append(a.getActivityId()).append(" - ").append(a.getTitle())
                        .append(" | ").append(a.getSpeaker()).append(" | ").append(a.getLocation())
                        .append(" | Cupo: ").append(a.getRegisteredCount()).append("/").append(a.getMaxCapacity())
                        .append("</p>");
            }
            if (list.getActivitiesCount() == 0) {
                sb.append("<p>No hay actividades en esta franja.</p>");
            }
            send(exchange, 200, page("Agenda", sb.toString()));
        } catch (StatusRuntimeException e) {
            sendError(exchange, "Servicio Agenda no disponible: " + e.getStatus());
        } catch (Exception e) {
            sendError(exchange, "Error interno: " + e.getMessage());
        }
    }

    private void handleActivityDetail(HttpExchange exchange) {
        try {
            String query = exchange.getRequestURI().getQuery();
            String idStr = param(query, "id");
            if (idStr == null) {
                send(exchange, 400, page("Error", "<h1>Falta parametro: id</h1>"));
                return;
            }
            int id = parseIntParam(idStr);
            ActivityResponse a = agendaStub.getActivityDetails(
                    ActivityRequest.newBuilder().setActivityId(id).build());
            if (!a.getFound()) {
                send(exchange, 404, page("No encontrada", "<h1>Actividad no encontrada</h1>"));
                return;
            }
            CapacityResponse cap = agendaStub.checkCapacity(
                    ActivityRequest.newBuilder().setActivityId(id).build());
            send(exchange, 200, page("Actividad",
                    "<h1>" + a.getTitle() + "</h1>"
                            + "<p>" + a.getDescription() + "</p>"
                            + "<p>Ponente: " + a.getSpeaker() + "</p>"
                            + "<p>Lugar: " + a.getLocation() + "</p>"
                            + "<p>Horario: " + a.getStartTime() + " - " + a.getEndTime() + "</p>"
                            + "<p>Aforo: " + cap.getRegisteredCount() + "/" + cap.getMaxCapacity()
                            + " (" + cap.getAvailableSpots() + " disponibles)</p>"));
        } catch (StatusRuntimeException e) {
            sendError(exchange, "Servicio Agenda no disponible: " + e.getStatus());
        } catch (Exception e) {
            sendError(exchange, "Error interno: " + e.getMessage());
        }
    }

    private void handleReserve(HttpExchange exchange) {
        try {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                send(exchange, 405, page("Error", "<h1>Use POST</h1>"));
                return;
            }
            String query = exchange.getRequestURI().getQuery();
            String attendeeIdStr = param(query, "attendeeId");
            String activityIdStr = param(query, "activityId");
            if (attendeeIdStr == null || activityIdStr == null) {
                send(exchange, 400, page("Error", "<h1>Faltan parametros: attendeeId, activityId</h1>"));
                return;
            }
            ReserveResponse r = workshopStub.reserveSpot(
                    ReserveRequest.newBuilder()
                            .setAttendeeId(parseIntParam(attendeeIdStr))
                            .setActivityId(parseIntParam(activityIdStr)).build());
            send(exchange, 200, page("Reserva",
                    "<h1>" + r.getMessage() + "</h1>"
                            + "<p>ID reserva: " + r.getReservationId()
                            + " | Estado: " + r.getStatus() + "</p>"));
        } catch (StatusRuntimeException e) {
            sendError(exchange, "Servicio Workshop no disponible: " + e.getStatus());
        } catch (Exception e) {
            sendError(exchange, "Error interno: " + e.getMessage());
        }
    }

    private void handleCancel(HttpExchange exchange) {
        try {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                send(exchange, 405, page("Error", "<h1>Use POST</h1>"));
                return;
            }
            String query = exchange.getRequestURI().getQuery();
            String reservationIdStr = param(query, "reservationId");
            if (reservationIdStr == null) {
                send(exchange, 400, page("Error", "<h1>Falta parametro: reservationId</h1>"));
                return;
            }
            ReserveResponse r = workshopStub.cancelReservation(
                    CancelReserveRequest.newBuilder()
                            .setReservationId(parseIntParam(reservationIdStr)).build());
            send(exchange, 200, page("Cancelacion", "<h1>" + r.getMessage() + "</h1>"));
        } catch (StatusRuntimeException e) {
            sendError(exchange, "Servicio Workshop no disponible: " + e.getStatus());
        } catch (Exception e) {
            sendError(exchange, "Error interno: " + e.getMessage());
        }
    }

    private void handleAttendeeReservations(HttpExchange exchange) {
        try {
            String query = exchange.getRequestURI().getQuery();
            String idStr = param(query, "id");
            if (idStr == null) {
                send(exchange, 400, page("Error", "<h1>Falta parametro: id</h1>"));
                return;
            }
            ReservationList list = workshopStub.getAttendeeReservations(
                    AttendeeIdRequest.newBuilder().setAttendeeId(parseIntParam(idStr)).build());
            StringBuilder sb = new StringBuilder("<h1>Reservas del asistente " + idStr + "</h1>");
            for (ReserveResponse r : list.getReservationsList()) {
                sb.append("<p>Reserva ").append(r.getReservationId())
                        .append(" | Actividad ").append(r.getActivityId())
                        .append(" | ").append(r.getStatus()).append("</p>");
            }
            if (list.getReservationsCount() == 0) {
                sb.append("<p>No hay reservas.</p>");
            }
            send(exchange, 200, page("Reservas", sb.toString()));
        } catch (StatusRuntimeException e) {
            sendError(exchange, "Servicio Workshop no disponible: " + e.getStatus());
        } catch (Exception e) {
            sendError(exchange, "Error interno: " + e.getMessage());
        }
    }

    private void handleConsolidated(HttpExchange exchange) {
        try {
            String query = exchange.getRequestURI().getQuery();
            String idStr = param(query, "id");
            if (idStr == null) {
                send(exchange, 400, page("Error", "<h1>Falta parametro: id</h1>"));
                return;
            }
            int id = parseIntParam(idStr);
            AttendeeResponse att = attendeeStub.getAttendee(
                    AttendeeIdRequest.newBuilder().setAttendeeId(id).build());
            if (!att.getFound()) {
                send(exchange, 404, page("No encontrado", "<h1>Asistente no encontrado</h1>"));
                return;
            }
            ReservationList list = workshopStub.getAttendeeReservations(
                    AttendeeIdRequest.newBuilder().setAttendeeId(id).build());
            StringBuilder sb = new StringBuilder();
            sb.append("<h1>Informacion consolidada</h1>");
            sb.append("<h2>Asistente</h2>");
            sb.append("<p>ID: ").append(att.getAttendeeId())
                    .append(" | Nombre: ").append(att.getName())
                    .append(" | Email: ").append(att.getEmail()).append("</p>");
            sb.append("<h2>Reservas</h2>");
            for (ReserveResponse r : list.getReservationsList()) {
                sb.append("<p>Reserva ").append(r.getReservationId())
                        .append(" | Actividad ").append(r.getActivityId())
                        .append(" | ").append(r.getStatus()).append("</p>");
            }
            if (list.getReservationsCount() == 0) {
                sb.append("<p>Sin reservas.</p>");
            }
            send(exchange, 200, page("Consolidado", sb.toString()));
        } catch (StatusRuntimeException e) {
            sendError(exchange, "Servicio no disponible: " + e.getStatus());
        } catch (Exception e) {
            sendError(exchange, "Error interno: " + e.getMessage());
        }
    }

    private int parseIntParam(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El parametro debe ser numerico, pero se recibio: " + value);
        }
    }

    private String param(String query, String key) {
        if (query == null) return null;
        for (String p : query.split("&")) {
            String[] parts = p.split("=", 2);
            if (parts.length == 2 && parts[0].equals(key)) return parts[1];
        }
        return null;
    }

    private String page(String title, String body) {
        return "<html><head><title>" + title + "</title></head><body>" + body + "</body></html>";
    }

    private void send(HttpExchange exchange, int code, String response) {
        try {
            byte[] bytes = response.getBytes("UTF-8");
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(code, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        } catch (IOException e) {
            System.err.println("Error enviando respuesta: " + e.getMessage());
        }
    }

    private void sendError(HttpExchange exchange, String message) {
        send(exchange, 500, page("Error del Servidor", "<h1>Error interno</h1><p>" + message + "</p>"));
    }
}
