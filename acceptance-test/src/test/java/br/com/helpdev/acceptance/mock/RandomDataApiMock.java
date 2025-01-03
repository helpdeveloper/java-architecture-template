package br.com.helpdev.acceptance.mock;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

import com.github.tomakehurst.wiremock.WireMockServer;

public class RandomDataApiMock {

   public static void mockSuccessRandomAddress(final WireMockServer mockServer) {
      mockServer.stubFor(get(urlPathMatching("/api/v2/addresses")).willReturn(
            aResponse().withBodyFile("sample-address.json").withStatus(200).withHeader("Content-Type", "application/json")));
   }

   public static void mockFailureRandomAddress(final WireMockServer mockServer) {
      mockServer.stubFor(get(urlPathMatching("/api/v2/addresses")).willReturn(aResponse().withStatus(500)));
   }

}