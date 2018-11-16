package topse.pattern.strategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import topse.pattern.strategy.data.Viewer;
import topse.pattern.strategy.stream.Reader;
import topse.pattern.util.CommonConstant;

/**
 * Contextクラス
 *
 * @author TOSHI.I
 */
public class ContentViewer {
    /** Viewストラテジ **/
    Viewer viewer = null;
    /** Streamストラテジ **/
    Reader reader = null;

    /**
     * コンストラクタ
     */
    public ContentViewer() {
    }

    /**
     *　 Viewストラテジをセットする。
     *
     * @param _viewer Viewストラテジ実装クラス
     */
    public void setViewer(Viewer _viewer) {
        viewer = _viewer;
    }

    /**
     *　 Readerストラテジをセットする。
     *
     * @param _reader Readerストラテジ実装クラス
     */
    public void setReader(Reader _reader) {
        reader = _reader;
    }

    /**
     * ファイルの読み出しを実行する。
     *
     * @return 読み出したファイルの文字列
     * @throws Exception ファイルの読み出しに失敗した
     */
    public String performRead() throws Exception {
        return readStream(performGetStream());
    }

    /**
     * 読み出したデータを表示する。
     *
     * @param _fileString 読み出したファイルの文字列
     * @throws Exception ファイルの読み出しに失敗した
     */
    public void performShowData(String _fileString) throws Exception {
        viewer.showData(_fileString);
    }

    /**
     * InputStreamを習得する
     *
     * @return InputStream
     * @throws Exception InputStreamの取得に失敗した
     */
    private InputStream performGetStream() throws Exception {
        return reader.getStream();
    }

    /**
     * InputStreamをバイト配列に変換する
     *
     * @param is InputStream
     * @return バイト配列
     */
    private String readStream(InputStream is) {
        //ファイルを開いて読む
        BufferedReader br = null;
        String line = null;
        String lines = "";
        try {
            br = new BufferedReader(new InputStreamReader(is, CommonConstant.CHARCODE_UTF8));
            while ((line = br.readLine()) != null) {
                lines += line;
                lines += "\n";
            }
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                //ファイルを閉じる
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
