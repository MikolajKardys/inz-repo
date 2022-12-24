import { Scene } from 'three'
import { PerspectiveCamera } from 'three'
import { WebGLRenderer } from 'three'

import { OrbitControls } from 'orbit-controls'

import { SimConfigLoader } from "./SimConfigLoader.js";
import { DataViewLoader } from "./DataViewLoader.js";
import { SimpleObstacles } from "./SimpleObstacles.js";
import { SimplePollution } from "./SimplePollution.js";
import { SimpleRoads } from "./SimpleRoads.js";
import { WireFrame } from "./WireFrame.js";

let scene
let camera
let renderer
let controls
let recorder

let x_dim = 0;
let y_dim = 0;
let z_dim = 0;

let visibleRangeX = [0, 0];
let visibleRangeY = [0, 0];
let visibleRangeZ = [0, 0];

let scaleFactor = 1;


document.getElementById('frame').addEventListener('change', reloadAll)

document.getElementById('next-frame-button').onclick = async function () {
    let currentFrame = document.getElementById('frame')
    currentFrame.value = parseInt(currentFrame.value) + 1;
    await reloadAll()
}

document.getElementById('previous-frame-button').onclick = async function () {
    let currentFrame = document.getElementById('frame')
    currentFrame.value = parseInt(currentFrame.value) - 1;
    await reloadAll()
}

document.getElementById('record-button').onclick = function () {
    startRecording()
}

document.getElementById('reload-size').onclick = async function () {
    reloadAll()
}

async function reloadAll(){
    // Load all configuration from json
    let simConfigLoader = await SimConfigLoader.loadFromJsonFile("./data/sim-config.json");

    // Set scene dimensions
    x_dim = simConfigLoader.dimensions.width;
    y_dim = simConfigLoader.dimensions.height;
    z_dim = simConfigLoader.dimensions.length;

    if (scene == null)
        initScene();

    // Load set ranges and scale
    visibleRangeX[0] = parseInt(document.getElementById('xBegin').value);
    visibleRangeX[1] = parseInt(document.getElementById('xEnd').value);
    visibleRangeZ[0] = parseInt(document.getElementById('zBegin').value);
    visibleRangeZ[1] = parseInt(document.getElementById('zEnd').value);

    visibleRangeY[0] = 0
    visibleRangeY[1] = y_dim - 1

    visibleRangeX[0] = Math.max(0, visibleRangeX[0]);
    visibleRangeX[1] = Math.min(x_dim - 1, visibleRangeX[1]);
    visibleRangeZ[0] = Math.max(0, visibleRangeZ[0]);
    visibleRangeZ[1] = Math.min(z_dim - 1, visibleRangeZ[1]);

    document.getElementById('xBegin').value = visibleRangeX[0]
    document.getElementById('xEnd').value = visibleRangeX[1]
    document.getElementById('zBegin').value = visibleRangeZ[0]
    document.getElementById('zEnd').value = visibleRangeZ[1]

    scaleFactor = parseFloat(document.getElementById('scale').value);


    // Add wireframe for reference
    WireFrame.addWireFrame()


    // Load buildings and roads
    if (simConfigLoader.buildingsFile !== ""){
        DataViewLoader.loadDataFrameFromDatFile(simConfigLoader.buildingsFile).then((dataView) => {
            SimpleObstacles.loadObstaclesFromDataView(dataView)
        });
    }

    if (simConfigLoader.roadsFile !== ""){
        DataViewLoader.loadDataFrameFromDatFile(simConfigLoader.roadsFile).then((dataView) => {
            SimpleRoads.loadRoadsFromDataView(dataView, simConfigLoader.sensors)
        });
    }


    // Set values for pollution components
    document.getElementById('total-frames').innerHTML = simConfigLoader.pollutionJsonData['iteration-number'];

    let currentFrame = document.getElementById('frame');
    let previousFrameButton = document.getElementById('previous-frame-button');
    let nextFrameButton = document.getElementById('next-frame-button');

    const min = simConfigLoader.pollutionJsonData['iteration-number'] > 0 ? 1 : 0;
    const max = simConfigLoader.pollutionJsonData['iteration-number'];

    let currentValue = currentFrame.value;

    previousFrameButton.disabled = false;
    nextFrameButton.disabled = false;

    if (currentValue >= max) {
        currentValue = max;
        nextFrameButton.disabled = true;
    }
    if (currentValue <= min) {
        currentValue = min;
        previousFrameButton.disabled = true;
    }

    currentFrame.min = min;
    currentFrame.max = max;
    currentFrame.value = currentValue;


    // Load pollution
    if (parseInt(currentValue) > 0){
        const currentIndex = parseInt(currentValue) - 1
        const pollutionFile = simConfigLoader.pollutionJsonData['pollution-file-prefix'] + currentIndex + ".dat"

        const dataView = await DataViewLoader.loadDataFrameFromDatFile(pollutionFile)
        SimplePollution.loadPollutionFromDataView(dataView)
    }
}

function initScene() {
    scene = new Scene()

    camera = new PerspectiveCamera(75, 2, 1, 2000)

    camera.position.x = 0
    camera.position.y = 0
    camera.position.z = z_dim

    camera.lookAt(0, 0, 0)

    renderer = new WebGLRenderer()
    renderer.setSize(1500, 750)
    renderer.setClearColor(0xdddddd, 0.3);

    document.getElementById('canvas').appendChild(renderer.domElement)

    controls = new OrbitControls(camera, renderer.domElement);

    recorder = new CCapture({
        format: 'gif'
    });

    //const geometry = new SphereGeometry(3, 50, 50, 0, Math.PI * 2, 0, Math.PI * 2);  //Sensor placeholder
    //const material = new MeshBasicMaterial( { color: 0xff0000 } );
    //const sphere = new Mesh( geometry, material );
    //sphere.position.set(z_dim / 2 - 115, -12.5, -x_dim / 2 + 160)
    //scene.add( sphere );

    animate()
}

function render(){
    renderer.render( scene, camera );
}

function animate(){
    requestAnimationFrame( animate );

    controls.update();

    renderer.render( scene, camera );
}

async function startRecording(){
    let currentFrame = document.getElementById('frame');
    currentFrame.value = currentFrame.min

    recorder.start();

    while (parseInt(currentFrame.value) <= currentFrame.max){
        await reloadAll()

        render()
        recorder.capture(renderer.domElement)

        currentFrame.value = parseInt(currentFrame.value) + 1
    }

    recorder.stop()
    recorder.save()
}

reloadAll()

export { scene, initScene, x_dim, y_dim, z_dim, visibleRangeX, visibleRangeY, visibleRangeZ, scaleFactor, reloadAll }