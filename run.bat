@echo off
title Lab03 ARSW - Launcher
chcp 65001 >nul

if "%1"=="" goto menu
if "%1"=="menu" goto menu
if "%1"=="clean" goto cmd_clean
if "%1"=="compile" goto cmd_compile
if "%1"=="install" goto cmd_install
goto run_%1_%2 2>nul
goto error

:menu
cls
echo ============================================
echo  Lab 03 - ARSW - Lanzador Rapido
echo ============================================
echo.
echo  COMPILACION / INSTALACION
echo  -------------------------
echo   c) Compilar todos los modulos
echo   i) Instalar todos los modulos (para gateways)
echo   x) Limpiar y recompilar
echo.
echo   NOTA: 'mvn clean' solo borra target/ y VSCode
echo         pierde las clases generadas por protobuf.
echo         Use las opciones de arriba en su lugar.
echo.
echo  GUIAS Y EJERCICIOS (servidor/cliente)
echo  ------------------------------------
echo   2a) Guide 2.2 - MovieServer TCP
echo   2b) Exercise 2.3 - Room Management TCP
echo   3a) Guide 3.2 - MovieHttpServer
echo   3b) Exercise 3.3 - Room Management HTTP
echo   4a) Guide 4.2 - MovieService RMI
echo   4b) Exercise 4.3 - Lab Inventory RMI
echo   5a) Guide 5.2 - MovieService gRPC
echo   5b) Exercise 5.3 - Wellness gRPC
echo   6a) Guide 6.2 - Movie Microservices
echo   6b) Exercise 6.3 - Wellness Microservices
echo   7a) Guide 7.2 - Movie Gateway
echo   7b) Exercise 7.3 - Wellness Gateway
echo   8)  Exercise 8 - ECICIENCIA
echo.
echo   0) Salir
echo ============================================
echo.

set /p opcion="Seleccione: "

if "%opcion%"=="c" goto cmd_compile
if "%opcion%"=="i" goto cmd_install
if "%opcion%"=="x" goto cmd_clean
if "%opcion%"=="2a" goto menu_guide2_2
if "%opcion%"=="2b" goto menu_exercise2_3
if "%opcion%"=="3a" goto menu_guide3_2
if "%opcion%"=="3b" goto menu_exercise3_3
if "%opcion%"=="4a" goto menu_guide4_2
if "%opcion%"=="4b" goto menu_exercise4_3
if "%opcion%"=="5a" goto menu_guide5_2
if "%opcion%"=="5b" goto menu_exercise5_3
if "%opcion%"=="6a" goto menu_guide6_2
if "%opcion%"=="6b" goto menu_exercise6_3
if "%opcion%"=="7a" goto menu_guide7_2
if "%opcion%"=="7b" goto menu_exercise7_3
if "%opcion%"=="8" goto menu_exercise8
if "%opcion%"=="0" exit /b 0
goto error

:: ============================================
:: COMPILACION / INSTALACION
:: ============================================

:cmd_compile
echo.
echo Compilando todos los modulos Maven...
call mvn compile
if %errorlevel%==0 (echo LISTO) else (echo ERROR)
goto fin

:cmd_install
echo.
echo Instalando todos los modulos (necesario para gateways)...
call mvn install -DskipTests
if %errorlevel%==0 (echo LISTO) else (echo ERROR)
goto fin

:cmd_clean
echo.
echo Limpiando y recompilando...
echo Si usa VSCode, recargue: Ctrl+Shift+P -^> Java: Reload Projects
call mvn clean compile
if %errorlevel%==0 (echo LISTO) else (echo ERROR)
goto fin

:: ============================================
:: GUIDE 2.2 - Movie TCP
:: ============================================

:menu_guide2_2
cls
echo ============================================
echo  Guide 2.2 - MovieServer TCP
echo ============================================
echo  1) Compilar
echo  2) Servidor (Terminal 1)
echo  3) Cliente (Terminal 2)
echo  0) Volver
echo ============================================
set /p op="Opcion: "
if "%op%"=="1" javac -d bin src/edu/eci/arsw/guide2_2/*.java & echo LISTO
if "%op%"=="2" java -cp bin edu.eci.arsw.guide2_2.MovieServer
if "%op%"=="3" java -cp bin edu.eci.arsw.guide2_2.MovieClient
if "%op%"=="0" goto menu
goto menu_guide2_2

:run_guide2_2_compile
javac -d bin src/edu/eci/arsw/guide2_2/*.java
echo LISTO - Guide 2.2 compilado
exit /b 0
:run_guide2_2_server
java -cp bin edu.eci.arsw.guide2_2.MovieServer
exit /b 0
:run_guide2_2_client
java -cp bin edu.eci.arsw.guide2_2.MovieClient
exit /b 0

:: ============================================
:: EXERCISE 2.3 - Room TCP
:: ============================================

:menu_exercise2_3
cls
echo ============================================
echo  Exercise 2.3 - Room Management TCP
echo ============================================
echo  1) Compilar
echo  2) Servidor (Terminal 1)
echo  3) Cliente (Terminal 2)
echo  0) Volver
echo ============================================
set /p op="Opcion: "
if "%op%"=="1" javac -d bin src/edu/eci/arsw/excercise2_3/*.java & echo LISTO
if "%op%"=="2" java -cp bin edu.eci.arsw.excercise2_3.RoomServer
if "%op%"=="3" java -cp bin edu.eci.arsw.excercise2_3.RoomClient
if "%op%"=="0" goto menu
goto menu_exercise2_3

:run_exercise2_3_compile
javac -d bin src/edu/eci/arsw/excercise2_3/*.java
echo LISTO
exit /b 0
:run_exercise2_3_server
java -cp bin edu.eci.arsw.excercise2_3.RoomServer
exit /b 0
:run_exercise2_3_client
java -cp bin edu.eci.arsw.excercise2_3.RoomClient
exit /b 0

:: ============================================
:: GUIDE 3.2 - Movie HTTP
:: ============================================

:menu_guide3_2
cls
echo ============================================
echo  Guide 3.2 - MovieHttpServer
echo ============================================
echo  1) Compilar
echo  2) Servidor (Terminal)
echo  0) Volver
echo ============================================
set /p op="Opcion: "
if "%op%"=="1" javac -d bin src/edu/eci/arsw/guide3_2/*.java & echo LISTO
if "%op%"=="2" java -cp bin edu.eci.arsw.guide3_2.MovieHttpServer
if "%op%"=="0" goto menu
goto menu_guide3_2

:run_guide3_2_compile
javac -d bin src/edu/eci/arsw/guide3_2/*.java
echo LISTO
exit /b 0
:run_guide3_2_server
java -cp bin edu.eci.arsw.guide3_2.MovieHttpServer
exit /b 0

:: ============================================
:: EXERCISE 3.3 - Room HTTP
:: ============================================

:menu_exercise3_3
cls
echo ============================================
echo  Exercise 3.3 - Room Management HTTP
echo ============================================
echo  1) Compilar
echo  2) Servidor (Terminal)
echo  0) Volver
echo ============================================
set /p op="Opcion: "
if "%op%"=="1" javac -d bin src/edu/eci/arsw/excercise3_3/*.java & echo LISTO
if "%op%"=="2" java -cp bin edu.eci.arsw.excercise3_3.RoomHttpServer
if "%op%"=="0" goto menu
goto menu_exercise3_3

:run_exercise3_3_compile
javac -d bin src/edu/eci/arsw/excercise3_3/*.java
echo LISTO
exit /b 0
:run_exercise3_3_server
java -cp bin edu.eci.arsw.excercise3_3.RoomHttpServer
exit /b 0

:: ============================================
:: GUIDE 4.2 - Movie RMI
:: ============================================

:menu_guide4_2
cls
echo ============================================
echo  Guide 4.2 - MovieService RMI
echo ============================================
echo  1) Compilar
echo  2) Servidor (Terminal 1)
echo  3) Cliente (Terminal 2)
echo  0) Volver
echo ============================================
set /p op="Opcion: "
if "%op%"=="1" javac -d bin src/edu/eci/arsw/guide4_2/*.java & echo LISTO
if "%op%"=="2" java -cp bin edu.eci.arsw.guide4_2.MovieRmiServer
if "%op%"=="3" java -cp bin edu.eci.arsw.guide4_2.MovieRmiClient
if "%op%"=="0" goto menu
goto menu_guide4_2

:run_guide4_2_compile
javac -d bin src/edu/eci/arsw/guide4_2/*.java
echo LISTO
exit /b 0
:run_guide4_2_server
java -cp bin edu.eci.arsw.guide4_2.MovieRmiServer
exit /b 0
:run_guide4_2_client
java -cp bin edu.eci.arsw.guide4_2.MovieRmiClient
exit /b 0

:: ============================================
:: EXERCISE 4.3 - Lab Inventory RMI
:: ============================================

:menu_exercise4_3
cls
echo ============================================
echo  Exercise 4.3 - Lab Inventory RMI
echo ============================================
echo  1) Compilar
echo  2) Servidor (Terminal 1)
echo  3) Cliente (Terminal 2)
echo  0) Volver
echo ============================================
set /p op="Opcion: "
if "%op%"=="1" javac -d bin src/edu/eci/arsw/excercise4_3/*.java & echo LISTO
if "%op%"=="2" java -cp bin edu.eci.arsw.excercise4_3.EquipmentRmiServer
if "%op%"=="3" java -cp bin edu.eci.arsw.excercise4_3.EquipmentRmiClient
if "%op%"=="0" goto menu
goto menu_exercise4_3

:run_exercise4_3_compile
javac -d bin src/edu/eci/arsw/excercise4_3/*.java
echo LISTO
exit /b 0
:run_exercise4_3_server
java -cp bin edu.eci.arsw.excercise4_3.EquipmentRmiServer
exit /b 0
:run_exercise4_3_client
java -cp bin edu.eci.arsw.excercise4_3.EquipmentRmiClient
exit /b 0

:: ============================================
:: GUIDE 5.2 - Movie gRPC
:: ============================================

:menu_guide5_2
cls
echo ============================================
echo  Guide 5.2 - MovieService gRPC
echo ============================================
echo  1) Compilar
echo  2) Servidor (Terminal 1)
echo  3) Cliente (Terminal 2)
echo  0) Volver
echo ============================================
set /p op="Opcion: "
if "%op%"=="1" mvn compile -f src/edu/eci/arsw/guide5_2/pom.xml & echo LISTO
if "%op%"=="2" mvn exec:java -f src/edu/eci/arsw/guide5_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide5_2.MovieGrpcServer
if "%op%"=="3" mvn exec:java -f src/edu/eci/arsw/guide5_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide5_2.MovieGrpcClient
if "%op%"=="0" goto menu
goto menu_guide5_2

:run_guide5_2_compile
mvn compile -f src/edu/eci/arsw/guide5_2/pom.xml
echo LISTO
exit /b 0
:run_guide5_2_server
mvn exec:java -f src/edu/eci/arsw/guide5_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide5_2.MovieGrpcServer
exit /b 0
:run_guide5_2_client
mvn exec:java -f src/edu/eci/arsw/guide5_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide5_2.MovieGrpcClient
exit /b 0

:: ============================================
:: EXERCISE 5.3 - Wellness gRPC
:: ============================================

:menu_exercise5_3
cls
echo ============================================
echo  Exercise 5.3 - Wellness gRPC
echo ============================================
echo  1) Compilar
echo  2) Servidor (Terminal 1)
echo  3) Cliente (Terminal 2)
echo  0) Volver
echo ============================================
set /p op="Opcion: "
if "%op%"=="1" mvn compile -f src/edu/eci/arsw/excercise5_3/pom.xml & echo LISTO
if "%op%"=="2" mvn exec:java -f src/edu/eci/arsw/excercise5_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise5_3.WellnessGrpcServer
if "%op%"=="3" mvn exec:java -f src/edu/eci/arsw/excercise5_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise5_3.WellnessGrpcClient
if "%op%"=="0" goto menu
goto menu_exercise5_3

:run_exercise5_3_compile
mvn compile -f src/edu/eci/arsw/excercise5_3/pom.xml
echo LISTO
exit /b 0
:run_exercise5_3_server
mvn exec:java -f src/edu/eci/arsw/excercise5_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise5_3.WellnessGrpcServer
exit /b 0
:run_exercise5_3_client
mvn exec:java -f src/edu/eci/arsw/excercise5_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise5_3.WellnessGrpcClient
exit /b 0

:: ============================================
:: GUIDE 6.2 - Movie Microservices
:: ============================================

:run_guide6_2_compile
mvn compile
echo LISTO
exit /b 0
:run_guide6_2_server1
mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide6_2.movie.MovieServiceServer
exit /b 0
:run_guide6_2_server2
mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide6_2.review.ReviewServiceServer
exit /b 0
:run_guide6_2_server3
mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide6_2.recommendation.RecommendationServiceServer
exit /b 0
:run_guide6_2_client
mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide6_2.MicroserviceClient
exit /b 0

:menu_guide6_2
cls
echo ============================================
echo  Guide 6.2 - Movie Microservices
echo ============================================
echo  Requiere 4 terminales: 3 servidores + 1 cliente
echo.
echo  1) Compilar todo
echo  2) Servidor 1 - MovieService (Terminal 1)
echo  3) Servidor 2 - ReviewService (Terminal 2)
echo  4) Servidor 3 - Recommendation (Terminal 3)
echo  5) Cliente (Terminal 4)
echo  0) Volver
echo ============================================
set /p op="Opcion: "
if "%op%"=="1" mvn compile & echo LISTO
if "%op%"=="2" mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide6_2.movie.MovieServiceServer
if "%op%"=="3" mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide6_2.review.ReviewServiceServer
if "%op%"=="4" mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide6_2.recommendation.RecommendationServiceServer
if "%op%"=="5" mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide6_2.MicroserviceClient
if "%op%"=="0" goto menu
goto menu_guide6_2

:: ============================================
:: EXERCISE 6.3 - Wellness Microservices
:: ============================================

:run_exercise6_3_compile
mvn compile
echo LISTO
exit /b 0
:run_exercise6_3_server1
mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.appointment.AppointmentServer
exit /b 0
:run_exercise6_3_server2
mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.medical.MedicalServer
exit /b 0
:run_exercise6_3_server3
mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.gym.GymServer
exit /b 0
:run_exercise6_3_server4
mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.recreation.RecreationServer
exit /b 0
:run_exercise6_3_client
mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.WellnessClient
exit /b 0

:menu_exercise6_3
cls
echo ============================================
echo  Exercise 6.3 - Wellness Microservices
echo ============================================
echo  Requiere 5 terminales: 4 servidores + 1 cliente
echo.
echo  1) Compilar todo
echo  2) Servidor 1 - Appointment (Terminal 1)
echo  3) Servidor 2 - Medical (Terminal 2)
echo  4) Servidor 3 - Gym (Terminal 3)
echo  5) Servidor 4 - Recreation (Terminal 4)
echo  6) Cliente (Terminal 5)
echo  0) Volver
echo ============================================
set /p op="Opcion: "
if "%op%"=="1" mvn compile & echo LISTO
if "%op%"=="2" mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.appointment.AppointmentServer
if "%op%"=="3" mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.medical.MedicalServer
if "%op%"=="4" mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.gym.GymServer
if "%op%"=="5" mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.recreation.RecreationServer
if "%op%"=="6" mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.WellnessClient
if "%op%"=="0" goto menu
goto menu_exercise6_3

:: ============================================
:: GUIDE 7.2 - Movie Gateway
:: ============================================

:run_guide7_2_compile
mvn install -DskipTests
echo LISTO
exit /b 0
:run_guide7_2_server1
mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide6_2.movie.MovieServiceServer
exit /b 0
:run_guide7_2_server2
mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide6_2.review.ReviewServiceServer
exit /b 0
:run_guide7_2_server3
mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide6_2.recommendation.RecommendationServiceServer
exit /b 0
:run_guide7_2_gateway
mvn exec:java -f src/edu/eci/arsw/guide7_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide7_2.MovieGateway
exit /b 0

:menu_guide7_2
cls
echo ============================================
echo  Guide 7.2 - Movie Gateway
echo ============================================
echo  Requiere: 4 terminales + instalar primero
echo.
echo  1) Compilar e instalar todo
echo  2) Servidor 1 - MovieService (Terminal 1)
echo  3) Servidor 2 - ReviewService (Terminal 2)
echo  4) Servidor 3 - Recommendation (Terminal 3)
echo  5) Gateway (Terminal 4)
echo  0) Volver
echo ============================================
set /p op="Opcion: "
if "%op%"=="1" mvn install -DskipTests & echo LISTO
if "%op%"=="2" mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide6_2.movie.MovieServiceServer
if "%op%"=="3" mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide6_2.review.ReviewServiceServer
if "%op%"=="4" mvn exec:java -f src/edu/eci/arsw/guide6_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide6_2.recommendation.RecommendationServiceServer
if "%op%"=="5" mvn exec:java -f src/edu/eci/arsw/guide7_2/pom.xml -Dexec.mainClass=edu.eci.arsw.guide7_2.MovieGateway
if "%op%"=="0" goto menu
goto menu_guide7_2

:: ============================================
:: EXERCISE 7.3 - Wellness Gateway
:: ============================================

:run_exercise7_3_compile
mvn install -DskipTests
echo LISTO
exit /b 0
:run_exercise7_3_server1
mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.appointment.AppointmentServer
exit /b 0
:run_exercise7_3_server2
mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.medical.MedicalServer
exit /b 0
:run_exercise7_3_server3
mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.gym.GymServer
exit /b 0
:run_exercise7_3_server4
mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.recreation.RecreationServer
exit /b 0
:run_exercise7_3_gateway
mvn exec:java -f src/edu/eci/arsw/excercise7_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise7_3.WellnessGateway
exit /b 0

:menu_exercise7_3
cls
echo ============================================
echo  Exercise 7.3 - Wellness Gateway
echo ============================================
echo  Requiere: 5 terminales + instalar primero
echo.
echo  1) Compilar e instalar todo
echo  2) Servidor 1 - Appointment (Terminal 1)
echo  3) Servidor 2 - Medical (Terminal 2)
echo  4) Servidor 3 - Gym (Terminal 3)
echo  5) Servidor 4 - Recreation (Terminal 4)
echo  6) Gateway (Terminal 5)
echo  0) Volver
echo ============================================
set /p op="Opcion: "
if "%op%"=="1" mvn install -DskipTests & echo LISTO
if "%op%"=="2" mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.appointment.AppointmentServer
if "%op%"=="3" mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.medical.MedicalServer
if "%op%"=="4" mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.gym.GymServer
if "%op%"=="5" mvn exec:java -f src/edu/eci/arsw/excercise6_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise6_3.recreation.RecreationServer
if "%op%"=="6" mvn exec:java -f src/edu/eci/arsw/excercise7_3/pom.xml -Dexec.mainClass=edu.eci.arsw.excercise7_3.WellnessGateway
if "%op%"=="0" goto menu
goto menu_exercise7_3

:: ============================================
:: EXERCISE 8 - ECICIENCIA
:: ============================================

:run_exercise8_compile
mvn compile -pl src/edu/eci/arsw/excercise8 -am
echo LISTO
exit /b 0
:run_exercise8_server1
mvn exec:java -pl src/edu/eci/arsw/excercise8 -Dexec.mainClass=edu.eci.arsw.excercise8.attendee.AttendeeServer
exit /b 0
:run_exercise8_server2
mvn exec:java -pl src/edu/eci/arsw/excercise8 -Dexec.mainClass=edu.eci.arsw.excercise8.agenda.AgendaServer
exit /b 0
:run_exercise8_server3
mvn exec:java -pl src/edu/eci/arsw/excercise8 -Dexec.mainClass=edu.eci.arsw.excercise8.workshop.WorkshopServer
exit /b 0
:run_exercise8_gateway
mvn exec:java -pl src/edu/eci/arsw/excercise8 -Dexec.mainClass=edu.eci.arsw.excercise8.gateway.EcicienciaGateway
exit /b 0

:menu_exercise8
cls
echo ============================================
echo  Exercise 8 - ECICIENCIA
echo ============================================
echo  Requiere: 4 terminales + compilar primero
echo.
echo  1) Compilar
echo  2) AttendeeService (Terminal 1, puerto 8091)
echo  3) AgendaService (Terminal 2, puerto 8092)
echo  4) WorkshopService (Terminal 3, puerto 8093)
echo  5) Gateway HTTP (Terminal 4, puerto 8090)
echo  0) Volver
echo ============================================
set /p op="Opcion: "
if "%op%"=="1" mvn compile -pl src/edu/eci/arsw/excercise8 -am & echo LISTO
if "%op%"=="2" mvn exec:java -pl src/edu/eci/arsw/excercise8 -Dexec.mainClass=edu.eci.arsw.excercise8.attendee.AttendeeServer
if "%op%"=="3" mvn exec:java -pl src/edu/eci/arsw/excercise8 -Dexec.mainClass=edu.eci.arsw.excercise8.agenda.AgendaServer
if "%op%"=="4" mvn exec:java -pl src/edu/eci/arsw/excercise8 -Dexec.mainClass=edu.eci.arsw.excercise8.workshop.WorkshopServer
if "%op%"=="5" mvn exec:java -pl src/edu/eci/arsw/excercise8 -Dexec.mainClass=edu.eci.arsw.excercise8.gateway.EcicienciaGateway
if "%op%"=="0" goto menu
goto menu_exercise8

:: ============================================
:: ERROR / FIN
:: ============================================

:error
echo.
echo Uso: run.bat [seccion] [accion]
echo.
echo Opciones disponibles desde la linea de comandos:
echo.
echo   run.bat compile                    - Compilar todos los modulos Maven
echo   run.bat install                    - Instalar todos los modulos
echo   run.bat clean                      - Limpiar y recompilar
echo   run.bat menu                       - Menu interactivo
echo.
echo Ejemplos por seccion:
echo   run.bat guide2_2 compile           - Compilar Guide 2.2
echo   run.bat guide2_2 server            - Iniciar servidor Guide 2.2
echo   run.bat guide2_2 client            - Iniciar cliente Guide 2.2
echo.
echo   run.bat guide5_2 compile           - Compilar Guide 5.2 (gRPC)
echo   run.bat guide5_2 server            - Iniciar servidor gRPC
echo   run.bat guide5_2 client            - Iniciar cliente gRPC
echo.
echo   run.bat guide6_2 server1           - Iniciar MovieService
echo   run.bat guide6_2 server2           - Iniciar ReviewService
echo   run.bat guide6_2 server3           - Iniciar RecommendationService
echo   run.bat guide6_2 client            - Iniciar cliente
echo.
echo   run.bat exercise8 compile          - Compilar Exercise 8
echo   run.bat exercise8 server1          - Iniciar AttendeeService
echo   run.bat exercise8 server2          - Iniciar AgendaService
echo   run.bat exercise8 server3          - Iniciar WorkshopService
echo   run.bat exercise8 gateway          - Iniciar Gateway HTTP
echo.
echo Para uso interactivo, ejecute 'run.bat' sin parametros.
echo.
echo NOTA: Al usar argumentos, los comandos se ejecutan en la
echo terminal actual (no abren ventanas nuevas).

:fin
echo.
exit /b 0
