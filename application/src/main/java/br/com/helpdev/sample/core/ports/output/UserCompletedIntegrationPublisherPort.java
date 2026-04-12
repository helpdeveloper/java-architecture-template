package br.com.helpdev.sample.core.ports.output;

import br.com.helpdev.sample.core.domain.entities.User;

public interface UserCompletedIntegrationPublisherPort {

   void sendUserCompletedPayload(User user);

}
