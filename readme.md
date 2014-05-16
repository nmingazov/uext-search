## Где я?
Это модуль, интегрирующий UIMA.Ext ( https://github.com/CLLKazan/UIMA-Ext/ ) c поисковым сервером (предположительно Solr) и key-value базой (пока не определенной).

## Как запускать?
Помимо стандартного maven понадобится apache tomcat 7 (http://tomcat.apache.org/download-70.cgi).
Параметры VM: -Xmx2048m -Dfile.encoding=UTF-8;
Так же понадобится настроенная MongoDB(http://mongodb.org/).

Затем, необходимо перенастроить под себя properties, находящиеся в src/main/resources.
1) database.properties - параметры MongoDB: host, port, username, password и dbname (имя базы).
Предварительно также нужно сконфигурировать пользователя внутри MongoDB.
2) uima.properties - нужно указать путь до dict.opcorpora.ser.


## Что за dict.opcorpora.ser?
Это сериализованный словарь. Забудь, зачем.
Для его получения нужно выкачать xml-словарь:
1) Скачать архив по адресу http://opencorpora.org/files/export/dict/dict.opcorpora.xml.bz2
2) Распаковать, получив dict.opcorpora.xml
Затем в свободной папке:
1) git clone https://github.com/CLLKazan/UIMA-Ext.git
2) cd UIMA-Ext/UIMA.Ext.Morph.OpenCorpora
2) mvn install
3) mvn exec:exec -Dexec.executable="java" -Dexec.classpathScope="test"
 -Dexec.args="-Xmx1500m -cp %classpath ru.ksu.niimm.cll.uima.morph.opencorpora.resource.XmlDictionaryParser 
 /path/to/dict.opcorpora.xml /serialized/dictionary/output/path"
 
В последней строке первый путь - это путь непосредственно к словарю dict.opcorpora.xml,
второй - путь до формирующегося dict.opcorpora.ser (включая сам файл).

## Тесты! Где же тесты?
Для них достаточно сделать mvn clean test