import { InstancedBufferAttribute } from 'three'
import { BoxBufferGeometry } from 'three'
import { RawShaderMaterial } from 'three'
import { DoubleSide } from 'three'
import { InstancedBufferGeometry } from 'three'
import { Mesh } from 'three'

import {x_dim, y_dim, z_dim, scene, visibleRangeX, visibleRangeY, visibleRangeZ, scaleFactor} from "./Main.js";

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

    static _addCells(colorArray) {
        const boxGeo = new BoxBufferGeometry(1, 1, 1)

        SimplePollution.cubeGeo = new InstancedBufferGeometry()
        SimplePollution.cubeGeo.index = boxGeo.index
        SimplePollution.cubeGeo.attributes.position = boxGeo.attributes.position

        const offsets = [];
        const orientations = [];

        const x_offset = x_dim / 2;
        const y_offset = y_dim / 2;
        const z_offset = z_dim / 2;

        for (let i = 0; i < z_dim; i++) {
            for (let j = 0; j < y_dim; j++) {
                for (let k = 0; k < x_dim; k++) {
                    const x = i - z_offset;
                    const y = j - y_offset;
                    const z = k - x_offset;

                    offsets.push(-x-0.5, y+0.5, z+0.5);

                    orientations.push(0, 0, 0, 0);
                }
            }
        }

        const offsetAttribute = new InstancedBufferAttribute(new Float32Array(offsets), 3);
        const orientationAttribute = new InstancedBufferAttribute(new Float32Array(orientations), 4);

        SimplePollution.cubeColors = new Float32Array(colorArray)
        const colorAttribute = new InstancedBufferAttribute(SimplePollution.cubeColors, 1);

        SimplePollution.cubeGeo.setAttribute('offset', offsetAttribute);
        SimplePollution.cubeGeo.setAttribute('orientation', orientationAttribute);

        SimplePollution.cubeGeo.setAttribute('color', colorAttribute);
        SimplePollution.cubeGeo.getAttribute('color').needsUpdate = true;

        const material = new RawShaderMaterial({
            vertexShader: SimplePollution._VS,
            fragmentShader: SimplePollution._FS,
            side: DoubleSide,
            transparent: true,
            depthWrite: false,
        });

        const cubeMesh = new Mesh(SimplePollution.cubeGeo, material);

        scene.add(cubeMesh);
    }

    static _updateCells(colorArray) {
        for (let i = 0; i < z_dim; i++) {
            for (let j = 0; j < y_dim; j++) {
                for (let k = 0; k < x_dim; k++) {
                    const index = y_dim * x_dim * i + x_dim * j + k;
					
	
					if (
						visibleRangeZ[0] <= k && k <= visibleRangeZ[1] && 
						visibleRangeX[0] <= i && i <= visibleRangeX[1]
					){
						SimplePollution.cubeColors[index] = colorArray[index];
					} else {
						SimplePollution.cubeColors[index] = 0;
					}
                }
            }
        }

        const colorAttribute = new InstancedBufferAttribute(SimplePollution.cubeColors, 1);

        SimplePollution.cubeGeo.setAttribute('color', colorAttribute);
        SimplePollution.cubeGeo.getAttribute('color').needsUpdate = true;
    }

    static loadPollutionFromDataView(dataView){
        const array = []
        for (let i = 0; i < x_dim; i++) {
            for (let j = 0; j < y_dim; j++){
                for (let k = 0; k < z_dim; k++){
                    let value = 0;

                    value = dataView.getFloat32((i * y_dim * z_dim + j * z_dim + k) * 4);

                    array.push(value * scaleFactor)
                }
            }
        }

        if (SimplePollution.cubeGeo == null) {
            SimplePollution._addCells(array)
        } else {
            SimplePollution._updateCells(array)
        }
    }
}

export { SimplePollution }