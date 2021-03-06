/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter1;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

/**
 *
 * @author user only
 */
public class ImageTest0 {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        System.out.println("Welcome to OpenCV " + Core.VERSION);
        Mat m = new Mat(5, 5, CvType.CV_8UC1, new Scalar(0));
        System.out.println("OpenCV Mat: " + m);
        Mat mr1 = m.row(1);
        mr1.setTo(new Scalar(1));
        Mat mc5 = m.col(4);
        mc5.setTo(new Scalar(5));
        System.out.println("OpenCV Mat data:\n" + m.dump());
        Mat n = m.clone();
        
        double o = m.dot(n);
        System.out.println("O = m.n --> "+o);
        
        Mat p = m.mul(n);
        System.out.println("OpenCV Mat data multiplication:\n" + p.dump());
        
        Mat q = Mat.eye(p.size(), CvType.CV_8UC1);
        System.out.println("OpenCV Mat data eye:\n" + q.dump());
        
        Mat r = new Mat(new Size(3,3),CvType.CV_8UC3, new Scalar(new double[]{10,5,1}));
        System.out.println("OpenCV Mat data 3 channel:\n" + r.dump());
        
        Mat s = Mat.eye(new Size(3,3), CvType.CV_8UC3);
        System.out.println("OpenCV Mat data eye:\n" + s.dump());
        
    }
}
