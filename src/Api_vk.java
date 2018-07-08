
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    public static String send(String message, String id, Integer peerState, Integer id_message, String attachment) throws IOException {

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
                "&v=5.52&access_token=" + accessToken;

        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String answer =
                "------------------------------------------------" + "\n" +
                "Json Ответ от API-VK: " + response.toString() + '\n' +
                "Id получателя: " + "id" + id + '\n' +
                "Текст сообщения: " + "\n" + message + "\n" +
                "------------------------------------------------";

        System.out.println("[send] " + "Отправка сообщения: " + message);
        return answer;
    }

    public  String getListFiends (String Id) throws IOException {

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


        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("[getListFiends]" + "\n" + "Не обработанный ответ на запрос friends.get: " + "\n" +
        "Полученный URL: " + url + "\n" +
        response.toString());

        return response.toString();

    }

    public static ArrayList parseJsonListFriends(String jsonAnswer)  {

        /*
        Метод парсит Json ответ на зпрос friends.get
        метод getListFiend(На вход принимает id пользователя)
        В будущем надо сделать универсалньый парсер для большинства json ответов
        или интегрировать мини парсер в каждый гет запрос..
         */

        String response = jsonAnswer;
        List<String> items = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONObject("response")
                .getJSONArray("items");
        for (int i = 0; i < jsonArray.length(); i++) {
            items.add(jsonArray.get(i).toString());
        }
        return (ArrayList) items;
    }

    public static String getHistoryDialog (String id, Integer count, Integer rev) throws IOException {

        /*
        Метод возвращает историю сообщений с выбранным пользователем
        Id - id пользователя, с ним мы хотим получить историю сообщений
        count - количество сообщений не беольше 200
        rev - на вход принимает 1 или 0.
        1 – возвращать сообщения в хронологическом порядке.
        0 – возвращать сообщения в обратном хронологическом порядке (по умолчанию). - Работаю всегда с этим параметром
         */

        String url = "https://api.vk.com/method/messages.getHistory?" +
                "user_id=" + URLEncoder.encode(id, "UTF8") +
                "&peer_id=" + URLEncoder.encode(id, "UTF8") +
                "&count=" + URLEncoder.encode(count.toString(), "UTF8") +
                "&rev=" + URLEncoder.encode(rev.toString(), "UTF8") +
                "&v=5.52&access_token=" + accessToken;

        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();


        System.out.println("[getHistoryDialog]" + "\n" + "Полученный URL: " + url + "\n" + response.toString());
        return response.toString();
    }

    public static String searchString (String str, String peer_id, String date,
                                       Integer offset, Integer count , Boolean unRead) throws IOException {
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

        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("[searchString]" +" Подстрока: " + str + "\n" + "Не обработанный ответ на запрос messages.search: " + "\n" +
                "Полученный URL: " + url + "\n" +
                response.toString());

        return response.toString();
    }

    public static int getCountUnreadMessages(Integer count, Integer unread) throws IOException {

        /*
        Параметр count определяет сколько сообщений минимум или максимум мы вернём
        Если count = 0, то запрос вернет количество не прочитанных сообщений. Массив items при этом будет пустым.
        Если  больше нуля, то вернёт самый актуальный диалог (который стоит на первом месте в сообщениях)
         */

        String url = "https://api.vk.com/method/messages.getDialogs?" +
                "unread=" + URLEncoder.encode(unread.toString(), "UTF8") +
                "&count=" + URLEncoder.encode(count.toString(), "UTF8") +
                "&v=5.52&access_token=" + accessToken;

        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //Обработка Json ответа, сохранение результату в объект "dCount"
        //Получаем количество не прочитанных сообщений
        JSONObject jsonObject = new JSONObject( response.toString() );
        Integer dCont = (Integer) jsonObject.getJSONObject("response")
                .get( "count" );

        System.out.println("[getCountUnreadMessages]" + " unread dialogs: " + dCont.toString() +  "\n" + "Не обработанный ответ на запрос messages.getDialogs: " + "\n" +
                "Полученный URL: " + url + "\n" +
                response.toString());

        return dCont;
    }


    public static void getLongPollServer (Integer ip_version) throws IOException {

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

        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject jsonObject = new JSONObject(response.toString());


        /**Если токен для отправки сообщений не верный
        */
        if (response.toString().contains("\"error_code\":5")) {
            System.out.println("[getLongPollServer]" +  "\n" + "Invalid access_token (4)" + "\n" +
                    "Полученный URL: " + url + "\n" +
                    response.toString());
            try {
                Thread.sleep(60000);
                getLongPollServer(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


        setKey(jsonObject.getJSONObject("response").get("key").toString());
        setServer(jsonObject.getJSONObject("response").get("server").toString());
        setTs(jsonObject.getJSONObject("response").getInt("ts"));

        System.out.println("[getLongPollServer]" +  "\n" + "Не обработанный ответ на запрос messages.getLongPollServer: " + "\n" +
                "Полученный URL: " + url + "\n" +
                response.toString());

    }

    public static String getEvents (String key, String server, Integer ts, Integer wait)
            throws IOException {

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


        obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("[getEvents]" +  "\n" + "Не обработанный ответ на запрос: " + "\n" +
                "Полученный URL: " + url + "\n" +
                "response = " + response.toString());

        JSONObject jsonObject = new JSONObject(response.toString());
            if (!(response.toString().contains("{\"failed\":2}"))) {
                setTs(jsonObject.getInt("ts"));
            }

        return  response.toString();
    }


    public static ArrayList parseEvents (String answer) {
        /*

        !!ПЕРЕНЕСТИ ВСЕ МЕТОДЫ ДЛЯ ПАРСИНГА В ОТДЕЛЬНЫЕ КЛАССЫ!!

        Метод-парсер обрабатывает результат метода getEvents()
        answer - строка с ответом метода getEvents()
        mod - модификатор, отвечает за данные, которые следует искать в входной строке

        Код нового сообщения 4
                               ,49 - входящее сообщение
                               ,17 - входящее для подписоты и всех остальных (подписчик и рандомная страница)
                               ,1 - Входящее для левого челика
        ???                    ,532497 - входящее с телефона
        ???                    ,972 - входящее с телефона, создатель конференции
                               ,35 - исходящее сообщение

         */
        String response = answer;
        String bodyMessage = null;

        try {
            response = response.substring( response.indexOf( "[4" ), response.lastIndexOf("]"));
            response = response.toString().replace( "[", "" )
                                            .replace( "]", "" );
            bodyMessage = response.substring( response.indexOf( "\"" ) + 1, response.lastIndexOf( "\"," ) );

            response = response.replace( "\"" + bodyMessage + "\",", "" );

            System.out.println( "Обработанный ответ на метод getEvents(): " + response + "\n" +
            "bodyMessage= " + bodyMessage);



            List<String> items = new ArrayList(Arrays.asList(response.split(",")));
            items.add(bodyMessage);

            //!!тест!!
            /*for(int i=0; i<items.size(); i++) {
                System.out.println(" -->"+items.get(i));
            }
            */

            System.out.println("[parseEvents]: Массив параметров: " + items);
            return (ArrayList) items;


        } catch (StringIndexOutOfBoundsException ex) {
            System.out.println( "Метод getEvents() не вернул строку удовлетворяющею условиям." );
            return null;
        }
    }

    public static void checkUnreadMessages () throws IOException {

        // время
        String timeToSearch;
        //Количество непрочитанных сообщений
        Integer unreadMessages = 0;
        //Json ответ message.getSearch (Поиск выполняем по подстроке "тест")
        String responseSearch;
        //Количество найденных сообщений методом searchString()
        Integer countSearch;

        /*
        id_user | body_message | date
        Три списка, решил не выдумывать и не создавать безсмысленных костылей.
        Массивы используются для хранения информации из метода searchString()
        В массивы сохраняются данные из не прочитанных сообщений
        */
        ArrayList<Integer> id_user = new ArrayList<Integer>();
        ArrayList<Integer> id_message = new ArrayList<Integer>();
        ArrayList<String> body_message = new ArrayList<String>();
        ArrayList<Integer> date = new ArrayList<Integer>();

        try {
            unreadMessages =  new Api_vk().getCountUnreadMessages(0,1 );
        } catch (IOException e) {
            e.printStackTrace();
        }

        responseSearch = new Api_vk().searchString("тест", "", "",0, 0, true);

        JSONObject jsonObject = new JSONObject( responseSearch );

        countSearch = jsonObject.getJSONObject( "response" ).getInt( "count" );

        //JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("items" );

        responseSearch = new Api_vk().searchString("тест", "", "",0, countSearch, true);

        jsonObject = new JSONObject( responseSearch );
        JSONArray jsonArray = jsonObject.getJSONObject( "response" ).getJSONArray( "items" );

        Integer countArrayItems = 0;
        for (int i = 0; i != countSearch; i++ ) {
            //  id_user | id_message | body_message | date
            if ((jsonArray.getJSONObject(i).getInt( "read_state" ) == 0) &&
                    (jsonArray.getJSONObject(i).getInt( "out" ) == 0)) {
                countArrayItems ++;
                id_user.add( jsonArray.getJSONObject( i).getInt( "user_id" ) );
                id_message.add( jsonArray.getJSONObject( i ).getInt( "id" ) );
                body_message.add( jsonArray.getJSONObject( i ).getString( "body" ));
                date.add( jsonArray.getJSONObject( i ).getInt( "date" ) );
            }
        }

        System.out.println( "Количество не прочитанных сообщений, с найденной подстрокой 'тест' : " + countArrayItems );



    }




    public static void parseMessages(String jsonAnswer) {

        /*

        !!ПЕРЕНЕСТИ ВСЕ МЕТОДЫ ДЛЯ ПАРСИНГА В ОТДЕЛЬНЫЕ КЛАССЫ!!

        Тестовый метод для обработки Json ответа от метода getHistoryDialog
        На данный момент парсит список 5 послених полученных сообщений !!!!
        Небходим исключительно для работы с методом getHistoryDialog!!!
         */

        String response = jsonAnswer;
        List<String> items = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONObject("response")
                .getJSONArray("items");
        for (int i = 0; i < jsonArray.length(); i++) {
            items.add(jsonArray.get(i ).toString());
            System.out.println( jsonArray.getJSONObject(i).get( "read_state" ));
        }
    }

    public static String getMessagesUploadServer (String type, String peer_id) throws IOException {

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

            System.out.println("[getMessagesUploadServer] " + "\n" + "url : " + url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("[getMessagesUploadServer] " + "\n" + "Ответ сервера : " + response.toString());

        JSONObject jsonObject = new JSONObject(response.toString());

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

    public static String docSave (String file) throws IOException {

        Integer id;
        Integer owner_id;


        String url = "https://api.vk.com/method/docs.save?" +
                "file=" + file +
                "&access_token=" + accessTokenForDocs +
                "&v=5.63";


        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject rootJson = new JSONObject(response.toString());
        JSONObject jsonObj  = rootJson.getJSONArray("response").getJSONObject(0);

        id = jsonObj.getInt("id");
        owner_id = jsonObj.getInt("owner_id");

        return "doc" + owner_id.toString() + "_" + id.toString();
    }

}
