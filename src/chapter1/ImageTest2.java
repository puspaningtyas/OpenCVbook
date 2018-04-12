/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter1;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.core.Mat;

/**
 *
 * @author user only
 */
public class ImageTest2 {

    public static void main(String[] args) {
        //Create a file chooser
        JFileChooser fc = new JFileChooser();
        //In response to a button click:
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            JPanel panel = new JPanel();
            File file = fc.getSelectedFile();
            opencv_core.IplImage img = cvLoadImage(file.getAbsolutePath());
            BufferedImage image = toBufferedImage(img);
            JLabel label = new JLabel(new ImageIcon(image));
            panel.add(label);

            // main window
            JFrame.setDefaultLookAndFeelDecorated(true);
            JFrame frame = new JFrame("JPanel Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // add the Jpanel to the main window
            frame.add(panel);

            frame.pack();
            frame.setVisible(true);
        }
    }

    public static BufferedImage toBufferedImage(opencv_core.IplImage m) {
        int type = BufferedImage.TYPE_3BYTE_BGR;
        Frame frame = new org.bytedeco.javacv.Frame();
        OpenCVFrameConverter.ToIplImage tools = new OpenCVFrameConverter.ToIplImage();
        frame = tools.convert(m);
        BufferedImage image = new BufferedImage(m.width(), m.height(), type);
        org.bytedeco.javacv.Java2DFrameConverter tools1 = new Java2DFrameConverter();
        image = tools1.convert(frame);
        return image;
    }
    
    public static Image toBufferedImageFromMat(Mat matrix) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (matrix.channels()>1){
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize= matrix.channels()*matrix.cols()*matrix.rows();
        byte buffer[]= new byte[bufferSize];
        matrix.get(0, 0, buffer);
        BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);
        byte targetPixels[] = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
    }
}
