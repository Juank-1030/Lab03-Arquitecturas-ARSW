package edu.eci.arsw.guide6_2;

import edu.eci.arsw.guide6_2.movie.MovieRequest;
import edu.eci.arsw.guide6_2.movie.MovieResponse;
import edu.eci.arsw.guide6_2.movie.MovieServiceGrpc;
import edu.eci.arsw.guide6_2.review.Review;
import edu.eci.arsw.guide6_2.review.ReviewList;
import edu.eci.arsw.guide6_2.review.ReviewRequest;
import edu.eci.arsw.guide6_2.review.ReviewServiceGrpc;
import edu.eci.arsw.guide6_2.recommendation.RecommendationList;
import edu.eci.arsw.guide6_2.recommendation.RecommendationRequest;
import edu.eci.arsw.guide6_2.recommendation.RecommendationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Scanner;

public class MicroserviceClient {
    public static void main(String[] args) {
        ManagedChannel movieChannel = ManagedChannelBuilder
                .forAddress("localhost", 50051).usePlaintext().build();
        ManagedChannel reviewChannel = ManagedChannelBuilder
                .forAddress("localhost", 50052).usePlaintext().build();
        ManagedChannel recommendationChannel = ManagedChannelBuilder
                .forAddress("localhost", 50053).usePlaintext().build();

        MovieServiceGrpc.MovieServiceBlockingStub movieStub =
                MovieServiceGrpc.newBlockingStub(movieChannel);
        ReviewServiceGrpc.ReviewServiceBlockingStub reviewStub =
                ReviewServiceGrpc.newBlockingStub(reviewChannel);
        RecommendationServiceGrpc.RecommendationServiceBlockingStub recommendationStub =
                RecommendationServiceGrpc.newBlockingStub(recommendationChannel);

        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Movie Microservices Client ===");

        while (true) {
            System.out.println("\nOperaciones disponibles:");
            System.out.println("  1. Consultar pelicula");
            System.out.println("  2. Consultar resenas");
            System.out.println("  3. Obtener recomendaciones");
            System.out.println("  4. Consultar todo (pelicula + resenas + recomendaciones)");
            System.out.println("  5. Salir");
            System.out.print("Seleccione una opcion: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    System.out.print("ID de la pelicula (1-3): ");
                    int id1 = Integer.parseInt(scanner.nextLine().trim());
                    MovieResponse movie = movieStub.getMovie(MovieRequest.newBuilder().setId(id1).build());
                    if (movie.getFound()) {
                        System.out.println("Pelicula: " + movie.getTitle()
                                + " - " + movie.getDirector() + " - " + movie.getYear());
                    } else {
                        System.out.println("Pelicula no encontrada");
                    }
                    break;

                case "2":
                    System.out.print("ID de la pelicula (1-3): ");
                    int id2 = Integer.parseInt(scanner.nextLine().trim());
                    ReviewList reviewList = reviewStub.getReviews(ReviewRequest.newBuilder().setMovieId(id2).build());
                    if (reviewList.getReviewsCount() > 0) {
                        System.out.println("Resenas de la pelicula " + id2 + ":");
                        for (Review r : reviewList.getReviewsList()) {
                            System.out.println("  - " + r.getAuthor() + ": " + r.getComment()
                                    + " Rating: " + r.getRating());
                        }
                    } else {
                        System.out.println("No hay resenas para esta pelicula");
                    }
                    break;

                case "3":
                    System.out.print("ID de la pelicula (1-3): ");
                    int id3 = Integer.parseInt(scanner.nextLine().trim());
                    RecommendationList recList = recommendationStub.getRecommendations(
                            RecommendationRequest.newBuilder().setMovieId(id3).build());
                    if (recList.getTitlesCount() > 0) {
                        System.out.println("Recomendaciones:");
                        for (String title : recList.getTitlesList()) {
                            System.out.println("  - " + title);
                        }
                    } else {
                        System.out.println("No hay recomendaciones");
                    }
                    break;

                case "4":
                    System.out.print("ID de la pelicula (1-3): ");
                    int id4 = Integer.parseInt(scanner.nextLine().trim());
                    MovieResponse m = movieStub.getMovie(MovieRequest.newBuilder().setId(id4).build());
                    ReviewList rl = reviewStub.getReviews(ReviewRequest.newBuilder().setMovieId(id4).build());
                    RecommendationList rcl = recommendationStub.getRecommendations(
                            RecommendationRequest.newBuilder().setMovieId(id4).build());

                    System.out.println("\n=== Resultados completos para pelicula " + id4 + " ===");
                    if (m.getFound()) {
                        System.out.println("Pelicula: " + m.getTitle() + " - " + m.getDirector() + " - " + m.getYear());
                    } else {
                        System.out.println("Pelicula: no encontrada");
                    }
                    if (rl.getReviewsCount() > 0) {
                        System.out.println("Resenas:");
                        for (Review r : rl.getReviewsList()) {
                            System.out.println("  - " + r.getAuthor() + ": " + r.getComment()
                                    + " Rating: " + r.getRating());
                        }
                    } else {
                        System.out.println("Resenas: no disponibles");
                    }
                    if (rcl.getTitlesCount() > 0) {
                        System.out.println("Recomendaciones:");
                        for (String title : rcl.getTitlesList()) {
                            System.out.println("  - " + title);
                        }
                    } else {
                        System.out.println("Recomendaciones: no disponibles");
                    }
                    break;

                case "5":
                    System.out.println("Saliendo...");
                    movieChannel.shutdown();
                    reviewChannel.shutdown();
                    recommendationChannel.shutdown();
                    return;

                default:
                    System.out.println("Opcion invalida. Use 1-5.");
            }
        }
    }
}
