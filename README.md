# PM10 dispersion simulator

This is the implementation of the pollution PM10 dispersion simulator made as a part of an Engineering thesis in the years 2022-2023.

## Recommended installation

The most recommended method of installation for this project is via Docker. After cloning the repository, first, build the provided Dockerfile:

```bash
docker build -t inz-repo .
```

Next, use the following command to launch the Docker container:

```bash
docker run -it --name inz-repo-container -v <path-to-repository>:/inz-repo -p 3000:3000 inz-repo
```

This step is not crucial to the usage of the main simulation. However, if skipped, manual installation of python requirements and node modules will be required before using any adapters. Other than that, this choice has no impact on the rest of the instructions presented below.

## Application usage

### Console interface

The application is meant to be used via the provided simple terminal interface. The recommended way of doing so would be through the provided *.jar* file, which contains the latest stable version of the application:

```bash
java -jar inz-repo-jar-1.0.0.jar <parameters>
```

### Adapters

The first part of the application is the auxiliary services, required for fetching most of the necessary data as well as the 3D visualization of the result. To launch the adapters to any of those programs, use the following command:

```bash
java -jar inz-repo-jar-1.0.0.jar run-adapter <adapter-name> <parameters>
```

Currently, there are four adapters that can be used to retrieve data, each with its own purpose:

- fetch-buildings - retrieving data regarding streets and buildings in the specified area
- calculate-emitters - estimation of the road emissions based on the map of the roads
- calculate-wind - calculation of the wind flow in the area based on the map of the buildings and other factors
- run-visualization - 3D visualization of the simulation results

For the first three adapters, parameters are to be passed in the format: ***key=value***. By using only an *h* parameter, a list of all possible options can be viewed. As for the visualization, it doesn't require any additional arguments. Whenever a new run of the main simulation will be started, it will be automatically configured. When running this adapter, a local website will open on port 3000 which can be opened via a browser. There, one can view the state of the selected area at several points during the simulation.


### Pollution dispersion engine

This part of the application can be executed via the other main command:

```bash
java -jar inz-repo-jar-1.0.0.jar run-simulation
```

This command takes no parameters and relies entirely on the contents of a *config.json* which should be placed in the same directory as the *.jar* file before its execution. A sample *config.json* file with all the necessary data files has been provided inside the *example-data* directory. Any config file must consist of the following data:

- "iterations" - the number of total iterations to be executed; each one corresponds exactly to 0.2 seconds
- "iterations-save" - iteration interval between every two frames visible inside the visualization
- "dimensions" - dimensions of the simulated area, "block-size" corresponds to the density of the selected grid (in the example: 2m x 2m x 2m).
- "data-files" - paths to files containing data from data adapters
- "updates" -  contains two timelines, one for emitters and one for walls, which dictate the pollution concentration for both of them throughout the simulation. A point in a timeline consists of an iteration number and the concentration (in mg) at this iteration. Between those points, the concentration will rise or fall linearly (or remain the same) until it smoothly reaches the next point.
- "adapters" - paths required for launching adapters; their values are present here mostly for reasons related to the development and in most cases should not be changed
