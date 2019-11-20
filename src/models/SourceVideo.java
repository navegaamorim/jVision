package models;

import interfaces.Source;


/**
 * @author 8130031
 *
 * Class that defines the frame reading from a video file
 * Defines the path to video
 *
 */
public class SourceVideo implements Source {

    private String video_path;

    public SourceVideo(String video_path) {
        this.video_path = video_path;
    }

    public String getVideo_path() {
        return video_path;
    }
}
