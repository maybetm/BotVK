
import java.io.*;

public class BotLogic {

    public static String getAnswer (String message) throws NullPointerException {

        /*
        Функция работает в тестовом режиме
        Возвращает два ответа:
        -"Тест пройден"
        -"Ключивых слов не было найденно"

        Метод ищет ключевые слова по типу:
        "Лукас, тест"
         */
        String mes = null;

       // mes = new String(message.getBytes("cp1251"),"utf-8");

        if (message.contains("Лукас,") && message.contains("тест")) {
                return "Тест успешно пройден.";
        } else {
                return "Ключевых слов в сообщении не найдено. Текст сообщения: " + message ;
        }

    }

    public static void sendVoiceMessage (String text, String peer_id) throws IOException {

        /*
        Отправляет голосовое сообщение
         */

        SpechKit.generateVoice(text, "zahar", "good", "opus", "ru-RU");
        String srv = Api_vk.getMessagesUploadServer("audio_message",  peer_id);
        String doc = Api_vk.loadAudioMessage(srv,"voice.ogg");
        String nameDoc = Api_vk.docSave(doc);
        Api_vk.send("..", peer_id, 0, 0, nameDoc);

    }
}
