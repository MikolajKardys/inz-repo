import { Vector3, Vector2 } from 'three'
import {  InstancedBufferGeometry } from 'three'
import { InstancedBufferAttribute } from 'three'
import { BoxBufferGeometry } from 'three'
import { Mesh } from 'three'
import { ShaderMaterial } from 'three'

import {x_dim, y_dim, z_dim, scene, visibleRangeX, visibleRangeZ, visibleRangeY} from "./Main.js";

class SimpleRoads {
    static roadMesh = null;

    static  _VS = `
        precision highp float;
        
        attribute vec3 offset;
        attribute vec4 orientation;
        
        attribute vec3 myScale;
        
        attribute vec3 color;
        
        varying vec3 v_color;
        
        void main(){
            vec3 pos = offset + position;
            vec3 vcV = cross( orientation.xyz, pos );
            pos = vcV * ( 2.0 * orientation.w ) + ( cross( orientation.xyz, vcV ) * 2.0 + pos );
            
            float new_x = (pos.x - offset.x + 0.5) * (myScale.x) + offset.x - 1.0;
            float new_y = (pos.y - offset.y + 0.5) * (myScale.y) + offset.y;
            float new_z = (pos.z - offset.z + 0.5) * (myScale.z) + offset.z;

            gl_Position = projectionMatrix * modelViewMatrix * vec4(new_x, new_y, new_z, 1.0 );
            
            v_color = color;
        }`;

    static _FS = `
        precision highp float;
        
        varying vec3 v_color;
        
        void main() {
            gl_FragColor = vec4(v_color, 1.);
        }`

    static _addRoads(positions, colors) {
        if (SimpleRoads.roadMesh !== null)
            scene.remove(SimpleRoads.roadMesh)

        const boxGeo = new BoxBufferGeometry(1, 1, 1);

        const cubeGeo = new InstancedBufferGeometry();
        cubeGeo.index = boxGeo.index;
        cubeGeo.attributes.position = boxGeo.attributes.position;

        const x_offset = z_dim / 2;
        const z_offset = x_dim / 2;

        const offsets = [];
        const orientations = [];
        const sizesArray = [];

        let colorsArray = [];

        for (let i = 0; i < positions.length; i++) {
            if (
                visibleRangeX[0] <= positions[i].y && positions[i].y <= visibleRangeX[1] &&
                visibleRangeZ[0] <= positions[i].x && positions[i].x <= visibleRangeZ[1]
            ){
                offsets.push(-positions[i].y + x_offset, -y_dim / 2, positions[i].x - z_offset);
                orientations.push(0, 0, 0, 0);

                colorsArray.push(colors[i].x, colors[i].y, colors[i].z)

                sizesArray.push(1, 0, 1);
            }
        }

        const offsetAttribute = new InstancedBufferAttribute(new Float32Array(offsets), 3);
        const orientationAttribute = new InstancedBufferAttribute(new Float32Array(orientations), 4);
        const colorAttribute = new InstancedBufferAttribute(new Float32Array(colorsArray), 3);
        const sizeAttribute = new InstancedBufferAttribute(new Float32Array(sizesArray), 3);

        cubeGeo.setAttribute('offset', offsetAttribute);
        cubeGeo.setAttribute('orientation', orientationAttribute);
        cubeGeo.setAttribute('color', colorAttribute);
        cubeGeo.setAttribute('myScale', sizeAttribute);

        const material = new ShaderMaterial({
            vertexShader: SimpleRoads._VS,
            fragmentShader: SimpleRoads._FS,
            depthTest: true
        });

        SimpleRoads.roadMesh = new Mesh(cubeGeo, material);
        scene.add(SimpleRoads.roadMesh);
    }

    static loadRoadsFromDataView(dataView) {
        const positions = []
        const colors = []

        for (let i = 0; i < x_dim; i++){
            for(let j = 0; j < z_dim; j++){
                let num = dataView.getInt32(4 * (i * z_dim + j));

                num >>>= 0;
                let b = num & 0xFF;
                let g = (num & 0xFF00) >>> 8;
                let r = (num & 0xFF0000) >>> 16;

                if (r > 0 || g > 0 || b > 0) {
                    colors.push(new Vector3(r / 255., g / 255., b / 255.))
                    positions.push(new Vector2(i, j))
                }
            }
        }

        SimpleRoads._addRoads(positions, colors)
    }
}

export { SimpleRoads }