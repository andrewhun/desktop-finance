FROM cimg/openjdk:21.0.0-node AS builder

USER root
RUN apt-get update -y && apt-get install --no-install-recommends -y \
      scons

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

FROM cimg/openjdk:21.0.0-node AS runner

COPY --from=builder /nsis/nsis/ /nsis/

WORKDIR /installer

# LABEL com.circleci.preserve-entrypoint=true

# ENTRYPOINT [ "/nsis/Bin/makensis", "-V4" ]
USER 1001