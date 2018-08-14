

import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;

public class Controller extends java.lang.Thread {



    public void run (){
        try {
            for (;;) {


                String answer = Api_vk.getEvents( Api_vk.getKey(), Api_vk.getServer(), Api_vk.getTs(), 60);

                if (answer.contains("{\"failed\":2}")) {
                    System.out.println("Connection failed. Error: <failed : 2>");
                    Api_vk.getLongPollServer( 3 );
                    answer =  new Api_vk().getEvents( Api_vk.getKey(), Api_vk.getServer(), Api_vk.getTs(),60);
                 }

                ArrayList eventsList = Api_vk.parseEvents(answer);

                if ((eventsList != null) ) {

                   if (eventsList.get(2).equals("49") || (eventsList.get(2).equals("532497")) || eventsList.get(2).equals("33") || eventsList.get(2).equals("1") || eventsList.get(2).equals("17")) {

                    Thread subThreadBot = new Thread(() -> {
                        System.out.println("[Thread_bot] bodyMessage = " + eventsList.get(eventsList.size() - 1).toString());
                        try {
                            sleep(setDelay());
                            System.out.println("[Thread_bot] delay = " + BotLogic.getCountEvents());
                            BotLogic.sendVoiceMessage(eventsList.get(eventsList.size() - 1).toString(), eventsList.get(3).toString());

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                       subThreadBot.start();
                   }

                } else {
                    System.out.println("Not new events, eventsList = " + eventsList);
                }
                

            }
        } catch (IOException e) {
            e.printStackTrace();

    }
}
    public static Integer setDelay () {

        //Возвращает время задержки
        //в зависимости от значения переменной countEvents
        //Это необходимо, чтобы распределить нагрузку, так как одновременно можно отправить только 3 сообщения
        //из-за ограничений SpeechKit и api вконтакте

        if (BotLogic.getCountEvents() < 3) {
            return 1000 * BotLogic.getCountEvents();
        }

        if (BotLogic.getCountEvents() >= 3) {
            return 2000 * BotLogic.getCountEvents();
        }

        return null;
    }

    public static boolean checkUnreadMessages () throws IOException {

        /*
        Метод проверяет количество непрочитанных сообщений
        Если они есть, то на каждое сообщение отвечает и возвращает истину
        Если непрочитанных сообщений нет, то возвращает false
         */
        Integer countUnreadChat;

        String response =  Api_vk.checkUnreadChat(0,20, "unread", 1, "profiles");

        JSONObject jsonObject = new JSONObject(response).getJSONObject("response");

        countUnreadChat = jsonObject.getInt("count");

        System.out.println("[checkUnreadMessages] countUnreadChat:" + countUnreadChat);

        if (0 == countUnreadChat) {

            return false;

        } else {

            for (int i = 0; i < countUnreadChat; i++) {
               System.out.println("\n" + jsonObject.getJSONArray("items").getJSONObject(i).getJSONObject("conversation").getJSONObject("peer").getInt("id"));
               System.out.println("\n" + jsonObject.getJSONArray("items").getJSONObject(i).getJSONObject("conversation").getInt("unread_count"));
            }

            return true;
        }
    }


    private static void send (Integer id, Integer countUnread, String jsonItems) {

        /*
        Отвечает пользователю на каждое непрочитанное сообщение
         */



    }

}




