public class Settings {

    private String YANDEX_SPEACH_KIT;
    private String VK_DOCS_TOKEN;
    private String VK_MESSAGES_TOKEN;

    public void setYANDEX_SPEACH_KIT(String YANDEX_SPEACH_KIT) {
        this.YANDEX_SPEACH_KIT = YANDEX_SPEACH_KIT;
    }

    public void setVK_DOCS_TOKEN(String VK_DOCS_TOKEN) {
        this.VK_DOCS_TOKEN = VK_DOCS_TOKEN;
    }

    public void setVK_MESSAGES_TOKEN(String VK_MESSAGES_TOKEN) {
        this.VK_MESSAGES_TOKEN = VK_MESSAGES_TOKEN;
    }

    public String getYANDEX_SPEACH_KIT() {
        return YANDEX_SPEACH_KIT;
    }

    public String getVK_DOCS_TOKEN() {
        return VK_DOCS_TOKEN;
    }

    public String getVK_MESSAGES_TOKEN() {
        return VK_MESSAGES_TOKEN;
    }




    public static void  setSettings () {

        /*
        Создание файла настроек и запись в него данных, если его нет
        Запись данных в файл, если он уже создан
         */

    }

    public static boolean checkFileSettings () {

        /*
            Проверк наличия файла настроек рядом с основным файлов
         */

        return false;
    }

}
