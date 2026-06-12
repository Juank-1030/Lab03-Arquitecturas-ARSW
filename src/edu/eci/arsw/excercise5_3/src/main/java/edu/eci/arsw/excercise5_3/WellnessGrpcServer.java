package edu.eci.arsw.excercise5_3;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WellnessGrpcServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50061)
                .addService(new AppointmentServiceImpl())
                .build();
        server.start();
        System.out.println("Wellness gRPC Server iniciado en puerto 50061");
        server.awaitTermination();
    }

    static class AppointmentServiceImpl extends AppointmentServiceGrpc.AppointmentServiceImplBase {
        private Map<String, Appointment> appointments = new HashMap<>();

        @Override
        public void requestAppointment(AppointmentRequest request,
                                        StreamObserver<AppointmentResponse> responseObserver) {
            String appointmentId = UUID.randomUUID().toString().substring(0, 8);
            Appointment appointment = Appointment.newBuilder()
                    .setId(appointmentId)
                    .setStudentId(request.getStudentId())
                    .setServiceType(request.getServiceType())
                    .setDate(request.getDate())
                    .setStatus(Status.REQUESTED)
                    .build();
            appointments.put(appointmentId, appointment);

            AppointmentResponse response = AppointmentResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Cita solicitada exitosamente")
                    .setAppointment(appointment)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void cancelAppointment(CancelRequest request,
                                        StreamObserver<CancelResponse> responseObserver) {
            Appointment existing = appointments.get(request.getAppointmentId());
            CancelResponse response;
            if (existing == null) {
                response = CancelResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("ERROR: cita no encontrada")
                        .build();
            } else if (existing.getStatus() == Status.CANCELLED) {
                response = CancelResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("ERROR: la cita ya fue cancelada")
                        .build();
            } else {
                Appointment updated = existing.toBuilder().setStatus(Status.CANCELLED).build();
                appointments.put(request.getAppointmentId(), updated);
                response = CancelResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Cita cancelada exitosamente")
                        .build();
            }
            responseObserver.onNext(response);
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
    }
}