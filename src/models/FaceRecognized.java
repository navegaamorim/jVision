package models;

/**
 * @author 8130031
 *
 * Result given by {@link jvision.JVisionRecognition} to {@link interfaces.BehaviourRecognition}
 * Contains an object with founded face and {@link OutputJVisionImage} with necessary information to draw a image
 *
 */
public class FaceRecognized {

    private String name;
    private OutputJVisionImage imageOut;

    private long time;
    private double confidence;

    public FaceRecognized() {
        this.imageOut = new OutputJVisionImage();
    }

    public FaceRecognized(String name, OutputJVisionImage imageOut, long time, double confidence) {
        this.name = name;
        this.imageOut = imageOut;
        this.time = time;
        this.confidence = confidence;
    }

    public String getName() {
        return name;
    }

    public double getConfidence() {
        return confidence;
    }

    public OutputJVisionImage getImageOut() {
        return imageOut;
    }

    public long getTime() {
        return time;
    }
}
