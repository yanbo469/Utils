package yanbo.utils.loveview;

import android.graphics.Path;

/**
 * @author Cuizhen
 * @date 2018/6/14-上午9:44
 */
class PathUtils {

    static Path getPathLeft(int width, int height, float topOff, float bottomOff) {
        Path path = getPathBrokenLine(width, height, topOff, bottomOff);
        path.lineTo(0, height);
        path.lineTo(0, 0);
        path.close();
        return path;
    }

    static Path getPathRight(int width, int height, float topOff, float bottomOff) {
        Path path = getPathBrokenLine(width, height, topOff, bottomOff);
        path.lineTo(width, height);
        path.lineTo(width, 0);
        path.close();
        return path;
    }

    private static Path getPathBrokenLine(int width, int height, float topOff, float bottomOff) {
        Path path = new Path();
        float halfWidth = width / 2;
        float offWidth = halfWidth / 10;
        float offHeight = (height * (1 - topOff - bottomOff)) / 8;
        path.moveTo(halfWidth, 0);
        path.lineTo(halfWidth, height * topOff);
        path.lineTo(halfWidth + offWidth, height * topOff + offHeight * 1);
        path.lineTo(halfWidth - offWidth, height * topOff + offHeight * 3);
        path.lineTo(halfWidth + offWidth, height * topOff + offHeight * 5);
        path.lineTo(halfWidth - offWidth, height * topOff + offHeight * 7);
        path.lineTo(halfWidth, height * (1 - bottomOff));
        path.lineTo(halfWidth, height);
        return path;
    }
}
