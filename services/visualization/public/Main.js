import { Scene } from 'three'
import { PerspectiveCamera } from 'three'
import { WebGLRenderer } from 'three'

import { OrbitControls } from 'orbit-controls'

import { SimConfigLoader } from "./SimConfigLoader.js";
import { DataViewLoader } from "./DataViewLoader.js";
import { SimpleObstacles } from "./SimpleObstacles.js";
import {SimplePollution} from "./SimplePollution.js";

let scene
let camera
let renderer
let controls
let recorder

let simConfigLoader = await SimConfigLoader.loadFromJsonFile("./data/sim-config.json")

document.getElementById('total-frames').innerHTML = simConfigLoader.pollutionJsonData['iteration-number'];

let currentFrame = document.getElementById('frame');
let previousFrameButton = document.getElementById('previous-frame-button');
let nextFrameButton = document.getElementById('next-frame-button');

currentFrame.min = simConfigLoader.pollutionJsonData['iteration-number'] > 0 ? 1 : 0;
currentFrame.max = simConfigLoader.pollutionJsonData['iteration-number'];

currentFrame.value = currentFrame.min;

async function changeFrame(_) {
    let currentValue = currentFrame.value;

    const min = simConfigLoader.pollutionJsonData['iteration-number'] > 0 ? 1 : 0;
    const max = simConfigLoader.pollutionJsonData['iteration-number'];

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

    currentFrame.value = currentValue;

    if (parseInt(currentValue) > 0){
        const pollutionFile = simConfigLoader.pollutionJsonData['pollution-files'][parseInt(currentValue) - 1]

        const dataView = await DataViewLoader.loadDataFrameFromDatFile(pollutionFile)
        SimplePollution.loadPollutionFromDataView(dataView)
    }
}

currentFrame.addEventListener('change', changeFrame)

document.getElementById('next-frame-button').onclick = function () {
    currentFrame.value = parseInt(currentFrame.value) + 1;
    currentFrame.dispatchEvent(new Event('change'));
}

document.getElementById('previous-frame-button').onclick = function () {
    currentFrame.value = parseInt(currentFrame.value) - 1;
    currentFrame.dispatchEvent(new Event('change'));
}

document.getElementById('record-button').onclick = function () {
    startRecording()
}

if (simConfigLoader.buildingsFile !== ""){
    DataViewLoader.loadDataFrameFromDatFile(simConfigLoader.buildingsFile).then((dataView) => {
        SimpleObstacles.loadObstaclesFromDataView(dataView)
    });
}

changeFrame()

function initScene(x_dim, y_dim, z_dim) {
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
    recorder.start();

    /*
    let current = currentFrame.min
    while (current <= currentFrame.max){
        currentFrame.value = current

        changeFrame()
        render()
        recorder.capture(renderer.domElement)

        current += 1
    }*/
    currentFrame.value = currentFrame.min

    while (parseInt(currentFrame.value) <= currentFrame.max){
        await changeFrame()

        render()
        recorder.capture(renderer.domElement)

        currentFrame.value = parseInt(currentFrame.value) + 1
    }

    recorder.stop()
    recorder.save()
}

export { scene, initScene }