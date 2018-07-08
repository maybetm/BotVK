

import java.io.IOException;
import java.util.ArrayList;

public class Thread_Bot extends java.lang.Thread {
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
                                    System.out.println("test " + eventsList.get(eventsList.size() - 1).toString());
                                    BotLogic.sendVoiceMessage(eventsList.get(eventsList.size() - 1).toString(), eventsList.get(3).toString());
                                }
                } else {
                    System.out.println("Сообщения нет. Не веришь, - смотри сам: " + eventsList);
                }
                try {
                    sleep( 0 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

    }
}
}



