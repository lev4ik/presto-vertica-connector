# Presto Vertica Plugin

This is a plugin for Presto that allow you to use Vertica Jdbc Connection

## Connection Configuration

Create new properties file inside etc/catalog dir:

    connector.name=vertica
    connection-url=jdbc:vertica://{host::localhost}:[{port::5433}][/{database}]
    connection-user=myuser
    connection-password=

Create a dir inside plugin dir called vertica. To make it easier you could copy mysql dir into vertica and remove mysql driver and prestodb-mysql jars. Finally put the presto-vertica in plugin/vertics folder. Here is the sptes:

    cd $PRESTODB_HOME
    cp -r plugin/mysql plugin/vertica
    rm plugin/vertica/mysql-connector*
    rm plugin/vertica/presto-mysql*
    mv /home/Downloads/presto-vertica*.jar plugin/vertica

## Building Presto Oracle JDBC Plugin

    mvn clean install
    
## Vertica Driver
Vertica driver is not present in Maven. It can be downloaded from https://my.vertica.com/download/vertica/client-drivers/
