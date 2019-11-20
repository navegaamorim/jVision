package jvision;

import enums.EnumShape;
import interfaces.Detection;
import models.OutputShape;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;

/**
 * @author 8130031
 *
 * Allows finding geometric shapes defined by {@link EnumShape} in a {@link Mat} object
 *
 */
public class DetectionShape implements Detection {

    private static double DEFAULT_AREA = 1000;

    private EnumShape shapeEnum;
    private double minimum_area;

    public DetectionShape(EnumShape shapeenum) {
        this.shapeEnum = shapeenum;
        this.minimum_area = DEFAULT_AREA;
    }

    public DetectionShape(EnumShape shapeEnum, double minimum_area) {
        this.shapeEnum = shapeEnum;
        this.minimum_area = minimum_area;
    }

    protected OutputShape detectShape(Mat frame) {

        if (shapeEnum == EnumShape.CIRCLE) {
            Mat gray = new Mat();
            Mat circles = new Mat();

            Imgproc.cvtColor(frame, gray, COLOR_BGR2GRAY);
            Imgproc.GaussianBlur(gray, gray, new Size(9, 9), 2, 2);

            Imgproc.HoughCircles(gray, circles, Imgproc.CV_HOUGH_GRADIENT, 2.0, gray.rows() / 8, 100, 300, 20, 400);

            return new OutputShape(gray, shapeEnum, circles);

        } else {

            Mat blurredImage = new Mat();
            Mat gray = new Mat();
            Mat morphologicOutput = new Mat();
            Mat canny = new Mat();

            // convert to gray
            Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
            // remove noise
            Imgproc.blur(gray, blurredImage, new Size(3, 3));
            //canny edge detection
            Imgproc.Canny(blurredImage, canny, 50, 255);

            //morphological transformations
            Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2));
            Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, 1));

            Imgproc.erode(canny, morphologicOutput, erodeElement);
            Imgproc.dilate(morphologicOutput, morphologicOutput, dilateElement);

            //find countours and draw them
            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();

            // find contours
            Imgproc.findContours(morphologicOutput, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

            for (int i = 0; i < contours.size(); ++i) {

                MatOfPoint contour = contours.get(i);

                MatOfPoint2f thisContour2f = new MatOfPoint2f();
                MatOfPoint approxContour = new MatOfPoint();
                MatOfPoint2f approxContour2f = new MatOfPoint2f();

                contour.convertTo(thisContour2f, CvType.CV_32FC2);

                double epsilon = Imgproc.arcLength(thisContour2f, true) * 0.02;
                Imgproc.approxPolyDP(thisContour2f, approxContour2f, epsilon, true);
                approxContour2f.convertTo(approxContour, CvType.CV_32S);


                double area = Imgproc.contourArea(contour);
                if (area > minimum_area) {//&& Imgproc.isContourConvex(contour

                    double sizes = approxContour.size().height;

                    System.out.println(".. " + sizes);

                    if (sizes == 3 && shapeEnum == EnumShape.TRIANGLE) { //triangle
                        return new OutputShape(morphologicOutput, shapeEnum, contours, i);
                    }

                    if (sizes == 4 && (shapeEnum == EnumShape.SQUARE || shapeEnum == EnumShape.RECTANGLE)) {
                        //bounding rect
                        Rect rect = Imgproc.boundingRect(approxContour);
                        float ratio = (float) rect.height / rect.width; // (>0.9 && <1.1 square ratio)

                        if ((ratio >= 0.90 && ratio <= 1.10) && shapeEnum == EnumShape.SQUARE) { //square
                            return new OutputShape(morphologicOutput, shapeEnum, contours, i);

                        } else if (shapeEnum == EnumShape.RECTANGLE) { //rectangle

                            return new OutputShape(morphologicOutput, shapeEnum, contours, i);
                        }
                    }

                    if (sizes == 5 && shapeEnum == EnumShape.PENTAGON) {
                        return new OutputShape(morphologicOutput, shapeEnum, contours, i);
                    }
                }
            }
            return new OutputShape(morphologicOutput, shapeEnum);

        }

    }

}
