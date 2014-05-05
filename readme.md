## Где я?
Это модуль, интегрирующий UIMA.Ext ( https://github.com/CLLKazan/UIMA-Ext/ ) c поисковым сервером (предположительно Solr) и key-value базой (пока не определенной).

## Как запускать?
Понадобится apache tomcat 7 (http://tomcat.apache.org/download-70.cgi).
Параметры VM: -Duima.datapath=<way to dict.opcorpora.ser without "<>"> -Xmx2048m -Dfile.encoding=UTF-8;

## Что за dict.opcorpora.ser?
Это сериализованный словарь. Забудь, зачем.
Для его получения  нужно выкачать xml по адресу http://opencorpora.org/files/export/dict/dict.opcorpora.xml.bz2.
Затем, необходимо разархивировать и запустить парсер ru.ksu.niimm.cll.uima.morph.opencorpora.resource.XmlDictionaryParser,
где в качестве первого аргумента необходимо указать путь к xml, а второго - путь выходного файла (dict.opcorpora.ser).