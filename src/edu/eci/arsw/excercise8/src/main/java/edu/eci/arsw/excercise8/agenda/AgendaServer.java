package edu.eci.arsw.excercise8.agenda;

import edu.eci.arsw.excercise8.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgendaServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(8092)
                .addService(new AgendaServiceImpl())
                .build();
        server.start();
        System.out.println("AgendaService gRPC iniciado en puerto 8092");
        server.awaitTermination();
    }

    static class AgendaServiceImpl extends AgendaServiceGrpc.AgendaServiceImplBase {
        private final Map<Integer, ActivityResponse> activities = new HashMap<>();

        public AgendaServiceImpl() {
            activities.put(1, ActivityResponse.newBuilder()
                    .setActivityId(1).setTitle("Charla: Machine Learning")
                    .setDescription("Introduccion a ML con Python").setSpeaker("Dr. Lopez")
                    .setLocation("Auditorio A").setStartTime("09:00").setEndTime("10:00")
                    .setMaxCapacity(50).setRegisteredCount(20).setFound(true).build());
            activities.put(2, ActivityResponse.newBuilder()
                    .setActivityId(2).setTitle("Taller: Arduino Basico")
                    .setDescription("Electronica y sensores").setSpeaker("Ing. Martinez")
                    .setLocation("Lab E301").setStartTime("10:00").setEndTime("12:00")
                    .setMaxCapacity(20).setRegisteredCount(18).setFound(true).build());
            activities.put(3, ActivityResponse.newBuilder()
                    .setActivityId(3).setTitle("Charla: Ciberseguridad")
                    .setDescription("Amenazas y defensas actuales").setSpeaker("Dr. Ramirez")
                    .setLocation("Auditorio B").setStartTime("14:00").setEndTime("15:00")
                    .setMaxCapacity(40).setRegisteredCount(10).setFound(true).build());
            activities.put(4, ActivityResponse.newBuilder()
                    .setActivityId(4).setTitle("Taller: Robotica")
                    .setDescription("Construccion de robots moviles").setSpeaker("Ing. Fernandez")
                    .setLocation("Lab E302").setStartTime("15:00").setEndTime("17:00")
                    .setMaxCapacity(15).setRegisteredCount(15).setFound(true).build());
        }

        @Override
        public void getActivitiesByTimeSlot(TimeSlotRequest request, StreamObserver<ActivityList> responseObserver) {
            ActivityList.Builder builder = ActivityList.newBuilder();
            for (ActivityResponse a : activities.values()) {
                if (a.getStartTime().compareTo(request.getStartTime()) >= 0
                        && a.getEndTime().compareTo(request.getEndTime()) <= 0) {
                    builder.addActivities(a);
                }
            }
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        }

        @Override
        public void getActivityDetails(ActivityRequest request, StreamObserver<ActivityResponse> responseObserver) {
            ActivityResponse response = activities.get(request.getActivityId());
            if (response == null) {
                response = ActivityResponse.newBuilder()
                        .setActivityId(request.getActivityId()).setFound(false).build();
            }
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void checkCapacity(ActivityRequest request, StreamObserver<CapacityResponse> responseObserver) {
            ActivityResponse activity = activities.get(request.getActivityId());
            CapacityResponse response;
            if (activity != null) {
                response = CapacityResponse.newBuilder()
                        .setActivityId(activity.getActivityId())
                        .setMaxCapacity(activity.getMaxCapacity())
                        .setRegisteredCount(activity.getRegisteredCount())
                        .setAvailableSpots(activity.getMaxCapacity() - activity.getRegisteredCount())
                        .build();
            } else {
                response = CapacityResponse.newBuilder()
                        .setActivityId(request.getActivityId())
                        .setMaxCapacity(0).setRegisteredCount(0).setAvailableSpots(0)
                        .build();
            }
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
