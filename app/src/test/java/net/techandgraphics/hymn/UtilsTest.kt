package net.techandgraphics.hymn

import org.junit.Assert
import org.junit.Test

class UtilsTest {

  @Test
  fun `pass currentTimeMillis returns true`() {
    val data = Utils.currentMillsDiff(System.currentTimeMillis())
    Assert.assertTrue(data)
  }

  @Test
  fun getThreshold() {
    for (i in 1..384) {
      println(Utils.getThreshold(384, 56, i))
    }
  }
}
