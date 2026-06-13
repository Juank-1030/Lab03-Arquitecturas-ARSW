package edu.eci.arsw.guide7_2;

import edu.eci.arsw.guide6_2.movie.MovieRequest;
import edu.eci.arsw.guide6_2.movie.MovieResponse;
import edu.eci.arsw.guide6_2.movie.MovieServiceGrpc;
import edu.eci.arsw.guide6_2.recommendation.RecommendationList;
import edu.eci.arsw.guide6_2.recommendation.RecommendationRequest;
import edu.eci.arsw.guide6_2.recommendation.RecommendationServiceGrpc;
import edu.eci.arsw.guide6_2.review.Review;
import edu.eci.arsw.guide6_2.review.ReviewList;
import edu.eci.arsw.guide6_2.review.ReviewRequest;
import edu.eci.arsw.guide6_2.review.ReviewServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Scanner;

public class MovieGateway {
    private static final String HOST = "localhost";
    private static final int MOVIE_PORT = 50051;
    private static final int REVIEW_PORT = 50052;
    private static final int RECOMMENDATION_PORT = 50053;

    private final ManagedChannel movieChannel;
    private final ManagedChannel reviewChannel;
    private final ManagedChannel recommendationChannel;
    private final MovieServiceGrpc.MovieServiceBlockingStub movieStub;
    private final ReviewServiceGrpc.ReviewServiceBlockingStub reviewStub;
    private final RecommendationServiceGrpc.RecommendationServiceBlockingStub recommendationStub;

    public MovieGateway() {
        movieChannel = ManagedChannelBuilder.forAddress(HOST, MOVIE_PORT).usePlaintext().build();
        reviewChannel = ManagedChannelBuilder.forAddress(HOST, REVIEW_PORT).usePlaintext().build();
        recommendationChannel = ManagedChannelBuilder.forAddress(HOST, RECOMMENDATION_PORT).usePlaintext().build();
        movieStub = MovieServiceGrpc.newBlockingStub(movieChannel);
        reviewStub = ReviewServiceGrpc.newBlockingStub(reviewChannel);
        recommendationStub = RecommendationServiceGrpc.newBlockingStub(recommendationChannel);
    }

    public void shutdown() {
        movieChannel.shutdown();
        reviewChannel.shutdown();
        recommendationChannel.shutdown();
    }

    public void printConsolidated(int movieId) {
        MovieResponse movie = movieStub.getMovie(MovieRequest.newBuilder().setId(movieId).build());
        ReviewList reviewList = reviewStub.getReviews(ReviewRequest.newBuilder().setMovieId(movieId).build());
        RecommendationList recList = recommendationStub.getRecommendations(
                RecommendationRequest.newBuilder().setMovieId(movieId).build());

        if (movie.getFound()) {
            System.out.println("Pelicula: " + movie.getTitle());
            System.out.println("Director: " + movie.getDirector());
            System.out.println("Anio: " + movie.getYear());
        } else {
            System.out.println("Pelicula: no encontrada");
            return;
        }

        System.out.println("Resenas:");
        if (reviewList.getReviewsCount() > 0) {
            for (Review r : reviewList.getReviewsList()) {
                System.out.println("  - " + r.getAuthor() + ": " + r.getComment()
                        + ". Rating: " + r.getRating());
            }
        } else {
            System.out.println("  No hay resenas disponibles");
        }

        System.out.println("Recomendaciones:");
        if (recList.getTitlesCount() > 0) {
            for (String title : recList.getTitlesList()) {
                System.out.println("  - " + title);
            }
        } else {
            System.out.println("  No hay recomendaciones disponibles");
        }
    }

    public static void main(String[] args) {
        MovieGateway gateway = new MovieGateway();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Movie Gateway (Consola) ===");
        System.out.print("Ingrese el ID de la pelicula: ");
        int movieId = scanner.nextInt();
        scanner.nextLine();

        System.out.println();
        gateway.printConsolidated(movieId);

        gateway.shutdown();
        scanner.close();
    }
}
