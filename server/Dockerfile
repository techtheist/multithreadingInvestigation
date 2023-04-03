FROM gradle:8.0.2-jdk17

WORKDIR /server

# Copy the Gradle build files
COPY ../build.gradle.kts .
COPY ../settings.gradle.kts .
COPY ../gradlew .
COPY ../gradle ./gradle/

# Download and cache the dependencies
RUN ./gradlew build || return 0

# Copy the application source code
COPY ../src ./src/

# Build the application
RUN ./gradlew build

# Set the command to run the application
CMD ["java", "-jar", "/server/build/libs/multithreadingInvestigation-0.1.jar"]