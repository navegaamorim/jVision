package interfaces;

import models.FaceRecognized;

/**
 * @author 8130031
 *
 * Behaviour to handle the result of {@link jvision.JVisionRecognition}
 *
 */
public interface BehaviourRecognition {

    void actionRecognition(FaceRecognized faceRecognized);

}
