# See here for image contents: https://github.com/microsoft/vscode-dev-containers/tree/v0.245.2/containers/cpp/.devcontainer/base.Dockerfile

# [Choice] Debian OS version (use bullseye on local arm64/Apple Silicon): buster, bullseye
ARG VARIANT="bullseye"
FROM mcr.microsoft.com/vscode/devcontainers/cpp:0-${VARIANT}

# [Optional] Uncomment this section to install additional OS packages.
# RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
#     && apt-get -y install --no-install-recommends <your-package-list-here>

# Install Android SDK
USER root
RUN apt-get update && apt-get install -y openjdk-17-jdk && apt-get install -y wget && apt-get install -y unzip
ENV ANDROID_SDK_ROOT /opt/android/sdk
ENV PATH $PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools
RUN mkdir -p /opt/android/sdk
RUN wget 'https://dl.google.com/android/repository/commandlinetools-linux-8512546_latest.zip' -O /tmp/cmdline-tools.zip
RUN unzip /tmp/cmdline-tools.zip -d /opt/android/sdk/cmdline-tools && mv /opt/android/sdk/cmdline-tools/cmdline-tools /opt/android/sdk/cmdline-tools/latest
RUN yes | sdkmanager --licenses && sdkmanager "platform-tools" "platforms;android-36" "build-tools;34.0.0"
RUN chown -R vscode:vscode /opt/android
USER vscode
