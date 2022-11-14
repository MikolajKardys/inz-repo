import * as THREE from 'three'

import { scene, initScene } from "./Main.js";

class SimpleObstacles {
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

    static getTypeColor(type){
        switch (type) {
            case 0: return new THREE.Vector3(125.0 / 255.0, 125.0 / 255.0, 125.0 / 255.0)
            case 1: return new THREE.Vector3(252.0 / 255.0, 3.0 / 255.0, 3.0 / 255.0)
            case 2: return new THREE.Vector3(100.0 / 255.0, 100.0 / 255.0, 100.0 / 255.0)
        }
    }

    static _addObstacleGeneric(x_dim, y_dim, z_dim, positions, sizes, types, boxGeo){
        const buildVertexColorArrayFace = function (r, g, b) {
            return [
                r, g, b,
                r, g, b,
                r, g, b,
                r, g, b
            ]
        }


        const cubeGeo = new THREE.InstancedBufferGeometry()
        cubeGeo.index = boxGeo.index
        cubeGeo.attributes.position = boxGeo.attributes.position

        const offsets = [];
        const orientations = [];

        const sizesArray = []

        let colorArray = []
        colorArray = colorArray
            .concat(buildVertexColorArrayFace(100. / 255., 100. / 255., 100. / 255.))
            .concat(buildVertexColorArrayFace(100. / 255., 100. / 255., 100. / 255.)) // prawo
            .concat(buildVertexColorArrayFace(150. / 255., 150. / 255., 150. / 255.)) // gora
            .concat(buildVertexColorArrayFace(0. / 255., 0. / 255., 0. / 255.)) // jest ok
            .concat(buildVertexColorArrayFace(200. / 255., 200. / 255., 200. / 255.))
            .concat(buildVertexColorArrayFace(200. / 255., 200. / 255., 200. / 255.))

        const x_offset = x_dim / 2;
        const y_offset = y_dim / 2;
        const z_offset = z_dim / 2;

        for (let i = 0; i < positions.length; i++) {
            offsets.push(positions[i].x - x_offset, positions[i].y - y_offset, positions[i].z - z_offset);
            orientations.push(0, 0, 0, 0);

            sizesArray.push(sizes[i].x, sizes[i].y, sizes[i].z)
        }

        const offsetAttribute = new THREE.InstancedBufferAttribute(new Float32Array(offsets), 3);
        const orientationAttribute = new THREE.InstancedBufferAttribute(new Float32Array(orientations), 4);

        const sizeAttribute = new THREE.InstancedBufferAttribute(new Float32Array(sizesArray), 3);
        const colorAttribute = new THREE.Float32BufferAttribute(new Float32Array(colorArray), 3)

        cubeGeo.setAttribute('offset', offsetAttribute);
        cubeGeo.setAttribute('orientation', orientationAttribute);

        cubeGeo.setAttribute('myScale', sizeAttribute);
        cubeGeo.setAttribute('color', colorAttribute);

        const material = new THREE.ShaderMaterial({
            uniforms: {
                lightDirection: { value: new THREE.Vector3(1.0, 1.0, 1.0).normalize() }
            },
            vertexShader: SimpleObstacles._VS,
            fragmentShader: SimpleObstacles._FS,

            depthTest: true
        });

        return [cubeGeo, material]
    }

    static _addObstacles(x_dim, y_dim, z_dim, positions, sizes, types) {
        const boxGeo = new THREE.BoxBufferGeometry(1, 1, 1)

        let obstacleValues =
            SimpleObstacles._addObstacleGeneric(x_dim, y_dim, z_dim, positions, sizes, types, boxGeo)

        const cubeMesh = new THREE.Mesh(obstacleValues[0], obstacleValues[1]);
        scene.add(cubeMesh);
    }

    static loadObstaclesFromDataView(dataView) {
        const x_dim = dataView.getInt32(0)
        const y_dim = dataView.getInt32(4)
        const z_dim = dataView.getInt32(8)

        if (scene == null){
            initScene(x_dim, y_dim, z_dim)
        }

        const obstacleNum = dataView.getInt32(12)

        const positions = []
        const sizes = []
        const types = []

        for (let i = 0; i < obstacleNum; i++) {
            const elements = []
            for (let j = 0; j < 7; j++){
                elements.push(dataView.getInt32(16 + 4 * (i * 7) + 4 * j))
            }

            types.push(elements[0])
            positions.push(new THREE.Vector3(elements[1], elements[3], elements[5]))
            sizes.push(new THREE.Vector3(elements[2] - elements[1], elements[4] - elements[3], elements[6] - elements[5]))
        }

        SimpleObstacles._addObstacles(x_dim, y_dim, z_dim, positions, sizes, types)
    }
}

export { SimpleObstacles }