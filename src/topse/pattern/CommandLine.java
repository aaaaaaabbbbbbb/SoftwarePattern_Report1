package topse.pattern;

import topse.pattern.strategy.ContentViewer;
import topse.pattern.strategy.data.Viewer;
import topse.pattern.strategy.stream.Reader;

/**
 * コマンドラインから実行される。
 *
 * @author TOSHI.I
 */
public class CommandLine {

    /**
    * mainクラス
    *
    * @param args コマンドラインから指定されたパラメータ
    */
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            try {
                Viewer v = Viewer.createDataHandler(args[i]);
                if (v == null) {
                    continue;
                }
                Reader r = Reader.createReaderHandler(args[i]);

                ContentViewer cv = new ContentViewer();
                cv.setReader(r);
                cv.setViewer(v);

                cv.performShowData(cv.performRead());
            } catch (Exception e) {
                System.err.println("Invalid filepath:" + args[i]);
            }
        }
    }
}
