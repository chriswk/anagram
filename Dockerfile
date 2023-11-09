FROM gcr.io/distroless/java17-debian12

WORKDIR /opt/application

COPY ./build/libs/anagram-0.0.3-SNAPSHOT.jar .

CMD ["/opt/application/anagram-0.0.3-SNAPSHOT.jar"]
