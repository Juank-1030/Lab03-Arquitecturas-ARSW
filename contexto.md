# Taller Integrador ARSW 2026-I
## Evolución de Arquitecturas Distribuidas con Java

**Escuela Colombiana de Ingeniería Julio Garavito**  
**Arquitecturas de Software - ARSW**  
**Duración:** 8 horas (dos sesiones de 4 horas)  
**Autor:** Rodrigo Humberto Gualtero  
**Modalidad:** Trabajo guiado, implementación progresiva y ejercicio aplicado

---

## 1. Introducción

Este taller propone una ruta práctica para comprender la evolución de los mecanismos de comunicación entre aplicaciones distribuidas. A diferencia de ejercicios aislados, se desarrolla un mismo caso base transformándolo progresivamente mediante distintos estilos arquitectónicos: cliente-servidor con sockets, HTTP, RMI, gRPC, microservicios y API Gateway.

El objetivo pedagógico es que el estudiante observe cómo cambia la arquitectura cuando cambian los mecanismos de comunicación, los contratos, las responsabilidades y el nivel de desacoplamiento entre componentes.

El taller está diseñado para dos sesiones de cuatro horas. En cada estilo arquitectónico se presenta una guía paso a paso con un ejemplo corto, seguida de un ejercicio aplicado de mayor exigencia analítica.

### 1.1 Objetivo General

Implementar y analizar la evolución de una aplicación distribuida en Java, identificando los problemas que resuelve cada estilo arquitectónico y las decisiones de diseño que surgen al pasar de comunicación directa a servicios distribuidos, contratos formales, microservicios y un punto de entrada centralizado.

### 1.2 Resultados de Aprendizaje

- Implementar una aplicación cliente-servidor usando sockets TCP
- Exponer funcionalidad básica mediante HTTP
- Comprender el modelo RPC con Java RMI
- Definir contratos de comunicación usando Protocol Buffers y gRPC
- Separar responsabilidades mediante microservicios cohesivos
- Centralizar el acceso a múltiples servicios con un API Gateway
- Comparar ventajas, limitaciones y *trade-offs* de cada estilo arquitectónico

### 1.3 Organización de las Sesiones

| Sesión | Duración | Temas | Producto Esperado |
|--------|----------|-------|-------------------|
| Sesión 1 | 4 horas | Sockets TCP, HTTP básico, RMI | Primeras tres versiones distribuidas del sistema base y ejercicios aplicados |
| Sesión 2 | 4 horas | gRPC, microservicios, API Gateway | Versión moderna orientada a servicios y diseño del ejercicio integrador |

### 1.4 Caso Base: Movie Information System

Sistema mínimo de consulta de películas. Funcionalidad sencilla que evoluciona en varios estilos arquitectónicos.

| ID | Título | Director | Año |
|----|--------|----------|-----|
| 1 | Interstellar | Christopher Nolan | 2014 |
| 2 | Matrix | Wachowski | 1999 |
| 3 | Inception | Christopher Nolan | 2010 |

**Modelo conceptual inicial:**

```java
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

    public String toText() {
        return id + "," + title + "," + director + "," + year;
    }
}
```

### 1.5 Recomendaciones

- Trabajo individual. Cada estudiante entrega su propia implementación, diagramas y reflexión.
- Ejecutar primero el ejemplo guiado antes del ejercicio aplicado.
- Registrar evidencias: capturas de consola, diagramas y respuestas.
- Sin base de datos; toda la información en memoria.
- Priorizar la comprensión arquitectónica sobre la cantidad de funcionalidades.

---

## 2. Parte I - Arquitectura Cliente-Servidor con Sockets TCP

### 2.1 ¿Qué problema resuelve?

Un programa cliente solicita un servicio a un servidor que escucha en un puerto específico. El protocolo de comunicación se diseña manualmente mediante mensajes de texto sobre sockets TCP.

```
Cliente Java
   |
   | "MOVIE:1"
   v
Servidor TCP Java
   |
   | "1,Interstellar,Christopher Nolan,2014"
   v
Cliente muestra el resultado
```

Útil para entender fundamentos de comunicación distribuida, pero obliga a definir manualmente el formato de mensajes, validación, errores y respuestas.

### 2.2 Guía: MovieServer TCP

**Paso 1 - Estructura del proyecto**

Carpeta `movie-tcp` con los archivos: `Movie.java`, `MovieRepository.java`, `MovieServer.java`, `MovieClient.java`.

**Paso 2 - Modelo Movie**

```java
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

**Paso 3 - Repositorio en memoria**

```java
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

**Paso 4 - Servidor TCP**

Escucha en el puerto `35000`. Recibe mensajes con formato `MOVIE:id` y responde en texto plano.

```java
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
            return "ERROR: formato inválido. Use MOVIE:id";
        try {
            int id = Integer.parseInt(request.split(":")[1]);
            Movie movie = repository.findById(id);
            if (movie == null) return "ERROR: película no encontrada";
            return movie.toText();
        } catch (Exception e) {
            return "ERROR: solicitud inválida";
        }
    }
}
```

**Paso 5 - Cliente TCP**

```java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MovieClient {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el ID de la película: ");
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

**Paso 6 - Ejecución**

```bash
javac *.java
java MovieServer
# En otra terminal:
java MovieClient
```

**Prueba esperada:** ID 1 → `1,Interstellar,Christopher Nolan,2014`

### 2.3 Ejercicio Aplicado 1: Sistema de Gestión de Salones

Diseñar e implementar un servidor TCP para gestionar reservas de salones (E301, E302, E303, E304).

**Requisitos funcionales:**
- Consultar estado de un salón
- Reservar un salón disponible
- Liberar un salón reservado

**Protocolo sugerido:**

| Comando | Respuesta |
|---------|-----------|
| `CONSULTAR_SALON,E303` | `SALON_DISPONIBLE` / `SALON_RESERVADO` |
| `RESERVAR_SALON,E303` | `RESERVA_EXITOSA` / `ERROR_SALON_NO_EXISTE` |
| `LIBERAR_SALON,E303` | `LIBERACION_EXITOSA` / `ERROR_OPERACION_INVALIDA` |

**Reflexión:**
- ¿Qué tan fácil sería agregar una nueva operación al protocolo?
- ¿Qué ocurre si dos clientes intentan reservar el mismo salón simultáneamente?
- ¿Dónde está definido realmente el contrato de comunicación?

---

## 3. Parte II - Arquitectura HTTP con Java

### 3.1 ¿Qué problema resuelve?

La versión TCP obliga a escribir un cliente Java. Con HTTP la funcionalidad se expone de forma estándar, accesible desde navegador, curl o Postman.

```
Navegador / Cliente HTTP
   |
   | GET /movie?id=1
   v
Servidor HTTP básico en Java
   |
   | HTML o texto
   v
Usuario visualiza el resultado
```

No se usa framework web. El objetivo es observar cómo HTTP se construye sobre TCP y cómo aparecen concepto como método, ruta, parámetro y respuesta.

### 3.2 Guía: MovieHttpServer

**Paso 1 - Servidor HTTP**

Usa `com.sun.net.httpserver.HttpServer` (incluido en el JDK).

```java
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
                    response = "<html><body><h1>Película no encontrada</h1></body></html>";
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

**Paso 2 - Ejecutar y probar**

```bash
javac *.java
java MovieHttpServer
# Abrir: http://localhost:8080/movie?id=1
```

**Paso 3 - Diferencia con TCP**

En TCP el contrato era `MOVIE:1`. En HTTP la solicitud tiene estructura estándar: método, ruta, parámetros, encabezados y cuerpo. Esto facilita la interoperabilidad con clientes que no sean Java.

### 3.3 Ejercicio Aplicado 2: Gestión de Salones vía HTTP

Transformar el ejercicio de salones a HTTP.

**Rutas requeridas:**

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/rooms` | Listar todos los salones |
| GET | `/rooms?id=E303` | Consultar un salón |
| POST | `/rooms/reserve?id=E303` | Reservar un salón |
| POST | `/rooms/release?id=E303` | Liberar un salón |

**Respuesta:** texto plano o HTML simple.

**Reflexión:**
- ¿Qué ventajas ofrece HTTP frente a un protocolo de texto manual?
- ¿Qué limitaciones tiene construir un servidor HTTP sin framework?
- ¿Cómo cambiaría la solución usando JSON en lugar de HTML?

---

## 4. Parte III - RPC con Java RMI

### 4.1 ¿Qué problema resuelve?

RMI permite invocar métodos de un objeto en otra máquina virtual Java. Se elimina el diseño manual del formato de mensajes; la comunicación se expresa como invocación remota de métodos.

```
Cliente Java
   |
   | lookup("movieService")
   v
RMI Registry
   |
   | referencia remota
   v
MovieService remoto
   |
   | getMovie(1)
   v
Resultado al cliente
```

RMI es importante como tecnología histórica y conceptual para entender el modelo RPC, aunque está fuertemente ligado al ecosistema Java.

### 4.2 Guía: MovieService con RMI

**Paso 1 - Movie serializable**

```java
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

    @Override
    public String toString() {
        return id + " - " + title + " (" + year + ") - " + director;
    }
}
```

**Paso 2 - Interfaz remota**

```java
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MovieService extends Remote {
    Movie getMovie(int id) throws RemoteException;
}
```

**Paso 3 - Implementación del servicio**

```java
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

**Paso 4 - Publicar el servicio**

```java
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

**Paso 5 - Cliente RMI**

```java
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MovieRmiClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 23000);
        MovieService service = (MovieService) registry.lookup("movieService");
        Movie movie = service.getMovie(1);
        System.out.println("Película recibida: " + movie);
    }
}
```

**Paso 6 - Ejecución**

```bash
javac *.java
java MovieRmiServer
# En otra terminal:
java MovieRmiClient
```

### 4.3 Ejercicio Aplicado 3: Inventario de Laboratorios

Sistema RMI para consultar y reservar equipos de laboratorio.

**Datos mínimos:** código, nombre, laboratorio, estado (disponible/reservado).

**Métodos requeridos:**
- `List<String> consultarEquipos()`
- `String consultarEquipo(String codigo)`
- `boolean reservarEquipo(String codigo)`
- `boolean liberarEquipo(String codigo)`

**Reflexión:**
- ¿Qué cambió al pasar de HTTP a RMI?
- ¿Dónde está definido el contrato de comunicación?
- ¿Qué problemas tendría este sistema con un cliente que no sea Java?

---

## 5. Parte IV - Comunicación Moderna con gRPC

### 5.1 ¿Qué problema resuelve?

gRPC implementa RPC moderno con contratos definidos en archivos `.proto`. A diferencia de RMI, no está limitado a Java: un servicio en Java puede ser consumido por clientes en otros lenguajes. Los mensajes son fuertemente tipados y se serializan con Protocol Buffers.

```
movie.proto
   |
   | genera clases Java
   v
Servidor gRPC <---- canal gRPC ----> Cliente gRPC
```

### 5.2 Guía: MovieService con gRPC

**Paso 1 - Estructura del proyecto Maven**

```
movie-grpc/
 ├── pom.xml
 └── src/main/
      ├── java/edu/eci/arsw/guide5_2/
     └── proto/movie.proto
```

**Paso 2 - pom.xml**

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>edu.eci.arsw</groupId>
    <artifactId>movie-grpc</artifactId>
    <version>1.0-SNAPSHOT</version>
    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
    </properties>
    <dependencies>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-netty-shaded</artifactId>
            <version>1.63.0</version>
        </dependency>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-protobuf</artifactId>
            <version>1.63.0</version>
        </dependency>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-stub</artifactId>
            <version>1.63.0</version>
        </dependency>
        <dependency>
            <groupId>com.google.protobuf</groupId>
            <artifactId>protobuf-java</artifactId>
            <version>3.25.3</version>
        </dependency>
        <dependency>
            <groupId>javax.annotation</groupId>
            <artifactId>javax.annotation-api</artifactId>
            <version>1.3.2</version>
        </dependency>
    </dependencies>
    <build>
        <extensions>
            <extension>
                <groupId>kr.motd.maven</groupId>
                <artifactId>os-maven-plugin</artifactId>
                <version>1.7.1</version>
            </extension>
        </extensions>
        <plugins>
            <plugin>
                <groupId>org.xolstice.maven.plugins</groupId>
                <artifactId>protobuf-maven-plugin</artifactId>
                <version>0.6.1</version>
                <configuration>
                    <protocArtifact>com.google.protobuf:protoc:3.25.3:exe:${os.detected.classifier}</protocArtifact>
                    <pluginId>grpc-java</pluginId>
                    <pluginArtifact>io.grpc:protoc-gen-grpc-java:1.63.0:exe:${os.detected.classifier}</pluginArtifact>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                            <goal>compile-custom</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

**Paso 3 - movie.proto**

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

**Paso 4 - Generar clases**

```bash
mvn clean compile
```

Las clases generadas aparecen en `target/generated-sources`.

**Paso 5 - Servidor gRPC**

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
                        .setId(request.getId())
                        .setFound(false)
                        .build();
            }
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
```

**Paso 6 - Cliente gRPC**

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

**Paso 7 - Ejecución**

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="edu.eci.arsw.guide5_2.MovieGrpcServer"
# En otra terminal:
mvn exec:java -Dexec.mainClass="edu.eci.arsw.guide5_2.MovieGrpcClient"
```

### 5.3 Ejercicio Aplicado 4: Sistema de Bienestar Universitario con gRPC

Servicio gRPC para gestionar citas de bienestar universitario.

**Servicio:**

```protobuf
service AppointmentService {
  rpc RequestAppointment (AppointmentRequest) returns (AppointmentResponse);
  rpc CancelAppointment (CancelRequest) returns (CancelResponse);
  rpc GetAppointments (StudentRequest) returns (AppointmentList);
}
```

**Entidades:**
- `Student`: id, name, institutionalEmail
- `Appointment`: id, studentId, serviceType, date, status
- `ServiceType`: MEDICINE, PSYCHOLOGY, DENTISTRY
- `Status`: REQUESTED, CANCELLED, ATTENDED

**Reglas:**
- Cita solicitada → estado `REQUESTED`
- Cita cancelada → no aparece como activa
- Consultar citas de un estudiante
- Todo en memoria

**Reflexión:**
- ¿Por qué el archivo `.proto` se considera un contrato?
- ¿Qué tan fácil sería crear un cliente en otro lenguaje?
- ¿Qué diferencias hay entre RMI y gRPC?

---

## 6. Parte V - Arquitectura de Microservicios

### 6.1 ¿Qué problema resuelve?

Hasta ahora, aunque cambió la tecnología de comunicación, el sistema sigue siendo un único servicio. Al crecer, conviene separar responsabilidades en servicios más pequeños, autónomos y cohesivos.

```
Cliente
   |
   +--- MovieService        (películas)
   +--- ReviewService       (reseñas)
   +--- RecommendationService (recomendaciones)
```

Microservicios no significa crear muchos servicios sin criterio. Cada uno debe tener una responsabilidad clara y no conocer detalles internos de otros.

### 6.2 Guía: Dividir el sistema de películas

**Paso 1 - Identificar responsabilidades**

| Servicio | Responsabilidad | Puerto |
|----------|----------------|--------|
| MovieService | Consultar información de películas | 50051 |
| ReviewService | Consultar reseñas de una película | 50052 |
| RecommendationService | Sugerir películas relacionadas | 50053 |

**Paso 2 - ReviewService.proto**

```protobuf
syntax = "proto3";
option java_multiple_files = true;
option java_package = "edu.eci.arsw.review";

service ReviewService {
  rpc GetReviews (ReviewRequest) returns (ReviewList);
}

message ReviewRequest {
  int32 movieId = 1;
}

message Review {
  string author = 1;
  string comment = 2;
  int32 rating = 3;
}

message ReviewList {
  repeated Review reviews = 1;
}
```

**Paso 3 - RecommendationService.proto**

```protobuf
syntax = "proto3";
option java_multiple_files = true;
option java_package = "edu.eci.arsw.recommendation";

service RecommendationService {
  rpc GetRecommendations (RecommendationRequest) returns (RecommendationList);
}

message RecommendationRequest {
  int32 movieId = 1;
}

message RecommendationList {
  repeated string titles = 1;
}
```

**Paso 4 - Ejecutar en puertos distintos**

Cada servicio tiene su propio servidor gRPC en su puerto correspondiente.

```java
Server movieServer = ServerBuilder.forPort(50051)
        .addService(new MovieServiceImpl()).build();
Server reviewServer = ServerBuilder.forPort(50052)
        .addService(new ReviewServiceImpl()).build();
Server recommendationServer = ServerBuilder.forPort(50053)
        .addService(new RecommendationServiceImpl()).build();
```

**Paso 5 - Cliente que consulta varios servicios**

El cliente abre un canal por cada servicio. Esto funciona, pero genera un nuevo problema: el cliente conoce demasiados servicios y puertos.

```
Cliente
   |--- localhost:50051 → MovieService
   |--- localhost:50052 → ReviewService
   |--- localhost:50053 → RecommendationService
```

### 6.3 Ejercicio Aplicado 5: Descomposición de Bienestar Universitario

A partir del ejercicio de citas con gRPC, proponer e implementar una descomposición en microservicios.

**Servicios mínimos sugeridos:**

| Servicio | Responsabilidad |
|----------|----------------|
| AppointmentService | Gestionar citas y turnos |
| MedicalService | Información de especialidades médicas |
| GymService | Reservas de sesiones de gimnasio |
| RecreationService | Préstamo de recursos recreativos |

**Producto esperado:**
- Diagrama de microservicios
- Descripción de responsabilidades
- Al menos dos servicios implementados en puertos distintos
- Cliente que consuma los servicios

**Reflexión:**
- ¿Por qué separar esos servicios y no otros?
- ¿Qué datos pertenecen a cada servicio?
- ¿Qué riesgo aparece cuando el cliente conoce todos los servicios?

---

## 7. Parte VI - API Gateway

### 7.1 ¿Qué problema resuelve?

Cuando el cliente conoce todos los microservicios, queda acoplado a sus direcciones, puertos y contratos individuales. Un API Gateway centraliza el acceso como punto de entrada único.

```
Cliente
   |
   v
API Gateway
   |
   +--- MovieService
   +--- ReviewService
   +--- RecommendationService
```

El Gateway se implementa como un programa Java sencillo para comprender el patrón arquitectónico.

### 7.2 Guía: MovieGateway

**Paso 1 - Responsabilidades del Gateway**
- Recibir solicitudes del cliente
- Conectarse internamente a los servicios necesarios
- Unificar la respuesta
- Evitar que el cliente conozca los puertos de cada microservicio

**Paso 2 - Gateway de consola**

```java
public class MovieGateway {
    public static void main(String[] args) {
        // 1. Crear canal hacia MovieService
        // 2. Crear canal hacia ReviewService
        // 3. Crear canal hacia RecommendationService
        // 4. Consultar los servicios internos
        // 5. Construir respuesta integrada para el cliente
    }
}
```

**Paso 3 - Formato de respuesta esperado**

```
Película: Interstellar
Director: Christopher Nolan
Año: 2014
Reseñas:
  - Excelente película de ciencia ficción. Rating: 5
  - Visualmente impresionante. Rating: 4
Recomendaciones:
  - Inception
  - Contact
  - 2001: A Space Odyssey
```

**Paso 4 - Análisis**

El Gateway centraliza el acceso pero también es un punto crítico: si falla, se pierde acceso a todo el sistema. En arquitecturas reales se requieren estrategias de disponibilidad, monitoreo y escalabilidad.

### 7.3 Ejercicio Aplicado 6: WellnessGateway

Gateway para centralizar el acceso a los servicios de bienestar universitario.

**Servicios internos:** AppointmentService, MedicalService, GymService, RecreationService

**Operaciones mínimas:**
- `requestAppointment(studentId, serviceType)`
- `getStudentWellnessSummary(studentId)`
- `reserveGymSession(studentId, timeSlot)`
- `reserveRecreationResource(studentId, resourceId)`

**Reflexión:**
- ¿Qué simplifica el Gateway para el cliente?
- ¿Qué complejidad agrega al sistema?
- ¿Qué pasa si el Gateway acumula demasiada lógica de negocio?

---

## 8. Ejercicio Integrador Final - Plataforma ECICIENCIA

Diseñar la arquitectura de una plataforma distribuida para la gestión del evento ECICIENCIA. No exige implementación completa, pero sí justificar decisiones arquitectónicas usando los estilos del taller.

### 8.1 Contexto

La Escuela necesita una plataforma para organizar actividades académicas, talleres, charlas y experiencias tecnológicas. Debe permitir registrar asistentes, consultar agenda, reservar talleres y controlar aforo.

### 8.2 Funcionalidades Mínimas

- Registro de asistentes
- Consulta de agenda
- Reserva de cupos en talleres
- Control de aforo por actividad
- Consulta de actividades por franja horaria

### 8.3 Actividades

1. Identificar los microservicios necesarios
2. Definir la responsabilidad de cada uno
3. Proponer los contratos gRPC principales
4. Diseñar un API Gateway
5. Elaborar un diagrama de arquitectura
6. Justificar por qué no se usaría un único servicio monolítico

### 8.4 Entregable

- Diagrama arquitectónico
- Lista de servicios y responsabilidades
- Al menos un archivo `.proto` propuesto
- Descripción del Gateway
- Reflexión (máximo 1 página) sobre la evolución arquitectónica del taller

---

## 9. Comparación de Estilos Arquitectónicos

| Estilo | Ventaja Principal | Limitación Principal | Cuándo Usarlo |
|--------|------------------|-------------------|---------------|
| **Sockets TCP** | Control total sobre la comunicación | Protocolo manual y de bajo nivel | Aprendizaje de redes o protocolos muy específicos |
| **HTTP** | Interoperabilidad y acceso desde navegador | Requiere diseñar rutas y formatos de respuesta | APIs web simples |
| **RMI** | Invocación remota de métodos en Java | Dependencia del ecosistema Java | Sistemas Java distribuidos controlados |
| **gRPC** | Contratos formales, eficiencia e interoperabilidad | Requiere configuración de protobuf | Microservicios y comunicación interna |
| **Microservicios** | Separación de responsabilidades y escalabilidad | Mayor complejidad operacional | Sistemas con dominios claramente separables |
| **API Gateway** | Punto de entrada único | Puede convertirse en cuello de botella | Sistemas con varios servicios internos |

---

## 10. Rúbrica de Evaluación

| Criterio | % | Descripción |
|----------|---|-------------|
| Implementación técnica | 30% | Ejemplos y ejercicios ejecutan correctamente según el estilo arquitectónico solicitado |
| Diseño arquitectónico | 25% | Separación de responsabilidades, contratos y servicios coherente |
| Análisis y reflexión | 20% | Explica ventajas, limitaciones y *trade-offs* de cada estilo |
| Diagramas | 15% | Representan correctamente componentes, comunicaciones y responsabilidades |
| Orden y documentación | 10% | Código organizado y entregable claro |

---

## 11. Entregables del Taller

- Código fuente de todas las versiones implementadas
- Evidencias de ejecución de cada parte
- Diagramas de arquitectura por estilo
- Solución de los ejercicios aplicados
- Diseño del ejercicio integrador ECICIENCIA
- Reflexión final sobre la evolución de la arquitectura

---

## 12. Cierre

Las arquitecturas distribuidas no existen por moda, sino como respuesta a problemas concretos: comunicación remota, interoperabilidad, contratos, separación de responsabilidades, escalabilidad y reducción del acoplamiento. El valor del taller no está solo en ejecutar código, sino en comprender por qué cada estilo arquitectónico existe y qué problema intenta resolver.

Al finalizar, el estudiante debe poder explicar cómo una solución simple evoluciona desde un servidor TCP hasta una arquitectura basada en servicios y Gateway, reconociendo los costos y beneficios de cada decisión.
