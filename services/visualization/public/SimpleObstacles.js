import { Float32BufferAttribute } from 'three'
import { Vector3 } from 'three'
import {  InstancedBufferGeometry } from 'three'
import { InstancedBufferAttribute } from 'three'
import { BoxBufferGeometry } from 'three'
import { Mesh } from 'three'
import { ShaderMaterial } from 'three'

import {x_dim, y_dim, z_dim, scene, visibleRangeX, visibleRangeY, visibleRangeZ} from "./Main.js";

class SimpleObstacles {
    static cubeMesh = null;

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
            
            float new_x = (pos.x - offset.x + 0.5) * (myScale.x) + offset.x - 0.5;
            float new_y = (pos.y - offset.y + 0.5) * (myScale.y) + offset.y - 0.5;
            float new_z = (pos.z - offset.z + 0.5) * (myScale.z) + offset.z - 0.5;

            gl_Position = projectionMatrix * modelViewMatrix * vec4(new_x, new_y, new_z, 1.0 );
            
            v_color = color;
        }`;

    static _FS = `
        precision highp float;
        
        varying vec3 v_color;
        
        void main() {
            gl_FragColor = vec4(v_color, 1.);
        }`

    static _addObstacleGeneric(positions, sizes, boxGeo){
        const hexToRgb = function (hex) {
            let result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
            return result ? {
                r: parseInt(result[1], 16),
                g: parseInt(result[2], 16),
                b: parseInt(result[3], 16)
            } : null;
        }

        const buildVertexColorArrayFace = function (color_str) {
            let color = hexToRgb(color_str)

            return [
                color.r / 255., color.g / 255., color.b / 255.,
                color.r / 255., color.g / 255., color.b / 255.,
                color.r / 255., color.g / 255., color.b / 255.,
                color.r / 255., color.g / 255., color.b / 255.
            ]
        }


        const cubeGeo = new InstancedBufferGeometry()
        cubeGeo.index = boxGeo.index
        cubeGeo.attributes.position = boxGeo.attributes.position

        const offsets = [];
        const orientations = [];

        const sizesArray = []

        let colorArray = []
        colorArray = colorArray
            .concat(buildVertexColorArrayFace("#003366"))
            .concat(buildVertexColorArrayFace("#003366")) // prawo
            .concat(buildVertexColorArrayFace("#0080ff")) // gora
            .concat(buildVertexColorArrayFace("#000000")) // jest ok
            .concat(buildVertexColorArrayFace("#0059b3"))
            .concat(buildVertexColorArrayFace("#0059b3"))

        const x_offset = x_dim / 2;
        const y_offset = y_dim / 2;
        const z_offset = z_dim / 2;

        for (let i = 0; i < positions.length; i++) {
            let x = -positions[i].z + z_offset;
            let y = positions[i].y - y_offset;
            let z = positions[i].x - x_offset;

            if (
                visibleRangeX[0] <= positions[i].z && positions[i].z <= visibleRangeX[1] &&
                visibleRangeY[0] <= positions[i].y && positions[i].y <= visibleRangeY[1] &&
                visibleRangeZ[0] <= positions[i].x && positions[i].x <= visibleRangeZ[1]
            ){
                offsets.push(x-0.5, y+0.5, z+0.5);
                orientations.push(0, 0, 0, 0);

                sizesArray.push(sizes[i].x, sizes[i].y, sizes[i].z)
            }
        }

        const offsetAttribute = new InstancedBufferAttribute(new Float32Array(offsets), 3);
        const orientationAttribute = new InstancedBufferAttribute(new Float32Array(orientations), 4);

        const sizeAttribute = new InstancedBufferAttribute(new Float32Array(sizesArray), 3);
        const colorAttribute = new Float32BufferAttribute(new Float32Array(colorArray), 3)

        cubeGeo.setAttribute('offset', offsetAttribute);
        cubeGeo.setAttribute('orientation', orientationAttribute);

        cubeGeo.setAttribute('myScale', sizeAttribute);
        cubeGeo.setAttribute('color', colorAttribute);

        const material = new ShaderMaterial({
            vertexShader: SimpleObstacles._VS,
            fragmentShader: SimpleObstacles._FS,
            depthTest: true
        });

        return [cubeGeo, material]
    }

    static _addObstacles(positions, sizes) {
        if (SimpleObstacles.cubeMesh !== null)
            scene.remove(SimpleObstacles.cubeMesh)

        const boxGeo = new BoxBufferGeometry(1, 1, 1)

        let obstacleValues =
            SimpleObstacles._addObstacleGeneric(positions, sizes, boxGeo)

        SimpleObstacles.cubeMesh = new Mesh(obstacleValues[0], obstacleValues[1]);

        scene.add(SimpleObstacles.cubeMesh);
    }

    static loadObstaclesFromDataView(dataView) {
        const positions = [];
        const sizes = [];

        for (let i = 0; i < x_dim; i++){
            for (let j = 0; j < z_dim; j++){
                let height = dataView.getInt32(4 * (i * z_dim + j));

                if (height > 0){
                    positions.push(new Vector3(i, 0, j))
                    sizes.push(new Vector3(1, height, 1))
                }
            }
        }


        SimpleObstacles._addObstacles(positions, sizes)
    }
}

export { SimpleObstacles }