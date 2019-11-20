package models;

import interfaces.Source;

/**
 * @author 8130031
 *
 * Class that defines the frame reading by camera
 * Defines the index of connected cameras
 *
 */
public class SourceCamera implements Source {

    private int camera_index;

    public SourceCamera(int camera_index) {
        this.camera_index = camera_index;
    }

    public int getCamera_index() {
        return camera_index;
    }
}
