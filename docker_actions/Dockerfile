# Container image
FROM maven:3.6.3-openjdk-16

# Copy source code to image
COPY /home/runner/work/defensepoint-xxebenchmark/defensepoint-xxebenchmark/src /home/app/src
COPY /home/runner/work/defensepoint-xxebenchmark/defensepoint-xxebenchmark/pom.xml /home/app

VOLUME ["/volumetest"]

# Run build application and copy result to mounted (host) direcotry
RUN mvn -f /home/app/pom.xml clean package && cp ./target/xxebenchmark-*.jar /home/java_app
