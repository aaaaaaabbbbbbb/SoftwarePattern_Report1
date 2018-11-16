package topse.pattern.strategy.data;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * JSON形式を扱うクラス
 *
 * @author TOSHI.I
 */
public class JSONViewer implements Viewer {

    /** 表示する時のインデントレベル **/
    private int indent = 0;
    /** 名前出力後に余分なスペースを出さないようにするためのフラグ変数 **/
    private boolean nameOut = false;

    /** 読み出すファイルのパス **/
    String path = null;

    /**
     * コンストラクタ
     *
     * @param _path ファイルのパス
     */
    public JSONViewer(String _path) {
        path = _path;
    }

    /**
     * 指定のファイルパスのJSONファイルを表示する
     */
    public void showData(String _fileString) throws Exception {
        if (_fileString == null) {
            return;
        }

        //文字列をJsonObjectに変換する
        JsonObject jsonValue = Json.parse(_fileString).asObject();
        if (jsonValue == null) {
            return;
        }

        //JSONを表示する
        showJSON(jsonValue);
    }

    /**
     *　JSONデータを表示する
     *
     * @param _jsonValue
     */
    private void showJSON(JsonValue _jsonValue) {
        if (!nameOut) {
            indentSpace(); //必要ならインデント出力
        }
        if (_jsonValue.isObject()) { //引数がオブジェクトの場合
            System.out.println("{");
            nameOut = false;
            for (JsonObject.Member o : _jsonValue.asObject()) { //オブジェクトの各要素について
                indent++;
                String name = o.getName(); //名前を取得
                JsonValue value = o.getValue(); //値を取得
                indentSpace();
                System.out.print(name + ": ");
                nameOut = true;
                showJSON(value); //値に関して再帰呼び出しする
                nameOut = false;
                indent--;
            }
            indentSpace();
            System.out.println("}");
        } else if (_jsonValue.isArray()) { //配列の各要素について
            System.out.println("[");
            nameOut = false;
            for (JsonValue value : _jsonValue.asArray()) { //配列の各要素について
                indent++;
                showJSON(value); //要素に関して再帰呼び出しする
                indent--;
            }
            indentSpace();
            System.out.println("]");
        } else if (_jsonValue.isNull()) {
            System.out.println("NULL");
        } else if (_jsonValue.isBoolean()) {
            System.out.println(_jsonValue.asBoolean());
        } else if (_jsonValue.isNumber()) {
            System.out.println(_jsonValue.asFloat()); //整数か小数かは判断が難しいため、とりあえず一律にfloatにする
        } else if (_jsonValue.isString()) {
            System.out.println(_jsonValue.asString());
        }
    }

    /**
     * インデントを表示する
     */
    private void indentSpace() {
        for (int i = 0; i < indent; i++) {
            System.out.print("   ");
        }
    }
}
