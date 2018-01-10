This demonstrates a bug with Splunk Java SDK where the `index.attach()` method of sending
events -- as recommended [in the docs](http://dev.splunk.com/view/java-sdk/SP-CAAAEJ2) for
sending many events -- ignores the server response, so the method call will succeed even if
the ingest fails.

To reproduce, enter credentials for a user _without_ the `tcp_edit_stream` capability in
[SplunkForwardingSpec](src/test/scala/bugreports/splunk/SplunkForwardingSpec.scala), and run
```
sbt test-only bugreports.splunk.SplunkForwardingSpec
```
The test succeeds (suggesting that the events were ingested successfully), but the events
are not (and should not be) ingested, because the user does not have permissions to perform
that operation.
