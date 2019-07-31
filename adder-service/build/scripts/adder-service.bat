@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  adder-service startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and ADDER_SERVICE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\adder-service-1.0.0-SNAPSHOT.jar;%APP_HOME%\lib\vertx-junit5-3.7.1.jar;%APP_HOME%\lib\vertx-kafka-client-3.7.1.jar;%APP_HOME%\lib\vertx-lang-kotlin-3.7.1.jar;%APP_HOME%\lib\junit-jupiter-engine-5.4.1.jar;%APP_HOME%\lib\vertx-rx-java-3.7.1.jar;%APP_HOME%\lib\vertx-rx-java2-3.7.1.jar;%APP_HOME%\lib\vertx-rx-gen-3.7.1.jar;%APP_HOME%\lib\vertx-core-3.7.1.jar;%APP_HOME%\lib\kafka_2.12-2.1.0.jar;%APP_HOME%\lib\kafka-clients-2.1.0.jar;%APP_HOME%\lib\slf4j-log4j12-1.7.21.jar;%APP_HOME%\lib\metrics-core-2.2.0.jar;%APP_HOME%\lib\scala-logging_2.12-3.9.0.jar;%APP_HOME%\lib\zkclient-0.10.jar;%APP_HOME%\lib\zookeeper-3.4.13.jar;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\log4j-1.2.17.jar;%APP_HOME%\lib\kotlin-stdlib-jdk8-1.3.20.jar;%APP_HOME%\lib\junit-platform-engine-1.4.1.jar;%APP_HOME%\lib\junit-jupiter-api-5.4.1.jar;%APP_HOME%\lib\junit-platform-commons-1.4.1.jar;%APP_HOME%\lib\apiguardian-api-1.0.0.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.34.Final.jar;%APP_HOME%\lib\netty-codec-http2-4.1.34.Final.jar;%APP_HOME%\lib\netty-codec-http-4.1.34.Final.jar;%APP_HOME%\lib\netty-handler-4.1.34.Final.jar;%APP_HOME%\lib\netty-resolver-dns-4.1.34.Final.jar;%APP_HOME%\lib\netty-codec-socks-4.1.34.Final.jar;%APP_HOME%\lib\netty-codec-dns-4.1.34.Final.jar;%APP_HOME%\lib\netty-codec-4.1.34.Final.jar;%APP_HOME%\lib\netty-transport-4.1.34.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.34.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.34.Final.jar;%APP_HOME%\lib\netty-common-4.1.34.Final.jar;%APP_HOME%\lib\vertx-codegen-3.7.1.jar;%APP_HOME%\lib\jackson-databind-2.9.8.jar;%APP_HOME%\lib\jackson-core-2.9.8.jar;%APP_HOME%\lib\rxjava-1.3.8.jar;%APP_HOME%\lib\rxjava-2.2.4.jar;%APP_HOME%\lib\reactive-streams-1.0.2.jar;%APP_HOME%\lib\zstd-jni-1.3.5-4.jar;%APP_HOME%\lib\lz4-java-1.5.0.jar;%APP_HOME%\lib\snappy-java-1.1.7.2.jar;%APP_HOME%\lib\jopt-simple-5.0.4.jar;%APP_HOME%\lib\scala-reflect-2.12.7.jar;%APP_HOME%\lib\kotlin-stdlib-jdk7-1.3.20.jar;%APP_HOME%\lib\kotlin-stdlib-1.3.20.jar;%APP_HOME%\lib\opentest4j-1.1.1.jar;%APP_HOME%\lib\jackson-annotations-2.9.0.jar;%APP_HOME%\lib\audience-annotations-0.5.0.jar;%APP_HOME%\lib\kotlin-stdlib-common-1.3.20.jar;%APP_HOME%\lib\annotations-13.0.jar;%APP_HOME%\lib\mvel2-2.3.1.Final.jar

@rem Execute adder-service
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %ADDER_SERVICE_OPTS%  -classpath "%CLASSPATH%" io.vertx.core.Launcher %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable ADDER_SERVICE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%ADDER_SERVICE_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
