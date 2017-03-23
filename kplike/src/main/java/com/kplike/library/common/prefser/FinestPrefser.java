package com.kplike.library.common.prefser;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

public class FinestPrefser {
    private static final String KEY_IS_NULL = "key == null";
    private static final String CLASS_OF_T_IS_NULL = "classOfT == null";
    private static final String TYPE_TOKEN_OF_T_IS_NULL = "typeTokenOfT == null";
    private static final String VALUE_IS_NULL = "value == null";

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;
    private final Map<Class<?>, Accessor<?>> accessors = new HashMap<>();
    private JsonConverter jsonConverter;

    private interface Accessor<T> {
        T get(String key, T defaultValue);

        void put(String key, T value);
    }

    /**
     * 创建FinestPrefser对象
     *
     * @param context Android Context
     */
    public FinestPrefser(@NonNull Context context) {
        this(context, new GsonConverter());
    }

    /**
     * 创建FinestPrefser对象
     *
     * @param context       Android Context
     * @param jsonConverter Json Converter
     */
    public FinestPrefser(@NonNull Context context, @NonNull JsonConverter jsonConverter) {
        this(context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE), jsonConverter);
    }

    /**
     * 创建FinestPrefser对象
     *
     * @param sharedPreferences instance of SharedPreferences
     */
    public FinestPrefser(@NonNull SharedPreferences sharedPreferences) {
        this(sharedPreferences, new GsonConverter());
    }

    /**
     * 创建FinestPrefser对象提供SharedPreferences的对象
     *
     * @param sharedPreferences instance of SharedPreferences
     * @param jsonConverter     Json Converter
     */
    public FinestPrefser(@NonNull SharedPreferences sharedPreferences,
                         @NonNull JsonConverter jsonConverter) {
        checkNotNull(sharedPreferences, "sharedPreferences == null");
        checkNotNull(jsonConverter, "jsonConverter == null");
        this.preferences = sharedPreferences;
        this.editor = preferences.edit();
        this.jsonConverter = jsonConverter;
        initAccessors();
    }

    /**
     * Returns SharedPreferences in case, we want to manipulate them without FinestPrefser.
     *
     * @return SharedPreferences instance of SharedPreferences
     */
    public SharedPreferences getPreferences() {
        return preferences;
    }

    /**
     * Checks if preferences contains value with a given key
     *
     * @param key provided key
     * @return true if preferences contains key and false if not
     */
    public boolean contains(String key) {
        return preferences.contains(key);
    }

    /**
     * Gets value from SharedPreferences with a given key and type
     * as a RxJava Observable, which can be subscribed.
     * If value is not found, we can return defaultValue.
     * Emit preference as first element of the stream even if preferences wasn't changed.
     *
     * @param key          key of the preference
     * @param classOfT     class of T (e.g. String.class)
     * @param defaultValue default value of the preference (e.g. "" or "undefined")
     * @param <T>          return type of the preference (e.g. String)
     * @return Observable value from SharedPreferences associated with given key or default value
     */
    public <T> Observable<T> getAndObserve(String key, Class<T> classOfT, T defaultValue) {
        return getAndObserve(key, TypeToken.fromClass(classOfT), defaultValue);
    }

    /**
     * Gets value from SharedPreferences with a given key and type token
     * as a RxJava Observable, which can be subscribed
     * If value is not found, we can return defaultValue.
     * Emit preference as first element of the stream even if preferences wasn't changed.
     *
     * @param key          key of the preference
     * @param typeTokenOfT type token of T (e.g. {@code new TypeToken<List<String>> {})
     * @param defaultValue default value of the preference (e.g. "" or "undefined")
     * @param <T>          return type of the preference (e.g. String)
     * @return Observable value from SharedPreferences associated with given key or default value
     */
    public <T> Observable<T> getAndObserve(final String key, final TypeToken<T> typeTokenOfT,
                                           final T defaultValue) {
        return observe(key, typeTokenOfT, defaultValue) // start observing
                .mergeWith(Observable.defer(new Func0<Observable<T>>() { // then start getting
                    @Override
                    public Observable<T> call() {
                        return Observable.just(get(key, typeTokenOfT, defaultValue));
                    }
                }));
    }

    /**
     * Gets value from SharedPreferences with a given key and type
     * as a RxJava Observable, which can be subscribed.
     * If value is not found, we can return defaultValue.
     *
     * @param key          key of the preference
     * @param classOfT     class of T (e.g. String.class)
     * @param defaultValue default value of the preference (e.g. "" or "undefined")
     * @param <T>          return type of the preference (e.g. String)
     * @return Observable value from SharedPreferences associated with given key or default value
     */
    public <T> Observable<T> observe(@NonNull String key, @NonNull Class<T> classOfT,
                                     T defaultValue) {
        checkNotNull(key, KEY_IS_NULL);
        checkNotNull(classOfT, CLASS_OF_T_IS_NULL);

        return observe(key, TypeToken.fromClass(classOfT), defaultValue);
    }

    /**
     * Gets value from SharedPreferences with a given key and type token
     * as a RxJava Observable, which can be subscribed.
     * If value is not found, we can return defaultValue.
     *
     * @param key          key of the preference
     * @param typeTokenOfT type token of T (e.g. {@code new TypeToken<List<String>> {})
     * @param defaultValue default value of the preference (e.g. "" or "undefined")
     * @param <T>          return type of the preference (e.g. String)
     * @return Observable value from SharedPreferences associated with given key or default value
     */
    public <T> Observable<T> observe(@NonNull final String key,
                                     @NonNull final TypeToken<T> typeTokenOfT, final T defaultValue) {
        checkNotNull(key, KEY_IS_NULL);
        checkNotNull(typeTokenOfT, TYPE_TOKEN_OF_T_IS_NULL);

        return observePreferences().filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String filteredKey) {
                return key.equals(filteredKey);
            }
        }).map(new Func1<String, T>() {
            @Override
            public T call(String s) {
                return get(key, typeTokenOfT, defaultValue);
            }
        });
    }

    /**
     * Gets value from SharedPreferences with a given key and type.
     * If value is not found, we can return defaultValue.
     *
     * @param key          key of the preference
     * @param classOfT     class of T (e.g. {@code String.class})
     * @param defaultValue default value of the preference (e.g. "" or "undefined")
     * @param <T>          return type of the preference (e.g. String)
     * @return value from SharedPreferences associated with given key or default value
     */
    public <T> T get(@NonNull String key, @NonNull Class<T> classOfT, T defaultValue) {
        checkNotNull(key, KEY_IS_NULL);
        checkNotNull(classOfT, CLASS_OF_T_IS_NULL);

        if (!contains(key) && defaultValue == null) {
            return null;
        }

        return get(key, TypeToken.fromClass(classOfT), defaultValue);
    }

    /**
     * Gets value from SharedPreferences with a given key and type.
     * If value is not found, we can return defaultValue.
     *
     * @param key          key of the preference
     * @param typeTokenOfT type token of T (e.g. {@code new TypeToken<List<String>> {})
     * @param defaultValue default value of the preference (e.g. "" or "undefined")
     * @param <T>          return type of the preference (e.g. String)
     * @return value from SharedPreferences associated with given key or default value
     */
    public <T> T get(@NonNull String key, @NonNull TypeToken<T> typeTokenOfT, T defaultValue) {
        checkNotNull(key, KEY_IS_NULL);
        checkNotNull(typeTokenOfT, TYPE_TOKEN_OF_T_IS_NULL);

        Type typeOfT = typeTokenOfT.getType();

        for (Map.Entry<Class<?>, Accessor<?>> entry : accessors.entrySet()) {
            if (typeOfT.equals(entry.getKey())) {
                @SuppressWarnings("unchecked") Accessor<T> accessor = (Accessor<T>) entry.getValue();
                return accessor.get(key, defaultValue);
            }
        }

        if (contains(key)) {
            return jsonConverter.fromJson(preferences.getString(key, null), typeOfT);
        } else {
            return defaultValue;
        }
    }

    /**
     * returns RxJava Observable from SharedPreferences used inside FinestPrefser object.
     * You can subscribe this Observable and every time,
     * when SharedPreferences will change, subscriber will be notified
     * about that (e.g. in call() method) and you will be able to read
     * key of the value, which has been changed.
     *
     * @return Observable with String containing key of the value in default SharedPreferences
     */
    public Observable<String> observePreferences() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            // NOTE: Without this OnChangeListener will be GCed.
            Collection<OnChangeListener> listenerReferences =
                    Collections.synchronizedList(new ArrayList<OnChangeListener>());

            @Override
            public void call(final Subscriber<? super String> subscriber) {
                final OnChangeListener onChangeListener = new OnChangeListener(subscriber);
                preferences.registerOnSharedPreferenceChangeListener(onChangeListener);
                listenerReferences.add(onChangeListener);
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        preferences.unregisterOnSharedPreferenceChangeListener(onChangeListener);
                        listenerReferences.remove(onChangeListener);
                    }
                }));
            }
        });
    }

    private static class OnChangeListener
            implements SharedPreferences.OnSharedPreferenceChangeListener {
        private final Subscriber<? super String> subscriber;

        public OnChangeListener(Subscriber<? super String> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onNext(key);
            }
        }
    }

    /**
     * Puts value to the SharedPreferences.
     *
     * @param key   key under which value will be stored
     * @param value value to be stored
     */
    public <T> void put(@NonNull String key, @NonNull T value) {
        checkNotNull(value, VALUE_IS_NULL);
        put(key, value, TypeToken.fromValue(value));
    }

    /**
     * Puts value to the SharedPreferences.
     *
     * @param key          key under which value will be stored
     * @param value        value to be stored
     * @param typeTokenOfT type token of T (e.g. {@code new TypeToken<> {})
     */
    public <T> void put(@NonNull String key, @NonNull T value, @NonNull TypeToken<T> typeTokenOfT) {
        checkNotNull(key, KEY_IS_NULL);
        checkNotNull(value, VALUE_IS_NULL);
        checkNotNull(typeTokenOfT, TYPE_TOKEN_OF_T_IS_NULL);

        if (!accessors.containsKey(value.getClass())) {
            String jsonValue = jsonConverter.toJson(value, typeTokenOfT.getType());
            editor.putString(key, String.valueOf(jsonValue)).apply();
            return;
        }

        Class<?> classOfValue = value.getClass();

        for (Map.Entry<Class<?>, Accessor<?>> entry : accessors.entrySet()) {
            if (classOfValue.equals(entry.getKey())) {
                @SuppressWarnings("unchecked") Accessor<T> accessor = (Accessor<T>) entry.getValue();
                accessor.put(key, value);
            }
        }
    }

    /**
     * Removes value defined by a given key.
     *
     * @param key key of the preference to be removed
     */
    public void remove(@NonNull String key) {
        checkNotNull(key, KEY_IS_NULL);
        if (!contains(key)) {
            return;
        }

        editor.remove(key).apply();
    }

    /**
     * Clears all SharedPreferences.
     */
    public void clear() {
        if (size() == 0) {
            return;
        }

        editor.clear().apply();
    }

    /**
     * Returns number of all items stored in SharedPreferences.
     *
     * @return number of all stored items
     */
    public int size() {
        return preferences.getAll().size();
    }

    private void initAccessors() {
        accessors.put(Boolean.class, new Accessor<Boolean>() {
            @Override
            public Boolean get(String key, Boolean defaultValue) {
                return preferences.getBoolean(key, defaultValue);
            }

            @Override
            public void put(String key, Boolean value) {
                editor.putBoolean(key, value).apply();
            }
        });

        accessors.put(Float.class, new Accessor<Float>() {
            @Override
            public Float get(String key, Float defaultValue) {
                return preferences.getFloat(key, defaultValue);
            }

            @Override
            public void put(String key, Float value) {
                editor.putFloat(key, value).apply();
            }
        });

        accessors.put(Integer.class, new Accessor<Integer>() {
            @Override
            public Integer get(String key, Integer defaultValue) {
                return preferences.getInt(key, defaultValue);
            }

            @Override
            public void put(String key, Integer value) {
                editor.putInt(key, value).apply();
            }
        });

        accessors.put(Long.class, new Accessor<Long>() {
            @Override
            public Long get(String key, Long defaultValue) {
                return preferences.getLong(key, defaultValue);
            }

            @Override
            public void put(String key, Long value) {
                editor.putLong(key, value).apply();
            }
        });

        accessors.put(Double.class, new Accessor<Double>() {
            @Override
            public Double get(String key, Double defaultValue) {
                return Double.valueOf(preferences.getString(key, String.valueOf(defaultValue)));
            }

            @Override
            public void put(String key, Double value) {
                editor.putString(key, String.valueOf(value)).apply();
            }
        });

        accessors.put(String.class, new Accessor<String>() {
            @Override
            public String get(String key, String defaultValue) {
                return preferences.getString(key, String.valueOf(defaultValue));
            }

            @Override
            public void put(String key, String value) {
                editor.putString(key, String.valueOf(value)).apply();
            }
        });
    }

    private void checkNotNull(Object object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
    }
}