package jvision;

import interfaces.Detection;
import models.OutputOpticalFlow;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 8130031
 *
 * Allows finding contours and edges in a {@link Mat} object
 *
 */
public class DetectionOpticalFlow implements Detection {

    //shitomasi corner detection params
    private int maxCorners = 20;
    private double qualityLevel = 0.8;
    private double minDistance = 20;

    private MatOfByte status = new MatOfByte();
    private MatOfFloat err = new MatOfFloat();

    private MatOfPoint2f matOfPointPrev, matOfPointThis;
    private MatOfPoint matOfPointCorners;
    private Mat matFlowThis, matFlowPrev, matOfPointSafe, mask;
    private List<Point> cornersPrev, cornersThis;
    private List<Byte> byteStatus = new ArrayList<>();
    //private Point prevPoint, nextPoint;

    public DetectionOpticalFlow() {
        init();
    }

    public DetectionOpticalFlow(int maxCorners, double qualityLevel, double minDistance) {
        this.maxCorners = maxCorners;
        this.qualityLevel = qualityLevel;
        this.minDistance = minDistance;

        init();
    }

    private void init() {
        matOfPointPrev = new MatOfPoint2f();
        matOfPointThis = new MatOfPoint2f();
        matOfPointCorners = new MatOfPoint();
        matFlowThis = new Mat();
        matFlowPrev = new Mat();
        matOfPointSafe = new Mat();
        mask = new Mat();
        cornersPrev = new ArrayList<>();
        cornersThis = new ArrayList<>();
    }

    protected OutputOpticalFlow detectFlow(Mat frame) {
        if (null != frame) {

            if (mask.rows() == 0) {
                mask = new Mat(new Size(frame.cols(), frame.rows()), frame.type());
            }

            if (matOfPointPrev.rows() == 0) {

                Imgproc.cvtColor(frame, matFlowThis, Imgproc.COLOR_BGR2GRAY);
                matFlowThis.copyTo(matFlowPrev);

                Imgproc.goodFeaturesToTrack(matFlowPrev, matOfPointCorners, maxCorners, qualityLevel, minDistance);
                matOfPointPrev.fromArray(matOfPointCorners.toArray());

                matOfPointPrev.copyTo(matOfPointSafe);

            } else {
                matFlowThis.copyTo(matFlowPrev);

                Imgproc.cvtColor(frame, matFlowThis, Imgproc.COLOR_RGBA2GRAY);
                Imgproc.goodFeaturesToTrack(matFlowThis, matOfPointCorners, maxCorners, 0.05, minDistance);

                matOfPointThis.fromArray(matOfPointCorners.toArray());
                matOfPointSafe.copyTo(matOfPointPrev);
                matOfPointThis.copyTo(matOfPointSafe);
            }

            Video.calcOpticalFlowPyrLK(matFlowPrev, matFlowThis, matOfPointPrev, matOfPointThis, status, err);

            cornersPrev = matOfPointPrev.toList();
            cornersThis = matOfPointThis.toList();
            byteStatus = status.toList();

            return new OutputOpticalFlow(mask, cornersPrev, cornersThis, byteStatus);

        } else {

            return new OutputOpticalFlow();
        }
    }
}
