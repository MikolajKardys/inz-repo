class DataViewLoader {
    static async loadDataFrameFromDatFile(fileName) {
        const response = await fetch(fileName);
        const reader = response.body.getReader();

        let array = new Uint8Array(0);
        while (true) {
            const {value, done} = await reader.read();

            if (done) {
                return new DataView(array.buffer);
            }

            let newArray = new Uint8Array(array.byteLength + value.byteLength)
            newArray.set(array, 0);
            newArray.set(value, array.byteLength);
            array = newArray
        }
    }
}

export { DataViewLoader }