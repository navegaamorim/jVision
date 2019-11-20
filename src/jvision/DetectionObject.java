package jvision;

import enums.EnumObjectTarget;
import interfaces.Detection;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

/**
 * @autor 8130031
 *
 * Allows finding a defined object {@link EnumObjectTarget} in a {@link Mat} object
 *
 */
public class DetectionObject implements Detection {

    private static float DEFAULT_MINIMUM_TARGET_AREA = 0.2f;

    //class variables
    private int objectSize;
    private CascadeClassifier cascadeClassifier;
    private float targetArea;//minimum percentage of target detected

    public DetectionObject(EnumObjectTarget targetType) {
        this.targetArea = DEFAULT_MINIMUM_TARGET_AREA;
        cascadeClassifier = new CascadeClassifier(EnumObjectTarget.getCascade(targetType));
    }

    public DetectionObject(float minimumTargetArea, String cascade_path) {
        targetArea = minimumTargetArea;
        cascadeClassifier = new CascadeClassifier(cascade_path);
    }

    protected Rect[] detectObject(Mat frame) {
        if (!frame.empty()) {

            MatOfRect objects = new MatOfRect();
            Mat grayFrame = new Mat();

            //convert frame to gray color
            Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
            // equalize the frame histogram to improve the result
            Imgproc.equalizeHist(grayFrame, grayFrame);

            // compute minimum object size
            int height = grayFrame.rows();
            if (Math.round(height * targetArea) > 0) {
                objectSize = Math.round(height * targetArea);
            }

            // detect faces
            cascadeClassifier.detectMultiScale(grayFrame, objects, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(objectSize, objectSize), new Size());

            return objects.toArray();
        } else {
            return new Rect[0];
        }
    }
}
