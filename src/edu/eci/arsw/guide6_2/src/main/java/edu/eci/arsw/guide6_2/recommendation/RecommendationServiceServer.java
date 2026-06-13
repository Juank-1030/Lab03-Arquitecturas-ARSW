package edu.eci.arsw.guide6_2.recommendation;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        private Map<Integer, List<String>> recommendations = new HashMap<>();

        public RecommendationServiceImpl() {
            List<String> movie1Recs = new ArrayList<>();
            movie1Recs.add("Inception");
            movie1Recs.add("Contact");
            movie1Recs.add("2001: A Space Odyssey");
            recommendations.put(1, movie1Recs);

            List<String> movie2Recs = new ArrayList<>();
            movie2Recs.add("The Matrix Reloaded");
            movie2Recs.add("Blade Runner");
            movie2Recs.add("Dark City");
            recommendations.put(2, movie2Recs);

            List<String> movie3Recs = new ArrayList<>();
            movie3Recs.add("Interstellar");
            movie3Recs.add("Shutter Island");
            movie3Recs.add("The Prestige");
            recommendations.put(3, movie3Recs);
        }

        @Override
        public void getRecommendations(RecommendationRequest request,
                                        StreamObserver<RecommendationList> responseObserver) {
            List<String> movieRecs = recommendations.get(request.getMovieId());
            RecommendationList.Builder listBuilder = RecommendationList.newBuilder();
            if (movieRecs != null) {
                listBuilder.addAllTitles(movieRecs);
            }
            responseObserver.onNext(listBuilder.build());
            responseObserver.onCompleted();
        }
    }
}
