package pl.agh.diffusion_project;

import pl.agh.diffusion_project.cells.Cell;

//public class SimpleWind implements Wind{
//    @Override
//    public void wind(Cell[][][] cell, int width, int length, int height) {
//        //wiatr co 2 iteracje w dół po całym x na środku y i z po 1/3 ich dugości
//        for (int i = 0; i < width-1; i++) {
//            for (int j = 0; j < length; j++) {
//                for (int k = 0; k < height; k++) {
//                    if (3*j >= length && 3*j < 2*length
//                        && 3*k >= height && 3*k < 2*height){
//                        cell[i][j][k].addWindNeighbor(cell[i+1][j][k], true, 2);
//                        cell[i+1][j][k].addWindNeighbor(cell[i][j][k], false, 2);
//                    }
//
//                }
//            }
//        }
//    }
//}
