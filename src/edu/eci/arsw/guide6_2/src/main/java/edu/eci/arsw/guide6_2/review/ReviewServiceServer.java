package edu.eci.arsw.guide6_2.review;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.HashMap;
import java.util.Map;

public class ReviewServiceServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50052)
                .addService(new ReviewServiceImpl())
                .build();
        server.start();
        System.out.println("ReviewService Microservicio iniciado en puerto 50052");
        server.awaitTermination();
    }

    static class ReviewServiceImpl extends ReviewServiceGrpc.ReviewServiceImplBase {
        private Map<Integer, ReviewResponse> reviews = new HashMap<>();

        public ReviewServiceImpl() {
            reviews.put(1, ReviewResponse.newBuilder()
                    .setMovieId(1).setReviewer("Roger Ebert")
                    .setRating(5).setComment("Una obra maestra de la ciencia ficcion")
                    .setFound(true).build());
            reviews.put(2, ReviewResponse.newBuilder()
                    .setMovieId(2).setReviewer("Peter Travers")
                    .setRating(4).setComment("Innovadora y visualmente impactante")
                    .setFound(true).build());
            reviews.put(3, ReviewResponse.newBuilder()
                    .setMovieId(3).setReviewer("Richard Roeper")
                    .setRating(5).setComment("Un viaje alucinante al subconsciente")
                    .setFound(true).build());
        }

        @Override
        public void getReview(ReviewRequest request,
                               StreamObserver<ReviewResponse> responseObserver) {
            ReviewResponse response = reviews.get(request.getMovieId());
            if (response == null) {
                response = ReviewResponse.newBuilder()
                        .setMovieId(request.getMovieId()).setFound(false).build();
            }
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
