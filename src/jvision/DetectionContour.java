package jvision;

import enums.EnumColor;
import interfaces.Detection;
import models.ColorObject;
import models.OutputColorContour;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * @autor 8130031
 *
 * Allows finding a {@link ColorObject} in {@link Mat} object
 *
 */
public class DetectionContour implements Detection {

    //class variables
    private ColorObject contourObject;

    public DetectionContour(EnumColor contourColor) {
        this.contourObject = new ColorObject(contourColor);
    }

    protected OutputColorContour detectContours(Mat frame) {
        Mat blurredImage = new Mat();
        Mat hsvImage = new Mat();
        Mat mask = new Mat();
        Mat morphologicOutput = new Mat();

        // remove noise
        Imgproc.blur(frame, blurredImage, new Size(7, 7));
        // convert to hsv
        Imgproc.cvtColor(blurredImage, hsvImage, Imgproc.COLOR_BGR2HSV);

        Scalar minValues = contourObject.getMin_hsv();
        Scalar maxValues = contourObject.getMax_hsv();

        // threshold HSV image
        Core.inRange(hsvImage, minValues, maxValues, mask);

        // morphological operators, dilate with large element, erode with small ones
        Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(24, 24));
        Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12, 12));
        Imgproc.erode(mask, morphologicOutput, erodeElement);
        Imgproc.erode(morphologicOutput, morphologicOutput, erodeElement);
        Imgproc.dilate(morphologicOutput, morphologicOutput, dilateElement);
        Imgproc.dilate(morphologicOutput, morphologicOutput, dilateElement);

        //find countours and draw them
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        boolean countoursFounded = false;

        // find contours
        Imgproc.findContours(morphologicOutput, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

        return new OutputColorContour(morphologicOutput, hierarchy, contours, contourObject);

    }

}
