import org.json.JSONObject;

import java.io.*;
import java.nio.CharBuffer;


public class Settings {

    private static final String fileName = "bot.properties";

    private static String YANDEX_SPEACH_KIT;
    private static String VK_DOCS_TOKEN;
    private static String VK_MESSAGES_TOKEN;
    private static String VK_GLOBAL_TOKEN;


    public static void setVkGlobalToken(String vkGlobalToken) {
        VK_GLOBAL_TOKEN = vkGlobalToken;
    }


    public static void setYANDEX_SPEACH_KIT(String YANDEX_SPEACH_KIT) {
        Settings.YANDEX_SPEACH_KIT = YANDEX_SPEACH_KIT;
    }

    public static void setVK_DOCS_TOKEN(String VK_DOCS_TOKEN) {
        Settings.VK_DOCS_TOKEN = VK_DOCS_TOKEN;
    }

    public static void setVK_MESSAGES_TOKEN(String VK_MESSAGES_TOKEN) {
        Settings.VK_MESSAGES_TOKEN = VK_MESSAGES_TOKEN;
    }

    public static String getFileName()          { return fileName; }

    public static String getYANDEX_SPEACH_KIT() {
        return YANDEX_SPEACH_KIT;
    }

    public static String getVK_DOCS_TOKEN()     { return VK_DOCS_TOKEN; }

    public static String getVK_MESSAGES_TOKEN() {
        return VK_MESSAGES_TOKEN;
    }

    public static String getVkGlobalToken()     { return VK_GLOBAL_TOKEN; }




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
            writer = new PrintWriter( getFileName(), "UTF-8");
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
            Проверка наличия файла настроек рядом с основным файлов
         */

        File file = new File(getFileName());

            if (file.exists()) {
                System.out.println("[checkFileSettings]" + "\n" + "File \"bot.properties\" is finded.");
                return true;
            } else {
                System.out.println("[checkFileSettings]" + "\n" + "File \"bot.properties\" is NOT finded.");
                return false;
            }
    }

    public static void loadSettings () {

        /*
        Загрузка настроек из файла bot.settings
        */

        String bot_properties = null;
        FileInputStream fileInputStream = null;
        byte[] bytesFile = new byte[0];

        try {
            fileInputStream = new FileInputStream(getFileName());
            bytesFile = new byte[fileInputStream.available()];
            fileInputStream.read(bytesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bot_properties = new String(bytesFile);

        JSONObject jsonObject = new JSONObject(bot_properties);

        //setVK_DOCS_TOKEN(jsonObject.getString("VK_DOCS_TOKEN"));
        //setVK_MESSAGES_TOKEN("VK_MESSAGES_TOKEN");
        //setYANDEX_SPEACH_KIT("YANDEX_SPEACH_KIT");

        System.out.println("[loadSettings]" + "\n" + "content of file is\"bot.properties\": " + bot_properties);



    }


}
