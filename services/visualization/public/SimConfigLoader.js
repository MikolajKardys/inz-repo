class SimConfigLoader {
    constructor(buildingsFile, pollutionJsonData) {
        this.buildingsFile = buildingsFile;
        this.pollutionJsonData = pollutionJsonData;
    }

    static async loadFromJsonFile(fileName) {
        const response = await fetch(fileName);
        const data = await response.json();

        return new SimConfigLoader(data['buildings-file'], data['pollution-data']);
    }
}

export { SimConfigLoader }