# BotVK
На данном этапе бот может обрабатывать несколько запросов за раз. Но не без оговорок. Необходимо понять, как бот отреагирует на сразу несколько сообщений. Пока это не ясно. По идее в событиях прилетит два сообщения. Надо научиться метод для парсинга событий такое обрабатывать.
Ну и использовать длинный запрос по другому.

В проекте применяются следующие сторонние решения:
# json in java: библиотека для обработки json ответов.
    http://mvnrepository.com/artifact/org.json/json/20180130
# MultipartUtility.java: класс для отправки сообщений. Немножко отредаченный под свои нужды.
    http://www.codejava.net/java-se/networking/upload-files-by-sending-multipart-request-programmatically
# Речевые технологии SpeechKit: облачное решение от Яндекса для синтеза речи.
    https://webasr.yandex.net/ttsdemo.html - форма для тестов;
    https://tech.yandex.ru/speechkit/cloud/doc/guide/common/speechkit-common-tts-http-request-docpage/ - документация.
  
  
# Список изменений, которые необходимо внести:
•	Изменить методы для парсинга входящих событий. 

• Переделать и переименовать метод checkUnreadMessages ().

  На данный момент он нигде не используется.
  Сделать так, чтобы он возвращал json объект. Объект должен в себе хранить массив непрочитанных сообщений с полаями:
  
    текстовое тело сообщения, дату отправки, id сообщения, id юзера.

•	добавить поддержку пары командлетов.
  К примеру: 
  
    "Лукас, голос дароу" - В ответ должно прийти голосовое сообщение с текстом "дароу"
        
    "Лукас, перешли голосовое id дароу" - в результате должно быть отправленно голосовое
     сообщение человеку с указанным id c текстом дароу.
        
 Данные командлеты можно реализовать с помощью клавиатуры с быстрыми командами.

•	Логгирование.

    Хорошо что на данном этапе есть хоть какие-то логи.
    Но желательно внедрять какие-то системы логгирования. Особенно в такого рода приложениях.

