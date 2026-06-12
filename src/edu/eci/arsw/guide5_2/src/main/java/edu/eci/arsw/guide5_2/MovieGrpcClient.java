package edu.eci.arsw.guide5_2;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class MovieGrpcClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        MovieServiceGrpc.MovieServiceBlockingStub stub =
                MovieServiceGrpc.newBlockingStub(channel);

        MovieRequest request = MovieRequest.newBuilder().setId(1).build();
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