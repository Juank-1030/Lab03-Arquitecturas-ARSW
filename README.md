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
| Socket TCP | [Guide 2.2](#3-guide-22---movieserver-tcp), [Exercise 2.3](#4-exercise-23---roomclient-tcp) | Bidirectional communication channel between two processes over a network |
| ServerSocket | [Guide 2.2](#3-guide-22---movieserver-tcp), [Exercise 2.3](#4-exercise-23---roomclient-tcp) | JDK class that allows a server to listen for incoming connections on a port |
| Application Protocol | [Guide 2.2](#3-guide-22---movieserver-tcp) | Format convention (e.g. `MOVIE:id`) agreed upon by client and server above TCP |
| HTTP | [Guide 3.2](#5-guide-32---moviehttpserver), [Exercise 3.3](#6-exercise-33---roomhttpserver) | Standard application protocol with method, path, headers, and body |
| HttpServer | [Guide 3.2](#5-guide-32---moviehttpserver), [Exercise 3.3](#6-exercise-33---roomhttpserver) | JDK built-in HTTP server (`com.sun.net.httpserver`) |
| REST-like API | [Exercise 3.3](#6-exercise-33---roomhttpserver) | Lightweight HTTP-based API using path segments for resource identifiers |
| HTTP Status Codes | [Exercise 3.3](#6-exercise-33---roomhttpserver) | Standard codes (200 OK, 201 Created, 404 Not Found, 500 Server Error) indicating response outcome |
| RMI | [Guide 4.2](#7-guide-42---movieservice-rmi), [Exercise 4.3](#8-exercise-43---equipmentservice-rmi) | Remote Method Invocation — invoke methods on objects in remote JVMs |
| Remote Interface | [Guide 4.2](#7-guide-42---movieservice-rmi) | Java interface extending `java.rmi.Remote` that defines the remote contract |
| RMI Registry | [Guide 4.2](#7-guide-42---movieservice-rmi), [Exercise 4.3](#8-exercise-43---equipmentservice-rmi) | Naming service that associates logical names with remote object references |
| UnicastRemoteObject | [Guide 4.2](#7-guide-42---movieservice-rmi), [Exercise 4.3](#8-exercise-43---equipmentservice-rmi) | JDK class that exports a remote object and creates a TCP listener for incoming RMI calls |
| gRPC | [Guide 5.2](#9-guide-52---movieservice-grpc), [Exercise 5.3](#10-exercise-53---wellnessservice-grpc) | Modern RPC framework using Protocol Buffers over HTTP/2 |
| .proto File | [Guide 5.2](#9-guide-52---movieservice-grpc), [Exercise 5.3](#10-exercise-53---wellnessservice-grpc) | Language-neutral contract file defining services and message types |
| Protocol Buffers | [Guide 5.2](#9-guide-52---movieservice-grpc), [Exercise 5.3](#10-exercise-53---wellnessservice-grpc) | Binary serialization format with schema-driven, strongly-typed messages |
| StreamObserver | [Exercise 5.3](#10-exercise-53---wellnessservice-grpc) | gRPC async callback interface for receiving responses on the server side |
| BlockingStub | [Exercise 5.3](#10-exercise-53---wellnessservice-grpc) | gRPC synchronous stub that blocks the calling thread until the response arrives |
| Protobuf Enum | [Exercise 5.3](#10-exercise-53---wellnessservice-grpc) | Enum type defined in `.proto` files (e.g. `ServiceType`, `Status`) for constrained string values |
| Microservices | [Guide 6.2](#11-guide-62---movie-microservices), [Exercise 6.3](#12-exercise-63---wellness-microservices) | Architectural style dividing a system into small, autonomous, cohesive services |
| Client-side Discovery | [Guide 6.2](#11-guide-62---movie-microservices), [Exercise 6.3](#12-exercise-63---wellness-microservices) | Pattern where the client knows the address of each service and connects directly |
| Unique Message Names | [Exercise 6.3](#12-exercise-63---wellness-microservices) | Avoiding protobuf global namespace collisions by using distinct message names across `.proto` files in the same module (e.g. `MedicalEmpty` / `RecreationEmpty`) |
| API Gateway | [Guide 7.2](#13-guide-72---moviegateway), [Exercise 7.3](#14-exercise-73---wellnessgateway), [Exercise 8](#15-exercise-8---eciciencia) | Single entry point that routes client requests to internal services |
| Server-Side Discovery | [Guide 7.2](#13-guide-72---moviegateway), [Exercise 7.3](#14-exercise-73---wellnessgateway) | Pattern where the gateway knows service locations and the client only knows the gateway |
| Protocol Adapter | [Guide 7.2](#13-guide-72---moviegateway), [Exercise 7.3](#14-exercise-73---wellnessgateway) | Gateway that translates between client protocol (HTTP) and backend protocol (gRPC) |

---

## 3. Guide 2.2 - MovieServer TCP

**Package:** `src/edu/eci/arsw/guide2_2/`

**Status:** :ballot_box_with_check: Implemented.

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
| Non-existent ID | `ERROR: pelicula no encontrada` |
| Invalid format | `ERROR: formato invalido. Use MOVIE:id` |

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
        System.out.println("MovieServer TCP escuchando en puerto 35000...");

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
            return "ERROR: formato invalido. Use MOVIE:id";
        try {
            int id = Integer.parseInt(request.split(":")[1]);
            Movie movie = repository.findById(id);
            if (movie == null) return "ERROR: pelicula no encontrada";
            return movie.toText();
        } catch (Exception e) {
            return "ERROR: solicitud invalida";
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
        System.out.print("Ingrese el ID de la pelicula: ");
        String id = scanner.nextLine();

        Socket socket = new Socket("127.0.0.1", 35000);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        out.println("MOVIE:" + id);
        String response = in.readLine();
        System.out.println("Respuesta del servidor: " + response);

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
MovieServer TCP escuchando en puerto 35000...

Terminal 2 (sesion 1 — pelicula existente):
Ingrese el ID de la pelicula: 1
Respuesta del servidor: 1,Interstellar,Christopher Nolan,2014

Terminal 2 (sesion 2 — ID inexistente):
Ingrese el ID de la pelicula: 5
Respuesta del servidor: ERROR: pelicula no encontrada

Terminal 2 (sesion 3 — entrada invalida):
Ingrese el ID de la pelicula: abc
Respuesta del servidor: ERROR: solicitud invalida
```

---

## 4. Exercise 2.3 - Room Management TCP

**Package:** `src/edu/eci/arsw/excercise2_3/`

**Status:** :ballot_box_with_check: Implemented.

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
RoomServer TCP escuchando en puerto 36000...

Terminal 2:
=== Sistema de Gestion de Salones ===
Ingrese comando (CONSULTAR_SALON, RESERVAR_SALON, LIBERAR_SALON): CONSULTAR_SALON
Ingrese codigo del salon (E301, E302, E303, E304): E303
Respuesta del servidor: SALON_DISPONIBLE

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

**Status:** :ballot_box_with_check: Implemented.

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
| Error response | `200 OK` with HTML body ("Pelicula no encontrada") |

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
9. The HTML response is built: success -> `"<html>...<h1>" + movie.toText() + "</h1>..."`, error -> `"<html>...<h1>Pelicula no encontrada</h1>..."`.
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
        System.out.println("MovieHttpServer escuchando en http://localhost:8080/movie?id=1");
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
                    response = "<html><body><h1>Pelicula no encontrada</h1></body></html>";
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
# <html><body><h1>Pelicula no encontrada</h1></body></html>

curl "http://localhost:8080/"
# 404 Not Found (default HttpServer response)
```

---

## 6. Exercise 3.3 - Room Management HTTP

**Package:** `src/edu/eci/arsw/excercise3_3/`

**Status:** :ballot_box_with_check: Implemented.

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
        System.out.println("RoomHttpServer escuchando en http://localhost:8081/rooms");
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
                response = "<html><body><h1>404 - Ruta no encontrada: "
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
            sb.append("<html><body><h1>Salones disponibles:</h1><ul>");
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
# <html><body><h1>Salones disponibles:</h1><ul><li>E301 - SALON_DISPONIBLE</li>...</ul></body></html>

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

**Status:** :ballot_box_with_check: Implemented.

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
        System.out.println("MovieService RMI publicado en puerto 23000...");
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
MovieService RMI publicado en puerto 23000...

Terminal 2:
Ingrese ID de pelicula (1-3): 1
Película recibida: 1 - Interstellar (2014) - Christopher Nolan

Ingrese ID de pelicula (1-3): 5
Película no encontrada
```

---

## 8. Exercise 4.3 - Lab Inventory RMI

**Package:** `src/edu/eci/arsw/excercise4_3/`

**Status:** :ballot_box_with_check: Implemented.

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
        if (eq == null) return "ERROR: equipo no encontrado";
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
        System.out.println("=== Sistema de Inventario de Laboratorios ===");

        while (true) {
            System.out.println("\nOperaciones disponibles:");
            System.out.println("  1. Listar todos los equipos");
            System.out.println("  2. Consultar un equipo");
            System.out.println("  3. Reservar un equipo");
            System.out.println("  4. Liberar un equipo");
            System.out.println("  5. Salir");
            System.out.print("Seleccione una opcion: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    List<String> equipos = service.consultarEquipos();
                    System.out.println("Equipos disponibles:");
                    for (String eq : equipos) System.out.println("  " + eq);
                    break;
                case "2":
                    System.out.print("Ingrese codigo del equipo: ");
                    String codeConsult = scanner.nextLine().trim();
                    System.out.println("Resultado: " + service.consultarEquipo(codeConsult));
                    break;
                case "3":
                    System.out.print("Ingrese codigo del equipo: ");
                    String codeReserve = scanner.nextLine().trim();
                    boolean reserved = service.reservarEquipo(codeReserve);
                    System.out.println(reserved ? "RESERVA_EXITOSA" : "ERROR: no se pudo reservar (no existe o ya reservado)");
                    break;
                case "4":
                    System.out.print("Ingrese codigo del equipo: ");
                    String codeRelease = scanner.nextLine().trim();
                    boolean released = service.liberarEquipo(codeRelease);
                    System.out.println(released ? "LIBERACION_EXITOSA" : "ERROR: no se pudo liberar (no existe o ya disponible)");
                    break;
                case "5":
                    System.out.println("Saliendo...");
                    return;
                default:
                    System.out.println("Opcion invalida. Use 1-5.");
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
EquipmentService RMI publicado en puerto 24000...

Terminal 2:
=== Sistema de Inventario de Laboratorios ===

Operaciones disponibles:
  1. Listar todos los equipos
  2. Consultar un equipo
  3. Reservar un equipo
  4. Liberar un equipo
  5. Salir
Seleccione una opcion: 1
Equipos disponibles:
  CEN001 - Centrifuga Eppendorf (Lab de Biologia) - DISPONIBLE
  MIC001 - Microscopio Olympus (Lab de Biologia) - DISPONIBLE
  OSC001 - Osciloscopio Tektronix (Lab de Electronica) - DISPONIBLE
  LAP002 - Laptop HP Spectre (Lab de Computacion) - DISPONIBLE
  LAP001 - Laptop Dell XPS 15 (Lab de Computacion) - DISPONIBLE
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

**Status:** :ballot_box_with_check: Implemented.

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
option java_package = "edu.eci.arsw.guide5_2";
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
mvn exec:java -f src/edu/eci/arsw/guide5_2/pom.xml -Dexec.mainClass="edu.eci.arsw.guide5_2.MovieGrpcServer"

# Terminal 2 — client
mvn exec:java -f src/edu/eci/arsw/guide5_2/pom.xml -Dexec.mainClass="edu.eci.arsw.guide5_2.MovieGrpcClient"
```

### Execution Flow

1. `mvn clean compile` executes the protobuf plugin, generating `MovieServiceGrpc.java`, `MovieRequest.java`, `MovieResponse.java`, etc.
2. `MovieGrpcServer.main()` creates a `MovieServiceImpl` and registers it via `ServerBuilder.forPort(50051).addService().build().start()`.
3. The server waits for gRPC connections on port 50051.
4. `MovieGrpcClient.main()` creates a `ManagedChannel` to `localhost:50051` with `usePlaintext()`.
5. From the channel, it creates a `MovieServiceBlockingStub`.
6. The client builds a `MovieRequest` via `MovieRequest.newBuilder().setId(1).build()`, invokes `stub.getMovie(request)`.
7. gRPC serializes `MovieRequest` to binary protobuf, sends it as HTTP/2 frames, the server deserializes, executes `getMovie()`, serializes `MovieResponse`, and returns it.
8. The client deserializes the response and accesses fields via generated getters.

### Step-by-Step Implementation

#### MovieGrpcServer.java

```java
package edu.eci.arsw.guide5_2;

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
        System.out.println("Movie gRPC Server iniciado en puerto 50051");
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
            System.out.println("Película: " + response.getTitle()
                    + " - " + response.getDirector()
                    + " - " + response.getYear());
        } else {
            System.out.println("Película no encontrada");
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
Movie gRPC Server iniciado en puerto 50051

Terminal 2:
Película: Interstellar - Christopher Nolan - 2014
```

---

## 10. Exercise 5.3 - University Wellness gRPC

**Package:** `src/edu/eci/arsw/excercise5_3/` (Maven project)

**Status:** :ballot_box_with_check: Implemented.

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
option java_package = "edu.eci.arsw.excercise5_3";
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
mvn exec:java -f src/edu/eci/arsw/excercise5_3/pom.xml -Dexec.mainClass="edu.eci.arsw.excercise5_3.WellnessGrpcServer"

# Terminal 2 — client
mvn exec:java -f src/edu/eci/arsw/excercise5_3/pom.xml -Dexec.mainClass="edu.eci.arsw.excercise5_3.WellnessGrpcClient"
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
package edu.eci.arsw.excercise5_3;

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
        System.out.println("Wellness gRPC Server iniciado en puerto 50061");
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
                    .setMessage("Cita solicitada exitosamente")
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
                        .setMessage("ERROR: cita no encontrada")
                        .build();
            } else if (existing.getStatus() == Status.CANCELLED) {
                response = CancelResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("ERROR: la cita ya fue cancelada")
                        .build();
            } else {
                Appointment updated = existing.toBuilder().setStatus(Status.CANCELLED).build();
                appointments.put(request.getAppointmentId(), updated);
                response = CancelResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Cita cancelada exitosamente")
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
package edu.eci.arsw.excercise5_3;

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
        System.out.println("=== Sistema de Bienestar Universitario ===");

        while (true) {
            System.out.println();
            System.out.println("Operaciones disponibles:");
            System.out.println("  1. Solicitar cita");
            System.out.println("  2. Cancelar cita");
            System.out.println("  3. Consultar citas de un estudiante");
            System.out.println("  4. Salir");
            System.out.print("Seleccione una opcion: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    System.out.print("ID del estudiante: ");
                    String studentId = scanner.nextLine().trim();
                    System.out.print("Tipo de servicio (MEDICINE, PSYCHOLOGY, DENTISTRY): ");
                    String serviceTypeStr = scanner.nextLine().trim().toUpperCase();
                    ServiceType serviceType;
                    try {
                        serviceType = ServiceType.valueOf(serviceTypeStr);
                    } catch (IllegalArgumentException e) {
                        System.out.println("ERROR: tipo de servicio invalido");
                        break;
                    }
                    System.out.print("Fecha (YYYY-MM-DD): ");
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
                        System.out.println("  ID cita: " + a.getId()
                                + " | Fecha: " + a.getDate()
                                + " | Estado: " + a.getStatus());
                    } else {
                        System.out.println(response.getMessage());
                    }
                    break;

                case "2":
                    System.out.print("ID de la cita a cancelar: ");
                    String appointmentId = scanner.nextLine().trim();
                    CancelRequest cancelReq = CancelRequest.newBuilder()
                            .setAppointmentId(appointmentId)
                            .build();
                    CancelResponse cancelRes = stub.cancelAppointment(cancelReq);
                    System.out.println(cancelRes.getMessage());
                    break;

                case "3":
                    System.out.print("ID del estudiante: ");
                    String consultStudentId = scanner.nextLine().trim();
                    StudentRequest studentReq = StudentRequest.newBuilder()
                            .setStudentId(consultStudentId)
                            .build();
                    AppointmentList list = stub.getAppointments(studentReq);
                    if (list.getAppointmentsCount() == 0) {
                        System.out.println("No se encontraron citas para el estudiante " + consultStudentId);
                    } else {
                        System.out.println("Citas de " + consultStudentId + ":");
                        for (Appointment a : list.getAppointmentsList()) {
                            System.out.println("  " + a.getId()
                                    + " | " + a.getServiceType()
                                    + " | " + a.getDate()
                                    + " | " + a.getStatus());
                        }
                    }
                    break;

                case "4":
                    System.out.println("Saliendo...");
                    channel.shutdown();
                    return;

                default:
                    System.out.println("Opcion invalida. Use 1-4.");
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
Wellness gRPC Server iniciado en puerto 50061

Terminal 2:
=== Sistema de Bienestar Universitario ===

--- Solicitar cita ---
Seleccione una opcion: 1
ID del estudiante: S123
Tipo de servicio (MEDICINE, PSYCHOLOGY, DENTISTRY): MEDICINE
Fecha (YYYY-MM-DD): 2026-06-15
Cita solicitada exitosamente
  ID cita: 62c333d4 | Fecha: 2026-06-15 | Estado: REQUESTED

--- Consultar citas ---
Seleccione una opcion: 3
ID del estudiante: S123
Citas de S123:
  62c333d4 | MEDICINE | 2026-06-15 | REQUESTED

--- Cancelar cita ---
Seleccione una opcion: 2
ID de la cita a cancelar: 62c333d4
Cita cancelada exitosamente

--- Verificar cancelacion ---
Seleccione una opcion: 3
ID del estudiante: S123
Citas de S123:
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

**Status:** :ballot_box_with_check: Implemented.

### Description

Decomposition of the movie system into 3 independent microservices, each in its own gRPC port. Unlike Guide 5.2 (single gRPC server with the entire domain), each service here is an independent process with its own `.proto`, its own `Server`, and its own port. The client must know and connect to all 3 ports individually.

### Services

| Service | Responsibility | Port |
|---------|----------------|------|
| MovieService | Query movie information | 50051 |
| ReviewService | Query movie reviews | 50052 |
| RecommendationService | Suggest related movies | 50053 |

### Architecture

Each microservice is an independent Maven process within the same module (same `pom.xml`). They share the same `groupId` but each has its own `.proto` with a unique `java_package`, generating classes in separate packages and avoiding name conflicts.

The client (`MicroserviceClient`) maintains 3 separate gRPC channels, one to each service, and an interactive menu to query each service individually or all at once.

### Components

| File | Description |
|------|-------------|
| `movie.proto` | `MovieService` with `GetMovie` RPC |
| `review.proto` | `ReviewService` with `GetReview` RPC |
| `recommendation.proto` | `RecommendationService` with `GetRecommendation` RPC |
| `MovieServiceServer.java` | gRPC server on port 50051 |
| `ReviewServiceServer.java` | gRPC server on port 50052 |
| `RecommendationServiceServer.java` | gRPC server on port 50053 |
| `MicroserviceClient.java` | Interactive client with 3 channels |

### gRPC Contracts (.proto)

Each `.proto` defines a single service with a single RPC, isolated in its own package:

| Proto | java_package | RPC |
|-------|-------------|-----|
| `movie.proto` | `edu.eci.arsw.guide6_2.movie` | `GetMovie(MovieRequest) → MovieResponse` |
| `review.proto` | `edu.eci.arsw.guide6_2.review` | `GetReview(ReviewRequest) → ReviewResponse` |
| `recommendation.proto` | `edu.eci.arsw.guide6_2.recommendation` | `GetRecommendation(RecommendationRequest) → RecommendationResponse` |

### Design Decisions

- **One `.proto` per service:** Each service has its own contract file with a unique `java_package`, preventing generated class name collisions (e.g., `MovieRequest` vs `ReviewRequest`).
- **Separate server processes:** Each microservice runs in its own JVM. This enables independent deployment, scaling, and failure isolation.
- **Client-side orchestration:** The `MicroserviceClient` acts as an aggregator (option 4 queries all 3 services). This is the Client-Side Discovery pattern. The downside is that the client knows all 3 ports.
- **Hardcoded data:** Each server pre-loads its own data in memory (movies, reviews, recommendations), following the same pattern as previous guides to keep focus on architecture.

### Implementation (representative files)

**movie.proto** — contract for MovieService:
```protobuf
syntax = "proto3";
option java_multiple_files = true;
option java_package = "edu.eci.arsw.guide6_2.movie";
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

**MovieServiceServer.java** — gRPC server for MovieService:
```java
package edu.eci.arsw.guide6_2.movie;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.HashMap;
import java.util.Map;

public class MovieServiceServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50051)
                .addService(new MovieServiceImpl()).build();
        server.start();
        System.out.println("MovieService Microservicio iniciado en puerto 50051");
        server.awaitTermination();
    }

    static class MovieServiceImpl extends MovieServiceGrpc.MovieServiceImplBase {
        private Map<Integer, MovieResponse> movies = new HashMap<>();

        public MovieServiceImpl() {
            movies.put(1, MovieResponse.newBuilder().setId(1)
                    .setTitle("Interstellar").setDirector("Christopher Nolan")
                    .setYear(2014).setFound(true).build());
            movies.put(2, MovieResponse.newBuilder().setId(2)
                    .setTitle("Inception").setDirector("Christopher Nolan")
                    .setYear(2010).setFound(true).build());
            movies.put(3, MovieResponse.newBuilder().setId(3)
                    .setTitle("The Matrix").setDirector("The Wachowskis")
                    .setYear(1999).setFound(true).build());
        }

        @Override
        public void getMovie(MovieRequest request,
                              StreamObserver<MovieResponse> responseObserver) {
            MovieResponse response = movies.getOrDefault(request.getId(),
                    MovieResponse.newBuilder().setId(request.getId()).setFound(false).build());
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
```

### How to Build and Run

```bash
# Compile from root
mvn clean compile

# Terminal 1 — MovieService
mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass="edu.eci.arsw.guide6_2.movie.MovieServiceServer"

# Terminal 2 — ReviewService
mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass="edu.eci.arsw.guide6_2.review.ReviewServiceServer"

# Terminal 3 — RecommendationService
mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass="edu.eci.arsw.guide6_2.recommendation.RecommendationServiceServer"

# Terminal 4 — Client
mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass="edu.eci.arsw.guide6_2.MicroserviceClient"
```

Servers must be started before the client; otherwise the client receives `StatusRuntimeException` with code `UNAVAILABLE`.

### Expected Output

```
Terminal 1:
MovieService Microservicio iniciado en puerto 50051

Terminal 2:
ReviewService Microservicio iniciado en puerto 50052

Terminal 3:
RecommendationService Microservicio iniciado en puerto 50053

Terminal 4:
=== Movie Microservices Client ===

--- Query movie ---
Select option: 1
Movie ID (1-3): 1
Movie: Interstellar - Christopher Nolan - 2014

--- Query review ---
Select option: 2
Movie ID (1-3): 1
Review by Roger Ebert: 5/5 - Una obra maestra de la ciencia ficcion

--- Query all ---
Select option: 4
Movie ID (1-3): 1

=== Full results for movie 1 ===
Movie: Interstellar - Christopher Nolan - 2014
Review: Roger Ebert - 5/5 - Una obra maestra de la ciencia ficcion
Recommendations: [2, 3]
```

### Key Architectural Lessons

1. **Separation of concerns:** Each microservice owns a single responsibility (movie data, reviews, recommendations). This follows the Single Responsibility Principle at the architectural level.
2. **Client knows too much:** The client must know that MovieService is on 50051, ReviewService on 50052, and RecommendationService on 50053. Any port change requires updating the client — this is client-to-service coupling.
3. **Operational complexity:** 4 processes instead of 2 (Guide 5.2). All 3 servers must be started before the client.
4. **Orchestration vs Choreography:** Option 4 implements orchestration (the client coordinates calls). An alternative would be choreography via event bus.

### Comparison: Single gRPC (Guide 5.2) vs Microservices (Guide 6.2)

| Aspect | Guide 5.2 (Single gRPC) | Guide 6.2 (Microservices) |
|--------|------------------------|---------------------------|
| Processes | 2 (server + client) | 4 (3 servers + client) |
| Ports | 1 (50051) | 3 (50051, 50052, 50053) |
| Contracts | 1 `.proto` with 1 RPC | 3 `.proto` with 1 RPC each |
| Client channels | 1 channel | 3 channels |
| Deployability | Single JAR | Independent processes |
| Fault isolation | Low (whole server fails) | High (per-service failure) |

### Reflection Questions

1. **Advantages of splitting the system into 3 microservices?**
   - Independent deployment: each service can be updated without affecting the others.
   - Selective scalability: if reviews have higher load, only ReviewService is scaled.
   - Fault isolation: if RecommendationService fails, MovieService and ReviewService keep working.
   - Independent teams: each service can be maintained by a different team.

2. **Disadvantages introduced by this architecture?**
   - Higher operational complexity: 3 servers to start and monitor.
   - Higher latency: the client makes 3 network calls instead of 1 (option 4).
   - The client must know the full topology (3 ports).
   - Error handling needed for each individual service.

3. **How does it compare to the monolithic approach of Guide 5.2?**
   - In Guide 5.2, the entire domain (movies, reviews, recommendations) was in a single `.proto` and a single server. The client made one call.
   - In Guide 6.2, each subdomain is an independent service. The client must make 3 calls.
   - Guide 5.2 is simpler to operate but less scalable. Guide 6.2 is more complex but more flexible.

---

## 12. Exercise 6.3 - Wellness Microservices

**Package:** `src/edu/eci/arsw/excercise6_3/`

**Status:** :ballot_box_with_check: Implemented.

### Description

Decomposition of the university wellness system into 4 microservices, each with a cohesive responsibility, following the same pattern as Guide 6.2 but applied to the wellness domain.

### Services

| Service | Responsibility | Port |
|---------|----------------|------|
| AppointmentService | Manage appointments and schedules | 50061 |
| MedicalService | Medical specialty information | 50062 |
| GymService | Gym session reservations | 50063 |
| RecreationService | Recreational resource loans | 50064 |

### Architecture

Each microservice is an independent Maven process within the same module. Each `.proto` defines its own contract with a unique `java_package` to avoid conflicts between identically-named messages (e.g., `MedicalEmpty` vs `RecreationEmpty`).

The client (`WellnessClient`) maintains 4 separate gRPC channels and an interactive menu with 18 operations covering full CRUD for each service.

> **Diagrams:** PlantUML sources at `docs/diagrams/exercise6_3_architecture.puml` and `docs/diagrams/exercise6_3_crud_flow.puml`.

### Components

| File | Description |
|------|-------------|
| `appointment.proto` | `AppointmentService` with 5 RPCs: create (request), cancel (soft delete), list, delete (hard), update date |
| `medical.proto` | `MedicalService` with 4 RPCs: `GetSpecialty`, `ListSpecialties`, `AddSpecialty`, `RemoveSpecialty` |
| `gym.proto` | `GymService` with 4 RPCs: `ReserveSession`, `GetSessions`, `CancelSession`, `GetAllSessions` |
| `recreation.proto` | `RecreationService` with 4 RPCs: `ReserveResource`, `ListResources`, `ReturnResource`, `AddResource` |
| `AppointmentServer.java` | gRPC server on port 50061 |
| `MedicalServer.java` | gRPC server on port 50062 |
| `GymServer.java` | gRPC server on port 50063 |
| `RecreationServer.java` | gRPC server on port 50064 |
| `WellnessClient.java` | Interactive client with 4 channels and 18 menu options |

### RPC Summary per Service

| Service | Create | Read | Update | Delete |
|---------|--------|------|--------|--------|
| AppointmentService | `RequestAppointment` | `GetAppointments` | `UpdateAppointmentDate` | `CancelAppointment` (soft), `DeleteAppointment` (hard) |
| MedicalService | `AddSpecialty` | `GetSpecialty`, `ListSpecialties` | — | `RemoveSpecialty` |
| GymService | `ReserveSession` | `GetSessions`, `GetAllSessions` | — | `CancelSession` (soft) |
| RecreationService | `AddResource` | `ListResources` | — | `ReturnResource` (toggle) |

### Design Decisions

- **Unique message names across `.proto` files:** Protobuf uses a global namespace for message types when multiple `.proto` files are compiled together. `MedicalEmpty` and `RecreationEmpty` are used instead of `Empty` in both to avoid "already defined" errors. Similarly, `GymEmpty` was added for `GetAllSessions`.
- **Separated domains:** Unlike Exercise 5.3 (one service with 3 RPCs), here each domain is an independent service with its own contract and in-memory data.
- **Availability control in RecreationService:** Uses a boolean `available` flag and `toBuilder().setAvailable(false)` to mark resources as reserved, demonstrating protobuf's immutability pattern. `ReturnResource` toggles `available` back to true.
- **Full CRUD capability:** Each service supports create and read operations; AppointmentService additionally supports update (reprogram date) and two-tier delete (soft cancel vs hard delete), while GymService and RecreationService support soft release patterns.

### Implementation (representative files)

**appointment.proto** — contract for AppointmentService:
```protobuf
syntax = "proto3";
option java_multiple_files = true;
option java_package = "edu.eci.arsw.excercise6_3.appointment";
option java_outer_classname = "AppointmentServiceProto";

enum ServiceType {
  MEDICINE = 0; PSYCHOLOGY = 1; DENTISTRY = 2;
}
enum Status {
  REQUESTED = 0; CANCELLED = 1; ATTENDED = 2;
}

service AppointmentService {
  rpc RequestAppointment (AppointmentRequest) returns (AppointmentResponse);
  rpc CancelAppointment (CancelRequest) returns (CancelResponse);
  rpc GetAppointments (StudentRequest) returns (AppointmentList);
  rpc DeleteAppointment (DeleteAppointmentRequest) returns (DeleteAppointmentResponse);
  rpc UpdateAppointmentDate (UpdateDateRequest) returns (UpdateDateResponse);
}
// ... messages omitted for brevity (see PLAN.md for full contracts)
```

**AppointmentServer.java** — gRPC server with full CRUD:
```java
package edu.eci.arsw.excercise6_3.appointment;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AppointmentServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50061)
                .addService(new AppointmentServiceImpl()).build();
        server.start();
        System.out.println("Appointment Microservicio iniciado en puerto 50061");
        server.awaitTermination();
    }

    static class AppointmentServiceImpl extends AppointmentServiceGrpc.AppointmentServiceImplBase {
        private Map<String, Appointment> appointments = new HashMap<>();

        @Override
        public void requestAppointment(AppointmentRequest request,
                                        StreamObserver<AppointmentResponse> responseObserver) {
            String id = UUID.randomUUID().toString().substring(0, 8);
            Appointment appointment = Appointment.newBuilder()
                    .setId(id).setStudentId(request.getStudentId())
                    .setServiceType(request.getServiceType())
                    .setDate(request.getDate()).setStatus(Status.REQUESTED).build();
            appointments.put(id, appointment);
            responseObserver.onNext(AppointmentResponse.newBuilder()
                    .setSuccess(true).setMessage("Cita solicitada exitosamente")
                    .setAppointment(appointment).build());
            responseObserver.onCompleted();
        }

        @Override
        public void cancelAppointment(CancelRequest request,
                                        StreamObserver<CancelResponse> responseObserver) {
            Appointment existing = appointments.get(request.getAppointmentId());
            if (existing == null || existing.getStatus() == Status.CANCELLED) {
                responseObserver.onNext(CancelResponse.newBuilder()
                        .setSuccess(false).setMessage("ERROR: cita no encontrada o ya cancelada").build());
            } else {
                appointments.put(request.getAppointmentId(),
                        existing.toBuilder().setStatus(Status.CANCELLED).build());
                responseObserver.onNext(CancelResponse.newBuilder()
                        .setSuccess(true).setMessage("Cita cancelada exitosamente").build());
            }
            responseObserver.onCompleted();
        }

        @Override
        public void deleteAppointment(DeleteAppointmentRequest request,
                                       StreamObserver<DeleteAppointmentResponse> responseObserver) {
            if (appointments.remove(request.getAppointmentId()) != null) {
                responseObserver.onNext(DeleteAppointmentResponse.newBuilder()
                        .setSuccess(true).setMessage("Cita eliminada permanentemente").build());
            } else {
                responseObserver.onNext(DeleteAppointmentResponse.newBuilder()
                        .setSuccess(false).setMessage("ERROR: cita no encontrada").build());
            }
            responseObserver.onCompleted();
        }

        @Override
        public void updateAppointmentDate(UpdateDateRequest request,
                                           StreamObserver<UpdateDateResponse> responseObserver) {
            Appointment existing = appointments.get(request.getAppointmentId());
            if (existing == null) {
                responseObserver.onNext(UpdateDateResponse.newBuilder()
                        .setSuccess(false).setMessage("ERROR: cita no encontrada").build());
            } else {
                Appointment updated = existing.toBuilder().setDate(request.getNewDate()).build();
                appointments.put(request.getAppointmentId(), updated);
                responseObserver.onNext(UpdateDateResponse.newBuilder()
                        .setSuccess(true).setMessage("Cita reprogramada exitosamente")
                        .setAppointment(updated).build());
            }
            responseObserver.onCompleted();
        }
        // getAppointments and other RPCs omitted for brevity
    }
}
```

### How to Build and Run

```bash
# Compile from root
mvn clean compile

# Terminal 1 — AppointmentService
mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass="edu.eci.arsw.excercise6_3.appointment.AppointmentServer"

# Terminal 2 — MedicalService
mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass="edu.eci.arsw.excercise6_3.medical.MedicalServer"

# Terminal 3 — GymService
mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass="edu.eci.arsw.excercise6_3.gym.GymServer"

# Terminal 4 — RecreationService
mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass="edu.eci.arsw.excercise6_3.recreation.RecreationServer"

# Terminal 5 — Client
mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass="edu.eci.arsw.excercise6_3.WellnessClient"
```

### Expected Output

```
Terminal 1:
Appointment Microservicio iniciado en puerto 50061

Terminal 2:
Medical Microservicio iniciado en puerto 50062

Terminal 3:
Gym Microservicio iniciado en puerto 50063

Terminal 4:
Recreation Microservicio iniciado en puerto 50064

Terminal 5:
=== Sistema de Bienestar (Microservicios) ===

--- Menu Principal ---
Citas:
  1. Solicitar cita
  2. Cancelar cita
  3. Listar citas por estudiante
  4. Eliminar cita (permanente)
  5. Reprogramar cita
Especialidades Medicas:
  6. Consultar especialidad
  7. Listar especialidades
  8. Agregar especialidad
  9. Eliminar especialidad
Gimnasio:
  10. Reservar sesion
  11. Consultar sesiones por estudiante
  12. Cancelar sesion
  13. Listar todas las sesiones
Recreacion:
  14. Reservar recurso
  15. Listar recursos
  16. Devolver recurso
  17. Agregar recurso
  0. Salir
Seleccione una opcion: 1
ID del estudiante: S123
Tipo de servicio (MEDICINE, PSYCHOLOGY, DENTISTRY): MEDICINE
Fecha (YYYY-MM-DD): 2026-06-15
Cita solicitada exitosamente
  ID cita: a1b2c3d4 | Fecha: 2026-06-15 | Estado: REQUESTED

--- Query medical specialty ---
Seleccione una opcion: 6
Codigo de especialidad: MED01
Especialidad: Medicina General
Descripcion: Atencion medica primaria y prevencion
Disponible: Si

--- List specialties ---
Seleccione una opcion: 7
Especialidades disponibles:
  MED01 - Medicina General (Disponible)
  MED02 - Psicologia (Disponible)
  MED03 - Odontologia (Disponible)

--- Add specialty ---
Seleccione una opcion: 8
Codigo de especialidad: MED04
Nombre: Fisioterapia
Descripcion: Rehabilitacion fisica y terapia deportiva
Especialidad agregada exitosamente
```

### Reflection Questions

1. **Why are unique message names needed across the 4 .proto files?**
   - Protobuf uses a global namespace for message types when multiple `.proto` files are compiled together. If two files define `Empty`, protoc throws an error. The solution is descriptive names like `MedicalEmpty` and `RecreationEmpty`.

2. **What advantage does separating AppointmentService from the other services provide?**
   - AppointmentService is the only service with mutable state (create/cancel appointments). The other services are primarily query + simple reservation. Separating them allows AppointmentService to be scaled independently if there is high appointment demand.

3. **How does this design compare to Exercise 5.3?**
   - In 5.3, the entire wellness domain was in a single server with 3 RPCs. In 6.3, there are 4 specialized servers. The advantage is deployment independence; the disadvantage is that the client needs 4 connections.

---

## 13. Guide 7.2 - MovieGateway

**Package:** `src/edu/eci/arsw/guide7_2/`

**Status:** :ballot_box_with_check: Implemented.

### Description

An API Gateway that centralizes access to the movie microservices (MovieService, ReviewService, RecommendationService). The client only knows the Gateway, not the individual ports of each service.

The Gateway receives a unified request, internally queries the 3 services, and consolidates the response. This solves the client-to-services coupling problem that arises in Guide 6.2.

### Architecture

`MovieGateway` runs an HTTP server (`com.sun.net.httpserver.HttpServer`) on port 8082. Internally, it creates 3 gRPC channels (one to each microservice) and acts as a gRPC client. The client only needs HTTP (browser, curl) and is completely unaware of the gRPC topology behind the Gateway.

| Endpoint | Method | Description | Delegates to |
|----------|--------|-------------|--------------|
| `/movie?id=X` | GET | Query movie info | MovieService (50051) |
| `/review?movieId=X` | GET | Query review | ReviewService (50052) |
| `/recommendation?movieId=X` | GET | Query recommendations | RecommendationService (50053) |
| `/consolidated?id=X` | GET | Query all 3 services at once | All 3 microservices |

### Components

| File | Description |
|------|-------------|
| `MovieGateway.java` | HTTP server (port 8082) + gRPC client for all 3 microservices |

### Design Decisions

- **Single process, single port:** Unlike Guide 6.2 (4 processes, 3 ports), the Gateway is a single process on a single port (8082). The client only knows `localhost:8082`.
- **Client-side Discovery → Server-side Routing:** Guide 6.2 required the client to know all 3 ports. The Gateway inverts this: it knows the 3 ports, and the client only knows the Gateway.
- **HTML responses:** Uses HTML (same as Guide 3.2) so the Gateway can be tested with any browser or curl.
- **Clean error handling:** Returns `400 Bad Request` for missing parameters and meaningful HTML error messages.

### Implementation

**MovieGateway.java** — HTTP server + gRPC client:
```java
package edu.eci.arsw.guide7_2;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import edu.eci.arsw.guide6_2.movie.*;
import edu.eci.arsw.guide6_2.review.*;
import edu.eci.arsw.guide6_2.recommendation.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class MovieGateway {
    private static final int GATEWAY_PORT = 8082;

    private final MovieServiceGrpc.MovieServiceBlockingStub movieStub;
    private final ReviewServiceGrpc.ReviewServiceBlockingStub reviewStub;
    private final RecommendationServiceGrpc.RecommendationServiceBlockingStub recStub;

    public MovieGateway() {
        ManagedChannel movieCh = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
        ManagedChannel reviewCh = ManagedChannelBuilder.forAddress("localhost", 50052).usePlaintext().build();
        ManagedChannel recCh = ManagedChannelBuilder.forAddress("localhost", 50053).usePlaintext().build();
        movieStub = MovieServiceGrpc.newBlockingStub(movieCh);
        reviewStub = ReviewServiceGrpc.newBlockingStub(reviewCh);
        recStub = RecommendationServiceGrpc.newBlockingStub(recCh);
    }

    public static void main(String[] args) throws Exception {
        MovieGateway gw = new MovieGateway();
        HttpServer server = HttpServer.create(new InetSocketAddress(GATEWAY_PORT), 0);
        server.createContext("/movie", gw::handleMovie);
        server.createContext("/review", gw::handleReview);
        server.createContext("/recommendation", gw::handleRecommendation);
        server.createContext("/consolidated", gw::handleConsolidated);
        server.setExecutor(null);
        server.start();
        System.out.println("MovieGateway HTTP iniciado en puerto " + GATEWAY_PORT);
    }

    private void handleMovie(HttpExchange ex) throws IOException {
        int id = extractId(ex.getRequestURI());
        if (id < 0) { send(ex, "<h1>ERROR: parametro invalido</h1>"); return; }
        MovieResponse m = movieStub.getMovie(MovieRequest.newBuilder().setId(id).build());
        if (m.getFound())
            send(ex, "<h1>" + m.getTitle() + "</h1><p>" + m.getDirector() + " (" + m.getYear() + ")</p>");
        else
            send(ex, "<h1>Pelicula no encontrada</h1>");
    }

    private void handleConsolidated(HttpExchange ex) throws IOException {
        int id = extractId(ex.getRequestURI());
        if (id < 0) { send(ex, "<h1>ERROR: parametro invalido</h1>"); return; }
        MovieResponse m = movieStub.getMovie(MovieRequest.newBuilder().setId(id).build());
        ReviewResponse r = reviewStub.getReview(ReviewRequest.newBuilder().setMovieId(id).build());
        RecommendationResponse rec = recStub.getRecommendation(
                RecommendationRequest.newBuilder().setMovieId(id).build());
        StringBuilder sb = new StringBuilder("<h1>Resultado consolidado para pelicula " + id + "</h1>");
        if (m.getFound()) sb.append("<h2>Pelicula</h2><p>").append(m.getTitle()).append("</p>");
        if (r.getFound()) sb.append("<h2>Resena</h2><p>").append(r.getReviewer()).append(": ")
                .append(r.getRating()).append("/5 - ").append(r.getComment()).append("</p>");
        if (rec.getFound()) { sb.append("<h2>Recomendaciones</h2><p>");
            for (int rid : rec.getRecommendedIdsList()) sb.append(rid).append(" "); sb.append("</p>"); }
        send(ex, sb.toString());
    }
    // review, recommendation handlers, extractId(), send() omitted for brevity
}
```

### How to Build and Run

```bash
# Step 1 — Install all artifacts (needed for gateway dependency resolution)
mvn install -DskipTests

# Terminal 1 — MovieService
mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide6_2.movie.MovieServiceServer

# Terminal 2 — ReviewService
mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide6_2.review.ReviewServiceServer

# Terminal 3 — RecommendationService
mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide6_2.recommendation.RecommendationServiceServer

# Terminal 4 — Gateway
mvn exec:java -f src/edu/eci/arsw/guide7_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide7_2.MovieGateway

# Test with curl
curl "http://localhost:8082/movie?id=1"
curl "http://localhost:8082/consolidated?id=1"
```

### Expected Output

```
Terminal 4:
MovieGateway HTTP iniciado en puerto 8082

curl "http://localhost:8082/movie?id=1"
# <html><body><h1>Interstellar</h1><p>Director: Christopher Nolan</p><p>Anio: 2014</p></body></html>

curl "http://localhost:8082/consolidated?id=1"
# <html><body><h1>Resultado consolidado para pelicula 1</h1>
#   <h2>Pelicula</h2><p>Interstellar - Christopher Nolan (2014)</p>
#   <h2>Resena</h2><p>Roger Ebert: 5/5 - Una obra maestra de la ciencia ficcion</p>
#   <h2>Recomendaciones</h2><p>2 3</p></body></html>
```

### Comparison: Microservices (Guide 6.2) vs Gateway (Guide 7.2)

| Aspect | Guide 6.2 (Client-Side Discovery) | Guide 7.2 (Server-Side Discovery) |
|--------|-----------------------------------|------------------------------------|
| Client protocol | gRPC (requires Java + stubs) | HTTP (browser, curl, any language) |
| Ports client knows | 3 (50051, 50052, 50053) | 1 (8082) |
| Processes to start | 4 (3 servers + client) | 4 (3 servers + Gateway) |
| Client complexity | High (4 channels, 4 stubs) | Low (single HTTP URL) |
| Coupling | Client coupled to topology | Client decoupled from topology |
| Routing | Client-side (client decides) | Server-side (Gateway decides) |
| Single point of failure | No (services are independent) | Yes (Gateway can become a bottleneck) |

### Key Architectural Lessons

1. **Protocol adaptation:** The Gateway acts as a protocol adapter — it receives HTTP and forwards gRPC. This allows non-gRPC clients to consume gRPC services.
2. **Topology hiding:** The client no longer needs to know about individual microservice ports. This is the fundamental benefit of the Gateway pattern: it inverts the discovery responsibility.
3. **Gateway as a simplification layer:** `/consolidated` is a single endpoint that internally calls 3 services. The Gateway provides a simplified API tailored to client needs, not a mirror of the internal architecture.
4. **Operational trade-off:** Adding the Gateway adds one more process to manage and one more network hop, but dramatically simplifies the client. For systems with many clients, this trade-off is almost always worth it.

### Reflection Questions

1. **What problem does the Gateway solve compared to Guide 6.2?**
   - In Guide 6.2, the client knew 3 ports (50051, 50052, 50053). If any port changed, every client had to be updated. The Gateway hides this topology: the client only knows port 8082. If a microservice moves, only the Gateway is updated.

2. **What is the trade-off of adding a Gateway?**
   - **Pro:** Client simplicity (one port, one protocol), centralized routing, ability to add cross-cutting concerns (logging, caching, rate-limiting).
   - **Con:** Single point of failure, extra network hop (latency), operational complexity (one more process to manage).

3. **How does this compare to the pattern used in Guide 6.2 (client-side discovery)?**
   - Guide 6.2 uses Client-Side Discovery: the client queries a service registry (or has hardcoded ports) and calls services directly. Guide 7.2 uses Server-Side Discovery: a Gateway is the single entry point and routes requests to the appropriate services.

---

## 14. Exercise 7.3 - WellnessGateway

**Package:** `src/edu/eci/arsw/excercise7_3/`

**Status:** :ballot_box_with_check: Implemented.

### Description

Gateway to centralize access to the university wellness services: AppointmentService, MedicalService, GymService, RecreationService. Follows the same pattern as Guide 7.2 but applied to the 4 wellness microservices.

### Architecture

`WellnessGateway` runs an HTTP server on port 8083 and internally creates 4 gRPC channels (one to each wellness microservice). The client uses HTTP (browser, curl) and is unaware of the gRPC topology.

| Endpoint | Method | Description | Delegates to |
|----------|--------|-------------|--------------|
| `/appointment?studentId=X&serviceType=Y&date=Z` | POST | Create appointment | AppointmentService (50061) |
| `/wellness-summary?studentId=X` | GET | Query all wellness data for a student | All 4 services |
| `/gym/reserve?studentId=X&timeSlot=Y` | POST | Reserve gym session | GymService (50063) |
| `/recreation/reserve?studentId=X&resourceId=Y` | POST | Reserve recreation resource | RecreationService (50064) |

### Components

| File | Description |
|------|-------------|
| `WellnessGateway.java` | HTTP server (port 8083) + gRPC client for all 4 microservices |

### Operations

| Operation | HTTP | Parameters | Gateway Action |
|-----------|------|------------|----------------|
| `requestAppointment` | `POST /appointment` | studentId, serviceType, date | Calls `appointmentStub.requestAppointment()` |
| `getStudentWellnessSummary` | `GET /wellness-summary` | studentId | Calls all 4 stubs, consolidates into one HTML page |
| `reserveGymSession` | `POST /gym/reserve` | studentId, timeSlot | Calls `gymStub.reserveSession()` |
| `reserveRecreationResource` | `POST /recreation/reserve` | studentId, resourceId | Calls `recreationStub.reserveResource()` |

### Design Decisions

- **Same pattern as Guide 7.2:** HTTP front-end, gRPC back-end, single process on a single port.
- **Consolidated endpoint:** `/wellness-summary` queries all 4 services and builds a single HTML page with sections for appointments, specialties, gym sessions, and recreation resources.
- **HTTP method semantics:** POST for state-changing operations (create appointment, reserve), GET for read-only queries (summary).
- **Error handling:** Returns 400 for missing parameters, 405 for wrong HTTP method, and meaningful HTML error messages.

### Implementation

**WellnessGateway.java** — HTTP server + gRPC client for 4 services:
```java
package edu.eci.arsw.excercise7_3;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import edu.eci.arsw.excercise6_3.appointment.*;
import edu.eci.arsw.excercise6_3.gym.*;
import edu.eci.arsw.excercise6_3.medical.*;
import edu.eci.arsw.excercise6_3.recreation.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class WellnessGateway {
    private static final int PORT = 8083;

    private final AppointmentServiceGrpc.AppointmentServiceBlockingStub aptStub;
    private final MedicalServiceGrpc.MedicalServiceBlockingStub medStub;
    private final GymServiceGrpc.GymServiceBlockingStub gymStub;
    private final RecreationServiceGrpc.RecreationServiceBlockingStub recStub;

    public WellnessGateway() {
        aptStub = AppointmentServiceGrpc.newBlockingStub(
                ManagedChannelBuilder.forAddress("localhost", 50061).usePlaintext().build());
        medStub = MedicalServiceGrpc.newBlockingStub(
                ManagedChannelBuilder.forAddress("localhost", 50062).usePlaintext().build());
        gymStub = GymServiceGrpc.newBlockingStub(
                ManagedChannelBuilder.forAddress("localhost", 50063).usePlaintext().build());
        recStub = RecreationServiceGrpc.newBlockingStub(
                ManagedChannelBuilder.forAddress("localhost", 50064).usePlaintext().build());
    }

    public static void main(String[] args) throws Exception {
        WellnessGateway gw = new WellnessGateway();
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/appointment", gw::handleAppointment);
        server.createContext("/wellness-summary", gw::handleSummary);
        server.createContext("/gym/reserve", gw::handleGym);
        server.createContext("/recreation/reserve", gw::handleRecreation);
        server.setExecutor(null);
        server.start();
        System.out.println("WellnessGateway HTTP iniciado en puerto " + PORT);
    }

    private void handleAppointment(HttpExchange ex) throws IOException {
        if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) { send(ex, 405, "Use POST"); return; }
        String sid = getParam(ex.getRequestURI().getQuery(), "studentId");
        String st = getParam(ex.getRequestURI().getQuery(), "serviceType");
        String dt = getParam(ex.getRequestURI().getQuery(), "date");
        if (sid == null || st == null || dt == null) { send(ex, 400, "Faltan parametros"); return; }
        AppointmentResponse r = aptStub.requestAppointment(AppointmentRequest.newBuilder()
                .setStudentId(sid).setServiceType(ServiceType.valueOf(st.toUpperCase())).setDate(dt).build());
        send(ex, 200, r.getSuccess() ? "Cita creada: " + r.getAppointment().getId() : r.getMessage());
    }

    private void handleSummary(HttpExchange ex) throws IOException {
        String sid = getParam(ex.getRequestURI().getQuery(), "studentId");
        if (sid == null) { send(ex, 400, "Falta studentId"); return; }
        StringBuilder sb = new StringBuilder("<h1>Resumen de " + sid + "</h1>");
        sb.append("<h2>Citas</h2>");
        for (Appointment a : aptStub.getAppointments(
                StudentRequest.newBuilder().setStudentId(sid).build()).getAppointmentsList())
            sb.append("<p>").append(a.getId()).append(" | ").append(a.getDate()).append("</p>");
        sb.append("<h2>Sesiones</h2>");
        for (GymSession s : gymStub.getSessions(
                StudentSessionsRequest.newBuilder().setStudentId(sid).build()).getSessionsList())
            sb.append("<p>").append(s.getId()).append(" | ").append(s.getTimeSlot()).append("</p>");
        send(ex, 200, sb.toString());
    }
    // handleGym, handleRecreation, getParam(), send() omitted for brevity
}
```

### How to Build and Run

```bash
# Step 1 — Install all artifacts (needed for gateway dependency resolution)
mvn install -DskipTests

# Terminal 1 — AppointmentService
mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.appointment.AppointmentServer

# Terminal 2 — MedicalService
mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.medical.MedicalServer

# Terminal 3 — GymService
mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.gym.GymServer

# Terminal 4 — RecreationService
mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.recreation.RecreationServer

# Terminal 5 — Gateway
mvn exec:java -f src/edu/eci/arsw/excercise7_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise7_3.WellnessGateway

# Test with curl
curl -X POST "http://localhost:8083/appointment?studentId=S123&serviceType=MEDICINE&date=2026-06-15"
curl "http://localhost:8083/wellness-summary?studentId=S123"
```

### Expected Output

```
Terminal 5:
WellnessGateway HTTP iniciado en puerto 8083

curl -X POST "http://localhost:8083/appointment?studentId=S123&serviceType=MEDICINE&date=2026-06-15"
# <html><body><h1>Cita solicitada exitosamente</h1>
#   <p>ID cita: a1b2c3d4 | Fecha: 2026-06-15 | Estado: REQUESTED</p></body></html>

curl "http://localhost:8083/wellness-summary?studentId=S123"
# <html><body><h1>Resumen de Bienestar para S123</h1>
#   <h2>Citas</h2><p>a1b2c3d4 | MEDICINE | 2026-06-15 | REQUESTED</p>
#   <h2>Especialidades Medicas</h2><p>MED01 - Medicina General (Disponible)</p>...
#   <h2>Sesiones de Gimnasio</h2><p>No hay sesiones registradas.</p>
#   <h2>Recursos Recreativos</h2><p>REC01 - Balones de futbol (Disponible)</p>...
# </body></html>
```

### Comparison: Exercise 6.3 vs Exercise 7.3

| Aspect | Exercise 6.3 (Client-Side Discovery) | Exercise 7.3 (Server-Side Discovery) |
|--------|---------------------------------------|--------------------------------------|
| Client knowledge | 4 ports, 4 gRPC stubs | 1 HTTP URL (port 8083) |
| Client type | Java-only (gRPC) | Any HTTP client |
| Startup | 5 terminals (4 servers + client) | 5 terminals (4 servers + Gateway) |
| API granularity | 18 menu options | 4 well-defined HTTP endpoints |
| Error handling | Per-service console messages | HTTP status codes (400, 405, 500) |

### Key Architectural Lessons

1. **Gateway as domain facade:** The WellnessGateway exposes only 4 endpoints instead of the full 18 gRPC operations. This facade pattern simplifies the client API by exposing only the operations that external consumers need.
2. **Sequential vs parallel aggregation:** The `/wellness-summary` endpoint calls 4 services sequentially. This is simple but slow. A performance optimization would be parallel calls using `CompletableFuture` or a thread pool.
3. **HTTP status codes:** Unlike the gRPC client (which uses exceptions), the Gateway returns proper HTTP status codes (400 for bad request, 405 for wrong method, 500 for server errors with meaningful HTML messages).
4. **Unified error experience:** All errors are returned as HTML, providing a consistent user experience regardless of which backend service failed.

### Reflection Questions

1. **How does the Gateway improve the client experience compared to Exercise 6.3?**
   - In Exercise 6.3, the client needed 4 gRPC channels, 4 stubs, a complex Java client, and knowledge of 4 ports. With the Gateway, the client only needs HTTP and one URL. Any language or tool (curl, browser, Postman) can interact with the wellness system.

2. **What are the scalability implications of the wellness-summary endpoint?**
   - `/wellness-summary` calls all 4 services sequentially. If one service is slow, the entire response is delayed. This could be improved with parallel calls (one thread per service) but adds complexity. For low-load scenarios, sequential is simpler and sufficient.

3. **What happens if one of the backend microservices is down?**
   - The Gateway will throw a `StatusRuntimeException` from gRPC. In the current implementation, this propagates as a 500 error. A production Gateway would add retry logic, circuit breakers, and graceful degradation (returning partial results).

---

## 15. Exercise 8 - ECICIENCIA

**Package:** `Maven module at src/edu/eci/arsw/excercise8/`

**Status:** Implemented. Running code available.

### Description

Integrative final exercise: fully implemented distributed platform for managing the ECICIENCIA event. Three gRPC microservices (AttendeeService, AgendaService, WorkshopService) and an API Gateway exposing HTTP endpoints.

### Architecture

```
Client / Browser
      |
      v
ECICIENCIA Gateway (:8090) — HTTP (JDK)
      |
      +--- gRPC → AttendeeService (:8091)
      +--- gRPC → AgendaService    (:8092)
      +--- gRPC → WorkshopService  (:8093)
```

### Components

**AttendeeServer.java** — gRPC server on port 8091. Manages attendee registration and lookup. Preloaded with 2 attendees (Carlos Perez, Maria Gomez). Exposes 3 RPCs: `RegisterAttendee`, `GetAttendee`, `ListAttendees`.

```java
static class AttendeeServiceImpl extends AttendeeServiceGrpc.AttendeeServiceImplBase {
    private final Map<Integer, AttendeeResponse> attendees = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public AttendeeServiceImpl() {
        attendees.put(1, AttendeeResponse.newBuilder()
                .setAttendeeId(1).setName("Carlos Perez")
                .setEmail("carlos@mail.com").setFound(true).build());
        attendees.put(2, AttendeeResponse.newBuilder()
                .setAttendeeId(2).setName("Maria Gomez")
                .setEmail("maria@mail.com").setFound(true).build());
        idCounter.set(3);
    }

    @Override
    public void registerAttendee(RegisterRequest request,
            StreamObserver<AttendeeResponse> responseObserver) {
        int id = idCounter.getAndIncrement();
        AttendeeResponse response = AttendeeResponse.newBuilder()
                .setAttendeeId(id).setName(request.getName())
                .setEmail(request.getEmail()).setFound(true).build();
        attendees.put(id, response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    // getAttendee, listAttendees follow the same pattern
}
```

**AgendaServer.java** — gRPC server on port 8092. Manages the event schedule. Preloaded with 4 activities (ML talk, Arduino workshop, Cybersecurity talk, Robotics workshop). Exposes 3 RPCs: `GetActivitiesByTimeSlot`, `GetActivityDetails`, `CheckCapacity`.

```java
static class AgendaServiceImpl extends AgendaServiceGrpc.AgendaServiceImplBase {
    private final Map<Integer, ActivityResponse> activities = new HashMap<>();
    // Preloaded activities with speaker, location, capacity
    @Override
    public void getActivitiesByTimeSlot(TimeSlotRequest request,
            StreamObserver<ActivityList> responseObserver) {
        ActivityList.Builder builder = ActivityList.newBuilder();
        for (ActivityResponse a : activities.values()) {
            if (a.getStartTime().compareTo(request.getStartTime()) >= 0
                    && a.getEndTime().compareTo(request.getEndTime()) <= 0) {
                builder.addActivities(a);
            }
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
    // getActivityDetails, checkCapacity follow the same pattern
}
```

**WorkshopServer.java** — gRPC server on port 8093. Manages reservations, waiting lists, and capacity. Exposes 4 RPCs: `ReserveSpot`, `CancelReservation`, `GetAttendeeReservations`, `GetAvailableSpots`. Implements waiting list logic: if an activity is full, the reservation enters WAITING status with a queue position.

```java
static class WorkshopServiceImpl extends WorkshopServiceGrpc.WorkshopServiceImplBase {
    private final Map<Integer, ReserveResponse> reservations = new HashMap<>();
    private final Map<Integer, Integer> activityCount = new HashMap<>();
    private final Map<Integer, Integer> activityCapacity = new HashMap<>();

    @Override
    public void reserveSpot(ReserveRequest request,
            StreamObserver<ReserveResponse> responseObserver) {
        int currentCount = activityCount.getOrDefault(request.getActivityId(), 0);
        int maxCap = activityCapacity.getOrDefault(request.getActivityId(), 0);
        String status;
        int queuePos;
        if (currentCount < maxCap) {
            status = "CONFIRMED";
            queuePos = 0;
            activityCount.put(request.getActivityId(), currentCount + 1);
        } else {
            status = "WAITING";
            queuePos = currentCount - maxCap + 1;
        }
        ReserveResponse response = ReserveResponse.newBuilder()
                .setReservationId(rid).setAttendeeId(request.getAttendeeId())
                .setActivityId(request.getActivityId()).setStatus(status)
                .setPositionInQueue(queuePos).setSuccess(true).build();
        reservations.put(rid, response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    // cancelReservation, getAttendeeReservations, getAvailableSpots follow
}
```

**EcicienciaGateway.java** — HTTP Gateway on port 8090. Uses `com.sun.net.httpserver.HttpServer` with 3 internal gRPC stubs. Exposes 8 HTTP endpoints:

| Method | Path | Parameters | Description |
|--------|------|-----------|-------------|
| GET | `/attendee` | `id` | Get attendee by ID |
| POST | `/attendee/register` | `name`, `email` | Register new attendee |
| GET | `/agenda` | `start`, `end` | Activities by time slot |
| GET | `/agenda/activity` | `id` | Activity details + capacity |
| POST | `/workshop/reserve` | `attendeeId`, `activityId` | Reserve a spot |
| POST | `/workshop/cancel` | `reservationId` | Cancel reservation |
| GET | `/workshop/attendee` | `id` | Attendee's reservations |
| GET | `/consolidated` | `id` | Full info + reservations |

```java
public class EcicienciaGateway {
    private final AttendeeServiceGrpc.AttendeeServiceBlockingStub attendeeStub;
    private final AgendaServiceGrpc.AgendaServiceBlockingStub agendaStub;
    private final WorkshopServiceGrpc.WorkshopServiceBlockingStub workshopStub;

    public static void main(String[] args) throws Exception {
        EcicienciaGateway gateway = new EcicienciaGateway();
        HttpServer server = HttpServer.create(new InetSocketAddress(8090), 0);
        server.createContext("/attendee/register", gateway::handleRegister);
        server.createContext("/attendee", gateway::handleGetAttendee);
        server.createContext("/agenda/activity", gateway::handleActivityDetail);
        server.createContext("/agenda", gateway::handleAgenda);
        server.createContext("/workshop/reserve", gateway::handleReserve);
        server.createContext("/workshop/cancel", gateway::handleCancel);
        server.createContext("/workshop/attendee", gateway::handleAttendeeReservations);
        server.createContext("/consolidated", gateway::handleConsolidated);
        server.setExecutor(null);
        server.start();
    }
    // Each handler extracts query params, calls the appropriate gRPC stub,
    // and returns HTML
}
```

### How to Build and Run

#### 1. Compile

```bash
# From repository root (compiles proto, generates gRPC stubs, compiles Java)
mvn compile -pl src/edu/eci/arsw/excercise8 -am
```

#### 2. Start the 4 processes (in order: 3 gRPC servers → Gateway)

Open **4 separate terminals** (cmd.exe or PowerShell) from the repository root:

```bash
# Terminal 1: AttendeeService (gRPC, port 8091)
mvn exec:java -pl src/edu/eci/arsw/excercise8 -Dexec.mainClass="edu.eci.arsw.excercise8.attendee.AttendeeServer"

# Terminal 2: AgendaService (gRPC, port 8092)
mvn exec:java -pl src/edu/eci/arsw/excercise8 -Dexec.mainClass="edu.eci.arsw.excercise8.agenda.AgendaServer"

# Terminal 3: WorkshopService (gRPC, port 8093)
mvn exec:java -pl src/edu/eci/arsw/excercise8 -Dexec.mainClass="edu.eci.arsw.excercise8.workshop.WorkshopServer"

# Terminal 4: ECICIENCIA Gateway (HTTP, port 8090)
mvn exec:java -pl src/edu/eci/arsw/excercise8 -Dexec.mainClass="edu.eci.arsw.excercise8.gateway.EcicienciaGateway"
```

> **Note:** Use `-pl src/edu/eci/arsw/excercise8` (project list by module path) from the root to avoid quoting issues on Windows. If you prefer `-f`, use:  
> `mvn exec:java -f src/edu/eci/arsw/excercise8/pom.xml -Dexec.mainClass="..."`

Wait for each terminal to show its "iniciado" message before starting the next one.

#### 3. Test with curl

All requests go to the Gateway on `http://localhost:8090`. Use a **5th terminal** for curl commands.

```bash
# ======================================================================
# 1. REGISTER a new attendee (POST)
# ======================================================================
curl -X POST "http://localhost:8090/attendee/register?name=Ana+Lopez&email=ana@mail.com"

# ======================================================================
# 2. GET attendee by ID (GET)
# ======================================================================
curl "http://localhost:8090/attendee?id=1"

# ======================================================================
# 3. GET agenda by time slot (GET)
# ======================================================================
curl "http://localhost:8090/agenda?start=09:00&end=12:00"

# ======================================================================
# 4. GET activity details + capacity (GET)
# ======================================================================
curl "http://localhost:8090/agenda/activity?id=2"

# ======================================================================
# 5. RESERVE a spot (POST)
# ======================================================================
curl -X POST "http://localhost:8090/workshop/reserve?attendeeId=1&activityId=2"

# ======================================================================
# 6. GET attendee's reservations (GET)
# ======================================================================
curl "http://localhost:8090/workshop/attendee?id=1"

# ======================================================================
# 7. GET consolidated info (attendee + reservations) (GET)
# ======================================================================
curl "http://localhost:8090/consolidated?id=1"

# ======================================================================
# 8. CANCEL a reservation (POST)
# ======================================================================
curl -X POST "http://localhost:8090/workshop/cancel?reservationId=1"

# ======================================================================
# 9. ERROR CASES — parameters missing
# ======================================================================
curl "http://localhost:8090/attendee"                       # 400 — falta id
curl -X POST "http://localhost:8090/attendee/register"      # 400 — faltan name y email
curl "http://localhost:8090/agenda"                         # 400 — faltan start y end
curl -X POST "http://localhost:8090/workshop/reserve"       # 400 — faltan attendeeId y activityId

# ======================================================================
# 10. ERROR CASES — non-numeric parameters
# ======================================================================
curl "http://localhost:8090/attendee?id=abc"                # 400 — id debe ser numerico

# ======================================================================
# 11. NOT FOUND cases
# ======================================================================
curl "http://localhost:8090/attendee?id=999"                # 404 — asistente no encontrado
curl "http://localhost:8090/agenda/activity?id=99"          # 404 — actividad no encontrada
curl "http://localhost:8090/consolidated?id=999"            # 404 — asistente no encontrado

# ======================================================================
# 12. WRONG HTTP method
# ======================================================================
curl "http://localhost:8090/attendee/register"              # 405 — GET no es POST
```

### Expected Output

All responses are HTML. Key examples:

| Request | Expected Response |
|---------|-----------------|
| `POST /attendee/register?name=Ana+Lopez&email=ana@mail.com` | `<h1>Asistente registrado</h1><p>ID: 3 \| Nombre: Ana Lopez \| Email: ana@mail.com</p>` |
| `GET /attendee?id=1` | `<h1>Asistente</h1><p>ID: 1 \| Nombre: Carlos Perez \| Email: carlos@mail.com</p>` |
| `GET /agenda?start=09:00&end=12:00` | 2 activities in list (ML talk + Arduino workshop) |
| `GET /agenda/activity?id=2` | `<h1>Taller: Arduino Basico</h1>...<p>Aforo: 18/20 (2 disponibles)</p>` |
| `POST /workshop/reserve?attendeeId=1&activityId=2` | `<h1>Reserva confirmada para actividad 2</h1><p>ID reserva: 2 \| Estado: CONFIRMED</p>` |
| `GET /workshop/attendee?id=1` | Lists reservation for attendee 1 |
| `GET /consolidated?id=1` | Full info + all reservations |
| `POST /workshop/cancel?reservationId=1` | `<h1>Reserva cancelada exitosamente</h1>` |
| Missing parameters | `<h1>Faltan parametros: ...</h1>` (HTTP 400) |
| Wrong method (GET on POST endpoint) | `<h1>Use POST</h1>` (HTTP 405) |
| Non-numeric id | `<h1>Error interno</h1>...parametro debe ser numerico...` (HTTP 500) |
| Backend server down | `<h1>Servicio X no disponible</h1>...` (HTTP 500) |

### Testing with a browser

You can also open the GET endpoints directly in a browser:

- `http://localhost:8090/attendee?id=1`
- `http://localhost:8090/agenda?start=09:00&end=12:00`
- `http://localhost:8090/agenda/activity?id=2`
- `http://localhost:8090/workshop/attendee?id=1`
- `http://localhost:8090/consolidated?id=1`

(POST endpoints like register, reserve, and cancel require curl or Postman.)

### Complete testing workflow (recommended order)

```bash
# 1. Register a new attendee
curl -X POST "http://localhost:8090/attendee/register?name=Ana+Lopez&email=ana@mail.com"
# → ID: 3

# 2. Verify the attendee was created
curl "http://localhost:8090/attendee?id=3"
# → Ana Lopez

# 3. Check available activities in the morning
curl "http://localhost:8090/agenda?start=09:00&end=12:00"
# → 2 activities: ML talk + Arduino workshop

# 4. Check capacity of the Arduino workshop (activity 2)
curl "http://localhost:8090/agenda/activity?id=2"
# → Cupo: 18/20 (2 disponibles)

# 5. Reserve a spot in the Arduino workshop
curl -X POST "http://localhost:8090/workshop/reserve?attendeeId=3&activityId=2"
# → CONFIRMED

# 6. Verify the reservation
curl "http://localhost:8090/workshop/attendee?id=3"
# → Shows reservation for activity 2

# 7. Get consolidated info for attendee 3
curl "http://localhost:8090/consolidated?id=3"
# → Attendee details + reservation list

# 8. Cancel the reservation
curl -X POST "http://localhost:8090/workshop/cancel?reservationId=1"
# → Cancelada exitosamente

# 9. Verify cancellation
curl "http://localhost:8090/workshop/attendee?id=3"
# → Shows CANCELLED status
```

### 15.1 Design Decisions

- **gRPC over HTTP for internal services:** Formal contracts guarantee compile-time interface validation.
- **Maven module with protobuf plugin:** Follows the same pattern as other gRPC modules (guide5_2, excercise5_3, etc.).
- **Gateway as protocol adapter:** HTTP ↔ gRPC translation; external clients never need protobuf.
- **Waiting list logic:** When capacity is reached, reservations enter WAITING status with a queue position — demonstrates real-world domain logic.
- **In-memory state:** Consistent with workshop constraints. Preloaded data for immediate testing.
- **Unique proto service names:** Prevents namespace collisions in generated Java code.

### 15.2 Architecture Diagram

A PlantUML diagram is available at `docs/diagrams/exercise8_architecture.puml`:

```plantuml
@startuml
!define RECTANGLE class
skinparam componentStyle rectangle
actor "Client / Browser" as client
rectangle "ECICIENCIA Gateway\n(:8090)" as gateway {
  component [HTTP Server\ncom.sun.net.httpserver] as http
  component [AttendeeStub\ngRPC] as atteStub
  component [AgendaStub\ngRPC] as agenStub
  component [WorkshopStub\ngRPC] as workStub
}
database "Attendee DB\n(in memory)" as atteDB
database "Agenda DB\n(in memory)" as agenDB
database "Reservation DB\n(in memory)" as workDB
rectangle "AttendeeService\n(:8091)" as atteSvc { component [gRPC Server] as atteGrpc }
rectangle "AgendaService\n(:8092)" as agenSvc { component [gRPC Server] as agenGrpc }
rectangle "WorkshopService\n(:8093)" as workSvc { component [gRPC Server] as workGrpc }
client --> gateway : HTTP
http --> atteStub : route
http --> agenStub : route
http --> workStub : route
atteStub --> atteSvc : gRPC
agenStub --> agenSvc : gRPC
workStub --> workSvc : gRPC
atteSvc --> atteDB : read/write
agenSvc --> agenDB : read/write
workSvc --> workDB : read/write
@enduml
```

### 15.3 Reflection on Architectural Evolution

The workshop traces a clear progression across six architectural styles, each solving a specific problem created by the previous one:

**1. TCP Sockets (Guide 2.2 → Exercise 2.3)** — Raw TCP required manual protocol design (`MOVIE:id`). The contract existed only in documentation. Every client had to be Java. Low-level control taught us what happens on the wire, but impractical beyond toy systems.

**2. HTTP (Guide 3.2 → Exercise 3.3)** — Added structure (method, route, parameters, headers, body) and interoperability. Any browser or curl could interact. Trade-off: manual query parsing, no framework support. Good for simple APIs but lacks formal contract rigor.

**3. Java RMI (Guide 4.2 → Exercise 4.3)** — Eliminated manual message parsing. Remote method calls look local. Fatal limitation: Java-only. Python or Node.js cannot consume RMI services.

**4. gRPC (Guide 5.2 → Exercise 5.3)** — Combined HTTP interoperability with RMI invocation semantics. Formal `.proto` contracts, strong typing, efficient binary serialization. Cross-language clients finally practical.

**5. Microservices (Guide 6.2 → Exercise 6.3)** — Split monoliths into cohesive services. Each owns its data and logic. New problem: clients know too many addresses and ports.

**6. API Gateway (Guide 7.2 → Exercise 7.3 → Exercise 8)** — Hides internal topology behind a single HTTP entry point. Client sends one request to one URL; Gateway fans out, aggregates, and handles errors.

**ECICIENCIA as composition:** The final exercise composes all lessons: gRPC contracts (style 4) for service boundaries, microservice decomposition (style 5) for domain separation, and an API Gateway (style 6) for unified access. The lower-level styles (TCP, HTTP without framework, RMI) are not used directly but understanding them is essential for recognizing why later styles exist — each one addresses a limitation of its predecessor.

**Key lesson:** Architectural decisions are trade-offs, not absolute improvements. gRPC is not "better" than TCP in every dimension. Microservices improve autonomy but add network complexity. The Gateway simplifies the client but becomes a single point of failure. A good architect chooses the style matching the problem's constraints.

---

## Port Summary

| Component | Technology | Port |
|-----------|------------|------|
| MovieServer TCP | Java Sockets | 35000 |
| RoomServer TCP | Java Sockets | 36000 |
| MovieHttpServer | com.sun.net.httpserver | 8080 |
| RoomHttpServer | com.sun.net.httpserver | 8081 |
| MovieGateway (Guide 7.2) | HTTP (JDK) | 8082 |
| WellnessGateway (Exercise 7.3) | HTTP (JDK) | 8083 |
| MovieService RMI | Java RMI | 23000 |
| EquipmentService RMI | Java RMI | 24000 |
| MovieGrpcServer (Guide 5.2) | gRPC | 50051 |
| MovieService (Microservice Guide 6.2) | gRPC | 50051 |
| ReviewService (Microservice Guide 6.2) | gRPC | 50052 |
| RecommendationService (Microservice Guide 6.2) | gRPC | 50053 |
| WellnessGrpcServer (Exercise 5.3) | gRPC | 50061 |
| AppointmentService (Microservice Exercise 6.3) | gRPC | 50061 |
| MedicalService (Microservice Exercise 6.3) | gRPC | 50062 |
| GymService (Microservice Exercise 6.3) | gRPC | 50063 |
| RecreationService (Microservice Exercise 6.3) | gRPC | 50064 |
| ECICIENCIA Gateway (Exercise 8) | HTTP (JDK) | 8090 |
| AttendeeService (Exercise 8) | gRPC | 8091 |
| AgendaService (Exercise 8) | gRPC | 8092 |
| WorkshopService (Exercise 8) | gRPC | 8093 |
