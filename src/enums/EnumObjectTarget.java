package enums;

/**
 * @author 8130031
 */
public enum EnumObjectTarget {

    FACE, EYES, BODY;

    public static String getCascade(EnumObjectTarget target) {
        switch (target) {
            case BODY:
                return "resources/haarcascade_fullbody.xml";
            case EYES:
                return "resources/haarcascade_eye.xml";
        }
        //face
        return "resources/haarcascade_frontalface_alt.xml";
    }

}
