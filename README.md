% author: Mikołaj Kardyś

# PM10 dispersion simulator

This is the implementation of the pollution PM10 dispersion simulator made as a part of an Engineering thesis in the years 2022-2023.

## Recommended installation

This application can be run directly on the user's PC. However, in its current state, it requires Java17, Python3 and Node to be installed before the usage. Also, Python and Node requirements must be met for each adapter before using it. Therefore, the recommended method of installation for this project is via Docker. After cloning the repository, first, build the provided Dockerfile:

```bash
docker build -t inz-repo .
```

Next, use the following command to launch the Docker container:

```bash
docker run -it --name inz-repo-container -v <path-to-repository>:/inz-repo -p 3000:3000 inz-repo
```

After these steps, no further configuration is required and the freshly opened terminal is ready for execution of any of the below scenarios.

## Application usage

### Console interface

The application is meant to be used via the provided terminal interface. The recommended way of doing so would be through the provided *.jar* file, which contains the latest stable version of the application:

```bash
java -jar inz-repo-jar-1.0.0.jar <parameters>
```

### Adapters

The first part of the application are the auxiliary services, required for fetching most of the necessary data as well as the 3D visualization of the result. To launch the adapters to any of those programs, use the following command:

```bash
java -jar inz-repo-jar-1.0.0.jar run-adapter <adapter-name> <adapter-parameters>
```

Currently, there are four adapters that can be used to retrieve data, each with its own purpose:

- fetch-buildings - retrieving data regarding streets and buildings in the specified area
- calculate-emitters - estimation of the road emissions based on the map of the roads
- calculate-wind - calculation of the wind flow in the area based on the map of the buildings and other factors
- run-visualization - 3D visualization of the simulation results

For the first three adapters, parameters are to be passed in the format: ***key=value***. By using only an *h* parameter, a list of all possible options can be viewed. As for the visualization, it doesn't require any additional arguments. Whenever a new run of the main simulation is started, it will be automatically configured. When running this adapter, a local Node server will start on port 3000 which can be opened via a browser on the host computer. There, one can view the state of the selected area at several points during the simulation.


### Pollution dispersion engine

This part of the application can be executed via the other main command:

```bash
java -jar inz-repo-jar-1.0.0.jar run-simulation
```

This command takes no parameters and relies entirely on the contents of a *config.json* file which should be placed in the same directory as the *.jar* file before its execution. A sample *config.json* file with all the necessary data files has been provided inside the *example-data* directory. Any config file must contain the following information:

- "iterations" - the number of total iterations to be executed; each one corresponds exactly to 0.2 seconds
- "iterations-save" - iteration interval between every two frames visible inside the visualization
- "dimensions" - dimensions of the simulated area, "block-size" corresponds to the density of the selected grid (in the example, '2' correspond to a grid of cubes 2m x 2m x 2m).
- "data-files" - paths to files containing data from the auxiliary services
- "updates" -  contains two timelines, one for emitters and one for walls, which dictate the pollution concentration for both of them throughout the simulation. A point in a timeline consists of an iteration number and the concentration (in mg) at this iteration. Between those points, the concentration will rise or fall linearly (or remain the same) until it smoothly reaches the next point.
- "adapters" - paths required for launching adapters; their values are present here mostly for reasons related to the development and in most cases should not be changed
