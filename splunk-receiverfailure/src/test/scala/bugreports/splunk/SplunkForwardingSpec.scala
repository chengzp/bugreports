package bugreports.splunk

import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date

import com.splunk.{HttpService, SSLSecurityProtocol, Service, ServiceArgs}
import org.scalatest._

class SplunkForwardingSpec extends FlatSpec with Matchers {

  private val username: String = ""
  private val password: String = ""
  private val hostUrl: String = ""
  private val port: Int = 8089
  private val indexName: String = "dtapi"

  // This is basically the example in Splunk docs: http://dev.splunk.com/view/java-sdk/SP-CAAAEJ2

  it should "fail on failures" /* but does not */ in {
    val service = createService()
    val index = service.getIndexes.get(indexName)
    require(index != null, s"Index $indexName was not found on Splunk")

    val sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    val date = sdf.format(new Date())

    val socket = index.attach()
    try {
      val outStream = socket.getOutputStream
      val outWriter = new OutputStreamWriter(outStream, StandardCharsets.UTF_8)
      outWriter.write(date + "Event one!\r\n")
      outWriter.write(date + "Event two!\r\n")
      outWriter.flush()
      println("Sent two event successfully.")

      // This does not work either; the socket is either already closed, or we get read timeouts,
      // depending on whether we close `outWriter`.
      // val resultsIn = socket.getInputStream
      // outWriter.close()
      // println(scala.io.Source.fromInputStream(resultsIn).mkString)
    } finally {
      socket.close()
    }
  }

  private def createService(): Service = {
    HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2)
    val loginArgs = new ServiceArgs()
    loginArgs.setUsername(username)
    loginArgs.setPassword(password)
    loginArgs.setHost(hostUrl)
    loginArgs.setPort(port)
    Service.connect(loginArgs)
  }
}
