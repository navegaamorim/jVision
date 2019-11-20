package models;

import interfaces.Source;

/**
 * @author 8130031
 *
 * Class that defines the frame reading from a image file
 * Defines the path to image
 *
 */
public class SourceImage implements Source {

    private String image_path;

    public SourceImage(String image_path) {
        this.image_path = image_path;
    }

    public String getImage_path() {
        return image_path;
    }
}
