FROM ubuntu:16.04
FROM node:12

RUN npm i -g violat
RUN apt-get update -y
RUN apt-get install -y build-essential
RUN apt-get install -y git
RUN apt-get install -y ant

RUN apt-get install -y maven

RUN apt-get update && apt-get install -y unzip
WORKDIR /gradle
RUN curl -L https://services.gradle.org/distributions/gradle-6.5.1-bin.zip -o gradle-6.5.1-bin.zip
RUN unzip gradle-6.5.1-bin.zip
ENV GRADLE_HOME=/gradle/gradle-6.5.1
ENV PATH=$PATH:$GRADLE_HOME/bin

# Download java 8(!)
RUN apt-get install -y openjdk-8-jdk

# Download Java Pathfinder(jpf)
RUN mkdir /usr/lib/JPF
WORKDIR /usr/lib/JPF
RUN git clone https://github.com/javapathfinder/jpf-core

# Setup jpf env
RUN mkdir /root/.jpf
RUN echo 'jpf-core = /usr/lib/JPF/jpf-core' >> /root/.jpf/site.properties
RUN echo 'extensions=${jpf-core}' >> /root/.jpf/site.properties

# Build jpf
WORKDIR /usr/lib/JPF/jpf-core
#RUN git checkout java-8
#RUN ant
RUN ./gradlew buildJars

# Setup path
ENV JPF_HOME=/usr/lib/JPF
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/
ENV PATH=$PATH:/usr/lib/JPF/jpf-core/bin

# Copy the code in a code directory
RUN mkdir /usr/code
WORKDIR /usr/code
RUN git clone https://github.com/alaukiknpant/intellijViolatPlugin.git
RUN git clone https://github.com/alaukiknpant/linearizabilityTests.git
