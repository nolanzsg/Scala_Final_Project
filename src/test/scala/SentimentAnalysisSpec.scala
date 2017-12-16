//package SentimentAnalysis



import org.scalatest.{FunSpec, Matchers}


class SentimentAnalysisSpec extends FunSpec with Matchers{



  describe("sentiment analysis") {

    it("should return 3.4375 when input is 'great phone'") {

      val input = "great iphone"

      val sentiment = helloGroup.calculateScore(input)

      sentiment should be (3.4375)

    }

    it("should return 3.125 when input is 'not great phone'") {

      val input = "great iphone"

      val sentiment = helloGroup.calculateScore(input)

      sentiment should be (3.125)

    }

    it("should return 1.875 when input is 'not bad phone'") {

      val input = "not bad phone"

      val sentiment = helloGroup.calculateScore(input)

      sentiment should be(1.875)

    }


  }



}
