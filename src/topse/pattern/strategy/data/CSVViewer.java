package topse.pattern.strategy.data;

import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.StringTokenizer;

import topse.pattern.util.CommonConstant;

/**
 * CSV形式を扱うクラス
 *
 * @author TOSHI.I
 */
public class CSVViewer implements Viewer {

    /** 読み出すファイルのパス **/
    String path = null;

    /**
     * コンストラクタ
     *
     * @param _path ファイルのパス
     */
    public CSVViewer(String _path) {
        path = _path;
    }

    /**
     * 指定のファイルパスのCSVファイルを表示する
     */
    public void showData(String _fileString) throws Exception {
        if (_fileString == null) {
            return;
        }

        //行ごとに分割する
        String[] separatedData = _fileString.split("\n");
        if (separatedData == null) {
            return;
        }

        //最大幅を調べる
        ArrayList<Integer> maxWidths = new ArrayList<Integer>();
        int maxCols = getMaxWidth(separatedData, maxWidths);

        //CSVを表示する
        showCSV(separatedData, maxWidths, maxCols);
    }

    /**
     * 行ごとの幅を調べて、最大幅を返す
     *
     * @param _separatedData 行ごとのデータ
     * @param _maxWidths 各行の幅
     * @return 最大幅
     */
    private int getMaxWidth(String[] _separatedData, ArrayList<Integer> _maxWidths) {
        //列数の最大値を求める変数
        int maxCols = -1;
        StringJoiner message = new StringJoiner(",", "[", "]"); //各列のバイト数を文字列として繋げる

        //CSVの各行について
        for (int i = 0; i < _separatedData.length; i++) {
            //行を,で分割する（データとして,があるものは考慮していない）
            StringTokenizer token = new StringTokenizer(_separatedData[i], ",");
            if (maxCols < token.countTokens()) { //列数を最大値と比較して
                maxCols = token.countTokens(); //それよりも多ければそれを最大値とする
            }
            int col = 0; //列番号のカウンタ
            while (token.hasMoreTokens()) { //それぞれの列について
                String item = token.nextToken(); //列の文字列データ
                int byteLen = sjisLength(item); //データのShift-JISバイト数
                try {
                    int colW = _maxWidths.get(col); //その列の最大バイト数を得る
                    if (colW < byteLen) { //最大バイト数より多い場合
                        _maxWidths.set(col, byteLen); //最大バイト数として設定する
                    }
                } catch (IndexOutOfBoundsException ex) { //getで例外が出る場合、
                    _maxWidths.add(byteLen); //その列の要素が配列にないので、追加で作る
                }
                col += 1; //列カウンタを進める
            }
        }

        //各列の列数を表示するが、最大30バイトとして、それより長い列は強制的に30バイトにする
        int counter = 0; //列カウンターとなる変数
        for (Integer n : _maxWidths) { //各列のバイト数がある配列のそれぞれの要素について
            if (n > 30) {
                //30を超えている場合は30にする
                _maxWidths.set(counter, 30);
            }
            message.add(_maxWidths.get(counter).toString()); //現在の列のバイト数を追加
            counter += 1; //列カウンターを進める
        }
        System.out.println("Detected Columns: " + maxCols + message); //コンソールに出力

        return maxCols;
    }

    /**
     * CSVを表示する
     *
     * @param _separatedData 行ごとのデータ
     * @param _maxWidths 各行の幅
     * @param _maxCols 最大幅
     */
    private void showCSV(String[] _separatedData, ArrayList<Integer> _maxWidths, int _maxCols) {
        //テーブル形式に出力（横の線）
        outputTableHorizontalLine(_maxWidths);

        //テーブル形式に出力
        //CSVの各行について
        for (int i = 0; i < _separatedData.length; i++) {
            //配列の配列を記録するオブジェクトを用意する
            ArrayList<ArrayList<String>> lineBuffer = new ArrayList<ArrayList<String>>();

            //データのフィールドを折り返して最大行数を取得する
            int maxLines = separateFieldtoLines(_separatedData[i], lineBuffer, _maxWidths);

            //テーブル形式に出力（縦の線）
            outputTableVerticalLine(lineBuffer, _maxWidths, maxLines, _maxCols);

            //テーブル形式に出力（横の線）
            outputTableHorizontalLine(_maxWidths);
        }
    }

    /**
     * 1つのフィールドについて、複数行になることを考慮して、行ごとに、さらに複数行にあらかじめ分離しておく
     *
     * @param _line 対象のデータ行の文字列
     * @param _lineBuffer 各データ列をデータごとに文字列に分割したものを格納する
     * @param _maxWidths 各行の幅
     * @return このデータの最大行数
     */
    private int separateFieldtoLines(String _line, ArrayList<ArrayList<String>> _lineBuffer, ArrayList<Integer> _maxWidths) {

        //列カウンタ
        int col = 0;
        //フィールドを折り返したときに最大何行に別れるか
        int maxLines = -1;
        //データ行を,で分割する（データとして,があるものは考慮していない）
        StringTokenizer token = new StringTokenizer(_line, ",");
        //それぞれの列について
        while (token.hasMoreTokens()) {
            _lineBuffer.add(new ArrayList<String>());
            //列用の配列をまず生成する。最初は要素はない
            String item = token.nextToken();
            //列の文字列を取り出す
            int colWidth = _maxWidths.get(col);
            int st = 0, lineCount = 0;
            //それぞれ、分割文字の最初の位置と、分割行数カウンタ
            for (int j = 0; j <= item.length(); j++) {
                //切れ目を探すために、文字列を順番に取り出す
                String part = item.substring(st, j);
                //部分的に取り出す文字列の候補
                if (sjisLength(part) > colWidth || j == item.length()) {
                    //バイト数が超えるか、最後の文字なら
                    _lineBuffer.get(col).add(item.substring(st, j - (j == item.length() ? 0 : 1)));
                    //列用の配列に、部分文字列を追加する。最後の場合と途中の場合で2つ目の引数が変わる
                    lineCount += 1; //行カウンタが増える
                    maxLines = lineCount > maxLines ? lineCount : maxLines; //1行分が最大何行に別れるかを求める
                    st = j - 1; //分割ポインタを進める
                }
            }
            //列カウンタを進める
            col += 1;
        }

        return maxLines;
    }

    /**
     * テーブル形式に出力（横の線）
     *
     * @param _maxWidths 各列の幅
     */
    private void outputTableHorizontalLine(ArrayList<Integer> _maxWidths) {
        StringJoiner message = new StringJoiner("-+-", "+-", "-+");
        for (Integer elm : _maxWidths) { //各列のバイト数がある配列のそれぞれの要素について
            message.add(repeatString("-", elm)); //列数分、マイナス記号を繰り返す
        }
        System.out.println(message); //コンソールに出力
    }

    /**
     * テーブル形式に出力（縦の線）
     *
     *　@param _lineBuffer
     * @param _maxWidths 各列の幅
     * @param _maxLines このデータ行の最大の行数
     * @param _maxCols 全データの最大の列幅
     */
    private void outputTableVerticalLine(ArrayList<ArrayList<String>> _lineBuffer, ArrayList<Integer> _maxWidths, int _maxLines, int _maxCols) {
        for (int j = 0; j < _maxLines; j++) { //CSVのレコードを複数の行に渡って出力する
            StringJoiner message = new StringJoiner(" | ", "| ", " |"); //1行分の文字列を接続で作る
            for (int k = 0; k < _maxCols; k++) { //列の数だけ繰り返す
                String item = "";
                try {
                    item = _lineBuffer.get(k).get(j); //列のその行の文字列を取り出す
                } catch (Exception e) { //文字列がないとgetで例外が出るが、無視する。その時は、itemは""
                    // ignored
                }
                message.add(item + repeatString(" ", _maxWidths.get(k) - sjisLength(item)));
                //文字列接続オブジェクトに追加するが、幅を決められたバイト数取るための空白の追加が必要
            }
            System.out.println(message);
        }
    }

    /**
     * 文字列をShift-JISでエンコードした時のバイト数を求める
     *
     * @param s バイト数を求める文字列
     * @return バイト数
     */
    private int sjisLength(String s) {
        int byteLen = 0;
        try {
            byteLen = s.getBytes(CommonConstant.CHARCODE_SJIS).length;
        } catch (Exception e) {
            e.printStackTrace(); //例外があれば、0を返してとりあえず続ける
        }
        return byteLen;
    }

    /**
     * 引数にし指定した文字列を繰り返した文字列を返す（Java11ではStringクラスにrepeatメソッドがあるそうです）
     *
     * @param s 処理対象の文字列
     * @param times 折り返す文字数
     * @return 折り返した文字列
     */
    private String repeatString(String s, int times) {
        StringJoiner aItem = new StringJoiner("");
        for (int c = 0; c < times; c++) {
            aItem.add(s);
        }
        return aItem.toString();
    }

}
