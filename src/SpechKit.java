import java.io.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class SpechKit {

    /**
     * SpeechKit Cloud
     * https://webasr.yandex.net/ttsdemo.html
     * https://tech.yandex.ru/speechkit/cloud/doc/guide/common/speechkit-common-tts-http-request-docpage/
     *
     **/

    private static  String accessToken = new Settings().getYANDEX_SPEACH_KIT();




    public static void generateVoice (String text, String speaker, String emotion,  String format, String lang, String fileName) throws IOException {

        /*
        Результатом выполнения метода должен быть аудиофайл
         */

        String url = "https://tts.voicetech.yandex.net/generate?" +
                "text=" + URLEncoder.encode(text, "UTF8") +
                "&format=" + URLEncoder.encode(format, "UTF8") +
                "&speaker=" + URLEncoder.encode(speaker, "UTF8") +
                "&emotion=" + URLEncoder.encode(emotion, "UTF8") +
                "&lang=" + URLEncoder.encode(lang, "UTF8") +
                "&key=" + accessToken;

        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();


        try {
            //параметры соединения
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(60000);
            connection.setRequestMethod("GET");

            ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
            FileOutputStream fos = new FileOutputStream(fileName);

            long filePosition = 0;
            long transferedBytes = fos.getChannel().transferFrom(rbc, filePosition, Long.MAX_VALUE);

            while(transferedBytes == Long.MAX_VALUE){
                filePosition += transferedBytes;
                transferedBytes = fos.getChannel().transferFrom(rbc, filePosition, Long.MAX_VALUE);
            }

            rbc.close();
            fos.close();

        } catch (ProtocolException e) {

            InputStreamReader inStream = new InputStreamReader(connection.getErrorStream());
            char[] cbuf = new char[1];
            String result= "";
            BufferedReader in = new BufferedReader(inStream);
            while (in.read(cbuf) != -1) {
                result += String.valueOf(cbuf);
            }
            System.out.println("result:" + result);
       }
    }
}
