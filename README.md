# diet-app

## Production Build:
1. Build production jar: mvn clean package -Pproduction
2. Build docker image nad push to docker hub: docker buildx build --platform linux/amd64,linux/arm64 -t image_tag --push .
