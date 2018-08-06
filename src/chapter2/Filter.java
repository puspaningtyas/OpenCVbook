/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter2;

import org.opencv.core.Mat;

/**
 *
 * @author user only
 */
public interface Filter {
    public void filter(Mat kernel);
}
