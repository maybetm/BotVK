
import java.io.*;
import java.util.UUID;

public class BotLogic {

    public static synchronized void setCountEvents(Integer countEvents) {
       BotLogic.countEvents = countEvents;
    }

    public static synchronized Integer getCountEvents() {
        return countEvents;
    }

    private static Integer countEvents = 0;

    public static String getAnswer (String message) throws NullPointerException {

        /*
        В конечном итоге, этот метод должен быть точкой входа в логику бота
        *******************************************************************
        Функция работает в тестовом режиме
        Возвращает два ответа:
        -"Тест пройден"
        -"Ключивых слов не было найденно"

        Метод ищет ключевые слова по типу:
        "Лукас, тест"
         */
        String mes = null;

        if (message.contains("Лукас,") && message.contains("тест")) {
                return "Тест успешно пройден.";
        } else {
                return "Ключевых слов в сообщении не найдено. Текст сообщения: " + message ;
        }

    }

    public static void sendVoiceMessage (String text, String peer_id) throws IOException {

        /*
        Отправляет голосовое сообщение
        Сперва метод generateVoice генерирует голосовое сообщение и сохраняет его в файл
        Потом череда методов загружает документ на сервер и отправляет сообщение с этим документом пользователю
         */
        BotLogic.setCountEvents( BotLogic.getCountEvents() + 1 );

        Settings settings = new Settings();

        //создаем файл с рандомным именем
        String fileName = UUID.randomUUID().toString();

        SpechKit.generateVoice(text, settings.getSpeechTypeSpeaker(), settings.getSpeechMotion(), settings.getSpeechFormatAudio(), settings.getSpeechLang(), fileName);
        String srv = Api_vk.getMessagesUploadServer("audio_message",  peer_id);
        String doc = Api_vk.loadAudioMessage(srv, fileName);
        String nameDoc = Api_vk.docSave(doc);
        Api_vk.send("..", peer_id, 0, 0, nameDoc);

        System.out.println("[sendVoiceMessage] " + " text of message: " + text +  "\n file of audio name: " + fileName + "\n" + "text of voice message: " + text );

        //удаляем файл
        deleteFile(fileName);

        BotLogic.setCountEvents( BotLogic.getCountEvents() - 1 );

    }

    public static void deleteFile (String fileName) {

        File file = new File(fileName);

        if (file.delete()) {
            System.out.println("[sendVoiceMessage] \"" + fileName + "\" File is deleted");

        } else {
            System.out.println("[sendVoiceMessage] \"" + fileName + "\"file is not find");
        }
    }
}
