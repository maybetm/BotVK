# BotVK
На данный момент бот работает в одном потоке, следовательно, бот не может обработать сразу несколько сообщений. Так как после получения уведомления выполнение запроса не возобновляется и никак не обрабатывается.

В проекте применяются следующие сторонние решения:
# json in java: библиотека для обработки json ответов.
  http://mvnrepository.com/artifact/org.json/json/20180130
# MultipartUtility.java: класс для отправки сообщений. Немножко отредаченный под свои нужды.
  http://www.codejava.net/java-se/networking/upload-files-by-sending-multipart-request-programmatically
# Речевые технологии SpeechKit: облачное решение от Яндекса для синтеза речи.
  https://webasr.yandex.net/ttsdemo.html - форма для тестов;
  https://tech.yandex.ru/speechkit/cloud/doc/guide/common/speechkit-common-tts-http-request-docpage/ - документация.
