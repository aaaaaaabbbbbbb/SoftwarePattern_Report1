package topse.pattern.strategy.stream;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * FILE関連処理をまとめたクラス
 *
 * @author TOSHI.I
 */
public class FileReader implements Reader {

    /** 処理結果：OK　**/
    private static final int FILEHANDLER_OK = 0;
    /** 処理結果：ファイルが存在しない　**/
    private static final int FILEHANDLER_NOTFOUND = 1;
    /** 処理結果：ファイルパス指定が不正　**/
    private static final int FILEHANDLER_INVALIDPATH = 2;

    /**　読み出すファイルのオブジェクト **/
    File file = null;

    /** ファイルのパス **/
    String path = null;

    /**
     * コンストラクタ
     *
     * @param _path ファイルのパス
     */
    public FileReader(String _path) {
        path = _path;
    }

    /**
     * CSVファイルを読み出す
     *
     * @return 読み出したファイルを文字列にしたもの
     * @throws Exception 読み出しに失敗した
     */
    public InputStream getStream() throws Exception {

        if (checkFileExisting(path) != FILEHANDLER_OK) {
            throw new Exception();
        }

        return new DataInputStream(new FileInputStream(path));
    }

    /**
     * 指定されたパスにファイルが存在するかを調べる
     *
     * @param _path ファイルのパス
     * @return 処理結果
     */
    private int checkFileExisting(String _path) {

        //nullチェック
        if (_path == null) {
            return FILEHANDLER_INVALIDPATH;
        }

        //Fileクラスのオブジェクトを確保
        file = new File(_path);
        if (!file.exists()) {
            return FILEHANDLER_NOTFOUND;
        }
        return FILEHANDLER_OK;
    }
}
