package edu.eci.arsw.excercise2_3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RoomClient {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Sistema de Gestion de Salones ===");
        System.out.print("Ingrese comando (CONSULTAR_SALON, RESERVAR_SALON, LIBERAR_SALON): ");
        String command = scanner.nextLine().trim();

        System.out.print("Ingrese codigo del salon (E301, E302, E303, E304): ");
        String code = scanner.nextLine().trim();

        Socket socket = new Socket("127.0.0.1", 36000);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        String message = command + "," + code;
        out.println(message);
        String response = in.readLine();
        System.out.println("Respuesta del servidor: " + response);

        in.close();
        out.close();
        socket.close();
    }
}
