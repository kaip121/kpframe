package com.kplike.library.common.prefser;

import java.lang.reflect.Type;

public interface JsonConverter {
  <T> T fromJson(String json, Type typeOfT);

  <T> String toJson(T object, Type typeOfT);
}