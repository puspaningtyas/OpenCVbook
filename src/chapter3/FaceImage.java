/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter3;

import chapter2.Image;
import java.io.File;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.flandmark;
import static org.bytedeco.javacpp.flandmark.flandmark_detect;
import static org.bytedeco.javacpp.flandmark.flandmark_init;
import static org.bytedeco.javacpp.helper.opencv_core.CV_RGB;
import static org.bytedeco.javacpp.helper.opencv_objdetect.cvHaarDetectObjects;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.CvSize;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.cvClearMemStorage;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvCreateMemStorage;
import static org.bytedeco.javacpp.opencv_core.cvGetSeqElem;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvLoad;
import static org.bytedeco.javacpp.opencv_core.cvPoint;
import static org.bytedeco.javacpp.opencv_core.cvReleaseMemStorage;
import static org.bytedeco.javacpp.opencv_core.cvSize;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_AA;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_FILLED;
import static org.bytedeco.javacpp.opencv_imgproc.cvCircle;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvRectangle;
import org.bytedeco.javacpp.opencv_objdetect;
import static org.bytedeco.javacpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;
import static org.bytedeco.javacpp.opencv_objdetect.CV_HAAR_DO_ROUGH_SEARCH;

/**
 *
 * @author user only
 */
public class FaceImage extends Image{

    private IplImage image;
    private File faceCascadeFile = new File("D:\\Users\\admin\\my Java\\OpenCVBook\\lib\\haarcascade_frontalface_alt.xml");
    private File flandmarkModelFile = new File("D:\\Users\\admin\\my Java\\OpenCVBook\\lib\\flandmark_model.dat");

    public FaceImage() {
    }

    public FaceImage(File imageFile) {
        super(imageFile);
        this.image = cvLoadImage(imageFile.getAbsolutePath());
    }

    
    
    /**
     * @return the image
     */
    public IplImage getImage() {
        return image;
    }

    /**
     * @param aImage the image to set
     */
    public void setImage(IplImage aImage) {
        image = aImage;
    }

    /**
     * @return the faceCascadeFile
     */
    public File getFaceCascadeFile() {
        return faceCascadeFile;
    }

    /**
     * @param aFaceCascadeFile the faceCascadeFile to set
     */
    public void setFaceCascadeFile(File aFaceCascadeFile) {
        faceCascadeFile = aFaceCascadeFile;
    }

    /**
     * @return the flandmarkModelFile
     */
    public File getFlandmarkModelFile() {
        return flandmarkModelFile;
    }

    /**
     * @param aFlandmarkModelFile the flandmarkModelFile to set
     */
    public void setFlandmarkModelFile(File aFlandmarkModelFile) {
        flandmarkModelFile = aFlandmarkModelFile;
    }

    public IplImage cvHaarClassifierDetection(IplImage src) {
//        IplImage src = new IplImage(source);
        opencv_objdetect.CvHaarClassifierCascade cascade
                = new opencv_objdetect.CvHaarClassifierCascade(cvLoad(faceCascadeFile.getAbsolutePath()));
        opencv_core.CvMemStorage storage = opencv_core.CvMemStorage.create();

        CvSize minFeatureSize = cvSize(40, 40);
        CvSeq faceRects = opencv_objdetect.cvHaarDetectObjects(
                src,
                cascade,
                storage,
                1.1, // image scale
                3,
                CV_HAAR_DO_ROUGH_SEARCH,
                minFeatureSize,
                cvSize(0, 0));
        cvClearMemStorage(storage);
        int total_Faces = faceRects.total();
        if (total_Faces < 1) {
            return null;
        }
        for (int i = 0; i < total_Faces; i++) {
            CvRect r = new CvRect(cvGetSeqElem(faceRects, i));
            cvRectangle(
                    src,
                    cvPoint(r.x(), r.y()),
                    cvPoint(r.width() + r.x(), r.height() + r.y()),
                    opencv_core.CvScalar.RED,
                    1,
                    CV_AA,
                    0);
        }
        cvReleaseMemStorage(storage);
        return src;
    }
    
    public IplImage cvHaarClassifierDetection() {
        return cvHaarClassifierDetection(this.image);
    }

    public IplImage cvFaceLandMarksDetection(IplImage src) {
        opencv_core.CvMemStorage storage = cvCreateMemStorage(0);
        cvClearMemStorage(storage);
        try {
            IplImage inputBW = cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 1);
            cvCvtColor(src, inputBW, CV_BGR2GRAY);
            opencv_objdetect.CvHaarClassifierCascade cascade
                    = new opencv_objdetect.CvHaarClassifierCascade(cvLoad(faceCascadeFile.getAbsolutePath()));

            CvSize minFeatureSize = cvSize(40, 40);
            CvSeq faceRects = opencv_objdetect.cvHaarDetectObjects(
                    src,
                    cascade,
                    storage,
                    1.1, // image scale
                    3,
                    CV_HAAR_DO_ROUGH_SEARCH,
                    minFeatureSize,
                    cvSize(0, 0));
            cvClearMemStorage(storage);
            int nFaces = faceRects.total();
            if (nFaces == 0) {
                return null;
            } else {
                flandmark.FLANDMARK_Model model = flandmark_init(flandmarkModelFile.getAbsolutePath());
                int[] bbox = new int[4];
                double[] landmarks = new double[2 * model.data().options().M()];

                for (int iface = 0; iface < nFaces; ++iface) {
                    BytePointer elem = cvGetSeqElem(faceRects, iface);
                    CvRect rect = new CvRect(elem);

                    bbox[0] = rect.x();
                    bbox[1] = rect.y();
                    bbox[2] = rect.x() + rect.width();
                    bbox[3] = rect.y() + rect.height();

                    flandmark_detect(inputBW, bbox, model, landmarks);
                    // display landmarks
                    cvRectangle(src, cvPoint(bbox[0], bbox[1]), cvPoint(bbox[2], bbox[3]), CV_RGB(255, 0, 0));
                    cvRectangle(src,
                            cvPoint((int) model.bb().get(0), (int) model.bb().get(1)),
                            cvPoint((int) model.bb().get(2), (int) model.bb().get(3)), CV_RGB(0, 0, 255));
                    cvCircle(src,
                            cvPoint((int) landmarks[0], (int) landmarks[1]), 3, CV_RGB(0, 0, 255), CV_FILLED, 8, 0);
                    for (int i = 2; i < 2 * model.data().options().M(); i += 2) {
                        cvCircle(src, cvPoint((int) (landmarks[i]), (int) (landmarks[i + 1])), 3, CV_RGB(255, 0, 0), CV_FILLED, 8, 0);
                    }
                }
            }

        } finally {
            cvReleaseMemStorage(storage);
        }
        return src;
    }
    
    public IplImage cvFaceLandMarksDetection() {
        return cvFaceLandMarksDetection(this.image);
    }
}
