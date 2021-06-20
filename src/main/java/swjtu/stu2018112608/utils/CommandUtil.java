package swjtu.stu2018112608.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandUtil {
    public static boolean execCommand(String cmd) {
        Process process = null;
        BufferedReader br = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            return false;
        } finally {
            try {
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
