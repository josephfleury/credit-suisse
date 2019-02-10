# credit-suisse
Credit Suisse Code example

# Running The Application
This is a spring boot application.

To run the application execute the following in the root directory 
- mvn spring-boot:run

To pass the event file to the application you must modify the application.properties file

- event.log.filepath=/logs/event.log

or

- mvn spring-boot:run -Devent.log.filepath=/logs/event.log

# Note
In an ideal situation the best way to tackle this in a highly scalable way is to use logstash and elasticsearch. 
The logs would get loaded into elasticsearch and an application would periodically poll elasticsearch for any new data.
This would reduce the work load of reading from a large log file into the jvm and could allow queries to be run against
the elasticsearch engine.

# Outstanding
For this to be a viable application reading from a log file we would need a watchdog to process the changes to the log 
as they happen. This would allow the application to handle a smaller number of events as they are created.


