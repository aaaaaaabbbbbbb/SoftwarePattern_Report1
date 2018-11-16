package topse.pattern.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import topse.pattern.util.CommonConstant;

/**
 * 動作テスト用の関数
 *
 */
public class ToolTestRun {

    /**
     * テスト用メイン関数
     * macOS and Linux users have to change the first character ; to :.
     *
     * @param _args 未使用
     */
    public static void main(String[] _args) {
        String classpath = "bin";
        classpath += ";lib/minimal-json-0.9.6-SNAPSHOT.jar";
        commandExecute("java -cp bin topse.pattern.CommandLine rsrcs/sample.csv");
        commandExecute("java -cp " + classpath + " topse.pattern.CommandLine https://server.msyk.net/sample.json");
        commandExecute("java -cp " + classpath + " topse.pattern.CommandLine rsrcs/sample2.json");
        commandExecute("java -cp bin topse.pattern.CommandLine https://raw.githubusercontent.com/aaaaaaabbbbbbb/SoftwarePattern_Report1/master/rsrcs/sample2.csv");
    }

    /**
     * ウィンドウズコマンドを実行する
     *
     * @param _cmd 実行するコマンド
     */
    private static void commandExecute(String _cmd) {
        try {
            Process p = Runtime.getRuntime().exec(_cmd);
            p.waitFor();
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, CommonConstant.CHARCODE_MS932));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                } else {
                    System.out.println("Output: " + line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
