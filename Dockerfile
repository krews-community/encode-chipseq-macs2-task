FROM openjdk:8-jre-slim as base
RUN apt-get update && apt-get install -y \
    build-essential \
    wget \
    python-pip \
    libbz2-dev \
    liblzma-dev \
    zlib1g-dev \
    libssl-dev \
    libpng-dev \
    python python-dev libstdc++ \
    mysql-server \
    default-libmysqlclient-dev \
    git && \
    git clone https://github.com/weng-lab/kent && \
        cd kent/src/lib && make CFLAGS=-DLIBUUID_NOT_PRESENT && cd ../jkOwnLib && make && cd ../htslib && make && \
        mkdir -p /root/bin/x86_64 && cd ../utils/bedClip && make && cd ../bedGraphToBigWig && make && \
        cd ../bedToBigBed && make && \
        cd / && rm -rf kent && mv /root/bin/x86_64/* /bin && \
        git clone https://github.com/arq5x/bedtools2 && cd bedtools2 && make && \
        mv bin/* /bin && cd .. && rm -rf bedtools2 && \
        pip install --no-cache-dir numpy && \
        pip install --no-cache-dir macs2 && \
    apt-get purge --auto-remove -y  --allow-remove-essential git build-essential python-pip python-dev git bash libpng-dev

FROM openjdk:8-jdk-alpine as build
COPY . /src
WORKDIR /src

RUN ./gradlew clean shadowJar

FROM base
RUN mkdir /app
COPY --from=build /src/build/chipseq-macs2-*.jar /app/chipseq.jar