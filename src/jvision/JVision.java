package jvision;

import interfaces.Action;
import interfaces.Source;
import models.*;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import utils.Utils;

import java.awt.image.BufferedImage;

/**
 * @author 8130031
 *
 * Handles the process of frame capturing defined by {@link SourceImage}
 *
 */
public abstract class JVision implements Action {

    private Source source = null;
    private JVisionConfig visionConfig = new JVisionConfig();
    private Mat frame = new Mat();

    private BufferedImage bufferedImage;    //buffered image source mode


    protected void initSource() {
        if (source instanceof SourceCamera) {
            startSourceCamera();
        } else if (source instanceof SourceVideo) {
            startSourceVideo();
        } else if (source instanceof SourceImage) {
            startSourceImage();
        }
    }


    private void startSourceCamera() {
        SourceCamera sourceCamera = (SourceCamera) source;

        VideoCapture capture = new VideoCapture();
        capture.open(sourceCamera.getCamera_index());

        new Thread() {
            public void run() {
                while (capture.isOpened()) {
                    // read the current frame
                    capture.read(frame);
                }
            }
        }.start();
    }

    private void startSourceVideo() {
        SourceVideo sourceVideo = (SourceVideo) source;

        VideoCapture capture = new VideoCapture();
        capture.open(sourceVideo.getVideo_path());

        new Thread() {
            public void run() {
                while (capture.isOpened()) {
                    // read the current frame
                    capture.read(frame);
                }
            }
        }.start();
    }

    private void startSourceImage() {
        SourceImage sourceImage = (SourceImage) source;
        frame = Imgcodecs.imread(sourceImage.getImage_path());
    }

    protected void updateSourceBuffered(BufferedImage bufferedImage) {
        frame = Utils.grabFrameFromBuffered(bufferedImage);
    }


    protected Source getSource() {
        return source;
    }

    protected void setSource(Source source) {
        this.source = source;
    }

    protected JVisionConfig getVisionConfig() {
        return visionConfig;
    }

    protected void setVisionConfig(JVisionConfig visionConfig) {
        this.visionConfig = visionConfig;
    }

    protected Mat getFrame() {
        return frame;
    }

}
