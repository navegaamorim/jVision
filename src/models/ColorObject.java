package models;

import enums.EnumColor;
import org.opencv.core.Scalar;

/**
 * @author 8130031
 *
 * Creates new colors {@link EnumColor}, with default HSV values
 * Allows definition of new colors by hsv values
 *
 */
public class ColorObject {

    private Scalar min_hsv, max_hsv, color;
    private EnumColor contourColor;

    public ColorObject(EnumColor contourColor) {
        this.contourColor = contourColor;

        switch (contourColor) {
            case BLUE:
                min_hsv = new Scalar(92, 0, 0);
                max_hsv = new Scalar(124, 256, 256);
                color = new Scalar(255, 0, 0);
                break;
            case GREEN:
                min_hsv = new Scalar(34, 50, 50);
                max_hsv = new Scalar(80, 220, 220);
                color = new Scalar(0, 255, 0);
                break;
            case RED:
                min_hsv = new Scalar(0, 200, 0);
                max_hsv = new Scalar(19, 255, 255);
                color = new Scalar(0, 0, 255);
                break;
            case YELLOW:
                min_hsv = new Scalar(20, 124, 123);
                max_hsv = new Scalar(30, 256, 256);
                color = new Scalar(0, 255, 255);
                break;

        }
    }


    public ColorObject(Scalar min_hsv, Scalar max_hsv, Scalar color) {
        this.min_hsv = min_hsv;
        this.max_hsv = max_hsv;
        this.color = color;
    }

    public Scalar getMin_hsv() {
        return min_hsv;
    }

    public void setMin_hsv(Scalar min_hsv) {
        this.min_hsv = min_hsv;
    }

    public Scalar getMax_hsv() {
        return max_hsv;
    }

    public void setMax_hsv(Scalar max_hsv) {
        this.max_hsv = max_hsv;
    }

    public Scalar getColor() {
        return color;
    }

    public void setColor(Scalar color) {
        this.color = color;
    }

    public EnumColor getContourColor() {
        return contourColor;
    }

    public void setContourColor(EnumColor contourColor) {
        this.contourColor = contourColor;
    }
}
