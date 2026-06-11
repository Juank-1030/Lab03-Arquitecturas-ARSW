package edu.eci.arsw.movie;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Scanner;

public class MovieGrpcClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        MovieServiceGrpc.MovieServiceBlockingStub stub =
                MovieServiceGrpc.newBlockingStub(channel);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese ID de pelicula (1-3): ");
        int id = scanner.nextInt();

        MovieRequest request = MovieRequest.newBuilder().setId(id).build();
        MovieResponse response = stub.getMovie(request);

        if (response.getFound()) {
            System.out.println("Pelicula: " + response.getTitle()
                    + " - " + response.getDirector()
                    + " - " + response.getYear());
        } else {
            System.out.println("Pelicula no encontrada");
        }

        channel.shutdown();
    }
}
