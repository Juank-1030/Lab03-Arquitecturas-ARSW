package edu.eci.arsw.guide6_2.recommendation;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.HashMap;
import java.util.Map;

public class RecommendationServiceServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50053)
                .addService(new RecommendationServiceImpl())
                .build();
        server.start();
        System.out.println("RecommendationService Microservicio iniciado en puerto 50053");
        server.awaitTermination();
    }

    static class RecommendationServiceImpl extends RecommendationServiceGrpc.RecommendationServiceImplBase {
        private Map<Integer, RecommendationResponse> recommendations = new HashMap<>();

        public RecommendationServiceImpl() {
            recommendations.put(1, RecommendationResponse.newBuilder()
                    .setMovieId(1).addRecommendedIds(2).addRecommendedIds(3).setFound(true).build());
            recommendations.put(2, RecommendationResponse.newBuilder()
                    .setMovieId(2).addRecommendedIds(1).addRecommendedIds(3).setFound(true).build());
            recommendations.put(3, RecommendationResponse.newBuilder()
                    .setMovieId(3).addRecommendedIds(1).addRecommendedIds(2).setFound(true).build());
        }

        @Override
        public void getRecommendation(RecommendationRequest request,
                                       StreamObserver<RecommendationResponse> responseObserver) {
            RecommendationResponse response = recommendations.get(request.getMovieId());
            if (response == null) {
                response = RecommendationResponse.newBuilder()
                        .setMovieId(request.getMovieId()).setFound(false).build();
            }
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
