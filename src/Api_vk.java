
import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Api_vk {

    /*
    accessToken - вечный токен с доступом к личным сообщениям,
    личной информации и к списку друзей
    */
    private static String
            accessToken = new Settings().getVK_MESSAGES_TOKEN();

    // accessTokenForDocs даёт доступ к методам Docs
    private static String
            accessTokenForDocs = new Settings().getVK_DOCS_TOKEN();

    private static String key;
    private static String server;
    private static Integer ts;

    public static String getKey() {
        return key;
    }

    public static String getServer() {
        return server;
    }

    public static Integer getTs() {
        return ts;
    }

    public static void setKey(String key) {
        Api_vk.key = key;
    }

    public static void setServer(String server) {
        Api_vk.server = server;
    }

    public static void setTs(Integer ts) {
        Api_vk.ts = ts;
    }


    //пространство в для запросов
    //true - место занято, false - место свободно
    private static final boolean[] spaceOfRequests  = new boolean[Constants.maxRequestCount];

    //Устанавливаем флаг "справедливый", в таком случае метод
    //aсquire() будет раздавать разрешения в порядке очереди
    private static final Semaphore SEMAPHORE = new Semaphore(Constants.maxRequestCount, true);

    private static String sendHttp (String url, String type) throws IOException, InterruptedException {

        //Метод отправляет http запрос
        //String url;
        //@type = GET or POST

        System.out.println("[sendHttp] Start request processing");

        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod(type);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("[sendHttp] Request processing completed");

        return response.toString();

    }


    public static String send(String message, String id, Integer peerState, Integer id_message, String attachment, String forward_messages) throws IOException, InterruptedException {

        /*
        МГНОВЕННО МОЖНО ОТПРАВИТЬ ТОЛЬКО 4 СООБЩЕНИЯ ОДНОМУ ПОЛЬЗОВАТЕЛЮ ИЛИ В ЧАТ

        Без URLEncoder.encode строка не верно складывается
        следовательно запрос не отрабатывает
        Если peerState = 0, то сообщение отправляется юзеру;
             peerState = 1, То сообщение оправляется в групповой чат;
             peetState = 2, то сообщение отправляется сообществу.

        id_messsage - параметр необходим для ответа на сообщение юзера

        Если type = 0, то отправляем обычное сооб
        */

        Integer peer_id = null;

        switch (peerState) {
            case 0 : peer_id = new Integer(id); break;
            case 1 : peer_id = 2000000000 + new Integer(id); break;
            case 2 : peer_id = new Integer(id); break;
        }


        String url = "https://api.vk.com/method/messages.send?" +
                "peer_id=" +  URLEncoder.encode(peer_id.toString(), "UTF8") +
                "&message=" + URLEncoder.encode(message, "UTF8") +
                "&attachment=" + URLEncoder.encode(attachment, "UTF8") +
                "&forward_messages=" + URLEncoder.encode(forward_messages, "UTF8") +
                "&v=5.52&access_token=" + accessToken;

        String response = sendHttp(url, "GET");

        String answer =
                "------------------------------------------------" + "\n" +
                "Json Ответ от API-VK: " + response + '\n' +
                "Id получателя: " + "id" + id + '\n' +
                "Текст сообщения: " + "\n" + message + "\n" +
                "------------------------------------------------";

        System.out.println("[send] " + "Отправка сообщения: " + message);
        return answer;
    }

    public static String send(String message, Integer id) throws IOException, InterruptedException {

        /*
        МГНОВЕННО МОЖНО ОТПРАВИТЬ ТОЛЬКО 4 СООБЩЕНИЯ ОДНОМУ ПОЛЬЗОВАТЕЛЮ ИЛИ В ЧАТ

        Без URLEncoder.encode строка не верно складывается
        следовательно запрос не отрабатывает
        Если peerState = 0, то сообщение отправляется юзеру;
             peerState = 1, То сообщение оправляется в групповой чат;
             peetState = 2, то сообщение отправляется сообществу.

        id_messsage - параметр необходим для ответа на сообщение юзера

        Если type = 0, то отправляем обычное сооб
        */

        Integer peer_id = id;


        String url = "https://api.vk.com/method/messages.send?" +
                "peer_id=" +  URLEncoder.encode(peer_id.toString(), "UTF8") +
                "&message=" + URLEncoder.encode(message, "UTF8") +
                "&v=5.52&access_token=" + accessToken;

        String response = sendHttp(url, "GET");

        String answer =
                "------------------------------------------------" + "\n" +
                        "Json Ответ от API-VK: " + response + '\n' +
                        "Id получателя: " + "id" + id + '\n' +
                        "Текст сообщения: " + "\n" + message + "\n" +
                        "------------------------------------------------";

        System.out.println("[send] " + "Отправка сообщения: " + message);
        return answer;
    }

    public  String getListFiends (String Id) throws IOException, InterruptedException {

        /*
        Метод должен возвращать список друзей в формате arraylist
        !!!На данном этапе метод возвращает ответ на запрос в не обработанном формате!!!
        */

        /*
        Без URLEncoder.encode строка не верно складывается
        следовательно запрос не отрабатывает
        */

        String url = "https://api.vk.com/method/friends.get?" +
                "user_id=" + URLEncoder.encode(Id, "UTF8") +
                "&v=5.52&access_token=" + accessToken;

        String response = sendHttp(url, "GET");


        System.out.println("[getListFiends]" + "\n" + "Не обработанный ответ на запрос friends.get: " + "\n" +
        "Полученный URL: " + url + "\n" +
        response);

        return response;

    }

    public static String getHistoryDialog (Integer id, Integer count, Integer rev) throws IOException, InterruptedException {

        /*
        Метод возвращает историю сообщений с выбранным пользователем
        Id - id пользователя, с ним мы хотим получить историю сообщений
        count - количество сообщений не беольше 200
        rev - на вход принимает 1 или 0.
        1 – возвращать сообщения в хронологическом порядке.
        0 – возвращать сообщения в обратном хронологическом порядке (по умолчанию). - Работаю всегда с этим параметром
         */

        String url = "https://api.vk.com/method/messages.getHistory?" +
//              "user_id=" + URLEncoder.encode(id.toString(), "UTF8") +
                "&peer_id=" + URLEncoder.encode(id.toString(), "UTF8") +
                "&count=" + URLEncoder.encode(count.toString(), "UTF8") +
                "&rev=" + URLEncoder.encode(rev.toString(), "UTF8") +
                "&v=5.52&access_token=" + accessToken;

        String response = sendHttp(url, "GET");

        System.out.println("[getHistoryDialog]" + "\n" + "Полученный URL: " + url + "\n" + response.toString());

        return response;
    }

    public static String searchString (String str, String peer_id, String date,
                                       Integer offset, Integer count , Boolean unRead) throws IOException, InterruptedException {
        /*
        Метод ищет строку (str) в входящих сообщениях
        Работает по только параметр str(q), для всех остальных надо делать проверку
        Входящая строка для проверки "тест"
        unRead = true, то возвращает только массив из не прочитанных сообщений

        response.items.read_state: 1 - Если сообщение прочитано
         */
        String url = "https://api.vk.com/method/messages.search?" +
                "q=" + URLEncoder.encode(str, "UTF8") +
                "&peer_id=" + URLEncoder.encode(peer_id, "UTF8") +
                "&date=" + URLEncoder.encode(date.toString(), "UTF8") +
                "&offset=" + URLEncoder.encode(offset.toString(), "UTF8") +
                "&count=" + URLEncoder.encode(count.toString(), "UTF8") +
                "&v=5.52&access_token=" + accessToken;

        String response = sendHttp(url, "GET");

        System.out.println("[searchString]" +" Подстрока: " + str + "\n" + "Не обработанный ответ на запрос messages.search: " + "\n" +
                "Полученный URL: " + url + "\n" +
                response);

        return response;
    }

    public static void getLongPollServer (Integer ip_version) throws IOException, InterruptedException {

        /**
        messages.getLongPollServer
        Возвращает key, server, ts
        Данные необходимы для работы следующего запросa "getEvents" - User Long Poll API
        #
         https://{$server}?act=a_check&key={$key}&ts={$ts}&wait=25&mode=2&version=2

        !!! Обрабатывает ошибку "\"error_code\":5" внутри метода
         используется рекурсия
         */

        String url = "https://api.vk.com/method/messages.getLongPollServer?" +
                "unread=" + URLEncoder.encode(ip_version.toString(), "UTF8") +
                "&v=5.52&access_token=" + accessToken;

        String response = sendHttp(url, "GET");

        JSONObject jsonObject = new JSONObject(response);


        /**Если токен для отправки сообщений не верный
        */
        if (response.contains("\"error_code\":5")) {
            System.out.println("[getLongPollServer]" +  "\n" + "Invalid access_token (4)" + "\n" +
                    "Полученный URL: " + url + "\n" +
                    response);
                getLongPollServer(3);
        }


        setKey(jsonObject.getJSONObject("response").get("key").toString());
        setServer(jsonObject.getJSONObject("response").get("server").toString());
        setTs(jsonObject.getJSONObject("response").getInt("ts"));

        System.out.println("[getLongPollServer]" +  "\n" + "Не обработанный ответ на запрос messages.getLongPollServer: " + "\n" +
                "Полученный URL: " + url + "\n" +
                response.toString());

    }

    public static String getEvents (String key, String server, Integer ts, Integer wait)
            throws IOException, InterruptedException {

        /*
        Возможно входные параметры стоит сменить на следующие параметры:
        String key, String server, String ts, String wait
        ---------------------------------------------------
        Метод возвращат список событий, который необходимо обработать
        https://{$server}?act=a_check&key={$key}&ts={$ts}&wait=25&mode=2&version=2


        */
        URL obj    =    null;
        String url =    "https://" + server + "?act=a_check" +
                        "&key=" + URLEncoder.encode(key, "UTF8") +
                        "&ts=" + URLEncoder.encode(ts.toString(), "UTF8") +
                        "&wait=" + URLEncoder.encode(wait.toString(), "UTF8") +
                        "&version=" + URLEncoder.encode("3", "UTF8") +
                        "&mode=" + URLEncoder.encode("2", "UTF8");

        System.out.println("[getEvents] started......" + " wait " + wait.toString()  + " sec" );

        String response = sendHttp(url, "GET");

        System.out.println("[getEvents]" +  "\n" + "Не обработанный ответ на запрос: " + "\n" +
                "Полученный URL: " + url + "\n" +
                "response = " + response.toString());

        JSONObject jsonObject = new JSONObject(response.toString());
            if (!(response.toString().contains("{\"failed\":2}"))) {
                setTs(jsonObject.getInt("ts"));
            }

        return  response;
    }


    public static String getDialogs(Integer count, Integer unread) throws IOException, InterruptedException {

        /*
        Параметр count определяет сколько сообщений минимум или максимум мы вернём
        Если count = 0, то запрос вернет количество не прочитанных сообщений. Массив items при этом будет пустым.
        Если  больше нуля, то вернёт самый актуальный диалог (который стоит на первом месте в сообщениях)
        Если unread = 1, вернуть только те диалоги, в которых есть непрочитанные сообщения.

        Метод объявлен устаревшим в версии 5.80
        https://vk.com/dev/messages.getDialogs
         */

        String url = "https://api.vk.com/method/messages.getDialogs?" +
                "unread=" + URLEncoder.encode(unread.toString(), "UTF8") +
                "&count=" + URLEncoder.encode(count.toString(), "UTF8") +
                "&v=5.52&access_token=" + accessToken;

        String response = sendHttp(url, "GET");

        return response;
    }

    public static String checkUnreadChat(Integer offset, Integer count, String filter, Integer extended, String fields) throws IOException, InterruptedException {

        //Метод возвращает список бесед пользователя
        //ссылка на документацию:
        //https://vk.com/dev/messages.getConversations?params[offset]=0&params[count]=20&params[filter]=unread&params[extended]=1&params[fields]=profiles&params[v]=5.80

        String url = null;
        try {
            url = "https://api.vk.com/method/messages.getConversations?" +
                    "count=" + URLEncoder.encode(count.toString(), "UTF8") +
                    "&filter=" + URLEncoder.encode(filter, "UTF8") +
                    "&extended=" + URLEncoder.encode(extended.toString(), "UTF8") +
                    "&fields=" + URLEncoder.encode(fields, "UTF8") +
                    "&v=5.52&access_token=" + accessToken;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String response = sendHttp(url, "GET");

        System.out.println("[checkUnreadChat]" + "\nURL: " + url +
        "\n" + "response: " + response);

        return response;
    }


    public static String getMessagesUploadServer (String type, String peer_id) throws IOException, InterruptedException {

        /*
        Метод получает сервер для загрузки документа типа doc или audio_message
        для отправки пользователю (peer_id)
         */

        String url = null;
        try {
            url = "https://api.vk.com/method/docs.getMessagesUploadServer?" +
                    "type=" + URLEncoder.encode(type, "UTF8") +
                    "&peer_id=" + URLEncoder.encode(peer_id, "UTF8") +
                    "&v=5.52&access_token=" + accessTokenForDocs;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String response = sendHttp(url, "GET");

        System.out.println("[getMessagesUploadServer] " + "\n" + "Ответ сервера : " + response.toString());

        JSONObject jsonObject = new JSONObject(response);

        return jsonObject.getJSONObject("response").get("upload_url").toString();
    }

    public static String loadAudioMessage (String server, String pathToFile) throws IOException {

        /*

        Метод загружает аудиофайл на полученный сервер
        метод связан с методом getMessagesUploadServer() и классом MultipartUtility

        */

        StringBuilder response_sb = new StringBuilder();
        try {
            MultipartUtility multipart = new MultipartUtility(server, "UTF-8");

            multipart.addFilePart("file", new File(pathToFile));

            List<String> response = multipart.finish();

            for (String line : response) {
                response_sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject(response_sb.toString());

        return jsonObject.get("file").toString();
    }

    public static String docSave (String file) throws IOException, InterruptedException {

        Integer id;
        Integer owner_id;


        String url = "https://api.vk.com/method/docs.save?" +
                "file=" + file +
                "&access_token=" + accessTokenForDocs +
                "&v=5.84";

        String response = sendHttp(url, "GET");

        System.out.println("[docSave] response: " + response );

        JSONObject rootJson = new JSONObject(response);
        JSONObject jsonObj  = rootJson.getJSONArray("response").getJSONObject(0);

        id = jsonObj.getInt("id");
        owner_id = jsonObj.getInt("owner_id");

        return "doc" + owner_id.toString() + "_" + id.toString();
    }

}
