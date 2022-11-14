import * as THREE from 'three'
import { OrbitControls } from 'orbit-controls'

import { SimplePollution } from "./SimplePollution.js";
import { DataViewLoader } from "./DataViewLoader.js";
import { SimpleObstacles } from "./SimpleObstacles.js";

let scene
let camera
let renderer
let controls
let recorder

/*
document.getElementById("files").addEventListener("change", async function (event) {
    let files = event.target.files;

    for (let i = 0; i < files.length; i++) {
        const reader = new FileReader();
        reader.readAsArrayBuffer(this.files[i]);

        reader.onload = function (e) {
            const buffer = e.target.result

            const dataView = new DataView(buffer)

            const x_dim = dataView.getInt32(0, false)
            const y_dim = dataView.getInt32(4, false)
            const z_dim = dataView.getInt32(8, false)

            const array = []
            for (let i = 0; i < x_dim * y_dim * z_dim; i++) {
                array.push(dataView.getFloat32(12 + i * 4, false))
            }

            if (SimplePollution.cubeGeo == null) {
                SimplePollution.addCells(x_dim, y_dim, z_dim, array)
            } else {
                SimplePollution.updateCells(x_dim, y_dim, z_dim, array)
            }

            render()
        }

        console.log(i)
        //await sleep(100)
    }
}, false);

document.getElementById("save_button").addEventListener("click", function () {
    stopRecorder()
}, false)*/
/*
document.getElementById("files").addEventListener("change", async function (event) {
    let file = event.;


    const reader = new FileReader();
    reader.readAsArrayBuffer(file);

    reader.onload = function (e) {
        const buffer = e.target.result

        const dataView = new DataView(buffer)

        const x_dim = dataView.getInt32(0, false)
        const y_dim = dataView.getInt32(4, false)
        const z_dim = dataView.getInt32(8, false)

        const array = []
        for (let i = 0; i < x_dim * y_dim * z_dim; i++) {
            array.push(dataView.getFloat32(12 + i * 4, false))
        }

        if (SimplePollution.cubeGeo == null) {
            SimplePollution.addCells(x_dim, y_dim, z_dim, array)
        } else {
            SimplePollution.updateCells(x_dim, y_dim, z_dim, array)
        }

        render()
    }

}, false);
*/

DataViewLoader.loadDataFrameFromDatFile("./data/obstacles.dat").then((dataView) => {
    SimpleObstacles.loadObstaclesFromDataView(dataView)

    render()
    animate()
})

function initScene(x_dim, y_dim, z_dim) {
    scene = new THREE.Scene()

    camera = new THREE.PerspectiveCamera(75, 4 / 3, 1, 2000)

    camera.position.x = 0
    camera.position.y = 0
    camera.position.z = z_dim

    camera.lookAt(0, 0, 0)

    renderer = new THREE.WebGLRenderer()
    renderer.setSize(1200, 900)
    renderer.setClearColor(0xdddddd, 0.3);

    document.body.appendChild(renderer.domElement)

    controls = new OrbitControls(camera, renderer.domElement);

    //recorder = new CCapture({
     //   format: 'jpg',
     //   quality: 99,
    //});
}

function render(){
    renderer.render(scene, camera.position);

    // recorder.capture(renderer.domElement)
}

function animate(){
    requestAnimationFrame( animate );

    controls.update();

    renderer.render( scene, camera );
}

function startRecorder(){
    recorder.start()
}

function stopRecorder(){
    recorder.stop()
    recorder.save()
}

export { scene, initScene }