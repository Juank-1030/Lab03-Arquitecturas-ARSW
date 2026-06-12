package edu.eci.arsw.excercise8.workshop;

import edu.eci.arsw.excercise8.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkshopServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(8093)
                .addService(new WorkshopServiceImpl())
                .build();
        server.start();
        System.out.println("WorkshopService gRPC iniciado en puerto 8093");
        server.awaitTermination();
    }

    static class WorkshopServiceImpl extends WorkshopServiceGrpc.WorkshopServiceImplBase {
        private final Map<Integer, ReserveResponse> reservations = new HashMap<>();
        private final Map<Integer, Integer> activityCount = new HashMap<>();
        private final Map<Integer, Integer> activityCapacity = new HashMap<>();
        private final Map<Integer, Queue<Integer>> activityWaitingQueue = new HashMap<>();
        private final AtomicInteger reservationIdCounter = new AtomicInteger(1);

        public WorkshopServiceImpl() {
            activityCapacity.put(1, 50);
            activityCapacity.put(2, 20);
            activityCapacity.put(3, 40);
            activityCapacity.put(4, 15);
            activityCount.put(1, 20);
            activityCount.put(2, 18);
            activityCount.put(3, 10);
            activityCount.put(4, 15);

            ReserveResponse r1 = ReserveResponse.newBuilder()
                    .setReservationId(1).setAttendeeId(1).setActivityId(2)
                    .setStatus("CONFIRMED").setPositionInQueue(0).setSuccess(true).setMessage("Reserva confirmada").build();
            reservations.put(1, r1);
            reservationIdCounter.set(2);
        }

        @Override
        public void reserveSpot(ReserveRequest request, StreamObserver<ReserveResponse> responseObserver) {
            int rid = reservationIdCounter.getAndIncrement();
            int currentCount = activityCount.getOrDefault(request.getActivityId(), 0);
            int maxCap = activityCapacity.getOrDefault(request.getActivityId(), 0);
            String status;
            int queuePos;
            String message;
            boolean success;

            if (currentCount < maxCap) {
                status = "CONFIRMED";
                queuePos = 0;
                message = "Reserva confirmada para actividad " + request.getActivityId();
                success = true;
                activityCount.put(request.getActivityId(), currentCount + 1);
            } else {
                status = "WAITING";
                queuePos = currentCount - maxCap + 1;
                message = "Actividad llena. Posicion en lista de espera: " + queuePos;
                success = false;
            }

            ReserveResponse response = ReserveResponse.newBuilder()
                    .setReservationId(rid).setAttendeeId(request.getAttendeeId())
                    .setActivityId(request.getActivityId()).setStatus(status)
                    .setPositionInQueue(queuePos).setSuccess(success).setMessage(message).build();
            reservations.put(rid, response);

            if ("WAITING".equals(status)) {
                activityWaitingQueue.computeIfAbsent(request.getActivityId(), k -> new LinkedList<>()).add(rid);
            }
            System.out.println("Reserva " + rid + " para asistente " + request.getAttendeeId()
                    + " en actividad " + request.getActivityId() + ": " + status);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void cancelReservation(CancelReserveRequest request, StreamObserver<ReserveResponse> responseObserver) {
            ReserveResponse existing = reservations.get(request.getReservationId());
            ReserveResponse response;
            if (existing != null && !"CANCELLED".equals(existing.getStatus())) {
                int actId = existing.getActivityId();
                int currentCount = activityCount.getOrDefault(actId, 0);
                activityCount.put(actId, Math.max(0, currentCount - 1));
                response = existing.toBuilder()
                        .setStatus("CANCELLED").setMessage("Reserva cancelada exitosamente").build();
                reservations.put(request.getReservationId(), response);
                System.out.println("Reserva " + request.getReservationId() + " cancelada.");

                Queue<Integer> queue = activityWaitingQueue.get(actId);
                if (queue != null && !queue.isEmpty()) {
                    int promotedId = queue.poll();
                    ReserveResponse waiting = reservations.get(promotedId);
                    if (waiting != null) {
                        ReserveResponse promoted = waiting.toBuilder()
                                .setStatus("CONFIRMED").setPositionInQueue(0)
                                .setSuccess(true).setMessage("Cupo liberado, reserva confirmada").build();
                        reservations.put(promotedId, promoted);
                        activityCount.put(actId, activityCount.getOrDefault(actId, 0) + 1);
                        System.out.println("Reserva " + promotedId + " promovida de WAITING a CONFIRMED.");
                    }
                }
            } else {
                response = ReserveResponse.newBuilder()
                        .setReservationId(request.getReservationId())
                        .setSuccess(false).setMessage("Reserva no encontrada o ya cancelada").build();
            }
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void getAttendeeReservations(AttendeeIdRequest request, StreamObserver<ReservationList> responseObserver) {
            ReservationList.Builder builder = ReservationList.newBuilder();
            for (ReserveResponse r : reservations.values()) {
                if (r.getAttendeeId() == request.getAttendeeId()) {
                    builder.addReservations(r);
                }
            }
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        }

        @Override
        public void getAvailableSpots(ActivityRequest request, StreamObserver<CapacityResponse> responseObserver) {
            int currentCount = activityCount.getOrDefault(request.getActivityId(), 0);
            int maxCap = activityCapacity.getOrDefault(request.getActivityId(), 0);
            CapacityResponse response = CapacityResponse.newBuilder()
                    .setActivityId(request.getActivityId())
                    .setMaxCapacity(maxCap).setRegisteredCount(currentCount)
                    .setAvailableSpots(Math.max(0, maxCap - currentCount)).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
