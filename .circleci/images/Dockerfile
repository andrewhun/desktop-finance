FROM cimg/openjdk:21.0.0-node AS builder

USER root
RUN apt-get update -y && apt-get install --no-install-recommends -y scons && \
    apt-get install -y dos2unix

ENV NSIS_VERSION=3.10

WORKDIR /nsis

RUN wget -q -O nsis-${NSIS_VERSION}-src.tar.bz2 "http://downloads.sourceforge.net/project/nsis/NSIS 3/${NSIS_VERSION}/nsis-${NSIS_VERSION}-src.tar.bz2?use_mirror=autoselect" && \
    wget -q -O nsis.zip "http://downloads.sourceforge.net/project/nsis/NSIS 3/${NSIS_VERSION}/nsis-${NSIS_VERSION}.zip?use_mirror=autoselect" && \
    tar -jxf nsis-${NSIS_VERSION}-src.tar.bz2 && \
    unzip nsis.zip -d /nsis

WORKDIR /nsis/nsis-$NSIS_VERSION-src

RUN scons NSIS_MAX_STRLEN=8192 NSIS_CONFIG_CONST_DATA_PATH=no SKIPSTUBS=all SKIPPLUGINS=all SKIPUTILS=all SKIPMISC=all PREFIX=/nsis/nsis-$NSIS_VERSION install-compiler && \
    mv /nsis/nsis-$NSIS_VERSION/makensis /nsis/nsis-$NSIS_VERSION/Bin/ #no idea why this is necessary, it looks for everything in ../

RUN mv /nsis/nsis-$NSIS_VERSION /nsis/nsis

ENV LAUNCH4J_VERSION=3.50

WORKDIR /launch4j

RUN wget "https://sourceforge.net/projects/launch4j/files/launch4j-3/${LAUNCH4J_VERSION}/launch4j-${LAUNCH4J_VERSION}-linux-x64.tgz" && \
    tar -xf "launch4j-${LAUNCH4J_VERSION}-linux-x64.tgz"

WORKDIR ./launch4j

RUN dos2unix ./launch4j && \
    apt-get remove --purge -y dos2unix

FROM cimg/openjdk:21.0.0-node AS runner

WORKDIR /installer

COPY --from=builder /nsis/nsis/ /installer/nsis/

COPY --from=builder /launch4j/launch4j /installer/launch4j
# Source: https://stackoverflow.com/questions/56579468/list-directory-var-lib-apt-lists-partial-is-missing-acquire-20-not-a-direc
USER 1001