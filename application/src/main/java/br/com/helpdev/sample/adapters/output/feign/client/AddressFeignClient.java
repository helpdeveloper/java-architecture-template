package br.com.helpdev.sample.adapters.output.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "random-data-api", path = "/api/v2/addresses")
public interface AddressFeignClient {

   @GetMapping(produces = "application/json")
   @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2.0))
   AddressDto generate();
}
