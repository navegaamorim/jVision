package models;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 8130031
 *
 * Output sended to {@link jvision.JVisionDetection} by {@link jvision.DetectionOpticalFlow}
 *
 */
public class OutputOpticalFlow {

    private Mat mask;
    private List<Point> cornersPrev;
    private List<Point> cornersThis;
    private List<Byte> byteStatus;

    public OutputOpticalFlow() {
    }

    public OutputOpticalFlow(Mat mask, List<Point> cornersPrev, List<Point> cornersThis, List<Byte> byteStatus) {
        this.mask = mask;
        this.cornersPrev = cornersPrev;
        this.cornersThis = cornersThis;
        this.byteStatus = byteStatus;
    }

    public Mat getMask() {
        return mask;
    }

    public List<Point> getCornersPrev() {
        return cornersPrev;
    }

    public List<Point> getCornersThis() {
        return cornersThis;
    }

    public List<Byte> getByteStatus() {
        return byteStatus;
    }

}
