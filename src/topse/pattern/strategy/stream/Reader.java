package topse.pattern.strategy.stream;

import java.io.InputStream;

/**
 * ストリーム関連処理をまとめたクラス
 *
 * @author TOSHI.I
 */
public interface Reader {

    /** URIのスキーマ：http　**/
    public static final String URISCHEME_HTTP = "http:";
    /** URIのスキーマ：https　**/
    public static final String URISCHEME_HTTPS = "https:";

    /**
    * ファイルを読み出す
    *
    * @return 読み出したファイルを文字列にしたもの
    * @throws Exception ファイルの読み出しに失敗した
    */
    public abstract InputStream getStream() throws Exception;

    /**
     * 実装クラスを作成して返す。
     *
     * @param _path 対象ファイルのパス
     * @return 実装クラス
     */
    public static Reader createReaderHandler(String _path) {
        Reader rh = null;
        if (_path.startsWith(URISCHEME_HTTP)
                || _path.startsWith(URISCHEME_HTTPS)) {
            //httpまたはhttps
            rh = new HttpReader(_path);
        } else {
            //ローカルファイル
            rh = new FileReader(_path);
        }
        return rh;
    }

}
