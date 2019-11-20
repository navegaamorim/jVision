package jvision;

import enums.EnumColor;
import models.ColorObject;
import org.opencv.core.Scalar;

import java.util.concurrent.TimeUnit;

/**
 * @author 8130031
 *
 * Contains default settings, and allows settings modification
 * -Frame rate of capturing image process
 * -Frame rate time unit
 * -Color of drawed lines in output images
 * -Thickness of drawed lines in output images
 *
 */
public class JVisionConfig {

    //service configs
    private static int DEFAULT_FRAME_RATE = 60;
    private static TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MILLISECONDS;

    //line configs
    private static ColorObject DEFAULT_LINECOLOR = new ColorObject(EnumColor.GREEN);
    private static int DEFAULT_THICKNESS = 2;


    private int frameRate;
    private TimeUnit timeUnit;

    private ColorObject lineColor;
    private int lineThickness;


    protected JVisionConfig() {
        lineColor = DEFAULT_LINECOLOR;
        lineThickness = DEFAULT_THICKNESS;

        frameRate = DEFAULT_FRAME_RATE;
        timeUnit = DEFAULT_TIME_UNIT;
    }

    public JVisionConfig(ColorObject lineColor, int lineThickness) {
        this.lineColor = lineColor;
        this.lineThickness = lineThickness;

        frameRate = DEFAULT_FRAME_RATE;
        timeUnit = DEFAULT_TIME_UNIT;
    }

    public JVisionConfig(int frameRate, TimeUnit timeUnit) {
        this.frameRate = frameRate;
        this.timeUnit = timeUnit;

        lineColor = DEFAULT_LINECOLOR;
        lineThickness = DEFAULT_THICKNESS;
    }

    public JVisionConfig(int frameRate, TimeUnit timeUnit, ColorObject lineColor, int lineThickness) {
        this.frameRate = frameRate;
        this.timeUnit = timeUnit;
        this.lineColor = lineColor;
        this.lineThickness = lineThickness;
    }

    protected int getFrameRate() {
        return frameRate;
    }

    protected TimeUnit getTimeUnit() {
        return timeUnit;
    }

    protected ColorObject getLineColor() {
        return lineColor;
    }

    protected int getLineThickness() {
        return lineThickness;
    }
}
