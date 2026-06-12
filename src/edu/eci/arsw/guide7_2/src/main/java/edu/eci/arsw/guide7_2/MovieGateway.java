package edu.eci.arsw.guide7_2;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import edu.eci.arsw.guide6_2.movie.MovieRequest;
import edu.eci.arsw.guide6_2.movie.MovieResponse;
import edu.eci.arsw.guide6_2.movie.MovieServiceGrpc;
import edu.eci.arsw.guide6_2.recommendation.RecommendationRequest;
import edu.eci.arsw.guide6_2.recommendation.RecommendationResponse;
import edu.eci.arsw.guide6_2.recommendation.RecommendationServiceGrpc;
import edu.eci.arsw.guide6_2.review.ReviewRequest;
import edu.eci.arsw.guide6_2.review.ReviewResponse;
import edu.eci.arsw.guide6_2.review.ReviewServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

public class MovieGateway {
    private static final int GATEWAY_PORT = 8082;
    private static final String MOVIE_HOST = "localhost";
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
        movieChannel = ManagedChannelBuilder.forAddress(MOVIE_HOST, MOVIE_PORT).usePlaintext().build();
        reviewChannel = ManagedChannelBuilder.forAddress(MOVIE_HOST, REVIEW_PORT).usePlaintext().build();
        recommendationChannel = ManagedChannelBuilder.forAddress(MOVIE_HOST, RECOMMENDATION_PORT).usePlaintext().build();
        movieStub = MovieServiceGrpc.newBlockingStub(movieChannel);
        reviewStub = ReviewServiceGrpc.newBlockingStub(reviewChannel);
        recommendationStub = RecommendationServiceGrpc.newBlockingStub(recommendationChannel);
    }

    public static void main(String[] args) throws Exception {
        MovieGateway gateway = new MovieGateway();
        HttpServer server = HttpServer.create(new InetSocketAddress(GATEWAY_PORT), 0);
        server.createContext("/movie", gateway::handleMovie);
        server.createContext("/review", gateway::handleReview);
        server.createContext("/recommendation", gateway::handleRecommendation);
        server.createContext("/consolidated", gateway::handleConsolidated);
        server.setExecutor(null);
        server.start();
        System.out.println("MovieGateway HTTP iniciado en puerto " + GATEWAY_PORT);
    }

    private void handleMovie(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        int id = extractId(query);
        String response;
        if (id < 0) {
            response = htmlPage("Movie Gateway", "<h1>ERROR: parametro 'id' invalido</h1>");
        } else {
            MovieResponse movie = movieStub.getMovie(MovieRequest.newBuilder().setId(id).build());
            if (movie.getFound()) {
                response = htmlPage(movie.getTitle(),
                        "<h1>" + movie.getTitle() + "</h1>"
                                + "<p>Director: " + movie.getDirector() + "</p>"
                                + "<p>Anio: " + movie.getYear() + "</p>");
            } else {
                response = htmlPage("Error", "<h1>Pelicula no encontrada</h1>");
            }
        }
        sendResponse(exchange, response);
    }

    private void handleReview(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        int movieId = extractId(query);
        String response;
        if (movieId < 0) {
            response = htmlPage("Movie Gateway", "<h1>ERROR: parametro 'movieId' invalido</h1>");
        } else {
            ReviewResponse review = reviewStub.getReview(ReviewRequest.newBuilder().setMovieId(movieId).build());
            if (review.getFound()) {
                response = htmlPage("Review",
                        "<h1>Resena de pelicula " + movieId + "</h1>"
                                + "<p>Critico: " + review.getReviewer() + "</p>"
                                + "<p>Puntuacion: " + review.getRating() + "/5</p>"
                                + "<p>Comentario: " + review.getComment() + "</p>");
            } else {
                response = htmlPage("Error", "<h1>Resena no encontrada</h1>");
            }
        }
        sendResponse(exchange, response);
    }

    private void handleRecommendation(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        int movieId = extractId(query);
        String response;
        if (movieId < 0) {
            response = htmlPage("Movie Gateway", "<h1>ERROR: parametro 'movieId' invalido</h1>");
        } else {
            RecommendationResponse rec = recommendationStub.getRecommendation(
                    RecommendationRequest.newBuilder().setMovieId(movieId).build());
            if (rec.getFound()) {
                StringBuilder sb = new StringBuilder();
                sb.append("<h1>Recomendaciones para pelicula ").append(movieId).append("</h1>");
                sb.append("<p>IDs recomendadas: ");
                for (int rid : rec.getRecommendedIdsList()) {
                    sb.append(rid).append(" ");
                }
                sb.append("</p>");
                response = htmlPage("Recommendations", sb.toString());
            } else {
                response = htmlPage("Error", "<h1>Recomendaciones no encontradas</h1>");
            }
        }
        sendResponse(exchange, response);
    }

    private void handleConsolidated(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        int id = extractId(query);
        String response;
        if (id < 0) {
            response = htmlPage("Movie Gateway", "<h1>ERROR: parametro 'id' invalido</h1>");
        } else {
            MovieResponse movie = movieStub.getMovie(MovieRequest.newBuilder().setId(id).build());
            ReviewResponse review = reviewStub.getReview(ReviewRequest.newBuilder().setMovieId(id).build());
            RecommendationResponse rec = recommendationStub.getRecommendation(
                    RecommendationRequest.newBuilder().setMovieId(id).build());

            StringBuilder sb = new StringBuilder();
            sb.append("<h1>Resultado consolidado para pelicula ").append(id).append("</h1>");
            if (movie.getFound()) {
                sb.append("<h2>Pelicula</h2>");
                sb.append("<p>").append(movie.getTitle()).append(" - ").append(movie.getDirector())
                        .append(" (").append(movie.getYear()).append(")</p>");
            } else {
                sb.append("<p>Pelicula no encontrada</p>");
            }
            if (review.getFound()) {
                sb.append("<h2>Resena</h2>");
                sb.append("<p>").append(review.getReviewer()).append(": ").append(review.getRating())
                        .append("/5 - ").append(review.getComment()).append("</p>");
            } else {
                sb.append("<p>Resena no encontrada</p>");
            }
            if (rec.getFound()) {
                sb.append("<h2>Recomendaciones</h2><p>");
                for (int rid : rec.getRecommendedIdsList()) {
                    sb.append(rid).append(" ");
                }
                sb.append("</p>");
            } else {
                sb.append("<p>Recomendaciones no encontradas</p>");
            }
            response = htmlPage("Consolidado", sb.toString());
        }
        sendResponse(exchange, response);
    }

    private int extractId(String query) {
        if (query == null || !query.startsWith("id=") && !query.startsWith("movieId=")) {
            return -1;
        }
        try {
            String prefix = query.startsWith("id=") ? "id=" : "movieId=";
            return Integer.parseInt(query.substring(prefix.length()));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String htmlPage(String title, String body) {
        return "<html><head><title>" + title + "</title></head><body>" + body + "</body></html>";
    }

    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
