## Где я?
Это модуль, интегрирующий UIMA.Ext ( https://github.com/CLLKazan/UIMA-Ext/ ) c поисковым сервером (предположительно Solr) и key-value базой (пока не определенной).

## Как запускать?
Помимо стандартного maven понадобится apache tomcat 7 (http://tomcat.apache.org/download-70.cgi).
Параметры VM: -Xmx2048m -Dfile.encoding=UTF-8;
Так же понадобится настроенная MongoDB(http://mongodb.org/).

Ещё, понадобится специфическим образом настроенный Solr (http://apache-mirror.rbc.ru/pub/apache/lucene/solr/4.7.2/).

Настройки:

1) Внутрь schema.xml добавить после всех <copyField> следующее: "<copyField source="span" dest="text"/>".

В раздел fields добавить поля:


   <field name="span" type="text_general" indexed="true" stored="true" required="false" />
   <field name="documentPositions" type="string" multiValued="true" indexed="false" stored="true" required="false" />
   
2) Внутри solrconfig.xml в конец (перед строками "<admin><defaultQuery>*:*</defaultQuery></admin>") 
добавить updateRequestProcessorChain следующего вида:

<updateRequestProcessorChain name="simpleChain">
  <processor class="solr.UUIDUpdateProcessorFactory">
      <str name="fieldName">id</str>
    </processor>
    <processor class="solr.LogUpdateProcessorFactory" />
    <processor class="solr.RunUpdateProcessorFactory" />
  </updateRequestProcessorChain>
  
   Найти requestHandler с именем update (строка, начинающаяся с "<requestHandler name="/update"..."), заменить его на
  
  <requestHandler name="/update" class="solr.UpdateRequestHandler">
         <lst name="defaults">
           <str name="update.chain">simpleChain</str>
         </lst>
    </requestHandler>

## Настройка properties из src/main/resources.

1) database.properties - параметры MongoDB: host, port, username, password и dbname (имя базы).
Предварительно также нужно сконфигурировать пользователя внутри MongoDB.

2) uima.properties - нужно указать путь до dict.opcorpora.ser.

3) solr.properties - нужно указать URL до solr.

## Что за dict.opcorpora.ser?
Это сериализованный словарь. Забудь, зачем.
Для его получения нужно выкачать xml-словарь:

1) Скачать архив по адресу http://opencorpora.org/files/export/dict/dict.opcorpora.xml.bz2

2) Распаковать, получив dict.opcorpora.xml

Затем в свободной папке:

1) git clone https://github.com/CLLKazan/UIMA-Ext.git

2) cd UIMA-Ext/UIMA.Ext.Morph.OpenCorpora

3) mvn install

4) mvn exec:exec -Dexec.executable="java" -Dexec.classpathScope="test"
 -Dexec.args="-Xmx1500m -cp %classpath ru.ksu.niimm.cll.uima.morph.opencorpora.resource.XmlDictionaryParser 
 /path/to/dict.opcorpora.xml /serialized/dictionary/output/path"
 
В последней строке первый путь - это путь непосредственно к словарю dict.opcorpora.xml,
второй - путь до формирующегося dict.opcorpora.ser (включая сам файл).

## Тесты! Где же тесты?
Для них достаточно сделать mvn clean test