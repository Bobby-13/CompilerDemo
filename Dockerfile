## Use Ubuntu 18.04 as the base image
#FROM ubuntu:18.04
#
## Configure any pending or incomplete package installations
#RUN dpkg --configure -a
#
## Set environment variables
#ENV DEBIAN_FRONTEND noninteractive
#
## Update the package lists and install various packages
#RUN apt-get update && \
#    apt-get -y install \
#    gcc g++ make \
#    default-jre default-jdk \
#    python3 python3-pip \
#    nodejs npm \
#    golang-go && \
#    rm -rf /var/lib/apt/lists/*
#
## Set up Node.js using NVM (Node Version Manager)
#ENV NODE_VERSION=16.13.2
#RUN curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.38.0/install.sh | bash && \
#    export NVM_DIR="/root/.nvm" && \
#    [ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh" && \
#    [ -s "$NVM_DIR/bash_completion" ] && \. "$NVM_DIR/bash_completion" && \
#    nvm install ${NODE_VERSION} && \
#    nvm use v${NODE_VERSION} && \
#    nvm alias default v${NODE_VERSION}
#
## Set environment variables for Node.js
#ENV NVM_DIR=/root/.nvm
#ENV PATH="/root/.nvm/versions/node/v${NODE_VERSION}/bin/:${PATH}"
#
## Set up GOPATH for Go
#ENV GOPATH /go
#ENV PATH $GOPATH/bin:/usr/local/go/bin:$PATH
#
## Set the working directory to /app
#
#
## Expose ports if needed (e.g., for a web server)
# EXPOSE 8080
#
## Copy the contents of the current directory to the /app directory in the image
#COPY target/CompilerDemo-0.0.1-SNAPSHOT.jar /app
#WORKDIR /app
## Optionally, you can install additional dependencies for your specific Java, C++, C, Python, and JavaScript projects
#
## Command to run when the container starts (modify as needed)
#CMD ["bash"]
##CMD ["java","-jar","SimpleWebAppBackend-0.0.1-SNAPSHOT.jar"]


FROM ubuntu:18.04

RUN dpkg --configure -a

ENV PYTHON_VERSION 3.7.7
ENV PYTHON_PIP_VERSION 20.1
ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update && apt-get -y install openjdk-17-jdk

RUN apt-get update
RUN apt-get -y install gcc mono-mcs golang-go \
    default-jre default-jdk nodejs npm \
    python3-pip python3 curl && \
    rm -rf /var/lib/apt/lists/*

ENV NODE_VERSION=16.13.2
RUN curl https://raw.githubusercontent.com/nvm-sh/nvm/v0.38.0/install.sh | bash
ENV NVM_DIR=/root/.nvm
RUN . "$NVM_DIR/nvm.sh" && nvm install ${NODE_VERSION}
RUN . "$NVM_DIR/nvm.sh" && nvm use v${NODE_VERSION}
RUN . "$NVM_DIR/nvm.sh" && nvm alias default v${NODE_VERSION}
ENV PATH="/root/.nvm/versions/node/v${NODE_VERSION}/bin/:${PATH}"
# RUN nvm install 16.13.2

#COPY . /app
#WORKDIR /app
#RUN npm install

COPY target/CompilerDemo-0.0.1-SNAPSHOT.jar /app/
WORKDIR /app


EXPOSE 8080
#CMD ["npm", "start"]
CMD ["java","-jar","CompilerDemo-0.0.1-SNAPSHOT.jar"]


