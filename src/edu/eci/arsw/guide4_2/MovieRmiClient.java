package edu.eci.arsw.guide4_2;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class MovieRmiClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 23000);
        MovieService service = (MovieService) registry.lookup("movieService");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese ID de pelicula (1-3): ");
        int id = scanner.nextInt();

        Movie movie = service.getMovie(id);
        if (movie != null) {
            System.out.println("Película recibida: " + movie);
        } else {
            System.out.println("Película no encontrada");
        }
    }
}
