package topse.pattern.strategy.data;

/**
 * 特定のデータ形式を扱うクラス
 *
 * @author TOSHI.I
 */
public interface Viewer {

    /** ファイルの拡張子：csv　**/
    public static final String EXTENSION_CSV = "csv";
    /** URIのスキーマ：https　**/
    public static final String EXTENSION_JSON = "json";

    /**
     * データを表示する
     *
     * @param _fileString 表示するデータの文字列
     * @throws Exception ファイルの読み込みに失敗した
     */
    public abstract void showData(String _fileString) throws Exception;

    /**
     * 実装クラスを作成して返す。
     *
     * @param _path 読み込むファイルのパス
     * @return 実装クラス
     */
    public static Viewer createDataHandler(String _path) {
        Viewer dh = null;
        if (_path.endsWith(EXTENSION_CSV)) {
            //csv
            dh = new CSVViewer(_path);
        } else if (_path.endsWith(EXTENSION_JSON)) {
            //json
            dh = new JSONViewer(_path);
        } else {
            return null;
        }
        return dh;
    }
}
