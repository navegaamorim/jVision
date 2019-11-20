package jvision;

import enums.EnumShape;
import models.OutputColorContour;
import models.OutputJVisionImage;
import models.OutputOpticalFlow;
import models.OutputShape;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import utils.Utils;

import java.util.ArrayList;

/**
 * @author 8130031
 *
 * Draws founded detections, receives necessary information to draw them
 * and send to {@link JVisionDetection} a {@link Mat} object
 *
 */
public class JVisionDetectionFactory {

    private JVisionConfig visionConfig;

    protected JVisionDetectionFactory(JVisionConfig visionConfig) {
        this.visionConfig = visionConfig;
    }

    protected OutputJVisionImage buildImage(Mat frame, ArrayList<Rect[]> objects, OutputShape shapes, OutputColorContour contour, OutputOpticalFlow opticalFlowOut) {
        boolean targetFound = false;

        Mat posProcess = frame;

        //objects
        if (objects.size() > 0) {
            for (Rect[] object : objects) {
                for (Rect rect : object) {
                    Imgproc.rectangle(frame, rect.tl(), rect.br(), visionConfig.getLineColor().getColor(), visionConfig.getLineThickness());

                    targetFound = true;
                }
            }
        }

        //shapes
        if (null != shapes) {
            if (shapes.getShapeEnum() == EnumShape.CIRCLE) {
                Mat circles = shapes.getCircles();

                for (int i = 0; i < circles.cols(); i++) {
                    double circle[] = circles.get(0, i);
                    if (circles == null)
                        break;

                    Point pt = new Point(Math.round(circle[0]), Math.round(circle[1]));
                    int radius = (int) Math.round(circle[2]);

                    Imgproc.circle(frame, pt, radius, visionConfig.getLineColor().getColor(), visionConfig.getLineThickness());
                    Imgproc.circle(frame, pt, 3, visionConfig.getLineColor().getColor(), visionConfig.getLineThickness());

                    targetFound = true;
                    posProcess = shapes.getPosProcessTest();
                }

            } else {
                Imgproc.drawContours(frame, shapes.getContours(), shapes.getCountoursIdx(), visionConfig.getLineColor().getColor(), visionConfig.getLineThickness());

                targetFound = true;
                posProcess = shapes.getPosProcessTest();
            }
        }

        //contours
        if (null != contour) {

            if (contour.getHierarchy().size().height > 0 && contour.getHierarchy().size().width > 0) {
                for (int idx = 0; idx >= 0; idx = (int) contour.getHierarchy().get(0, idx)[0]) {
                    Imgproc.drawContours(frame, contour.getContours(), idx, contour.getContourObject().getColor(), visionConfig.getLineThickness());

                    targetFound = true;
                    posProcess = contour.getPosProcessTest();
                }
            }
        }

        //optical flow
        if (null != opticalFlowOut) {
            int x, y;
            Point prevPoint, nextPoint;

            y = opticalFlowOut.getByteStatus().size() - 1;

            for (x = 0; x < y; x++) {
                if (opticalFlowOut.getByteStatus().get(x) == 1) {

                    prevPoint = opticalFlowOut.getCornersThis().get(x);
                    nextPoint = opticalFlowOut.getCornersPrev().get(x);

                    Imgproc.circle(frame, prevPoint, 10, visionConfig.getLineColor().getColor());
                    Imgproc.line(opticalFlowOut.getMask(), prevPoint, nextPoint, visionConfig.getLineColor().getColor(), visionConfig.getLineThickness());
                }
            }

            Mat mat = new Mat();
            Core.add(opticalFlowOut.getMask(), frame, mat);
            targetFound = true;
        }

        return new OutputJVisionImage(posProcess, frame, targetFound);
    }
}
