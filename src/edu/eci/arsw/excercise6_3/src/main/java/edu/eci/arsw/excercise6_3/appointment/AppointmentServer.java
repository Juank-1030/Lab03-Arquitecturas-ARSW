package edu.eci.arsw.excercise6_3.appointment;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AppointmentServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50061)
                .addService(new AppointmentServiceImpl())
                .build();
        server.start();
        System.out.println("Appointment Microservicio iniciado en puerto 50061");
        server.awaitTermination();
    }

    static class AppointmentServiceImpl extends AppointmentServiceGrpc.AppointmentServiceImplBase {
        private Map<String, Appointment> appointments = new HashMap<>();

        public AppointmentServiceImpl() {
            Appointment sample = Appointment.newBuilder()
                    .setId("a1b2c3d4").setStudentId("S123")
                    .setServiceType(ServiceType.MEDICINE).setDate("2026-06-15")
                    .setStatus(Status.REQUESTED).build();
            appointments.put("a1b2c3d4", sample);
        }

        @Override
        public void requestAppointment(AppointmentRequest request,
                                        StreamObserver<AppointmentResponse> responseObserver) {
            String id = UUID.randomUUID().toString().substring(0, 8);
            Appointment appointment = Appointment.newBuilder()
                    .setId(id).setStudentId(request.getStudentId())
                    .setServiceType(request.getServiceType())
                    .setDate(request.getDate()).setStatus(Status.REQUESTED)
                    .build();
            appointments.put(id, appointment);
            responseObserver.onNext(AppointmentResponse.newBuilder()
                    .setSuccess(true).setMessage("Cita solicitada exitosamente")
                    .setAppointment(appointment).build());
            responseObserver.onCompleted();
        }

        @Override
        public void cancelAppointment(CancelRequest request,
                                        StreamObserver<CancelResponse> responseObserver) {
            Appointment existing = appointments.get(request.getAppointmentId());
            if (existing == null) {
                responseObserver.onNext(CancelResponse.newBuilder()
                        .setSuccess(false).setMessage("ERROR: cita no encontrada").build());
            } else if (existing.getStatus() == Status.CANCELLED) {
                responseObserver.onNext(CancelResponse.newBuilder()
                        .setSuccess(false).setMessage("ERROR: la cita ya fue cancelada").build());
            } else {
                Appointment updated = existing.toBuilder().setStatus(Status.CANCELLED).build();
                appointments.put(request.getAppointmentId(), updated);
                responseObserver.onNext(CancelResponse.newBuilder()
                        .setSuccess(true).setMessage("Cita cancelada exitosamente").build());
            }
            responseObserver.onCompleted();
        }

        @Override
        public void getAppointments(StudentRequest request,
                                     StreamObserver<AppointmentList> responseObserver) {
            AppointmentList.Builder listBuilder = AppointmentList.newBuilder();
            for (Appointment a : appointments.values()) {
                if (a.getStudentId().equals(request.getStudentId())) {
                    listBuilder.addAppointments(a);
                }
            }
            responseObserver.onNext(listBuilder.build());
            responseObserver.onCompleted();
        }

        @Override
        public void deleteAppointment(DeleteAppointmentRequest request,
                                       StreamObserver<DeleteAppointmentResponse> responseObserver) {
            Appointment existing = appointments.get(request.getAppointmentId());
            if (existing == null) {
                responseObserver.onNext(DeleteAppointmentResponse.newBuilder()
                        .setSuccess(false).setMessage("ERROR: cita no encontrada").build());
            } else {
                appointments.remove(request.getAppointmentId());
                responseObserver.onNext(DeleteAppointmentResponse.newBuilder()
                        .setSuccess(true).setMessage("Cita eliminada permanentemente").build());
            }
            responseObserver.onCompleted();
        }

        @Override
        public void updateAppointmentDate(UpdateDateRequest request,
                                           StreamObserver<UpdateDateResponse> responseObserver) {
            Appointment existing = appointments.get(request.getAppointmentId());
            if (existing == null) {
                responseObserver.onNext(UpdateDateResponse.newBuilder()
                        .setSuccess(false).setMessage("ERROR: cita no encontrada").build());
            } else if (existing.getStatus() == Status.CANCELLED) {
                responseObserver.onNext(UpdateDateResponse.newBuilder()
                        .setSuccess(false).setMessage("ERROR: no se puede reprogramar una cita cancelada").build());
            } else {
                Appointment updated = existing.toBuilder().setDate(request.getNewDate()).build();
                appointments.put(request.getAppointmentId(), updated);
                responseObserver.onNext(UpdateDateResponse.newBuilder()
                        .setSuccess(true).setMessage("Cita reprogramada exitosamente")
                        .setAppointment(updated).build());
            }
            responseObserver.onCompleted();
        }
    }
}
