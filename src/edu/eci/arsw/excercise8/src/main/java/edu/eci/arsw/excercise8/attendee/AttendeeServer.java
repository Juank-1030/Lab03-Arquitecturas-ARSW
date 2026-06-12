package edu.eci.arsw.excercise8.attendee;

import edu.eci.arsw.excercise8.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AttendeeServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(8091)
                .addService(new AttendeeServiceImpl())
                .build();
        server.start();
        System.out.println("AttendeeService gRPC iniciado en puerto 8091");
        server.awaitTermination();
    }

    static class AttendeeServiceImpl extends AttendeeServiceGrpc.AttendeeServiceImplBase {
        private final Map<Integer, AttendeeResponse> attendees = new HashMap<>();
        private final AtomicInteger idCounter = new AtomicInteger(1);

        public AttendeeServiceImpl() {
            AttendeeResponse a1 = AttendeeResponse.newBuilder()
                    .setAttendeeId(1).setName("Carlos Perez").setEmail("carlos@mail.com").setFound(true).build();
            AttendeeResponse a2 = AttendeeResponse.newBuilder()
                    .setAttendeeId(2).setName("Maria Gomez").setEmail("maria@mail.com").setFound(true).build();
            attendees.put(1, a1);
            attendees.put(2, a2);
            idCounter.set(3);
        }

        @Override
        public void registerAttendee(RegisterRequest request, StreamObserver<AttendeeResponse> responseObserver) {
            int id = idCounter.getAndIncrement();
            AttendeeResponse response = AttendeeResponse.newBuilder()
                    .setAttendeeId(id).setName(request.getName())
                    .setEmail(request.getEmail()).setFound(true).build();
            attendees.put(id, response);
            System.out.println("Registrado asistente: " + request.getName() + " (ID " + id + ")");
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void getAttendee(AttendeeIdRequest request, StreamObserver<AttendeeResponse> responseObserver) {
            AttendeeResponse response = attendees.get(request.getAttendeeId());
            if (response == null) {
                response = AttendeeResponse.newBuilder()
                        .setAttendeeId(request.getAttendeeId()).setFound(false).build();
            }
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void listAttendees(Empty request, StreamObserver<AttendeeList> responseObserver) {
            AttendeeList.Builder builder = AttendeeList.newBuilder();
            for (AttendeeResponse a : attendees.values()) {
                builder.addAttendees(a);
            }
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        }
    }
}
