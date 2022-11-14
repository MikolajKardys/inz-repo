import * as THREE from 'three'

import { scene, initScene } from "./Main.js";

class SimplePollution {
     static  _VS = `
        precision highp float;
        
        uniform mat4 modelViewMatrix;
        uniform mat4 projectionMatrix;
        
        attribute vec3 position;
        attribute vec3 offset;
        attribute vec4 orientation;
        
        attribute float color;
        
        varying vec4 cubeColor;
        
        void main(){
            vec3 pos = offset + position;
            vec3 vcV = cross( orientation.xyz, pos );
            pos = vcV * ( 2.0 * orientation.w ) + ( cross( orientation.xyz, vcV ) * 2.0 + pos );

            gl_Position = projectionMatrix * modelViewMatrix * vec4(pos, 1.0 );
            
            cubeColor = vec4(0, 0, 0, color);
        }`;

    static _FS = `
        precision highp float;
        
        varying vec4 cubeColor;
        
        void main() {
            gl_FragColor = cubeColor;
        }`

    static cubeGeo;
    static cubeColors;

    static _addCells(x_dim, y_dim, z_dim, colorArray) {
        const boxGeo = new THREE.BoxBufferGeometry(1, 1, 1)

        SimplePollution.cubeGeo = new THREE.InstancedBufferGeometry()
        SimplePollution.cubeGeo.index = boxGeo.index
        SimplePollution.cubeGeo.attributes.position = boxGeo.attributes.position

        const offsets = [];
        const orientations = [];

        const x_offset = x_dim / 2;
        const y_offset = y_dim / 2;
        const z_offset = z_dim / 2;

        for (let i = 0; i < x_dim; i++) {
            for (let j = 0; j < 100; j++) {
                for (let k = 0; k < z_dim; k++) {
                    const x = i - x_offset;
                    const y = j - y_offset;
                    const z = k - z_offset;

                    offsets.push(x, y, z);

                    orientations.push(0, 0, 0, 0);
                }
            }
            console.log("test")
        }

        const offsetAttribute = new THREE.InstancedBufferAttribute(new Float32Array(offsets), 3);
        const orientationAttribute = new THREE.InstancedBufferAttribute(new Float32Array(orientations), 4);

        SimplePollution.cubeColors = new Float32Array(colorArray)
        const colorAttribute = new THREE.InstancedBufferAttribute(SimplePollution.cubeColors, 1);

        SimplePollution.cubeGeo.setAttribute('offset', offsetAttribute);
        SimplePollution.cubeGeo.setAttribute('orientation', orientationAttribute);

        SimplePollution.cubeGeo.setAttribute('color', colorAttribute);
        SimplePollution.cubeGeo.getAttribute('color').needsUpdate = true;

        const material = new THREE.RawShaderMaterial({
            vertexShader: SimplePollution._VS,
            fragmentShader: SimplePollution._FS,
            side: THREE.DoubleSide,
            transparent: true,
            depthWrite: false,
        });

        const cubeMesh = new THREE.Mesh(SimplePollution.cubeGeo, material);

        scene.add(cubeMesh);
    }

    static _updateCells(x_dim, y_dim, z_dim, colorArray) {
        for (let i = 0; i < x_dim; i++) {
            for (let j = 0; j < y_dim; j++) {
                for (let k = 0; k < z_dim; k++) {
                    const index = y_dim * z_dim * i + z_dim * j + k;

                    SimplePollution.cubeColors[index] = colorArray[index]
                }
            }
        }

        const colorAttribute = new THREE.InstancedBufferAttribute(SimplePollution.cubeColors, 1);

        SimplePollution.cubeGeo.setAttribute('color', colorAttribute);
        SimplePollution.cubeGeo.getAttribute('color').needsUpdate = true;
    }

    static loadPollutionFromDataView(dataView){
        const x_dim = 100
        const y_dim = 100
        const z_dim = 100

        if (scene == null){
            initScene(x_dim, y_dim, z_dim)
        }

        const array = []
        for (let i = 0; i < x_dim * y_dim * z_dim; i++) {
            array.push(dataView.getFloat32(12 + i * 4, false))
        }

        if (SimplePollution.cubeGeo == null) {
            SimplePollution._addCells(x_dim, y_dim, z_dim, array)
        } else {
            SimplePollution._updateCells(x_dim, y_dim, z_dim, array)
        }
    }
}

export { SimplePollution }