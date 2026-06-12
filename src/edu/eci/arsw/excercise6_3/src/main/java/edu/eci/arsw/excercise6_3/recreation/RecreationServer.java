package edu.eci.arsw.excercise6_3.recreation;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.HashMap;
import java.util.Map;

public class RecreationServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50064)
                .addService(new RecreationServiceImpl())
                .build();
        server.start();
        System.out.println("Recreation Microservicio iniciado en puerto 50064");
        server.awaitTermination();
    }

    static class RecreationServiceImpl extends RecreationServiceGrpc.RecreationServiceImplBase {
        private Map<String, RecreationResource> resources = new HashMap<>();
        private Map<String, String> reservations = new HashMap<>();

        public RecreationServiceImpl() {
            resources.put("REC01", RecreationResource.newBuilder()
                    .setId("REC01").setName("Balones de futbol").setAvailable(true).build());
            resources.put("REC02", RecreationResource.newBuilder()
                    .setId("REC02").setName("Raquetas de tenis").setAvailable(true).build());
            resources.put("REC03", RecreationResource.newBuilder()
                    .setId("REC03").setName("Juegos de mesa").setAvailable(true).build());
        }

        @Override
        public void reserveResource(ResourceRequest request,
                                     StreamObserver<ResourceResponse> responseObserver) {
            RecreationResource resource = resources.get(request.getResourceId());
            if (resource == null) {
                responseObserver.onNext(ResourceResponse.newBuilder()
                        .setSuccess(false).setMessage("ERROR: recurso no encontrado").build());
            } else if (!resource.getAvailable()) {
                responseObserver.onNext(ResourceResponse.newBuilder()
                        .setSuccess(false).setMessage("ERROR: recurso ya reservado").build());
            } else {
                RecreationResource updated = resource.toBuilder().setAvailable(false).build();
                resources.put(request.getResourceId(), updated);
                reservations.put(request.getResourceId(), request.getStudentId());
                responseObserver.onNext(ResourceResponse.newBuilder()
                        .setSuccess(true).setMessage("Recurso reservado exitosamente").build());
            }
            responseObserver.onCompleted();
        }

        @Override
        public void listResources(RecreationEmpty request,
                                   StreamObserver<ResourceList> responseObserver) {
            ResourceList.Builder listBuilder = ResourceList.newBuilder();
            for (RecreationResource r : resources.values()) {
                listBuilder.addResources(r);
            }
            responseObserver.onNext(listBuilder.build());
            responseObserver.onCompleted();
        }

        @Override
        public void returnResource(ReturnResourceRequest request,
                                    StreamObserver<ReturnResourceResponse> responseObserver) {
            RecreationResource resource = resources.get(request.getResourceId());
            if (resource == null) {
                responseObserver.onNext(ReturnResourceResponse.newBuilder()
                        .setSuccess(false).setMessage("ERROR: recurso no encontrado").build());
            } else if (resource.getAvailable()) {
                responseObserver.onNext(ReturnResourceResponse.newBuilder()
                        .setSuccess(false).setMessage("ERROR: el recurso no estaba reservado").build());
            } else {
                RecreationResource updated = resource.toBuilder().setAvailable(true).build();
                resources.put(request.getResourceId(), updated);
                reservations.remove(request.getResourceId());
                responseObserver.onNext(ReturnResourceResponse.newBuilder()
                        .setSuccess(true).setMessage("Recurso devuelto exitosamente").build());
            }
            responseObserver.onCompleted();
        }

        @Override
        public void addResource(AddResourceRequest request,
                                 StreamObserver<AddResourceResponse> responseObserver) {
            if (resources.containsKey(request.getId())) {
                responseObserver.onNext(AddResourceResponse.newBuilder()
                        .setSuccess(false).setMessage("ERROR: el recurso ya existe").build());
            } else {
                RecreationResource resource = RecreationResource.newBuilder()
                        .setId(request.getId()).setName(request.getName())
                        .setAvailable(true).build();
                resources.put(request.getId(), resource);
                responseObserver.onNext(AddResourceResponse.newBuilder()
                        .setSuccess(true).setMessage("Recurso agregado exitosamente").build());
            }
            responseObserver.onCompleted();
        }
    }
}
