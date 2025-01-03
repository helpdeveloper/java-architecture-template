package br.com.helpdev.sample.core.ports.input;

import java.util.UUID;

public interface UserEnricherPort {

   void enrichUser(final UUID userUuid);

}
