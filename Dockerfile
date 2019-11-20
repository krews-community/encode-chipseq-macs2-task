FROM openjdk:8-jre-slim as base
RUN apt-get update && apt-get install -y \
    build-essential \
    wget \
    python3-pip \
    libbz2-dev \
    liblzma-dev \
    zlib1g-dev \
    libssl-dev \
    libpng-dev \
	python3 python3-dev \
bedtools \
	git && \
	wget http://hgdownload.soe.ucsc.edu/admin/exe/linux.x86_64/bigWigCorrelate -O /bin/bigWigCorrelate && chmod +x /bin/bigWigCorrelate && \
	wget http://hgdownload.soe.ucsc.edu/admin/exe/linux.x86_64/bedClip -O /bin/bedClip && chmod +x /bin/bedClip && \
	wget http://hgdownload.soe.ucsc.edu/admin/exe/linux.x86_64/bedGraphToBigWig -O /bin/bedGraphToBigWig && chmod +x /bin/bedGraphToBigWig && \
	wget http://hgdownload.soe.ucsc.edu/admin/exe/linux.x86_64/bedToBigBed -O /bin/bedToBigBed && chmod +x /bin/bedToBigBed && \
        python3 -m pip install --no-cache-dir numpy && \
        python3 -m pip install --no-cache-dir macs2 && \
    apt-get purge --auto-remove -y  --allow-remove-essential git build-essential python3-pip python3-dev git

FROM openjdk:8-jdk-alpine as build
COPY . /src
WORKDIR /src

RUN ./gradlew clean shadowJar

FROM base
RUN mkdir /app
COPY --from=build /src/build/chipseq-macs2-*.jar /app/chipseq.jar
