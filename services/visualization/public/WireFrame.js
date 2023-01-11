import {scene, x_dim, y_dim, z_dim} from "./Main.js";
import { BoxGeometry, EdgesGeometry, LineBasicMaterial, LineSegments } from 'three'

class WireFrame {
    static wireFrame = null;

    static addWireFrame(){
        const extra = new BoxGeometry(z_dim, y_dim, x_dim);
        let geo = new EdgesGeometry( extra );
        let mat = new LineBasicMaterial( { color: 0x000000, linewidth: 2 } );
        WireFrame.wireframe = new LineSegments( geo, mat );
        WireFrame.wireframe.position.set(0, 0, 0);

        scene.add( WireFrame.wireframe );
    }
}

export { WireFrame }