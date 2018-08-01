

import java.io.IOException;

import java.util.ArrayList;

public class Thread_Bot extends java.lang.Thread {

    public static void setCountEvents(Integer countEvents) {
        Thread_Bot.countEvents = countEvents;
    }

    public static Integer getCountEvents() {
        return countEvents;
    }

    private static Integer countEvents = 0;

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
                            BotLogic.sendVoiceMessage(eventsList.get(eventsList.size() - 1).toString(), eventsList.get(3).toString());
                        } catch (IOException e) {
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

        return setDelay();
    }
}




