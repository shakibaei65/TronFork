package org.tron.common.application;

import org.tron.common.parameter.CommonParameter;

public interface Service {

  void init();

  void init(CommonParameter parameter);

  void start();

  void stop();
}
