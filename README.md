# Lab 03: Evolution of Distributed Architectures

**Escuela Colombiana de Ingenieria Julio Garavito**  
**Arquitecturas de Software - ARSW**  
**Author:** Rodrigo Humberto Gualtero

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Glossary](#2-glossary)
3. [Guide 2.2 - MovieServer TCP](#3-guide-22---movieserver-tcp)
4. [Exercise 2.3 - Room Management TCP](#4-exercise-23---room-management-tcp)
5. [Guide 3.2 - MovieHttpServer](#5-guide-32---moviehttpserver)
6. [Exercise 3.3 - Room Management HTTP](#6-exercise-33---room-management-http)
7. [Guide 4.2 - MovieService RMI](#7-guide-42---movieservice-rmi)
8. [Exercise 4.3 - Lab Inventory RMI](#8-exercise-43---lab-inventory-rmi)
9. [Guide 5.2 - MovieService gRPC](#9-guide-52---movieservice-grpc)
10. [Exercise 5.3 - University Wellness gRPC](#10-exercise-53---university-wellness-grpc)
11. [Guide 6.2 - Movie Microservices](#11-guide-62---movie-microservices)
12. [Exercise 6.3 - Wellness Microservices](#12-exercise-63---wellness-microservices)
13. [Guide 7.2 - MovieGateway](#13-guide-72---moviegateway)
14. [Exercise 7.3 - WellnessGateway](#14-exercise-73---wellnessgateway)
15. [Exercise 8 - ECICIENCIA](#15-exercise-8---eciciencia)

---

## 1. Introduction

This document traces the progressive evolution of distributed communication mechanisms in the context of the ARSW Lab 03 workshop. Starting from raw TCP sockets and progressing through HTTP, Java RMI, gRPC, microservices, and an API Gateway, each section presents a guided example followed by an applied exercise. The same base domain (a movie information system) is transformed across architectural styles to highlight how the choice of communication mechanism affects coupling, interoperability, contract formality, and design decisions. The workshop is structured as a hands-on journey where each style builds on the lessons of the previous one.

| Architectural Style | Key Advantage | Key Limitation | When to Use |
|---------------------|---------------|----------------|-------------|
| **Sockets TCP** | Full control over communication | Manual, low-level protocol design | Learning networking or very specific protocols |
| **HTTP** | Interoperability, browser access | Must design routes and response formats | Simple web APIs |
| **RMI** | Remote method invocation in Java | Tied to the Java ecosystem | Controlled Java distributed systems |
| **gRPC** | Formal contracts, efficiency, multi-language | Requires protobuf toolchain setup | Microservices and internal communication |
| **Microservices** | Separation of concerns, scalability | Higher operational complexity | Systems with clearly separable domains |
| **API Gateway** | Single entry point | Can become a bottleneck | Systems with multiple internal services |

---

## 2. Glossary

| Term | Section | Description |
|------|---------|-------------|
| Socket TCP | [Guide 2.2](#3-guide-22---movieserver-tcp) | Bidirectional communication channel between two processes over a network |
| ServerSocket | [Guide 2.2](#3-guide-22---movieserver-tcp) | JDK class that allows a server to listen for incoming connections on a port |
| Application Protocol | [Guide 2.2](#3-guide-22---movieserver-tcp) | Format convention (e.g. `MOVIE:id`) agreed upon by client and server above TCP |
| HTTP | [Guide 3.2](#5-guide-32---moviehttpserver) | Standard application protocol with method, path, headers, and body |
| HttpServer | [Guide 3.2](#5-guide-32---moviehttpserver) | JDK built-in HTTP server (`com.sun.net.httpserver`) |
| RMI | [Guide 4.2](#7-guide-42---movieservice-rmi) | Remote Method Invocation — invoke methods on objects in remote JVMs |
| Remote Interface | [Guide 4.2](#7-guide-42---movieservice-rmi) | Java interface extending `java.rmi.Remote` that defines the remote contract |
| RMI Registry | [Guide 4.2](#7-guide-42---movieservice-rmi) | Naming service that associates logical names with remote object references |
| gRPC | [Guide 5.2](#9-guide-52---movieservice-grpc) | Modern RPC framework using Protocol Buffers over HTTP/2 |
| .proto File | [Guide 5.2](#9-guide-52---movieservice-grpc) | Language-neutral contract file defining services and message types |
| Protocol Buffers | [Guide 5.2](#9-guide-52---movieservice-grpc) | Binary serialization format with schema-driven, strongly-typed messages |
| Microservices | [Guide 6.2](#11-guide-62---movie-microservices) | Architectural style dividing a system into small, autonomous, cohesive services |
| API Gateway | [Guide 7.2](#13-guide-72---moviegateway) | Single entry point that routes client requests to internal services |

---

## 3. Guide 2.2 - MovieServer TCP

**Package:** `src/edu/eci/arsw/guide2_2/`

### Problem Statement

Implement a TCP server that allows a client to query movie information using a text-based protocol. The server listens on port 35000 and the client sends a `MOVIE:<id>` command, receiving a CSV-formatted response.

### Architecture

The system consists of two processes communicating via a TCP socket. `MovieServer` listens on port 35000 and holds a `MovieRepository` with three pre-loaded movies in memory. `MovieClient` connects to the server, sends a text request in the format `MOVIE:<id>`, and receives a CSV response (`id,title,director,year`). There are no intermediaries — the client writes directly to the socket's `OutputStream` and reads from the `InputStream`. The protocol is ad-hoc: both parties must agree on the exact text format, the delimiter (`:`), and the response structure.

### Components

| File | Description |
|------|-------------|
| `Movie.java` | Data model with CSV serialization |
| `MovieRepository.java` | In-memory repository with 3 pre-loaded movies |
| `MovieServer.java` | TCP server on port 35000 |
| `MovieClient.java` | Console-based TCP client |

### Communication Protocol

| Command | Response |
|---------|----------|
| `MOVIE:<id>` | `id,title,director,year` |
| Non-existent ID | `ERROR: movie not found` |
| Invalid format | `ERROR: invalid format. Use MOVIE:id` |

### How to Build and Run

```bash
# Terminal 1 — start server
javac -d bin src/edu/eci/arsw/guide2_2/*.java
java -cp bin edu.eci.arsw.guide2_2.MovieServer

# Terminal 2 — run client
java -cp bin edu.eci.arsw.guide2_2.MovieClient
```

The server must be started before the client; otherwise the client receives `ConnectionException: Connection refused`.

### Execution Flow

1. `MovieServer.main()` instantiates `MovieRepository` and creates a `ServerSocket` bound to port 35000.
2. The server enters an infinite loop calling `serverSocket.accept()`, blocking until a client connects.
3. When a client connects, `accept()` returns a `Socket`. The server wraps the `InputStream` in a `BufferedReader` and the `OutputStream` in a `PrintWriter` with `autoFlush=true`.
4. The server reads a full line with `in.readLine()` (e.g. `MOVIE:1`).
5. `processRequest()` validates the prefix, extracts the ID, queries the repository, and serializes the result.
6. The server writes the response with `out.println()` and closes the streams and socket.
7. The `MovieClient` reads user input from the console, opens a `Socket("127.0.0.1", 35000)`, sends `MOVIE:<id>`, and displays the server response.

### Step-by-Step Implementation

#### Movie.java

```java
package edu.eci.arsw.guide2_2;

public class Movie {
    private int id;
    private String title;
    private String director;
    private int year;

    public Movie(int id, String title, String director, int year) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.year = year;
    }

    public int getId() { return id; }

    public String toText() {
        return id + "," + title + "," + director + "," + year;
    }
}
```

#### MovieRepository.java

```java
package edu.eci.arsw.guide2_2;

import java.util.HashMap;
import java.util.Map;

public class MovieRepository {
    private Map<Integer, Movie> movies = new HashMap<>();

    public MovieRepository() {
        movies.put(1, new Movie(1, "Interstellar", "Christopher Nolan", 2014));
        movies.put(2, new Movie(2, "Matrix", "Wachowski", 1999));
        movies.put(3, new Movie(3, "Inception", "Christopher Nolan", 2010));
    }

    public Movie findById(int id) { return movies.get(id); }
}
```

#### MovieServer.java

```java
package edu.eci.arsw.guide2_2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MovieServer {
    public static void main(String[] args) throws Exception {
        MovieRepository repository = new MovieRepository();
        ServerSocket serverSocket = new ServerSocket(35000);
        System.out.println("MovieServer TCP listening on port 35000...");

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

    private static String processRequest(String request, MovieRepository repository) {
        if (request == null || !request.startsWith("MOVIE:"))
            return "ERROR: invalid format. Use MOVIE:id";
        try {
            int id = Integer.parseInt(request.split(":")[1]);
            Movie movie = repository.findById(id);
            if (movie == null) return "ERROR: movie not found";
            return movie.toText();
        } catch (Exception e) {
            return "ERROR: invalid request";
        }
    }
}
```

#### MovieClient.java

```java
package edu.eci.arsw.guide2_2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MovieClient {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter movie ID: ");
        String id = scanner.nextLine();

        Socket socket = new Socket("127.0.0.1", 35000);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        out.println("MOVIE:" + id);
        String response = in.readLine();
        System.out.println("Server response: " + response);

        in.close();
        out.close();
        socket.close();
    }
}
```

### Design Decisions

- **Pure `ServerSocket`:** Uses the minimal JDK class to expose transport in its most elemental form, so the student understands that every distributed system reads and writes bytes from a socket.
- **Port 35000:** Outside the well-known and registered port ranges to avoid conflicts with system services and common development tools.
- **`autoFlush=true` in `PrintWriter`:** Ensures each `println()` sends data immediately over the network, appropriate for a simple line-based protocol.
- **One connection at a time:** The `while(true)` with blocking `accept()` is deliberately simple for later comparison with multi-threaded and NIO-based servers.
- **CSV serialization in `toText()`:** Centralizes format logic; if the format changes to JSON later, only this method is modified.
- **Immutability of Movie:** No setters — movies are immutable once created, avoiding inconsistent states in a distributed environment.
- **`HashMap<Integer, Movie>`:** O(1) average lookup; pre-loading in the constructor requires no external configuration.

### Design Reflection

- **The contract is implicit:** There is no interface file, schema, or WSDL. The contract exists only in documentation and `processRequest()`. Any change requires manual modification on both sides.
- **Tight coupling:** Client and server share the same data model (`Movie`), serialization format (CSV), and message convention (`MOVIE:<id>`). They cannot evolve independently.
- **No network abstraction:** Server code mixes business logic (finding a movie) with transport logic (reading/writing sockets), hindering unit testing and reuse.
- **No concurrency:** Two simultaneous clients are queued. If the first client is slow, the second experiences unnecessary delay.
- **No network exception handling:** If the client disconnects before sending a complete message, the server may receive `null` or corrupted data.

### Expected Output

```
Terminal 1:
MovieServer TCP listening on port 35000...

Terminal 2 (existing movie):
Enter movie ID: 1
Server response: 1,Interstellar,Christopher Nolan,2014

Terminal 2 (non-existent ID):
Enter movie ID: 5
Server response: ERROR: movie not found

Terminal 2 (invalid input):
Enter movie ID: abc
Server response: ERROR: invalid request
```

---

## 4. Exercise 2.3 - Room Management TCP

**Package:** `src/edu/eci/arsw/excercise2_3/`

### Problem Statement

Design and implement a TCP server for managing room reservations (rooms E301, E302, E303, E304). The system supports consulting room status, reserving an available room, and releasing a reserved room.

### Architecture

The system follows the same client-server pattern as Guide 2.2 but with a different domain and protocol. `RoomServer` listens on port 36000 and maintains a `RoomRepository` with four pre-loaded rooms. `RoomClient` sends commands in the format `COMANDO,CODIGO` (e.g. `RESERVAR_SALON,E303`) and receives plain-text responses. The comma (`,`) as separator distinguishes this protocol from the movie protocol (which uses `:`). Unlike the movie server (read-only), this server supports write operations (reserve/release) that mutate shared repository state.

### Components

| File | Description |
|------|-------------|
| `Room.java` | Room model with code and reservation status |
| `RoomRepository.java` | In-memory repository with 4 pre-loaded rooms |
| `RoomServer.java` | TCP server on port 36000 |
| `RoomClient.java` | Console-based TCP client |

### Communication Protocol

| Command | Success Response | Error Response |
|---------|-----------------|----------------|
| `CONSULTAR_SALON,CODIGO` | `SALON_DISPONIBLE` / `SALON_RESERVADO` | `ERROR_SALON_NO_EXISTE` |
| `RESERVAR_SALON,CODIGO` | `RESERVA_EXITOSA` | `ERROR_SALON_NO_EXISTE` / `ERROR_SALON_RESERVADO` |
| `LIBERAR_SALON,CODIGO` | `LIBERACION_EXITOSA` | `ERROR_SALON_NO_EXISTE` / `ERROR_OPERACION_INVALIDA` |

### How to Build and Run

```bash
# Terminal 1 — start server
javac -d bin src/edu/eci/arsw/excercise2_3/*.java
java -cp bin edu.eci.arsw.excercise2_3.RoomServer

# Terminal 2 — run client
java -cp bin edu.eci.arsw.excercise2_3.RoomClient
```

### Execution Flow

1. `RoomServer.main()` creates a `RoomRepository` (four rooms: E301-E304) and a `ServerSocket` on port 36000.
2. The server accepts a connection, reads a line with format `COMANDO,CODIGO`, and calls `processRequest()`.
3. `processRequest()` splits the line by `,`, identifies the command via `switch`, and delegates to the appropriate repository method.
4. The repository method validates existence and current state, mutates state if needed, and returns a protocol string.
5. The server sends the response back to the client and closes the connection.

### Step-by-Step Implementation

#### Room.java

```java
package edu.eci.arsw.excercise2_3;

public class Room {
    private String code;
    private boolean reserved;

    public Room(String code) {
        this.code = code;
        this.reserved = false;
    }

    public String getCode() { return code; }
    public boolean isReserved() { return reserved; }
    public void setReserved(boolean reserved) { this.reserved = reserved; }

    public String toStatusText() {
        return reserved ? "SALON_RESERVADO" : "SALON_DISPONIBLE";
    }
}
```

#### RoomRepository.java

```java
package edu.eci.arsw.excercise2_3;

import java.util.HashMap;
import java.util.Map;

public class RoomRepository {
    private Map<String, Room> rooms = new HashMap<>();

    public RoomRepository() {
        rooms.put("E301", new Room("E301"));
        rooms.put("E302", new Room("E302"));
        rooms.put("E303", new Room("E303"));
        rooms.put("E304", new Room("E304"));
    }

    public String consult(String code) {
        Room room = rooms.get(code);
        if (room == null) return "ERROR_SALON_NO_EXISTE";
        return room.toStatusText();
    }

    public String reserve(String code) {
        Room room = rooms.get(code);
        if (room == null) return "ERROR_SALON_NO_EXISTE";
        if (room.isReserved()) return "ERROR_SALON_RESERVADO";
        room.setReserved(true);
        return "RESERVA_EXITOSA";
    }

    public String release(String code) {
        Room room = rooms.get(code);
        if (room == null) return "ERROR_SALON_NO_EXISTE";
        if (!room.isReserved()) return "ERROR_OPERACION_INVALIDA";
        room.setReserved(false);
        return "LIBERACION_EXITOSA";
    }
}
```

#### RoomServer.java (key section)

```java
ServerSocket serverSocket = new ServerSocket(36000);
while (true) {
    Socket clientSocket = serverSocket.accept();
    BufferedReader in = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));
    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
    String request = in.readLine();
    String response = processRequest(request, repository);
    out.println(response);
    in.close(); out.close(); clientSocket.close();
}
```

#### RoomClient.java (key section)

```java
System.out.print("Enter command (CONSULTAR_SALON, RESERVAR_SALON, LIBERAR_SALON): ");
String command = scanner.nextLine().trim();
System.out.print("Enter room code (E301, E302, E303, E304): ");
String code = scanner.nextLine().trim();

Socket socket = new Socket("127.0.0.1", 36000);
PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
BufferedReader in = new BufferedReader(
        new InputStreamReader(socket.getInputStream()));

String message = command + "," + code;
out.println(message);
String response = in.readLine();
System.out.println("Server response: " + response);
```

### Design Decisions

- **String key for rooms:** Room codes like `E301` are alphanumeric, so `Map<String, Room>` is used instead of `Map<Integer, Room>`.
- **Mutable Room model:** Unlike `Movie` (immutable), `Room` has a `setReserved()` method because the exercise requires state modification.
- **Each repository method returns the protocol string directly:** This eliminates the need for the server to interpret intermediate codes.
- **`split(",", 2)` with limit 2:** Prevents room codes containing commas (not applicable here, but good practice) from breaking parsing.
- **Race condition potential:** If two clients call `RESERVAR_SALON,E303` simultaneously, both could pass the `isReserved()` check before either `setReserved(true)` completes. This is a classic "check-then-act" race condition, invisible in a single-threaded server but requiring `synchronized` in a multi-threaded one.

### Design Differences from Guide 2.2

| Aspect | Guide 2.2 (MovieServer) | Exercise 2.3 (RoomServer) |
|--------|------------------------|---------------------------|
| Separator | `:` (`MOVIE:1`) | `,` (`CONSULTAR_SALON,E303`) |
| Operations | Read-only | Read, write, release |
| Model | Immutable (`Movie`) | Mutable (`Room`) |
| Port | 35000 | 36000 |
| Client | Prompts only for ID | Prompts for command + code |
| Additional errors | — | `ERROR_SALON_RESERVADO`, `ERROR_OPERACION_INVALIDA` |

### Design Reflection

- Adding a new operation requires: (1) adding the method in `RoomRepository`, (2) adding a `case` in `processRequest()`, (3) documenting the new command. The text-based protocol makes the change simple but error-prone if client and server are not updated simultaneously.
- In the current single-threaded server, simultaneous client requests are queued, so there is no race condition. However, if the server used threads, both clients could pass the `isReserved()` validation before either executes `setReserved(true)`, resulting in a double booking.
- Like Guide 2.2, the contract is implicit: it exists in documentation and `processRequest()`. There is no interface file, schema, or WSDL.

### Expected Output

```
Terminal 1:
RoomServer TCP listening on port 36000...

Terminal 2:
=== Room Management System ===
Enter command (CONSULTAR_SALON, RESERVAR_SALON, LIBERAR_SALON): CONSULTAR_SALON
Enter room code (E301, E302, E303, E304): E303
Server response: SALON_DISPONIBLE

Complete sequence:
CONSULTAR_SALON, E303  -> SALON_DISPONIBLE
RESERVAR_SALON, E303   -> RESERVA_EXITOSA
CONSULTAR_SALON, E303  -> SALON_RESERVADO
LIBERAR_SALON, E303    -> LIBERACION_EXITOSA
```

### Reflection Questions

1. **How easy would it be to add a new operation?** Only requires: (1) adding the method in `RoomRepository`, (2) adding a `case` in `processRequest()`, (3) documenting the new command. The text-based protocol makes the change simple but error-prone if client and server are not updated at the same time.

2. **What happens if two clients try to reserve the same room simultaneously?** In the current single-threaded server, the second client waits for the first to finish, so there is no race condition. But if the server used threads, both could pass the `isReserved()` check before either executes `setReserved(true)`, resulting in a double booking.

3. **Where is the communication contract actually defined?** Like Guide 2.2, the contract is implicit: it exists in documentation and in the logic of `processRequest()`. There is no interface file, schema, or WSDL to formalize it.

---

## 5. Guide 3.2 - MovieHttpServer

**Package:** `src/edu/eci/arsw/guide3_2/`

### Problem Statement

Replace the custom TCP client with any standard HTTP client (browser, curl, Postman). The server uses `com.sun.net.httpserver.HttpServer` (included in the JDK).

### Architecture

The system replaces the custom TCP client with any standard HTTP client (browser, curl, Postman). `MovieHttpServer` runs on port 8080 using `com.sun.net.httpserver.HttpServer`. It registers a `MovieHandler` at the `/movie` context, which extracts the `id` parameter from the query string of `GET` requests, queries the `MovieRepository`, and returns the result as a minimal HTML page. The critical architectural change is that the client no longer needs custom Java code — the contract is now the standard HTTP protocol: method (`GET`), path (`/movie`), and query parameter (`id=1`).

### Components

| File | Description |
|------|-------------|
| `Movie.java` | Data model (identical to guide2_2) |
| `MovieRepository.java` | In-memory repository (identical to guide2_2) |
| `MovieHttpServer.java` | HTTP server on port 8080 with inner `MovieHandler` |

### Communication Protocol

| Element | Value |
|---------|-------|
| Method | `GET` |
| Path | `/movie` |
| Parameter | `id=<number>` |
| Success response | `200 OK` with HTML body |
| Error response | `200 OK` with HTML body ("not found") |

### How to Build and Run

```bash
# Terminal
javac -d bin src/edu/eci/arsw/guide3_2/*.java
java -cp bin edu.eci.arsw.guide3_2.MovieHttpServer

# Test with curl
curl "http://localhost:8080/movie?id=1"
```

### Execution Flow

1. `HttpServer.create(new InetSocketAddress(8080), 0)` creates the HTTP server bound to port 8080.
2. `server.createContext("/movie", handler)` registers `MovieHandler` for requests whose path starts with `/movie`.
3. `server.setExecutor(null)` configures the default executor (single-threaded, same behavior as guide2_2).
4. `server.start()` starts the server on a background thread.
5. For each `GET /movie?id=1`, `HttpServer` invokes `MovieHandler.handle(exchange)`.
6. `exchange.getRequestURI().getQuery()` extracts `"id=1"`.
7. `extractId()` validates the `id=` prefix and extracts the numeric value. Returns `-1` if invalid.
8. `repository.findById(id)` looks up the movie.
9. The HTML response is built: success -> `"<html>...<h1>" + movie.toText() + "</h1>..."`, error -> `"<html>...<h1>Movie not found</h1>..."`.
10. `exchange.sendResponseHeaders(200, response.getBytes().length)` sends status code 200 and `Content-Length`.
11. `exchange.getResponseBody()` returns the `OutputStream` for writing the body.
12. The response is written, the stream is closed, and the response completes.

### Step-by-Step Implementation

#### MovieHttpServer.java

```java
package edu.eci.arsw.guide3_2;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class MovieHttpServer {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        MovieRepository repository = new MovieRepository();
        server.createContext("/movie", new MovieHandler(repository));
        server.setExecutor(null);
        server.start();
        System.out.println("MovieHttpServer listening at http://localhost:8080/movie?id=1");
    }

    static class MovieHandler implements HttpHandler {
        private MovieRepository repository;

        public MovieHandler(MovieRepository repository) {
            this.repository = repository;
        }

        @Override
        public void handle(HttpExchange exchange) {
            try {
                String query = exchange.getRequestURI().getQuery();
                int id = extractId(query);
                Movie movie = repository.findById(id);
                String response;
                if (movie == null) {
                    response = "<html><body><h1>Movie not found</h1></body></html>";
                } else {
                    response = "<html><body><h1>" + movie.toText() + "</h1></body></html>";
                }
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private int extractId(String query) {
            if (query == null || !query.startsWith("id=")) return -1;
            return Integer.parseInt(query.substring(3));
        }
    }
}
```

### Design Decisions

- **`HttpServer` from JDK:** No external dependencies required. Provides path-based routing (`createContext`), status codes, and body streaming.
- **Port 8080:** Conventional port for HTTP development servers. Recognized by browsers, curl, and HTTP tools.
- **`setExecutor(null)`:** Single-threaded, same blocking behavior as guide2_2. Passing `Executors.newFixedThreadPool(10)` would enable concurrency.
- **Manual query string parsing:** `HttpServer` does not provide automatic parameter parsing. `extractId()` manually checks the `id=` prefix.
- **HTML response format:** HTML was chosen because it is the browser's native format. The switch from CSV (machine-readable) to HTML (human-readable) reflects that HTTP serves human clients (browsers) while TCP serves machine clients.
- **Hardcoded 200:** All responses use 200 OK, even when the movie does not exist. A proper RESTful API would use `404 Not Found`. This simplification keeps the focus on HTTP transport, not API design.

### Design Reflection

- **HTTP standardizes the contract:** The protocol is no longer `MOVIE:<id>` but `GET /movie?id=<number>`. Any HTTP client understands this structure without additional documentation.
- **The business model does not change:** `Movie` and `MovieRepository` are identical to guide 2.2. Only the transport layer was replaced, confirming the separation of concerns principle.
- **Immediate interoperability:** The service can be tested with curl, a browser, or Postman without writing a single line of client code.
- **Limitations of the built-in server:** No automatic parameter parsing, no content negotiation, no proper HTTP status codes, no HTTPS support.

### Comparison: TCP vs HTTP

| Aspect | Guide 2.2 (TCP) | Guide 3.2 (HTTP) |
|--------|-----------------|------------------|
| Transport | Raw TCP socket | HTTP 1.1 over TCP |
| Contract | Ad-hoc `MOVIE:<id>` | Standard: `GET /movie?id=<n>` |
| Client | Java only (`MovieClient`) | Any HTTP client |
| Message structure | Single text line | Method + path + query + headers + body |
| Request parsing | `split(":")` manually | `HttpExchange.getRequestURI().getQuery()` |
| Response format | Plain text (CSV) | HTML with tags |
| Error codes | Text in response | 200 OK always (no 404) |
| Port | 35000 | 8080 |
| Server API | `ServerSocket` + `accept()` | `HttpServer` + `createContext()` |
| Concurrency | Single-threaded blocking | Single-threaded (`setExecutor(null)`) |
| Interoperability | Low (proprietary protocol) | High (standard protocol) |

### Expected Output

```
curl "http://localhost:8080/movie?id=1"
# <html><body><h1>1,Interstellar,Christopher Nolan,2014</h1></body></html>

curl "http://localhost:8080/movie?id=999"
# <html><body><h1>Movie not found</h1></body></html>

curl "http://localhost:8080/"
# 404 Not Found (default HttpServer response)
```

---

## 6. Exercise 3.3 - Room Management HTTP

**Package:** `src/edu/eci/arsw/excercise3_3/`

### Problem Statement

Transform the TCP room management system (Exercise 2.3) to HTTP using `com.sun.net.httpserver`. The domain (rooms with reservation/release) is the same, but the protocol changes from an ad-hoc format (`COMANDO,CODIGO`) to standardized HTTP routes.

### Architecture

The system extends the HTTP pattern from Guide 3.2 to support multiple operations on a single resource. `RoomHttpServer` runs on port 8081 and registers a single `RoomsHandler` at the `/rooms` context. Unlike the movie server (which only handles GET), this handler inspects both the HTTP method (`GET` vs `POST`) and the path (`/rooms`, `/rooms/reserve`, `/rooms/release`) to dispatch to the correct `RoomRepository` method.

### Components

| File | Description |
|------|-------------|
| `Room.java` | Data model (identical to excercise2_3) |
| `RoomRepository.java` | In-memory repository. Adds `findAll()` compared to excercise2_3 |
| `RoomHttpServer.java` | HTTP server on port 8081 with inner `RoomsHandler` |

### Routes Implemented

| Method | Route | Query | Description |
|--------|-------|-------|-------------|
| `GET` | `/rooms` | (none) | List all rooms with their status |
| `GET` | `/rooms` | `id=E303` | Query status of a specific room |
| `POST` | `/rooms/reserve` | `id=E303` | Reserve a room |
| `POST` | `/rooms/release` | `id=E303` | Release a room |

### How to Build and Run

```bash
# Terminal
javac -d bin src/edu/eci/arsw/excercise3_3/*.java
java -cp bin edu.eci.arsw.excercise3_3.RoomHttpServer

# Test with curl
curl "http://localhost:8081/rooms"
curl "http://localhost:8081/rooms?id=E303"
curl -X POST "http://localhost:8081/rooms/reserve?id=E303"
curl -X POST "http://localhost:8081/rooms/release?id=E303"
```

### Execution Flow

1. `HttpServer.create(new InetSocketAddress(8081), 0)` creates the server on port 8081.
2. `createContext("/rooms", new RoomsHandler())` registers the handler for any path starting with `/rooms`.
3. `handler.handle(exchange)` is invoked for each incoming request.
4. `exchange.getRequestMethod()` returns `"GET"` or `"POST"`.
5. `exchange.getRequestURI().getPath()` returns `"/rooms"`, `"/rooms/reserve"`, or `"/rooms/release"`.
6. `exchange.getRequestURI().getQuery()` returns the query string (e.g. `"id=E303"`) or `null`.
7. The handler applies dispatch logic: GET `/rooms` without query -> list all; GET `/rooms` with query -> consult; POST `/rooms/reserve` -> reserve; POST `/rooms/release` -> release.
8. `exchange.sendResponseHeaders(200, length)` sends status 200.
9. The response body is written and closed.

### Step-by-Step Implementation

#### RoomHttpServer.java

```java
package edu.eci.arsw.excercise3_3;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAuthority;

public class RoomHttpServer {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/rooms", new RoomsHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("RoomHttpServer listening at http://localhost:8081/rooms");
    }

    static class RoomsHandler implements HttpHandler {
        private RoomRepository repository = new RoomRepository();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();
            String response;

            if ("GET".equals(method) && path.equals("/rooms")) {
                if (query == null) {
                    response = listAll();
                } else {
                    String code = extractId(query);
                    response = toHtml(code, repository.consult(code));
                }
            } else if ("POST".equals(method) && path.equals("/rooms/reserve")) {
                String code = extractId(query);
                response = toHtml(code, repository.reserve(code));
            } else if ("POST".equals(method) && path.equals("/rooms/release")) {
                String code = extractId(query);
                response = toHtml(code, repository.release(code));
            } else {
                response = "<html><body><h1>404 - Route not found: "
                        + method + " " + path + "</h1></body></html>";
            }

            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private String extractId(String query) {
            if (query == null || !query.startsWith("id=")) return "";
            return query.substring(3);
        }

        private String toHtml(String code, String message) {
            return "<html><body><h1>" + code + ": " + message + "</h1></body></html>";
        }

        private String listAll() {
            StringBuilder sb = new StringBuilder();
            sb.append("<html><body><h1>Available rooms:</h1><ul>");
            for (Room room : repository.findAll()) {
                sb.append("<li>").append(room.getCode())
                  .append(" - ").append(room.toStatusText()).append("</li>");
            }
            sb.append("</ul></body></html>");
            return sb.toString();
        }
    }
}
```

#### RoomRepository.java (with findAll)

```java
public Collection<Room> findAll() {
    return rooms.values();
}
```

### Design Decisions

- **Port 8081:** Avoids conflict with Guide 3.2 (port 8080), following the convention from TCP exercises (35000 vs 36000).
- **Single context (`/rooms`) with internal dispatch:** Instead of creating separate contexts for each route, one handler dispatches internally using method + path inspection.
- **`path.equals("/rooms")` exact match:** Prevents `/rooms/algo` from being interpreted as a room listing.
- **`findAll()` added to repository:** HTTP introduces a discovery operation (list all rooms) that did not exist in the TCP protocol.
- **Query string as differentiator:** GET `/rooms` without query lists all; with query consults a specific room.
- **No proper HTTP status codes:** All responses use 200, even for errors (same limitation as Guide 3.2).

### Design Differences from Exercise 2.3 (TCP)

| Aspect | Exercise 2.3 (TCP) | Exercise 3.3 (HTTP) |
|--------|--------------------|---------------------|
| Transport | Raw TCP socket | HTTP 1.1 over TCP |
| Message format | `COMANDO,CODIGO` | GET/POST + path + query |
| Commands | `CONSULTAR_SALON`, `RESERVAR_SALON`, `LIBERAR_SALON` | GET `/rooms?id=`, POST `/rooms/reserve?id=`, POST `/rooms/release?id=` |
| List rooms | Not supported | GET `/rooms` |
| Parsing | `split(",", 2)` manual | `getRequestMethod()`, `getPath()`, `getQuery()` |
| Separator | Comma (`,`) | Path and query string |
| Response format | Plain text | HTML with tags |
| Port | 36000 | 8081 |
| Client | Java only (`RoomClient`) | Any HTTP client |
| Routing | `switch(command)` with 3 cases | `if (method+path)` with 4 cases + catch-all 404 |
| Repository | Individual operations only | `findAll()` added for listing |

### Reflection Questions

1. **What advantages does HTTP offer compared to a manual text protocol?**
   - Standardization: any HTTP client (browser, curl, Postman) can consume the service without custom code.
   - Built-in semantics: GET (read) vs POST (write) express intent.
   - Structure: method, path, query, headers, and body are universal concepts.
   - Tooling: debugging with DevTools, proxies like Fiddler, clients like Postman.

2. **What limitations does building an HTTP server without a framework have?**
   - Manual routing (no `@GetMapping`).
   - Manual query string parsing (no `@RequestParam`).
   - No content negotiation (no JSON vs XML).
   - No correct HTTP status codes (always 200).
   - No structured error handling.
   - No automatic input validation.
   - No support for HTTPS, CORS, sessions, etc.

3. **How would the solution change using JSON instead of HTML?**
   - The response would be `{"code": "E303", "status": "SALON_DISPONIBLE"}` instead of HTML.
   - It would be easier to parse by machine clients (JavaScript, Python, etc.).
   - Direct browser visualization would be lost.
   - A JSON library (Jackson, Gson) or manual parsing would be required.
   - `Content-Type` would change from `text/html` to `application/json`.

### Expected Output

```
curl "http://localhost:8081/rooms"
# <html><body><h1>Available rooms:</h1><ul><li>E301 - SALON_DISPONIBLE</li>...</ul></body></html>

curl "http://localhost:8081/rooms?id=E303"
# <html><body><h1>E303: SALON_DISPONIBLE</h1></body></html>

curl -X POST "http://localhost:8081/rooms/reserve?id=E303"
# <html><body><h1>E303: RESERVA_EXITOSA</h1></body></html>

curl -X POST "http://localhost:8081/rooms/release?id=E303"
# <html><body><h1>E303: LIBERACION_EXITOSA</h1></body></html>
```

---

## 7. Guide 4.2 - MovieService RMI

**Package:** `src/edu/eci/arsw/guide4_2/`

### Problem Statement

Replace HTTP with Java RMI to expose movie query functionality as a remote method invocation. RMI eliminates manual message format design; communication is expressed as remote method calls.

### Architecture

The system introduces a three-tier RMI architecture. `MovieRmiServer` creates a `MovieServiceImpl` (extending `UnicastRemoteObject` for auto-export) and binds it to an RMI `Registry` on port 23000 under the name `"movieService"`. `MovieRmiClient` connects to the same Registry via `LocateRegistry.getRegistry()`, looks up `"movieService"` to obtain a stub (a dynamic proxy implementing `MovieService` locally), and calls `getMovie(2)` as if it were a local method. Behind the scenes, RMI serializes the `int` argument, transmits it to the server's JVM, deserializes it, invokes the real implementation, serializes the returned `Movie` object, and sends it back to the client where it is deserialized into a local copy.

### Components

| File | Description |
|------|-------------|
| `Movie.java` | Data model, implements `Serializable` (required for pass-by-value) |
| `MovieService.java` | Remote interface extending `Remote` |
| `MovieServiceImpl.java` | Implementation extending `UnicastRemoteObject` |
| `MovieRmiServer.java` | Creates Registry on port 23000 and publishes the service |
| `MovieRmiClient.java` | Obtains remote reference and invokes `getMovie()` |

### Remote Contract

```java
public interface MovieService extends Remote {
    Movie getMovie(int id) throws RemoteException;
}
```

| Element | Description |
|---------|-------------|
| Interface | `MovieService` extends `java.rmi.Remote` |
| Method | `Movie getMovie(int id)` |
| Exception | `RemoteException` (every remote call must declare it) |
| Parameter passing | `int id` — by value (primitives are always copied) |
| Return | `Movie` — by value (serialization), the entire object is copied |
| Registry port | 23000 |
| Service name | `"movieService"` |

### How to Build and Run

```bash
# Terminal 1 — start server
javac -d bin src/edu/eci/arsw/guide4_2/*.java
java -cp bin edu.eci.arsw.guide4_2.MovieRmiServer

# Terminal 2 — run client
java -cp bin edu.eci.arsw.guide4_2.MovieRmiClient
```

### Execution Flow

1. `MovieRmiServer.main()` creates `MovieServiceImpl`, which extends `UnicastRemoteObject` and auto-exports.
2. `LocateRegistry.createRegistry(23000)` creates a Registry process on port 23000 within the same JVM.
3. `registry.rebind("movieService", service)` associates the name `"movieService"` with the remote reference.
4. `MovieRmiClient.main()` calls `LocateRegistry.getRegistry("127.0.0.1", 23000)` to connect to the Registry.
5. `registry.lookup("movieService")` obtains the stub (proxy) of the remote service.
6. `service.getMovie(2)` invokes the method on the proxy. RMI serializes the argument `2` and sends it to the server.
7. On the server, RMI deserializes the arguments, invokes `MovieServiceImpl.getMovie(2)`, obtains the `Movie` object, and serializes it back to the client.
8. On the client, RMI deserializes the `Movie` object (creating a local copy) and returns it.

### Step-by-Step Implementation

#### Movie.java

```java
package edu.eci.arsw.guide4_2;

import java.io.Serializable;

public class Movie implements Serializable {
    private int id;
    private String title;
    private String director;
    private int year;

    public Movie(int id, String title, String director, int year) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.year = year;
    }

    public int getId() { return id; }

    @Override
    public String toString() {
        return id + " - " + title + " (" + year + ") - " + director;
    }
}
```

#### MovieService.java

```java
package edu.eci.arsw.guide4_2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MovieService extends Remote {
    Movie getMovie(int id) throws RemoteException;
}
```

#### MovieServiceImpl.java

```java
package edu.eci.arsw.guide4_2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class MovieServiceImpl extends UnicastRemoteObject implements MovieService {
    private Map<Integer, Movie> movies = new HashMap<>();

    public MovieServiceImpl() throws RemoteException {
        movies.put(1, new Movie(1, "Interstellar", "Christopher Nolan", 2014));
        movies.put(2, new Movie(2, "Matrix", "Wachowski", 1999));
        movies.put(3, new Movie(3, "Inception", "Christopher Nolan", 2010));
    }

    @Override
    public Movie getMovie(int id) throws RemoteException {
        return movies.get(id);
    }
}
```

#### MovieRmiServer.java

```java
package edu.eci.arsw.guide4_2;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MovieRmiServer {
    public static void main(String[] args) throws Exception {
        MovieService service = new MovieServiceImpl();
        Registry registry = LocateRegistry.createRegistry(23000);
        registry.rebind("movieService", service);
        System.out.println("MovieService RMI published on port 23000...");
    }
}
```

#### MovieRmiClient.java

```java
package edu.eci.arsw.guide4_2;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class MovieRmiClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 23000);
        MovieService service = (MovieService) registry.lookup("movieService");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter movie ID (1-3): ");
        int id = scanner.nextInt();
        Movie movie = service.getMovie(id);
        if (movie != null) {
            System.out.println("Movie received: " + movie);
        } else {
            System.out.println("Movie not found");
        }
    }
}
```

### Design Decisions

- **`UnicastRemoteObject`:** Base class for RMI remote objects communicating via direct TCP (unicast). The constructor automatically exports the object.
- **`RemoteException`:** Every remote invocation can fail due to network issues. The compiler forces declaration or handling of `RemoteException`.
- **`Serializable` on Movie:** Required for pass-by-value semantics. RMI serializes the object for network transmission.
- **`LocateRegistry.createRegistry(23000)` vs external `rmiregistry`:** The Registry is created within the same JVM, avoiding a separate terminal.
- **`rebind()` vs `bind()`:** `rebind()` replaces any existing binding, more tolerant of server restarts.

### Design Reflection

- **RMI eliminates manual parsing:** No `split(":")` or `getQuery()`. Arguments are passed as Java method parameters. RMI handles serialization, transmission, deserialization, and routing.
- **The contract is a Java interface:** Anyone with the `MovieService` interface knows exactly what methods and types exist. No external documentation or curl experiments needed.
- **The object travels over the network:** `Movie` is transmitted completely (by value). The client receives a local copy. Modifying the copy does not affect the server.
- **Extreme coupling:** Client and server must share the same `MovieService` interface and `Movie` class. Changing the package, adding a field, or changing the serialization version breaks communication.
- **`RemoteException` is contagious:** Any method invoking a remote service must declare or catch `RemoteException`, contaminating the client method signatures.

### Comparison: HTTP vs RMI

| Aspect | Guide 3.2 (HTTP) | Guide 4.2 (RMI) |
|--------|------------------|-----------------|
| Paradigm | Request-response via URL | Remote method invocation |
| Contract | Implicit (GET /movie?id=n) | Explicit (`MovieService` interface) |
| Serialization | Manual to HTML | Automatic via Java Serialization |
| Request parsing | Manual (query string) | Automatic (Java arguments) |
| Response format | HTML (text) | Serialized Java object |
| Client | Any HTTP client | Java only (same interface) |
| Port | 8080 (server) | 23000 (Registry) + anonymous (object) |
| Coupling | Low (only known URL) | High (same Java interface) |
| Interoperability | High (HTTP is universal) | Low (Java only) |
| Error handling | HTTP codes + body | `RemoteException` + possible nulls |

### Expected Output

```
Terminal 1:
MovieService RMI published on port 23000...

Terminal 2:
Enter movie ID (1-3): 1
Movie received: 1 - Interstellar (2014) - Christopher Nolan

Enter movie ID (1-3): 5
Movie not found
```

---

## 8. Exercise 4.3 - Lab Inventory RMI

**Package:** `src/edu/eci/arsw/excercise4_3/`

### Problem Statement

Build an RMI system for querying and reserving laboratory equipment. Unlike the room domain (Exercise 2.3/3.3) which worked with plain strings, the lab inventory requires returning structured objects and lists. RMI handles this complexity automatically via serialization.

### Architecture

The system follows the same RMI pattern as Guide 4.2 but with a richer contract including collection retrieval (`List<String> consultarEquipos()`) and write operations (`boolean reservarEquipo()`, `boolean liberarEquipo()`). `EquipmentRmiServer` publishes an `EquipmentServiceImpl` (auto-exported via `UnicastRemoteObject`) on port 24000 under the name `"equipmentService"`. The implementation maintains a `HashMap<String, Equipment>` with five lab equipment items.

### Components

| File | Description |
|------|-------------|
| `Equipment.java` | Serializable model (code, name, lab, status) |
| `EquipmentService.java` | Remote interface with 4 methods |
| `EquipmentServiceImpl.java` | Implementation with HashMap of 5 equipment items |
| `EquipmentRmiServer.java` | Creates Registry on port 24000 and publishes `"equipmentService"` |
| `EquipmentRmiClient.java` | Interactive client with menu testing all methods |

### Remote Contract

```java
public interface EquipmentService extends Remote {
    List<String> consultarEquipos() throws RemoteException;
    String consultarEquipo(String codigo) throws RemoteException;
    boolean reservarEquipo(String codigo) throws RemoteException;
    boolean liberarEquipo(String codigo) throws RemoteException;
}
```

| Method | Return | Description |
|--------|--------|-------------|
| `consultarEquipos()` | `List<String>` | List all equipment (text format) |
| `consultarEquipo(codigo)` | `String` | Status of a specific equipment or error message |
| `reservarEquipo(codigo)` | `boolean` | `true` if reserved successfully, `false` if not exists or already reserved |
| `liberarEquipo(codigo)` | `boolean` | `true` if released successfully, `false` if not exists or already available |

### Test Data

| Code | Name | Lab | Initial Status |
|------|------|-----|----------------|
| LAP001 | Laptop Dell XPS 15 | Computer Lab | Available |
| LAP002 | Laptop HP Spectre | Computer Lab | Available |
| OSC001 | Oscilloscope Tektronix | Electronics Lab | Available |
| MIC001 | Microscope Olympus | Biology Lab | Available |
| CEN001 | Centrifuge Eppendorf | Biology Lab | Available |

### How to Build and Run

```bash
# Terminal 1 — start server
javac -d bin src/edu/eci/arsw/excercise4_3/*.java
java -cp bin edu.eci.arsw.excercise4_3.EquipmentRmiServer

# Terminal 2 — run client
java -cp bin edu.eci.arsw.excercise4_3.EquipmentRmiClient
```

### Execution Flow

1. `EquipmentRmiServer.main()` creates `EquipmentServiceImpl`.
2. `LocateRegistry.createRegistry(24000)` creates the Registry.
3. `registry.rebind("equipmentService", service)` publishes the service.
4. The client obtains the remote Registry and performs a lookup.
5. `service.consultarEquipos()` returns serialized `ArrayList<String>`.
6. `service.consultarEquipo("LAP001")` returns a descriptive string or error.
7. `service.reservarEquipo("LAP001")` returns `true` if it exists and is available.
8. `service.liberarEquipo("LAP001")` returns `true` if it exists and is reserved.

### Step-by-Step Implementation

#### EquipmentService.java

```java
package edu.eci.arsw.excercise4_3;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface EquipmentService extends Remote {
    List<String> consultarEquipos() throws RemoteException;
    String consultarEquipo(String codigo) throws RemoteException;
    boolean reservarEquipo(String codigo) throws RemoteException;
    boolean liberarEquipo(String codigo) throws RemoteException;
}
```

#### EquipmentServiceImpl.java

```java
package edu.eci.arsw.excercise4_3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipmentServiceImpl extends UnicastRemoteObject implements EquipmentService {
    private Map<String, Equipment> equipment = new HashMap<>();

    public EquipmentServiceImpl() throws RemoteException {
        equipment.put("LAP001", new Equipment("LAP001", "Laptop Dell XPS 15", "Computer Lab"));
        equipment.put("LAP002", new Equipment("LAP002", "Laptop HP Spectre", "Computer Lab"));
        equipment.put("OSC001", new Equipment("OSC001", "Oscilloscope Tektronix", "Electronics Lab"));
        equipment.put("MIC001", new Equipment("MIC001", "Microscope Olympus", "Biology Lab"));
        equipment.put("CEN001", new Equipment("CEN001", "Centrifuge Eppendorf", "Biology Lab"));
    }

    @Override
    public List<String> consultarEquipos() {
        List<String> result = new ArrayList<>();
        for (Equipment eq : equipment.values()) {
            result.add(eq.toString());
        }
        return result;
    }

    @Override
    public String consultarEquipo(String codigo) {
        Equipment eq = equipment.get(codigo);
        if (eq == null) return "ERROR: equipment not found";
        return eq.toString();
    }

    @Override
    public boolean reservarEquipo(String codigo) {
        Equipment eq = equipment.get(codigo);
        if (eq == null || eq.isReserved()) return false;
        eq.setReserved(true);
        return true;
    }

    @Override
    public boolean liberarEquipo(String codigo) {
        Equipment eq = equipment.get(codigo);
        if (eq == null || !eq.isReserved()) return false;
        eq.setReserved(false);
        return true;
    }
}
```

#### EquipmentRmiClient.java (interactive menu)

```java
package edu.eci.arsw.excercise4_3;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class EquipmentRmiClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 24000);
        EquipmentService service = (EquipmentService) registry.lookup("equipmentService");
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Laboratory Inventory System ===");

        while (true) {
            System.out.println("\nAvailable operations:");
            System.out.println("  1. List all equipment");
            System.out.println("  2. Query equipment");
            System.out.println("  3. Reserve equipment");
            System.out.println("  4. Release equipment");
            System.out.println("  5. Exit");
            System.out.print("Select an option: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    List<String> equipos = service.consultarEquipos();
                    System.out.println("Available equipment:");
                    for (String eq : equipos) System.out.println("  " + eq);
                    break;
                case "2":
                    System.out.print("Enter equipment code: ");
                    String codeConsult = scanner.nextLine().trim();
                    System.out.println("Result: " + service.consultarEquipo(codeConsult));
                    break;
                case "3":
                    System.out.print("Enter equipment code: ");
                    String codeReserve = scanner.nextLine().trim();
                    boolean reserved = service.reservarEquipo(codeReserve);
                    System.out.println(reserved ? "RESERVA_EXITOSA" : "ERROR: could not reserve (not exists or already reserved)");
                    break;
                case "4":
                    System.out.print("Enter equipment code: ");
                    String codeRelease = scanner.nextLine().trim();
                    boolean released = service.liberarEquipo(codeRelease);
                    System.out.println(released ? "LIBERACION_EXITOSA" : "ERROR: could not release (not exists or already available)");
                    break;
                case "5":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Use 1-5.");
            }
        }
    }
}
```

### Design Decisions

- **`List<String>` as return type:** `List` is an interface; RMI serializes the concrete implementation (typically `ArrayList`) on the server and deserializes it on the client.
- **`boolean` as success indicator:** Unlike previous exercises using error strings, `reservarEquipo()`/`liberarEquipo()` return `boolean`. This simplifies the contract but reduces error information — the client only knows "succeeded" or "failed", not why.
- **`String consultarEquipo()` with error messages:** Asymmetric with the boolean methods — this one returns a descriptive string including the error cause.
- **No `synchronized`:** Default RMI server is single-threaded. No race condition risk with a single client.
- **Interactive menu client:** Unlike Guide 4.2 (single query), this client implements a persistent menu loop for multiple operations per session.

### Design Reflection

- **RMI abstracts the network almost completely:** The client calls `service.reservarEquipo("LAP001")` as if it were a local object. No URL, no query string, no `OutputStream`.
- **`List<String>` travels transparently:** An `ArrayList` is serialized, transmitted, and deserialized without a single line of parsing code.
- **`boolean` as return is insufficient:** When `reservarEquipo()` returns `false`, the client does not know if the equipment does not exist or is already reserved. RMI would allow custom exceptions, but `boolean` was chosen for simplicity at the cost of error clarity.
- **The Java contract is the strongest so far:** Unlike TCP (implicit contract in text) and HTTP (implicit contract in routes), RMI has a formal compilable contract. If the interface changes, the compiler detects the contract violation immediately.

### Expected Output

```
Terminal 1:
EquipmentService RMI published on port 24000...

Terminal 2:
=== Laboratory Inventory System ===

Available operations:
  1. List all equipment
  2. Query equipment
  3. Reserve equipment
  4. Release equipment
  5. Exit
Select an option: 1
Available equipment:
  CEN001 - Centrifuge Eppendorf (Biology Lab) - AVAILABLE
  MIC001 - Microscope Olympus (Biology Lab) - AVAILABLE
  OSC001 - Oscilloscope Tektronix (Electronics Lab) - AVAILABLE
  LAP002 - Laptop HP Spectre (Computer Lab) - AVAILABLE
  LAP001 - Laptop Dell XPS 15 (Computer Lab) - AVAILABLE
```

### Reflection Questions

1. **What changed when moving from HTTP to RMI?**
   - The concept of "route" and "HTTP method" disappears. Now there are Java methods.
   - Manual parameter parsing (query string) disappears. Now there are method parameters.
   - `RemoteException` appears — every remote call can fail.
   - The client is no longer universal (browser/curl) but exclusively Java.
   - Serialization is automatic: Java objects travel over the network without text conversion.

2. **Where is the communication contract defined?**
   - In the `EquipmentService` interface that extends `Remote`.
   - Any change to the interface (new method, signature change) requires recompiling both client and server.
   - There is no external document — the Java interface IS the contract.

3. **What problems would this system have with a non-Java client?**
   - RMI uses Java Object Serialization, a private binary format of Java.
   - There is no way for a Python, JavaScript, or Go client to consume the service.
   - Port 24000 does not speak HTTP — it cannot be tested with curl or Postman.
   - Solution: gRPC (next section) defines the contract in a `.proto` file that generates code in any language.

---

## 9. Guide 5.2 - MovieService gRPC

**Package:** `src/edu/eci/arsw/guide5_2/` (Maven project)

### Problem Statement

Implement a movie query service using gRPC with Protocol Buffers. The contract is defined in a language-neutral `.proto` file, enabling clients in any language.

### Architecture

gRPC introduces a fundamental change: the contract is defined in a `.proto` file independent of language. A tool (`protoc`) generates both server and client code from this file. The server implements the generated interface and the client invokes it as if local, but the underlying communication uses HTTP/2 with binary serialization via Protocol Buffers.

`MovieGrpcServer` runs on port 50051 using `ServerBuilder.forPort()`. It registers `MovieServiceImpl` extending the generated `MovieServiceGrpc.MovieServiceImplBase`. `MovieGrpcClient` creates a `ManagedChannel` to `localhost:50051`, builds a `MovieServiceBlockingStub`, and invokes `stub.getMovie(request)`.

### Components

| File | Description |
|------|-------------|
| `pom.xml` | Maven configuration with gRPC dependencies and protobuf plugin |
| `movie.proto` | Service and message definitions |
| `MovieGrpcServer.java` | gRPC server with service implementation |
| `MovieGrpcClient.java` | gRPC client with blocking stub |

### Contract (.proto)

```protobuf
syntax = "proto3";
option java_multiple_files = true;
option java_package = "edu.eci.arsw.movie";
option java_outer_classname = "MovieProto";

service MovieService {
  rpc GetMovie (MovieRequest) returns (MovieResponse);
}

message MovieRequest {
  int32 id = 1;
}

message MovieResponse {
  int32 id = 1;
  string title = 2;
  string director = 3;
  int32 year = 4;
  bool found = 5;
}
```

### How to Build and Run

```bash
# Compile (generates code from .proto)
mvn clean compile -f src/edu/eci/arsw/guide5_2/pom.xml

# Terminal 1 — server
mvn exec:java -f src/edu/eci/arsw/guide5_2/pom.xml -Dexec.mainClass="edu.eci.arsw.movie.MovieGrpcServer"

# Terminal 2 — client
mvn exec:java -f src/edu/eci/arsw/guide5_2/pom.xml -Dexec.mainClass="edu.eci.arsw.movie.MovieGrpcClient"
```

### Execution Flow

1. `mvn clean compile` executes the protobuf plugin, generating `MovieServiceGrpc.java`, `MovieRequest.java`, `MovieResponse.java`, etc.
2. `MovieGrpcServer.main()` creates a `MovieServiceImpl` and registers it via `ServerBuilder.forPort(50051).addService().build().start()`.
3. The server waits for gRPC connections on port 50051.
4. `MovieGrpcClient.main()` creates a `ManagedChannel` to `localhost:50051` with `usePlaintext()`.
5. From the channel, it creates a `MovieServiceBlockingStub`.
6. The client builds a `MovieRequest` via `MovieRequest.newBuilder().setId(2).build()`, invokes `stub.getMovie(request)`.
7. gRPC serializes `MovieRequest` to binary protobuf, sends it as HTTP/2 frames, the server deserializes, executes `getMovie()`, serializes `MovieResponse`, and returns it.
8. The client deserializes the response and accesses fields via generated getters.

### Step-by-Step Implementation

#### MovieGrpcServer.java

```java
package edu.eci.arsw.movie;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.HashMap;
import java.util.Map;

public class MovieGrpcServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50051)
                .addService(new MovieServiceImpl())
                .build();
        server.start();
        System.out.println("Movie gRPC Server started on port 50051");
        server.awaitTermination();
    }

    static class MovieServiceImpl extends MovieServiceGrpc.MovieServiceImplBase {
        private Map<Integer, MovieResponse> movies = new HashMap<>();

        public MovieServiceImpl() {
            movies.put(1, MovieResponse.newBuilder()
                    .setId(1).setTitle("Interstellar")
                    .setDirector("Christopher Nolan").setYear(2014).setFound(true).build());
            movies.put(2, MovieResponse.newBuilder()
                    .setId(2).setTitle("Matrix")
                    .setDirector("Wachowski").setYear(1999).setFound(true).build());
            movies.put(3, MovieResponse.newBuilder()
                    .setId(3).setTitle("Inception")
                    .setDirector("Christopher Nolan").setYear(2010).setFound(true).build());
        }

        @Override
        public void getMovie(MovieRequest request,
                              StreamObserver<MovieResponse> responseObserver) {
            MovieResponse response = movies.get(request.getId());
            if (response == null) {
                response = MovieResponse.newBuilder()
                        .setId(request.getId()).setFound(false).build();
            }
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
```

#### MovieGrpcClient.java

```java
package edu.eci.arsw.movie;

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
            System.out.println("Movie: " + response.getTitle()
                    + " - " + response.getDirector()
                    + " - " + response.getYear());
        } else {
            System.out.println("Movie not found");
        }
        channel.shutdown();
    }
}
```

### Design Decisions

- **`.proto` as universal contract:** The `.proto` file is the equivalent of the RMI `Remote` interface, but language-independent.
- **Protocol Buffers:** Binary serialization format — more compact and faster than Java Object Serialization and JSON.
- **Code generation:** The `protobuf-maven-plugin` invokes `protoc` during `mvn compile`. Classes are regenerated automatically when `.proto` changes.
- **`bool found` instead of `null`:** Protobuf has no `null` concept. A boolean field indicates whether the movie was found.
- **`StreamObserver` callback:** gRPC's mechanism for sending responses. `onNext()` sends the response, `onCompleted()` signals completion.
- **BlockingStub:** The simplest stub type. The call blocks until the response is received.

### Design Reflection

- **The `.proto` IS the contract:** Anyone reading `movie.proto` knows exactly what services, methods, and types exist, regardless of language.
- **Multi-language clients:** The same gRPC server can be consumed from Python, JavaScript, Go, etc. Impossible with RMI.
- **Code generation eliminates manual errors:** In RMI, the developer wrote the interface and implementation by hand. With gRPC, only the `.proto` changes, and code is regenerated automatically.
- **Protobuf is more efficient than Java Serialization:** Binary, compact, schema-driven. Less network overhead, faster serialization/deserialization.
- **HTTP/2 as transport:** Multiplexing (multiple calls over one connection), header compression, bidirectional streaming.
- **No `RemoteException`:** gRPC uses `StatusRuntimeException` with standardized error codes (NOT_FOUND, INTERNAL, UNAVAILABLE), more portable across languages.

### Comparison: RMI vs gRPC

| Aspect | Guide 4.2 (RMI) | Guide 5.2 (gRPC) |
|--------|-----------------|-------------------|
| Service definition | Java interface `extends Remote` | `.proto` file |
| Messages | Arbitrary Java classes (Serializable) | Protobuf messages with schema |
| Serialization | Java Object Serialization (binary, heavy) | Protocol Buffers (binary, compact) |
| Transport | RMI protocol over TCP | HTTP/2 |
| Client | Java only | Java, Python, Go, JS, C#, etc. |
| Code generation | No (interface written by hand) | Yes (`protoc` generates stubs and servers) |
| Null handling | Java `null` | `bool found` or `oneof` |
| Builder | No (constructor/setters) | Yes (generated Builder pattern) |
| Port | 23000 (Registry) + anonymous (object) | 50051 (single port for everything) |
| Tooling | `rmiregistry`, `javac` | `protoc`, Maven plugin |

### Expected Output

```
Terminal 1:
Movie gRPC Server started on port 50051

Terminal 2 (existing movie):
Enter movie ID (1-3): 1
Movie: Interstellar - Christopher Nolan - 2014

Terminal 2 (non-existent ID):
Enter movie ID (1-3): 5
Movie not found
```

---

## 10. Exercise 5.3 - University Wellness gRPC

**Package:** `src/edu/eci/arsw/excercise5_3/` (Maven project)

### Problem Statement

Implement a university wellness appointment management system using gRPC. Unlike the movie domain (read-only), this system requires three operations: requesting an appointment (creation), cancelling an appointment (state modification), and querying a student's appointments (filtered listing). All data in memory, with mutable state on the server.

### Architecture

The system follows the same gRPC pattern as Guide 5.2 but with three RPCs instead of one. `WellnessGrpcServer` runs on port 50061 and registers an `AppointmentServiceImpl` extending `AppointmentServiceGrpc.AppointmentServiceImplBase`. The client is interactive with a menu (options 1-4) and uses an `AppointmentServiceBlockingStub` to invoke remote methods.

The `.proto` defines three main entities (`Student`, `Appointment`, `ServiceType`, `Status`) and five request/response messages, demonstrating protobuf's richness for modeling complex domains.

### Components

| File | Description |
|------|-------------|
| `pom.xml` | Maven configuration (same as guide5_2, different artifactId) |
| `appointment.proto` | Service, messages, and enumerations |
| `WellnessGrpcServer.java` | Server with `AppointmentServiceImpl` |
| `WellnessGrpcClient.java` | Interactive client with menu |

### Contract (.proto)

```protobuf
syntax = "proto3";
option java_multiple_files = true;
option java_package = "edu.eci.arsw.wellness";
option java_outer_classname = "AppointmentProto";

enum ServiceType {
  MEDICINE = 0;
  PSYCHOLOGY = 1;
  DENTISTRY = 2;
}

enum Status {
  REQUESTED = 0;
  CANCELLED = 1;
  ATTENDED = 2;
}

message Student {
  string id = 1;
  string name = 2;
  string institutionalEmail = 3;
}

message Appointment {
  string id = 1;
  string studentId = 2;
  ServiceType serviceType = 3;
  string date = 4;
  Status status = 5;
}

message AppointmentRequest {
  string studentId = 1;
  ServiceType serviceType = 2;
  string date = 3;
}

message AppointmentResponse {
  bool success = 1;
  string message = 2;
  Appointment appointment = 3;
}

message CancelRequest {
  string appointmentId = 1;
}

message CancelResponse {
  bool success = 1;
  string message = 2;
}

message StudentRequest {
  string studentId = 1;
}

message AppointmentList {
  repeated Appointment appointments = 1;
}

service AppointmentService {
  rpc RequestAppointment (AppointmentRequest) returns (AppointmentResponse);
  rpc CancelAppointment (CancelRequest) returns (CancelResponse);
  rpc GetAppointments (StudentRequest) returns (AppointmentList);
}
```

### How to Build and Run

```bash
# Compile
mvn clean compile -f src/edu/eci/arsw/excercise5_3/pom.xml

# Terminal 1 — server
mvn exec:java -f src/edu/eci/arsw/excercise5_3/pom.xml -Dexec.mainClass="edu.eci.arsw.wellness.WellnessGrpcServer"

# Terminal 2 — client
mvn exec:java -f src/edu/eci/arsw/excercise5_3/pom.xml -Dexec.mainClass="edu.eci.arsw.wellness.WellnessGrpcClient"
```

### Execution Flow

1. `mvn clean compile` generates classes from `appointment.proto`.
2. `WellnessGrpcServer` starts on port 50061 with `AppointmentServiceImpl`.
3. Client connects via `ManagedChannel` to `localhost:50061` and creates a blocking stub.
4. Menu loop: option 1 calls `requestAppointment()`, option 2 calls `cancelAppointment()`, option 3 calls `getAppointments()`.
5. `requestAppointment()` generates a UUID-based ID, creates an `Appointment` with `Status.REQUESTED`, stores it, and returns the full appointment details.
6. `cancelAppointment()` looks up the appointment by ID, validates existence and current status, updates to `Status.CANCELLED` via `toBuilder()`, and stores the updated version.
7. `getAppointments()` filters all stored appointments by `studentId` and returns matching entries.

### Step-by-Step Implementation

#### WellnessGrpcServer.java (AppointmentServiceImpl)

```java
package edu.eci.arsw.wellness;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WellnessGrpcServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50061)
                .addService(new AppointmentServiceImpl())
                .build();
        server.start();
        System.out.println("Wellness gRPC Server started on port 50061");
        server.awaitTermination();
    }

    static class AppointmentServiceImpl extends AppointmentServiceGrpc.AppointmentServiceImplBase {
        private Map<String, Appointment> appointments = new HashMap<>();

        @Override
        public void requestAppointment(AppointmentRequest request,
                                        StreamObserver<AppointmentResponse> responseObserver) {
            String appointmentId = UUID.randomUUID().toString().substring(0, 8);
            Appointment appointment = Appointment.newBuilder()
                    .setId(appointmentId)
                    .setStudentId(request.getStudentId())
                    .setServiceType(request.getServiceType())
                    .setDate(request.getDate())
                    .setStatus(Status.REQUESTED)
                    .build();
            appointments.put(appointmentId, appointment);

            AppointmentResponse response = AppointmentResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Appointment requested successfully")
                    .setAppointment(appointment)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void cancelAppointment(CancelRequest request,
                                        StreamObserver<CancelResponse> responseObserver) {
            Appointment existing = appointments.get(request.getAppointmentId());
            CancelResponse response;
            if (existing == null) {
                response = CancelResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("ERROR: appointment not found")
                        .build();
            } else if (existing.getStatus() == Status.CANCELLED) {
                response = CancelResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("ERROR: appointment already cancelled")
                        .build();
            } else {
                Appointment updated = existing.toBuilder().setStatus(Status.CANCELLED).build();
                appointments.put(request.getAppointmentId(), updated);
                response = CancelResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Appointment cancelled successfully")
                        .build();
            }
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void getAppointments(StudentRequest request,
                                      StreamObserver<AppointmentList> responseObserver) {
            AppointmentList.Builder listBuilder = AppointmentList.newBuilder();
            for (Appointment a : appointments.values()) {
                if (a.getStudentId().equals(request.getStudentId())) {
                    listBuilder.addAppointments(a);
                }
            }
            responseObserver.onNext(listBuilder.build());
            responseObserver.onCompleted();
        }
    }
}
```

#### WellnessGrpcClient.java (interactive menu)

```java
package edu.eci.arsw.wellness;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Scanner;

public class WellnessGrpcClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50061)
                .usePlaintext()
                .build();

        AppointmentServiceGrpc.AppointmentServiceBlockingStub stub =
                AppointmentServiceGrpc.newBlockingStub(channel);

        Scanner scanner = new Scanner(System.in);
        System.out.println("=== University Wellness System ===");

        while (true) {
            System.out.println("\nAvailable operations:");
            System.out.println("  1. Request appointment");
            System.out.println("  2. Cancel appointment");
            System.out.println("  3. Query student appointments");
            System.out.println("  4. Exit");
            System.out.print("Select an option: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    System.out.print("Student ID: ");
                    String studentId = scanner.nextLine().trim();
                    System.out.print("Service type (MEDICINE, PSYCHOLOGY, DENTISTRY): ");
                    String serviceTypeStr = scanner.nextLine().trim().toUpperCase();
                    ServiceType serviceType;
                    try {
                        serviceType = ServiceType.valueOf(serviceTypeStr);
                    } catch (IllegalArgumentException e) {
                        System.out.println("ERROR: invalid service type");
                        break;
                    }
                    System.out.print("Date (YYYY-MM-DD): ");
                    String date = scanner.nextLine().trim();

                    AppointmentRequest request = AppointmentRequest.newBuilder()
                            .setStudentId(studentId)
                            .setServiceType(serviceType)
                            .setDate(date)
                            .build();
                    AppointmentResponse response = stub.requestAppointment(request);
                    if (response.getSuccess()) {
                        System.out.println(response.getMessage());
                        Appointment a = response.getAppointment();
                        System.out.println("  ID: " + a.getId()
                                + " | Date: " + a.getDate()
                                + " | Status: " + a.getStatus());
                    } else {
                        System.out.println(response.getMessage());
                    }
                    break;

                case "2":
                    System.out.print("Appointment ID to cancel: ");
                    String appointmentId = scanner.nextLine().trim();
                    CancelRequest cancelReq = CancelRequest.newBuilder()
                            .setAppointmentId(appointmentId)
                            .build();
                    CancelResponse cancelRes = stub.cancelAppointment(cancelReq);
                    System.out.println(cancelRes.getMessage());
                    break;

                case "3":
                    System.out.print("Student ID: ");
                    String consultStudentId = scanner.nextLine().trim();
                    StudentRequest studentReq = StudentRequest.newBuilder()
                            .setStudentId(consultStudentId)
                            .build();
                    AppointmentList list = stub.getAppointments(studentReq);
                    if (list.getAppointmentsCount() == 0) {
                        System.out.println("No appointments found for student " + consultStudentId);
                    } else {
                        System.out.println("Appointments for " + consultStudentId + ":");
                        for (Appointment a : list.getAppointmentsList()) {
                            System.out.println("  " + a.getId()
                                    + " | " + a.getServiceType()
                                    + " | " + a.getDate()
                                    + " | " + a.getStatus());
                        }
                    }
                    break;

                case "4":
                    System.out.println("Exiting...");
                    channel.shutdown();
                    return;

                default:
                    System.out.println("Invalid option. Use 1-4.");
            }
        }
    }
}
```

### Design Decisions

- **Three RPCs in one service:** Demonstrates that a `.proto` file can define multiple methods in a single service, each with its own request/response message pair.
- **Enumerations in protobuf:** `ServiceType` and `Status` are defined as enums, transmitted as integers (0, 1, 2), not strings.
- **`repeated` for lists:** `AppointmentList` uses `repeated Appointment appointments` which generates `List<Appointment>` in Java.
- **UUID for appointment IDs:** `UUID.randomUUID().toString().substring(0, 8)` generates unique 8-character IDs, avoiding collisions without a centralized counter.
- **`toBuilder()` pattern:** Protobuf messages are immutable. To "modify" an appointment's status, `existing.toBuilder().setStatus(Status.CANCELLED).build()` creates a new object with the change.
- **`AppointmentResponse` includes full `Appointment`:** The response contains both a `success` boolean and the complete `Appointment` object, allowing the client to display details without a second query.
- **Descriptive error messages in `CancelResponse`:** Unlike the `boolean` approach in Exercise 4.3, the cancel response includes a `string message` that distinguishes "not found" from "already cancelled".

### Design Reflection

- **Protobuf forces contract-first design:** Before writing a line of code, messages and services must be defined in the `.proto`. This is *design by contract*: the contract is the starting point, not an accidental result of implementation.
- **Typed enumerations vs strings:** In RMI, `consultarEquipo()` returned strings like `"AVAILABLE"`. In gRPC, appointment status is an `enum Status`. Strong typing eliminates typos and enables IDE autocompletion.
- **Immutable messages:** Protobuf messages are immutable. To "change" state, a new instance is created via `toBuilder()`. This encourages a functional programming style and avoids side effects.
- **`repeated` simplifies collections:** In RMI, `consultarEquipos()` returned `List<String>`. In gRPC, `GetAppointments` returns `AppointmentList` with `repeated Appointment appointments`. The container message is explicit and can evolve independently.

### Expected Output

```
Terminal 1:
Wellness gRPC Server started on port 50061

Terminal 2:
=== University Wellness System ===

--- Request appointment ---
Select an option: 1
Student ID: S123
Service type (MEDICINE, PSYCHOLOGY, DENTISTRY): MEDICINE
Date (YYYY-MM-DD): 2026-06-15
Appointment requested successfully
  ID: 62c333d4 | Date: 2026-06-15 | Status: REQUESTED

--- Query student appointments ---
Select an option: 3
Student ID: S123
Appointments for S123:
  62c333d4 | MEDICINE | 2026-06-15 | REQUESTED

--- Cancel appointment ---
Select an option: 2
Appointment ID to cancel: 62c333d4
Appointment cancelled successfully

--- Verify cancellation ---
Select an option: 3
Student ID: S123
Appointments for S123:
  62c333d4 | MEDICINE | 2026-06-15 | CANCELLED
```

### Reflection Questions

1. **Why is the `.proto` file considered a contract?**
   - Because it defines exactly what methods exist, what parameters they receive, and what they return.
   - Any client or server that complies with the `.proto` can communicate, regardless of language.
   - The `.proto` is the single source of truth — the implementation must comply with it, not the other way around.
   - Unlike RMI (where the contract is a Java interface that only Java developers can read), the `.proto` is a text file that any developer can understand.

2. **How easy would it be to create a client in another language?**
   - Extremely easy: just copy `appointment.proto` and run `protoc` with the desired language plugin.
   - For example, for a Python client: `pip install grpcio grpcio-tools`, then `python -m grpc_tools.protoc -I. --python_out=. --grpc_python_out=. appointment.proto`.
   - The generated code includes Python classes with the same methods and types as the Java client.
   - No need to reimplement serialization logic or the network protocol — gRPC handles it automatically.

3. **What are the differences between RMI and gRPC?**
   - RMI is Java-only; gRPC is multi-language.
   - RMI uses Java Object Serialization; gRPC uses Protocol Buffers (faster, more compact).
   - RMI defines the contract as a Java interface; gRPC defines it as a `.proto` file.
   - RMI uses a proprietary binary protocol over TCP; gRPC uses HTTP/2 (multiplexing, streaming).
   - RMI does not generate code; gRPC generates stubs and servers automatically from the `.proto`.
   - RMI has no standard error mechanism (used `null` or `RemoteException`); gRPC has standardized error codes (NOT_FOUND, INTERNAL, etc.).

---

## 11. Guide 6.2 - Movie Microservices

**Package:** `src/edu/eci/arsw/guide6_2/`

**Status:** Not yet implemented.

### Description

This section will decompose the movie system into 3 independent microservices, each in its own gRPC port. The goal is to demonstrate separation of concerns and independent deployability.

### Proposed Services

| Service | Responsibility | Port |
|---------|----------------|------|
| MovieService | Query movie information | 50051 |
| ReviewService | Query movie reviews | 50052 |
| RecommendationService | Suggest related movies | 50053 |

### How to Build and Run (when implemented)

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="edu.eci.arsw.guide6_2.movie.MovieServiceServer"              # Terminal 1
mvn exec:java -Dexec.mainClass="edu.eci.arsw.guide6_2.review.ReviewServiceServer"            # Terminal 2
mvn exec:java -Dexec.mainClass="edu.eci.arsw.guide6_2.recommendation.RecommendationServiceServer" # Terminal 3
mvn exec:java -Dexec.mainClass="edu.eci.arsw.guide6_2.MicroserviceClient"                    # Terminal 4
```

See [PLAN.md](PLAN.md#guia-62---microservicios-peliculas) for full details.

> **Note:** This section is a placeholder. The implementation is pending.

---

## 12. Exercise 6.3 - Wellness Microservices

**Package:** `src/edu/eci/arsw/excercise6_3/`

**Status:** Not yet implemented.

### Description

This section will decompose the university wellness system into 4 microservices, each with a cohesive responsibility, following the same pattern as Guide 6.2.

### Proposed Services

| Service | Responsibility | Port |
|---------|----------------|------|
| AppointmentService | Manage appointments and schedules | 50061 |
| MedicalService | Medical specialty information | 50062 |
| GymService | Gym session reservations | 50063 |
| RecreationService | Recreational resource loans | 50064 |

### How to Build and Run (when implemented)

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="edu.eci.arsw.excercise6_3.appointment.AppointmentServer"
mvn exec:java -Dexec.mainClass="edu.eci.arsw.excercise6_3.medical.MedicalServer"
mvn exec:java -Dexec.mainClass="edu.eci.arsw.excercise6_3.gym.GymServer"
mvn exec:java -Dexec.mainClass="edu.eci.arsw.excercise6_3.recreation.RecreationServer"
```

See [PLAN.md](PLAN.md#ejercicio-63---microservicios-bienestar) for full details.

> **Note:** This section is a placeholder. The implementation is pending.

---

## 13. Guide 7.2 - MovieGateway

**Package:** `src/edu/eci/arsw/guide7_2/`

**Status:** Not yet implemented.

### Description

An API Gateway that centralizes access to the movie microservices (MovieService, ReviewService, RecommendationService). The client only knows the Gateway, not the individual ports of each service.

The Gateway receives a unified request, internally queries the 3 services, and consolidates the response. This solves the client-to-services coupling problem that arises in Guide 6.2.

### How to Build and Run (when implemented)

```bash
javac -d bin src/edu/eci/arsw/guide7_2/*.java
java -cp bin edu.eci.arsw.guide7_2.MovieGateway
```

See [PLAN.md](PLAN.md#guia-72---moviegateway) for full details.

> **Note:** This section is a placeholder. The implementation is pending.

---

## 14. Exercise 7.3 - WellnessGateway

**Package:** `src/edu/eci/arsw/excercise7_3/`

**Status:** Not yet implemented.

### Description

Gateway to centralize access to the university wellness services: AppointmentService, MedicalService, GymService, RecreationService.

### Minimum Operations

- `requestAppointment(studentId, serviceType)`
- `getStudentWellnessSummary(studentId)`
- `reserveGymSession(studentId, timeSlot)`
- `reserveRecreationResource(studentId, resourceId)`

### How to Build and Run (when implemented)

```bash
javac -d bin src/edu/eci/arsw/excercise7_3/*.java
java -cp bin edu.eci.arsw.excercise7_3.WellnessGateway
```

See [PLAN.md](PLAN.md#ejercicio-73---wellnessgateway) for full details.

> **Note:** This section is a placeholder. The implementation is pending.

---

## 15. Exercise 8 - ECICIENCIA

**Package:** `src/edu/eci/arsw/excercise8/`

**Status:** Not yet implemented.

### Description

Integrative final exercise: design the architecture of a distributed platform for managing the ECICIENCIA event. Includes attendee registration, schedule consultation, workshop reservations, and capacity control.

### Proposed Services

| Service | Port | Responsibility |
|---------|------|----------------|
| AttendeeService | 8091 | Attendee registration |
| AgendaService | 8092 | Schedule and capacity consultation |
| WorkshopService | 8093 | Workshop reservations |
| Gateway | — | Unified entry point |

### How to Build and Run (when implemented)

```bash
javac -d bin src/edu/eci/arsw/excercise8/**/*.java
java -cp bin edu.eci.arsw.excercise8.attendee.AttendeeServer   # Terminal 1
java -cp bin edu.eci.arsw.excercise8.agenda.AgendaServer       # Terminal 2
java -cp bin edu.eci.arsw.excercise8.workshop.WorkshopServer   # Terminal 3
java -cp bin edu.eci.arsw.excercise8.gateway.EcicienciaGateway  # Terminal 4
```

See [PLAN.md](PLAN.md#ejercicio-8---eciciencia) for full details.

> **Note:** This section is a placeholder. The implementation is pending.

---

## Port Summary

| Component | Technology | Port |
|-----------|------------|------|
| MovieServer TCP | Java Sockets | 35000 |
| RoomServer TCP | Java Sockets | 36000 |
| MovieHttpServer | com.sun.net.httpserver | 8080 |
| RoomHttpServer | com.sun.net.httpserver | 8081 |
| MovieService RMI | Java RMI | 23000 |
| EquipmentService RMI | Java RMI | 24000 |
| MovieGrpcServer | gRPC | 50051 |
| WellnessGrpcServer | gRPC | 50061 |
| Movie Microservices (future) | gRPC | 50051-50053 |
| Wellness Microservices (future) | gRPC | 50061-50064 |
| ECICIENCIA services (future) | HTTP (JDK) | 8091-8093 |
