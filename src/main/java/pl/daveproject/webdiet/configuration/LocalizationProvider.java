package pl.daveproject.webdiet.configuration;

import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.internal.JsonSerializer;
import elemental.json.JsonFactory;
import elemental.json.JsonValue;
import elemental.json.impl.JreJsonFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Slf4j
@Component
public class LocalizationProvider implements I18NProvider {
    public static final String BUNDLE_PREFIX = "translate";

    public final Locale LOCALE_PL = Locale.forLanguageTag("pl");

    @Override
    public List<Locale> getProvidedLocales() {
        return List.of(LOCALE_PL);
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (key == null) {
            log.warn("Got lang request for key with null value!");
            return StringUtils.EMPTY;
        }

        final var bundle = ResourceBundle.getBundle(BUNDLE_PREFIX, locale);

        try {
            var value = bundle.getString(key);
            if (params.length > 0) {
                value = MessageFormat.format(value, params);
            }
            return value;
        } catch (final MissingResourceException e) {
            log.warn("Missing resource", e);
            return "!%s: %s".formatted(locale.getLanguage(), key);
        }
    }

    public static <V> V getI18n(String stringJson, Class<V> type) {
        return jsonToClass(getI18nJsonContent(stringJson, type), type);
    }

    public static <V> JsonValue getI18nJsonContent(String stringJson, Class<V> type) {
        final JsonFactory JSON_FACTORY = new JreJsonFactory();
        JsonValue jsonI18n = JSON_FACTORY.parse(stringJson);
        return jsonI18n;
    }

    public static <V> V jsonToClass(JsonValue jsonValue, Class<V> type) {
        return JsonSerializer.toObject(type, jsonValue);
    }
}
