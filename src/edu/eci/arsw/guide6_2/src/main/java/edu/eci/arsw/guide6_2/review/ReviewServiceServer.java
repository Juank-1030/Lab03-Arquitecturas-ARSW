package edu.eci.arsw.guide6_2.review;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        private Map<Integer, List<Review>> reviews = new HashMap<>();

        public ReviewServiceImpl() {
            List<Review> movie1Reviews = new ArrayList<>();
            movie1Reviews.add(Review.newBuilder()
                    .setAuthor("Roger Ebert")
                    .setComment("Una obra maestra de la ciencia ficcion")
                    .setRating(5).build());
            movie1Reviews.add(Review.newBuilder()
                    .setAuthor("Peter Travers")
                    .setComment("Visualmente impresionante")
                    .setRating(4).build());
            reviews.put(1, movie1Reviews);

            List<Review> movie2Reviews = new ArrayList<>();
            movie2Reviews.add(Review.newBuilder()
                    .setAuthor("Richard Roeper")
                    .setComment("Innovadora y revolucionaria")
                    .setRating(5).build());
            reviews.put(2, movie2Reviews);

            List<Review> movie3Reviews = new ArrayList<>();
            movie3Reviews.add(Review.newBuilder()
                    .setAuthor("Christopher Orr")
                    .setComment("Un viaje alucinante al subconsciente")
                    .setRating(5).build());
            movie3Reviews.add(Review.newBuilder()
                    .setAuthor("Lisa Schwarzbaum")
                    .setComment("Compleja y fascinante")
                    .setRating(4).build());
            reviews.put(3, movie3Reviews);
        }

        @Override
        public void getReviews(ReviewRequest request,
                                StreamObserver<ReviewList> responseObserver) {
            List<Review> movieReviews = reviews.get(request.getMovieId());
            ReviewList.Builder listBuilder = ReviewList.newBuilder();
            if (movieReviews != null) {
                listBuilder.addAllReviews(movieReviews);
            }
            responseObserver.onNext(listBuilder.build());
            responseObserver.onCompleted();
        }
    }
}
