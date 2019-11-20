package models;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

/**
 * @author 8130031
 *
 * Output sended to {@link interfaces.BehaviourDetection} by {@link jvision.JVisionDetection}
 *
 */
public class OutputJVisionImage {

    private Mat posProcessTest;

    private Mat frame;
    private Rect[] targets;
    private boolean targetsFound;

    public OutputJVisionImage() {
        this.frame = new Mat();
        this.targets = new Rect[0];
        this.targetsFound = false;
    }

    /**
     *
     *
     * @param frame
     * @param targetsFound
     */
    public OutputJVisionImage(Mat posProcess, Mat frame, boolean targetsFound) {
        this.posProcessTest = posProcess;
        this.frame = frame;
        this.targetsFound = targetsFound;
        this.targets = new Rect[0];
    }

    public Mat getFrame() {
        return frame;
    }

    public void setFrame(Mat frame) {
        this.frame = frame;
    }

    public Rect[] getTargets() {
        return targets;
    }

    public void setTargets(Rect[] targets) {
        this.targets = targets;
    }

    public boolean isTargetsFound() {
        return targetsFound;
    }

    public void setTargetsFound(boolean targetsFound) {
        this.targetsFound = targetsFound;
    }

    public Mat getPosProcessTest() {
        return posProcessTest;
    }

    public void setPosProcessTest(Mat posProcessTest) {
        this.posProcessTest = posProcessTest;
    }
}
