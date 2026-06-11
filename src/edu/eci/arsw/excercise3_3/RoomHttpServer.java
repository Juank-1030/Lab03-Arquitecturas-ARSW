package edu.eci.arsw.excercise3_3;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class RoomHttpServer {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/rooms", new RoomsHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("RoomHttpServer escuchando en http://localhost:8081/rooms");
    }

    static class RoomsHandler implements HttpHandler {
        private RoomRepository repository = new RoomRepository();

        @Override
        public void handle(HttpExchange exchange) {
            try {
                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();
                String query = exchange.getRequestURI().getQuery();
                String response;

                if ("GET".equals(method) && path.equals("/rooms")) {
                    if (query == null) {
                        response = listAll();
                    } else {
                        String code = extractId(query);
                        String status = repository.consult(code);
                        response = toHtml(code, status);
                    }
                } else if ("POST".equals(method) && path.equals("/rooms/reserve")) {
                    String code = extractId(query);
                    String result = repository.reserve(code);
                    response = toHtml(code, result);
                } else if ("POST".equals(method) && path.equals("/rooms/release")) {
                    String code = extractId(query);
                    String result = repository.release(code);
                    response = toHtml(code, result);
                } else {
                    response = "<html><body><h1>404 - Ruta no encontrada: " + method + " " + path + "</h1></body></html>";
                }

                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String listAll() {
            StringBuilder sb = new StringBuilder();
            sb.append("<html><body><h1>Salones disponibles:</h1><ul>");
            for (Room room : repository.findAll()) {
                sb.append("<li>").append(room.getCode()).append(" - ").append(room.toStatusText()).append("</li>");
            }
            sb.append("</ul></body></html>");
            return sb.toString();
        }

        private String toHtml(String code, String message) {
            return "<html><body><h1>" + code + ": " + message + "</h1></body></html>";
        }

        private String extractId(String query) {
            if (query == null || !query.startsWith("id=")) return "";
            return query.substring(3);
        }
    }
}
