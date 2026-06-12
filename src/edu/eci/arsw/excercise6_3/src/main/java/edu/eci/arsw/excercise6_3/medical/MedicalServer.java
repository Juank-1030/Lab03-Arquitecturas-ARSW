package edu.eci.arsw.excercise6_3.medical;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.HashMap;
import java.util.Map;

public class MedicalServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50062)
                .addService(new MedicalServiceImpl())
                .build();
        server.start();
        System.out.println("Medical Microservicio iniciado en puerto 50062");
        server.awaitTermination();
    }

    static class MedicalServiceImpl extends MedicalServiceGrpc.MedicalServiceImplBase {
        private Map<String, SpecialtyResponse> specialties = new HashMap<>();

        public MedicalServiceImpl() {
            specialties.put("MED01", SpecialtyResponse.newBuilder()
                    .setCode("MED01").setName("Medicina General")
                    .setDescription("Atencion medica primaria y prevencion")
                    .setAvailable(true).setFound(true).build());
            specialties.put("MED02", SpecialtyResponse.newBuilder()
                    .setCode("MED02").setName("Psicologia")
                    .setDescription("Atencion psicologica y terapia")
                    .setAvailable(true).setFound(true).build());
            specialties.put("MED03", SpecialtyResponse.newBuilder()
                    .setCode("MED03").setName("Odontologia")
                    .setDescription("Cuidado dental y ortodoncia")
                    .setAvailable(true).setFound(true).build());
        }

        @Override
        public void getSpecialty(SpecialtyRequest request,
                                  StreamObserver<SpecialtyResponse> responseObserver) {
            SpecialtyResponse response = specialties.get(request.getCode());
            if (response == null) {
                response = SpecialtyResponse.newBuilder()
                        .setCode(request.getCode()).setFound(false).build();
            }
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void listSpecialties(MedicalEmpty request,
                                     StreamObserver<SpecialtyList> responseObserver) {
            SpecialtyList.Builder listBuilder = SpecialtyList.newBuilder();
            for (SpecialtyResponse s : specialties.values()) {
                listBuilder.addSpecialties(s);
            }
            responseObserver.onNext(listBuilder.build());
            responseObserver.onCompleted();
        }

        @Override
        public void addSpecialty(AddSpecialtyRequest request,
                                  StreamObserver<AddSpecialtyResponse> responseObserver) {
            if (specialties.containsKey(request.getCode())) {
                responseObserver.onNext(AddSpecialtyResponse.newBuilder()
                        .setSuccess(false).setMessage("ERROR: la especialidad ya existe").build());
            } else {
                SpecialtyResponse specialty = SpecialtyResponse.newBuilder()
                        .setCode(request.getCode()).setName(request.getName())
                        .setDescription(request.getDescription())
                        .setAvailable(request.getAvailable()).setFound(true).build();
                specialties.put(request.getCode(), specialty);
                responseObserver.onNext(AddSpecialtyResponse.newBuilder()
                        .setSuccess(true).setMessage("Especialidad agregada exitosamente").build());
            }
            responseObserver.onCompleted();
        }

        @Override
        public void removeSpecialty(RemoveSpecialtyRequest request,
                                     StreamObserver<RemoveSpecialtyResponse> responseObserver) {
            if (!specialties.containsKey(request.getCode())) {
                responseObserver.onNext(RemoveSpecialtyResponse.newBuilder()
                        .setSuccess(false).setMessage("ERROR: especialidad no encontrada").build());
            } else {
                specialties.remove(request.getCode());
                responseObserver.onNext(RemoveSpecialtyResponse.newBuilder()
                        .setSuccess(true).setMessage("Especialidad eliminada exitosamente").build());
            }
            responseObserver.onCompleted();
        }
    }
}
