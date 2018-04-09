/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter3;

import java.io.File;
import java.util.ArrayList;
import org.bytedeco.javacpp.DoublePointer;
import static org.bytedeco.javacpp.helper.opencv_objdetect.cvHaarDetectObjects;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import static org.bytedeco.javacpp.opencv_core.cvClearMemStorage;
import static org.bytedeco.javacpp.opencv_core.cvGetSeqElem;
import static org.bytedeco.javacpp.opencv_core.cvLoad;
import static org.bytedeco.javacpp.opencv_core.cvPoint;
import static org.bytedeco.javacpp.opencv_imgproc.CV_AA;
import static org.bytedeco.javacpp.opencv_imgproc.cvRectangle;
import org.bytedeco.javacpp.opencv_objdetect;
import static org.bytedeco.javacpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;

/**
 *
 * @author user only
 */
public class Face {
    private CvRect rect;
    private DoublePointer bb;
    private double[] landmarks;

    private static ArrayList<Face> listOfFace = new ArrayList<Face>();
    
    private static File imageFile;
    private static IplImage image;
    private static File faceCascadeFile = new File("D:\\Users\\admin\\my Java\\OpenCVSample\\src\\haarcascade_frontalface_alt.xml");
    private static File flandmarkModelFile = new File("D:\\Users\\admin\\my Java\\OpenCVSample\\src\\flandmark_model.xml");

    public Face() {
    }

    public Face(CvRect rect, DoublePointer bb, double[] landmarks) {
        this.rect = rect;
        this.bb = bb;
        this.landmarks = landmarks;
    }
    
    /**
     * @return the rect
     */
    public CvRect getRect() {
        return rect;
    }

    /**
     * @param rect the rect to set
     */
    public void setRect(CvRect rect) {
        this.rect = rect;
    }

    /**
     * @return the bb
     */
    public DoublePointer getBb() {
        return bb;
    }

    /**
     * @param bb the bb to set
     */
    public void setBb(DoublePointer bb) {
        this.bb = bb;
    }

    /**
     * @return the landmarks
     */
    public double[] getLandmarks() {
        return landmarks;
    }

    /**
     * @param landmarks the landmarks to set
     */
    public void setLandmarks(double[] landmarks) {
        this.landmarks = landmarks;
    }

    /**
     * @return the listOfFace
     */
    public static ArrayList<Face> getListOfFace() {
        return listOfFace;
    }

    /**
     * @param aListOfFace the listOfFace to set
     */
    public static void setListOfFace(ArrayList<Face> aListOfFace) {
        listOfFace = aListOfFace;
    }

    /**
     * @return the imageFile
     */
    public static File getImageFile() {
        return imageFile;
    }

    /**
     * @param aImageFile the imageFile to set
     */
    public static void setImageFile(File aImageFile) {
        imageFile = aImageFile;
    }

    /**
     * @return the image
     */
    public static IplImage getImage() {
        return image;
    }

    /**
     * @param aImage the image to set
     */
    public static void setImage(IplImage aImage) {
        image = aImage;
    }

    /**
     * @return the faceCascadeFile
     */
    public static File getFaceCascadeFile() {
        return faceCascadeFile;
    }

    /**
     * @param aFaceCascadeFile the faceCascadeFile to set
     */
    public static void setFaceCascadeFile(File aFaceCascadeFile) {
        faceCascadeFile = aFaceCascadeFile;
    }

    /**
     * @return the flandmarkModelFile
     */
    public static File getFlandmarkModelFile() {
        return flandmarkModelFile;
    }

    /**
     * @param aFlandmarkModelFile the flandmarkModelFile to set
     */
    public static void setFlandmarkModelFile(File aFlandmarkModelFile) {
        flandmarkModelFile = aFlandmarkModelFile;
    }
        
    public static IplImage detect(IplImage src) {
//        IplImage src = new IplImage(source);
        opencv_objdetect.CvHaarClassifierCascade cascade = 
                new opencv_objdetect.CvHaarClassifierCascade(cvLoad(faceCascadeFile.getAbsolutePath()));
        opencv_core.CvMemStorage storage = opencv_core.CvMemStorage.create();
        
        opencv_core.CvSeq sign = cvHaarDetectObjects(
                src,
                cascade,
                storage,
                1.5,
                3,
                CV_HAAR_DO_CANNY_PRUNING);
        cvClearMemStorage(storage);
        int total_Faces = sign.total();
        if (total_Faces <1)
            return null;
        for (int i = 0; i < total_Faces; i++) {
            CvRect r = new CvRect(cvGetSeqElem(sign, i));
            cvRectangle(
                    src,
                    cvPoint(r.x(), r.y()),
                    cvPoint(r.width() + r.x(), r.height() + r.y()),
                    opencv_core.CvScalar.RED,
                    2,
                    CV_AA,
                    0);

        }

        return src;
    }

    
}
