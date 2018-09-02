

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.IntStream;

public class Controller extends Thread {


    @Override
    public void run() {
           try {
                Controller.checkUnreadMessages();
                for (;;) {

                    String answer = Api_vk.getEvents( Api_vk.getKey(), Api_vk.getServer(), Api_vk.getTs(), 60);

                    if (answer.contains("{\"failed\":2}")) {
                        System.out.println("[Controller] Connection failed. Error: <failed : 2>");
                        Api_vk.getLongPollServer( 3 );
                        answer =  new Api_vk().getEvents( Api_vk.getKey(), Api_vk.getServer(), Api_vk.getTs(),60);
                    }

                    JSONArray messagesList = new JSONObject(getInfoInputMessages(answer)).getJSONArray("items");

                    System.out.println("[Controller] size of messagesList: " + messagesList.length());

                    if (messagesList.length() != 0) {

                        for (int i = 0; i < messagesList.length(); i++) {
                            Integer iFinal = i;
                            Thread subThreadBot = new Thread(() -> {

                                System.out.println("[Controller] new message: " + messagesList.getJSONArray(iFinal).get(5));
                                try {
                                    BotLogic.sendVoiceMessage(messagesList.getJSONArray(iFinal).getString(5),    //
                                            messagesList.getJSONArray(iFinal).getInt(3)); //id чата
                                  // Api_vk.send(messagesList.getJSONArray(iFinal).getString(5), messagesList.getJSONArray(iFinal).getInt(3));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                // BotLogic.main(messagesList.getJSONArray(iFinal).getString(5), messagesList.getJSONArray(iFinal).toString());
                            });

                            subThreadBot.start();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }



    private static boolean checkUnreadMessages () throws IOException, InterruptedException {

        /*
        Метод проверяет количество непрочитанных сообщений
        Если они есть, то на каждое сообщение отвечает и возвращает истину
        Если непрочитанных сообщений нет, то возвращает false

        ВЫЗЫВАЕТ replyToUnread()
         */

        Integer countUnreadChat;

        String response =  Api_vk.checkUnreadChat(0,20, "unread", 1, "profiles");

        JSONObject jsonObject = new JSONObject(response).getJSONObject("response");

        countUnreadChat = jsonObject.getInt("count");

        System.out.println("[checkUnreadMessages] countUnreadChat: " + countUnreadChat);

        if (0 == countUnreadChat) {

            return false;

        } else {

            for (int i = 0; i < countUnreadChat; i++) {
               System.out.println("[checkUnreadMessages] "
                       + jsonObject.getJSONArray("items").getJSONObject(i).getJSONObject("conversation").getJSONObject("peer").getInt("id"));
               System.out.println("count unread messages " + jsonObject.getJSONArray("items").getJSONObject(i).getJSONObject("conversation").getInt("unread_count"));

               replyToUnread(jsonObject.getJSONArray("items").getJSONObject(i).getJSONObject("conversation").getJSONObject("peer").getInt("id"),
                        jsonObject.getJSONArray("items").getJSONObject(i).getJSONObject("conversation").getInt("unread_count"),
                       null, 1000);
            }

            return true;
        }
    }


    private static void replyToUnread(Integer user_id, Integer countUnread, String jsonItems, Integer delayInMilisec) throws IOException, InterruptedException {

        /*
        Отвечает пользователю на каждое непрочитанное сообщение

        Метод надо перенести в Botlogic
         */

        Integer countAnswer = 0;

        Settings settings = new Settings();

        String jsonObj = Api_vk.getHistoryDialog(user_id, countUnread, 0);

        JSONObject jsonObject = new JSONObject(jsonObj);

        JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("items");

        System.out.println("[replyToUnread] countUnread = " + countUnread + "\nJSON: " + jsonObj);

        for (int i = countUnread - 1; i >= 0; i--) {
                        System.out.println("items[" + i + "]: " + jsonArray.getJSONObject(i).getString("body"));
            if ((countAnswer%3 == 0) && (countAnswer > 0) )  {
                        System.out.println("Items["+ i + "] Initialization sleep on 1 sec \n");
                        System.out.println("items[" + i + "]: " + jsonArray.getJSONObject(i).getString("body"));

                try {
                    Thread.sleep(delayInMilisec);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            //создаем файл с рандомным именем
            String fileName = UUID.randomUUID().toString();
            SpechKit.generateVoice(jsonArray.getJSONObject(i).getString("body"),
                    settings.getSpeechTypeSpeaker(), settings.getSpeechMotion(), settings.getSpeechFormatAudio(), settings.getSpeechLang(), fileName);
            String srv = Api_vk.getMessagesUploadServer("audio_message",  user_id.toString());
            String doc = Api_vk.loadAudioMessage(srv, fileName);
            String nameDoc = Api_vk.docSave(doc);
            Api_vk.send("", user_id.toString(), 0, 0, nameDoc,
                    "");

            System.out.println("[sendVoiceMessage] " + " text of message: " +
                          jsonArray.getJSONObject(i).getString("body") +
                          "\n file of audio name: " + fileName + "\n" +
                          "text of voice message: " + jsonArray.getJSONObject(i).getString("body") );
            //удаляем файл
            BotLogic.deleteFile(fileName);

            countAnswer ++;
        }

    }


    private static String getInfoInputMessages(String answer) {
        /*

        !!ПЕРЕНЕСТИ МЕТОД В КОНТРОЛЛЕР, ПОТОМУ ЧТО ПРОСТО РАБОТАЕТ С ДАННЫМИ ПОЛУЧЕННЫМИ БЛАГОДАРЯ МЕТОДАМ ИЗ API_VK

        МЕТОД СОЗДАН ЧТОБЫ ОБРАБАТЫВАТЬ ИНФОРМАЦИЮ ИЗ МЕТОДА Api_vk.getEvents()

        Метод-парсер обрабатывает результат метода getEvents()
        answer - строка с ответом метода getEvents()
        mod - модификатор, отвечает за данные, которые следует искать в входной строке

        ВОЗВРАЩАЕТ СПИСОК СОБЫТИЙ С КОДОМ ВХОДЯЩИХ СООБЩЕНИЙ,
                            PARAMETER[0] = 4
                            PARAMETER[2] = CONSTANTS.incomingMessagesID[]


        Код нового сообщения 4 - eventsArray.get(0).equals(4)
        Код входящего сообщения - Constants.incomingMessagesID.equals(eventsArray.get(2)

        incomingMessagesID - Массив идентификаторов для новый входящих сообщений.

         */
        ArrayList<Object> items = new ArrayList<>();

        JSONObject listOfMessages = new JSONObject();

        JSONObject jsonObject = new JSONObject(answer);

        JSONArray jsonArray = jsonObject.getJSONArray("updates");

        System.out.println("[getInfoInputMessages] jsonArray (" + jsonArray.length() + ") : " + jsonArray );

        for (int i = 0; i < jsonArray.length(); i++) {

            System.out.println("[getInfoInputMessages] jsonArray (" + i + ") : " + jsonArray.get(i));

            JSONArray eventsArray = (JSONArray) jsonArray.get(i);
            System.out.println("[getInfoInputMessages] eventsArray: " +eventsArray.get(0));

            if ((eventsArray.getInt(0) == 4) && (IntStream.of(Constants.incomingMessagesID).anyMatch(x -> x == eventsArray.getInt(2)))) {
                System.out.println("[getInfoInputMessages] Adding an array to items: " + jsonArray.get(i));
                items.add((jsonArray.get(i)));
            }

        }
        listOfMessages.put("items", items);
        System.out.println("[getInfoInputMessages] items: " + items);
        System.out.println("[getInfoInputMessages] listOfMessages: " + listOfMessages);

        return listOfMessages.toString();
    }

}




