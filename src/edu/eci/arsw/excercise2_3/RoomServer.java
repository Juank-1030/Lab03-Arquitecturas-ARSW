package edu.eci.arsw.excercise2_3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RoomServer {
    public static void main(String[] args) throws Exception {
        RoomRepository repository = new RoomRepository();
        ServerSocket serverSocket = new ServerSocket(36000);
        System.out.println("RoomServer TCP escuchando en puerto 36000...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String request = in.readLine();
            String response = processRequest(request, repository);
            out.println(response);
            in.close();
            out.close();
            clientSocket.close();
        }
    }

    private static String processRequest(String request, RoomRepository repository) {
        if (request == null) {
            return "ERROR: solicitud vacia";
        }

        String[] parts = request.split(",", 2);
        if (parts.length < 2) {
            return "ERROR: formato invalido. Use COMANDO,CODIGO";
        }

        String command = parts[0].trim();
        String code = parts[1].trim();

        switch (command) {
            case "CONSULTAR_SALON":
                return repository.consult(code);
            case "RESERVAR_SALON":
                return repository.reserve(code);
            case "LIBERAR_SALON":
                return repository.release(code);
            default:
                return "ERROR: comando desconocido. Use CONSULTAR_SALON, RESERVAR_SALON o LIBERAR_SALON";
        }
    }
}
