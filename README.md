# log-events-reader

1. In /hsqldb/data run
java -cp ../lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:logdb --dbname.0 logdb
2. gradle build
3. java -jar build/libs/log-events-reader-0.0.1-SNAPSHOT.jar "logfile.txt"


