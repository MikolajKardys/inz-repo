import { x_dim, y_dim, z_dim, scene } from "./Main.js";
import { BoxGeometry, EdgesGeometry, LineBasicMaterial, LineSegments } from 'three'

class WireFrame {
    static addWireFrame(){
        const extra = new BoxGeometry(x_dim, y_dim, z_dim)
        let geo = new EdgesGeometry( extra );
        let mat = new LineBasicMaterial( { color: 0x000000, linewidth: 2 } );
        let wireframe = new LineSegments( geo, mat );
        wireframe.position.set(0, 0, 0)
        scene.add( wireframe );
    }
}

export { WireFrame }