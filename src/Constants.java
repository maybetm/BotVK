import java.lang.reflect.Array;

public class Constants {

    /**
     *Event ID for incoming messages
     *                            ,49 - входящее сообщение
     *                                ,17 - входящее для подписоты и всех остальных (подписчик и рандомная страница)
     *                                ,1 - Входящее для левого челика
     *         ???                    ,532497 - входящее с телефона
     *         ???                    ,972 - входящее с телефона, создатель конференции
     *                                ,35 - исходящее сообщение
     */
    protected static int[] incomingMessagesID  = {49, 33, 1, 17, 532497, 972};


    /**
    Количество возможных одновременных запросов
     */
    protected static final Integer maxRequestCount = 3;

    /**
    Массив ошибок, после которых можно попробовать повторить запрос
     */

    protected static final String[] errorsList = {"\"failed\":2", "\"error_code\":6", "\"error_code\":5"};

}
