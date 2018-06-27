

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

public class Main {

    public static void main(String[] args) throws IOException {

    //test


     // иначе плывёт Кодировка в терминале на windows и Linux
        System.setProperty("file.encoding","UTF-8");
        Field charset = null;
        try {
            charset = Charset.class.getDeclaredField("defaultCharset");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        charset.setAccessible(true);
        try {
            charset.set(null,null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        LongPollServer();
        new Thread().run();
    }

    public static void LongPollServer () throws IOException {

        new Api_vk().getLongPollServer( 3 );
        String response =  new Api_vk().getEvents( Api_vk.getKey(), Api_vk.getServer(), Api_vk.getTs(),5);
        Api_vk.parseEvents(response);
    }
}
