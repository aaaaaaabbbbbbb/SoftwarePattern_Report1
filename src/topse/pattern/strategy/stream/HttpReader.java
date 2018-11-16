package topse.pattern.strategy.stream;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * HTTP関連処理をまとめたクラス
 *
 * @author TOSHI.I
 */
public class HttpReader implements Reader {

    /** メソッド名：Get　**/
    private static final String METHOD_GET = "GET";

    /** 処理結果：OK　**/
    private static final int HTTPHANDLER_OK = 0;
    /** 処理結果：URLの指定不正　**/
    private static final int HTTPHANDLER_INVALIDURL = 1;
    /** 処理結果：接続に失敗した　**/
    private static final int HTTPHANDLER_CONNECTIONFAILURE = 2;

    /** コネクションのインスタンス **/
    HttpURLConnection con = null;
    /** ファイルのパス **/
    String path = null;

    /**
     * コンストラクタ
     *
     * @param _path ファイルのパス
     */
    public HttpReader(String _path) {
        path = _path;
    }

    /**
     * HTTP経由でファイルを読み出す
     *
     * @param _url ファイルのURL
     * @return 読み出したファイルを文字列にしたもの
     * @throws Exception レスポンスの読み出しに失敗した
     */
    public InputStream getStream() throws Exception {

        //HttpURLConnectionを生成
        int ret = openConnetion(path);
        if (ret != HttpReader.HTTPHANDLER_OK) {
            throw new Exception();
        }

        //Getリクエストを送信する
        ret = requestGet();
        if (ret != HttpReader.HTTPHANDLER_OK) {
            throw new Exception();
        }

        //レスポンスのステータスコードを判定する
        ret = checkResponseCode();
        if (ret != HttpReader.HTTPHANDLER_OK) {
            throw new Exception();
        }

        return new DataInputStream(con.getInputStream());
    }

    /**
     * 指定のURLへ接続するHttpURLConnectionを生成する
     *
     * @param _urlstring 指定されたURLの文字列
     * @return 処理結果
     */
    private int openConnetion(String _urlstring) {
        try {
            if (_urlstring == null) {
                return HTTPHANDLER_INVALIDURL;
            }

            URL url = new URL(_urlstring);
            con = (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return HTTPHANDLER_INVALIDURL;
        } catch (IOException e) {
            e.printStackTrace();
            return HTTPHANDLER_CONNECTIONFAILURE;
        }
        return HTTPHANDLER_OK;
    }

    /**
     * Getリクエストを行う
     *
     * @return 処理結果
     */
    private int requestGet() {
        try {
            //処理に専念
            con.setAllowUserInteraction(false);
            //リダイレクトは受け入れて処理する
            con.setInstanceFollowRedirects(true);
            con.setRequestMethod(METHOD_GET);
            //接続
            con.connect();
        } catch (ProtocolException e) {
            //固定値のため発生しない
            e.printStackTrace();
            return HTTPHANDLER_CONNECTIONFAILURE;
        } catch (IOException e) {
            e.printStackTrace();
            return HTTPHANDLER_CONNECTIONFAILURE;
        }
        return HTTPHANDLER_OK;
    }

    /**
     * StatusCodeを取得し、OKかを判定する。
     *
     * @return <pre>
     *  {@link HttpReader#HTTPHANDLER_OK} 200 OKを取得した
     *  {@link HttpReader#HTTPHANDLER_CONNECTIONFAILURE} 200 OK以外の値を取得した。または取得に失敗した。
     * </pre>
     */
    private int checkResponseCode() {
        int statusCode;
        try {
            statusCode = con.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                return HTTPHANDLER_OK;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return HTTPHANDLER_CONNECTIONFAILURE;
        }
        return HTTPHANDLER_CONNECTIONFAILURE;
    }
}
