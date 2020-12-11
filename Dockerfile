# Container image
FROM maven:3.6.3-openjdk-16

# Copy source code to image
COPY src /home/app/src
COPY pom.xml /home/app

VOLUME ["/volumetest"]

# Run build application and copy result to mounted (host) direcotry
RUN mvn -f /home/app/pom.xml clean package && cp /home/app/target/xxebenchmark-*.jar /home/java_app
