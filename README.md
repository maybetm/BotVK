BotVK

  На данном этапе бот может обрабатывать несколько запросов за раз. Но не без оговорок. Необходимо понять, как бот отреагирует на     сразу несколько сообщений. Пока это не ясно. По идее в событиях прилетит два сообщения. Надо научиться метод для парсинга событий   такое обрабатывать. Ну и использовать длинный запрос по другому.

В проекте применяются следующие сторонние решения:

  json in java: библиотека для обработки json ответов.

    http://mvnrepository.com/artifact/org.json/json/20180130

  MultipartUtility.java: класс для отправки сообщений. Немножко отредаченный под свои нужды.

    http://www.codejava.net/java-se/networking/upload-files-by-sending-multipart-request-programmatically

  Речевые технологии SpeechKit: облачное решение от Яндекса для синтеза речи.

    https://webasr.yandex.net/ttsdemo.html - форма для тестов;
    https://tech.yandex.ru/speechkit/cloud/doc/guide/common/speechkit-common-tts-http-request-docpage/ - документация.

