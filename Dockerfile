FROM timbru31/java-node

RUN apt-get update && apt-get upgrade -y
RUN apt-get install -y python3-pip

WORKDIR /inz-repo
EXPOSE 3000

ENTRYPOINT python3 -m pip install -r ./services/building_fetcher/requirements.txt && python3 -m pip install -r ./services/pollution_emitters/requirements.txt && python3 -m pip install -r ./services/wind_calculator/requirements.txt && cd ./services/visualization && npm install && cd ../.. && /bin/bash