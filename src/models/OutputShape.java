package models;

import enums.EnumShape;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import java.util.List;

/**
 * @author 8130031
 *
 * Output given by {@link jvision.DetectionShape} to {@link jvision.JVisionDetection}
 *
 * In circle shape it's used HoughCircle methode, and have a {@link Mat} as result
 * In other shapes it's used findContours methode, and have a {@link List<MatOfPoint>} as result
 *
 */
public class OutputShape {

    private Mat posProcessTest;

    private EnumShape shapeEnum;

    private Mat circles;
    private List<MatOfPoint> contours;
    private int countoursIdx;

    public OutputShape() {
    }

    public OutputShape(Mat posProcess, EnumShape shape) {
        this.posProcessTest = posProcess;
        this.shapeEnum = shape;
    }

    public OutputShape(Mat posProcess, EnumShape shape, Mat circles) {
        this.posProcessTest = posProcess;
        this.shapeEnum = shape;
        this.circles = circles;
    }

    public OutputShape(Mat posProcess, EnumShape shape, List<MatOfPoint> contours, int countoursIdx) {
        this.posProcessTest = posProcess;
        this.shapeEnum = shape;
        this.contours = contours;
        this.countoursIdx = countoursIdx;
    }

    public List<MatOfPoint> getContours() {
        return contours;
    }

    public int getCountoursIdx() {
        return countoursIdx;
    }

    public Mat getCircles() {
        return circles;
    }

    public EnumShape getShapeEnum() {
        return shapeEnum;
    }

    public Mat getPosProcessTest() {
        return posProcessTest;
    }
}

