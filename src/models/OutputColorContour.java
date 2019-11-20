package models;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import java.util.List;

/**
 * @author 8130031
 *
 * Output given by {@link jvision.DetectionContour} to {@link jvision.JVisionDetection}
 *
 */
public class OutputColorContour {

    private Mat posProcessTest;

    private Mat hierarchy;
    private List<MatOfPoint> contours;
    private ColorObject contourObject;

    public OutputColorContour() {
    }

    public OutputColorContour(Mat posProcess, Mat hierarchy, List<MatOfPoint> contours, ColorObject contourObject) {
        this.posProcessTest = posProcess;
        this.hierarchy = hierarchy;
        this.contours = contours;
        this.contourObject = contourObject;
    }

    public Mat getHierarchy() {
        return hierarchy;
    }

    public List<MatOfPoint> getContours() {
        return contours;
    }

    public ColorObject getContourObject() {
        return contourObject;
    }

    public Mat getPosProcessTest() {
        return posProcessTest;
    }
}
