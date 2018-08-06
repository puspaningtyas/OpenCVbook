/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template imageFile, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter2;

import java.io.File;

/**
 *
 * @author user only
 */
public class Image {

    private File imageFile;

    public Image() {
    }

    public Image(File file) {
        this.imageFile = file;
    }

    /**
     * @return the imageFile
     */
    public File getImageFile() {
        return imageFile;
    }

    /**
     * @param imageFile the imageFile to set
     */
    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    
    
}
