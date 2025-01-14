/*
 * Copyright 2021-2022 Signal Messenger, LLC
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.whispersystems.textsecuregcm.recaptcha;

import com.google.common.annotations.VisibleForTesting;
import java.util.Objects;
import javax.annotation.Nonnull;

public class TransitionalRecaptchaClient implements RecaptchaClient {

  @VisibleForTesting
  static final String V2_PREFIX = "signal-recaptcha-v2" + EnterpriseRecaptchaClient.SEPARATOR;

  private final LegacyRecaptchaClient legacyRecaptchaClient;
  private final EnterpriseRecaptchaClient enterpriseRecaptchaClient;

  public TransitionalRecaptchaClient(
      @Nonnull final LegacyRecaptchaClient legacyRecaptchaClient,
      @Nonnull final EnterpriseRecaptchaClient enterpriseRecaptchaClient) {
    this.legacyRecaptchaClient = Objects.requireNonNull(legacyRecaptchaClient);
    this.enterpriseRecaptchaClient = Objects.requireNonNull(enterpriseRecaptchaClient);
  }

  @Override
  public boolean verify(@Nonnull final String token, @Nonnull final String ip) {
    if (token.startsWith(V2_PREFIX)) {
      return enterpriseRecaptchaClient.verify(token.substring(V2_PREFIX.length()), ip);
    } else {
      return legacyRecaptchaClient.verify(token, ip);
    }
  }

}
