class SimConfigLoader {
    constructor(buildingsFile, roadsFile, sensors, pollutionJsonData, dimensions) {
        this.buildingsFile = buildingsFile;
        this.roadsFile = roadsFile;
        this.sensors = sensors;
        this.pollutionJsonData = pollutionJsonData;
        this.dimensions = dimensions;
    }

    static async loadFromJsonFile(fileName) {
        const response = await fetch(fileName);
        const data = await response.json();

        return new SimConfigLoader(
            data['buildings-file'],
            data['roads-file'],
            data['sensors'],
            data['pollution-data'],
            data['dimensions']
        );
    }
}

export { SimConfigLoader }