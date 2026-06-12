package edu.eci.arsw.excercise6_3.gym;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GymServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50063)
                .addService(new GymServiceImpl())
                .build();
        server.start();
        System.out.println("Gym Microservicio iniciado en puerto 50063");
        server.awaitTermination();
    }

    static class GymServiceImpl extends GymServiceGrpc.GymServiceImplBase {
        private Map<String, GymSession> sessions = new HashMap<>();

        @Override
        public void reserveSession(SessionRequest request,
                                    StreamObserver<SessionResponse> responseObserver) {
            String sessionId = UUID.randomUUID().toString().substring(0, 8);
            GymSession session = GymSession.newBuilder()
                    .setId(sessionId)
                    .setStudentId(request.getStudentId())
                    .setTimeSlot(request.getTimeSlot())
                    .setActive(true)
                    .build();
            sessions.put(sessionId, session);
            responseObserver.onNext(SessionResponse.newBuilder()
                    .setSuccess(true).setMessage("Sesion reservada exitosamente")
                    .setSessionId(sessionId).build());
            responseObserver.onCompleted();
        }

        @Override
        public void getSessions(StudentSessionsRequest request,
                                 StreamObserver<SessionList> responseObserver) {
            SessionList.Builder listBuilder = SessionList.newBuilder();
            for (GymSession s : sessions.values()) {
                if (s.getStudentId().equals(request.getStudentId())) {
                    listBuilder.addSessions(s);
                }
            }
            responseObserver.onNext(listBuilder.build());
            responseObserver.onCompleted();
        }

        @Override
        public void cancelSession(CancelSessionRequest request,
                                   StreamObserver<CancelSessionResponse> responseObserver) {
            GymSession existing = sessions.get(request.getSessionId());
            if (existing == null) {
                responseObserver.onNext(CancelSessionResponse.newBuilder()
                        .setSuccess(false).setMessage("ERROR: sesion no encontrada").build());
            } else if (!existing.getActive()) {
                responseObserver.onNext(CancelSessionResponse.newBuilder()
                        .setSuccess(false).setMessage("ERROR: la sesion ya fue cancelada").build());
            } else {
                GymSession updated = existing.toBuilder().setActive(false).build();
                sessions.put(request.getSessionId(), updated);
                responseObserver.onNext(CancelSessionResponse.newBuilder()
                        .setSuccess(true).setMessage("Sesion cancelada exitosamente").build());
            }
            responseObserver.onCompleted();
        }

        @Override
        public void getAllSessions(GymEmpty request,
                                    StreamObserver<SessionList> responseObserver) {
            SessionList.Builder listBuilder = SessionList.newBuilder();
            for (GymSession s : sessions.values()) {
                listBuilder.addSessions(s);
            }
            responseObserver.onNext(listBuilder.build());
            responseObserver.onCompleted();
        }
    }
}
