@echo off
title Lab03 ARSW - Clean
chcp 65001 >nul

echo ============================================
echo  Lab 03 - ARSW - Limpieza Segura
echo ============================================
echo.
echo  PRECAUCION: No use 'mvn clean' solo, porque
echo  borra las clases generadas por protobuf y
echo  VSCode pierde las referencias en el editor.
echo.
echo  El comando 'mvn compile' es mas seguro porque
echo  no borra target/ y regenera solo lo necesario.
echo.
echo ============================================
echo  1) Compilar todo (seguro, recomendado)
echo  2) Limpiar y recompilar (si hay problemas)
echo  3) Compilar solo Exercise 8
echo  4) Instalar todos los modulos (para gateways)
echo  0) Salir
echo ============================================
echo.

set /p opcion="Seleccione una opcion: "

if "%opcion%"=="1" goto compile
if "%opcion%"=="2" goto clean_compile
if "%opcion%"=="3" goto excercise8
if "%opcion%"=="4" goto install
if "%opcion%"=="0" goto salir
goto error

:compile
echo.
echo Compilando todos los modulos...
call mvn compile
if %errorlevel%==0 (echo. & echo LISTO - Compilacion exitosa) else (echo. & echo ERROR - Revise los mensajes anteriores)
goto fin

:clean_compile
echo.
echo ============================================
echo  ATENCION: VSCode mostrara errores
echo  temporalmente en clases que usan protobuf
echo  (guide5_2, exercise5_3, guide6_2, etc.).
echo ============================================
echo.
echo  PASOS para solucionar los errores en VSCode:
echo   1) Espere a que termine la compilacion
echo   2) Cierre y vuelva a abrir el archivo con error
echo      (O mejor: Ctrl+Shift+P -^> Java: Reload Projects)
echo.
echo  NOTA: La opcion 1 (compilar sin limpiar) NO
echo  genera este problema. Use opcion 1 a menos
echo  que tenga problemas de compilacion reales.
echo.
call mvn clean compile
if %errorlevel%==0 (echo. & echo LISTO - Limpieza y compilacion exitosa) else (echo. & echo ERROR - Revise los mensajes anteriores)

echo.
echo ============================================
echo  Si VSCode aun muestra errores, haga:
echo    Ctrl+Shift+P -^> Java: Reload Projects
echo ============================================
goto fin

:excercise8
echo.
echo Compilando Exercise 8 (ECICIENCIA)...
call mvn compile -pl src/edu/eci/arsw/excercise8 -am
if %errorlevel%==0 (echo. & echo LISTO - Exercise 8 compilado) else (echo. & echo ERROR - Revise los mensajes anteriores)
goto fin

:install
echo.
echo Instalando todos los modulos (necesario para gateways)...
call mvn install -DskipTests
if %errorlevel%==0 (echo. & echo LISTO - Modulos instalados) else (echo. & echo ERROR - Revise los mensajes anteriores)
goto fin

:error
echo Opcion no valida.

:salir
echo.
echo Saliendo...
exit /b 0

:fin
echo.
pause
