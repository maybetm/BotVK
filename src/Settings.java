import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Settings {

    private static String YANDEX_SPEACH_KIT = "1212";
    private static String VK_DOCS_TOKEN = "222222";
    private static String VK_MESSAGES_TOKEN = "121212";

    public static void setYANDEX_SPEACH_KIT(String YANDEX_SPEACH_KIT) {
        Settings.YANDEX_SPEACH_KIT = YANDEX_SPEACH_KIT;
    }

    public static void setVK_DOCS_TOKEN(String VK_DOCS_TOKEN) {
        Settings.VK_DOCS_TOKEN = VK_DOCS_TOKEN;
    }

    public static void setVK_MESSAGES_TOKEN(String VK_MESSAGES_TOKEN) {
        Settings.VK_MESSAGES_TOKEN = VK_MESSAGES_TOKEN;
    }

    public static String getYANDEX_SPEACH_KIT() {
        return YANDEX_SPEACH_KIT;
    }

    public static String getVK_DOCS_TOKEN() {
        return VK_DOCS_TOKEN;
    }

    public static String getVK_MESSAGES_TOKEN() {
        return VK_MESSAGES_TOKEN;
    }




    public static void  setSettings () {

        /*
        Создание файла настроек "bot.settings" и запись в него данных, если его нет
        Запись данных в файл, если он уже создан
         */

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("YANDEX_SPEACH_KIT", getYANDEX_SPEACH_KIT());
        jsonObject.put("VK_DOCS_TOKEN", getVK_DOCS_TOKEN());
        jsonObject.put("VK_MESSAGES_TOKEN", getVK_MESSAGES_TOKEN());

        PrintWriter writer = null;
        try {
            writer = new PrintWriter("bot.properties", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.println(jsonObject);
        writer.close();

        System.out.println("[setSetting]" + "\n" + "Saving settings in file \"bot.settings\"" + "\n" +
        "String config: " + jsonObject.toString());


    }

    public static boolean checkFileSettings () {

        /*
            Проверк наличия файла настроек рядом с основным файлов
         */

        return false;
    }

    public void loadSettings () {

        /*
        Загрузка настроек из файла bot.settings
         */

    }

}
